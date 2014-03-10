
public class Fighter extends Adventurer
{
	
	public Fighter()
	{
		super();
		baseAttack=2;
		currentAttack=2;
		baseHealth=10;
		currentHealth=10;
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
	
	public boolean canSee(int xRel, int yRel)
	{
		if(xRel*xRel+yRel*yRel<=25)
			return true;
		return false;
	}
}