import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class MainMenu extends JPanel implements ActionListener, Runnable, Constants {
	GamePanel game;
	Image background;
	JButton play,next;
	JPanel panel1;
	JLabel nameLabel;
	JPanel askName;
	JTextField name;
	String server="localhost";
	JLabel slotsAvailable;
	boolean stillMain= true;
	
	Thread t=new Thread(this);
	String player="Joseph";
	String pname;
	boolean connected=false;
    DatagramSocket socket = new DatagramSocket();
	String serverData;
	
	public MainMenu(JPanel panel1, String server) throws Exception{
		this.setLayout(null);
		background = Toolkit.getDefaultToolkit().getImage( "images/background1.jpg" );
		this.panel1 = panel1;
		Image imge = Toolkit.getDefaultToolkit().getImage( "images/playbutton.jpg" );
		play = new JButton("Play");	
		play.setIcon(new ImageIcon(imge));
		play.setBorderPainted(false); 
		play.setFocusPainted(false);  
		play.setContentAreaFilled(false);  
		play.setBounds(520, 220, 260, 110);
		play.addActionListener(this);
		this.add(play);
		slotsAvailable = new JLabel("Available:");
		slotsAvailable.setBounds(100, 400, 200, 200);
		slotsAvailable.setForeground(Color.WHITE);
		this.add(slotsAvailable);
		this.server = server;
		t.start();
		
	
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == play){
			//instantiate gameBoard
			
			
			try {
				askName= new NamePanel(panel1, this.server);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			panel1.add(askName, "Name");
			CardLayout cardLayout = (CardLayout)(panel1.getLayout());
			cardLayout.show(panel1, "Name");
			this.stillMain = false;
			
			
		}
		

	}
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
	}
	
	public void run(){
		while(this.stillMain){
			try{
				Thread.sleep(1);
			}catch(Exception ioe){}
						
			//Get the data from players
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			
			
			send("MAIN");	
			try{
	 			socket.receive(packet);
	 			serverData=new String(buf);
	 			serverData=serverData.trim();
	 				
	 			slotsAvailable.setText("Connected: "+ serverData);
			}catch(Exception ioe){System.out.println("Error");}
	
			System.out.println("send");
		}
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
