package lecteur_musique.gui;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.control.ListCell;
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
import lecteur_musique.config.Config;
import lecteur_musique.config.ConfigParams;
import lecteur_musique.model.Dashboard;
import lecteur_musique.model.Music;
import lecteur_musique.model.musicreader.MP3MusicReader;
import lecteur_musique.model.musicreader.MusicReader;
import lecteur_musique.model.observer.DashboardListener;

public class DashboardController implements Initializable, DashboardListener {

    private MediaPlayer mediaPlayer;
    private Dashboard dashboard;
    private Config config;
    private boolean isPlaying;
    private boolean isPauseChangeValue;

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

    private FilteredList<Music> filteredList;

    @FXML
    private HBox contentLabelsPriority;

    private boolean looping = false;

    private final ObjectProperty<ListCell<Music>> dragSource = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
	config = new Config();

	dashboard = new Dashboard(
		FXCollections.observableList(new ArrayList<>()),
		FXCollections.observableList(new ArrayList<>())
	);
	dashboard.addListener(this);
	isPlaying = false;
	isPauseChangeValue = false;

	String musicFolder = config.getValueOf(ConfigParams.MUSIC_FOLDER_KEY);
	if (musicFolder != null) {
	    readMusics(musicFolder);
	}

	sliderVolume.valueProperty().addListener((observable, oldValue, newValue) -> {
	    if (mediaPlayer != null) {
		mediaPlayer.setVolume(sliderVolume.getValue() / 100);
	    }
	    progressVolume.setProgress(sliderVolume.getValue() / sliderVolume.getMax());
	});

	sliderTime.valueProperty().addListener((observable, oldDuration, newDuration) -> {
	    progressTime.setProgress(sliderTime.getValue() / sliderTime.getMax());
	});

	ObservableList<Music> observableSortedMusics = FXCollections.observableList(dashboard.getSortedMusics());
	ObservableList<Music> observablePriority = (ObservableList<Music>) dashboard.getPriorityQueue();
	ObservableList<Music> observableSecondary = (ObservableList<Music>) dashboard.getSecondaryQueue();

	MusicCellFactory searchFactory = new MusicCellFactory(dashboard, observableSortedMusics, false);
	MusicCellFactory priorityFactory = new MusicCellFactory(dashboard, dashboard.getPriorityQueue(), true);
	MusicCellFactory secondaryFactory = new MusicCellFactory(dashboard, dashboard.getSecondaryQueue(), true);

	searchList.setCellFactory(searchFactory);
	priorityList.setCellFactory(priorityFactory);
	secondaryList.setCellFactory(secondaryFactory);

	priorityList.setItems(observablePriority);
	secondaryList.setItems(observableSecondary);

	setListenerSearch(observableSortedMusics);

	disableDefaultFocusTextField();

	dashboard.shuffleSecondaryQueue();
	dashboard.nextMusic();
	
	setKeyEventToListView(searchList);
	setKeyEventToListView(priorityList);
	setKeyEventToListView(secondaryList);
	
