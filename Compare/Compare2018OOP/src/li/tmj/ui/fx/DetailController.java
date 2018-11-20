package li.tmj.ui.fx;

import java.net.URL;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import li.tmj.ui.controls.ObjectCombobox;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.pmw.tinylog.Logger;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import li.tmj.app.Application;
import li.tmj.conf.Config;
import li.tmj.model.FSCompareObject;
import li.tmj.model.RecursionType;
import li.tmj.model.RunningStatus;
import li.tmj.ui.lang.LanguageSet;
import li.tmj.ui.lang.Localization;
import li.tmj.ui.row.Rows;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import java.io.IOException;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import li.tmj.model.FSItem;

public class DetailController implements Initializable {
//	@FXML Button personenBt;//fx:id="personenBt" disable="true" mnemonicParsing="false" onAction="#personenBtAction" text="Personen" />
//	@FXML Button quitBt;//onAction="#quitBtAction" text="Beenden"
//	TabPane
	@FXML TableView<FSItem> detailsTab; //editable="true" 
	//TableColumn nameCol;//id="id" fx:id="nameCol" editable="false" prefWidth="75.0" text="Name" />
	 //TableColumn fx:id="criterion2Col" editable="false" prefWidth="75.0" text="Criterion2" />
//	TextField // fx:id="vornameFiled" layoutX="205.0" layoutY="28.0" promptText="Vorname" />
	            /*            <TextField fx:id="nachnameField" layoutX="205.0" layoutY="74.0" promptText="Nachname" />
	                        <TextField fx:id="emailField" layoutX="205.0" layoutY="349.0" promptText="email" /> */
//	Button // text="Save" />
//	HBox 
	@FXML Label infoLabel; 
	
		private ObservableList<FSItem> oList=FXCollections.observableArrayList();//Factorymethode
//		@FXML TableColumn<FSItem,String> nameCol; // exemplarisch programmatische Implementierung

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setFxText();
		makeTable();
	}
	
	private void setFxText() {
//		personenBt.setText(Localization.get("Persons"));
//		quitBt.setText(Localization.get("Quit"));;
	}
	
	private void makeTable() {
//				nameCol.setCellValueFactory(new PropertyValueFactory<>("name")); // exemplarisch programmatische Implementierung 
//				pathCol.setCellValueFactory(new PropertyValueFactory<>("path")); // exemplarisch programmatische Implementierung 
//				typeCol.setCellValueFactory(new PropertyValueFactory<>("type")); // exemplarisch programmatische Implementierung 

//	    TableColumn<FSItem, String> detailColumn=new TableColumn<>( "Name" );
//	    detailColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>( param.getValue().getName() ));
//	    detailsTab.getColumns().add(detailColumn);
		for (int i = 0; i < 2; i++) {
		    final int finalIndex = i;
			TableColumn<FSItem, String> detailColumn=new TableColumn<>( "criterion"+(i+1) );
		    detailColumn.setCellValueFactory(param -> 
		    		new ReadOnlyObjectWrapper<>( String.valueOf( param.getValue().getMatches()[finalIndex] ) )
		    	);
		    detailsTab.getColumns().add(detailColumn);
		}
		

	
	
//		TableColumn<FSItem,String>[] detailColumns=new TableColumn[5];
//		detailColumns[0].setCellValueFactory(new PropertyValueFactory<>("name"));
//		detailColumns[1].setCellValueFactory(new PropertyValueFactory<>("path"));
//		detailColumns[2].setCellValueFactory(new PropertyValueFactory<>("type"));
//		for(int i=0;i<2;i++) {
////			detailColumns[3+i].setCellValueFactory(new PropertyValueFactory<>("criterion"+i));
//			detailColumns[3+i].setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getMatches()[i]) );
//		}
		
		
//		TableView<ObservableList<String>> tableView = new TableView<>();
//		List<String> columnNames = dataGenerator.getNext(N_COLS);
//		for (int i = 0; i < columnNames.size(); i++) {
//		    final int finalIdx = i;
//		    TableColumn<ObservableList<String>, String> column = new TableColumn<>(
//		            columnNames.get(i)
//		    );
//		    column.setCellValueFactory(param ->
//		            new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx))
//		    );
//		    tableView.getColumns().add(column);
//		}
		
		
				  
//				FSItem.setCriterionCount(criterionCount);
				oList.add(new FSItem("Name1","Path1","Typ1"));
				oList.add(new FSItem("Name2","Path2"," Typ2"));
				oList.add(new FSItem("Name3","Path3","Typ3", new boolean[] {false}));
				detailsTab.setItems(oList);

				
				
//				DesignPattern p=new DesignPattern("Singleton","Creational","nur eine Instanz einer Klasse");
//				oList.add(p);
//				map.put("singleton", p);
//				p=new DesignPattern("Strategy","Behavioral"," definiert eine Familie austauschbarer Algorithmen");
//				oList.add(p);
//				map.put("strategy", p);
//				p=new DesignPattern("MVC","Andere","Trennung von Model, View, Control");
//				oList.add(p);
//				map.put("mvc", p);
//				tableView.setItems(oList);
//				display();
//				map.get("mvc").setName("xx"+map.get("mvc").getName());;
			
			
//			public void display() {
//				for(int i=0;i<oList.size();i++) {
//					DesignPattern d=oList.get(i);
//					DesignPattern d2=tableView.getItems().get(i);
//					System.out.println("pattern("+i+"):"+d 
//							+ ( (d==d2) ? " = " : " ≠ " ) 
//							+ "tableItem("+i+"):"+d2
//							);
//				}
//			}

//			@FXML public void onTableViewMouseReleased() {
//				display();
			}
		

//	}
	
	@FXML
	private void personenBtAction(javafx.event.ActionEvent event) throws IOException {
//		FxScene.PERSON.showOnNewStage();
//		FxManager.closeMyStage((Button) event.getSource());
	}
	
	@FXML private void quitBtAction() {
		Platform.exit();
	}
}


