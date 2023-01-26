package fr.vinvin129.budgetmanager.ihm.views.controllers.create.category;

import fr.vinvin129.budgetmanager.budgetLogic.categories.CategoryController;
import fr.vinvin129.budgetmanager.budgetLogic.moments.CategoryMoment;
import fr.vinvin129.budgetmanager.exceptions.BudgetTooSmallException;
import fr.vinvin129.budgetmanager.exceptions.CategoryTooBigException;
import fr.vinvin129.budgetmanager.exceptions.CreateCategoryException;
import fr.vinvin129.budgetmanager.exceptions.IllegalCategorySizeException;
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

    private CategoryController categoryController = null;

    /**
     * @return the {@link CategoryMoment} object
     * @throws CreateCategoryException if category can't be created
     */
    @Override
    public CategoryMoment getCategoryMoment() throws CreateCategoryException {
        String name = this.name.getText();
        String allocation = this.allocation.getText();
        if (name.equals("") || allocation.equals("")) {
            throw new CreateCategoryException("le nom et l'allocation de la catégorie ne doivent pas être vide");
        }
        try {
            if (categoryController != null) {
                categoryController.setName(name);
                categoryController.setAllocationPerMonth(Double.parseDouble(allocation));
                return categoryController.getModel().getMoment();
            } else {
                return CategoryMoment.create(name, Double.parseDouble(allocation));
            }
        } catch (NumberFormatException e) {
            throw new CreateCategoryException("la valeur du champ allocation doit être un nombre");
        } catch (IllegalCategorySizeException | BudgetTooSmallException e) {
            throw new CreateCategoryException(e.getMessage());
        } catch (CategoryTooBigException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * set default value for the form
     * @param categoryController the {@link CategoryController} controller for category data
     */
    @Override
    public void setInitialCategoryController(CategoryController categoryController) {
        if (categoryController != null) {
            this.categoryController = categoryController;
            this.name.setText(categoryController.getModel().getName());
            this.allocation.setText(String.valueOf(categoryController.getModel().getAllocationPerMonth()));
        }
    }
}
