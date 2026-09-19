package library;

import java.util.Date;

/**
 * A ReferenceItem is a library item that cannot be checked out.
 * All checkout-related operations are overridden to be no-ops.
 */
public class ReferenceItem extends AbstractItem
{
  /**
   * Constructs a ReferenceItem with the given title.
   */
  public ReferenceItem(String givenTitle)
  {
    super(givenTitle);
  }

  /** Cannot be checked out; does nothing. */
  @Override
  public void checkOut(Patron p, Date now)
  {
    // can't be checked out
  }

  /** Cannot be checked out; does nothing. */
  @Override
  public void checkIn()
  {
    // can't be checked out
  }

  /** Cannot be renewed; does nothing. */
  @Override
  public void renew(Date now)
  {
    // can't be checked out
  }

  /** Never overdue; always returns 0. */
  @Override
  public double getFine(Date now)
  {
    return 0;
  }

  /** Never overdue; always returns false. */
  @Override
  public boolean isOverdue(Date now)
  {
    return false;
  }

  /** Can never be checked out; always returns false. */
  @Override
  public boolean isCheckedOut()
  {
    return false;
  }
}
