/**
 * A floor tile, which will populate the vast majority of layer 0 of a map (all of it in the current version).
 * @author Andrew Simler
 */
public class FloorTile extends Tile
{
	public FloorTile(GameMap m, int x, int y)
	{
		super(m,x,y);
		tileType=FLOOR_TILE;
	}	
}