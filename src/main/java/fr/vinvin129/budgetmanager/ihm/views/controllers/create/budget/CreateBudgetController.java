package fr.vinvin129.budgetmanager.ihm.views.controllers.create.budget;

import fr.vinvin129.budgetmanager.exceptions.BudgetTooSmallException;
import fr.vinvin129.budgetmanager.exceptions.CreateBudgetException;
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

    public Budget getBudget() throws CreateBudgetException {
        String name = this.budgetName.getText();
        String allocation = this.budgetAllocation.getText();
        if (name.equals("") || allocation.equals("")) {
            throw new CreateBudgetException("le nom et l'allocation du budget ne doivent pas être vide");
        }
        Budget budget;
        try {
            budget = new Budget(name, Integer.parseInt(allocation));
        } catch (NumberFormatException | IllegalBudgetSizeException e) {
            throw new CreateBudgetException("la valeur du champ allocation doit être un nombre");
        }
        if (categoryList.getItems().size() == 0) {
            throw new CreateBudgetException("il doit y avoir au moins une catégorie dans le budget");
        }
        for (Category category : categoryList.getItems()) {
            try {
                budget.addCategory(category);
            } catch (BudgetTooSmallException e) {
                throw new CreateBudgetException(e.getMessage());
            }
        }

        return budget;
    }

    /**
     * @return the {@link Category} object or null
     */
    @Override
    public Category getCategory() throws CreateCategoryException {
        try {
            return new BudgetCategory(getBudget());
        } catch (CreateBudgetException e) {
            throw new CreateCategoryException("le budget auquel est lié la catégorie n'a pas pu être crée\n (" + e.getDescription() + ")");
        }
    }
}
