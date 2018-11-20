package ui.lang;

import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/*
 * TODO XML config file
 * TODO Dieses Konzept auf alle Textinformationen anwenden
 */
public final class LanguageSet {
	private static String language="";
	public static final String LANG_DEU="deu";
	public static final String LANG_ENG="eng";
	public static final String LANG_DEFAULT="eng";
	private static final HashMap<String, HashMap<String, String>> map= new HashMap<>();
	private static final HashMap<String,String> languages=new HashMap<>();
	private static String element="";

	static {
		languages.put("Deutsch", LANG_DEU);
		languages.put("Englisch", LANG_ENG);
		
		HashMap<String,String>langMap=new HashMap<>();
		langMap.put("notCalculated", "not calculated");
		langMap.put("same", "same");
		langMap.put("different", "different");
		langMap.put("File", "File");
		langMap.put("Folder", "Folder");
//		langMap.put("LANG_DEU", "German");
//		langMap.put("LANG_ENG", "English");
		map.put(LANG_ENG, langMap);
		
		langMap=new HashMap<>();
		langMap.put("notCalculated", "nicht berechnet");
		langMap.put("same", "gleich");
		langMap.put("different", "verschieden");
		langMap.put("File", "Datei");
		langMap.put("Folder", "Ordner");
//		langMap.put("LANG_DEU", "Deutsch");
//		langMap.put("LANG_ENG", "Englisch");
		map.put(LANG_DEU, langMap);
	}

	public static HashMap<String,String> languages(){
		return languages;
	}

	
	public static ObservableList<String> displayNames(){
		return FXCollections.observableArrayList(languages.keySet());
	}
	
	public static String getLanguage() {
		return language;
	}

	public static void setLanguage(String language) {
		LanguageSet.language=language;
	}

	public static String notCalculateded() {
		return get("notCalculated");
	}

	public static String same() {
		return get("same");
	}

	public static String different() {
		return get("different");
	}

	public static String file() {
		return get("File");
	}

	public static String folder() {
		return get("Folder");
	}

//	public static String langDeu() {
//		return get("LANG_DEU");
//	}
//
//	public static String langEng() {
//		return get("LANG_ENG");
//	}

	private static String get(String key) {
		if(get(language,key))
			return element;
		else {
			get(LANG_DEFAULT,key);
			return element;
		}
	}
	
	private static boolean get(String language,String key) {
		if(!map.containsKey(language)) 
			return false;
		else {
			HashMap<String,String> m=map.get(language);
			if(!m.containsKey(key))
				return false;
			else{
				element=m.get(key);
				return true;
			}
		}
	}
}
