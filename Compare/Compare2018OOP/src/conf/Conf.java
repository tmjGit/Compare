package conf;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Hilfsklasse nur um die Properties zu laden.
 */
public final class Conf { // kann nicht vererbt werden

	public final static String PROPERTY_PATH = "/properties/conf.properties";// TODO flexibler machen

	private static Properties conf;

	private Conf(){} // kann nicht instanziiert werden.

	static {// statischer "Konstruktor" (wird beim Laden der Klasse aufegrufen)
		conf = new Properties(); // evtl. besser als Singleton

		try {
			conf.load(new BufferedInputStream(Conf.class.getResourceAsStream(PROPERTY_PATH)));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String get(String key) {// Conf.get("datetimeformat")
		return conf.getProperty(key);
	}

}
