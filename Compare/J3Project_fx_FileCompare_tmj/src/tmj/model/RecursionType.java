package tmj.model;

import tmj.ui.controls.ComboBoxable;
import tmj.ui.lang.Localization;

public enum RecursionType implements ComboBoxable { 
	RECURSION_OFF, 
	RECURSION_FILES, 
	RECURSION_DIRS, 
	RECURSION_ALL,
	RECURSION_DIRS1, 
	RECURSION_ALL1 ;
	
	@Override
	public String getDisplayName() {
		return Localization.get(name());
	}
}
