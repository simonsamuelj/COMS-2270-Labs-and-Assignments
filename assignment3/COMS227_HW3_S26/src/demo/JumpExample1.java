package demo;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import api.Drawable;
import hw3.FlyingElement;
import hw3.SimpleElement;

/**
 * Example using a "jumping" with a FlyingElement.
 * 
 * To control the FlyingElement use left arrow, right arrow, and "a" to jump.
 * 
 * For this demo to work you need to have a working SimpleElement and a
 * FlyingElement.
 */
public class JumpExample1 extends GameBase {
	public static void main(String[] args) {
		start(JumpExample1.class);
	}

	// suppress compiler warning
	private static final long serialVersionUID = 1L;

	// Window size, fixed for simplicity
	private static final int WIDTH = 600;
	private static final int HEIGHT = 400;

	// Dimensions for objects
	private static final int SIZE = 30;

	// Player motion constants
	private static final double PLAYER_JUMP_VELOCITY = -12;
	private static final double GRAVITY = 0.8;
	private static final double PLAYER_SPEED = 2;

	/**
	 * The player.
	 */
	private FlyingElement player;

	/**
	 * A platform for player to land on.
	 */
	private SimpleElement platform;

	public JumpExample1() {
		platform = new SimpleElement(100, 300, 400, 20);

		player = new FlyingElement(100, 300 - SIZE, SIZE, SIZE + SIZE / 2);
		player.setColor(Color.YELLOW);
	}

	@Override
	public int getGameWidth() {
		return WIDTH;
	}

	@Override
	public int getGameHeight() {
		return HEIGHT;
	}

	@Override
	protected ArrayList<Drawable> getAllDrawables() {
		ArrayList<Drawable> arr = new ArrayList<>();
		arr.add(player);
		arr.add(platform);
		return arr;
	}

	@Override
	protected void doUpdates() {
		player.update();
		platform.update();

		System.out.println(player.getDeltaY());

		if (player.collides(platform)) {
			// we landed! no longer flying...
			player.setGrounded(true);

			// align bottom of player with top of platform
			player.setPosition(player.getXReal(), platform.getYReal() - player.getHeight());

			// restore horizontal velocity, set vertical velocity to zero
			player.setVelocity(player.getDeltaX(), 0);
		}
	}

	@Override
	protected void doKeyPressed(int ch) {
		if (ch == KeyEvent.VK_LEFT) {
			player.setVelocity(-PLAYER_SPEED, player.getDeltaY());
		} else if (ch == KeyEvent.VK_RIGHT) {
			player.setVelocity(PLAYER_SPEED, player.getDeltaY());
		} else if (ch == KeyEvent.VK_A) {
			// jump
			player.setVelocity(player.getDeltaX(), PLAYER_JUMP_VELOCITY);
			player.setGravity(GRAVITY);
			player.setGrounded(false);
		}
	}

	@Override
	protected void doKeyReleased(int ch) {
		if (ch == KeyEvent.VK_LEFT) {
			player.setVelocity(0, player.getDeltaY());
		} else if (ch == KeyEvent.VK_RIGHT) {
			player.setVelocity(0, player.getDeltaY());
		}
	}
}
