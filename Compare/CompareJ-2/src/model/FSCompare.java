package model;

import ui.lang.LanguageSet;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * JavaFX Bean, Listener/Binding geeignet
 * Properties
 */
public abstract class FSCompare {
	protected StringProperty value=new SimpleStringProperty();  // binding 1/7
	protected RecursionType recursion=RecursionType.RECURSION_OFF;//TODO does not work properly
	
	public FSCompare() {
		setValue("<" + LanguageSet.notCalculateded() + ">");
	}
	
	// final bedeutet hier: Methode kann nicht überschrieben werden.
	public final StringProperty valueProperty() { // Getter für die Property
		return this.value;
	}

	public final String getValue() { // nach außen normale Getter
		return this.valueProperty().get();
	}

	/*
	 * final vor dem Parameter bedeutet, dass die Variable nicht innerhalb dieser
	 * Methode verändert wird/werden kann. Das gilt bzw. sollte gelten eigentlich
	 * mindestens für alle Getter und Setter
	 */
//	private final void setValue(final String value) { // nach außen normale Setter
	protected final void setValue(String value) { 
		this.valueProperty().set(value);
	}

	public RecursionType getRecursion() {
		return recursion;
	}

	public void setRecursion(RecursionType recursion) {
		this.recursion = recursion;
	}
	
	protected String compareResult(boolean result) {
		if (result)
			return LanguageSet.same();
		else
			return LanguageSet.different();
	}
}
