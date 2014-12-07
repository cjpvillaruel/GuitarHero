
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * The class that contains the state of the game.
 * The game state refers the current position of the players etc.
 * @author Joseph Anthony C. Hermocilla
 *
 */


public class Game{
	/**
	 * This is a map(key-value pair) of <player name,NetPlayer>
	 */
	private Map players=new HashMap();
	ArrayList<Circle> circles;
	
	/**
	 * Simple constructor
	 *
	 */
	public Game(){
		initializeCircles();
	}
	
	/**
	 * Update the game state. Called when player moves
	 * @param name
	 * @param player
	 */
	public void update(String name, NetPlayer player){
		players.put(name,player);
	}
	
	/**
	 * String representation of this object. Used for data transfer
	 * over the network
	 */
	public String toString(){
		String retval="";
		for(Iterator ite=players.keySet().iterator();ite.hasNext();){
			String name=(String)ite.next();
			NetPlayer player=(NetPlayer)players.get(name);
			retval+=player.toString()+":";
		}
		return retval;
	}
	
	/**
	 * Returns the map
	 * @return
	 */
	public Map getPlayers(){
		return players;
	}
	
	public void initializeCircles(){
		  this.circles = new ArrayList<Circle>();
		  Random randomGenerator = new Random();
		  int[] gaps = {90, 100, 150,200, 400};
		  int gap = randomGenerator.nextInt(5);
		  int num =0;
		  Circle circle;
		  int color, color2;
		  int count=0;
		  while(num >= -5000){
			  color = randomGenerator.nextInt(4);
			  circle = new Circle(color);
			  circle.y = num;
			  circles.add(circle);
			  gap = randomGenerator.nextInt(5);
			  count++;
			  //randomize another circle
			  int addMore = randomGenerator.nextInt(1);
			  if(addMore == 1){
				  do{
					  color2 = randomGenerator.nextInt(4);
				  }while(color2 == color);
				  
				  circle = new Circle(color2);
				  circle.y = num;
				  circles.add(circle);
				  count++;
			  }
			  
			  num-= gaps[gap];
		  }
		 // System.out.println(count);  
	}
	
	public String circlestoString(){
		String str = "";
		for (Circle circle : circles){
			str+= " "+circle.colornum +","+circle.y;
				
		 }
		return str;
	}
	
}
