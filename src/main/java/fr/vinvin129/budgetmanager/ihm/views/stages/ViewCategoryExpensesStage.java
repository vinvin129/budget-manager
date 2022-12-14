package fr.vinvin129.budgetmanager.ihm.views.stages;

import fr.vinvin129.budgetmanager.budgetLogic.categories.Category;
import fr.vinvin129.budgetmanager.budgetLogic.categories.CategoryController;
import fr.vinvin129.budgetmanager.ihm.IHM;
import fr.vinvin129.budgetmanager.ihm.views.controllers.dashboard.ViewCategoryExpensesController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * the stage for view category expenses
 * @author vinvin129
 */
public class ViewCategoryExpensesStage extends Stage {
    /**
     * create a new {@link Stage} for view expenses of a {@link Category} by this {@link CategoryController}
     * @param categoryController the {@link CategoryController} object
     * @throws IOException if FXML file can't be loaded
     */
    public ViewCategoryExpensesStage(CategoryController categoryController) throws IOException {
        super();
        this.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(IHM.class.getResource("dashboard/view-category-expenses.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ViewCategoryExpensesController controller = fxmlLoader.getController();
        controller.setCategoryController(categoryController);
        this.setScene(scene);
    }
}
