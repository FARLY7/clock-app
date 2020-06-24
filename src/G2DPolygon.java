import java.awt.Color;
import java.awt.Polygon;
import java.util.ArrayList;

/*
 * A Graphics 2D Polygon 
 * Defined as a set of G2DPoints 
 * On construction an empty ArrayList of points is created then
 * addPoint is called to add new points.
 * 
 */
public class G2DPolygon implements G2DObject {
	
	private ArrayList<G2DPoint> points;
	private Polygon awtPolygon;
	private Color color;
	
	// -- Constructors 
	public G2DPolygon(){
		this(Color.BLACK);
	}
	public G2DPolygon(Color color){
		this.color = color;
		this.points = new ArrayList<G2DPoint>();
	}
	
	// -- other functions

	public void addPoint(G2DPoint pt){
		points.add(pt);
	}
		
	/*
	 * Draw a filled polygon in the given abstract Canvas 
	 * @see uk.ac.strath.aes02112.graphics2012.G2DObject#draw(uk.ac.strath.aes02112.graphics2012.G2DAbstractCanvas)
	 */
	@Override
	public void draw(G2DAbstractCanvas absCanvas) {
		// First create a polygon of physical points on the physical canvas space then draw that polygon using AWT
		awtPolygon = new Polygon();
		for (G2DPoint absPt : points )
			awtPolygon.addPoint(absCanvas.physicalX(absPt.getX()), absCanvas.physicalY(absPt.getY()));
		
		absCanvas.getPhysicalGraphics().setColor(color);
		absCanvas.getPhysicalGraphics().fillPolygon(awtPolygon);
		absCanvas.getPhysicalGraphics().drawPolygon(awtPolygon);
	}

	@Override
	public G2DPolygon deepClone() {
		G2DPolygon newPoly = new G2DPolygon(new Color(color.getRGB()));
		for (G2DPoint pt : points )
			newPoly.addPoint(pt.deepClone());
		return newPoly;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public void addPoint(double x, double y) {
		addPoint(new G2DPoint(x,y));
	}
	
	public boolean isInside(int x, int y){
		return awtPolygon.contains(x, y);
	}
	
	public void transform(Matrix transformationMatrix) {
		for (G2DPoint pt : points) pt.transform(transformationMatrix);
	}
	
	
	
}
