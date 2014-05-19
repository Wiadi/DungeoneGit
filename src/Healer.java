/**
 * An Adventurer capable of healing other Adventurers rather than hurting Monsters.
 * @author Andrew Simler
 */
public class Healer extends Adventurer
{
	public Healer(GameMap m, int x, int y)
	{
		super(m,x,y);
		tileType=HEALER;
		baseAttack=-2;
		currentAttack=-2;
		attackRange=1;
		abilities[REVIVE]=true;
		baseHealth=10;
		currentHealth=10;
		moveRange=1;
		regenRate=3;
		visionRange=5;
	}
}