package fr.vinvin129.budgetmanager;

import fr.vinvin129.budgetmanager.budgetLogic.Spent;
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
        BudgetController budgetPrincipal = new BudgetController(new BudgetMoment(
                        "budget principal",
                        1000,
                        0,
                        new CategoryMoment[]{})
        );
        CategoryMoment bouffeMoment = new CategoryMoment(
                "bouffe",
                300,
                0,
                new Spent[]{},
                null);
        CategoryMoment technologiesMoment = new CategoryMoment(
                "Technologie",
                300,
                0,
                new Spent[]{},
                new BudgetMoment(
                        "Technologie",
                        300,
                        0,
                        new CategoryMoment[]{}
                ));
        CategoryMoment logementMoment = new CategoryMoment(
                "logement",
                400,
                0,
                new Spent[]{},
                null);
        CategoryMoment telephoneMoment = new CategoryMoment(
                "telephone",
                300,
                0,
                new Spent[]{},
                null);

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
