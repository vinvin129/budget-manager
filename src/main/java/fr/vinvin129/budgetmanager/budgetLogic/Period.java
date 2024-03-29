package fr.vinvin129.budgetmanager.budgetLogic;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Calendar;
import java.util.Formatter;

/**
 * a specific moment in the time
 * @param month the month
 * @param year the year
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonSerialize
public record Period(int month, int year) implements Comparable<Period> {
    @Override
    public int compareTo(Period o) {
        int compYear = Integer.compare(this.year, o.year);
        if (compYear == 0) {
            return Integer.compare(this.month, o.month);
        }
        return compYear;
    }

    @Override
    public String toString() {
        Formatter fmt;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.MONTH, this.month);
        fmt = new Formatter();
        fmt.format("%tB ", cal);
        return fmt.toString() + this.year;
    }
}