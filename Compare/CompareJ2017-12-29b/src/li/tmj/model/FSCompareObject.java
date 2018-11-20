package li.tmj.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

import org.pmw.tinylog.Logger;

/**
 * FSCompareObject manages the files and compare criteria.
 * So it is like a table where the criteria are the rows and
 * the files are the columns.
 */
public class FSCompareObject extends FSCompare {
	private ArrayList<FSCompareCriterion> criteria=new ArrayList<>();
	private File[] files=null;
	private FSCompareObjectService service=new FSCompareObjectService();
	private RunningStatus status=RunningStatus.STOPPED;
	private Function<Boolean,Boolean> onSucceeded;
	
	public FSCompareObject() {
		super();
		files=new File[2];//TODO make flexibel!
		service.setOnSucceeded(e -> {
//			Logger.debug("Succeeded: value={}, files={}, e={}",service.getValue(),files.toString(),e);
			setValue(compareResult( service.getValue() ));
			status=RunningStatus.STOPPED;
			if(onSucceeded!=null)
				onSucceeded.apply(false);
		});
	}
	
	public RunningStatus getStatus() {
		return this.status;
	}
	
	public File getFile(int Index) {
		return files[Index];
	}

	public void setFile(int Index,File file) {
		files[Index] = file;
		setChildFile(Index,file);
	}
    
	private void setChildFile(int Index,File file) {
		for (FSCompareCriterion c:criteria)
			if(c!=null)
				c.updateFile(Index,file);
	}
	
	public void setOnSucceeded(Function<Boolean,Boolean> onSucceeded) {
		this.onSucceeded=onSucceeded;
	}
	
	public void stop() {
		service.stop();
	}
    
	public void pause() {
		status=RunningStatus.PAUSED;
		service.pause();
	}
    
	public void resume() {
		status=RunningStatus.RUNNING;
		service.resume();
	}
    
	// returns the Listindex of the new criterion
	public int addCriterion(FSCompareCriterion c) {
		initCriterion(c);
		criteria.add(c);
		return criteria.size()-1;
	}
	
	public void setCriterion(int Index,FSCompareCriterion c) {
		criteria.set(Index, c);
		initCriterion(c);
	}

	public void initCriterion(FSCompareCriterion c) {
		if (c!=null) {
			for(int i=0; i<files.length; i++)
				c.updateFile(i,files[i]);
		}
	}
	
	public void compare(boolean compareFull) {//TODO Testversion
		Logger.trace("compare as service...");
		service.setCriteria(criteria);
		service.setRecursion(recursion);
		service.setFiles(files);
		service.setCompareFull(compareFull);
		status=RunningStatus.RUNNING;
		service.restart();
	}

	@Override
	public String toString() {
		return "FSCompareObject [criteria=" + criteria + ", files=" + Arrays.toString(files) + "]";
	}

}
