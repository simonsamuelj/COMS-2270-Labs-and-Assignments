package hw3;

/**
 * An element with two distinctive behaviors. First, it can be set up to move
 * vertically within a fixed set of boundaries. On reaching a boundary, the
 * y-component of its velocity is reversed so the lift "bounces" up and down.
 * Second, it maintains a list of <em>associated</em> elements (such as
 * {@link AttachedElement} or {@link FollowerElement}) whose motion occurs
 * relative to this lift. When the lift's {@link #update()} is called, all
 * associated elements are also updated.
 *
 * @author Simon S.
 */
public class LiftElement extends ContainerElement {

    /**
     * Constructs a new LiftElement at the given position with the given
     * dimensions. The velocity defaults to zero. The initial vertical
     * boundaries are set to [x, x + width].
     *
     * @param x      x-coordinate of initial position of upper left corner
     * @param y      y-coordinate of initial position of upper left corner
     * @param width  element's width
     * @param height element's height
     */
    public LiftElement(double x, double y, int width, int height) {
        super(x, y, width, height);
    }

    /**
     * {@inheritDoc}
     * Increments the frame count, moves the position by the current velocity,
     * checks whether the lift has reached a vertical boundary (and reverses
     * the y-velocity if so), and then calls {@code update()} on all
     * associated elements.
     */
    @Override
    public void update() {
        incrementFrameCount();
        setPosition(getXReal() + getDeltaX(), getYReal() + getDeltaY());

        if (getYReal() + getHeight() >= getMax()) {
            setPosition(getXReal(), getMax() - getHeight());
            setVelocity(getDeltaX(), -getDeltaY());
        } else if (getYReal() <= getMin()) {
            setPosition(getXReal(), getMin());
            setVelocity(getDeltaX(), -getDeltaY());
        }

        updateAssociated();
    }
}
