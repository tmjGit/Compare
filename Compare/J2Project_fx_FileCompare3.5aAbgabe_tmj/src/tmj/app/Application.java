package tmj.app;

import tmj.conf.Configs;

public class Application {
	public static final String NAME="FileCompare";
	public static final String DOMAIN="de.tmj";
	public static final String ORGANIZATION="tmj";
	public static final String PROPERTY_FILE = "/properties/" + NAME + ".conf";
	public static final String MAIN_CONF="main";
	public static final String MISSING_RESOURCE="<missing resource>";
	public static final String MISSING_VALUE="<missing value>";
	
	static {
		Configs.create(MAIN_CONF, PROPERTY_FILE);
	}
	
	public static String confMainGet(String key) {
		return Configs.get(MAIN_CONF, key);
	}
	
}

