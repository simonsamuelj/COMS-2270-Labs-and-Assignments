package library;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Abstract base class providing common implementation for all library items.
 * Handles title, due date, checked-out patron, and the shared logic for
 * checkOut, checkIn, isCheckedOut, isOverdue, getDueDate, getPatron,
 * getTitle, and compareTo.
 */
public abstract class AbstractItem implements Item {
    private String title;
    private Date dueDate;
    private Patron checkedOutTo;

    /**
     * Constructs an AbstractItem with the given title. Due date and patron
     * start as null (not checked out).
     */
    protected AbstractItem(String givenTitle)
    {
        title = givenTitle;
        dueDate = null;
        checkedOutTo = null;
    }

    /**
     * Returns the number of days this item may be checked out for.
     * Subclasses that support checkout must override this.
     */
    protected int getCheckoutDays()
    {
        return 0;
    }

    /**
     * Performs the actual checkout: sets dueDate to getCheckoutDays() from
     * the given date (at 11:59:59 pm) and records the patron.
     * Only does anything if the item is not already checked out.
     */
    @Override
    public void checkOut(Patron p, Date now)
    {
        if (!isCheckedOut())
        {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(now);
            cal.add(Calendar.DAY_OF_YEAR, getCheckoutDays());
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            dueDate = cal.getTime();
            checkedOutTo = p;
            onCheckOut();
        }
    }

    /**
     * Hook called at the end of a successful checkOut. Subclasses may
     * override to perform additional work (e.g. resetting a renewal counter).
     */
    protected void onCheckOut()
    {
        // default: nothing extra
    }

    /**
     * Checks in this item. Does nothing if not currently checked out.
     */
    @Override
    public void checkIn()
    {
        if (isCheckedOut())
        {
            checkedOutTo = null;
            dueDate = null;
            onCheckIn();
        }
    }

    /**
     * Hook called at the end of a successful checkIn. Subclasses may
     * override to perform additional work (e.g. resetting a renewal counter).
     */
    protected void onCheckIn()
    {
        // default: nothing extra
    }

    /**
     * Renews this item. Default implementation does nothing (no renewal
     * allowed). Subclasses that support renewal should override this.
     */
    @Override
    public void renew(Date now)
    {
        // default: cannot be renewed
    }

    @Override
    public boolean isCheckedOut()
    {
        return dueDate != null;
    }

    @Override
    public boolean isOverdue(Date now)
    {
        if (!isCheckedOut())
        {
            return false;
        }
        return now.after(dueDate);
    }

    /**
     * Helper that returns the number of days this item is overdue, or 0
     * if it is not overdue. Uses ceiling division so any portion of a day
     * counts as a full day.
     */
    protected int daysLate(Date now)
    {
        if (!isOverdue(now))
        {
            return 0;
        }
        double elapsed = now.getTime() - dueDate.getTime();
        int millisPerDay = 24 * 60 * 60 * 1000;
        return (int) Math.ceil(elapsed / millisPerDay);
    }

    @Override
    public Date getDueDate()
    {
        return dueDate;
    }

    /**
     * Returns the current due date so subclasses (e.g. Book.renew) can
     * pass it back into checkOut without breaking encapsulation.
     */
    protected Date getCurrentDueDate()
    {
        return dueDate;
    }

    /**
     * Returns the patron currently holding this item, or null.
     */
    protected Patron getCurrentPatron()
    {
        return checkedOutTo;
    }

    @Override
    public Patron getPatron()
    {
        return checkedOutTo;
    }

    @Override
    public String getTitle()
    {
        return title;
    }

    @Override
    public int compareTo(Item other)
    {
        return title.compareTo(other.getTitle());
    }
}
