package lab1;
import java.util.Calendar;
import java.util.List;

import javax.swing.JOptionPane;

import aima.core.agent.Action;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.qsearch.TreeSearch;
import aima.core.search.uninformed.BreadthFirstSearch;
import lab1.util.SimpleCubeViewer;

public class TestRubiksCube {

	public static void main(String[] args) {

		SimpleCubeViewer scv = new SimpleCubeViewer(400, 400);
		
		RubiksCube rubiksCube = new RubiksCube(3, 4);
		
		System.out.println("Initial moves: " + rubiksCube.getInitialMoves());
		scv.showMoves(rubiksCube.getInitialMoves());
		
		Problem problem = new Problem(rubiksCube, RubiksCubeFunctionFactory.getSymmetricActionsFunction(), 
				RubiksCubeFunctionFactory.getResultFunction(), new RCgoalTest());

		SearchForActions search = new BreadthFirstSearch(new TreeSearch());
						
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
