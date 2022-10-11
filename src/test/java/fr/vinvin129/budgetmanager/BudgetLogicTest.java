package fr.vinvin129.budgetmanager;

import fr.vinvin129.budgetmanager.exceptions.BudgetTooSmallException;
import fr.vinvin129.budgetmanager.exceptions.IllegalBudgetSizeException;
import fr.vinvin129.budgetmanager.exceptions.IllegalCategorySizeException;
import fr.vinvin129.budgetmanager.models.budget_logic.Budget;
import fr.vinvin129.budgetmanager.models.budget_logic.BudgetCategory;
import fr.vinvin129.budgetmanager.models.budget_logic.Category;
import fr.vinvin129.budgetmanager.models.budget_logic.StandardCategory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BudgetLogicTest {
    @Test
    void logic1() throws BudgetTooSmallException, IllegalCategorySizeException, IllegalBudgetSizeException {
        Budget budgetPrincipal = new Budget("budget principal", 1000);
        Budget budgetTechnologie = new Budget("Technologie", 300);
        Category bouffe = new StandardCategory("bouffe", 300);
        Category technologiesCat = new BudgetCategory(budgetTechnologie);
        Category logement = new StandardCategory("logement", 400);
        Category telephone = new StandardCategory("telephone", 300);

        budgetPrincipal.addCategory(bouffe);
        budgetPrincipal.addCategory(technologiesCat);
        budgetPrincipal.addCategory(logement);
        budgetTechnologie.addCategory(telephone);
        assertEquals(0, budgetPrincipal.getBalance(), "on commence donc ça doit être vide");
        assertEquals(0, technologiesCat.getBalance(), "on commence donc ça doit être vide");
        assertEquals(0, logement.getBalance(), "on commence donc ça doit être vide");
        assertEquals(0, bouffe.getBalance(), "on commence donc ça doit être vide");

        budgetPrincipal.newMonth();
        assertEquals(700, budgetPrincipal.getBalance(), "on commence donc ça doit être vide");
        assertEquals(300, technologiesCat.getBalance(), "on commence donc ça doit être vide");
        assertEquals(400, logement.getBalance(), "on commence donc ça doit être vide");
        assertEquals(300, bouffe.getBalance(), "on commence donc ça doit être vide");

        budgetPrincipal.newMonth();
        assertEquals(1400, budgetPrincipal.getBalance(), "on commence donc ça doit être vide");
        assertEquals(600, technologiesCat.getBalance(), "on commence donc ça doit être vide");
        assertEquals(400, logement.getBalance(), "on commence donc ça doit être vide");
        assertEquals(300, bouffe.getBalance(), "on commence donc ça doit être vide");

        assertDoesNotThrow(() -> budgetPrincipal.addSpent(new Spent(bouffe, "un bk !", 10)));

        assertEquals(1390, budgetPrincipal.getBalance(), "on commence donc ça doit être vide");
        assertEquals(600, technologiesCat.getBalance(), "on commence donc ça doit être vide");
        assertEquals(400, logement.getBalance(), "on commence donc ça doit être vide");
        assertEquals(290, bouffe.getBalance(), "on commence donc ça doit être vide");

        assertDoesNotThrow(() -> budgetTechnologie.addSpent(new Spent(telephone, "un tel !", 200)));

        assertEquals(1390, budgetPrincipal.getBalance(), "on commence donc ça doit être vide");
        assertEquals(400, technologiesCat.getBalance(), "on commence donc ça doit être vide");
        assertEquals(400, logement.getBalance(), "on commence donc ça doit être vide");
        assertEquals(290, bouffe.getBalance(), "on commence donc ça doit être vide");
        assertEquals(100, telephone.getBalance(), "on commence donc ça doit être vide");

        budgetPrincipal.newMonth();
        assertEquals(2090, budgetPrincipal.getBalance(), "on commence donc ça doit être vide");
        assertEquals(700, technologiesCat.getBalance(), "on commence donc ça doit être vide");
        assertEquals(400, logement.getBalance(), "on commence donc ça doit être vide");
        assertEquals(300, bouffe.getBalance(), "on commence donc ça doit être vide");
        assertEquals(300, telephone.getBalance(), "on commence donc ça doit être vide");
    }
}
