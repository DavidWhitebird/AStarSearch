package starsearch;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A graph with data rows/columns starting at zero. Creates an index of
 * the title data.
 */
public class Graph {
	public int[][] graph;
	public String valueSeparator = "\\s+"; // Value separator for input files
	public int noEdgeValue = 999; // Value for unknown path costs
	public String[][] titleIndex; // Index of names/headers of rows/columns
	private File inputFile;
	private int maxNameLength = 20; // to align string output

	Graph(File f) {
		inputFile = f;
		importDataFile();
	}

	/**
	 * Assumes 1st row & column are headers, does not include them in output graph
	 * This will also set the values of titleIndex when it runs.
	 * 
	 * @param inputDirectory
	 *            file with input data
	 * @return graph of the file in int[][] format.
	 */
	public void importDataFile() {
		Scanner scanner;
		int columns = 0, rows = 0;
		String line = "";

		try {
			scanner = new Scanner(inputFile);
			while (scanner.hasNextLine()) {
				rows++;
				scanner.nextLine();
			}
			rows--; // this excludes column title row
			scanner.close();
			scanner = new Scanner(inputFile);
			line = scanner.nextLine();

			String[] rRowTokens = line.split(valueSeparator);
			columns = rRowTokens.length; // this excludes row description column

			// Add column titles to titleIndex
			titleIndex = new String[rows][2];
			for (int i = 1; i < rRowTokens.length; i++) {
				titleIndex[i - 1][1] = rRowTokens[i];
			}
			scanner.close();

			// Initialize Graph
			graph = new int[rows][columns];
			scanner = new Scanner(inputFile);

			// skip the column titles
			scanner.nextLine();
			// start with the first row at index 1.
			for (int i = 1; scanner.hasNextLine(); i++) {
				line = scanner.nextLine();
				rRowTokens = line.split(valueSeparator);
				// add row titles to title index
				titleIndex[i - 1][0] = rRowTokens[0];
				for (int j = 1; j < rRowTokens.length; j++) {
					String rowToken = rRowTokens[j];
					if (rowToken.equals("-")) {
						graph[i - 1][j - 1] = noEdgeValue;
					} else {
						graph[i - 1][j - 1] = Integer.parseInt(rowToken);
					}
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
		}
	}

	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < graph.length; i++) {
			for (int j = 0; j < graph.length; j++) {
				s = s.concat("\t" + graph[i][j]);
			}
			s = s.concat("\r\n");
		}
		return s;
	}

	/**
	 * Puts together a string version of the city index.
	 * 
	 * @return A single string which prints as an aligned index given mono-spaced
	 *         font
	 */
	public String titleIndexToStringAligned() {
		String s = "";
		String addString;
		String align = "";
		int length = 0;
		for (int i = 0; i < titleIndex.length; i++) {
			// find how much space to add between tabs (for aligning title)
			length = titleIndex[i][0].length();
			length = maxNameLength - length;
			align = "";
			for (int m = 0; m < length; m++) {
				align = align.concat(" ");
			}
			addString = (i + "\t" + titleIndex[i][0] + align + "\t" + titleIndex[i][1] + "\r\n");
			s = s + addString;
		}
		s.trim();
		return s;
	}

	/**
	 * Returns the title/header index of columns/rows.
	 * 
	 * @return
	 */
	public String[][] getTitleIndex() {
		return titleIndex;
	}

	/**
	 * Searches the city index for the parameter name/city code, and return the
	 * parameter city's graph index number.
	 * 
	 * @param cityName
	 *            Can be either the full city name or the city code.
	 * @return the index of the city.
	 */
	public int getCityIndex(String cityName) {
		for (int i = 0; i < titleIndex.length; i++) {
			if (titleIndex[i][0].equals(cityName)) {
				return i;
			}
		}
		for (int i = 0; i < titleIndex.length; i++) {
			if (titleIndex[i][1].equals(cityName)) {
				return i;
			}
		}
		return -1;
	}

	public String getCityName(int index) {
		return titleIndex[index][0];
	}

	public String getCityCode(int index) {
		return titleIndex[index][1];
	}

	/**
	 * Returns the distance between parameter cities
	 * 
	 * @param sourceCity
	 * @param goalCity
	 * @return int distance
	 */
	public int getDistance(int sourceCity, int goalCity) {
		if (sourceCity == goalCity) {
			return 0;
		}
		if (sourceCity < goalCity) {
			return graph[sourceCity][goalCity];
		}
		return graph[goalCity][sourceCity];
	}

	public int length() {
		return graph.length;
	}

	/**
	 * Puts together a list of the city names.
	 * 
	 * @return ArrayList<String> of city names.
	 */
	public ArrayList<String> getCityList() {
		ArrayList<String> cityList = new ArrayList<String>(graph.length);
		for (int i = 0; i < graph.length; i++) {
			cityList.add(i, getCityName(i));
		}
		return cityList;
	}

	/**
	 * Puts together list of neighbors for parameter city.
	 * 
	 * @param sourceCity
	 * @return ArrayList<Integer> of neighbors
	 */
	public ArrayList<Integer> getNeighbors(int sourceCity) {
		ArrayList<Integer> neighbors = new ArrayList<>();

		// get the column & row portions of the graph and parse it into an array
		for (int i = 0; i < sourceCity; i++)
			if (graph[i][sourceCity] < noEdgeValue) {
				neighbors.add(i);
			}
		for (int i = sourceCity; i < graph.length; i++) {
			if (graph[sourceCity][i] < noEdgeValue) {
				neighbors.add(i);
			}
		}
		return neighbors;
	}
}