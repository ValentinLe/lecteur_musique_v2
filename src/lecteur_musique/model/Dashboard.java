package lecteur_musique.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lecteur_musique.model.observer.AbstractListenableDashboard;

/**
 * Le Dashboard permet de gerer les differentes listes d'attente, la musique
 * courrante et de decider qu'elle sera la prochaine. Il fera en sorte de vider
 * en priorite la liste d'attente quand celle-ci n'est pas vide, sinon il
 * prendra la premiere musique de la liste d'attente secondaire
 */
public class Dashboard extends AbstractListenableDashboard {

    // la musique qui est actuellement en train de jouer
    private Music currentMusic;
    // la liste prioritaire de musiques
    private List<Music> priorityQueue;
    // la liste secondaire de musiques
    private List<Music> secondaryQueue;
    // l'ensemble des musiques qui ont etees placees dans la liste d'attente prioritaire
    private Set<Music> musicsAddedToPriorityQueue;

    /**
     * Construit le dashboard en donnant les listes d'attentes en parametres
     *
     * @param priorityQueue la liste d'attente prioritaire
     * @param secondaryQueue la liste d'attente secondaire
     */
    public Dashboard(List<Music> priorityQueue, List<Music> secondaryQueue) {
	this.priorityQueue = priorityQueue;
	this.secondaryQueue = secondaryQueue;
	this.musicsAddedToPriorityQueue = new HashSet<>();
    }

