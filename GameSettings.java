
public class GameSettings extends SpaceObject {
	
	  @SuppressWarnings("unused")
	public static void initGame() {

		    // Initialize game data and sprites.

			int score = 0;
			int shipsLeft = AsteroidGame.MAX_SHIPS;
		    double AsteroidGameSpeed = AsteroidGame.MIN_ROCK_SPEED;
		    int newShipScore = AsteroidGame.NEW_SHIP_POINTS;
		    int newUfoScore = AsteroidGame.NEW_UFO_POINTS;
		    Ship.initShip();
		    AsteroidGame.initPhotons();
		    UFO.stopUfo();
		    Missile.stopMissle();
		    Asteroid.initAsteroids();
		    Explosion.initExplosions();
		    boolean playing = true;
		    boolean paused = false;
		    long photonTime = System.currentTimeMillis();
		  }

		  @SuppressWarnings("unused")
		public static void endGame() {

		    // Stop ship, flying saucer, guided missle and associated sounds.

		    boolean playing = false;
		    Ship.stopShip();
		    UFO.stopUfo();
		    Missile.stopMissle();
		  }

		  public void start() {

		    if (AsteroidGame.loopThread == null) {
		      AsteroidGame.loopThread = new Thread();
		      AsteroidGame.loopThread.start();
		    }
		    if (!AsteroidGame.loaded && AsteroidGame.loadThread == null) {
		    	AsteroidGame.loadThread = new Thread();
		    	AsteroidGame.loadThread.start();
		    }
		  }

		  public void stop() {

		    if (AsteroidGame.loopThread != null) {
		    	AsteroidGame.loopThread.stop();
		    	AsteroidGame.loopThread = null;
		    }
		    if (AsteroidGame.loadThread != null) {
		    	AsteroidGame.loadThread.stop();
		    	AsteroidGame.loadThread = null;
		    }
		  }


}