
package lecteur_musique.model.observer;

public interface DashboardListener {
    
    public void queuesHasChanged();
    
    public void currentMusicHasChanged();
    
    public void contentHasChanged();
    
}
