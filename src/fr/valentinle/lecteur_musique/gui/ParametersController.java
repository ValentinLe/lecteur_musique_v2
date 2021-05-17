package fr.valentinle.lecteur_musique.gui;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import fr.valentinle.lecteur_musique.config.Config;
import fr.valentinle.lecteur_musique.config.ConfigParams;
import fr.valentinle.lecteur_musique.model.Dashboard;
import fr.valentinle.lecteur_musique.model.Music;
import fr.valentinle.lecteur_musique.model.musicreader.MP3MusicReader;
import fr.valentinle.lecteur_musique.model.musicreader.MusicReader;

/**
 * Controller de la fenetre de parametres
 */
public class ParametersController implements Initializable {

    // dashboard de l'application
    private Dashboard dashboard;
    // configurations de l'application
    private Config config;

    @FXML
    private TextField musicFolder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Setter sur le dashboard
     * 
     * @param dashboard le dashboard a setter
     */
    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

    /**
     * Setter sur les configurations
     * 
     * @param config les nouvelles configurations
     */
    public void setConfig(Config config) {
        this.config = config;
        // on ajoute le contenu des configurations dans la fenetre si ils existent
        String configMusicFolder = config.getValueOf(ConfigParams.MUSIC_FOLDER_KEY);
        if (configMusicFolder != null) {
            musicFolder.setText(configMusicFolder);
        }
    }

    /**
     * Validation des parametres
     * 
     * @param e event de click sur le bouton valider
     */
    @FXML
    private void valid(ActionEvent e) {
        String configMusicFolder = config.getValueOf(ConfigParams.MUSIC_FOLDER_KEY);
        String newFolder = musicFolder.getText();
        if (!newFolder.isEmpty() && (configMusicFolder == null || !newFolder.equals(configMusicFolder))) {
            // si le contenu du label du chemin des musiques n'est pas vide et
            // que le chemin present n'est pas deja dans les configurations
            File folder = new File(newFolder);
            if (folder.exists()) {
                // si le nouveau chemin specifie existe on va lire les musiques
                MusicReader reader = new MP3MusicReader();
                List<Music> musics = null;
                musics = reader.read(newFolder);
                if (musics != null) {
                    // si on a bien des musiques on va reinitialiser le dashboard et
                    // les configurations
                    config.setValueOf(ConfigParams.MUSIC_FOLDER_KEY, newFolder);
                    config.write();
                    dashboard.clear();
                    dashboard.addAllMusic(musics);
                    dashboard.shuffleSecondaryQueue();
                    dashboard.nextMusic();
                }
            }
        }
        // on ferme la fenetre
        close((Node) e.getSource());
    }

    /**
     * Annulation des parametres
     * 
     * @param e event de click sur le bouton cancel
     */
    @FXML
    private void cancel(ActionEvent e) {
        // on ferme la fenetre sans rien prendre en compte
        close((Node) e.getSource());
    }

    /**
     * Ferme la fenetre dans lequel se trouve le noeud donne
     * 
     * @param node le noeud dont fermer la fenetre dans laquelle il se trouve
     */
    private void close(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    /**
     * Ouverture du chooser de dossier
     * 
     * @param e event de click sur le bouton parcourir
     */
    @FXML
    private void musicPathChooser(ActionEvent e) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog((Stage) musicFolder.getScene().getWindow());

        if (selectedDirectory != null) {
            String newPath = selectedDirectory.getAbsolutePath();
            musicFolder.setText(newPath);
        }
    }
}
