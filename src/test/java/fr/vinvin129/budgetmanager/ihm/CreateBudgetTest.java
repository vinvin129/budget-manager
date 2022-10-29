package fr.vinvin129.budgetmanager.ihm;

import fr.vinvin129.budgetmanager.exceptions.BudgetTooSmallException;
import fr.vinvin129.budgetmanager.exceptions.IllegalBudgetSizeException;
import fr.vinvin129.budgetmanager.exceptions.IllegalCategorySizeException;
import fr.vinvin129.budgetmanager.models.budget_logic.Budget;
import fr.vinvin129.budgetmanager.models.budget_logic.BudgetCategory;
import fr.vinvin129.budgetmanager.models.budget_logic.Category;
import fr.vinvin129.budgetmanager.models.budget_logic.StandardCategory;
import javafx.scene.input.KeyCode;
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

    static void createBudget(FXRobotCustom robotCustom, String windowName, Budget budget, boolean main) {
        robotCustom.clickOn("#budgetName");
        robotCustom.write(budget.getName());
        robotCustom.clickOn("#budgetAllocation");
        robotCustom.write(String.valueOf(budget.getAllocationPerMonth()));

        for (Category category : budget.getCategories()) {
            robotCustom.clickOn("#addCategoryButton");
            String catNameBot = "category-" + ++windowNb + "Window";
            robotCustom.changeToNewWindowCreated(catNameBot);
            if (category instanceof BudgetCategory) {
                robotCustom.clickOn("#typeChoice");
                robotCustom.type(KeyCode.DOWN);
                robotCustom.type(KeyCode.ENTER);

                createBudget(robotCustom, catNameBot, ((BudgetCategory) category).getBudget(), false);
            } else {
                robotCustom.clickOn("#name");
                robotCustom.write(category.getName());
                robotCustom.clickOn("#allocation");
                robotCustom.write(String.valueOf(category.getAllocationPerMonth()));
            }
            robotCustom.clickOn("#validateCategoryCreation");
            robotCustom.removeWindow(catNameBot);

            robotCustom.changeWindow(windowName);
        }
        if (main) {
            robotCustom.clickOn("#validateBudgetCreation");
        }
        budget.newMonth();
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
        createBudget(robotCustom, "root", b, true);
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
        createBudget(robotCustom, "root", b, true);
        Assertions.assertEquals(b, app.budget);
    }
}
