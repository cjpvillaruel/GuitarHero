import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class ChatPanel extends JPanel implements Runnable, ActionListener, Constants {

	String server;
	DatagramSocket socket;
	JTextArea chatbox = new JTextArea(20,20);
	JTextArea chatField = new JTextArea(2,2);
	JScrollPane scrollingArea2 = new JScrollPane(chatField);
	JScrollPane scrollingArea = new JScrollPane(chatbox);
	JButton sendButton = new JButton(">");
	String name;
	String serverData;
	Thread t=new Thread(this);
	public ChatPanel(GamePanel game) throws Exception{
		this.name = game.player1;
		this.server = game.server;
		this.setLayout(new FlowLayout());
		this.setBounds(20,265, 160, 280);
		chatbox = new JTextArea(2, 2);
		this.socket = game.socket;
		chatbox.setText("CHATBOX\n");
		chatbox.setLineWrap( true );
		scrollingArea.setViewportView(chatbox);
		scrollingArea.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		scrollingArea.setPreferredSize(new Dimension(140,200));
		
		
		chatField.setLineWrap( true );
		chatbox.setText("");
		scrollingArea2.setViewportView(chatField);
		scrollingArea2.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		scrollingArea2.setPreferredSize(new Dimension(110,50));
		
		sendButton.setPreferredSize(new Dimension(30,30));
		
		this.add(scrollingArea);
		this.add(scrollingArea2);
		this.add(sendButton);
		send("CHATCONNECT "+name);
		sendButton.addActionListener(this);
	
		t.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == sendButton){
			System.out.println("send button");
			if(chatField.getText().trim() != ""){
				String str = "MESSAGE>"+name+":";
				str += chatField.getText().trim();
				send(str);
				chatField.setText("");
			}	
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
			
			
			try{
	 			socket.receive(packet);
	 			serverData=new String(buf);
	 			serverData=serverData.trim();
	 				
	 			System.out.println(serverData);
	 			if (serverData.startsWith("CHATNOTIF")){
	 				String tokens[] = serverData.split(":");
	 				this.chatbox.append(tokens[1]+"\n");
	 			}
	 			else if (serverData.startsWith("CHATMESSAGE")){
	 				String tokens[] = serverData.split(">");
	 				this.chatbox.append(tokens[1]+"\n");
	 			}
	 			else if (serverData.startsWith("PLAYERCOUNT")){
	 				System.out.println("fail");
	 			}
	 			
			}catch(Exception ioe){System.out.println("Error");}
	
			
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
