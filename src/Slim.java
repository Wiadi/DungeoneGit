/**
 * The most basic type of Monster, a slim can walk around and hit things.
 * @author 941923
 */
public class Slim extends Monster
{
	public Slim()
	{
		super();
		tileType+=SLIM;
		baseAttack=1;
		currentAttack=1;
		baseHealth=5;
		currentHealth=5;
		for(int i=0;i<abilities.length;i++)
			abilities[i]=false;
	}
	public boolean canMoveTo(int xRel, int yRel)
	{
		if(Math.abs(xRel)<=1 && Math.abs(yRel)<=1)
			return true;
		return false;
	}
	public boolean canAttack(int xRel, int yRel)
	{
		if(Math.abs(xRel)<=1 && Math.abs(yRel)<=1)
			return true;
		return false;
	}
}