/**
 * The objective, which the Adventurers must reach to clear the level.
 * @author 941923
 */
public class ObjectiveTile extends Tile
{
	public ObjectiveTile(GameMap m, int x, int y)
	{
		super(m,x,y);
		tileType=OBJECTIVE;
	}	
}