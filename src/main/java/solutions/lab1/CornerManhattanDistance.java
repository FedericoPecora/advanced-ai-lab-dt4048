package solutions.lab1;
import java.util.function.ToDoubleFunction;

import aima.core.search.framework.evalfunc.HeuristicFunction;
import lab1.RubiksCube;


public class CornerManhattanDistance implements HeuristicFunction, ToDoubleFunction<Object> {

	private int [][][] goal;
	private int n;
	
	public CornerManhattanDistance(RubiksCube rc) {
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
		int cornerVal = 0;
		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				for (int k = 0; k < n; k++){
					if(isCorner(i, j, k, n-1))
						cornerVal += evaluateManhattanDistanceOf(rc.getState()[i][j][k].getId(), i, j, k, goal);
				}
			}
		}
		return (((double)cornerVal)/4);
	}
	
	private int evaluateManhattanDistanceOf(int id, int i, int j, int k, int[][][] goal) {		
		for (int i1 = 0; i1 < goal.length; i1++){
			for (int j1 = 0; j1 < goal.length; j1++){
				for (int k1 = 0; k1 < goal.length; k1++){
					if(goal[i1][j1][k1] == id){
						return (Math.abs(i1-i)+Math.abs(j1-j)+Math.abs(k1-k));
					}
				}
			}
		}
		return 0;
	}
	
	private boolean isCorner(int i, int j, int k, int n){
		if((i == 0 || i == n) && (j == 0 || j == n) && (k == 0 || k == n)) return true;
		return false;
	}
	
	@Override
	public double applyAsDouble(Object arg0) {
		return h(arg0);
	}

}
