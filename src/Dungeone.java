import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Maintains a GameMap and associated Actors
 * Interprets user input to provide game control
 * Controls output display
 * @author Jacob Ryder
 */
@SuppressWarnings("serial")
public class Dungeone extends Canvas{
	private GameMap map;
	private ArrayList<Adventurer> party;
	private ArrayList<Adventurer> dead;
	private ArrayList<Monster> mobs;
	private int turn;	//0-Dungeonee, 1-Dungeoneer
	private int[] action;	//0-Dungeonee, 1-Dungeoneer
	private int[] select; //mobile
	private int[] pick; //fixed
	private int state; //0-setup, 1-standard, 2-switch
	private boolean cont;
	private boolean end;
	private Graphics g;
	private BufferedImage buff;
	private KeyEvent event;
	private MouseEvent event2;
	private final static int WIDTH=50;
	private final static int HEIGHT=40;
	
	private int count;
	
	public static void main(String[] args){
		Frame frame = new Frame();
		Dungeone game = new Dungeone();
		frame.add(game);
		frame.setTitle("Dungeone");
		frame.setSize(1200, 800);
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		frame.setVisible(true);
		
		game.init();
		game.run();
	}
	
	public Dungeone() {
		super();
		KeyAdapter key = new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e) {
				event = e;
			}
		};
		MouseAdapter mouse = new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				event2 = e;
			}
		};
		addKeyListener(key);
		addMouseListener(mouse);
	}
	
	
	/**
	 * Initializes GameMap, related objects, and graphical output
	 */
	public void init(){
		g = this.getGraphics();
		buff = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		update(); //load screen
		
		map = new GameMap(WIDTH,HEIGHT);	//50x35 for aesthetics
		party = new ArrayList<Adventurer>();
		dead = new ArrayList<Adventurer>();
		mobs = new ArrayList<Monster>();
//		map.placeTile(2, 2, 1, new SpawnTile(map, 2, 2));
//		map.placeTile(20, 19, 1, new ObjectiveTile(map, 20, 19));
		turn = 0;
		action = new int[]{7, 5};
		select = new int[]{0,0};
		pick  = new int[]{-1,-1};
		state = 0;
		count = 0;
		cont = true;
		
		event = null;
		event2 = null;
		}

	/**
	 * Alternates turns until one of two end conditions are met:
	 * The Dungeoneer wins if all adventurers are removed from the map
	 * The Dungeonee wins if an adventurer reaches the objective
	 */
	public void run(){
		update();
		while(state == 0 || !(map.checkObjective() || party.isEmpty())){ //no end during setup
			turn();
		}
		state = 1;
		turn = 1;
		update(); //visibility at end
	}
	
	/**
	 * Interprets a turn for either the Dungeoneer or Dungeonee
	 * Uses KeyEvents to control actions, each expending Action Points
	 * Turn ends when the player passes or no Action Points are left
	 */
	public void turn(){
		select = new int[]{0,0};
		pick = new int[]{-1,-1};
		action[turn] += 5;
		if (action[turn] > 20)
			action[turn] = 20;
		if(turn == 0)
			for(Adventurer a: party)
				a.regen();
		if(turn == 1)
			for(Monster m: mobs)
				m.regen();
		update();
		
		cont = true;
		end = false;
		while(!end){ //pass conditions, end conditions
			try {Thread.sleep(1l);} //stuff gets weird without this
			catch (InterruptedException e) {}
			if(event != null || event2 != null){
				if (state == 2)
					state = 1;
				else if (state != 2){
				if(event!= null){
				switch(event.getKeyChar()){
				//wasd to control select
				case 'w':
					select[1]--;
					if (select[1] < 0)
						select[1] = HEIGHT - 1;
					break;
				case 's':
					select[1]++;
					if (select[1] > HEIGHT - 1)
						select[1] = 0;
					break;
				case 'a':
					select[0]--;
					if (select[0] < 0)
						select[0] = WIDTH - 1;
					break;
				case 'd':
					select[0]++;
					if (select[0] > WIDTH - 1)
						select[0] = 0;
					break;
				//space to pick a tile
				case ' ':
					if(pick[0] == -1 && pick[1] == -1){
						pick[0] = select[0];
						pick[1] = select[1];
					}
					else{
						pick[0] = -1;
						pick[1] = -1;
					}
					break;
//				case 't':
//					if(select[0] != -1 && select[1] != -1)
//					if(pick[0] != -1 && pick[1] != -1){
//						ArrayList<int[]> path = map.aStar(select[0], select[1], pick[0], pick[1]);
//						if(path == null)
//							System.out.println("Andy was right");
//						else{
//						for(int[] point: path){
//							System.out.println(point[0] + ", " + point[1]);
//						}
//						}
//					}
//					break;
				//z to move from pick to select
				case 'z':
					if(cont)
						move();
					break;
				//x to have pick attack select 
				case 'x':
					if(cont)
						attack();
					break;
				//c to place a unit	on select
				case 'c':
					if(cont)
						create();
					break;
				//r or p to pass turn	
				case 'r':
				case 'p':	
					if(state == 0 || state == 1){ //maybe change
						if(cont)
							cont = false;
						else
							end = true;
					}	
					break;
				default:
					switch(event.getKeyCode()){
					//arrows to also control movement
					case KeyEvent.VK_UP:
						select[1]--;
						if (select[1] < 0)
							select[1] = HEIGHT - 1;
						break;
					case KeyEvent.VK_DOWN:
						select[1]++;
						if (select[1] > HEIGHT - 1)
							select[1] = 0;
						break;
					case KeyEvent.VK_LEFT:
						select[0]--;
						if (select[0] < 0)
							select[0] = WIDTH - 1;
						break;
					case KeyEvent.VK_RIGHT:
						select[0]++;
						if (select[0] > WIDTH - 1)
							select[0] = 0;
						break;
					}
				} //switch(event
				} //if event != null
				else if(event2 != null){ //do things based on keyEvent
					int x = event2.getX();
					int y = event2.getY();
					if(event2.getButton() == MouseEvent.BUTTON1){
						if((x > 100 && x < 100 + 15*WIDTH) && (y > 40 && y < 40 + 15*HEIGHT)){
							x = (x - 100)/15;
							y = (y - 40)/15;
							select[0] = x;
							select[1] = y;
							if(cont){
							if(pick[0] == x && pick[1] == y){
								//System.out.println("Create");
								create();
							}
							}
							pick[0] = x;
							pick[1] = y;
						}
					} //if(m1
					if(event2.getButton() == MouseEvent.BUTTON3){ //right click is m3
						if((x > 100 && x < 100 + 15*WIDTH) && (y > 40 && y < 40 + 15*HEIGHT)){
							x = (x - 100)/15;
							y = (y - 40)/15;
							select[0] = x;
							select[1] = y;
							//pretests?
							if(cont){
							if(map.getTile(select[0], select[1], 2).getType() < Tile.ADVENTURER){
								if(map.getTile(select[0], select[1], 1).getType() != Tile.DOOR_TILE || ((DoorTile) map.getTile(select[0], select[1], 1)).isOpen()){
									//System.out.println("Move");
									move();
								}
								else{
									//System.out.println("Door");
									attack();
								}
							}
							else if(map.getTile(select[0], select[1], 2).getType() >= Tile.ADVENTURER){
								//System.out.println("Attack");
								attack();
							}
							}
						}
					}//if(m3
					if(event2.getButton() == MouseEvent.BUTTON2){ //middle is m2
						if(state == 0 || state == 1){ //maybe change
							boolean goal = false;
							if(turn == 0)
								goal = true;
							else{
							for(int i = 0; i < WIDTH; i++)
								for(int j = 0; j < HEIGHT; j++)
									if(map.getTile(i, j, 1).getType() == Tile.OBJECTIVE)
										goal = true;
							}
							if(goal){
							if(cont)
								cont = false;
							else
								end = true;
							}
						}
					} //m2
					//System.out.println("s: " + select[0] + ", " + select[1]);
					//System.out.println("p: " + pick[0] + ", " + pick[1]);
					//System.out.println(event2.getButton());
				} //if(event2!=null
				} //if(state!= 2
				event = null;
				event2 = null;
				
				
				if(action[turn] <=0)
					cont = false;
				if(state != 0 && (map.checkObjective() || party.isEmpty())){
					cont = false;
				}
				update();
			} //if(event||event2 != null
		}//while(cont
		
		if(state == 0 || state == 1)
			state = 2;
		else if(state == 2)
			state = 1;
		turn = (turn+1)%2;
		update();
	}
	
	/**
	 * Attempts to move an actor from the picked tile to the selected tile
	 */
	public void move(){
		if(select[0] != -1 && select[1] != -1){
		if(pick[0] != -1 && pick[1] != -1){
			if(select[0] != pick[0] || select[1] != pick[1]){
				if(map.getTile(pick[0], pick[1], 2).getType() >= Tile.ADVENTURER) //200 mob
				if(map.getTile(select[0], select[1], 2).getType() == Tile.EMPTY_TILE)
				if(map.getTile(select[0], select[1], 1).getType() != Tile.DOOR_TILE || (map.getTile(select[0], select[1], 1).getType() == Tile.DOOR_TILE && ((DoorTile) map.getTile(select[0], select[1], 1)).isOpen())){
					Actor picked = (Actor) map.getTile(pick[0], pick[1], 2);
					if((turn == 0 && state != 0 && picked.getType() < Tile.MONSTER) || (turn == 1 && picked.getType() >= Tile.MONSTER)){
						//if(picked.canMove(select[0], select[1])
						ArrayList<int[]> path = map.aStar(pick[0], pick[1], select[0], select[1]);
//						if(path == null)
//							System.out.println("No path");
//						else
//						for(int[] point: path){
//							System.out.println(point[0] + ", " + point[1]);
//						}
						if(path != null && (path.size() - 1) <= action[turn]*picked.getMoveRange()){
							map.move(pick[0], pick[1], select[0], select[1], 2);
							pick[0] = select[0];
							pick[1] = select[1];
							action[turn] -= (path.size() - 1)/picked.getMoveRange();
						}
					}
				}
			}
		}
		}
	}
	
	/**
	 * Attempts to attack the selected actor with the picked actor
	 */
	public void attack(){
		if(select[0] != -1 && select[1] != -1){
		if(pick[0] != -1 && pick[1] != -1){
			if(select[0] != pick[0] || select[1] != pick[1]){
				if(map.getTile(pick[0], pick[1], 2).getType() >= Tile.ADVENTURER){
					if(map.getTile(select[0], select[1], 2).getType() >= Tile.ADVENTURER){
						Actor picked = (Actor) map.getTile(pick[0], pick[1], 2);
						Actor selected = (Actor) map.getTile(select[0], select[1], 2);
						if((turn == 0 && state != 0 && picked.getType() < Tile.MONSTER) || (turn == 1 && picked.getType() >= Tile.MONSTER))
							if(picked.canAttack(select[0], select[1])){
								if(map.attack(pick[0], pick[1], select[0], select[1], 2)) //tests if dead after
									if(selected.getType() >= Tile.MONSTER)
										mobs.remove(selected);
									else{
										party.remove(selected);
										dead.add((Adventurer)selected);
									}
								action[turn]--;
							}
					}
					else if(map.getTile(select[0], select[1], 1).getType() == Tile.DOOR_TILE){
						Actor picked = (Actor) map.getTile(pick[0], pick[1], 2);
						DoorTile selected = (DoorTile) map.getTile(select[0], select[1], 1);
						if((turn == 0 && picked.getType() < Tile.MONSTER) || (turn == 1 && picked.getType() >= Tile.MONSTER))
							if(picked.canAttack(select[0], select[1])){
								selected.toggleOpen();
								action[turn]--;
							}
					}
				}
			}
		}
		}
	}
	
	/**
	 * Attempts to create an actor at the selected tile
	 */
	public void create(){
	if(turn == 0){
		if(state == 0){
			if(select[0] != -1 && select[1] != -1){
			if(map.getTile(select[0], select[1], 2).getType() == Tile.EMPTY_TILE){
				int x = -1, y = -1;
				for(int i = 0; i < map.getSize()[0]; i++)
					for(int j = 0; j < map.getSize()[1]; j++)
						if(map.getTile(i, j, 1).tileType == Tile.SPAWN_TILE){
							x = i;
							y = j;
						}
				if(Math.abs(select[0] - x) <= 1 && Math.abs(select[1] - y) <= 1){
					Fighter swrd = new Fighter(map, select[0], select[1]);
					party.add(swrd);
					//System.out.println(select[0]+" "+select[1]);
					map.placeTile(select[0], select[1], 2, swrd);
					action[turn]-=3;
				}
			}
		}
		}
	}
	if(turn == 1){ //needs to be able to switch mob type
		if(!(select[0] == -1 && select[1] == -1)){
			boolean goal = false;
//			SpawnTile spawn = null;
			for(int i = 0; i < WIDTH; i++)
				for(int j = 0; j < HEIGHT; j++)
					if(map.getTile(i, j, 1).getType() == Tile.OBJECTIVE)
						goal = true;
			if(!goal){
//				SpawnTile spawn = null;
//				for(int i = 0; i < map.getSize()[0]; i++)
//					for(int j = 0; j < map.getSize()[1]; j++)
//						if(map.getTile(i, j, 1).tileType == Tile.SPAWN_TILE)
//							spawn = (SpawnTile) map.getTile(i, j, 1);
//				if(spawn != null && map.getTile(select[0], select[1], 1).getType() == Tile.EMPTY_TILE){
//					map.toggleDoors();
//					if(map.aStar(spawn.getX(), spawn.getY(), select[0], select[1]) != null){
					if(!vision(select[0], select[1])){
						ObjectiveTile obj = new ObjectiveTile(map, select[0], select[1]);
						map.placeTile(select[0], select[1], 1, obj);
					}
//					}
//					map.toggleDoors();
//				}
			}
			if(goal){
			if(map.getTile(select[0], select[1], 2).getType() == Tile.EMPTY_TILE){
//				boolean hidden = true;
//				for(Adventurer a: party)
//					if(a.canSee(select[0], select[1]))
//						hidden = false;
////				SpawnTile spawn= null;
//				for(int m = 0; m < WIDTH; m++)
//					for(int n = 0; n < HEIGHT; n++)
//						if(map.getTile(m, n, 1).tileType == Tile.SPAWN_TILE)
//							spawn = (SpawnTile) map.getTile(m, n, 1);
//				if(spawn.canSee(select[0], select[1]))
//					hidden = false;
//				if(hidden){
				if(!vision(select[0], select[1])){
					Slim slim = new Slim(map, select[0], select[1]);
					mobs.add(slim);
					map.placeTile(select[0], select[1], 2, slim);
					action[turn]-=5;
				}
//				}
//			}
			}
		}
	}
	}
	}
	
	/**
	 * Checks if any Adventurer tile or Spawn tile can see a given tile
	 * @param x - x location of the tile being checked
	 * @param y - y location of the tile being checked
	 * @return boolean whether the tile is seen 
	 */
	public boolean vision(int x, int y){
		boolean seen = false;
//		ArrayList<int[]> path;
		SpawnTile spawn= null;
		for(int i = 0; i < WIDTH; i++)
			for(int j = 0; j < HEIGHT; j++)
				if(map.getTile(i, j, 1).tileType == Tile.SPAWN_TILE)
					spawn = (SpawnTile) map.getTile(i, j, 1);
		if(spawn!= null){
//			path = map.aStar(spawn.getX(), spawn.getY(), x, y);
//			if(path != null && path.size() - 1 <= 5)
			if(spawn.canSee(x, y))
				seen = true;
		}
		for(int i = 0; i < WIDTH; i++)
			for(int j = 0; j < HEIGHT; j++)
				if(map.getTile(i, j, 1).tileType == Tile.WARP_TILE)
					if(((WarpTile)(map.getTile(i, j, 1))).canSee(x, y))
							seen = true;
		for(Adventurer a: party){
//			path = map.aStar(a.getX(), a.getY(), x, y);
//			if(path != null && path.size() - 1 <= a.getVisRange())
			if(a.canSee(x, y))
				seen = true;
		}
		return seen;
	}
	
	/**
	 * Displays buffered image
	 * @param g - Graphics object created by Canvas
	 */
	public void paint(Graphics g){
		if(buff != null){
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage(buff, 0, 0, getWidth(), getHeight(), null);
		}
		else
			System.out.println("No Image");
	}
	
	/**
	 * Buffers and displays a new frame
	 */
	public void update(){
		buffer();
		paint(g);
//		try {Thread.sleep(50l);} 
//		catch (InterruptedException e) {}
		count++;
	}
	
	/**
	 * Creates an image to be displayed later
	 */
	public void buffer(){
		Graphics2D g = buff.createGraphics();
		g.setBackground(Color.black);
		g.clearRect(0, 0, getWidth(), getHeight());
		if(map == null){
			g.setColor(Color.white);
			g.drawLine(0, 0, 100, 100);
			//add an actual image
		}
		else{
			
		//Map and Vision
		for(int i = 0; i < WIDTH; i += 1){
			for(int j = 0; j < HEIGHT; j += 1){
				g.setColor(Color.white);
				g.drawRect(i*15+100, j*15+40, 15, 15);
				
				if(state == 0 || state == 1){
					if(i==select[0] && j==select[1])
						g.fillRect(i*15+100, j*15+40, 15, 15);
					if(i==pick[0] && j==pick[1]){
						g.setColor(Color.green);
						g.fillRect(i*15+100, j*15+40, 15, 15);
					}
				}
				
				if(state == 0){ //spawn grants vision during setup
					g.setColor(Color.black);
//					SpawnTile spawn= null;
//					for(int m = 0; m < WIDTH; m++)
//						for(int n = 0; n < HEIGHT; n++)
//							if(map.getTile(m, n, 1).tileType == Tile.SPAWN_TILE)
//								spawn = (SpawnTile) map.getTile(m, n, 1);
//					if(spawn != null && spawn.canSee(i, j)){
					if(vision(i, j)){
						if(map.getTile(i, j, 2).tileType == Tile.ADVENTURER)
							g.setColor(Color.blue);
						else if(map.getTile(i, j, 2).tileType == Tile.WALL_TILE)
							g.setColor(Color.darkGray);
						else if(map.getTile(i, j, 1).tileType == Tile.DOOR_TILE){
							if(((DoorTile) map.getTile(i, j, 1)).isOpen()) //change if open
								g.setColor(new Color(176, 128, 96)); //light brown
							else
								g.setColor(new Color(110, 63, 25)); //dark brown
						}
						else if(map.getTile(i, j, 1).tileType == Tile.SPAWN_TILE)
							g.setColor(Color.cyan);
						else if (map.getTile(i, j, 1).tileType == Tile.WARP_TILE)
							g.setColor(new Color(25, 25, 100));
						else if(map.getTile(i, j, 0).tileType == Tile.FLOOR_TILE)
							g.setColor(Color.lightGray);
						else
							g.setColor(Color.black);
						g.fillRect(i*15+102, j*15+43, 10, 10);
					}
				}
				
				if(state == 1){ //dungeonee sees by units and spawn, dungeoneer sees all
					switch(map.getTile(i, j, 2).tileType){
						case Tile.WALL_TILE:
							g.setColor(Color.darkGray);
							break;
						case Tile.ADVENTURER:
							g.setColor(Color.blue);
							break;
						case Tile.MONSTER:
							g.setColor(Color.red);
							break;
						default:	
							switch(map.getTile(i, j, 1).tileType){
								case Tile.OBJECTIVE:
									g.setColor(Color.green);
									break;
								case Tile.SPAWN_TILE:
									g.setColor(Color.cyan);
									break;
								case Tile.DOOR_TILE:
									if(((DoorTile) map.getTile(i, j, 1)).isOpen()) //change if open
										g.setColor(new Color(176, 128, 96)); //light brown
									else
										g.setColor(new Color(110, 63, 25)); //dark brown
									break;
								case Tile.WARP_TILE:
									g.setColor(new Color(25, 25, 100)); //dark blue
									break;
								default:
									switch(map.getTile(i, j, 0).tileType){
										case Tile.FLOOR_TILE:
											g.setColor(Color.lightGray);
											break;
										default:
											g.setColor(Color.black);
									}
							}
					}
					if(turn == 0){
						if(vision(i, j))
							g.fillRect(i*15+102, j*15+42, 10, 10);
					}
					if(turn == 1)
						g.fillRect(i*15+102, j*15+42, 10, 10);
				}
			}
		}
		
		//Ranges
		//still needs work
		if (state == 0 || state == 1){
			if(turn == 1){
				for(int i = 0; i < WIDTH; i++)
					for(int j = 0; j < HEIGHT; j++)
						if(vision(i, j)){
							g.setColor(Color.blue);
							g.drawLine(i*15+100, j*15+40, i*15+115, j*15+40);
						}
			}
			if(pick[0] != -1 && pick[1] != -1){
				if(map.getTile(pick[0], pick[1], 2).getType() >= 100){
					Actor a = (Actor) map.getTile(pick[0], pick[1], 2);
					for(int i = 0; i < WIDTH; i++)
						for(int j = 0; j < HEIGHT; j++){
							if(a.canMoveTo(i, j)){
								g.setColor(Color.green);
								g.drawLine(i*15+100, j*15+40, i*15+100, j*15+47);
							}
							if(a.canAttack(i, j)){
								g.setColor(Color.red);
								g.drawLine(i*15+100, j*15+48, i*15+100, j*15+55);
							}
						}
				}
			}
		}
		
		//Stats 
		g.setColor(Color.white);
		g.drawString("Stats:", 0, 12);
		g.drawString("Turn: "+turn, 0, 24);
		g.drawString("AP: "+action[turn%2], 0, 36);
		g.drawString("Select: " +select[0] + ", " + select[1], 0, 48);
		g.drawString("Pick: " +pick[0] + ", " + pick[1], 0, 60);
		g.drawString("State: " +state, 0, 72);
		g.drawString("Frames: "+count, 0, 84);
		
		//Controls
		g.drawString("Key Controls:", 0, 108);
		g.drawString("WASD - Move S", 0, 120);
		g.drawString("Space - Set P", 0, 132);
		g.drawString("Z - Move P to S", 0, 144);
		g.drawString("X - Attack S w/ P", 0, 156);
		g.drawString("C - Create at S", 0, 168);
		g.drawString("R/P - Pass Turn", 0, 180);
		
		g.drawString("Mouse Controls:", 0, 204);
		g.drawString("M1 - Set S and P", 0, 216);
		g.drawString("M1 on P - Create", 0, 228);
		g.drawString("M2 - Move S", 0, 240);
		g.drawString("M2 - Move P to S", 0, 252);
		g.drawString("M2 - Attack S w/ P ", 0, 264);
		g.drawString("M3 - Pass Turn", 0, 276);
		
		
		//End Conditions
		if(state != 0 && map.checkObjective()){
			g.drawString("Winner:", 900, 312);
			g.drawString("Dungeonee!", 900, 324);
		}
		if(state != 0 && party.isEmpty()){
			g.drawString("Winner:", 900, 300);
			g.drawString("Dungeoneer!", 900, 324);
		}
		
		//Tile Info
		if(state == 0 || state == 1){
		if((turn == 1 || vision(pick[0],pick[1])) && pick[0] != -1 && pick[1] != -1){
			g.drawString("Layer 0:", 900, 48);
			switch(map.getTile(pick[0], pick[1], 0).getType()){
				case Tile.FLOOR_TILE:
					g.drawString("Floor", 900, 60);
					g.drawString("You can walk on it.", 900, 72);
					break;
				default:
					g.drawString("Nothing of interest", 900, 60);
			}
			g.drawString("Layer 1:", 900, 108);
			switch(map.getTile(pick[0], pick[1], 1).getType()){
				case Tile.WALL_TILE:
					g.drawString("Wall", 900, 120);
					g.drawString("You can't walk on it.", 900, 132);
					break;
				case Tile.OBJECTIVE:
					g.drawString("Objective", 900, 120);
					g.drawString("A winner is an adventurer here.", 900, 132);
					break;
				case Tile.SPAWN_TILE:
					g.drawString("Spawn", 900, 120);
					g.drawString("Adventurers enter here.", 900, 132);
					break;
				case Tile.WARP_TILE:
					g.drawString("Teleporter", 900, 120);
					g.drawString("You can walk through it.", 900, 132);
					break;
				case Tile.DOOR_TILE:
					g.drawString("Door", 900, 120);
					g.drawString("Open: " + ((DoorTile)map.getTile(pick[0], pick[1], 1)).isOpen(), 900, 132);
					g.drawString("You can sometimes walk on it.", 900, 144);
					break;
				default:
					g.drawString("Nothing of interest", 900, 120);
			}
			Actor temp;
			g.drawString("Layer 2:", 900, 168);
			switch(map.getTile(pick[0], pick[1], 2).getType()){
				case Tile.WALL_TILE:
					g.drawString("Wall", 900, 180);
					g.drawString("You can't walk on it.", 900, 192);
					break;
				case Tile.FIGHTER:
					temp = ((Actor)map.getTile(pick[0], pick[1], 2));
					g.drawString("Fighter", 900, 180);
					g.drawString("Health: " + temp.getCurrHP() + "/" + temp.getBaseHP(), 900, 192);
					g.drawString("Attack: " + temp.getBaseAtt(), 900, 204);
					g.drawString("Move: " + temp.getMoveRange(), 900, 216);
					g.drawString("Adventurer with a sword.", 900, 228);
					break;
				case Tile.SLIM:
					temp = ((Actor)map.getTile(pick[0], pick[1], 2));
					g.drawString("Slim", 900, 180);
					g.drawString("Health: " + temp.getCurrHP() + "/" + temp.getBaseHP(), 900, 192);
					g.drawString("Attack: " + temp.getBaseAtt(), 900, 204);
					g.drawString("Move: " + temp.getMoveRange(), 900, 216);
					g.drawString("Kinda shady.", 900, 228);
					break;
				default:
					g.drawString("Nothing of interest", 900, 180);
			}
			}
		if(!cont && !end)
			g.drawString("Turn Ended. Hit R/P/M3 to Pass Turn.", 900, 300);
		}
	}
	}
}
