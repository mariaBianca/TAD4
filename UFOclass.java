
public class UFOclass extends SpaceObject{
	
	static UFOclass ufo = new UFOclass();
	
	
	public void createUFO(){
	    // Create shape for the flying saucer.
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
	
	  public static void initUfo() {

		    double angle, speed;

		    // Randomly set flying saucer at left or right edge of the screen.

		    ufo.active = true;
		    ufo.x = -SpaceObject.width / 2;
		    ufo.y = Math.random() * 2 * SpaceObject.height - SpaceObject.height;
		    angle = Math.random() * Math.PI / 4 - Math.PI / 2;
		    speed = Asteroids.MAX_ROCK_SPEED / 2 + Math.random() * (Asteroids.MAX_ROCK_SPEED / 2);
		    ufo.deltaX = speed * -Math.sin(angle);
		    ufo.deltaY = speed *  Math.cos(angle);
		    if (Math.random() < 0.5) {
		      ufo.x = SpaceObject.width / 2;
		      ufo.deltaX = -ufo.deltaX;
		    }
		    if (ufo.y > 0)
		      ufo.deltaY = ufo.deltaY;
		    ufo.render();
		    Asteroids.saucerPlaying = true;
		    if (Asteroids.sound)
		      Asteroids.saucerSound.loop();
		    Asteroids.ufoCounter = (int) Math.abs(SpaceObject.width / ufo.deltaX);
		  }
	  
	  public static void updateUfo() {

		    int i, d;
		    boolean wrapped;

		    // Move the flying saucer and check for collision with a photon. Stop it
		    // when its counter has expired.

		    if (ufo.active) {
		      if (--Asteroids.ufoCounter <= 0) {
		        if (--Asteroids.ufoPassesLeft > 0)
		          UFOclass.initUfo();
		        else
		          stopUfo();
		      }
		      if (ufo.active) {
		        ufo.advance();
		        ufo.render();
		        for (i = 0; i < Asteroids.MAX_SHOTS; i++)
		          if (Asteroids.photons[i].active && ufo.isColliding(Asteroids.photons[i])) {
		            if (Asteroids.sound)
		             // Asteroids.crashSound.play();
		            //Asteroids.explode(ufo);
		            stopUfo();
		            Asteroids.score += Asteroids.UFO_POINTS;
		          }

		          // On occassion, fire a missle at the ship if the saucer is not too
		          // close to it.

		         // d = (int) Math.max(Math.abs(ufo.x - ship.x), Math.abs(ufo.y - ship.y));
		          //if (ship.active && hyperCounter <= 0 &&
		            //  ufo.active && !missle.active &&
		              //d > MAX_ROCK_SPEED * FPS / 2 &&
		              //Math.random() < MISSLE_PROBABILITY)
		            //initMissle();
		       }
		    }
		  }

	  public static void stopUfo() {

		    ufo.active = false;
		    Asteroids.ufoCounter = 0;
		    Asteroids.ufoPassesLeft = 0;
		    if (Asteroids.loaded)
		      Asteroids.saucerSound.stop();
		    Asteroids.saucerPlaying = false;
		  }

}
