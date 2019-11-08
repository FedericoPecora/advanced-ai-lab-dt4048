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
			if (solution.isEmpty()) {
				System.out.println("Problem not solvable.");
			}
			else {
				System.out.println("Solution found!");
				System.out.println("SOLUTION: " + solution);
				System.out.println("Metrics: " + search.getMetrics());
				System.out.println("Time [msec]: " + time);
				
				JOptionPane.showMessageDialog(null, "Click OK to visualize the solution", "Solution found!", JOptionPane.INFORMATION_MESSAGE);
				
				//Visualize solution
				scv.showMoves(solution);				
			}
		}
		catch (Exception e) { e.printStackTrace(); }

	}

}
