
public abstract class Adventurer extends Actor
{
	public Adventurer()
	{
		super();
		tileType+=100;
	}
	
	public abstract boolean canSee(int xRel, int yRel);
}