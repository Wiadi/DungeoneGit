/**
 * A wall tile, used to construct the walls of the map.
 * @author 941923
 */
public class WallTile extends Tile
{
	public WallTile(GameMap m, int x, int y)
	{
		super(m,x,y);
		tileType=WALL_TILE;
	}
}