package li.tmj.ui.lang;

import java.io.IOException;
import java.security.KeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.pmw.tinylog.Logger;

import li.tmj.app.Application;
import li.tmj.conf.Config;
import li.tmj.conf.ConfigEntries;
import li.tmj.conf.Configs;
import li.tmj.conf.FileConfig;

public final class Localization {
	public static Config config;

	static {
		config=createLocalization();
		config.setMissingValueDefinition(Configs.CURRENT_IDENTIFIER); // if a key evaluates to nothing, return the key
	}
	
	private Localization() {}
	
	public static Config getConfig() {
		return config;
	}
	
	public static String get(String key) {
		try {
			return config.get(key);
			
		} catch (KeyException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return key;
	}
	
	private static Config createLocalization() {
		HashMap<String,FileConfig> languagesMap=listInstalledLanguages();
		String languageCode= ConfigEntries.language(); 
//		if (null==languageCode || "".equals(languageCode)) {
//			return null;//TODO No localization setting found
//		}
		Config c=null;
		ArrayList<Config> cList=new ArrayList<>();
		if(null!=languageCode && !"".equals(languageCode)) {
//			Logger.trace("Localization: languageCode="+languageCode);
			if(languageCode.startsWith("{")) { // Maybe, there is a set of language keys defined.
				if(languageCode.endsWith("}")) {
					languageCode=languageCode.substring(1,languageCode.length()-1);
				}else {
					languageCode=languageCode.substring(1,languageCode.length());
				}
			}
			String[] languageCodes=languageCode.split(",");
			for(String code:languageCodes) {
				if(languagesMap.containsKey(code)) {
					if(null==c) {
						c=languagesMap.get(code) ; // the first found as Config
					}else {
						cList.add(languagesMap.get(code)); // the rest as additional fallback configs
					}
				}
			}
		}
		if(null==c) { // if none found, try default:
			if (languagesMap.containsKey(ConfigEntries.NAME_DEFAULT)) {
				c=languagesMap.get(ConfigEntries.NAME_DEFAULT) ;
				Logger.trace("Localization: {}: c={}",ConfigEntries.NAME_DEFAULT,c);
			}else {
				throw new MissingResourceException(ErrorSet.missingLanguageResource(),Localization.class.getName(),languageCode);
			}
		}else if(cList.size()>0) {//we found a config and fallbacks:
			c.setFallbacks(cList.toArray(new Config[cList.size()]));
		}else if(languagesMap.containsKey(ConfigEntries.NAME_DEFAULT)) { // we found a config but no fallbacks. Try default:
				c.setFallbacks(languagesMap.get(ConfigEntries.NAME_DEFAULT));
		}
//		if (languagesMap.containsKey(languageCode)) {
//			c=languagesMap.get(languageCode) ;
//			System.out.println("Localization: c="+c+", setFallback...");
//			c.setFallbacks(languagesMap.get("default"));
//		} else if (languagesMap.containsKey("default")) {
//			c=languagesMap.get("default") ;
//			System.out.println("Localization: default: c="+c);
//		}else {
//			throw new MissingResourceException("Missing language resource!",Localization.class.getName(),languageCode);
//		}
		return c;
	}
	
	private static HashMap<String,FileConfig> listInstalledLanguages() {
		Pattern pattern = Pattern.compile(Application.LANG_RESOURCES, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
		Predicate<String> p = pattern.asPredicate();
		return Configs.listInstalledConfigs( "src/" + Application.PROP_PATH ,
			p,
			s -> filenameToLocale(s)
		);
	}
	
	private static String filenameToLocale(String filename) {
		// <LANG_BASENAME>_XX_YY.<PROP_SUFFIX>... // the structure of the file names is ensured by the Predicate above
		int i=0;
		if(filename.endsWith("."+Application.PROP_SUFFIX)){
			i=1+Application.PROP_SUFFIX.length();
		}else if(filename.endsWith("."+Application.PROP_SUFFIX2)){
			i=1+Application.PROP_SUFFIX2.length();
		}else if(filename.endsWith(".")){
			i=1;
		}
		String s;
		if(i>0) { // yes, we have a suffix
			s = filename.substring(0, filename.length()-i);
		}else {
			s=filename;
		}
		if(s.length() > Application.LANG_BASENAME.length()) {
			return s.substring(Application.LANG_BASENAME.length()+1);
		}
		return ConfigEntries.NAME_DEFAULT;
	}
	
}