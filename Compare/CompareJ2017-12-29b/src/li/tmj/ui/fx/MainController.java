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
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import li.tmj.app.Application;
import li.tmj.conf.Config;
import li.tmj.model.FSCompareObject;
import li.tmj.model.RecursionType;
import li.tmj.model.RunningStatus;
//import li.tmj.ui.lang.Language;
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

public class MainController implements Initializable {
	@FXML AnchorPane anchorPane1;
	@FXML AnchorPane anchorPane2;
	@FXML AnchorPane anchorPane3;
	@FXML Button file1Bt;
	@FXML Button file2Bt;
	@FXML Button folder1Bt;
	@FXML Button folder2Bt;
	@FXML Button startBt;
	@FXML Button entireBt;
	@FXML Label fileT;
	@FXML Label recursionT;
	@FXML Label object1T;
	@FXML Label object2T;
	@FXML Label search1T;
	@FXML Label search2T;
	@FXML Label compareT;
	@FXML Label resultT;
	@FXML TextField pathField1;
	@FXML TextField pathField2;
	@FXML ObjectCombobox<Config> langCb;
//	@FXML ObjectCombobox<Language> langCb;
	@FXML ObjectCombobox<RecursionType> recursionCb;
	FSCompareObject fsco=null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setFxText();
		fsco=new FSCompareObject();
		recursionCb.getItems().addAll(RecursionType.values());
		recursionCb.setValue(RecursionType.RECURSION_OFF);
		resultT.textProperty().bind(fsco.valueProperty());// binding 2/2
		fsco.setOnSucceeded(b->{
			Logger.debug("Succeeded function: setRunning({})...",b);
			setStatus(RunningStatus.STOPPED); 
			return true;
		});
		Rows.createRows(fsco,
				Integer.parseInt( Application.confMainGet("rowcount") ), //adjustable preset 
				new Pane[] {anchorPane1,anchorPane2,anchorPane3,anchorPane1});
	}

	private void setFxText() {
		file1Bt.setText(LanguageSet.fileLabel()+"...");
		file2Bt.setText(LanguageSet.fileLabel()+"...");
		folder1Bt.setText(LanguageSet.folderLabel()+"...");
		folder2Bt.setText(LanguageSet.folderLabel()+"...");
		setStatus(RunningStatus.STOPPED);
		fileT.setText(LanguageSet.fileLabel()+"/"+LanguageSet.folderLabel());
		recursionT.setText(LanguageSet.recursionLabel());
		object1T.setText(LanguageSet.objectLabel()+" 1");
		object2T.setText(LanguageSet.objectLabel()+" 2");
		search1T.setText(LanguageSet.browseLabel());
		search2T.setText(LanguageSet.browseLabel());
		compareT.setText(LanguageSet.compareLabel());
		resultT.setText("&lt;"+ LanguageSet.notCalculateded() +"&gt;");
		pathField1.setPromptText(LanguageSet.fileLabel()+ "/"+LanguageSet.fileLabel() );
		pathField2.setPromptText(LanguageSet.fileLabel()+ "/"+LanguageSet.fileLabel() );
		langCb.getItems().addAll(Localization.config);
//		langCb.getItems().addAll(Localization.getLanguages());
		langCb.setValue(Localization.config);
//		langCb.setValue(Localization.getLanguage());
//		langCb.setValue(Localization.getLanguage());
	}
	
	@FXML public void startBtAction(ActionEvent event) {
		if(fsco.getStatus().equals(RunningStatus.RUNNING) || fsco.getStatus().equals(RunningStatus.PAUSED)) {
			setStatus(RunningStatus.STOPPING);
			fsco.stop();
		}else {// fsco.getStatus().equals(RunningStatus.STOPPED)
			setStatus(RunningStatus.RUNNING);
			fsco.compare(false);
		}
	}

	@FXML public void entireBtAction(ActionEvent event) {
		if(fsco.getStatus().equals(RunningStatus.RUNNING)) {
			setStatus(RunningStatus.PAUSED);
			fsco.pause();
		}else if (fsco.getStatus().equals(RunningStatus.PAUSED)) {
			setStatus(RunningStatus.RUNNING);
			fsco.resume();
		}else { // fsco.getStatus().equals(RunningStatus.STOPPED)
			setStatus(RunningStatus.RUNNING);
			fsco.compare(true);
		}
	}
	
	private void setStatus(RunningStatus status) {
		Logger.trace("status= {}",status);
		switch(status) {
		case RUNNING:
			startBt.setText(LanguageSet.cancelLabel());
			entireBt.setText(LanguageSet.pauseLabel());
//				startBt.setDisable(false);
//				entireBt.setDisable(false);
			break;
		case STOPPED:
			startBt.setText(LanguageSet.startLabel());
			entireBt.setText(LanguageSet.entireLabel());
			startBt.setDisable(false);
			entireBt.setDisable(false);
			break;
		case STOPPING:
			startBt.setDisable(true);
			entireBt.setDisable(true);
			break;
		case PAUSED:
//				startBt.setText(LanguageSet.cancelLabel());
			entireBt.setText(LanguageSet.resumeLabel());
//				startBt.setDisable(false);
//				entireBt.setDisable(false);
			break;
//				default: undefined error exception
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
		Rows.addRow();
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
     		   Logger.warn(Localization.get("ToomanyfilesdroppedWetakefirst2"));
     	   event.setDropCompleted(true);// event feedback: object successfully transferred
        }else
        	event.setDropCompleted(false);
        event.consume();
	}

	@SuppressWarnings("unchecked")
	@FXML public void langCbAction(ActionEvent event) {
		// TODO Das Umschalten wirkt sich bisher erst bei erneuter Verwendung aus. 
		// Die GUI sollte aber besser umgehend aktualisiert werden.
//TODO		ComboBox<Language> cb=(ComboBox<Language>) event.getSource();
		//TODO LanguageSet.setLanguage(cb.getValue().getName());
	}

	@SuppressWarnings("unchecked")
	@FXML public void recursionCbAction(ActionEvent event) {
		ComboBox<RecursionType> cb=(ComboBox<RecursionType>) event.getSource();
		fsco.setRecursion(cb.getValue());
	}
}
