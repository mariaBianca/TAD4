import java.awt.Polygon;

public class Asteroid {
	
	
	  public static void initAsteroids() {

		    int i, j;
		    int s;
		    double theta, r;
		    int x, y;

		    // Create random shapes, positions and movements for each asteroid.

		    for (i = 0; i < AsteroidGame.MAX_ROCKS; i++) {

		      // Create a jagged shape for the asteroid and give it a random rotation.

		    	AsteroidGame.asteroids[i].shape = new Polygon();
		      s = AsteroidGame.MIN_ROCK_SIDES + (int) (Math.random() * (AsteroidGame.MAX_ROCK_SIDES - AsteroidGame.MIN_ROCK_SIDES));
		      for (j = 0; j < s; j ++) {
		        theta = 2 * Math.PI / s * j;
		        r = AsteroidGame.MIN_ROCK_SIZE + (int) (Math.random() * (AsteroidGame.MAX_ROCK_SIZE - AsteroidGame.MIN_ROCK_SIZE));
		        x = (int) -Math.round(r * Math.sin(theta));
		        y = (int)  Math.round(r * Math.cos(theta));
		        AsteroidGame.asteroids[i].shape.addPoint(x, y);
		      }
		      AsteroidGame.asteroids[i].active = true;
		      AsteroidGame.asteroids[i].angle = 0.0;
		      AsteroidGame.asteroids[i].deltaAngle = Math.random() * 2 * AsteroidGame.MAX_ROCK_SPIN - AsteroidGame.MAX_ROCK_SPIN;

		      // Place the asteroid at one edge of the screen.

		      if (Math.random() < 0.5) {
		    	  AsteroidGame.asteroids[i].x = -SpaceObject.width / 2;
		        if (Math.random() < 0.5)
		        	AsteroidGame.asteroids[i].x = SpaceObject.width / 2;
		        AsteroidGame.asteroids[i].y = Math.random() * SpaceObject.height;
		      }
		      else {
		    	  AsteroidGame.asteroids[i].x = Math.random() * SpaceObject.width;
		    	  AsteroidGame.asteroids[i].y = -SpaceObject.height / 2;
		        if (Math.random() < 0.5)
		        	AsteroidGame.asteroids[i].y = SpaceObject.height / 2;
		      }

		      // Set a random motion for the asteroid.

		      AsteroidGame.asteroids[i].deltaX = Math.random() * AsteroidGame.asteroidsSpeed;
		      if (Math.random() < 0.5)
		    	  AsteroidGame.asteroids[i].deltaX = -AsteroidGame.asteroids[i].deltaX;
		      AsteroidGame.asteroids[i].deltaY = Math.random() * AsteroidGame.asteroidsSpeed;
		      if (Math.random() < 0.5)
		    	  AsteroidGame.asteroids[i].deltaY = -AsteroidGame.asteroids[i].deltaY;

		      AsteroidGame.asteroids[i].render();
		      AsteroidGame.asteroidIsSmall[i] = false;
		    }

		    AsteroidGame.asteroidsCounter = AsteroidGame.STORM_PAUSE;
		    AsteroidGame.asteroidsLeft = AsteroidGame.MAX_ROCKS;
		    if (AsteroidGame.asteroidsSpeed < AsteroidGame.MAX_ROCK_SPEED)
		    	AsteroidGame.asteroidsSpeed += 0.5;
		  }

		  public static void initSmallAsteroids(int n) {

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
		    tempX = AsteroidGame.asteroids[n].x;
		    tempY = AsteroidGame.asteroids[n].y;
		    do {
		      if (!AsteroidGame.asteroids[i].active) {
		    	  AsteroidGame.asteroids[i].shape = new Polygon();
		        s = AsteroidGame.MIN_ROCK_SIDES + (int) (Math.random() * (AsteroidGame.MAX_ROCK_SIDES - AsteroidGame.MIN_ROCK_SIDES));
		        for (j = 0; j < s; j ++) {
		          theta = 2 * Math.PI / s * j;
		          r = (AsteroidGame.MIN_ROCK_SIZE + (int) (Math.random() * (AsteroidGame.MAX_ROCK_SIZE - AsteroidGame.MIN_ROCK_SIZE))) / 2;
		          x = (int) -Math.round(r * Math.sin(theta));
		          y = (int)  Math.round(r * Math.cos(theta));
		          AsteroidGame.asteroids[i].shape.addPoint(x, y);
		        }
		        AsteroidGame.asteroids[i].active = true;
		        AsteroidGame.asteroids[i].angle = 0.0;
		        AsteroidGame.asteroids[i].deltaAngle = Math.random() * 2 * AsteroidGame.MAX_ROCK_SPIN - AsteroidGame.MAX_ROCK_SPIN;
		        AsteroidGame.asteroids[i].x = tempX;
		        AsteroidGame.asteroids[i].y = tempY;
		        AsteroidGame.asteroids[i].deltaX = Math.random() * 2 * AsteroidGame.asteroidsSpeed - AsteroidGame.asteroidsSpeed;
		        AsteroidGame.asteroids[i].deltaY = Math.random() * 2 * AsteroidGame.asteroidsSpeed - AsteroidGame.asteroidsSpeed;
		        AsteroidGame.asteroids[i].render();
		        AsteroidGame.asteroidIsSmall[i] = true;
		        count++;
		        AsteroidGame.asteroidsLeft++;
		      }
		      i++;
		    } while (i < AsteroidGame.MAX_ROCKS && count < 2);
		  }

		  public static void updateAsteroids() {

		    int i, j;

		    // Move any active asteroids and check for collisions.

		    for (i = 0; i < AsteroidGame.MAX_ROCKS; i++)
		      if (AsteroidGame.asteroids[i].active) {
		    	  AsteroidGame.asteroids[i].advance();
		    	  AsteroidGame.asteroids[i].render();

		        // If hit by photon, kill asteroid and advance score. If asteroid is
		        // large, make some smaller ones to replace it.

		        for (j = 0; j < AsteroidGame.MAX_SHOTS; j++)
		          if (AsteroidGame.photons[j].active && AsteroidGame.asteroids[i].active && AsteroidGame.asteroids[i].isColliding(AsteroidGame.photons[j])) {
		        	  AsteroidGame.asteroidsLeft--;
		        	  AsteroidGame.asteroids[i].active = false;
		        	  AsteroidGame.photons[j].active = false;
		            if (AsteroidGame.sound)
		              Audio.explosionSound.play();
		            Explosion.explode(AsteroidGame.asteroids[i]);
		            if (! AsteroidGame.asteroidIsSmall[i]) {
		            	AsteroidGame.score += AsteroidGame.BIG_POINTS;
		              initSmallAsteroids(i);
		            }
		            else
		            	AsteroidGame.score += AsteroidGame.SMALL_POINTS;
		          }

		        // If the ship is not in hyperspace, see if it is hit.

		        if (AsteroidGame.ship.active && AsteroidGame.hyperCounter <= 0 &&
		        		AsteroidGame.asteroids[i].active && AsteroidGame.asteroids[i].isColliding(AsteroidGame.ship)) {
		          if (AsteroidGame.sound)
		            Audio.crashSound.play();
		          Explosion.explode(AsteroidGame.ship);
		          AsteroidGame.ship.stopShip();
		          UFOclass.stopUfo();
		          AsteroidGame. missile.stopMissle();
		        }
		    }
		  }


}
