package li.tmj.ui.fx;

import java.io.IOException;
import java.net.URL;
import org.pmw.tinylog.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class FxManager {
	public static URL getFxRessource(String relativePath) {
		Logger.trace("relativePath={}, url=...",relativePath);
		URL url= FxManager.class.getResource(relativePath);
		Logger.trace("relativePath={}, url={}",relativePath,url);
		return url;
	}
	
	/**
	 * Searches for <enum's fileName>.fxml und application.css in its class folder
	 * and initializes the FX Scene with it.
	 * @return The FX Scene
	 * @throws IOException falls Load fehl schlägt.
	 */
	public static Scene get(String fileName) throws IOException {
		Scene scene = new Scene( FXMLLoader.load(getFxRessource(fileName+".fxml")) );
		//UnsupportedOperationException - add is not supported by this list. Ist es aber bei ObservableList.
		//ClassCastException - class of element prevents add. Bei URL nicht der Fall.
		//IllegalArgumentException - property of element prevents add. Nicht bei URL.
		//NullPointerException - element==null and list does not permit null elements. Nicht der Fall bei ObservableList.
		scene.getStylesheets().add(getFxRessource("application.css").toExternalForm());
		return scene;
	}
}
