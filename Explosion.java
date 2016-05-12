import java.awt.Polygon;

//This class represents the object Explosion and its implementation
public class Explosion extends SpaceObject {

	//create the explosion object
	//SpaceObject explosion = new SpaceObject();

	static final int MAX_SCRAP = 40;          // explosions.
	// Explosion data.
	static int[] explosionCounter = new int[MAX_SCRAP];  // Time counters for explosions.
	static int   explosionIndex;   


	public static void initExplosions() {

		int i;

		for (i = 0; i <MAX_SCRAP; i++) {
			AsteroidGame.explosion[i].shape = new Polygon();
			AsteroidGame.explosion[i].active = false;
			explosionCounter[i] = 0;
		}
		explosionIndex = 0;
	}

	public static void explode(SpaceObject s) {

		int c, i, j;
		int cx, cy;

		// Create objects for explosion animation. The each individual line segment
		// of the given sObj is used to create a new sObj that will move
		// outward  from the sObj's original position with a random rotation.

		s.render();
		c = 2;
		if (AsteroidGame.detail || s.sObj.npoints < 6)
			c = 1;
		for (i = 0; i < s.sObj.npoints; i += c) {
			explosionIndex++;
			if (explosionIndex >= MAX_SCRAP)
				explosionIndex = 0;
			AsteroidGame.explosion[explosionIndex].active = true;
			AsteroidGame.explosion[explosionIndex].shape = new Polygon();
			j = i + 1;
			if (j >= s.sObj.npoints)
				j -= s.sObj.npoints;
			cx = (int) ((s.shape.xpoints[i] + s.shape.xpoints[j]) / 2);
			cy = (int) ((s.shape.ypoints[i] + s.shape.ypoints[j]) / 2);
			AsteroidGame.explosion[explosionIndex].shape.addPoint(
					s.shape.xpoints[i] - cx,
					s.shape.ypoints[i] - cy);
			AsteroidGame.explosion[explosionIndex].shape.addPoint(
					s.shape.xpoints[j] - cx,
					s.shape.ypoints[j] - cy);
			AsteroidGame.explosion[explosionIndex].x = s.x + cx;
			AsteroidGame.explosion[explosionIndex].y = s.y + cy;
			AsteroidGame.explosion[explosionIndex].deltaAngle = 4 * (Math.random() * 2 * AsteroidGame.MAX_ROCK_SPIN - AsteroidGame.MAX_ROCK_SPIN);
			AsteroidGame.explosion[explosionIndex].deltaX = (Math.random() * 2 * AsteroidGame.MAX_ROCK_SPEED - AsteroidGame.MAX_ROCK_SPEED + s.deltaX) / 2;
			AsteroidGame.explosion[explosionIndex].deltaY = (Math.random() * 2 * AsteroidGame.MAX_ROCK_SPEED - AsteroidGame.MAX_ROCK_SPEED + s.deltaY) / 2;
			explosionCounter[explosionIndex] = AsteroidGame.SCRAP_COUNT;
		}
	}

	public static void updateExplosions() {

		int i;

		// Move any active explosion debris. Stop explosion when its counter has
		// expired.

		for (i = 0; i < MAX_SCRAP; i++)
			if (AsteroidGame.explosion[i].active) {
				AsteroidGame.explosion[i].advance();
				AsteroidGame.explosion[i].render();
				if (--explosionCounter[i] < 0)
					AsteroidGame.explosion[i].active = false;
			}
	}



	public void explode(Explosion s) {

		int c, i, j;
		int cx, cy;

		// Create objects for explosion animation. The each individual line segment
		// of the given sObj is used to create a new sObj that will move
		// outward  from the sObj's original position with a random rotation.

		s.render();
		c = 2;
		if (AsteroidGame.detail || s.sObj.npoints < 6)
			c = 1;
		for (i = 0; i < s.sObj.npoints; i += c) {
			explosionIndex++;
			if (explosionIndex >= MAX_SCRAP)
				explosionIndex = 0;
			s.active = true;
			s.shape = new Polygon();
			j = i + 1;
			if (j >= s.sObj.npoints)
				j -= s.sObj.npoints;
			cx = (int) ((s.shape.xpoints[i] + s.shape.xpoints[j]) / 2);
			cy = (int) ((s.shape.ypoints[i] + s.shape.ypoints[j]) / 2);
			s.shape.addPoint(
					s.shape.xpoints[i] - cx,
					s.shape.ypoints[i] - cy);
			s.shape.addPoint(
					s.shape.xpoints[j] - cx,
					s.shape.ypoints[j] - cy);
			s.x = s.x + cx;
			s.y = s.y + cy;
			s.angle = s.angle;
			s.deltaAngle = 4 * (Math.random() * 2 * AsteroidGame.MAX_ROCK_SPIN - AsteroidGame.MAX_ROCK_SPIN);
			s.deltaX = (Math.random() * 2 * AsteroidGame.MAX_ROCK_SPEED - AsteroidGame.MAX_ROCK_SPEED + s.deltaX) / 2;
			s.deltaY = (Math.random() * 2 * AsteroidGame.MAX_ROCK_SPEED - AsteroidGame.MAX_ROCK_SPEED + s.deltaY) / 2;
			// AsteroidGame.explosionCounter = AsteroidGame.SCRAP_COUNT;
		}
	}

}

