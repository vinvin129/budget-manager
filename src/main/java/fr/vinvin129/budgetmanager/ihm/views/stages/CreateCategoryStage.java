package fr.vinvin129.budgetmanager.ihm.views.stages;

import fr.vinvin129.budgetmanager.ihm.IHM;
import fr.vinvin129.budgetmanager.ihm.views.controllers.create.category.CreateCategoryController;
import fr.vinvin129.budgetmanager.models.budget_logic.Category;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateCategoryStage extends Stage {
    CreateCategoryController controller;
    Category category = null;
    public CreateCategoryStage() throws IOException {
        super();
        this.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxmlLoader = new FXMLLoader(IHM.class.getResource("createViews/categories/create-category.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        this.controller = fxmlLoader.getController();
        controller.validate.setOnAction(actionEvent -> {
            this.category = this.controller.getCategory();
            close();
        });
        controller.cancel.setOnAction(actionEvent -> {
            close();
        });
        this.setScene(scene);
    }

    public Category display() {
        this.showAndWait();
        return category;
    }
}
