/******************************************************************************
  Asteroids, Version 1.3

  Copyright 1998-2001 by Mike Hall.
  Please see http://www.brainjar.com for terms of use.

  Revision History:

  1.01, 12/18/1999: Increased number of active photons allowed.
                    Improved explosions for more realism.
                    Added progress bar for loading of sound clips.
  1.2,  12/23/1999: Increased frame rate for smoother animation.
                    Modified code to calculate game object speeds and timer
                    counters based on the frame rate so they will remain
                    constant.
                    Improved speed limit checking for ship.
                    Removed wrapping of photons around screen and set a fixed
                    firing rate.
                    Added sprites for ship's thrusters.
  1.3,  01/25/2001: Updated to JDK 1.1.8.

  Usage:

  <applet code="Asteroids.class" width=w height=h></applet>

  Keyboard Controls:

  S            - Start Game    P           - Pause Game
  Cursor Left  - Rotate Left   Cursor Up   - Fire Thrusters
  Cursor Right - Rotate Right  Cursor Down - Fire Retro Thrusters
  Spacebar     - Fire Cannon   H           - Hyperspace
  M            - Toggle Sound  D           - Toggle Graphics Detail

 ******************************************************************************/

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import java.applet.Applet;
import java.applet.AudioClip;

/******************************************************************************
  Main applet code.
 ******************************************************************************/

public class AsteroidGame extends Applet implements Runnable, KeyListener {

	// Copyright information.

	String copyName = "Asteroids";
	String copyVers = "Version 1.3";
	String copyInfo = "Copyright 1998-2001 by Mike Hall";
	String copyLink = "http://www.brainjar.com";
	String copyText = copyName + '\n' + copyVers + '\n'
			+ copyInfo + '\n' + copyLink;

	// Thread control variables.

	Thread loadThread;
	Thread loopThread;

	// Constants

	static final int DELAY = 20;             // Milliseconds between screen and
	static final int FPS   =                 // the resulting frame rate.
			Math.round(1000 / DELAY);

	static final int MAX_SHOTS =  8;          // Maximum number of sprites
	static final int MAX_ROCKS =  8;          // for photons, asteroids and
	static final int MAX_SCRAP = 40;          // explosions.

	static final int SCRAP_COUNT  = 2 * FPS;  // Timer counter starting values
	static final int HYPER_COUNT  = 3 * FPS;  // calculated using number of
	static final int MISSLE_COUNT = 4 * FPS;  // seconds x frames per second.
	static final int STORM_PAUSE  = 2 * FPS;

	static final int    MIN_ROCK_SIDES =   6; // Ranges for asteroid shape, size
	static final int    MAX_ROCK_SIDES =  16; // speed and rotation.
	static final int    MIN_ROCK_SIZE  =  20;
	static final int    MAX_ROCK_SIZE  =  40;
	static final double MIN_ROCK_SPEED =  40.0 / FPS;
	static final double MAX_ROCK_SPEED = 240.0 / FPS;
	static final double MAX_ROCK_SPIN  = Math.PI / FPS;

	static final int MAX_SHIPS = 3;           // Starting number of ships for
	// each game.
	static final int UFO_PASSES = 3;          // Number of passes for flying
	// saucer per appearance.

	// Ship's rotation and acceleration rates and maximum speed.

	static final double SHIP_ANGLE_STEP = Math.PI / FPS;
	static final double SHIP_SPEED_STEP = 15.0 / FPS;
	static final double MAX_SHIP_SPEED  = 1.25 * MAX_ROCK_SPEED;

	static final int FIRE_DELAY = 50;         // Minimum number of milliseconds
	// required between photon shots.

	// Probablility of flying saucer firing a missile during any given frame
	// (other conditions must be met).

	static final double MISSLE_PROBABILITY = 0.45 / FPS;

	static final int BIG_POINTS    =  25;     // Points scored for shooting
	static final int SMALL_POINTS  =  50;     // various objects.
	static final int UFO_POINTS    = 250;
	static final int MISSLE_POINTS = 500;

	// Number of points the must be scored to earn a new ship or to cause the
	// flying saucer to appear.

	static final int NEW_SHIP_POINTS = 5000;
	static final int NEW_UFO_POINTS  = 2750;

