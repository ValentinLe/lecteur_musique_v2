package lecteur_musique.gui;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import lecteur_musique.model.Music;

public class MusicCell extends ListCell<Music> {

    @FXML
    private Label name;

    @FXML
    private Label author;

    @FXML
    private Label duration;
    
    @FXML
    private GridPane gridpane;

    private ListView<Music> parent;
    
    public MusicCell(ListView<Music> parent) {
	loadFXML();
	this.parent = parent;
    }

    private void loadFXML() {
	try {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ressources/fxml/music_cell.fxml"));
	    loader.setController(this);
	    loader.load();
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

    @Override
    protected void updateItem(Music music, boolean empty) {
	super.updateItem(music, empty);

	if (!empty) {
	    name.setText(music.getName());
	    author.setText(music.getAuthor());
	    duration.setText(music.getStringDuration());
	    prefWidthProperty().bind(parent.widthProperty().subtract(Integer.MAX_VALUE));
	    setGraphic(gridpane);
	} else {
	    name.setText("");
	    author.setText("");
	    duration.setText("");
	}
    }
}
