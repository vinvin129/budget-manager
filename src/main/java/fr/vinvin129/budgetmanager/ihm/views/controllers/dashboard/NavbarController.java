package fr.vinvin129.budgetmanager.ihm.views.controllers.dashboard;

import fr.vinvin129.budgetmanager.budgetLogic.Period;
import fr.vinvin129.budgetmanager.budgetLogic.history.History;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class NavbarController {
    @FXML
    public Button previousButton;
    @FXML
    public Button nextButton;
    @FXML
    public Label monthLabel;
    private Period actualPeriod = History.createPeriod();

    /**
     * create a template when view is loaded
     */
    @FXML
    public void initialize() {
        this.monthLabel.setText(actualPeriod.toString());
    }

    public void setActualPeriod(Period actualPeriod) {
        this.actualPeriod = actualPeriod;
        this.monthLabel.setText(actualPeriod.toString());
    }
}
