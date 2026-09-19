package hw3;

import java.util.ArrayList;

import api.AbstractElement;

/**
 * Abstract element that extends {@link VelocityElement} with two additional
 * responsibilities shared by {@link PlatformElement} and {@link LiftElement}:
 * <ol>
 *   <li>Movement boundaries ({@code min} and {@code max}) that cause the
 *       element to "bounce" when a boundary is reached.</li>
 *   <li>A list of <em>associated</em> elements ({@link AttachedElement} and
 *       {@link FollowerElement}) whose {@code update()} is called each frame
 *       after the container moves.</li>
 * </ol>
 *
 * @author Simon S.
 */
public abstract class ContainerElement extends VelocityElement {

    /** Lower movement boundary. */
    private double min;

    /** Upper movement boundary. */
    private double max;

    /** Elements that move relative to this container. */
    private ArrayList<AbstractElement> associated;

    /**
     * Constructs a ContainerElement at the given position with the given
     * dimensions. The initial boundaries are set to {@code [x, x + width]}.
     *
     * @param x      x-coordinate of the upper-left corner
     * @param y      y-coordinate of the upper-left corner
     * @param width  element width in pixels
     * @param height element height in pixels
     */
    protected ContainerElement(double x, double y, int width, int height) {
        super(x, y, width, height);
        min = x;
        max = x + width;
        associated = new ArrayList<AbstractElement>();
    }

    /**
     * Sets the movement boundaries for this element.
     *
     * @param lower the lower boundary value
     * @param upper the upper boundary value
     */
    public void setBounds(double lower, double upper) {
        min = lower;
        max = upper;
    }

    /**
     * Returns the lower boundary value.
     *
     * @return the minimum boundary
     */
    public double getMin() {
        return min;
    }

    /**
     * Returns the upper boundary value.
     *
     * @return the maximum boundary
     */
    public double getMax() {
        return max;
    }

    /**
     * Adds an {@link AttachedElement} to the associated list and sets this
     * element as its base.
     *
     * @param e the AttachedElement to associate
     */
    public void addAssociated(AttachedElement e) {
        associated.add(e);
        e.setBase(this);
    }

    /**
     * Adds a {@link FollowerElement} to the associated list and sets this
     * element as its base.
     *
     * @param e the FollowerElement to associate
     */
    public void addAssociated(FollowerElement e) {
        associated.add(e);
        e.setBase(this);
    }

    /**
     * Returns the list of elements currently associated with this container.
     *
     * @return the list of associated elements
     */
    public ArrayList<AbstractElement> getAssociated() {
        return associated;
    }

    /**
     * Removes all associated elements that have been marked for deletion.
     */
    public void deleteMarkedAssociated() {
        for (int i = 0; i < associated.size(); i++) {
            if (associated.get(i).isMarked()) {
                associated.remove(i);
                i--; // adjust index after removal
            }
        }
    }

    /**
     * Calls {@link AbstractElement#update()} on every element in the
     * associated list. Subclasses should invoke this after updating the
     * container's own position.
     */
    protected void updateAssociated() {
        for (AbstractElement a : associated) {
            a.update();
        }
    }
}
