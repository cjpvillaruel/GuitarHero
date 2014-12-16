import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
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


public class HelpPanel extends JPanel implements ActionListener,Constants {
	JButton about;
	JLabel aboutLabel;
	JLabel aboutLabel2;
	JPanel abowt, panel1;
	JTextField name;
	JTextField port;
	JButton close;
	GamePanel game;

	
    
    
    String server="localhost";
	String serverData;
	java.net.URL imgURL = getClass().getResource( "images/helpbutton.jpg");
	Image imge = Toolkit.getDefaultToolkit().getImage(imgURL );
	java.net.URL imgURL1 = getClass().getResource( "images/back.jpg");
	Image imge2 = Toolkit.getDefaultToolkit().getImage( imgURL1 );
	java.net.URL imgURL2 = getClass().getResource( "images/help.jpg");
	Image imge3 = Toolkit.getDefaultToolkit().getImage( imgURL2 );
	
	
/*	play.setIcon(new ImageIcon(imge));
	play.setBorderPainted(false); 
	play.setFocusPainted(false);  
	play.setContentAreaFilled(false);  
	play.setBounds(520, 220, 260, 110);
	play.addActionListener(this);
	this.add(play);*/

	
	public HelpPanel(JPanel panel1) throws Exception{
		about= new JButton(); 
		abowt = new JPanel(new FlowLayout());
		close = new JButton();
		close.addActionListener(this);
		aboutLabel = new JLabel();
		
		about.setIcon(new ImageIcon(imge));
		about.setBorderPainted(false); 
		about.setFocusPainted(false);  
		about.setContentAreaFilled(false);  
		//about.setBounds(0, 0, 260, 110);
		//about.addActionListener(this);
		//about.setEnabled(false);
		this.add(about);
		//this.add(name);
		close.setIcon(new ImageIcon(imge2));
		close.setBorderPainted(false); 
		close.setFocusPainted(false);  
		close.setContentAreaFilled(false);  
		//close.setBounds(0, 0, 260, 110);
		close.addActionListener(this);
		//close.setEnabled(false);
		this.add(close);
		
		aboutLabel.setIcon(new ImageIcon(imge3));
		//aboutLabel.setBorderPainted(false); 
		//aboutLabel.setFocusPainted(false);  
		//aboutLabel.setContentAreaFilled(false);  
		//aboutLabel.setBounds(0, 0, 260, 110);
		aboutLabel.setBackground(new Color(128, 4, 4));
		this.add(aboutLabel);
		this.setBackground(new Color(128, 4, 4));
		this.panel1 = panel1;
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == close){
			CardLayout cardLayout = (CardLayout)(panel1.getLayout());
			cardLayout.show(panel1, "Main");
		}
	}
}
