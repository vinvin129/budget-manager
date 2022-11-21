package fr.vinvin129.budgetmanager.ihm.views.controllers.create.category;

import fr.vinvin129.budgetmanager.budgetLogic.categories.CategoryController;
import fr.vinvin129.budgetmanager.budgetLogic.moments.CategoryMoment;
import fr.vinvin129.budgetmanager.exceptions.CreateCategoryException;

/**
 * an interface for create a category with IHM
 * @author vinvin129
 */
public interface CreateCategory {
    /**
     * @return the {@link CategoryMoment} object
     * @throws CreateCategoryException if category can't be created
     */
    CategoryMoment getCategoryMoment() throws CreateCategoryException;

    void setInitialCategoryController(CategoryController category);
}
