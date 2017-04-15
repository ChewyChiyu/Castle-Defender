import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Timer;


public class Character {
	private Timer movement;
	private Timer grace;
	private Timer moveAI;
	private boolean tookDamage = true;
	private boolean attacking = false;
	private boolean aiEnabled = false;
	private boolean retreating = false;
	private Direction d = Direction.LEFT;
	private int x;
	private int y;
	private int width;
	private int height;
	private int xVelocity;
	private int yVelocity;
	private int speed;
	private int movePhase = 0;
	private int attackPhase = 0;
	private int health;
	private int initialHealth;
	private int power;
	private CharacterTypes type;
	public BufferedImage spriteSheet;
	public String bufferedImagePath;
	public BufferedImage[] walkRight = new BufferedImage[9];
	public BufferedImage[] walkDown = new BufferedImage[9];
	public BufferedImage[] walkUp = new BufferedImage[9];
	public BufferedImage[] walkLeft = new BufferedImage[9];
	public BufferedImage[] attackUp;
	public BufferedImage[] attackDown;
	public BufferedImage[] attackLeft;
	public BufferedImage[] attackRight;

	public Character(String imagePath,int x , int y, int width, int height,int speed,int health,int power, CharacterTypes type){
		this.type = type;
		this.x = x;
		this.y = y;
		this.health = health;
		initialHealth = health;
		this.power = power;
		this.width = width;
		this.height = height;
		this.speed = speed;
		bufferedImagePath = imagePath;
		splitSpriteSheet();
		
		
		grace = new Timer(200,e ->{
			if(!tookDamage){
				 tookDamage = true;
			}
		});
			
		grace.start();
		
		moveAI = new Timer(2000, e->{
			if(retreating){
				retreating = false;
			}
			if(aiEnabled){
				thinkProcess();
			}
		});
		
		moveAI.start();
		
		movement = new Timer(65,e->{
			if(xVelocity!=0||yVelocity!=0)
				incrementMovePhase();
			if(attacking){
				incrementAttackPhase();
			}
			if(aiEnabled){
				thinkAttackProcess();
			}
		});
		movement.start();
	}
	public CharacterTypes getType(){
		return type;
	}
	public void thinkProcess(){
		if(!tookDamage){
			attack();
			if(type.equals(CharacterTypes.SKELLY))
				return;
			switch(d){
			case RIGHT:
			changeDirection(Direction.LEFT);
			changeXVelocity(-speed);
			break;
			case LEFT:
				changeDirection(Direction.RIGHT);
				changeXVelocity(speed);
				break;
			case UP:
				changeDirection(Direction.DOWN);
				changeYVelocity(-speed);
				break;
			case DOWN:
				changeDirection(Direction.UP);
				changeYVelocity(speed);
				break;
			}
			retreating = true;
		}
		if(!retreating){
		changeDirection(Direction.RIGHT);
		changeXVelocity(speed);
		}
		
	}
	public void thinkAttackProcess(){
		if(!tookDamage)
		attack();
	}
	public int getPower(){
		return power;
	}
	public int getHealth(){
		return health;
	}
	public void toggleAI(){
		aiEnabled = !aiEnabled;
	}
	public boolean fatalDamage(int inc){
		if(tookDamage){
		if((health-=inc)<0){
			return true;
		}
		tookDamage = false;
		return false;
		}
		return false;
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
		if(type.equals(CharacterTypes.OGRE)||type.equals(CharacterTypes.GOLDEN_KNIGHT)){
		
		attackUp = new BufferedImage[6];
		attackDown = new BufferedImage[6];	
		attackLeft = new BufferedImage[6];	
		attackRight = new BufferedImage[6];	
			
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
		if(type.equals(CharacterTypes.SKELLY)){
			
			attackUp = new BufferedImage[13];
			attackDown = new BufferedImage[13];	
			attackLeft = new BufferedImage[13];	
			attackRight = new BufferedImage[13];
			
			xBuffer = 0;
			for(int index = 0; index < 6; index++){
				attackUp[index] = spriteSheet.getSubimage(xBuffer, 1024, 64, 60);
				xBuffer+=64;
			}
			xBuffer = 0;
			for(int index = 0; index < 6; index++){
				attackLeft[index] = spriteSheet.getSubimage(xBuffer, 1088, 64, 60);
				xBuffer+=64;
			}
			xBuffer = 0;
			for(int index = 0; index < 6; index++){
				attackDown[index] = spriteSheet.getSubimage(xBuffer, 1152, 64, 60);
				xBuffer+=64;
			}
			xBuffer = 0;
			for(int index = 0; index < 6; index++){
				attackRight[index] = spriteSheet.getSubimage(xBuffer, 1216, 64, 60);
				xBuffer+=64;
			}
			
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
		g.setColor(Color.green);
		g.fillRect(x, y, health, 8);
	}
	public void drawAttack(Graphics g, int x , int y){
		if(type.equals(CharacterTypes.OGRE)||type.equals(CharacterTypes.GOLDEN_KNIGHT)){
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
		if(type.equals(CharacterTypes.SKELLY)){
			switch(d){  
			case RIGHT : g.drawImage(attackRight[attackPhase], x, y,100,100, null);
			break;
			case LEFT : g.drawImage(attackLeft[attackPhase], x, y,100,100, null);
			break;
			case UP : g.drawImage(attackUp[attackPhase], x, y,100,100, null);
			break;
			case DOWN : g.drawImage(attackDown[attackPhase], x, y,100,100, null);
			break;
			}
			}
		g.setColor(Color.green);
		g.fillRect(x, y+5, health, 8); // 5 pixel buffer
	}
	public void attack(){
		attacking = true;
	}
	public void incrementMovePhase(){
		movePhase++;
		if(movePhase>=9)
			movePhase = 0;
	}
	public void incrementAttackPhase(){
		attackPhase++;
		if(attackPhase>=6){
			attackPhase = 0;
			attacking = false;
		}
	}
	public void heal(){
		health = initialHealth;
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
