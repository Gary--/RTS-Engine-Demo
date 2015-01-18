import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.BigImage;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.UnicodeFont;

import java.awt.Font;

import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;


public class BaseTest extends BasicGame{

	//CONSTANTS
	static int windowWidth = 1166; //resolution settings
	static int windowHeight = 568;
	
	static int frameRate = 40; // maximum framerate
	
	static boolean fpsShow = false; //show FPS tracker or not
	static boolean vSync = true; //attempt to match monitor refresh rate
	static boolean fullScreen = false; //enable/disable fullscreen
	static boolean mouseGrab = false; //trap mouse in window and make invisible
	
	int scrollSpeed = 900; //speed map scrolls with arrows or mouse movement
	                       //PIXELS PER SECOND
	int borderWidth = 15; //distance from window edge that mouse map scroll
						  //will register
	
	float initialX = 10000; //starting position for camera
	float initialY = 5320;
	
	// path to map graphic
	boolean largeMap = false; //set true if map dimension exceeds 4096
	String mapPath = "Graphics\\Maps\\FillerBG.png";
	// path to Mini Map UI graphic
	String miniMapUIPath = "Graphics\\UI\\MiniMapBase.png";
	// path to unit information UI window graphic
	String unitInfoUIPath = "Graphics\\UI\\UnitInfoBase.png";
	//path to ability using box UI graphic
	String abilityGridUIPath = "Graphics\\UI\\AbilityGridBase.png";
	
	//-------------------------------------------------------------------------
	
	// CONSTANTS FOR SCROLL SPEED
	private static final int LEFT = 0,
							 RIGHT = 1,
							 DOWN = 2,
							 UP = 3;
	
	
	//VARIABLE DECLARATIONS
	Music testa;
	Unit hell, hell1, hell2;
	Simulation sim = new Simulation();
	ArrayList<Unit> selection = new ArrayList<Unit>();
	ArrayList<SUnit> selectionPath = new ArrayList<SUnit>();
	ArrayList<Unit> units = new ArrayList<Unit>();
	ArrayList<Unit> drawnUnits = new ArrayList<Unit>();
	ArrayList<Unit> prioritySelection = new ArrayList<Unit>();
	ArrayList<Line2D> walls = new ArrayList<Line2D>();
	Button [] buttons = new Button[16];
	Rectangle [] selectionIcons = new Rectangle[16];
	Unit [] unitIcons = new Unit[16];
	Image heroBack;
	
	UnicodeFont fontBig, fontMed, fontSm;
	//maps -- variables to obtain map image and its dimensions.
	Image map = null;
	int mapWidth, mapHeight;
	
	//variables for minimap
	Image miniMap = null;
	int miniMapWidth, miniMapHeight;
	//11x16 is where the black square of 204x204 starts in minimap UI
	boolean miniMapDisplay;
	int squareLength;
	int distFromLeft;
	int distFromTop;
	
	float speedModifier; //used to obtain delta value to multiply
	
	//viewport variables
	Rectangle viewPortBox = null;
	float viewPortWidth, viewPortHeight;
	float viewPortScrollSpeedX, viewPortScrollSpeedY;
	
	//UI -- images pertaining to user interface
	Image miniMapUI = null;
	int miniMapUIX, miniMapUIY;
	float miniXDiff, miniYDiff;
	Image unitInfoUI = null;
	
	int unitInfoUIX, unitInfoUIY;
	Image abilityGridUI = null;
	int abilityGridUIX, abilityGridUIY;
	boolean mapClick;
	boolean moveCommanding=false;
	
	//positions -- the X Y positions of important objects
	float cameraX, cameraY;
	int miniMapX, miniMapY;
	float viewPortX, viewPortY; //ratio to map may be float
	int mousePosX, mousePosY;
	int commandX, commandY;
	int clickCount = 1;
	int frameCount = 1;
	
	//map scroll boundaries used to find right and bottom sides
	int rightSide;
	int bottomSide;
	
	//calculated map extent
	int rightEdge;
	int bottomEdge;
	int miniRightEdge;
	int miniBottomEdge;
	
	//disallows double camera speed if both key and mouse used
	boolean keyMove;
	
	//selection box
	Rectangle dragBox = new Rectangle(0,0,1,1);
	boolean dragActive = false;
	int dragStartX, dragStartY;
	int dragEndX, dragEndY;
	
	//priority unit
	Unit priority;
	
	//unit info
	boolean unitInfoClicking = false;
	int curTab = 0;
	
	// random anims
	SpriteSheet moveCommandSprite;
	Animation moveCommandAnim;
	
	//
	
	private void unitSpawnHack(Input input) throws SlickException{
		if (input.isKeyPressed(Input.KEY_Q)){
			Unit add = UnitData.Test(mousePosX-(int)cameraX, 
					                 mousePosY-(int)cameraY);
			units.add(add);
			sim.add_unit(add.getPath());
		}
		
		if (input.isKeyPressed(Input.KEY_W)){
			Unit add = UnitData.Etrom(mousePosX-(int)cameraX, 
								      mousePosY-(int)cameraY);
			units.add(add);
			sim.add_unit(add.getPath());
		}
	}
	


