package fr.vinvin129.budgetmanager.budgetLogic.history;

import fr.vinvin129.budgetmanager.budgetLogic.Period;

/**
 * an interface to implement navigation
 * @param <T> the type to navigate in the time
 * @author vinvin129
 */
public interface HistoryNav<T> {
    /**
     * nav to the previous month of T
     * @return the T of previous month
     */
    T previousMonth();

    /**
     * nav to the next month of T
     * @return the T of next month
     */
    T nextMonth();

    /**
     * create a new month of T, but doesn't nav to it
     * @return the new T
     */
    T newMonth();

    /**
     * get the actual T object
     * @return actual T object
     */
    T getActualModel();

    /**
     * get actual period
     * @return the {@link Period} object
     */
    Period getActualPeriod();
}
