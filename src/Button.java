import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;


public class Button {
	private String name = "Unnamed Skill";
	private String tooltip = "No tooltip provided";
	private Rectangle button;
	private int x,y;
	private Image icon = null;
	//private Skill skill;
	private boolean canAutoCast = false; //whether or not its possible
	private boolean isAutoCast = false; //if autocast is currently active
	private boolean isPassive = false; //clicking this will do nothing
	private boolean isDown = false; //is the button being pressed?
	
	public Button (String name, String icpath, int x, int y) 
			throws SlickException{
		this.setName(name);
		this.setIcon(new Image(icpath));
		this.x=x;
		this.y=y;
		this.button = new Rectangle (x,y,48,48);
		
	}
	
	public Button (String name, String icpath, int x, int y, boolean a) 
			throws SlickException{
		this.setName(name);
		this.setIcon(new Image(icpath));
		this.setPassive(a);
		this.x=x;
		this.y=y;
		this.button = new Rectangle (x,y,48,48);
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Image getIcon() {
		return icon;
	}

	public void setIcon(Image icon) {
		this.icon = icon;
	}
	
	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public boolean canAutoCast() {
		return canAutoCast;
	}

	public void setAutoCastable(boolean canAutoCast) {
		this.canAutoCast = canAutoCast;
	}

	public boolean isAutoCast() {
		return isAutoCast;
	}

	public void setAutoCast(boolean isAutoCast) {
		this.isAutoCast = isAutoCast;
	}

	public boolean isPassive() {
		return isPassive;
	}

	public void setPassive(boolean isPassive) {
		this.isPassive = isPassive;
	}

	public boolean isDown() {
		return isDown;
	}

	public void setDown(boolean b) {
		this.isDown = b;
	}
	
	public void setPosn(int x, int y){
		this.x=x;
		this.y=y;
		this.button = new Rectangle(x, y, 48, 48);
	}
	
	public boolean has(int x, int y){
		return button.contains(x, y);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
}
