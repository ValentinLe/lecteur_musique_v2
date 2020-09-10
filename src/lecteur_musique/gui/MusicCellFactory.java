package lecteur_musique.gui;

import java.util.List;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import lecteur_musique.model.Dashboard;
import lecteur_musique.model.Music;

/**
 * Factory sur une music d'une listView
 */
public class MusicCellFactory implements Callback<ListView<Music>, ListCell<Music>> {
    
    // le dashboard de l'application
    private Dashboard dashboard;
    // la liste que represente la listView
    private List<Music> queue;
    // booleen si la listView possede le drag and drop
    private boolean draggable;
    
    public MusicCellFactory(Dashboard dashboard, List<Music> queue, boolean draggable) {
	this.dashboard = dashboard;
	this.queue = queue;
	this.draggable = draggable;
    }
    
    @Override
    public ListCell<Music> call(ListView<Music> param) {
	return new MusicListCell(dashboard, queue, draggable);
    }
}
