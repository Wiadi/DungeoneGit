/**
 * Monsters are the Actors controlled and placed by the Dungeoneer.
 * @author Andrew Simler
 */
public abstract class Monster extends Actor
{
	public Monster(GameMap m, int x, int y)
	{
		super(m,x,y);
		tileType=MONSTER;
	}
}