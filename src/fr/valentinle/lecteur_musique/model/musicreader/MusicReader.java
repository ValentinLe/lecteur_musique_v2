package fr.valentinle.lecteur_musique.model.musicreader;

import java.util.List;

import fr.valentinle.lecteur_musique.model.Music;

/**
 * Interface d'un listeur de fichier de musique
 */
public interface MusicReader {

    /**
     * Permet de lire toutes les musiques d'un certain format presente dans un
     * dossier donne
     *
     * @param folder le dossier contenant les musiques que l'ont veut lister
     * @return la liste des musiques presente dans le dossier
     */
    public List<Music> read(String folder);

}
