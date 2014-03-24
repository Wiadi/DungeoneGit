import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
/**
 * Stores all map data for the current floor in three two-dimensional grids and modifies
 * those map data as actions are taken.
 * @author 941923
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
		dispMap();
		for(int i=0;i<width;i++)
			for(int j=0;j<height;j++)
				if(tiles[i][j][1].getType()==Tile.OBJECTIVE)
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
	public boolean move(int xStart, int yStart, int xEnd, int yEnd, int layer)
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
	public boolean attack(int xStart, int yStart, int xEnd, int yEnd, int layer)
	{
		if(tiles[xStart][yStart][layer].getType()>=Tile.ADVENTURER && tiles[xEnd][yEnd][layer].getType()>=Tile.ADVENTURER)
		{
			if(((Actor)tiles[xEnd][yEnd][layer]).takeDamage(((Actor)tiles[xStart][yStart][layer]).getCurrAtt())<=0)
			{
				tiles[xEnd][yEnd][layer]=new EmptyTile();
				return true;
			}
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
		if(toPlace.getType()==Tile.OBJECTIVE)
		{
			objX=x;
			objY=y;
		}
		Tile temp=tiles[x][y][layer];
		tiles[x][y][layer]=toPlace;
		return temp;
	}
	/**
	 * Returns the tile at a given location on the map.
	 * @param x - x position of the tile to get
	 * @param y - y position of the tile to get
	 * @param layer - layer in which the tile to get is located
	 * @return the tile at the requested location
	 */
	public Tile getTile(int x, int y, int layer)
	{
		return tiles[x][y][layer];
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
		try
		{
			Scanner reader=new Scanner(new File("map.txt"));
			int layer=0;
			int x=-1;
			int y=0;
			while(layer<3)
			{
				x++;
				switch(reader.nextInt())
				{
					case -2:				layer++;
											x=-1;
											y=0;
											break;
					case -1:				x=-1;
											y++;
											break;
					case Tile.EMPTY_TILE:	tiles[x][y][layer]=new EmptyTile();
											break;
					case Tile.FLOOR_TILE:	tiles[x][y][layer]=new FloorTile();
											break;
					case Tile.WALL_TILE:	tiles[x][y][layer]=new WallTile();
											break;
					case Tile.OBJECTIVE: 	tiles[x][y][layer]=new ObjectiveTile();
											break;
					case Tile.DOOR_TILE:	tiles[x][y][layer]=new DoorTile();
											break;
					case Tile.SPAWN_TILE:	tiles[x][y][layer]=new SpawnTile();
											break;
					case Tile.FIGHTER:		tiles[x][y][layer]=new Fighter();
											break;
					case Tile.SLIM:			tiles[x][y][layer]=new Slim();
											break;
					case Tile.IMP:			tiles[x][y][layer]=new Imp();
											break;
					case Tile.KNIGHT:		tiles[x][y][layer]=new Knight();
											break;
					default:				System.err.println("Invalid tile type found in map data.");
											break;
				}
			}
			reader.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Error - map file not found");
		}
	}
	/**
	 * Temporary "tester" method to check whether various map methods work properly.
	 */
	private void dispMap()
	{
		for(int x=0;x<tiles.length;x++)
		{
			for(int y=0;y<tiles[x].length;y++)
			{
				for(int l=0;l<tiles[x][y].length;l++)
					System.out.print(tiles[x][y][l].getType()+",");
				System.out.print(" ");
			}
			System.out.println();
		}
	}
	/**
	 * Teleports the target to a random valid location on the map.
	 * @param xStart - x location of the Actor causing the teleportation
	 * @param yStart - y location of the Actor causing the teleportation
	 * @param xEnd - x location of the target to be teleported
	 * @param yEnd - y location of the target to be teleported
	 * @param layer - layer in which the Actor and target are located
	 * @return true if (xStart, yStart) refers to the location of an Actor with Baleful Teleport, false else
	 */
	public boolean baleTele(int xStart, int yStart, int xEnd, int yEnd, int layer)
	{
		if(tiles[xStart][yStart][layer].getType()>=Tile.ADVENTURER)
			if(((Actor)tiles[xStart][yStart][layer]).hasAbility(Actor.BALE_TELE))
			{
				int x=(int)(Math.random()*tiles.length);
				while(!move(xEnd,yEnd,x,(int)(Math.random()*tiles[x].length),layer));
				return true;
			}
		return false;
	}
	
	private void randMap()
	{
		int width=tiles.length;
		int height=tiles[0].length;
		int type=0;
		Room[][] rooms=new Room[width/Room.WIDTH][height/Room.HEIGHT];
		Room hold=null;
		boolean spawnSet=false;
		for(int xr=0;xr<rooms.length;xr++)
			for(int yr=0;yr<rooms[0].length;yr++)
			{
				if(!spawnSet)
					if((xr==rooms.length-1 && yr==rooms[0].length-1) || (int)(Math.random()*rooms.length*rooms[0].length)==0)
					{
						hold=new Room(Room.SPAWN);
						spawnSet=true;
					}
				else
					hold=new Room((int)(Math.random()*Room.NUM_TYPES)+1);
				if(xr>0)
					while(!hold.checkAdj(rooms[xr-1][yr],3))
						hold=new Room((int)(Math.random()*Room.NUM_TYPES)+1);
				if(yr>0)
					while(!hold.checkAdj(rooms[xr][yr-1],0))
						hold=new Room((int)(Math.random()*Room.NUM_TYPES)+1);
			}
	}
}