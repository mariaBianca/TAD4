
public class Ship extends SpaceObject {


	public static void defineShip(Ship ship){
		ship.shape.addPoint(0, -10);
		ship.shape.addPoint(7, 10);
		ship.shape.addPoint(-7, 10);
	}


	public static void initShip(Ship ship, Thruster fwdThruster, Thruster revThruster) {

		// Reset the ship sprite at the center of the screen.

		ship.active = true;
		ship.angle = 0.0;
		ship.deltaAngle = 0.0;
		ship.x = 0.0;
		ship.y = 0.0;
		ship.deltaX = 0.0;
		ship.deltaY = 0.0;
		ship.render();

		// Initialize thruster sprites.

		fwdThruster.x = ship.x;
		fwdThruster.y = ship.y;
		fwdThruster.angle = ship.angle;
		fwdThruster.render();
		revThruster.x = ship.x;
		revThruster.y = ship.y;
		revThruster.angle = ship.angle;
		revThruster.render();

		if (AsteroidGame.loaded)
			AsteroidGame.thrustersSound.stop();
		AsteroidGame.thrustersPlaying = false;
		AsteroidGame.hyperCounter = 0;
	}
	

	  public static void updateShip(Ship ship, boolean left, boolean right, boolean down, boolean up, 
			  Thruster fwdThruster, Thruster revThruster) {

	    double dx, dy, speed;

	    if (!AsteroidGame.playing)
	      return;

	    // Rotate the ship if left or right cursor key is down.

	    if (left) {
	      ship.angle += AsteroidGame.SHIP_ANGLE_STEP;
	      if (ship.angle > 2 * Math.PI)
	        ship.angle -= 2 * Math.PI;
	    }
	    if (right) {
	      ship.angle -= AsteroidGame.SHIP_ANGLE_STEP;
	      if (ship.angle < 0)
	        ship.angle += 2 * Math.PI;
	    }

	    // Fire thrusters if up or down cursor key is down.

	    dx = AsteroidGame.SHIP_SPEED_STEP * -Math.sin(ship.angle);
	    dy = AsteroidGame.SHIP_SPEED_STEP *  Math.cos(ship.angle);
	    if (up) {
	      ship.deltaX += dx;
	      ship.deltaY += dy;
	    }
	    if (down) {
	        ship.deltaX -= dx;
	        ship.deltaY -= dy;
	    }

	    // Don't let ship go past the speed limit.

	    if (up || down) {
	      speed = Math.sqrt(ship.deltaX * ship.deltaX + ship.deltaY * ship.deltaY);
	      if (speed > AsteroidGame.MAX_SHIP_SPEED) {
	        dx = AsteroidGame.MAX_SHIP_SPEED * -Math.sin(ship.angle);
	        dy = AsteroidGame.MAX_SHIP_SPEED *  Math.cos(ship.angle);
	        if (up)
	          ship.deltaX = dx;
	        else
	          ship.deltaX = -dx;
	        if (up)
	          ship.deltaY = dy;
	        else
	          ship.deltaY = -dy;
	      }
	    }

	    // Move the ship. If it is currently in hyperspace, advance the countdown.

	    if (ship.active) {
	      ship.advance();
	      ship.render();
	      if (AsteroidGame.hyperCounter > 0)
	        AsteroidGame.hyperCounter--;

	      // Update the thruster sprites to match the ship sprite.

	      fwdThruster.x = ship.x;
	      fwdThruster.y = ship.y;
	      fwdThruster.angle = ship.angle;
	      fwdThruster.render();
	      revThruster.x = ship.x;
	      revThruster.y = ship.y;
	      revThruster.angle = ship.angle;
	      revThruster.render();
	    }

	    // Ship is exploding, advance the countdown or create a new ship if it is
	    // done exploding. The new ship is added as though it were in hyperspace.
	    // (This gives the player time to move the ship if it is in imminent
	    // danger.) If that was the last ship, end the game.

	    else
	      if (--AsteroidGame.shipCounter <= 0)
	        if (AsteroidGame.shipsLeft > 0) {
	          Ship.initShip(ship, fwdThruster, revThruster);
	          AsteroidGame.hyperCounter = AsteroidGame.HYPER_COUNT;
	        }
	        //else
	          //AsteroidGame.endGame();
	  }
	  
	  public static void stopShip(Ship ship) {

		    ship.active = false;
		    AsteroidGame.shipCounter = AsteroidGame.SCRAP_COUNT;
		    if (AsteroidGame.shipsLeft > 0)
		      AsteroidGame.shipsLeft--;
		    if (AsteroidGame.loaded)
		      AsteroidGame.thrustersSound.stop();
		    AsteroidGame.thrustersPlaying = false;
		  }


}
