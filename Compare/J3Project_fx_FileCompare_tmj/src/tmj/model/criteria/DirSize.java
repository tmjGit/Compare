package tmj.model.criteria;

import java.io.File;
import java.io.FilenameFilter;
import org.pmw.tinylog.Logger;
import tmj.model.FSCompareCriterion;
import tmj.model.FSCompareFileValue;
import tmj.model.RecursionType;

public class DirSize extends FSCompareCriterion {
		public DirSize(FSCompareFileValue[] criterionValues,boolean recursive) { // The recursion in this class defines a criterion type and is independend of the super classes own recursion value.
			 //In case of recursion, we better turn off runsAutomatically.
			super(criterionValues,"Size", !recursive, false,true, f -> readDisplayValueStatic(f, recursive ? RecursionType.RECURSION_ALL : RecursionType.RECURSION_OFF) );
			if (recursive)
				recursion=RecursionType.RECURSION_ALL;
			else
				recursion=RecursionType.RECURSION_OFF;
		}

		@Override
		public boolean compareConcreteIntern(File[] files,boolean updateValue) { // make flexible
			Logger.trace("compareConcreteIntern...");
//			if(files.length<2) // super class checked this
//				return false;
//			if (files[0]==null ^ files[1]==null)
//				return false;
//			if(files[0]==null)
//				return true;		
			long[] l=new long[files.length];
			for (int i=0; i<files.length; i++) {
				l[i]=readValueIntern(files[i],recursion);
				if (updateValue) {
					setFileValue(i, Long.toString(l[i]));
				}
			}
			boolean result=l[0]==l[1];
			return result;
		}

		private static String readDisplayValueStatic(File file,RecursionType recursion) {
			return Long.toString(readValueIntern(file,recursion));
		}
		
		private static long readValueIntern(File file,RecursionType recursion) {
			if (file != null) 
				switch(recursion) {
					case RECURSION_OFF: return file.listFiles().length;
					case RECURSION_ALL: return determineValueRecursive(file);
//					case RecursionType.RECURSION_FILES: //TODO count files, only
//					case RecursionType.RECURSION_DIRS: //TODO count dirs, only
					default: return 0; // wrong recursion type
				}
			Logger.warn("file=null!");
			return 0;
		}

		private static long determineValueRecursive(File file) { //TODO for better performance use java.nio.file.Files, java.nio.file.Path?
			long result=file.listFiles().length;
//			Path path=file.toPath();
//			Stream<Path> str=Files.list(path);
//			result=str.count();
			File[] subdirs = file.listFiles(new FilenameFilter() {
				@Override public boolean accept(File f, String name) {
				    return new File(f, name).isDirectory();
				  }
			});
			for (File f:subdirs) {
				result += determineValueRecursive(f);
			}
			return result;
		}

	}

