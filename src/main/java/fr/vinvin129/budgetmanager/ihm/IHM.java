package fr.vinvin129.budgetmanager.ihm;

import fr.vinvin129.budgetmanager.exceptions.CreateBudgetException;
import fr.vinvin129.budgetmanager.ihm.views.controllers.HomeController;
import fr.vinvin129.budgetmanager.ihm.views.controllers.create.budget.CreateBudgetController;
import fr.vinvin129.budgetmanager.models.budget_logic.Budget;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class IHM extends Application {
    protected Stage principalStage = null;
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

    private void showBudget() throws IOException {
        if (this.budget == null) {
            FXMLLoader fxmlLoader = new FXMLLoader(IHM.class.getResource("createViews/budgets/create-budget.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 500);
            principalStage.setScene(scene);
            CreateBudgetController createBudgetController = fxmlLoader.getController();
            createBudgetController.validateBudgetCreation.setOnAction(actionEvent -> {
                try {
                    System.out.println(createBudgetController.getBudget());
                } catch (CreateBudgetException e) {
                    e.showWarningAlert();
                }
            });
        }
    }

    public static void main(String[] args) {
        launch();
    }
}