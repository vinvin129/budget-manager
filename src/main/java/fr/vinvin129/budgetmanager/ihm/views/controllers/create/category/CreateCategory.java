package fr.vinvin129.budgetmanager.ihm.views.controllers.create.category;

import fr.vinvin129.budgetmanager.exceptions.CreateCategoryException;
import fr.vinvin129.budgetmanager.models.budget_logic.Category;

/**
 * an interface for create a category with IHM
 * @author vinvin129
 */
public interface CreateCategory {
    /**
     * @return the {@link Category} object
     * @throws CreateCategoryException if category can't be created
     */
    Category getCategory() throws CreateCategoryException;

    void setInitialCategory(Category category);
}
