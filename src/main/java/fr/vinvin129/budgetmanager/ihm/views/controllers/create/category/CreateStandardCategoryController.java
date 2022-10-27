package fr.vinvin129.budgetmanager.ihm.views.controllers.create.category;

import fr.vinvin129.budgetmanager.exceptions.CreateCategoryException;
import fr.vinvin129.budgetmanager.exceptions.IllegalCategorySizeException;
import fr.vinvin129.budgetmanager.models.budget_logic.Category;
import fr.vinvin129.budgetmanager.models.budget_logic.StandardCategory;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * the controller for standard categories creations
 * @author vinvin129
 */
public class CreateStandardCategoryController implements CreateCategory {

    /**
     * FXML reference for the category name emplacement
     */
    @FXML
    public TextField name;
    /**
     * FXML reference for the category allocation emplacement
     */
    @FXML
    public TextField allocation;

    /**
     * @return the {@link Category} object
     * @throws CreateCategoryException if category can't be created
     */
    @Override
    public Category getCategory() throws CreateCategoryException {
        String name = this.name.getText();
        String allocation = this.allocation.getText();
        if (name.equals("") || allocation.equals("")) {
            throw new CreateCategoryException("le nom et l'allocation de la catégorie ne doivent pas être vide");
        }
        try {
            return new StandardCategory(name, Double.parseDouble(allocation));
        } catch (NumberFormatException e) {
            throw new CreateCategoryException("la valeur du champ allocation doit être un nombre");
        } catch (IllegalCategorySizeException e) {
            throw new CreateCategoryException(e.getMessage());
        }
    }
}
