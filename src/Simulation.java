//Commands are givin through this
//Mush gonna be interfacing directly with this

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.newdawn.slick.Graphics;


public class Simulation {
	Gpath simp;//pathing simulator
	ArrayList<SUnit> units;
	
	public Simulation(){
		simp = new Gpath();
		units = new ArrayList<SUnit>();
	}
	
	
	public void add_wall(Line2D wall){
		simp.add_wall(wall);
	}
	
	public void add_unit(SUnit sunit){
		//sunit.punit = new PUnit();
		simp.add_unit(sunit.punit);
		units.add(sunit);
	}
	
	//======== Commanding units
	public void group_move_order(ArrayList<SUnit> units,Point2D dest){
		double tot_area = 0, dest_radius;
		for (int i=0;i<units.size();++i){
			PUnit u = units.get(i).punit;
			tot_area += u.radius*u.radius  *Math.PI;
		}
		dest_radius = Math.sqrt(tot_area/Math.PI);
		for (int i=0;i<units.size();++i){
			PUnit u = units.get(i).punit;
			u.set_dest(dest);
			u.dest_radius = dest_radius;
		}
	}
	
	
	
	public void display(Graphics g){
		simp.display(g);
	}
	
	public void tick(){
		simp.tick();
	}
}
