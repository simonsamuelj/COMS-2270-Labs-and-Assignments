package demo;

import java.awt.Color;
import java.util.ArrayList;

import api.CircleRenderer;
import api.Drawable;
import hw3.AttachedElement;
import hw3.FollowerElement;
import hw3.LiftElement;
import hw3.PlatformElement;

/**
 * No controls, just a cool animation.
 * 
 * Requires hw4.AttachedElement, LiftElement, FollowerElement, and
 * PlatformElement.
 */
public class PlatformExample extends GameBase {
	public static void main(String[] args) {
		start(PlatformExample.class);
	}

	// suppress compiler warning
	private static final long serialVersionUID = 1L;

	// Window size, fixed for simplicity
	private static final int WIDTH = 600;
	private static final int HEIGHT = 400;

	// Dimensions for objects
	private static final int SIZE = 30;

	/**
	 * Some platforms.
	 */
	private ArrayList<PlatformElement> platforms = new ArrayList<>();
	private ArrayList<LiftElement> elevators = new ArrayList<>();

	public PlatformExample() {
		PlatformElement p0 = new PlatformElement(100, 200, SIZE * 4, SIZE * 2);
		platforms.add(p0);

		// add an enemy on p0
		FollowerElement e = new FollowerElement(SIZE, SIZE, 0);
		e.setColor(Color.RED);
		e.setVelocity(2, 0);
		p0.addAssociated(e);

		LiftElement p1 = new LiftElement(300, 200, SIZE * 3, SIZE / 2);
		elevators.add(p1);
		p1.setBounds(100, 200); // vertical motion bounds
		p1.setVelocity(0, 3); // initial velocity

		// charm on the elevator, at right
		AttachedElement c = new AttachedElement(SIZE, SIZE, p1.getWidth() - SIZE, 25);
		c.setRenderer(new CircleRenderer(Color.PINK));
		p1.addAssociated(c);

		e = new FollowerElement(SIZE, SIZE, 0);
		e.setColor(Color.RED);
		e.setVelocity(1, 0);
		p1.addAssociated(e);

		PlatformElement p2 = new PlatformElement(200, 300, SIZE * 5, SIZE);
		platforms.add(p2);
		p2.setBounds(200, 500);
		p2.setVelocity(2, 0); // moving platform

		// a moving enemy, starting at left
		e = new FollowerElement(SIZE, SIZE, 0);
		e.setColor(Color.RED);
		e.setVelocity(2, 0);
		p2.addAssociated(e);

		// a nonmoving enemy on p2, fixed in the center
		e = new FollowerElement(SIZE, SIZE, (p2.getWidth() - SIZE) / 2);
		e.setColor(Color.YELLOW);
		p2.addAssociated(e);

		// a charm on p2, also at the center, slightly above the enemy
		c = new AttachedElement(SIZE, SIZE, (p2.getWidth() - SIZE) / 2, SIZE + 5);
		// c.setColor(Color.PINK);
		c.setRenderer(new CircleRenderer(Color.PINK));
		p2.addAssociated(c);
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

		for (PlatformElement p : platforms) {
			arr.add(p);
			for (Drawable c : p.getAssociated()) {
				arr.add(c);
			}
		}

		for (LiftElement e : elevators) {
			arr.add(e);
			for (Drawable c : e.getAssociated()) {
				arr.add(c);
			}
		}

		return arr;
	}

	@Override
	protected void doUpdates() {
		for (PlatformElement p : platforms) {
			p.update();
		}

		for (LiftElement e : elevators) {
			e.update();
		}
	}

}
