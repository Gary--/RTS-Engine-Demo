import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;


public class Gpath {
	final static double inf = Double.POSITIVE_INFINITY;
	
	
	private boolean refresh_needed = false;  //refresh_terrain() needed
	
	
	public ArrayList<Point2D> points;       //list of pathing nodes
	public ArrayList<Line2D> walls;         //list of walls (boundaries)
	
	public ArrayList<PUnit> units;
	
	//adjacency matrix for points i and j
	private double dist[][];                 
	
	//indice of point where we should head if we are at i and want to go to j
	private int next[][];                 
	
	
	public Gpath(){
		points = new ArrayList<Point2D>();
		walls = new ArrayList<Line2D>(); 
		units = new ArrayList<PUnit>(); 
	}
	
	//===================================================== INTIAlIZATIONS
	public void add_unit(PUnit u){
		u.sim = this;
		
		//=== make it so lazy people can spawn units at the exact same place
		boolean shared_point=true;
		while (shared_point){
			shared_point = false;
			for (int i=0;i<units.size();i++){
				if (Ggeo.point_equal(u.pos, units.get(i).pos)){
					shared_point = true;
					u.pos.setLocation(u.pos.getX()+1, u.pos.getY());
					break;
				}
			}
		}
		units.add(u);
	}
	
	public void add_wall(Line2D wall){
		refresh_needed = true;
		
		double to_extend = 5;
		
		//we extend the end point of the line to handle edge cases better
		wall.setLine(Ggeo.extend_toward(wall.getP1(), wall.getP2(), -to_extend),
				    Ggeo.extend_toward(wall.getP2(), wall.getP1(), -to_extend));
		
		double wall_angle = Ggeo.line_angle(wall);
		
		//angle the wall makes with the line drawn from an end point to the
		//actual pathing node point.
		double angle_off = Math.PI/4;
		double dist_off = 20;
		Point2D dir1=Ggeo.Point_from_ang_dist(wall_angle+angle_off, dist_off);
		Point2D dir2=Ggeo.Point_from_ang_dist(wall_angle-angle_off, dist_off);
		
		//we add the 4 node points associated with the wall
		points.add(Ggeo.point_sub(wall.getP1(),dir1));
		points.add(Ggeo.point_sub(wall.getP1(),dir2));
		points.add(Ggeo.point_add(wall.getP2(),dir1));
		points.add(Ggeo.point_add(wall.getP2(),dir2));
		
		walls.add(wall);
	}
	
	private void refresh_terrain(){
		System.out.println("=== Refreshing terrain...");
		
		int N = points.size();
		refresh_needed = false;
		
		dist = new double[N][N];
		
		//if we want to get from i to j, we want to get to inter[i][k] first
		//this is recursive: to get from i to k, we may have to visit m
		int inter[][] = new int[N][N];
		
		
		//initialize distances for points that directly connect
		for (int i=0;i<N;++i){
			dist[i][i] = 0.;//it connects to itself with no cost
			inter[i][i] = -1;
			
			Point2D p = points.get(i);
			
			for (int j=i+1;j<N;++j){
				inter[i][j] = inter[j][i] = -1;
				
				Point2D q = points.get(j);
				
				//we can go from p to q in a straight line
				if (direct_path_works(p,q)){
					dist[i][j] = dist[j][i] = p.distance(q);
				} else {
					dist[i][j] = dist[j][i] = inf;
				}
			}
		}
		
		//We run the Floyd-Warshall all pairs of shortest paths algorithm with
		//path reconstruction
		for (int i=0; i<N ;++i){
			for (int j=0 ;j<N;++j){
				for (int k=0;k<N;++k){
                    double alt = dist[i][k] + dist[k][j];
                    if (alt < dist[i][j]){
                        dist[i][j] = alt;
                        inter[i][j] = k;
                    }
				}
			}
		}
		
		//System.out.println("HERE!");
		
		
		//We convert the intermediate array to next, which tells us immediately
		//where to head if we want to go from i to j
		next = new int[N][N];
		for (int i=0; i<N ;++i){
			for (int j=0 ;j<N;++j){
				int targ = j;//where we should head to get to j
				
				//we trance to any intermediate nodes we must visit
				while (inter[i][targ] != -1){
					//System.out.println("HERE! " + targ);
					targ = inter[i][targ];
				}
				
				next[i][j] = targ;
			}
		}
	}
	
