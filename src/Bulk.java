/**
 * A bulkier, more damaging, but slower Monster.
 * @author Andrew Simler
 */
public class Bulk extends Monster
{
	public Bulk(GameMap m, int x, int y)
	{
		super(m,x,y);
		tileType=Tile.BULK;
		baseAttack=2;
		currentAttack=2;
		attackRange=1;
		baseHealth=10;
		currentHealth=10;
		moveRange=0.5;
		regenRate=1;
	}
}