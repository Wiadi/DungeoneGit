import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
	private int spnX;
	private int spnY;
	public GameMap(int width, int height)
	{
		tiles=new Tile[width][height][3];
		randMap();
//		genMap();
//		dispMap();
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
			tiles[xEnd][yEnd][layer]=tiles[xStart][yStart][layer].changePos(xEnd,yEnd);
			tiles[xStart][yStart][layer]=new EmptyTile(this,xStart,yStart);
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
				tiles[xEnd][yEnd][layer]=new EmptyTile(this,xEnd,yEnd);
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
			Scanner reader=new Scanner(new File("demo_map.txt"));
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
					case Tile.EMPTY_TILE:	tiles[x][y][layer]=new EmptyTile(this,x,y);
											break;
					case Tile.FLOOR_TILE:	tiles[x][y][layer]=new FloorTile(this,x,y);
											break;
					case Tile.WALL_TILE:	tiles[x][y][layer]=new WallTile(this,x,y);
											break;
					case Tile.OBJECTIVE: 	tiles[x][y][layer]=new ObjectiveTile(this,x,y);
											break;
					case Tile.DOOR_TILE:	tiles[x][y][layer]=new DoorTile(this,x,y);
											break;
					case Tile.SPAWN_TILE:	tiles[x][y][layer]=new SpawnTile(this,x,y);
											break;
					case Tile.FIGHTER:		tiles[x][y][layer]=new Fighter(this,x,y);
											break;
					case Tile.SLIM:			tiles[x][y][layer]=new Slim(this,x,y);
											break;
					case Tile.IMP:			tiles[x][y][layer]=new Imp(this,x,y);
											break;
					case Tile.KNIGHT:		tiles[x][y][layer]=new Knight(this,x,y);
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
		Room hold=null;
		boolean spawnSet=false;
		for(int x=0;x<width;x+=Room.WIDTH)
			for(int y=0;y<height;y+=Room.HEIGHT)
			{
				if(!spawnSet && ((x==width-Room.WIDTH && y==height-Room.HEIGHT) || (int)(Math.random()*(width/Room.WIDTH)*(height/Room.HEIGHT))==0))
				{
					System.out.println(spawnSet);
					hold=new Room(this, Room.SPAWN, x, y);
					spawnSet=true;
					spnX=x+(Room.WIDTH-1)/2;
					spnY=y+(Room.HEIGHT-1)/2;
					System.out.println(spnX+" "+spnY);
				}
				else
					hold=new Room(this, (int)(Math.random()*Room.NUM_TYPES)+1, x, y);
//				if(xr>0)
//					while(!hold.checkAdj(rooms[xr-1][yr],3))
//						hold=new Room(this, (int)(Math.random()*Room.NUM_TYPES)+1);
//				if(yr>0)
//					while(!hold.checkAdj(rooms[xr][yr-1],0))
//						hold=new Room(this, (int)(Math.random()*Room.NUM_TYPES)+1);
				for(int xr=0;xr<Room.WIDTH;xr++)
					for(int yr=0;yr<Room.HEIGHT;yr++)
						tiles[x+xr][y+yr]=hold.getLayout()[xr][yr];
				hold=null;
			}
		clearDoors();
	}

	private void clearDoors()
	{
		int open=0;
		for(int i=0;i<tiles.length;i++)
			for(int j=0;j<tiles[0].length;j++)
			{
				if(tiles[i][j][1].getType()==4)
				{
					if(i>0 && tiles[i-1][j][1].getType()==0)
						open++;
					if(j>0 && tiles[i][j-1][1].getType()==0)
						open++;
					if(i<tiles.length-1 && tiles[i+1][j][1].getType()==0)
						open++;
					if(j<tiles[0].length-1 && tiles[i][j+1][1].getType()==0)
						open++;
					if(open<2)
					{
						tiles[i][j][1]=new WallTile(this,i,j);
						tiles[i][j][2]=new WallTile(this,i,j);
					}
				}
				open=0;
			}
	}
	
	public int[] getSize()
	{
		return new int[]{tiles.length,tiles[0].length};
	}
	
	public boolean hasPath(int xs, int ys, int xe, int ye)
	{
		int[] hold;
		int[][] branches=new int[tiles.length][tiles[0].length];
		ArrayList<int[]> branchLocs=new ArrayList<int[]>();
		for(int i=0;i<branches.length;i++)
			for(int j=0;j<branches[0].length;j++)
				branches[i][j]=0;
		ArrayList<Integer> paths=paths(xs,ys);
		int dir=paths.get(0);
		branches[xs][ys]=1;
		branchLocs.add(new int[]{xs,ys});
		switch(paths.size())
		{
			case 1: if(paths.get(0)==dir)
						hold=pathMove(xs,ys,dir);
					else
						hold=branchLocs.get(branchLocs.size()-1);
					break;
			case 2: if(paths.get(0)==-dir)
						dir=paths.get(1);
					else
						dir=paths.get(0);
					hold=pathMove(xs,ys,dir);
					break;
		}
		return false;
	}
	
	public ArrayList<int[]> aStar(int xs, int ys, int xe, int ye)
	{
		ArrayList<int[]> closedSet=new ArrayList<int[]>();
		ArrayList<int[]> openSet=new ArrayList<int[]>();
		openSet.add(new int[]{xs,ys});
		int[][] cameFrom=new int[tiles.length][tiles[0].length];
		int[][] gScore=new int[tiles.length][tiles[0].length];
		gScore[xs][ys]=0;
		int[][] fScore=new int[tiles.length][tiles[0].length];
		fScore[xs][ys]=gScore[xs][ys]+Math.abs(xs-xe)+Math.abs(ys-ye);
		while(openSet.size()>0)
		{
			
		}
		return null;
	}
	
	public ArrayList<Integer> paths(int x,int y)
	{
		ArrayList<Integer> hold=new ArrayList<Integer>();
		{
			if(y>0 && (tiles[x][y-1][2].getType()==0 || tiles[x][y-1][2].getType()==4))
				hold.add(1);
			if(x<tiles.length-1 && y>0 && (tiles[x+1][y-1][2].getType()==0 || tiles[x+1][y-1][2].getType()==4))
				hold.add(2);
			if(x<tiles.length-1 && (tiles[x+1][y][2].getType()==0 || tiles[x+1][y][2].getType()==4))
				hold.add(3);
			if(x<tiles.length-1 && y<tiles[0].length-1 && (tiles[x+1][y+1][2].getType()==0 || tiles[x+1][y+1][2].getType()==4))
				hold.add(4);
			if(y<tiles[0].length-1 && (tiles[x][y+1][2].getType()==0 || tiles[x][y+1][2].getType()==4))
				hold.add(-1);
			if(x>0 && y<tiles[0].length-1 && (tiles[x-1][y+1][2].getType()==0 || tiles[x-1][y+1][2].getType()==4))
				hold.add(-2);
			if(x>0 && (tiles[x-1][y][2].getType()==0 || tiles[x-1][y][2].getType()==4))
				hold.add(-3);
			if(x>0 && y>0 && (tiles[x-1][y-1][2].getType()==0 || tiles[x-1][y-1][2].getType()==4))
				hold.add(-4);
		}
		return hold;
	}
	
	public int[] pathMove(int x, int y, int dir)
	{
		int xe=x;
		int ye=y;
		switch(dir)
		{
			case 2: xe++;
			case 1: ye--;
					break;
			case 4: ye++;
			case 3: xe++;
					break;
			case -2: xe--;
			case -1: ye++;
					 break;
			case -4: ye--;
			case -3: xe--;
					 break;
		}
		if(tiles[xe][ye][2].getType()==0)
		{
			x=xe;
			y=ye;
		}
		return new int[]{x,y};
	}
}