package library;

/**
 * A Movie is a library item that can be checked out for 7 days and cannot be renewed.
 * If overdue, the fine is $3.00 plus $0.50 per day,
 * up to a maximum equal to the item's replacement cost.
 */
public class Movie extends CheckoutItem
{
  /**
   * Duration of this Movie, in minutes.
   */
  private int duration;

  /**
   * Constructs a Movie with the given title, replacement cost, and duration.
   *
   * @param givenTitle    title for this item
   * @param givenCost     replacement cost for this item, in dollars
   * @param givenDuration duration of this item, in minutes
   */
  public Movie(String givenTitle, double givenCost, int givenDuration)
  {
    super(givenTitle, givenCost);
    duration = givenDuration;
  }

  /**
   * Returns the duration of this Movie.
   */
  public int getDuration()
  {
    return duration;
  }

  /** Movies check out for 7 days. */
  @Override
  protected int getCheckoutDays()
  {
    return 7;
  }

  /**
   * Raw fine formula: $3.00 base + $0.50 per day late.
   * CheckoutItem.getFine caps this at the replacement cost.
   */
  @Override
  protected double computeRawFine(int daysLate)
  {
    return 3.0 + daysLate * 0.50;
  }
}
