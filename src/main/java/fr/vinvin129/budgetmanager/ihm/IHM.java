package fr.vinvin129.budgetmanager.ihm;

import fr.vinvin129.budgetmanager.exceptions.CreateBudgetException;
import fr.vinvin129.budgetmanager.ihm.views.controllers.HomeController;
import fr.vinvin129.budgetmanager.ihm.views.controllers.create.budget.CreateBudgetController;
import fr.vinvin129.budgetmanager.ihm.views.controllers.dashboard.ViewBudgetController;
import fr.vinvin129.budgetmanager.models.budget_logic.Budget;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * the main class for the IHM
 * @author vinvin129
 */
public class IHM extends Application {
    /**
     * this principal stage
     */
    protected Stage principalStage = null;
    /**
     * the main {@link Budget}
     */
    protected Budget budget = null;

    @Override
    public void start(Stage stage) throws IOException {
        principalStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(IHM.class.getResource("home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        HomeController homeController = fxmlLoader.getController();
        homeController.startButton.setOnAction(actionEvent -> {
            try {
                showBudget();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        stage.setTitle("Budget Manager");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * create the main {@link Budget}. And in the future : show this {@link Budget} graphically
     * @throws IOException if FXML template to create budget can't be loaded from disk
     */
    private void showBudget() throws IOException {
        /*try {
            this.budget = new Budget("test", 1000);
        } catch (IllegalBudgetSizeException e) {
            throw new RuntimeException(e);
        }*/
        if (this.budget == null) {
            FXMLLoader fxmlLoader = new FXMLLoader(IHM.class.getResource("createViews/budgets/create-budget.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 500);
            principalStage.setScene(scene);
            CreateBudgetController createBudgetController = fxmlLoader.getController();
            createBudgetController.validateBudgetCreation.setOnAction(actionEvent -> {
                try {
                    this.budget = createBudgetController.getBudget();
                    FXMLLoader dashboardLoader = new FXMLLoader(IHM.class.getResource("dashboard/view-budget.fxml"));
                    Scene dashboardScene = new Scene(dashboardLoader.load(), 800, 500);
                    ViewBudgetController dashboardController = dashboardLoader.getController();
                    dashboardController.setBudget(budget);
                    principalStage.setScene(dashboardScene);
                } catch (CreateBudgetException e) {
                    e.showWarningAlert();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            FXMLLoader dashboardLoader = new FXMLLoader(IHM.class.getResource("dashboard/view-budget.fxml"));
            Scene scene = new Scene(dashboardLoader.load(), 800, 500);
            ViewBudgetController dashboardController = dashboardLoader.getController();
            dashboardController.setBudget(budget);
            principalStage.setScene(scene);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}