package hw3;

import api.AbstractElement;

/**
 * A follower element is one that is associated with another "base" element
 * such as a {@link PlatformElement} or {@link LiftElement}. The follower's
 * position tracks the base element's position: when the base moves, the
 * follower moves with it. However, unlike an {@link AttachedElement}, the
 * follower is not always at a fixed location relative to the base. When the
 * follower's horizontal velocity is set to a non-zero value, the follower
 * will oscillate back and forth between the left and right edges of the
 * base element it is associated with.
 * <p>
 * The boundaries for oscillation are automatically updated each frame to
 * match the base element's current left and right edges.
 *
 * @author Simon S.
 */
public class FollowerElement extends VelocityElement {

    /** Left boundary (base's left edge) for oscillation. */
    private double min;

    /** Right boundary (base's right edge) for oscillation. */
    private double max;

    /** Horizontal offset used when the base is first set. */
    private int initialOffset;

    /** The base element this follower tracks. */
    private AbstractElement base;

    /** Current horizontal offset from the base's x-coordinate. */
    private double offset;

    /**
     * Constructs a new FollowerElement with the given dimensions and initial
     * horizontal offset. Before being added to a base element, the x and y
     * coordinates are zero.
     *
     * @param width         element's width in pixels
     * @param height        element's height in pixels
     * @param initialOffset horizontal offset added to the base's x-coordinate
     *                      when the base is first set
     */
    public FollowerElement(int width, int height, int initialOffset) {
        super(0, 0, width, height);
        min = 0;
        max = width;
        this.initialOffset = initialOffset;
        offset = 0;
    }

    /**
     * Sets the oscillation boundaries for this follower.
     *
     * @param lower the left boundary (base's left edge)
     * @param upper the right boundary (base's right edge)
     */
    public void setBounds(double lower, double upper) {
        this.min = lower;
        this.max = upper;
    }

    /**
     * Returns the current left boundary for oscillation.
     *
     * @return the minimum x boundary
     */
    public double getMin() {
        return min;
    }

    /**
     * Returns the current right boundary for oscillation.
     *
     * @return the maximum x boundary
     */
    public double getMax() {
        return max;
    }

    /**
     * {@inheritDoc}
     * Increments the frame count, recalculates the oscillation boundaries
     * based on the base element's current position, moves the follower
     * relative to the base by applying the horizontal velocity to its
     * offset, and reverses the horizontal velocity if a boundary is reached.
     */
    @Override
    public void update() {
        incrementFrameCount();
        setBounds(base.getXReal(), base.getXReal() + base.getWidth());
        setPosition(base.getXReal() + offset + getDeltaX(), base.getYReal() - getHeight());

        if (getXReal() + getWidth() >= getMax()) {
            setPosition(max - getWidth(), getYReal());
            setVelocity(-getDeltaX(), getDeltaY());
        } else if (getXReal() <= min) {
            setPosition(min, getYReal());
            setVelocity(-getDeltaX(), getDeltaY());
        }

        offset = getXReal() - base.getXReal();
    }

    /**
     * Sets the base element that this follower is associated with and
     * initializes the follower's position and boundaries.
     *
     * @param base the element this follower should track
     */
    public void setBase(AbstractElement base) {
        this.base = base;
        setBounds(base.getXReal(), base.getXReal() + base.getWidth());
        offset = initialOffset;
        setPosition(base.getXReal() + offset, base.getYReal() - getHeight());
    }
}
