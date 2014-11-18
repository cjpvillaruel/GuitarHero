import java.net.InetAddress;

/**
 * This class encapsulates a network players
 * @author Joseph Anthony C. Hermocilla
 *
 */

public class NetPlayer {
	/**
	 * The network address of the player
	 */
	private InetAddress address;
	
	/**
	 * The port number of  
	 */
	private int port;
	
	/**
	 * The name of the player
	 */
	private String name;
	
	private String message;


	/**
	 * Constructor
	 * @param name
	 * @param address
	 * @param port
	 */
	public NetPlayer(InetAddress address, int port, String name){
		this.address = address;
		this.port = port;
		this.name = name;

	}

	public void setName(String name){
		this.name = name;
	}
	/**
	 * Returns the address
	 * @return
	 */
	public InetAddress getAddress(){
		return address;
	}

	/**
	 * Returns the port number
	 * @return
	 */
	public int getPort(){
		return port;
	}

	/**
	 * Returns the name of the player
	 * @return
	 */
	public String getName(){
		return name;
	}

	public void setMessage(String message){
		this.message = message;
	}
	public String getMessage(){
		return message;
	}

	/**
	 * String representation. used for transfer over the network
	 */
	public String toString(){
		String retval="";
		retval+="PLAYER ";
		retval+=name+" ";
		retval+=message;
		return retval;
	}
}