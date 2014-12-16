
public class GuitarHero {

	public static void main(String[] args) {
		try{
		GuitarHeroGUI gui= new GuitarHeroGUI(args[0]);
		}catch(Exception e){
			System.out.print("To run: java -jar GuitarHero.jar 192.168.x.x");
		}
	}

}
