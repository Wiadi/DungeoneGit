/**
 * Actors are tiles which can act - the monsters populating the map and the adventurers
 * exploring it.
 * @author 941923
 */
public abstract class Actor extends Tile
{
	protected int baseAttack;
	protected int currentAttack;
	protected int attackRange;
	protected boolean[] abilities;
	protected int baseHealth;
	protected int currentHealth;
	protected int moveRange;
	private final static int NUM_ABILS=1;
	public final static int BALE_TELE=0;
	public Actor(GameMap m, int x, int y)
	{
		super(m,x,y);
		abilities=new boolean[NUM_ABILS];
		for(int i=0;i<NUM_ABILS;i++)
			abilities[i]=false;
	}
	/**
	 * Checks whether an Actor can move to a given relative location.
	 * @param xRel - x position of the target location relative to the Actor
	 * @param yRel - y position of the target location relative to the Actor
	 * @return true if the Actor can move to the target location, false otherwise
	 */
	public boolean canMoveTo(int xRel, int yRel)
	{
		if(Math.abs(xRel)<=moveRange && Math.abs(yRel)<=moveRange)
			return true;
		return false;
	}
	/**
	 * Checks whether an Actor can attack a given relative location.
	 * @param xRel - x position of the target location relative to the Actor
	 * @param yRel - y position of the target location relative to the Actor
	 * @return true if the Actor can attack the target location, false otherwise
	 */
	public boolean canAttack(int xRel, int yRel)
	{
		if(Math.abs(xRel)<=attackRange && Math.abs(yRel)<=attackRange)
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
	public int getCurrAtt()
	{
		return currentAttack;
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
}