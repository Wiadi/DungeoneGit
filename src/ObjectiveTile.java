/**
 * The objective, which the Adventurers must reach to clear the level.
 * @author Andrew Simler
 */
public class ObjectiveTile extends Tile
{
	public ObjectiveTile(GameMap m, int x, int y)
	{
		super(m,x,y);
		tileType=OBJECTIVE;
	}	
}