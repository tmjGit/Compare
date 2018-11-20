package tmj.model;

import java.io.File;
import java.util.function.Function;

import org.pmw.tinylog.Logger;

/** 
 * FSCompareFileValue representates the value of one criterion
 * of one file. Analog to a table this would be one cell.
 */
public class FSCompareFileValue extends FSCompare {
	private ReadDisplayValueService service;
	
	public FSCompareFileValue(Function<File,String> function) {
		Logger.trace("Konstruktor...");
		service=new ReadDisplayValueService(function);
	}

	public ReadDisplayValueService getService() {
		return service;
	}
}