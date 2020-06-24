
public class MatrixTransform
{	
	public static Matrix rotate(double x, double y, double degrees)
	{
		double radians = Math.toRadians(degrees);
		double[][] moveToOrigin = { {1.0, 0.0, -x} , {0.0, 1.0, -y} , {0.0, 0.0, 1.0} };
		double[][] rotateDegrees = { {Math.cos(radians), -Math.sin(radians), 0.0} , { Math.sin(radians), Math.cos(radians), 0.0} , {0.0, 0.0, 1.0} };
		double[][] moveBack = { {1.0, 0.0, x} , {0.0, 1.0, y} , {0.0, 0.0, 1.0} };
		Matrix rotateMatrix = new Matrix(moveBack).multiply(new Matrix(rotateDegrees).multiply(new Matrix(moveToOrigin)));
		return rotateMatrix;
	}
	
	public static Matrix translate(double x, double y)
	{
		double[][] moveMatrix = { {1.0, 0.0, x} , {0.0, 1.0, y} , {0.0, 0.0, 1.0} };
		return new Matrix(moveMatrix);
	}	
}
