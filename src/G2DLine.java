import java.awt.Color;

/*
 * A Graphics 2D Line - defined as a start and end point
 */
public class G2DLine implements G2DObject {
	
	private G2DPoint start, end;
	private Color color = Color.BLACK;
	
	// -- Constructors
	
	public G2DLine(G2DPoint start, G2DPoint end){
		this.start = start;
		this.end = end;
	}
	
	public G2DLine(G2DPoint start, G2DPoint end, Color color){
		this(start,end); setColor(color);
	}
	
	// -- other methods
	
	public void setColor(Color color) {
		this.color = color;
	}

	public G2DLine(double x1, double y1, double x2, double y2){
		this(new G2DPoint(x1,y1), new G2DPoint(x2,y2));
	}

	public G2DLine(double x1, double y1, double x2, double y2, Color color){
		this(new G2DPoint(x1,y1), new G2DPoint(x2,y2), color);
	}

	@Override
	public void draw(G2DAbstractCanvas absCanvas) {
		absCanvas.getPhysicalGraphics().setColor(color);
		int px1 = absCanvas.physicalX(start.getX());
		int py1 = absCanvas.physicalY(start.getY());
		int px2 = absCanvas.physicalX(end.getX());
		int py2 = absCanvas.physicalY(end.getY());
		absCanvas.getPhysicalGraphics().drawLine(px1, py1, px2, py2);
	}

	@Override
	public G2DLine deepClone() {
		return new G2DLine(start.deepClone(),end.deepClone(), new Color(color.getRGB()));
	}

	G2DPoint getStart() {
		return start;
	}

	void setStart(G2DPoint start) {
		this.start = start;
	}

	G2DPoint getEnd() {
		return end;
	}

	void setEnd(G2DPoint end) {
		this.end = end;
	}

	public void transform(Matrix transformationMatrix) {
		start.transform(transformationMatrix);
		end.transform(transformationMatrix);
	}

}
