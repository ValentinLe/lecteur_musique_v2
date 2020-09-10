package lecteur_musique.model.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe permettant d'avoir un objet ecoutable
 */
public abstract class AbstractListenableDashboard implements ListenableDashboard {

    // liste des ecouteurs du model
    private List<DashboardListener> listeners;

    /**
     * Un objet ecoutable n'a aucun listener au depart
     */
    public AbstractListenableDashboard() {
	this.listeners = new ArrayList<>();
    }

    /**
     * Ajoute un listener au model
     *
     * @param listener le listener a ajouter
     */
    @Override
    public void addListener(DashboardListener listener) {
	listeners.add(listener);
    }

    /**
     * Supprime un listener du model si il est present dans la liste des
     * listener
     *
     * @param listener le listener a supprimer
     * @return true si le model contient le listener, false sinon
     */
    @Override
    public boolean removeListener(DashboardListener listener) {
	return listeners.remove(listeners);
    }

    /**
     * Indique a tous les listeners que l'ordre des elements des listes
     * d'attente a change
     */
    @Override
    public void queuesHasChanged() {
	for (DashboardListener l : listeners) {
	    l.queuesHasChanged();
	}
    }

    /**
     * Indique a tous les listeners que la musique en cours de lecture a change
     */
    @Override
    public void currentMusicHasChanged() {
	for (DashboardListener l : listeners) {
	    l.currentMusicHasChanged();
	}
    }

    /**
     * Indique a tous les listeners que le contenu du model a change
     */
    @Override
    public void contentHasChanged() {
	for (DashboardListener l : listeners) {
	    l.contentHasChanged();
	}
    }

    /**
     * Indique a tous les listeners que tout a change
     */
    public void allHasChanged() {
	queuesHasChanged();
	currentMusicHasChanged();
	contentHasChanged();
    }

}
