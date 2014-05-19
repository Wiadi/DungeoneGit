import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
/**
 * This class is used to generate the 5x5 "rooms" which are assembled into random maps.
 * @author Andrew Simler
 */
public class Room
{
	private Tile[][][] layout;
	private String file;
	private int type, ulx, uly;
	private GameMap map;
	public final static int WIDTH=5;
	public final static int HEIGHT=5;
	public final static int NUM_TYPES=7;
	public final static int SPAWN=0;
	public final static int CORRIDOR_HORIZONTAL=1;
	public final static int CORRIDOR_VERTICAL=2;
	public final static int CORRIDOR_INTERSECT=3;
	public final static int ROOM_3_SQUARE=4;
	public final static int WALL=5;
	public final static int DOUBLE_CORRIDOR_HORIZONTAL=6;
	public final static int ROOM_5_SQUARE=7;
	public Room(GameMap m,int t, int x, int y)
	{
		layout=new Tile[WIDTH][HEIGHT][3];
		type=t;
		ulx=x;
		uly=y;
		map=m;
		switch(type)
		{
			case SPAWN: file="spwn.txt";
						break;
			case ROOM_3_SQUARE: file="r3sq.txt";
								break;
			case CORRIDOR_HORIZONTAL: file="chor.txt";
								      break;
			case CORRIDOR_VERTICAL: file="cver.txt";
									break;
			case WALL: file="wall.txt";
					   break;
			case CORRIDOR_INTERSECT: file="cx.txt";
									 break;
			case DOUBLE_CORRIDOR_HORIZONTAL: file="2chor.txt";
											 break;
			case ROOM_5_SQUARE: file="r5sq.txt";
								break;
			default: file="";
					 System.err.println("Invalid room type");
					 break;
		}
		readRoom();
	}
	/**
	 * Reads the data for a given room from a text file corresponding to the room type.
	 */
	private void readRoom()
	{
		try
		{
			Scanner reader=new Scanner(new File(file));
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
					case Tile.EMPTY_TILE:	layout[x][y][layer]=new EmptyTile(map,x+ulx,y+uly);
											break;
					case Tile.FLOOR_TILE:	layout[x][y][layer]=new FloorTile(map,x+ulx,y+uly);
											break;
					case Tile.WALL_TILE:	layout[x][y][layer]=new WallTile(map,x+ulx,y+uly);
											break;
					case Tile.OBJECTIVE: 	layout[x][y][layer]=new ObjectiveTile(map,x+ulx,y+uly);
											break;
					case Tile.DOOR_TILE:	layout[x][y][layer]=new DoorTile(map,x+ulx,y+uly);
											break;
					case Tile.SPAWN_TILE:	layout[x][y][layer]=new SpawnTile(map,x+ulx,y+uly);
											break;
					case Tile.FIGHTER:		layout[x][y][layer]=new Fighter(map,x+ulx,y+uly);
											break;
					case Tile.SLIM:			layout[x][y][layer]=new Slim(map,x+ulx,y+uly);
											break;
					case Tile.IMP:			layout[x][y][layer]=new Imp(map,x+ulx,y+uly);
											break;
					case Tile.KNIGHT:		layout[x][y][layer]=new Knight(map,x+ulx,y+uly);
											break;
				}
			}
			reader.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Error - room file not found");
		}
	}
	public Tile[][][] getLayout()
	{
		return layout;
	}
	public int getType()
	{
		return type;
	}
}