
import java.awt.Color;

/*
 * A point object implementing G2DObject
 * This isn't much use for drawing on screen but is used extensively to define other objects
 */
public class G2DPoint implements G2DObject {

	private Matrix ptMatrix;
	private Color color = Color.BLACK;
	
	// -- Constructors
	
	public G2DPoint(double x, double y){
		ptMatrix = new Matrix(3,1);//create homogeneous 2D coordinate vector - a 3row 1col matrix
		this.setX(x);
		this.setY(y);
		ptMatrix.set(2, 0, 1);
	}
	
	public G2DPoint(double x, double y, Color color){
		this(x,y);
		this.setColor(color);
	}
	
	// -- other methods
	
	public void setColor(Color color){
		this.color = color;
	}
	
	@Override
	public void draw(G2DAbstractCanvas absCanvas) {
		absCanvas.getPhysicalGraphics().setColor(color);
		int px = absCanvas.physicalX(getX());
		int py = absCanvas.physicalY(getY());
		absCanvas.getPhysicalGraphics().drawLine(px, py, px,py);
	}

	@Override
	public G2DPoint deepClone() {
		return new G2DPoint(getX(), getY(), new Color(color.getRGB()));
	}

	double getX() {
		return ptMatrix.get(0, 0);
	}

	void setX(double x) {
		ptMatrix.set(0, 0, x);
	}

	double getY() {
		return ptMatrix.get(1, 0);
	}

	void setY(double y) {
		ptMatrix.set(1, 0, y);
	}

	@Override
	public void transform(Matrix transformationMatrix) {
		ptMatrix = transformationMatrix.multiply(ptMatrix);
	}


}

