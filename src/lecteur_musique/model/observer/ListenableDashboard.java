
package lecteur_musique.model.observer;

/**
 * Interface representant un listener de model
 */
public interface ListenableDashboard {
    
    /**
     * Ajoute un listener au model
     * @param listener le listener a ajouter
     */
    public void addListener(DashboardListener listener);
    
    /**
     * Supprime un listener du model si il est present dans la liste des listener
     * @param listener le listener a supprimer
     * @return true si le model contient le listener, false sinon
     */
    public boolean removeListener(DashboardListener listener);
    
    /**
     * Indique a tous les listeners que l'ordre des elements des listes d'attente
     * a change
     */
    public void queuesHasChanged();
    
    /**
     * Indique a tous les listeners que la musique en cours de lecture a change
     */
    public void currentMusicHasChanged();
    
    /**
     * Indique a tous les listeners que le contenu du model a change
     */
    public void contentHasChanged();
    
}
