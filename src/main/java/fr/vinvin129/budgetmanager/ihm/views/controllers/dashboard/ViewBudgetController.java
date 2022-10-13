package fr.vinvin129.budgetmanager.ihm.views.controllers.dashboard;

import fr.vinvin129.budgetmanager.models.budget_logic.Budget;
import fr.vinvin129.budgetmanager.models.budget_logic.BudgetCategory;
import fr.vinvin129.budgetmanager.models.budget_logic.Category;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.Arrays;

/**
 * the controller to view a {@link fr.vinvin129.budgetmanager.models.budget_logic.Budget}
 * @author vinvin129
 */
public class ViewBudgetController {
    /**
     * FXML reference for the budget graph
     */
    @FXML
    public PieChart budgetGraph;
    /**
     * FXML reference for the choice mode
     */
    @FXML
    public ToggleGroup graphViewMode;
    /**
     * FXML reference for the view of graph
     */
    @FXML
    public AnchorPane graphView;
    /**
     * FXML reference for the budget name
     */
    @FXML
    public Label name;
    /**
     * the float label to show the {@link javafx.scene.chart.PieChart.Data} value
     * note : not in FXML file !
     */
    private final Label caption = new Label("");
    /**
     * budget showed
     */
    private Budget budget = null;

    /**
     * create a template when view is loaded
     */
    @FXML
    public void initialize() {
        caption.setTextFill(Color.BLACK);
        caption.setStyle("-fx-font: 12 arial;");
        PieChart.Data slice1 = new PieChart.Data("Cat 1 (Budget)", 17947195);
        PieChart.Data slice2 = new PieChart.Data("Cat 2 (Standard)", 11540278);
        PieChart.Data slice3 = new PieChart.Data("cat 3 (Standard)", 10982829);
        PieChart.Data slice4 = new PieChart.Data("cat 4 (Budget)", 4116242);
        PieChart.Data slice5 = new PieChart.Data("Libre", 28584442);

        budgetGraph.getData().add(slice1);
        budgetGraph.getData().add(slice2);
        budgetGraph.getData().add(slice3);
        budgetGraph.getData().add(slice4);
        budgetGraph.getData().add(slice5);

        budgetGraph.setLegendSide(Side.LEFT);
        graphView.getChildren().add(caption);

    }

    /**
     * set the budget to show
     * @param budget the {@link Budget} object
     */
    public void setBudget(Budget budget) {
        this.budget = budget;
        this.name.setText("Budget" + this.budget.getName());
        updateData();
    }

    /**
     * add listeners for mouse position
     * @param data the {@link javafx.scene.chart.PieChart.Data} object
     */
    private void addDataMouseListeners(PieChart.Data data) {
        data.getNode().setOnMouseMoved(mouseEvent -> {
            caption.setTranslateX(mouseEvent.getSceneX());
            caption.setTranslateY(mouseEvent.getSceneY());

            caption.setText(String.valueOf(data.getPieValue()));
        });
        data.getNode().setOnMouseExited(mouseEvent -> caption.setText(null));
    }

    /**
     * actualize the graph data with the budget attribute
     */
    public void updateData() {
        budgetGraph.getData().forEach(data -> {
            data.getNode().setOnMouseMoved(null);
            data.getNode().setOnMouseExited(null);
        });
        budgetGraph.getData().clear();
        if (this.budget != null) {
            Arrays.stream(this.budget.getCategories())
                    .forEach(category -> {
                        PieChart.Data data = createAllocationData(category);
                        budgetGraph.getData().add(data);
                        addDataMouseListeners(data);
                    });
            PieChart.Data freeData = new PieChart.Data("Libre", this.budget.getFreeAllocationPerMonth());
            budgetGraph.getData().add(freeData);
            addDataMouseListeners(freeData);
        }
    }

    /**
     * create a new {@link javafx.scene.chart.PieChart.Data} for a {@link Category}
     * @param category the {@link Category} object
     * @return a {@link javafx.scene.chart.PieChart.Data} object
     */
    private PieChart.Data createAllocationData(Category category) {
        String type = category instanceof BudgetCategory ? "Budget" : "Standard";
        String s = category.getName() + " (" + type + ")";
        double v = category.getAllocationPerMonth();
        return new PieChart.Data(s, v);
    }
}
