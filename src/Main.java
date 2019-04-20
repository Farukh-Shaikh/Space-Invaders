import javax.swing.JFrame;										// imported so that the JFrame class can be instantiated in program

public class Main {

	public static void main(String[] args) {
		JFrame frame = new JFrame();							// Class of JFrame is instantiated
		GamePlay gameplay = new GamePlay();						// Gameplay class's object has been created
		frame.setSize(1000, 700);								// setting the length and width of the frame
		frame.setLocationRelativeTo(null);						//to create frame at middle of screen
		frame.setTitle("Space Invaders");
		frame.setResizable(false);
		frame.setVisible(true);									// Setting it true so its visible on screen
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// so the program closes when x is pressed on the screen
		frame.add(gameplay);									// gameplay and all its components are added to the frame
	}

}
