package starsearch;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import java.io.File;
import javafx.geometry.*;

/**
 * Utility to display a popup window directoryChooser.
 * Use getInputDirectory() after calling to obtain chosen directory.
 */
public class AlertBoxFileChooser {
	static File workingDirectory;
	static File inputDirectory;
	static String inputFileName;
	static Stage stage;
	
	/**
	 * Display a window to notify user of an event. User must be close window before
	 * returning to previous.
	 * @param title String giving window title.
	 * @param message String of message inside window.
	 */
	public static void display(String title, String message) {
		stage = new Stage();
		workingDirectory = new File(System.getProperty("user.dir"));

		// Block events to other windows
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle(title);
		stage.setMinWidth(250);
		
		
	    Button directoryInChooserButton = new Button("Select directory of input files");
	    directoryInChooserButton.setOnAction(e -> {
	    	chooseInputDirectory(workingDirectory);
	    	notifyIn();
	    });
		
	    Button confirmDirectory = new Button("Confirm Selection");
	    confirmDirectory.setOnAction(e -> {
	    	stage.close();
	    });

		Label label = new Label();
		label.setText(message);
		Button closeButton = new Button("Close");
		closeButton.setOnAction(e -> stage.close());

		VBox vBox = new VBox();
		vBox.getChildren().addAll(label, directoryInChooserButton, confirmDirectory);
		VBox.setMargin(label, new Insets(5, 5, 5, 5));
		VBox.setMargin(confirmDirectory, new Insets(5, 5, 5, 5));
		VBox.setMargin(directoryInChooserButton, new Insets(5, 5, 5, 5));
		vBox.setAlignment(Pos.CENTER);

		// Display window and wait for it to be closed before returning
		Scene scene = new Scene(vBox);
		// CSS stylesheet
		File f = new File(System.getProperty("user.dir")+"\\src\\main\\resources\\" +"style.css");
		scene.getStylesheets().clear();
		scene.getStylesheets().add( "file:///" + f.getAbsolutePath().replace("\\", "/"));
		stage.setScene(scene);
		stage.showAndWait();
	}

	/**
	 * Opens directoryChooser for user to select input File directory.
	 * @param directory File of desired starting browsing directory.
	 * Sets static inputDirectory variable to File chosen by user.
	 */
	public static void chooseInputDirectory(File directory){
		DirectoryChooser directoryChooser = new DirectoryChooser();
	    directoryChooser.setInitialDirectory(directory);
	    inputDirectory = directoryChooser.showDialog(stage);
	}
	
	/**
	 * Notifies user of input directory setting change:
	 * 	Creates pop-up window of file path
	 */
	public static void notifyIn(){
		if(inputDirectory != null){
			AlertBox.display("Notification", "Input directory has been set to:\r\n"
				+inputDirectory.getAbsolutePath());
		}
	}
	/**
	 * @return the inputDirectory value
	 */
	public static File getInputDirectory() {
		return inputDirectory;
	}
}