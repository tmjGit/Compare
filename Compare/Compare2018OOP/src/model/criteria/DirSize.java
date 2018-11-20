package model.criteria;

import java.io.File;
import java.io.FilenameFilter;

import model.FSCompareCriterionElement;
import model.RecursionType;

public class DirSize extends FSCompareCriterionElement {
		public DirSize(boolean recursive) {
			super("Size", recursive ? false : true ,false,true); //In case of recursion, we better turn off runsAutomatically.
			if (recursive)
				recursion=RecursionType.RECURSION_ALL;
			else
				recursion=RecursionType.RECURSION_OFF;
		}

		@Override
		public boolean compareTo(File f) {
			return compare(file, f);
		}

		@Override
		public boolean compare(File f1,File f2) {
			if(f1==null ^ f2==null)
				return false;
			else if(f1==null) // so f2==null either!
				return true;
			else
				return ( determineValueContrete(f1) == determineValueContrete(f2) );
		}
		
		@Override
		protected String determineValueContrete(File file) {
			if (file != null) 
				return Long.toString(determineValueContreteLg(file));
			System.err.println("FileName.determineValueContrete file=null!");
			return null;
		}
		
		protected long determineValueContreteLg(File file) {
			if (file != null) 
				switch(recursion) {
				case RECURSION_OFF: return file.listFiles().length;
				case RECURSION_ALL: return determineValueRecursive(file);
//				case RecursionType.RECURSION_FILES: //TODO count files, only
//				case RecursionType.RECURSION_DIRS: //TODO count dirs, only
				default: return 0; // wrong recursion type
				}
			System.err.println("FileName.determineValueContrete file=null!");
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

		@Override
		public String toString() {
			return "FileName [file=" + file + "]";
		}

	}

