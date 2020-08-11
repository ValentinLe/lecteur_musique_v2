package lecteur_musique.gui;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import lecteur_musique.model.DashBoard;
import lecteur_musique.model.Music;
import lecteur_musique.model.musicreader.MP3MusicReader;
import lecteur_musique.model.musicreader.MusicReader;

public class DashBoardController implements Initializable {

    private MediaPlayer mediaPlayer;
    private DashBoard dashBoard;
    private boolean isPlaying;
    private boolean isPauseChangeValue;

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
    private Button bmute;

    @FXML
    private Button bparameters;

    private FilteredList<Music> filteredList;
    
    @FXML
    private HBox contentLabelsPriority; 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
	dashBoard = new DashBoard(
		FXCollections.observableList(new ArrayList<>()),
		FXCollections.observableList(new ArrayList<>())
	);
	isPlaying = false;
	isPauseChangeValue = false;
	String folder = "C:\\Users\\Val\\Desktop\\Dossier\\musiques\\";

	MusicReader reader = new MP3MusicReader();
	try {
	    dashBoard.addAllMusic(reader.read(folder));
	} catch (Exception e) {
	    e.printStackTrace();
	}
	dashBoard.shuffleSecondaryQueue();
	dashBoard.nextMusic();

	sliderVolume.valueProperty().addListener((observable, oldValue, newValue) -> {
	    if (mediaPlayer != null) {
		mediaPlayer.setVolume(sliderVolume.getValue() / 100);
	    }
	    progressVolume.setProgress(sliderVolume.getValue() / sliderVolume.getMax());
	});

	sliderTime.valueProperty().addListener((observable, oldDuration, newDuration) -> {
	    progressTime.setProgress(sliderTime.getValue() / sliderTime.getMax());
	});

	searchList.setCellFactory(new MusicCellFactory());
	priorityList.setCellFactory(new MusicCellFactory());
	secondaryList.setCellFactory(new MusicCellFactory());

	ObservableList<Music> observableSortedMusics = FXCollections.observableList(dashBoard.getSortedMusics());
	ObservableList<Music> observablePriority = (ObservableList<Music>) dashBoard.getPriorityQueue();
	ObservableList<Music> observableSecondary = (ObservableList<Music>) dashBoard.getSecondaryQueue();

	priorityList.setItems(observablePriority);
	secondaryList.setItems(observableSecondary);

	filteredList = new FilteredList<>(observableSortedMusics, s -> true);
	searchList.setItems(filteredList);

	searchinput.textProperty().addListener((observable, oldValue, newValue) -> {
	    filteredList.setPredicate(music -> {
		if (newValue == null || newValue.isEmpty()) {
		    return true;
		}
		String lowerCaseSearch = searchinput.getText().toLowerCase();
		return music.getName().toLowerCase().contains(lowerCaseSearch) ||
			music.getAuthor().toLowerCase().contains(lowerCaseSearch);
	    });
	});

	update();

	disableDefaultFocusTextField();
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

    @FXML
    private void playPause(ActionEvent event) {
	switchPlayPause();
	Image image = null;
	if (isPlaying) {
	    bplaypause.setStyle("-fx-background-image: url('/ressources/images/pause.png')");
	} else {
	    bplaypause.setStyle("-fx-background-image: url('/ressources/images/play.png')");
	}
    }

    private void play() {
	mediaPlayer.play();
	isPlaying = true;
    }

    private void pause() {
	mediaPlayer.pause();
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
	dashBoard.shuffleSecondaryQueue();
    }

    @FXML
    private void nextMusic(ActionEvent e) {
	dashBoard.nextMusic();
	update();
    }

    @FXML
    private void precedentMusic(ActionEvent e) {
	dashBoard.precedentMusic();
	update();
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
    }

    @FXML
    private void parameters(ActionEvent e) {

    }

    private boolean isVerticalPositionnateInTrack(Slider slider, MouseEvent e) {
	int paddindX = 4;
	int paddingY = 1;
	boolean xRespected = paddindX <= e.getX() && e.getX() <= slider.getWidth() - paddindX;
	boolean yRespected = paddingY <= e.getY() && e.getY() <= slider.getHeight() - paddingY;
	return xRespected && yRespected;
    }

    @FXML
    private void sliderTimePressed(MouseEvent e) {
	if (isVerticalPositionnateInTrack(sliderTime, e)) {
	    if (isPlaying) {
		isPauseChangeValue = true;
	    }
	    pause();
	    Music currentMusic = dashBoard.getCurrentMusic();
	    double value = sliderTime.getValue();
	    mediaPlayer.seek(new Duration(value * 1000));
	}
    }

    @FXML
    private void sliderTimeReleased(MouseEvent e) {
	if (isPauseChangeValue) {
	    play();
	}
	isPauseChangeValue = false;
    }

    @FXML
    private void moveToPriority(MouseEvent e) {
	if (e.getClickCount() == 2) {
	    int index = secondaryList.getSelectionModel().getSelectedIndex();
	    if (index >= 0) {
		Music music = dashBoard.getMusicAt(dashBoard.getSecondaryQueue(), index);
		dashBoard.switchToPriority(music);
		updateLabelsMusic();
	    }
	}
    }
    
    @FXML
    private void moveToPrioritySearched(MouseEvent e) {
	if (e.getClickCount() == 2) {
	    int index = searchList.getSelectionModel().getSelectedIndex();
	    if (index >= 0) {
		Music music = filteredList.get(index);
		dashBoard.switchToPriority(music);
		updateLabelsMusic();
	    }
	}
    }

    @FXML
    private void moveToSecondary(MouseEvent e) {
	if (e.getClickCount() == 2) {
	    int index = priorityList.getSelectionModel().getSelectedIndex();
	    if (index >= 0) {
		Music music = dashBoard.getMusicAt(dashBoard.getPriorityQueue(), index);
		dashBoard.switchToSecondary(music);
		updateLabelsMusic();
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
	Music currentMusic = dashBoard.getCurrentMusic();
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
	    nextMusic(null);
	});
	if (isMuted) {
	    mediaPlayer.setMute(true);
	}
	updateLabelsMusic();
    }

    private void updateLabelsMusic() {
	Music currentMusic = dashBoard.getCurrentMusic();
	long sumDuration = dashBoard.getSumDurationOfPriorityQueue();
	if (sumDuration > 0) {
	    durationPriority.setText("(" + Music.stringDuration(sumDuration) + ")");
	    contentLabelsPriority.setSpacing(8);
	} else {
	    durationPriority.setText("");
	    contentLabelsPriority.setSpacing(0);
	}
	titleMusic.setText(currentMusic.getName());
	authorMusic.setText(currentMusic.getAuthor());
	durationTimeLab.setText(currentMusic.getStringDuration());
    }

}
