
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
/**
 * The game client itself!
 * @author Joseph Anthony C. Hermocilla
 *
 */

public class GameClient extends JPanel implements Runnable, Constants{
	/**
	 * Main window
	 */
	JFrame frame= new JFrame();
	
	/**
	 * Player position, speed etc.
	 */
	int x=10,y=10,xspeed=2,yspeed=2,prevX,prevY;
	
	/**
	 * Game timer, handler receives data from server to update game state
	 */
	Thread t=new Thread(this);
	
	/**
	 * Nice name!
	 */
	String name="Joseph";
	
	/**
	 * Player name of others
	 */
	String pname;
	
	/**
	 * Server to connect to
	 */
	String server="localhost";

	/**
	 * Flag to indicate whether this player has connected or not
	 */
	boolean connected=false;
	
	/**
	 * get a datagram socket
	 */
    DatagramSocket socket = new DatagramSocket();

	
    /**
     * Placeholder for data received from server
     */
	String serverData;
	
	/**
	 * Offscreen image for double buffering, for some
	 * real smooth animation :)
	 */
	//BufferedImage offscreen;

	JTextArea text;
	/**
	 * Basic constructor
	 * @param server
	 * @param name
	 * @throws Exception
	 */
	public GameClient(String server,String name) throws Exception{
		this.server=server;
		this.name=name;
		
		frame.setTitle(APP_NAME+":"+name);
		//set some timeout for the socket
		socket.setSoTimeout(100);
		
		//Some gui stuff i hate.
		//
		this.setLayout(new FlowLayout());
		text = new JTextArea(10, 10);
		this.add(text);
		this.add(new JButton("Send"));
		frame.getContentPane().add(this);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(640, 480);
		frame.setVisible(true);
		
		//create the buffer
		//offscreen=(BufferedImage)this.createImage(640, 480);
		
		//Some gui stuff again...
		frame.addKeyListener(new KeyHandler());		
		frame.addMouseMotionListener(new MouseMotionHandler());
		
		//tiime to play
		t.start();		
	}
	
	/**
	 * Helper method for sending data to server
	 * @param msg
	 */
	public void send(String msg){
		try{
			byte[] buf = msg.getBytes();
        	InetAddress address = InetAddress.getByName(server);
        	DatagramPacket packet = new DatagramPacket(buf, buf.length, address, PORT);
        	socket.send(packet);
        }catch(Exception e){}
		
	}

	/**
	 * The juicy part!
	 */
	public void run(){
		while(true){
			try{
				Thread.sleep(1);
			}catch(Exception ioe){}
						
			//Get the data from players
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try{
     			socket.receive(packet);
			}catch(Exception ioe){/*lazy exception handling :)*/}
			
			serverData=new String(buf);
			serverData=serverData.trim();
			
			//if (!serverData.equals("")){
			//	System.out.println("Server Data:" +serverData);
			//}

			//Study the following kids. 
			if (!connected && serverData.startsWith("CONNECTED")){
				connected=true;
				System.out.println("Connected.");
			}else if (!connected){
				System.out.println("Connecting..");				
				send("CONNECT "+name);
			}else if (connected){
				//offscreen.getGraphics().clearRect(0, 0, 640, 480);
				if (serverData.startsWith("PLAYER")){
					
					String[] playersInfo = serverData.split(":");
					for (int i=0;i<playersInfo.length;i++){
						System.out.println(playersInfo[i]);
						String[] playerInfo = playersInfo[i].split(" ");
						String pname =playerInfo[1];
						
						System.out.println( playersInfo[i]);
										
					}
					//show the changes
					//frame.repaint();
				}			
			}			
		}
	}
	

	
	
	
	class MouseMotionHandler extends MouseMotionAdapter{
		public void mouseMoved(MouseEvent me){
			x=me.getX();y=me.getY();
			
			send("PLAYER "+name+" "+"hi"+name);
							
		}
	}
	
	class KeyHandler extends KeyAdapter{
		public void keyPressed(KeyEvent ke){
			prevX=x;prevY=y;
			switch (ke.getKeyCode()){
			case KeyEvent.VK_DOWN:y+=yspeed;break;
			case KeyEvent.VK_UP:y-=yspeed;break;
			case KeyEvent.VK_LEFT:x-=xspeed;break;
			case KeyEvent.VK_RIGHT:x+=xspeed;break;
			}
			if (prevX != x || prevY != y){
				send("PLAYER "+name+" "+"hi"+name);
			}	
		}
	}
	
	
	public static void main(String args[]) throws Exception{
		if (args.length != 2){
			System.out.println("Usage: java -jar circlewars-client <server> <player name>");
			System.exit(1);
		}

		new GameClient(args[0],args[1]);
	}
}
