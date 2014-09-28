package solutions.lab1;
import java.util.Calendar;

import lab1.MisplacedCubiesNotAdmissible;
import lab1.RCgoalTest;
import lab1.RubiksCube;
import lab1.RubiksCubeFunctionFactory;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.informed.AStarSearch;

public class TestRubiksCubeComplete {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		RubiksCube rubiksCube = new RubiksCube(3, 4);
		Problem problem = new Problem(rubiksCube, RubiksCubeFunctionFactory.getSymmetricActionsFunction(), 
				RubiksCubeFunctionFactory.getResultFunction(), new RCgoalTest());

		//BFS runs out of mem; BFS w/ GS is actually slower because of the rep. state checking
		//BUT: BFS is optimal, and solutions are shallow... so....
//		Search search = new BreadthFirstSearch(new TreeSearch()); //1-4, 5 = OOM
		
		//Re-run BFS with asymmetric actions - predict whether this will be better and write your hypothesis
		//Verify and write comment and completion of explanation.
		
		//Problem w/ BFS: too much mem.  Let's try DFS (stay asymmetric). Predict performance: will it find a solution
		//in reasonable time for even small problems?  Verify and explain observation. 
//		Search search = new DepthFirstSearch(new TreeSearch());
		
		//Ooops, need GS because space has loops (although not naturally infinite, hence DFS is complete w/ GS)
		//Why does it still not find a solution even to a small problem?
		//[But really would need to get lucky, because space is very deep _and_ wide, so DFS completely inappropriate]
//		Search search = new DepthFirstSearch(new GraphSearch());
		
		//Think of the search space: sol are shallow, memory was the real problem with BFS... what can we do?
//		Search search = new IterativeDeepeningSearch();
		
		//We need to inform the search.  Try the given simple H (#misplaced)
		//This is not admissible - why?  Also gives very poor performance...
		//[Need to /8]
//		Search search = new AStarSearch(new GraphSearch(), new MisplacedCubiesNotAdmissible(rubiksCube));
		//Admissible version much much faster
		Search search = new AStarSearch(new GraphSearch(), new MisplacedCubiesAdmissible(rubiksCube));
		
		//Can we do better?  Implement 3D-MD.  Is it admissible?
		//[No: need to /8]
//		Search search = new AStarSearch(new GraphSearch(), new ManhattanDistance(rubiksCube));
		
		//MAX {MH_corners, MH_edges} is better, try it...
		//[Note: need to /4]
//		Search search = new AStarSearch(new GraphSearch(), new CornerManhattanDistance(rubiksCube));
//		Search search = new AStarSearch(new GraphSearch(), new EdgeManhattanDistance(rubiksCube));
//		Search search = new AStarSearch(new GraphSearch(), new MaximumManhattanDistance(rubiksCube));
						
		long start = Calendar.getInstance().getTimeInMillis();
		try {
			System.out.println("SOLUTION: " + search.search(problem));
			System.out.println("Metrics: " + search.getMetrics());
			System.out.println("Time [msec]: " + (Calendar.getInstance().getTimeInMillis()-start));
		}
		catch (Exception e) { e.printStackTrace(); }

	}

}
