package fr.vinvin129.budgetmanager.events;

/**
 * a listener to execute an {@link Event} when {@link EventT}
 * @param eventType the {@link EventT}
 * @param event the {@link Event}
 * @author vinvin129
 */
public record Listener(EventT eventType, Event event) {

}