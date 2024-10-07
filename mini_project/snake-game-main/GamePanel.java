import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	static final int WIDTH = 500;
	static final int HEIGHT = 500;
	static final int UNIT_SIZE = 20;
	static final int NUMBER_OF_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);

	final int x[] = new int[NUMBER_OF_UNITS];
	final int y[] = new int[NUMBER_OF_UNITS];

	int length = 5;
	int foodEaten;
	int foodX;
	int foodY;
	char direction = 'D';
	boolean running = false;
	boolean gameOver = false;
	Random random;
	Timer timer;

	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(Color.DARK_GRAY);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		play();
	}

	public void play() {
		addFood();
		running = true;
		gameOver = false;
		timer = new Timer(80, this);
		timer.start();
	}

	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		draw(graphics);
	}

	public void move() {
		for (int i = length; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}

		switch (direction) {
			case 'L' -> x[0] = x[0] - UNIT_SIZE;
			case 'R' -> x[0] = x[0] + UNIT_SIZE;
			case 'U' -> y[0] = y[0] - UNIT_SIZE;
			case 'D' -> y[0] = y[0] + UNIT_SIZE;
		}
	}

	public void checkFood() {
		if (x[0] == foodX && y[0] == foodY) {
			length++;
			foodEaten++;
			addFood();
		}
	}

	public void draw(Graphics graphics) {
		if (running) {
			graphics.setColor(new Color(210, 115, 90));
			graphics.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

			graphics.setColor(Color.white);
			graphics.fillRect(x[0], y[0], UNIT_SIZE, UNIT_SIZE);

			for (int i = 1; i < length; i++) {
				graphics.setColor(new Color(40, 200, 150));
				graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
			}

			graphics.setColor(Color.white);
			graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 25));
			FontMetrics metrics = getFontMetrics(graphics.getFont());
			graphics.drawString("Score: " + foodEaten, (WIDTH - metrics.stringWidth("Score: " + foodEaten)) / 2, graphics.getFont().getSize());

		} else {
			gameOver(graphics);
		}
	}

	public void addFood() {
		foodX = random.nextInt((int) (WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		foodY = random.nextInt((int) (HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
	}

	public void checkHit() {
		for (int i = length; i > 0; i--) {
			if (x[0] == x[i] && y[0] == y[i]) {
				running = false;
			}
		}

		if (x[0] < 0 || x[0] > WIDTH || y[0] < 0 || y[0] > HEIGHT) {
			running = false;
		}

		if (!running) {
			timer.stop();
			gameOver = true;
		}
	}

	public void gameOver(Graphics graphics) {
		graphics.setColor(Color.red);
		graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 50));
		FontMetrics metrics = getFontMetrics(graphics.getFont());
		graphics.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 2);

		graphics.setColor(Color.white);
		graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 25));
		metrics = getFontMetrics(graphics.getFont());
		graphics.drawString("Score: " + foodEaten, (WIDTH - metrics.stringWidth("Score: " + foodEaten)) / 2, graphics.getFont().getSize());

		graphics.drawString("Press Enter to Restart", (WIDTH - metrics.stringWidth("Press Space to Restart")) / 2, HEIGHT / 2 + 50);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (running) {
			move();
			checkFood();
			checkHit();
		}
		repaint();
	}

	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					if (direction != 'R' && running) {
						direction = 'L';
					}
					break;

				case KeyEvent.VK_RIGHT:
					if (direction != 'L' && running) {
						direction = 'R';
					}
					break;

				case KeyEvent.VK_UP:
					if (direction != 'D' && running) {
						direction = 'U';
					}
					break;

				case KeyEvent.VK_DOWN:
					if (direction != 'U' && running) {
						direction = 'D';
					}
					break;

				case KeyEvent.VK_ENTER:
					if (gameOver) {
						resetGame();
					}
					break;
			}
		}
	}

	public void resetGame() {
		length = 5;
		foodEaten = 0;
		direction = 'D';
		for (int i = 0; i < NUMBER_OF_UNITS; i++) {
			x[i] = 0;
			y[i] = 0;
		}
		play();
	}
}
