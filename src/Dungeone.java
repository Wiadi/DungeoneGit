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
	int turn;	//0-Dungeoneer, 1-Dungeonee
	int[] action;	//0-Dungeoneer, 1-Dungeonee
	int[] select;
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
				System.out.println(e.getKeyChar());
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
			party.add(new Fighter());
		mobs = new ArrayList<Actor>();
		turn = 0;
		action = new int[]{10, 0};
		select = new int[]{0,0};
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
		while(!(map.checkObjective() || party.isEmpty())){
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
		action[turn] += 5;
		if (action[turn] > 20)
			action[turn] = 20;
		
		if(action[turn] > 15)
			action[turn] = 15;
		while(action[turn] >0){ //pass conditions, end conditions
			if(event != null){
				switch(event.getKeyChar()){
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
				case ' ':
					action[turn]++;
					//change turn
					break;
				default:
					switch(event.getKeyCode()){
					
					}
				}
				event = null;
				action[turn]--;
				update();
			}
		}
		turn = (turn+1)%2;
		//add ap
		//go until no ap
		//switch turn
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
		g.setColor(Color.white);
		g.drawString("Turn: "+turn, 0, 12);
		g.drawString("AP: "+action[turn%2], 0, 24);
		g.drawString("Select: " +select[0] + ", " + select[1], 0, 36);
		//g.drawString("Frames: "+count, 0, 48);
	}
}
