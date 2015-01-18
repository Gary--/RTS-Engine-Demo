import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class ButtonData {
	public static Button Move(int x, int y) throws SlickException{
		Button button = new Button("Move", "Graphics\\Icons\\move.png", x, y);
		button.setTooltip("Move this unit to the designated point.");
		//button.setAutoCastable(...);
		//button.setPassive(...);
		//button.setSkill(...);
		
		return button;
	}
	
	public static Button Attack(int x, int y) throws SlickException{
		Button button = new Button("Attack", "Graphics\\Icons\\attack.png", x, y);
		button.setTooltip("Command this unit to attack.  Target a unit or "
						+ "target the ground to move to destination while"
						+ "attacking all hostile units on the way.");
		//button.setAutoCastable(...);
		//button.setPassive(...);
		//button.setSkill(...);
		
		return button;
	}
}
