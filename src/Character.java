import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Timer;


public abstract class Character {
	private Timer movement;
	private Direction d = Direction.RIGHT;
	private int x;
	private int y;
	private int width;
	private int height;
	private int xVelocity;
	private int yVelocity;
	private int speed;
	private int movePhase = 0;
	public BufferedImage spriteSheet;
	public String bufferedImagePath;
	public BufferedImage[] walkRight = new BufferedImage[9];
	public BufferedImage[] walkDown = new BufferedImage[9];
	public BufferedImage[] walkUp = new BufferedImage[9];
	public BufferedImage[] walkLeft = new BufferedImage[9];

	public Character(String imagePath,int x , int y, int width, int height,int speed){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.speed = speed;
		bufferedImagePath = imagePath;
		splitSpriteSheet();
		
		movement = new Timer(70,e->{
			if(xVelocity!=0||yVelocity!=0)
				incrementMovePhase();
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
			walkRight[index] = spriteSheet.getSubimage(xBuffer, 710, 65, 60);
			xBuffer+=65;
		}
		xBuffer = 0;
		for(int index = 0; index < 9; index++){
			walkDown[index] = spriteSheet.getSubimage(xBuffer, 650, 65, 60);
			xBuffer+=65;
		}
		xBuffer = 0;
		for(int index = 0; index < 9; index++){
			walkLeft[index] = spriteSheet.getSubimage(xBuffer, 580, 65, 60);
			xBuffer+=65;
		}xBuffer = 0;
		for(int index = 0; index < 9; index++){
			walkUp[index] = spriteSheet.getSubimage(xBuffer, 520, 65, 60);
			xBuffer+=65;
		}

	}
	public void draw(Graphics g, int x, int y){
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
	public void incrementMovePhase(){
		System.out.println(getX() + " " + getY());
		movePhase++;
		if(movePhase>=9)
			movePhase = 0;
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
