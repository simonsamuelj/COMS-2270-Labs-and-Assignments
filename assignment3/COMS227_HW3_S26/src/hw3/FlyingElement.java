package hw3;

/**
 * A moving element in which the vertical velocity is adjusted each frame by a
 * gravitational constant to simulate gravity. The element can be set to
 * "grounded", meaning gravity will no longer influence its vertical velocity.
 * <p>
 * By default, a new FlyingElement is grounded with zero gravity. To enable
 * gravity, call {@link #setGravity(double)} with a positive value and
 * {@link #setGrounded(boolean)} with {@code false}.
 *
 * @author Simon S.
 */
public class FlyingElement extends VelocityElement {

    /** Whether this element is currently grounded (gravity disabled). */
    private boolean grounded;

    /** Gravitational acceleration in pixels per frame squared. */
    private double gravity;

    /**
     * Constructs a new FlyingElement at the given position with the given
     * dimensions. By default the element is grounded (gravity has no effect)
     * and gravity is zero.
     *
     * @param x      x-coordinate of upper left corner
     * @param y      y-coordinate of upper left corner
     * @param width  element's width
     * @param height element's height
     */
    public FlyingElement(double x, double y, int width, int height) {
        super(x, y, width, height);
        grounded = true;
        gravity = 0;
    }

    /**
     * {@inheritDoc}
     * Increments the frame count, moves the position by the current velocity
     * vector, and then — if the element is not grounded — adds the gravity
     * constant to the vertical velocity.
     */
    @Override
    public void update() {
        incrementFrameCount();
        setPosition(getXReal() + getDeltaX(), getYReal() + getDeltaY());
        if (!grounded) {
            setVelocity(getDeltaX(), getDeltaY() + gravity);
        }
    }

    /**
     * Sets the gravitational constant applied to the vertical velocity
     * each frame when this element is not grounded.
     *
     * @param gravity gravitational acceleration in pixels per frame squared
     */
    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    /**
     * Sets whether this element is grounded. When grounded, the gravity
     * constant is not applied during {@link #update()}.
     *
     * @param grounded {@code true} to disable gravity, {@code false} to enable it
     */
    public void setGrounded(boolean grounded) {
        this.grounded = grounded;
    }

    /**
     * Returns whether this element is currently grounded.
     *
     * @return {@code true} if grounded (gravity disabled), {@code false} otherwise
     */
    public boolean isGrounded() {
        return grounded;
    }
}
