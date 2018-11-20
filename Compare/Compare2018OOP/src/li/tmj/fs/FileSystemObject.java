package li.tmj.fs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

public class FileSystemObject extends File {
	private static final long serialVersionUID = 2727559932725175793L;
	private File resourceFork=null;

	public FileSystemObject(URI uri) {
		super(uri);
		calculateResourceForkPart();
	}

	public FileSystemObject(String parent, String child) {
		super(parent, child);
		calculateResourceForkPart();
	}

	public FileSystemObject(String pathname) {
		super(pathname);
		calculateResourceForkPart();
	}
	
	public FileSystemObject(File parent, String child) {
		super(parent, child);
		calculateResourceForkPart();
	}
	
	private void calculateResourceForkPart() {
		if(java.lang.System.getProperty(li.tmj.system.System.PROPERTY_OSNAME).equals(li.tmj.system.System.MACOS)) {
			String path=this.getAbsolutePath();
			System.out.println( java.lang.System.getProperty(li.tmj.system.System.PROPERTY_OSVERSION).toString() );
			if(li.tmj.system.System.osVersionComparator().compareTo("10.7")<0) {
				path = path + "/rsrc";
			}else {//since Mac OS X 10.7 (released in 2011), you have to use /..namedfork/rsrc.
				path = path + "/..namedfork/rsrc";
			}
			System.out.println( "path="+path );
			resourceFork=new File(path);
		}
	}
	
	public long dataForkLength() {
		return super.length();
	}
	
	public long resourceForkLength() {
		if(null==resourceFork) {
			return 0;
		}
		return resourceFork.length();
	}
	
	@Override public long length() {
		return super.length()+resourceForkLength();
	}
	
	public boolean hasDataFork() {
		return dataForkLength()>0; // an empty fork is the definition of no fork
	}
	
	public boolean hasResourceFork() {
		return resourceForkLength()>0;// an empty fork is the definition of no fork
	}
	
	public void removeDataFork() throws IOException {
		FileOutputStream out=new FileOutputStream( getAbsoluteFile() );
		out.write(new byte[0]);
		out.close();
		//TODO just set the file to 0. 
	}
	
	public void removeResourceFork() {
		if(hasResourceFork()) {
			resourceFork.delete();
		}
	}
	
	public File getDataFork() {
		return getAbsoluteFile();
	}
	
	public File getResourceFork() {
		if(null==resourceFork) {
			return null;
		}
		return resourceFork.getAbsoluteFile();
	}


//	private File file;
	
//	public FileSystemObject(File file) {
//		this.file=file;
//	}
//	
//	public DataFork() {
//		file.exists();
//		file.isDirectory();
//		file.mkdirs()
//	}
	
	
	//new FileSystemObject( "test1");
	//public String getName() = test1 // no Getter!
	//public String getAbsolutePath() = /Users/tobias/Documents/Docs/develop/files/Compare/CompareJ/test1 // no Getter!
	//public String getPath() = test1 // 
	//public String getCanonicalPath() = /Users/tobias/Documents/Docs/develop/files/Compare/CompareJ/test1 // not a Getter!!
	//public String toString() = test1
	//public Path toPath() = test1
	
	//public String getParent() // not a Getter!!
	//public File getParentFile() // not a Getter!!
	//public boolean isAbsolute()
	//public File getAbsoluteFile() // not a Getter!!
	//public File getCanonicalFile()  // not a Getter!!
	//public URI toURI()
	//public boolean canRead()
	//public boolean canWrite()
	//public boolean exists()
	//public boolean isDirectory()
	//public boolean isFile()
	//public boolean isHidden()
	//public long lastModified()
	//public long length()
	//public boolean createNewFile()
	//public boolean delete()
	//public void deleteOnExit()
	//public String[] list()
	//public String[] list(FilenameFilter filter)
	//public File[] listFiles()
	//public File[] listFiles(FilenameFilter filter)
	//public File[] listFiles(FileFilter filter)
	//public boolean mkdir()
	//public boolean mkdirs()
	//public boolean renameTo(File dest)
	//public boolean setLastModified(long time) // not a Setter!
	//public boolean setReadOnly()  // not a Setter!
	//public boolean setWritable(boolean writable, boolean ownerOnly) // not a Setter!
	//public boolean setWritable(boolean writable) // not a Setter!
	//public boolean setReadable(boolean readable, boolean ownerOnly) // not a Setter!
	//public boolean setReadable(boolean readable) // not a Setter!
	//public boolean setExecutable(boolean executable, boolean ownerOnly)  // no Setter!
	//public boolean setExecutable(boolean executable) // no Setter!
	//public boolean canExecute()
	//public static File[] listRoots()
	//public long getTotalSpace()
	//public long getFreeSpace()
	//public long getUsableSpace()
	//public static File createTempFile(String prefix, String suffix, File directory)
	//public static File createTempFile(String prefix, String suffix)
	//public int compareTo(File pathname)
	//public boolean equals(Object obj) 
	//public int hashCode()

	
	
	public static void main(String[] args) throws IOException  {
		FileSystemObject f;
		f=new FileSystemObject( "/Users/tobias/Downloads/Testdata/xyz.txt");
		System.out.println("f="+f.toString()+"\nlength="+f.length()+"\nDFlength="+f.dataForkLength()+"\nRFlength="+f.resourceForkLength() );
//		f.removeDataFork();
//		System.out.println("f="+f.toString()+"\nlength="+f.length()+"\nDFlength="+f.dataForkLength()+"\nRFlength="+f.resourceForkLength() );
//		System.out.println("f="+f.toString()+"\nexists()="+f.exists()+"\nhasRF="+f.hasResourceFork());
//		f=new FileSystemObject( "/Users/tobias/Downloads/Testdata/2 kein RF.txt");
//		System.out.println("f="+f.toString()+"\nexists()="+f.exists()+"\nhasRF="+f.hasResourceFork());
	}
}

