package fr.vinvin129.budgetmanager.ihm.views.controllers.dashboard;

import fr.vinvin129.budgetmanager.Spent;
import fr.vinvin129.budgetmanager.ihm.IHM;
import fr.vinvin129.budgetmanager.ihm.views.controllers.create.spent.CreateSpentController;
import fr.vinvin129.budgetmanager.models.budget_logic.Budget;
import fr.vinvin129.budgetmanager.models.budget_logic.Category;
import fr.vinvin129.budgetmanager.models.budget_logic.StandardCategory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

/**
 * the controller for the view of a category
 * @author vinvin129
 */
public class ViewCategoryExpensesController {
    /**
     * FXML reference to spentList VBox
     */
    @FXML
    public VBox spentList;
    /**
     * FXML reference to label name
     */
    @FXML
    public Label name;
    /**
     * the linked {@link Category} object (can be null)
     */
    private Category category = null;

    /**
     * the linked {@link Budget} object (can be null)
     */
    private Budget budget = null;

    /**
     * on click on add spent button to create a {@link Spent} for this {@link Category} linked
     * @param actionEvent the event
     */
    @FXML
    public void addExpense(ActionEvent actionEvent) throws IOException {
        if (budget != null && category != null && category instanceof StandardCategory) {
            Stage addSpentStage = new Stage();
            FXMLLoader addSpentViewLoader = new FXMLLoader(IHM.class.getResource("createViews/spents/create-spent.fxml"));
            addSpentStage.setScene(new Scene(addSpentViewLoader.load()));
            CreateSpentController controller = addSpentViewLoader.getController();
            controller.setSpecificCategory(budget, (StandardCategory) category);
            addSpentStage.show();
        }
    }

    /**
     * a customised {@link AnchorPane} for show {@link Spent}
     */
    private class SpentView extends AnchorPane {
        /**
         * the linked {@link Spent} object
         */
        private final Spent spent;

        /**
         * create a view based on {@link AnchorPane} with a {@link Spent} object
         * @param spent the {@link Spent} object
         */
        public SpentView(Spent spent) {
            super();
            this.spent = spent;
            this.setHeight(ViewCategoryExpensesController.this.spentList.getHeight()-2);
            this.setWidth(ViewCategoryExpensesController.this.spentList.getWidth()-2);
            Label labelLabel = new Label(spent.label());
            Label priceLabel = new Label(String.valueOf(spent.price()));
            this.getChildren().addAll(labelLabel, priceLabel);
            AnchorPane.setLeftAnchor(labelLabel, 0.0);
            AnchorPane.setBottomAnchor(labelLabel, 0.0);
            AnchorPane.setTopAnchor(labelLabel, 0.0);

            AnchorPane.setRightAnchor(priceLabel, 0.0);
            AnchorPane.setBottomAnchor(priceLabel, 0.0);
            AnchorPane.setTopAnchor(priceLabel, 0.0);
        }

        public Spent getSpent() {
            return spent;
        }
    }

    /**
     * change the category to view
     * @param budget the {@link Budget} of category
     * @param category the {@link Category} object
     */
    public void setCategory(Budget budget, Category category) {
        if (Arrays.asList(budget.getCategories()).contains(category)) {
            this.budget = budget;
            this.category = category;
            this.name.setText("Catégorie " + this.category.getName());
            this.spentList.getChildren().clear();
            Arrays.stream(this.category.getSpentList())
                    .forEach(spent -> this.spentList.getChildren().add(new SpentView(spent)));
        }
    }
}
