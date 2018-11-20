package model;

import java.io.Serializable;

//public final class RecursionType {
//	public static final String RECURSION_OFF="recursion_off";
//	public static final String RECURSION_FILES="recursion_files";
//	public static final String RECURSION_DIRS="recursion_dirs";
//	public static final String RECURSION_ALL="recursion_all";
//	private static final ObservableList<String> recursionTypes=FXCollections.observableArrayList();
//
//	static {
//		recursionTypes.add(RecursionType.RECURSION_OFF);
//		recursionTypes.add(RecursionType.RECURSION_FILES);
//		recursionTypes.add(RecursionType.RECURSION_DIRS);
//		recursionTypes.add(RecursionType.RECURSION_ALL);
//	}
//	
//	public static ObservableList<String> recursionTypes(){
//		return recursionTypes;
//	}
//}

public enum RecursionType implements Serializable { RECURSION_OFF, RECURSION_FILES, RECURSION_DIRS, RECURSION_ALL; 
//	private static final ObservableList<RecursionType> recursionTypes=FXCollections.observableArrayList();
//	private static final ObservableList<String> recursionTypeNames=FXCollections.observableArrayList();
		
//	static {
//		for(RecursionType entry:RecursionType.values()) {
//			recursionTypes.add(entry);
//		}
//		recursionTypes.add(RECURSION_OFF);
//		recursionTypes.add(RECURSION_FILES);
//		recursionTypes.add(RECURSION_DIRS);
//		recursionTypes.add(RECURSION_ALL);
//		for(RecursionType entry:recursionTypes) {
//			recursionTypeNames.add(entry.name());
//		}
//	}
		
//	public static ObservableList<RecursionType> recursionTypes(){
//		return FXCollections.observableArrayList(RecursionType.values());
//		return RecursionType.recursionTypes;
//	}

	
	public static RecursionType parse(String importText) {
		for(RecursionType entry:RecursionType.values())
			if(entry.name().equals(importText))
				return entry;
		return null;
	}
}
