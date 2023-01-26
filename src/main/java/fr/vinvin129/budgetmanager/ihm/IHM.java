package fr.vinvin129.budgetmanager.ihm;

import fr.vinvin129.budgetmanager.budgetLogic.Backup;
import fr.vinvin129.budgetmanager.budgetLogic.Period;
import fr.vinvin129.budgetmanager.budgetLogic.budgets.Budget;
import fr.vinvin129.budgetmanager.budgetLogic.budgets.BudgetController;
import fr.vinvin129.budgetmanager.budgetLogic.history.History;
import fr.vinvin129.budgetmanager.budgetLogic.moments.BudgetMoment;
import fr.vinvin129.budgetmanager.exceptions.CreateBudgetException;
import fr.vinvin129.budgetmanager.exceptions.IllegalBudgetSizeException;
import fr.vinvin129.budgetmanager.ihm.views.controllers.HomeController;
import fr.vinvin129.budgetmanager.ihm.views.controllers.create.budget.CreateBudgetController;
import fr.vinvin129.budgetmanager.ihm.views.controllers.dashboard.DashboardController;
import fr.vinvin129.budgetmanager.ihm.views.controllers.dashboard.ViewBudgetController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

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
     * the main {@link BudgetController}
     */
    protected BudgetController budgetController = null;

    @Override
    public void start(Stage stage) throws IOException {
        principalStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(IHM.class.getResource("home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        HomeController homeController = fxmlLoader.getController();
        homeController.startButton.setOnAction(actionEvent -> {
            try {
                loadBudget();
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
     * Permet de charger en mémoire les données sauvegardées sur le disque et d'initialiser le gestionnaire de l'historique {@link History}.
     * En cas d'absence de sauvegarde ou en cas de sauvegarde compromise, ne fait rien.
     */
    private void loadBudget() {
        Backup backup = Backup.INSTANCE;
        Optional<Map<Period, BudgetMoment>> opData = backup.load();
        if (opData.isPresent()) {
            Map<Period, BudgetMoment> data = opData.get();
            try {
                Optional<BudgetController> optionalBudgetController = History.INSTANCE.initialize(new TreeMap<>(data));
                optionalBudgetController.ifPresent(controller -> this.budgetController = controller);
            } catch (IllegalBudgetSizeException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * create the main {@link BudgetController}. And show this {@link Budget} graphically
     * @throws IOException if FXML template to create budget can't be loaded from disk
     */
    private void showBudget() throws IOException {
        if (this.budgetController == null) {
            FXMLLoader fxmlLoader = new FXMLLoader(IHM.class.getResource("createViews/budgets/create-budget.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 500);
            principalStage.setScene(scene);
            CreateBudgetController createBudgetController = fxmlLoader.getController();
            createBudgetController.validateBudgetCreation.setOnAction(actionEvent -> {
                try {
                    this.budgetController = createBudgetController.getBudgetController();
                    this.budgetController.newMonth();
                    History.INSTANCE.initialize(budgetController);
                    FXMLLoader dashboardLoader = new FXMLLoader(IHM.class.getResource("dashboard/dashboard.fxml"));
                    Scene dashboardScene = new Scene(dashboardLoader.load(), 800, 500);
                    DashboardController dashboardController = dashboardLoader.getController();
                    ViewBudgetController viewBudgetController = dashboardController.getMainBudgetViewController().orElseThrow();
                    viewBudgetController.setBudgetController(budgetController);
                    principalStage.setScene(dashboardScene);
                } catch (CreateBudgetException e) {
                    e.showWarningAlert();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            FXMLLoader dashboardLoader = new FXMLLoader(IHM.class.getResource("dashboard/dashboard.fxml"));
            Scene dashboardScene = new Scene(dashboardLoader.load(), 800, 500);
            DashboardController dashboardController = dashboardLoader.getController();
            ViewBudgetController viewBudgetController = dashboardController.getMainBudgetViewController().orElseThrow();
            viewBudgetController.setBudgetController(budgetController);
            principalStage.setScene(dashboardScene);
        }
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop(){
        History history = History.INSTANCE;
        if (history.getActualPeriod() != null) {
            try {
                history.save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}