package lab1;
import lab1.RubiksCube;
import aima.core.search.framework.HeuristicFunction;

public class ManhattanDistance implements HeuristicFunction {

	private int [][][] goal = null;
	private int n = -1;
	
	public ManhattanDistance(RubiksCube rc) {
		n = rc.getState().length;
		goal = new int[n][n][n];
		int counter = 1;
		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				for (int k = 0; k < n; k++){
					goal[i][j][k] = counter;
					counter++;
				}
			}
		}
	}
	
	
	@Override
	public double h(Object state) {
		return 0;
	}
	


}
