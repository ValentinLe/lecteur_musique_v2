/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ressources.fxml;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Val
 */
public class DashBoardController implements Initializable {

    @FXML
    private Slider sliderTime;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
	// TODO
    }    

    @FXML
    private void playPause(ActionEvent event) {
    }

    @FXML
    private void changePosition(MouseEvent event) {
    }

    @FXML
    private void nextMusic(ActionEvent event) {
    }

    @FXML
    private void precedentMusic(ActionEvent event) {
    }
    
}
