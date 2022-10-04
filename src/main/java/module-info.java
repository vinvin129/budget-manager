module fr.vinvin129.budgetmanager {
    requires javafx.controls;
    requires javafx.fxml;


    opens fr.vinvin129.budgetmanager to javafx.fxml;
    exports fr.vinvin129.budgetmanager;
    exports fr.vinvin129.budgetmanager.models.budget_logic;
    exports fr.vinvin129.budgetmanager.exceptions;
    opens fr.vinvin129.budgetmanager.models.budget_logic to javafx.fxml;
}