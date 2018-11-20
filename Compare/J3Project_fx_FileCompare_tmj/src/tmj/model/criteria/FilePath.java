package tmj.model.criteria;

import java.io.File;

import org.pmw.tinylog.Logger;

import tmj.model.FSCompareCriterion;
import tmj.model.FSCompareFileValue;

public class FilePath extends FSCompareCriterion {
	public FilePath(FSCompareFileValue[] criterionValues) {
		super(criterionValues,"Pfad",true,true,true, f->readDisplayValueStatic(f) ); 
	}

	@Override
	public boolean compareConcreteIntern(File[] files,boolean updateValue) { // make flexible
		Logger.trace("compareConcreteIntern...");
//		if(files.length<2) // super class checked this
//			return false;
//		if (files[0]==null ^ files[1]==null)
//			return false;
//		if(files[0]==null)
//			return true;
		String[] s=new String[files.length];
		for (int i=0; i<files.length; i++) {
			s[i]=readDisplayValueStatic(files[i]);
			if (updateValue) {
				setFileValue(i,s[i]);
			}
		}
		boolean result=s[0].equals(s[1]);
		return result;
	}

	protected static String readDisplayValueStatic(File file) {
		String s=file.getAbsolutePath();
		return s.substring(0, s.length()-file.getName().length());
	}

}
