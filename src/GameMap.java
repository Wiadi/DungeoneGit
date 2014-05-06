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
	private ArrayList<int[]> warpTiles;
	public GameMap(int width, int height)
	{
		tiles=new Tile[width][height][3];
		warpTiles=new ArrayList<int[]>();
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
		if(toPlace.getType()==Tile.SPAWN_TILE)
		{
			spnX=x;
			spnY=y;
		}
		if(toPlace.getType()==Tile.WARP_TILE)
			warpTiles.add(new int[]{x,y});
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
					hold=new Room(this, Room.SPAWN, x, y);
					spawnSet=true;
					spnX=x+(Room.WIDTH-1)/2;
					spnY=y+(Room.HEIGHT-1)/2;
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
		toggleDoors();
		warpConnect();
		toggleDoors();
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
	
	public ArrayList<int[]> aStar(int xs, int ys, int xe, int ye)
	{
		boolean[][] closedSet=new boolean[tiles.length][tiles[0].length];
		boolean[][] openSet=new boolean[tiles.length][tiles[0].length];
		openSet[xs][ys]=true;
		String[][] cameFrom=new String[tiles.length][tiles[0].length];
		int[][] gScore=new int[tiles.length][tiles[0].length];
		gScore[xs][ys]=0;
		int[][] fScore=new int[tiles.length][tiles[0].length];
		fScore[xs][ys]=gScore[xs][ys]+Math.abs(xs-xe)+Math.abs(ys-ye);
		int[] current;
		int tentativeGScore,x,y,currF;
		while(!checkEmpty(openSet))			
		{
			x=0;
			y=0;
			currF=Integer.MAX_VALUE;
			for(int i=0;i<openSet.length;i++)
				for(int j=0;j<openSet[0].length;j++)
					if(openSet[i][j] && fScore[i][j]<=currF)
					{
						x=i;
						y=j;
					}
			current=new int[]{x,y};
			if(current[0]==xe && current[1]==ye)
				return reconstructPath(cameFrom, new int[]{xe,ye});
			openSet[current[0]][current[1]]=false;
			closedSet[current[0]][current[1]]=true;
			for(int[] i:neighborList(current, new ArrayList<int[]>()))
			{
				if(closedSet[i[0]][i[1]])
					continue;
				tentativeGScore=gScore[current[0]][current[1]]+1;
				if(!openSet[i[0]][i[1]] || tentativeGScore<gScore[i[0]][i[1]])
				{
					cameFrom[i[0]][i[1]]=current[0]+", "+current[1];
					gScore[i[0]][i[1]]=tentativeGScore;
					fScore[i[0]][i[1]]=gScore[i[0]][i[1]]+Math.abs(i[0]-xe)+Math.abs(i[1]-ye);
					openSet[i[0]][i[1]]=true;
				}
			}
		}
		return null;
	}

	private ArrayList<int[]> reconstructPath(String[][] cameFrom, int[] loc)
	{
		ArrayList<int[]> path=new ArrayList<int[]>();
		if(cameFrom[loc[0]][loc[1]]!=null)
			path=reconstructPath(cameFrom, parseLoc(cameFrom[loc[0]][loc[1]]));
		path.add(loc);
		return path;
	}
	
	private int[] parseLoc(String loc)
	{
		boolean flip=true;
		String x="",y="";
		for(int i=0;i<loc.length();i++)
		{
			if(loc.charAt(i)>47 && loc.charAt(i)<58)
			{
				if(flip)
					x+=loc.substring(i,i+1);
				else
					y+=loc.substring(i,i+1);
			}
			else
				flip=false;
		}
		return new int[]{Integer.parseInt(x),Integer.parseInt(y)};
	}
	
	private ArrayList<int[]> neighborList(int[] loc, ArrayList<int[]> check)
	{
		ArrayList<int[]> neighbors=new ArrayList<int[]>();
		int x=loc[0], y=loc[1];
		boolean checked;
		if(y>0 && canMoveOver(tiles[x][y-1]))
			neighbors.add(new int[]{x,y-1});
		if(x<tiles.length-1 && y>0 && canMoveOver(tiles[x+1][y-1]))
			neighbors.add(new int[]{x+1,y-1});
		if(x<tiles.length-1 && canMoveOver(tiles[x+1][y]))
			neighbors.add(new int[]{x+1,y});
		if(x<tiles.length-1 && y<tiles[0].length-1 && canMoveOver(tiles[x+1][y+1]))
			neighbors.add(new int[]{x+1,y+1});
		if(y<tiles[0].length-1 && canMoveOver(tiles[x][y+1]))
			neighbors.add(new int[]{x,y+1});
		if(x>0 && y<tiles[0].length-1 && canMoveOver(tiles[x-1][y+1]))
			neighbors.add(new int[]{x-1,y+1});
		if(x>0 && canMoveOver(tiles[x-1][y]))
			neighbors.add(new int[]{x-1,y});
		if(x>0 && y>0 && canMoveOver(tiles[x-1][y-1]))
			neighbors.add(new int[]{x-1,y-1});
//		System.out.println(x+" "+y+" "+(tiles[x][y][1].getType()==Tile.WARP_TILE));
		check.add(loc);
		if(tiles[x][y][1].getType()==Tile.WARP_TILE)
			for(int[] i:warpTiles)
			{
				checked=false;
				for(int[] j:check)
					if(j==i)
						checked=true;
				if(!checked)
					neighbors.addAll(neighborList(i,check));
			}
		return neighbors;
	}
	
	private boolean checkEmpty(boolean[][] set)
	{
		for(int x=0;x<set.length;x++)
			for(int y=0;y<set[0].length;y++)
				if(set[x][y])
					return false;
		return true;
	}
	
	private boolean canMoveOver(Tile[] t)
	{
		return (t[2].getType()==Tile.EMPTY_TILE || (t[1].getType()==Tile.DOOR_TILE && ((DoorTile)t[1]).isOpen()));
	}
	
	public void toggleDoors()
	{
		for(int x=0;x<tiles.length;x++)
			for(int y=0;y<tiles[0].length;y++)
				if(tiles[x][y][1].getType()==Tile.DOOR_TILE)
					((DoorTile)tiles[x][y][1]).toggleOpen();
	}
	
	private void warpConnect()
	{
		int x=spnX,y=spnY;
		int[] telR,telU;
		ArrayList<int[]> reach;
		ArrayList<int[]> unreach;
		boolean[][] ur=listUnreach(x,y);
		boolean reachPlaced=false;
		while(!checkEmpty(ur))
		{
			unreach=new ArrayList<int[]>();
			reach=new ArrayList<int[]>();
			for(int i=0;i<ur.length;i++)
				for(int j=0;j<ur[0].length;j++)
				{
					if(ur[i][j])
						unreach.add(new int[]{i,j});
					else if(tiles[i][j][1].getType()==Tile.EMPTY_TILE)
						reach.add(new int[]{i,j});
				}
			telR=reach.get((int)(Math.random()*reach.size()));
			telU=unreach.get((int)(Math.random()*unreach.size()));
			if(!reachPlaced)
			{
				placeTile(telR[0], telR[1], 1, new WarpTile(this, telR[0],telR[1]));
				reachPlaced=true;
			}
			placeTile(telU[0], telU[1], 1, new WarpTile(this, telU[0],telU[1]));
			for(int i=0;i<ur.length;i++)
			{
				for(int j=0;j<ur[0].length;j++)
					System.out.print(ur[i][j]+"\t");
				System.out.println();
			}
			System.out.println();
			ur=listUnreach(x,y);
		}
	}
	
	private boolean[][] listUnreach(int x, int y)
	{
		boolean[][] unreach=new boolean[tiles.length][tiles[0].length];
		for(int i=0;i<tiles.length;i++)
			for(int j=0;j<tiles[0].length;j++)
			{
				if(aStar(x,y,i,j)==null && tiles[i][j][1].getType()==Tile.EMPTY_TILE)
					unreach[i][j]=true;
				else
					unreach[i][j]=false;
			}
		return unreach;
	}
}
