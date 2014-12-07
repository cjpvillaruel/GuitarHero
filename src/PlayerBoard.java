import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;


public class PlayerBoard extends JPanel implements Constants, ActionListener {
	Image background;
	private Timer timer;
	int time;
	int x= 0, y=0,width= 340, height=5;
	Image pink, blue, violet, green;
	ArrayList <Circle> circles;
	public PlayerBoard(ArrayList<Circle> circles){
		this.circles = circles;
		this.setBackground(Color.CYAN);
		background = Toolkit.getDefaultToolkit().getImage( "images/playerboard.jpg" );
		pink =  Toolkit.getDefaultToolkit().getImage( "images/pink.png" );
		green =  Toolkit.getDefaultToolkit().getImage( "images/green.png" );
		violet =  Toolkit.getDefaultToolkit().getImage( "images/violet.png" );
		blue =  Toolkit.getDefaultToolkit().getImage( "images/blue.png" );
		this.setDoubleBuffered( true );
		 this.timer = new Timer( 5, this );
	
		 
		// System.out.println(circles.size());
		 time=0;
		//timer.start();

	}
	
	public void startGame(){
		this.setFocusable(true);
		this.requestFocusInWindow();
		timer.start();
		// Extending KeyAdapter allows to override only the methods of interest
        this.addKeyListener( new TAdapter() );
	}
	public void drawCenteredCircle(Graphics2D g, int x, int y, int r1,int r2) {
		  x = x-(r1/2);
		  y = y-(r1/2);
		  g.setColor(new Color(9, 101, 254));
		  g.fillOval(x,y,r1,r2);
		}
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
		Graphics2D g2d = (Graphics2D)g;
		//g.drawLine(x, y, x+50, y);
		g.setColor(Color.BLACK);
		if(y <= 330)
		g.fillRect(x, y, width, height);

		//drawCenteredCircle(g2d, x, y, width,height);
		//draw Circles
		
		 for (Circle circle : circles){
			 if( circle.y >= 0 && circle.y <= 330){
				 g.setColor(circle.color);
				 g.fillOval(circle.x,circle.y,circle.width,circle.height);
			 }
		 }
		
		
		 g.drawImage(green, 10, 275, this);
		 g.drawImage(pink, 95, 275, this);
		 g.drawImage(blue, 167, 275, this);
		 g.drawImage(violet, 250, 275, this);
		Toolkit.getDefaultToolkit().sync();
		g.dispose();

	}
    public void actionPerformed( ActionEvent event ) {
    	//System.out.println(time);
    	time++;
    	y++;
    	for (Circle circle : circles){
			circle.y++;
		 }
        repaint();

    }
    private class TAdapter extends KeyAdapter {

        public void keyReleased( KeyEvent event ) {
            System.out.print("release");
        }

        public void keyPressed( KeyEvent event ) {
           System.out.print("press");
        }

    }

}
