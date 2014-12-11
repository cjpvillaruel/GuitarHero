import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class NamePanel extends JPanel implements ActionListener,Constants {
	JLabel nameLabel, status;
	JPanel askName;
	JTextField name;
	JTextField port;
	JButton next;
	GamePanel game;
	JPanel panel1;
    DatagramSocket socket = new DatagramSocket();
	String server="localhost";
	String serverData;
	
	
	public NamePanel(JPanel panel1, String server) throws Exception{
		nameLabel= new JLabel("Enter your name:");
		status= new JLabel("status");
		askName = new JPanel(new FlowLayout());
		name = new JTextField(10);
		port = new JTextField(10);
		//name.setBounds(0, 0,100, 50);
		nameLabel.setForeground(Color.white);
		status.setForeground(Color.white);
		next = new JButton("Next");
		next.addActionListener(this);
		this.add(nameLabel);
		this.add(name);
		this.add(next);
		this.add(status);
		this.setBackground(new Color(128, 4, 4));
		this.panel1 = panel1;
		this.server = server;
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	//System.out.print(e.getSource().toString());
		if(e.getSource()== next){
			
			send("CONNECT"+" "+name.getText());
			//RECEIVE
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			int count = 1000;
			while(count >=0){
				try{
		 			socket.receive(packet);
		 			serverData=new String(buf);
		 			serverData=serverData.trim();
				
		 			if(serverData.startsWith("CONNECTED")){
		 				game= new GamePanel(this.panel1, this.server,name.getText(), this.socket);
		 				panel1.add(game, "Game");
		 				CardLayout cardLayout = (CardLayout)(panel1.getLayout());
		 				cardLayout.show(panel1, "Game");	
		 				break;
		 			}
		 			if (serverData.startsWith("RENAME")){
		 				status.setText("Username already exists.");
		 				break;
		 			}
//		 			else{
//		 				game= new GamePanel(this.panel1, this.server,name.getText(), this.socket);
//		 				panel1.add(game, "Game");
//		 				CardLayout cardLayout = (CardLayout)(panel1.getLayout());
//		 				cardLayout.show(panel1, "Game");	
//		 			}
		 			System.out.print(serverData);
				}catch(Exception ex){}
				
				count--;
			}
			
			
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
