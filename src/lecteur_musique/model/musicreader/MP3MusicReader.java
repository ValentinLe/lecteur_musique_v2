
package lecteur_musique.model.musicreader;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lecteur_musique.model.Music;

public class MP3MusicReader implements MusicReader {
    
    @Override
    public List<Music> read(String folder) {
	List<Music> musics = new ArrayList<>();
	File dir = new File(folder);
	for (String name : dir.list()) {
	    try {
		File musicFile = new File(dir.getAbsolutePath(), name);
		Mp3File file = new Mp3File(musicFile.getAbsolutePath());
		ID3v2 id3v2Tag = file.getId3v2Tag();
		Mp3File mp3file = new Mp3File(musicFile.getAbsolutePath());
		musics.add(new Music(musicFile.getAbsolutePath(), id3v2Tag.getArtist(), mp3file.getLengthInSeconds()));
	    } catch(IllegalArgumentException ex) {
		continue;
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	}
	return musics;
    }
}
