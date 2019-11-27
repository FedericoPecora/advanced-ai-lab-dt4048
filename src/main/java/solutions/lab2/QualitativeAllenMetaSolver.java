package solutions.lab2;

import org.metacsp.framework.ConstraintNetwork;
import org.metacsp.framework.meta.MetaConstraintSolver;
import org.metacsp.framework.meta.MetaVariable;
import org.metacsp.time.qualitative.QualitativeAllenIntervalConstraint;

public class QualitativeAllenMetaSolver extends MetaConstraintSolver {

	public QualitativeAllenMetaSolver() {
		super(new Class[] {QualitativeAllenIntervalConstraint.class}, 0, new ExQualitativeAllenSolverComplete());
	}

	private static final long serialVersionUID = -4483336505882361527L;

	@Override
	public void preBacktrack() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void postBacktrack(MetaVariable metaVariable) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void retractResolverSub(ConstraintNetwork metaVariable, ConstraintNetwork metaValue) {
		// TODO Auto-generated method stub
	}

	@Override
	protected boolean addResolverSub(ConstraintNetwork metaVariable, ConstraintNetwork metaValue) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected double getUpperBound() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void setUpperBound() {
		// TODO Auto-generated method stub
	}

	@Override
	protected double getLowerBound() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void setLowerBound() {
		// TODO Auto-generated method stub
	}

	@Override
	protected boolean hasConflictClause(ConstraintNetwork metaValue) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void resetFalseClause() {
		// TODO Auto-generated method stub
	}

}
