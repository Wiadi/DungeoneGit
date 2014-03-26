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
	/**
	 * Checks whether an Adventurer can see a given relative location.
	 * @param xRel - x position of the target location relative to the Actor
	 * @param yRel - y position of the target location relative to the Actor
	 * @return true if the Adventurer can see the target location, false otherwise
	 */
	public boolean canSee(int x, int y)
	{
		if(Math.pow(Math.abs(x-xPos),2)+Math.pow(Math.abs(y-yPos),2)<=Math.pow(visionRange,2))
		{
			for(int i=Math.min(x,xPos)+1;i<Math.max(x,xPos);i++)
				if(map.getTile(i,yPos,3).getType()==2 || (map.getTile(i,yPos,3).getType()==4 && !((DoorTile)(map.getTile(i,yPos,3))).isOpen()))
					return false;
			for(int i=Math.min(y,yPos)+1;i<Math.max(y,yPos);i++)
				if(map.getTile(xPos,i,3).getType()==2 || (map.getTile(xPos,i,3).getType()==4 && !((DoorTile)(map.getTile(xPos,i,3))).isOpen()))
					return false;
			return true;
		}
		return false;
	}
}