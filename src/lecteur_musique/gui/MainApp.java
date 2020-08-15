
package lecteur_musique.gui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.*;

public class MainApp extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
	primaryStage.setMaximized(true);

	// Add to scene
	Parent root = FXMLLoader.load(getClass().getResource("/ressources/fxml/Dashboard.fxml"));
	Scene scene = new Scene(root);
	scene.getStylesheets().add(getClass().getResource("/ressources/css/style.css").toString());

	// Show the stage
	primaryStage.setTitle("Media Player");
	primaryStage.setScene(scene);
	primaryStage.show();
    }

    public static void main(String[] args) {
	launch(args);
    }
}
