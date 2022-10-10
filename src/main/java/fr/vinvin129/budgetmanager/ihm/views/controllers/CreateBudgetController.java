package fr.vinvin129.budgetmanager.ihm.views.controllers;

import fr.vinvin129.budgetmanager.ihm.views.stages.CreateCategoryStage;
import fr.vinvin129.budgetmanager.models.budget_logic.Category;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;

public class CreateBudgetController {

    @FXML
    public TextField budgetName;
    @FXML
    public TextField budgetAllocation;
    @FXML
    public ListView<Category> categoryList;
    public Button addCategoryButton;

    @FXML
    public void addCategory(ActionEvent actionEvent) throws IOException {
        System.out.println("ajout d'une catégorie");
        Category category = new CreateCategoryStage().display();
        if (category != null) {
            categoryList.getItems().add(category);
        }
    }

    @FXML
    public void validate(ActionEvent actionEvent) {
        System.out.println("validation du budget");
    }
}
