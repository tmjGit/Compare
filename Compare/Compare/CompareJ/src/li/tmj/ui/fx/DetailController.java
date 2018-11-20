package li.tmj.ui.fx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import li.tmj.ui.controls.ObjectCombobox;
import java.io.File;
import java.util.List;
import org.pmw.tinylog.Logger;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import li.tmj.model.FSItem;
import javafx.scene.control.TableColumn;

public class DetailController implements Initializable {
//	@FXML Button personenBt;//fx:id="personenBt" disable="true" mnemonicParsing="false" onAction="#personenBtAction" text="Personen" />
//	@FXML Button quitBt;//onAction="#quitBtAction" text="Beenden"
//	BorderPane
//	TabPane
	@FXML TableView<FSItem> detailsTab; //editable="true" 
	//TableColumn nameCol;//id="id" fx:id="nameCol" editable="false" prefWidth="75.0" text="Name" />
	 //TableColumn id="vorname" fx:id="pathCol" prefWidth="75.0" text="Path" />
	 //TableColumn id="nachname" fx:id="typeCol" editable="false" prefWidth="75.0" text="Type" />
	 //TableColumn id="plz" fx:id="criterion1Col" prefWidth="75.0" text="Criterion1" />
	 //TableColumn fx:id="criterion2Col" editable="false" prefWidth="75.0" text="Criterion2" />
//	AnchorPane 
//	TextField // fx:id="vornameFiled" layoutX="205.0" layoutY="28.0" promptText="Vorname" />
	            /*            <TextField fx:id="nachnameField" layoutX="205.0" layoutY="74.0" promptText="Nachname" />
	                        <TextField fx:id="plzField" layoutX="205.0" layoutY="121.0" promptText="PLZ" />
	                        <TextField fx:id="ortField" layoutX="205.0" layoutY="163.0" promptText="Ort" />
	                        <TextField fx:id="strasseField" layoutX="205.0" layoutY="210.0" promptText="Straße" />
	                        <TextField fx:id="telfonField" layoutX="205.0" layoutY="256.0" promptText="Telefon" />
	                        <TextField fx:id="mobilField" layoutX="205.0" layoutY="303.0" promptText="Mobil" />
	                        <TextField fx:id="emailField" layoutX="205.0" layoutY="349.0" promptText="email" /> */
//	Button // text="Save" />
//	HBox 
	Label infoLabel; 
	
		private ObservableList<FSItem> oList=FXCollections.observableArrayList();//Factorymethode
		@FXML TableColumn<FSItem,String> nameCol; // exemplarisch programmatische Implementierung
		@FXML TableColumn<FSItem,String> pathCol; // exemplarisch programmatische Implementierung
		@FXML TableColumn<FSItem,String> typeCol; // exemplarisch programmatische Implementierung
		@FXML TableColumn<FSItem,String> criterion1Col; // exemplarisch programmatische Implementierung
		@FXML TableColumn<FSItem,String> criterion2Col; // exemplarisch programmatische Implementierung
		
	

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setFxText();
		
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name")); // exemplarisch programmatische Implementierung 
		pathCol.setCellValueFactory(new PropertyValueFactory<>("path")); // exemplarisch programmatische Implementierung 
		typeCol.setCellValueFactory(new PropertyValueFactory<>("type")); // exemplarisch programmatische Implementierung 
//		criterion1Col.setCellValueFactory(new PropertyValueFactory<>("criterion1")); // exemplarisch programmatische Implementierung 
//		criterion2Col.setCellValueFactory(new PropertyValueFactory<>("criterion2")); // exemplarisch programmatische Implementierung 
		  
//		FSItem.setCriterionCount(criterionCount);
		oList.add(new FSItem("Name1","Path1","Typ1"));
		oList.add(new FSItem("Name2","Path2"," Typ2"));
		oList.add(new FSItem("Name3","Path3","Typ3", new boolean[] {false}));
		detailsTab.setItems(oList);

	}
	
	private void setFxText() {
//		personenBt.setText(Localization.get("Persons"));
//		quitBt.setText(Localization.get("Quit"));;
	}
	
	@FXML
	private void personenBtAction(javafx.event.ActionEvent event) throws IOException {
//		FxScene.PERSON.showOnNewStage();
//		FxManager.closeMyStage((Button) event.getSource());
	}
	
	@FXML private void quitBtAction() {
		Platform.exit();
	}
}


