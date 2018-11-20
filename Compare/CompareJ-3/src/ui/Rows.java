package ui;

import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import model.FSCompareObject;
import model.criteria.FSCompareCriteriaPlaceholder;

public class Rows {
	public static FSCompareObject fsco=null;
	public static ObservableList<FSCompareCriteriaPlaceholder> criteria=null;
	public static ArrayList<Row> rows=null;
	private static ArrayList<ObservableList<Node>> columns=null;
	//TODO add XML setting object & file for predefined Rows
	
	public static void setupRows(FSCompareObject fsco, ArrayList<ObservableList<Node>> columns) {
		Rows.fsco=fsco;
		Rows.columns=columns;
		rows=new ArrayList<Row>();
		criteria = FSCompareCriteriaPlaceholder.criteriaPlaceholderList();	
	}
	
	/**
	 * To use createRow(), you must define the environment by setupRows().
	 */
	public static Row createRow(FSCompareCriteriaPlaceholder criterion) {
		return new Row(fsco,criteria, criterion, columns);
	}
	
	public static Row addRow(FSCompareCriteriaPlaceholder criterion) {
		Row r=createRow(criterion);
		rows.add(r);
		return r;
	}
	
	/**
	 * createRows() does not need setupRows() to be run
	 */
	public static ArrayList<Row> createRows(
			FSCompareObject fsco,
			FSCompareCriteriaPlaceholder[] criteria, 
			ArrayList<ObservableList<Node>> columns) {
		setupRows(fsco, columns);	
		for (int i=0; i<criteria.length; i++) {
			rows.add(createRow(criteria[i]));
		}
		return rows;
	}
	
	public static ArrayList<Row> createRows(
			FSCompareObject fsco,
			FSCompareCriteriaPlaceholder[] criteria, 
			Pane[] columns) {
		ArrayList<ObservableList<Node>> list=new ArrayList<>();
		for (Pane p:columns)
			list.add(p.getChildren());	
		return createRows(fsco, criteria, list);
	}

 }//				FSCompareCriteriaPlaceholder criterion, 

