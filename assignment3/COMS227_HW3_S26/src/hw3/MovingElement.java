package hw3;

/**
 * An element whose {@code update} method updates the position each frame
 * according to a <em>velocity</em> vector (deltaX, deltaY). The units are
 * assumed to be "pixels per frame". The element also supports keeping track of
 * bounds that can be used to limit the range of motion; however, MovingElement
 * does not enforce the boundaries itself.
 *
 * @author Simon S.
 */
public class MovingElement extends VelocityElement {

    /** Lower movement boundary. */
    private double lower;

    /** Upper movement boundary. */
    private double upper;

    /**
     * Constructs a MovingElement at the given position with the given dimensions.
     * The velocity defaults to zero in both directions. The initial bounds are set
     * to [x, x + width].
     *
     * @param x      x-coordinate of upper left corner
     * @param y      y-coordinate of upper left corner
     * @param width  object's width
     * @param height object's height
     */
    public MovingElement(double x, double y, int width, int height) {
        super(x, y, width, height);
        lower = x;
        upper = x + width;
    }

    /**
     * {@inheritDoc}
     * Increments the frame count and then moves the position by the current
     * velocity vector (deltaX, deltaY).
     */
    @Override
    public void update() {
        incrementFrameCount();
        setPosition(getXReal() + getDeltaX(), getYReal() + getDeltaY());
    }

    /**
     * Sets the movement boundaries for this element.
     *
     * @param lower the lower (left or top) boundary
     * @param upper the upper (right or bottom) boundary
     */
    public void setBounds(double lower, double upper) {
        this.lower = lower;
        this.upper = upper;
    }

    /**
     * Returns the lower (minimum) boundary value.
     *
     * @return the lower boundary
     */
    public double getMin() {
        return lower;
    }

    /**
     * Returns the upper (maximum) boundary value.
     *
     * @return the upper boundary
     */
    public double getMax() {
        return upper;
    }
}
