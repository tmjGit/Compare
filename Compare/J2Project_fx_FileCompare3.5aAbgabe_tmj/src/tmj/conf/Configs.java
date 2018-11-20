package tmj.conf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyException;
import java.util.HashMap;
import org.pmw.tinylog.Logger;

import tmj.app.Application;

public final class Configs {
	private static HashMap<String,Config> confMap=new HashMap<>();

	private Configs(){} // kann nicht instanziiert werden.

	public static void create(String name,String file) {
		Logger.trace("Creating new Config Object...");
		confMap.put(name, new Config(file));
	}

	public static String get(String name,String key) {
		if(!confMap.containsKey(name)) {
			Logger.error("Configuration \"{}\" does not exist!",name);
			return Application.MISSING_VALUE;
		}
		try {
			return confMap.get(name).get(key);
		} catch (KeyException e) {
			Logger.warn("Key \"{}\" does not exist in the configuration \"{}\"!",key,name);
			return Application.MISSING_VALUE;
		} catch (FileNotFoundException e) {
			Logger.warn("Configuration file \"{}\" does not exist!",name);
			return Application.MISSING_RESOURCE;
		} catch (IOException e) {
			Logger.error(e,"Error getting configuration file \"{}\"!",name);
			return Application.MISSING_RESOURCE;
		}
	}

}
