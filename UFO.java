public class UFO extends SpaceObject {

	
	public static void defineUFO(UFO ufo){
		
	    ufo.shape.addPoint(-15, 0);
	    ufo.shape.addPoint(-10, -5);
	    ufo.shape.addPoint(-5, -5);
	    ufo.shape.addPoint(-5, -8);
	    ufo.shape.addPoint(5, -8);
	    ufo.shape.addPoint(5, -5);
	    ufo.shape.addPoint(10, -5);
	    ufo.shape.addPoint(15, 0);
	    ufo.shape.addPoint(10, 5);
	    ufo.shape.addPoint(-10, 5);

	}
}
