package li.tmj.app;

import li.tmj.conf.Configs;
import li.tmj.log.Log;
import li.tmj.ui.lang.ErrorSet;
import org.pmw.tinylog.Logger;

public class Application {
	public static final String NAME="FileCompare";
	public static final String DOMAIN="li.tmj";
	public static final String ORGANIZATION="tmj";
	public static final String PROP_PATH = "properties/";
	public static final String PROP_SUFFIX = "properties";
	public static final String PROP_SUFFIX2 = "conf";
	public static final String LANG_BASENAME = "Messages";
	public static final String LANG_RESOURCES= LANG_BASENAME + "(_[^.]*)?(\\."+PROP_SUFFIX+"|\\."+PROP_SUFFIX+"|\\.|)";
	public static final String PROPERTY_FILE = "/" +PROP_PATH+ NAME + ".conf";
	public static final String MAIN_CONF="main";
	public static final String MISSING_RESOURCE;
	public static final String MISSING_VALUE;
	
	static {
		Configs.setMissingResourceDefinition(Configs.EXCEPTION); // if a non existing resource is called, throw exception rather than return a special value
		Configs.create(MAIN_CONF, PROPERTY_FILE, true);
		Configs.get(MAIN_CONF).setMissingValueDefinition(Configs.APPLICATION_DEFAULTS);
		MISSING_RESOURCE=ErrorSet.missingResource();
		MISSING_VALUE=ErrorSet.missingValue();
	}
	
	public static void init() {
		Log.init();
	}
	
	public static String confMainGet(String key) {
		Logger.trace("Application.confMainGet: key={}",key);
		return Configs.get(MAIN_CONF, key);
	}
	
}

