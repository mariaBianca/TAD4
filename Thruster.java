import java.awt.Polygon;

class Thruster extends SpaceObject {
	
	
	//class that defines the specifications of the front, respectively the back thruster

	static Thruster fwdThruster = new Thruster();
	static Thruster revThruster = new Thruster();

	

	public void createFwdThruster() {
	    fwdThruster.shape.addPoint(0, 12);
	    fwdThruster.shape.addPoint(-3, 16);
	    fwdThruster.shape.addPoint(0, 26);
	    fwdThruster.shape.addPoint(3, 16);
		
	}
	
	public void createRvThruster(){
	    revThruster.shape.addPoint(-2, 12);
	    revThruster.shape.addPoint(-4, 14);
	    revThruster.shape.addPoint(-2, 20);
	    revThruster.shape.addPoint(0, 14);
	    revThruster.shape.addPoint(2, 12);
	    revThruster.shape.addPoint(4, 14);
	    revThruster.shape.addPoint(2, 20);
	    revThruster.shape.addPoint(0, 14);

	}
}
