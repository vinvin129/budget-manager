package fr.vinvin129.budgetmanager.ihm;

import fr.vinvin129.budgetmanager.Spent;
import fr.vinvin129.budgetmanager.exceptions.BudgetNotContainCategoryException;
import fr.vinvin129.budgetmanager.exceptions.BudgetTooSmallException;
import fr.vinvin129.budgetmanager.exceptions.IllegalBudgetSizeException;
import fr.vinvin129.budgetmanager.exceptions.IllegalCategorySizeException;
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
import java.util.Arrays;

@ExtendWith(ApplicationExtension.class)
public class CreateSpentTest {

    static void addSpent(FXRobotCustom robotCustom, Budget budget, Spent spent, boolean selectItem) throws BudgetNotContainCategoryException {
        robotCustom.clickOn("#titleTextField");
        robotCustom.write(spent.label());
        robotCustom.clickOn("#priceTextField");
        robotCustom.write(String.valueOf(spent.price()));
        if (selectItem) {
            Assertions.assertTrue(robotCustom.selectItem("#budgetChoiceBox", budget));
            Assertions.assertTrue(robotCustom.selectItem("#categoryChoiceBox", spent.category()));
        }
        robotCustom.clickOn("#validate");
        budget.addSpent(spent);
        Assertions.assertTrue(Arrays.asList(spent.category().getSpentList()).contains(spent));
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
    void createSpentFromDashboard(FxRobot robot) throws IllegalBudgetSizeException, IllegalCategorySizeException, BudgetTooSmallException, BudgetNotContainCategoryException {
        robot.clickOn("#startButton");
        FXRobotCustom robotCustom = new FXRobotCustom(robot);
        Budget b = new Budget("principal", 1000);
        Budget b2 = new Budget("budget2", 300);
        Category bite = new StandardCategory("bite", 200);
        Category toto = new StandardCategory("toto", 300);
        Category tata = new BudgetCategory(b2);
        b2.addCategory(bite);
        b.addCategory(toto);
        b.addCategory(tata);
        CreateBudgetTest.createBudget(robotCustom, "root", b, true, false);
        robotCustom.changeWindow("root");

        robotCustom.clickOn("#addSpentButton");
        robotCustom.changeToNewWindowCreated("newSpentWindow");
        Spent spent1 = new Spent(toto, "une dépense", 30, null);
        addSpent(robotCustom, b, spent1, true);
        robotCustom.removeWindow("newSpentWindow");
        robotCustom.changeWindow("root");

        robotCustom.clickOn("#addSpentButton");
        robotCustom.changeToNewWindowCreated("newSpentWindow");
        Spent spent2 = new Spent(bite, "une autre dépense", 50, null);
        addSpent(robotCustom, b2, spent2, true);
        robotCustom.removeWindow("newSpentWindow");
        robotCustom.changeWindow("root");
    }

    @Test
    void createSpentFromCategorySpentList(FxRobot robot) throws IllegalBudgetSizeException, IllegalCategorySizeException, BudgetTooSmallException, BudgetNotContainCategoryException {
        robot.clickOn("#startButton");
        FXRobotCustom robotCustom = new FXRobotCustom(robot);
        Budget b = new Budget("principal", 1000);
        Budget b2 = new Budget("budget2", 300);
        Category bite = new StandardCategory("bite", 200);
        Category toto = new StandardCategory("toto", 300);
        Category tata = new BudgetCategory(b2);
        b2.addCategory(bite);
        b.addCategory(toto);
        b.addCategory(tata);
        CreateBudgetTest.createBudget(robotCustom, "root", b, true, false);
        robotCustom.changeWindow("root");
        DashbordTest.clickOnChartCategory(robotCustom, toto);

        robotCustom.changeToNewWindowCreated("expensesCategoryView");
        robotCustom.clickOn("#addSpentButton");
        robotCustom.changeToNewWindowCreated("newSpentWindow");
        Spent spent1 = new Spent(toto, "une dépense", 30.54, null);
        addSpent(robotCustom, b, spent1, false);
        robotCustom.removeWindow("newSpentWindow");
        robotCustom.changeWindow("expensesCategoryView");

        robotCustom.clickOn("#addSpentButton");
        robotCustom.changeToNewWindowCreated("newSpentWindow");
        Spent spent2 = new Spent(bite, "une autre dépense", 50.99, null);
        addSpent(robotCustom, b2, spent2, false);
        robotCustom.removeWindow("newSpentWindow");
        robotCustom.changeWindow("expensesCategoryView");
    }
}
