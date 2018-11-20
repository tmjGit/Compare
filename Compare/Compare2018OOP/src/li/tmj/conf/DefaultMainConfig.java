package li.tmj.conf;

import java.util.Properties;

public final class DefaultMainConfig {
	public static final String NAME = "main";

	private DefaultMainConfig() {	}

	public static Properties create() {
		Properties prop;
		prop = new Properties();
		prop.setProperty("datetimeformat", "yyyy-MM-dd HH:mm");
		prop.setProperty("logfilename", "");
		prop.setProperty("logfilepath", "");
		prop.setProperty("loglevel", "trace");
		prop.setProperty("logmetalevel", "debug");
		prop.setProperty("logmetaformat", "{date:yyyy-MM-dd HH:mm:ss} {level} [{thread}] {class}.{method}: {message}");
		prop.setProperty("logformat", "{date:yyyy-MM-dd HH:mm:ss} {level}: {message}");
		prop.setProperty("language", "deu");
		prop.setProperty("rowcount", "2");
		// prop.put(key, value)
		return prop;
	}

}

