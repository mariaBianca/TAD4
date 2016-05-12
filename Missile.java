
public class Missile extends SpaceObject{
	
	public static void defineMissile(Missile missile){
	    missile.shape.addPoint(0, -4);
	    missile.shape.addPoint(1, -3);
	    missile.shape.addPoint(1, 3);
	    missile.shape.addPoint(2, 4);
	    missile.shape.addPoint(-2, 4);
	    missile.shape.addPoint(-1, 3);
	    missile.shape.addPoint(-1, -3);

	}

}
