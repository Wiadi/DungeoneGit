/**
 * The basic unit of the game. Every layer of every square of the map holds a tile; tile
 * types include objects, structural features, living creatures, and empty space.
 * @author 941923
 */
public abstract class Tile
{	
	protected int tileType;
	protected GameMap map;
	protected int xPos;
	protected int yPos;
	public final static int EMPTY_TILE=0;
	public final static int FLOOR_TILE=1;
	public final static int WALL_TILE=2;
	public final static int OBJECTIVE=3;
	public final static int DOOR_TILE=4;
	public final static int SPAWN_TILE=5;
	public final static int WARP_TILE=6;
	public final static int ADVENTURER=100;
	public final static int FIGHTER=ADVENTURER+0;
	public final static int HEALER=ADVENTURER+1;
	public final static int RANGER=ADVENTURER+2;
	public final static int MONSTER=200;
	public final static int SLIM=MONSTER+0;
	public final static int IMP=MONSTER+1;
	public final static int KNIGHT=MONSTER+2;
	public Tile(GameMap m, int x, int y)
	{
		map=m;
		xPos=x;
		yPos=y;
		tileType=-1;
	}
	/**
	 * Returns an integer denoting the type of tile a Tile is.
	 * @return 0 if an empty tile,
	 * 		   1 if a floor tile,
	 * 		   2 if a wall tile,
	 * 		   3 if the objective,
	 * 		   4 if a door tile,
	 * 		   5 if the spawn tile,
	 * 		   1xx if an Adventurer,
	 * 				100 if a Fighter,
	 * 		   2xx if a Monster,
	 * 				200 if a Slim
	 * 				201 if an Imp
	 * 				202 if a Knight
	 */
	public int getType()
	{
		return tileType;
	}
	
	public Tile changePos(int x, int y)
	{
		xPos=x;
		yPos=y;
		return this;
	}
	/**
	 * Checks whether a Tile can see a given location.
	 * @param x - x position of the target location
	 * @param y - y position of the target location
	 * @return true if the Tile can see the target location, false otherwise
	 */
	public boolean canSee(int x, int y)
	{
		return false;
	}
	
	public int getX()
	{
		return xPos;
	}
	
	public int getY()
	{
		return yPos;
	}
}