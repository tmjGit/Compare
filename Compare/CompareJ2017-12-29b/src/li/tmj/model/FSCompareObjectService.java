package li.tmj.model;

import java.io.File;
import java.util.ArrayList;
import org.pmw.tinylog.Logger;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import li.tmj.model.RuntimeException.IllegalRecursionTypeException;

/**
 * FSCompareObject manages the files and compare criteria.
 * So it is like a table where the criteria are the rows and
 * the files are the columns.
 */
public class FSCompareObjectService extends Service<Boolean> {
	private ArrayList<FSCompareCriterion> criteria=null;
	private File[] files=null;
	private boolean compareFull=false;
	private RecursionType recursion;
	private boolean cancel;

	/////////////////////////////////////////////////
	public boolean workInThreads=false; // switch for test cases
	/////////////////////////////////////////////////

	public void setCriteria(ArrayList<FSCompareCriterion> criteria) {
		this.criteria=criteria;
	}
	
	public void setFiles(File[] files) {
		this.files=files;
	}
	
	public void setCompareFull(boolean compareFull) {
		this.compareFull=compareFull;
	}

	public void setRecursion(RecursionType recursion) {
		this.recursion = recursion;
	}
	
	private boolean compare(boolean compareFull) {//compareInternRecursiveNoThread(boolean compareFull) { 
		Logger.trace("recursion={}, compareFull={}, files={}",recursion,compareFull,files.toString());
		boolean compared=true;// final result
		//TODO the matches should be displayed/used outside of this method.
		boolean[] matches=new boolean[criteria.size()]; // stores the separated results of the comparings by the criteria, each. 
		for (int i=0; i < matches.length; i++) { // we check all criteria true or false; ignoring undefined criteria
			if(cancel) return false;
			FSCompareCriterion c=criteria.get(i);
			if(c==null) {
				matches[i]=true; // no criteria so not relevant, we calculate it as true for not destroying the final result.
			}else {
				matches[i]=compareIntern2(c);
				if(!matches[i]) { 
					if(!compareFull) {return false;}
					compared=false; 
				}	
			}
		}
		return compared;
	}
	
	private boolean compareRecursive2(File file1,File file2,FSCompareCriterion c,RecursionType recursion) { //TODO for better performance use java.nio.file.Files, java.nio.file.Path?
		//check contents of file1/file2 recursively
//		if(cancel) return false;
		Logger.trace("Comparing w/ recursion: files={}",files.toString());
//		Path path=file.toPath();
//		Stream<Path> str=Files.list(path);
//		result=str.count();
		
		File[] childs;//File[][] childs=new File[1][]; //File[][] childs=new File[files.length][]; 
//		for (int i=0; i<2; i++) {
			if(cancel) return false;
			childs=file1.listFiles();//childs[i]=files[i].listFiles();
//		}
		for(File f1:childs) {
		//for (File f1:childs[0]) { //TODO make flexibel
			if(cancel) return false;
			File f2=new File(file2,f1.getName());//new File(files[1],f1.getName()); // virtual file from the one folder within the other.
			if(!f2.exists()) { return false; }	// if one file does not exist in both folders, the folders can't be same.
			if(!comparePair2(f1, f2,c,recursion)) { return false; } // the file or folders exists, both, so we compare it.
		}
		childs=file2.listFiles();
		for(File f2:childs) {//for (File f2:childs[1]) {
			if(cancel) {return false;}
			File f1=new File(file1,f2.getName());//new File(files[0],f2.getName()); // virtual file from the one folder within the other.
			if(!f1.exists()) {return false;}//this file does not exist in both folders, so the folders are not identically!
			//if exists, we compared it already.
		}
		return true;
	}
	
