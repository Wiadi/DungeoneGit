/**
 * A door tile, which can be open (permitting movement) or closed (blocking it).
 * @author Andrew Simler
 */
public class DoorTile extends Tile
{
	private boolean open;
	public DoorTile(GameMap m, int x, int y)
	{
		super(m,x,y);
		tileType=DOOR_TILE;
		open=false;
	}
	/**
	 * Checks whether the door is open.
	 * @return true if the door is open, false otherwise
	 */
	public boolean isOpen()
	{
		return open;
	}
	/**
	 * Closes an open door, or opens a closed door.
	 * @return true if the door is now open, false otherwise
	 */
	public boolean toggleOpen()
	{
		open=!open;
		return open;
	}
}