	// Background stars.

	int     numStars;
	Point[] stars;

	// Game data.

	static int score;
	int highScore;
	int newShipScore;
	int newUfoScore;

	// Flags for game state and options.

	static boolean loaded = false;
	boolean paused;
	static boolean playing;
	static boolean sound;
	static boolean detail;

	// Key flags.

	static boolean left  = false;
	static boolean right = false;
	static boolean up    = false;
	static boolean down  = false;

	// Sprite objects.

	Ship   ship;
	Thruster   fwdThruster, revThruster;
	UFO   ufo;
	Missile   missile;
	Photon[] photons    = new Photon[MAX_SHOTS];
	Asteroid[] asteroids  = new Asteroid[MAX_ROCKS];
	Explosion[] explosions = new Explosion[MAX_SCRAP];

	// Ship data.

	static int shipsLeft;       // Number of ships left in game, including current one.
	static int shipCounter;     // Timer counter for ship explosion.
	static int hyperCounter;    // Timer counter for hyperspace.

	// Photon data.

	static int   photonIndex;    // Index to next available photon sprite.
	long  photonTime;     // Time value used to keep firing rate constant.

	// Flying saucer data.

	static int ufoPassesLeft;    // Counter for number of flying saucer passes.
	static int ufoCounter;       // Timer counter used to track each flying saucer pass.

	// missile data.

	static int missleCounter;    // Counter for life of missile.

	// Asteroid data.

	boolean[] asteroidIsSmall = new boolean[MAX_ROCKS];    // Asteroid size flag.
	static int       asteroidsCounter;                            // Break-time counter.
	static double    asteroidsSpeed;                              // Asteroid speed.
	static int       asteroidsLeft;                               // Number of active asteroids.

	// Explosion data.

	int[] explosionCounter = new int[MAX_SCRAP];  // Time counters for explosions.
	static int   explosionIndex;                         // Next available explosion sprite.

	// Sound clips.

	static AudioClip crashSound;
	static AudioClip explosionSound;
	AudioClip fireSound;
	static AudioClip missleSound;
	static AudioClip saucerSound;
	static AudioClip thrustersSound;
	AudioClip warpSound;

	// Flags for looping sound clips.

	static boolean thrustersPlaying;
	static boolean saucerPlaying;
	static boolean misslePlaying;

	// Counter and total used to track the loading of the sound clips.

	int clipTotal   = 0;
	int clipsLoaded = 0;

	// Off screen image.

	Dimension offDimension;
	Image     offImage;
	Graphics  offGraphics;

	// Data for the screen font.

	Font font      = new Font("Helvetica", Font.BOLD, 12);
	FontMetrics fm = getFontMetrics(font);
	int fontWidth  = fm.getMaxAdvance();
	int fontHeight = fm.getHeight();

	public String getAppletInfo() {

		// Return copyright information.

		return(copyText);
	}

	public void init() {

		Dimension d = getSize();
		int i;

		// Display copyright information.

		System.out.println(copyText);

		// Set up key event handling and set focus to applet window.

		addKeyListener(this);
		requestFocus();

		// Save the screen size.

		SpaceObject.width = d.width;
		SpaceObject.height = d.height;

		// Generate the starry background.

		numStars = SpaceObject.width * SpaceObject.height / 5000;
		stars = new Point[numStars];
		for (i = 0; i < numStars; i++)
			stars[i] = new Point((int) (Math.random() * SpaceObject.width), (int) (Math.random() * SpaceObject.height));

		// Create shape for the ship sprite.

		ship = new Ship();
		Ship.defineShip(ship);

		// Create shapes for the ship thrusters.

		//forward thruster
		fwdThruster = new Thruster();
		Thruster.defineFwdThruster(fwdThruster);
		//backward thruster
		revThruster = new Thruster();
		Thruster.defineRevThruster(revThruster);

		// Create shape for each photon sprites.

		for (i = 0; i < MAX_SHOTS; i++) {
			photons[i] = new Photon();
			Photon.definePhotons(photons[i]);}

		// Create shape for the flying saucer.

		ufo = new UFO();
		UFO.defineUFO(ufo);

		// Create shape for the guided missile.

		missile = new Missile();
		Missile.defineMissile(missile);

		// Create asteroid sprites.

		for (i = 0; i < MAX_ROCKS; i++)
			asteroids[i] = new Asteroid();


		// Create explosion sprites.

		for (i = 0; i < MAX_SCRAP; i++)
			explosions[i] = new Explosion();

		// Initialize game data and put us in 'game over' mode.

		highScore = 0;
		sound = true;
		detail = true;
		initGame();
		endGame();
	}

