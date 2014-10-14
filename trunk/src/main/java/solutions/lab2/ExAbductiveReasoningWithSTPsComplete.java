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
package solutions.lab2;

import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;

import org.metacsp.framework.ConstraintNetwork;
import org.metacsp.framework.Variable;
import org.metacsp.meta.simplePlanner.ProactivePlanningDomain;
import org.metacsp.meta.simplePlanner.SimplePlanner;
import org.metacsp.meta.simplePlanner.SimplePlannerInferenceCallback;
import org.metacsp.multi.activity.Activity;
import org.metacsp.multi.activity.ActivityNetworkSolver;
import org.metacsp.multi.allenInterval.AllenIntervalConstraint;
import org.metacsp.multi.allenInterval.AllenIntervalNetworkSolver;
import org.metacsp.sensing.ConstraintNetworkAnimator;
import org.metacsp.sensing.InferenceCallback;
import org.metacsp.sensing.Sensor;
import org.metacsp.time.Bounds;
import org.metacsp.utility.UI.ConstraintNetworkFrame;
import org.metacsp.utility.logging.MetaCSPLogging;
import org.metacsp.utility.timelinePlotting.TimelinePublisher;
import org.metacsp.utility.timelinePlotting.TimelineVisualizer;

import cern.colt.Arrays;

public class ExAbductiveReasoningWithSTPsComplete {

	public static void main(String[] args) {

		long origin = Calendar.getInstance().getTimeInMillis();

		// Create ActivityNetworkSolver, origin = current time
		final ActivityNetworkSolver ans = new ActivityNetworkSolver(origin,origin+100000);
		MetaCSPLogging.setLevel(ans.getClass(), Level.FINE);

		InferenceCallback cb = new InferenceCallback() {
			private Vector<Activity> recognizedActs = new Vector<Activity>();

			@Override
			public void doInference(long currentTime) {
				for (Activity stoveAct : ans.getActivitiesWithSymbols("Stove", new String[] { "On" })) {
					for (Activity locationAct : ans.getActivitiesWithSymbols("Location", new String[] { "Kitchen" })) {
						Activity cooking = (Activity) ans.createVariable("Human");
						cooking.setSymbolicDomain("Cooking");
						AllenIntervalConstraint cookingEqualsOn = new AllenIntervalConstraint(AllenIntervalConstraint.Type.Equals);
						AllenIntervalConstraint cookingDuringKitchen = new AllenIntervalConstraint(AllenIntervalConstraint.Type.DuringOrEqualsOrStartsOrFinishes);
						cookingEqualsOn.setFrom(cooking);
						cookingEqualsOn.setTo(stoveAct);
						cookingDuringKitchen.setFrom(cooking);
						cookingDuringKitchen.setTo(locationAct);
						AllenIntervalConstraint after = null;
						for (Activity previouslyRecognizedAct : recognizedActs) {
							after = new AllenIntervalConstraint(AllenIntervalConstraint.Type.After);
							after.setFrom(cooking);
							after.setTo(previouslyRecognizedAct);
						}
						if (ans.addConstraints(cookingEqualsOn,cookingDuringKitchen, after)) {
							recognizedActs.add(cooking);
						}
						else {
							ans.removeVariable(cooking);
						}
					}
				}
			}
		};

		// Create animator and tell it to animate the ActivityNetworkSolver w/
		// period 1000 msec
		ConstraintNetworkAnimator animator = new ConstraintNetworkAnimator(ans, 1000, cb);

		// Add some sensor traces, dispatch with offset from 'origin' (= current
		// time)
		Sensor sensorA = new Sensor("Location", animator);
		Sensor sensorB = new Sensor("Stove", animator);
		sensorA.registerSensorTrace("sensorTraces/location.st", origin);
		sensorB.registerSensorTrace("sensorTraces/stove.st", origin);

		// Visualize progression
		TimelinePublisher tp = new TimelinePublisher(ans, new Bounds(0, 60000), true, "Time", "Location", "Stove", "Human");
		TimelineVisualizer tv = new TimelineVisualizer(tp);
		tv.startAutomaticUpdate(1000);

	}

}
