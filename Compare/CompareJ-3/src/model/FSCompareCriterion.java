package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

public class FSCompareCriterion extends FSCompare {
	private FSCompareCriterionElement[] elements=null;
	
	public FSCompareCriterion(FSCompareCriterionElement[] elements) {
		super();
		Boolean equal=true;
		for(int i=1; i<elements.length; i++) {
			if (! elements[0].getClass().equals(elements[1].getClass())) {
				equal=false;
				break;
			}
		}
		if (! equal) {
			throw new RuntimeException("FSCompareCriterion's elements must have the same type!");
		}
		this.elements=elements;
	}

	public boolean compare() throws FileNotFoundException{
		return compareIntern();
	}
	
	public boolean compare(File f1, File f2) throws FileNotFoundException{
		Boolean result=false;
//		if (files.length>1) {
			if(get(0).isSuitable())
//			if(files(0).isSuitable()) {//TODO
				result=get(0).compare( f1, f2 ); //result=get(0).compareTo(get(1).file);
//				get(0).determineValue();
//				get(1).determineValue();
//			}
//		}
//		setCompareToResult(result);
		return result;
		
//		return compareIntern();
	}
	
	public boolean compareIntern() throws FileNotFoundException{ // TODO make flexibel!
		Boolean result=false;
		if (elements.length>1) {
			if(get(0).isSuitable()) {
				result=get(0).compareTo(get(1).file);
				get(0).determineValue();
				get(1).determineValue();
			}
		}
		setCompareToResult(result);
		return result;
	}
	
	public FSCompareCriterionElement get(int Index) {
		return elements[Index];
	}
	
	public String getName() {
		if (elements.length<1)
			return null;
		return elements[0].getName();
	};
	
	public boolean mayRunAutomatically() {
		if (elements.length<1)
			return false;
		return elements[0].runsAutomatically();
	};
	
	public boolean isFileSuitable() {
		if (elements.length<1)
			return false;
		return elements[0].isFileSuitable();
	};
	
	public boolean isDirectorySuitable() {
		if (elements.length<1)
			return false;
		return elements[0].isDirectorySuitable();
	};

	public void determineValues() {
		for(FSCompareCriterionElement e:elements)
			e.determineValue();
	};
	
	private void setCompareToResult(boolean result) {
			setValue(compareResult(result));
	}
	
	@Override
	public String toString() {
		return "FSCompareCriterion [value=" + value + ", elements="
				+ Arrays.toString(elements) + "]";
	}
	
}