	private void drawWallHack(Graphics g){
		for (int i = 0; i<walls.size(); i++){
			g.setLineWidth(1);
			g.setColor(Color.red);
			g.drawLine((float)walls.get(i).getX1()+cameraX, 
					   (float)walls.get(i).getY1()+cameraY,
					   (float)walls.get(i).getX2()+cameraX, 
					   (float)walls.get(i).getY2()+cameraY);
			g.setLineWidth(1);
		}
	}


	private void initMap() throws SlickException {
		// if map bigger than hardware limit (4096 width or height for me)
		// make it a new BigImage instead.  It will be slower, however.
		if (largeMap)
			map = new BigImage(mapPath);
		else
			map = new Image(mapPath);
		mapWidth = map.getWidth();
		mapHeight = map.getHeight();
	}


	private void initUI() throws SlickException {
		miniMapUI = new Image(miniMapUIPath);
		miniMapUIX = 0;
		miniMapUIY = windowHeight - miniMapUI.getHeight(); 
		
		distFromLeft = 12; //width of borders on minimap UI
		distFromTop = 17;
		squareLength = 204; //refers to size of black box in minimap UI
		
		//following gets proper minimap image and xy for minimap to fit in UI
		if (mapWidth>mapHeight){ //limit scale by longest side
			miniMap = map.getScaledCopy((float)squareLength/mapWidth);
			miniMapX = miniMapUIX+distFromLeft;
			miniMapY = miniMapUIY
					   +distFromTop
					   +squareLength/2
					   -miniMap.getHeight()/2;
		}else{
			miniMap = map.getScaledCopy((float)squareLength/mapHeight);
			miniMapX = miniMapUIX
					   +distFromLeft
					   +squareLength/2
					   -miniMap.getWidth()/2;
			miniMapY = miniMapUIY+distFromTop;
		}
		miniMapWidth = miniMap.getWidth();
		miniMapHeight = miniMap.getHeight();
		
		//viewPort related (viewing box on minimap)
		viewPortWidth = (float) (miniMapWidth*(windowWidth*1.0/mapWidth));
		viewPortHeight = (float) (miniMapHeight*(windowHeight*1.0/mapHeight));
		viewPortX = miniMapX + -cameraX*(windowWidth/mapWidth);
		viewPortY = miniMapY + -cameraY*(windowHeight/mapHeight);
		//defines initial position of viewport
		viewPortBox = new Rectangle(viewPortX, 
									viewPortY, 
									viewPortWidth, 
									viewPortHeight);
		//scrollSpeed for viewport
		viewPortScrollSpeedX = (float)
								(scrollSpeed*1.0/mapWidth*miniMapWidth);
		viewPortScrollSpeedY = (float)
								(scrollSpeed*1.0/mapHeight*miniMapHeight);
		
		
		abilityGridUI = new Image(abilityGridUIPath);
		abilityGridUIX = windowWidth - abilityGridUI.getWidth();
		abilityGridUIY = windowHeight - abilityGridUI.getHeight();
		
		unitInfoUI = new Image(unitInfoUIPath);
		unitInfoUIX = windowWidth/2 - unitInfoUI.getWidth()/2;
		unitInfoUIY = windowHeight - unitInfoUI.getHeight();	
		
		//calculated right and bottom sides of window used for mouse map scroll
		rightSide = windowWidth - borderWidth; 
		bottomSide = windowHeight - borderWidth;
		
		//calculated right and bottom extents of current map
		rightEdge = -(mapWidth-windowWidth);
		bottomEdge = -(mapHeight-windowHeight);
		// minimap counterparts
		miniRightEdge = Math.round(miniMapX + miniMapWidth - viewPortWidth);
		miniBottomEdge = Math.round(miniMapY + miniMapHeight - viewPortHeight);
		moveCommandSprite = new SpriteSheet("Graphics\\Misc\\Cursor\\moveCommand.png", 68, 48);
		moveCommandAnim = new Animation(moveCommandSprite, 20);
		moveCommandAnim.setLooping(false);
	}
	
	private void resetAbilityGrid() throws SlickException{
		int firstx = abilityGridUIX + 10;
		int firsty = abilityGridUIY + 19;
		int shift = 53;
		int curbut = 0;
		for (int i = 0; i<buttons.length/4; i++){
			for(int j = 0; j<buttons.length/4; j++){
				if (buttons[curbut]!=null){
					buttons[curbut].setPosn(firstx+shift*j,firsty+shift*i);
				}
				curbut++;
			}
		}
		
	}


