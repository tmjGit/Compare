package li.tmj.model;

import java.io.File;
import java.util.function.Function;
import org.pmw.tinylog.Logger;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ReadDisplayValueService extends Service<String >{
	private File file;
	private Function<File,String> function;
	private String directValue=null; // If the value was already calculated by the calling instance, the service can use it.
	
	public ReadDisplayValueService(Function<File,String> function) {
//		Logger.trace("Constructor function={}",function);
		this.function=function;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
		directValue=null;
	}

	public void setDirectValue(String directValue) {
		this.directValue = directValue;
		file=null;
	}
	
	@Override
	protected Task<String> createTask() {
		Task<String> t=new Task<String>() {
			@Override
			protected String call() throws Exception {
				if(directValue!=null) {
					Logger.trace("directValue={}",directValue);
					String string=directValue;
					directValue=null;
					return string;					
				}
				Logger.trace("file={}",file);
				if (file==null)
					return null;
				return function.apply(file);					
			}
		};
		return t;
	}

}
