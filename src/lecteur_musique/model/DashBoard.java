
package lecteur_musique.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DashBoard {
    
    private Music currentMusic;
    private List<Music> priorityQueue;
    private List<Music> secondaryQueue;

    public DashBoard(List<Music> priorityQueue, List<Music> secondaryQueue) {
	this.priorityQueue = priorityQueue;
	this.secondaryQueue = secondaryQueue;
    }
    
    public DashBoard() {
	this(new ArrayList<>(), new ArrayList<>());
    }

    public Music getCurrentMusic() {
	return currentMusic;
    }

    public List<Music> getPriorityQueue() {
	return priorityQueue;
    }

    public List<Music> getSecondaryQueue() {
	return secondaryQueue;
    }
    
    public Music getMusicAt(List<Music> queue, int index) {
	return queue.get(index);
    }
    
    public boolean isEmpty() {
	return this.priorityQueue.isEmpty() && this.secondaryQueue.isEmpty();
    }
    
    public void nextMusic() {
	if (!isEmpty()) {
	    if (currentMusic != null) {
		secondaryQueue.add(currentMusic);
		currentMusic = null;
	    }
	    if (!priorityQueue.isEmpty()) {
		currentMusic = priorityQueue.remove(0);
	    } else {
		currentMusic = secondaryQueue.remove(0);
	    }
	}
    }
    
    public void precedentMusic() {
	if (!secondaryQueue.isEmpty() && currentMusic != null) {
	    Music lastMusic = secondaryQueue.get(secondaryQueue.size() - 1);
	    if (!priorityQueue.isEmpty()) {
		priorityQueue.add(0, currentMusic);
	    } else {
		secondaryQueue.add(0, currentMusic);
	    }
	    currentMusic = lastMusic;
	    secondaryQueue.remove(currentMusic);
	}
    }
    
    public void addMusic(Music music) {
	this.secondaryQueue.add(music);
    }
    
    public void addAllMusic(Collection<Music> musics) {
	for (Music music : musics) {
	    addMusic(music);
	}
    }
    
    public boolean switchToPriority(Music music) {
	return switchMusic(secondaryQueue, priorityQueue, music);
    }
    
    public boolean switchToSecondary(Music music) {
	return switchMusic(priorityQueue, secondaryQueue, music);
    }
    
    public boolean switchMusic(List<Music> startQueue, List<Music> endQueue, Music music) {
	if (startQueue.contains(music)) {
	    startQueue.remove(music);
	    endQueue.add(music);
	    return true;
	} else {
	    return false;
	}
    }
    
    public void shuffleSecondaryQueue() {
	Collections.shuffle(secondaryQueue);
    }

    @Override
    public String toString() {
	String stringRepr = "\n";
	stringRepr += "Current Music : " + currentMusic + "\n";
	stringRepr += "Priority Queue :";
	for (Music music : priorityQueue) {
	    stringRepr += "\n   " + music;
	}
	stringRepr += "\nSecondary Queue :";
	for (Music music : secondaryQueue) {
	    stringRepr += "\n   " + music;
	}
	return stringRepr;
    }
    
    
}
