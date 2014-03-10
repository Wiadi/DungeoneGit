
public class GameMap
{
	private Tile[][][] tiles;
	
	public GameMap(int width, int height)
	{
		tiles=new Tile[width][height][3];
	}
	
	public boolean move(int xStart, int xEnd, int yStart, int yEnd, int layer)
	{
		if(tiles[xEnd][yEnd][layer].getType()==0)
		{
			tiles[xEnd][yEnd][layer]=tiles[xStart][yStart][layer];
			tiles[xStart][yStart][layer]=new EmptyTile();
			return true;
		}
		return false;
	}
	
	public boolean attack(int xStart, int xEnd, int yStart, int yEnd, int layer)
	{
		if(tiles[xStart][yStart][layer].getType()>=100)
		{
			if((tiles[xStart][yStart][layer].getType()<200 && tiles[xEnd][yEnd][layer].getType()>=200) || (tiles[xStart][yStart][layer].getType()>=200 && tiles[xEnd][yEnd][layer].getType()<200))
				if(((Actor)tiles[xEnd][yEnd][layer]).takeDamage(((Actor)tiles[xStart][yStart][layer]).getCurrAtt())<=0)
					tiles[xEnd][yEnd][layer]=new EmptyTile();
			return true;
		}
		return false;
	}
	
	public Tile placeTile(int x, int y, int layer, Tile toPlace)
	{
		Tile temp=tiles[x][y][layer];
		tiles[x][y][layer]=toPlace;
		return temp;
	}
}