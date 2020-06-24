import java.awt.Color;
import java.awt.Font;


public class G2DString implements G2DObject{

	private String caption;
	private Font font;
	private G2DPoint point;
	private int size;
	private Color color;
	
	
	public G2DString(String caption, G2DPoint point, int size){	
		this(caption, point.getX(), point.getY(), size, Color.BLACK);	
	}
	public G2DString(String caption, G2DPoint point, int size, Color color){
		this(caption, point.getX(), point.getY(), size, color);
	}
	public G2DString(String caption, double x, double y, int size){
		this(caption, x, y, size, Color.BLACK);
	}
	public G2DString(String caption, double x, double y, int size, Color color){
		this.caption = caption;
		point = new G2DPoint(x, y);
		this.size = size;
		this.color = color;
	}

	public void draw(G2DAbstractCanvas absCanvas) {
		font = new Font("Times New Roman", Font.BOLD, (int) absCanvas.getScaleY(size));
		absCanvas.getPhysicalGraphics().setFont(font);
		absCanvas.getPhysicalGraphics().setColor(color);
		absCanvas.getPhysicalGraphics().drawString(caption, absCanvas.physicalX(point.getX()), absCanvas.physicalY(point.getY()));
	}

	public void setColor(Color color)
	{
	}
	
	public void setText(String s)
	{
		this.caption = s;
	}
	
	
	public void transform(Matrix transformationMatrix) {
	}

	/* Deep clone not finished */
	public G2DString deepClone() {
		return new G2DString(caption, point.deepClone(), size);
	}

	public G2DPoint getPoint(){
		return point;
	}
	
}
