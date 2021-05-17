package fr.valentinle.lecteur_musique.gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import fr.valentinle.lecteur_musique.config.Config;
import fr.valentinle.lecteur_musique.config.ConfigParams;
import fr.valentinle.lecteur_musique.model.Dashboard;
import fr.valentinle.lecteur_musique.model.Music;
import fr.valentinle.lecteur_musique.model.musicreader.MP3MusicReader;
import fr.valentinle.lecteur_musique.model.musicreader.MusicReader;
import fr.valentinle.lecteur_musique.model.observer.DashboardListener;

/**
 * Controller de la fenetre principale
 */
public class DashboardController implements Initializable, DashboardListener {

	// le lecteur de musique
	private MediaPlayer mediaPlayer;
	// le dashboard qui indiquera au lecteur quelle musique il doit jouer
	private Dashboard dashboard;
	// les configurations de l'application
	private Config config;
	// booleen si la musique est en train de jouer ou non
	private boolean isPlaying;
	// booleen pour indiquer que la pause vient du changement du seek de la musique
	private boolean isPauseChangeValue;
	// boolean pour savoir si le loop est active ou non sur la musique courrante
	private boolean looping = false;

	@FXML
	private Node root;

	@FXML
	private TextField searchinput;

	@FXML
	private Slider sliderTime;

	@FXML
	private ProgressBar progressTime;

	@FXML
	private GridPane zoneLists;

	@FXML
	private ListView<Music> searchList;

	@FXML
	private ListView<Music> priorityList;

	@FXML
	private ListView<Music> secondaryList;

	@FXML
	private Label durationPriority;

	@FXML
	private Label titleMusic;

	@FXML
	private Label authorMusic;

	@FXML
	private Label currentTimeLab;

	@FXML
	private Label durationTimeLab;

	@FXML
	private Slider sliderVolume;

	@FXML
	private ProgressBar progressVolume;

	@FXML
	private Button bplaypause;

	@FXML
	private Button bloop;

	@FXML
	private Button bmute;

	@FXML
	private Button bparameters;

	// liste filtrer de musique pour la recherche
	private FilteredList<Music> filteredList;

	@FXML
	private HBox contentLabelsPriority;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		config = new Config();

		dashboard = new Dashboard(FXCollections.observableList(new ArrayList<>()),
				FXCollections.observableList(new ArrayList<>()));
		// on ajoute le controller comme listener du model et qui ecoutera les
		// changements
		dashboard.addListener(this);
		isPlaying = false;
		isPauseChangeValue = false;

		// on recupere le dossier contenant les musiques indiquer dans les
		// configurations
		String musicFolder = config.getValueOf(ConfigParams.MUSIC_FOLDER_KEY);
		if (musicFolder != null) {
			// si y'a bien un dossier indiquer des les configurations on lit toutes
			// les musiques presentes
			readMusics(musicFolder);
		}