	root.setOnKeyPressed((e) -> {
	    if (e.getCode() == KeyCode.ESCAPE) {
		searchinput.setText("");
	    }
	});
    }
    
    private void setKeyEventToListView(ListView<Music> listView) {
	listView.setOnKeyPressed((e) -> {
	    List<Music> startQueue;
	    List<Music> endQueue;
	    Music music;
	    KeyCode moveCode;
	    boolean canMoveMusic = true;
	    
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
	    
	    KeyCombination keyCombSwitch = new KeyCodeCombination(moveCode, KeyCodeCombination.CONTROL_DOWN);
	    KeyCombination keyCombMoveUp = new KeyCodeCombination(KeyCode.UP, KeyCodeCombination.CONTROL_DOWN);
	    KeyCombination keyCombMoveDown = new KeyCodeCombination(KeyCode.DOWN, KeyCodeCombination.CONTROL_DOWN);
	    
	    if (keyCombSwitch.match(e)) {
		dashboard.switchMusic(startQueue, endQueue, music);
	    } else if (keyCombMoveUp.match(e)) {
		if (canMoveMusic) {
		    dashboard.moveMusicUp(startQueue, music);
		    listView.getSelectionModel().select(startQueue.indexOf(music));
		} else {
		    e.consume();
		}
	    } else if (keyCombMoveDown.match(e)) {
		if (canMoveMusic) {
		    dashboard.moveMusicDown(startQueue, music);
		    listView.getSelectionModel().select(startQueue.indexOf(music));
		} else {
		    e.consume();
		}
	    } else if (e.getCode() == KeyCode.ESCAPE) {
		searchinput.setText("");
	    }
	    
	});
    }

    private void setListenerSearch(ObservableList<Music> observableMusic) {
	filteredList = new FilteredList<>(observableMusic, s -> true);
	searchList.setItems(filteredList);

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

    private void disableDefaultFocusTextField() {
	final BooleanProperty firstTime = new SimpleBooleanProperty(true);

	searchinput.focusedProperty().addListener((observable, oldValue, newValue) -> {
	    if (newValue && firstTime.get()) {
		zoneLists.requestFocus(); // Delegate the focus to container 
		firstTime.setValue(false); // Variable value changed for future references 
	    }
	});
    }

    public void readMusics(String musicFolder) {
	MusicReader reader = new MP3MusicReader();
	try {
	    dashboard.addAllMusic(reader.read(musicFolder));
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    @FXML
    private void playPause(ActionEvent event) {
	switchPlayPause();
    }

    private void play() {
	play(true);
    }

    private void play(boolean turnButton) {
	if (mediaPlayer != null) {
	    mediaPlayer.play();
	    if (turnButton) {
		bplaypause.setStyle("-fx-background-image: url('/ressources/images/pause.png')");
	    }
	    isPlaying = true;
	}
    }

    private void pause() {
	pause(true);
    }

    private void pause(boolean turnButton) {
	if (mediaPlayer != null) {
	    mediaPlayer.pause();
	}
	if (turnButton) {
	    bplaypause.setStyle("-fx-background-image: url('/ressources/images/play.png')");
	}
	isPlaying = false;
    }

    private void switchPlayPause() {
	if (isPlaying) {
	    pause();
	} else {
	    play();
	}
    }

    @FXML
    private void shuffleSecondary(ActionEvent e) {
	dashboard.shuffleSecondaryQueue();
    }

    @FXML
    private void loop() {
	looping = !looping;
	lockButton(bloop);
    }

    @FXML
    private void nextMusic(ActionEvent e) {
	dashboard.nextMusic();
    }

    @FXML
    private void precedentMusic(ActionEvent e) {
	dashboard.precedentMusic();
    }

    @FXML
    private void mute(ActionEvent e) {
	if (!mediaPlayer.isMute()) {
	    mediaPlayer.setMute(true);
	    bmute.setStyle("-fx-background-image: url('/ressources/images/soundOff.png')");
	} else {
	    mediaPlayer.setMute(false);
	    bmute.setStyle("-fx-background-image: url('/ressources/images/soundOn.png')");
	}
	lockButton(bmute);
    }

    @FXML
    private void parameters(ActionEvent e) {
	try {
	    FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(getClass().getResource("/ressources/fxml/Parameters.fxml"));
	    Parent root = loader.load();
	    ParametersController paramControl = loader.getController();
	    paramControl.setDashboard(dashboard);
	    paramControl.setConfig(config);
	    Stage stage = new Stage();
	    stage.setTitle("Paramètres");
	    stage.setResizable(false);
	    Scene scene = new Scene(root);
	    scene.getStylesheets().add(getClass().getResource("/ressources/css/parameters.css").toString());
	    stage.getIcons().add(new Image("/ressources/images/logo.png"));
	    stage.setScene(scene);
	    stage.initOwner((Stage) zoneLists.getScene().getWindow());
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.centerOnScreen();
	    stage.show();
	} catch (IOException ex) {
	    ex.printStackTrace();
	}
    }

    private void lockButton(Button button) {
	String classLocked = "locked";
	ObservableList<String> classButton = button.getStyleClass();
	if (!classButton.contains(classLocked)) {
	    classButton.add(classLocked);
	} else {
	    classButton.remove(classLocked);
	}
    }

    private boolean isVerticalPositionnateInTrack(Slider slider, MouseEvent e) {
	int paddindX = 4;
	int paddingY = 1;
	boolean xRespected = paddindX <= e.getX() && e.getX() <= slider.getWidth() - paddindX;
	boolean yRespected = paddingY <= e.getY() && e.getY() <= slider.getHeight() - paddingY;
	return xRespected && yRespected;
    }

    private void positionnateMusiqueLikeSlider() {
	double value = sliderTime.getValue();
	mediaPlayer.seek(new Duration(value * 1000));
    }

    @FXML
    private void sliderTimePressed(MouseEvent e) {
	if (isVerticalPositionnateInTrack(sliderTime, e)) {
	    if (isPlaying) {
		isPauseChangeValue = true;
	    }
	    pause(false);
	    positionnateMusiqueLikeSlider();
	}
    }

    @FXML
    private void sliderTimeReleased(MouseEvent e) {
	if (isPauseChangeValue) {
	    play(false);
	}
	positionnateMusiqueLikeSlider();
	isPauseChangeValue = false;
    }

    @FXML
    private void moveToPriority(MouseEvent e) {
	if (Math.floorMod(e.getClickCount(), 2) == 0) {
	    int index = secondaryList.getSelectionModel().getSelectedIndex();
	    if (index >= 0) {
		Music music = dashboard.getMusicAt(dashboard.getSecondaryQueue(), index);
		dashboard.switchToPriority(music);
	    }

	}
    }

    @FXML
    private void moveToPrioritySearched(MouseEvent e) {
	if (Math.floorMod(e.getClickCount(), 2) == 0) {
	    int index = searchList.getSelectionModel().getSelectedIndex();
	    if (index >= 0) {
		Music music = filteredList.get(index);
		dashboard.switchToPriority(music);
	    }
	}
    }

    @FXML
    private void moveToSecondary(MouseEvent e) {
	if (Math.floorMod(e.getClickCount(), 2) == 0) {
	    int index = priorityList.getSelectionModel().getSelectedIndex();
	    if (index >= 0) {
		Music music = dashboard.getMusicAt(dashboard.getPriorityQueue(), index);
		dashboard.switchToSecondary(music);
	    }
	}
    }

    private void update() {
	boolean isMuted = false;
	if (mediaPlayer != null) {
	    isMuted = mediaPlayer.isMute();
	    mediaPlayer.stop();
	    mediaPlayer.dispose();
	}
	Music currentMusic = dashboard.getCurrentMusic();
	if (currentMusic != null) {
	    Media media = new Media(Paths.get(currentMusic.getFullName()).toUri().toString().replace('\\', '/'));
	    mediaPlayer = new MediaPlayer(media);
	    sliderTime.setMin(0);
	    sliderTime.setMax((double) currentMusic.getDuration());
	    sliderTime.setValue(0);
	    if (isPlaying) {
		play();
	    }

	    mediaPlayer.currentTimeProperty().addListener((observable, oldDuration, newDuration) -> {
		Duration d = mediaPlayer.getCurrentTime();
		sliderTime.setValue(d.toSeconds());
		currentTimeLab.setText(Music.stringDuration((long) d.toSeconds()));
	    });

	    mediaPlayer.setOnEndOfMedia(() -> {
		if (looping) {
		    mediaPlayer.seek(Duration.ZERO);
		} else {
		    nextMusic(null);
		}
	    });
	    if (isMuted) {
		mediaPlayer.setMute(true);
	    }
	} else {
	    pause();
	}
	updateLabelsMusic();
    }

    private void updateLabelsMusic() {
	Music currentMusic = dashboard.getCurrentMusic();
	long sumDuration = dashboard.getSumDurationOfPriorityQueue();
	if (sumDuration > 0) {
	    durationPriority.setText("(" + Music.stringDuration(sumDuration) + ")");
	    contentLabelsPriority.setSpacing(8);
	} else {
	    durationPriority.setText("");
	    contentLabelsPriority.setSpacing(0);
	}
	if (currentMusic != null) {
	    titleMusic.setText(currentMusic.getName());
	    authorMusic.setText(currentMusic.getAuthor());
	    durationTimeLab.setText(currentMusic.getStringDuration());
	}
    }

    @Override
    public void queuesHasChanged() {
	updateLabelsMusic();
    }

    @Override
    public void currentMusicHasChanged() {
	update();
    }

    @Override
    public void contentHasChanged() {
	ObservableList<Music> observableSortedMusics = FXCollections.observableList(dashboard.getSortedMusics());
	setListenerSearch(observableSortedMusics);
    }

}
