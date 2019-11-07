package lab1;
import java.util.Calendar;
import java.util.List;

import aima.core.agent.Action;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.qsearch.TreeSearch;
import aima.core.search.uninformed.BreadthFirstSearch;

public class TestRubiksCube {

	public static void main(String[] args) {

		RubiksCube rubiksCube = new RubiksCube(3, 4);
		
		System.out.println("Initial configuration:\n============\n" + rubiksCube + "\n============");
		
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
