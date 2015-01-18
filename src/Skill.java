import org.newdawn.slick.Animation;


public class Skill {
	//targeting types
	public static final int SINGLE = 0,
			                AOE = 1,
			                SELF = 2;
	//target's alignment
	public static final int ALLY = 0,
							ENEMY = 1,
							BOTH = 2;
	//projectile types
	public static final int INSTANT = 0,
							BOLT = 1,
	                        NONE = 3;
	
	
	private int targetType=SINGLE;
	private int tarAlign=BOTH;
	private int castPosX=0, castPosY=0;
	private int range=10;
	private int mpCost=0;
	private int delay=0;
	private int cooldown=0;
	private Animation animation;
	private int projectileType;
	
	public Skill(){}

	public int getTargetType() {
		return targetType;
	}

	public void setTargetType(int targetType) {
		this.targetType = targetType;
	}

	public int getTarAlign() {
		return tarAlign;
	}

	public void setTarAlign(int tarAlign) {
		this.tarAlign = tarAlign;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getMpCost() {
		return mpCost;
	}

	public void setMpCost(int mpCost) {
		this.mpCost = mpCost;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public Animation getAnim() {
		return animation;
	}

	public void setAnim(Animation animation) {
		this.animation = animation;
	}

	public int getProjectileType() {
		return projectileType;
	}

	public void setProjectileType(int projectileType) {
		this.projectileType = projectileType;
	}
	
	public void setCastPos(int x, int y){
		castPosX = x;
		castPosY = y;
	}
	
	public int getCastX(){
		return castPosX;
	}
	public int getCastY(){
		return castPosY;
	}

}
