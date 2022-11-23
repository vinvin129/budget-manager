package fr.vinvin129.budgetmanager.budgetLogic.history;

import fr.vinvin129.budgetmanager.budgetLogic.Period;

public interface HistoryNav<T> {
    T previousMonth();

    T nextMonth();

    T newMonth();

    T getActualModel();

    Period getActualPeriod();
}