	private void initMapBoundaries() {
		walls.add(new Line2D.Double(0, 0, 0, mapHeight));
		walls.add(new Line2D.Double(0, 0, mapWidth, 0));
		walls.add(new Line2D.Double(mapWidth, 0, mapWidth, 
										mapHeight));
		walls.add(new Line2D.Double(0, mapHeight, mapWidth, 
										mapHeight));	
	}


	private void initFonts() throws SlickException {
		fontBig = new UnicodeFont(new Font("Cambria", Font.BOLD, 20));
		fontBig.getEffects().add(new ColorEffect(java.awt.Color.white));
		fontBig.addAsciiGlyphs();
		fontBig.loadGlyphs();
		fontMed = new UnicodeFont(new Font("Cambria", Font.BOLD, 17));
		fontMed.getEffects().add(new ColorEffect(java.awt.Color.white));
		fontMed.addAsciiGlyphs();
		fontMed.loadGlyphs();
		fontSm = new UnicodeFont(new Font("Cambria", Font.BOLD, 14));
		fontSm.getEffects().add(new ColorEffect(java.awt.Color.white));
		fontSm.addAsciiGlyphs();
		fontSm.loadGlyphs();
		
	}



	private void initWalls() {
		walls.add(new Line2D.Double(new Point2D.Double(100,500),
                new Point2D.Double(300,200)));
		walls.add(new Line2D.Double(new Point2D.Double(400,400),
		new Point2D.Double(200,300)));
		walls.add(new Line2D.Double(new Point2D.Double(200,200),
		new Point2D.Double(500,200)));
		walls.add(new Line2D.Double(new Point2D.Double(500,200),
		new Point2D.Double(600,400)));
		walls.add(new Line2D.Double(new Point2D.Double(600,400),
		new Point2D.Double(650,50)));
		walls.add(new Line2D.Double(new Point2D.Double(0,0),
		new Point2D.Double(500,100)));
		walls.add(new Line2D.Double(new Point2D.Double(100,600),
		new Point2D.Double(500,500)));
		walls.add(new Line2D.Double(new Point2D.Double(100,600),
		new Point2D.Double(500,300)));
	}
	
	private void initUnitInfoIcons() throws SlickException{
		int initX = 153+unitInfoUIX; //starting point from unitInfoUIY
		int initY = 22+unitInfoUIY;  //starting point from unitInfoUIY
		int hGap = 53; //horizontal icon gap
		int vGap = 69; //vertical icon gap
		int curIcon = 0; //current icon being added
		heroBack = new Image("Graphics\\Icons\\herobackicon.png");
		for (int i = 0; i<2; i++){
			for (int j = 0; j<8; j++){
				selectionIcons[curIcon] = new Rectangle(initX+hGap*j, 
														initY+vGap*i,
														48, 48);
				curIcon++;
			}
		}
	}
	

	private boolean inMinimap(int x, int y){
		return ((x>miniMapX) 
				&& (x<miniMapX+miniMapWidth)
				&& (y>miniMapY) 
				&& (y<miniMapY+miniMapHeight));
	}
	
	private boolean inMinimapUI(int x, int y){
		return ((x>miniMapUIX) 
				&& (x<miniMapUIX+miniMapUI.getWidth())
				&& (y>miniMapUIY) 
				&& (y<miniMapUIY+miniMapUI.getHeight()));
	}
	
	
	private boolean inUnitInfo(int x, int y){
		return ((x>unitInfoUIX) 
				&& (x<unitInfoUIX+unitInfoUI.getWidth())
				&& (y>unitInfoUIY) 
				&& (y<unitInfoUIY+unitInfoUI.getHeight()));
	}
	
	private boolean inAbilityGrid(int x, int y){
		return ((x>abilityGridUIX) 
				&& (x<abilityGridUIX+abilityGridUI.getWidth())
				&& (y>abilityGridUIY) 
				&& (y<abilityGridUIY+abilityGridUI.getHeight()));
	}
	
	private void mapScroll(int direction){
		float newScroll = (float) (scrollSpeed*speedModifier);
		float newViewX = (float) (viewPortScrollSpeedX*speedModifier);
		float newViewY = (float) (viewPortScrollSpeedY*speedModifier);
		
		if (direction == LEFT){
			cameraX += newScroll;
			viewPortBox.setX(viewPortBox.getX()-newViewX);
			if (dragActive){
	            dragStartX += newScroll;
	            dragBox.setX(dragStartX);
			}
		}else if (direction == RIGHT){
			cameraX -= newScroll;
			viewPortBox.setX(viewPortBox.getX()+newViewX);
			if (dragActive){
	            dragStartX -= newScroll;
	            dragBox.setX(dragStartX);
			}
		}if (direction == UP){
			cameraY += newScroll;
			viewPortBox.setY(viewPortBox.getY()-newViewY);
			if (dragActive){
	            dragStartY += newScroll;
	            dragBox.setY(dragStartY);
			}
		}else if(direction == DOWN){
			cameraY -= newScroll;
			viewPortBox.setY(viewPortBox.getY()+newViewY);
			if (dragActive){
	            dragStartY -= newScroll;
	            dragBox.setY(dragStartY);
			}
		}
		mapPositionCorrection();
	}

