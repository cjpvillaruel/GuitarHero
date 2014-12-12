
import java.awt.List;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
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
	int highScore = 0;
	int gameStage=WAITING_FOR_PLAYERS;
	int numPlayers;
	Thread t = new Thread(this);
	int count, winnerScore=0;
	String winner="";
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
		//System.out.print("broadcast");
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
		//System.out.print(address+" "+port);
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
			
			if(playerData.startsWith("MESSAGE")){
				String tokens[] = playerData.split(">");
				broadcast("CHATMESSAGE>"+tokens[1]);	
			}
			// process
			switch(gameStage){
				  case WAITING_FOR_PLAYERS:
						//System.out.println("Game State: Waiting for players...");
						if (playerData.startsWith("CONNECT")){
							String tokens[] = playerData.split(" ");
							//check if duplicate name
							boolean isFound=false;
							for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();){
								if(tokens[1].equals((String)ite.next())){
									isFound= true;
								}
							}
							
							if(!isFound){
								NetPlayer player=new NetPlayer(packet.getAddress(),packet.getPort(),tokens[1]);
								System.out.println("Player connected" +packet.getPort());
								game.update(tokens[1].trim(),player);
								broadcast("CONNECTED "+tokens[1]);
								playerCount++;
								if (playerCount==numPlayers){
									gameStage=GAME_START;
									String str = "COMPLETE";
									for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();){
										String name=(String)ite.next();
										str+=" "+ name;
									}
									broadcast(str);
								}
							}
							else{
								send(packet.getAddress(),packet.getPort(),"RENAME");
							}
						}
						
						else if(playerData.startsWith("MAIN")){
							send(packet.getAddress(),packet.getPort(),playerCount+"" );
						}
						else if(playerData.startsWith("CHATCONNECT")){
							String tokens[] = playerData.split(" ");
								
							//send(packet.getAddress(),packet.getPort(),"CHATNOTIF:"+tokens[1]+" connected.");
							broadcast("CHATNOTIF:"+tokens[1]+" connected.");
							
							System.out.println("chat connect");
						}
						
						else if(playerData.startsWith("WAITING")){
							broadcast("PLAYERCOUNT:"+numPlayers);	
						}
					  break;	
				  case GAME_START:
					  
					  broadcast("START");
					  if(playerData.startsWith("CHATCONNECT")){
							String tokens[] = playerData.split(" ");
								
							//send(packet.getAddress(),packet.getPort(),"CHATNOTIF:"+tokens[1]+" connected.");
							broadcast("CHATNOTIF:"+tokens[1]+" connected.");
							System.out.println("chat connect");
						}
					 
					  else if(playerData.startsWith("WAITING")){
							broadcast("PLAYERCOUNT:"+numPlayers);	
						}
					  else if(playerData.startsWith("READY")){
						  	String tokens[] = playerData.split(" ");
							broadcast("PLAYERCOUNT:"+numPlayers);
							//CIRCLE NAME 1,2 3,4 
							broadcast("CIRCLES " +tokens[1]+ game.circlestoString()); 
							count++;
							if(count ==2){
								broadcast("READY");
								gameStage=IN_PROGRESS;
							}
					   }
					 
					
					  break;
				  case IN_PROGRESS:
					 // System.out.println("Game State: IN_PROGRESS");
					  if(playerData.startsWith("SCORE")){
						  String tokens[] = playerData.split(" ");
						  broadcast(playerData);
					  }
					  if(playerData.startsWith("KEY")){
						  broadcast(playerData);
					  }
					  
					  else if(playerData.startsWith("PLAYERSCORE")){
						  String tokens[] = playerData.split(" ");
						  System.out.println(playerData);
						  if(winner.equals("") ){
							  winner =tokens[1];
							  winnerScore = Integer.parseInt(tokens[2]);
						  }
						  else{
							  
							  if(winnerScore < Integer.parseInt(tokens[2])){
								  broadcast("WINNER 1 "+ tokens[1] );
								  System.out.print(winnerScore);
							  }
							  else if(winnerScore > Integer.parseInt(tokens[2])){
								  broadcast("WINNER 1 "+ winner );
							  }
							  else if(winnerScore== Integer.parseInt(tokens[2]) ){
								  broadcast("WINNER 2");
							  }
						  }
					  }
					  
				break;
			}				  
		}
	}	
	
	
	public static void main(String args[]){
		
		new GameServer();
	}
}

