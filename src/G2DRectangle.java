import java.awt.Color;

public class G2DRectangle implements G2DObject {
	
	/*
	 * Store the rectangle as a G2DPolygon
	 * This is necessary because when a rectangle is rotated it can no longer be easily specified by the coordinates of it's corner
	 * Makes implementation easy as the G2DRectangle class is now, effectively, a set of special constructors for a G2DPolygon
	 */
	private G2DPolygon poly;
	
	/*
	 * The constructors 
	 *    - allow construction with topleft and bottomRight corners either as points or x,y
	 *    - also allow construction with and without colour (without implies black)
	 *    - all constructors effectively just create a G2DPolygon
	 */
	
	public G2DRectangle(G2DPoint topLeft, G2DPoint bottomRight){
		this(topLeft.getX(), topLeft.getY(), bottomRight.getX(), bottomRight.getY(), Color.BLACK);
	}
	public G2DRectangle(G2DPoint topLeft, G2DPoint bottomRight, Color color){
		this(topLeft.getX(), topLeft.getY(), bottomRight.getX(), bottomRight.getY(), color);
	}
	public G2DRectangle(double x1, double y1, double x2, double y2){
		this(x1,y1,x2,y2,Color.BLACK);
	}
	public G2DRectangle(double x1, double y1, double x2, double y2, Color color){
		poly = new G2DPolygon(color);
		poly.addPoint(x1, y1);
		poly.addPoint(x1, y2);
		poly.addPoint(x2, y2);
		poly.addPoint(x2, y1);
	}
	
	/*
	 *  A special constructor needed for deepCloning - this allows the creation of a new object
	 *  of type G2DRectangle so that deepClone can return a G2DRectangle and not a G2DPolygon
	 *  - is private to stop external calls as these may create G2DRectangle objects that do not 
	 *  contain rectangles (as any valid polygon could be passed).
	 */
	private G2DRectangle(G2DPolygon poly){
		this.poly = poly;
	}

	@Override
	public G2DRectangle deepClone() {
		return new G2DRectangle(poly.deepClone());
	}

	/*
	 * Remaining functions should simply call the ploygon equivalent
	 */
	
	@Override
	public void draw(G2DAbstractCanvas absCanvas) {
		poly.draw(absCanvas);
	}

	@Override
	public void setColor(Color color) {
		poly.setColor(color);
	}
	@Override
	public void transform(Matrix transformationMatrix) {
		poly.transform(transformationMatrix);
	}

}
