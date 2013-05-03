 package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.listeners.ClientListener;
import net.listeners.GameListener;

import main.entities.Player;


public class Game implements ClientListener {
	private ArrayList<Player> players;
	private ArrayList<GameListener> gamers;
	private MyCanvas canvas;
	
	public Game() {
		players = new ArrayList<Player>();
		gamers = new ArrayList<GameListener>();
	}
	
	public void addGameListener(GameListener gamer) {
		gamers.add(gamer);
	}
	
	
	@Override
	public void onClientConnected(Player player) {
		players.add(player);
		
		for(Player p : players) {
			System.out.println("Game.onClientConnected() current players : "+p);
		}
		
		if(players.size() > 1) {
			startGame();
		}
	}

	private void startGame() {
		prepareStadium();
	}

	private void prepareStadium() {
		JFrame stadiumFrame = new JFrame("Stadium");
		stadiumFrame.setSize(getStadiumDimension());
		stadiumFrame.setLocationRelativeTo(null);
		stadiumFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		canvas = new MyCanvas();
		canvas.setBackground(Color.DARK_GRAY);
		stadiumFrame.add(canvas);
		stadiumFrame.setVisible(true);
		startGameLoop();
	}
	

	private void startGameLoop() {
		while(true) {
			canvas.cool_move();
			sleepThread();
		}
	}

	private void sleepThread() {
		try {
			Thread.sleep(10);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	@SuppressWarnings("unused")
	class MyCanvas extends JPanel {
		private static final long serialVersionUID = 1L;

		public final static int DIAMETER = 40;
		private int x;
		private int y;
		private boolean moveRight;
		private boolean moveLeft;
		private boolean moveUp;
		private boolean moveDown;

		
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			drawElement(g);
		}

		
		private void drawElement(Graphics g) {
			g.setColor(Color.red);
			g.fillOval(x, y, DIAMETER, DIAMETER);
		}
		
		
		public void cool_move() {
			if(moveLeft)
				moveLeft();
			else 
				moveRight();
			
			if(moveDown)
				moveDown();
			else
				moveUp();

			repaint();
			if(x > 500) {
				gamers.get(0).move((y ), x- 540);
			} else {
				gamers.get(1).move(x, y);
			}
			
//			for(GameListener gamer : gamers)
//				gamer.move(x, y, DIAMETER);
		}
		
		
		private void moveUp() {
			y -= 1;
			
			if(y <= 0) {
				moveUp = false;
				moveDown = true;
			}
				
		}

	
		private void moveDown() {
			y += 1;
			
			if(y >= this.getHeight() - DIAMETER) {
				moveDown = false;
				moveUp = true;
			}
		}

		
		private void moveRight() {
			x -= 1;
			
			if(x <= 0) {
				moveLeft = true;
				moveRight = false;
			}
		}

		
		private void moveLeft() {
			x += 1;

			if(x >= this.getWidth() - DIAMETER) {
				moveLeft = false;
				moveRight = true;
			}
		}
		
	}
	
	
	private Dimension getStadiumDimension() {
		int width = 0;
		
		for(Player p : players) {
			width += p.getDimension().width;
		}		
		
		return new Dimension(width, players.get(0).getDimension().height);
	}

}
