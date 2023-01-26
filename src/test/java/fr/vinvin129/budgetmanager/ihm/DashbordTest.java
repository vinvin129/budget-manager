package fr.vinvin129.budgetmanager.ihm;

import fr.vinvin129.budgetmanager.budgetLogic.categories.CategoryController;
import fr.vinvin129.budgetmanager.ihm.views.controllers.dashboard.ViewBudgetController;

public class DashbordTest {
    static void clickOnChartCategory(FXRobotCustom robotCustom, CategoryController categoryController) {
        robotCustom.clickOnChartDataNode("#budgetGraph", d ->
                d.getName().equals(ViewBudgetController.getCategoryChartDataName(categoryController.getModel()))
                        && (d.getPieValue() == categoryController.getModel().getAmountSpent()
                        || d.getPieValue() == categoryController.getModel().getAllocationPerMonth()));
    }
}
