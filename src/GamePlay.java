import javax.swing.*;				// different swing classes has been used
import java.awt.*;					// or JPanel, listeners, as well as other functions
import java.awt.event.*;			// for any event exceptions

public class GamePlay extends JPanel implements KeyListener, ActionListener {
	private boolean play = true; // play variable used for the end game and to restart it

	private boolean bullet[] = new boolean[3]; // variable for the three bullets that the ship can fire at a time.
	private int[] bullposY = new int[3];		// stores the y coordinates of the bullets
	private int x[] = new int[3]; // stores the x-coordinates of the three bullets
	
	private boolean wavecheck[] = new boolean[3]; // variable that checks if the enemy is fully destroyed or not
	private int down[] = new int[3]; // is used to increment the y-coordinate of wave every time the frame is repainted
	private int[] check = new int[4]; // is also used to check when to generate the the waves
	
	private int score = 0;			// variable for counting score
	private int count=0;			//count is used for bullets
	private int prev = 0;			//prev is used to produce a slight delay 
	private int p=0;				// p is for the fire that generates on collision
	
	//desX and desY are collision coordinates
	private int desX;
	private int desY; 
						
						 
	// for player to move right or left
	boolean right = false; 
	boolean left = false;
		
	private Timer timer;			//used to call the javax timer class
	private int delay = 8;			// delay used in the timer

	private int playerX = 450;		// X-coordinates of the the player ship

	

	private Enemy en[] = new Enemy[3];							// Making an object of the enemy class which is used to make the enemy waves
	private Image bg, player, enemy, bull, colEnemy, colPlay;	// variables used for storing the image of all these objects
	private Color transColor;									// Color for the box around the image
	private boolean colBullet, colPlayer;						// to check for the collision of bullet and player with enemy

	
	//Constructor of the GamePlay class with no parameters passing through
	public GamePlay() {
		//Generating three enemy waves
		en[0] = new Enemy(5);
		en[0].wave(0);
		en[1] = new Enemy(3);
		en[1].wave(40);
		en[2] = new Enemy(6);
		en[2].wave(-40);
		
		//Initialising the arrays
		for (int i = 0; i <= 2; i++) {
			bullet[i] = false;
		}
		for (int i1 = 0; i1 <= 2; i1++) {
			bullposY[i1] = 0;
		}
		for (int i2 = 0; i2 <= 2; i2++) {
			x[i2] = 0;
		}
		for (int i3 = 0; i3 <= 2; i3++) {
			wavecheck[i3] = false;
		}
		
		// Adding keylisteners and time starting fucntions for the basic commands
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();

		//assigning the images to each of the image class variables
		bg = new ImageIcon("space1.gif").getImage();
		player = new ImageIcon("spaceship.gif").getImage();
		bull = new ImageIcon("bullet.png").getImage();
		colEnemy = new ImageIcon("blast.png").getImage();
		colPlay = new ImageIcon("fire.png").getImage();

		//setting the color to null
		transColor = new Color(0, 0, 0, 0);
	}

	//Function that is used to draw all the objects in the panel
	public void paintComponent(Graphics gr) {
		// background
		gr.setColor(Color.black);
		gr.fillRect(1, 1, 1000, 700);
		gr.drawImage(bg, 0, 0, 1000, 700, null);

		// drawing enemies
		en[0].draw((Graphics2D) gr, down[0]);				//draws the first wave
		prev++;
		if (prev % 4 == 0)
			down[0] += 3;
		if (down[0] > 150)									// only runs if the y-coordinate of wave 1 becomes greater than 150
			check[1] = 1;
		if (check[1] == 1) {
			en[1].draw((Graphics2D) gr, down[1]);			// draws the second wave
			if (prev % 4 == 0)
				down[1] += 3;
		}
		
		// runs if wave 1 crosses the screen and all the enemies in it have been destroyed, resets the wave to its initial condition
		if (down[0] > 600 && wavecheck[0] == false) {		
			down[0] = 0;
			for (int j = 0; j < en[0].map.length; j++) {
				en[0].setBrickValue(1, j);
			}
		}
		if (down[1] > 150)									// only runs if the y-coordinate of wave 2 becomes greater than 150
			check[2] = 1;
		if (check[2] == 1) {
			en[2].draw((Graphics2D) gr, down[2]);			// draws the third wave
			if (prev % 4 == 0)
				down[2] += 3;
		}
		
		// runs if wave 2 crosses the screen and all the enemies in it have been destroyed, resets the wave to its initial condition
		if (down[1] > 600 && wavecheck[1] == false) {		
			down[1] = 0;
			for (int j = 0; j < en[1].map.length; j++) {
				en[1].setBrickValue(1, j);
			}
		}
		
		// runs if wave 3 crosses the screen and all the enemies in it have been destroyed, resets the wave to its initial condition
		if (down[2] > 600 && wavecheck[2] == false) {		
			down[2] = 0;
			for (int j = 0; j < en[2].map.length; j++) {
				if (en[2].map[j] == 0)
					en[2].setBrickValue(1, j);
			}
		}
		
		// runs if collision of the bullet with enemy is true, draws the fire or blast image
		if (colBullet) {									

			gr.drawImage(colEnemy, desX, desY, 80, 120, null);

			if (p % 4 == 0) {
				colBullet = false;
				p = 0;
			}
			p++;
		}

		// the player
		gr.setColor(Color.blue);
		gr.fillRect(playerX, 570, 80, 100);
		gr.drawImage(player, playerX, 570, 80, 100, null);

		//runs if collision of the ship with enemy is true, draws the fire or blast image
		if (colPlayer) {

			gr.drawImage(colPlay, desX, desY, 100, 120, null);

			if (p % 4 == 0) {
				colPlayer = false;
				p = 0;
			}
			p++;
		}

		// draws the bullet on the screen when z is presses, a max of three bullets can exist on screen at a time

		for (int i = 0; i <= 2; i++) {
			if (bullet[i] == true) {
				gr.setColor(transColor);
				gr.fillRect(x[i], bullposY[i], 10, 20);
				gr.drawImage(bull, x[i], bullposY[i], 10, 20, null);
				if (bullposY[i] > 0) {
					bullposY[i] -= 5;
				} else {
					bullposY[i] = 0;
					bullet[i] = false;
				}
			}
		}
		
		// runs when right arrow key is pressed
		if (right && playerX < (1000 - 100)) {
			play = true;
			playerX += 3;
		}
		
		// runs when left arrow key is pressed
		if (left && playerX >= 5) {
			play = true;
			playerX -= 3;
		}
		
		// checks whether the whole enemy wave has been destroyed or not, set the variable to true if it is not fully destroyed
		for (int y = 0; y <= 2; y++) {
			for (int col = 0; col < en[y].map.length; col++) {
				if (en[y].map[col] == 1) {
					wavecheck[y] = true;
					break;
				} else
					wavecheck[y] = false;
			}
		}
		
		// Check for when to end the game
		for (int y = 0; y <= 2; y++) {
			
			// checks if an enemy exists and whether it has reached the end of screen or not
			if (down[y] - 10 > 620 && wavecheck[y] == true) {
				play = false;
				for (int i = 0; i <= 2; i++) {
					//values are initialised
					down[i] = 0;
					check[i] = 0;
					for (int j = 0; j < en[i].map.length; j++) {
						en[i].setBrickValue(1, j);
					}
				}
				// displays Game Over and the scores when game ends
				gr.setColor(Color.RED);
				gr.setFont(new Font("serif", Font.BOLD, 30));
				gr.drawString("Game Over, Score: " + score, 230, 300);
				
				// displays the string to show how to restart game
				gr.setFont(new Font("serif", Font.BOLD, 30));
				gr.drawString("Press Enter to restart", 230, 350);
			}

			// scores
			gr.setColor(Color.WHITE);
			gr.setFont(new Font("serif", Font.BOLD, 10));
			gr.drawString("" + score, 890, 30);
		}
	}
	
