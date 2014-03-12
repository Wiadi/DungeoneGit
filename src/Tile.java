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
	
	public int getType()
	{
		return tileType;
	}
}