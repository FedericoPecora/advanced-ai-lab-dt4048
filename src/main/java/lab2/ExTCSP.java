package lab2;

import java.util.Vector;
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
import org.metacsp.time.TimePoint;
import org.metacsp.utility.logging.MetaCSPLogging;

public class ExTCSP {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		TCSPSolver metaSolver = new TCSPSolver(0, 100, 0);
		DistanceConstraintSolver groundSolver = (DistanceConstraintSolver)metaSolver.getConstraintSolvers()[0];		
		APSPSolver groundGroundSolver = (APSPSolver)groundSolver.getConstraintSolvers()[0];
		
		MetaCSPLogging.setLevel(metaSolver.getClass(), Level.FINEST);

		/*
		In a restaurant, we have both a human barman and a robot
		waiter. Both can prepare coffees, but only the robot waiter can
		deliver items to guests. A guest enters the restaurant and orders a
		coffee at time 3. The human waiter takes between 5 and 7 minutes to
		prepare a coffee, while the robot waiter takes 8 to 10 minutes to
		prepare a coffee. The trip form the counter where the prepared coffee
		is placed when ready to any guest table is either 5 to 10 minutes long
		if the robot navigates through the tables and guests, or 7 minutes
		long if it chooses a fixed predefined path.  The serving coffee task
		is fully accomplished when the robot brings a sugar pot to the guest’s
		table. This action is either takes 4 to 10 minutes taking the first
		pre-defined path or 6 to 8 minutes taking the second path.  In order
		to avoid the coffee getting cold, the sugar pot should be served at
		most 12 minutes after the is coffee prepared. The whole serving time
		(i.e., waiting time for both coffee and sugar) should not exceed 15
		minutes.		 
		 */ 
		
		//an example of modeling a time point
		MultiTimePoint guestOrder = (MultiTimePoint)groundSolver.createVariable();
		

		//add more variable here
		//your code here
		
		ConstraintNetwork.draw(groundSolver.getConstraintNetwork(), "TCSP");
		ConstraintNetwork.draw(groundGroundSolver.getConstraintNetwork(), "STP");
		
		Vector<DistanceConstraint> cons = new Vector<DistanceConstraint>();
		
		//an example of creating a constraint between two time points
		DistanceConstraint guestOrderTime = new DistanceConstraint(new Bounds(3, 3));
		guestOrderTime.setFrom(groundSolver.getSource());
		guestOrderTime.setTo(guestOrder);
		cons.add(guestOrderTime);
		
		//add more constraint here
		//your code here
		
		//here you add a constraints into teh ground solver
		groundSolver.addConstraints(cons.toArray(new DistanceConstraint[cons.size()]));
		
		VariableOrderingH varOH = new MostConstrainedFirstVarOH();
		
		ValueOrderingH valOH = new WidestIntervalFirstValOH();

		TCSPLabeling metaCons = new TCSPLabeling(varOH, valOH);
		metaSolver.addMetaConstraint(metaCons);
		
		System.out.println("Solved? " + metaSolver.backtrack());
		
		System.out.println(guestOrder.getLowerBound());
		
		
		
		
		metaSolver.draw();
		

		
	}
}
