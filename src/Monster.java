/**
 * Monsters are the Actors controlled and placed by the Dungeoneer.
 * @author 941923
 */
public abstract class Monster extends Actor
{
	public Monster(GameMap m, int x, int y)
	{
		super(m,x,y);
		tileType=MONSTER;
	}
}