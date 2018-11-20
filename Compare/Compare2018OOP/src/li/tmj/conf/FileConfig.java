package li.tmj.conf;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.function.Supplier;
import org.pmw.tinylog.Logger;

import li.tmj.ui.lang.ErrorSet;

public class FileConfig extends Config {
	private File file=null;

	public FileConfig(String name, String relativePath) {
		this(name, relativePath, null);
	}

	public FileConfig(String name, String relativePath, Supplier<Properties> defaultFactory) {
		super(name);
//		this.defaultFactory=defaultFactory;
		URL url = getClass().getResource( relativePath ); // getResource(name) sucht 
		/*
		 * basePath = Project/bin/ =  search path used to load classes
		 * if(name begins with '/') absoluteName = name.substring(1);
		   else absoluteName = <Paketname mit / statt .> + name;
		 * return basePath + absoluteName;  or null if not found.
		 * Throws:NullPointerException - If name is null.
		 */
		File file;
		if (url==null) { // file does not exist
//			hasFile=false;
			// create virtual file
			url = getClass().getResource( "/" );// Project/bin/
			file=new File(url.getPath() + relativePath.substring(1)); // w/o first char which is "/"
//			Logger.warn("File not found: {}", file);
		}else {
			file=new File(url.getPath());
//			hasFile=true;
		}
		init(file, defaultFactory);
	}

	public FileConfig(String name, File file) {
		this(name, file, null);
	}

	public FileConfig(String name, File file, Supplier<Properties> defaultFactory) {
		super(name);
		init(file, defaultFactory);
	}

	private void init(File file, Supplier<Properties> defaultFactory) {
//		Logger.trace("name={}" , getName() );
		this.defaultFactory=defaultFactory;
		this.file=file;
		hasFile=file.exists();
		if (!hasFile) { // file does not exist
			Logger.warn(ErrorSet.configurationFileXdoesNotExist(), file);
		}
//		Logger.trace("name={}, this.file={}, hasFile={}",getName(),this.file,hasFile);
	}
	
	public File getFile() {
		return file;
	}

