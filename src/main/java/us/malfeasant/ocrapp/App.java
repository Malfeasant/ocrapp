package us.malfeasant.ocrapp;

import java.io.IOException;
import java.nio.file.Path;

import org.tinylog.Logger;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class App extends Application {
	private final ReadOnlyStringWrapper fileName = new ReadOnlyStringWrapper();
	// TODO bind fileName to inputFile somehow...
	private SubtitleFile inputFile;
	// Subtitle file that OCR is being performed on.
	// If no file has been imported yet, can be null.
	private Path outputFile;	// Text file of OCR results- can be null if hasn't been saved yet.
	// TODO replace Path with something more specific to text file writing...
	private boolean modified = true;	// TODO: true is only for debugging...

	private final ObservableList<SubPicture> subList = FXCollections.observableArrayList();
	private final ListView<SubPicture> listView = new ListView<>(subList);
	// Listview of all subtitles- display as timestamps
	private final ImageView subImage = new ImageView();
	// where decoded image of selected subtitle is displayed
	private final TextArea subText = new TextArea();
	// where text of the selected subtitle is displayed

	public void start(Stage stage) {
		//var prefs = Preferences.userNodeForPackage(getClass());
		// removing this until I have a use for it...
		stage.titleProperty().bind(Bindings.concat(
			"OCRApp", 
			Bindings.when(fileName.isNotEmpty()).then(
				Bindings.concat(" - ", fileName.getReadOnlyProperty())
			).otherwise("")
			// Is there a better way to do this that doesn't require otherwise()?
			// Meh, it works.
		));

		subImage.setFitWidth(320);
		subImage.setFitHeight(240);
		subImage.setPreserveRatio(true);

		listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		listView.setCellFactory(lv -> {
			return new ListCell<>() {
				@Override
				protected void updateItem(SubPicture item, boolean empty) {
					super.updateItem(item, empty);

					setText(item == null ? "" : item.prettyPrint());
				}
			};
		});

		var importItem = new MenuItem("Import...");
		var exitItem = new MenuItem("Exit");
		var fileMenu = new Menu("File", null, importItem, new SeparatorMenuItem(), exitItem);
		var mBar = new MenuBar(fileMenu);
		var pane = new BorderPane(subImage, mBar, subText, null, listView);	// TODO more nodes...
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
		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.sizeToScene();
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
				// If user clicks ok, we can discard the file(s)
				modified = !bt.equals(ButtonType.OK);
				Logger.debug("{} clicked.", bt);
			});
		}
		if (!modified) {	// need to check again because it might have changed
			subList.clear();
			inputFile = null;
			fileName.set("");
		}
		Logger.debug("checkDiscard() Returning {}.", !modified);
		return !modified;
	}

	private void importFile(Path f) {	// f should not be null
		try {
			inputFile = new SubtitleFile(f);
			fileName.set(f.toAbsolutePath().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DecodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			if (checkDiscard()) {
				importFile(dragBoard.getFiles().get(0).toPath());
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}