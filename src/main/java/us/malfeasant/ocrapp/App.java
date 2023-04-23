package us.malfeasant.ocrapp;

import java.io.IOException;
import java.nio.file.Path;
import java.util.prefs.Preferences;

import org.tinylog.Logger;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import us.malfeasant.ocrapp.sup.ReadSUP;
import us.malfeasant.ocrapp.sup.UnknownSegmentTypeException;

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
		pane.setOnDragOver(e -> handleDragOver(e));
		pane.setOnDragDropped(e -> handleDrop(e));
		importItem.setOnAction(event -> {
			if (modified && inputFile != null) {
				var alert = new Alert(AlertType.CONFIRMATION, "Current file will be lost- proceed?");
				var response = alert.showAndWait();
				if (!(response.isPresent() && response.get() == ButtonType.OK)) {
					return;	// abort the import
				}
			}
			var chooser = new FileChooser();
			chooser.setTitle("Import graphical subtitles");
			chooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Subtitle files",
				"*.sup", "*.idx", "*.sub"));
			var chosenFile = chooser.showOpenDialog(stage);
			if (chosenFile != null) {	// could be null if dialog cancelled...
				importFile(chosenFile.toPath());
			}
		});
		Scene scene = new Scene(pane, winWidth, winHeight);
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void importFile(Path f) {	// f should not be null
		var name = f.getFileName().toString();
		var lastDot = name.lastIndexOf(".");
		var ext = name.substring(lastDot + 1);
		if (ext.equals("sup")) {
			Logger.info("Got a .sup file!");
			ReadSUP sup;
			try {
				sup = new ReadSUP(f);
			} catch (UnknownSegmentTypeException e) {
				// TODO Could potentially recover from this- maybe pop up dialog
				// asking if user wants to continue?
				// On second thought, no- since it's backed by an enum type,
				// anything unknown has to be an error that stops processing.
				// TODO alert user, reset ui 
			} catch (IOException e) {
				// TODO alert user, reset ui
				e.printStackTrace();
			} catch (DecodeException e) {
				// TODO alert user, reset ui
			}
		} else if (ext.equals("idx") || ext.equals("sub")) {
			// We allow the user to pick either .idx or .sub, but in reality, only the .sub
			// is needed- if .idx selected, we just use it to find the .sub file.
			// TODO Maybe identify the file type by reading it rather than by extension?
			Logger.info("Got a .idx/.sub file!");
			// find & handle .sub file
		}
		inputFile = f;
	}

	private void handleDragOver(DragEvent event) {
		var dragBoard = event.getDragboard();
		if (dragBoard.hasFiles() && dragBoard.getFiles().size() == 1) {
			// only want single files, no multiples
			event.acceptTransferModes(TransferMode.ANY);
			event.consume();
		}
	}

	private void handleDrop(DragEvent event) {
		var dragBoard = event.getDragboard();
		if (dragBoard.hasFiles()) {
			event.setDropCompleted(true);
			event.consume();
			Logger.info("Files dropped: " + dragBoard.getFiles());
			if (modified) {
				// TODO offer to save output, cancel -> early return
			}
			importFile(dragBoard.getFiles().get(0).toPath());
		}
	}
}