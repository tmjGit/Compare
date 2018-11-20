package tmj.model;

import java.io.File;
import java.util.ArrayList;
import org.pmw.tinylog.Logger;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import tmj.model.RuntimeException.IllegalRecursionTypeException;

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
	public boolean workInThreads=true; // switch for test cases
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

	public boolean compare(boolean compareFull) {
		boolean b;
		if(compareFull)
			b=compareFullIntern();
		else
			b=compareIntern();
		return b;
	}
	
	private boolean compareIntern() {
		if(cancel) return false;
		Logger.trace("recursion={}",recursion);
		switch(recursion) {
			case RECURSION_OFF: return compareSingle(files, true, false);
			case RECURSION_ALL: {
				boolean b=compareRecursive(files);
				return b;
			}
			case RECURSION_FILES: throw IllegalRecursionTypeException.createIllegalRecursionTypeException(recursion);//TODO count files, only
			case RECURSION_DIRS: throw IllegalRecursionTypeException.createIllegalRecursionTypeException(recursion); //TODO count dirs, only
			default: throw IllegalRecursionTypeException.createIllegalRecursionTypeException(recursion); // return false; // we use enums, now, so this can be removed
		}
	}

	private boolean compareRecursive(File[] files) { //TODO for better performance use java.nio.file.Files, java.nio.file.Path?
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
			if(!comparePair(f1, f2))
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
	
	private boolean comparePair(File file1,File file2)  { // part of recursive compare
		if(cancel) return false;
		Logger.trace("Comparing w/ recursion: file1={}, file2={}",file1,file2);
		if(file1.isDirectory() && file2.isDirectory())  // next recursion level
			return compareRecursive(new File[] {file1,file2});
		if(file1.isDirectory() || file2.isDirectory()) // only one of them
			return false;
		// now compare the two files...	
		return compareSingle(new File[] {file1,file2},false,true); 
	}
	
	private boolean compareSingle(File[] files,boolean updateValue,boolean concrete)  { 
		if(cancel) return false;
		Logger.debug("Comparing w/o recursion: concrete={}, updateValue={}, files={}",concrete,updateValue,files);
		if(workInThreads) 
			return compareSingleThreads(files, updateValue, concrete);
		else 
			return compareSingleNoThread(files, updateValue, concrete);
	}
	
	private boolean compareSingleNoThread(File[] files,boolean updateValue,boolean concrete)  { 
		if(cancel) return false;
		Logger.debug("compareInternSingle w/o threads: concrete={}, updateValue={}, files={}",concrete,updateValue,files);
		for (FSCompareCriterion c:criteria) {
			Logger.trace("c={}",c);
			if(c!=null) {
				if(concrete) {
					if(c.compareConcreteIntern(files,updateValue))
						return false; // if we find one false, than the result is false; ignoring undefined criteria
				}else {
					if(c.compare(files,updateValue))
						return false; // if we find one false, than the result is false; ignoring undefined criteria						
				}
			}
			if (c!=null && !c.compare(files,updateValue)) // if we find one false, than the result is false; ignoring undefined criteria
				return false;
		}
		return true;
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

}
