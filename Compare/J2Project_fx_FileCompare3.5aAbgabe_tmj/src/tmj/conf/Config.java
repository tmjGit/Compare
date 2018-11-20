package tmj.conf;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyException;
import java.util.NoSuchElementException;
import java.util.Properties;
import org.pmw.tinylog.Logger;

public class Config {
		private Properties prop;
		private String file;
		private boolean loaded=false;

		public Config(String file) {
			this.file=file;
		}

		public boolean load() throws FileNotFoundException, IOException {
			prop = new Properties(); 
			try {
				Logger.trace("load...");
				prop.load(new BufferedInputStream(Configs.class.getResourceAsStream( file ))); 
				loaded=true;

			} catch (FileNotFoundException e) {
				Logger.trace("File not found: {}", file);
			} catch (IOException e) {
				Logger.trace(e,"File could not be touched: {}.", file);
			}
			return loaded;
		}

		public String get(String key) throws KeyException, FileNotFoundException, IOException {
			if(!loaded) // already in RAM
				if(!load()) // tried to load but failed, throwing exception
			if(!prop.containsKey(key))
				throw new NoSuchElementException();
			return prop.getProperty(key);
		}

	}
