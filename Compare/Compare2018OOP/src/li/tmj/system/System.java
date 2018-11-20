package li.tmj.system;

import java.io.IOException;
import java.util.Comparator;

import li.tmj.app.Application;
import li.tmj.system.Windows;
import li.tmj.ui.lang.ErrorSet;
import li.tmj.ui.lang.LanguageSet;
import li.tmj.ui.lang.Localization;

import org.pmw.tinylog.Logger;

public class System {
	public static final String PROPERTY_HOME = "user.home";
	public static final String PROPERTY_OSNAME = "os.name";
	public static final String PROPERTY_OSVERSION = "os.version";
	public static final String WINDOWS = "Windows";
	public static final String MACOS = "Mac OS X";
	public static final String FILESEPARATOR;

	static {
		FILESEPARATOR=java.lang.System.getProperty("file.separator");
	}
	
	public static OsVersionComparator osVersionComparator() {
		return new OsVersionComparator(java.lang.System.getProperty(PROPERTY_OSVERSION));
	}
	
	public static class OsVersionComparator implements Comparator<String>{
		private String versionString;
		
		public OsVersionComparator(String versionString) {
			this.versionString=versionString;
		}

		@Override
		public int compare(String o1, String o2) {
			String[] oo1,oo2;
			oo1=o1.split(".");
			oo2=o2.split(".");
			int i=0;
			while(i<oo1.length || i<oo2.length) {//for(int i=0;i<oo1.length;i++) {
				int j1,j2;
				if(i<oo1.length) {
					j1=Integer.parseInt(oo1[i]);
				}else {
					j1=0;
				}
				if(i<oo2.length) {
					j2=Integer.parseInt(oo2[i]);
				}else {
					j2=0;
				}
				if(j1<j2) {
					return -1;//smaller than
				}else if(j1>j2) {
					return 1; // greater than
				}//else equal > next digit
			}
			return 0; // equals
		}
		
		public int compareTo(String versionString) {
			return compare(this.versionString,versionString);
		}
		
	}
	
	public static String standardLogPath() {
		if(java.lang.System.getProperty(PROPERTY_OSNAME).equals(MACOS)) {
			return java.lang.System.getProperty(PROPERTY_HOME)+FILESEPARATOR+"Library"+FILESEPARATOR+"Logs"+FILESEPARATOR;
			
		}else if ( java.lang.System.getProperty(PROPERTY_OSNAME).startsWith(WINDOWS) ) {
//			if (com.sun.jna.Platform.isWindows()) {
//			C:\ProgramData\MyCompany // XP
//			AppData\*\Microsoft\(app name),
//	  	 	organization\app convention 
			try {
				return Windows.getLocalAppData()+FILESEPARATOR+Application.ORGANIZATION+FILESEPARATOR;
			} catch (IOException e) {
				Logger.error(e,ErrorSet.couldNotGetWindowsPathLOCALAPPDATAforLogfile());
			}
			return "";
			
		}else
			return "";
	}
}


