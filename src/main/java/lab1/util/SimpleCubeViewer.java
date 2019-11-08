package lab1.util;
import java.awt.Container;
import java.awt.GraphicsConfiguration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;


import aima.core.agent.Action;
import lab1.RubiksCube;
import lab1.util.cubie_viz.Viewer3D;

public class SimpleCubeViewer {

	public static GraphicsConfiguration gc;
	private Viewer3D viewer = null;
	private int w, h;
	private static int buffer = 10;
	private boolean ready = false;
	
	public SimpleCubeViewer(int w, int h) {
		this.w = w;
		this.h = h;
		JFrame frame= new JFrame(gc);
		frame.setTitle("Rubik's Cube visualization");
		this.setup(frame);
		frame.setSize(this.w+buffer, this.h+buffer);
		//frame.setLocation(200, 200);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		try { Thread.sleep(1000); }
		catch (InterruptedException e) { e.printStackTrace(); }
		viewer.setDisabledMouseMoves(true);
	}
	
	private void setup(JFrame frame) {
		Container cp = frame.getContentPane();
		ActionListener a = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//System.out.println("action: " + arg0);
			}
		};
		this.viewer = new Viewer3D(this.w, this.h, a);
		cp.add(viewer);
		
		//Put in same pose as lab explanation
		viewer.showMove(9, -1);

	}

	public void showMoves(List<Action> moves) {
		this.showMoves(moves.toArray(new Action[moves.size()]));
	}
	
	public void showMoves(Action ... moves) {
		
		for (Action move : moves) {
			if (move.equals(RubiksCube.TOP)) viewer.showMove(0, 1);
			else if (move.equals(RubiksCube.TOPI)) viewer.showMove(0, -1);
			else if (move.equals(RubiksCube.RIGHT)) viewer.showMove(1, 1);
			else if (move.equals(RubiksCube.RIGHTI)) viewer.showMove(1, -1);
			else if (move.equals(RubiksCube.FRONT)) viewer.showMove(2, 1);
			else if (move.equals(RubiksCube.FRONTI)) viewer.showMove(2, -1);
			else if (move.equals(RubiksCube.BOTTOM)) viewer.showMove(3, 1);
			else if (move.equals(RubiksCube.BOTTOMI)) viewer.showMove(3, -1);
			else if (move.equals(RubiksCube.LEFT)) viewer.showMove(4, 1);
			else if (move.equals(RubiksCube.LEFTI)) viewer.showMove(4, -1);
			else if (move.equals(RubiksCube.BACK)) viewer.showMove(5, 1);
			else if (move.equals(RubiksCube.BACKI)) viewer.showMove(5, -1);
		    
			while (viewer.isTwisting()) {
				try { Thread.sleep(10); }
				catch (InterruptedException e) { e.printStackTrace(); }
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException{
		SimpleCubeViewer scv = new SimpleCubeViewer(400, 400);
		scv.showMoves(
				RubiksCube.TOP, RubiksCube.TOPI,
				RubiksCube.BOTTOM, RubiksCube.BOTTOMI,
				
				RubiksCube.LEFT, RubiksCube.LEFTI,
				RubiksCube.RIGHT, RubiksCube.RIGHTI,
				
				RubiksCube.FRONT, RubiksCube.FRONTI,
				RubiksCube.BACK, RubiksCube.BACKI
			);
	}
	
}
