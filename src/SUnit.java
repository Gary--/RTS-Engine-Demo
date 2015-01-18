import java.awt.geom.Point2D;


//access by mush
public class SUnit {
	Ggeo simp;
	PUnit punit;
	
	//things that don't change much
	int allegience,        //0 is neutral, teams (assumed FFA) otherwiseking
		attack_range      //you are dumb if you dunno what this is. Quit deving.
	;
	
	//things constantly changing
	int cool_down=0,
		move_down=0
			;
	
	//=== CONSTRUCTORS
	public SUnit(){
		this.punit = new PUnit();
	}
	
	public SUnit(Point2D pos,double radius,double move_speed){
		this.punit = new PUnit();
		
		setPos(pos);
		setRadius(radius);
		setMoveSpeed(move_speed);
	}
	

	//======================= ALL THEM SETS ============
	public void setRadius(double radius){
		punit.radius = radius;
	}
	
	public void setMoveSpeed(double ms){
		punit.move_speed = ms;
	}
	
	public void setPos(Point2D pos){
		punit.pos = pos;
	}
	
	public void setPos(double x, double y){
		setPos(new Point2D.Double(x,y));
	}
	
	//======================= ALL THEM GETS ===========
	public double getAngle(){
		return punit.angle_deg;
	}
	
	public boolean isMoving(){
		return punit.is_moving;
	}
	
	//----
	
	public Point2D getPos(){
		return new Point2D.Float(getX(), getY());
	}
	
	public float getRadius(){
		return (float)punit.radius;
	}
	
	public float getX(){
		return (float)punit.pos.getX();
	}
	
	public float getY(){
		return (float)punit.pos.getY();
	}
}
