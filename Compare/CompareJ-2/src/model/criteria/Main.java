package model.criteria;

import java.io.File;

public class Main {

	public static void main(String[] args) {
		File f=new File("/Users/tobias/Documents/Docs/develop/files/iCompare/Testdaten/ResTest/test1");
		System.out.println("f="+f+", exists="+f.exists());
		File f1=rfFile(f,"/rsrc");
		System.out.println("f1="+f1+", exists="+f1.exists());
		f1=rfFile(f,"/..namedfork/rsrc");
		System.out.println("f1="+f1+", exists="+f1.exists());
		
		

	}
	
	
	
	// Eigene File Klasse schreiben, die ggf. Resourcen und Mac-Pakete erkennt/berücksichtigt?
	
	
	
	
	
	public static File rfFile(File f, String s) {
//		String s="/rsrc";
//		String s="/..namedfork/rsrc";
		File file=new File( f.getAbsolutePath() + s);
		
		//-e zeige ACL-InhaltMac   OS   X	nice		Programm mit veränderter Priorität starte
		//Information über Resource Fork einer Datei zeigen:
	//<Dateiname>/rsrc	OS X (älter)
	//<Dateiname>/..namedfork/rsrc	OS X (neuer), 10.12.6
	
		return file;//TOO
	}
	
}



//You can use:
//
//System.getProperty("os.name")
//P.S. You may find this code useful:
//
//class ShowProperties {
//    public static void main(String[] args) {
//        System.getProperties().list(System.out);
//    }
//}
//All it does is print out all the properties provided by your Java implementations. It'll give you an idea of what you can find out about your Java environment via properties. :-)

//My docs would probably best be handled by accessing:
//
//System.getProperty("user.home");
//Look up the docs on System.getProperty.


//
//Any information you can get about the user's environment can be fetched from
//
//System.getProperty("...");
//For a list of what you can get, take a look here: http://mgrand.home.mindspring.com/java-system-properties.htm
//
//I don't think you'll be able to get the path you require (the All Users path) in an OS dependent way. After all - do other operating systems have an equivalent? Your best bet is to probably inspect:
//
//System.getProperty("os.name");
//to see if you are running Windows and then if so use "C:\Documents & Settings\All Users\".
//
//But you'll be better off just constantly using
//
//System.getProperty("user.home");
//(as mentioned by other people) throughout the application. Or alternatively, allow the user to specify the directory to store whatever it is you want to store.