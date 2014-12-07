import java.awt.Color;


public class Circle {
	public int x, y, width, height;
	boolean isVisible;
	Color color;
	int colornum;
	public Circle(int colorstr){
		this.colornum = colorstr;
		switch(colorstr){
			case 0: color= new Color(9, 101, 254); //blue
					x  = 190;
			break;
			case 1: color= new Color(247, 23, 134); //pink
					x = 110;
			break;
			case 2: color= new Color(68, 14, 98); //violet
					x = 270;
			break;
			case 3: color= new Color(13, 115, 5); //green
					x = 25;
			break;
		}
		this.isVisible = false;
		height = 30;
		width = 50;
	}
	public void move(){
		this.y++;
	}
}
