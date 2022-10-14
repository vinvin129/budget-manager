package fr.vinvin129.budgetmanager.ihm.views.controllers.dashboard;

import fr.vinvin129.budgetmanager.Spent;
import fr.vinvin129.budgetmanager.models.budget_logic.Category;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

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
     * a customised {@link AnchorPane} for show {@link Spent}
     */
    private static class SpentView extends AnchorPane {
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
     * @param category the {@link Category} object
     */
    public void setCategory(Category category) {
        this.category = category;
        this.name.setText("Catégorie " +this.category.getName());
        this.spentList.getChildren().clear();
        Arrays.stream(this.category.getSpentList())
                .forEach(spent -> this.spentList.getChildren().add(new SpentView(spent)));
    }
}
