/**
 * A frustrating but individually harmless Monster, the Imp teleports its enemies.
 * @author 941923
 */
public class Imp extends Monster
{
	public Imp()
	{
		super();
		tileType=IMP;
		baseAttack=0;
		currentAttack=0;
		attackRange=1;
		abilities[BALE_TELE]=true;
		baseHealth=1;
		currentHealth=1;
		moveRange=2;
	}
}