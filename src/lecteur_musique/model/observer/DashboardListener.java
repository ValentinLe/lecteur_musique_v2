package lecteur_musique.model.observer;

public interface DashboardListener {

    /**
     * Indique qu'au moins un des elements d'une ou plusieurs listes d'attentes
     * ont ete deplaces
     */
    public void queuesHasChanged();

    /**
     * Indique que la musique en cours de lecture a changee
     */
    public void currentMusicHasChanged();

    /**
     * Indique que l'ensemble des musiques presentes dans le dashboard a change
     */
    public void contentHasChanged();

}