	//================================================== convenience stuff

	
	//================================================== ACTUAL COMPUTATIONS
	public void set_unit_dest(PUnit u, Point2D dest){

		//we can walk straight there
		if (direct_path_works(u.pos, dest)){
			u.dest = dest;
			u.path = new ArrayList<Point2D>();
			u.is_moving = true;
			u.path.add(dest);
			return;
		}
		
		//indices of points reachable directly from the current and destination
		//respectively
		ArrayList<Integer> firsts = new ArrayList<Integer>();
		ArrayList<Integer> lasts = new ArrayList<Integer>();
		
		for (int i=0;i<points.size();++i){
			Point2D p = points.get(i);
			
			if (direct_path_works(p, u.pos)){
				firsts.add(i);
			}
			if (direct_path_works(p, dest)){
				lasts.add(i);
			}
		}
		
		/*we find the first node to visit from the current position and the
		last node to visit before the destination that would result in the
		shortest path*/
		double best_dist = inf;
		int best_first=-1, best_last=-1;
		
		for (int i=0;i<firsts.size();++i){
			int a=firsts.get(i);
			Point2D p=points.get(a);
			if (! direct_path_works(u.pos, p)){
				continue;
			}
			double d1 = u.pos.distance(p);
			
			for (int j=0;j<lasts.size();++j){
				int b=lasts.get(j);
				Point2D q=points.get(b);
				if (! direct_path_works(dest, q)){
					continue;
				}
				
				double d2 = dest.distance(q);
				double tot_dist = d1 + dist[a][b] + d2;
				
				if (tot_dist < best_dist){
					best_dist = tot_dist;
					best_first = a;
					best_last = b;
				}
			}
		}
		
		if (best_dist == inf){
			return;
		}
		
		u.dest = dest;
		u.path = new ArrayList<Point2D>();
		u.is_moving = true;
		
		//System.out.println("Best dist: " + best_dist);
		u.path.add(points.get(best_first));
		
		
		while (next[best_first][best_last]!=best_last){
			best_first = next[best_first][best_last];
			u.path.add(points.get(best_first));
		}
		
        u.path.add(points.get(best_last));
        u.path.add(u.dest);
		
	}
	
	//perform set pathing tick
	private void unit_tick(PUnit u){
		if (!u.is_moving){
			return;
		}
		
		//Point2D old_pos = u.pos;
		
		//we can see the point after this one, we skip the next point
		if (u.path.size()>1 && direct_path_works(u.pos, u.path.get(1))){
			u.path.remove(0);
		}
		
		double dist_left=u.move_speed;//how much distance this unit can go
		while (dist_left!=0 && u.path!=null && u.path.size()!=0){
			double dist_to_next = u.pos.distance(u.path.get(0));
			
			//we can reach past the next node
			if (dist_left > dist_to_next){
				dist_left -= dist_to_next;
				u.new_pos = u.path.get(0);
				u.path.remove(0);
			} else {
				u.new_pos = Ggeo.extend_toward(u.pos, u.path.get(0), dist_left);
				dist_left = 0;
			}
		}
		
		//the unit passed through a wall during this move, we must reset
		//its path
		if (!direct_path_works(u.new_pos, u.pos)){
			//System.out.println("WOulda walked through wall!");
			u.new_pos = u.pos;
			u.set_dest(u.dest);
			unit_tick(u);
			return;
		}
		
		//change angle
		if (!Ggeo.point_equal(u.pos, u.new_pos)){
			u.angle = Ggeo.point_angle_to(u.pos, u.new_pos);
		}
		
		// finalize
		u.pos = u.new_pos;
		
		//no more points to visit, this thing is done moving
		if (u.path.size()==0 || 
				//allowed error radius.
				(u.pos.distance(u.dest) < u.dest_radius &&
				 direct_path_works(u.pos, u.dest))
				){
			
			u.path = null;
			u.is_moving = false;
			u.dest = u.pos;
		}
	}
	
