import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

//JR

@SuppressWarnings("serial")
public class Dungeone extends Canvas{
	GameMap map;
	ArrayList<Actor> party;
	ArrayList<Actor> mobs;
	int turn;	//0-Dungeoneer, 1-Dungeonee
	int[] action;	//0-D'eer, 1-D'ee
	Graphics g;
	BufferedImage buff;
	
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
	}
	
	public void init(){
		map = new GameMap(10,10);//subject to change
		party = new ArrayList<Actor>();
			party.add(new Fighter());
		mobs = new ArrayList<Actor>();
		turn = 0;
		action = new int[]{15, 5};
		g = this.getGraphics();
		buff = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
	}

	public void run(){
		while(true){ //map.checkObjective(), party.isEmpty()
			turn();
		}
		//end conditions here, but also to exit turn
	}
	
	//add action
	//go until no action
	//switch turn
	public void turn(){
		action[turn] += 5;
		if(action[turn] > 15)
			action[turn] = 15;
		while(action[turn] >0){
			update();
			action[turn]--;
		}
		turn = (turn+1)%2;
	}
	
	
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(buff, 0, 0, getWidth(), getHeight(), null);
	}
	
	
	public void update(){
		buffer();
		paint(g);
		try {Thread.sleep(50l);} 
		catch (InterruptedException e) {}
	}
	
	public void buffer(){
		Graphics2D g = buff.createGraphics();
		g.setBackground(Color.black);
		g.clearRect(0, 0, getWidth(), getHeight()); //based on window size
		g.setColor(Color.green);
		for(int i = 20; i < 200; i += 20)
			for(int j = 20; j < 200; j += 20)
				g.drawRect(i, j, 20, 20);
		g.drawString(""+turn, 0, 12);
		g.drawString(""+action[turn%2], 0, 24);
	}
}
