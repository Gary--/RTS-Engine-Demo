import java.awt.geom.Line2D;
import java.awt.geom.Point2D;


public class Ggeo {
	public static final double EPS=1;

	public static Point2D Point_from_ang_dist(double ang,double dist){
		return new Point2D.Double(dist*Math.cos(ang), dist*Math.sin(ang));
	}
	
	public static Point2D point_add(Point2D a, Point2D b){
		return new Point2D.Double(a.getX()+b.getX(), a.getY()+b.getY());
	}
	
	public static Point2D point_sub(Point2D a, Point2D b){
		return new Point2D.Double(a.getX()-b.getX(), a.getY()-b.getY());
	}
	
	public static double point_dot(Point2D a, Point2D b){
		return a.getX()*b.getX() + a.getY() + b.getY();
	}
	
	public static double point_vec_angle(Point2D a, Point2D b){
		//angle between two vectors represented by this point
		return point_dot(a,b) / point_length(a) / point_length(b);
	}
	
	public static Point2D point_scalar_mult(double d,Point2D a){
		return new Point2D.Double(d*a.getX(), d*a.getY());
	}
	
	public static Point2D point_unit(Point2D a){
		double d=point_length(a);
		return new Point2D.Double(a.getX()/d, d*a.getY()/d);
	}
	
	public static double point_angle(Point2D a){
		return Math.atan2(a.getY(), a.getX());
	}
	
	public static double line_angle(Line2D line){
		return point_angle(point_sub(line.getP2(),line.getP1()));
	}
	
	public static Point2D line_perp(Line2D line){
		return new Point2D.Double(
				line.getP1().getX() - line.getP2().getX(),
			  - line.getP1().getY() + line.getP2().getY()
				);
				
	}
	
	public static double point_length(Point2D a){
		return Math.hypot(a.getX(), a.getY());
	}
	
	public static boolean point_equal(Point2D a, Point2D b){
		return a.getX() == b.getX() && a.getY() == b.getY();
	}
	
	public static double point_angle_to(Point2D a, Point2D b){
		return point_angle(point_sub(b,a));
	}
	
	
	public static double to_degrees(double ang){
		return Math.toDegrees( ang - Math.PI/2)%(360);
	}
	
	//returns the point a moved dist toward point b
	public static Point2D extend_toward(Point2D a, Point2D b, double dist){
		if (a.distance(b) < dist){
			return b;
		}
		double ang=point_angle_to(a,b);
		return new Point2D.Double(
				a.getX() + dist*Math.cos(ang),
				a.getY() + dist*Math.sin(ang));
	}
	
}

