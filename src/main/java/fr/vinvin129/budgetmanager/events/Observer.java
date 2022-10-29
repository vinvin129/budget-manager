package fr.vinvin129.budgetmanager.events;

/**
 * an object that observe an {@link Observable}
 * @author vinvin129
 */
public abstract class Observer {
    /**
     * the global listener
     */
    private final Listener listener = new Listener(EventT.ALL_EVENTS, this::onEvent);

    /**
     * on event received
     * @param eventT the {@link EventT}
     */
    abstract protected void onEvent(EventT eventT);

    /**
     * add a {@link Observable} object
     * @param observable the {@link Observable}
     */
    protected final void addObservable(Observable observable) {
        observable.addListener(listener);
    }

    /**
     * remove a {@link Observable} object
     * @param observable the {@link Observable}
     */
    protected final void removeObservable(Observable observable) {
        observable.removeListener(listener);
    }
}