	public void initGame() {

		// Initialize game data and sprites.

		score = 0;
		shipsLeft = MAX_SHIPS;
		asteroidsSpeed = MIN_ROCK_SPEED;
		newShipScore = NEW_SHIP_POINTS;
		newUfoScore = NEW_UFO_POINTS;
		Ship.initShip(ship,fwdThruster,revThruster );
		Photon.initPhotons(photons);
		UFO.stopUfo(ufo);
		Missile.stopMissle(missile);
		Asteroid.initAsteroids(asteroids,asteroidIsSmall);
		Explosion.initExplosions(explosions, explosionCounter);
		playing = true;
		paused = false;
		photonTime = System.currentTimeMillis();
	}

	public void endGame() {

		// Stop ship, flying saucer, guided missile and associated sounds.

		playing = false;
		Ship.stopShip(ship);
		UFO.stopUfo(ufo);
		Missile.stopMissle(missile);
	}

	public void start() {

		if (loopThread == null) {
			loopThread = new Thread(this);
			loopThread.start();
		}
		if (!loaded && loadThread == null) {
			loadThread = new Thread(this);
			loadThread.start();
		}
	}

	public void stop() {

		if (loopThread != null) {
			loopThread.stop();
			loopThread = null;
		}
		if (loadThread != null) {
			loadThread.stop();
			loadThread = null;
		}
	}

	public void run() {

		int i, j;
		long startTime;

		// Lower this thread's priority and get the current time.

		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		startTime = System.currentTimeMillis();

		// Run thread for loading sounds.

		if (!loaded && Thread.currentThread() == loadThread) {
			loadSounds();
			loaded = true;
			loadThread.stop();
		}

		// This is the main loop.

		while (Thread.currentThread() == loopThread) {

			if (!paused) {

				// Move and process all sprites.

				Ship.updateShip(ship, left, right, down, up, fwdThruster, revThruster);
				Photon.updatePhotons(photons);
				ufo.updateUfo(ufo, photons, ship, missile);
				Missile.updateMissle(missile, photons, ship, ufo);
				Asteroid.initAsteroids(asteroids, asteroidIsSmall);
				Asteroid.updateAsteroids(asteroids, photons, asteroidIsSmall, ship, missile, ufo);
				Explosion.updateExplosions(explosions, explosionCounter);

				// Check the score and advance high score, add a new ship or start the
				// flying saucer as necessary.

				if (score > highScore)
					highScore = score;
				if (score > newShipScore) {
					newShipScore += NEW_SHIP_POINTS;
					shipsLeft++;
				}
				if (playing && score > newUfoScore && !ufo.active) {
					newUfoScore += NEW_UFO_POINTS;
					ufoPassesLeft = UFO_PASSES;
					UFO.initUfo(ufo);
				}

				// If all asteroids have been destroyed create a new batch.

				if (asteroidsLeft <= 0)
					if (--asteroidsCounter <= 0)
						Asteroid.initAsteroids(asteroids,asteroidIsSmall);
			}

			// Update the screen and set the timer for the next loop.

			repaint();
			try {
				startTime += DELAY;
				Thread.sleep(Math.max(0, startTime - System.currentTimeMillis()));
			}
			catch (InterruptedException e) {
				break;
			}
		}
	}

