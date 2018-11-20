package li.tmj.model;

import org.pmw.tinylog.Logger;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/** 
 * FSCompareFileValue representates the value of one criterion
 * of one file. Analog to a table this would be one cell.
 */
public class FSCompareValueService extends Service<String> {
	private String newValue;
	
	public void setNewValue(String newValue) {
		this.newValue=newValue;
	}

	@Override
	protected Task<String> createTask() {
		Task<String> t=new Task<String>() {
			@Override
			protected String call() throws Exception {
				Logger.debug("Updating Value");
				return newValue;
			}
		};
		return t;
	}
}
