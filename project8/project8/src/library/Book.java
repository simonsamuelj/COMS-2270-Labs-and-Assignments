package library;

import java.util.Date;

/**
 * A Book is a library item that can be checked out for 21 days and renewed at most twice.
 * If overdue, the fine is .25 per day.
 */
public class Book extends AbstractItem
{
  /**
   * Number of times the item has been renewed for the current patron.
   */
  private int renewalCount;

  /**
   * Constructs a Book with the given title.
   */
  public Book(String givenTitle)
  {
    super(givenTitle);
    renewalCount = 0;
  }

  /** Books check out for 21 days. */
  @Override
  protected int getCheckoutDays()
  {
    return 21;
  }

  /** Reset renewal count when the book is freshly checked out or checked in. */
  @Override
  protected void onCheckOut()
  {
    renewalCount = 0;
  }

  /** Reset renewal count on check-in. */
  @Override
  protected void onCheckIn()
  {
    renewalCount = 0;
  }

  /**
   * Renews this book if it is checked out, not overdue, and has been renewed
   * fewer than 2 times. Renewal extends from the current due date.
   */
  @Override
  public void renew(Date now)
  {
    if (isCheckedOut() && !isOverdue(now) && renewalCount < 2)
    {
      // Extend from the existing due date, not from today.
      // We call checkOut with the current due date so the
      // onCheckOut hook would reset renewalCount — so we
      // save and restore it manually around the call.
      int countBefore = renewalCount;
      checkOut(getCurrentPatron(), getCurrentDueDate());
      renewalCount = countBefore + 1;
    }
  }

  /**
   * Returns the fine owed: $0.25 per day overdue, no cap.
   */
  @Override
  public double getFine(Date now)
  {
    if (isCheckedOut() && isOverdue(now))
    {
      return daysLate(now) * 0.25;
    }
    return 0;
  }

}
