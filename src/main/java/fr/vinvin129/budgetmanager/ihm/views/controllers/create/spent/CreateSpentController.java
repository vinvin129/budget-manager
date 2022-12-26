package fr.vinvin129.budgetmanager.ihm.views.controllers.create.spent;

import fr.vinvin129.budgetmanager.budgetLogic.Spent;
import fr.vinvin129.budgetmanager.budgetLogic.budgets.Budget;
import fr.vinvin129.budgetmanager.budgetLogic.categories.BudgetCategory;
import fr.vinvin129.budgetmanager.budgetLogic.categories.Category;
import fr.vinvin129.budgetmanager.budgetLogic.categories.CategoryController;
import fr.vinvin129.budgetmanager.budgetLogic.categories.StandardCategory;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Arrays;
import java.util.Objects;

/**
 * the controller to create a {@link fr.vinvin129.budgetmanager.budgetLogic.Spent} object for a {@link StandardCategory}
 * @author vinvin129
 */
public class CreateSpentController {
    /**
     * FXML reference for the cancel button
     */
    @FXML
    public Button cancel;
    /**
     * FXML reference for the validate button
     */
    @FXML
    public Button validate;
    /**
     * FXML reference for the title of {@link fr.vinvin129.budgetmanager.budgetLogic.Spent}
     */
    @FXML
    public TextField titleTextField;
    /**
     * FXML reference for the selection of the {@link Budget} of the {@link fr.vinvin129.budgetmanager.budgetLogic.Spent}
     */
    @FXML
    public ChoiceBox<Budget> budgetChoiceBox;
    /**
     * FXML reference for the selection of the {@link StandardCategory} of the {@link Budget} selected
     */
    @FXML
    public ChoiceBox<StandardCategory> categoryChoiceBox;
    /**
     * FXML reference for the root pane of this view
     */
    @FXML
    public BorderPane view;
    /**
     * FXML reference for the price spinner
     */
    @FXML
    public TextField priceTextField;

    /**
     * call when validate button is pressed
     */
    @FXML
    public void validateSpentCreation() {
        String label = titleTextField.getText();
        double price;
        try {
            price = Double.parseDouble(priceTextField.getText());
        } catch (NumberFormatException e) {
            showWaringAlterAndWait("le champ 'prix' doit être un nombre !");
            return;
        }
        Budget selectedBudget = this.budgetChoiceBox.getValue();
        StandardCategory selectedCategory = this.categoryChoiceBox.getValue();
        if (label == null || Objects.equals(label.trim(), "")) {
            showWaringAlterAndWait("Le champ 'intitulé' ne doit pas être vide");
            return;
        }

        if (price <= 0) {
            showWaringAlterAndWait("Le champ 'prix' doit être suppérieur à 0");
            return;
        }

        if (selectedBudget != null && selectedCategory != null) {
            Spent spent = new Spent(label, Double.parseDouble(priceTextField.getText()), null);
            //TODO à changer
            selectedCategory.getController().addSpent(spent);
            cancelSpentCreation();
        } else {
            showWaringAlterAndWait("Vous devez sélectionner un budget et une catégorie");
        }
    }

    /**
     * show an alert window for impossibility to create spent and wait this window's closed
     * @param description a description
     */
    private void showWaringAlterAndWait(String description) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Alerte lors de la création d'une dépense");
        alert.setHeaderText("impossible de créer la dépense");
        alert.setContentText(description);
        alert.showAndWait();
    }

    /**
     * call when cancel button is pressed
     */
    @FXML
    public void cancelSpentCreation() {
        Window window = this.view.getScene().getWindow();
        if (window instanceof Stage) {
            ((Stage) window).close();
        }
    }

    /**
     * call when choice box of budget is changed
     */
    @FXML
    public void budgetChoiceChanged() {
        this.categoryChoiceBox.getItems().clear();
        Budget budget = this.budgetChoiceBox.getValue();
        if (budget != null) {
            Arrays.stream(budget.getCategoryControllers())
                    .map(CategoryController::getModel)
                    .filter(category -> category instanceof StandardCategory)
                    .forEach(category -> this.categoryChoiceBox.getItems().add((StandardCategory) category));
            this.categoryChoiceBox.setValue(this.categoryChoiceBox.getItems().get(0));
        }
    }

    /**
     * change the main {@link Budget} object
     * @param budget the {@link Budget} object
     */
    public void setBudgetRoot(Budget budget) {
        ObservableList<Budget> items = this.budgetChoiceBox.getItems();
        items.clear();
        items.add(budget);
        this.budgetChoiceBox.setValue(budget);
        Arrays.stream(budget.getCategoryControllers())
                .map(CategoryController::getModel)
                .filter(category -> category instanceof BudgetCategory)
                .forEach(category -> items.add(((BudgetCategory)category).getBudgetController().getModel()));
        //budgetChoiceChanged(null);
    }

    /**
     * the create spent window will be configured to a specific {@link Category} and can't be changed in IHM
     * @param category the {@link StandardCategory} object
     */
    public void setSpecificCategory(StandardCategory category) {
        if (category == null) {
            throw new NullPointerException();
        }
        Budget budget = category.getController().getBudgetParentController().getModel();
        if (budget == null) {
            throw new NullPointerException();
        }

        if (Arrays.asList(budget.getCategoryControllers()).contains(category.getController())) {
            this.budgetChoiceBox.setDisable(true);
            this.categoryChoiceBox.setDisable(true);
            this.budgetChoiceBox.setValue(budget);
            this.categoryChoiceBox.setValue(category);
        }
    }
}
