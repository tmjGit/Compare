package tmj.model.criteria;

import java.io.File;
import org.pmw.tinylog.Logger;
import tmj.model.FSCompareCriterion;
import tmj.model.FSCompareFileValue;

public class FileName extends FSCompareCriterion {
	public FileName(FSCompareFileValue[] criterionValues) {
		super(criterionValues,"Name",true,true,true, f -> readDisplayValueStatic(f) ); 
	}

	@Override
	public boolean compareConcreteIntern(File[] files,boolean updateValue) { // make flexible
		Logger.trace("updateValue={}, files={}",files.toString());
//		if(files.length<2) // super class checked this
//			return false;
//		if(files[0]==null) {
//			for(int i=1; i<files.length; i++)
//				if(files[i]!=null)
//					return false;
//			return true;
//		}
		String[] s=new String[files.length];
		for (int i=0; i<files.length; i++) {
			s[i]=files[i].getName();
			if (updateValue) {
				setFileValue(i,s[i]);
			}
		}
		boolean result=s[0].equals(s[1]);
		return result;
	}
	
	private static String readDisplayValueStatic(File file) {
		return file.getName();
	}

}
