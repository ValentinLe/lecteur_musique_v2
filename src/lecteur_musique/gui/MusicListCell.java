package lecteur_musique.gui;

import java.io.IOException;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import lecteur_musique.model.Dashboard;
import lecteur_musique.model.Music;

/**
 * represente la liste des cellules de musiques d'une listeView
 */
public class MusicListCell extends ListCell<Music> {

    @FXML
    private Label name;

    @FXML
    private Label author;

    @FXML
    private Label duration;

    @FXML
    private GridPane gridpane;

    // le dashboard de l'application
    private Dashboard dashboard;

    // la liste que contient la listView
    private List<Music> queue;
    
    // booleen si la listView possede le drag and drop
    private boolean draggble;
    
    // seprateur pour les messages pour gerer le drag and drop
    final private String separatorData = "-";

    public MusicListCell(Dashboard dashboard, List<Music> queue, boolean draggable) {
	loadFXML();
	this.dashboard = dashboard;
	this.queue = queue;
	this.draggble = draggable;
	
	setOnMouseClicked((e) -> {
	    if (isEmpty()) {
		e.consume();
	    }
	});
	
	if (draggable) {
	    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
	    
	    setOnDragDetected((e) -> {
		if (getItem() == null) {
		    return;
		}
		ObservableList<Music> items = getListView().getItems();

		Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
		Node cell = (Node) e.getSource();
		dragboard.setDragView(cell.snapshot(null, null));
		ClipboardContent content = new ClipboardContent();
		// on ajoute l'indice de l'element drag dans le dragboard pour le
		// recuperer dans le dropped
		content.putString(Integer.toString(queue.indexOf(getItem())));
		dragboard.setContent(content);
		e.consume();
	    });
	    
	    setOnDragOver((e) -> {
		if (e.getDragboard().hasString()) {
		    e.acceptTransferModes(TransferMode.MOVE);
		}
		e.consume();
	    });

	    setOnDragDropped((e) -> {
		Dragboard db = e.getDragboard();
		boolean success = false;
		if (db.hasString()) {
		    // si y'a un String dans le dragboard
		    ObservableList<Music> items = getListView().getItems();
		    int hoverIndex = items.indexOf(getItem());
		    // on recupere l'indice de l'element drag contenu dans le dragboard
		    int dragIndex = Integer.parseInt(db.getString());
		    
		    // on recupere la liste de la musique drag
		    List<Music> startQueue = ((MusicListCell) e.getGestureSource()).getQueue();
		    List<Music> endQueue = startQueue;
		    Music musicHover = getItem();
		    boolean sameQueue = true;
		    if (!getQueue().equals(startQueue)) {
			// on regarde si la musique survole se trouve dans la meme 
			// liste
			endQueue = dashboard.getOtherQueue(startQueue);
			sameQueue = false;
		    }
		    Music musicDragged = startQueue.get(dragIndex);
		    if (getItem() == null || !musicDragged.equals(getItem())) {
			// si on drop sur du vide de la listView ou si la musique 
			// survole est differente de celle drag
			if (hoverIndex < 0) {
			    // si l'indice de l'element survole n'est pas defini,
			    // on survole le vide de la listView donc on ajoutera
			    // en fin de liste
			    hoverIndex = endQueue.size();
			}
			
			if (sameQueue) {
			    // si la musique drag et survolee ont la meme liste,
			    // on deplace la musique drag dans la liste
			    dashboard.moveMusic(queue, musicDragged, hoverIndex);
			} else {
			    // sinon on change la musique drag vers la liste de la
			    // musique survole a sa position
			    dashboard.switchMusic(startQueue, endQueue, musicDragged, hoverIndex);
			}
			if (hoverIndex == endQueue.size()) {
			    // si l'indice survole etait le dernier on retire sa valeur
			    hoverIndex -= 1;
			}
		    }
		    success = true;
		    // on selection la position survole dans la listView
		    getListView().getSelectionModel().select(hoverIndex);
		}
		e.setDropCompleted(success);
		e.consume();
	    });
	}
    }

    /**
     * Getter sur la liste d'attente de la listView
     * @return la liste d'attente de la listView
     */
    public List<Music> getQueue() {
	return queue;
    }

    /**
     * Charge la structure d'une cellule representant une musique
     */
    private void loadFXML() {
	try {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ressources/fxml/MusicCell.fxml"));
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
	    // si la cellule contient une musique, on affiche ses informations
	    name.setText(music.getName());
	    author.setText(music.getAuthor());
	    duration.setText(music.getStringDuration());
	    prefWidthProperty().bind(getListView().widthProperty().subtract(Integer.MAX_VALUE));
	    setGraphic(gridpane);
	} else {
	    // sinon on affiche rien
	    name.setText("");
	    author.setText("");
	    duration.setText("");
	}
    }
}
