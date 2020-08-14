
package lecteur_musique.model.observer;

public interface ListenableDashboard {
    
    public void addListener(DashboardListener listener);
    
    public boolean removeListener(DashboardListener listener);
    
    public void queuesHasChanged();
    
    public void currentMusicHasChanged();
    
}
