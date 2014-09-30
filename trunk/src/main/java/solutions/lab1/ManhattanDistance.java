package solutions.lab1;
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
		RubiksCube rc = (RubiksCube) state;		
		int retVal = 0;
		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				for (int k = 0; k < n; k++){
					retVal += evaluateManhattanDistanceOf(rc.getState()[i][j][k].getId(), i, j, k);
				}
			}
		}
		return (((double)retVal)/16);
	}
	
	private int evaluateManhattanDistanceOf(int id, int i, int j, int k) {
		for (int i1 = 0; i1 < goal.length; i1++){
			for (int j1 = 0; j1 < goal.length; j1++){
				for (int k1 = 0; k1 < goal.length; k1++){
					if(goal[i1][j1][k1] == id) {
						return (Math.abs(i1-i)+Math.abs(j1-j)+Math.abs(k1-k));
					}
				}
			}
		}
		return 0;
	}

}
