package lab2;

import org.metacsp.framework.ConstraintNetwork;
import org.metacsp.framework.ValueOrderingH;
import org.metacsp.framework.VariableOrderingH;
import org.metacsp.meta.TCSP.MostConstrainedFirstVarOH;
import org.metacsp.meta.TCSP.TCSPLabeling;
import org.metacsp.meta.TCSP.TCSPSolver;
import org.metacsp.meta.TCSP.WidestIntervalFirstValOH;
import org.metacsp.multi.TCSP.DistanceConstraint;
import org.metacsp.multi.TCSP.DistanceConstraintSolver;
import org.metacsp.multi.TCSP.MultiTimePoint;
import org.metacsp.time.APSPSolver;
import org.metacsp.time.Bounds;

public class EasyTestTCSP {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		TCSPSolver metaSolver = new TCSPSolver(0, 100, 0);
		DistanceConstraintSolver groundSolver = (DistanceConstraintSolver)metaSolver.getConstraintSolvers()[0];		
		APSPSolver groundGroundSolver = (APSPSolver)groundSolver.getConstraintSolvers()[0];
		
		MultiTimePoint source = groundSolver.getSource();
		
		MultiTimePoint timePointA = (MultiTimePoint)groundSolver.createVariable();
		
		ConstraintNetwork.draw(groundSolver.getConstraintNetwork(), "TCSP");
		ConstraintNetwork.draw(groundGroundSolver.getConstraintNetwork(), "STP");
		
		

		
		DistanceConstraint distanceBetweenOriginandA = new DistanceConstraint(new Bounds(5, 7), new Bounds(4, 6));
		distanceBetweenOriginandA.setFrom(source);
		distanceBetweenOriginandA.setTo(timePointA);
		
		
		groundSolver.addConstraints(new DistanceConstraint[] {distanceBetweenOriginandA});
		
		VariableOrderingH varOH = new MostConstrainedFirstVarOH();		
		ValueOrderingH valOH = new WidestIntervalFirstValOH();
		
		
		TCSPLabeling metaCons = new TCSPLabeling(varOH, valOH);
		metaSolver.addMetaConstraint(metaCons);
		
		System.out.println("Solved? " + metaSolver.backtrack());		
		
		
		System.out.println(timePointA);
		
		long loweBound = timePointA.getLowerBound();
		long upperBound = timePointA.getUpperBound();
		System.out.println(loweBound);
		System.out.println(upperBound);
		
		metaSolver.draw();	

	}

}
