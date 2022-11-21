package fr.vinvin129.budgetmanager.budgetLogic;

/**
 * a specific moment in the time
 * @param month the month
 * @param year the year
 */
public record Period(int month, int year) implements Comparable<Period> {
    @Override
    public int compareTo(Period o) {
        int compYear = Integer.compare(this.year, o.year);
        if (compYear == 0) {
            return Integer.compare(this.month, o.month);
        }
        return compYear;
    }
}