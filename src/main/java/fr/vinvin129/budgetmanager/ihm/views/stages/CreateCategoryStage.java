package fr.vinvin129.budgetmanager.ihm.views.stages;

import fr.vinvin129.budgetmanager.exceptions.CreateCategoryException;
import fr.vinvin129.budgetmanager.ihm.IHM;
import fr.vinvin129.budgetmanager.ihm.views.controllers.create.category.CreateCategoryController;
import fr.vinvin129.budgetmanager.models.budget_logic.Category;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * an independent window like dialog to get a created category
 * @author vinvin129
 */
public class CreateCategoryStage extends Stage {
    /**
     * the controller for create category view
     */
    CreateCategoryController controller;
    /**
     * the category created when validate button was pressed
     */
    Category category = null;

    /**
     * create a new instance for create graphically a category
     * @throws IOException if template FXML can't be load from disk
     */
    public CreateCategoryStage() throws IOException {
        super();
        this.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxmlLoader = new FXMLLoader(IHM.class.getResource("createViews/categories/create-category.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        this.controller = fxmlLoader.getController();
        controller.validateCategoryCreation.setOnAction(actionEvent -> {
            try {
                this.category = this.controller.getCategory();
                close();
            } catch (CreateCategoryException e) {
                e.showWarningAlert();
            }
        });
        controller.cancel.setOnAction(actionEvent -> close());
        this.setScene(scene);
    }

    /**
     * show and wait that new window was closed
     * @return the created category or null if no created
     */
    public Category display() {
        this.showAndWait();
        return category;
    }

    public void display(Category category) {
        this.controller.setInitialCategory(category);
        display();
    }
}
