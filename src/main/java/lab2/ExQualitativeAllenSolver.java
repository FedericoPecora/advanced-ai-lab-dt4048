package lab2;

import java.util.Arrays;
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

	@Override
	public boolean propagate() {		
		if(this.getConstraints().length == 0) return true;
		createCompleteNetwork();
		successfulPropagation = false;
		if (pathConsistency()) {
			successfulPropagation = true;
		}
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

	@Override
	protected boolean addConstraintsSub(Constraint[] c) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void removeConstraintsSub(Constraint[] c) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected Variable[] createVariablesSub(int num) {
		SimpleAllenInterval[] ret = new SimpleAllenInterval[num];
		for (int i = 0; i < num; i++) ret[i] = new SimpleAllenInterval(this, IDs++);
			return ret;
	}

	@Override
	protected void removeVariablesSub(Variable[] v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerValueChoiceFunctions() {
		// TODO Auto-generated method stub
		
	}
	
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
		
		ConstraintNetwork.draw(solver.getConstraintNetwork(),"BEFORE PROPAGATION");
		
		//Try to add the constraints
		if (!solver.addConstraints(con0,con1,con2)) System.out.println("Failed to add constraints!");
		else System.out.println("Added constraints!");
		
		ConstraintNetwork.draw(solver.getConstraintNetwork(),"AFTER PROPAGATION");
	}


}
