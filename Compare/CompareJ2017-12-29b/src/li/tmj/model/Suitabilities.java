//package li.tmj.model;
//
//import java.io.File;
//
//public enum Suitabilities {
//
//	FILES_ONLY, 
//	DIRECTORIES_ONLY,
//	FILES_AND_DIRECTORIES,
//	irectorySuitable,
//	() {
//		return isDirectorySuitable;
//	}
//	
//	/**
//	 * Checks if this CriterionElement is suitable for this file;
//	 * e.g. (isDirectorySuitable && file.isDirectory)
//	 */
//	public boolean isSuitable(File file) {
//		return (file!=null && (isDirectorySuitable || !file.isDirectory()) && (isFileSuitable || file.isDirectory()));
//	}
//}
