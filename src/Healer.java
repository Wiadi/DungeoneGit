
public class Healer extends Adventurer
{
	public Healer(GameMap m, int x, int y)
	{
		super(m,x,y);
		tileType=HEALER;
		baseAttack=-1;
		currentAttack=-1;
		attackRange=1;
		abilities[REVIVE]=true;
		baseHealth=10;
		currentHealth=10;
		moveRange=1;
		regenRate=1;
		visionRange=5;
	}
}