package lab2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;
import java.util.Vector;

import org.metacsp.framework.Constraint;
import org.metacsp.framework.ConstraintNetwork;
import org.metacsp.framework.ConstraintSolver;
import org.metacsp.framework.Variable;
import org.metacsp.time.qualitative.QualitativeAllenIntervalConstraint;
import org.metacsp.time.qualitative.QualitativeAllenIntervalConstraint.Type;

import org.metacsp.time.qualitative.SimpleAllenInterval;


/**
 * Class for Advanced AI lab on temporal reasoning.  Your task: implement the pathConsistency() method
 * and test with class TestExQualitativeAllenSolver.
 * 
 * @author fpa
 *
 */
public class ExQualitativeAllenSolver extends ConstraintSolver {
	
	private static final long serialVersionUID = 9130340233823443991L;
	private int IDs = 0;
	private ConstraintNetwork completeNetwork = null;
	private boolean successfulPropagation = false;
	
	public ExQualitativeAllenSolver() {
		super(new Class[]{QualitativeAllenIntervalConstraint.class}, SimpleAllenInterval.class);
		this.setOptions(OPTIONS.AUTO_PROPAGATE);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Populates a given solver's constraint network with a given number of variables and
	 * random constraints among them.
	 * @param solver A solver to use for variable creation and to add the constraints to.
	 * @param numVars The number of variables to create.
	 * @param probConnected The probability that two variables should be connected by a constraint.
	 * @param maxRelations The maximum size of the set of disjunctive relations between two constraints.
	 * @param propagateWhileAddingConstraints If set to <code>true</code>, generated constraints are
	 * guaranteed to be k-consistent (depending on the level of k-consistency implemented in the
	 * propagate function). 
	 */
	public static void generateRandomConstraintNetwork(ExQualitativeAllenSolver solver, int numVars, double probConnected, int maxRelations, boolean propagateWhileAddingConstraints) {
		Random rand = new Random(Calendar.getInstance().getTimeInMillis());
		Variable[] vars = solver.createVariables(numVars);
		for (int i = 0; i < numVars; i++) {
			for (int j = 0; j < numVars; j++) {
				if (i != j && rand.nextDouble() < probConnected) {
					boolean retry = false;
					do {
						SimpleAllenInterval sai_i = (SimpleAllenInterval)vars[i];
						SimpleAllenInterval sai_j = (SimpleAllenInterval)vars[j];
						boolean[] types = new boolean[QualitativeAllenIntervalConstraint.Type.values().length];
						for (int k = 0; k < types.length; k++) types[k] = false;
						int numRelations = 1;
						if (maxRelations > 1) numRelations += rand.nextInt(maxRelations-1);
						Type[] typesForConstraint = new Type[numRelations];
						while (numRelations > 0) {
							if (rand.nextBoolean()) {
								int l = rand.nextInt(types.length);
								if (!types[l]) {
									types[l] = true;
									numRelations--;
									typesForConstraint[numRelations] = QualitativeAllenIntervalConstraint.Type.values()[l];
								}
							}
						}
						QualitativeAllenIntervalConstraint aic = new QualitativeAllenIntervalConstraint(typesForConstraint);
						aic.setFrom(sai_i);
						aic.setTo(sai_j);
						
						if (propagateWhileAddingConstraints && !solver.addConstraint(aic)) {
							//System.out.println("Could not add " + aic);
							retry = true;
						}
						else {
							if (!propagateWhileAddingConstraints) solver.addConstraintNoPropagation(aic);
							retry = false;
							System.out.println("Added " + aic);
						}
					}
					while (retry);
				}
			}
		}
	}

	@Override
	public boolean propagate() {	
		successfulPropagation = false;
		if(this.getConstraints().length == 0) return true;
		this.createCompleteNetwork();
		successfulPropagation = false;
		if (pathConsistency() && arcConsistency()) successfulPropagation = true;
		return successfulPropagation;
	}

	@Override
	public ConstraintNetwork getConstraintNetwork() {
		if (successfulPropagation) return completeNetwork;
		else return super.getConstraintNetwork();
	}

	private void createCompleteNetwork() {
		completeNetwork = new ConstraintNetwork(this);
		ConstraintNetwork originalNetwork = this.getConstraintNetwork();
		for (Variable var : originalNetwork.getVariables()) completeNetwork.addVariable(var);
		for (Constraint con : originalNetwork.getConstraints()) completeNetwork.addConstraint(con);
		Variable[] vars = completeNetwork.getVariables();
		for (int i = 0; i < vars.length; i++) {
			for (int j = 0; j < vars.length; j++) {
				if (i != j && originalNetwork.getConstraint(vars[i],vars[j]) == null) {
					if (originalNetwork.getConstraint(vars[j],vars[i]) != null) {
						//add inverse
						Type[] types = ((QualitativeAllenIntervalConstraint)originalNetwork.getConstraint(vars[j],vars[i])).getTypes();
						Type[] inverses = QualitativeAllenIntervalConstraint.getInverseRelation(types);
						QualitativeAllenIntervalConstraint inverse = new QualitativeAllenIntervalConstraint(inverses);
						inverse.setFrom(vars[i]);
						inverse.setTo(vars[j]);
						completeNetwork.addConstraint(inverse);
					}
					else {
						//create universal relation
						Type[] allTypes = new Type[QualitativeAllenIntervalConstraint.Type.values().length];
						for (int k = 0; k < QualitativeAllenIntervalConstraint.Type.values().length; k++) allTypes[k] = QualitativeAllenIntervalConstraint.Type.values()[k];
						QualitativeAllenIntervalConstraint universe = new QualitativeAllenIntervalConstraint(allTypes);
						universe.setFrom(vars[i]);
						universe.setTo(vars[j]);
						completeNetwork.addConstraint(universe);
					}
				}
			}	
		}
	}
	
	private boolean pathConsistency() {
		
		/**
		 * To get variables in the network:
		 * Variable[] vars = this.completeNetwork.getVariables();
		 * 
		 * To get the constraint between two variables in the network:
		 * QualitativeAllenIntervalConstraint r_ij =
		 *    (QualitativeAllenIntervalConstraint)completeNetwork.getConstraint(vars[i], vars[j]);
		 *    
		 * To get an array of Types representing the disjunction of relations along edge r_ij:
		 * r_ij.getTypes()
		 * 
		 * Operations on QualitativeAllenIntervalConstraints r1 and r2:
		 * QualitativeAllenIntervalConstraint composition = getComposition(r1,r2);
		 * QualitativeAllenIntervalConstraint intersection = getIntersection(r1,r2);
		 * 
		 * To remove a QualitativeAllenIntervalConstraint r_ij from the network
		 * completeNetwork.removeConstraint(r_ij);
		 * 
		 * To add a QualitativeAllenIntervalConstraint r_ij to the network
		 * completeNetwork.addConstraint(r_ij);
		 */
		
		//YOUR CODE HERE
		return true;
	}

	/**
	 * Computes the intersection of two {@link QualitativeAllenIntervalConstraint}s, that is,
	 * the set of qualitative relations they have in common.
	 * @param o1 The first constraint.
	 * @param o2 The second constraint.
	 * @return A constraint whose qualitative relations are the intersection of those of the
	 * given constraints.
	 */
	private QualitativeAllenIntervalConstraint getIntersection(QualitativeAllenIntervalConstraint o1, QualitativeAllenIntervalConstraint o2) {
		Vector<QualitativeAllenIntervalConstraint.Type> intersetction =  new Vector<QualitativeAllenIntervalConstraint.Type>();
		for (Type t : o1.getTypes()) {
			if (Arrays.asList(o2.getTypes()).contains(t)) intersetction.add(t);
		}
		QualitativeAllenIntervalConstraint ret = new QualitativeAllenIntervalConstraint(intersetction.toArray(new Type[intersetction.size()]));
		ret.setFrom(o1.getFrom());
		ret.setTo(o1.getTo());
		return ret;
	}
	
	/**
	 * Computes the composition of two {@link QualitativeAllenIntervalConstraint}s according to Allen's
	 * composition table.
	 * @param o1 The first constraints.
	 * @param o2 The second constraint.
	 * @return A constraint whose qualitative relations are the result of composing the two given constraints.
	 */
	private QualitativeAllenIntervalConstraint getComposition(QualitativeAllenIntervalConstraint o1, QualitativeAllenIntervalConstraint o2) {
		Vector<QualitativeAllenIntervalConstraint.Type> cmprelation =  new Vector<QualitativeAllenIntervalConstraint.Type>();
		for (int t = 0; t < o1.getTypes().length; t++) {
			for (int t2 = 0; t2 < o2.getTypes().length; t2++) {
				QualitativeAllenIntervalConstraint.Type[] tmpType = QualitativeAllenIntervalConstraint.transitionTable[o1.getTypes()[t].ordinal()][o2.getTypes()[t2].ordinal()];
				for(QualitativeAllenIntervalConstraint.Type t3: tmpType) {
					if(!cmprelation.contains(t3)) cmprelation.add(t3);
				}	
			}
		}
		QualitativeAllenIntervalConstraint ret = new QualitativeAllenIntervalConstraint(cmprelation.toArray(new Type[cmprelation.size()]));
		ret.setFrom(o1.getFrom());
		ret.setTo(o2.getTo());
		return ret;
	}
	
	/**
	 * WHAT SHOULD THIS DO?
	 * @param qc A constraint, considered as arc (<code>qc.getFrom()</code>, <code>qc.getTo()</code>).
	 * @return <code>false</code> iff the domain of qc.getFrom() "has been emptied".
	 */
	private boolean revise(QualitativeAllenIntervalConstraint qc) {
		//YOUR CODE HERE!
		return true;
	}
	
	private boolean arcConsistency() {
		//System.out.println("Running arc-consistency algorithm AC1");
		boolean fixedpoint = false;
		Constraint[] cons = this.completeNetwork.getConstraints();
		do {
			fixedpoint = true;
			for (Constraint con : cons) {
				QualitativeAllenIntervalConstraint qc = (QualitativeAllenIntervalConstraint)con;
				int typesBeforeRevision = qc.getTypes().length;
				if (!revise(qc)) return false;
				if (qc.getTypes().length < typesBeforeRevision) fixedpoint = false;
			}
		}
		while(fixedpoint == false);
		return true;
	}

	@Override
	protected boolean addConstraintsSub(Constraint[] c) {
		return true;
	}

	@Override
	protected void removeConstraintsSub(Constraint[] c) { }
	
	@Override
	protected Variable[] createVariablesSub(int num) {
		SimpleAllenInterval[] ret = new SimpleAllenInterval[num];
		for (int i = 0; i < num; i++) ret[i] = new SimpleAllenInterval(this, IDs++);
			return ret;
	}

	@Override
	protected void removeVariablesSub(Variable[] v) { }

	@Override
	public void registerValueChoiceFunctions() { }
	
	public static void main(String[] args) {
		ExQualitativeAllenSolver solver = new ExQualitativeAllenSolver(); 
		Variable[] vars = solver.createVariables(3);
		
		SimpleAllenInterval interval1 = (SimpleAllenInterval)vars[0];
		SimpleAllenInterval interval2 = (SimpleAllenInterval)vars[1];
		SimpleAllenInterval interval3 = (SimpleAllenInterval)vars[2];
		
		//interval1 {before v meets} interval2
		QualitativeAllenIntervalConstraint con0 = new QualitativeAllenIntervalConstraint(QualitativeAllenIntervalConstraint.Type.Before, QualitativeAllenIntervalConstraint.Type.Meets);
		con0.setFrom(interval1);
		con0.setTo(interval2);
//		System.out.println("Adding constraint " + con0 + ": " + solver.addConstraint(con0));
		
		//interval2 {after} interval3 
		QualitativeAllenIntervalConstraint con1 = new QualitativeAllenIntervalConstraint(QualitativeAllenIntervalConstraint.Type.After);
		con1.setFrom(interval2);
		con1.setTo(interval3);
//		System.out.println("Adding constraint " + con1 + ": " + solver.addConstraint(con1));
		
		//interval3 {finishes} interval1
		QualitativeAllenIntervalConstraint con2 = new QualitativeAllenIntervalConstraint(QualitativeAllenIntervalConstraint.Type.Finishes);
		con2.setFrom(interval3);
		con2.setTo(interval1);
//		System.out.println("Adding constraint " + con2 + ": " + solver.addConstraint(con2));
		
		//Try to add the constraints
		if (!solver.addConstraints(con0,con1,con2)) System.out.println("Failed to add constraints!");
		else System.out.println("Added constraints!");
		
		ConstraintNetwork.draw(solver.getConstraintNetwork(),"Constraint Network");
	}


}
