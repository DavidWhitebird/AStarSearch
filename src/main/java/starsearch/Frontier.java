package starsearch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A frontier queue for the AStarSearch class. Each entry is an int array: 
 * City | f() | PrevCity
 */
public class Frontier {
	private List<int[]> frontier;
	private int processedCity; // The city being processed when frontier is updated.

	Frontier() {
		frontier = new ArrayList<int[]>();
	}

	/**
	 * Add a value to the frontier queue. This method will automatically sort the
	 * queue with the new value.
	 * 
	 * @param cityIndex
	 * @param fVal
	 * @param processingCity
	 */
	public void update(int cityIndex, int fVal, int processingCity) {
		int[] r = { cityIndex, fVal, processingCity };

		// check if city already in frontier, delete if so
		for (int i = 0; i < frontier.size(); i++) {
			if (frontier.get(i)[0] == cityIndex) {
				frontier.remove(i);
			}
		}
		// add the parameter city entry & sort frontier queue
		frontier.add(r);
		Collections.sort(frontier, new sortByfValue());
		this.processedCity = processingCity;
	}

	public int[] getEntry(int i) {
		return frontier.get(i);
	}

	/**
	 * Remove & return entry at front of frontier queue
	 * 
	 * @return
	 */
	public int[] pop() {
		int[] n = frontier.get(0);
		frontier.remove(0);
		return n;
	}

	/**
	 * Creates a copy of a frontier.
	 * 
	 * @return Frontier
	 */
	public Frontier copy() {
		Frontier f = new Frontier();
		List<int[]> copy = new ArrayList<>(frontier);
		f.frontier = copy;
		return f;
	}

	public int size() {
		return frontier.size();
	}

	public void remove() {
		frontier.remove(0);
	}

	/**
	 * Checks if elements are left in Frontier
	 * 
	 * @return True for for more cities
	 */
	public boolean hasMoreCities() {
		return !frontier.isEmpty();
	}

	public int entryCount() {
		return frontier.size();
	}

	public int getProcessedCity() {
		return processedCity;
	}

	public void addFinalDestination(int goalCity) {
		frontier.add(new int[0]);
		processedCity = goalCity;
	}

	/**
	 * Returns a one line String of the Frontier.
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		String s = "";
		String addString;
		for (int i = 0; i < frontier.size(); i++) {
			addString = ("  " + frontier.get(i)[0] + " " + frontier.get(i)[1] + "  " + frontier.get(i)[2] + " |");
			s = s + addString;
		}
		return s;
	}

	/**
	 * Returns a String of the Frontier with each entry on a newline.
	 * 
	 * @return String
	 */
	public String functionToStringByLine() {
		String s = "";
		for (int i = 0; i < frontier.size(); i++) {
			s = s.concat("city:   " + frontier.get(i)[0] + "\tf():   " + frontier.get(i)[1] + "\r\n");
		}
		return s;
	}

	/**
	 * Returns a String of the Frontier with each entry on a newline.
	 * 
	 * @return String
	 */
	public String toStringByLine() {
		String s = "";
		int align;
		String spaces = "";
		for (int i = 0; i < frontier.size(); i++) {
			align = 3 - String.valueOf(frontier.get(i)[1]).length();
			for (int j = 0; j < align; j++) {
				spaces = spaces.concat(" ");
			}
			s = s.concat("city:   " + frontier.get(i)[0] + "\tf():   " + frontier.get(i)[1] + spaces
					+ "\t\tprevCity:   " + frontier.get(i)[2] + "\r\n");
		}
		return s;
	}
}

/**
 * Used for sorting the frontier. A simple implementation of Comparator to have
 * a compare method for sorting the frontier.
 */
class sortByfValue implements Comparator<int[]> {
	@Override
	public int compare(int[] o1, int[] o2) {
		if (o1[1] > o2[1]) {
			return 1;
		}
		if (o1[1] < o2[1]) {
			return -1;
		}
		return 0;
	}
}
