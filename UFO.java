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

	public static void initUfo(UFO ufo) {

		double angle, speed;

		// Randomly set flying saucer at left or right edge of the screen.

		ufo.active = true;
		ufo.x = -SpaceObject.width / 2;
		ufo.y = Math.random() * 2 * SpaceObject.height - SpaceObject.height;
		angle = Math.random() * Math.PI / 4 - Math.PI / 2;
		speed = AsteroidGame.MAX_ROCK_SPEED / 2 + Math.random() * (AsteroidGame.MAX_ROCK_SPEED / 2);
		ufo.deltaX = speed * -Math.sin(angle);
		ufo.deltaY = speed *  Math.cos(angle);
		if (Math.random() < 0.5) {
			ufo.x = SpaceObject.width / 2;
			ufo.deltaX = -ufo.deltaX;
		}
		if (ufo.y > 0)
			ufo.deltaY = ufo.deltaY;
		ufo.render();
		AsteroidGame.saucerPlaying = true;
		if (AsteroidGame.sound)
			AsteroidGame.saucerSound.loop();
		AsteroidGame.ufoCounter = (int) Math.abs(SpaceObject.width / ufo.deltaX);
	}

	public void updateUfo(UFO ufo, Photon[] photons, Ship ship, Missile missile) {

		int i, d;
		boolean wrapped;

		// Move the flying saucer and check for collision with a photon. Stop it
		// when its counter has expired.

		if (ufo.active) {
			if (-- AsteroidGame.ufoCounter <= 0) {
				if (--AsteroidGame.ufoPassesLeft > 0)
					initUfo(ufo);
				//else
					//stopUfo();                      UNCOMMENT HERE
			}
			if (ufo.active) {
				ufo.advance();
				ufo.render();
				for (i = 0; i < AsteroidGame.MAX_SHOTS; i++)
					if (photons[i].active && ufo.isColliding(photons[i])) {
						if (AsteroidGame.sound)
							AsteroidGame.crashSound.play();
						//explode(ufo);                       UNCOMMENT HERE
						stopUfo(ufo);                        
						AsteroidGame.score += AsteroidGame.UFO_POINTS;
					}

				// On occassion, fire a missile at the ship if the saucer is not too
				// close to it.

				d = (int) Math.max(Math.abs(ufo.x - ship.x), Math.abs(ufo.y - ship.y));
				if (ship.active && AsteroidGame.hyperCounter <= 0 &&
						ufo.active && !missile.active &&
						d > AsteroidGame.MAX_ROCK_SPEED * AsteroidGame.FPS / 2 &&
						Math.random() < AsteroidGame.MISSLE_PROBABILITY);
					Missile.initMissle(missile, ufo);                                
			}
		}
	}

	
	 public static void stopUfo(UFO ufo) {

		    ufo.active = false;
		    AsteroidGame.ufoCounter = 0;
		    AsteroidGame.ufoPassesLeft = 0;
		    if (AsteroidGame.loaded)
		    	AsteroidGame.saucerSound.stop();
		    AsteroidGame.saucerPlaying = false;
		  }
}
