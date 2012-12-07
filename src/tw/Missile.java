package tw;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import tw.Tank.Direction;

public class Missile {
	public static final int XSPEED = 10;
	public static final int YSPEED = 10;
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	
	int x, y;
	Tank.Direction dir;
	private boolean good;
	private boolean lived = true;
	private TankClient tc;
	
	public Missile(int x, int y, Direction dir) {
		this.dir = dir;
		this.x = x;
		this.y = y;
	}
	
	public Missile(int x, int y, boolean good, Tank.Direction dir, TankClient tc){
		this(x, y, dir);
		this.good = good;
		this.tc = tc;
	}
	public boolean isLived() {
		return lived;
	}
	public void setLived(boolean lived) {
		this.lived = lived;
	}
	
	public void draw(Graphics g){
		if(!lived){
			tc.missiles.remove(this);
			return;
		}
		Color c = g.getColor();
		g.setColor(Color.BLACK);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		
		move();
	}
	private void move() {
		switch(dir){
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			break;
		}
		if(x< 0 || y< 0|| x>TankClient.GMAE_WIDTH || y > TankClient.GAME_LENGTH){
			lived = false;
			tc.missiles.remove(this);
		}
			
	}
	
	public Rectangle getRect(){
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	public boolean hitTank(Tank t){
		if(this.lived && this.getRect().intersects(t.getRect()) && t.isLive() &&this.good!=t.isGood()){
			t.setLive(false);
			this.lived = false;
			Explode e = new Explode(x, y, tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}
	
	public boolean hitTanks(List<Tank> tanks){
		for(int i=0; i<tanks.size(); i++){
			if(hitTank(tanks.get(i))){
				return true;
			}
		}
		return false;
	}
}
