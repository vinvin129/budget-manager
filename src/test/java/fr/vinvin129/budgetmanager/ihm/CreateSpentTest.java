package fr.vinvin129.budgetmanager.ihm;

import fr.vinvin129.budgetmanager.budgetLogic.Spent;
import fr.vinvin129.budgetmanager.budgetLogic.budgets.BudgetController;
import fr.vinvin129.budgetmanager.budgetLogic.categories.BudgetCategory;
import fr.vinvin129.budgetmanager.budgetLogic.categories.CategoryController;
import fr.vinvin129.budgetmanager.budgetLogic.moments.BudgetMoment;
import fr.vinvin129.budgetmanager.budgetLogic.moments.CategoryMoment;
import fr.vinvin129.budgetmanager.exceptions.BudgetTooSmallException;
import fr.vinvin129.budgetmanager.exceptions.IllegalBudgetSizeException;
import fr.vinvin129.budgetmanager.exceptions.IllegalCategorySizeException;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.framework.junit5.Stop;

import java.io.IOException;
import java.util.Arrays;

@ExtendWith(ApplicationExtension.class)
public class CreateSpentTest {

    static void addSpent(FXRobotCustom robotCustom, CategoryController categoryController, Spent spent, boolean selectItem) {
        robotCustom.clickOn("#titleTextField");
        robotCustom.write(spent.label());
        robotCustom.clickOn("#priceTextField");
        robotCustom.write(String.valueOf(spent.price()));
        if (selectItem) {
            Assertions.assertTrue(
                    robotCustom.selectItem("#budgetChoiceBox", categoryController
                            .getBudgetParentController()
                            .getModel()));
            Assertions.assertTrue(
                    robotCustom.selectItem("#categoryChoiceBox", categoryController.getModel()));
        }
        robotCustom.clickOn("#validate");
        categoryController.addSpent(spent);
        Assertions.assertTrue(Arrays.asList(categoryController.getModel().getSpentList()).contains(spent));
    }

    IHM app;
    @Start
    private void start(Stage stage) throws IOException {
        app = new IHM();
        app.start(stage);

    }

    @Stop
    private void stop() throws Exception {
        app.stop();
    }

    @Test
    void createSpentFromDashboard(FxRobot robot) throws IllegalBudgetSizeException, IllegalCategorySizeException, BudgetTooSmallException {
        robot.clickOn("#startButton");
        FXRobotCustom robotCustom = new FXRobotCustom(robot);
        BudgetController b = new BudgetController(BudgetMoment.create("principal", 1000));
        CategoryController toto = b.addCategory(CategoryMoment.create("toto", 300));
        CategoryController tata = b.addCategory(CategoryMoment.create(
                BudgetMoment.create("budget2", 300)
        ));
        CategoryController bite = ((BudgetCategory)tata.getModel())
                .getBudgetController()
                .addCategory(CategoryMoment.create("bite", 200));
        CreateBudgetTest.createBudget(robotCustom, "root", b, true, false);
        robotCustom.changeWindow("root");

        robotCustom.clickOn("#addSpentButton");
        robotCustom.changeToNewWindowCreated("newSpentWindow");
        CategoryController realToto = Arrays.stream(app.budgetController.getModel().getCategoryControllers())
                .filter(categoryController -> categoryController.getModel().getMoment().equals(toto.getModel().getMoment()))
                .findFirst().orElseThrow();
        BudgetController realTata = ((BudgetCategory) Arrays.stream(app.budgetController.getModel().getCategoryControllers())
                .filter(categoryController -> categoryController.getModel().getMoment().equals(tata.getModel().getMoment()))
                .findFirst().orElseThrow().getModel()).getBudgetController();
        CategoryController realBite = Arrays.stream(realTata.getModel().getCategoryControllers())
                .filter(categoryController -> categoryController.getModel().getMoment().equals(bite.getModel().getMoment()))
                .findFirst().orElseThrow();

        addSpent(robotCustom, realToto, new Spent("une dépense", 30, null), true);
        robotCustom.removeWindow("newSpentWindow");
        robotCustom.changeWindow("root");

        robotCustom.clickOn("#addSpentButton");
        robotCustom.changeToNewWindowCreated("newSpentWindow");
        addSpent(robotCustom, realBite, new Spent("une autre dépense", 50, null), true);
        robotCustom.removeWindow("newSpentWindow");
        robotCustom.changeWindow("root");
    }

    @Test
    void createSpentFromCategorySpentList(FxRobot robot) throws IllegalBudgetSizeException, IllegalCategorySizeException, BudgetTooSmallException {
        robot.clickOn("#startButton");
        FXRobotCustom robotCustom = new FXRobotCustom(robot);
        BudgetController b = new BudgetController(BudgetMoment.create("principal", 1000));
        CategoryController toto = b.addCategory(CategoryMoment.create("toto", 300));
        CategoryController tata = b.addCategory(CategoryMoment.create(
                BudgetMoment.create("budget2", 300)
        ));
        CategoryController bite = ((BudgetCategory)tata.getModel())
                .getBudgetController()
                .addCategory(CategoryMoment.create("bite", 200));
        CreateBudgetTest.createBudget(robotCustom, "root", b, true, false);
        robotCustom.changeWindow("root");
        DashbordTest.clickOnChartCategory(robotCustom, toto);

        robotCustom.changeToNewWindowCreated("expensesCategoryView");
        robotCustom.clickOn("#addSpentButton");
        robotCustom.changeToNewWindowCreated("newSpentWindow");
        addSpent(robotCustom, toto, new Spent("une dépense", 30.54, null), false);
        robotCustom.removeWindow("newSpentWindow");
        robotCustom.changeWindow("expensesCategoryView");

        robotCustom.clickOn("#addSpentButton");
        robotCustom.changeToNewWindowCreated("newSpentWindow");
        addSpent(robotCustom, bite, new Spent("une autre dépense", 50.99, null), false);
        robotCustom.removeWindow("newSpentWindow");
        robotCustom.changeWindow("expensesCategoryView");
    }
}
