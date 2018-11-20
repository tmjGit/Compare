package li.tmj.ui.lang;

// TODO automatisch generieren?
public final class LanguageSet {//implements ComboBoxable { 
	public static String browseLabel() { return get("Browse"); }
	public static String cancelLabel() { return get("Cancel"); }	
	public static String compareLabel() { return get("Compare"); }
	public static String entireLabel() { return get("Entire"); }
	public static String missingResource() { return get("missingResource"); }
	public static String notCalculateded() { return get("notCalculated");	}
	public static String different() { return get("different"); }
	public static String fileLabel() { return get("File"); }
	public static String folderLabel() { return get("Folder"); }
	public static String objectLabel() { return get("Object"); }
	public static String pauseLabel() { return get("Pause"); }
	public static String recursionLabel() { return get("Recursion"); }
	public static String resumeLabel() { return get("Resume"); }
	public static String same() { return get("same"); }
	public static String startLabel() { return get("Start"); }

	private static String get(String key) { return Localization.get(key); }
	
}
