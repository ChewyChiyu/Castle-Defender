import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Timer;


public abstract class Character {
	private Timer movement;
	private boolean attacking = false;
	private Direction d = Direction.RIGHT;
	private int x;
	private int y;
	private int width;
	private int height;
	private int xVelocity;
	private int yVelocity;
	private int speed;
	private int movePhase = 0;
	private int attackPhase = 0;
	public BufferedImage spriteSheet;
	public String bufferedImagePath;
	public BufferedImage[] walkRight = new BufferedImage[9];
	public BufferedImage[] walkDown = new BufferedImage[9];
	public BufferedImage[] walkUp = new BufferedImage[9];
	public BufferedImage[] walkLeft = new BufferedImage[9];
	public BufferedImage[] attackUp = new BufferedImage[6];
	public BufferedImage[] attackDown = new BufferedImage[6];
	public BufferedImage[] attackLeft = new BufferedImage[6];
	public BufferedImage[] attackRight = new BufferedImage[6];

	public Character(String imagePath,int x , int y, int width, int height,int speed){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.speed = speed;
		bufferedImagePath = imagePath;
		splitSpriteSheet();
		
		movement = new Timer(65,e->{
			if(xVelocity!=0||yVelocity!=0)
				incrementMovePhase();
			if(attacking){
				incrementAttackPhase();
			}
		});
		movement.start();
		
	}
	public void splitSpriteSheet(){
		URL imageUrl = getClass().getResource(bufferedImagePath);
		try {
			spriteSheet = ImageIO.read(imageUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Walk right
		int xBuffer = 0;
		for(int index = 0; index < 9; index++){
			walkRight[index] = spriteSheet.getSubimage(xBuffer, 710, 64, 60);
			xBuffer+=64;
		}
		xBuffer = 0;
		for(int index = 0; index < 9; index++){
			walkDown[index] = spriteSheet.getSubimage(xBuffer, 650, 64, 60);
			xBuffer+=64;
		}
		xBuffer = 0;
		for(int index = 0; index < 9; index++){
			walkLeft[index] = spriteSheet.getSubimage(xBuffer, 580, 64, 60);
			xBuffer+=64;
		}
		xBuffer = 0;
		for(int index = 0; index < 9; index++){
			walkUp[index] = spriteSheet.getSubimage(xBuffer, 520, 64, 60);
			xBuffer+=64;
		}
		xBuffer = 64;
		for(int index = 0; index < 6; index++){
			attackUp[index] = spriteSheet.getSubimage(xBuffer-10, 1390, 110, 90);
			xBuffer+=192;
		}
		xBuffer = 64;
		for(int index = 0; index < 6; index++){
			attackLeft[index] = spriteSheet.getSubimage(xBuffer-60, 1602, 120, 60);
			xBuffer+=192;
		}
		xBuffer = 64;
		for(int index = 0; index < 6; index++){
			attackDown[index] = spriteSheet.getSubimage(xBuffer-20, 1794, 120, 90);
			xBuffer+=192;
		}
		xBuffer = 64;
		for(int index = 0; index < 6; index++){
			attackRight[index] = spriteSheet.getSubimage(xBuffer, 1986, 130, 60);
			xBuffer+=192;
		}
	}
	public boolean isAttacking(){
		return attacking;
	}
	public void stopAttack(){
		attacking = false;
	}
	public void drawMove(Graphics g, int x, int y){
		
		switch(d){
		case RIGHT : g.drawImage(walkRight[movePhase], x, y,100,100, null);
		break;
		case LEFT : g.drawImage(walkLeft[movePhase], x, y,100,100, null);
		break;
		case UP : g.drawImage(walkUp[movePhase], x, y,100,100, null);
		break;
		case DOWN : g.drawImage(walkDown[movePhase], x, y,100,100, null);
		break;
		}
	}
	public void drawAttack(Graphics g, int x , int y){
		switch(d){  
		case RIGHT : g.drawImage(attackRight[attackPhase], x+10, y-10,170,110, null);
		break;
		case LEFT : g.drawImage(attackLeft[attackPhase], x-70, y-10,170,110, null);
		break;
		case UP : g.drawImage(attackUp[attackPhase], x-10, y-50,150,150, null);
		break;
		case DOWN : g.drawImage(attackDown[attackPhase], x-20, y-10,160,160, null);
		break;
		}
	}
	public void attack(){
		attacking = true;
	}
	public void incrementMovePhase(){
		System.out.println(getX() + " " + getY());
		movePhase++;
		if(movePhase>=9)
			movePhase = 0;
	}
	public void incrementAttackPhase(){
		System.out.println(getX() + " " + getY());
		attackPhase++;
		if(attackPhase>=6){
			attackPhase = 0;
			attacking = false;
		}
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getSpeed(){
		return speed;
	}
	public Direction getDirection(){
		return d;
	}
	public void changeDirection(Direction d){
		this.d = d;
	}
	public void changeX(int inc){
		x += inc;
	}
	public void changeY(int inc){
		y += inc;
	}
	public int getXVelocity(){
		return xVelocity;
	}
	public int getYVelocity(){
		return yVelocity;
	}
	public int getHeight(){
		return height;
	}
	public int getWidth(){
		return width;
	}
	public void changeXVelocity(int inc){
		xVelocity = inc;
	}
	public void changeYVelocity(int inc){
		yVelocity = inc;
	}
}
