package fr.vinvin129.budgetmanager.ihm.views.controllers.create.category;

import fr.vinvin129.budgetmanager.models.budget_logic.Category;
import fr.vinvin129.budgetmanager.models.budget_logic.StandardCategory;
import javafx.scene.control.TextField;

public class CreateStandardCategoryController implements CreateCategory {

    public TextField name;
    public TextField allocation;


    @Override
    public Category getCategory() {
        String name = this.name.getText();
        String allocation = this.allocation.getText();
        if (name.equals("") || allocation.equals("")) {
            return null;
        }
        try {
            return new StandardCategory(name, Integer.parseInt(allocation));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
