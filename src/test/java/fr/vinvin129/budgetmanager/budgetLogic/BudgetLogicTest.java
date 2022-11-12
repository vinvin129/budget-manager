package fr.vinvin129.budgetmanager.budgetLogic;

import fr.vinvin129.budgetmanager.budgetLogic.budgets.BudgetController;
import fr.vinvin129.budgetmanager.budgetLogic.categories.BudgetCategory;
import fr.vinvin129.budgetmanager.budgetLogic.categories.CategoryController;
import fr.vinvin129.budgetmanager.budgetLogic.moments.BudgetMoment;
import fr.vinvin129.budgetmanager.budgetLogic.moments.CategoryMoment;
import fr.vinvin129.budgetmanager.exceptions.BudgetTooSmallException;
import fr.vinvin129.budgetmanager.exceptions.IllegalBudgetSizeException;
import fr.vinvin129.budgetmanager.exceptions.IllegalCategorySizeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BudgetLogicTest {
    @Test
    void logic1() throws BudgetTooSmallException, IllegalCategorySizeException, IllegalBudgetSizeException {
        BudgetController budgetPrincipal = new BudgetController(
                BudgetMoment.create("budget principal", 1000)
        );
        CategoryMoment bouffeMoment = CategoryMoment.create("bouffe", 300);
        CategoryMoment technologiesMoment = CategoryMoment.create(
                BudgetMoment.create("Technologie", 300)
        );
        CategoryMoment logementMoment = CategoryMoment.create("logement", 400);
        CategoryMoment telephoneMoment = CategoryMoment.create("telephone", 300);

        CategoryController bouffe = budgetPrincipal.addCategory(bouffeMoment);
        CategoryController technologiesCat = budgetPrincipal.addCategory(technologiesMoment);
        CategoryController logement = budgetPrincipal.addCategory(logementMoment);
        CategoryController telephone = ((BudgetCategory)technologiesCat.getModel())
                .getBudgetController()
                .addCategory(telephoneMoment);

        assertEquals(0, budgetPrincipal.getModel().getBalance(), "on commence donc ça doit être vide");
        assertEquals(0, technologiesCat.getModel().getBalance(), "on commence donc ça doit être vide");
        assertEquals(0, logement.getModel().getBalance(), "on commence donc ça doit être vide");
        assertEquals(0, bouffe.getModel().getBalance(), "on commence donc ça doit être vide");

        budgetPrincipal.newMonth();
        assertEquals(700, budgetPrincipal.getModel().getBalance(), "on commence donc ça doit être vide");
        assertEquals(300, technologiesCat.getModel().getBalance(), "on commence donc ça doit être vide");
        assertEquals(400, logement.getModel().getBalance(), "on commence donc ça doit être vide");
        assertEquals(300, bouffe.getModel().getBalance(), "on commence donc ça doit être vide");

        budgetPrincipal.newMonth();
        assertEquals(1400, budgetPrincipal.getModel().getBalance(), "on commence donc ça doit être vide");
        assertEquals(600, technologiesCat.getModel().getBalance(), "on commence donc ça doit être vide");
        assertEquals(400, logement.getModel().getBalance(), "on commence donc ça doit être vide");
        assertEquals(300, bouffe.getModel().getBalance(), "on commence donc ça doit être vide");

        assertDoesNotThrow(() -> bouffe.addSpent(new Spent("un bk !", 10, null)));

        assertEquals(1390, budgetPrincipal.getModel().getBalance(), "on commence donc ça doit être vide");
        assertEquals(600, technologiesCat.getModel().getBalance(), "on commence donc ça doit être vide");
        assertEquals(400, logement.getModel().getBalance(), "on commence donc ça doit être vide");
        assertEquals(290, bouffe.getModel().getBalance(), "on commence donc ça doit être vide");

        assertDoesNotThrow(() -> telephone.addSpent(new Spent("un tel !", 200, null)));

        assertEquals(1390, budgetPrincipal.getModel().getBalance(), "on commence donc ça doit être vide");
        assertEquals(400, technologiesCat.getModel().getBalance(), "on commence donc ça doit être vide");
        assertEquals(400, logement.getModel().getBalance(), "on commence donc ça doit être vide");
        assertEquals(290, bouffe.getModel().getBalance(), "on commence donc ça doit être vide");
        assertEquals(100, telephone.getModel().getBalance(), "on commence donc ça doit être vide");

        budgetPrincipal.newMonth();
        assertEquals(2090, budgetPrincipal.getModel().getBalance(), "on commence donc ça doit être vide");
        assertEquals(700, technologiesCat.getModel().getBalance(), "on commence donc ça doit être vide");
        assertEquals(400, logement.getModel().getBalance(), "on commence donc ça doit être vide");
        assertEquals(300, bouffe.getModel().getBalance(), "on commence donc ça doit être vide");
        assertEquals(300, telephone.getModel().getBalance(), "on commence donc ça doit être vide");
    }
}
