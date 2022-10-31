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
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Arrays;

/**
 * the controller for budgets creations
 * @author vinvin129
 */
public class CreateBudgetController implements CreateCategory {

    /**
     * FXML reference for the budget name emplacement
     */
    @FXML
    public TextField budgetName;
    /**
     * FXML reference for the budget allocation emplacement
     */
    @FXML
    public TextField budgetAllocation;
    /**
     * FXML reference for the list of categories emplacement
     */
    @FXML
    public ListView<Category> categoryList;
    /**
     * FXML reference for the button to add category
     */
    @FXML
    public Button addCategoryButton;
    /**
     * FXML reference for the button validate the creation of the budget
     */
    @FXML
    public Button validateBudgetCreation;

    private BudgetCategory initialCategory = null;

    /**
     * create a template when view is loaded
     */
    @FXML
    public void initialize() {
        final MultipleSelectionModel<Category> selectionModel = this.categoryList.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        selectionModel.selectedItemProperty()
                .addListener((observableValue, oldCategory, newCategory) -> {
                    if (newCategory != null) {
                        Platform.runLater(() -> {
                            selectionModel.clearSelection();
                            try {
                                new CreateCategoryStage().display(newCategory);
                                this.categoryList.refresh();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });

                    }
                });
    }

    /**
     * called when the button to add category was pressed
     * @throws IOException .
     */
    @FXML
    public void addCategory() throws IOException {
        System.out.println("ajout d'une catégorie");
        Category category = new CreateCategoryStage().display();
        if (category != null) {
            categoryList.getItems().add(category);
        }
    }

    /**
     * create a {@link Budget} object with data in the view
     * @return a {@link Budget} object
     * @throws CreateBudgetException if the budget can't be created
     */
    public Budget getBudget() throws CreateBudgetException {
        String name = this.budgetName.getText();
        String allocation = this.budgetAllocation.getText();
        if (name.equals("") || allocation.equals("")) {
            throw new CreateBudgetException("le nom et l'allocation du budget ne doivent pas être vide");
        }
        Budget budget;
        try {
            if (initialCategory != null) {
                budget = initialCategory.getBudget();
                initialCategory.setName(name);
                budget.setAllocationPerMonth(Double.parseDouble(allocation));
            } else {
                budget = new Budget(name, Double.parseDouble(allocation));
            }
        } catch (NumberFormatException | IllegalBudgetSizeException e) {
            throw new CreateBudgetException("la valeur du champ allocation doit être un nombre");
        } catch (BudgetTooSmallException e) {
            throw new CreateBudgetException(e.getMessage());
        }
        if (categoryList.getItems().size() == 0) {
            throw new CreateBudgetException("il doit y avoir au moins une catégorie dans le budget");
        }
        Category[] categories = budget.getCategories();
        for (Category category : categories) {
            budget.removeCategory(category);
        }
        for (Category category : categoryList.getItems()) {
            try {
                budget.addCategory(category);
            } catch (BudgetTooSmallException e) {
                throw new CreateBudgetException(e.getMessage());
            }
        }

        if (initialCategory == null) {
            budget.newMonth();
        }
        return budget;
    }

    /**
     * @return the {@link Category} object
     * @throws CreateCategoryException if category can't be created
     */
    @Override
    public Category getCategory() throws CreateCategoryException {
        try {
            if (initialCategory != null) {
                return initialCategory;
            } else {
                return new BudgetCategory(getBudget());
            }
        } catch (CreateBudgetException e) {
            throw new CreateCategoryException("le budget auquel est lié la catégorie n'a pas pu être crée\n (" + e.getDescription() + ")");
        }
    }

    @Override
    public void setInitialCategory(Category category) {
        if (category instanceof BudgetCategory) {
            this.initialCategory = (BudgetCategory) category;
            this.budgetName.setText(category.getName());
            this.budgetAllocation.setText(String.valueOf(category.getAllocationPerMonth()));
            this.categoryList.getItems()
                    .setAll(Arrays.asList(
                                    ((BudgetCategory) category)
                                            .getBudget().getCategories()));
        }
    }
}
