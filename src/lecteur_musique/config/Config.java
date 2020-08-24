package lecteur_musique.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

public class Config {

    private String configFileName;
    private Properties properties;

    public Config(String configFileName) {
	this.configFileName = System.getProperty("user.dir") + configFileName;
	this.properties = new Properties();
	read();
    }
    
    public Config() {
	this(ConfigParams.CONFIG_FILENAME);
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
