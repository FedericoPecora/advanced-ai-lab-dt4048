package lab1;
public class Cubie{
	
	private Face[] faces = new Face[6];
	private int id;
	//face front 0
	//face back 1
	//face right 2
	//face left 3
	//face up 4
	//face down 5
	
	public Cubie(int id, Face[] faces){
		this.id = id;
		this.faces = faces;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public Face[] getFaces() {
		return faces;
	}
	
	public void setFaces(Face[] faces) {
		this.faces = faces;
	}
	
	public Face getFaceByDim(int i){
		for (int j = 0; j < faces.length; j++) {
			if(i == j)
				return faces[j];
		}
		return null;
	}
	
}