	private void keyMapScroll(Input input){
		if (!(cameraX==0) && input.isKeyDown(Input.KEY_LEFT)
				&& !input.isKeyDown(Input.KEY_RIGHT)){
			mapScroll(LEFT);
			keyMove = true;
		}else if (!(cameraX==rightEdge) && input.isKeyDown(Input.KEY_RIGHT)
				&& !input.isKeyDown(Input.KEY_LEFT)){
			mapScroll(RIGHT);
			keyMove = true;
		}if (!(cameraY==0) && input.isKeyDown(Input.KEY_UP)
				&& !input.isKeyDown(Input.KEY_DOWN)){
			mapScroll(UP);
			keyMove = true;
		}else if (!(cameraY==bottomEdge) && input.isKeyDown(Input.KEY_DOWN)
				&& !input.isKeyDown(Input.KEY_UP)){
			mapScroll(DOWN);
			keyMove = true;
		}
		if (keyMove && !input.isKeyDown(Input.KEY_LEFT)
			        && !input.isKeyDown(Input.KEY_RIGHT)
			        && !input.isKeyDown(Input.KEY_UP)
			        && !input.isKeyDown(Input.KEY_DOWN))
			keyMove = false;
	}
	
	private void mapPositionCorrection() {
		if (cameraX>0){
			cameraX = 0;
			viewPortBox.setX(miniMapX);
		}if (cameraY>0){
			cameraY = 0;
			viewPortBox.setY(miniMapY);
		}if (cameraX<rightEdge){
			cameraX = rightEdge;
			viewPortBox.setX(miniRightEdge);
		}if (cameraY<bottomEdge){
			cameraY = bottomEdge;
			viewPortBox.setY(miniBottomEdge);
		}		
	}
	
	private void mouseMapScroll(Input input){
		if(!keyMove){//disallows key+mouse double speed
			// camera not at left edge and mouse at left side?
			if (!(cameraX==0) && mousePosX<borderWidth) 
				mapScroll(LEFT);
			// camera not at right edge and mouse at right side?
			else if (!(cameraX==rightEdge) && mousePosX>rightSide)
				mapScroll(RIGHT);
			// camera not at top edge and mouse at top side?
			if (!(cameraY==0) && mousePosY<borderWidth)
				mapScroll(UP);
			// camera not at bottom edge and mouse at bottom side?
			else if (!(cameraY==bottomEdge) && mousePosY>bottomSide)
				mapScroll(DOWN);
		} 
	}


