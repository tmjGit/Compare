package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import conf.Conf;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import ui.lang.LanguageSet;
import model.FSCompareObject;
import model.RecursionType;
import model.criteria.FSCompareCriteria;
import model.criteria.FSCompareCriteriaPlaceholder;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import ui.Rows;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.control.ComboBox;

public class SampleController implements Initializable {
	@FXML AnchorPane anchorPane1;
	@FXML AnchorPane anchorPane2;
	@FXML AnchorPane anchorPane3;
	@FXML TextField pathField1;
	@FXML TextField pathField2;
	@FXML Label resultT;
	@FXML ComboBox<String> langCb;
	@FXML ComboBox<RecursionType> recursionCb;
	FSCompareObject fsco=null;
	@FXML Label fileT;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setLanguage(Conf.get("language"));//TODO
//		LanguageSet.setLanguage(LanguageSet.LANG_DEU);
		fileT.setText(LanguageSet.file()+"/"+LanguageSet.folder());
		fsco=new FSCompareObject();
		langCb.setItems(LanguageSet.displayNames());
		langCb.setValue("Deutsch");//TODO
		
		recursionCb.getItems().addAll(RecursionType.values());
		RecursionType type= RecursionType.parse(Conf.get("recursiontype"));
		if(type!=null)
			fsco.setRecursion(type);
		recursionCb.setValue(type);//RecursionType.RECURSION_OFF);
		resultT.textProperty().bind(fsco.valueProperty());// binding 2/2
		
		makeRows();
	}

	private void makeRows() {
		String[] str=Conf.get("rows").split(",");
		FSCompareCriteriaPlaceholder[] criteria=new FSCompareCriteriaPlaceholder[str.length];
		for(int i=0; i<str.length; i++) {
			FSCompareCriteria c=FSCompareCriteria.parse(str[i].trim());
			if(c!=null)
				criteria[i]=new FSCompareCriteriaPlaceholder(c, c.displayName);
		}
//		FSCompareCriteriaPlaceholder[] str= { new FSCompareCriteriaPlaceholder(FSCompareCriteria.CRITERION_DIRSIZE, FSCompareCriteria.CRITERION_DIRSIZE.displayName), new FSCompareCriteriaPlaceholder(FSCompareCriteria.CRITERION_FILEPATH, FSCompareCriteria.CRITERION_FILEPATH.displayName) }  ;
		Rows.createRows(fsco, criteria, new Pane[] {anchorPane1,anchorPane2,anchorPane3,anchorPane1});
//		Rows.createRows(fsco,new FSCompareCriteriaPlaceholder[2], new Pane[] {anchorPane1,anchorPane2,anchorPane3,anchorPane1});
	}
	
	@FXML public void startBtAction(ActionEvent event) {
		try {
			fsco.compare(false);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML public void fullBtAction(ActionEvent event) {
		try {
			fsco.compare(true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML public void pathField1KeyReleased(KeyEvent event) {
		File f=setFile(event);
		if (f!=null)
			fsco.setFile(0,f);
	}

	@FXML public void pathField2KeyReleased(KeyEvent event) {
		File f=setFile(event);
		if (f!=null)
			fsco.setFile(1,f);
	}
	
	private File setFile(KeyEvent event) {
		if (event.getCode()==KeyCode.ENTER) {
			TextField field=(TextField) event.getSource();
			File f = new File(field.getText());
			if ((f != null) && (f.canRead()))
				return f;
		}
		return null;
	}
	
	private File fileChooser(TextField field,int compareIndex) { // TODO should allow files and folders!
		FileChooser c=new FileChooser();
		File  f = c.showOpenDialog(null);
		return setFile(f,field,compareIndex);
	}

	private File directoryChooser(TextField field,int compareIndex) { // TODO s. fileChooser
		DirectoryChooser c = new DirectoryChooser();
		File f = c.showDialog(null);
		return setFile(f,field,compareIndex);
	}

	private File setFile(File f, TextField field,int compareIndex) { 
		if ((f != null) && (f.canRead())) {
			field.setText(f.getAbsolutePath());
			fsco.setFile(compareIndex, f);
			return f;
		}
		return null;
	}

	@FXML public void chooseFile1BtAction(ActionEvent event) {
		fileChooser(pathField1,0); 
	}

	@FXML public void chooseFile2BtAction(ActionEvent event) {
		fileChooser(pathField2,1); 
	}
	
	@FXML public void chooseDir1BtAction(ActionEvent event) {
		directoryChooser(pathField1,0); 
	}

	@FXML public void chooseDir2BtAction(ActionEvent event) {
		directoryChooser(pathField2,1); 
	}

	@FXML public void addBtAction(ActionEvent event) {
		Rows.addRow(null);
	}

	@FXML public void anchorPaneDragOver(DragEvent event) {
		event.acceptTransferModes(TransferMode.LINK);
        event.consume();
	}

	@FXML public void anchorPane2Dropped(DragEvent event) {
		dropped(event,pathField1,0);        		   
	}

	@FXML public void anchorPane3Dropped(DragEvent event) {
		dropped(event,pathField2,1);        		   
	}
	
	private void dropped(DragEvent event, TextField field,int compareIndex) {
		Dragboard db = event.getDragboard();
        if (db.hasFiles()) {
     	   List<File> ff=db.getFiles();
     	   if(ff.size()>1) {
     		   setFile(ff.get(0),pathField1,0);
     		   setFile(ff.get(1),pathField2,1);        		   
     	   }else if(ff.size()>0)
     		   setFile(ff.get(0),field,compareIndex);
     	   if(ff.size()>2)
     		   System.out.println("Too many files dropped! We take the first two, only.");//TODO log as warning   
     	   event.setDropCompleted(true);// event feedback: object successfully transferred
        }else
        	event.setDropCompleted(false);
        event.consume();
	}

	@FXML @SuppressWarnings("unchecked") 
	public void langCbAction(ActionEvent event) {
		// TODO Das Umschalten wirkt sich bisher erst bei erneuter Verwendung aus. 
		// Die GUI sollte aber besser umgehend aktualisiert werden.
		ComboBox<String> cb=(ComboBox<String>) event.getSource();
		LanguageSet.setLanguage(LanguageSet.languages().get(cb.getValue()));
	}
	
	private void setLanguage(String lang) {
//		System.out.println("lang="+lang);
		LanguageSet.setLanguage(lang);
	}

	@FXML @SuppressWarnings("unchecked")
	public void recursionCbAction(ActionEvent event) {
		ComboBox<RecursionType> cb=(ComboBox<RecursionType>) event.getSource();
		setRecursionType(cb.getValue());
	}
	
	private void setRecursionType(RecursionType type) {
		fsco.setRecursion(type);
	}
}
