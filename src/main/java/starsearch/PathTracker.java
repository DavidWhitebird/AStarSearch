package starsearch;

/**
 * Used to track the progress of AStarSearch frontier. Contains methods to
 * analyze search output and return results. Each array in the set contains:
 * city | f() | LCP ancestor city | heuristic to goal city | LCP miles to city
 */
public class PathTracker {
	private int[][] pathTracker;
	private int[] pathTaken;
	private Graph distGraph;
	private Graph heurGraph;
	private int goalCity;
	private int sourceCity;

	// These are for readability, labels for pathTracker entries
	private final int cityCol = 0;
	private final int costFunctionCol = 1;
	private final int prevCityCol = 2;
	private final int heurCol = 3;
	private final int lcpMilesCol = 4;

	private final int numArrayColumns = 5; // Size of array for each city
	private final int noPathYet = 9999; // f() & LCP initialization value
	private final int startCityPrevCity = noPathYet;

	/**
	 * 1st Graph is distance Graph, 2nd Graph is heuristic Graph.
	 * 
	 * @param distGraph
	 * @param heurGraph
	 */
	PathTracker(Graph distGraph, Graph heurGraph) {
		this.distGraph = distGraph;
		this.heurGraph = heurGraph;
	}

	/**
	 * Initializes the path tracker for a sourceCity & goalCity. The heuristic to
	 * the goal city will be added for every city.
	 * 
	 * @param sourceCity
	 * @param goalCity
	 */
	public void initializePathTracker(int sourceCity, int goalCity) {
		this.goalCity = goalCity;
		this.sourceCity = sourceCity;
		// create the container with an array for each city in the graph
		pathTracker = new int[distGraph.length()][numArrayColumns];

		// assign each array in container city, heuristic to goal city, & noPathYet to
		// other values.
		for (int i = 0; i < pathTracker.length; i++) {
			pathTracker[i][cityCol] = i;
			pathTracker[i][costFunctionCol] = noPathYet;
			pathTracker[i][heurCol] = heurGraph.getDistance(i, goalCity);
			pathTracker[i][lcpMilesCol] = noPathYet;
			pathTracker[i][prevCityCol] = noPathYet;
		}
		// create special case entry for sourceCity
		pathTracker[sourceCity][costFunctionCol] = heurGraph.getDistance(sourceCity, goalCity); // f = heuristic for
																								// source
		pathTracker[sourceCity][prevCityCol] = startCityPrevCity; // source is self for start city
		pathTracker[sourceCity][lcpMilesCol] = 0; // 0 miles to get to the start city from the start city
	}

	/**
	 * Traces backwards through the PathTracker to obtain the specific path to take
	 * from source to goal. This path is stored in PathTracker.pathTaken
	 */
	public void findPathTaken() {
		int currentCity = goalCity;
		int index;
		int arraySize = 1;

		// find length needed for array
		while (pathTracker[currentCity][prevCityCol] != noPathYet) {
			arraySize++;
			currentCity = pathTracker[currentCity][prevCityCol];
		}
		pathTaken = new int[arraySize];
		currentCity = goalCity;
		for (index = arraySize - 1; index > 0; index--) {
			pathTaken[index] = currentCity;
			currentCity = pathTracker[currentCity][prevCityCol];
		}
		pathTaken[0] = sourceCity;
	}

	/**
	 * Provides a table formatted String list of the path taken from source city to
	 * goal city. city code | city | next city | distance | total miles
	 * 
	 * @return String
	 */
	public String pathTakenToString() {
		String s = "";
		int city;
		String cityCode;
		String cityName;
		String nextCity = "";
		String distance = "";
		String milesTraveled;
		int length = 20; // size of largest city, for formatting

		// ensure pathTaken has been calculated
		if (pathTaken == null) {
			findPathTaken();
		}

		// create headers for output
		s = s.concat("City#   \tCity Name\t\tNext City\t\tDistance\t\tMiles Traveled\r\n");

		// obtain values for String variables and add to output String
		for (int i = 0; i < pathTaken.length; i++) {
			city = pathTaken[i];
			cityCode = distGraph.getCityCode(city);
			cityName = distGraph.getCityName(city);
			milesTraveled = String.valueOf(pathTracker[city][lcpMilesCol]);

			String align = "";

			for (int m = 0; m < length - cityName.length(); m++) {
				align = align.concat(" ");
			}
			if (i < pathTaken.length - 1) {
				nextCity = String.valueOf(distGraph.getCityCode(pathTaken[i + 1]));
				distance = String.valueOf(distGraph.getDistance(city, pathTaken[i + 1]));
			}
			if (i == pathTaken.length - 1) {
				nextCity = "-";
				distance = "-";
			}
			s = s.concat(cityCode + "\t\t" + cityName + align + "\t" + nextCity + "\t\t\t" + distance + "\t\t\t"
					+ milesTraveled + "\r\n");
		}
		return s;
	}

	public void setCostFunction(int city, int value) {
		pathTracker[city][costFunctionCol] = value;
	}

	public void setPreviousCity(int city, int previousCity) {
		pathTracker[city][prevCityCol] = previousCity;
	}

	public void setMilesToCity(int city, int milesToCity) {
		pathTracker[city][lcpMilesCol] = milesToCity;
	}

	public void setHeuristic(int city, int heuristicToGoalCity) {
		pathTracker[city][heurCol] = heuristicToGoalCity;
	}

	public int getMilesToCity(int city) {
		return pathTracker[city][lcpMilesCol];
	}

	public int getPreviousCity(int city) {
		return pathTracker[city][prevCityCol];
	}

	public int getCostFunction(int city) {
		return pathTracker[city][costFunctionCol];
	}

	public int getHeuristic(int city) {
		return pathTracker[city][heurCol];
	}

	@Override
	public String toString() {
		String s = "";
		String addString;
		addString = ("index\tf()\tpreCity\theur\tlcpMls\tCity#\tCity");
		s = s + addString + "\r\n";
		for (int i = 0; i < pathTracker.length; i++) {
			addString = (pathTracker[i][cityCol] + "\t" + pathTracker[i][costFunctionCol] + "\t"
					+ pathTracker[i][prevCityCol] + "\t" + pathTracker[i][heurCol] + "\t" + pathTracker[i][lcpMilesCol]
					+ "\t" + distGraph.getCityCode(i) + "\t" + distGraph.getCityName(i));
			s = s + addString + "\r\n";
		}
		s.trim();
		return s;
	}
}
