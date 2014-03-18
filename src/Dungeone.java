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
		action = new int[]{15, 0};
		select = new int[]{0,0};
		
		g = this.getGraphics();
		buff = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
	}

	/**
	 * Alternates turns until one of two end conditions are met:
	 * The Dungeoneer wins if all adventurers are removed from the map
	 * The Dungeonee wins if an adventurer reaches the objective
	 */
	public void run(){
		while(true){ //map.checkObjective(), party.isEmpty()
			turn();
		}
		//end conditions here, but also to exit turn
	}
	
	/**
	 * Interprets a turn for either the Dungeoneer or Dungeonee
	 * Uses KeyEvents to control actions, each expending Action Points
	 * Turn ends when the player passes or no Action Points are left
	 */
	public void turn(){
		select = new int[]{0,0};
		action[turn] += 5;
		//ap limit
		
		if(action[turn] > 15)
			action[turn] = 15;
		while(action[turn] >0){
			update();
			if(event != null){
				System.out.println(event.getKeyChar() + " used");
				event = null;
				action[turn]--;
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
	}
	
	/**
	 * Creates an image to be displayed later
	 */
	public void buffer(){
		Graphics2D g = buff.createGraphics();
		g.setBackground(Color.black);
		g.clearRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.green);
		for(int i = 20; i < 200; i += 20)
			for(int j = 20; j < 200; j += 20)
				g.drawRect(i, j, 20, 20);
		g.drawString(""+turn, 0, 12);
		g.drawString(""+action[turn%2], 0, 24);
	}
}
