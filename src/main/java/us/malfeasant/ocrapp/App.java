package us.malfeasant.ocrapp;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Path;
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
import us.malfeasant.ocrapp.sup.ReadSUP;

public class App extends Application {
	private Path inputFile;	// Subtitle file that OCR is being performed on.  If no file has been imported yet, can be null.
	private Path outputFile;	// Text file of OCR results- can be null if hasn't been saved yet.
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
				try {
					importFile(chosenFile.toPath());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        });
    	Scene scene = new Scene(pane, winWidth, winHeight);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    private void importFile(Path f) throws IOException {	// f should not be null
    	inputFile = f;
    	var name = f.getFileName().toString();
    	var lastDot = name.lastIndexOf(".");
    	var ext = name.substring(lastDot + 1);
    	if (ext.equals("sup")) {
    		System.out.println("Got a .sup file!");
    		var sup = new ReadSUP(f);
    	} else if (ext.equals("idx")) {
    		System.out.println("Got a .idx file!");
    	}
    }
}