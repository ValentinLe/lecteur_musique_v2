
package lecteur_musique.gui;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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

    @FXML
    private Slider sliderTime;
    
    
    @FXML
    private ListView<String> priorityList;
    @FXML
    private ListView<String> secondaryList;
    
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
    private Button bplaypause;
    
    @FXML
    private Button bmute;
    
    @FXML
    private Button bparameters;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
	dashBoard = new DashBoard();
	isPlaying = false;
	String folder = "C:\\Users\\Val\\Desktop\\Dossier\\musiques\\";

	MusicReader reader = new MP3MusicReader();
	try {
	    dashBoard.addAllMusic(reader.read(folder));
	} catch (Exception e) {
	    e.printStackTrace();
	}
	dashBoard.shuffleSecondaryQueue();
	dashBoard.nextMusic();
	updatePlayer();
	
	sliderVolume.valueProperty().addListener((observable, oldDuration, newDuration) -> {
	    if (mediaPlayer != null) {
		mediaPlayer.setVolume(sliderVolume.getValue()/100);
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
	updateSecondaryList();
    }

    @FXML
    private void nextMusic(ActionEvent e) {
	dashBoard.nextMusic();
	updatePlayer();
	
    }

    @FXML
    private void precedentMusic(ActionEvent e) {
	dashBoard.precedentMusic();
	updatePlayer();
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
    
    private boolean isPositionnateInTrack(MouseEvent e) {
	int paddindX = 5;
	int paddingY = 0;
	boolean xRespected = paddindX <= e.getX() && e.getX() <= sliderTime.getWidth() - paddindX;
	boolean yRespected = paddingY <= e.getY() && e.getY() <= sliderTime.getHeight() - paddingY;
	return xRespected && yRespected;
    }
    
    @FXML
    private void sliderTimePressed(MouseEvent e) {
	if (isPositionnateInTrack(e)) {
	    pause();
	    Music currentMusic = dashBoard.getCurrentMusic();
	    double value = sliderTime.getValue();
	    mediaPlayer.seek(new Duration(value * 1000));
	}
    }
    
    @FXML
    private void sliderTimeReleased(MouseEvent e) {
	play();
    }

    private void updatePlayer() {
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
	updatePrimaryList();
	updateSecondaryList();
    }
    
    private void updateLabelsMusic() {
	Music currentMusic = dashBoard.getCurrentMusic();
	titleMusic.setText(currentMusic.getName());
	authorMusic.setText(currentMusic.getAuthor());
	durationTimeLab.setText(Music.stringDuration(currentMusic.getDuration()));
    }
    
    private void updatePrimaryList() {
	priorityList.getItems().clear();
	for (Music m : dashBoard.getPriorityQueue()) {
	    priorityList.getItems().add(m.getName());
	}
    }
    
    private void updateSecondaryList() {
	secondaryList.getItems().clear();
	for (Music m : dashBoard.getSecondaryQueue()) {
	    secondaryList.getItems().add(m.getName());
	}
    }

}
