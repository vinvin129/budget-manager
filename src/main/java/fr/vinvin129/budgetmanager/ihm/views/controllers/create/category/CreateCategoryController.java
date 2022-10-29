package fr.vinvin129.budgetmanager.ihm.views.controllers.create.category;

import fr.vinvin129.budgetmanager.exceptions.CreateCategoryException;
import fr.vinvin129.budgetmanager.ihm.IHM;
import fr.vinvin129.budgetmanager.models.budget_logic.BudgetCategory;
import fr.vinvin129.budgetmanager.models.budget_logic.Category;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;

/**
 * the controller for categories creations
 * @author vinvin129
 */
public class CreateCategoryController implements CreateCategory {
    /**
     * FXML reference for type's choice emplacement
     */
    @FXML
    public ChoiceBox<String> typeChoice;
    /**
     * FXML reference for button to create the category
     */
    @FXML
    public Button validateCategoryCreation;
    /**
     * FXML reference for button to cancel the category creation
     */
    @FXML
    public Button cancel;
    /**
     * FXML reference for the root view
     */
    @FXML
    public BorderPane view;
    /**
     * FXML reference for the budget category view
     */
    @FXML
    private BorderPane budgetView;
    /**
     * FXML reference for the standard category view
     */
    @FXML
    private GridPane standardView;
    /**
     * the budget view controller
     */
    private CreateCategory budgetViewController;
    /**
     * the standard view controller
     */
    private CreateStandardCategoryController standardViewController;
    /**
     * the actual view controller between standard and budget
     */
    private CreateCategory actualCategoryController;

    /**
     * load budget and standard views templates and controllers. And set the current view to standard
     * @throws IOException if load from disk fail
     */
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

    /**
     * set view template and controller to selected type (standard or budget) when selected type change in IHM
     */
    @FXML
    public void onChoiceChange() {
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

    @Override
    public void setInitialCategory(Category category) {
        typeChoice.getSelectionModel().select(category instanceof BudgetCategory ? 1 : 0);
        this.actualCategoryController.setInitialCategory(category);
    }

    /**
     * @return the {@link Category} object
     * @throws CreateCategoryException if category can't be created
     */
    @Override
    public Category getCategory() throws CreateCategoryException {
        if (actualCategoryController == null) {
            throw new CreateCategoryException("erreur inconnue (problème de controleurs)");
        }
        return actualCategoryController.getCategory();
    }
}
