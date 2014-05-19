/**
 * A powerful and durable Monster that moves somewhat awkwardly, like a chess knight. NOTE: not currently in use.
 * @author Andrew Simler
 */
public class Knight extends Monster
{
	public Knight(GameMap m, int x, int y)
	{
		super(m,x,y);
		tileType=KNIGHT;
		baseAttack=2;
		currentAttack=2;
		attackRange=1;
		baseHealth=10;
		currentHealth=10;
		regenRate=2;
	}
	public boolean canMoveTo(int x, int y)
	{
		return ((Math.abs(x-xPos)==1 && Math.abs(y-yPos)==2) || (Math.abs(x-xPos)==2 && Math.abs(y-yPos)==1));
	}
}