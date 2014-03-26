import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Room
{
	private Tile[][][] layout;
	private String file;
	private int type;
	private GameMap map;
	public final static int WIDTH=5;
	public final static int HEIGHT=5;
	public final static int NUM_TYPES=5;
	public final static int SPAWN=0;
	public final static int CORRIDOR_HORIZONTAL=1;
	public final static int CORRIDOR_VERTICAL=2;
	public final static int WALL=3;
	public final static int CORRIDOR_INTERSECT=4;
	public final static int ROOM_3_SQUARE=5;
	public Room(GameMap m,int t)
	{
		layout=new Tile[WIDTH][HEIGHT][3];
		type=t;
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
					case Tile.EMPTY_TILE:	layout[x][y][layer]=new EmptyTile(map,x,y);
											break;
					case Tile.FLOOR_TILE:	layout[x][y][layer]=new FloorTile(map,x,y);
											break;
					case Tile.WALL_TILE:	layout[x][y][layer]=new WallTile(map,x,y);
											break;
					case Tile.OBJECTIVE: 	layout[x][y][layer]=new ObjectiveTile(map,x,y);
											break;
					case Tile.DOOR_TILE:	layout[x][y][layer]=new DoorTile(map,x,y);
											break;
					case Tile.SPAWN_TILE:	layout[x][y][layer]=new SpawnTile(map,x,y);
											break;
					case Tile.FIGHTER:		layout[x][y][layer]=new Fighter(map,x,y);
											break;
					case Tile.SLIM:			layout[x][y][layer]=new Slim(map,x,y);
											break;
					case Tile.IMP:			layout[x][y][layer]=new Imp(map,x,y);
											break;
					case Tile.KNIGHT:		layout[x][y][layer]=new Knight(map,x,y);
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
	
	public boolean checkAdj(Room adj, int face)
	{
		switch(type)
		{
			case SPAWN:
			case ROOM_3_SQUARE: int x,y;
								if(face==0 || face==2)
								{
									y=(HEIGHT-1)*face/2;
										for(x=0;x<WIDTH;x++)
											for(int layer=0;layer<3;layer++)
												if(adj.getLayout()[x][HEIGHT-1-y][layer].getType()==Tile.EMPTY_TILE)
													layout[x][y][layer]=new DoorTile(map,x,y);
								}
								else
								{
									x=(WIDTH-1)*(face-1)/2;
									for(y=0;y<HEIGHT;y++)
										for(int layer=0;layer<3;layer++)
											if(adj.getLayout()[WIDTH-1-x][y][layer].getType()==Tile.EMPTY_TILE)
												layout[x][y][layer]=new DoorTile(map,x,y);
								}
								return true;
			case CORRIDOR_INTERSECT: if(face==0 || face==2)
										for(int layer=0;layer<3;layer++)
											if(adj.getLayout()[WIDTH/2][(HEIGHT-1)*(1-face/2)][layer].getType()==Tile.EMPTY_TILE)
												layout[WIDTH/2][(HEIGHT-1)*face/2][layer]=new DoorTile(map,WIDTH/2,(HEIGHT-1)*face/2);
			
		}
		return false;
	}
	
}