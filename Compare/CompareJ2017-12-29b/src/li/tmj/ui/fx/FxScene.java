package li.tmj.ui.fx;

import java.io.IOException;
import javafx.scene.Scene;

public enum FxScene {
	MAIN ("Main");
	
	private String fileName;
	
	private FxScene(String fileName) {
		this.fileName=fileName;
	}
	
	public Scene get() throws IOException {
		return FxManager.get(fileName);
	}
}
