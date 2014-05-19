/**
 * An empty tile, placed to avoid having to deal with obnoxious null pointer exceptions.
 * @author Andrew Simler
 */
public class EmptyTile extends Tile
{
	public EmptyTile(GameMap m, int x, int y)
	{
		super(m,x,y);
		tileType=EMPTY_TILE;
	}
}