//os name   os arch   java version
//Mac OS X   i386   1.5.0_05
//Mac OS X   i386   1.5.0_06
//Mac OS X   ppc   1.5.0_02
//Mac OS X   ppc   1.4.2_05
//Mac OS X   ppc   1.4.2_03
//Mac OS X   ppc   1.4.2_07
//Mac OS X   ppc   1.4.2_09
//Mac OS X   ppc   1.5.0_05
//Mac OS X   ppc   1.5.0_06
//Linux   i386   1.5.0_02
//Linux   i386   1.5.0
//Linux   i386   1.5.0_03
//Linux   i386   1.4.2_06
//Linux   i386   1.5.0_01
//Linux   i386   1.4.2_05
//Linux   i386   1.4.2_08
//Linux   i386   1.6.0-ea
//Linux   i386   1.4.2_03
//Linux   i386   1.4.2_07
//Linux   i386   1.4.2_02
//Linux   i386   1.4.2-beta
//Linux   i386   1.4.2
//Linux   i386   1.4.2_04
//Linux   i386   1.4.2-01
//Linux   i386   1.5.0-rc
//Linux   i386   1.4.2-rc1
//Linux   i386   1.5.0_04
//Linux   i386   1.4.2_09
//Linux   i386   1.5.0_05
//Linux   i386   1.6.0-beta
//Linux   i386   1.6.0-rc
//Linux   i386   1.4.2_10
//Linux   i386   1.5.0_06
//Linux   i386   1.6.0-beta2
//Linux   i386   1.4.2_11
//Linux   i386   1.5.0_07
//Linux   amd64   1.4.2-01
//Linux   amd64   1.5.0_05
//SunOS   x86   1.5.0_04
//SunOS   x86   1.5.0_06
//SunOS   sparc   1.5.0_02
//SunOS   sparc   1.4.2_04
//SunOS   sparc   1.5.0-beta2
//SunOS   sparc   1.5.0_05
//SunOS   sparc   1.5.0_06
//FreeBSD   i386   1.4.2-p6
//FreeBSD   i386   1.4.2-p7
//Windows XP   x86   1.5.0_02
//Windows XP   x86   1.5.0
//Windows XP   x86   1.5.0_03
//Windows XP   x86   1.4.2_06
//Windows XP   x86   1.5.0_01
//Windows XP   x86   1.4.2_05
//Windows XP   x86   1.4.2_08
//Windows XP   x86   1.6.0-ea
//Windows XP   x86   1.4.2_03
//Windows XP   x86   1.4.2_07
//Windows XP   x86   1.4.2_02
//Windows XP   x86   1.4.2
//Windows XP   x86   1.4.2_04
//Windows XP   x86   1.5.0-beta2
//Windows XP   x86   1.4.1_02
//Windows XP   x86   1.5.0-rc
//Windows XP   x86   1.4.1
//Windows XP   x86   1.4.0
//Windows XP   x86   1.6.0
//Windows XP   x86   1.4.2_01
//Windows XP   x86   1.5.0_04
//Windows XP   x86   1.5.0-beta
//Windows XP   x86   1.4.1_01
//Windows XP   x86   1.4.2_09
//Windows XP   x86   1.5.0_05
//Windows XP   x86   1.4.1_03
//Windows XP   x86   1.6.0-beta
//Windows XP   x86   1.6.0-rc
//Windows XP   x86   1.4.2_10
//Windows XP   x86   1.5.0_06
//Windows XP   x86   1.4.0_03
//Windows XP   x86   1.6.0-beta2
//Windows XP   x86   1.4.2_11
//Windows XP   x86   1.5.0_07
//Windows 2003   x86   1.5.0_02
//Windows 2003   x86   1.5.0
//Windows 2003   x86   1.5.0_03
//Windows 2003   x86   1.4.2_06
//Windows 2003   x86   1.5.0_01
//Windows 2003   x86   1.4.2_05
//Windows 2003   x86   1.6.0-ea
//Windows 2003   x86   1.4.2_07
//Windows 2003   x86   1.4.2_04
//Windows 2003   x86   1.5.0-rc
//Windows 2003   x86   1.5.0_04
//Windows 2003   x86   1.5.0-beta
//Windows 2003   x86   1.5.0_05
//Windows 2003   x86   1.6.0-rc
//Windows 2003   x86   1.5.0_06
//Windows 2003   x86   1.5.0_07
//Windows 2000   x86   1.5.0_02
//Windows 2000   x86   1.5.0
//Windows 2000   x86   1.5.0_03
//Windows 2000   x86   1.4.2_06
//Windows 2000   x86   1.5.0_01
//Windows 2000   x86   1.4.2_05
//Windows 2000   x86   1.4.2_08
//Windows 2000   x86   1.6.0-ea
//Windows 2000   x86   1.4.2_03
//Windows 2000   x86   1.4.2_07
//Windows 2000   x86   1.4.2_02
//Windows 2000   x86   1.4.2-beta
//Windows 2000   x86   1.4.2
//Windows 2000   x86   1.4.2_04
//Windows 2000   x86   1.5.0-beta2
//Windows 2000   x86   1.4.1_02
//Windows 2000   x86   1.5.0-rc
//Windows 2000   x86   1.4.1
//Windows 2000   x86   1.4.2_01
//Windows 2000   x86   1.5.0_04
//Windows 2000   x86   1.4.2_09
//Windows 2000   x86   1.5.0_05
//Windows 2000   x86   1.6.0-beta
//Windows 2000   x86   1.6.0-rc
//Windows 2000   x86   1.4.2_10
//Windows 2000   x86   1.5.0_06
//Windows 2000   x86   1.6.0-beta2
//Windows 2000   x86   1.5.0_07
//Windows 2000   x86   1.4.1_07
//Windows 98   x86   1.5.0_03
//Windows 98   x86   1.4.2_06
//Windows 98   x86   1.5.0_01
//Windows 98   x86   1.4.2_02
//Windows 98   x86   1.4.0
//Windows 98   x86   1.4.2_01
//Windows NT   x86   1.5.0_02
//Windows NT   x86   1.5.0
//Windows NT   x86   1.4.2_05
//Windows NT   x86   1.4.2_08
//Windows NT   x86   1.4.2_03
//Windows Me   x86   1.5.0_04
//Windows Me   x86   1.5.0_06