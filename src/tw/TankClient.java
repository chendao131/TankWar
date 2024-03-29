package tw;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class TankClient extends Frame{
	public static final int GMAE_WIDTH = 800;
	public static final int GAME_LENGTH = 600;
	
	Tank myTank = new Tank(50, 50, true, Tank.Direction.STOP, this);
	List<Missile> missiles = new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	List<Tank> tanks = new ArrayList<Tank>();
	
	Image offScreenImage = null;
	public void paint(Graphics g) {
		g.drawString("missles count"+missiles.size(), 10, 50);
		g.drawString("explodes count:"+ explodes.size(), 10, 70);
		g.drawString("tanks count:" + tanks.size(), 10, 90);
		for(int i=0; i<missiles.size(); i++){
			Missile m = missiles.get(i);
			m.hitTank(myTank);
			m.hitTanks(tanks);
			m.draw(g);
		}
		
		for(int i=0; i<explodes.size(); i++){
			Explode e = explodes.get(i);
			e.draw(g);
		}
		
		for(int i=0; i<tanks.size(); i++){
			Tank t = tanks.get(i);
			t.draw(g);
		}
		
		myTank.draw(g);
	}

	public void update(Graphics g) {
		if(offScreenImage == null){
			offScreenImage = this.createImage(GMAE_WIDTH, GAME_LENGTH);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GREEN);
		gOffScreen.fillRect(0, 0, GMAE_WIDTH, GAME_LENGTH);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	
	public void launchFrame(){
		
		for(int i=0; i<10; i++){
			tanks.add(new Tank(50+40*(i+1), 50, false, Tank.Direction.D, this));
		}
		
		this.setLocation(400, 300);
		this.setSize(GMAE_WIDTH, GAME_LENGTH);
		this.setTitle("TankWar");
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setResizable(false);
		this.setBackground(Color.GREEN);
		this.addKeyListener(new KeyMonitor());
		setVisible(true);
		new Thread(new PaintThread()).start();
	}
	
	private class PaintThread implements Runnable{
		public void run() {
			while(true){
				repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class KeyMonitor extends KeyAdapter{
		public void keyReleased(KeyEvent e){
			myTank.KeyReleased(e);
		}
		public void keyPressed(KeyEvent e) {
			myTank.KeyPressed(e);
		}
	}
	
	public static void main(String[] args){
		TankClient tc = new TankClient();
		tc.launchFrame();
	}
}
