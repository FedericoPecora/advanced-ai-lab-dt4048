package solutions.lab1;
import lab1.RubiksCube;
import aima.core.search.framework.HeuristicFunction;

public class MaximumManhattanDistance implements HeuristicFunction{

	private int [][][] goal;
	private int n;
	
	public MaximumManhattanDistance(RubiksCube rc) {
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
		int edgeVal = 0;
		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				for (int k = 0; k < n; k++){
					if(isCorner(i, j, k, n-1))
						cornerVal += evaluateManhattanDistanceOf(rc.getState()[i][j][k].getId(), i, j, k, goal);
					else if(isEdge(i, j, k, n-1))
						edgeVal += evaluateManhattanDistanceOf(rc.getState()[i][j][k].getId(), i, j, k, goal);
				}
			}
		}
		return Math.max((((double)cornerVal)/8), (((double)edgeVal)/8));
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
		
		if((i == 0 || i == n) && (j == 0 || j == n) && (k == 0 || k == n))
			return true;
		
		return false;
	}

	
	private boolean isEdge(int i, int j, int k, int n) {
		//this is specific to 3*3 cube
		String[] edges = new String[]{"010","100","120","210","001","021","201","221", "012","102","122","212"};
		
		String res = String.valueOf(i).concat(String.valueOf(j).concat(String.valueOf(k)));
		for (int l = 0; l < edges.length; l++) {
			if(edges[l].equals(res)) return true;
		}
		return false;
	}
}