		// listener sur le changement de valeur du silder du volume qui changera
		// egalement le volume de la musique
		sliderVolume.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (mediaPlayer != null) {
				mediaPlayer.setVolume(sliderVolume.getValue() / 100);
			}
			// on change aussi la valeur de la progressBar qui permet de faire le
			// bandeau colore
			progressVolume.setProgress(sliderVolume.getValue() / sliderVolume.getMax());
		});

		// ajout du listener quand la valeur du slider representant le temps de
		// lecture de la musique change, on chegera la valeur de la progressBar
		// qui permet d'avoir un bandeau colore
		sliderTime.valueProperty().addListener((observable, oldDuration, newDuration) -> {
			progressTime.setProgress(sliderTime.getValue() / sliderTime.getMax());
		});

		// listes observables de la liste de recherches et des deux listes d'attentes
		ObservableList<Music> observableSortedMusics = FXCollections.observableList(dashboard.getSortedMusics());
		ObservableList<Music> observablePriority = (ObservableList<Music>) dashboard.getPriorityQueue();
		ObservableList<Music> observableSecondary = (ObservableList<Music>) dashboard.getSecondaryQueue();

		// on indique les elements personnalises aux listes, seul la liste de recherche
		// ne contient pas de drag and drop
		MusicCellFactory searchFactory = new MusicCellFactory(dashboard, observableSortedMusics, false);
		MusicCellFactory priorityFactory = new MusicCellFactory(dashboard, dashboard.getPriorityQueue(), true);
		MusicCellFactory secondaryFactory = new MusicCellFactory(dashboard, dashboard.getSecondaryQueue(), true);

		searchList.setCellFactory(searchFactory);
		priorityList.setCellFactory(priorityFactory);
		secondaryList.setCellFactory(secondaryFactory);

		// on ajout les elements des listes d'attentes
		priorityList.setItems(observablePriority);
		secondaryList.setItems(observableSecondary);

		// on gere les listener et contenu de la liste de recherche
		setListenerSearch(observableSortedMusics);

		// empecher que la barre de recherche soit focus des l'ouverture de
		// l'application
		disableDefaultFocusTextField();

		dashboard.shuffleSecondaryQueue();
		dashboard.nextMusic();

		// ajout des controls claviers sur les listes
		setKeyEventToListView(searchList);
		setKeyEventToListView(priorityList);
		setKeyEventToListView(secondaryList);

		// event pour effacer le contenu de la barre de recherche
		root.setOnKeyPressed((e) -> {
			if (e.getCode() == KeyCode.ESCAPE) {
				searchinput.setText("");
			}
		});
	}

	/**
	 * Ajoute un control clavier sur une liste
	 *
	 * @param listView la liste sur laquelle ajouter le control clavier
	 */
	private void setKeyEventToListView(ListView<Music> listView) {
		listView.setOnKeyPressed((e) -> {
			List<Music> startQueue;
			List<Music> endQueue;
			Music music;
			KeyCode moveCode;
			boolean canMoveMusic = true;

			// on detecte sur quelle liste on se trouve et donc quel control il
			// faut lui associer et de quelle liste vers quelle liste il faut deplacer
			// la musique ciblee
			if (priorityList.isFocused()) {
				startQueue = dashboard.getPriorityQueue();
				endQueue = dashboard.getSecondaryQueue();
				music = priorityList.getSelectionModel().getSelectedItem();
				moveCode = KeyCode.RIGHT;
			} else {
				startQueue = dashboard.getSecondaryQueue();
				endQueue = dashboard.getPriorityQueue();
				if (secondaryList.isFocused()) {
					music = secondaryList.getSelectionModel().getSelectedItem();
					moveCode = KeyCode.LEFT;
				} else {
					canMoveMusic = false;
					music = searchList.getSelectionModel().getSelectedItem();
					moveCode = KeyCode.RIGHT;
				}
			}

			// combinaison de touches pour deplacer vers le haut/bas et changer la
			// musique de liste
			KeyCombination keyCombSwitch = new KeyCodeCombination(moveCode, KeyCodeCombination.CONTROL_DOWN);
			KeyCombination keyCombMoveUp = new KeyCodeCombination(KeyCode.UP, KeyCodeCombination.CONTROL_DOWN);
			KeyCombination keyCombMoveDown = new KeyCodeCombination(KeyCode.DOWN, KeyCodeCombination.CONTROL_DOWN);

			Music musicSelected = listView.getSelectionModel().getSelectedItem();
			ObservableList<Music> items = listView.getItems();

			if (keyCombSwitch.match(e)) {
				dashboard.switchMusic(startQueue, endQueue, music);
			} else if (keyCombMoveUp.match(e)) {
				if (canMoveMusic) {
					// on se trouve sur une des deux liste d'attente donc on peut
					// monter la musique dans la liste
					dashboard.moveMusicUp(startQueue, music);
					listView.getSelectionModel().select(startQueue.indexOf(music));
				} else {
					// on est dans la liste de recherche et on peut pas monter la musique
					// on bloque l'event pour eviter le deplacement vers le haut
					// (donnee par defaut par ListView)
					e.consume();
				}
			} else if (keyCombMoveDown.match(e)) {
				if (canMoveMusic) {
					// on se trouve sur une des deux liste d'attente donc on peut
					// descendre la musique dans la liste
					dashboard.moveMusicDown(startQueue, music);
					listView.getSelectionModel().select(startQueue.indexOf(music));
				} else {
					// on est dans la liste de recherche et on peut pas descendre la musique
					// on bloque l'event pour eviter le deplacement vers le haut
					// (donnee par defaut par ListView)
					e.consume();
				}
			} else if (e.getCode() == KeyCode.UP) {
				if (musicSelected != null) {
					// on selectionne l'element d'au dessus de celui selectionne
					int indexSelected = items.indexOf(musicSelected);
					int position = indexSelected > 0 ? indexSelected - 1 : 0;
					listView.getSelectionModel().select(position);
				}
				if (musicSelected == null && !listView.getItems().isEmpty()) {
					// on selectionne le dernier element si aucun n'est selectionner
					listView.getSelectionModel().select(items.size() - 1);
				}
				e.consume();
			} else if (e.getCode() == KeyCode.DOWN) {
				if (musicSelected != null) {
					// on selectionne l'element d'en dessous de celui selectionne
					int indexSelected = items.indexOf(musicSelected);
					int position = indexSelected < items.size() ? indexSelected + 1 : items.size() - 1;
					listView.getSelectionModel().select(position);
				}
				if (musicSelected == null && !listView.getItems().isEmpty()) {
					// on selectionne le premier element si aucun n'est selectionner
					listView.getSelectionModel().select(0);
				}
				e.consume();
			} else if (e.getCode() == KeyCode.ESCAPE) {
				// on efface le contenu de la barre de recherche
				searchinput.setText("");
			}

		});
	}

	/**
	 * Ajout du listener sur la recherche de musique
	 *
	 * @param observableMusic la liste de recherche
	 */
	private void setListenerSearch(ObservableList<Music> observableMusic) {
		// on accepte toutes les musiques au depart car la recherche est vide
		filteredList = new FilteredList<>(observableMusic, s -> true);
		searchList.setItems(filteredList);

		// si on tape du texte dans la barre de recherche on va filtrer les musiques
		// qui seront presentes dans la liste de recherche en modifiant la liste
		// filtrer qui modifira la liste de recherche automatiquement car c'est
		// une observableList
		searchinput.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredList.setPredicate(music -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseSearch = searchinput.getText().toLowerCase();
				return music.getName().toLowerCase().contains(lowerCaseSearch)
						|| music.getAuthor().toLowerCase().contains(lowerCaseSearch);
			});
		});
	}

	/**
	 * Desactive le focus sur la barre de recherche lors du lancement de
	 * l'application
	 */
	private void disableDefaultFocusTextField() {
		final BooleanProperty firstTime = new SimpleBooleanProperty(true);

		searchinput.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue && firstTime.get()) {
				zoneLists.requestFocus(); // Delegate the focus to container
				firstTime.setValue(false); // Variable value changed for future references
			}
		});
	}

	/**
	 * Lit les musique d'un dossier et les ajoutes dans le dashboard
	 *
	 * @param musicFolder le dossier contenant les musiques a lire
	 */
	public void readMusics(String musicFolder) {
		MusicReader reader = new MP3MusicReader();
		try {
			dashboard.addAllMusic(reader.read(musicFolder));
		} catch (Exception e) {
			File folderMusic = new File(config.getValueOf(ConfigParams.MUSIC_FOLDER_KEY));
			if (!folderMusic.exists()) {
				config.removeConfig(ConfigParams.MUSIC_FOLDER_KEY);
				config.write();
			}
		}
	}

	/**
	 * Met en pause la lecture quand elle est en cours ou met play si elle est en
	 * pause
	 *
	 * @param event event de click du bouton playPause
	 */
	@FXML
	private void playPause(ActionEvent event) {
		switchPlayPause();
	}

	/**
	 * Change l'etat de lecture de la musique, met en pause quand elle est en play
	 * et inversement
	 */
	private void switchPlayPause() {
		if (isPlaying) {
			pause();
		} else {
			play();
		}
	}

	/**
	 * Met la lecture en play la lecture en changeant le logo du boutton playPause
	 */
	private void play() {
		play(true);
	}

	/**
	 * Met la lecture en play la lecture en changeant le logo du boutton playPause
	 * ou non
	 *
	 * @param turnButton true si on affiche le logo pause, false si on laisse le
	 *                   logo play sur le boutton (pour le seek quand on deplace le
	 *                   slider de la position)
	 */
	private void play(boolean turnButton) {
		if (mediaPlayer != null) {
			mediaPlayer.play();
			if (turnButton) {
				bplaypause.setStyle("-fx-background-image: url('/resources/images/pause.png')");
			}
			isPlaying = true;
		}
	}

	/**
	 * Met en pause la lecture en changeant le logo du boutton
	 */
	private void pause() {
		pause(true);
	}

	/**
	 * Met en pause la lecture en changeant le logo du boutton playPause ou non
	 *
	 * @param turnButton true si on affiche le logo play, false si on laisse le logo
	 *                   pause sur le boutton
	 */
	private void pause(boolean turnButton) {
		if (mediaPlayer != null) {
			mediaPlayer.pause();
		}
		if (turnButton) {
			bplaypause.setStyle("-fx-background-image: url('/resources/images/play.png')");
		}
		isPlaying = false;
	}

	/**
	 * Melange la liste secondaire
	 *
	 * @param e event de click du bouton shuffle
	 */
	@FXML
	private void shuffleSecondary(ActionEvent e) {
		dashboard.shuffleSecondaryQueue();
	}

	/**
	 * Active ou desactive le loop de la musique courrante et ajoute un affichage
	 * sur le bouton pour indiquer si il est locker ou non
	 */
	@FXML
	private void loop() {
		looping = !looping;
		lockButton(bloop);
	}

	/**
	 * Passage a la musique suivante
	 *
	 * @param e event de click du bouton next
	 */
	@FXML
	private void nextMusic(ActionEvent e) {
		dashboard.nextMusic();
	}

	/**
	 * Passage a la musique precedente
	 *
	 * @param e event de click du bouton precedent
	 */
	@FXML
	private void precedentMusic(ActionEvent e) {
		dashboard.precedentMusic();
	}

	/**
	 * Mute la lecture et change le logo ainsi que l'apparence de celui-ci pour
	 * indiquer qu'il est locker et mis en muer
	 *
	 * @param e event de click du bouton mute
	 */
	@FXML
	private void mute(ActionEvent e) {
		if (!mediaPlayer.isMute()) {
			mediaPlayer.setMute(true);
			bmute.setStyle("-fx-background-image: url('/resources/images/soundOff.png')");
		} else {
			mediaPlayer.setMute(false);
			bmute.setStyle("-fx-background-image: url('/resources/images/soundOn.png')");
		}
		lockButton(bmute);
	}

	/**
	 * Lance la fenetre de parametre
	 *
	 * @param e event de click du bouton parametre
	 */
	@FXML
	private void parameters(ActionEvent e) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/resources/fxml/Parameters.fxml"));
			Parent root = loader.load();
			ParametersController paramControl = loader.getController();
			// on ajoute le dashboard et les configuration au controller de la fenetre
			// de parametres
			paramControl.setDashboard(dashboard);
			paramControl.setConfig(config);
			Stage stage = new Stage();
			stage.setTitle("Paramètres");
			stage.setResizable(false);
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/resources/css/parameters.css").toString());
			stage.getIcons().add(new Image("/resources/images/logo.png"));
			stage.setScene(scene);
			stage.initOwner((Stage) zoneLists.getScene().getWindow());
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.centerOnScreen();
			stage.show();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Affiche le bouton comme locker/delocker en y ajoutant/retirant une classe CSS
	 *
	 * @param button le bouton a locker/delocker visuelement
	 */
	private void lockButton(Button button) {
		String classLocked = "locked";
		ObservableList<String> classButton = button.getStyleClass();
		if (!classButton.contains(classLocked)) {
			// le bouton ne contient pas la classe lock donc n'est pas locker,
			// dans ce cas on le lock
			classButton.add(classLocked);
		} else {
			classButton.remove(classLocked);
		}
	}

	/**
	 * Test si le click sur le slider de position se trouve bien dans la zone du
	 * slider (probeme avec le click qui est detecte mais la boule du slider ne se
	 * deplace pas)
	 *
	 * @param slider le slider sur lequel tester le click
	 * @param e      event de click sur le slider
	 * @return true si le click a bien ete fait dans la bonne zone du slider
	 */
	private boolean isVerticalPositionnateInTrack(Slider slider, MouseEvent e) {
		int paddindX = 4;
		int paddingY = 1;
		boolean xRespected = paddindX <= e.getX() && e.getX() <= slider.getWidth() - paddindX;
		boolean yRespected = paddingY <= e.getY() && e.getY() <= slider.getHeight() - paddingY;
		return xRespected && yRespected;
	}

	/**
	 * Change la position de la musique selon la position du slider
	 */
	private void positionnateMusiqueLikeSlider() {
		double value = sliderTime.getValue();
		mediaPlayer.seek(new Duration(value * 1000));
	}

	/**
	 * Slider de la position de la musique est presse
	 *
	 * @param e event de presse du click de souris du slider de position
	 */
	@FXML
	private void sliderTimePressed(MouseEvent e) {
		if (isVerticalPositionnateInTrack(sliderTime, e)) {
			if (isPlaying) {
				isPauseChangeValue = true;
			}
			// on met pause sans changer le logo du bouton
			pause(false);
			positionnateMusiqueLikeSlider();
		}
	}

	/**
	 * Slider de la position de la musique est relache
	 *
	 * @param e event de relachement du click de souris du slider de position
	 */
	@FXML
	private void sliderTimeReleased(MouseEvent e) {
		if (isVerticalPositionnateInTrack(sliderTime, e)) {
			if (isPauseChangeValue) {
				// on met play sans changer le logo du bouton
				play(false);
			}
			positionnateMusiqueLikeSlider();
			isPauseChangeValue = false;
		}
	}

	/**
	 * Gestion event du click sur la listeView secondaire
	 *
	 * @param e event de click sur la secondaryList
	 */
	@FXML
	private void moveToPriority(MouseEvent e) {
		if (Math.floorMod(e.getClickCount(), 2) == 0) {
			// si on a fait n double click (le conteur ce stack quand on fait des
			// doubles click rapidement)
			int index = secondaryList.getSelectionModel().getSelectedIndex();
			if (index >= 0) {
				Music music = dashboard.getMusicAt(dashboard.getSecondaryQueue(), index);
				dashboard.switchToPriority(music);
			}

		}
	}

	/**
	 * Gestion event du click sur la listeView de la recherche
	 *
	 * @param e event de click sur la searchList
	 */
	@FXML
	private void moveToPrioritySearched(MouseEvent e) {
		if (Math.floorMod(e.getClickCount(), 2) == 0) {
			// si on a fait n double click (le conteur ce stack quand on fait des
			// doubles click rapidement)
			int index = searchList.getSelectionModel().getSelectedIndex();
			if (index >= 0) {
				Music music = filteredList.get(index);
				dashboard.switchToPriority(music);
			}
		}
	}

	/**
	 * Gestion event du click sur la listeView prioritaire
	 *
	 * @param e event de click sur la priorityList
	 */
	@FXML
	private void moveToSecondary(MouseEvent e) {
		if (Math.floorMod(e.getClickCount(), 2) == 0) {
			// si on a fait n double click (le conteur ce stack quand on fait des
			// doubles click rapidement)
			int index = priorityList.getSelectionModel().getSelectedIndex();
			if (index >= 0) {
				Music music = dashboard.getMusicAt(dashboard.getPriorityQueue(), index);
				dashboard.switchToSecondary(music);
			}
		}
	}

	/**
	 * actualise la fenetre principale en changeant les listes, les sliders, le
	 * lecteur avec la nouvelle musique courante
	 */
	private void update() {
		boolean isMuted = false;
		if (mediaPlayer != null) {
			// si il y a une musique en cours de lecture on arrete le lecteur
			isMuted = mediaPlayer.isMute();
			mediaPlayer.stop();
			mediaPlayer.dispose();
		}
		Music currentMusic = dashboard.getCurrentMusic();
		if (currentMusic != null) {
			// on charge la musique dans le lecteur
			Media media = new Media(Paths.get(currentMusic.getFullName()).toUri().toString().replace('\\', '/'));
			mediaPlayer = new MediaPlayer(media);
			// on prepare les valeurs du slider de position
			sliderTime.setMin(0);
			sliderTime.setMax((double) currentMusic.getDuration());
			sliderTime.setValue(0);
			if (isPlaying) {
				// si l'ancien lecteur avait la musique en cours de lecture,
				// on lance la lecture de la nouvelle musique
				play();
			}

			// ajout du listener sur la lecture de la musique qui changera la valeur du
			// slider de position au fur et a mesure de la lecture
			mediaPlayer.currentTimeProperty().addListener((observable, oldDuration, newDuration) -> {
				Duration d = mediaPlayer.getCurrentTime();
				sliderTime.setValue(d.toSeconds());
				// on change aussi le label contenant le temps courant de la lecture
				currentTimeLab.setText(Music.stringDuration((long) d.toSeconds()));
			});

			// ajout du listener quand la musique est finie
			mediaPlayer.setOnEndOfMedia(() -> {
				if (looping) {
					// si on a active le loop, on remet la musique a zero
					mediaPlayer.seek(Duration.ZERO);
				} else {
					// sinon on change de musique
					nextMusic(null);
				}
			});
			if (isMuted) {
				// si on avait mute le lecteur, on mute le nouveau
				mediaPlayer.setMute(true);
			}
		} else {
			// pas de musique dans le dashboard on met pause
			pause();
		}
		updateLabelsMusic();
	}

	private void updateLabelsMusic() {
		Music currentMusic = dashboard.getCurrentMusic();
		long sumDuration = dashboard.getSumDurationOfPriorityQueue();
		if (sumDuration > 0) {
			// si y'a au moins une musique dans la liste d'attente on affiche la somme
			durationPriority.setText("(" + Music.stringDuration(sumDuration) + ")");
			contentLabelsPriority.setSpacing(8);
		} else {
			// sinon on affiche pas la somme
			durationPriority.setText("");
			contentLabelsPriority.setSpacing(0);
		}
		if (currentMusic != null) {
			// on change les labels concernant les donnees de la musique courante
			titleMusic.setText(currentMusic.getName());
			authorMusic.setText(currentMusic.getAuthor());
			durationTimeLab.setText(currentMusic.getStringDuration());
		}
	}

	/**
	 * Les listes d'attentes ont changees
	 */
	@Override
	public void queuesHasChanged() {
		updateLabelsMusic();
	}

	/**
	 * La musique courante a changee
	 */
	@Override
	public void currentMusicHasChanged() {
		update();
	}

	/**
	 * le contenu du dashboard a change
	 */
	@Override
	public void contentHasChanged() {
		// on modifie la liste de recherche qui doit etre recalcule
		ObservableList<Music> observableSortedMusics = FXCollections.observableList(dashboard.getSortedMusics());
		setListenerSearch(observableSortedMusics);
	}

}
