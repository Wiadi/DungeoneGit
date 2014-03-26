/**
 * The most basic type of Adventurer, a fighter can walk around and hit things.
 * @author 941923
 */
public class Fighter extends Adventurer
{
	public Fighter(GameMap m, int x, int y)
	{
		super(m,x,y);
		tileType=FIGHTER;
		baseAttack=2;
		currentAttack=2;
		attackRange=1;
		baseHealth=10;
		currentHealth=10;
		moveRange=1;
		visionRange=5;
		for(int i=0;i<abilities.length;i++)
			abilities[i]=false;
	}
}