package li.tmj.ui.fx;

import java.io.IOException;
import javafx.scene.Scene;
import javafx.stage.Stage;

public enum FxScene {
	MAIN ("Main"),
	DETAIL ("Detail");
	
	private String fileName;
	
	private FxScene(String fileName) {
		this.fileName=fileName;
	}
	
	public Scene get() throws IOException {
		return FxManager.get(fileName);
	}
	
	public void showOnStage(Stage stage) throws IOException {
		stage.setScene(get());
		stage.show();
	}
	
	public void showOnNewStage() throws IOException {
		showOnStage(new Stage());
	}
}
	
