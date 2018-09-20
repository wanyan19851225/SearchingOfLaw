package Demo;

import java.io.IOException;
import java.text.ParseException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.NebulaBrickWallSkin;
import org.pushingpixels.substance.api.skin.SubstanceNebulaBrickWallLookAndFeel;

import Com.DisplayGui;

public class Demo{
	
	public static void main(String[] args) throws IOException, ParseException, UnsupportedLookAndFeelException {	

		JFrame.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){ 
				try {
					UIManager.setLookAndFeel(new SubstanceNebulaBrickWallLookAndFeel());
					SubstanceLookAndFeel.setSkin(new NebulaBrickWallSkin());
					new DisplayGui();
				} catch (UnsupportedLookAndFeelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}); 	
	}
}
