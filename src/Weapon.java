import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;


public class Weapon {
	//AttackTypes
	public static final int NORMAL = 0, //damage that is mitigated by pdef
			                MAGIC = 1, //damage that is mitigated by mdef
			                ADMINISTRATIVE = 2; //true damage
    private String name = "Weapon";
	private int range = 10;
	private int damage = 10;
	private int attackType = NORMAL;
	private int attackSpeed = 10;
	private Image icon = null;
	private Animation animation = null;
	// private Skill effect = null;
	
	public Weapon (String name){
		this.name = name;
	}
	
	public Weapon (String name, int dmg){
		this.name = name;
		this.damage = dmg;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setDamage(int dmg){
		damage = dmg;
	}
	
	public int getDamage (){
		return damage;
	}
	
	public void setRange(int r){
		range = r;
	}
	
	public int getRange(){
		return range;
	}
	
	public void setAttackType(int type){
		attackType = type;
	}
	
	public int getAttackType(){
		return attackType;
	}
	
	public void setAttackSpeed(int s){
		attackSpeed = s;
	}
	
	public int getAttackSpeed(){
		return attackSpeed;
	}
	
	public void setAnim(String path, int tw, int th, int speed) 
			throws SlickException{
		SpriteSheet temp = new SpriteSheet(new Image(path), tw, th);
		animation = new Animation(temp, speed);
	}
	
	public Animation getAnim(){
		return animation;
	}
	
	public void setIcon(String path) 
			throws SlickException{
		icon = new Image(path);
	}
	
	public Image getIcon(){
		return icon;
	}
}
