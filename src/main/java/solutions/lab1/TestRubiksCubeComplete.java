package solutions.lab1;
import java.util.Calendar;
import java.util.List;

import lab1.MisplacedCubiesNotAdmissible;
import lab1.RCgoalTest;
import lab1.RubiksCube;
import lab1.RubiksCubeFunctionFactory;
import aima.core.agent.Action;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.qsearch.GraphSearch;
import aima.core.search.framework.qsearch.TreeSearch;
import aima.core.search.informed.AStarSearch;
import aima.core.search.uninformed.BreadthFirstSearch;
import aima.core.search.uninformed.DepthFirstSearch;
import aima.core.search.uninformed.IterativeDeepeningSearch;

public class TestRubiksCubeComplete {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		RubiksCube rubiksCube = new RubiksCube(3, 4);
		
		System.out.println("Initial configuration:\n============\n" + rubiksCube + "\n============");
		
		Problem problem = new Problem(rubiksCube, RubiksCubeFunctionFactory.getSymmetricActionsFunction(), 
				RubiksCubeFunctionFactory.getResultFunction(), new RCgoalTest());

		//BFS runs out of mem; BFS w/ GS is actually slower because of the rep. state checking
		//BUT: BFS is optimal, and solutions are shallow... so....
		SearchForActions search = new BreadthFirstSearch(new TreeSearch()); //1-4, 5 = OOM
		
		//Re-run BFS with asymmetric actions - predict whether this will be better and write your hypothesis
		//Verify and write comment and completion of explanation.
		
		//Problem w/ BFS: too much mem.  Let's try DFS (stay asymmetric). Predict performance: will it find a solution
		//in reasonable time for even small problems?  Verify and explain observation. 
//		SearchForActions search = new DepthFirstSearch(new TreeSearch());
		
		//Ooops, need GS because space has loops (although not naturally infinite, hence DFS is complete w/ GS)
		//Why does it still not find a solution even to a small problem?
		//[But really would need to get lucky, because space is very deep _and_ wide, so DFS completely inappropriate]
//		SearchForActions search = new DepthFirstSearch(new GraphSearch());
		
		//Think of the search space: sol are shallow, memory was the real problem with BFS... what can we do?
//		SearchForActions search = new IterativeDeepeningSearch();
		
		//We need to inform the search.  Try the given simple H (#misplaced)
		//This is not admissible - why?  Also gives very poor performance...
		//[Need to /8]
//		SearchForActions search = new AStarSearch(new GraphSearch(), new MisplacedCubiesNotAdmissible(rubiksCube));
		//Admissible version much much faster
//		SearchForActions search = new AStarSearch(new GraphSearch(), new MisplacedCubiesAdmissible(rubiksCube));
		
		//Can we do better?  Implement 3D-MD.  Is it admissible?
		//[No: need to /16]
//		SearchForActions search = new AStarSearch(new GraphSearch(), new ManhattanDistance(rubiksCube));
		
		//MAX {MH_corners, MH_edges} is better, try it...
		//[Note: need to /8]
//		SearchForActions search = new AStarSearch(new GraphSearch(), new CornerManhattanDistance(rubiksCube));
//		SearchForActions search = new AStarSearch(new GraphSearch(), new EdgeManhattanDistance(rubiksCube));
//		SearchForActions search = new AStarSearch(new GraphSearch(), new MaximumManhattanDistance(rubiksCube));
						
		long start = Calendar.getInstance().getTimeInMillis();
		List<Action> solution;
		try {
			System.out.println("Searching for a solution...");
			solution = search.findActions(problem);
			long time = (Calendar.getInstance().getTimeInMillis()-start);
			if (solution.isEmpty()) {
				System.out.println("Problem not solvable.");
			}
			else {
				System.out.println("Solution found!");
				System.out.println("SOLUTION: " + search.findActions(problem));
				System.out.println("Metrics: " + search.getMetrics());
				System.out.println("Time [msec]: " + time);
				
				//Verify the solution
				for (Action a : solution) {
					rubiksCube.move(a);
				}
				System.out.println("Configuration after applying the solution:\n============\n" + rubiksCube + "\n============");
			}
		}
		catch (Exception e) { e.printStackTrace(); }

	}

}
