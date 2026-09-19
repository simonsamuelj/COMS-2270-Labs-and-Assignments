package hw3;

/**
 * Minimal concrete extension of {@link CoreElement}. The {@code update} method
 * in this implementation just increments the frame count. This element has no
 * built-in movement or other behaviors; it simply exists at a fixed position
 * and can be drawn, repositioned, and checked for collisions.
 *
 * @author Simon S.
 */
public class SimpleElement extends CoreElement {

    /**
     * Constructs a new SimpleElement at the given position with the given
     * dimensions. The frame count is initially zero and the element is not
     * marked for deletion.
     *
     * @param x      x-coordinate of upper left corner
     * @param y      y-coordinate of upper left corner
     * @param width  element's width in pixels
     * @param height element's height in pixels
     */
    public SimpleElement(double x, double y, int width, int height) {
        super(x, y, width, height);
    }

    /**
     * {@inheritDoc}
     * In this implementation, the only action is to increment the frame count.
     */
    @Override
    public void update() {
        incrementFrameCount();
    }
}
