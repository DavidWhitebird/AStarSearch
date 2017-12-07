package starsearch;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import java.io.File;

import javafx.geometry.*;

/**
 * Utility to easily display a popup window notification.
 */
public class AlertBox {

	/**
	 * Display a window to notify user of an event. User must be close window before
	 * returning to previous.
	 * 
	 * @param title String giving window title.
	 * @param message String of message inside window.
	 */
	public static void display(String title, String message) {
		Stage stage = new Stage();

		// Block events to other windows
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle(title);
		stage.setMinWidth(250);

		Label label = new Label();
		label.setText(message);
		Button closeButton = new Button("Close");
		closeButton.setOnAction(e -> stage.close());

		VBox vBox = new VBox();
		vBox.getChildren().addAll(label, closeButton);
		VBox.setMargin(label, new Insets(5, 5, 5, 5));
		VBox.setMargin(closeButton, new Insets(5, 5, 5, 5));
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

}