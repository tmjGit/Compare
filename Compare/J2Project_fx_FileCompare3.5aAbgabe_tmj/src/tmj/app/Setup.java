package tmj.app;

import tmj.log.Log;
import tmj.ui.lang.Localization;

public final class Setup {
	private Setup() {}
	
	public static void init() {
		Log.init();
		Localization.setLanguage( Application.confMainGet("language") ); // LanguageSet.setLanguage(LanguageSet.LANG_DEU);
	}
}
