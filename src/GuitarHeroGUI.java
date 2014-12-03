import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class GuitarHeroGUI extends JFrame{
	public JPanel panel1, panel2,panel3, buttonPanel,textBoxPanel;
	public MainMenu mainPanel;
	String server;
	public GuitarHeroGUI(){
		super("GuitarHero");
		addComponents();
		
		// get screen dimensions using Toolkit and Dimension classes.
		Toolkit kit = Toolkit.getDefaultToolkit(); 
		Dimension screenSize = kit.getScreenSize(); 
		//int screenHeight = screenSize.height/2; 
		//int screenWidth = screenSize.width/2*;
		this.setSize( 900, 600 );   //sets the size of the frame, half of the screen's width and height
		//this.setSize(screenWidth, screenHeight); 
		// centers the frame in screen
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true); 
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		this.server = "192.168.43.218";
		//
	}
	private void addComponents(){
		Container c = this.getContentPane();
		panel1 = new JPanel();
		panel1.setBackground(Color.CYAN);
		panel1.setSize(new Dimension(100,200));

		panel2 = new JPanel();
		panel2.setBackground(Color.RED);
		panel2.setSize(new Dimension(800,500));
		
		CardLayout layout = new CardLayout();
		panel1.setLayout(layout); 
		
		JPanel buttonPanel = new JPanel(new FlowLayout());

		buttonPanel.add(new JButton("OK"));
		buttonPanel.add(new JButton("Cancel"));    

		JPanel textBoxPanel = new JPanel(new FlowLayout());

		textBoxPanel.add(new JLabel("Name:"));
		textBoxPanel.add(new JTextField(20));

		try {
			mainPanel = new MainMenu(panel1, server);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		panel1.add("Main", mainPanel);

	
		CardLayout cardLayout = (CardLayout)(panel1.getLayout());
		cardLayout.show(panel1, "Main");	
		//c.add(temp);
		c.add(panel1);
	}
	
}
