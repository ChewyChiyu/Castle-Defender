import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
@SuppressWarnings("serial")
public class WorldGamePanel extends JPanel implements Runnable{
	Thread t;
	Timer worldTick;
	ArrayList<Character> characters = new ArrayList<Character>();
	int lives = 5;
	GoldenKnight player = new GoldenKnight((int) (Toolkit.getDefaultToolkit().getScreenSize().width*.8),(int) (Toolkit.getDefaultToolkit().getScreenSize().height*.5));
	BufferedImage castle;
	Castle base = new Castle(100);
	boolean isRunning;
	public static void main(String[] args){
		new WorldGamePanel();
	}
	public WorldGamePanel(){
		setUpPanel();
		setUpBackDrop();
		start();
	}
	public void setUpBackDrop(){
		URL imageUrl = getClass().getResource("imgs/castleBackDrop.jpg");
		try {
			castle = ImageIO.read(imageUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public void setUpPanel(){
		characters.add(player);
		JFrame frame = new JFrame("Slashin Nash");
		frame.add(this);
		this.setLayout(null);
		frame.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("A"), "A");
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("S"), "S");
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("D"), "D");
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("W"), "W");

		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released A"), "rA");
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released S"), "rS");
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released D"), "rD");
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released W"), "rW");

		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "SPACE");
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released SPACE"), "rSPACE");

		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("P"), "pause");
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "SPACE");

		this.getActionMap().put("A", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {

				player.changeXVelocity(-player.getSpeed());
				player.changeDirection(Direction.LEFT);
			}

		});
		this.getActionMap().put("D", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				player.changeXVelocity(player.getSpeed());
				player.changeDirection(Direction.RIGHT);
			}

		});
		this.getActionMap().put("W", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				player.changeYVelocity(-player.getSpeed());
				player.changeDirection(Direction.UP);
			}

		});
		this.getActionMap().put("S", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				player.changeYVelocity(player.getSpeed());
				player.changeDirection(Direction.DOWN);
			}

		});
		this.getActionMap().put("rA", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				player.changeXVelocity(0);

			}

		});
		this.getActionMap().put("rD", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				player.changeXVelocity(0);
			}

		});
		this.getActionMap().put("rW", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				player.changeYVelocity(0);
			}

		});
		this.getActionMap().put("rS", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				player.changeYVelocity(0);

			}

		});

		this.getActionMap().put("SPACE", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				player.attack();
			}

		});
		this.getActionMap().put("pause", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(isRunning){
					stop();
				}else
					start();
			}

		});
		worldTick = new Timer(2000,e->{
			spawnInMonster();
			checkForPlayerDeath();
		});
		worldTick.start();	
		}
	public void spawnInMonster(){
		characters.add(new Ogre(0,(int)(Math.random()*300)+300,Direction.RIGHT));
	}
	public synchronized void start(){
		t = new Thread(this);
		isRunning = true;
		t.start();
	}
	public synchronized void stop(){
		try{
			isRunning = false;
			t.join();
		}catch(Exception e){

		}
	}
	public void run(){
		while(isRunning){
			try{
				updatePanel();
				Thread.sleep(10);
			}catch(Exception e){

			}
		}
	}
	public void updatePanel(){
		updateCharacterLocations();
		checkForWounds();
		repaint();
	}
	
	public void checkForPlayerDeath(){
		if(characters.indexOf(player)==-1){
			player = new GoldenKnight((int) (Toolkit.getDefaultToolkit().getScreenSize().width*.8),(int) (Toolkit.getDefaultToolkit().getScreenSize().height*.5));
			characters.add(player);
			lives--;
		}
	}
	public void checkForWounds(){
		for(int index = 0; index < characters.size(); index++){
			Character c = characters.get(index);
			int swordPointX, swordPointY;
			if(c.isAttacking()){
				switch(c.getDirection()){
				case UP: 
					swordPointX = c.getX()+(c.getWidth()/2);
					swordPointY = c.getY();
					for(int index2 = 0; index2 < characters.size(); index2++){
						if(!c.equals(characters.get(index2))){
							int bodyX1 = characters.get(index2).getX();
							int bodyX2 = characters.get(index2).getX() + characters.get(index2).getWidth();
							int bodyY1 = characters.get(index2).getY();
							int bodyY2 = characters.get(index2).getY() + characters.get(index2).getHeight();
							if(bodyX1<=swordPointX&&bodyX2>=swordPointX&&bodyY1<=swordPointY&&bodyY2>=swordPointY){
								if(characters.get(index2).fatalDamage(c.getPower())){
									characters.remove(characters.get(index2));
								}
							}
						}
					}
					break;
				case DOWN: 
					swordPointX = c.getX()+(c.getWidth()/2);
					swordPointY = c.getY()+c.getHeight();
					for(int index2 = 0; index2 < characters.size(); index2++){
						if(!c.equals(characters.get(index2))){
							int bodyX1 = characters.get(index2).getX();
							int bodyX2 = characters.get(index2).getX() + characters.get(index2).getWidth();
							int bodyY1 = characters.get(index2).getY();
							int bodyY2 = characters.get(index2).getY() + characters.get(index2).getHeight();
							if(bodyX1<=swordPointX&&bodyX2>=swordPointX&&bodyY1<=swordPointY&&bodyY2>=swordPointY){
								if(characters.get(index2).fatalDamage(c.getPower())){
									characters.remove(characters.get(index2));
								}
							}
						}
					}
					break;	
				case LEFT:
					swordPointX = c.getX();
					swordPointY = c.getY()+c.getHeight()/2;
					for(int index2 = 0; index2 < characters.size(); index2++){
						if(!c.equals(characters.get(index2))){
							int bodyX1 = characters.get(index2).getX();
							int bodyX2 = characters.get(index2).getX() + c.getWidth();
							int bodyY1 = characters.get(index2).getY();
							int bodyY2 = characters.get(index2).getY() + c.getHeight();
							if(bodyX1<=swordPointX&&bodyX2>=swordPointX&&bodyY1<=swordPointY&&bodyY2>=swordPointY){
								if(characters.get(index2).fatalDamage(c.getPower())){
									characters.remove(characters.get(index2));
								}
							}
						}
					}
					break;
				case RIGHT:
					swordPointX = c.getX()+c.getWidth();
					swordPointY = c.getY()+c.getHeight()/2;
					for(int index2 = 0; index2 < characters.size(); index2++){
						if(!c.equals(characters.get(index2))){
							int bodyX1 = characters.get(index2).getX();
							int bodyX2 = characters.get(index2).getX() + c.getWidth();
							int bodyY1 = characters.get(index2).getY();
							int bodyY2 = characters.get(index2).getY() + c.getHeight();
							if(bodyX1<=swordPointX&&bodyX2>=swordPointX&&bodyY1<=swordPointY&&bodyY2>=swordPointY){
								if(characters.get(index2).fatalDamage(c.getPower())){
									characters.remove(characters.get(index2));
								}
							}
						}
					}
					break;
				}
			}
		}
	}
	public void updateCharacterLocations(){
		for(int index = 0; index < characters.size(); index++){
			
			Character c = characters.get(index);
			if(c.isAttacking())
				continue;				//no moving while attacking
			if(c.getX()<=0){
				c.changeX(c.getSpeed());
			}
			if(c.getY()<=Toolkit.getDefaultToolkit().getScreenSize().height/3){ //trees and grass
				c.changeY(c.getSpeed());
			}
			if(c.getY()>=Toolkit.getDefaultToolkit().getScreenSize().height){ //120 pixel buffer
				c.changeY(-c.getSpeed());
			}
			if(c.getX()>(int) (Toolkit.getDefaultToolkit().getScreenSize().width*.7)&&c.getY()<(int) (Toolkit.getDefaultToolkit().getScreenSize().height*.5)){
				c.changeY(c.getSpeed());
			}
			if(c.getX()>(int) (Toolkit.getDefaultToolkit().getScreenSize().width*.7)&&c.getY()>(int) (Toolkit.getDefaultToolkit().getScreenSize().height*.6)){
				c.changeY(-c.getSpeed());
			}
			if(c.getX()>1000){
				if(index!=0){
				characters.remove(c);
				base.fatalDamage(c.getPower());
				}
			}
			c.changeX(c.getXVelocity());
			c.changeY(c.getYVelocity());
		}

	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		drawBackDrop(g);
		drawCharacters(g);
		drawBase(g);
		drawLives(g);
	}
	public void drawLives(Graphics g){
		int xBuffer = Toolkit.getDefaultToolkit().getScreenSize().width/2-100; //100 pixel buffer
		for(int index = 0; index < lives; index++){
			g.fillRect(xBuffer, 30, 70, 70);
			xBuffer+=80;
		}
	}
	public void drawBackDrop(Graphics g){
		g.drawImage(castle, 0, 0,Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height,null);
	}
	public void drawBase(Graphics g){
		base.draw(g);
	}
	public void drawCharacters(Graphics g){
		for(int index = 0; index < characters.size(); index++){
			if(characters.get(index).isAttacking()){
				characters.get(index).drawAttack(g, characters.get(index).getX(), characters.get(index).getY());
				continue;
			}
			characters.get(index).drawMove(g, characters.get(index).getX(), characters.get(index).getY());
			
		}
	}

}
