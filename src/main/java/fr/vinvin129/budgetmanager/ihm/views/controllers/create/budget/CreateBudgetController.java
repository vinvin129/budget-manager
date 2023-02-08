package fr.vinvin129.budgetmanager.ihm.views.controllers.create.budget;

import fr.vinvin129.budgetmanager.budgetLogic.budgets.BudgetController;
import fr.vinvin129.budgetmanager.budgetLogic.categories.BudgetCategory;
import fr.vinvin129.budgetmanager.budgetLogic.categories.Category;
import fr.vinvin129.budgetmanager.budgetLogic.categories.CategoryController;
import fr.vinvin129.budgetmanager.budgetLogic.moments.BudgetMoment;
import fr.vinvin129.budgetmanager.budgetLogic.moments.CategoryMoment;
import fr.vinvin129.budgetmanager.exceptions.*;
import fr.vinvin129.budgetmanager.ihm.views.controllers.create.category.CreateCategory;
import fr.vinvin129.budgetmanager.ihm.views.stages.CreateCategoryStage;
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

    private BudgetController budgetController = new BudgetController(BudgetMoment.create("", 1));

    public CreateBudgetController() throws IllegalBudgetSizeException {
    }

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
                                new CreateCategoryStage().display(newCategory.getController());
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
    public void addCategory() throws IOException, IllegalBudgetSizeException, BudgetTooSmallException, IllegalCategorySizeException {
        try {
            updateCommonBudgetData();
            CategoryMoment categoryMoment = new CreateCategoryStage().display();
            if (categoryMoment == null) {
                return;
            }
            CategoryController categoryController = this.budgetController.addCategory(categoryMoment);
            if (categoryController != null) {
                categoryList.getItems().add(categoryController.getModel());
            }
        } catch (CreateBudgetException e) {
            e.showWarningAlert();
        }
    }

    private void updateCommonBudgetData() throws CreateBudgetException {
        String name = this.budgetName.getText();
        String allocation = this.budgetAllocation.getText();
        if (name.equals("") || allocation.equals("")) {
            throw new CreateBudgetException("Le nom et l'allocation du budget ne doivent pas être vide.");
        }
        try {
            this.budgetController.setName(name);
            this.budgetController.setAllocationPerMonth(Double.parseDouble(allocation));
        } catch (NumberFormatException e) {
            throw new CreateBudgetException("La valeur du champ allocation doit être un nombre.");
        } catch (BudgetTooSmallException e) {
            throw new CreateBudgetException(e.getMessage());
        }
    }

    /**
     * create a {@link BudgetController} object with data in the view
     * @return a {@link BudgetController} object
     * @throws CreateBudgetException if the budget can't be created
     */
    public BudgetController getBudgetController() throws CreateBudgetException {
        updateCommonBudgetData();

        if (categoryList.getItems().size() == 0) {
            throw new CreateBudgetException("Il doit y avoir au moins une catégorie dans le budget.");
        }
        return this.budgetController;
    }

    /**
     * @return the {@link Category} object
     * @throws CreateCategoryException if category can't be created
     */
    @Override
    public CategoryMoment getCategoryMoment() throws CreateCategoryException {
        try {
            return CategoryMoment.create(getBudgetController().getModel().getMoment());
        } catch (CreateBudgetException e) {
            throw new CreateCategoryException("Le budget auquel est lié la catégorie n'a pas pu être crée\n (" + e.getDescription() + ").");
        }
    }

    @Override
    public void setInitialCategoryController(CategoryController categoryController) {
        if (categoryController.getModel() instanceof BudgetCategory budgetCategory) {
            this.budgetController = budgetCategory.getBudgetController();
            this.budgetName.setText(this.budgetController.getModel().getName());
            this.budgetAllocation.setText(String.valueOf(this.budgetController.getModel().getAllocationPerMonth()));
            this.categoryList.getItems()
                    .setAll(Arrays.stream(this.budgetController.getModel().getCategoryControllers())
                            .map(CategoryController::getModel)
                            .toList());
        }
    }
}
