package us.malfeasant.ocrapp;

import java.io.File;
import java.util.prefs.Preferences;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class App extends Application {
	private File inputFile;	// Subtitle file that OCR is being performed on.  If no file has been imported yet, can be null.
	private File outputFile;	// Text file of OCR results- can be null if hasn't been saved yet.
	private boolean modified = true;	// new file is by definition not modified	TODO: true is only for debugging...
	
    public void start(Stage stage) {
    	var prefs = Preferences.userNodeForPackage(getClass());
    	int winWidth = prefs.getInt("WinWidth", 640);
    	int winHeight = prefs.getInt("WinHeight", 480);
    	
    	var importItem = new MenuItem("Import...");
    	var exitItem = new MenuItem("Exit");
    	var fileMenu = new Menu("File", null, importItem, new SeparatorMenuItem(), exitItem);
    	var mBar = new MenuBar(fileMenu);
    	var pane = new BorderPane(null, mBar, null, null, null);	// TODO more nodes...
        importItem.setOnAction(e -> {
        	if (modified && inputFile != null) {
        		var alert = new Alert(AlertType.CONFIRMATION, "Current file will be lost- proceed?");
        		var response = alert.showAndWait();
        		if (!(response.isPresent() && response.get() == ButtonType.OK)) {
       				return;	// abort the import
        		}
        	}
        	var chooser = new FileChooser();
        	chooser.setTitle("Import graphical subtitles");
        	chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Subtitle files", "*.sup", "*.idx"));
        	var chosenFile = chooser.showOpenDialog(stage);
        	if (chosenFile != null) {	// could be null if dialog cancelled...
        		importFile(chosenFile);
        	}
        });
    	Scene scene = new Scene(pane, winWidth, winHeight);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    private void importFile(File f) {	// f should not be null
    	inputFile = f;
    }
}