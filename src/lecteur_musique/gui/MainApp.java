
package lecteur_musique.gui;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.media.MediaPlayer;
import javafx.stage.*;
import lecteur_musique.model.DashBoard;
import lecteur_musique.model.musicreader.MP3MusicReader;
import lecteur_musique.model.musicreader.MusicReader;

public class MainApp extends Application {
    
    private CustomMediaPlayer player;
    private MediaPlayer mediaPlayer;
    
    @Override
    public void start(Stage primaryStage) {
	
	String folder = "C:\\Users\\Val\\Desktop\\Dossier\\musiques\\";
	
	MusicReader reader = new MP3MusicReader();
	DashBoard dashboard = new DashBoard();
	try {
	    dashboard.addAllMusic(reader.read(folder));
	} catch (Exception e) {
	    e.printStackTrace();
	}
	dashboard.shuffleSecondaryQueue();
	dashboard.nextMusic();
	CustomMediaPlayer player = new CustomMediaPlayer(dashboard);
	mediaPlayer = player.getPlayer();

	// Add to scene
	Group root = new Group();
	Scene scene = new Scene(root, 500, 200);

	// Show the stage
	primaryStage.setTitle("Media Player");
	primaryStage.setScene(scene);
	primaryStage.show();
	player.play();
    }

    public static void main(String[] args) {
	launch(args);
    }
}
