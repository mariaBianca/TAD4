import java.awt.Polygon;

public class Explosion extends SpaceObject{
	

	public static void initExplosions(Explosion[] explosions, int[] explosionCounter) {

		int i;

		for (i = 0; i < AsteroidGame.MAX_SCRAP; i++) {
			explosions[i].shape = new Polygon();
			explosions[i].active = false;
			explosionCounter[i] = 0;
		}
		AsteroidGame.explosionIndex = 0;
	}
	
	public static void updateExplosions(Explosion[] explosions, int[] explosionCounter) {

		int i;

		// Move any active explosion debris. Stop explosion when its counter has
		// expired.

		for (i = 0; i < AsteroidGame.MAX_SCRAP; i++)
			if (explosions[i].active) {
				explosions[i].advance();
				explosions[i].render();
				if (-- explosionCounter[i] < 0)
					explosions[i].active = false;
			}
	}
	

	public void explode(SpaceObject s, Explosion[] explosions, int[] explosionCounter) {

		int c, i, j;
		int cx, cy;

		// Create sprites for explosion animation. The each individual line segment
		// of the given sprite is used to create a new sprite that will move
		// outward  from the sprite's original position with a random rotation.

		s.render();
		c = 2;
		if (AsteroidGame.detail || s.sprite.npoints < 6)
			c = 1;
		for (i = 0; i < s.sprite.npoints; i += c) {
			AsteroidGame.explosionIndex++;
			if (AsteroidGame.explosionIndex >= AsteroidGame.MAX_SCRAP)
				AsteroidGame.explosionIndex = 0;
			explosions[AsteroidGame.explosionIndex].active = true;
			explosions[AsteroidGame.explosionIndex].shape = new Polygon();
			j = i + 1;
			if (j >= s.sprite.npoints)
				j -= s.sprite.npoints;
			cx = (int) ((s.shape.xpoints[i] + s.shape.xpoints[j]) / 2);
			cy = (int) ((s.shape.ypoints[i] + s.shape.ypoints[j]) / 2);
			explosions[AsteroidGame.explosionIndex].shape.addPoint(
					s.shape.xpoints[i] - cx,
					s.shape.ypoints[i] - cy);
			explosions[AsteroidGame.explosionIndex].shape.addPoint(
					s.shape.xpoints[j] - cx,
					s.shape.ypoints[j] - cy);
			explosions[AsteroidGame.explosionIndex].x = s.x + cx;
			explosions[AsteroidGame.explosionIndex].y = s.y + cy;
			explosions[AsteroidGame.explosionIndex].angle = s.angle;
			explosions[AsteroidGame.explosionIndex].deltaAngle = 4 * (Math.random() * 2 * AsteroidGame.MAX_ROCK_SPIN - AsteroidGame.MAX_ROCK_SPIN);
			explosions[AsteroidGame.explosionIndex].deltaX = (Math.random() * 2 * AsteroidGame.MAX_ROCK_SPEED - AsteroidGame.MAX_ROCK_SPEED + s.deltaX) / 2;
			explosions[AsteroidGame.explosionIndex].deltaY = (Math.random() * 2 * AsteroidGame.MAX_ROCK_SPEED - AsteroidGame.MAX_ROCK_SPEED + s.deltaY) / 2;
			explosionCounter[AsteroidGame.explosionIndex] = AsteroidGame.SCRAP_COUNT;
		}
	}





}
