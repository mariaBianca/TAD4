
public class Photon extends SpaceObject {
	
	
	public static void definePhotons(Photon photon){
		
	      photon.shape.addPoint(1, 1);
	      photon.shape.addPoint(1, -1);
	      photon.shape.addPoint(-1, 1);
	      photon.shape.addPoint(-1, -1);

	}

}
