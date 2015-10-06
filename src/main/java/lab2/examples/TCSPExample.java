package lab2.examples;

import java.util.logging.Level;

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
import org.metacsp.utility.logging.MetaCSPLogging;

public class TCSPExample {

	/**
	 * Class for Advanced AI lab on temporal reasoning. 
	 * @author iran
	 *
	 */
	public static void main(String[] args) {
		
		MetaCSPLogging.setLevel(Level.FINEST);
		//Instantiate a TCSP solver - this solver does the search in the space of STPs
		TCSPSolver metaSolver = new TCSPSolver(0, 100);
		
		//Below the TCSP there is another solver which maintains MultiTimePoints and
		//constraints w/ disjunctions (DistanceConstraints)
		//The TCSP metaSolver "tests" its assignments by adding them as constraints to this solver.
		//This solver is therefore the "ground" solver of the TCSP
		DistanceConstraintSolver groundSolver = (DistanceConstraintSolver)metaSolver.getConstraintSolvers()[0];
		
		//Below the ground solver, there is a STP solver, which performs propagation through Floyd-Warshall  
		APSPSolver groundGroundSolver = (APSPSolver)groundSolver.getConstraintSolvers()[0];
		
		//A MultiTimepoint is different from a TimePoint because its domain is NOT pair of bounds,
		//rather a multitude of pairs of bounds, depending on the disjunctions in the DistanceConstraints.
		//For each MultiTimePoint in the groundSolver there is a TimePoint in the STP, whose variables
		//are TimePoints.  The domain of a TimePoint is ONE pair of bounds.
		//If the DistanceConstraints have no disjunctions, then the domains of MultiTimepoints are the same
		//as the domains of their corresponding TimePoints.
		//
		//Source represents the origin of time
		MultiTimePoint source = groundSolver.getSource();
		MultiTimePoint multiTimePointA = (MultiTimePoint)groundSolver.createVariable();
		MultiTimePoint multiTimePointB = (MultiTimePoint)groundSolver.createVariable();

		//Observe the bounds of the multiTimePoints BEFORE adding any constraints
		System.out.println("Bounds before adding constraints:");
		System.out.println(" A: " +multiTimePointA.getDomain());
		System.out.println(" B: " +multiTimePointB.getDomain());
		
		ConstraintNetwork.draw(groundSolver.getConstraintNetwork(), "TCSP");
		ConstraintNetwork.draw(groundGroundSolver.getConstraintNetwork(), "STP");
		
		//Add a disjunctive constraint between source and multiTimePointA
		DistanceConstraint distanceBetweenOriginandA = new DistanceConstraint(new Bounds(5, 7), new Bounds(4, 6));
		distanceBetweenOriginandA.setFrom(source);
		distanceBetweenOriginandA.setTo(multiTimePointA);

		//Add a non-disjunctive constraint between source and multiTimePointB
		DistanceConstraint distanceBetweenOriginandB = new DistanceConstraint(new Bounds(23, 56));
		distanceBetweenOriginandB.setFrom(source);
		distanceBetweenOriginandB.setTo(multiTimePointB);
		
		//Add the constraint to the ground solver
		groundSolver.addConstraints(distanceBetweenOriginandA);
		groundSolver.addConstraints(distanceBetweenOriginandB);

		//Observe the bounds of the multiTimePoints AFTER adding any constraints
		System.out.println("Bounds after adding constraints:");
		System.out.println(" A: " +multiTimePointA.getDomain());
		System.out.println(" B: " +multiTimePointB.getDomain());
			
		//Provide variable and value ordering heuristics for the high-level CSP
		VariableOrderingH varOH = new MostConstrainedFirstVarOH();		
		ValueOrderingH valOH = new WidestIntervalFirstValOH();
		
		//The "meta-constraint" of the high-level CSP imposes that "labelings have to be consistent".
		//This is implemented by a class called TCSPLabeling (which extends MetaConstraint)
		TCSPLabeling metaCons = new TCSPLabeling(varOH, valOH);
		metaSolver.addMetaConstraint(metaCons);
		
		//Ask the meta-solver to return one solution, if possible, of the TCSP
		System.out.println("Solved? " + metaSolver.backtrack());		
	
		//Show the search tree of the TCSP solver
		metaSolver.draw();	

		//Observe the bounds of the multiTimePoints AFTER solving
		System.out.println("Bounds before adding constraints:");
		System.out.println(" A: " +multiTimePointA.getDomain());
		System.out.println(" B: " +multiTimePointB.getDomain());
	
	}

}
