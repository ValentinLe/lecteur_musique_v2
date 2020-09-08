
package lecteur_musique.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Represente une musique
 */
public class Music implements Comparable<Music> {

    // nom complet de la musique
    private String fullName;
    // nom du fichier de la musique sans l'extension
    private String name;
    // l'auteur de la musique
    private String author;
    // la duree en secondes de la musique
    private long duration;
    
    /**
     * 
     * @param fullname le nom absolue/complet de la musique
     * @param author l'auteur de la musique
     * @param duration la duree en secondes de la musique
     */
    public Music(String fullname, String author, long duration) {
	this.fullName = fullname;
	this.name = getFileNameWithoutExtension(fullname);
	this.author = author;
	this.duration = duration;
    }

    /**
     * La fonction de hash d'une musique se fait en fonction de son nom
     * @return la hashcode de la musique
     */
    @Override
    public int hashCode() {
	int hash = 7;
	hash = 79 * hash + Objects.hashCode(this.name);
	return hash;
    }
    
    /**
     * Donne une representation sous forme de String d'une musique contenant le 
     * nom et l'auteur de celle-ci
     * @return la representation sous forme de String d'une musique
     */
    @Override
    public String toString() {
	return "Music{" + "name=" + name + ", author=" + author + '}';
    }
    
    /**
     * Donne la representation d'une duree en secondes sous forme 'm:ss'
     * @param secondsValue la duree en secondes dont on veut la representation
     * @return la representation d'une duree en secondes sous forme 'm:ss'
     */
    public static String stringDuration(long secondsValue) {
	long minutes = Math.floorDiv(secondsValue, 60);
	long seconds = Math.floorMod(secondsValue, 60);
	String res = "";
	res += minutes + ":";
	if (seconds < 10) {
	    // on ajoute un 0 quand le nombre de secondes contient un chiffre
	    res += "0";
	}
	res += seconds;
	return res;
    }

    /**
     * Deux musiques sont egales si elles ont le meme nom
     * @param obj l'autre objet avec lequel tester l'egalite
     * @return true si la musique a le meme nom que celle donnee
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final Music other = (Music) obj;
	if (!Objects.equals(this.name, other.getName())) {
	    return false;
	}
	return true;
    }
    
    /**
     * Getter sur le nom complet/absolue de la musique
     * @return le nom complet/absolue de la musique
     */
    public String getFullName() {
	return fullName;
    }

    /**
     * Getter sur le nom de la musique
     * @return le nom de la musique
     */
    public String getName() {
	return name;
    }

    /**
     * Getter sur l'auteur de la musique
     * @return l'auteur de la musique
     */
    public String getAuthor() {
	return author;
    }

    /**
     * Getter sur la duree de la musique en secondes
     * @return la duree de la musique en secondes
     */
    public long getDuration() {
	return duration;
    }
    
    /**
     * Donne le nombre de minutes que dure la musique (partie entiere)
     * @return le nombre de minutes que dure la musique (partie entiere)
     */
    public long getMinutes() {
	return Math.floorDiv(duration, 60);
    }
    
    /**
     * Donne le nombre de secondes que dure la musique
     * @return le nombre de secondes que dure la musique
     */
    public long getSecondes() {
	return Math.floorMod(duration, 60);
    }
    
    /**
     * Donne la duree la musique sous forme de String 'm:ss' de la musique
     * @return la duree la musique sous forme de String 'm:ss' de la musique
     */
    public String getStringDuration() {
	return stringDuration(getDuration());
    }
    
    /**
     * Donne le nom du fichier sans extension ni chemin absolue
     * @param fullname le chemin absolue du fichier dont on veut le nom
     * @return le nom du fichier sans extension ni chemin absolue
     */
    private String getFileNameWithoutExtension(String fullname) {
	Path path = Paths.get(fullname);
	String fileName = path.getFileName().toString();
	if (fileName.indexOf(".") > 0) {
	    // si le fichier contient une extension
	    return fileName.substring(0, fileName.lastIndexOf("."));
	} else {
	    // le fichier ne contient pas d'extensions, on peut le retourner directement 
	    return fileName;
	}
    }

    /**
     * Compare deux musique par rapport a leur nom
     * @param o l'autre musique a comparer
     * @return 1, 0 ou -1 si le nom de la musique est plus petit, egale ou plus 
     * grand que l'autre musique que l'on compare
     */
    @Override
    public int compareTo(Music o) {
	return getName().compareTo(o.getName());
    }
}
