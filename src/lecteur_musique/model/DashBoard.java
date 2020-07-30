
package lecteur_musique.model;

import java.util.LinkedList;
import java.util.Queue;

public class DashBoard {
    
    private Music currentMusic;
    private LinkedList<Music> priorityQueue;
    private LinkedList<Music> secondaryQueue;

    public DashBoard() {
	this.priorityQueue = new LinkedList<>();
	this.secondaryQueue = new LinkedList<>();
    }

    public Music getCurrentMusic() {
	return currentMusic;
    }

    public Queue<Music> getPriorityQueue() {
	return priorityQueue;
    }

    public Queue<Music> getSecondaryQueue() {
	return secondaryQueue;
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
		currentMusic = priorityQueue.remove();
	    } else {
		currentMusic = secondaryQueue.remove();
	    }
	}
    }
    
    public void precedentMusic() {
	if (!isEmpty() && currentMusic != null) {
	    Music lastMusic = secondaryQueue.getLast();
	    if (!priorityQueue.isEmpty()) {
		priorityQueue.addFirst(currentMusic);
	    } else {
		secondaryQueue.addFirst(currentMusic);
	    }
	    currentMusic = lastMusic;
	    secondaryQueue.remove(currentMusic);
	}
    }
    
    public void addMusic(Music music) {
	this.secondaryQueue.add(music);
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
