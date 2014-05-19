/**
 * A warp tile, through which Actors can travel to all other warp tiles on the map regardless of distance.
 * @author Andrew Simler
 */
public class WarpTile extends Tile
{
	public WarpTile(GameMap m, int x, int y)
	{
		super(m,x,y);
		tileType=Tile.WARP_TILE;
	}
	public boolean canSee(int x, int y)
	{
		return map.checkWarp() && Math.abs(x-xPos)<=1 && Math.abs(y-yPos)<=1;
	}
}