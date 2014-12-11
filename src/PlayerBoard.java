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
	boolean f1=false, f2=false, f3=false, f4=false, space = false;
	ArrayList <Circle> circles;
	boolean isPlayer;
	GamePanel gamePanel;
	int incre=0;
	ArrayList<Integer> circlesY;
	public PlayerBoard(ArrayList<Circle> circles, boolean isPlayer1, GamePanel gamePanel){
		this.circles = circles;
		this.setBackground(Color.CYAN);
		background = Toolkit.getDefaultToolkit().getImage( "images/playerboard1.jpg" );
		pink =  Toolkit.getDefaultToolkit().getImage( "images/pink.png" );
		green =  Toolkit.getDefaultToolkit().getImage( "images/green.png" );
		violet =  Toolkit.getDefaultToolkit().getImage( "images/violet.png" );
		blue =  Toolkit.getDefaultToolkit().getImage( "images/blue.png" );
		this.setDoubleBuffered( true );
		 this.timer = new Timer( 5, this );
		 incre= 0;
		 this.isPlayer = isPlayer1;
		 this.gamePanel = gamePanel;
		 
		// System.out.println(circles.size());
		 time=0;
		 circlesY= new ArrayList<Integer>();
		 for(Circle circle: circles){
			 circlesY.add(circle.y);
		 }
		//timer.start();

	}
	
	public void startGame(){
		
		timer.start();
		// Extending KeyAdapter allows to override only the methods of interest
		if(isPlayer){
			this.setFocusable(true);
			this.requestFocusInWindow();
	        this.addKeyListener( new TAdapter() );
		}
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
		 //green
		 if(f1 && space){
			 g.drawImage(green, 10, 275, this);
			 for (Circle circle : circles){
				 if( circle.y >= 300 && circle.y < 330 && circle.colornum== 3){
					//increment score green
					 if(isPlayer)
					 gamePanel.updateScore(20);
					
					break;
				 }
			 } 
			 
		 }
		 if(f2 && space){
			 g.drawImage(pink, 95, 275, this);
			 for (Circle circle : circles){
				 if( circle.y >= 300 && circle.y < 330 && circle.colornum== 1){
					//increment score green
					 if(isPlayer)
					 gamePanel.updateScore(20);
					 
					break;
				 }
			 }
		 }
		 if(f3 && space){
			 g.drawImage(blue, 167, 275, this);
			 for (Circle circle : circles){
				 if( circle.y >= 300 && circle.y < 330 && circle.colornum== 0){
					//increment score green
					 if(isPlayer) 
					 gamePanel.updateScore(20);
					 break;
				 }
			 }
		 }
		 if(f4 && space){
			 g.drawImage(violet, 250, 275, this);
			 for (Circle circle : circles){
				 if( circle.y >= 300 && circle.y < 330 && circle.colornum== 2){
					//increment score green
					 if(isPlayer)
					 gamePanel.updateScore(20);
					 
					 break;
				 }
			 }
		 }
		 Toolkit.getDefaultToolkit().sync();
		g.dispose();

	}
	
	public void initializeCircleY(){
		int i=0;
		for(Integer m : circlesY){
			circles.get(i).y= m;
			i++;
		}
	}
	
	public void updateOpponent(String serverData){
		if(!isPlayer){
			String[] tokens = serverData.split(" ");
			if(tokens[2].equals("PRESSED")){
				switch(tokens[3]){
					case "F1": f1= true; break;
					case "F2": f2= true; break;
					case "F3": f3= true; break;
					case "F4": f4= true; break;
					case "SPACE": space= true ;break;
					
				}
				
			}
			else if(tokens[2].equals("RELEASED")){
				switch(tokens[3]){
					case "F1": f1= false; break;
					case "F2": f2= false; break;
					case "F3": f3= false; break;
					case "F4": f4= false; break;
					case "SPACE": space= false; break;
				}
			
			}
		}
	}

    public void actionPerformed( ActionEvent event ) {
    	
    	for (Circle circle : circles){
			circle.y++;			
		 }
    	
    	y++;
    	
    	if(circles.get(circles.size()-1).y <= 335){
    		repaint();
    	}
    	else {
    		f1=f2=f3=f4=space= false;
    		repaint();
    		timer.stop();
    		
    		if(this.isPlayer){
    			gamePanel.sendWinner();
    		}
    	}
    	
    	
       
        time++;

    }
    public void focus(){
    	this.setFocusable(true);
		this.requestFocusInWindow();
        this.addKeyListener( new TAdapter() );
    }
    private class TAdapter extends KeyAdapter {

        public void keyReleased( KeyEvent event ) {
        	 int key = event.getKeyCode();
        	 if(key == KeyEvent.VK_F1){
        		 	f1= false;
        		 	gamePanel.send("KEY "+gamePanel.name + " RELEASED "+ "F1");
        		 	
        	 }
        	 else if(key == KeyEvent.VK_F2){
      		 	f2= false;
      		 	space = false;
      		 	gamePanel.send("KEY "+gamePanel.name + " RELEASED "+ "F2");
         	 }
         	 else if(key == KeyEvent.VK_F3){
      		 	f3= false;
      		 	space = false;
      		 	gamePanel.send("KEY "+gamePanel.name + " RELEASED "+ "F3");
         	 }
         	 else if(key == KeyEvent.VK_F4){
       		 	f4= false;
       		 	space = false;
       		 	gamePanel.send("KEY "+gamePanel.name + " RELEASED "+ "F4");
          	 }
         	 else if(key == KeyEvent.VK_SPACE){
        		 space= false;
        		 gamePanel.send("KEY "+gamePanel.name + " RELEASED "+ "SPACE");
           	 }
        }

        public void keyPressed( KeyEvent event ) {
        	 int key = event.getKeyCode();
        	
        	 if(key == KeyEvent.VK_F1){
        		 gamePanel.send("KEY "+gamePanel.name + " PRESSED "+ "F1");
     		 	f1= true;
        	 }
        	 else if(key == KeyEvent.VK_F2){
        		 gamePanel.send("KEY "+gamePanel.name + " PRESSED "+ "F2");
     		 	f2= true;
        	 }
        	 else if(key == KeyEvent.VK_F3){
        		 gamePanel.send("KEY "+gamePanel.name + " PRESSED "+ "F3");
     		 	f3= true;
        	 }
        	 else if(key == KeyEvent.VK_F4){
        		 gamePanel.send("KEY "+gamePanel.name + " PRESSED "+ "F4");
      		 	f4= true;
         	 }
        	 
        	 else if(key == KeyEvent.VK_SPACE){
        		 gamePanel.send("KEY "+gamePanel.name + " PRESSED "+ "SPACE");
        		 space= true;
        		 
           	 }
        }

    }

}
