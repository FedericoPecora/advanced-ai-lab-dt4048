package solutions.lab2.debug;

import org.metacsp.framework.ConstraintNetwork;

import solutions.lab2.ExQualitativeAllenSolverComplete;

public class AllenConstraintNetworkGenerator {
	
	public static void main(String[] args) {
		ExQualitativeAllenSolverComplete solver = new ExQualitativeAllenSolverComplete();

		//Simple test for infeasibility
//		Variable[] vars = solver.createVariables(2);
//		SimpleAllenInterval interval1 = (SimpleAllenInterval)vars[0];
//		SimpleAllenInterval interval2 = (SimpleAllenInterval)vars[1];
//		
//		QualitativeAllenIntervalConstraint con0 = new QualitativeAllenIntervalConstraint(QualitativeAllenIntervalConstraint.Type.FinishedBy);
//		con0.setFrom(interval1);
//		con0.setTo(interval2);
//		
//		if (!solver.addConstraints(con0)) System.out.println("Failed to add constraints!");
//		
//		QualitativeAllenIntervalConstraint con1 = new QualitativeAllenIntervalConstraint(QualitativeAllenIntervalConstraint.Type.Overlaps);
//		con1.setFrom(interval2);
//		con1.setTo(interval1);
//	
//		System.out.println("Before entering: " + solver.getConstraintNetwork());
//		if (!solver.addConstraints(con1)) System.out.println("Failed to add constraints!");
//		//if (!solver.addConstraints(con0,con1)) System.out.println("Failed to add constraints!");
		
		//Generate random network
		ExQualitativeAllenSolverComplete.generateRandomConstraintNetwork(solver, 8, 0.4, 2, false);
		if (!solver.propagate()) System.out.println("Failed to propagate!");
		else System.out.println("Propagation successful!");
				
		ConstraintNetwork.draw(solver.getConstraintNetwork(),"Randomly generated network");
		
	}

}
