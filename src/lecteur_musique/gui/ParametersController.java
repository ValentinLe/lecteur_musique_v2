package lecteur_musique.gui;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lecteur_musique.config.Config;
import lecteur_musique.config.ConfigParams;
import lecteur_musique.model.Dashboard;
import lecteur_musique.model.Music;
import lecteur_musique.model.musicreader.MP3MusicReader;
import lecteur_musique.model.musicreader.MusicReader;

public class ParametersController implements Initializable {

    private Dashboard dashboard;
    private Config config;

    @FXML
    private TextField musicFolder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
	
    }

    public void setDashboard(Dashboard dashboard) {
	this.dashboard = dashboard;
    }

    public void setConfig(Config config) {
	this.config = config;
	String configMusicFolder = config.getValueOf(ConfigParams.MUSIC_FOLDER_KEY);
	if (configMusicFolder != null) {
	    musicFolder.setText(configMusicFolder);
	}
    }

    @FXML
    private void valid(ActionEvent e) {
	String configMusicFolder = config.getValueOf(ConfigParams.MUSIC_FOLDER_KEY);
	String newFolder = musicFolder.getText();
	if (!newFolder.isEmpty() && (configMusicFolder == null || !newFolder.equals(configMusicFolder))) {
	    File folder = new File(newFolder);
	    if (folder.exists()) {
		MusicReader reader = new MP3MusicReader();
		List<Music> musics = null;
		try {
		    musics = reader.read(newFolder);
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
		if (musics != null) {
		    config.setValueOf(ConfigParams.MUSIC_FOLDER_KEY, newFolder);
		    dashboard.clear();
		    dashboard.addAllMusic(musics);
		    dashboard.shuffleSecondaryQueue();
		    dashboard.nextMusic();
		}
	    }
	}
	close((Node) e.getSource());
    }
    
    @FXML
    private void cancel(ActionEvent e) {
	close((Node) e.getSource());
    }
    
    private void close(Node node) {
	Stage stage = (Stage) node.getScene().getWindow();
	stage.close();
    }
}
