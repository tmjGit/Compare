package li.tmj.model;

import org.pmw.tinylog.Logger;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import li.tmj.ui.lang.LanguageSet;

/**
 * JavaFX Bean, Listener/Binding geeignet
 * Properties
 */
public abstract class FSCompare {
	private FSCompareValueService service=new FSCompareValueService();
	protected RecursionType recursion=RecursionType.RECURSION_OFF;//TODO does not work properly
	
	public FSCompare() {
		setValue("<" + LanguageSet.notCalculateded() + ">");
	}
	
	public final String getValue() { // nach außen normale Getter
		return service.valueProperty().get();
	}

	public final void setValue(String value) {
		Logger.trace("value={}",value);
		service.setNewValue(value);
		Platform.runLater(()->{// w/o runlater and if called from another own
			service.restart(); // thread, restart() will hang here w/o errors
		});
	}
	
	public final ReadOnlyObjectProperty<String> valueProperty() {
		return service.valueProperty();
	}

	public RecursionType getRecursion() {
		return recursion;
	}

	public void setRecursion(RecursionType recursion) {
		this.recursion = recursion;
	}
	
	protected static String compareResult(boolean result) {
		if (result)
			return LanguageSet.same();
		else
			return LanguageSet.different();
	}
}