	public void loadSounds() {

		// Load all sound clips by playing and immediately stopping them. Update
		// counter and total for display.

		try {
			crashSound     = getAudioClip(new URL(getCodeBase(), "crash.au"));
			clipTotal++;
			explosionSound = getAudioClip(new URL(getCodeBase(), "explosion.au"));
			clipTotal++;
			fireSound      = getAudioClip(new URL(getCodeBase(), "fire.au"));
			clipTotal++;
			missleSound    = getAudioClip(new URL(getCodeBase(), "missile.au"));
			clipTotal++;
			saucerSound    = getAudioClip(new URL(getCodeBase(), "saucer.au"));
			clipTotal++;
			thrustersSound = getAudioClip(new URL(getCodeBase(), "thrusters.au"));
			clipTotal++;
			warpSound      = getAudioClip(new URL(getCodeBase(), "warp.au"));
			clipTotal++;
		}
		catch (MalformedURLException e) {}

		try {
			crashSound.play();     crashSound.stop();     clipsLoaded++;
			repaint(); Thread.currentThread().sleep(DELAY);
			explosionSound.play(); explosionSound.stop(); clipsLoaded++;
			repaint(); Thread.currentThread().sleep(DELAY);
			fireSound.play();      fireSound.stop();      clipsLoaded++;
			repaint(); Thread.currentThread().sleep(DELAY);
			missleSound.play();    missleSound.stop();    clipsLoaded++;
			repaint(); Thread.currentThread().sleep(DELAY);
			saucerSound.play();    saucerSound.stop();    clipsLoaded++;
			repaint(); Thread.currentThread().sleep(DELAY);
			thrustersSound.play(); thrustersSound.stop(); clipsLoaded++;
			repaint(); Thread.currentThread().sleep(DELAY);
			warpSound.play();      warpSound.stop();      clipsLoaded++;
			repaint(); Thread.currentThread().sleep(DELAY);
		}
		catch (InterruptedException e) {}
	}

	public void keyPressed(KeyEvent e) {

		char c;

		// Check if any cursor keys have been pressed and set flags.

		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			left = true;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			right = true;
		if (e.getKeyCode() == KeyEvent.VK_UP)
			up = true;
		if (e.getKeyCode() == KeyEvent.VK_DOWN)
			down = true;

		if ((up || down) && ship.active && !thrustersPlaying) {
			if (sound && !paused)
				thrustersSound.loop();
			thrustersPlaying = true;
		}

		// Spacebar: fire a photon and start its counter.

		if (e.getKeyChar() == ' ' && ship.active) {
			if (sound & !paused)
				fireSound.play();
			photonTime = System.currentTimeMillis();
			photonIndex++;
			if (photonIndex >= MAX_SHOTS)
				photonIndex = 0;
			photons[photonIndex].active = true;
			photons[photonIndex].x = ship.x;
			photons[photonIndex].y = ship.y;
			photons[photonIndex].deltaX = 2 * MAX_ROCK_SPEED * -Math.sin(ship.angle);
			photons[photonIndex].deltaY = 2 * MAX_ROCK_SPEED *  Math.cos(ship.angle);
		}

		// Allow upper or lower case characters for remaining keys.

		c = Character.toLowerCase(e.getKeyChar());

		// 'H' key: warp ship into hyperspace by moving to a random location and
		// starting counter.

		if (c == 'h' && ship.active && hyperCounter <= 0) {
			ship.x = Math.random() * SpaceObject.width;
			ship.y = Math.random() * SpaceObject.height;
			hyperCounter = HYPER_COUNT;
			if (sound & !paused)
				warpSound.play();
		}

		// 'P' key: toggle pause mode and start or stop any active looping sound
		// clips.

		if (c == 'p') {
			if (paused) {
				if (sound && misslePlaying)
					missleSound.loop();
				if (sound && saucerPlaying)
					saucerSound.loop();
				if (sound && thrustersPlaying)
					thrustersSound.loop();
			}
			else {
				if (misslePlaying)
					missleSound.stop();
				if (saucerPlaying)
					saucerSound.stop();
				if (thrustersPlaying)
					thrustersSound.stop();
			}
			paused = !paused;
		}

		// 'M' key: toggle sound on or off and stop any looping sound clips.

		if (c == 'm' && loaded) {
			if (sound) {
				crashSound.stop();
				explosionSound.stop();
				fireSound.stop();
				missleSound.stop();
				saucerSound.stop();
				thrustersSound.stop();
				warpSound.stop();
			}
			else {
				if (misslePlaying && !paused)
					missleSound.loop();
				if (saucerPlaying && !paused)
					saucerSound.loop();
				if (thrustersPlaying && !paused)
					thrustersSound.loop();
			}
			sound = !sound;
		}

		// 'D' key: toggle graphics detail on or off.

		if (c == 'd')
			detail = !detail;

		// 'S' key: start the game, if not already in progress.

		if (c == 's' && loaded && !playing)
			initGame();

		// 'HOME' key: jump to web site (undocumented).

		if (e.getKeyCode() == KeyEvent.VK_HOME)
			try {
				getAppletContext().showDocument(new URL(copyLink));
			}
		catch (Exception excp) {}
	}

