import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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


public class GamePanel extends JPanel implements ActionListener , Runnable, Constants, KeyListener{
	public JPanel panel1;
	Image background;
	ChatPanel chatPanel;
	PlayerBoard playerBoard1, playerBoard2;
	JPanel board1, board2;
	String player1;
	JLabel status;
	String server;
	DatagramSocket socket;
	String name;
	ArrayList <Circle> circles;
	JButton button, button2;
	String serverData;
	JPanel cardLayoutPanel1;
	JPanel cardLayoutPanel2;
	boolean waiting= true;
	Thread t=new Thread(this);
	public GamePanel(JPanel panel1, String server, String name, DatagramSocket socket) throws SocketException{
		this.server = server;
		this.panel1 = panel1;
		this.setLayout(null);
		this.player1 = name;
		this.socket= socket;
		this.name = name;
		try {
			chatPanel = new ChatPanel(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 circles = new ArrayList<Circle>();
		
		CardLayout layout = new CardLayout();
		board1 = new JPanel(layout);
		board2 = new JPanel(layout);
		
		cardLayoutPanel1 = new JPanel(new CardLayout());
		cardLayoutPanel2 = new JPanel(new CardLayout());
		
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
		
		cardLayoutPanel1.add(buttonPanel, "Player1");
		cardLayoutPanel2.add(buttonPanel2, "Player2");
		
		
		
		
		
		cardLayoutPanel1.setBounds(186,150, 335, 394);
		cardLayoutPanel2.setBounds(535,150, 335, 394);
		this.add(cardLayoutPanel1);
		this.add(cardLayoutPanel2);
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
			send("READY "+ this.name);
		}
	}
	@Override
	public void run() {
		while(true){
			try{
				Thread.sleep(1);
			}catch(Exception ioe){}
						
			//Get the data from players
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			
			
			try{
	 			socket.receive(packet);
	 			serverData=new String(buf);
	 			serverData=serverData.trim();
	 				
	 			if (serverData.startsWith("CHATNOTIF")){
	 				String tokens[] = serverData.split(":");
	 				chatPanel.chatbox.append(tokens[1]+"\n");
	 			}
	 			else if (serverData.startsWith("CHATMESSAGE")){
	 				String tokens[] = serverData.split(">");
	 				chatPanel.chatbox.append(tokens[1]+"\n");
	 			}
	 			else if (serverData.startsWith("READY")){
	 				playerBoard2= new PlayerBoard(circles, false);
	 				cardLayoutPanel2.add(playerBoard2,"board2");
	 				//show playerboard2
	 				CardLayout cardLayout = (CardLayout)(cardLayoutPanel2.getLayout());
	 				cardLayout.show(cardLayoutPanel2, "board2");
	 				//start game
	 				playerBoard1.startGame();
	 				playerBoard2.startGame();
	 			}
	 			else if(serverData.startsWith("COMPLETE")){
	 				button.setEnabled(true);
	 				button2.setEnabled(true);
	 				
	 			}
	 			else if(serverData.startsWith("CIRCLES")){
	 				String tokens[] = serverData.split(" "); //SPLIT BY SPACES
	 				circles = new ArrayList<Circle>();
	 				
	 				System.out.print("circle");
	 				for(int i =2 ;i< tokens.length; i++){
	 					String token[] = tokens[i].split(",");
	 					Circle cir= new Circle(Integer.parseInt(token[0]));
	 					cir.y = Integer.parseInt(token[1]);
	 					circles.add(cir);
	 				}
	 			
	 				if(tokens[1].equals(this.name)){
	 					
		 				playerBoard1= new PlayerBoard(circles, true);
		 				cardLayoutPanel1.add(playerBoard1,"board1");
		 				CardLayout cardLayout = (CardLayout)(cardLayoutPanel1.getLayout());
		 				cardLayout.show(cardLayoutPanel1, "board1");
	 				}
	 				
	 				
	 			}
			}catch(Exception ioe){System.out.println("Error");}			
		}
	
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
	}
	
	
	public void send(String msg){
		try{
			byte[] buf = msg.getBytes();
        	InetAddress address = InetAddress.getByName(server);
        	DatagramPacket packet = new DatagramPacket(buf, buf.length, address, PORT);
        	socket.send(packet);
        }catch(Exception e){}
		
	}
	public void keyTyped(KeyEvent e) {
    	System.out.print("typed");
       
    }

    /** Handle the key-pressed event from the text field. */
    public void keyPressed(KeyEvent e) {
    	System.out.print("typed");
        
    }
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
