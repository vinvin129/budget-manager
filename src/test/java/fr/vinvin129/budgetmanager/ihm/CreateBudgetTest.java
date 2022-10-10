package fr.vinvin129.budgetmanager.ihm;

import fr.vinvin129.budgetmanager.models.budget_logic.Category;
import fr.vinvin129.budgetmanager.models.budget_logic.StandardCategory;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.ListViewMatchers;

import java.io.IOException;

@ExtendWith(ApplicationExtension.class)
public class CreateBudgetTest {
    @Start
    private void start(Stage stage) throws IOException {
        IHM app = new IHM();
        app.start(stage);
    }

    void toCreateBudget(FxRobot robot) {
        robot.clickOn("#startButton");
    }

    void createBudget(FxRobot robot, String name, String allocation) {
        toCreateBudget(robot);
        robot.clickOn("#budgetName");
        robot.write(name);
        robot.clickOn("#budgetAllocation");
        robot.write(allocation);
    }

    @Test
    void createStandardCategory(FxRobot robot) {
        createBudget(robot, "budget test", "1000");
        robot.clickOn("#addCategoryButton");
        robot.clickOn("#name");
        robot.write("cat1");
        robot.clickOn("#allocation");
        robot.write("300");
        robot.clickOn("#validate");
        Category category = new StandardCategory("cat1", 300);
        FxAssert.verifyThat("#categoryList", ListViewMatchers.hasListCell(category));
    }
}
