import java.awt.Color;

/*
 * An interface for a general Graphics 2D Object
 * All G2D objects should implement this so that this set
 * of operations can work on all classes.
 */
public interface G2DObject {

	/*
	 * Draw the current object onto the given abstract canvas
	 */
	public void draw(G2DAbstractCanvas absCanvas);
	
	/*
	 * Clone the current object deeply - not called clone to save hassle from Cloneable
	 */
	public G2DObject deepClone();
	
	public void setColor(Color color);
	
	/**
	 * Translate the object by +x. +y in the abstract coordinate space
	 * @param x
	 * @param y
	 */
	//public void translate(double x, double y);
	
	/**
	 * Rotate the object around the origin by  **degrees""
	 * @param angle
	 */
	//public void rotateAroundOrigin(double degrees);
	
	/**
	 * Rotate the object around its own centre by  **degrees""
	 * @param angle
	 */
	//public void rotateAroundPoint( double x, double y, double degrees);
	
	
	public void transform(Matrix transformationMatrix);
}
