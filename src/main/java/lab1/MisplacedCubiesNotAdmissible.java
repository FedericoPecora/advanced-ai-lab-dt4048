package lab1;

import java.util.function.ToDoubleFunction;

import aima.core.search.framework.evalfunc.HeuristicFunction;

public class MisplacedCubiesNotAdmissible implements HeuristicFunction, ToDoubleFunction<Object> {

	private int [][][] goal;
	private int n;
	
	public MisplacedCubiesNotAdmissible(RubiksCube rc) {
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
		RubiksCube rc = (RubiksCube) state;	
		int retVal = 0;
		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				for (int k = 0; k < n; k++){
					if (goal[i][j][k] != rc.getState()[i][j][k].getId()) retVal++;
				}
			}
		}
		return ((double)retVal);
	}
	
	@Override
	public double applyAsDouble(Object arg0) {
		return h(arg0);
	}
	
}
