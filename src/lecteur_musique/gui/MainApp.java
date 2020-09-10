package lecteur_musique.gui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.stage.*;

/**
 * Classe principale de l'application
 */
public class MainApp extends Application {

    /**
     * Demarre l'application
     *
     * @param primaryStage le stage principal de l'application
     * @throws IOException si une des ressources n'est pas lu correctement
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
	primaryStage.setMaximized(true);

	// Conetenu de la scene
	Parent root = FXMLLoader.load(getClass().getResource("/ressources/fxml/Dashboard.fxml"));
	Scene scene = new Scene(root);
	scene.getStylesheets().add(getClass().getResource("/ressources/css/main.css").toString());

	// Affichage du stage
	primaryStage.setTitle("Slatify");
	primaryStage.getIcons().add(new Image("/ressources/images/logo.png"));
	primaryStage.setScene(scene);
	primaryStage.show();
    }

    /**
     * Lance l'application
     *
     * @param args les arguments de la ligne de commande
     */
    public static void main(String[] args) {
	launch(args);
    }
}
