
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
 
public class GaryGeoPlayground extends BasicGame{
 
	// CONSTANTS
	private static int windowWidth = 1100;
	private static int windowHeight = 700;
	private static int frameRate = 60;
	private static boolean fullScreen = false;
	

	
	int dragStartX=0,dragStartY=0,dragEndX,dragEndY;
	boolean dragActive=false;
	Rectangle dragBox = new Rectangle(0,0,1,1);
	ArrayList<SUnit> selection;
	
	// Declare variables here, but you don't have to give them values yet
	// once you declare them here, initialize them in the init method below!
	private Simulation sim;

	
    public GaryGeoPlayground(){
        super("Gary's Geography Playground!!!"); //window caption
	}
	 
    @Override
	public void init(GameContainer gc) 
			throws SlickException {
		sim = new Simulation();
		selection = new ArrayList<SUnit>();
		
		sim.add_unit(new SUnit(new Point2D.Double(250,250),20,2));
		sim.add_wall(new Line2D.Double(new Point2D.Double(100,600),
	            new Point2D.Double(500,300)));
		
		
		sim.add_unit(new SUnit(new Point2D.Double(300,250),30,4));
		sim.add_unit(new SUnit(new Point2D.Double(250,250),20,1));
		sim.add_unit(new SUnit(new Point2D.Double(300,250),15,2));
		sim.add_unit(new SUnit(new Point2D.Double(250,250),20,2));
		sim.add_unit(new SUnit(new Point2D.Double(300,250),15,1));
		
		sim.add_unit(new SUnit(new Point2D.Double(250,250),20,2));
		sim.add_unit(new SUnit(new Point2D.Double(300,250),30,4));
		sim.add_unit(new SUnit(new Point2D.Double(250,250),20,1));
		sim.add_unit(new SUnit(new Point2D.Double(300,250),15,2));
		sim.add_unit(new SUnit(new Point2D.Double(250,250),20,2));
		sim.add_unit(new SUnit(new Point2D.Double(300,250),15,1));
		
		//sim.add_wall(new Line2D.Double(new Point2D.Double(0,0),
	    //        new Point2D.Double(500,500)));
		
		sim.add_wall(new Line2D.Double(new Point2D.Double(100,500),
				                       new Point2D.Double(300,200)));
		
		sim.add_wall(new Line2D.Double(new Point2D.Double(400,400),
	            new Point2D.Double(200,300)));
		
		sim.add_wall(new Line2D.Double(new Point2D.Double(200,200),
	            new Point2D.Double(500,200)));
		
		sim.add_wall(new Line2D.Double(new Point2D.Double(500,200),
	            new Point2D.Double(600,400)));
		
		sim.add_wall(new Line2D.Double(new Point2D.Double(600,400),
	            new Point2D.Double(650,50)));
		
		//sim.add_wall(new Line2D.Double(new Point2D.Double(600,400),
	    //        new Point2D.Double(130,143)));
		
		sim.add_wall(new Line2D.Double(new Point2D.Double(0,0),
	            new Point2D.Double(500,100)));
		
		sim.add_wall(new Line2D.Double(new Point2D.Double(100,600),
	            new Point2D.Double(500,500)));
		
		sim.add_wall(new Line2D.Double(new Point2D.Double(100,600),
	            new Point2D.Double(500,300)));
		
		
		
		/*
	
	    ((100,600),(500.,300.)),
		*/
		
		// the init method will only run once.
		// use this place to initialize values!
		// dont render graphics here! it won't work!
	}

	@Override
    public void update(GameContainer gc, int delta) 
			throws SlickException{
    	//System.out.println("1");
    	// this is the game loop, this handles calculations and value updates
    	// don't render graphics here! it won't work!
 
    	//that int delta thing in the parameters is supposedly the time passed
    	//since last loop in milliseconds, used to ensure constant framerate
    	//on all hardware.
    	//I KNOW WE NEED TO USE IT FOR OUR GAME... just not fully sure how to
    	//yet.  but you multiply by it or something. i dunno.
    	
    	

    	
    	//following grabs input with the name 'i'
    	Input input = gc.getInput();

    	
    	
    	int mx = input.getMouseX(), my = input.getMouseY();
    	int mousePosX = mx;
    	int mousePosY = my;
    	
    	
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
   		 dragStartX = mousePosX;
   		 dragStartY = mousePosY;
   		 dragActive = true;
   		 dragBox.setX(dragStartX);
   		 dragBox.setY(dragStartY);
   		}

   		if (dragActive && input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
   		 dragBox.setWidth(mousePosX-dragStartX);
   		 dragBox.setHeight(mousePosY-dragStartY);
   		}
   		  
   		if (dragActive && !input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
   		 dragActive = false;
   		 dragEndX = (int) (dragBox.getX()+dragBox.getWidth());
   		 dragEndY = (int) (dragBox.getY()+dragBox.getHeight());

   		 
   		 selection = new ArrayList<SUnit>();
   		 for (int i=0;i<sim.units.size();++i){
   			 SUnit u=sim.units.get(i);
   			 if (dragBox.contains((float)u.getX(), (float)u.getY())){
   				 selection.add(u);
   				 //System.out.println("!!");
   			 }
   		 }
   		}
    	

   		
    	// using this, we can use stuff like:
    	if (input.isKeyDown(Input.KEY_5)){
    		System.out.println("You pressed 5!");
    	}
    	
    	//similarly 
    	
    	if (input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)){
    		sim.group_move_order(selection, new Point2D.Double(mx, my));
    		//for (int i=0;i<selection.size();++i){
    		//	selection.get(i).set_dest(mx,my);
    		//}
    		//gary.set_dest(mx, my);
    		//assert false;
    	}
    	
    	sim.tick();
    	// you use getMouseX() and getMouseY() to do exactly what you think
    	
    	
    }
 
    public void render(GameContainer gc, Graphics g) 
			throws SlickException{
    	if (g.getClip()==null)
    		g.setClip(0,0,windowWidth,windowHeight);
    	
    	if (dragActive){
    		g.draw(dragBox);
    	}
    	

    	//System.out.println("2");
    	// the above clips rendering to only the window extent
    	
    	sim.display(g);
    	
    	
   		//for (int i=0;i<selection.size();++i){
   			//SUnit u = selection.get(i);
   			//sim.draw_circle_out(g, u.pos.getX() , u.pos.getY() , u.radius);
   		//}
    	
    	
    }
 
    public static void main(String[] args) 
			throws SlickException
    {
         AppGameContainer app = 
			new AppGameContainer(new GaryGeoPlayground());
 
         app.setDisplayMode(windowWidth, windowHeight, fullScreen);
         app.setTargetFrameRate(frameRate);
         app.start();
    }
}
