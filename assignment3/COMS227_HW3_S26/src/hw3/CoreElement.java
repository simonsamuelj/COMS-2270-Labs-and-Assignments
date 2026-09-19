package hw3;

import java.awt.Rectangle;

import api.AbstractElement;

/**
 * Abstract base class for all game elements in hw3. Encapsulates the
 * state and behavior that is common to every element type: position
 * (x, y), dimensions (width, height), a frame counter, and a
 * deletion-marker flag. Implements all of the geometric and bookkeeping
 * methods required by {@link AbstractElement} so that concrete subclasses
 * do not need to repeat them.
 *
 * @author Simon S.
 */
public abstract class CoreElement extends AbstractElement {

    /** Exact x-coordinate of the upper-left corner. */
    private double x;

    /** Exact y-coordinate of the upper-left corner. */
    private double y;

    /** Width of the bounding rectangle in pixels. */
    private int width;

    /** Height of the bounding rectangle in pixels. */
    private int height;

    /** Number of times {@link #update()} has been called. */
    private int frameCount;

    /** Whether this element has been marked for deletion. */
    private boolean marked;

    /**
     * Constructs a CoreElement at the given position with the given dimensions.
     * The frame count starts at zero and the element is not marked for deletion.
     *
     * @param x      x-coordinate of the upper-left corner
     * @param y      y-coordinate of the upper-left corner
     * @param width  element width in pixels
     * @param height element height in pixels
     */
    protected CoreElement(double x, double y, int width, int height) {
        super();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        frameCount = 0;
        marked = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getXInt() {
        return (int) Math.round(x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getYInt() {
        return (int) Math.round(y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Rectangle getRect() {
        return new Rectangle(getXInt(), getYInt(), width, height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPosition(double newX, double newY) {
        x = newX;
        y = newY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getXReal() {
        return x;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getYReal() {
        return y;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFrameCount() {
        return frameCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMarked() {
        return marked;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void markForDeletion() {
        marked = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean collides(AbstractElement other) {
        return getRect().intersects(other.getRect());
    }

    /**
     * Increments the frame counter by one. Subclasses should call
     * {@code super.update()} or call this method directly at the start
     * of their own {@code update()} implementations to keep the counter
     * accurate.
     */
    protected void incrementFrameCount() {
        frameCount++;
    }
}
