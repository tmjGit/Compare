package tmj.ui.lang;

import tmj.ui.controls.ComboBoxable;

public class Language implements ComboBoxable{
	private String name;
	
	public Language(String name) {
		this.name=name;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public String getDisplayName() {
		return Localization.get(name);
	}

	@Override
	public String toString() {
		return "Language [name=" + name + "]";
	}
}
