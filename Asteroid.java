import java.awt.Polygon;

public class Asteroid extends SpaceObject{
	

	public static void initAsteroids(Asteroid[] asteroids, boolean asteroidIsSmall[]) {

		int i, j;
		int s;
		double theta, r;
		int x, y;

		// Create random shapes, positions and movements for each asteroid.

		for (i = 0; i < AsteroidGame.MAX_ROCKS; i++) {

			// Create a jagged shape for the asteroid and give it a random rotation.

			asteroids[i].shape = new Polygon();
			s = AsteroidGame.MIN_ROCK_SIDES + (int) (Math.random() * (AsteroidGame.MAX_ROCK_SIDES - AsteroidGame.MIN_ROCK_SIDES));
			for (j = 0; j < s; j ++) {
				theta = 2 * Math.PI / s * j;
				r = AsteroidGame.MIN_ROCK_SIZE + (int) (Math.random() * (AsteroidGame.MAX_ROCK_SIZE - AsteroidGame.MIN_ROCK_SIZE));
				x = (int) -Math.round(r * Math.sin(theta));
				y = (int)  Math.round(r * Math.cos(theta));
				asteroids[i].shape.addPoint(x, y);
			}
			asteroids[i].active = true;
			asteroids[i].angle = 0.0;
			asteroids[i].deltaAngle = Math.random() * 2 * AsteroidGame.MAX_ROCK_SPIN - AsteroidGame.MAX_ROCK_SPIN;

			// Place the asteroid at one edge of the screen.

			if (Math.random() < 0.5) {
				asteroids[i].x = -SpaceObject.width / 2;
				if (Math.random() < 0.5)
					asteroids[i].x = SpaceObject.width / 2;
				asteroids[i].y = Math.random() * SpaceObject.height;
			}
			else {
				asteroids[i].x = Math.random() * SpaceObject.width;
				asteroids[i].y = -SpaceObject.height / 2;
				if (Math.random() < 0.5)
					asteroids[i].y = SpaceObject.height / 2;
			}

			// Set a random motion for the asteroid.

			asteroids[i].deltaX = Math.random() * AsteroidGame.asteroidsSpeed;
			if (Math.random() < 0.5)
				asteroids[i].deltaX = -asteroids[i].deltaX;
			asteroids[i].deltaY = Math.random() * AsteroidGame.asteroidsSpeed;
			if (Math.random() < 0.5)
				asteroids[i].deltaY = -asteroids[i].deltaY;

			asteroids[i].render();
			asteroidIsSmall[i] = false;
		}

		AsteroidGame.asteroidsCounter = AsteroidGame.STORM_PAUSE;
		AsteroidGame.asteroidsLeft = AsteroidGame.MAX_ROCKS;
		if (AsteroidGame.asteroidsSpeed < AsteroidGame.MAX_ROCK_SPEED)
			AsteroidGame.asteroidsSpeed += 0.5;
	}

	public static void initSmallAsteroids(int n, Asteroid[] asteroids, boolean[] asteroidIsSmall) {

		int count;
		int i, j;
		int s;
		double tempX, tempY;
		double theta, r;
		int x, y;

		// Create one or two smaller asteroids from a larger one using inactive
		// asteroids. The new asteroids will be placed in the same position as the
		// old one but will have a new, smaller shape and new, randomly generated
		// movements.

		count = 0;
		i = 0;
		tempX = asteroids[n].x;
		tempY = asteroids[n].y;
		do {
			if (!asteroids[i].active) {
				asteroids[i].shape = new Polygon();
				s = AsteroidGame.MIN_ROCK_SIDES + (int) (Math.random() * (AsteroidGame.MAX_ROCK_SIDES - AsteroidGame.MIN_ROCK_SIDES));
				for (j = 0; j < s; j ++) {
					theta = 2 * Math.PI / s * j;
					r = (AsteroidGame.MIN_ROCK_SIZE + (int) (Math.random() * (AsteroidGame.MAX_ROCK_SIZE - AsteroidGame.MIN_ROCK_SIZE))) / 2;
					x = (int) -Math.round(r * Math.sin(theta));
					y = (int)  Math.round(r * Math.cos(theta));
					asteroids[i].shape.addPoint(x, y);
				}
				asteroids[i].active = true;
				asteroids[i].angle = 0.0;
				asteroids[i].deltaAngle = Math.random() * 2 * AsteroidGame.MAX_ROCK_SPIN - AsteroidGame.MAX_ROCK_SPIN;
				asteroids[i].x = tempX;
				asteroids[i].y = tempY;
				asteroids[i].deltaX = Math.random() * 2 * AsteroidGame.asteroidsSpeed - AsteroidGame.asteroidsSpeed;
				asteroids[i].deltaY = Math.random() * 2 * AsteroidGame.asteroidsSpeed - AsteroidGame.asteroidsSpeed;
				asteroids[i].render();
				asteroidIsSmall[i] = true;
				count++;
				AsteroidGame.asteroidsLeft++;
			}
			i++;
		} while (i < AsteroidGame.MAX_ROCKS && count < 2);
	}


	public static void updateAsteroids(Asteroid[] asteroids, Photon[] photons, boolean[] asteroidisSmall,
			Ship ship, Missile missile, UFO ufo) {

		int i, j;

		// Move any active asteroids and check for collisions.

		for (i = 0; i < AsteroidGame.MAX_ROCKS; i++)
			if (asteroids[i].active) {
				asteroids[i].advance();
				asteroids[i].render();

				// If hit by photon, kill asteroid and advance score. If asteroid is
				// large, make some smaller ones to replace it.

				for (j = 0; j < AsteroidGame.MAX_SHOTS; j++)
					if (photons[j].active && asteroids[i].active && asteroids[i].isColliding(photons[j])) {
						AsteroidGame.asteroidsLeft--;
						asteroids[i].active = false;
						photons[j].active = false;
						if (AsteroidGame.sound)
							AsteroidGame.explosionSound.play();
						Explosion.explode(asteroids[i]);                    
						if (!asteroidisSmall[i]) {
							AsteroidGame.score += AsteroidGame.BIG_POINTS;
							Asteroid.initSmallAsteroids(i, asteroids, asteroidisSmall);
						}
						else
							AsteroidGame.score += AsteroidGame.SMALL_POINTS;
					}

				// If the ship is not in hyperspace, see if it is hit.

				if (ship.active && AsteroidGame.hyperCounter <= 0 &&
						asteroids[i].active && asteroids[i].isColliding(ship)) {
					if (AsteroidGame.sound)
						AsteroidGame.crashSound.play();
					Explosion.explode(ship);                                                                        
					Ship.stopShip(ship);
					UFO.stopUfo(ufo);
					Missile.stopMissle(missile);
				}
			}
	}



}
