module fr.vinvin129.budgetmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires com.fasterxml.jackson.databind;

    exports fr.vinvin129.budgetmanager.budgetLogic;
    exports fr.vinvin129.budgetmanager.budgetLogic.budgets;
    exports fr.vinvin129.budgetmanager.budgetLogic.categories;
    exports fr.vinvin129.budgetmanager.budgetLogic.moments;
    exports fr.vinvin129.budgetmanager.exceptions;
    opens fr.vinvin129.budgetmanager.budgetLogic to com.fasterxml.jackson.databind, javafx.fxml;
    opens fr.vinvin129.budgetmanager.budgetLogic.budgets to javafx.fxml;
    opens fr.vinvin129.budgetmanager.budgetLogic.categories to javafx.fxml;
    opens fr.vinvin129.budgetmanager.budgetLogic.moments to com.fasterxml.jackson.databind, javafx.fxml;
    exports fr.vinvin129.budgetmanager.ihm;
    opens fr.vinvin129.budgetmanager.ihm to javafx.fxml;
    exports fr.vinvin129.budgetmanager.ihm.views.controllers;
    opens fr.vinvin129.budgetmanager.ihm.views.controllers to javafx.fxml;
    exports fr.vinvin129.budgetmanager.ihm.views.stages;
    opens fr.vinvin129.budgetmanager.ihm.views.stages to javafx.fxml;
    exports fr.vinvin129.budgetmanager.ihm.views.controllers.create.category;
    opens fr.vinvin129.budgetmanager.ihm.views.controllers.create.category to javafx.fxml;
    exports fr.vinvin129.budgetmanager.ihm.views.controllers.create.budget;
    opens fr.vinvin129.budgetmanager.ihm.views.controllers.create.budget to javafx.fxml;
    exports fr.vinvin129.budgetmanager.ihm.views.controllers.dashboard;
    opens fr.vinvin129.budgetmanager.ihm.views.controllers.dashboard to javafx.fxml;
    exports fr.vinvin129.budgetmanager.ihm.views.controllers.create.spent;
    opens fr.vinvin129.budgetmanager.ihm.views.controllers.create.spent to javafx.fxml;
    exports fr.vinvin129.budgetmanager.events;
    exports fr.vinvin129.budgetmanager.budgetLogic.history;
    opens fr.vinvin129.budgetmanager.budgetLogic.history to javafx.fxml;
}