package fr.vinvin129.budgetmanager.ihm;

import fr.vinvin129.budgetmanager.models.budget_logic.Budget;
import fr.vinvin129.budgetmanager.models.budget_logic.BudgetCategory;
import fr.vinvin129.budgetmanager.models.budget_logic.Category;
import fr.vinvin129.budgetmanager.models.budget_logic.StandardCategory;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.api.FxRobotContext;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.framework.junit5.Stop;
import org.testfx.matcher.control.ListViewMatchers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        robot.clickOn("#validateCategoryCreation");
        Category category = new StandardCategory("cat1", 300);
        FxAssert.verifyThat("#categoryList", ListViewMatchers.hasListCell(category));
    }



    @Test
    void createBudgetCategory(FxRobot robot) {
        List<Window> windows = new ArrayList<>();
        FxRobotContext context = robot.robotContext();
        toCreateBudget(robot);
        robot.clickOn("#budgetName");
        robot.write("budget test2");
        robot.clickOn("#budgetAllocation");
        robot.write("1000");

        Window window1 = context.getWindowFinder().targetWindow();
        windows.add(window1);

        robot.clickOn("#addCategoryButton");
        Window window2 = context.getWindowFinder().listTargetWindows().stream().filter(window -> !windows.contains(window)).findFirst().orElseThrow();
        windows.add(window2);

        Node actualNode = context.getNodeFinder().rootNode(window2);
        robot.clickOn(actualNode.lookup("#typeChoice"));
        robot.type(KeyCode.DOWN);
        robot.type(KeyCode.ENTER);

        robot.clickOn(actualNode.lookup("#budgetName"));
        //robot.clickOn(robot.robotContext().getNodeFinder().lookup("#budgetName").queryAll().toArray(new javafx.scene.Node[0])[1]);
        robot.write("bugcat1");
        robot.clickOn(actualNode.lookup("#budgetAllocation"));
        //robot.clickOn(robot.robotContext().getNodeFinder().lookup("#budgetAllocation").queryAll().toArray(new javafx.scene.Node[0])[1]);
        robot.write("300");


        robot.clickOn(actualNode.lookup("#addCategoryButton"));
        Window window3 = context.getWindowFinder().listTargetWindows().stream().filter(window -> !windows.contains(window)).findFirst().orElseThrow();
        windows.add(window3);
        actualNode = context.getNodeFinder().rootNode(window3);
        robot.clickOn(actualNode.lookup("#name"));
        robot.write("cat1");
        robot.clickOn(actualNode.lookup("#allocation"));
        robot.write("100");
        robot.clickOn(actualNode.lookup("#validateCategoryCreation"));

        actualNode = context.getNodeFinder().rootNode(window2);
        robot.clickOn(actualNode.lookup("#validateCategoryCreation"));

        // actualNode = context.getNodeFinder().rootNode(window1);
        Budget budget = new Budget("bugcat1", 300);
        budget.addCategory(new StandardCategory("cat1", 100));
        Category category = new BudgetCategory(budget);
        FxAssert.verifyThat("#categoryList", ListViewMatchers.hasListCell(category));
    }
}
