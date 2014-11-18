import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class GamePanel extends JPanel implements ActionListener , Runnable, Constants{
	public JPanel panel1;
	Image background;
	JPanel chatPanel;
	JPanel playerPanel2, playerPanel;

	JLabel status;
	
	public GamePanel(JPanel panel1, String server, String name) throws SocketException{
		
		this.panel1 = panel1;
		this.setLayout(null);
		
		status = new JLabel("Status:");
		status.setBounds(0, 0, 200, 200);
		this.add(status);
		background = Toolkit.getDefaultToolkit().getImage( "images/gameLayout.jpg" );
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void run() {
		
		
	}

//	protected void paintComponent(Graphics g) {
//		super.paintComponent(g);
////		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
//	}

}
