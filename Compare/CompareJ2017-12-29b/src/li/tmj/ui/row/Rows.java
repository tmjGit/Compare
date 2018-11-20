package li.tmj.ui.row;

import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import li.tmj.model.FSCompareObject;
import li.tmj.model.criteria.FSCompareCriteria;

public class Rows {
	public static FSCompareObject fsco=null;
	public static FSCompareCriteria[] criteria=null;
	public static ArrayList<Row> rows=null;
	private static ArrayList<ObservableList<Node>> columns=null;
	//TODO add XML setting object & file for predefined Rows
	
	public static void setupRows(FSCompareObject fsco, ArrayList<ObservableList<Node>> columns) {
		Rows.fsco=fsco;
		Rows.columns=columns;
		rows=new ArrayList<Row>();
		criteria = FSCompareCriteria.values();	
	}
	
	/**
	 * To use createRow(), you must define the environment by setupRows().
	 */
	public static Row createRow() {
		return new Row(fsco,criteria,columns);
	}
	
	public static Row addRow() {
		Row r=createRow();
		rows.add(r);
		return r;
	}
	
	/**
	 * createRows() does not need setupRows() to be run
	 */
	public static ArrayList<Row> createRows(FSCompareObject fsco,int countRows, ArrayList<ObservableList<Node>> columns) {
		setupRows(fsco, columns);	
		for (int i=0; i<countRows; i++) {
			rows.add(createRow());
		}
		return rows;
	}
	
	public static ArrayList<Row> createRows(FSCompareObject fsco,int countRows, Pane[] columns) {
		ArrayList<ObservableList<Node>> list=new ArrayList<>();
		for (Pane p:columns)
			list.add(p.getChildren());	
		return createRows(fsco,countRows,  list);
	}

 }