	private boolean comparePair2(File file1,File file2,FSCompareCriterion c,RecursionType recursion)  { // part of recursive compare
		if(cancel) return false;
		Logger.trace("Comparing w/ recursion: file1={}, file2={}",file1,file2);
		if(file1.isDirectory()) {
			if(!file2.isDirectory()) {
				return false; // only one of them is directory!
			}else {//both are directories...
				if(recursion==RecursionType.RECURSION_OFF || recursion==RecursionType.RECURSION_ALL || recursion==RecursionType.RECURSION_ALL1 || recursion==RecursionType.RECURSION_DIRS || recursion==RecursionType.RECURSION_DIRS1){
				// ...and the RecursionType orders us to compare those.
				if(!compareSingleNoThread(new File[] {file1,file2},c,false,true)) { return false; } 
				//}else {//...but we don't want to compare directories, this time.
				}
				if(recursion != RecursionType.RECURSION_OFF ) {  // next recursion level
					if(!compareRecursive2(file1,file2,c,recursion)) { return false; }
				}
			}
			
		}else if(file2.isDirectory()) {//only file2 is directory!
			return false;
			
		}else if(recursion==RecursionType.RECURSION_OFF || recursion==RecursionType.RECURSION_ALL || recursion==RecursionType.RECURSION_ALL1 || recursion==RecursionType.RECURSION_FILES) {
			// both are not directories and the RecursionType orders us to compare those.
			if(!compareSingleNoThread(new File[] {file1,file2},c,false,true)) { return false; } 
		}

		return true;// No difference found!
	}

	private boolean compareIntern2(FSCompareCriterion c) {
		//TODO We need detailed results not only of the recuresive final result, but from the comparings, each - and not only when finished, but during process!
	//private boolean compareSingleNoThread(File[] files,boolean updateValue,boolean concrete)  { //comparer all criteria
		Logger.trace("recursion={}, files={}",recursion,files);
//		Logger.trace("recursion={}, concrete={}, updateValue={}, files={}",recursion,concrete,updateValue,files);
//		boolean result=true;
		if(cancel) return false;			
		switch(recursion) {
			case RECURSION_OFF: {// No recursion. Checks only the selected file or folder.
//				return compareSingle(files, true, false);
//				if(workInThreads) {
//					return compareSingleThreads(files, true, false);
//				}else {
				if(!compareSingleNoThread(files, c,true, false)) {
					return false;
				}
//				}
				break;
			}
						
			case RECURSION_FILES: {// Checks recursively but only files: If the selected item is a file, it will be checked but there is effectively no recursion. 
			// Otherwise the algorithm runs through the directory tree and checks all found files.
			// If there is one or more files with no corresponding item to verify with, the summed result will be false.
				if(files[0].isDirectory() && files[1].isDirectory()) { // directories, so start recursion:
					if(!compareRecursive2(files[0],files[1], c,recursion)) { return false; }
				}else if(files[0].isDirectory() || files[1].isDirectory()){// one directory, one not -> false
					return false;
				}else if(!compareSingleNoThread(files, c,true, false)) {// non directories, so compare it
					return false;
				}
				break;
			}
			
			case RECURSION_DIRS: {// Checks recursively but only directories: If the selected item is a file, it does nothing returning false.
			// Otherwise the algorithm runs through the directory tree and checks all found directories by itself and keeps on running through its childs.
			// If there is one or more directories with no corresponding item to verify with, the summed result will be false.
				if(files[0].isFile() || files[1].isFile()) { //RecursionType works only with top level directories
					return false;
				} else { // we found directories so we compare it...
					if(!compareSingleNoThread(files, c,true, false)) { return false; }
					if(!compareRecursive2(files[0],files[1], c,recursion)) { return false; } //...and start recursion
				}
				break;
			}
				
			case RECURSION_ALL: { // Checks recursively all files and directories. If the selected item is a file, it will be checked but there is effectively no recursion.
			// Otherwise the algorithm runs through the directory tree starting with the selected item itself, and checks all found files and directories and keeps on running through the directory's childs.
			// If there is one or more file or directory with no corresponding item to verify with, the summed result will be false.
				if(files[0].isDirectory() != files[1].isDirectory()) { return false; }// one directory, one not -> false
				if(!compareSingleNoThread(files, c,true, false)) { return false; }
				if(files[0].isDirectory()) {//directories, so start recursion:
					if(!compareRecursive2(files[0],files[1], c,recursion)) { return false; } 
				}
				break;
			}
				
			case RECURSION_DIRS1: {// Checks recursively all sub directories: If the selected item is a file, it does nothing returning false.
			// Otherwise the algorithm runs through the directory tree starting with the selected item's childs, and checks all found directories by itself and keeps on running through its childs.
			// If there is one or more directories with no corresponding item to verify with, the summed result will be false.
				//throw IllegalRecursionTypeException.createIllegalRecursionTypeException(recursion); //TODO count dirs, only
				if(!compareRecursive2(files[0],files[1], c,recursion)) {
					return false;
				}
//					boolean b=compareRecursive(files,c);
//					return b;
				break;
			}
				
			case RECURSION_ALL1: {// Checks recursively all containing files and directories. If the selected item is a file, it will be checked but there is effectively no recursion.
			// Otherwise the algorithm runs through the directory tree starting with the selected item's childs, and checks all found files and directories and keeps on running through the directory's childs.
			// If there is one or more file or directory with no corresponding item to verify with, the summed result will be false.
				//throw IllegalRecursionTypeException.createIllegalRecursionTypeException(recursion); //TODO count dirs, only
				if(!compareRecursive2(files[0],files[1], c,recursion)) {
					return false;
				}
//					boolean b=compareRecursive(files,c);
//					return b;
				break;
			}
			
			default: throw IllegalRecursionTypeException.createIllegalRecursionTypeException(recursion); // return false; // we use enums, now, so this can be removed
		}
		
//		if(concrete) {
//			if(!c.compareConcreteIntern(files,updateValue))
//				return false; // if we find one false, than the result is false; ignoring undefined criteria
//		}else {
//			if(!c.compare(files,updateValue))
//				return false; // if we find one false, than the result is false; ignoring undefined criteria						
//		}
	
//		if (c!=null && !c.compare(files,updateValue)) // if we find one false, than the result is false; ignoring undefined criteria
//			return false;

//		Logger.trace("recursion={}",recursion);
		return true;
	}
	
