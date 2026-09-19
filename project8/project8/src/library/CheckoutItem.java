package library;

import java.util.Date;

/**
 * Abstract class for library items that can actually be checked out.
 * Adds a replacement cost used to cap overdue fines.
 * Book, Movie, and Documentary all extend this class.
 */
public abstract class CheckoutItem extends AbstractItem
{
  private double replacementCost;

  /**
   * Constructs a CheckoutItem with the given title and replacement cost.
   */
  protected CheckoutItem(String givenTitle, double givenCost)
  {
    super(givenTitle);
    replacementCost = givenCost;
  }

  /**
   * Returns the replacement cost for this item, used to cap fines.
   */
  protected double getReplacementCost()
  {
    return replacementCost;
  }

  /**
   * Returns the raw (uncapped) fine for the given number of days late.
   * Subclasses implement their own fine formula here.
   */
  protected abstract double computeRawFine(int daysLate);

  /**
   * Returns the fine owed if this item is overdue, capped at the
   * replacement cost. Returns 0 if not overdue.
   */
  @Override
  public double getFine(Date now)
  {
    if (isCheckedOut() && isOverdue(now))
    {
      return Math.min(computeRawFine(daysLate(now)), replacementCost);
    }
    return 0;
  }
}
