package lab1;

import aima.core.search.framework.problem.GoalTest;

public class RCgoalTest implements GoalTest{

	@Override
	public  boolean isGoalState(Object state) {
				
		String[] goal = new String[]{"y","w","g","b","o","r"};
		
		RubiksCube rc = (RubiksCube)state;
		int n = rc.getState().length;
		for (int k = 0; k < 6; k++) {
			for (int i = 0; i < rc.getState().length; i++) {
				for (int j = 0; j < rc.getState().length; j++) {
					if(k == 0){
						if(goal[0].compareTo(rc.getState()[0][i][j].getFaceByDim(0).getColor()) !=0) return false;
						//System.out.print(rc.getState()[0][i][j].getFaceByDim(0).getColor());
					}
					else if(k == 1){
						if(goal[1].compareTo(rc.getState()[n-1][i][j].getFaceByDim(1).getColor()) !=0) return false;
						//System.out.print(rc.getState()[n-1][i][j].getFaceByDim(1).getColor());
					}
					else if(k == 2){
						if(goal[2].compareTo(rc.getState()[i][0][j].getFaceByDim(3).getColor()) !=0) return false;
						//System.out.print(rc.getState()[i][0][j].getFaceByDim(3).getColor());
					}
					else if(k == 3){
						if(goal[3].compareTo(rc.getState()[i][n-1][j].getFaceByDim(2).getColor()) !=0) return false;
						//System.out.print(rc.getState()[i][n-1][j].getFaceByDim(2).getColor());
					}
					else if(k == 4){
						if(goal[4].compareTo(rc.getState()[i][j][0].getFaceByDim(4).getColor()) !=0) return false;
						//System.out.print(rc.getState()[i][j][0].getFaceByDim(4).getColor());
					}
					else if(k == 5){
						if(goal[5].compareTo(rc.getState()[i][j][n-1].getFaceByDim(5).getColor()) !=0) return false;
						//System.out.print(rc.getState()[i][j][n-1].getFaceByDim(5).getColor());
					}

				}
			}			
		}
		//System.out.println(rc);
		return true;
	}

	
}
