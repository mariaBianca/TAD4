
public class Missile extends SpaceObject{
	
	SpaceObject missile = new SpaceObject();
	UFOclass ufo = new UFOclass();
	SpaceObject ship = new SpaceObject();
	
	
	
	
	public void createMissile(){
	    // Create shape for the guided missile.

	    missile = new Missile();
	    missile.shape.addPoint(0, -4);
	    missile.shape.addPoint(1, -3);
	    missile.shape.addPoint(1, 3);
	    missile.shape.addPoint(2, 4);
	    missile.shape.addPoint(-2, 4);
	    missile.shape.addPoint(-1, 3);
	    missile.shape.addPoint(-1, -3);

	}
	
	  public void initMissle() {

		    missile.active = true;
		    missile.angle = 0.0;
		    missile.deltaAngle = 0.0;
		    missile.x = ufo.x;
		    missile.y = ufo.y;
		    missile.deltaX = 0.0;
		    missile.deltaY = 0.0;
		    missile.render();
		    Asteroids.missleCounter = Asteroids.MISSLE_COUNT;
		    if (Asteroids.sound)
		      Asteroids.missleSound.loop();
		    Asteroids.misslePlaying = true;
		  }


	  public void updateMissle() {

	    int i;

	    // Move the guided missile and check for collision with ship or photon. Stop
	    // it when its counter has expired.

	    if (missile.active) {
	      if (--Asteroids.missleCounter <= 0)
	        stopMissle();
	      else {
	        guideMissle();
	        missile.advance();
	        missile.render();
	        for (i = 0; i < Asteroids.MAX_SHOTS; i++)
	          if (Asteroids.photons[i].active && missile.isColliding(Asteroids.photons[i])) {
	            if (Asteroids.sound)
	              Asteroids.crashSound.play();
	            //explode(missile);
	            stopMissle();
	            Asteroids.score += Asteroids.MISSLE_POINTS;
	          }
	        if (missile.active && ship.active &&
	            Asteroids.hyperCounter <= 0 && ship.isColliding(missile)) {
	          if (Asteroids.sound)
	            //crashSound.play();
	          //explode(ship);
	          //stopShip();
	          UFOclass.stopUfo();
	          stopMissle();
	        }
	      }
	    }
	  }

	  public void guideMissle() {

	    double dx, dy, angle;

	    if (!ship.active || Asteroids.hyperCounter > 0)
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

	    missile.deltaX = 0.75 * Asteroids.MAX_ROCK_SPEED * -Math.sin(missile.angle);
	    missile.deltaY = 0.75 * Asteroids.MAX_ROCK_SPEED *  Math.cos(missile.angle);
	  }

	  public void stopMissle() {

	    missile.active = false;
	    Asteroids.missleCounter = 0;
	    if (Asteroids.loaded)
	      //missleSound.stop();
	    Asteroids.misslePlaying = false;
	  }

}
