import java.awt.Dimension;
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
	JPanel playerPanel2, playerPanel;
	String player1;
	JLabel status;
	String server;
	DatagramSocket socket;
	
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
		status = new JLabel("Status:");
		
		this.add(chatPanel);
		
		
		//chatPanel.add(status);
		
		//this.add(status);
		background = Toolkit.getDefaultToolkit().getImage( "images/gameLayout.jpg" );
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void run() {
		
		
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
	}

}
