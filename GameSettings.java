
public class GameSettings extends SpaceObject {
	
	  @SuppressWarnings("unused")
	public static void initGame() {

		    // Initialize game data and sprites.

<<<<<<< HEAD
			int score = 0;
			int shipsLeft = AsteroidGame.MAX_SHIPS;
=======
		    int score = 0;
		    int shipsLeft = AsteroidGame.MAX_SHIPS;
>>>>>>> e929581dc911aea34b11c8de3274f5c83632913a
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

		  @SuppressWarnings("unused")
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