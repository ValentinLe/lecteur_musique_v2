
package lecteur_musique.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Music {

    private String fullName;
    private String name;
    private String author;
    private long duration;
    
    public Music(String fullname, String author, long duration) {
	setFullName(fullname);
	setName(getFileNameWithoutExtension(fullname));
	setAuthor(author);
	setDuration(duration);
    }

    public void setFullName(String fullName) {
	this.fullName = fullName;
    }

    public void setName(String name) {
	this.name = name;
    }

    @Override
    public int hashCode() {
	int hash = 7;
	hash = 79 * hash + Objects.hashCode(this.name);
	return hash;
    }

    @Override
    public String toString() {
	return "Music{" + "name=" + name + ", author=" + author + '}';
    }
    
    public static String stringDuration(long secondsValue) {
	long minutes = Math.floorDiv(secondsValue, 60);
	long seconds = Math.floorMod(secondsValue, 60);
	String res = "";
	res += minutes + ":";
	if (seconds < 10) {
	    res += "0";
	}
	res += seconds;
	return res;
    }

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

    public void setAuthor(String author) {
	this.author = author;
    }

    public void setDuration(long duration) {
	this.duration = duration;
    }

    public String getFullName() {
	return fullName;
    }

    public String getName() {
	return name;
    }

    public String getAuthor() {
	return author;
    }

    public long getDuration() {
	return duration;
    }
    
    public long getMinutes() {
	return Math.floorDiv(duration, 60);
    }
    
    public long getSecondes() {
	return Math.floorMod(duration, 60);
    }
    
    private String getFileNameWithoutExtension(String fullname) {
	Path path = Paths.get(fullname);
	String fileName = path.getFileName().toString();
	if (fileName.indexOf(".") > 0) {
	    return fileName.substring(0, fileName.lastIndexOf("."));
	} else {
	    return fileName;
	}
    }
    
}
