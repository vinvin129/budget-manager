package fr.vinvin129.budgetmanager.ihm.views.controllers.dashboard;

import fr.vinvin129.budgetmanager.budgetLogic.Period;
import fr.vinvin129.budgetmanager.budgetLogic.history.History;
import fr.vinvin129.budgetmanager.events.EventT;
import fr.vinvin129.budgetmanager.events.Observer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class NavbarController extends Observer {
    private final History history = History.INSTANCE;

    @FXML
    public Button previousButton;
    @FXML
    public Button nextButton;
    @FXML
    public Label monthLabel;

    /**
     * create a template when view is loaded
     */
    @FXML
    public void initialize() {
        setActualPeriod(history.getActualPeriod());
        this.addObservable(history);
    }

    private void setActualPeriod(Period actualPeriod) {
        Platform.runLater(() -> {
            this.previousButton.setDisable(!history.hasPrevious());
            this.monthLabel.setText(actualPeriod.toString());
        });
    }

    @FXML
    public void previous() {
        history.previousMonth();
    }

    @FXML
    public void next() {
        if (history.hasNext()) {
            history.nextMonth();
        } else {
            history.newMonth();
            history.nextMonth();
        }
    }

    @Override
    protected void onEvent(EventT eventT) {
        if (eventT.equals(EventT.HISTORY_MONTH_CHANGE)) {
            setActualPeriod(history.getActualPeriod());
        }
    }
}
