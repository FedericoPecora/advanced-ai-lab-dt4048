package lab1;
import java.util.Calendar;

import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.TreeSearch;
import aima.core.search.uninformed.BreadthFirstSearch;

public class TestRubiksCube {

	public static void main(String[] args) {

		RubiksCube rubiksCube = new RubiksCube(3, 2);
		Problem problem = new Problem(rubiksCube, RubiksCubeFunctionFactory.getSymmetricActionsFunction(), 
				RubiksCubeFunctionFactory.getResultFunction(), new RCgoalTest());

		Search search = new BreadthFirstSearch(new TreeSearch());
		
		long start = Calendar.getInstance().getTimeInMillis();
		try {
			System.out.println("SOLUTION: " + search.search(problem));
			System.out.println("Metrics: " + search.getMetrics());
			System.out.println("Time [msec]: " + (Calendar.getInstance().getTimeInMillis()-start));
		}
		catch (Exception e) { e.printStackTrace(); }
	}

}