	private boolean compareSingleNoThread(File[] files,FSCompareCriterion c,boolean updateValue,boolean concrete)  { //comparer all criteria
//TODO Algorithmus wirklich korrekt so?
		if(cancel) return false;
		Logger.debug("compareInternSingle w/o threads: concrete={}, updateValue={}, files={}",concrete,updateValue,files);
//		for (FSCompareCriterion c:criteria) {
//			Logger.trace("c={}",c);
//			if(c!=null) {
				if(concrete) {
					if(!c.compareConcreteIntern(files,updateValue)){
						return false; // if we find one false, than the result is false; ignoring undefined criteria
					}
				}else {
					if(!c.compare(files,updateValue)) {
						return false; // if we find one false, than the result is false; ignoring undefined criteria						
					}
				}
//			}
			if ( !c.compare(files,updateValue)) { // if we find one false, than the result is false; ignoring undefined criteria
				return false;
			}
//		}
		return true;
	}
	
	private boolean compareRecursive(File[] files,FSCompareCriterion c) { //TODO for better performance use java.nio.file.Files, java.nio.file.Path?
		if(cancel) return false;
		Logger.trace("Comparing w/ recursion: files={}",files.toString());
//		Path path=file.toPath();
//		Stream<Path> str=Files.list(path);
//		result=str.count();
		
		File[][] childs=new File[files.length][]; 
		for (int i=0; i<2; i++) {
			if(cancel) return false;
			childs[i]=files[i].listFiles();
		}
		for (File f1:childs[0]) { //TODO make flexibel
			if(cancel) return false;
			File f2=new File(files[1],f1.getName()); // virtual file from the one folder within the other.
			if(!f2.exists()) // if one file does not exist in both folders, the folders can't be same.
				return false;
			if(!comparePair(f1, f2,c))
				return false;
		}
		for (File f2:childs[1]) {
			if(cancel) return false;
			File f1=new File(files[0],f2.getName()); // virtual file from the one folder within the other.
			if(!f1.exists())
				return false;
			//if exists, we compared it already.
		}
		return true;
	}
	
