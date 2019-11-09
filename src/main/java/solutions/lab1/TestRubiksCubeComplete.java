package solutions.lab1;
import java.util.Calendar;
import java.util.List;

import javax.swing.JOptionPane;

import lab1.MisplacedCubiesNotAdmissible;
import lab1.RCgoalTest;
import lab1.RubiksCube;
import lab1.RubiksCubeFunctionFactory;
import lab1.util.SimpleCubeViewer;
import aima.core.agent.Action;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.qsearch.GraphSearch;
import aima.core.search.framework.qsearch.TreeSearch;
import aima.core.search.informed.AStarSearch;
import aima.core.search.local.Scheduler;
import aima.core.search.local.SimulatedAnnealingSearch;
import aima.core.search.uninformed.BreadthFirstSearch;
import aima.core.search.uninformed.DepthFirstSearch;
import aima.core.search.uninformed.IterativeDeepeningSearch;

public class TestRubiksCubeComplete {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		SimpleCubeViewer scv = new SimpleCubeViewer(400, 400);
		
		RubiksCube rubiksCube = new RubiksCube(3, 10);
		
		System.out.println("Initial moves: " + rubiksCube.getInitialMoves());
		scv.showMoves(rubiksCube.getInitialMoves());
		
		Problem problem = new Problem(rubiksCube, RubiksCubeFunctionFactory.getSymmetricActionsFunction(), 
				RubiksCubeFunctionFactory.getResultFunction(), new RCgoalTest());

		//BFS runs out of mem; BFS w/ GS is actually slower because of the rep. state checking
		//BUT: BFS is optimal, and solutions are shallow... so....
//		SearchForActions search = new BreadthFirstSearch(new TreeSearch()); //1-4, 5 = OOM
		
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
		SearchForActions search = new AStarSearch(new GraphSearch(), new MaximumManhattanDistance(rubiksCube));
		
//		Scheduler sched = new Scheduler(40, 0.0045, 1000);
//		SearchForActions search = new SimulatedAnnealingSearch(new MaximumManhattanDistance(rubiksCube), sched);
//		SearchForActions search = new SimulatedAnnealingSearch(new MisplacedCubiesAdmissible(rubiksCube), sched);
//		SearchForActions search = new SimulatedAnnealingSearch(new ManhattanDistance(rubiksCube), sched);
//		SearchForActions search = new SimulatedAnnealingSearch(new EdgeManhattanDistance(rubiksCube), sched);
//		SearchForActions search = new SimulatedAnnealingSearch(new CornerManhattanDistance(rubiksCube), sched);
						
		long start = Calendar.getInstance().getTimeInMillis();
		List<Action> solution;
		try {
			System.out.println("Searching for a solution...");
			solution = search.findActions(problem);
			long time = (Calendar.getInstance().getTimeInMillis()-start);
			
			//Check if reached state is a solution (may not be in case of local search)
			for (Action a : solution) rubiksCube.move(a);
			RCgoalTest goalTest = new RCgoalTest();
			boolean goalReached = goalTest.isGoalState(rubiksCube);
			
			if (!goalReached) {
				//Show message
				JOptionPane.showMessageDialog(null,
						"Time: " + time/1000.0 + " seconds" +
						"\nMetrics: " + search.getMetrics() +
						"\nClick OK to see explored states",
						"Solution not found within time/iteration limit", JOptionPane.INFORMATION_MESSAGE);
				
				if (!solution.isEmpty()) {
					//Visualize incomplete progress towards solution (local search)
					scv.showMoves(solution);
				}
			}
			else {
				//Show message
				JOptionPane.showMessageDialog(null,
						"Time: " + time/1000.0 + " seconds" +
						"\nSolution length: " + solution.size() +
						"\nMetrics: " + search.getMetrics() +
						"\nClick OK to visualize the solution",
						"Solution found!", JOptionPane.INFORMATION_MESSAGE);
				//Visualize solution
				scv.showMoves(solution);
			}
			
		}
		catch (Exception e) { e.printStackTrace(); }

	}

}
