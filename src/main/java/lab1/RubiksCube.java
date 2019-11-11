package lab1;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import aima.core.agent.Action;
import aima.core.agent.impl.DynamicAction;

public class RubiksCube {
	
	private int n;
	private List<Action> initialMoves = null;

	/**
	 * Rotates the left disc 90 degrees counter-clockwise 
	 */
	public static Action LEFT = new DynamicAction("Left");

	/**
	 * Rotates the right disc 90 degrees counter-clockwise 
	 */
	public static Action RIGHT = new DynamicAction("Right");

	/**
	 * Rotates the top disc 90 degrees counter-clockwise 
	 */
	public static Action TOP = new DynamicAction("Top");

	/**
	 * Rotates the bottom disc 90 degrees counter-clockwise 
	 */
	public static Action BOTTOM = new DynamicAction("Bottom");

	/**
	 * Rotates the front disc 90 degrees counter-clockwise 
	 */
	public static Action FRONT = new DynamicAction("Front");

	/**
	 * Rotates the back disc 90 degrees counter-clockwise 
	 */
	public static Action BACK = new DynamicAction("Back");
	
	//inverse
	
	/**
	 * Rotates the left disc 90 degrees clockwise 
	 */
	public static Action LEFTI = new DynamicAction("LeftI");

	/**
	 * Rotates the right disc 90 degrees clockwise 
	 */
	public static Action RIGHTI = new DynamicAction("RightI");

	/**
	 * Rotates the top disc 90 degrees clockwise 
	 */
	public static Action TOPI = new DynamicAction("TopI");

	/**
	 * Rotates the bottom disc 90 degrees clockwise 
	 */
	public static Action BOTTOMI = new DynamicAction("BottomI");

	/**
	 * Rotates the front disc 90 degrees clockwise 
	 */
	public static Action FRONTI = new DynamicAction("FrontI");

	/**
	 * Rotates the back disc 90 degrees clockwise 
	 */
	public static Action BACKI = new DynamicAction("BackI");
	
	public static enum rubikCubeFace {front, back, left, right, bottom, top};
	
	private Cubie[][][] cube;
	
	@Override
	public boolean equals(Object o){
		
		RubiksCube rc = (RubiksCube)o;		
		return rc.toRepresentation().equals(this.toRepresentation());
	}
	
	@Override 	
	public int hashCode(){
		return this.toRepresentation().hashCode();
	}
	
