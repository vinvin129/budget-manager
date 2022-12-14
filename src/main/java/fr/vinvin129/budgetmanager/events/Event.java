package fr.vinvin129.budgetmanager.events;

/**
 * an event / action to be executed
 * @author vinvin129
 */
@FunctionalInterface
public interface Event {
    /**
     * the event method
     * @param eventT the {@link EventT}
     */
    void fire(EventT eventT);
}
