package gameClient;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;


/**
 * This class represents a very simple GUI class to present a
 * game on a graph - you are welcome to use this class - yet keep in mind
 * that the code is not well written in order to force you improve the
 * code and not to take it "as is".
 *
 */
public class MyFrame extends JFrame{
	private int _ind;
	private Arena _ar;
	private gameClient.util.Range2Range _w2f;
	private MyPanel panel;
	private static long time;
	private Image backgroundImage ;
	
	public MyFrame(String a, long time) {
		super(a);
		int _ind = 0;
		this.time = time;
		panel = new MyPanel(_ind, _ar, _w2f, time);
		this.add(panel);
		try {
			backgroundImage = ImageIO.read(new File("data/screen.jpg"));
		} catch (IOException e) {
		
			e.printStackTrace();
		}
	}
	
	public void update(Arena ar) {
		panel.update(ar);
	}
	
	public MyPanel getPanel() {
		return panel;
	}
	
	
	 
	
}