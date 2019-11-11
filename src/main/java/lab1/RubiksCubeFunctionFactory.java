package lab1;
import java.util.LinkedHashSet;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.search.framework.problem.ActionsFunction;
import aima.core.search.framework.problem.ResultFunction;


public class RubiksCubeFunctionFactory {

	private static ActionsFunction _actionsFunction = null;
	private static ResultFunction _resultFunction = null;

	public static ActionsFunction getSymActionsFunction() {
		if (null == _actionsFunction) {
			_actionsFunction = new RCactionFunction(true);
		}
		return _actionsFunction;
	}

	public static ActionsFunction getAsymActionsFunction() {
		if (null == _actionsFunction) {
			_actionsFunction = new RCactionFunction(false);
		}
		return _actionsFunction;
	}
	
	public static ResultFunction getResultFunction() {
		if (null == _resultFunction) {
			_resultFunction = new RCresultFunction();
		}
		return _resultFunction;
	}

	private static class RCactionFunction implements ActionsFunction {

		private boolean symmetric;

		public RCactionFunction(boolean s) {
			symmetric = s;
		}
		
		@Override
		public Set<Action> actions(Object s) {
			Set<Action> actions = new LinkedHashSet<Action>();		
			actions.add(RubiksCube.BACK);
			actions.add(RubiksCube.FRONT);
			actions.add(RubiksCube.BOTTOM);
			actions.add(RubiksCube.TOP);		
			actions.add(RubiksCube.LEFT);
			actions.add(RubiksCube.RIGHT);
			if (symmetric) {
				actions.add(RubiksCube.BACKI);
				actions.add(RubiksCube.FRONTI);
				actions.add(RubiksCube.BOTTOMI);
				actions.add(RubiksCube.TOPI);		
				actions.add(RubiksCube.LEFTI);
				actions.add(RubiksCube.RIGHTI);
			}
			return actions;
		}
	}
	
	
	private static class RCresultFunction implements ResultFunction{

		@Override
		public Object result(Object s, Action a) {
			
			RubiksCube rc = (RubiksCube) s;

			if (RubiksCube.FRONT.equals(a)) {
				RubiksCube newRC = new RubiksCube(rc);
				newRC.moveFront();
				return newRC;
			}
			else if (RubiksCube.BACK.equals(a)) {
				RubiksCube newRC = new RubiksCube(rc);
				newRC.moveBack();
				return newRC;
			}
			else if (RubiksCube.LEFT.equals(a)) {
				RubiksCube newRC = new RubiksCube(rc);
				newRC.moveLeft();
				return newRC;
			}
			else if (RubiksCube.RIGHT.equals(a)) {
				RubiksCube newRC = new RubiksCube(rc);
				newRC.moveRight();
				return newRC;
			}
			else if (RubiksCube.TOP.equals(a)) {
				RubiksCube newRC = new RubiksCube(rc);
				newRC.moveUp();
				return newRC;
			}
			else if (RubiksCube.BOTTOM.equals(a)) {
				RubiksCube newRC = new RubiksCube(rc);
				newRC.moveDown();
				return newRC;
			}
			
			//Inverses
			else if (RubiksCube.FRONTI.equals(a)) {
				RubiksCube newRC = new RubiksCube(rc);
				newRC.moveFrontI();
				return newRC;
			}
			else if (RubiksCube.BACKI.equals(a)) {
				RubiksCube newRC = new RubiksCube(rc);
				newRC.moveBackI();
				return newRC;
			}
			else if (RubiksCube.LEFTI.equals(a)) {
				RubiksCube newRC = new RubiksCube(rc);
				newRC.moveLeftI();
				return newRC;
			}
			else if (RubiksCube.RIGHTI.equals(a)) {
				RubiksCube newRC = new RubiksCube(rc);
				newRC.moveRightI();
				return newRC;
			}
			else if (RubiksCube.TOPI.equals(a)) {
				RubiksCube newRC = new RubiksCube(rc);
				newRC.moveUpI();
				return newRC;
			}
			else if (RubiksCube.BOTTOMI.equals(a)) {
				RubiksCube newRC = new RubiksCube(rc);
				newRC.moveDownI();
				return newRC;
			}
			return rc;
		}	
	}
}