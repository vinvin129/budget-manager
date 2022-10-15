package fr.vinvin129.budgetmanager.ihm.views.controllers.create.spent;

import fr.vinvin129.budgetmanager.Spent;
import fr.vinvin129.budgetmanager.exceptions.BudgetNotContainCategoryException;
import fr.vinvin129.budgetmanager.models.budget_logic.Budget;
import fr.vinvin129.budgetmanager.models.budget_logic.BudgetCategory;
import fr.vinvin129.budgetmanager.models.budget_logic.StandardCategory;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Arrays;

/**
 * the controller to create a {@link fr.vinvin129.budgetmanager.Spent} object for a {@link StandardCategory}
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
     * FXML reference for the title of {@link fr.vinvin129.budgetmanager.Spent}
     */
    @FXML
    public TextField titleTextField;
    /**
     * FXML reference for the selection of the {@link Budget} of the {@link fr.vinvin129.budgetmanager.Spent}
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
     * @param actionEvent the event
     */
    @FXML
    public void validateSpentCreation(ActionEvent actionEvent) throws Exception {
        String label = titleTextField.getText();
        Budget selectedBudget = this.budgetChoiceBox.getValue();
        StandardCategory selectedCategory = this.categoryChoiceBox.getValue();
        if (selectedBudget != null && selectedCategory != null) {
            try {
                Spent spent = new Spent(selectedCategory, label, Integer.parseInt(priceTextField.getText()));
                try {
                    selectedBudget.addSpent(spent);
                } catch (BudgetNotContainCategoryException e) {
                    throw new RuntimeException(e);
                }
            } catch (NumberFormatException e) {
                throw new Exception("ça doit être un nombre !!!");
            }
            cancelSpentCreation(actionEvent);
        }
    }

    /**
     * call when cancel button is pressed
     * @param actionEvent the event
     */
    @FXML
    public void cancelSpentCreation(ActionEvent actionEvent) {
        Window window = this.view.getScene().getWindow();
        if (window instanceof Stage) {
            ((Stage) window).close();
        }
    }

    /**
     * call when choice box of budget is changed
     * @param actionEvent the event
     */
    @FXML
    public void budgetChoiceChanged(ActionEvent actionEvent) {
        this.categoryChoiceBox.getItems().clear();
        Budget budget = this.budgetChoiceBox.getValue();
        if (budget != null) {
            Arrays.stream(budget.getCategories())
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
        Arrays.stream(budget.getCategories())
                .filter(category -> category instanceof BudgetCategory)
                .forEach(category -> items.add(((BudgetCategory)category).getBudget()));
        //budgetChoiceChanged(null);
    }
}