	private String toRepresentation(){
		
		String ret = "";
		
		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				for (int k = 0; k < n; k++){
					for (int k2 = 0; k2 < this.cube[i][j][k].getFaces().length; k2++) {
						ret += this.cube[i][j][k].getFaces()[k2];
					}					
					
				}
			}
		}	
		return ret;
		
	}
	
	/**
	 * Get the list of initial moves applied to this {@link RubiksCube}.
	 * @return The list of initial moves applied to this {@link RubiksCube}.
	 */
	public List<Action> getInitialMoves() {
		return initialMoves;
	}
	
	/**
	 * Creates a (n x n x n) Rubik's Cube. Its state is obtained by applying a given number of random (symmetric) moves
	 * from the "solved" state. 
	 * @param n The dimension of the cube. 
	 * @param r Number of initial random moves.
	 */
	public RubiksCube(int n, int r){
		this.n = n;
		cube = new Cubie[n][n][n];
		initialize();
		this.initialMoves = generateRandomMoves(r);
	}

	/**
	 * Apply a given move to this {@link RubiksCube}
	 * @param a A valid {@link RubiksCube} move.
	 */
	public void move(Action a) {
		if (a.equals(RubiksCube.BACK)) this.moveBack();
		else if (a.equals(RubiksCube.BACKI)) this.moveBackI();
		else if (a.equals(RubiksCube.FRONT)) this.moveFront();
		else if (a.equals(RubiksCube.FRONTI)) this.moveFrontI();
		else if (a.equals(RubiksCube.BOTTOM)) this.moveDown();
		else if (a.equals(RubiksCube.BOTTOMI)) this.moveDownI();
		else if (a.equals(RubiksCube.TOP)) this.moveUp();
		else if (a.equals(RubiksCube.TOPI)) this.moveUpI();
		else if (a.equals(RubiksCube.LEFT)) this.moveLeft();
		else if (a.equals(RubiksCube.LEFTI)) this.moveLeftI();
		else if (a.equals(RubiksCube.RIGHT)) this.moveRight();
		else if (a.equals(RubiksCube.RIGHTI)) this.moveRightI();
	}
	
	private ArrayList<Action> generateRandomMoves(int m){
		//Action[] actions = RubikCubeFunctionFactory.getSymmetricActionsFunction().actions(this).toArray(new Action[RubikCubeFunctionFactory.getSymmetricActionsFunction().actions(this).size()]);
//		String initialMoves = "";
		
		ArrayList<Action> ret = new ArrayList<Action>();
		
		Set<Action> actionSet = new LinkedHashSet<Action>();
		actionSet.add(RubiksCube.BACK);
		actionSet.add(RubiksCube.FRONT);
		actionSet.add(RubiksCube.BOTTOM);
		actionSet.add(RubiksCube.TOP);		
		actionSet.add(RubiksCube.LEFT);
		actionSet.add(RubiksCube.RIGHT);
		actionSet.add(RubiksCube.BACKI);
		actionSet.add(RubiksCube.FRONTI);
		actionSet.add(RubiksCube.BOTTOMI);
		actionSet.add(RubiksCube.TOPI);		
		actionSet.add(RubiksCube.LEFTI);
		actionSet.add(RubiksCube.RIGHTI);
		Action[] actions = actionSet.toArray(new Action[actionSet.size()]);
		
	    Random randomGenerator = new Random(370991777);
	    for (int i = 0; i < m; i++){
	    	int randomInt = randomGenerator.nextInt(actions.length);
			if (RubiksCube.FRONT.equals(actions[randomInt]))	{
				ret.add(RubiksCube.FRONT);
				moveFront();
//				initialMoves += RubiksCube.FRONT + " - "; 
			}
			else if (RubiksCube.BACK.equals(actions[randomInt])) {
				ret.add(RubiksCube.BACK);
				moveBack();
//				initialMoves += RubiksCube.BACK + " - ";
			}
			else if (RubiksCube.LEFT.equals(actions[randomInt])) {
				ret.add(RubiksCube.LEFT);
				moveLeft();
//				initialMoves += RubiksCube.LEFT + " - ";
			}
			else if (RubiksCube.RIGHT.equals(actions[randomInt])) {
				ret.add(RubiksCube.RIGHT);
				moveRight();
//				initialMoves += RubiksCube.RIGHT + " - ";
			}
			else if (RubiksCube.TOP.equals(actions[randomInt])) {
				ret.add(RubiksCube.TOP);
				moveUp();
//				initialMoves += RubiksCube.TOP + " - ";
			}
			else if (RubiksCube.BOTTOM.equals(actions[randomInt])) {
				ret.add(RubiksCube.BOTTOM);
				moveDown();
//				initialMoves += RubiksCube.BOTTOM + " - ";
			}
			else if (RubiksCube.FRONTI.equals(actions[randomInt])) {
				ret.add(RubiksCube.FRONTI);
				moveFrontI();
//				initialMoves += RubiksCube.FRONTI + " - ";
			}
			else if (RubiksCube.BACKI.equals(actions[randomInt])) {
				ret.add(RubiksCube.BACKI);
				moveBackI();
//				initialMoves += RubiksCube.BACKI + " - ";
			}
			else if (RubiksCube.LEFTI.equals(actions[randomInt])) {
				ret.add(RubiksCube.LEFTI);
				moveLeftI();
//				initialMoves += RubiksCube.LEFTI + " - ";
			}
			else if (RubiksCube.RIGHTI.equals(actions[randomInt])) {
				ret.add(RubiksCube.RIGHTI);
				moveRightI();
//				initialMoves += RubiksCube.RIGHTI + " - ";
			}
			else if (RubiksCube.TOPI.equals(actions[randomInt])) {
				ret.add(RubiksCube.TOPI);
				moveUpI();
//				initialMoves += RubiksCube.TOPI + " - ";
			}
			else if (RubiksCube.BOTTOMI.equals(actions[randomInt])) {
				ret.add(RubiksCube.BOTTOMI);
				moveDownI();
//				initialMoves += RubiksCube.BOTTOMI + " - ";
			}
	    }
    	//System.out.println("Initial moves: " + initialMoves.substring(0,initialMoves.length()-3));
	    return ret;
	}

	/**
	 * Creates a Rubik's cube which is identical to a give Rubik's cube.
	 * @param copyRubik a Rubik's cube to copy.
	 */
	public RubiksCube(RubiksCube copyRubik) {
		this(copyRubik.getState());
	}
	
	/**
	 * Creates a Rubik's cube given a 3D representation of the state.
	 * @param copyRubik a 3D representation of the state.
	 */	
	public RubiksCube(Cubie[][][] state) {
		this.n = state.length;
		this.cube = copy(state);
		
	}
	
	/**
	 * Returns the current state of the Rubik's cube.
	 * @return the current state of the Rubik's cube.
	 */
	public Cubie[][][] getState() {
		return cube;
	}
	
	private String printFace(Cubie[][][] c, rubikCubeFace face){
		String ret = "= " + face + " =\n";
		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j < c.length; j++) {
				if(face.ordinal() == 0){
					ret += c[0][i][j].getFaceByDim(0).getColor();
					//System.out.print(c[0][i][j].getFaceByDim(0).getColor());
				}
				else if(face.ordinal() == 1){
					ret += c[n-1][i][j].getFaceByDim(1).getColor();
					//System.out.print(c[n-1][i][j].getFaceByDim(1).getColor());
				}
				else if(face.ordinal() == 2){
					ret += c[i][0][j].getFaceByDim(3).getColor();
					//System.out.print(c[i][0][j].getFaceByDim(3).getColor());
				}
				else if(face.ordinal() == 3){
					ret += c[i][n-1][j].getFaceByDim(2).getColor();
					//System.out.print(c[i][n-1][j].getFaceByDim(2).getColor());
				}
				else if(face.ordinal() == 4){
					ret += c[i][j][0].getFaceByDim(4).getColor();
					//System.out.print(c[i][j][0].getFaceByDim(4).getColor());
				}
				else if(face.ordinal() == 5){
					ret += c[i][j][n-1].getFaceByDim(5).getColor();
					//System.out.print(c[i][j][n-1].getFaceByDim(5).getColor());
				}
			}
			ret += "\n";
		}
		
		return ret;
		
	}
	
	public String toString() {
		String ret = "";
		for (rubikCubeFace face : rubikCubeFace.values()) {
			ret += printFace(getState(), face) + "\n";
		}
		return ret;
	}

	private Face[] getInitialFaces(){
		Vector<Face> face = new Vector<Face>();		
		face.add(new Face("y"));
		face.add(new Face("w"));
		face.add(new Face("b"));
		face.add(new Face("g"));
		face.add(new Face("o"));
		face.add(new Face("r"));
		return face.toArray(new Face[face.size()]);
	}
	
	private void initialize(){
		int counter = 1;
		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				for (int k = 0; k < n; k++){
					Cubie c = new Cubie(counter, getInitialFaces());
					cube[i][j][k] = c;
					counter++;
				}
			}
		}
	}


	
	private Cubie [][][] copy(Cubie [][][] cube){
		Cubie[][][] result = new Cubie[n][n][n];
		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				for (int k = 0; k < n; k++){
					Cubie cnew = new Cubie(cube[i][j][k].getId(), copyFaces(cube[i][j][k].getFaces()));					
					result[i][j][k] = cnew;
				}
			}
		}				
		return result;
	}

	private Face[] copyFaces(Face[] faces) {
		
		Vector<Face> ret = new Vector<Face>();
		for (int i = 0; i < faces.length; i++) {
			ret.add(new Face(faces[i].getColor()));
		}
		
		return ret.toArray(new Face[ret.size()]);
	}
	
	private Cubie rotate(Cubie c, int dim) {		
		Face[] fs = new Face[6];
		if(dim == 1){
			//
			fs[0] = new Face(c.getFaceByDim(0).getColor());
			fs[1] = new Face(c.getFaceByDim(1).getColor());
			
			fs[5] = new Face(c.getFaceByDim(2).getColor());
			fs[2] = new Face(c.getFaceByDim(4).getColor());
			fs[4] = new Face(c.getFaceByDim(3).getColor());
			fs[3] = new Face(c.getFaceByDim(5).getColor());
			
		}
		
		else if(dim == 2){
			
			fs[2] = new Face(c.getFaceByDim(2).getColor());
			fs[3] = new Face(c.getFaceByDim(3).getColor());
			
			fs[0] = new Face(c.getFaceByDim(4).getColor());
			fs[4] = new Face(c.getFaceByDim(1).getColor());
			fs[1] = new Face(c.getFaceByDim(5).getColor());
			fs[5] = new Face(c.getFaceByDim(0).getColor());
			
		}
		else if(dim == 3){
			//2-0-3-1-2
			fs[4] = new Face(c.getFaceByDim(4).getColor());
			fs[5] = new Face(c.getFaceByDim(5).getColor());
			
			fs[0] = new Face(c.getFaceByDim(2).getColor());
			fs[3] = new Face(c.getFaceByDim(0).getColor());
			fs[1] = new Face(c.getFaceByDim(3).getColor());
			fs[2] = new Face(c.getFaceByDim(1).getColor());
			
		}
		else if(dim == -1){
			//
			fs[0] = new Face(c.getFaceByDim(0).getColor());
			fs[1] = new Face(c.getFaceByDim(1).getColor());
			
			fs[2] = new Face(c.getFaceByDim(5).getColor());
			fs[4] = new Face(c.getFaceByDim(2).getColor());
			fs[3] = new Face(c.getFaceByDim(4).getColor());
			fs[5] = new Face(c.getFaceByDim(3).getColor());
			
		}
		
		else if(dim == -2){
			
			fs[2] = new Face(c.getFaceByDim(2).getColor());
			fs[3] = new Face(c.getFaceByDim(3).getColor());
			
			fs[4] = new Face(c.getFaceByDim(0).getColor());
			fs[1] = new Face(c.getFaceByDim(4).getColor());
			fs[5] = new Face(c.getFaceByDim(1).getColor());
			fs[0] = new Face(c.getFaceByDim(5).getColor());
			
		}
		else if(dim == -3){
			
			fs[4] = new Face(c.getFaceByDim(4).getColor());
			fs[5] = new Face(c.getFaceByDim(5).getColor());
			
			fs[2] = new Face(c.getFaceByDim(0).getColor());
			fs[0] = new Face(c.getFaceByDim(3).getColor());
			fs[3] = new Face(c.getFaceByDim(1).getColor());
			fs[1] = new Face(c.getFaceByDim(2).getColor());
			
		}
		return new Cubie(c.getId(), fs);
	}

	private Cubie[][] rotate(Cubie[][] ar, int dim){
		Cubie[][] result = new Cubie[n][n];
		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				if(dim == 1){
					result[n - j - 1][i] = rotate(ar[i][j], dim);
				}
				else if(dim == 2){
					result[n - j - 1][i] = rotate(ar[i][j], dim);
				}
				else if(dim == 3){
					result[n - j - 1][i] = rotate(ar[i][j], dim);
				}
				else if(dim == -1){					
					result[i][n - j - 1] = rotate(ar[j][i], dim);
				}
				else if(dim == -2){
					result[i][n - j - 1] = rotate(ar[j][i], dim);
				}
				else if(dim == -3){
					result[i][n - j - 1] = rotate(ar[j][i], dim);
				}

			}
		}
		return result;
	}

	private void rotate(Cubie [][][] cube, int dim, int num){
		Cubie[][][] result = copy(cube);
		Cubie[][] intermediate = new Cubie[n][n];
		
		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				if (dim == 1 || dim == -1){
					intermediate[i][j] = new Cubie(cube[num][i][j].getId(), copyFaces(cube[num][i][j].getFaces()));
				}				
				else if (dim == 2 || dim == -2){
					intermediate[i][j] = new Cubie(cube[i][num][j].getId(), copyFaces(cube[i][num][j].getFaces()));
				}				
				else if (dim == 3 || dim == -3){
					intermediate[i][j] = new Cubie(cube[i][j][num].getId(), copyFaces(cube[i][j][num].getFaces()));
				}				
			}
		}
		Cubie[][] tmp = rotate(intermediate, dim);
		for (int i = 0; i < n; i++){
			for (int j = 0; j < n; j++){
				if (dim == 1 || dim == -1){
					result[num][i][j] = tmp[i][j];
				}				
				else if (dim == 2 || dim == -2){
					result[i][num][j] = tmp[i][j];
				}				
				else if (dim == 3 || dim == -3){
					result[i][j][num] = tmp[i][j];
				}				
			}
		}
		
		this.cube = copy(result);
	}
	
	/**
	 * Implements LEFT 
	 */
	public void moveLeft(){
		rotate(this.cube, 2, 0);
	}
	
	/**
	 * Implements RIGHT 
	 */	
	public void moveRight(){
		rotate(this.cube, 2, n-1);
	}

	/**
	 * Implements UP 
	 */
	public void moveUp(){
		rotate(this.cube, 3, n-1);
	}

	/**
	 * Implements DOWN 
	 */
	public void moveDown(){
		rotate(this.cube, 3, 0);		
	}
	
	/**
	 * Implements FRONT 
	 */
	public void moveFront(){
		rotate(this.cube, 1, 0);		
	}

	/**
	 * Implements BACK 
	 */
	public void moveBack(){
		rotate(this.cube, 1, n-1);		
	}

	/////////////////////////////////////////
	
	/**
	 * Implements LEFTI 
	 */
	public void moveLeftI(){
		rotate(this.cube, -2, 0);
	}

	/**
	 * Implements RIGHTI 
	 */
	public void moveRightI(){
		rotate(this.cube, -2, n-1);

	}

	/**
	 * Implements UPI 
	 */
	public void moveUpI(){
		rotate(this.cube, -3, n-1);
	}

	/**
	 * Implements DOWNI 
	 */
	public void moveDownI(){
		rotate(this.cube, -3, 0);
	}
	
	/**
	 * Implements FRONTI 
	 */
	public void moveFrontI(){
		rotate(this.cube, -1, 0);		
	}

	/**
	 * Implements BACKI 
	 */
	public void moveBackI(){
		rotate(this.cube, -1, n-1);		
	}
}
