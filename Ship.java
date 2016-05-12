
public class Ship extends SpaceObject{

	//this class represents the definition and the implementations of the Object Ship

	static Ship ship = new Ship();
	Thruster fwdThruster = new Thruster();
	Thruster revThruster = new Thruster();

	// Ship's rotation and acceleration rates and maximum speed.

	static final double SHIP_ANGLE_STEP = Math.PI / AsteroidGame.FPS;
	static final double SHIP_SPEED_STEP = 15.0 / AsteroidGame.FPS;
	static final double MAX_SHIP_SPEED  = 1.25 * AsteroidGame.MAX_ROCK_SPEED;




	public void defineShip(){

		ship.shape.addPoint(0, -10);
		ship.shape.addPoint(7, 10);
		ship.shape.addPoint(-7, 10);
	}


	public static void initShip() {

		// Reset the ship sObj at the center of the screen.

		active = true;
		angle = 0.0;
		deltaAngle = 0.0;
		x = 0.0;
		y = 0.0;
		deltaX = 0.0;
		deltaY = 0.0;
		ship.render();

		// Initialize thruster sprites.

		//fwdThruster.x = ship.x;
		//fwdThruster.y = ship.y;
		//fwdThruster.angle = ship.angle;
		//fwdThruster.render();
		//revThruster.x = ship.x;
		//revThruster.y = ship.y;
		//revThruster.angle = ship.angle;
		//revThruster.render();

		if (AsteroidGame.loaded)
			//thrustersSound.stop();
			Audio.thrustersPlaying = false;
		AsteroidGame.hyperCounter = 0;
	}

	public void updateShip() {

		double dx, dy, speed;

		if (!AsteroidGame.playing)
			return;

		// Rotate the ship if left or right cursor key is down.

		if (AsteroidGame.left) {
			ship.angle += SHIP_ANGLE_STEP;
			if (ship.angle > 2 * Math.PI)
				ship.angle -= 2 * Math.PI;
		}
		if (AsteroidGame.right) {
			ship.angle -= SHIP_ANGLE_STEP;
			if (ship.angle < 0)
				ship.angle += 2 * Math.PI;
		}

		// Fire thrusters if up or down cursor key is down.

		dx = SHIP_SPEED_STEP * -Math.sin(ship.angle);
		dy = SHIP_SPEED_STEP *  Math.cos(ship.angle);
		if (AsteroidGame.up) {
			ship.deltaX += dx;
			ship.deltaY += dy;
		}
		if (AsteroidGame.down) {
			ship.deltaX -= dx;
			ship.deltaY -= dy;
		}

		// Don't let ship go past the speed limit.

		if (AsteroidGame.up || AsteroidGame.down) {
			speed = Math.sqrt(ship.deltaX * ship.deltaX + ship.deltaY * ship.deltaY);
			if (speed > MAX_SHIP_SPEED) {
				dx = MAX_SHIP_SPEED * -Math.sin(ship.angle);
				dy = MAX_SHIP_SPEED *  Math.cos(ship.angle);
				if (AsteroidGame.up)
					ship.deltaX = dx;
				else
					ship.deltaX = -dx;
				if (AsteroidGame.up)
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

			// Update the thruster sprites to match the ship sObj.

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
			if (-- AsteroidGame.shipCounter <= 0)
				if (AsteroidGame.shipsLeft > 0) {
					initShip();
					AsteroidGame.hyperCounter = AsteroidGame.HYPER_COUNT;
				}
				else
					GameSettings.endGame();
	}

	public static void stopShip() {

		active = false;
		AsteroidGame.shipCounter = AsteroidGame.SCRAP_COUNT;
		if (AsteroidGame.shipsLeft > 0)
			AsteroidGame.shipsLeft--;
		if (AsteroidGame.loaded)
			//thrustersSound.stop();
			Audio.thrustersPlaying = false;
	}

}