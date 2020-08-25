package lecteur_musique.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.Properties;

public class Config {

    private String configFileName;
    private Properties properties;

    public Config(String configFileName) {
	this.configFileName = getLocation(configFileName);
	this.properties = new Properties();
	read();
    }
    
    public Config() {
	this(ConfigParams.CONFIG_FILENAME);
    }
    
    public String getLocation(String configFileName) {
	String path1 = System.getProperty("user.dir") + configFileName;
	if (new File(path1).exists()) {
	    return path1;
	}
	CodeSource codeSource = Config.class.getProtectionDomain().getCodeSource();
	String path2 = null;
	try {
	    path2 = codeSource.getLocation().toURI().getPath();
	} catch (URISyntaxException ex) {
	    ex.printStackTrace();
	}
	path2 = new File(path2).getParent() + configFileName;
	if (new File(path2).exists()) {
	    return path2;
	}
	return null;
    }

    public void setConfigFileName(String newConfigFileName) throws IOException {
	File newConfigFile = new File(newConfigFileName);
	if (newConfigFile.exists()) {
	    configFileName = newConfigFileName;
	    read();
	} else {
	    throw new IOException("The file you want set don't exists.");
	}
    }

    public void read() {
	Reader reader = null;
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

    public boolean write() {
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
	return false;
    }
    
    public Properties getProperties() {
	return properties;
    }

    public String getValueOf(String configKey) {
	return properties.getProperty(configKey);
    }
    
    public void setValueOf(String configKey, String configValue) {
	properties.setProperty(configKey, configValue);
    }
}
