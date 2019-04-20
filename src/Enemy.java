import java.awt.*;							//awt lib imported for Graphics and color 
import javax.swing.ImageIcon;				// imported for the Image class


public class Enemy {
		int rnd[] = new int[7];
		long x = 0;
		public int map[];				// a single array for one wave of enemy
		public int brickWidth;			// width of the enemy
		public int brickHeight;			// height of the enemy
		
		private Image e;				// variable of image for adding enemy image
		private Color transColor;		// for setting the background color of enemy object
		
		//Array is initialised
		public Enemy(int col) {
			map = new int[col];
				for(int j = 0; j<map.length;j++) {
					map[j] = 1;
				}
			
			// hardcoding the height and width of enemy
			brickWidth = 60;
			brickHeight = 60;
			
			// setting the background to null and importing the enemy image
			transColor=new Color(0,0,0,0);
			e=new ImageIcon("enemy.png").getImage();
		}
		
		// For setting different x coordinates for each wave and its objects
		public void wave(int x) {
			rnd[0] = 0;
			for(int j = 1; j<map.length;j++) {
				rnd[j] = 100 + x;
			}
			for(int j = 1; j<map.length;j++) {
				rnd[j] += rnd[j-1];
			}
		}
		
		// Function that draws the wave objects on specific coordinates, parameters that are acquired are 2dGraphics and int 
		public void draw(Graphics2D g,int down) {
			
				// Runs for the length of array
				for(int j = 0; j<map.length;j++) {
					// runs only if the particular object of the waves is 1 that is exists and needs to be drawn
					if(map[j] >0) {
						g.setColor(transColor);
						g.fillRect(j * brickWidth  +80+ rnd[j], down-10, brickWidth, brickHeight);
						g.drawImage(e, j * brickWidth  +80+ rnd[j], down-10, brickWidth, brickHeight,null);
						g.setStroke(new BasicStroke(20));										// makes spaces between the objects, stopping them from appearing as one big rectangle
						g.setColor(Color.black);
						g.drawRect(j*brickWidth  +80+ rnd[j], down-10, brickWidth, brickHeight);
					}
				}
		}
		
		// Sets the value of map[x] accordingly if the enemy exits then 1 if not then 0
		public void setBrickValue(int value, int col) {
			map[col] = value;
		}
	

}
