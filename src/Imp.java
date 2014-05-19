/**
 * A frustrating but individually harmless Monster, the Imp teleports its enemies. NOTE: not currently in use.
 * @author Andrew Simler
 */
public class Imp extends Monster
{
	public Imp(GameMap m, int x, int y)
	{
		super(m,x,y);
		tileType=IMP;
		baseAttack=0;
		currentAttack=0;
		attackRange=1;
		abilities[BALE_TELE]=true;
		baseHealth=1;
		currentHealth=1;
		moveRange=2;
		regenRate=0;
	}
}