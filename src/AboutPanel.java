import java.awt.CardLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;


public class AboutPanel extends JPanel implements ActionListener {

	Image background;
	JButton back;
	JPanel panel1;
	public AboutPanel(JPanel panel1){
		background = Toolkit.getDefaultToolkit().getImage( "images/about.jpg" );
		back = new JButton("Back");
		this.add(back);
		back.addActionListener(this);
		this.panel1 = panel1;
	}
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
	}
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == back){
			CardLayout cardLayout = (CardLayout)(panel1.getLayout());
			cardLayout.show(panel1, "Main");
		}
	
	}
	
}
