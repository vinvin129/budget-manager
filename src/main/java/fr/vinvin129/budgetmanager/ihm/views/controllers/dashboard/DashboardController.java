package fr.vinvin129.budgetmanager.ihm.views.controllers.dashboard;

import fr.vinvin129.budgetmanager.ihm.IHM;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Optional;

public class DashboardController {
    @FXML
    public AnchorPane navBar;
    @FXML
    public BorderPane mainPane;
    @FXML
    public BorderPane viewMainBudget;

    private ViewBudgetController mainBudgetViewController;

    @FXML
    public void initialize() {
        FXMLLoader budgetViewLoader = new FXMLLoader(IHM.class.getResource("dashboard/view-budget.fxml"));
        try {
            this.mainPane.setCenter(budgetViewLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.mainBudgetViewController = budgetViewLoader.getController();
    }

    public Optional<ViewBudgetController> getMainBudgetViewController() {
        return Optional.ofNullable(mainBudgetViewController);
    }
}
