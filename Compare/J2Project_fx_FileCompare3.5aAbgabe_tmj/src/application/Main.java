package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import tmj.app.Setup;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		Setup.init();		
		
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("/tmj/ui/fxml/FileCompare.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/tmj/ui/FileCompare.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

