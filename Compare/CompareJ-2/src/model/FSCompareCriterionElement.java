package model;

import java.io.File;
import java.io.FileNotFoundException;

public abstract class FSCompareCriterionElement extends FSCompare {
		private String name=null; // public final String NAME; // should be a constant for the sub class
		protected File file = null; // TODO remove this, if the getFile event was implemented
		private boolean runsAutomatically=false;
		private boolean isFileSuitable=false;
		private boolean isDirectorySuitable=false;
		
		protected abstract String determineValueContrete(File file);
		public abstract boolean compareTo(File f) throws FileNotFoundException;
		public abstract boolean compare(File f1,File f2) throws FileNotFoundException;
		
		protected FSCompareCriterionElement(String name,boolean mayRunAutomatically,boolean isFileSuitable,boolean isDirectorySuitable) {
			super();
			this.name=name;
			this.runsAutomatically=mayRunAutomatically;
			this.isFileSuitable=isFileSuitable;
			this.isDirectorySuitable=isDirectorySuitable;
		}
	    
	    public void determineValue() {
			if(!isSuitable())
				setValue("");
			else
				setValue(determineValueContrete(file));
		}
	     
		public String getName() {
			return name;
		};
		
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
		public boolean isSuitable() {
			return (file!=null && (isDirectorySuitable || !file.isDirectory()) && (isFileSuitable || file.isDirectory()));
		}
		
		public void setFile(File file) { 
			// TODO implement event to ask the CompareObject that uses this criterion for the file object
			// That would ensure that file is really the object that CompareObject is using, now.
			this.file = file;
			if (runsAutomatically())
				determineValue();
		}
		
		@Override
		public String toString() {
			return "FSCompareCriterionElement [name=" + name + ", value=" + value + ", file=" + file
					+ ", runsAutomatically=" + runsAutomatically + "]";
		}
		
		
	}
