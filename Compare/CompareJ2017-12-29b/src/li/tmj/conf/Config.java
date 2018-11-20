package li.tmj.conf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyException;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.function.Supplier;
import org.pmw.tinylog.Logger;
import li.tmj.app.Application;
import li.tmj.ui.controls.ComboBoxable;
import li.tmj.ui.lang.ErrorSet;
import li.tmj.ui.lang.Localization;

public abstract class Config implements ComboBoxable {
	private String name;
	protected Properties prop;
	protected Supplier<Properties> defaultFactory;
	protected boolean loaded=false;
	protected boolean changed=false;
	protected boolean hasFile=false;
	protected Config[] fallbacks=null;
	private Configs missingValueDefinition=Configs.CURRENT_IDENTIFIER;
	
	/**
	 * Read the configuration from the assigned file.
	 * @return	true on success.
	 */
	protected abstract boolean read() ;
	
	public Config(String name) {
		this.name=name;
	}
	
	public String getName() {
		return name;
	}

	public boolean hasChanged() {
		return changed;
	}
	
	public boolean hasFile() {
		return hasFile;
	}
	
	public Configs getMissingValueDefinition() {
		return missingValueDefinition;
	}

	public void setMissingValueDefinition(Configs missingValueDefinition) {
		this.missingValueDefinition = missingValueDefinition;
	}

	public String missingValue(String key) {
		switch(missingValueDefinition) {
			case APPLICATION_DEFAULTS: return Application.MISSING_VALUE;
			case CURRENT_IDENTIFIER: return key;
			case EMPTY_STRING: return "";
			case EXCEPTION: // falling through:
			default: throw new NoSuchElementException();
		}
	}

	/**
	 * The load method will be called automatically, if nesseccary.
	 * But it is possible to call it in advance, if preferred.
	 * @return	true on success or if already done.
	 */
	public boolean load() {
		if(!loaded) {// already in RAM
			if(!reload()) {// tried to load but failed, throwing exception
				return false;
			}
		}
		return loaded;
	}

	public String get(String key) throws KeyException, FileNotFoundException, IOException {
		return getWithFallbacks(key, fallbacks);
	}
	
	/* Varianten:
	 * defaultConfigs ist null -> use new Config[]{} // unwahrscheinlich aber möglich.
	 */
	public String getWithFallbacks(String key,Config... fallbackConfigs) throws KeyException, FileNotFoundException, IOException {
		Logger.debug("key={}",key);
		Config[] fallbacks=fallbackConfigs;
		if (fallbacks==null) {// falls null übergeben wurde, ist kein default gewünscht ->
			fallbacks=new Config[] {}; // ggf. leeres Array = kein default, z. B. zur Vermeidung von Rekursion
		}			
		
		if(key==null || key.equals("")) {
			throw new KeyException("key="+key);
		}
		if(!load()) { // tried to load but failed, throwing exception
			return null;
		}
		if(prop.containsKey(key)) {
			return prop.getProperty(key);
		}
		if(null!=fallbacks) for (Config c:fallbacks) {
			if( null!=c) {
				try {
					return c.getWithFallbacks(key, new Config[] {}); // no recursion!
				
				} catch (NoSuchElementException e) { // key also not found in this fallback config.
					// try next
				} catch (FileNotFoundException e) { // this fallback config has no existing file.
					// try next
//					} catch (IOException e) { // don't catch this exception, because it is a general one.
				}
			}
		}
		Logger.warn(ErrorSet.keyNotFoundInConfiguration(),key,name);
		return missingValue(key);
	}
	
	public void setFallbacks(Config... fallbacks) {
		this.fallbacks=fallbacks;
		for(Config c:fallbacks) {
			c.setMissingValueDefinition(Configs.EXCEPTION);//In case of missing value, fallbacks must throw an exception, for property working fallback mechanism!
		}
	}

	public boolean set(String key,String value) {// throws KeyException, FileNotFoundException, IOException {
		if(!load()) // tried to load but failed, throwing exception
			return false; // abort
		String s=(String) prop.setProperty(key, value); // if it did not exist before, it evaluates to null.
		changed = s==null || !s.equals(value); // was it really a new value?
		return true; // successfull
	}
	
	/**
	 * In order to reload making sense, a Config object must have 
	 * an existing file allocated or a working defaultFactory assigned.
	 * @return	true on success
	 */
	public boolean reload()  {
		if(!hasFile()) {
			return resetToDefault();
		}else {//read from file
			return read();
		}
	}

	/**
	 * Apply internal default configuration.
	 * @return	true on success
	 * @throws NoSuchElementException if there is no assigned defaultFactory.
	 */
	public boolean resetToDefault() throws NoSuchElementException {
		if(defaultFactory==null) {
			Logger.error(ErrorSet.configurationHasNoDefault(),name);
			throw new NoSuchElementException(String.format(ErrorSet.configurationXhasNoDefault(),name));
		}
		prop=defaultFactory.get();
		loaded=true;
		changed=hasFile(); // If no file, this is reload meaning no change. Otherwise override contents.
		return true;
	}

	@Override
	public String getDisplayName() {
		return Localization.get(name);
	}

	@Override
	public String toString() {
		return "Config [name=" + name + "]";
	}
}


