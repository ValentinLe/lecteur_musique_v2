
package lecteur_musique.model.musicreader;

import java.util.List;
import lecteur_musique.model.Music;

public interface MusicReader {
    
    public List<Music> read(String folder) throws Exception;
    
}
