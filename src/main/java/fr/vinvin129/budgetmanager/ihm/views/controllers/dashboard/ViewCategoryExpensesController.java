package fr.vinvin129.budgetmanager.ihm.views.controllers.dashboard;

import fr.vinvin129.budgetmanager.Spent;
import fr.vinvin129.budgetmanager.events.EventT;
import fr.vinvin129.budgetmanager.events.Observer;
import fr.vinvin129.budgetmanager.ihm.IHM;
import fr.vinvin129.budgetmanager.ihm.views.controllers.create.spent.CreateSpentController;
import fr.vinvin129.budgetmanager.models.budget_logic.Budget;
import fr.vinvin129.budgetmanager.models.budget_logic.Category;
import fr.vinvin129.budgetmanager.models.budget_logic.StandardCategory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
public class ViewCategoryExpensesController extends Observer {
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
     * FXML reference to add a spent button
     */
    @FXML
    public Button addSpentButton;
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
     */
    @FXML
    public void addExpense() throws IOException {
        if (budget != null && category != null && category instanceof StandardCategory) {
            Stage addSpentStage = new Stage();
            FXMLLoader addSpentViewLoader = new FXMLLoader(IHM.class.getResource("createViews/spents/create-spent.fxml"));
            addSpentStage.setScene(new Scene(addSpentViewLoader.load()));
            CreateSpentController controller = addSpentViewLoader.getController();
            controller.setSpecificCategory(budget, (StandardCategory) category);
            addSpentStage.show();
        }
    }

    @Override
    protected void onEvent(EventT eventT) {
        if (eventT.equals(EventT.DATA_CHANGE)) {
            Platform.runLater(this::refresh);
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
     * refresh the view
     */
    private void refresh() {
        this.spentList.getChildren().setAll(Arrays.stream(this.category.getSpentList()).map(SpentView::new).toList());
    }

    /**
     * change the category to view
     * @param budget the {@link Budget} of category
     * @param category the {@link Category} object
     */
    public void setCategory(Budget budget, Category category) {
        if (this.budget != null) {
            removeObservable(this.budget);
        }
        if (Arrays.asList(budget.getCategories()).contains(category)) {
            this.budget = budget;
            addObservable(this.budget);
            this.category = category;
            this.name.setText("Catégorie " + this.category.getName());
            this.spentList.getChildren().clear();
            refresh();
        }
    }
}
