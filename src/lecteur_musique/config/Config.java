package lecteur_musique.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

/**
 * Classe permettant de gerer les configuration de l'application
 */
public class Config {

    // le chemin du fichier de configuration
    private String configFileName;
    // les differentes configurations de l'application sous forme de cle:valeur
    private Properties properties;
    
    /**
     * Charge les configurations d'un fichier
     * @param configFileName le fichier de configuration a charger
     */
    public Config(String configFileName) {
	this.configFileName = getLocation(configFileName);
	this.properties = new Properties();
	read();
    }
    
    /**
     * Charge les configurations du fichier de configuration par defaut
     */
    public Config() {
	this(ConfigParams.CONFIG_FILENAME);
    }
    
    /**
     * Donne la localisation du fichier de configuration
     * @param configFileName le fichier de configuration a trouver
     * @return le nom absolue du fichier de configuration ou null si il n'a pas
     * ete trouve
     */
    public String getLocation(String configFileName) {
	// si l'execution se trouve a la racine du projet, on retourne le chemin
	// jusqu'a la racine plus la position du fichier de configuraion depuis la
	// racine
	String path1 = System.getProperty("user.dir") + configFileName;
	if (new File(path1).exists()) {
	    return path1;
	}
	return null;
    }

    /**
     * Change le fichier de configuration
     * @param newConfigFileName le nouveau fichier de configuration a prendre en compte
     * @throws IOException si le nouveau fichier de configuration n'existe pas
     */
    public void setConfigFileName(String newConfigFileName) throws IOException {
	File newConfigFile = new File(newConfigFileName);
	if (newConfigFile.exists()) {
	    configFileName = newConfigFileName;
	    read();
	} else {
	    throw new IOException("The file you want set don't exists.");
	}
    }

    /**
     * Lis le contenu du fichier de configuration
     */
    public void read() {
	if (configFileName != null) {
	    Reader reader = null;
	    // on efface les configurations des eventuels lectures precedentes
	    properties.clear();
	    try {
		reader = new FileReader(configFileName);
		properties.load(reader);
	    } catch (IOException ex) {
		ex.printStackTrace();
	    } finally {
		if (reader != null) {
		    try {
			reader.close();
		    } catch (IOException ex) {
			ex.printStackTrace();
		    }
		}
	    }
	}
    }

    /**
     * Ecrit les configurations actuelles dans le fichier de configuration
     */
    public void write() {
	if (configFileName != null) {
	    Writer writer = null;
	    try {
		writer = new FileWriter(configFileName);
		properties.store(writer, configFileName);
		writer.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    } finally {
		if (writer != null) {
		    try {
			writer.close();
		    } catch (IOException ex) {
			ex.printStackTrace();
		    }
		}
	    }
	}
    }
    
    /**
     * Getter sur les properties
     * @return les properties des configurations
     */
    public Properties getProperties() {
	return properties;
    }

    /**
     * Donne la valeur de la cle de configuration donnee
     * @param configKey la cle de configuration dont on souhaite la valeur
     * @return la valeur de la cle de configuration
     */
    public String getValueOf(String configKey) {
	return properties.getProperty(configKey);
    }
    
    /**
     * Change la valeur d'une cle de configuration
     * @param configKey la cle de configuration a laquelle changer la valeur
     * @param configValue la nouvelle valeur de la cle
     */
    public void setValueOf(String configKey, String configValue) {
	properties.setProperty(configKey, configValue);
    }
}
