package library;

/**
 * A Documentary is a library item that can be checked out for 14 days and cannot be renewed.
 * If overdue, the fine is $1.00 per day for the first 5 days and $0.50 per day thereafter,
 * up to a maximum equal to the item's replacement cost.
 */
public class Documentary extends CheckoutItem
{
  /**
   * Duration of this Documentary, in minutes.
   */
  private int duration;

  /**
   * Constructs a Documentary with the given title, replacement cost, and duration.
   *
   * @param givenTitle    title for this item
   * @param givenCost     replacement cost for this item, in dollars
   * @param givenDuration duration of this item, in minutes
   */
  public Documentary(String givenTitle, double givenCost, int givenDuration)
  {
    super(givenTitle, givenCost);
    duration = givenDuration;
  }

  /**
   * Returns the duration of this Documentary.
   */
  public int getDuration()
  {
    return duration;
  }

  /** Documentaries check out for 14 days. */
  @Override
  protected int getCheckoutDays()
  {
    return 14;
  }

  /**
   * Raw fine formula: $1.00/day for the first 5 days, $0.50/day after that.
   * CheckoutItem.getFine caps this at the replacement cost.
   */
  @Override
  protected double computeRawFine(int daysLate)
  {
    if (daysLate <= 5)
    {
      return daysLate * 1.0;
    }
    return 5.0 + (daysLate - 5) * 0.50;
  }
}
