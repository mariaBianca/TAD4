
public class GameSettings extends SpaceObject {
	
	  public static void initGame() {

		    // Initialize game data and sprites.

		    int score = 0;
		    int shipsLeft = AsteroidGame.MAX_SHIPS;
		    double AsteroidGameSpeed = AsteroidGame.MIN_ROCK_SPEED;
		    int newShipScore = AsteroidGame.NEW_SHIP_POINTS;
		    int newUfoScore = AsteroidGame.NEW_UFO_POINTS;
		    //initShip();
		    //initPhotons();
		    //stopUfo();
		    //stopMissle();
		    //initAsteroidGame();
		    //initExplosions();
		    boolean playing = true;
		    boolean paused = false;
		    long photonTime = System.currentTimeMillis();
		  }

		  public static void endGame() {

		    // Stop ship, flying saucer, guided missle and associated sounds.

		    boolean playing = false;
		    //stopShip();
		    //stopUfo();
		    //stopMissle();
		  }

		  public void start() {

//		    if (loopThread == null) {
//		      loopThread = new Thread(this);
//		      loopThread.start();
//		    }
//		    if (!loaded && loadThread == null) {
//		      loadThread = new Thread(this);
//		      loadThread.start();
//		    }
		  }

		  public void stop() {

//		    if (loopThread != null) {
//		      loopThread.stop();
//		      loopThread = null;
//		    }
//		    if (loadThread != null) {
//		      loadThread.stop();
//		      loadThread = null;
//		    }
		  }


}
