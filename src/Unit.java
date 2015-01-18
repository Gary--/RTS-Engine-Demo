import java.awt.geom.Point2D;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/*Created by: Maltose
 *
 * The Unit class provides a logical way to create the main objects of the RTS
 * Units vary from warriors to structures, destructibles, and doodads.
 *
 * Structures are usually immobile, and have the ability to create or upgrade 
 * units
 *
 * Destructibles are mainly aesthetic or obstacles that can be targeted
 * and destroyed
 *
 * Doodads serve as mainly aesthetic items that set the atmosphere, 
 * they are not targetable or destructible
 * 
 * You can make a quick switch to properties to classify a unit as one of
 * the above quickly, by using the setAsX() function, where X is Destructible
 * Doodad or Structure.
 */

public class Unit {
	//Constants

	//MoveTypes
	public static final int //NONE = -1, //for immobile units
							GROUND = 0, //restrained to land
							AIR = 1, //unrestrained travel
							WATER = 2; //restrained to water
	
	//Directions
	public static final int S = 0, //south
			                SW = 1, //southwest
							W = 2, //west
							NW = 3, //northwest
							N = 4, //north
							NE = 5, //northeast
							E = 6, //east
							SE = 7; //southeast
	//Animation Types
	public static final int IDLE = 0,
							WALK = 1,
							ATTACK = 2,
							DEATH = 3,
							//--
							SPEC1 = 4,
							SPEC2 = 5,
							SPEC3 = 6,
							SPEC4 = 7;
	
	//Portrait
	public static final int TALK = 1;
							//IDLE = 0, -- shared with Animation Types
	
	//---------------------------------------------------
	
	private String name;
    private String faction;
	private int maxHP = 50;
    private int HP = 50;
    private int maxMP = 50;
    private int MP = 50;
    private int unitRadius = 10;
    private int moveType = GROUND;
    private int moveSpeed = 3;
    private int bonusAtk = 0;
    private Weapon primary;
    private Weapon secondary;
    private int defense = 10;
    private int mdefense = 10;
    private boolean isSelectable = true;
    private boolean isInvincible = false;
    private boolean isImmobile = false;
    private boolean isFriendly = true;
    private int height = 0, width = 0;
    private int posnAdjust = 0;
    private int priority = 1;
    private int id = 0;
    private Button[] grid = new Button[16];
    private Image icon;
    private Point2D posn;;
    private SUnit pathing;
    
    // following are animation arrays.
    // directions are 0 - south, 1 - southwest,...,7 - southeast
    
    //idle, walk, attack, death, spec 1 - 4
    
    private Animation[][] poses = new Animation[8][8];
    private int curAnim = IDLE;
    private int direction = S;
    
    // Animation portrait;
    private Animation[] portrait = new Animation[2];
    private int curPort = IDLE;
    
    // SKILLARRAY
    //private Skill[] skills = new Skill[16];
    
	public Unit (String name, int radius, int spawnX, int spawnY) 
			throws SlickException{
		this.name = name;
		this.unitRadius = radius;
		this.posn = new Point2D.Double(spawnX, spawnY);
		this.pathing = new SUnit(posn, unitRadius, moveSpeed);
		icon = new Image("Graphics//Icons//Test.png");
	}
	
	
	// ensure that there are 8 rows, for 8 directions
	// starting south and going clockwise
	public void setAnim(int pose, String path, int col, int speed) 
			throws SlickException{
		Image temp = new Image(path);
		int frameW = temp.getWidth()/col;
		int frameH = temp.getHeight()/8;
		int tempW = temp.getWidth();
		Image temp2;
		SpriteSheet temps;
		for (int i = 0; i<8; i++){
			temp2 = temp.getSubImage(0,i*frameH,tempW,frameH);
			temps = new SpriteSheet(temp2, frameW, frameH);
			this.poses[pose][i] = new Animation(temps, speed);
		}
		if (height == 0 || width == 0){
			height = frameH;
			width = frameW;
		}
	}
	
	public void setCAnim(int type){
		this.curAnim = type;
	}
	
	public void setDir(int dir){
		this.direction = dir;
	}
	
	public Animation getAnim(){
		return this.poses[curAnim][direction];
	}

