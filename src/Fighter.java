/**
 * The most basic type of Adventurer, a fighter can walk around and hit things.
 * @author Andrew Simler
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
		baseHealth=15;
		currentHealth=15;
		moveRange=1;
		visionRange=5;
		regenRate=2;
		for(int i=0;i<abilities.length;i++)
			abilities[i]=false;
	}
}