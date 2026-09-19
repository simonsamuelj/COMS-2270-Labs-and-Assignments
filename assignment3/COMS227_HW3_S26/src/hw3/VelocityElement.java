package hw3;

/**
 * Abstract element that adds a velocity vector (deltaX, deltaY) to the
 * basic state provided by {@link CoreElement}. The velocity represents
 * the number of pixels the element moves per frame. Concrete subclasses
 * are responsible for deciding how and when to apply the velocity in their
 * {@link #update()} implementations.
 *
 * @author Simon S.
 */
public abstract class VelocityElement extends CoreElement {

    /** Horizontal velocity in pixels per frame. */
    private double deltaX;

    /** Vertical velocity in pixels per frame. */
    private double deltaY;

    /**
     * Constructs a VelocityElement at the given position with the given
     * dimensions. The initial velocity is zero in both directions.
     *
     * @param x      x-coordinate of the upper-left corner
     * @param y      y-coordinate of the upper-left corner
     * @param width  element width in pixels
     * @param height element height in pixels
     */
    protected VelocityElement(double x, double y, int width, int height) {
        super(x, y, width, height);
        deltaX = 0;
        deltaY = 0;
    }

    /**
     * Sets the velocity vector for this element.
     *
     * @param deltaX horizontal velocity in pixels per frame
     * @param deltaY vertical velocity in pixels per frame
     */
    public void setVelocity(double deltaX, double deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    /**
     * Returns the current horizontal velocity.
     *
     * @return horizontal velocity in pixels per frame
     */
    public double getDeltaX() {
        return deltaX;
    }

    /**
     * Returns the current vertical velocity.
     *
     * @return vertical velocity in pixels per frame
     */
    public double getDeltaY() {
        return deltaY;
    }
}
