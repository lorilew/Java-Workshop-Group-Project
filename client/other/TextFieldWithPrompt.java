package client.other;

import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JTextField;

/**
 * 
 * @author Joaquin de la Sierra
 *
 */

public class TextFieldWithPrompt extends JTextField {

	public static final long serialVersionUID = 1L;
	

	@Override
	protected void paintComponent(java.awt.Graphics g) {
	    super.paintComponent(g);

	    if(getText().isEmpty()){
	        Graphics2D g2 = (Graphics2D)g.create();
	        
	        File image = new File("src/res/usernametext.png");
	        
	        try {
				g2.drawImage(ImageIO.read(image), 12 ,23, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        g2.dispose(); //Dispose since I'm calling graphics.
	    }
	  }
	
}
