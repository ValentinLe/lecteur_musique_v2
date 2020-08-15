package lecteur_musique.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class configWriter {

    public static boolean write(Map<String, String> mapConfig, String configFileName) throws URISyntaxException, FileNotFoundException, IOException {
	StringBuilder sb = new StringBuilder();
	for (String configKey : mapConfig.keySet()) {
	    sb.append(configKey);
	    sb.append(ConfigParams.SEPARATOR);
	    sb.append(mapConfig.get(configKey));
	    sb.append(System.lineSeparator());
	}
	URI fileName = ConfigReader.class.getResource(configFileName).toURI();
	OutputStream output = new FileOutputStream(fileName.getPath());
	File configFile = new File(fileName);
	if (configFile.exists()) {
	    try {
		return configFile.createNewFile();
	    } catch (FileNotFoundException e) {
		e.printStackTrace();
	    }
	}
	return false;
    }
}
