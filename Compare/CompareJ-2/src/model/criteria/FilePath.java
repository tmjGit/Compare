package model.criteria;

import java.io.File;
import model.FSCompareCriterionElement;

public class FilePath extends FSCompareCriterionElement {
	public FilePath() {
		super("Pfad",true,true,true); 
	}

	@Override
	public boolean compareTo(File f) {
		return compare(file, f);
	}

	@Override
	public boolean compare(File f1,File f2) {
		System.out.println("FileName.compare: f1="+f1+", f2="+f2);
		if (f1!=null && f2!=null) {
			boolean result= f1.getAbsolutePath().equals(f2.getAbsolutePath());
			return result;
		}
		return false;
	}

	@Override
	protected String determineValueContrete(File file) {
		if (file != null) {
			String s=file.getAbsolutePath();
			return s.substring(0, s.length()-file.getName().length());
		}
		System.err.println("FilePath.determineValueContrete file=null!");
		return null;
	}

	@Override
	public String toString() {
		return "FilePath [file=" + file + "]";
	}

}
