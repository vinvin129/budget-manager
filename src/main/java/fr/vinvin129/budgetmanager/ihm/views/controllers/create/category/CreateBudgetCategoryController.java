package fr.vinvin129.budgetmanager.ihm.views.controllers.create.category;

import fr.vinvin129.budgetmanager.models.budget_logic.Budget;
import fr.vinvin129.budgetmanager.models.budget_logic.BudgetCategory;
import fr.vinvin129.budgetmanager.models.budget_logic.Category;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class CreateBudgetCategoryController implements CreateCategory{
    public TextField budgetName;
    public TextField budgetAllocation;
    public ListView<Category> categoryList;

    /**
     * @return the {@link Category} object or null
     */
    @Override
    public Category getCategory() {
        String name = this.budgetName.getText();
        String allocation = this.budgetAllocation.getText();
        if (name.equals("") || allocation.equals("")) {
            return null;
        }
        Budget budget;
        try {
            budget = new Budget(name, Integer.parseInt(allocation));
        } catch (NumberFormatException e) {
            return null;
        }
        categoryList.getItems().forEach(budget::addCategory);
        return new BudgetCategory(budget);
    }
}
