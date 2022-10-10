package fr.vinvin129.budgetmanager.ihm.views.controllers.create.category;

import fr.vinvin129.budgetmanager.ihm.IHM;
import fr.vinvin129.budgetmanager.models.budget_logic.Category;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class CreateCategoryController implements CreateCategory {
    @FXML
    public ChoiceBox<String> typeChoice;
    public Button validateCategoryCreation;
    public Button cancel;
    public BorderPane view;
    private BorderPane budgetView;
    private GridPane standardView;
    private CreateCategory budgetViewController;
    private CreateStandardCategoryController standardViewController;
    private CreateCategory actualCategoryController;

    @FXML
    public void initialize() throws IOException {
        FXMLLoader budgetViewLoader = new FXMLLoader(IHM.class.getResource("createViews/budgets/create-budget.fxml"));
        FXMLLoader standardViewLoader = new FXMLLoader(IHM.class.getResource("createViews/categories/create-standard-category.fxml"));
        this.budgetView = budgetViewLoader.load();
        this.budgetView.setBottom(null);
        this.standardView = standardViewLoader.load();
        this.budgetViewController = budgetViewLoader.getController();
        this.standardViewController = standardViewLoader.getController();
        this.view.setCenter(standardView);
        this.actualCategoryController = this.standardViewController;
    }



    @FXML
    public void onChoiceChange(ActionEvent actionEvent) {
        switch (typeChoice.getSelectionModel().getSelectedItem()) {
            case "Standard" -> {
                view.setCenter(standardView);
                view.getScene().getWindow().setHeight(standardView.getPrefHeight());
                view.getScene().getWindow().setWidth(standardView.getPrefWidth());
                actualCategoryController = standardViewController;
            }
            case "Budget" -> {
                view.setCenter(budgetView);
                view.getScene().getWindow().setHeight(budgetView.getPrefHeight());
                view.getScene().getWindow().setWidth(budgetView.getPrefWidth());
                actualCategoryController = budgetViewController;
            }
            default -> view.setCenter(null);
        }
    }

    /**
     * @return the {@link Category} object or null
     */
    @Override
    public Category getCategory() {
        if (actualCategoryController == null) {
            return null;
        }
        return actualCategoryController.getCategory();
    }
}
