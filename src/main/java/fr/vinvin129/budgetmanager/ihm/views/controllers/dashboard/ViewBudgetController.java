package fr.vinvin129.budgetmanager.ihm.views.controllers.dashboard;

import fr.vinvin129.budgetmanager.ihm.IHM;
import fr.vinvin129.budgetmanager.ihm.views.controllers.create.spent.CreateSpentController;
import fr.vinvin129.budgetmanager.ihm.views.stages.ViewCategoryExpensesStage;
import fr.vinvin129.budgetmanager.models.budget_logic.Budget;
import fr.vinvin129.budgetmanager.models.budget_logic.BudgetCategory;
import fr.vinvin129.budgetmanager.models.budget_logic.Category;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
     * the toggle button to show expense view
     */
    @FXML
    public ToggleButton expenseViewMode;
    /**
     * the toggle button to show allocation view
     */
    @FXML
    public ToggleButton allocationViewMode;
    /**
     * FXML reference for the add spent button
     */
    @FXML
    public Button addSpentButton;
    /**
     * budget showed
     */
    private Budget budget = null;
    /**
     * the list of {@link Category} with their linked {@link javafx.scene.chart.PieChart.Data}
     */
    private final Map<PieChart.Data, Category> dataCategoryMap = new HashMap<>();

    /**
     * get a {@link String} for the name of {@link javafx.scene.chart.PieChart.Data} object of a {@link Category}
     * @param category the {@link Category} object
     * @return a {@link String} like "categoryName (categoryType)"
     */
    public static String getCategoryChartDataName(Category category) {
        String type = category instanceof BudgetCategory ? "Budget" : "Standard";
        return category.getName() + " (" + type + ")";
    }

    @FXML
    public void viewModeChanged(ActionEvent actionEvent) {
        Toggle toggle = this.graphViewMode.getSelectedToggle();
        if (toggle == allocationViewMode) {
            updateData(ViewMode.ALLOCATION);
        } else if (toggle == expenseViewMode) {
            updateData(ViewMode.EXPENSES);
        }
    }

    /**
     * on click on add spent button
     * @param actionEvent the event
     */
    @FXML
    public void addSpent(ActionEvent actionEvent) throws IOException {
        Stage addSpentStage = new Stage();
        FXMLLoader addSpentViewLoader = new FXMLLoader(IHM.class.getResource("createViews/spents/create-spent.fxml"));
        addSpentStage.setScene(new Scene(addSpentViewLoader.load()));
        CreateSpentController controller = addSpentViewLoader.getController();
        controller.setBudgetRoot(this.budget);
        addSpentStage.show();
    }

    /**
     * choice mode
     */
    public enum ViewMode {
        ALLOCATION,
        EXPENSES,
    }

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
        this.name.setText("Budget " + this.budget.getName());
        updateData(ViewMode.ALLOCATION);
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

        data.getNode().setOnMouseClicked(mouseEvent -> {
            Category category = this.dataCategoryMap.get(data);
            if (category != null) {
                if (category instanceof BudgetCategory) {
                    FXMLLoader budgetViewLoader = new FXMLLoader(IHM.class.getResource("dashboard/view-budget.fxml"));
                    Scene budgetViewScene;
                    try {
                        budgetViewScene = new Scene(budgetViewLoader.load(), 800, 500);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    ViewBudgetController budgetViewController = budgetViewLoader.getController();
                    budgetViewController.setBudget(((BudgetCategory) category).getBudget());
                    Stage budgetViewStage = new Stage();
                    budgetViewStage.setScene(budgetViewScene);
                    budgetViewStage.show();
                } else {
                    try {
                        new ViewCategoryExpensesStage(budget, category).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println(category);
            }
        });
    }

    /**
     * actualize the graph data with the budget attribute
     * @param viewMode the selected mode
     */
    public void updateData(ViewMode viewMode) {
        budgetGraph.getData().forEach(data -> {
            data.getNode().setOnMouseMoved(null);
            data.getNode().setOnMouseExited(null);
        });
        budgetGraph.getData().clear();
        this.dataCategoryMap.clear();
        if (this.budget != null) {
            Arrays.stream(this.budget.getCategories())
                    .forEach(category -> {
                        PieChart.Data data = viewMode == ViewMode.ALLOCATION ? createAllocationData(category) : createExpensesData(category);
                        budgetGraph.getData().add(data);
                        this.dataCategoryMap.put(data, category);
                        addDataMouseListeners(data);
                    });
            if (viewMode == ViewMode.ALLOCATION) {
                PieChart.Data freeData = new PieChart.Data("Libre", this.budget.getFreeAllocationPerMonth());
                budgetGraph.getData().add(freeData);
                addDataMouseListeners(freeData);
            }
        }
    }

    /**
     * create a new {@link javafx.scene.chart.PieChart.Data} for a {@link Category} with allocation
     * @param category the {@link Category} object
     * @return a {@link javafx.scene.chart.PieChart.Data} object
     */
    private PieChart.Data createAllocationData(Category category) {
        String s = getCategoryChartDataName(category);
        double v = category.getAllocationPerMonth();
        return new PieChart.Data(s, v);
    }

    /**
     * create a new {@link javafx.scene.chart.PieChart.Data} for a {@link Category} with expenses
     * @param category the {@link Category} object
     * @return a {@link javafx.scene.chart.PieChart.Data} object
     */
    private PieChart.Data createExpensesData(Category category) {
        String s = getCategoryChartDataName(category);
        double v = category.getAmountSpent();
        return new PieChart.Data(s, v);
    }
}
