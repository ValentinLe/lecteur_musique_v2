
package lecteur_musique.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lecteur_musique.model.observer.AbstractListenableDashboard;


public class DashBoard extends AbstractListenableDashboard {
    
    private Music currentMusic;
    private List<Music> priorityQueue;
    private List<Music> secondaryQueue;
    private Set<Music> musicsAddedToPriorityQueue;

    public DashBoard(List<Music> priorityQueue, List<Music> secondaryQueue) {
	this.priorityQueue = priorityQueue;
	this.secondaryQueue = secondaryQueue;
	this.musicsAddedToPriorityQueue = new HashSet<>();
	
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
    
    public List<Music> getMusics() {
	List<Music> musics = new ArrayList<>();
	musics.addAll(getPriorityQueue());
	musics.addAll(getSecondaryQueue());
	if (getCurrentMusic() != null) {
	    musics.add(getCurrentMusic());
	}
	return musics;
    }
    
    public List<Music> getSortedMusics() {
	List<Music> sortedMusics = getMusics();
	Collections.sort(sortedMusics);
	return sortedMusics;
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
	    if (priorityQueue.isEmpty()) {
		musicsAddedToPriorityQueue.clear();
	    }
	    allHasChanged();
	}
    }
    
    public void precedentMusic() {
	if (!secondaryQueue.isEmpty() && currentMusic != null) {
	    Music lastMusic = secondaryQueue.get(secondaryQueue.size() - 1);
	    if (!priorityQueue.isEmpty() && musicsAddedToPriorityQueue.contains(lastMusic)) {
		priorityQueue.add(0, currentMusic);
	    } else {
		secondaryQueue.add(0, currentMusic);
	    }
	    currentMusic = lastMusic;
	    secondaryQueue.remove(lastMusic);
	    allHasChanged();
	}
    }
    
    public void addMusic(Music music) {
	this.secondaryQueue.add(music);
	queuesHasChanged();
    }
    
    public void addAllMusic(Collection<Music> musics) {
	for (Music music : musics) {
	    addMusic(music);
	}
    }
    
    public boolean switchToPriority(Music music) {
	musicsAddedToPriorityQueue.add(music);
	return switchMusic(secondaryQueue, priorityQueue, music);
    }
    
    public boolean switchToSecondary(Music music) {
	if (musicsAddedToPriorityQueue.contains(music)) {
	    musicsAddedToPriorityQueue.remove(music);
	}
	return switchMusic(priorityQueue, secondaryQueue, music);
    }
    
    public boolean switchMusic(List<Music> startQueue, List<Music> endQueue, Music music) {
	if (startQueue.contains(music)) {
	    startQueue.remove(music);
	    endQueue.add(music);
	    queuesHasChanged();
	    return true;
	} else {
	    return false;
	}
    }
    
    public void shuffleSecondaryQueue() {
	Collections.shuffle(secondaryQueue);
	queuesHasChanged();
    }
    
    public long getSumDurationOfPriorityQueue() {
	return getSumDurationOf(priorityQueue);
    }
    
    public long getSumDurationOfSecondaryQueue() {
	return getSumDurationOf(secondaryQueue);
    }
    
    public long getSumDurationOf(List<Music> queue) {
	long sum = 0;
	for (Music music : queue) {
	    sum += music.getDuration();
	}
	return sum;
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
