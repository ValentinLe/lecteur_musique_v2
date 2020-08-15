package lecteur_musique.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ConfigReader {

    public static Map<String, String> read(String configFileName) {
	Map<String, String> mapConfig = new HashMap<>();
	try {
	    InputStream input = ConfigReader.class.getResourceAsStream(configFileName);
	    BufferedReader br = new BufferedReader(new InputStreamReader(input));
	    try {
		String line;
		while ((line = br.readLine()) != null) {
		    String[] keyValue = line.split(ConfigParams.SEPARATOR);
		    if (keyValue.length == 2) {
			mapConfig.put(keyValue[0], keyValue[1]);
		    } 
		}
	    } finally {
		br.close();
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return mapConfig;
    }
}
