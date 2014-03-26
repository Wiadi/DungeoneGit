/**
 * Adventurers are the Actors controlled by the Dungeonee.
 * @author 941923
 */
public abstract class Adventurer extends Actor
{
	public Adventurer(GameMap m, int x, int y)
	{
		super(m,x,y);
		tileType=ADVENTURER;
	}
	/**
	 * Checks whether an Adventurer can see a given relative location.
	 * @param xRel - x position of the target location relative to the Actor
	 * @param yRel - y position of the target location relative to the Actor
	 * @return true if the Adventurer can see the target location, false otherwise
	 */
	public abstract boolean canSee(int xRel, int yRel);
}