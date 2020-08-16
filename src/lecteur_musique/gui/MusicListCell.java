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

public class MusicListCell extends ListCell<Music> {

    @FXML
    private Label name;

    @FXML
    private Label author;

    @FXML
    private Label duration;

    @FXML
    private GridPane gridpane;

    private Dashboard dashboard;

    private List<Music> queue;

    private boolean draggable;

    final private String separatorData = "-";

    public MusicListCell(Dashboard dashboard, List<Music> queue, boolean draggable) {
	loadFXML();
	this.dashboard = dashboard;
	this.queue = queue;
	this.draggable = draggable;

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
		content.putString(Integer.toString(queue.indexOf(getItem())));
		dragboard.setContent(content);
		e.consume();
	    });

	    setOnDragOver((e) -> {
		if (e.getGestureSource() != this && e.getDragboard().hasString()) {
		    e.acceptTransferModes(TransferMode.COPY_OR_MOVE);
		}
		e.consume();
	    });

	    setOnDragDropped((e) -> {
		Dragboard db = e.getDragboard();
		boolean success = false;
		if (db.hasString()) {
		    ObservableList<Music> items = getListView().getItems();
		    String[] data = db.getString().split(separatorData);
		    int hoverIndex = items.indexOf(getItem());
		    int dragIndex = Integer.parseInt(data[0]);

		    List<Music> startQueue = ((MusicListCell) e.getGestureSource()).getQueue();
		    List<Music> endQueue = startQueue;
		    Music musicHover = getItem();

		    if (!startQueue.contains(musicHover)) {
			endQueue = dashboard.getOtherQueue(startQueue);
		    }
		    Music musicDragged = startQueue.get(dragIndex);
		    if (getItem() == null || !musicDragged.equals(getItem())) {
			startQueue.remove(musicDragged);
			if (hoverIndex < 0) {
			    endQueue.add(musicDragged);
			} else {
			    endQueue.add(hoverIndex, musicDragged);
			}
		    }
		    success = true;
		    getListView().getSelectionModel().select(hoverIndex);
		}
		e.setDropCompleted(success);
		e.consume();
	    });
	}
    }

    public List<Music> getQueue() {
	return queue;
    }

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
	    name.setText(music.getName());
	    author.setText(music.getAuthor());
	    duration.setText(music.getStringDuration());
	    prefWidthProperty().bind(getListView().widthProperty().subtract(Integer.MAX_VALUE));
	    setGraphic(gridpane);
	} else {
	    name.setText("");
	    author.setText("");
	    duration.setText("");
	}
    }
}