	// performs certain actions every second
	@Override
	public void actionPerformed(ActionEvent e) {
		// runs only if the play is true
		if (play) {
			timer.start();
			
			// checks for the collision of bullet with enemy
			collision(0);
			collision(1);
			collision(2);
			
			// paints the all the components again after each small delay
			repaint();
		}
	}
	
	//Runs in case a certain key is released
	@Override
	public void keyReleased(KeyEvent e) {
		
		// Runs when right arrow key is released
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			right = false;
		}
		
		// Runs when left arrow key is released
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			left = false;
		}

	}

	//added just to override the method so the class can be used
	@Override
	public void keyTyped(KeyEvent e) {
	}

	// Runs in case a certain key is pressed
	@Override
	public void keyPressed(KeyEvent e) {
		
		// Runs when right arrow key is pressed, moves the ship to right
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			right = true;

		}
		
		// Runs when left arrow key is pressed, moves the ship to left
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			left = true;

		}
		
		// Runs when z key is pressed, bullets are generated
		if (e.getKeyCode() == KeyEvent.VK_Z) {
			if (count == 3) {
				count = 0;
			}
			if (bullposY[count] == 0) {
				bullet[count] = true;
				x[count] = playerX + 34;
				bullposY[count] = 550;
				count++;
			}
		}
		
		// Runs when enter key is pressed, used to restart the game when it ends
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (!play) {
				// variables are initialised for the new game
				play = true;
				for (int i = 0; i <= 2; i++) {
					bullet[i] = false;
					bullposY[i] = 0;
					x[i] = 0;
					count = 0;
				}
				for (int y = 0; y <= 2; y++) {
					wavecheck[y] = false;
				}
				count = 0;
				score = 0;
				playerX = 450;
				repaint();
			}
		}
	}

	// Function made for checking the collision of bullets with enemies
	public void collision(int i) {
		// Loop runs for the length of en array size
		A: for (int j = 0; j < en[i].map.length; j++) {

			// Runs if an objects exists at that specific point
			if (en[i].map[j] > 0) {
				int brickX = j * en[i].brickWidth + 80 + en[i].rnd[j];
				int brickY = down[i] - 10; 
				int brickWidth = en[i].brickWidth; 
				int brickHeight = en[i].brickHeight;

				Rectangle EnemyRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);   		// a rectangle is made using enemy coordinates
				Rectangle playerRect = new Rectangle(playerX, 570, 80, 100);							// a rectangle made using the player ship
				
				//Checks when the player ship collides with the enemy ship
				if (playerRect.intersects(EnemyRect)) {
					colPlayer = true;
					desX = brickX - 15;
					desY = brickY;
					en[i].setBrickValue(0, j);
					colPlayer = true;
				}
				
				//Runs for all three bullets
				for (int k = 0; k <= 2; k++) {
					Rectangle bullRect = new Rectangle(x[k], bullposY[k], 10, 20);						// a rectangle for bullet is created
					
					// runs only if a bullet collides with an enemy
					if (bullRect.intersects(EnemyRect)) {
						en[i].setBrickValue(0, j);
						desX = brickX - 10;
						desY = bullposY[k] - 80;
						colBullet = true;
						score += 5;
						bullet[k] = false;
						x[k] = 0;
						bullposY[k] = 0;

						break A;										// breaks after it checks if any of the three bullets has collided with the enemy and continues the above for loop
					}
				}
			}
		}
	}

}
