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
}
