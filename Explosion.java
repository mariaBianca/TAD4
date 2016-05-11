import java.awt.Polygon;


public class Explosion extends SpaceObject {
	
	SpaceObject explosion = new SpaceObject();
	
	public void createExplosion(){
		
		
		
	}
		
	public void initExplosions() {

	   

	    
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
	    if (Asteroids.detail || s.sObj.npoints < 6)
	      c = 1;
	    for (i = 0; i < s.sObj.npoints; i += c) {
	      Asteroids.explosionIndex++;
	      if (Asteroids.explosionIndex >= Asteroids.MAX_SCRAP)
	        Asteroids.explosionIndex = 0;
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
	      explosion.deltaAngle = 4 * (Math.random() * 2 * Asteroids.MAX_ROCK_SPIN - Asteroids.MAX_ROCK_SPIN);
	      explosion.deltaX = (Math.random() * 2 * Asteroids.MAX_ROCK_SPEED - Asteroids.MAX_ROCK_SPEED + s.deltaX) / 2;
	      explosion.deltaY = (Math.random() * 2 * Asteroids.MAX_ROCK_SPEED - Asteroids.MAX_ROCK_SPEED + s.deltaY) / 2;
	     // Asteroids.explosionCounter = Asteroids.SCRAP_COUNT;
	    }
	  }

	  public void updateExplosions() {

	    int i;

	    // Move any active explosion debris. Stop explosion when its counter has
	    // expired.

	    for (i = 0; i < Asteroids.MAX_SCRAP; i++)
	      if (explosion.active) {
	        explosion.advance();
	        explosion.render();
	        if (--Asteroids.explosionCounter[i] < 0)
	          explosion.active = false;
	      }
	  }

}
