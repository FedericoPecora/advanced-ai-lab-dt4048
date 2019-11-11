package lab1;

import java.util.function.ToDoubleFunction;

import aima.core.search.framework.evalfunc.HeuristicFunction;

public class MaximumManhattanDistance implements HeuristicFunction, ToDoubleFunction<Object> {

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
	//Your in this method!
	public double h(Object state) {
		return 0;
	}
	
	//this is specific to 3*3 cube
	private boolean isCorner(int i, int j, int k){
		
		if((i == 0 || i == 2) && (j == 0 || j == 2) && (k == 0 || k == 2))
			return true;
		
		return false;
	}

	//this is specific to 3*3 cube
	private boolean isEdge(int i, int j, int k) {		
		String[] edges = new String[]{"010","100","120","210","001","021","201","221", "012","102","122","212"};
		
		String res = String.valueOf(i).concat(String.valueOf(j).concat(String.valueOf(k)));
		for (int l = 0; l < edges.length; l++) {
			if(edges[l].equals(res)) return true;
		}
		return false;
	}
	
	@Override
	public double applyAsDouble(Object arg0) {
		return h(arg0);
	}
}
