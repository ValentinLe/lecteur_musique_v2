
package lecteur_musique.gui;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
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
    private ListView<String> secondaryList;

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
	for (Music m : dashBoard.getSecondaryQueue()) {
	    secondaryList.getItems().add(m.getName());
	}
    }
    
    @FXML
    private void closeApp(ActionEvent event) {
	System.exit(0);
    }
    
    @FXML
    private void playPause(ActionEvent event) {
	switchPlayPause();
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
    private void nextMusic(ActionEvent e) {
	dashBoard.nextMusic();
	updatePlayer();
    }

    @FXML
    private void precedentMusic(ActionEvent e) {
	dashBoard.precedentMusic();
	updatePlayer();
    }
    
    private boolean isPositionnateInTrack(MouseEvent e) {
	int paddindX = 8;
	int paddingY = 0;
	boolean xRespected = paddindX <= e.getX() && e.getX() <= sliderTime.getWidth() - paddindX;
	boolean yRespected = paddingY <= e.getY() && e.getY() <= sliderTime.getHeight() - paddingY;
	return xRespected && yRespected;
    }
    
    @FXML
    private void sliderTimeDragged(MouseEvent e) {
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
	if (mediaPlayer != null) {
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
	});
	
	mediaPlayer.setOnEndOfMedia(() -> {
	    nextMusic(null);
	});
    }

}