    /**
     * Construit le dashboard avec les deux listes d'attente vides
     */
    public Dashboard() {
	this(new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Getter sur la musique qui est en train de jouer
     *
     * @return la musique courrante
     */
    public Music getCurrentMusic() {
	return currentMusic;
    }

    /**
     * Getter sur la liste d'attente prioritaire
     *
     * @return la liste d'attente prioritaire
     */
    public List<Music> getPriorityQueue() {
	return priorityQueue;
    }

    /**
     * Getter sur la liste d'attente secondaire
     *
     * @return la liste d'attente secondaire
     */
    public List<Music> getSecondaryQueue() {
	return secondaryQueue;
    }

    /**
     * Donne l'autre liste que la liste d'attente donnee en parametre
     *
     * @param queue liste d'attente dont on veut l'oppose
     * @return l'autre liste que la liste d'attente donnee en parametre
     */
    public List<Music> getOtherQueue(List<Music> queue) {
	if (getPriorityQueue() == queue) {
	    return getSecondaryQueue();
	} else {
	    return getPriorityQueue();
	}
    }

    /**
     * Donne la musique a une position donnee dans la liste d'attente donnee
     *
     * @param queue la liste d'attente
     * @param index la position de la musique que l'ont souhaite
     * @return la musique a une position voulu dans la liste d'attente
     */
    public Music getMusicAt(List<Music> queue, int index) {
	return queue.get(index);
    }

    /**
     * Donne toutes les musiques du dashboard (courrante et dans les deux listes
     * d'attentes)
     *
     * @return toutes les musiques du dashboard
     */
    public List<Music> getMusics() {
	List<Music> musics = new ArrayList<>();
	musics.addAll(getPriorityQueue());
	musics.addAll(getSecondaryQueue());
	if (getCurrentMusic() != null) {
	    musics.add(getCurrentMusic());
	}
	return musics;
    }

    /**
     * Donne la liste des musiques dans l'ordre croissant selon leur comparateur
     * (ici dans l'ordre alphabetique car le compareTo de Music est selon le nom
     * de la musique)
     *
     * @return la liste des musiques dans l'ordre croissant
     */
    public List<Music> getSortedMusics() {
	List<Music> sortedMusics = getMusics();
	Collections.sort(sortedMusics);
	return sortedMusics;
    }

    /**
     * Test si le dashboard est vide (il contient pas de musiques dans les
     * listes d'attentes et n'a pas de musique en cours de lecture)
     *
     * @return true si le dashboard contient aucune musiques, false sinon
     */
    public boolean isEmpty() {
	return this.currentMusic == null && this.priorityQueue.isEmpty() && this.secondaryQueue.isEmpty();
    }

    /**
     * Passage a la musique suivante, on place la musique courante en fin de la
     * liste d'attente secondaire et on prend ensuite la premiere musique d'une
     * des liste d'attente selon si la liste d'attente prioritaire est vide ou
     * non
     */
    public void nextMusic() {
	if (!isEmpty()) {
	    if (currentMusic != null) {
		// on place la musique courante a la fin de liste secondaire
		secondaryQueue.add(currentMusic);
		currentMusic = null;
	    }
	    if (priorityQueue.isEmpty()) {
		// si il n'y a plus de musique dans la liste prioritaire on efface
		// la memoires des musiques qui y ont etaient ajoutees
		musicsAddedToPriorityQueue.clear();
	    }
	    if (!priorityQueue.isEmpty()) {
		// si la liste prioritaire est vide on prend la premiere de la liste
		// secondaire
		currentMusic = priorityQueue.remove(0);
	    } else {
		// sinon on prend la premiere de la liste prioritaire
		currentMusic = secondaryQueue.remove(0);
	    }
	    // indications aux listeners
	    queuesHasChanged();
	    currentMusicHasChanged();
	}
    }

    public void precedentMusic() {
	if (!secondaryQueue.isEmpty() && currentMusic != null) {
	    // on recupere la derniere musique de la liste d'attente secondaire
	    Music lastMusic = secondaryQueue.get(secondaryQueue.size() - 1);
	    if (musicsAddedToPriorityQueue.contains(currentMusic)) {
		// si la musique a ete dans la liste d'attente prioritaire, elle y
		// est remise en tete de liste
		priorityQueue.add(0, currentMusic);
	    } else {
		// sinon elle est mise en tete de liste secondaire
		secondaryQueue.add(0, currentMusic);
	    }
	    // la musique courante devient alors la derniere musique jouee
	    currentMusic = lastMusic;
	    secondaryQueue.remove(lastMusic);
	    // indications aux listeners
	    queuesHasChanged();
	    currentMusicHasChanged();
	}
    }

    /**
     * Ajoute une musique dans le dashboard, elle sera placee a la fin de la
     * liste d'attente secondaire
     *
     * @param music la musique a ajouter
     */
    public void addMusic(Music music) {
	this.secondaryQueue.add(music);
	// indications aux listeners
	contentHasChanged();
    }

    /**
     * Ajoute toute les musiques de la collection donnee
     *
     * @param musics la collection contenant les musiques a ajouter
     */
    public void addAllMusic(Collection<Music> musics) {
	for (Music music : musics) {
	    addMusic(music);
	}
    }

    /**
     * Nettoie le dashboard en retirant toutes les musiques (dans les deux
     * listes d'attentes et la musique courante)
     */
    public void clear() {
	this.currentMusic = null;
	this.priorityQueue.clear();
	this.secondaryQueue.clear();
	this.musicsAddedToPriorityQueue.clear();
	// indications aux listeners
	allHasChanged();
    }

    /**
     * Deplace la musique de la liste secondaire vers la liste prioritaire si
     * celle-ci est dans la liste secondaire
     *
     * @param music la musique a deplacer
     * @return true si la musique a bien ete deplacee, false sinon
     */
    public boolean switchToPriority(Music music) {
	// on garde en memoire la musique qui a ete ajoutee a la liste prioritaire
	musicsAddedToPriorityQueue.add(music);
	return switchMusic(secondaryQueue, priorityQueue, music);
    }

    /**
     * Deplace la musique de la liste prioritaire vers la liste secondaire si
     * celle-ci est dans la liste prioritaire
     *
     * @param music la musique a deplacer
     * @return true si la musique a bien ete deplacee, false sinon
     */
    public boolean switchToSecondary(Music music) {
	if (musicsAddedToPriorityQueue.contains(music)) {
	    // on retire de la memoire la musique qui a ete ajoutee a la liste prioritaire
	    musicsAddedToPriorityQueue.remove(music);
	}
	return switchMusic(priorityQueue, secondaryQueue, music);
    }

    /**
     * Deplace une musique donnee d'une liste de depart vers une liste de
     * destination
     *
     * @param startQueue la liste de depart
     * @param endQueue la liste de destination
     * @param music la musique a deplacer
     * @return true si la musique a bien ete deplacee, false sinon
     */
    public boolean switchMusic(List<Music> startQueue, List<Music> endQueue, Music music) {
	// on prend la position du dernier element de la liste d'arrivee
	int index = endQueue.isEmpty() ? 0 : endQueue.size();
	return switchMusic(startQueue, endQueue, music, index);
    }

    /**
     * Deplace une musique donnee d'une liste de depart vers une liste de
     * destination a un indice donnee
     *
     * @param startQueue la liste de depart
     * @param endQueue la liste de destination
     * @param music la musique a deplacer
     * @param index l'indice dans la liste de destination auquel placer la
     * musique
     * @return true si la musique a bien ete deplacee, false sinon
     */
    public boolean switchMusic(List<Music> startQueue, List<Music> endQueue, Music music, int index) {
	if (startQueue.contains(music)) {
	    startQueue.remove(music);
	    endQueue.add(index, music);
	    // indications aux listeners
	    queuesHasChanged();
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * Deplace une musique dans une liste
     *
     * @param queue la liste dans laquelle la musique sera deplacee
     * @param music la musique a deplacer
     * @param indexEnd l'indice ou devra se trouver la musique dans la liste
     * apres le deplacement
     * @return true si la musique a bien ete deplacee, false sinon
     */
    public boolean moveMusic(List<Music> queue, Music music, int indexEnd) {
	if (queue.contains(music)) {
	    queue.remove(music);
	    // on prend la position avec une saturation par rapport a la taille
	    // de la liste d'arrivee
	    int position = queue.size() < indexEnd ? queue.size() : indexEnd;
	    queue.add(position, music);
	    // indications aux listeners
	    queuesHasChanged();
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * Deplace une musique vers le haut dans une liste d'attente donnee
     *
     * @param queue la liste d'attente
     * @param music la musique a monter dans la liste d'attente
     * @return true si la musique a bien ete deplacee, false sinon
     */
    public boolean moveMusicUp(List<Music> queue, Music music) {
	int indexMusic = queue.indexOf(music);
	if (indexMusic > 0) {
	    moveMusic(queue, music, indexMusic - 1);
	    return true;
	}
	return false;
    }

    /**
     * Deplace une musique vers le bas dans une liste d'attente donnee
     *
     * @param queue la liste d'attente
     * @param music la musique a descendre dans la liste d'attente
     * @return true si la musique a bien ete deplacee, false sinon
     */
    public boolean moveMusicDown(List<Music> queue, Music music) {
	int indexMusic = queue.indexOf(music);
	if (indexMusic >= 0 && indexMusic < queue.size() - 1) {
	    moveMusic(queue, music, indexMusic + 1);
	    return true;
	}
	return false;
    }

    /**
     * Melange la liste d'attente secondaire
     */
    public void shuffleSecondaryQueue() {
	Collections.shuffle(secondaryQueue);
	// indications aux listeners
	queuesHasChanged();
    }

    /**
     * Donne la somme des durees des musiques de la liste d'attente prioritaire
     *
     * @return la somme des durees des musiques de la liste d'attente
     * prioritaire
     */
    public long getSumDurationOfPriorityQueue() {
	return getSumDurationOf(priorityQueue);
    }

    /**
     * Donne la somme des durees des musiques de la liste d'attente prioritaire
     *
     * @return la somme des durees des musiques de la liste d'attente
     * prioritaire
     */
    public long getSumDurationOfSecondaryQueue() {
	return getSumDurationOf(secondaryQueue);
    }

    /**
     * Donne la somme des durees des musiques d'une liste d'attente donnee
     *
     * @param queue la liste d'attente
     * @return la somme des durees des musiques de la liste d'attente
     */
    public long getSumDurationOf(List<Music> queue) {
	long sum = 0;
	for (Music music : queue) {
	    sum += music.getDuration();
	}
	return sum;
    }

    /**
     * Representation du dashboard sous forme de String
     *
     * @return la representation sous forme de String du dashboard
     */
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
