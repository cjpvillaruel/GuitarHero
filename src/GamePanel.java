import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import static javax.swing.ScrollPaneConstants.*;


public class GamePanel extends JPanel implements ActionListener , Runnable, Constants{
	public JPanel panel1;
	Image background;
	ChatPanel chatPanel;
	PlayerBoard playerBoard1, playerBoard2;
	JPanel board1, board2;
	String player1;
	JLabel status;
	String server;
	DatagramSocket socket;
	ArrayList <Circle> circles;
	JButton button, button2;
	String serverData;
	boolean waiting= true;
	Thread t=new Thread(this);
	public GamePanel(JPanel panel1, String server, String name, DatagramSocket socket) throws SocketException{
		this.server = server;
		this.panel1 = panel1;
		this.setLayout(null);
		this.player1 = name;
		this.socket= socket;
		try {
			chatPanel = new ChatPanel(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 circles = new ArrayList<Circle>();
		 initializeCircles();
		CardLayout layout = new CardLayout();
		board1 = new JPanel(layout);
		board2 = new JPanel(layout);
		JPanel buttonPanel = new JPanel(new FlowLayout());
		JPanel buttonPanel2 = new JPanel(new FlowLayout());
		button = new JButton("Ready");
		button.setEnabled(false);
		button2 = new JButton("Ready");
		button2.setEnabled(false);
		buttonPanel.add(button);
		buttonPanel2.add(button2);
		button.addActionListener(this);
		button2.addActionListener(this);
		board1.add("button", buttonPanel);
		board2.add("button", buttonPanel2);
		
		playerBoard1= new PlayerBoard(circles);
		playerBoard2= new PlayerBoard(circles);
		playerBoard1.setBounds(186,150, 335, 394);
		playerBoard2.setBounds(535,150, 335, 394);
		this.add(playerBoard1);
		this.add(playerBoard2);
		status = new JLabel("Status:");
		
		this.add(chatPanel);
		t.start();
		
		
		
		//this.add(status);
		background = Toolkit.getDefaultToolkit().getImage( "images/gameLayout.jpg" );
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if( e.getSource() == button){
			System.out.println("hehe");
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
				while(true){
					try{
						Thread.sleep(1);
					}catch(Exception ioe){}
								
					//Get the data from players
					byte[] buf = new byte[256];
					DatagramPacket packet = new DatagramPacket(buf, buf.length);
					
					System.out.println("receive");
					
		
		
				try{
		 			socket.receive(packet);
		 			serverData=new String(buf);
		 			serverData=serverData.trim();
		 				
		 			System.out.println(serverData);
		
		 			
				}catch(Exception ioe){System.out.println("Error");}	

				}
	
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
	}
	public void initializeCircles(){
		  Random randomGenerator = new Random();
		  int[] gaps = {90, 100, 150,200, 400};
		  int gap = randomGenerator.nextInt(5);
		  int num =0;
		  Circle circle;
		  int color, color2;
		  int count=0;
		  while(num >= -5000){
			  color = randomGenerator.nextInt(4);
			  circle = new Circle(color);
			  circle.y = num;
			  circles.add(circle);
			  gap = randomGenerator.nextInt(5);
			  count++;
			  //randomize another circle
			  int addMore = randomGenerator.nextInt(1);
			  if(addMore == 1){
				  do{
					  color2 = randomGenerator.nextInt(4);
				  }while(color2 == color);
				  
				  circle = new Circle(color2);
				  circle.y = num;
				  circles.add(circle);
				  count++;
			  }
			  
			  num-= gaps[gap];
		  }
		 // System.out.println(count);
		  
	}
	public void send(String msg){
		try{
			byte[] buf = msg.getBytes();
        	InetAddress address = InetAddress.getByName(server);
        	DatagramPacket packet = new DatagramPacket(buf, buf.length, address, PORT);
        	socket.send(packet);
        }catch(Exception e){}
		
	}

}
