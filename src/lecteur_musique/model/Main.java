
package lecteur_musique.model;

import lecteur_musique.model.musicreader.MP3MusicReader;
import lecteur_musique.model.musicreader.MusicReader;

public class Main {
    
    public static void main(String[] args) {
	String folder = "C:\\Users\\Val\\Desktop\\Dossier\\musiques\\";
	MusicReader reader = new MP3MusicReader();
	DashBoard dashboard = new DashBoard();
	try {
	    dashboard.addAllMusic(reader.read(folder));
	} catch (Exception e) {
	    e.printStackTrace();
	}
	dashboard.shuffleSecondaryQueue();
	dashboard.nextMusic();
    }
}
