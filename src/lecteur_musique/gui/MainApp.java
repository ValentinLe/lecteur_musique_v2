
package lecteur_musique.gui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.stage.*;

public class MainApp extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
	primaryStage.setMaximized(true);

	// Add to scene
	Parent root = FXMLLoader.load(getClass().getResource("/ressources/fxml/Dashboard.fxml"));
	Scene scene = new Scene(root);
	scene.getStylesheets().add(getClass().getResource("/ressources/css/main.css").toString());

	// Show the stage
	primaryStage.setTitle("Slatify");
	primaryStage.getIcons().add(new Image("/ressources/images/logo.png"));
	primaryStage.setScene(scene);
	primaryStage.show();
    }

    public static void main(String[] args) {
	launch(args);
    }
}
