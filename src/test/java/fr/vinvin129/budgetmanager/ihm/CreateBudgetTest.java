package fr.vinvin129.budgetmanager.ihm;

import fr.vinvin129.budgetmanager.exceptions.*;
import fr.vinvin129.budgetmanager.models.budget_logic.Budget;
import fr.vinvin129.budgetmanager.models.budget_logic.BudgetCategory;
import fr.vinvin129.budgetmanager.models.budget_logic.Category;
import fr.vinvin129.budgetmanager.models.budget_logic.StandardCategory;
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

    static void createBudget(FXRobotCustom robotCustom, String windowName, Budget budget, boolean main, boolean clear) {
        robotCustom.clickOn("#budgetName");
        if (clear) {
            robotCustom.clearTextField("#budgetName");
        }
        robotCustom.write(budget.getName());
        robotCustom.clickOn("#budgetAllocation");
        if (clear) {
            robotCustom.clearTextField("#budgetAllocation");
        }
        robotCustom.write(String.valueOf(budget.getAllocationPerMonth()));

        for (Category category : budget.getCategories()) {
            robotCustom.clickOn("#addCategoryButton");
            String catNameBot = "category-" + ++windowNb + "Window";
            robotCustom.changeToNewWindowCreated(catNameBot);
            createCategory(robotCustom, catNameBot, category, clear);
            robotCustom.removeWindow(catNameBot);

            robotCustom.changeWindow(windowName);
        }
        if (main) {
            robotCustom.clickOn("#validateBudgetCreation");
        }
        budget.newMonth();
    }

    static void createCategory(FXRobotCustom robotCustom, String windowName, Category category, boolean clear) {
        if (category instanceof BudgetCategory) {
            robotCustom.selectItem("#typeChoice", "Budget");
            robotCustom.changeWindow(windowName);
            createBudget(robotCustom, windowName, ((BudgetCategory) category).getBudget(), false, clear);
        } else {
            robotCustom.selectItem("#typeChoice", "Standard");
            robotCustom.changeWindow(windowName);
            robotCustom.clickOn("#name");
            if (clear) {
                robotCustom.clearTextField("#name");
            }
            robotCustom.write(category.getName());
            robotCustom.clickOn("#allocation");
            if (clear) {
                robotCustom.clearTextField("#allocation");
            }
            robotCustom.write(String.valueOf(category.getAllocationPerMonth()));
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
    void createStandardCategory(FxRobot robot) throws IllegalCategorySizeException, IllegalBudgetSizeException, BudgetTooSmallException {
        robot.clickOn("#startButton");
        Budget b = new Budget("budget test", 1000);
        b.addCategory(new StandardCategory("cat1", 300));
        FXRobotCustom robotCustom = new FXRobotCustom(robot);
        createBudget(robotCustom, "root", b, true, false);
        Assertions.assertEquals(b, app.budget);
    }

    @Test
    void createBudgetCategory(FxRobot robot) throws BudgetTooSmallException, IllegalCategorySizeException, IllegalBudgetSizeException {
        robot.clickOn("#startButton");
        Budget b = new Budget("budget test2", 1000);
        Budget b2 = new Budget("bugcat1", 300);
        b2.addCategory(new StandardCategory("cat1", 100));
        b.addCategory(new BudgetCategory(b2));
        FXRobotCustom robotCustom = new FXRobotCustom(robot);
        createBudget(robotCustom, "root", b, true, false);
        Assertions.assertEquals(b, app.budget);
    }

    @Test
    void updateCreatedCategory(FxRobot robot) throws IllegalBudgetSizeException,
            IllegalCategorySizeException,
            BudgetTooSmallException,
            BudgetCategoryTooSmallException,
            CategoryTooBigException {
        robot.clickOn("#startButton");
        Budget b = new Budget("budget test2", 1000);
        Budget b2 = new Budget("bugcat1", 300);
        StandardCategory cat1 = new StandardCategory("cat1", 100);
        BudgetCategory bugcat1 = new BudgetCategory(b2);
        b2.addCategory(cat1);
        b.addCategory(bugcat1);
        FXRobotCustom robotCustom = new FXRobotCustom(robot);

        robotCustom.clickOn("#addCategoryButton");
        String catNameBot = "cat1Window";
        robotCustom.changeToNewWindowCreated(catNameBot);
        createCategory(robotCustom, catNameBot, cat1, false);
        robotCustom.removeWindow(catNameBot);
        robotCustom.changeWindow("root");

        robotCustom.selectListItem("#categoryList", (Category) cat1);
        robot.sleep(1000);
        robotCustom.changeToNewWindowCreated("update cat1");
        b.setAllocationPerMonthOfCategory(cat1, 50);
        createCategory(robotCustom, "update cat1", cat1, true);
    }
}
