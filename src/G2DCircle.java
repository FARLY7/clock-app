import java.awt.Color;


public class G2DCircle implements G2DObject{

	private G2DPolygon poly;
	private G2DPoint centre;
	private int radius;
	
	public G2DCircle(double x, double y, int radius, Color color){
		this( new G2DPoint(x, y), radius, color);
	}
	public G2DCircle(double x, double y, int radius){
		this(new G2DPoint(x, y), radius, Color.BLACK);
	}
	public G2DCircle(G2DPoint centre, int radius){
		this(centre, radius, Color.BLACK);
	}
	public G2DCircle(G2DPoint centre, int radius, Color color){
		poly = new G2DPolygon(color);
		this.centre = centre;
		this.radius = radius;
		
		for( int i = 0 ; i < 359 ; i += 1)
		{
			G2DPoint point = new G2DPoint(centre.getX(), centre.getY() - radius);
			point.transform(MatrixTransform.rotate(centre.getX(), centre.getY(), i));
			poly.addPoint(point);
		}		
	}
	
	public void draw(G2DAbstractCanvas absCanvas) {
		poly.draw(absCanvas);
	}

	public void setColor(Color color)
	{
		poly.setColor(color);
	}

	public G2DCircle deepClone() {
		return new G2DCircle(poly.deepClone(), centre.deepClone(), radius);
	}
	
	private G2DCircle(G2DPolygon poly, G2DPoint centre, int radius){
		this.poly = poly;
		this.centre = centre;
		this.radius = radius;
	}

	public G2DPoint getCentre(){
		return centre;
	}

	public void transform(Matrix transformationMatrix) {
		poly.transform(transformationMatrix);
	}
	
}
