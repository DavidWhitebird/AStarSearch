package starsearch;


import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Driver UI for AStarSearch
 *
 */
public class MainUI extends Application {
	// UI variables
	Stage stage1;
	Scene scene;
	static File inputFile;
	static File outputFile;
	static File workingDirectory;
	static File inputDirectory;
	static String appendFileText = "_out.txt";

	// Search Variables
	public static String valueSeparator = "\\s+";
	public static String inputFileDistanceName = "MnDOTactualDistances-spaces.txt";
	public static File inputFileDistance;
	public static String inputFileHeuristicName = "MnDOTheuristicDistances-spaces.txt";
	public static File inputFileHeuristic;
	public static String outputFileName;
	public static Graph distGraph;
	public static Graph heurGraph;
	public static String startCity;
	public static int startCityIndex;
	public static String goalCity;
	public static int goalCityIndex;
	public static List<String> cityNames; // For user selection

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		workingDirectory = new File(System.getProperty("user.dir"));
		inputDirectory = workingDirectory;

		// Initialize input/output files
		inputFileDistance = new File(workingDirectory + "\\src\\main\\resources", inputFileDistanceName);
		inputFileHeuristic = new File(workingDirectory + "\\src\\main\\resources", inputFileHeuristicName);
		
		while(!inputFileDistance.isFile()) {
			updateInputFilePath();
			inputFileDistance = new File(inputDirectory, inputFileDistanceName);
			inputFileHeuristic = new File(inputDirectory, inputFileHeuristicName);
		}


		// Create graphs
		distGraph = new Graph(inputFileDistance);
		heurGraph = new Graph(inputFileHeuristic);

		// Create list of cities for user to choose from
		cityNames = distGraph.getCityList();
		List<String> list = new ArrayList<String>(cityNames);
		ObservableList<String> observableList = FXCollections.observableList(list);

		// Source City Chooser
		ComboBox<String> cbSourceCity = new ComboBox<>(observableList);
		cbSourceCity.setOnAction((event) -> {
			startCity = cbSourceCity.getValue();
		});
		// Goal City Chooser
		ComboBox<String> cbGoalCity = new ComboBox<>(observableList);
		cbGoalCity.setOnAction((event) -> {
			goalCity = cbGoalCity.getValue();
		});

		// UI Buttons & Labels
		Button searchButton = new Button("Run Search");
		
		// Notify user that output file of search results has been created
		searchButton.setOnAction(e -> {
			runSearch();
			AlertBox.display("Search Completed", "Output results file written at  at \r\n" + workingDirectory+"\\target");
		});
		Label labSourceCity = new Label("Source City");
		Label labGoalCity = new Label("Goal City");

		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(25, 25, 25, 25));
		gridPane.add(labSourceCity, 0, 0);
		gridPane.add(cbSourceCity, 1, 0);
		gridPane.add(labGoalCity, 0, 1);
		gridPane.add(cbGoalCity, 1, 1);
		gridPane.add(searchButton, 2, 1);

		scene = new Scene(gridPane, 400, 100);
		
		// CSS stylesheet
		File f = new File(workingDirectory+"\\src\\main\\resources\\" +"style.css");
		scene.getStylesheets().clear();
		scene.getStylesheets().add( "file:///" + f.getAbsolutePath().replace("\\", "/"));
		
		Stage stage = primaryStage;
		stage.setTitle("A* Search MN");
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Primary method to run search.
	 */
	public void runSearch() {
		// look up cities to get index
		startCityIndex = distGraph.getCityIndex(startCity);
		goalCityIndex = distGraph.getCityIndex(goalCity);

		// Instantiate the search Class with the input graphs
		AStarSearch search = new AStarSearch(distGraph, heurGraph);
		search.search(startCity, goalCity);

		// Create output file
		workingDirectory = new File(System.getProperty("user.dir"));
		outputFileName = (startCity + "-" + goalCity + ".txt");
		outputFile = new File(workingDirectory+ "\\target", outputFileName);

		// Print results to file
		try {
			FileWriter fw = new FileWriter(outputFile, true);
			fw.write(search.frontierTrackerToString() + "\r\n\r\n");
			fw.write("\t\t*** Cities gone through, from source to destination ***\r\n");
			fw.write(search.pathTakenToString());
			fw.close();
		} catch (IOException e) {
		}
	}
	/**
	 * Called if input files not found.
	 * Calls a fileChooser for user to select input file.
	 * 
	 */
	private void updateInputFilePath() {
		AlertBoxFileChooser.display("Input Files Not Found", "Please select directory of file location.");
		inputDirectory = AlertBoxFileChooser.getInputDirectory();
	}
}
