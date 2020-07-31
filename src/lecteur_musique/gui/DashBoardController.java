
package lecteur_musique.gui;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    }

    @FXML
    private void playPause(ActionEvent event) {
	if (isPlaying) {
	    mediaPlayer.pause();
	} else {
	    mediaPlayer.play();
	}
	isPlaying = !isPlaying;
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
    private void changePosition(MouseEvent e) {
	Music currentMusic = dashBoard.getCurrentMusic();
	mediaPlayer.pause();
	double value = sliderTime.getValue();
	mediaPlayer.seek(new Duration(value * 1000));
	mediaPlayer.play();
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
	    mediaPlayer.play();
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