	public void setPortPose(int pose){
		curPort = pose;
	}
	
	public Animation getPortr(){
		return this.portrait[curPort];
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setMaxHP(int hp){
		this.maxHP = hp;
	}
	
	public void setHP(int hp){
		this.HP = hp;
		if (this.HP>maxHP)
			this.HP = maxHP;
		if (this.HP<0)
			this.HP = 0;
	}
	
	public int getMaxHP(){
		return this.maxHP;
	}
	
	public int getHP(){
		return this.HP;
	}
	
	public void setMaxMP(int mp){
		this.maxMP = mp;
	}
	
	public int getMaxMP(){
		return this.maxMP;
	}
	
	public void setMP(int mp){
		this.MP = mp;
		if (this.MP>maxMP)
			this.MP = maxMP;
		if (this.MP<0)
			this.MP = 0;
	}
	
	public int getMP(){
		return this.MP;
	}
	
	public void setMoveSpeed(int ms){
		this.moveSpeed = ms;
		this.pathing = new SUnit(posn, unitRadius, moveSpeed);
	}
	
	public int getMoveSpeed(){
		return this.moveSpeed;
	}
	
	public void setMoveType(int type){
		this.moveType = type;
	}
	
	public int getMoveType(){
		return this.moveType;
	}
	
	public void setDefense(int def){
		this.defense = def;
	}
	
	public int getDefense(){
		return this.defense;
	}
	
	public void setMDefense(int def){
		this.mdefense = def;
	}
	
	public int getMDefense(){
		return this.mdefense;
	}
	
	public void setInvincible(boolean i){
		this.isInvincible = i;
	}

	public boolean isInvincible(){
		return isInvincible;
	}
	
	public void setSelectable(boolean i){
		this.isSelectable = i;
	}
	
	public boolean isSelectable(){
		return isSelectable;
	}
	
	public void setImmobile(boolean i){
		this.isImmobile = i;
	}
	
	public boolean isImmobile(){
		return isImmobile;
	}
	
	public void setFriendly(boolean i){
		this.isFriendly = i;
	}
	
	public boolean isFriendly(){
		return isFriendly;
	}
	
	public void setPrimary(Weapon p){
		this.primary = p;
	}
	
	public void setSecondary(Weapon s){
		this.secondary = s;
	}
	
	public Weapon getPrimary(){
		return this.primary;
	}
	
	public Weapon getSecondary(){
		return this.secondary;
	}
	
	public void setAsStructure(){
        this.isSelectable = true;
        this.isInvincible = false;
        this.isImmobile = true;
	}
	
	public void setAsDestructible(){
		this.isSelectable = false;
        this.isInvincible = false;
        this.isImmobile = true;
	}
	
	public void setAsDoodad(){
		this.isSelectable = false;
        this.isInvincible = true;
        this.isImmobile = true;
	}

	public String getFaction() {
		return faction;
	}

	public void setFaction(String faction) {
		this.faction = faction;
	}

	public void setPortIdle(String portpath) throws SlickException {
		SpriteSheet temp = new SpriteSheet(new Image(portpath), 120, 164);
		portrait[IDLE] = new Animation(temp, 90);		
	}
	public void setPortTalk(String portpath) throws SlickException {
		SpriteSheet temp = new SpriteSheet(new Image(portpath), 120, 164);
		portrait[TALK] = new Animation(temp, 90);		
	}

	public int getBonusAtk() {
		return bonusAtk;
	}

	public void setBonusAtk(int bonusAtk) {
		this.bonusAtk = bonusAtk;
	}

	public SUnit getPath() {
		return pathing;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getWidth(){
		return width;
	}
	public int getPosnAdjust() {
		return posnAdjust;
	}
	public void setPosnAdjust(int posnAdjust) {
		this.posnAdjust = posnAdjust;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getID() {
		return id;
	}
	public void setID(int id) {
		this.id = id;
	}
	public Button[] getGrid() {
		return grid;
	}
	public void setGrid(Button[] grid) {
		this.grid = grid;
	}
	
	public boolean isHero(){
		return false;
	}
	
	public int getExp(){
		return 0;
	}


	public Image getIcon() {
		return icon;
	}


	public void setIcon(Image icon) {
		this.icon = icon;
	}
	
	
}
