
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * The main game server. It just accepts the messages sent by one player to
 * another player
 * @author Joseph Anthony C. Hermocilla
 *
 */

public class GameServer implements Runnable, Constants{
	/**
	 * Placeholder for the data received from the player
	 */	 
	String playerData;
	int playerCount=0;
    DatagramSocket serverSocket = null;
	Game game;

	int gameStage=WAITING_FOR_PLAYERS;
	int numPlayers;
	Thread t = new Thread(this);

	public GameServer(){
		this.numPlayers = 2;
		try {
            serverSocket = new DatagramSocket(PORT);
			serverSocket.setSoTimeout(100);
		} catch (IOException e) {
            System.err.println("Could not listen on port: "+PORT);
            System.exit(-1);
		}catch(Exception e){}
		//Create the game state
		game = new Game();
		
		System.out.println("Game created...");
		
		//Start the game thread
		t.start();
	}
	
	/**
	 * Helper method for broadcasting data to all players
	 * @param msg
	 */
	public void broadcast(String msg){
		for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();){
			String name=(String)ite.next();
			NetPlayer player=(NetPlayer)game.getPlayers().get(name);			
			send(player,msg);	
		}
	}


	/**
	 * Send a message to a player
	 * @param player
	 * @param msg
	 */
	public void send(NetPlayer player, String msg){
		DatagramPacket packet;	
		byte buf[] = msg.getBytes();		
		packet = new DatagramPacket(buf, buf.length, player.getAddress(),player.getPort());
		try{
			serverSocket.send(packet);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	public void send(InetAddress address, int port, String msg){
		DatagramPacket packet;	
		byte buf[] = msg.getBytes();		
		packet = new DatagramPacket(buf, buf.length, address,port);
		try{
			serverSocket.send(packet);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	/**
	 * The juicy part
	 */
	public void run(){
		while(true){
						
			// Get the data from players
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try{
     			serverSocket.receive(packet);
			}catch(Exception ioe){}
			
			/**
			 * Convert the array of bytes to string
			 */
			playerData=new String(buf);
			
			//remove excess bytes
			playerData = playerData.trim();
			//if (!playerData.equals("")){
			//	System.out.println("Player Data:"+playerData);
			//}
			System.out.print(playerData);
		
			// process
			switch(gameStage){
				  case WAITING_FOR_PLAYERS:
						//System.out.println("Game State: Waiting for players...");
						if (playerData.startsWith("CONNECT")){
							String tokens[] = playerData.split(" ");
							NetPlayer player=new NetPlayer(packet.getAddress(),packet.getPort(),tokens[1]);
							System.out.println("Player connected");
							game.update(tokens[1].trim(),player);
							broadcast("CONNECTED "+tokens[1]);
							playerCount++;
							if (playerCount==numPlayers){
								gameStage=GAME_START;
							}
						}
						
						else if(playerData.startsWith("MAIN")){
							send(packet.getAddress(),packet.getPort(),playerCount+"" );
						}
					  break;	
				  case GAME_START:
					  System.out.println("Game State: START");
					  broadcast("START");
					  gameStage=IN_PROGRESS;
					  break;
				  case IN_PROGRESS:
					  //System.out.println("Game State: IN_PROGRESS");
					 
					  //Player data was received!
//					  if (playerData.startsWith("PLAYER")){
//						  //Tokenize:
//						  //The format: PLAYER <player name> <x> <y>
//						  String[] playerInfo = playerData.split(" ");					  
//						  String pname =playerInfo[1];
//						  String msg = playerInfo[2];
//						  //Get the player from the game state
//						  NetPlayer player=(NetPlayer)game.getPlayers().get(pname);					  
//						 
//						  player.setMessage(msg);
//						  //Update the game state
//						  game.update(pname, player);
//						  //Send to all the updated game state
//						  broadcast(game.toString());
//					  }
					  break;
			}				  
		}
	}	
	
	
	public static void main(String args[]){
		
		new GameServer();
	}
}

