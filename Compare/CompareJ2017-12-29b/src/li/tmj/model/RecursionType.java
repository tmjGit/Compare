package li.tmj.model;

import li.tmj.ui.controls.ComboBoxable;
import li.tmj.ui.lang.Localization;

public enum RecursionType implements ComboBoxable { 
	RECURSION_OFF, // No recursion. Checks only the selected file or folder.
	RECURSION_FILES, // Checks recursively but only files: If the selected item is a file, it will be checked but there is effectively no recursion. 
	// Otherwise the algorithm runs through the directory tree and checks all found files.
	// If there is one or more files with no corresponding item to verify with, the summed result will be false.
	RECURSION_DIRS, // Checks recursively but only directories: If the selected item is a file, it does nothing returning false.
	// Otherwise the algorithm runs through the directory tree and checks all found directories by itself and keeps on running through its childs.
	// If there is one or more directories with no corresponding item to verify with, the summed result will be false.
	RECURSION_ALL, // Checks recursively all files and directories. If the selected item is a file, it will be checked but there is effectively no recursion.
	// Otherwise the algorithm runs through the directory tree starting with the selected item itself, and checks all found files and directories and keeps on running through the directory's childs.
	// If there is one or more file or directory with no corresponding item to verify with, the summed result will be false.
	RECURSION_DIRS1, // Checks recursively all sub directories: If the selected item is a file, it does nothing returning false.
	// Otherwise the algorithm runs through the directory tree starting with the selected item's childs, and checks all found directories by itself and keeps on running through its childs.
	// If there is one or more directories with no corresponding item to verify with, the summed result will be false.
	RECURSION_ALL1 ;// Checks recursively all containing files and directories. If the selected item is a file, it will be checked but there is effectively no recursion.
	// Otherwise the algorithm runs through the directory tree starting with the selected item's childs, and checks all found files and directories and keeps on running through the directory's childs.
	// If there is one or more file or directory with no corresponding item to verify with, the summed result will be false.
	
	@Override
	public String getDisplayName() {
		return Localization.get(name());
	}
}
