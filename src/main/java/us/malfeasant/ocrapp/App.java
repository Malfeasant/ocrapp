package us.malfeasant.ocrapp;

import java.util.prefs.Preferences;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class App extends Application {
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
        	var chooser = new FileChooser();
        	chooser.setTitle("Import graphical subtitles");
        	chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Subtitle files", "*.sup", "*.idx"));
        	var chosenFile = chooser.showOpenDialog(stage);
        	if (chosenFile != null) {
        		// TODO
        	}
        });
    	Scene scene = new Scene(pane, winWidth, winHeight);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    
}