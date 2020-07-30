
package lecteur_musique.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Music {

    private String fullName;
    private String name;
    private String author;
    private int duration;
    
    public Music(String fullname, String author, int duration) {
	setFullName(fullname);
	Path path = Paths.get(fullname);
	setName(path.getFileName().toString());
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

    public void setDuration(int duration) {
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

    public int getDuration() {
	return duration;
    }
    
}
