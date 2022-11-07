package fr.vinvin129.budgetmanager.models.budget_logic;

import java.util.Calendar;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class History {
    private final Budget budget;

    private Period actualPeriod;

    private final SortedSet<Period> periods = new TreeSet<>();

    public static Period getPeriodByDate(Calendar calendar) {
        return new Period(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
    }

    public History(Budget budget, Period firstPeriod) {
        if (budget == null) {
            throw new NullPointerException();
        }
        this.budget = budget;
        this.actualPeriod = firstPeriod;
        this.periods.add(firstPeriod);
    }

    private synchronized void newMonth(Period period) {
        Calendar newP = Calendar.getInstance();
        //noinspection MagicConstant
        newP.set(period.year(), period.month(), 1);
        newP.add(Calendar.MONTH, 1);
        this.actualPeriod = getPeriodByDate(newP);
        this.periods.add(this.actualPeriod);
        this.budget.newMonth();
    }

    public synchronized void nextMonth() {
        this.periods.stream()
                .filter(period -> this.actualPeriod.compareTo(period) < 0)
                .findFirst()
                .ifPresentOrElse(
                        period -> this.actualPeriod = period, () -> newMonth(this.actualPeriod));
    }

    public synchronized boolean lastMonth() {
        Period last = this.periods.stream()
                .filter(period -> this.actualPeriod.compareTo(period) > 0).max(Comparator.naturalOrder())
                .orElse(null);

        if (last == null) {
            return false;
        }
        this.actualPeriod = last;
        return true;
    }

    public synchronized Period getActualPeriod() {
        return actualPeriod;
    }

    public Budget getBudget() {
        return budget;
    }
}
