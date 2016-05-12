
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

	public static void initMissle(Missile missile, UFO ufo) {

		missile.active = true;
		missile.angle = 0.0;
		missile.deltaAngle = 0.0;
		missile.x = ufo.x;
		missile.y = ufo.y;
		missile.deltaX = 0.0;
		missile.deltaY = 0.0;
		missile.render();
		AsteroidGame.missleCounter = AsteroidGame.MISSLE_COUNT;
		if ( AsteroidGame.sound)
			AsteroidGame.missleSound.loop();
		AsteroidGame.misslePlaying = true;
	}


	public static void updateMissle(Missile missile, Photon[] photons, Ship ship, UFO ufo) {

		int i;

		// Move the guided missile and check for collision with ship or photon. Stop
		// it when its counter has expired.

		if (missile.active) {
			if (--AsteroidGame.missleCounter <= 0)
				;  //stopMissle();                             UNCOMMENT HERE
			else {
				//guideMissle();                             UNCOMMENT HERE
				missile.advance();
				missile.render();
				for (i = 0; i < AsteroidGame.MAX_SHOTS; i++)
					if (photons[i].active && missile.isColliding(photons[i])) {
						if (AsteroidGame.sound)
							AsteroidGame.crashSound.play();
						//explode(missile);                             UNCOMMENT HERE
						//stopMissle();                             UNCOMMENT HERE
						AsteroidGame.score += AsteroidGame.MISSLE_POINTS;
					}
				if (missile.active && ship.active &&
						AsteroidGame.hyperCounter <= 0 && ship.isColliding(missile)) {
					if (AsteroidGame.sound)
						AsteroidGame.crashSound.play();
					//explode(ship);                UNCOMMENT HERE
					Ship.stopShip(ship);
					UFO.stopUfo(ufo);
					//stopMissle();
				}
			}
		}
	}
	public void guideMissle(Ship ship, Missile missile) {

		double dx, dy, angle;

		if (!ship.active || AsteroidGame.hyperCounter > 0)
			return;

		// Find the angle needed to hit the ship.

		dx = ship.x - missile.x;
		dy = ship.y - missile.y;
		if (dx == 0 && dy == 0)
			angle = 0;
		if (dx == 0) {
			if (dy < 0)
				angle = -Math.PI / 2;
			else
				angle = Math.PI / 2;
		}
		else {
			angle = Math.atan(Math.abs(dy / dx));
			if (dy > 0)
				angle = -angle;
			if (dx < 0)
				angle = Math.PI - angle;
		}

		// Adjust angle for screen coordinates.

		missile.angle = angle - Math.PI / 2;                              
		// Change the missile's angle so that it points toward the ship.

		missile.deltaX = 0.75 * AsteroidGame.MAX_ROCK_SPEED * -Math.sin(missile.angle);            
		missile.deltaY = 0.75 * AsteroidGame.MAX_ROCK_SPEED *  Math.cos(missile.angle);           


	}

	public static void stopMissle(Missile missile) {

		missile.active = false;
		AsteroidGame.missleCounter = 0;
		if (AsteroidGame.loaded)
			AsteroidGame.missleSound.stop();
		AsteroidGame.misslePlaying = false;
	}

}