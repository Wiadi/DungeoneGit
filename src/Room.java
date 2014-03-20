import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Room
{
	private Tile[][][] layout;
	private String file;
	public final static int NUM_TYPES=5;
	private final static int ROOM_3_SQUARE=0;
	private final static int CORRIDOR_HORIZONTAL=1;
	private final static int CORRIDOR_VERTICAL=2;
	private final static int WALL=3;
	private final static int CORRIDOR_INTERSECT=4;
	public Room(int type)
	{
		layout=new Tile[5][5][3];
		switch(type)
		{
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
			default: file="";
					 System.err.println("Invalid room type");
					 break;
		}
		readRoom();
	}
	
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
					case Tile.EMPTY_TILE:	layout[x][y][layer]=new EmptyTile();
											break;
					case Tile.FLOOR_TILE:	layout[x][y][layer]=new FloorTile();
											break;
					case Tile.WALL_TILE:	layout[x][y][layer]=new WallTile();
											break;
					case Tile.OBJECTIVE: 	layout[x][y][layer]=new ObjectiveTile();
											break;
					case Tile.FIGHTER:		layout[x][y][layer]=new Fighter();
											break;
					case Tile.SLIM:			layout[x][y][layer]=new Slim();
											break;
					case Tile.IMP:			layout[x][y][layer]=new Imp();
											break;
					case Tile.KNIGHT:		layout[x][y][layer]=new Knight();
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
}