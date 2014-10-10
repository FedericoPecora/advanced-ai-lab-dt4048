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

public class TestTCSPSolverComplete {
	
	public static void main(String args[]) {
		
		TCSPSolver metaSolver = new TCSPSolver(0, 100, 0);
		DistanceConstraintSolver groundSolver = (DistanceConstraintSolver)metaSolver.getConstraintSolvers()[0];		
		APSPSolver groundGroundSolver = (APSPSolver)groundSolver.getConstraintSolvers()[0];
		
		MetaCSPLogging.setLevel(metaSolver.getClass(), Level.FINEST);

		/*
		 * In a restaurant, we have both a human waiter and a robot waiter. A guest Enters a restaurant and orders a coffee. 
		 * . A human waiter takes (5-7) min to make a coffee ready and A robot waiter takes (8-10) min to make a coffee ready. Then, 
		 * the robot travel to serve a coffee. It either takes (5-10) min if it travel among the guest and tables or takes 7 min if it travels in a 
		 * fixed path designed only for a robot. Then, the robot has to serve a sugorpot which it takes either [5,10] or 6 min. In order to avoid 
		 * coffee getting cold, guest must have 15 min after the coffee prepared. The whole serving time should not exceed than 25 min.         
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

		DistanceConstraint servingTime = new DistanceConstraint(new Bounds(0, 20));
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
