import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Maintains a GameMap and associated Actors
 * Interprets user input to provide game control
 * @author Jacob Ryder
 */
@SuppressWarnings("serial")
public class Dungeone extends Canvas{
	GameMap map;
	ArrayList<Adventurer> party;
	ArrayList<Monster> mobs;
	int turn;	//0-Dungeonee, 1-Dungeoneer
	int[] action;	//0-Dungeonee, 1-Dungeoneer
	int[] select; //mobile
	int[] pick; //fixed
	int state; //0-setup, 1-standard, 2-switch
	Graphics g;
	BufferedImage buff;
	static KeyEvent event;
	
	int count;
	
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
		KeyAdapter key = new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e) {
				//System.out.println(e.getKeyChar());
				event = e;
				//System.out.println(event.getKeyChar());
			}
		};
		frame.addKeyListener(key);
		game.addKeyListener(key);
		frame.setVisible(true);
		
		game.init();
		game.run();
	}
	
	public Dungeone() {
		super();
	}
	
	
	/**
	 * Initializes GameMap, related objects, and graphical output
	 */
	public void init(){
		map = new GameMap(40,40);	//40x40 base
		party = new ArrayList<Adventurer>();
//			party.add(new Fighter());
//			map.placeTile(1, 1, 2, party.get(0));
		mobs = new ArrayList<Monster>();
		map.placeTile(2, 2, 1, new SpawnTile(map, 2, 2));
		map.placeTile(20, 19, 1, new ObjectiveTile(map, 20, 19));
		turn = 0;
		action = new int[]{4, 0};
		select = new int[]{0,0};
		pick  = new int[]{-1,-1};
		state = 0;
		count = 0;
		
		g = this.getGraphics();
		event = null; //needed?
		buff = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
	}

	/**
	 * Alternates turns until one of two end conditions are met:
	 * The Dungeoneer wins if all adventurers are removed from the map
	 * The Dungeonee wins if an adventurer reaches the objective
	 */
	public void run(){
		while(state == 0 || !(map.checkObjective() || party.isEmpty())){ //no end during setup
			turn();
		}
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
		//System.out.println("1");
		update();
		//System.out.println("2");
		
		boolean cont = true;
		while(cont){ //pass conditions, end conditions
			try {Thread.sleep(1l);} 
			catch (InterruptedException e) {}
			//System.out.println("3"); //logic error here
			//update();
			if(event != null){
				//System.out.println(event.getKeyChar());
				if (state == 2)
					state = 1;
				else if (state != 2){
				switch(event.getKeyChar()){
				//wasd to control select
				case 'w':
					if (select[1] > 0)
						select[1]--;
					break;
				case 's':
					if (select[1] < map.getSize()[1]-1)
						select[1]++;
					break;
				case 'a':
					if (select[0] > 0)
						select[0]--;
					break;
				case 'd':
					if (select[0] < map.getSize()[0]-1)
						select[0]++;
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
				//z to move from pick to select
				case 'z':	//potentially inefficient
					if(pick[0] != -1 && pick[1] != -1){
						if(map.getTile(pick[0], pick[1], 2).getType() >= Tile.ADVENTURER) //200 mob
						if(map.getTile(select[0], select[1], 2).getType() == Tile.EMPTY_TILE) //get doors out of 2
						if(map.getTile(select[0], select[1], 1).getType() != Tile.DOOR_TILE || (map.getTile(select[0], select[1], 1).getType() == Tile.DOOR_TILE && ((DoorTile) map.getTile(select[0], select[1], 1)).isOpen())){
							Actor picked = (Actor) map.getTile(pick[0], pick[1], 2);
							if((turn == 0 && picked.getType() < Tile.MONSTER) || (turn == 1 && picked.getType() >= Tile.MONSTER))
							if(picked.canMoveTo(select[0], select[1])){ //now absolute
								map.move(pick[0], pick[1], select[0], select[1], 2);
								pick[0] = select[0];
								pick[1] = select[1];
								action[turn]--;
							}
						}
					}
					break;
				//x to have pick attack select 
				case 'x': //potentially inefficient
					if(pick[0] != -1 && pick[1] != -1){
						if(map.getTile(pick[0], pick[1], 2).getType() >= Tile.ADVENTURER){
							if(map.getTile(select[0], select[1], 2).getType() >= Tile.ADVENTURER){
								Actor picked = (Actor) map.getTile(pick[0], pick[1], 2);
								Actor selected = (Actor) map.getTile(select[0], select[1], 2);
								if((turn == 0 && picked.getType() < Tile.MONSTER) || (turn == 1 && picked.getType() >= Tile.MONSTER))
									if(picked.canAttack(select[0], select[1])){ //now absolute
										if(map.attack(pick[0], pick[1], select[0], select[1], 2)) //tests if dead after
											if(selected.getType() >= Tile.MONSTER)
												mobs.remove(selected);
											else
												party.remove(selected);
										action[turn]--;
									}
							}
							else if(map.getTile(select[0], select[1], 1).getType() == Tile.DOOR_TILE){
								Actor picked = (Actor) map.getTile(pick[0], pick[1], 2);
								DoorTile selected = (DoorTile) map.getTile(select[0], select[1], 1);
								//DoorTile selected2 = (DoorTile) map.getTile(select[0], select[1], 2);
								if((turn == 0 && picked.getType() < Tile.MONSTER) || (turn == 1 && picked.getType() >= Tile.MONSTER))
									if(picked.canAttack(select[0], select[1])){
										selected.toggleOpen();
										//selected2.toggleOpen();
										action[turn]--;
									}
							}
						}
					}
					break;
				//c to place a unit	on select
				case 'c':
					if(turn == 0){
						if(state == 0){ 
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
									System.out.println(select[0]+" "+select[1]);
									map.placeTile(select[0], select[1], 2, swrd);
									action[turn]-=3;
								}
							}
						}
					}
					if(turn == 1){ //needs to be able to switch mob type
						if(!(select[0] == -1 && select[1] == -1)){
							if(map.getTile(select[0], select[1], 2).getType() == Tile.EMPTY_TILE){
								boolean hidden = true;
								for(Adventurer a: party)
									if(a.canSee(select[0], select[1]))
										hidden = false;
								if(hidden){
									Slim slim = new Slim(map, select[0], select[1]);
									mobs.add(slim);
									map.placeTile(select[0], select[1], 2, slim);
									action[turn]-=5;
								}
							}
						}
					}
					break;
				//r or p to pass turn	
				case 'r':
				case 'p':	
					if(state == 0 || state == 1) //maybe change
						cont = false;
					break;
				default:
					switch(event.getKeyCode()){
					//arrows to also control movement
					case KeyEvent.VK_UP:
						if (select[1] > 0)
							select[1]--;
						break;
					case KeyEvent.VK_DOWN:
						if (select[1] < map.getSize()[1]-1)
							select[1]++;
						break;
					case KeyEvent.VK_LEFT:
						if (select[0] > 0)
							select[0]--;
						break;
					case KeyEvent.VK_RIGHT:
						if (select[0] < map.getSize()[0]-1)
							select[0]++;
						break;
					}
				}
				}
				event = null;
				//System.out.println("nullified");
				
				if(action[turn] <=0)
					cont = false;
				if(state != 0 && (map.checkObjective() || party.isEmpty())){
					cont = false;
				}
				update();
			} //if(event
			//update();
			//System.out.println(cont);
		}//while(cont
		
		if(state == 0 || state == 1)
			state = 2;
		else if(state == 2)
			state = 1;
		turn = (turn+1)%2;
		update();
	}
	
	/**
	 * Displays buffered image
	 * @param g - Graphics object created by Canvas
	 */
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(buff, 0, 0, getWidth(), getHeight(), null);
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
		for(int i = 0; i < 40; i += 1)
			for(int j = 0; j < 40; j += 1){
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
					int x = -1, y = -1;
					for(int m = 0; m < map.getSize()[0]; m++)
						for(int n = 0; n < map.getSize()[1]; n++)
							if(map.getTile(m, n, 1).tileType == Tile.SPAWN_TILE){
								x = m;
								y = n;
							}
					if(Math.pow(Math.abs(x-i),2)+Math.pow(Math.abs(y-j),2)<=Math.pow(4,2)){
						if(map.getTile(i, j, 2).tileType == Tile.ADVENTURER)
							g.setColor(Color.blue);
						else if(map.getTile(i, j, 2).tileType == Tile.WALL_TILE)
							g.setColor(Color.darkGray);
						else if(map.getTile(i, j, 1).tileType == Tile.DOOR_TILE){
							g.setColor(new Color(110, 63, 25)); //brown
						}
						else if(map.getTile(i, j, 1).tileType == Tile.SPAWN_TILE)
							g.setColor(Color.cyan);
						else if(map.getTile(i, j, 0).tileType == Tile.FLOOR_TILE)
							g.setColor(Color.lightGray);
						else
							g.setColor(Color.black);
						g.fillRect(i*15+102, j*15+43, 10, 10);
						
					}
				}
				
				if(state == 1 || (state == 0 && turn == 1)){
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
									//System.out.println("Objective at " + i + ", " + j);
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
//						for(int x = 0; x < 10; x++) //map.getSize[0]
//							for(int y = 0; y < 10; y++) //map.getSize[1]
//								if(map.getTile(x, y, 2).getType() >= Tile.ADVENTURER)
//									if(map.getTile(x, y, 2).getType() < Tile.MONSTER)
//										if(((Adventurer)(map.getTile(x, y, 2))).canSee(x-i, y-j))
//											g.fillRect(i*40+105, j*40+45, 30, 30);
						for(Adventurer a: party){
							if(a.canSee(i, j))
								g.fillRect(i*15+102, j*15+42, 10, 10);
						}
					}
					if(turn == 1)
						g.fillRect(i*15+102, j*15+42, 10, 10);
				}
			}
		g.setColor(Color.white);
		g.drawString("Stats:", 0, 12);
		g.drawString("Turn: "+turn, 0, 24);
		g.drawString("AP: "+action[turn%2], 0, 36);
		g.drawString("Select: " +select[0] + ", " + select[1], 0, 48);
		g.drawString("Pick: " +pick[0] + ", " + pick[1], 0, 60);
		g.drawString("State: " +state, 0, 72);
		g.drawString("Frames: "+count, 0, 84);
		
		g.drawString("Controls:", 0, 108);
		g.drawString("Z - Move P to S", 0, 120);
		g.drawString("X - Attack S w/ P", 0, 132);
		g.drawString("C - Create U at S", 0, 144);
		g.drawString("R/P - Pass Turn", 0, 156);
		//more controls?
		
		if(state != 0 && map.checkObjective()){
			g.drawString("Winner:", 0, 288);
			g.drawString("Dungeonee!", 0, 300);
		}
		if(state != 0 && party.isEmpty()){
			g.drawString("Winner:", 0, 288);
			g.drawString("Dungeoneer!", 0, 300);
		}
		//System.out.println("finished buffer");
	}
}
