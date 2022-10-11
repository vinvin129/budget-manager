package fr.vinvin129.budgetmanager.ihm.views.controllers.create.budget;

import fr.vinvin129.budgetmanager.exceptions.BudgetTooSmallException;
import fr.vinvin129.budgetmanager.exceptions.CreateCategoryException;
import fr.vinvin129.budgetmanager.exceptions.IllegalBudgetSizeException;
import fr.vinvin129.budgetmanager.ihm.views.controllers.create.category.CreateCategory;
import fr.vinvin129.budgetmanager.ihm.views.stages.CreateCategoryStage;
import fr.vinvin129.budgetmanager.models.budget_logic.Budget;
import fr.vinvin129.budgetmanager.models.budget_logic.BudgetCategory;
import fr.vinvin129.budgetmanager.models.budget_logic.Category;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;

public class CreateBudgetController implements CreateCategory {

    @FXML
    public TextField budgetName;
    @FXML
    public TextField budgetAllocation;
    @FXML
    public ListView<Category> categoryList;
    public Button addCategoryButton;
    public Button validateBudgetCreation;

    @FXML
    public void addCategory(ActionEvent actionEvent) throws IOException {
        System.out.println("ajout d'une catégorie");
        Category category = new CreateCategoryStage().display();
        if (category != null) {
            categoryList.getItems().add(category);
        }
    }

    public Budget getBudget() {
        String name = this.budgetName.getText();
        String allocation = this.budgetAllocation.getText();
        if (name.equals("") || allocation.equals("")) {
            return null;
        }
        Budget budget;
        try {
            budget = new Budget(name, Integer.parseInt(allocation));
        } catch (NumberFormatException | IllegalBudgetSizeException e) {
            return null;
        }
        if (categoryList.getItems().size() == 0) {
            return null;
        }
        for (Category category : categoryList.getItems()) {
            try {
                budget.addCategory(category);
            } catch (BudgetTooSmallException e) {
                return null;
            }
        }

        return budget;
    }

    /**
     * @return the {@link Category} object or null
     */
    @Override
    public Category getCategory() throws CreateCategoryException {
        Budget budget = getBudget();
        if (budget == null) {
            throw new CreateCategoryException("le budget auquel est lié la catégorie n'a pas pu être crée.");
        }
        return new BudgetCategory(budget);
    }
}
