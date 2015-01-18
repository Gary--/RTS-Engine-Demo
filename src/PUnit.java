import java.awt.geom.Point2D;
import java.util.ArrayList;



public class PUnit {
	double 
		radius,//    collision radius
		move_speed,
		dest_radius=0,
		angle=0,
		angle_deg
		;
	
	boolean shoved_through_wall = false,
			moved_this_tick=false;
	
	Point2D pos,dest,new_pos,old_pos;
	ArrayList<Point2D> path=null;
	
	
	boolean is_moving=false;
	
	Gpath sim;//      the simulator object associated containing this
	
	public PUnit(){}
	
	public PUnit(Point2D pos,double radius,double move_speed){
		this.pos = pos;
		this.radius = radius;
		this.move_speed = move_speed;
		//this.sim = sim;
	}
	
	public void set_dest(Point2D dest){
		//is_moving = true;
		sim.set_unit_dest(this, dest);
	}
	
	public void set_dest(double x,double y){
		set_dest(new Point2D.Double(x, y));
		
	}
	
}
