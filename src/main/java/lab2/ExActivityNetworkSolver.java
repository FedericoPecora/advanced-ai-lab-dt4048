/*******************************************************************************
 * Copyright (c) 2010-2013 Federico Pecora <federico.pecora@oru.se>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
package lab2;

import java.util.logging.Level;

import org.metacsp.framework.ConstraintNetwork;
import org.metacsp.multi.activity.Activity;
import org.metacsp.multi.activity.ActivityNetworkSolver;
import org.metacsp.multi.activity.SymbolicVariableActivity;
import org.metacsp.multi.allenInterval.AllenIntervalConstraint;
import org.metacsp.multi.allenInterval.AllenIntervalNetworkSolver;
import org.metacsp.time.Bounds;
import org.metacsp.utility.logging.MetaCSPLogging;
import org.metacsp.utility.timelinePlotting.TimelinePublisher;
import org.metacsp.utility.timelinePlotting.TimelineVisualizer;

public class ExActivityNetworkSolver {
	
	public static void main(String[] args) {
		
		MetaCSPLogging.setLevel(Level.FINEST);
//		MetaCSPLogging.setLevel(solver.getClass(), Level.FINEST);
//		MetaCSPLogging.setLevel(solver.getConstraintSolvers()[0].getClass(), Level.FINEST);

		ActivityNetworkSolver solver = new ActivityNetworkSolver(0,500);
		SymbolicVariableActivity act1 = (SymbolicVariableActivity)solver.createVariable("Component");
		act1.setSymbolicDomain("A");
		SymbolicVariableActivity act2 = (SymbolicVariableActivity)solver.createVariable("Component");
		act2.setSymbolicDomain("B");
		
		ConstraintNetwork.draw(solver.getConstraintNetwork());
		
		//A is between 10 and 20 before B
		AllenIntervalConstraint con1 = new AllenIntervalConstraint(AllenIntervalConstraint.Type.Before, new Bounds(10, 20));
		con1.setFrom(act1);
		con1.setTo(act2);

		//A has a duration
		AllenIntervalConstraint con2 = new AllenIntervalConstraint(AllenIntervalConstraint.Type.Duration, new Bounds(5, 5));
		con2.setFrom(act1);
		con2.setTo(act1);

		//B has a duration
		AllenIntervalConstraint con3 = new AllenIntervalConstraint(AllenIntervalConstraint.Type.Duration, new Bounds(5, 5));
		con3.setFrom(act2);
		con3.setTo(act2);

		//B starts no earlier than 13
		AllenIntervalConstraint con4 = new AllenIntervalConstraint(AllenIntervalConstraint.Type.Release, new Bounds(13, solver.getHorizon()));
		con4.setFrom(act2);
		con4.setTo(act2);
		
		solver.addConstraints(con1,con2,con3,con4);
		
		TimelinePublisher tp = new TimelinePublisher(solver, new Bounds(0,50), true, "Component");
		TimelineVisualizer tv = new TimelineVisualizer(tp);
		tp.publish(true, false);
		
		//Get the various layers of CSPs:
		//  LAYER 2: an ActivityNetworkSolver (network of Augmented Allen Interval constraints among Activities)
		ConstraintNetwork.draw(solver.getConstraintNetwork(), "LAYER 2 (Network of Activities)");
		//  LAYER 1: an AllenIntervalNetworkSolver (network of Augmented Allen Interval constraints among Allen Intervals)
		ConstraintNetwork.draw(solver.getConstraintSolvers()[0].getConstraintNetwork(), "LAYER 1 (Network of Allen Intervals)");
		//  LAYER 0: an APSPSolver (network of Simple Distance Constraints, STP)
		ConstraintNetwork.draw(((AllenIntervalNetworkSolver)solver.getConstraintSolvers()[0]).getConstraintSolvers()[0].getConstraintNetwork(), "LAYER 0 (Network of Time Points)");
		
		//YOUR CODE HERE
				
	}

}
