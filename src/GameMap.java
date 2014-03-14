/**
 * Stores all map data for the current floor in three two-dimensional grids and modifies
 * those map data as actions are taken.
 * @author Andrew Simler
 */
public class GameMap
{
	private Tile[][][] tiles;
	private int objX;
	private int objY;
	public GameMap(int width, int height)
	{
		tiles=new Tile[width][height][3];
		genMap();
		for(int i=0;i<width;i++)
			for(int j=0;j<height;j++)
				if(tiles[i][j][1].getType()==Tile.OBJECTIVE_TILE)
				{
					objX=i;
					objY=j;
				}
	}
	/**
	 * Moves a tile from one location to an empty location in the same layer.
	 * @param xStart - x position of the tile to move
	 * @param xEnd - x position of the target location
	 * @param yStart - y position of the tile to move
	 * @param yEnd - y position of the target location
	 * @param layer - layer in which the tile to move is located
	 * @return true if the tile was successfully moved, false if it was blocked
	 */
	public boolean move(int xStart, int xEnd, int yStart, int yEnd, int layer)
	{
		if(tiles[xEnd][yEnd][layer].getType()==Tile.EMPTY_TILE)
		{
			tiles[xEnd][yEnd][layer]=tiles[xStart][yStart][layer];
			tiles[xStart][yStart][layer]=new EmptyTile();
			return true;
		}
		return false;
	}
	/**
	 * Launches an attack by one Actor tile on another Actor tile, deleting the attacked
	 * tile if its health would be reduced to 0 or below.
	 * @param xStart - x position of the attacking tile
	 * @param xEnd - x position of the attacked tile
	 * @param yStart - y position of the attacking tile
	 * @param yEnd - y position of the attacked tile
	 * @param layer - layer in which the tiles are located (always 2, currently)
	 * @return true if the attack is successful, false if either tile is not an Actor
	 */
	public boolean attack(int xStart, int xEnd, int yStart, int yEnd, int layer)
	{
		if(tiles[xStart][yStart][layer].getType()>=Tile.ADVENTURER)
		{
			if((tiles[xStart][yStart][layer].getType()<Tile.MONSTER && tiles[xEnd][yEnd][layer].getType()>=Tile.MONSTER) || (tiles[xStart][yStart][layer].getType()>=Tile.MONSTER && tiles[xEnd][yEnd][layer].getType()<Tile.MONSTER))
				if(((Actor)tiles[xEnd][yEnd][layer]).takeDamage(((Actor)tiles[xStart][yStart][layer]).getCurrAtt())<=0)
					tiles[xEnd][yEnd][layer]=new EmptyTile();
			return true;
		}
		return false;
	}
	/**
	 * Places a specified tile at a location in the map, replacing whatever tile was
	 * previously there.
	 * @param x - x position at which to place the tile
	 * @param y - y position at which to place the tile
	 * @param layer - layer in which to place the tile
	 * @param toPlace - tile to place
	 * @return the tile replaced, or null if there was no tile previously present
	 */
	public Tile placeTile(int x, int y, int layer, Tile toPlace)
	{
		Tile temp=tiles[x][y][layer];
		tiles[x][y][layer]=toPlace;
		return temp;
	}
	/**
	 * Checks whether there is currently an Adventurer standing on the Objective.
	 * @return true if an Adventurer is standing on the Objective, false otherwise
	 */
	public boolean checkObjective()
	{
		return (tiles[objX][objY][2].getType()>=Tile.ADVENTURER && tiles[objX][objY][2].getType()<Tile.MONSTER);
	}
	/**
	 * Generates the starting map layout.
	 */
	public void genMap()
	{
		
	}
}