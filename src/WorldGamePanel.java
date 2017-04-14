import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
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
@SuppressWarnings("serial")
public class WorldGamePanel extends JPanel implements Runnable{
	Thread t;
	ArrayList<Character> characters = new ArrayList<Character>();
	BufferedImage castle;
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
		characters.add(new GoldenKnight(700,600));
		JFrame frame = new JFrame("Slashin Nash");
		frame.add(this);
		this.setLayout(null);
		frame.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
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

		this.getActionMap().put("A", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				characters.get(0).changeXVelocity(-characters.get(0).getSpeed());
				characters.get(0).changeDirection(Direction.LEFT);
			}

		});
		this.getActionMap().put("D", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				characters.get(0).changeXVelocity(characters.get(0).getSpeed());
				characters.get(0).changeDirection(Direction.RIGHT);
			}

		});
		this.getActionMap().put("W", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				characters.get(0).changeYVelocity(-characters.get(0).getSpeed());
				characters.get(0).changeDirection(Direction.UP);
			}

		});
		this.getActionMap().put("S", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				characters.get(0).changeYVelocity(characters.get(0).getSpeed());
				characters.get(0).changeDirection(Direction.DOWN);
			}

		});
		this.getActionMap().put("rA", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				characters.get(0).changeXVelocity(0);

			}

		});
		this.getActionMap().put("rD", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				characters.get(0).changeXVelocity(0);
			}

		});
		this.getActionMap().put("rW", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				characters.get(0).changeYVelocity(0);
			}

		});
		this.getActionMap().put("rS", new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				characters.get(0).changeYVelocity(0);

			}

		});

		
		
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
		repaint();
	}
	public void updateCharacterLocations(){
		for(int index = 0; index < characters.size(); index++){
			Character c = characters.get(index);
		if(c.getX()<=0){
			c.changeX(c.getSpeed());
		}
		if(c.getY()<=230){ //trees and grass
			c.changeY(c.getSpeed());
		}
		if(c.getY()>=Toolkit.getDefaultToolkit().getScreenSize().height-120){ //120 pixel buffer
			c.changeY(-c.getSpeed());
		}
		if(c.getX()>950&&c.getY()>430){
			c.changeY(-c.getSpeed());
		}
		if(c.getX()>950&&c.getY()<380){
			c.changeY(c.getSpeed());
		}
		if(c.getX()>1115){
			System.out.println("in castle!");
		}
		c.changeX(c.getXVelocity());
		c.changeY(c.getYVelocity());
		}
		
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		drawBackDrop(g);
		drawCharacters(g);
	}
	public void drawBackDrop(Graphics g){
		g.drawImage(castle, 0, 0,Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height,null);
	}
	public void drawCharacters(Graphics g){
		for(int index = 0; index < characters.size(); index++)
		characters.get(index).draw(g, characters.get(index).getX(), characters.get(index).getY());
	}
	
}
