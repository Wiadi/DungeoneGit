/**
 * An empty tile, placed to avoid having to deal with obnoxious null pointer exceptions.
 * @author 941923
 */
public class EmptyTile extends Tile
{
	public EmptyTile()
	{
		super();
		tileType+=1;
	}
}