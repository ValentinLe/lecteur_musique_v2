package lecteur_musique.gui;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import lecteur_musique.model.Music;

public class MusicCellFactory implements Callback<ListView<Music>, ListCell<Music>> {

    @Override
    public ListCell<Music> call(ListView<Music> param) {
	return new MusicCell();
    }
}
