/**
 * The most basic type of Monster, a slim can walk around and hit things.
 * @author 941923
 */
public class Slim extends Monster
{
	public Slim(GameMap m, int x, int y)
	{
		super(m,x,y);
		tileType=SLIM;
		baseAttack=1;
		currentAttack=1;
		attackRange=1;
		baseHealth=5;
		currentHealth=5;
		moveRange=1;
		regenRate=1;
	}
}