package starsearch;

import java.util.ArrayList;
import java.util.List;

/**
 * Performs A* Search process on given input Graphs & source/destination cities.
 */
public class AStarSearch {
	private Graph distGraph;
	private Graph heurGraph;
	private int sourceCity;
	private int goalCity;

	private Frontier frontier;
	private List<Frontier> frontierTracker; // logs the frontier each time a city is processed
	private PathTracker pathTracker; // maintains most recent f() & prevCity for each city

	private int maxNumFrontierEntries; // counts required columns for output of frontier entries
	private int runCount; // count of cities processed

	/**
	 * Constructor
	 * 
	 * @param dist Graph with no titles for rows or columns
	 * @param heur Graph with no titles for rows or columns
	 */
	AStarSearch(Graph distGraph, Graph heurGraph) {
		this.distGraph = distGraph;
		this.heurGraph = heurGraph;
		frontier = new Frontier();
		frontierTracker = new ArrayList<Frontier>();
		maxNumFrontierEntries = 0;
		pathTracker = new PathTracker(distGraph, heurGraph);
	}

	/**
	 * Searches the Class graphs for the shortest route from source to goal
	 * 
	 * @param source
	 * @param goal
	 */
	public void search(String sourceCityName, String goalCityName) {
		this.sourceCity = distGraph.getCityIndex(sourceCityName);
		this.goalCity = distGraph.getCityIndex(goalCityName);
		boolean found = false; // used to end search loop when destination reached.

		// initialize pathTracker & frontier with starting & goal cities
		pathTracker.initializePathTracker(sourceCity, goalCity);
		frontier.update(sourceCity, pathTracker.getHeuristic(sourceCity), sourceCity);

		// calls primary search method for each city in the frontier queue until goal is
		// reached.
		while (!found && frontier.hasMoreCities()) {
			frontier = frontier.copy();
			found = processCity(frontier.pop()[0], goalCity);
		}
	}

	/**
	 * Calculates the next frontier from a start node towards a goal node.
	 * 
	 * @param parentCity
	 * @param goalCity
	 * @return true if goal City is reached. False otherwise.
	 */
	public boolean processCity(int parentCity, int goalCity) {

		// get neighbors of city to process
		ArrayList<Integer> neighbors = distGraph.getNeighbors(parentCity);

		// calculate cost to parent city & it's neighbors
		int milesToParentCity = pathTracker.getMilesToCity(parentCity);

		// for each neighbor, calculate f(), update pathTracker
		for (int i = 0; i < neighbors.size(); i++) {
			int nextCity = neighbors.get(i);
			int milesToNextCity = distGraph.getDistance(parentCity, nextCity);

			// calculate total estimated path cost for next city
			int costFunction = heurGraph.getDistance(nextCity, goalCity) + milesToParentCity + milesToNextCity;

			// if old f() > than new f(), update path tracker & frontier queue
			if (pathTracker.getCostFunction(nextCity) > costFunction) {

				// update pathTracker and frontier
				pathTracker.setCostFunction(nextCity, costFunction);
				pathTracker.setPreviousCity(nextCity, parentCity);
				pathTracker.setMilesToCity(nextCity, milesToParentCity + milesToNextCity);
				frontier = frontier.copy();
				frontier.update(nextCity, costFunction, parentCity);

				// update frontierTracker and it's city index
				frontierTracker.add(frontier);
				runCount++;
			}
			maxNumFrontierEntries = Math.max(frontier.size(), maxNumFrontierEntries);

			// end search if goal city found
			if (nextCity == goalCity) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Calls frontier method to get the actual shortest path found.
	 * 
	 * @return
	 */
	public String pathTakenToString() {
		return pathTracker.pathTakenToString();
	}

	public String pathTrackerToString() {
		return pathTracker.toString();
	}

	/**
	 * Returns A String containing the entire log of frontiers, formatted as a
	 * table.
	 * 
	 * @return String
	 */
	public String frontierTrackerToString() {
		String s = "City |";
		String tempS = "";
		for (int i = 0; i < maxNumFrontierEntries; i++) { // append the title row
			s = s.concat(" City f() Prev |");
		}
		s = s.concat("\r\n-----|");
		for (int i = 0; i < maxNumFrontierEntries; i++) { // append dashed line
			s = s.concat("---------------|");
		}
		s = s.concat("\r\n");
		for (int i = 0; i < frontierTracker.size(); i++) { // append all entries in frontier #i
			tempS = tempS.concat(
					distGraph.getCityCode(frontierTracker.get(i).getProcessedCity()) + "  |" + frontierToString(i));
			for (int j = frontierTracker.get(i).size(); j < maxNumFrontierEntries; j++) { // add empty column lines
				tempS = tempS.concat("               |");
			}
			tempS = tempS.concat("\r\n");
		}
		s = s.concat(tempS);
		s = s.concat(distGraph.getCityCode(goalCity) + "  |");
		tempS = "";
		for (int j = 0; j < maxNumFrontierEntries; j++) { // add empty column lines
			tempS = tempS.concat("               |");
		}
		s = s.concat(tempS);
		s = s.trim();
		return s;
	}

	/**
	 * Returns a string of a single Frontier.
	 * 
	 * @param frontierNum
	 * @return String of single Frontier.
	 */
	public String frontierToString(int frontierNum) {
		String cityTs;
		String fTs;
		String prevTs;
		String s = "";
		int align;
		String spaces = "";
		Frontier tempFrontier;
		int[] tempFrontierEntry;
		tempFrontier = frontierTracker.get(frontierNum);

		for (int i = 0; i < tempFrontier.entryCount(); i++) {
			tempFrontierEntry = tempFrontier.getEntry(i);
			cityTs = distGraph.getCityCode(tempFrontierEntry[0]);
			fTs = String.valueOf(tempFrontierEntry[1]);
			prevTs = distGraph.getCityCode(tempFrontierEntry[2]);
			align = 3 - String.valueOf(fTs).length();
			spaces = "";
			for (int j = 0; j < align; j++) {
				spaces = spaces.concat(" ");
			}
			s = s.concat("  " + cityTs + " " + fTs + spaces + "  " + prevTs + " |");
		}
		return s;
	}

	public int getRunCount() {
		return runCount;
	}
}
