
package lecteur_musique.gui;

import java.nio.file.Paths;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import lecteur_musique.model.DashBoard;
import lecteur_musique.model.Music;

public class CustomMediaPlayer {
    
    private MediaPlayer mediaPlayer;
    private DashBoard dashBoard;

    public CustomMediaPlayer(DashBoard dashBoard) {
	this.dashBoard = dashBoard;
	nextMusic();
    }
    
    public MediaPlayer getPlayer() {
	return mediaPlayer;
    }
    
    public void nextMusic() {
	dashBoard.nextMusic();
	updateMusic();
    }
    
    public void precedentMusic() {
	dashBoard.precedentMusic();
	updateMusic();
    }
    
    public void updateMusic() {
	if (mediaPlayer != null) {
	    mediaPlayer.stop();
	}
	Music currentMusic = dashBoard.getCurrentMusic();
	Media media = new Media(Paths.get(currentMusic.getFullName()).toUri().toString().replace('\\', '/'));
	mediaPlayer = new MediaPlayer(media);
    }
    
    public void play() {
	mediaPlayer.play();
    }
    
    public void pause() {
	mediaPlayer.pause();
    }
    
    public void setVolume(double volume) {
	mediaPlayer.setVolume(volume);
    }
    
    public void mute() {
	mediaPlayer.setMute(!mediaPlayer.isMute());
    }
    
    public void shuffleSecondary() {
	dashBoard.shuffleSecondaryQueue();
    }
    
}