	public void keyReleased(KeyEvent e) {

		// Check if any cursor keys where released and set flags.

		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			left = false;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			right = false;
		if (e.getKeyCode() == KeyEvent.VK_UP)
			up = false;
		if (e.getKeyCode() == KeyEvent.VK_DOWN)
			down = false;

		if (!up && !down && thrustersPlaying) {
			thrustersSound.stop();
			thrustersPlaying = false;
		}
	}

	public void keyTyped(KeyEvent e) {}

	public void update(Graphics g) {

		paint(g);
	}

	public void paint(Graphics g) {

		Dimension d = getSize();
		int i;
		int c;
		String s;
		int w, h;
		int x, y;

		// Create the off screen graphics context, if no good one exists.

		if (offGraphics == null || d.width != offDimension.width || d.height != offDimension.height) {
			offDimension = d;
			offImage = createImage(d.width, d.height);
			offGraphics = offImage.getGraphics();
		}

		// Fill in background and stars.

		offGraphics.setColor(Color.black);
		offGraphics.fillRect(0, 0, d.width, d.height);
		if (detail) {
			offGraphics.setColor(Color.white);
			for (i = 0; i < numStars; i++)
				offGraphics.drawLine(stars[i].x, stars[i].y, stars[i].x, stars[i].y);
		}

		// Draw photon bullets.

		offGraphics.setColor(Color.white);
		for (i = 0; i < MAX_SHOTS; i++)
			if (photons[i].active)
				offGraphics.drawPolygon(photons[i].sprite);

		// Draw the guided missile, counter is used to quickly fade color to black
		// when near expiration.

		c = Math.min(missleCounter * 24, 255);
		offGraphics.setColor(new Color(c, c, c));
		if (missile.active) {
			offGraphics.drawPolygon(missile.sprite);
			offGraphics.drawLine(missile.sprite.xpoints[missile.sprite.npoints - 1], missile.sprite.ypoints[missile.sprite.npoints - 1],
					missile.sprite.xpoints[0], missile.sprite.ypoints[0]);
		}

		// Draw the asteroids.

		for (i = 0; i < MAX_ROCKS; i++)
			if (asteroids[i].active) {
				if (detail) {
					offGraphics.setColor(Color.black);
					offGraphics.fillPolygon(asteroids[i].sprite);
				}
				offGraphics.setColor(Color.white);
				offGraphics.drawPolygon(asteroids[i].sprite);
				offGraphics.drawLine(asteroids[i].sprite.xpoints[asteroids[i].sprite.npoints - 1], asteroids[i].sprite.ypoints[asteroids[i].sprite.npoints - 1],
						asteroids[i].sprite.xpoints[0], asteroids[i].sprite.ypoints[0]);
			}

		// Draw the flying saucer.

		if (ufo.active) {
			if (detail) {
				offGraphics.setColor(Color.black);
				offGraphics.fillPolygon(ufo.sprite);
			}
			offGraphics.setColor(Color.white);
			offGraphics.drawPolygon(ufo.sprite);
			offGraphics.drawLine(ufo.sprite.xpoints[ufo.sprite.npoints - 1], ufo.sprite.ypoints[ufo.sprite.npoints - 1],
					ufo.sprite.xpoints[0], ufo.sprite.ypoints[0]);
		}

		// Draw the ship, counter is used to fade color to white on hyperspace.

		c = 255 - (255 / HYPER_COUNT) * hyperCounter;
		if (ship.active) {
			if (detail && hyperCounter == 0) {
				offGraphics.setColor(Color.black);
				offGraphics.fillPolygon(ship.sprite);
			}
			offGraphics.setColor(new Color(c, c, c));
			offGraphics.drawPolygon(ship.sprite);
			offGraphics.drawLine(ship.sprite.xpoints[ship.sprite.npoints - 1], ship.sprite.ypoints[ship.sprite.npoints - 1],
					ship.sprite.xpoints[0], ship.sprite.ypoints[0]);

			// Draw thruster exhaust if thrusters are on. Do it randomly to get a
			// flicker effect.

			if (!paused && detail && Math.random() < 0.5) {
				if (up) {
					offGraphics.drawPolygon(fwdThruster.sprite);
					offGraphics.drawLine(fwdThruster.sprite.xpoints[fwdThruster.sprite.npoints - 1], fwdThruster.sprite.ypoints[fwdThruster.sprite.npoints - 1],
							fwdThruster.sprite.xpoints[0], fwdThruster.sprite.ypoints[0]);
				}
				if (down) {
					offGraphics.drawPolygon(revThruster.sprite);
					offGraphics.drawLine(revThruster.sprite.xpoints[revThruster.sprite.npoints - 1], revThruster.sprite.ypoints[revThruster.sprite.npoints - 1],
							revThruster.sprite.xpoints[0], revThruster.sprite.ypoints[0]);
				}
			}
		}

		// Draw any explosion debris, counters are used to fade color to black.

		for (i = 0; i < MAX_SCRAP; i++)
			if (explosions[i].active) {
				c = (255 / SCRAP_COUNT) * explosionCounter [i];
				offGraphics.setColor(new Color(c, c, c));
				offGraphics.drawPolygon(explosions[i].sprite);
			}

		// Display status and messages.

		offGraphics.setFont(font);
		offGraphics.setColor(Color.white);

		offGraphics.drawString("Score: " + score, fontWidth, fontHeight);
		offGraphics.drawString("Ships: " + shipsLeft, fontWidth, d.height - fontHeight);
		s = "High: " + highScore;
		offGraphics.drawString(s, d.width - (fontWidth + fm.stringWidth(s)), fontHeight);
		if (!sound) {
			s = "Mute";
			offGraphics.drawString(s, d.width - (fontWidth + fm.stringWidth(s)), d.height - fontHeight);
		}

		if (!playing) {
			s = copyName;
			offGraphics.drawString(s, (d.width - fm.stringWidth(s)) / 2, d.height / 2 - 2 * fontHeight);
			s = copyVers;
			offGraphics.drawString(s, (d.width - fm.stringWidth(s)) / 2, d.height / 2 - fontHeight);
			s = copyInfo;
			offGraphics.drawString(s, (d.width - fm.stringWidth(s)) / 2, d.height / 2 + fontHeight);
			s = copyLink;
			offGraphics.drawString(s, (d.width - fm.stringWidth(s)) / 2, d.height / 2 + 2 * fontHeight);
			if (!loaded) {
				s = "Loading sounds...";
				w = 4 * fontWidth + fm.stringWidth(s);
				h = fontHeight;
				x = (d.width - w) / 2;
				y = 3 * d.height / 4 - fm.getMaxAscent();
				offGraphics.setColor(Color.black);
				offGraphics.fillRect(x, y, w, h);
				offGraphics.setColor(Color.gray);
				if (clipTotal > 0)
					offGraphics.fillRect(x, y, (int) (w * clipsLoaded / clipTotal), h);
				offGraphics.setColor(Color.white);
				offGraphics.drawRect(x, y, w, h);
				offGraphics.drawString(s, x + 2 * fontWidth, y + fm.getMaxAscent());
			}
			else {
				s = "Game Over";
				offGraphics.drawString(s, (d.width - fm.stringWidth(s)) / 2, d.height / 4);
				s = "'S' to Start";
				offGraphics.drawString(s, (d.width - fm.stringWidth(s)) / 2, d.height / 4 + fontHeight);
			}
		}
		else if (paused) {
			s = "Game Paused";
			offGraphics.drawString(s, (d.width - fm.stringWidth(s)) / 2, d.height / 4);
		}

		// Copy the off screen buffer to the screen.

		g.drawImage(offImage, 0, 0, this);
	}
}
