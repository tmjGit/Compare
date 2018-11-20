package li.tmj.ui.row;

import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import li.tmj.model.FSCompareCriterion;
import li.tmj.model.FSCompareObject;
import li.tmj.model.criteria.FSCompareCriteria;
import li.tmj.ui.controls.ObjectCombobox;
import li.tmj.ui.lang.LanguageSet;
import org.pmw.tinylog.Logger;

public class Row {
		private static final int CONTROLS_DISTANCE=12; // first control:  y=12
		private static final int CONTROLS_HEIGHT=18;   // second control: y=12+18+12=42
		private static final int CONTROLS_WIDTH=126;
		private static int Index=0;
		private FSCompareCriterion fscc=null;
		private int objectCriterionIndex;
		private ObjectCombobox<FSCompareCriteria> headerCb=null;
		private Label[] labelT=null; // contains the File-Object's criterion's data, e.g. the file name  //  binding A1/2
		
		public Row(FSCompareObject fsco,FSCompareCriteria[] criteria, ArrayList<ObservableList<Node>> columns) {
			init(fsco,criteria, columns);
		}
		
		public Row(FSCompareObject fsco,FSCompareCriteria[] criteria, Pane[] columns) {
			ArrayList<ObservableList<Node>> list=new ArrayList<>();
			for (Pane p:columns)
				list.add(p.getChildren());
			init(fsco,criteria,list); 
		}
		
		public void init(FSCompareObject fsco,FSCompareCriteria[] criteria, ArrayList<ObservableList<Node>> columns) {
			objectCriterionIndex=fsco.addCriterion(null);
			labelT=new Label[columns.size()-1]; // e.g. 4 columns -> labelT[0]..label[2]
			arrangeHeaderCb(criteria);
			
			if (labelT.length<1) {
				Logger.warn("Count middle columns = zero!");
				return; // TODO exception handling, parameter error
			}
			arrangeFirstColumn(columns);			
			arrangeMiddleColumns(columns);
			
			if (labelT.length<2) {
				Logger.warn("Count middle columns < 2!");
				return; // TODO exception handling, parameter error
			}
			arrangeLastColumn(columns);
			setOrder(++Index);
			headerCbAction(fsco);
		}

		private void arrangeHeaderCb(FSCompareCriteria[] criteria) {
			headerCb=new ObjectCombobox<>();
			headerCb.getItems().setAll(criteria);
			headerCb.setMinWidth(CONTROLS_WIDTH+100);
			headerCb.setMaxWidth(CONTROLS_WIDTH+100);
		}

		private void arrangeFirstColumn(ArrayList<ObservableList<Node>> columns) {
			columns.get(0).add(headerCb);  // first column
			headerCb.setLayoutX(CONTROLS_DISTANCE);
		}

		private void arrangeMiddleColumns(ArrayList<ObservableList<Node>> columns) {
			Label l;
			for (int i=0; i<labelT.length-1; i++) { // Second column upto last but one. If there are less than 2 columns, there is no middle column.
				l=new Label("");
				columns.get(i+1).add(l);
				l.setLayoutX(CONTROLS_DISTANCE);
				labelT[i]=l;
			}
		}

		private void arrangeLastColumn(ArrayList<ObservableList<Node>> columns) {
			Label l=new Label("<" + LanguageSet.notCalculateded() + ">"); // last column
			columns.get(labelT.length).add(l);
			l.setMinWidth(CONTROLS_WIDTH);
			l.setMaxWidth(CONTROLS_WIDTH);
			AnchorPane.setRightAnchor(l, 12.0); 
			labelT[labelT.length-1]=l;
		}

		private void headerCbAction(FSCompareObject fsco) {
			//header has the known criteria.
			//if a criterion is choosen (for this row), for each file column the DisplayValue must be bound.
			//And the result value must be bound, too.
			headerCb.valueProperty().addListener( (observable, oldValue, newValue) -> {
				fscc=newValue.create(labelT.length-1); // the last labelT is the result.
				fsco.setCriterion(objectCriterionIndex,  fscc); // also assigns files
				for(int i=0; i<labelT.length-1; i++) {
					labelT[i].textProperty().bind(fscc.fileValueProperty(i)); // binding A2/2
				}
				labelT[labelT.length-1].textProperty().bind(fscc.valueProperty()); // binding B2/2	
			} );			
		}
		
		public void setOrder(int Index) {
			int myTop=top(Index+1);
			headerCb.setLayoutY(myTop);
			for (int i=0; i<labelT.length; i++) {
				labelT[i].setLayoutY(myTop);}
		}
		
		private int top(int Index) {
			return CONTROLS_DISTANCE+Index*(CONTROLS_DISTANCE+CONTROLS_HEIGHT); // Index 2: 12 + 2*(12 + 18) = 72
		}

		public ComboBox<FSCompareCriteria> getHeader() {
			return headerCb;
		}
		
		public Label[] getLabel() {
			return labelT;
		}
		
	 }
