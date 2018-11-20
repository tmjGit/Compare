package tmj.ui.lang;

import java.util.Collection;
import java.util.HashMap;
import java.io.File;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import org.pmw.tinylog.Logger;
import tmj.app.Application;

/*
 * TODO Dieses Konzept auf alle Textinformationen anwenden
 * TODO besser 3stellige Sprachcodes nach ISO nutzen: deu, eng, ...
 */
public final class Localization {
	private static final String LANG_DEFAULT="en_US"; 
	private static Language language=null;//String language=null;
	private static final Map<String,ResourceBundle> languagesMap=new HashMap<>();
	private static String element="";
	private static final String PATH = "properties/";
	private static final String NAME = "MessagesBundle";
	private static final String SUFFIX = "properties";
	private static final String RESOURCES = NAME + "_.._..\\."+SUFFIX;
	private static final Map<String,Language> languages=new HashMap<>();

	static {
		init();
	}
	
	private Localization(){} // kann nicht instanziiert werden.

	public static void setLanguage(Language language) {
		Logger.trace("language={}",language);
		if(language==null) // No checking of existance, here. Resources could be added later. Before LANG_DEFAULT will be used.
			Logger.warn("\"null\" is no appropriate language!");
		else
			Localization.language=language;
	}
	
	public static void setLanguage(String language) {
		Logger.trace("language={}",language);
		if(language==null || language.equals("")) 
			Logger.warn("\"{}\" is not an appropriate language!",language);
		else
			Localization.language=languages.get(language);// No checking of existance, here. Resources could be added later. Meanwhile LANG_DEFAULT will be used.
	}
	
	public static Language getLanguage() {
		return language;
	}
	
	public static Collection<Language> getLanguages() {
		return languages.values();
	}

	private static void init() {
		Logger.trace("init...");
		// prop.load(new BufferedInputStream(Configs.class.getResourceAsStream( file )));
		File f = new File("src/" + PATH);
		if (!f.exists())
			return;
		File[] resourceFiles = f.listFiles((dir, name) -> {
			// Logger.trace("dir="+dir+", name="+name);
			Pattern pattern = Pattern.compile(RESOURCES, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			Predicate<String> p = pattern.asPredicate();
			return p.test(name);
		});
		for (int i = 0; i < resourceFiles.length; i++) {
			String s=filenameToFilecode( resourceFiles[i].getName() );
			languages.put(s, new Language(s));
		}
}

	private static String filenameToFilecode(String s) {
		return s.substring(NAME.length() + 1, s.indexOf("."+SUFFIX) ); // <NAME>_XX_YY.<SUFFIX>... // the structure of the file names is ensured by the Predicate above
	}

	public static void load() { // optional preload the defined language resource
		Logger.trace("languaceCode="+language);
		getResource(language.getName());
	}

	private static ResourceBundle getResource(String languageCode) {
		Logger.trace("languaceCode="+languageCode);
		if (languageCode.equals(""))
			return null;
		if(languagesMap.containsKey(languageCode)) // language resource not loaded, yet
			return languagesMap.get(languageCode);
		if(!languages.containsKey(languageCode)) // no resource file for this language
			return null;
		return loadResource(languageCode);
	}

	private static ResourceBundle loadResource(String languageCode) {
		Logger.trace("languaceCode="+languageCode);
		int separatorIndex=languageCode.indexOf('_');
		String language=languageCode.substring(0, separatorIndex);
		String country=languageCode.substring(separatorIndex+1,languageCode.length());
		Logger.trace("languageCode={}, language={}, country={}",languageCode,language,country);
		Locale locale = new Locale(language, country);
		try {
			ResourceBundle resource= ResourceBundle.getBundle(PATH+NAME, locale); // getBundle(baseName, locale,  this.getClass().getClassLoader()), // throws MissingResourceException -
			// <Projectfolder>/<src>/<PATH><NAME>
			languagesMap.put(languageCode, resource);
			return resource;
		}catch(MissingResourceException e) {
			Logger.warn("Language resource \"{}\" not found at \"{}\"!",locale,PATH+NAME);
			return null;
		}
	}
	
	public static String get(String key) {
		Logger.debug("key={}",key);
		if(key==null || key.equals(""))
			return "";
		if(language==null)
			setLanguage(LANG_DEFAULT); // ist dies gut oder ein unsauberer Seiteneffekt, weil dann eine Sprache gesetzt ist, obwohl eigentlich keine gesetzt wurde?
		if(get(language.getName(),key))
			return element;
		if( get(LANG_DEFAULT,key) )
			return element;
		return element; // In case of error this contains an appropriate dummy value and will be logged.
	}
	private static boolean get(String languageCode, String key) {
		Logger.trace("languageCode={}, key={}",languageCode, key);
	  try {
		  ResourceBundle resource=getResource(languageCode);
		  if(resource==null) {
			  element=Application.MISSING_RESOURCE;
			  return false;
		  }else {
			  element= resource.getString(key);
			  return true;
		  }
	  }catch(NullPointerException e) { //key is null
		  Logger.error(e,"languageCode={}, key={}",languageCode,key);
		  return false;
	  }catch(MissingResourceException e) { //no entry for the given key found
		  Logger.warn("Resource for language \"{}\" contains no entry for \"{}\"!",languageCode,key);
		  element= Application.MISSING_VALUE;
		  return false;
	  }catch(ClassCastException e) {// result is not a String
		  Logger.error(e,"languageCode={}, key={}",languageCode,key);
		  return false;
	  }
  }
 
}