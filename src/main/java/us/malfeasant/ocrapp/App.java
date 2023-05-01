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

public class App extends Application {
	
	private SubtitleFile inputFile;	// Subtitle file that OCR is being performed on.
	// If no file has been imported yet, can be null.
	private Path outputFile;	// Text file of OCR results- can be null if hasn't been saved yet.
	// TODO replace Path with something more specific to text file writing...
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
			if (checkDiscard()) {
				var chooser = new FileChooser();
				chooser.setTitle("Import graphical subtitles");
				chooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("Subtitle files",
					"*.sup", "*.idx", "*.sub"));
				var chosenFile = chooser.showOpenDialog(stage);
				if (chosenFile != null) {	// shouldn't be, but why risk it...
					inputFile = null;
					importFile(chosenFile.toPath());
				}
			}
		});
		Scene scene = new Scene(pane, winWidth, winHeight);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * If file modified, alert user, allow aborting whatever action
	 * @return true if ok to proceed
	 */
	private boolean checkDiscard() {
		if (modified) {
			var alert = new Alert(AlertType.CONFIRMATION,
				"Unsaved output will be lost- proceed?");
			alert.showAndWait().ifPresent(bt -> {
				modified = !bt.equals(ButtonType.OK);
				Logger.debug("{} clicked.", bt);
			});
		}
		Logger.debug("Returning {}.", !modified);
		return !modified;
	}
	public static void main(String[] args) {
		launch(args);
	}

	private void importFile(Path f) {	// f should not be null
		if (checkDiscard()) {
			try {
				inputFile = new SubtitleFile(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DecodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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