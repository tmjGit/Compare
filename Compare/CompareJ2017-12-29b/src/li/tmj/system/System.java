package li.tmj.system;

import java.io.IOException;
import li.tmj.app.Application;
import li.tmj.system.Windows;
import li.tmj.ui.lang.Localization;

import org.pmw.tinylog.Logger;

public class System {
	private static final String PROPERTY_HOME = "user.home";
	private static final String WINDOWS = "Windows";
	private static final String PROPERTY_OSNAME = "os.name";
	private static final String MACOS = "Mac OS X";
	private static final String FILESEPARATOR;

	static {
		FILESEPARATOR=java.lang.System.getProperty("file.separator");
	}
	
	public static String standardLogPath() {
		if(java.lang.System.getProperty(PROPERTY_OSNAME).equals(MACOS)) {
			return java.lang.System.getProperty(PROPERTY_HOME)+FILESEPARATOR+"Library"+FILESEPARATOR+"Logs"+FILESEPARATOR;
			
		}else if ( java.lang.System.getProperty(PROPERTY_OSNAME).substring(0, 6).equals(WINDOWS) ) {
//			if (com.sun.jna.Platform.isWindows()) {
//			C:\ProgramData\MyCompany // XP
//			AppData\*\Microsoft\(app name),
//	  	 	organization\app convention 
			try {
				return Windows.getLocalAppData()+FILESEPARATOR+Application.ORGANIZATION+FILESEPARATOR;
			} catch (IOException e) {
				Logger.error(Localization.get("CouldnotgetWindowspathLOCALAPPDATAforthelogfile")+" "+e.fillInStackTrace());
			}
			return "";
			
		}else
			return "";
	}
}
