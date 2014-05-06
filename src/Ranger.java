
public class Ranger extends Adventurer
{
	public Ranger(GameMap m, int x, int y)
	{
		super(m,x,y);
		tileType=RANGER;
		baseAttack=1;
		currentAttack=1;
		attackRange=3;
		baseHealth=10;
		currentHealth=10;
		moveRange=1;
		regenRate=1;
		visionRange=7;
	}
}