	public void initial_unit_tick(){
		//first, move all units along their merry way
		for (int i=0;i<units.size();++i){
			PUnit u=units.get(i);
			
			u.moved_this_tick = false;
			u.old_pos = u.pos;
			
			unit_tick(u);
		}
	}
	
	public void unit_shoves(){
		//do some initializations
		for (int i=0;i<units.size();++i){
			PUnit u=units.get(i);
			u.new_pos = u.pos;
			u.shoved_through_wall = false;
		}
		
		//=== do unit unit collisions
		for (int cycles=0;cycles<3;++cycles){
			for (int i=0;i<units.size();++i){
				PUnit a=units.get(i);//first unit
				
				for (int j=i+1;j<units.size();++j){
					PUnit b=units.get(j);//second unit
					
					Point2D diff = Ggeo.point_sub(b.new_pos, a.new_pos);
					double dist = Ggeo.point_length(diff);
					double rad_sum = a.radius + b.radius;
					
					//they are not overlapping
					if (dist > rad_sum){
						continue;
					}
					
					double to_shove = (rad_sum - dist)*1.2;
					double a_shove = 0.5, b_shove = 0.5;
					
					/*
					if (a.shoved_through_wall){
						a_shove -= 0.5;
						b_shove += 0.5;
					}
					
					if (b.shoved_through_wall){
						b_shove -= 0.5;
						a_shove += 0.5;
					}*/
					
					
					if (!a.shoved_through_wall){
						a.new_pos = Ggeo.extend_toward(
								a.new_pos, b.new_pos, -a_shove*to_shove);
					}
					
					if (!b.shoved_through_wall){
						b.new_pos = Ggeo.extend_toward(
								b.new_pos, a.new_pos, -b_shove*to_shove);
					}
				}
			}
			
			//make sure nothing gets shove through wall
			for (int i=0;i<units.size();++i){
				PUnit u=units.get(i);
				
				//the wall that this unit moved through
				Line2D wall = direct_path_works2(u.pos, u.new_pos);
				if (wall != null){
					
					 
					//original
					u.shoved_through_wall = true;
					u.new_pos = u.pos;
					
					
					/*
					double k=1.5;
					//how far the unit ended up on the other side of the wall
					double df = wall.ptLineDist(u.new_pos);
					Point2D perp = 
							Ggeo.point_scalar_mult(df*k,
									Ggeo.point_unit(Ggeo.line_perp(wall))
							)
							;
					System.out.println("df: " + df);
					Point2D new_new_pos = Ggeo.point_add(u.new_pos,perp);
					if (wall.ptLineDist(new_new_pos)<df*k){
						u.new_pos = new_new_pos;
					} else {
						u.new_pos = Ggeo.point_sub(u.new_pos,perp);
						
					}
					
					*/
					//u.new_pos = Ggeo.extend_toward(u.pos, u.new_pos, new_dist);
					

					
					
				}
			}
		}
		
		for (int i=0;i<units.size();++i){
			PUnit u=units.get(i);
			if (!direct_path_works(u.pos, u.new_pos)){
				u.new_pos = u.pos;
			}
		}
		
	}
	