	private void commandInput(Input input){
		if (!dragActive){
			for (int i = 0; i<buttons.length; i++){
				if (buttons[i] != null){
					if (buttons[i].has(mousePosX, mousePosY)){
						if (!buttons[i].isDown() && (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))){
							buttons[i].setDown(true);
							System.out.println("You are pressing "+ buttons[i].getName());
						}
						if (buttons[i].isDown() && !(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))){
							buttons[i].setDown(false);
							System.out.println("You are no longer pressing "+ buttons[i].getName() + " and the skill is in effect.");
						}
					}
					else if (buttons[i].isDown()){
						buttons[i].setDown(false);
						System.out.println("You are no longer pressing "+ buttons[i].getName());
					}
				}
			}
		}
	}

	private void dragSelection(Input input) throws SlickException{
		if (dragActive && input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
			dragBox.setWidth(mousePosX-dragStartX);
			dragBox.setHeight(mousePosY-dragStartY);
		}
		
		if (dragActive && !input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
			dragActive = false;
			dragEndX = (int) (dragBox.getX()+dragBox.getWidth());
			dragEndY = (int) (dragBox.getY()+dragBox.getHeight());
			int minX = dragStartX;
			int maxX = dragEndX;
	        int backup;
	        if (minX>maxX){
	                backup = minX;
	                minX = maxX;
	                maxX = backup;
	        }
	        int minY = dragStartY;
	        int maxY = dragEndY;
	        if (minY>maxY){
	        	backup = minY;
	        	minY = maxY;
	        	maxY = backup;
	        }
	        if (!input.isKeyDown(Input.KEY_LSHIFT)){
	        	selection = new ArrayList<Unit>();
	        	selectionPath = new ArrayList<SUnit>();
	        }
	        int unitID = -1;
	        for (int i = 0; i< units.size(); i++){
	        	float x = units.get(i).getPath().getX()+cameraX;
            	float y = units.get(i).getPath().getY()+cameraY;
            	float radius = (float)units.get(i).getPath().getRadius();
            	int h = units.get(i).getHeight();
	        	Rectangle rect = new Rectangle(x-radius, y-h, radius*2, h);
	        	if(rect.contains(dragEndX,dragEndY)){
	        		unitID = units.get(i).getID();
	        		break;
	        	}
	        }
	        if (unitID!=-1 && (input.isKeyDown(Input.KEY_LCONTROL) 
	        		|| clickCount>=2)){
	        	clickCount=1;
	        	Rectangle span = new Rectangle(0,0,windowWidth,windowHeight);
	        	for(int i = 0; i<units.size(); i++){
	        		Unit u = units.get(i);
	        		if(span.contains(u.getPath().getX()+cameraX,
	        						 u.getPath().getY()+cameraY)
	        			&& u.getID()==unitID){
	        			selection.add(u);
            			selectionPath.add(u.getPath());
	        		}
	        	}
	        }
	        for (int i = 0; i< units.size(); i++){
	        	Unit u = units.get(i);
	            if (!selection.contains(u)){
	            	if (dragBox.getWidth()<0){
	            		dragBox.setX(dragBox.getX()+dragBox.getWidth());
	            		dragBox.setWidth(-dragBox.getWidth());
	            	}
	            	if (dragBox.getHeight()<0){
	            		dragBox.setY(dragBox.getY()+dragBox.getHeight());
	            		dragBox.setHeight(-dragBox.getHeight());
	            	}
	            	float x = units.get(i).getPath().getX()+cameraX;
	            	float y = units.get(i).getPath().getY()+cameraY;
	            	float radius = (float)units.get(i).getPath().getRadius();
	            	//Circle circ = new Circle(x,y,radius);
	            	int h = units.get(i).getHeight();
	            	Rectangle rect = new Rectangle(x-radius, y-h, radius*2, h);
	            	if (/*dragBox.intersects(circ) ||*/ 
	            		dragBox.intersects(rect)){
	            			selection.add(u);
	            			selectionPath.add(u.getPath());
	            	}
	        	}
	        }
	        clickCount++; //used for double click
	        updatePriorities();
	        resetAbilityGrid();
	    }
		
	}
	

	private void updatePriorities() {
    	unitIcons = new Unit[16];
		 if (selection.size()!=0){
		        prioritySelection = selection;
		        Collections.sort(prioritySelection, new Comparator<Unit>(){
				     public int compare(Unit u1, Unit u2){
				         if(u1.getPriority() == u2.getPriority())
				             return 0;
				         return u1.getPriority() < u2.getPriority() ? -1 : 1;
				     }
				});
		        priority = prioritySelection.get(0);
	        }else{
	        	priority = null;	
	        }
		 updateSelectionInfo();
		
	}
	
	private void updateSelectionInfo(){
		if (selection.size()!=0){
			for (int i = 0; i<Math.min(16,prioritySelection.size()); i++)
				unitIcons[i] = prioritySelection.get(i);
			buttons = priority.getGrid();
		} 
		else{
			buttons = new Button[16];
		}
	}



	private void minimapClick(Input input){
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
			if  (inMinimap(mousePosX,mousePosY))
				mapClick = true;
			else if(!inAbilityGrid(mousePosX,mousePosY)
					&& !inUnitInfo(mousePosX,mousePosY)
					&& !inMinimapUI(mousePosX,mousePosY)){
				dragStartX = mousePosX;
				dragStartY = mousePosY;
				dragActive = true;
				dragBox.setX(dragStartX);
				dragBox.setY(dragStartY);
			}
		}
		
		
		if (mapClick && input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
				miniXDiff = viewPortX - miniMapX;
				miniYDiff = viewPortY - miniMapY;
				viewPortX = mousePosX-viewPortWidth/2;
	            if (viewPortX<miniMapX)
	                viewPortX = miniMapX;
	            else if (viewPortX>miniRightEdge)
	                viewPortX = miniRightEdge;
	            viewPortY = mousePosY-viewPortHeight/2;
	            if (viewPortY<miniMapY)
	                viewPortY = miniMapY;
	            else if (viewPortY>miniBottomEdge)
	                viewPortY = miniBottomEdge;         
	            cameraX = -miniXDiff/miniMapWidth*mapWidth;
	            cameraY = -miniYDiff/miniMapHeight*mapHeight;
	            viewPortBox.setY(viewPortY);
	            viewPortBox.setX(viewPortX);
	    }
		if (mapClick && !input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
			mapClick = false;
	}

	private void moveCommand(Input input){
		if (input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)){
			if(selection.size() != 0){
				if (inMinimap(mousePosX,mousePosY)){ //move command via minimap
					moveCommandAnim.restart();
					moveCommanding = true;
					double xDiff = mousePosX-miniMapX;
		            double yDiff = mousePosY-miniMapY;
		            double actualX = xDiff/miniMapWidth*mapWidth;
		            double actualY = yDiff/miniMapHeight*mapHeight;
		            commandX = (int)(xDiff/miniMapWidth*mapWidth+cameraX);
					commandY = (int)(yDiff/miniMapHeight*mapHeight+cameraY);
					sim.group_move_order(selectionPath, 
		    			new Point2D.Double(actualX, actualY));
				}else if (!inMinimapUI(mousePosX, mousePosY) //immediate screen
					&& !inUnitInfo(mousePosX, mousePosY)
					&& !inAbilityGrid(mousePosX, mousePosY)){
					moveCommandAnim.restart();
					moveCommanding = true;
					commandX = (int) (mousePosX-cameraX);
					commandY = (int) (mousePosY-cameraY);
					sim.group_move_order(selectionPath, 
	    				new Point2D.Double(mousePosX-cameraX, 
	    								   mousePosY-cameraY));
				}
			}
		}
	}

	private void unitDirectionUpdate(){
		for(int i = 0; i < units.size(); i++){
			double angle = units.get(i).getPath().getAngle() + 45/2;
			if (angle<0) angle+=360;
			angle%=360;
			int a = (int)(angle/45)+6;
			a%=8;
			if(a<0)
				a+=8;
			units.get(i).setDir(a);
			continue;
		}
	}
	
	private void unitPoseUpdate(){
		for(int i = 0; i < units.size(); i++){
			SUnit u = units.get(i).getPath();
			if (u.isMoving())
				units.get(i).setCAnim(Unit.WALK);
			else
				units.get(i).setCAnim(Unit.IDLE);
		}
	}
	
	private void unitInfoUpdate(Input input) throws SlickException{
		if (selection.size()!= 0){
			if (input.isKeyPressed(Input.KEY_TAB)){
				setNextPriority();
			}
			if (input.isKeyPressed(Input.KEY_A)){
				for (int i = 0; i<selection.size(); i++){
					selection.get(i).setHP(selection.get(i).getHP()-5);
				}
			}
			if (selection.size()>1){
				if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
					unitInfoClicking = true;
				}
				if (unitInfoClicking 
						&& !input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
					for (int i = selection.size()-1; i>=0; i--){
						if (selectionIcons[i].contains(mousePosX,mousePosY)){
							if (input.isKeyDown(Input.KEY_LSHIFT)){
								selection.remove(unitIcons[i]);
								selectionPath.remove(unitIcons[i].getPath());
							}else{
								selection.clear();
								selection.add(unitIcons[i]);
								selectionPath.clear();
								selectionPath.add(unitIcons[i].getPath());
								
							}
							updatePriorities();
							break;
						}
						unitInfoClicking = false;
						
					}
				}
			}
				
		}
	}
	
	private void deathUpdate() throws SlickException{ //remove dead units
		ArrayList<Unit> toremove = new ArrayList<Unit>();
		ArrayList<SUnit> toremove2 = new ArrayList<SUnit>();
		boolean priorityDead = false;
		for (int i = 0; i<units.size(); i++){
			if (units.get(i).getHP() == 0){
				toremove.add(units.get(i));
				toremove2.add(units.get(i).getPath());
				if (units.get(i).equals(priority))
					priorityDead = true;
			}
		}
		units.removeAll(toremove);
		selection.removeAll(toremove);
		prioritySelection.removeAll(toremove);
		selectionPath.removeAll(toremove2);
		if (priorityDead) //update priority unit if he died
			setNextPriority();

	}
	
	private void setNextPriority() throws SlickException{
		if (selection.size()!=0){
			int curPriority = priority.getPriority();
			for (int i = 0; i<prioritySelection.size(); i++){
				if (prioritySelection.get(i).getPriority()>curPriority){
					priority = prioritySelection.get(i);
					break;
				}
				if (i == prioritySelection.size()-1)    //if no one with 
					priority = prioritySelection.get(0);//less priority, 
				                                        //go back to first
			}
			buttons = priority.getGrid();
			resetAbilityGrid();
		}else{
			buttons = new Button[16];
			resetAbilityGrid();
			priority = null;
		}
	}

	private void drawSelectionCircles(Graphics g) {
		for (int i=0;i<selection.size();i++){
			Unit u = selection.get(i);
			double diameter = u.getPath().getRadius()*2;
			int adj = u.getPosnAdjust();
			g.setLineWidth(2);
			g.setColor(Color.green);
			g.drawOval((float)(u.getPath().getX()+cameraX-diameter/2),
					   (float)(u.getPath().getY()+cameraY-diameter/2)-adj,
					   (float)diameter,
					   (float)diameter);
			g.setLineWidth(1);
		}	
	}

	private void drawMiniMapDots(Graphics g) {
		for (int i = 0; i<units.size(); i++){ 
            double xDiff = units.get(i).getPath().getX();
            double yDiff = units.get(i).getPath().getY();
            double radius = units.get(i).getPath().getRadius();
            float miniUnitX = (float) (miniMapX+xDiff*miniMapWidth/mapWidth);
            float miniUnitY = (float) (miniMapY+yDiff*miniMapHeight/mapHeight);
            float miniSize = (float)((radius*2)*viewPortWidth/windowWidth);
            Rectangle blip = new Rectangle(miniUnitX,
            		                       miniUnitY, 
            		                       miniSize, 
            		                       miniSize);
            if (selection.contains(units.get(i)))
            	g.setColor(new Color(0,255,0));
            else
            	g.setColor(new Color(0,200,0));
            g.fill(blip);   
		}
	}

	private void drawUnits(Graphics g) {
		drawnUnits = units;
		Collections.sort(drawnUnits, new Comparator<Unit>(){
		     public int compare(Unit u1, Unit u2){
		         if(u1.getPath().getY() == u2.getPath().getY())
		             return 0;
		         return u1.getPath().getY() < u2.getPath().getY() ? -1 : 1;
		     }
		});
		for (int i = 0; i<drawnUnits.size(); i++){
			Unit u = drawnUnits.get(i);
			int uh = u.getHeight();
			int uw = u.getWidth()/2;
			u.getAnim().draw(u.getPath().getX()+cameraX-uw, 
							 u.getPath().getY()+cameraY-uh);
		}	
	}

	private void drawDragBox(Graphics g) {
		if (dragActive){
			g.setLineWidth(2);
			g.setColor(Color.green);
			g.draw(dragBox);
			g.setLineWidth(1);
		}	
	}

	private void drawUnitInfo(Graphics g) throws SlickException {
		if (priority!=null){
			priority.getPortr().draw(unitInfoUIX, unitInfoUIY);				
		}
		unitInfoUI.draw(unitInfoUIX, unitInfoUIY);
		if (selection.size() == 1){
			Unit u = selection.get(0);
			g.setColor(Color.white);
			g.setFont(fontBig);
			int distLeft = 359;
			int distTop = 17;
			String name = u.getName();
			g.drawString(name, unitInfoUIX+distLeft-fontBig.getWidth(name)/2, 
							   unitInfoUIY+distTop);
			int maxHP = u.getMaxHP();
			int hp = u.getHP();
			int maxMP = u.getMaxMP();
		    int mp = u.getMaxMP();
			int vShift = 20;
			int hpBarLength = 390;
			g.setFont(fontSm);
			g.drawString("HP",unitInfoUIX+145, unitInfoUIY+39);
			g.setColor(Color.red);
			g.fillRect(unitInfoUIX+167, unitInfoUIY+43,hpBarLength,12);
			g.setColor(new Color(34,177,76));
			g.fillRect(unitInfoUIX+167, unitInfoUIY+43,
					   hp*hpBarLength/maxHP,12);
			g.setColor(Color.white);
			g.drawString(hp+"/"+maxHP, unitInfoUIX+343, unitInfoUIY+39);
			if (maxMP!=0){
				g.drawString("MP",unitInfoUIX+145, unitInfoUIY+39+vShift);
				g.setColor(Color.black);
				g.fillRect(unitInfoUIX+167, 
						   unitInfoUIY+43+vShift,hpBarLength,12);
				g.setColor(Color.blue);
				g.fillRect(unitInfoUIX+167, 
						   unitInfoUIY+43+vShift,mp*hpBarLength/maxMP,12);
				g.setColor(Color.white);
				g.drawString(mp+"/"+maxMP, unitInfoUIX+343, 
						     unitInfoUIY+39+vShift);
			}
			if (u.isHero()){
				int xp = u.getExp();
				int maxXP = 100;
				g.drawString("XP",unitInfoUIX+145, unitInfoUIY+39+vShift*2);
				g.setColor(Color.black);
				g.fillRect(unitInfoUIX+167, 
						   unitInfoUIY+43+vShift*2,hpBarLength,12);
				g.setColor(Color.orange);
				g.fillRect(unitInfoUIX+167, unitInfoUIY+43+vShift*2,
						   (xp*hpBarLength/maxXP),12);
				g.setColor(Color.white);
				g.drawString(xp+"/"+maxXP, unitInfoUIX+343, 
						     unitInfoUIY+39+vShift*2);
			}
		}
		else if (selection.size()>1){
			int hpGap = 52; //distance from icon to hp bar
			int mpGap = 60; //distance from icon to mp bar
			for (int i = 0; i<selectionIcons.length; i++){
				if (unitIcons[i]!=null){
					Unit u = unitIcons[i];
					int maxHP = u.getMaxHP();
					int hp = u.getHP();
					int maxMP = u.getMaxMP();
				    int mp = u.getMaxMP();
				    if (u.getID()==priority.getID()){
				    	g.setLineWidth(2);
				    	g.setColor(Color.white);
						g.fillRect(selectionIcons[i].getX()-2,
								   selectionIcons[i].getY()-2,
								   52,52);
						g.setLineWidth(1);
				    }
					u.getIcon().draw(selectionIcons[i].getX(), 
									 selectionIcons[i].getY());
					if (u.isHero()){
				    	g.setColor(Color.orange);
						g.drawRect(selectionIcons[i].getX()-1,
								   selectionIcons[i].getY()-1,
							       50,50);
				    }
					g.setColor(Color.red);
					g.fillRect(selectionIcons[i].getX(),
							   selectionIcons[i].getY()+hpGap,48,5);
					g.setColor(new Color(34,177,76));
					g.fillRect(selectionIcons[i].getX(),
							   selectionIcons[i].getY()+hpGap,hp*48/maxHP,5);
					
					if (maxMP!=0){
						g.setColor(Color.black);
						g.fillRect(selectionIcons[i].getX(),
								   selectionIcons[i].getY()+mpGap,48,5);
						g.setColor(Color.blue);
						g.fillRect(selectionIcons[i].getX(),
								   selectionIcons[i].getY()+mpGap,mp*48/maxMP,
								   5);
					}
					if (!dragActive 
							&& selectionIcons[i].contains(mousePosX, 
														  mousePosY)){
							g.setColor(Color.gray);
							g.setLineWidth(2);
							g.drawRect(selectionIcons[i].getX()-1, 
									selectionIcons[i].getY()-1, 
									50, 50);
							g.setLineWidth(1);
					}
					
				}
			}
		}
	}
	private void drawAbilities(Graphics g){
		for (int i = 0; i<buttons.length; i++){
			if (buttons[i]!=null){
				buttons[i].getIcon().draw(buttons[i].getX(), buttons[i].getY());
				if (!dragActive){
					if (buttons[i].isDown()){
						g.setColor(Color.green);
						g.setLineWidth(2);
						g.drawRect(buttons[i].getX()-1, buttons[i].getY()-1, 50, 50);
						g.setLineWidth(1);
					}else if(buttons[i].has(mousePosX, mousePosY)){
						g.setLineWidth(2);
						g.setColor(Color.white);
						g.drawRect(buttons[i].getX()-1, buttons[i].getY()-1, 50, 50);
						g.setLineWidth(1);
					}
				}
			}
		}
	}

	public BaseTest(){
		super("Map"); //Window caption
	}

	@Override
	//initialize all values here.
	public void init(GameContainer container) throws SlickException{
		cameraX = -initialX;
		cameraY = -initialY;
		initFonts();
		initMap();
    	initWalls();
		initMapBoundaries();
		for (int i = 0; i<walls.size(); i++) //add walls to simulation
			sim.add_wall(walls.get(i));
		initUI();
		resetAbilityGrid();
		initUnitInfoIcons();
		mapPositionCorrection(); //if initial posn was overshot
	}
	
	@Override
	//calculations and updating should go into here
	public void update (GameContainer container, int delta)
			throws SlickException{
		sim.tick();
		speedModifier = (float) (delta/1000.0);
		if(clickCount>=2){
			frameCount++;
			if (frameCount>10){
				frameCount = 1;
				clickCount=1;
			}
		}
		Input input = container.getInput(); //obtains all input
		if (input.isKeyDown(Input.KEY_ESCAPE))
			System.exit(0);
		keyMapScroll(input);
		mousePosX = input.getMouseX();
		mousePosY = input.getMouseY();
		mouseMapScroll(input);
		minimapClick(input);
		dragSelection(input);
		unitSpawnHack(input);
		moveCommand(input);
		commandInput(input);
		unitDirectionUpdate();
		unitPoseUpdate();
		unitInfoUpdate(input);
		deathUpdate();
		if(moveCommandAnim.isStopped())
			moveCommanding = false;
	}
	
	//all graphics rendering should be done here
	public void render(GameContainer container, Graphics g)
			throws SlickException{
		if (g.getClip()==null) //only renders window area, saves power
			g.setClip(0,0,windowWidth,windowHeight);
		map.draw(cameraX, cameraY);
		if (moveCommanding)
			moveCommandAnim.draw(commandX-33+cameraX, commandY-41+cameraY);
		drawWallHack(g);
		drawSelectionCircles(g);
		drawUnits(g);
		drawDragBox(g);
		miniMapUI.draw(miniMapUIX, miniMapUIY);
		miniMap.draw(miniMapX, miniMapY);
		drawMiniMapDots(g);
		g.setColor(Color.white);
		g.draw(viewPortBox); //white minimap box
		g.setColor(Color.darkGray);
		g.fillRect(unitInfoUIX,unitInfoUIY,120,164); //portrait bg
		abilityGridUI.draw(abilityGridUIX, abilityGridUIY);
		drawAbilities(g);
		drawUnitInfo(g);
		g.drawString(""+clickCount,0,0);
		g.drawString(""+selection.size(),0,15);
		
	}
	
	// initializes the window
	public static void main(String [] args){
		try{
			AppGameContainer app = new AppGameContainer(new BaseTest());
			app.setDisplayMode(windowWidth, windowHeight, fullScreen);
			app.setVSync(vSync);
			app.setTargetFrameRate(frameRate);
			app.setShowFPS(fpsShow);
			app.setMouseGrabbed(mouseGrab);
			app.start();
		} catch (SlickException e){
			e.printStackTrace();
		}
	}
}
