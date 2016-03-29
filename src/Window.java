import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;



public class Window extends JPanel{

	static Bus bus = new Bus();

	double conv = .62222222222222;

	boolean instantiatedImage = false;

	static boolean u = false;
	static boolean d = false;
	static boolean l = false;
	static boolean r = false;
	Color desertFar = new Color(255, 239, 69);
	Color sky = new Color(88, 211, 255);
	Color desertClose = new Color(173, 159, 0);

	static int[] xRoadPts = {-700, 350, 900};
	static int[] yRoadPts = {525, 100, 525};

	static int[] xMPHNeedlePts = {-40, 0, 0}; 
	static int[] yMPHNeedlePts = {0, -3, 3}; 


	static int angle;

	static Image busImage;


	public static void main(String [] args) throws InterruptedException{
		JFrame frame = new JFrame("Desert Bus 2");
		Window game = new Window();
		frame.add(game);
		frame.setSize(700, 525);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);


		AbstractAction rightArrow = new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
		        xRoadPts[0]-= bus.speed/5;
//		        xRoadPts[1]-=5;
		        xRoadPts[2]-=bus.speed/5;
		    }
		};

		AbstractAction leftArrow = new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
		    	xRoadPts[0]+=bus.speed/5;
//		        xRoadPts[1]+=5;
		        xRoadPts[2]+=bus.speed/5;
		    }
		};

		AbstractAction upArrowRelease = new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
		        u = false;
		    }
		};
//
		AbstractAction downArrowRelease = new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
		        d = false;
		    }
		};

		AbstractAction upArrow = new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
		        u = true;
		    }
		};

		AbstractAction downArrow = new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
		    	d = true;
		    }
		};

		InputMap im = game.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
	    ActionMap am = game.getActionMap();

	    //Key Presses
	    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "RightArrow");
	    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "LeftArrow");
	    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "UpArrow");
	    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "DownArrow");

	    am.put("RightArrow", rightArrow);
	    am.put("LeftArrow", leftArrow);
	    am.put("UpArrow", upArrow);
	    am.put("DownArrow", downArrow);

	    //Key releases
	    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "releaseUpArrow");
	    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "releaseDownArrow");

	    am.put("releaseUpArrow", upArrowRelease);
	    am.put("releaseDownArrow", downArrowRelease);

		while(true){
			//main loop
			frame.repaint();
			bus.accelerate();
			bus.speed = bus.calculateSpeed();
			Thread.sleep(50);
		}

	}
	@Override
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D gd = (Graphics2D) g;
//		gd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
//		AffineTransform current = gd.getTransform();
//		AffineTransform to = AffineTransform.getRotateInstance(Math.toRadians(angle), 350, 263);		
		gd.setColor(desertFar);
		gd.fillRect(0, 100, 700, 525);
		gd.setColor(sky);
		gd.fillRect(0, 0, 700, 100);
		int[] newX = new int[3];
		newX[0] = xRoadPts[0] - 150;
		newX[1] = xRoadPts[1];
		newX[2] = xRoadPts[2] + 150;

		gd.setColor(desertClose);
		gd.fillPolygon(newX, yRoadPts, yRoadPts.length);

		gd.setColor(Color.LIGHT_GRAY);
//		gd.setTransform(to);
		gd.fillPolygon(xRoadPts, yRoadPts, xRoadPts.length);
//		gd.setTransform(current);
		gd.setColor(Color.yellow);
		int[] xLinePts = new int[3];
		xLinePts[0] = xRoadPts[0] + 775;
		xLinePts[1] = xRoadPts[1];
		xLinePts[2] = xRoadPts[2] - 775;

		int[] yLinePts = {525, 100, 525};
		gd.fillPolygon(xLinePts, yLinePts, xLinePts.length);


		gd.setColor(Color.white);
		gd.drawPolygon(xRoadPts, yRoadPts, xRoadPts.length);


//		gd.drawString("GEAR " + String.valueOf(bus.currentGear), 100, 100);
//		gd.drawString("RPM " + String.valueOf(bus.engineRPM), 100, 120);
//		gd.drawString("SPEED " + String.valueOf(bus.speed), 100, 140);

		if(!instantiatedImage){

			try {
				busImage = ImageIO.read(this.getClass().getResource("transparent db.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

//			int newWidth = (int)(busImage.getWidth(null)*conv);
//			int newHeight = (int)(busImage.getHeight(null) * conv);


		}

		gd.drawImage(busImage, 0, 0, 700, 525, null);
		drawRPM(gd);
		drawMPH(gd);

		gd.setFont(new Font("Courier", 12, 12));
		gd.setColor(Color.GREEN);

		gd.drawString(String.format("%.1fmi", bus.totalMiles), 170, 350);

	}

	public void drawRPM(Graphics2D gd){

		gd.setColor(Color.black);

		AffineTransform current = gd.getTransform();
		double theta = Math.toRadians(-45 + (.0675*bus.engineRPM));
		AffineTransform newT = AffineTransform.getRotateInstance(theta, 95, 355);
		gd.setTransform(newT);
		gd.translate(95, 355);
		gd.fillPolygon(xMPHNeedlePts, yMPHNeedlePts, xMPHNeedlePts.length);
		gd.setTransform(current);
	}

	public void drawMPH(Graphics2D gd){

		gd.setColor(Color.black);

		AffineTransform current = gd.getTransform();
		double theta = Math.toRadians(-45 + (3.375*bus.speed));
		AffineTransform newT = AffineTransform.getRotateInstance(theta, 315, 355);
		gd.setTransform(newT);
		gd.translate(315, 355);
		gd.fillPolygon(xMPHNeedlePts, yMPHNeedlePts, xMPHNeedlePts.length);
		gd.setTransform(current);

	}

}
