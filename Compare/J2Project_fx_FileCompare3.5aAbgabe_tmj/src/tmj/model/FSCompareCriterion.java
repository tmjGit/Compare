package tmj.model;

import org.pmw.tinylog.Logger;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import tmj.ui.lang.LanguageSet;
import java.io.File;
import java.util.function.Function;

/**
 * FSCompareCriterion representates one compare criterion and the result value.
 * Analog to a table this is one row.
 * It can handle multiple files aka columns and the correcponding value objects aka cells.
 * It also provides quasi static calculations for recursive calls.
 */
/**
 * JavaFX Bean, Listener/Binding geeignet
 * Properties
 */
public abstract class FSCompareCriterion extends FSCompare {
	private FSCompareFileValue[] fileValues;
	private String name; // public final String NAME; // how can this be a constant for the sub class?
	private boolean runsAutomatically;
	private boolean isFileSuitable;
	private boolean isDirectorySuitable;
	
	protected abstract boolean compareConcreteIntern(File[] files,boolean updateValue);
	
	public FSCompareCriterion(FSCompareFileValue[] fileValues,String name,boolean mayRunAutomatically,boolean isFileSuitable,boolean isDirectorySuitable,Function<File,String> function) {
		super();
		this.fileValues=fileValues;
		this.name=name;
		this.runsAutomatically=mayRunAutomatically;
		this.isFileSuitable=isFileSuitable;
		this.isDirectorySuitable=isDirectorySuitable;
		for(int i=0; i<fileValues.length; i++) 
			fileValues[i]=new FSCompareFileValue(function);
	}
	
	public String getName() {
		return name;
	}
	
	public boolean runsAutomatically() {
		return runsAutomatically;
	}
	
	public boolean isFileSuitable() {
		return isFileSuitable;
	}
	
	public boolean isDirectorySuitable() {
		return isDirectorySuitable;
	}
	
	/**
	 * Checks if this CriterionElement is suitable for this file;
	 * e.g. (isDirectorySuitable && file.isDirectory)
	 */
	public boolean isSuitable(File file) {
		return (file!=null && (isDirectorySuitable || !file.isDirectory()) && (isFileSuitable || file.isDirectory()));
	}
	
	public void updateFile(int index,File file) {
		Logger.debug("index={}, file={}",index,file);
    	if (!runsAutomatically())
			return;
		if (file == null) {
			Logger.warn("file=null!");
			setFileValue(index,"");
		}else if(!isSuitable(file))
			setFileValue(index,"");
		else {
			setFileValue(index,file);
		}
	}
	
	public boolean compare(File[] files,boolean updateValue) { // updateValue refers to the value each file's property aka cell
		Logger.trace("updateValue={}, files={}",updateValue,files);
		if (fileValues.length!=files.length)
			Logger.warn("Configuration error: {} fileValues (display fields) but {} files to compair in this criterion ({})! This will probably cause an error, later.",fileValues.length,files.length,name);
		
		if(files.length<2)
			return false; // there is nothing to compare
		if(files[0]==null) { // if the first is null, we return true if all are null.
			for(int i=1; i<files.length; i++)
				if(files[i]!=null)
					return false;
			return true;
		}
		for(int i=1; i<files.length; i++) // We know the first is not null, so all must not be null.
			if(files[i]==null)
				return false;
		for (File f:files)
			if(!isSuitable(f))
				return false;
		boolean b=compareConcreteIntern(files,updateValue);
		setValue( b ? LanguageSet.same() : LanguageSet.different() );
		Logger.trace("return {}",b);
		return b;
	}
	
	public ReadOnlyObjectProperty<String> fileValueProperty(int index) {
		return fileValues[index].getService().valueProperty();
	}

	protected void setFileValue(int index,File f) {
		fileValues[index].getService().setFile(f);
		restartFileValueService(index);
	}
	
	protected void setFileValue(int index,String s) {
		fileValues[index].getService().setDirectValue(s);
		restartFileValueService(index);
	}
	
	private void restartFileValueService(int index) {
		Platform.runLater(()->{
			fileValues[index].getService().restart();
		});
	}
	
}
