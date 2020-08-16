package lecteur_musique.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class ConfigWriter {

    public static boolean write(Map<String, String> mapConfig, String configFileName) {
	StringBuilder sb = new StringBuilder();
	Iterator<String> it = mapConfig.keySet().iterator();
	while (it.hasNext()) {
	    String configKey = it.next();
	    sb.append(configKey);
	    sb.append(ConfigParams.SEPARATOR);
	    sb.append(mapConfig.get(configKey));
	    if (it.hasNext()) {
		sb.append(System.lineSeparator());
	    }
	}
	File configFile = new File(ConfigWriter.class.getResource(configFileName).getFile());
	try {
	    if (!configFile.exists()) {
		configFile.createNewFile();
	    }
	    FileWriter fw = new FileWriter(configFile);
	    BufferedWriter br = new BufferedWriter(fw);
	    br.write(sb.toString());
	    br.close();
	    fw.close();
	    return true;
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return false;
    }
}
