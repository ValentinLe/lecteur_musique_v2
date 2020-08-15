package lecteur_musique.config;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Config {

    private String configFileName;
    private Map<String, String> mapConfig;

    public Config(String configFileName) {
	this.configFileName = configFileName;
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
	mapConfig = ConfigReader.read(configFileName);
    }

    public boolean write() {
	return configWriter.write(mapConfig, configFileName);
    }
    
    @Override
    protected void finalize() {
	write();
    }
    
    public Map<String, String> getMap() {
	return mapConfig;
    }

    public String getValueOf(String configKey) {
	return mapConfig.get(configKey);
    }
    
    public void setValueOf(String configKey, String configValue) {
	mapConfig.put(configKey, configValue);
	write();
    }
}
