/**
 * The basic unit of the game. Every layer of every square of the map holds a tile; tile
 * types include objects, structural features, living creatures, and empty space.
 * @author 941923
 */
public abstract class Tile
{	
	protected int tileType;
	public Tile()
	{
		tileType=-1;
	}
	/**
	 * Returns an integer denoting the type of tile a Tile is.
	 * @return 0 if an empty tile,
	 * 		   1 if a floor tile,
	 * 		   2 if a wall tile,
	 * 		   1xx if an Adventurer,
	 * 				100 if a Fighter,
	 * 		   2xx if a Monster,
	 * 				200 if a Slim
	 */
	public int getType()
	{
		return tileType;
	}
}