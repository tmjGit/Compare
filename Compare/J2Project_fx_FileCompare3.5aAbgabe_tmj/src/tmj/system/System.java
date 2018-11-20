package tmj.system;

import java.io.IOException;
import tmj.app.Application;
import tmj.system.Windows;
import org.pmw.tinylog.Logger;

public class System {
	public static String standardLogPath() {
		String s=java.lang.System.getProperty("file.separator");
		if(java.lang.System.getProperty("os.name").equals("Mac OS X")) {
			return java.lang.System.getProperty("user.home")+s+"Library"+s+"Logs"+s;
			
		}else if ( java.lang.System.getProperty("os.name").substring(0, 6).equals("Windows") ) {
//			if (com.sun.jna.Platform.isWindows()) {
//			C:\ProgramData\MyCompany // XP
//			AppData\*\Microsoft\(app name),
//	  	 	organization\app convention 
			try {
				return Windows.getLocalAppData()+s+Application.ORGANIZATION+s;
			} catch (IOException e) {
				Logger.error("Could not get Windows' path %LOCALAPPDATA% for the log file. "+e.fillInStackTrace());
			}
			return "";
			
		}else
			return "";
	}
}
