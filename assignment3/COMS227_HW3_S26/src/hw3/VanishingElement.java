package hw3;

/**
 * An element that does not move. Instead, it is intended to appear on the
 * screen for a fixed number of frames, after which it automatically marks
 * itself for deletion. This is useful for temporary visual effects such as
 * explosions, score popups, or other short-lived on-screen elements.
 *
 * @author Simon S.
 */
public class VanishingElement extends CoreElement {

    /** The frame count at which this element marks itself for deletion. */
    private int finalFrame;

    /**
     * Constructs a new VanishingElement at the given position with the given
     * dimensions and lifetime. The element will automatically mark itself for
     * deletion after {@code initialLife} calls to {@link #update()}.
     *
     * @param x           x-coordinate of upper left corner
     * @param y           y-coordinate of upper left corner
     * @param width       element's width
     * @param height      element's height
     * @param initialLife the number of frames until this element marks itself for
     *                    deletion
     */
    public VanishingElement(double x, double y, int width, int height, int initialLife) {
        super(x, y, width, height);
        finalFrame = initialLife;
    }

    /**
     * {@inheritDoc}
     * Increments the frame count. If the frame count reaches the lifetime
     * specified at construction, the element is automatically marked for
     * deletion.
     */
    @Override
    public void update() {
        incrementFrameCount();
        if (getFrameCount() == finalFrame) {
            markForDeletion();
        }
    }
}
