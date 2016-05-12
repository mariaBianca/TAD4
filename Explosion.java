import java.awt.Polygon;


public class Explosion extends SpaceObject {

	SpaceObject explosion = new SpaceObject();

	public void createExplosion(){



	}

	public void initExplosions() {

<<<<<<< HEAD
		int i;

		for (i = 0; i < AsteroidGame.MAX_SCRAP; i++) {
			AsteroidGame.explosion[i].shape = new Polygon();
			AsteroidGame.explosion[i].active = false;
			AsteroidGame.explosionCounter[i] = 0;
		}
		AsteroidGame.explosionIndex = 0;
	}

	public static void explode(SpaceObject s) {

		int c, i, j;
		int cx, cy;

		// Create sprites for explosion animation. The each individual line segment
		// of the given sObj is used to create a new sObj that will move
		// outward  from the sObj's original position with a random rotation.

		s.render();
		c = 2;
		//if (detail || s.sObj.npoints < 6)
		//c = 1;
		for (i = 0; i < s.sObj.npoints; i += c) {
			AsteroidGame.explosionIndex++;
			if (AsteroidGame.explosionIndex >= AsteroidGame.MAX_SCRAP)
				AsteroidGame.explosionIndex = 0;
			AsteroidGame.explosion[AsteroidGame.explosionIndex].active = true;
			AsteroidGame.explosion[AsteroidGame.explosionIndex].shape = new Polygon();
			j = i + 1;
			if (j >= s.sObj.npoints)
				j -= s.sObj.npoints;
			cx = (int) ((s.shape.xpoints[i] + s.shape.xpoints[j]) / 2);
			cy = (int) ((s.shape.ypoints[i] + s.shape.ypoints[j]) / 2);
			AsteroidGame.explosion[AsteroidGame.explosionIndex].shape.addPoint(
					s.shape.xpoints[i] - cx,
					s.shape.ypoints[i] - cy);
			AsteroidGame.explosion[AsteroidGame.explosionIndex].shape.addPoint(
					s.shape.xpoints[j] - cx,
					s.shape.ypoints[j] - cy);
			AsteroidGame.explosion[AsteroidGame.explosionIndex].x = s.x + cx;
			AsteroidGame.explosion[AsteroidGame.explosionIndex].y = s.y + cy;
			AsteroidGame.explosion[AsteroidGame.explosionIndex].deltaAngle = 4 * (Math.random() * 2 * AsteroidGame.MAX_ROCK_SPIN - AsteroidGame.MAX_ROCK_SPIN);
			AsteroidGame.explosion[AsteroidGame.explosionIndex].deltaX = (Math.random() * 2 * AsteroidGame.MAX_ROCK_SPEED - AsteroidGame.MAX_ROCK_SPEED + s.deltaX) / 2;
			AsteroidGame.explosion[AsteroidGame.explosionIndex].deltaY = (Math.random() * 2 * AsteroidGame.MAX_ROCK_SPEED - AsteroidGame.MAX_ROCK_SPEED + s.deltaY) / 2;
			AsteroidGame.explosionCounter[AsteroidGame.explosionIndex] = AsteroidGame.SCRAP_COUNT;
		}
	}

	public static void updateExplosions() {

		int i;

		// Move any active explosion debris. Stop explosion when its counter has
		// expired.

		for (i = 0; i < AsteroidGame.MAX_SCRAP; i++)
			if (AsteroidGame.explosion[i].active) {
				AsteroidGame.explosion[i].advance();
				AsteroidGame.explosion[i].render();
				if (-- AsteroidGame.explosionCounter[i] < 0)
					AsteroidGame.explosion[i].active = false;
			}
	}



