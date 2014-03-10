
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
	
	public abstract boolean canMoveTo(int xRel, int yRel);

	public abstract boolean canAttack(int xRel, int yRel);
	
	public boolean hasAbility(int abilIndex)
	{
		return abilities[abilIndex];
	}
	
	public int getCurrAtt()
	{
		return currentAttack;
	}
	
	public int takeDamage(int damage)
	{
		currentHealth-=damage;
		return currentHealth;
	}
}