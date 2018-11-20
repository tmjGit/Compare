package li.tmj.ui.controls;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.ComboBoxListCell;

public class ObjectCombobox<T extends ComboBoxable> extends ComboBox<T> {	
	public ObjectCombobox() {
		super();
		init();
	}
	
	public ObjectCombobox(ObservableList<T> items) {
		super(items);
		init();
	}
	
	public void init() {
		this.setCellFactory(param->new ComboBoxCell());
		this.setButtonCell(new ComboBoxCell());
	}
	private class ComboBoxCell extends ComboBoxListCell<T> {
		@Override
		public void updateItem(T p, boolean empty) {
			super.updateItem(p, empty);
			if (p != null)
				setText(p.getDisplayName());
		}
	}	
}