	public void explode(Explosion s) {

		int c, i, j;
		int cx, cy;

		// Create sprites for explosion animation. The each individual line segment
		// of the given sObj is used to create a new sObj that will move
		// outward  from the sObj's original position with a random rotation.

		s.render();
		c = 2;
		if (AsteroidGame.detail || s.sObj.npoints < 6)
			c = 1;
		for (i = 0; i < s.sObj.npoints; i += c) {
			AsteroidGame.explosionIndex++;
			if (AsteroidGame.explosionIndex >= AsteroidGame.MAX_SCRAP)
				AsteroidGame.explosionIndex = 0;
			explosion.active = true;
			explosion.shape = new Polygon();
			j = i + 1;
			if (j >= s.sObj.npoints)
				j -= s.sObj.npoints;
			cx = (int) ((s.shape.xpoints[i] + s.shape.xpoints[j]) / 2);
			cy = (int) ((s.shape.ypoints[i] + s.shape.ypoints[j]) / 2);
			explosion.shape.addPoint(
					s.shape.xpoints[i] - cx,
					s.shape.ypoints[i] - cy);
			explosion.shape.addPoint(
					s.shape.xpoints[j] - cx,
					s.shape.ypoints[j] - cy);
			explosion.x = s.x + cx;
			explosion.y = s.y + cy;
			explosion.angle = s.angle;
			explosion.deltaAngle = 4 * (Math.random() * 2 * AsteroidGame.MAX_ROCK_SPIN - AsteroidGame.MAX_ROCK_SPIN);
			explosion.deltaX = (Math.random() * 2 * AsteroidGame.MAX_ROCK_SPEED - AsteroidGame.MAX_ROCK_SPEED + s.deltaX) / 2;
			explosion.deltaY = (Math.random() * 2 * AsteroidGame.MAX_ROCK_SPEED - AsteroidGame.MAX_ROCK_SPEED + s.deltaY) / 2;
			// AsteroidGame.explosionCounter = AsteroidGame.SCRAP_COUNT;
		}
	}
=======
	   

	    
	      explosion.shape = new Polygon();
	      explosion.active = false;
	    

	  }

	  public void explode(Explosion s) {

	    int c, i, j;
	    int cx, cy;

	    // Create sprites for explosion animation. The each individual line segment
	    // of the given sObj is used to create a new sObj that will move
	    // outward  from the sObj's original position with a random rotation.

	    s.render();
	    c = 2;
	    if (AsteroidGame.detail || s.sObj.npoints < 6)
	      c = 1;
	    for (i = 0; i < s.sObj.npoints; i += c) {
	      AsteroidGame.explosionIndex++;
	      if (AsteroidGame.explosionIndex >= AsteroidGame.MAX_SCRAP)
	        AsteroidGame.explosionIndex = 0;
	      explosion.active = true;
	      explosion.shape = new Polygon();
	      j = i + 1;
	      if (j >= s.sObj.npoints)
	        j -= s.sObj.npoints;
	      cx = (int) ((s.shape.xpoints[i] + s.shape.xpoints[j]) / 2);
	      cy = (int) ((s.shape.ypoints[i] + s.shape.ypoints[j]) / 2);
	      explosion.shape.addPoint(
	        s.shape.xpoints[i] - cx,
	        s.shape.ypoints[i] - cy);
	      explosion.shape.addPoint(
	        s.shape.xpoints[j] - cx,
	        s.shape.ypoints[j] - cy);
	      explosion.x = s.x + cx;
	      explosion.y = s.y + cy;
	      explosion.angle = s.angle;
	      explosion.deltaAngle = 4 * (Math.random() * 2 * AsteroidGame.MAX_ROCK_SPIN - AsteroidGame.MAX_ROCK_SPIN);
	      explosion.deltaX = (Math.random() * 2 * AsteroidGame.MAX_ROCK_SPEED - AsteroidGame.MAX_ROCK_SPEED + s.deltaX) / 2;
	      explosion.deltaY = (Math.random() * 2 * AsteroidGame.MAX_ROCK_SPEED - AsteroidGame.MAX_ROCK_SPEED + s.deltaY) / 2;
	     // AsteroidGame.explosionCounter = AsteroidGame.SCRAP_COUNT;
	    }
	  }

	  public void updateExplosions() {

	    int i;

	    // Move any active explosion debris. Stop explosion when its counter has
	    // expired.

	    for (i = 0; i < AsteroidGame.MAX_SCRAP; i++)
	      if (explosion.active) {
	        explosion.advance();
	        explosion.render();
	        if (--AsteroidGame.explosionCounter[i] < 0)
	          explosion.active = false;
	      }
	  }
>>>>>>> e929581dc911aea34b11c8de3274f5c83632913a

}

