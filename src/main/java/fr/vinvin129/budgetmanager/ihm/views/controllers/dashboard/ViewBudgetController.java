package fr.vinvin129.budgetmanager.ihm.views.controllers.dashboard;

import fr.vinvin129.budgetmanager.budgetLogic.budgets.Budget;
import fr.vinvin129.budgetmanager.budgetLogic.budgets.BudgetController;
import fr.vinvin129.budgetmanager.budgetLogic.categories.BudgetCategory;
import fr.vinvin129.budgetmanager.budgetLogic.categories.Category;
import fr.vinvin129.budgetmanager.budgetLogic.categories.CategoryController;
import fr.vinvin129.budgetmanager.budgetLogic.history.History;
import fr.vinvin129.budgetmanager.events.EventT;
import fr.vinvin129.budgetmanager.events.Observer;
import fr.vinvin129.budgetmanager.ihm.IHM;
import fr.vinvin129.budgetmanager.ihm.views.controllers.create.spent.CreateSpentController;
import fr.vinvin129.budgetmanager.ihm.views.stages.ViewCategoryExpensesStage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
 * the controller to view a {@link Budget}
 * @author vinvin129
 */
public class ViewBudgetController extends Observer {
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
     * FXML reference for the balance label
     */
    @FXML
    public Label balanceLabel;
    /**
     * FXML reference for the allocation per month label
     */
    @FXML
    public Label allocationPerMonthLabel;
    /**
     * controller of budget showed
     */
    private BudgetController budgetController = null;
    /**
     * the list of {@link Category} with their linked {@link javafx.scene.chart.PieChart.Data}
     */
    private final Map<PieChart.Data, Category> dataCategoryMap = new HashMap<>();
    private ViewMode viewMode = ViewMode.ALLOCATION;

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
    public void viewModeChanged() {
        Toggle toggle = this.graphViewMode.getSelectedToggle();
        if (toggle == allocationViewMode) {
            this.viewMode = ViewMode.ALLOCATION;
        } else if (toggle == expenseViewMode) {
            this.viewMode = ViewMode.EXPENSES;
        }
        refresh();
    }

    /**
     * on click on add spent button
     */
    @FXML
    public void addSpent() throws IOException {
        Stage addSpentStage = new Stage();
        FXMLLoader addSpentViewLoader = new FXMLLoader(IHM.class.getResource("createViews/spents/create-spent.fxml"));
        addSpentStage.setScene(new Scene(addSpentViewLoader.load()));
        CreateSpentController controller = addSpentViewLoader.getController();
        controller.setBudgetRoot(this.budgetController.getModel());
        addSpentStage.show();
    }

    @Override
    protected void onEvent(EventT eventT) {
        Platform.runLater(() -> {
            if (eventT.equals(EventT.DATA_CHANGE)) {
                refresh();
            } else if (eventT.equals(EventT.HISTORY_MONTH_CHANGE)) {
                this.addSpentButton.setDisable(History.INSTANCE.hasNext());
            }
        });

    }

    /**
     * update all elements in the view
     */
    public void refresh() {
        this.name.setText("Budget " + this.budgetController.getModel().getName());
        this.balanceLabel.setText("Solde : " + this.budgetController.getModel().getBalance());
        this.allocationPerMonthLabel
                .setText("Allocation par mois : " + this.budgetController.getModel().getAllocationPerMonth());
        createNewChart();
        updateData();
    }

    private void createNewChart() {
        graphView.getChildren().clear();
        this.budgetGraph = new PieChart();
        graphView.getChildren().add(this.budgetGraph);
        this.budgetGraph.setId("budgetGraph");
        budgetGraph.setLegendVisible(false);
        AnchorPane.setTopAnchor(budgetGraph, 0.0);
        AnchorPane.setBottomAnchor(budgetGraph, 0.0);
        AnchorPane.setRightAnchor(budgetGraph, 0.0);
        AnchorPane.setLeftAnchor(budgetGraph, 0.0);
        graphView.getChildren().add(caption);
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
        budgetGraph.setLegendVisible(false);
        graphView.getChildren().add(caption);
        this.addObservable(History.INSTANCE);
    }

    /**
     * set the controller of the budget to show
     * @param budgetController the {@link BudgetController} object
     */
    public void setBudgetController(BudgetController budgetController) {
        if (this.budgetController != null) {
            removeObservable(this.budgetController);
        }
        this.budgetController = budgetController;
        addObservable(this.budgetController);
        refresh();
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
                if (category instanceof BudgetCategory budgetCategory) {
                    FXMLLoader budgetViewLoader = new FXMLLoader(IHM.class.getResource("dashboard/view-budget.fxml"));
                    Scene budgetViewScene;
                    try {
                        budgetViewScene = new Scene(budgetViewLoader.load(), 800, 500);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    ViewBudgetController budgetViewController = budgetViewLoader.getController();
                    budgetViewController.setBudgetController(budgetCategory.getBudgetController());
                    Stage budgetViewStage = new Stage();
                    budgetViewStage.setScene(budgetViewScene);
                    budgetViewStage.show();
                } else {
                    try {
                        new ViewCategoryExpensesStage(category.getController()).show();
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
     */
    public void updateData() {
        budgetGraph.getData().forEach(data -> {
            data.getNode().setOnMouseMoved(null);
            data.getNode().setOnMouseExited(null);
        });
        budgetGraph.getData().clear();
        this.dataCategoryMap.clear();
        if (this.budgetController != null) {
            Arrays.stream(this.budgetController.getModel().getCategoryControllers())
                    .map(CategoryController::getModel)
                    .forEach(category -> {
                        PieChart.Data data = viewMode == ViewMode.ALLOCATION ? createAllocationData(category) : createExpensesData(category);
                        budgetGraph.getData().add(data);
                        this.dataCategoryMap.put(data, category);
                        addDataMouseListeners(data);
                    });
            if (this.viewMode == ViewMode.ALLOCATION) {
                PieChart.Data freeData = new PieChart.Data("Libre", this.budgetController.getModel().getFreeAllocationPerMonth());
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
