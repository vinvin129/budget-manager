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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.framework.junit5.Stop;
import org.testfx.matcher.control.ListViewMatchers;

import java.io.IOException;

@ExtendWith(ApplicationExtension.class)
public class CreateBudgetTest {

    static class IHMTest extends IHM {
        Budget getBudget() {
            return this.budget;
        }
    }
    IHM app;
    @Start
    private void start(Stage stage) throws IOException {
        app = new IHMTest();
        app.start(stage);

    }

    @Stop
    private void stop() throws Exception {
        app.stop();
    }

    @Test
    void createStandardCategory(FxRobot robot) throws IllegalCategorySizeException {
        robot.clickOn("#startButton");
        FXRobotCustom robotCustom = new FXRobotCustom(robot);

        robotCustom.clickOn("#budgetName");
        robotCustom.write("budget test");
        robotCustom.clickOn("#budgetAllocation");
        robotCustom.write("1000");

        robotCustom.clickOn("#addCategoryButton");
        robotCustom.changeToNewWindowCreated("categoryWindow");
        robotCustom.clickOn("#name");
        robotCustom.write("cat1");
        robotCustom.clickOn("#allocation");
        robotCustom.write("300");
        robotCustom.clickOn("#validateCategoryCreation");
        robotCustom.changeWindow("root");
        Category category = new StandardCategory("cat1", 300);
        FxAssert.verifyThat("#categoryList", ListViewMatchers.hasListCell(category));
    }



    @Test
    void createBudgetCategory(FxRobot robot) throws BudgetTooSmallException, IllegalCategorySizeException, IllegalBudgetSizeException {
        robot.clickOn("#startButton");
        FXRobotCustom robotCustom = new FXRobotCustom(robot);

        robotCustom.clickOn("#budgetName");
        robotCustom.write("budget test2");
        robotCustom.clickOn("#budgetAllocation");
        robotCustom.write("1000");

        robotCustom.clickOn("#addCategoryButton");
        robotCustom.changeToNewWindowCreated("category1Window");

        robotCustom.clickOn("#typeChoice");
        robot.type(KeyCode.DOWN);
        robot.type(KeyCode.ENTER);

        robotCustom.clickOn("#budgetName");
        robotCustom.write("bugcat1");
        robotCustom.clickOn("#budgetAllocation");
        robotCustom.write("300");

        robotCustom.clickOn("#addCategoryButton");
        robotCustom.changeToNewWindowCreated("category2Window");

        robotCustom.clickOn("#name");
        robotCustom.write("cat1");
        robotCustom.clickOn("#allocation");
        robotCustom.write("100");
        robotCustom.clickOn("#validateCategoryCreation");

        robotCustom.changeWindow("category1Window");
        robotCustom.clickOn("#validateCategoryCreation");

        robotCustom.changeWindow("root");
        Budget budget = new Budget("bugcat1", 300);
        budget.addCategory(new StandardCategory("cat1", 100));
        Category category = new BudgetCategory(budget);
        FxAssert.verifyThat("#categoryList", ListViewMatchers.hasListCell(category));
    }
}