	private boolean comparePair(File file1,File file2,FSCompareCriterion c)  { // part of recursive compare
		if(cancel) return false;
		Logger.trace("Comparing w/ recursion: file1={}, file2={}",file1,file2);
		if(file1.isDirectory() && file2.isDirectory())  // next recursion level
			return compareRecursive(new File[] {file1,file2},c);
		if(file1.isDirectory() || file2.isDirectory()) // only one of them
			return false;
		// now compare the two files...	
		return compareSingleNoThread(new File[] {file1,file2},c,false,true); 
	}
	
	private boolean compareSingleThreads(File[] files,boolean updateValue,boolean concrete)  { 
		Logger.debug("compareInternSingle w/ threads: concrete={}, updateValue={}, files={}",concrete,updateValue,files);
		// For each criterion we need a Thread and a Runnable.
		Thread[] tt=new Thread[criteria.size()];
		FunctionRunnable[] rr=new FunctionRunnable[tt.length];
		for (int i=0; i<tt.length; i++) {
			if(cancel) return false;
			FSCompareCriterion c=criteria.get(i);
			if(c==null)
				rr[i]=new FunctionRunnable(null, ff -> true);
			else if(concrete)
				rr[i]=new FunctionRunnable(files, ff -> c.compareConcreteIntern(ff, updateValue));
			else
				rr[i]=new FunctionRunnable(files, ff -> c.compare(ff, updateValue));
			tt[i]=new Thread(rr[i]);
			tt[i].start(); // The Threads are immediately, i.e. work parallel after all has been created.
		}
		for (int i=0; i<tt.length; i++) { // Check the results:
			if(cancel) return false;
			try {
				tt[i].join(); // Before we can get the result, we have to wait the thread to finish.
			} catch (InterruptedException e) {
				Logger.error(e);
				e.printStackTrace();
			}
			if(!rr[i].getValue()) // If one result is false, we need not check the rest.
				return false;
		}
		return true;
	}

	private boolean compareFullIntern() { 
		Logger.trace("recursion={}",recursion);
		if(workInThreads) 
			return compareFullInternThreads();
		else
			return compareFullInternNoThread();
	}

	private boolean compareFullInternNoThread() { 
		boolean compared=true;
		boolean[] matches=new boolean[criteria.size()];
		for (int i=0; i < matches.length; i++) { // we check all criteria true or false; ignoring undefined criteria
			if(cancel) return false;
			FSCompareCriterion c=criteria.get(i);
			if(c==null)
				matches[i]=true;
			else {
				matches[i]=c.compare(files,true);
				if(!matches[i])
					compared=false;	
			}
		}
		return compared;
	}

	private boolean compareFullInternThreads() { 
		Logger.debug("compareFullIntern w/ threads...");
		return compareSingleThreads(files,true,false);
	}
	
	public void stop() {
		Logger.debug("Stopping...");
		cancel=true;
		//TODO cancel sub threads and tell criterion methods to cancel.
	}
	
	public void pause() {//TODO
		Logger.debug("Pausing...not implemented, yet");
//		cancel=true;
		//TODO cancel sub threads and tell criterion methods to cancel.
	}
	
	public void resume() {//TODO
		Logger.debug("Resuming...not implemented, yet");
//		cancel=true;
		//TODO cancel sub threads and tell criterion methods to cancel.
	}
	