	public void finalize_unit_tick(){
		//finalize the unit positions.
		//set turn angle etc
		for (int i=0;i<units.size();++i){
			PUnit u=units.get(i);
			
			
			u.pos = u.new_pos;
			
			//if the unit moved a noticeable amount, we set its angle
			if (u.old_pos.distance(u.new_pos)>0.1){
				u.angle = Ggeo.point_angle_to(u.old_pos, u.pos);
				u.angle_deg = Math.toDegrees(u.angle)%360.;
				u.moved_this_tick = true;
			}
		}
	}
	
	public void tick(){
		if (refresh_needed){
			refresh_terrain();
		}
		
		initial_unit_tick();
		
		unit_shoves();
		
		finalize_unit_tick();
		
	}
	
	
	//null if the path from (x1,y1) to (x2,y2) is unobstructed, else returns
	//the pointer to the wall blocking them.
	private Line2D direct_path_works2(double x1,double y1,double x2,double y2){
		for (int i=0;i<walls.size();++i){
			if (walls.get(i).intersectsLine(x1,y1,x2,y2)){
				//System.out.println("== " + x1 + " " + y2 + " " + x2 + " " + y2);
				return walls.get(i);
			}
		}
		return null;
	}
	
	private Line2D direct_path_works2(Point2D a, Point2D b){
		return direct_path_works2(a.getX(), a.getY(), b.getX(), b.getY());
	}
	
	private boolean direct_path_works(Point2D a, Point2D b){
		return direct_path_works2(a.getX(), a.getY(), b.getX(), b.getY())==null;
	}
	
	//null if the path from a to b is unobstructed, else returns the wall
	//that blocks them

	//================= very primitive display methods for testing purposes
	public void draw_circle(Graphics g,double x, double y, double radius){
		g.fillOval((float)(x-radius), (float)(y-radius), 
				   (float)(2*radius), (float)(2*radius));
	}
	
	public void draw_circle_out(Graphics g,double x, double y, double radius){
		g.drawOval((float)(x-radius), (float)(y-radius), 
				   (float)(2*radius), (float)(2*radius));
	}
	
	public void draw_line(Graphics g,Point2D a,Point2D b){
		g.drawLine((float)a.getX(), (float)a.getY(),
				   (float)b.getX(), (float)b.getY());
		
	}
	
	
	public void display(Graphics g){
		//=== draw the walls
		g.setColor(Color.white);
		for (int i=0;i<walls.size();++i){
			Line2D line = walls.get(i);
			draw_line(g,line.getP1(),line.getP2());
		}
		
		//=== UNITS
		
		for (int i=0;i<units.size();++i){
			g.setColor(Color.red);
			PUnit u = units.get(i);
			draw_circle(g, u.pos.getX(),u.pos.getY(),u.radius);
			
			

			
			//draw direction line
			g.setColor(Color.green);
			Point2D p = Ggeo.Point_from_ang_dist(u.angle, u.radius);
			draw_line(g,u.pos, Ggeo.point_add(u.pos, p));
			
			//draw blue circle for is moving
			if (u.moved_this_tick){
				g.setColor(Color.blue);
				draw_circle(g, u.pos.getX(),u.pos.getY(),u.radius/2);
			}
			
			
			//draw pathing lines
			if (u.path!=null){
				g.setColor(Color.green);
				//assert direct_path_works(u.pos,u.path.get(0));
				//System.out.println(u.path.get(0));
				draw_line(g,u.pos, u.path.get(0));
				for (int j=1;j<u.path.size();++j){
					draw_line(g,u.path.get(j),u.path.get(j-1));
				}
			}
		}
		
		g.setColor(Color.green);
		//=== Connections between points
		for (int i=0;i<points.size();++i){
			Point2D p = points.get(i);
			draw_circle(g, p.getX(),p.getY(),3);
			for (int j=0;j<points.size();++j){
				if (j==i){
					continue;
				}
				
				/*
				Point2D q = points.get(j);
				if (direct_path_works(p,q)){
					g.setColor(Color.green);
					draw_line(g,p,q);
				}*/
			}
		}
		
	}
}

