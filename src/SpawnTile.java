/**
 * Designates the room in which adventurers will appear in a given level.
 * @author 941923
 */
public class SpawnTile extends Tile
{
	public SpawnTile(GameMap m, int x, int y)
	{
		super(m,x,y);
		tileType=SPAWN_TILE;
	}
}