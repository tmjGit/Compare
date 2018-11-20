package model.criteria;

import java.io.File;
import model.FSCompareCriterionElement;

public class FileName extends FSCompareCriterionElement {
	public FileName() {
		super("Name",true,true,true); 
	}

	@Override
	public boolean compareTo(File f) {
		return compare(file, f);
	}

	@Override
	public boolean compare(File f1,File f2) {
		System.out.println("FileName.compare: f1="+f1+", f2="+f2);
		if (f1!=null && f2!=null) {
			boolean result= f1.getName().equals(f2.getName());
			return result;
		}
		return false;
	}

	@Override
	protected String determineValueContrete(File file) {
		if (file != null) 
			return file.getName();
		System.err.println("FileName.determineValueContrete file=null!");
		return null;
	}

	@Override
	public String toString() {
		return "FileName [file=" + file + "]";
	}

}
