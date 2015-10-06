package solutions.lab2;

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

public class ExTCSPComplete {
	
	public static void main(String args[]) {
		
		TCSPSolver metaSolver = new TCSPSolver(0, 100, 0);
		DistanceConstraintSolver groundSolver = (DistanceConstraintSolver)metaSolver.getConstraintSolvers()[0];		
		APSPSolver groundGroundSolver = (APSPSolver)groundSolver.getConstraintSolvers()[0];
		
		MetaCSPLogging.setLevel(metaSolver.getClass(), Level.FINEST);

		/*
			In a restaurant, we have both a human barman and a robot waiter. Both can prepare
			coffees, but only the robot waiter can deliver items to guests. A guest enters the
			restaurant and orders a coffee at time 3. The human waiter takes between 5 and 7
			minutes to prepare a coffee, while the robot waiter takes 8 to 10 minutes to prepare
			a coffee. The trip form the counter where the prepared coffee is placed when ready
			to any guest table is either 5 to 10 minutes long if the robot navigates through
			the tables and guests, or 7 minutes long if it chooses a fixed predefined path. The
			serving coffee task is fully accomplished when the robot brings sugar to the guest’s
			table. This action either takes 4 to 10 minutes (if the robot brings a sugar bowl) or
			6 to 8 minutes (if the robot brings sugar bags). In order to avoid the coffee getting
			cold, the sugar should be served at most 12 minutes after the coffee is prepared.
			The whole serving time (i.e., the time between ordering and having both coffee
			and sugar on the table) should not exceed 15 minutes.		 
		 */ 
		
		MultiTimePoint guestOrder = (MultiTimePoint)groundSolver.createVariable();
		MultiTimePoint coffeeReady = (MultiTimePoint)groundSolver.createVariable();
		MultiTimePoint servingCoffee = (MultiTimePoint)groundSolver.createVariable();
		MultiTimePoint servingSugarPot = (MultiTimePoint)groundSolver.createVariable();
		
		ConstraintNetwork.draw(groundSolver.getConstraintNetwork(), "TCSP");
		ConstraintNetwork.draw(groundGroundSolver.getConstraintNetwork(), "STP");
		
		Vector<DistanceConstraint> cons = new Vector<DistanceConstraint>();
		
		DistanceConstraint guestOrderTime = new DistanceConstraint(new Bounds(3, 3));
		guestOrderTime.setFrom(groundSolver.getSource());
		guestOrderTime.setTo(guestOrder);
		cons.add(guestOrderTime);
		
		DistanceConstraint preparingCoffee = new DistanceConstraint(new Bounds(5, 7), new Bounds(8, 10));
		preparingCoffee.setFrom(guestOrder);
		preparingCoffee.setTo(coffeeReady);
		cons.add(preparingCoffee);
		
//		DistanceConstraint coffeePreparing = new DistanceConstraint(new Bounds(6, 6));
//		coffeePreparing.setFrom(groundSolver.getSource());
//		coffeePreparing.setTo(coffeeReady);
//		cons.add(coffeePreparing);

		DistanceConstraint travelToServCoffee = new DistanceConstraint(new Bounds(5, 10), new Bounds(7, 7) );
		travelToServCoffee.setFrom(coffeeReady);
		travelToServCoffee.setTo(servingCoffee);
		cons.add(travelToServCoffee);

		DistanceConstraint travelToServSugorPot = new DistanceConstraint(new Bounds(4, 10), new Bounds(6, 8));
		travelToServSugorPot.setFrom(servingCoffee);
		travelToServSugorPot.setTo(servingSugarPot);
		cons.add(travelToServSugorPot);
		
		DistanceConstraint validDurationForHotCoffee = new DistanceConstraint(new Bounds(0, 12));
		validDurationForHotCoffee.setFrom(coffeeReady);
		validDurationForHotCoffee.setTo(servingSugarPot);
		cons.add(validDurationForHotCoffee);

		DistanceConstraint servingTime = new DistanceConstraint(new Bounds(0, 15));
		servingTime.setFrom(guestOrder);
		servingTime.setTo(servingSugarPot);
		cons.add(servingTime);

		groundSolver.addConstraints(cons.toArray(new DistanceConstraint[cons.size()]));
		//groundSolver.addConstraints(new DistanceConstraint[] {preparingCoffee,travelToServCoffee,travelToServSugorPot,validDurationForHotCoffee, servingTime});
		
		VariableOrderingH varOH = new MostConstrainedFirstVarOH();
		
		ValueOrderingH valOH = new WidestIntervalFirstValOH();

		TCSPLabeling metaCons = new TCSPLabeling(varOH, valOH);
		metaSolver.addMetaConstraint(metaCons);
		
		System.out.println("Solved? " + metaSolver.backtrack());
		
		System.out.println(groundSolver.getSource());
		System.out.println(coffeeReady);
		System.out.println(servingCoffee);
		
		System.out.println(((TimePoint)servingSugarPot.getInternalVariables()[0]).getLowerBound());
		System.out.println(((TimePoint)servingSugarPot.getInternalVariables()[0]).getUpperBound());
		
		
		metaSolver.draw();
		
//		System.out.println(metaSolver.getDescription());
//		System.out.println(metaCons.getDescription());
		
	}


}
