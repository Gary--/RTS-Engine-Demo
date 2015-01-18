import org.newdawn.slick.SlickException;


public class Hero extends Unit{
	private int level = 1;
	private int exp = 0;
	private int maxExp = 100;
	
	public Hero(String name,int radius,int spawnX,int spawnY) throws SlickException {
		super(name, radius, spawnX, spawnY);
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getExp() {
		return exp;
	}
	public void addExp(int exp){
		this.exp+=exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	
	public boolean isHero(){
		return true;
	}
	
}
