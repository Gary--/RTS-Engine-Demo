import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class UnitData{
	public static Unit Test(int x, int y) throws SlickException{
		Unit unit = new Unit("Test", 30, x, y);
		unit.setID(0);
		unit.setPriority(2);
		unit.setPosnAdjust(15);
		unit.setMoveSpeed(5);
		unit.setMP(0);
		unit.setMaxMP(0);
		unit.setIcon(new Image("Graphics\\Icons\\Test.png"));
		unit.setAnim(Unit.IDLE,"Graphics\\Sprites\\Test\\Test.png",4, 110);
		unit.setAnim(Unit.WALK,"Graphics\\Sprites\\Test\\TestM.png",4, 110);
		unit.setAnim(Unit.DEATH,"Graphics\\Sprites\\Test\\TestD.png",4, 110);
		unit.setAnim(Unit.ATTACK,"Graphics\\Sprites\\Test\\TestA.png",4, 110);
		unit.setPortIdle("Graphics\\Portraits\\Test\\Test.png");
		unit.setPortTalk("Graphics\\Portraits\\Test\\Test.png");
		
		//GRID
		Button[] grid = new Button[16];
		grid[0] = ButtonData.Move(x, y);
		grid[2] = ButtonData.Attack(x, y);
		unit.setGrid(grid);
		
		return unit;
	}
	
	public static Hero Etrom(int x, int y) throws SlickException{
		Hero unit = new Hero("Etrom", 20, x, y);
		unit.setID(1);
		unit.setPriority(1);
		unit.setFaction("Aelonite");
		unit.setMaxHP(50);
		unit.setHP(50);
		unit.setMaxMP(50);
		unit.setMP(50);
		//unit.setPrimary(...);
		//unit.setSecondary(...);
		//unit.setMoveSpeed(...);
		unit.setMoveType(Unit.GROUND);
		//unit.addSkill(...);
		//...
		//unit.setDefense(...);
		//unit.setMDefense(...);
		unit.setIcon(new Image("Graphics\\Icons\\etrom.png"));
		unit.setAnim(Unit.IDLE,"Graphics\\Sprites\\Etrom\\Etrom.png",1, 110);
		unit.setAnim(Unit.WALK,"Graphics\\Sprites\\Etrom\\Etrom.png",1, 110);
		unit.setAnim(Unit.DEATH,"Graphics\\Sprites\\Test\\TestD.png",1, 110);
		unit.setAnim(Unit.ATTACK,"Graphics\\Sprites\\Test\\TestA.png",1, 110);
		unit.setPortIdle("Graphics\\Portraits\\Etrom\\Etrom.png");
		unit.setPortTalk("Graphics\\Portraits\\Etrom\\Etrom.png");
		unit.setPosnAdjust(15);
		
		Button[] grid = new Button[16];
		grid[0] = ButtonData.Move(x, y);
		grid[1] = ButtonData.Attack(x, y);
		
		unit.setGrid(grid);
		
		return unit;
	}
	
	public static Unit Penman(int x, int y) throws SlickException{
		Unit unit = new Unit("Penman",20,x,y);
		unit.setFaction("Aelonite");
		unit.setMaxHP(50);
		unit.setHP(50);
		unit.setMaxMP(0);
		unit.setMP(0);
		//unit.setPrimary(...);
		//unit.setSecondary(...);
		//unit.setMoveSpeed(...);
		unit.setMoveType(Unit.GROUND);
		//unit.addSkill(...);
		//...
		//unit.setDefense(...);
		//unit.setMDefense(...);
		//unit.setIdleAnim(...);
		//unit.setWalkAnim(...);
		//unit.setAtkAnim(...);
		//unit.setDeathAnim(...);
		return unit;
	}
	
}





