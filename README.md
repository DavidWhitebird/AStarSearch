# AStarSearch

Demonstration of A* search. Based on Poole & Mackworth's presentation in Artificial Intelligence: Foundations of Computational Agents. -http://artint.info/html/ArtInt_57.html

Requires 2 specifically named input files with specific format. Default located in src/main/resources.

Basic Construction of Algorithm:
----------------------------------
	Examine all current node edges
		Calculate estimate to get to goal node from each connected edge by given heuristic
			Place all connected edges into priority queue (frontier) by their estimated cost to the goal
				Repeat for node at head of queue until goal node is reached
----------------------------------

The heuristic is simply an estimated cost guaranteed to be less than the actual cost.
For NP complete problems, or problems with high-cost optimal solutions in general, a heuristic can often be calculated relatively cheaply, and lead to a satisfactory solution.

The specific problem addressed here is finding the lowest mileage path to get from one major Minnesota city to another. We are given the distance between connected cities, and an estimate from each city to the goal city. The actual cost from connected cities input is from MNDOT ( http://www.dot.state.mn.us/roadway/data/ ). The heuristic input is an "as the crow flies" straight distance between all cities (actually this one is just randomly lowered amounts of the actual calculated distance between all cities).

When ran from MainUI, the program will present the user with a GUI requesting to choose start & end cities in Minnesota. There is a dropdown list of about 50 cities. After selecting the cities, it will run the search and create a file of the output named after the cities, notifying the user of the file location.

The text file shows the entire progress of the search, displaying the updated frontier for each city it moves to to calculate, as well as total miles through each city in the final optimal path.
