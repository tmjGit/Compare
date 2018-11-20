//package li.tmj.conf.new_;
//
//import java.io.BufferedInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//import java.security.KeyException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.NoSuchElementException;
//import java.util.Properties;
//import java.util.ResourceBundle;
//import java.util.function.Function;
//import java.util.function.Predicate;
//import java.util.function.Supplier;
//import java.util.regex.Pattern;
//import org.pmw.tinylog.Logger;
//import li.tmj.app.Application;
//import li.tmj.conf.Config;
//
///**
// * Konfiguration bzgl. eines Kriteriums bspw. Sprache
// * Welche Sprache soll eingestellt sein?
// * Welche Sprachen sollen als Backup (default) fungieren?
// * Welche Sprachen sind als Datei vorhanden?
// * Welche Sprachen wurden bereits geladen.
// * @author TMJ
// *
// */
//public abstract class Configuration<T extends ConfigurationItem> {
//	protected Function<String,String> fileNameToCode;
//	protected ConfigurationItem[] items;//= {new LanguageBAK("en_US")}; //TODO defaultItems und eigentlichen item zusammenfassen in 1 Array?
////	protected ConfigurationItem item=null;
//	protected T prop;
//	protected  final Map<String,ConfigurationItem> foundItems=new HashMap<>();
//	private static final Map<String,ResourceBundle> loadedItems=new HashMap<>();
////	protected Function<String,T> creator;
//	protected Supplier<T> creator;
//	
//
//	private void init() {
//		Logger.trace("init...");
//		File f = new File("src/" + Application.PROP_PATH);
//		String regex=Application.LANG_RESOURCES;
//		
//		generateConfigurationsMap(f,regex);
//	}
//	
////    private ConfigurationItem create(String name) {
////        ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
//////        System.out.println("superclass="+superclass);
//////        Type type = superclass.getActualTypeArguments()[0];
//////        System.out.println("type="+type);
////        Class<T> c=  (Class<T>) superclass.getActualTypeArguments()[0];
//////        System.out.println("c="+c);
////        try {
//////        	ConfigurationItem ci =  c.newInstance();
////        	ConfigurationItem ci =  c.getConstructor( String .class).newInstance(name);
////    		
//////        		System.out.println("ci="+ci+", ci.getClass()="+ci.getClass());
////	        return ci;
////		} catch (InstantiationException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		} catch (IllegalAccessException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		} catch (IllegalArgumentException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		} catch (InvocationTargetException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		} catch (NoSuchMethodException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		} catch (SecurityException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////	    return null;
////    }
//	 
//	 protected void generateConfigurationsMap(File file, String regex){
//		File[] configurationFiles = listConfigurationFiles(file, regex);
//		
//		for (int i = 0; i < configurationFiles.length; i++) {
//			String s=extractName( configurationFiles[i].getName() );
////			try {
////		       Type t= (Class<T>) paramType.getActualTypeArguments()[0];
//		        T t=null;
//		        ConfigurationItem ci=ConfigurationItem.createNew(t.getClass(),"");
////				Class<T> c=T;
//				foundItems.put(s, creator.get() );//foundItems.put(s, new ConfigurationItem(s));
////				foundItems.put(s, create(s) );//foundItems.put(s, new ConfigurationItem(s));
//
////			} catch (InstantiationException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			} catch (IllegalAccessException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			} catch (IllegalArgumentException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			} catch (InvocationTargetException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			} catch (NoSuchMethodException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			} catch (SecurityException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
//		}
//	}
//	
//	public static File[] listConfigurationFiles(File f, String regex){
//		// prop.load(new BufferedInputStream(Configs.class.getResourceAsStream( file )));
//		if (!f.exists()) return null; // TODO exception
//		File[] configurationFiles = f.listFiles((dir, name) -> {
//			Pattern pattern = Pattern.compile(regex, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
//			Predicate<String> p = pattern.asPredicate();
//			return p.test(name);
//		});
//		return configurationFiles;
//	}
//
//	private String extractName(String fileName) {
//		return fileNameToCode.apply(fileName);
//	}
//	
//	public void setItem(ConfigurationItem item) {
//		Logger.trace("item={}",item);
//		if(item==null) // No checking of existance, here. Resources could be added later. Meanwhile a default will be used.
//			Logger.warn("\"null\" is no appropriate item!");
//		else
//			this.item=item;
//	}
//	
//	public void setItem(String item) {
//		Logger.trace("item={}",item);
//		if(item==null || item.equals("")) 
//			Logger.warn("\"{}\" is not an appropriate ConfigurationItem!",item);
//		else
//			this.item=foundItems.get(item);// No checking of existance, here. Resources could be added later. Meanwhile a default will be used.
//	}
//	
//	/**
//	 * The load method will be called automatically, if nesseccary.
//	 * But it is possible to call it in advance, if preferred.
//	 * @return	true on success or if already done.
//	 */
//	public boolean load() {
////		if(!loaded) {// already in RAM
//			if(!reload()) {// tried to load but failed, throwing exception
//				return false;
//			}
////		}
//		return true;//loaded;
//	}
//
//	/**
//	 * In order to reload making sense, a Config object must have 
//	 * an existing file allocated or a working defaultFactory assigned.
//	 * @return	true on success
//	 */
//	public boolean reload()  {
//		if(!hasFile()) {
//			return resetToDefault();
//		}else {//read from file
//			return read();
//		}
//	}
//
//	/**
//	 * Apply internal default configuration.
//	 * @return	true on success
//	 * @throws NoSuchElementException if there is no assigned defaultFactory.
//	 */
//	public boolean resetToDefault() throws NoSuchElementException {
//		if(defaultFactory==null) {
//			Logger.error("This configuration has no default!");
//			throw new NoSuchElementException("This configuration has no default!");
//		}
//		prop=defaultFactory.get();
//		loaded=true;
//		changed=hasFile(); // If no file, this is reload meaning no change. Otherwise override contents.
//		return true;
//	}
//
//	protected boolean read() {
//		Logger.trace("itemname="+item.getName());
//		item.read();
////		getResource(item.getName());
////		prop = new ConfigItem(); 
////		try {
////			Logger.trace("load...");
////			prop.load(new BufferedInputStream(new FileInputStream(file)));
////			loaded=true;
////			changed=false;
////
////		} catch (FileNotFoundException e) {
////			Logger.trace("File not found: {}", file);
////			//TODO file vanished! or changed to a directory rather than a regular file, 
////			// or for some other reason cannot be opened for reading
////			return false;
////		} catch (SecurityException e) {
////			//TODO if a security manager exists and its checkRead method denies read access to the file.
////			Logger.trace(e,"File could not be touched: {}", file);
////			return false;
////		}catch (IOException e){
////			//TODO error occurred when reading from the input stream
////			e.getStackTrace();
////			return false;
////		}catch(IllegalArgumentException e) {
////			//TODO input stream contains a malformed Unicode escape sequence.
////			e.getStackTrace();
////			return false;
////		}
//		return true;
//	}
//
//	
//
//	
//	
//	public void setDefaultItems(String... defaults) {
//		ArrayList<ConfigurationItem> ll=new ArrayList<>();
//		for (String s:defaults) {
//			if(null!=s) {
//				ConfigurationItem l=foundItems.get(s);
//				
//				ll.add(l);
//			}
//		}
//		this.items=(ConfigurationItem[]) ll.toArray();
//	}
//
//}
