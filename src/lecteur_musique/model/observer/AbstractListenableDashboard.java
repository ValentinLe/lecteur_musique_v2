
package lecteur_musique.model.observer;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractListenableDashboard implements ListenableDashboard {
    
    private List<DashboardListener> listeners;
    
    public AbstractListenableDashboard() {
	this.listeners = new ArrayList<>();
    }
    
    @Override
    public void addListener(DashboardListener listener) {
	listeners.add(listener);
    }
    
    @Override
    public boolean removeListener(DashboardListener listener) {
	return listeners.remove(listeners);
    }
    
    @Override
    public void queuesHasChanged() {
	for (DashboardListener l : listeners) {
	    l.queuesHasChanged();
	}
    }
    
    @Override
    public void currentMusicHasChanged() {
	for (DashboardListener l : listeners) {
	    l.currentMusicHasChanged();
	}
    }
    
    public void allHasChanged() {
	queuesHasChanged();
	currentMusicHasChanged();
    }
    
}
