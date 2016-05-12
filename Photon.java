public class Photon extends SpaceObject {
	public static void definePhotons(Photon photon){

		photon.shape.addPoint(1, 1);
		photon.shape.addPoint(1, -1);
		photon.shape.addPoint(-1, 1);
		photon.shape.addPoint(-1, -1);
	}
	public static void initPhotons(Photon[] photons) {
		int i;
		for (i = 0; i < AsteroidGame.MAX_SHOTS; i++)
			photons[i].active = false;
		AsteroidGame.photonIndex = 0;
	}
	public static void updatePhotons(Photon[] photons) {
		int i;
		// Move any active photons. Stop it when its counter has expired.
		for (i = 0; i < AsteroidGame.MAX_SHOTS; i++)
			if (photons[i].active) {
				if (!photons[i].advance())
					photons[i].render();
				else
					photons[i].active = false;
			}
	}
}