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
	ArrayList<Actor> party;
	ArrayList<Actor> mobs;
	int turn;	//0-Dungeonee, 1-Dungeoneer
	int[] action;	//0-Dungeonee, 1-Dungeoneer
	int[] select; //mobile
	int[] pick; //fixed
	int state; //0-setup, 1-standard, 2-switch
	Graphics g;
	BufferedImage buff;
	static KeyEvent event;
	
	//int count;
	
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
		map = new GameMap(10,10);	//subject to change
		party = new ArrayList<Actor>();
//			party.add(new Fighter());
//			map.placeTile(1, 1, 2, party.get(0));
		mobs = new ArrayList<Actor>();
		map.placeTile(2, 2, 1, new SpawnTile());
		map.placeTile(8, 8, 1, new ObjectiveTile());
		turn = 0;
		action = new int[]{4, 10};
		select = new int[]{0,0};
		pick  = new int[]{-1,-1};
		state = 0;
		//count = 0;
		
		g = this.getGraphics();
		buff = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
	}

	/**
	 * Alternates turns until one of two end conditions are met:
	 * The Dungeoneer wins if all adventurers are removed from the map
	 * The Dungeonee wins if an adventurer reaches the objective
	 */
	public void run(){
		while(state == 0 || !(map.checkObjective() || party.isEmpty())){ //no end during setup
			update();
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
		
		boolean cont = true;
		while(cont){ //pass conditions, end conditions
			if(event != null){
				switch(event.getKeyChar()){
				//wasd to control select
				case 'w':
					if (select[1] > 0)
						select[1]--;
					break;
				case 's':
					if (select[1] < 10-1)
						select[1]++;
					break;
				case 'a':
					if (select[0] > 0)
						select[0]--;
					break;
				case 'd':
					if (select[0] < 10-1)
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
				case 'z':	//INEFFECIENT
					if(pick[0] != -1 && pick[1] != -1){
						if(map.getTile(pick[0], pick[1], 2).getType() >= Tile.ADVENTURER) //200 mob
						if(map.getTile(select[0], select[1], 2).getType() == 0){
							Actor picked = (Actor) map.getTile(pick[0], pick[1], 2);
							if((turn == 0 && picked.getType() < Tile.MONSTER) || (turn == 1 && picked.getType() >= Tile.MONSTER))
							if(picked.canMoveTo(select[0] - pick[0], select[1] - pick[1])){
								map.move(pick[0], pick[1], select[0], select[1], 2);
								pick[0] = select[0];
								pick[1] = select[1];
								action[turn]--;
							}
						}
					}
					break;
				//x to have pick attack select 
				case 'x': //INEFFICIENT
					if(pick[0] != -1 && pick[1] != -1){
						if(map.getTile(pick[0], pick[1], 2).getType() >= Tile.ADVENTURER)
						if(map.getTile(select[0], select[1], 2).getType() >= Tile.ADVENTURER){
							Actor picked = (Actor) map.getTile(pick[0], pick[1], 2);
							Actor selected = (Actor) map.getTile(select[0], select[1], 2);
							if((turn == 0 && picked.getType() < Tile.MONSTER) || (turn == 1 && picked.getType() >= Tile.MONSTER))
							if(picked.canAttack(select[0] - pick[0], select[1] - pick[1])){
								if(map.attack(pick[0], pick[1], select[0], select[1], 2)) //tests if dead after
									if(selected.getType() >= Tile.MONSTER)
										mobs.remove(selected);
									else
										party.remove(selected);
								action[turn]--;
							}
						}
					}
					break;
				//c to place a unit	on select
				case 'c':
					if(turn == 0){
						System.out.println("0");
						if(state == 0){ 
							System.out.println("1");
							if(pick[0] != -1 && pick[1] != -1){
								System.out.println("2");
								if(map.getTile(pick[0], pick[1], 2).getType() == Tile.SPAWN_TILE)
									System.out.println("3");
									if(map.getTile(select[0], select[1], 2).getType() == Tile.EMPTY_TILE){
										System.out.println("4");
										if(Math.abs(select[0] - pick[0]) <= 1 && Math.abs(select[1] - pick[1]) <= 1){
											System.out.println("5");
											Fighter swrd = new Fighter();
											party.add(swrd);
											map.placeTile(select[0], select[1], 2, swrd);
											action[turn]-=3;
										}
									}
							}
						}
					}
					if(turn == 1){ //needs to be able to switch mob type
						if(!(select[0] == -1 && select[1] == -1)){
							if(map.getTile(select[0], select[1], 2).getType() == Tile.EMPTY_TILE){
								//check for adventurer vision
								Slim slim = new Slim();
								mobs.add(slim);
								map.placeTile(select[0], select[1], 2, slim);
								action[turn]-=3;
							}
						}
					}
					break;
				//p to pass turn	
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
						if (select[1] < 10-1)
							select[1]++;
						break;
					case KeyEvent.VK_LEFT:
						if (select[0] > 0)
							select[0]--;
						break;
					case KeyEvent.VK_RIGHT:
						if (select[0] < 10-1)
							select[0]++;
						break;
					}
				}
				event = null;
				
				if(action[turn] <=0)
					cont = false;
				if(state != 0 && (map.checkObjective() || party.isEmpty())){
					cont = false;
					System.out.println("tghin");
				}
				update();
			}
		}
		if(state == 0 && turn == 1)
			state = 1;
		turn = (turn+1)%2;
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
		//count++;
	}
	
	/**
	 * Creates an image to be displayed later
	 */
	public void buffer(){
		Graphics2D g = buff.createGraphics();
		g.setBackground(Color.black);
		g.clearRect(0, 0, getWidth(), getHeight());
		for(int i = 0; i < 10; i += 1)
			for(int j = 0; j < 10; j += 1){
				g.setColor(Color.white);
				g.drawRect(i*40+100, j*40+40, 40, 40);
				if(i==select[0] && j==select[1])
					g.fillRect(i*40+100, j*40+40, 40, 40);
				if(i==pick[0] && j==pick[1]){
					g.setColor(Color.green);
					g.fillRect(i*40+100, j*40+40, 40, 40);
				}
				
				if(state == 0 || state == 1){
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
					g.fillRect(i*40+105, j*40+45, 30, 30);
				}
			}
		g.setColor(Color.white);
		g.drawString("Turn: "+turn, 0, 12);
		g.drawString("AP: "+action[turn%2], 0, 24);
		g.drawString("Select: " +select[0] + ", " + select[1], 0, 36);
		g.drawString("Selected: " +pick[0] + ", " + pick[1], 0, 48);
		g.drawString("State: " +state, 0, 60);
		//g.drawString("Frames: "+count, 0, 48);
		if(state != 0 && (map.checkObjective() || party.isEmpty()))
			g.drawString("Hey someone won", 400, 500);
	}
}