	@Override
	protected Task<Boolean> createTask() {
		Task<Boolean> t= new Task<Boolean>() { // compareFullIntern
			@Override
			protected Boolean call() throws Exception {
				Logger.debug("Startet: criteria={}, recursion={}, files={}, compareFull={}",criteria,recursion,files,compareFull);
				cancel=false;
//				updateMessage("Call läuft...");
				boolean b= compare(compareFull);
				Logger.trace("Finished: b={} (criteria={}, recursion={}, files={}, compareFull={})",b,criteria,recursion,files,compareFull);
				return b;
			}
		};
		return t;
	}
	


//	public boolean compare(boolean compareFull) {
//		boolean b;
//		if(compareFull)
//			b=compareFullIntern();
//		else
//			b=compareIntern();
//		return b;
//	}

	
//	private boolean compareIntern() {
//		Logger.trace("recursion={}, files={}",recursion,files);
////		boolean result=true;
//		for (FSCompareCriterion c:criteria) {
//			if(cancel) return false;
//			if(c!=null) {
//				Logger.trace("c={}",c);	
//				if(!compareIntern2(c)) {
//					return false;
//				}
//	//			boolean b=compareRecursive(files,c);
//	//			return b;	
//			}
////			if (c!=null && !c.compare(files,updateValue)) // if we find one false, than the result is false; ignoring undefined criteria
////				return false;
//		}
//		return true;
////		Logger.trace("recursion={}",recursion);
//	
//	}
	
//	private boolean compareInternOLD() {
//		if(cancel) return false;
//		Logger.trace("recursion={}",recursion);
//		switch(recursion) {
//			case RECURSION_OFF: // No recursion. Checks only the selected file or folder.
//				return compareSingleOLD(files, true, false);
//						
//			case RECURSION_FILES: // Checks recursively but only files: If the selected item is a file, it will be checked but there is effectively no recursion. 
//			// Otherwise the algorithm runs through the directory tree and checks all found files.
//			// If there is one or more files with no corresponding item to verify with, the summed result will be false.
//				throw IllegalRecursionTypeException.createIllegalRecursionTypeException(recursion);//TODO count files, only
//			
//			case RECURSION_DIRS: // Checks recursively but only directories: If the selected item is a file, it does nothing returning false.
//			// Otherwise the algorithm runs through the directory tree and checks all found directories by itself and keeps on running through its childs.
//			// If there is one or more directories with no corresponding item to verify with, the summed result will be false.
//				throw IllegalRecursionTypeException.createIllegalRecursionTypeException(recursion); //TODO count dirs, only
//				
//			case RECURSION_ALL: { // Checks recursively all files and directories. If the selected item is a file, it will be checked but there is effectively no recursion.
//			// Otherwise the algorithm runs through the directory tree starting with the selected item itself, and checks all found files and directories and keeps on running through the directory's childs.
//			// If there is one or more file or directory with no corresponding item to verify with, the summed result will be false.
//				boolean b=compareRecursiveOLD(files);
//				return b;
//			}
//				
//			case RECURSION_DIRS1: // Checks recursively all sub directories: If the selected item is a file, it does nothing returning false.
//			// Otherwise the algorithm runs through the directory tree starting with the selected item's childs, and checks all found directories by itself and keeps on running through its childs.
//			// If there is one or more directories with no corresponding item to verify with, the summed result will be false.
//				throw IllegalRecursionTypeException.createIllegalRecursionTypeException(recursion); //TODO count dirs, only
//				
//			case RECURSION_ALL1: {// Checks recursively all containing files and directories. If the selected item is a file, it will be checked but there is effectively no recursion.
//			// Otherwise the algorithm runs through the directory tree starting with the selected item's childs, and checks all found files and directories and keeps on running through the directory's childs.
//			// If there is one or more file or directory with no corresponding item to verify with, the summed result will be false.
//				//throw IllegalRecursionTypeException.createIllegalRecursionTypeException(recursion); //TODO count dirs, only
//				boolean b=compareRecursive1(files);
//				return b;
//			}
//			
//			
//			default: throw IllegalRecursionTypeException.createIllegalRecursionTypeException(recursion); // return false; // we use enums, now, so this can be removed
//		}
//	}
	
//	private boolean compareRecursiveOLD(File[] files) { //TODO for better performance use java.nio.file.Files, java.nio.file.Path?
//		if(cancel) return false;
//		Logger.trace("Comparing w/ recursion: files={}",files.toString());
////		Path path=file.toPath();
////		Stream<Path> str=Files.list(path);
////		result=str.count();
//		
//		File[][] childs=new File[files.length][]; 
//		for (int i=0; i<2; i++) {
//			if(cancel) return false;
//			childs[i]=files[i].listFiles();
//		}
//		for (File f1:childs[0]) { //TODO make flexibel
//			if(cancel) return false;
//			File f2=new File(files[1],f1.getName()); // virtual file from the one folder within the other.
//			if(!f2.exists()) // if one file does not exist in both folders, the folders can't be same.
//				return false;
//			if(!comparePairOLD(f1, f2))
//				return false;
//		}
//		for (File f2:childs[1]) {
//			if(cancel) return false;
//			File f1=new File(files[0],f2.getName()); // virtual file from the one folder within the other.
//			if(!f1.exists())
//				return false;
//			//if exists, we compared it already.
//		}
//		return true;
//	}
//	
//	private boolean comparePairOLD(File file1,File file2)  { // part of recursive compare
//		if(cancel) return false;
//		Logger.trace("Comparing w/ recursion: file1={}, file2={}",file1,file2);
//		if(file1.isDirectory() && file2.isDirectory())  // next recursion level
//			return compareRecursiveOLD(new File[] {file1,file2});
//		if(file1.isDirectory() || file2.isDirectory()) // only one of them
//			return false;
//		// now compare the two files...	
//		return compareSingleOLD(new File[] {file1,file2},false,true); 
//	}
	
//	private boolean compareRecursive1(File[] files) { //TODO for better performance use java.nio.file.Files, java.nio.file.Path?
//		if(cancel) return false;
//		Logger.trace("Comparing w/ recursion: files={}",files.toString());
////		Path path=file.toPath();
////		Stream<Path> str=Files.list(path);
////		result=str.count();
//		
//		File[][] childs=new File[files.length][]; // Arrays to hold the childs
//		for (int i=0; i<2; i++) {
//			if(cancel) return false;
//			childs[i]=files[i].listFiles();
//		}
//		for (File f1:childs[0]) { //TODO make flexibel // compare the childs of one with the other childs
//			if(cancel) return false;
//			File f2=new File(files[1],f1.getName()); // virtual file from the one folder within the other.
//			if(!f2.exists()) // if one file does not exist in both folders, the folders can't be same.
//				return false;
//			if(!comparePair1(f1, f2))
//				return false;
//		}
//		for (File f2:childs[1]) {// compare the other childs with the on, if there are additional items.
//			if(cancel) return false;
//			File f1=new File(files[0],f2.getName()); // virtual file from the one folder within the other.
//			if(!f1.exists()) // This file in the other folder has no counter part in the one folder.
//				return false;
//			//if exists, we compared it already.
//		}
//		return true;
//	}
//	
//	private boolean comparePair1(File file1,File file2)  { // part of recursive compare
//		if(cancel) return false;
//		Logger.trace("Comparing w/ recursion: file1={}, file2={}",file1,file2);
//		if(file1.isDirectory() && file2.isDirectory())  // next recursion level
//			return compareRecursive1(new File[] {file1,file2});
//		if(file1.isDirectory() || file2.isDirectory()) // only one of them
//			return false;
//		// now compare the two files...	
//		return compareSingleOLD(new File[] {file1,file2},false,true); 
//	}
//	
//	private boolean compareSingleOLD(File[] files,boolean updateValue,boolean concrete)  { 
//		if(cancel) return false;
//		Logger.debug("Comparing w/o recursion: concrete={}, updateValue={}, files={}",concrete,updateValue,files);
//		if(workInThreads) 
//			return compareSingleThreads(files, updateValue, concrete);
//		else 
//			return compareSingleNoThreadOLD(files, updateValue, concrete);
//	}
//	
//	private boolean compareSingleNoThreadOLD(File[] files,boolean updateValue,boolean concrete)  { //comparer all criteria
//		if(cancel) return false;
//		Logger.debug("compareInternSingle w/o threads: concrete={}, updateValue={}, files={}",concrete,updateValue,files);
//		for (FSCompareCriterion c:criteria) {
//			Logger.trace("c={}",c);
//			if(c!=null) {
//				if(concrete) {
//					if(!c.compareConcreteIntern(files,updateValue))
//						return false; // if we find one false, than the result is false; ignoring undefined criteria
//				}else {
//					if(!c.compare(files,updateValue))
//						return false; // if we find one false, than the result is false; ignoring undefined criteria						
//				}
//			}
//			if (c!=null && !c.compare(files,updateValue)) // if we find one false, than the result is false; ignoring undefined criteria
//				return false;
//		}
//		return true;
//	}

}
