package fr.vinvin129.budgetmanager.events;

/**
 * an event type for an event
 * @param name the event name
 * @author vinvin129
 */
public record EventT(String name) {
    /**
     * the {@link EventT} for Tests
     */
    public static EventT TEST = new EventT("Test");
    /**
     * the {@link EventT} for Data change like a new value
     */
    public static EventT DATA_CHANGE = new EventT("Data Change");
    /**
     * the {@link EventT} for added expense
     */
    public static EventT ADDED_EXPENSE = new EventT("new expense");
    /**
     * the {@link EventT} for when history month was changed
     */
    public static EventT HISTORY_MONTH_CHANGE = new EventT("History month was changed");
    /**
     * the {@link EventT} for all events
     */
    public static EventT ALL_EVENTS = new EventT("all events");
}
