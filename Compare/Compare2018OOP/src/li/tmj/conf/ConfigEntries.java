package li.tmj.conf;

import li.tmj.app.Application;

// TODO automatisch generieren?
public final class ConfigEntries {//implements ComboBoxable { 
	public static final String NAME_DEFAULT="default";
	
	public static String datetimeformat() { return get("datetimeformat"); }
	public static String logfilename() { return get("logfilename"); }
	public static String logfilepath() { return get("logfilepath"); }
	public static String loglevel() { return get("loglevel"); }
	public static String logmetalevel() { return get("logmetalevel"); }
	public static String logmetaformat() { return get("logmetaformat"); }
	public static String logformat() { return get("logformat"); }
	public static String language() { return get("language"); }
	public static String rowcount() { return get("rowcount"); }


	private static String get(String key) { return Application.confMainGet(key); }
	
}
