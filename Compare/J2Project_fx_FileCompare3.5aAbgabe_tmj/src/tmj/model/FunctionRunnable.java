package tmj.model;

import java.io.File;
import java.util.function.Function;

public class FunctionRunnable implements Runnable {
	private boolean value;
	File[] files;
	Function<File[],Boolean> function;
	
	public FunctionRunnable(File[] files, Function<File[],Boolean> function) {
		this.files=files;
		this.function=function;
	}
	
	public boolean getValue() {
		return value;
	}

	@Override
	public void run() {
		value=function.apply(files);
	}

}
