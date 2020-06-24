import java.awt.Color;
import java.util.ArrayList;

public class G2DGroup implements G2DObject {
	
	ArrayList<G2DObject> objects ;
	
	public G2DGroup(){ objects = new ArrayList<G2DObject>();}
	
	private G2DGroup(ArrayList<G2DObject> objects){
		this.objects = objects;
	}

	public void draw(G2DAbstractCanvas absCanvas) {
		for (G2DObject obj : objects) obj.draw(absCanvas);
	}

	public G2DGroup deepClone() {
		ArrayList<G2DObject> newObjects = new ArrayList<G2DObject>();
		for (G2DObject obj : objects)
			newObjects.add(obj.deepClone());
		return new G2DGroup(newObjects);
	}

	public void setColor(Color color) {
		for (G2DObject obj : objects) obj.setColor(color);
	}
	
	public void add(G2DObject obj) {
		objects.add(obj);
	}
	
	public void add(G2DGroup group) {		
		for(G2DObject obj : group.objects) objects.add(obj);
	}

	public void transform(Matrix transformationMatrix) {
		for (G2DObject o : objects) o.transform(transformationMatrix);
	}
	

}
