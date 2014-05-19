/**
 * Exactly what it sounds like: a Monster with no use except blocking passages as though it were a wall.
 * @author Andrew Simler
 */
public class LivingWall extends Monster
{
	public LivingWall(GameMap m, int x, int y)
	{
		super(m,x,y);
		tileType=Tile.LIVING_WALL;
		baseAttack=0;
		currentAttack=0;
		attackRange=0;
		baseHealth=10;
		currentHealth=10;
		moveRange=0;
		regenRate=2;
	}
}