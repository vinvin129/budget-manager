package fr.vinvin129.budgetmanager.ihm.views.controllers.create.category;

import fr.vinvin129.budgetmanager.exceptions.CreateCategoryException;
import fr.vinvin129.budgetmanager.models.budget_logic.Category;
import fr.vinvin129.budgetmanager.models.budget_logic.StandardCategory;
import javafx.scene.control.TextField;

public class CreateStandardCategoryController implements CreateCategory {

    public TextField name;
    public TextField allocation;


    @Override
    public Category getCategory() throws CreateCategoryException {
        String name = this.name.getText();
        String allocation = this.allocation.getText();
        if (name.equals("") || allocation.equals("")) {
            throw new CreateCategoryException("le nom et l'allocation de la catégorie ne doivent pas être vide");
        }
        try {
            return new StandardCategory(name, Integer.parseInt(allocation));
        } catch (NumberFormatException e) {
            throw new CreateCategoryException("la valeur du champ allocation doit être un nombre");
        }
    }
}
