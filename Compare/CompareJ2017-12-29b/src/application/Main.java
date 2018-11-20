package application;

import javafx.application.Application;
import javafx.stage.Stage;
import li.tmj.ui.fx.FxScene;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			li.tmj.app.Application.init();

			primaryStage.setScene( FxScene.MAIN.get() );
			primaryStage.show();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
