package fr.valentinle.lecteur_musique.model.musicreader;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;

import fr.valentinle.lecteur_musique.model.Music;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Listeur de fichier MP3
 */
public class MP3MusicReader implements MusicReader {

	/**
	 * Permet de lire toutes les musiques au format MP3 presentes dans un dossier
	 * donne
	 *
	 * @param folder le dossier contenant les musiques que l'ont veut lister
	 * @return la liste des musiques au format MP3 presente dans le dossier
	 */
	@Override
	public List<Music> read(String folder) {
		List<Music> musics = new ArrayList<>();
		File dir = new File(folder);
		for (String name : dir.list()) {
			try {
				File musicFile = new File(dir.getAbsolutePath(), name);
				Mp3File fileMp3 = new Mp3File(musicFile.getAbsolutePath());
				ID3v2 id3v2Tag = fileMp3.getId3v2Tag();
				Mp3File mp3file = new Mp3File(musicFile.getAbsolutePath());
				musics.add(new Music(musicFile.getAbsolutePath(), id3v2Tag.getArtist(), mp3file.getLengthInSeconds()));
			} catch (IllegalArgumentException ex) {
				// fichier non MP3, on passe au fichier suivant dans le dossier
				continue;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return musics;
	}
}
