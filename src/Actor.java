/**
 * Actors are tiles which can act - the monsters populating the map and the adventurers
 * exploring it.
 * @author 941923
 */
public abstract class Actor extends Tile
{
	protected int baseAttack;
	protected int currentAttack;
	protected boolean[] abilities;
	protected int baseHealth;
	protected int currentHealth;
	public Actor()
	{
		super();
		abilities=new boolean[0];
		tileType+=1;
	}
	/**
	 * Checks whether an Actor can move to a given relative location.
	 * @param xRel - x position of the target location relative to the Actor
	 * @param yRel - y position of the target location relative to the Actor
	 * @return true if the Actor can move to the target location, false otherwise
	 */
	public abstract boolean canMoveTo(int xRel, int yRel);
	/**
	 * Checks whether an Actor can attack a given relative location.
	 * @param xRel - x position of the target location relative to the Actor
	 * @param yRel - y position of the target location relative to the Actor
	 * @return true if the Actor can attack the target location, false otherwise
	 */
	public abstract boolean canAttack(int xRel, int yRel);
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