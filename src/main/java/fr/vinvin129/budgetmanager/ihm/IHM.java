package fr.vinvin129.budgetmanager.ihm;

import fr.vinvin129.budgetmanager.models.budget_logic.Budget;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class IHM extends Application {
    private static Stage principalStage = null;
    private static Budget budget = null;

    public static Budget getBudget() {
        return budget;
    }

    public static void setBudget(Budget budget) {
        IHM.budget = budget;
    }

    public static Stage getPrincipalStage() {
        return principalStage;
    }

    @Override
    public void start(Stage stage) throws IOException {
        principalStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(IHM.class.getResource("home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        stage.setTitle("Budget Manager");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}