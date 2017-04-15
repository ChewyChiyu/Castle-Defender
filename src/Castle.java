import java.awt.Color;
import java.awt.Graphics;

public class Castle {
	private int health;
	private int initialHealth;
	public Castle(int health){
		this.health = health;
		initialHealth = health;
	}
	public int getHealth(){
		return health;
	}
	public boolean fatalDamage(int pow){
		if((health-=pow)<0){
			return true;
		}
		return false;
	}
	public void heal(){
		health = initialHealth;
	}
	public void draw(Graphics g){
		g.setColor(Color.GREEN);
		g.fillRect(900, 150, health*4, 10);
	}		
}
