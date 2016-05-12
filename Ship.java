
public class Ship extends SpaceObject {


	public static void defineShip(Ship ship){
		ship.shape.addPoint(0, -10);
		ship.shape.addPoint(7, 10);
		ship.shape.addPoint(-7, 10);

	}

}
