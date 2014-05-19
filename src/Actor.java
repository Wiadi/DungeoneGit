/**
 * Actors are tiles which can act - the monsters populating the map and the adventurers
 * exploring it.
 * @author Andrew Simler
 */
public abstract class Actor extends Tile
{
	protected int baseAttack;
	protected int currentAttack;
	protected int attackRange;
	protected boolean[] abilities;
	protected int baseHealth;
	protected int currentHealth;
	protected double moveRange;
	protected int regenRate;
	private final static int NUM_ABILS=2;
	public final static int BALE_TELE=0;
	public final static int REVIVE=1;
	public Actor(GameMap m, int x, int y)
	{
		super(m,x,y);
		abilities=new boolean[NUM_ABILS];
		for(int i=0;i<NUM_ABILS;i++)
			abilities[i]=false;
	}
	public int getBaseAtt(){
		return baseAttack;
	}
	public int getCurrAtt(){
		return currentAttack;
	}
	public int getAttRange(){
		return attackRange;
	}
	public int getBaseHP(){
		return baseHealth;
	}
	public int getCurrHP(){
		return currentHealth;
	}
	public double getMoveRange(){
		return moveRange;
	}
	/**
	 * Checks whether an Actor can move to a given relative location.
	 * @param xRel - x position of the target location relative to the Actor
	 * @param yRel - y position of the target location relative to the Actor
	 * @return true if the Actor can move to the target location, false otherwise
	 */
	public boolean canMoveTo(int x, int y)
	{
		if(Math.abs(x-xPos)<=moveRange && Math.abs(y-yPos)<=moveRange)
			return true;
		return false;
	}
	/**
	 * Checks whether an Actor can attack a given relative location.
	 * @param xRel - x position of the target location relative to the Actor
	 * @param yRel - y position of the target location relative to the Actor
	 * @return true if the Actor can attack the target location, false otherwise
	 */
	public boolean canAttack(int x, int y)
	{
		if((Math.abs(x-xPos)<=attackRange && Math.abs(y-yPos)<=attackRange) || (map.getTile(xPos, yPos, 1).getType()==Tile.WARP_TILE && map.getTile(x, y, 1).getType()==Tile.WARP_TILE))
			return true;
		return false;
	}
	/**
	 * Checks whether an Actor has a given ability.
	 * @param abilIndex - index in the ability array of the ability to check
	 * @return true if the Actor has the ability, false otherwise
	 */
	public boolean hasAbility(int abilIndex)
	{
		return abilities[abilIndex];
	}
	/**
	 * Deals a given amount of damage to an Actor.
	 * @param damage - amount of damage to be dealt
	 * @return health the Actor has remaining after taking damage
	 */
	public int takeDamage(int damage)
	{
		currentHealth-=damage;
		return currentHealth;
	}
	/**
	 * Restores a certain amount of health to an Actor based on its rate of regeneration
	 */
	public void regen()
	{
		currentHealth=Math.min(baseHealth, currentHealth+regenRate);
	}
}