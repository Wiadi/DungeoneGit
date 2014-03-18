
public class Knight extends Monster
{
	public Knight()
	{
		super();
		tileType=KNIGHT;
		baseAttack=2;
		currentAttack=2;
		attackRange=1;
		baseHealth=10;
		currentHealth=10;
	}
	public boolean canMoveTo(int xRel, int yRel)
	{
		return ((Math.abs(xRel)==1 && Math.abs(yRel)==2) || (Math.abs(xRel)==2 && Math.abs(yRel)==1));
	}
}