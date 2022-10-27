package fr.vinvin129.budgetmanager.events;

import java.util.HashSet;
import java.util.Set;

/**
 * an object that can be observed
 * @author vinvin129
 */
public abstract class Observable {
    private final Set<Listener> listeners = new HashSet<>();

    /**
     * add a new {@link Listener} for this observable
     * @param listener the {@link Listener} object
     */
    public final void addListener(Listener listener) {
        listeners.add(listener);
    }

    /**
     * remove a specific {@link Listener} on this observable
     * @param listener the {@link Listener} object
     */
    public final void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    /**
     * remove a {@link Listener} with specific {@link EventT} on this observable
     * @param event the {@link EventT} object
     */
    public final void removeListener(EventT event) {
        this.listeners.removeIf(listener -> listener.eventType().equals(event));
    }

    /**
     * execute the linked {@link Event} with a {@link EventT}
     * @param event the {@link EventT} object
     */
    protected final void fire(EventT event) {
        this.listeners.stream()
                .filter(l -> l.eventType().equals(event))
                .forEach(l -> l.event().fire());
    }
}