	protected boolean read() {
		prop = new Properties(); 
		try {
			Logger.trace("read...");
			prop.load(new BufferedInputStream(new FileInputStream(file)));
			loaded=true;
			changed=false;

		} catch (FileNotFoundException e) {
			Logger.trace(ErrorSet.fileNotFoundX(), file);
			//TODO file vanished! or changed to a directory rather than a regular file, 
			// or for some other reason cannot be opened for reading
			return false;
		} catch (SecurityException e) {
			//TODO if a security manager exists and its checkRead method denies read access to the file.
			Logger.trace(e,ErrorSet.fileCouldNotBeTouchedX(), file);
			return false;
		}catch (IOException e){
			//TODO error occurred when reading from the input stream
			e.getStackTrace();
			return false;
		}catch(IllegalArgumentException e) {
			//TODO input stream contains a malformed Unicode escape sequence.
			e.getStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * @return	true if successfully saved
	 * @throws FileNotFoundException - if the file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
	 * @throws SecurityException - if a security manager exists and its checkWrite method denies write access to the file.
	 * @throws IOException - if writing the property list to the specified output stream throws an IOException.
	 * @throws ClassCastException  - if the Properties object contains any keys or values that are not Strings.
	 */
	public boolean save() throws FileNotFoundException, SecurityException,IOException {// throws IOException, URISyntaxException  {
		if(hasFile() && !changed) { // there is nothing new to write
			return false;// TODO should this return false meaning "we did not write" or true meaning "it is all right"?
		} 
		// if not stored, yet, we store it, now.
		if(prop==null) {// there is nothing to write
			return false;
		}
		try {
			prop.store(new FileOutputStream( file ), "");
			//FileNotFoundException - if the file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
			//SecurityException - if a security manager exists and its checkWrite method denies write access to the file.
			//IOException - if writing this property list to the specified output stream throws an IOException.
			//ClassCastException  - if this Properties object contains any keys or values that are not Strings.
			// NullPointerException - if out is null.
			hasFile=true; // Wether we had a file or not, now we have one.
			changed=false; // if successfully stored, reset the flag
			return true;
		}catch(ClassCastException e) {
			Logger.error(e,ErrorSet.configurationContainsObscureValues());
			throw e; 
		}
	}
	
}



//import java.io.BufferedInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.net.URL;
//import java.util.Properties;
//import java.util.function.Supplier;
//
//import org.pmw.tinylog.Logger;
//
//public class FileConfig extends Config {
//	private File file=null;
//
//	public FileConfig(String relativePath) {
//		this(relativePath, null);
//	}
//
//	public FileConfig(String relativePath, Supplier<Properties> defaultFactory) {
//		this.defaultFactory=defaultFactory;
//		URL url = getClass().getResource( relativePath ); // getResource(name) sucht 
//		/*
//		 * basePath = Project/bin/ =  search path used to load classes
//		 * if(name begins with '/') absoluteName = name.substring(1);
//		   else absoluteName = <Paketname mit / statt .> + name;
//		 * return basePath + absoluteName;  or null if not found.
//		 * Throws:NullPointerException - If name is null.
//		 */
//		if (url==null) { // file does not exist
//			hasFile=false;
//			// create virtual file
//			url = getClass().getResource( "/" );// Project/bin/
//			file=new File(url.getPath() + relativePath.substring(1)); // w/o first char which is "/"
//			Logger.warn("File not found: {}", file);
//		}else {
//			file=new File(url.getPath());
//			hasFile=true;
//		}
//	}
//	
//	public File getFile() {
//		return file;
//	}
//
//	protected boolean read() {
//		prop = new Properties(); 
//		try {
//			Logger.trace("load...");
//			prop.load(new BufferedInputStream(new FileInputStream(file)));
//			loaded=true;
//			changed=false;
//
//		} catch (FileNotFoundException e) {
//			Logger.trace("File not found: {}", file);
//			//TODO file vanished! or changed to a directory rather than a regular file, 
//			// or for some other reason cannot be opened for reading
//			return false;
//		} catch (SecurityException e) {
//			//TODO if a security manager exists and its checkRead method denies read access to the file.
//			Logger.trace(e,"File could not be touched: {}", file);
//			return false;
//		}catch (IOException e){
//			//TODO error occurred when reading from the input stream
//			e.getStackTrace();
//			return false;
//		}catch(IllegalArgumentException e) {
//			//TODO input stream contains a malformed Unicode escape sequence.
//			e.getStackTrace();
//			return false;
//		}
//		return true;
//	}
//	
//	/**
//	 * @return	true if successfully saved
//	 * @throws FileNotFoundException - if the file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
//	 * @throws SecurityException - if a security manager exists and its checkWrite method denies write access to the file.
//	 * @throws IOException - if writing the property list to the specified output stream throws an IOException.
//	 * @throws ClassCastException  - if the Properties object contains any keys or values that are not Strings.
//	 */
//	public boolean save() throws FileNotFoundException, SecurityException,IOException {// throws IOException, URISyntaxException  {
//		if(hasFile() && !changed) { // there is nothing new to write
//			return false;// TODO should this return false meaning "we did not write" or true meaning "it is all right"?
//		} 
//		// if not stored, yet, we store it, now.
//		if(prop==null) {// there is nothing to write
//			return false;
//		}
//		try {
//			prop.store(new FileOutputStream( file ), "");
//			//FileNotFoundException - if the file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
//			//SecurityException - if a security manager exists and its checkWrite method denies write access to the file.
//			//IOException - if writing this property list to the specified output stream throws an IOException.
//			//ClassCastException  - if this Properties object contains any keys or values that are not Strings.
//			// NullPointerException - if out is null.
//			hasFile=true; // Wether we had a file or not, now we have one.
//			changed=false; // if successfully stored, reset the flag
//			return true;
//		}catch(ClassCastException e) {
//			Logger.error(e,"Internal error, the configuration seems to contain obscure values.");
//			throw e; 
//		}
//	}
//	
//}
