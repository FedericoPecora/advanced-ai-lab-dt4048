package solutions.lab2;

import java.util.ArrayList;

import org.metacsp.framework.Constraint;
import org.metacsp.framework.ConstraintNetwork;
import org.metacsp.framework.ConstraintSolver;
import org.metacsp.framework.ValueOrderingH;
import org.metacsp.framework.Variable;
import org.metacsp.framework.VariableOrderingH;
import org.metacsp.framework.meta.MetaConstraint;
import org.metacsp.framework.meta.MetaVariable;
import org.metacsp.time.qualitative.QualitativeAllenIntervalConstraint;
import org.metacsp.time.qualitative.QualitativeAllenIntervalConstraint.Type;

public class AllenLabeling extends MetaConstraint {

	public AllenLabeling(VariableOrderingH varOH, ValueOrderingH valOH) {
		super(varOH, valOH);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 6574627810008056717L;

	@Override
	public ConstraintNetwork[] getMetaVariables() {
		ArrayList<ConstraintNetwork> ret = new ArrayList<ConstraintNetwork>();
		for (Variable v1 : this.getGroundSolver().getVariables()) {
			for (Variable v2 : this.getGroundSolver().getVariables()) {
				if (!v1.equals(v2)) {
					Constraint[] cons = this.getGroundSolver().getConstraints(v1, v2);
					QualitativeAllenIntervalConstraint inters = ExQualitativeAllenSolverComplete.createUniversalConstraint(v1, v2);
					for (Constraint con : cons) {
						QualitativeAllenIntervalConstraint qc = (QualitativeAllenIntervalConstraint)con;
						inters = ExQualitativeAllenSolverComplete.getIntersection(inters, qc);
					}
					if (inters.getTypes().length > 1) {
						ConstraintNetwork cn = new ConstraintNetwork(this.getGroundSolver());
						cn.addConstraint(inters);
						ret.add(cn);						
					}
				}
			}
		}
		return ret.toArray(new ConstraintNetwork[ret.size()]);
	}

	@Override
	public ConstraintNetwork[] getMetaValues(MetaVariable metaVariable) {
		ConstraintNetwork cn = metaVariable.getConstraintNetwork();
		ArrayList<ConstraintNetwork> ret = new ArrayList<ConstraintNetwork>(); 
		QualitativeAllenIntervalConstraint qc = (QualitativeAllenIntervalConstraint)cn.getConstraints()[0];
		for (Type t : qc.getTypes()) {
			QualitativeAllenIntervalConstraint oneRelation = new QualitativeAllenIntervalConstraint(t);
			oneRelation.setFrom(qc.getFrom());
			oneRelation.setTo(qc.getTo());
			ConstraintNetwork oneValue = new ConstraintNetwork(this.getGroundSolver());
			oneValue.addConstraint(oneRelation);
			ret.add(oneValue);
		}
		return ret.toArray(new ConstraintNetwork[ret.size()]);
	}

	@Override
	public void markResolvedSub(MetaVariable metaVariable, ConstraintNetwork metaValue) {
		// TODO Auto-generated method stub
	}

	@Override
	public void draw(ConstraintNetwork network) {
		// TODO Auto-generated method stub
	}

	@Override
	public ConstraintSolver getGroundSolver() {
		return this.metaCS.getConstraintSolvers()[0];
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEdgeLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEquivalent(Constraint c) {
		// TODO Auto-generated method stub
		return false;
	}

}
