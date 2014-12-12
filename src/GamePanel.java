import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import static javax.swing.ScrollPaneConstants.*;


public class GamePanel extends JPanel implements ActionListener , Runnable, Constants, KeyListener{
	public JPanel panel1;
	public Image background;
	public ChatPanel chatPanel;
	public PlayerBoard playerBoard1, playerBoard2;
	public JPanel board1, board2;
	public String player1;
	public JLabel status;
	public JLabel playername1, playername2;
	public String server;
	public DatagramSocket socket;
	public String name;
	public ArrayList <Circle> circles;
	public JButton button, button2;
	public String serverData;
	public JPanel cardLayoutPanel1;
	public JPanel cardLayoutPanel2;
	boolean waiting= true;
	int score1=0, score2=0;
	Thread t=new Thread(this);
	boolean endGame= true;
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
		buttonPanel.setBackground(new Color(32,33,30));
		buttonPanel2.setBackground(new Color(32,33,30));
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
		
		this.playername1 = new JLabel(this.name+": "+ score1);
		
		this.playername1.setBounds(300, 40, 400, 100);
		this.playername2 = new JLabel("Waiting...");
		this.playername2.setBounds(650,40, 400, 100);
		this.playername1.setFont(new Font("SansSerif", Font.BOLD, 40));
		this.playername1.setForeground(Color.WHITE);
		this.playername2.setFont(new Font("SansSerif", Font.BOLD, 30));
		this.playername2.setForeground(Color.white);
		this.add(playername1);
		this.add(playername2);
		
		
		
		//this.add(status);
		background = Toolkit.getDefaultToolkit().getImage( "images/gameLayout.jpg" );
	}
	public void sendWinner(){
		send("PLAYERSCORE "+name+" "+score1);
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
	 				playerBoard2= new PlayerBoard(circles, false, this);
	 				cardLayoutPanel2.add(playerBoard2,"board2");
	 				//show playerboard2
	 				CardLayout cardLayout = (CardLayout)(cardLayoutPanel2.getLayout());
	 				cardLayout.show(cardLayoutPanel2, "board2");
	 				
	 				try {
	 			         // Open an audio input stream.
	 			         File soundFile = new File("music/Avatar.wav");
	 					AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
	 			         // Get a sound clip resource.
	 			         Clip clip = AudioSystem.getClip();
	 			         // Open audio clip and load samples from the audio input stream.
	 			         clip.open(audioIn);
	 			         clip.loop(1);
	 			         
	 			      } catch (UnsupportedAudioFileException e) {
	 			          e.printStackTrace();
	 			      } catch (IOException e) {
	 			         e.printStackTrace();
	 			      } catch (LineUnavailableException e) {
	 			         e.printStackTrace();
	 			      }
	 				//start game
	 				playerBoard1.startGame();
	 				playerBoard2.startGame();
	 				
	 				
	 				
	 			}
	 			else if(serverData.startsWith("COMPLETE")){
	 				button.setEnabled(true);
	 				//button2.setEnabled(true);
	 				String tokens[] = serverData.split(" ");
	 				
	 				System.out.print(tokens[1]);
	 				
	 				if(tokens[1].equals(this.name))
	 					this.playername2.setText(tokens[2]+": "+ score2);
	 				else{
	 					this.playername2.setText(tokens[1]+": "+ score2);
	 					System.out.print(tokens[1]);
	 				}
	 				
	 				
	 			}
	 			else if(serverData.startsWith("CIRCLES")){
	 				String tokens[] = serverData.split(" "); //SPLIT BY SPACES
	 				if(tokens[1].equals(this.name)){
	 					circles = new ArrayList<Circle>();
	 				
	 				
	 				System.out.print("circle");
	 				for(int i =2 ;i< tokens.length; i++){
	 					String token[] = tokens[i].split(",");
	 					Circle cir= new Circle(Integer.parseInt(token[0]));
	 					cir.y = Integer.parseInt(token[1]);
	 					circles.add(cir);
	 				}
	 			
	 				//if(tokens[1].equals(this.name)){
	 					
		 				playerBoard1= new PlayerBoard(circles, true, this);
		 				
		 				cardLayoutPanel1.add(playerBoard1,"board1");
		 				CardLayout cardLayout = (CardLayout)(cardLayoutPanel1.getLayout());
		 				cardLayout.show(cardLayoutPanel1, "board1");
	 				}

	 			}
	 			else if(serverData.startsWith("CIRCLEUPDATE")){
	 				String tokens[] = serverData.split(" ");
	 				int i=1;
	 				for(Circle circle: circles){
	 					String token[] = tokens[i].split(",");
	 					circle.y= Integer.parseInt(token[1]);
	 					i++;
	 				}
	 				System.out.print("UPDATE");
	 				
	 			}
	 			else if(serverData.startsWith("SCORE")){
	 				String tokens[] = serverData.split(" ");
	 				if(!tokens[1].equals(name)){
	 					playername2.setText(tokens[1]+": "+ tokens[2]);
	 				}
	 				
	 				
	 			}
	 			//receive when opponent pressed/released
	 			else if(serverData.startsWith("KEY")){
	 				String tokens[] = serverData.split(" ");
	 				if(!tokens[1].equals(name)){
	 					playerBoard2.updateOpponent(serverData);
	 				}
	 			}
	 			else if(serverData.startsWith("WINNER")){
	 				String tokens[] = serverData.split(" ");
	 				
	 				
	 				if(tokens[1].equals("1")){
	 					String winnerName =  tokens[2]+" wins! Play Again?";
	 					JLabel winner = new JLabel( tokens[2]+" wins! Play Again?");
	 					JPanel panel = new JPanel(new GridLayout(2,2));
	 					
	 					JOptionPane.showConfirmDialog(null, winnerName, "Play Again", JOptionPane.YES_NO_OPTION);
	 					
	 				}
	 				else if(tokens[1].equals("2") ){
	 					System.out.printf("TIE");
	 					
	 				}
	
	 			}
	 			
	 			
			}catch(Exception ioe){System.out.println("Error");}			
		}
	
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
	}
	
	public void updateScore(int score){
		score1++;
		playername1.setText(name+": "+score1);
		send("SCORE "+name+" "+score1);
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
