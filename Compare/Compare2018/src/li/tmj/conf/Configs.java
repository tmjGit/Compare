package li.tmj.conf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.KeyException;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.function.Function;
import java.util.function.Predicate;
import org.pmw.tinylog.Logger;
import li.tmj.app.Application;
import li.tmj.ui.lang.ErrorSet;

public enum Configs {
	// if a Config file by name resp. an entry by key evaluates to nothing, ...
	APPLICATION_DEFAULTS, // ... return Application's constant
	CURRENT_IDENTIFIER,   // ... return the file name resp. the key
	EMPTY_STRING,         // ... return an emty String
	EXCEPTION;            // ... throw MissingResourceException resp. NoSuchElementException
	
	private static HashMap<String,Config> confMap=new HashMap<>();
	private static String localeKeySeparator="_"; // e.g. MessageBundle_de_DE.properties
	private static Configs missingResourceDefinition=Configs.CURRENT_IDENTIFIER;

	private Configs(){} // kann nicht instanziiert werden.

	public static String getLocaleKeySeparator() {
		return localeKeySeparator;
	}

	public static void setLocaleKeySeparator(String localeKeySeparator) {
		Configs.localeKeySeparator = localeKeySeparator;
	}

	public static Configs getMissingResourceDefinition() {
		return missingResourceDefinition;
	}

	public static void setMissingResourceDefinition(Configs missingResourceDefinition) {
		Configs.missingResourceDefinition = missingResourceDefinition;
	}
	
	public static String missingResource(String configName) {
		switch(missingResourceDefinition) {
			case APPLICATION_DEFAULTS: return Application.MISSING_RESOURCE;
			case CURRENT_IDENTIFIER: return configName;
			case EMPTY_STRING: return "";
			case EXCEPTION: // falling through:
			default: throw new MissingResourceException(ErrorSet.configurationDoesNotExist(), Config.class.getName(), configName);
		}
	}

	/*
	 * This looks for config files, so it generates FileConfigs rather than Configs.
	 */
	public static HashMap<String,FileConfig> listInstalledConfigs(String propertyFolderPath, Predicate<String> p, Function<String,String> filenameToFilecode ) {
		Logger.trace("init...");
		File f = new File( propertyFolderPath );
		if (!f.exists()) {
			//TODO Exception
			return null;
		}
		File[] foundFiles = f.listFiles((dir, name) -> {
			// Logger.trace("dir="+dir+", name="+name);
			return p.test(name);
		});
		HashMap<String,FileConfig> foundConfigs=new HashMap<>();//new FileConfig[foundFiles.length];//found
		for (int i = 0; i < foundFiles.length; i++) {
			String s=filenameToFilecode.apply( foundFiles[i].getName() );
			foundConfigs.put(s, new FileConfig(s, foundFiles[i] ) );
		}
		return foundConfigs;
	}

	/**
	 * Creates an Config object with the given name and relative path and stores it 
	 * in the internal confMap for later use with get. If there is no configuration 
	 * file at that path, it optionally creates a new file with default values, if
	 * createFile is true.
	 * @param name			  name of the configuration used to access it with get.
	 * @param relativePath	  relative path of the configuration file as defined by Java for resources.
	 * @param createFile      optional boolean to indicate wether the method should create the file if missing.
	 */
	public static Config create(String name,String relativePath, boolean createFile) { // createFile = create properties file with default values, if not exist
		if(null==name || "".equals(name)) {
			//TODO exception, logging
			return null;
		}
		Logger.trace("Creating new Config Object...");
		FileConfig c;
		c=new FileConfig(name, relativePath, () -> DefaultMainConfig.create() );
		if(!c.hasFile) {
			c.resetToDefault();
		}
		return confMap.put(name, c);
	}
	

//		// <Projectfolder>/<src>/<PROP_PATH><LANG_BASENAME>
//	}catch(MissingResourceException e) {


	/**
	 * Combines the basename and the localeKeys separated with localeKeySeparator
	 * to a resource name and searches for it. If found a FileConfig based on that
	 * file will be returned. Otherwise the procedure is repeated ignoring the last 
	 * localeKey. This will be repeated until a file was found, if there is a matching one.
	 * The last file name would contain no localeKey at all.
	 * This emulates the behavior of the class.getResource() method.
	 * @param name
	 * @param basename
	 * @param localeKeys
	 * @return
	 */
	public static Config create(String name, String basename, String... localeKeys) { // change to FileConfig? This only makes sense, for FileConfigs
		String[] fullnames=new String[localeKeys.length+1];
		fullnames[localeKeys.length]=basename;
		for (int i=localeKeys.length; i>0; i--) {
			fullnames[i-1]=fullnames[i]+localeKeySeparator+localeKeys[localeKeys.length-i];
		}
		for(String relativePath:fullnames) {
			URL url = Configs.class.getResource( relativePath ); 
			if(null!=url) {
				Config c=create(name, url.toString(), false);
				if(c.hasFile) {
					return c;
				}				
			}
		}
		return null;
	}
	
	// e.g. create("Deutsch", "Messages", "de", "DE") sucht nach Messages_de_DE und Messages_de und Messages
	// e.g. create("Deutsch", "Messages", "de") sucht nach Messages_de und Messages
//	public static Config create(String name, String basename, String... localeKeys) {
//		
//	}
	
//	public static Config create(String name, File file, boolean createFile) {
//		//TODO
//		Properties p=new Propertie
//	}

//	public static Config create(String name, String relativePath, Locale locale, boolean createFile) {
//		//TODO
//	}
//	
//	public static Config create(String name) {
//		//TODO
//	}

	public static Config get(String name) {
		if(null==name || "".equals(name)) {
			//TODO exception, logging
			return null;
		}
		if(!confMap.containsKey(name)) {
			Logger.error(ErrorSet.configurationXdoesNotExist(),name);
			return null;
		}
		return confMap.get(name);
	}

	public static String get(String name,String key) {
		if(null==name || "".equals(name)) {
			//TODO exception, logging
			return null;
		}
		if(!confMap.containsKey(name)) {
			Logger.error(ErrorSet.configurationXdoesNotExist(),name);
			return missingResource(name);
		}
		try {
			return confMap.get(name).get(key);
		} catch (KeyException e) {
			Logger.warn(ErrorSet.keyXdoesNotExistInConfiguration(),key,name);
			return confMap.get(name).missingValue(key);
		} catch (FileNotFoundException e) {
			Logger.warn(ErrorSet.configurationFileXdoesNotExist(),name);
			return missingResource(name);
		} catch (IOException e) {
			Logger.error(e,ErrorSet.errorGettingConfigurationFileX(),name);
			return missingResource(name);
		}
	}
	
	public static boolean save(String name) {
		if(!confMap.containsKey(name)) {
			Logger.error(ErrorSet.configurationXdoesNotExist(),name);
			return false;
		}
		try {
			Config c= confMap.get(name);
			if(c instanceof FileConfig) {
				return ((FileConfig) c).save();
			}
			
			
		} catch (FileNotFoundException e) {
			Logger.warn(ErrorSet.configurationFileXdoesNotExist(),name);
			return false;
		} catch (IOException e) {
			Logger.error(e,ErrorSet.errorGettingConfigurationFileX(),name);
			return false;
		}
		return false;
	}

}
