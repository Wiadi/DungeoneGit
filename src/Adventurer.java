/**
 * Adventurers are the Actors controlled by the Dungeonee.
 * @author 941923
 */
public abstract class Adventurer extends Actor
{
	protected int visionRange;
	public Adventurer(GameMap m, int x, int y)
	{
		super(m,x,y);
		tileType=ADVENTURER;
	}
	public boolean canSee(int x, int y)
	{
		if(Math.pow(Math.abs(x-xPos),2)+Math.pow(Math.abs(y-yPos),2)<=Math.pow(visionRange,2))
		{
			for(int i=Math.min(x,xPos)+1;i<Math.max(x,xPos);i++)
				if(map.getTile(i,yPos,2).getType()==2 || (map.getTile(i,yPos,1).getType()==4 && !((DoorTile)(map.getTile(i,yPos,1))).isOpen()))
					return false;
			for(int i=Math.min(y,yPos)+1;i<Math.max(y,yPos);i++)
				if(map.getTile(xPos,i,2).getType()==2 || (map.getTile(xPos,i,1).getType()==4 && !((DoorTile)(map.getTile(xPos,i,1))).isOpen()))
					return false;
			return true;
		}
		return false;
	}
}