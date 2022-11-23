package fr.vinvin129.budgetmanager.ihm;

import fr.vinvin129.budgetmanager.budgetLogic.budgets.BudgetController;
import fr.vinvin129.budgetmanager.budgetLogic.categories.BudgetCategory;
import fr.vinvin129.budgetmanager.budgetLogic.categories.CategoryController;
import fr.vinvin129.budgetmanager.budgetLogic.moments.BudgetMoment;
import fr.vinvin129.budgetmanager.budgetLogic.moments.CategoryMoment;
import fr.vinvin129.budgetmanager.exceptions.IllegalBudgetSizeException;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.framework.junit5.Stop;

import java.io.IOException;

@ExtendWith(ApplicationExtension.class)
public class CreateBudgetTest {
    static int windowNb = 0;

    static void createBudget(FXRobotCustom robotCustom, String windowName, BudgetController budgetController, boolean main, boolean clear) {
        robotCustom.clickOn("#budgetName");
        if (clear) {
            robotCustom.clearTextField("#budgetName");
        }
        robotCustom.write(budgetController.getModel().getName());
        robotCustom.clickOn("#budgetAllocation");
        if (clear) {
            robotCustom.clearTextField("#budgetAllocation");
        }
        robotCustom.write(String.valueOf(budgetController.getModel().getAllocationPerMonth()));

        for (CategoryController categoryController : budgetController.getModel().getCategoryControllers()) {
            robotCustom.clickOn("#addCategoryButton");
            String catNameBot = "category-" + ++windowNb + "Window";
            robotCustom.changeToNewWindowCreated(catNameBot);
            createCategory(robotCustom, catNameBot, categoryController, clear);
            robotCustom.removeWindow(catNameBot);

            robotCustom.changeWindow(windowName);
        }
        if (main) {
            robotCustom.clickOn("#validateBudgetCreation");
            budgetController.newMonth();
        }
    }

    static void createCategory(FXRobotCustom robotCustom, String windowName, CategoryController categoryController, boolean clear) {
        if (categoryController.getModel() instanceof BudgetCategory budgetCategory) {
            robotCustom.selectItem("#typeChoice", "Budget");
            robotCustom.changeWindow(windowName);
            createBudget(robotCustom, windowName, budgetCategory.getBudgetController(), false, clear);
        } else {
            robotCustom.selectItem("#typeChoice", "Standard");
            robotCustom.changeWindow(windowName);
            robotCustom.clickOn("#name");
            if (clear) {
                robotCustom.clearTextField("#name");
            }
            robotCustom.write(categoryController.getModel().getName());
            robotCustom.clickOn("#allocation");
            if (clear) {
                robotCustom.clearTextField("#allocation");
            }
            robotCustom.write(String.valueOf(categoryController.getModel().getAllocationPerMonth()));
        }
        robotCustom.clickOn("#validateCategoryCreation");
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
    void createStandardCategory(FxRobot robot) {
        robot.clickOn("#startButton");
        BudgetController b;
        try {
            b = new BudgetController(new BudgetMoment(
                    "budget test",
                    1000,
                    0,
                    new CategoryMoment[]{CategoryMoment.create("cat1", 300)}
            ));
        } catch (IllegalBudgetSizeException e) {
            throw new RuntimeException(e);
        }
        FXRobotCustom robotCustom = new FXRobotCustom(robot);
        createBudget(robotCustom, "root", b, true, false);
        Assertions.assertEquals(b.getModel().getMoment(), app.budgetController.getModel().getMoment());
    }

    @Test
    void createBudgetCategory(FxRobot robot) {
        robot.clickOn("#startButton");
        BudgetController b;
        try {
            b = new BudgetController(new BudgetMoment(
                    "budget test2",
                    1000,
                    0,
                    new CategoryMoment[]{
                            CategoryMoment.create(
                                    new BudgetMoment("budget test2", 1000, 0,
                                            new CategoryMoment[]{CategoryMoment.create("cat1", 100)}
                                    )
                            )
                    }));
        } catch (IllegalBudgetSizeException e) {
            throw new RuntimeException(e);
        }
        FXRobotCustom robotCustom = new FXRobotCustom(robot);
        createBudget(robotCustom, "root", b, true, false);
        Assertions.assertEquals(b.getModel().getMoment(), app.budgetController.getModel().getMoment());
    }

    @Test
    void updateCreatedCategory(FxRobot robot) {
        robot.clickOn("#startButton");
        try {
            BudgetController b = new BudgetController(BudgetMoment.create("budget test2", 1000));
            CategoryController bugcat1 = b.addCategory(CategoryMoment.create(BudgetMoment.create("bugcat1", 300)));
            CategoryController cat1 = ((BudgetCategory) bugcat1.getModel())
                    .getBudgetController().addCategory(CategoryMoment.create("cat1", 100));
            FXRobotCustom robotCustom = new FXRobotCustom(robot);

            robotCustom.clickOn("#budgetName");
            robotCustom.write(b.getModel().getName());
            robotCustom.clickOn("#budgetAllocation");
            robotCustom.write(String.valueOf(b.getModel().getAllocationPerMonth()));

            robotCustom.clickOn("#addCategoryButton");
            String catNameBot = "cat1Window";
            robotCustom.changeToNewWindowCreated(catNameBot);
            createCategory(robotCustom, catNameBot, cat1, false);
            robotCustom.removeWindow(catNameBot);
            robotCustom.changeWindow("root");

            robotCustom.selectListItem("#categoryList", cat1.getModel());
            robot.sleep(1000);
            robotCustom.changeToNewWindowCreated("update cat1");
            cat1.setAllocationPerMonth(50);
            createCategory(robotCustom, "update cat1", cat1, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
