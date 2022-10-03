module fr.vinvin128.budgetmanager {
    requires javafx.controls;
    requires javafx.fxml;


    opens fr.vinvin129.budgetmanager to javafx.fxml;
    exports fr.vinvin129.budgetmanager;
    exports fr.vinvin129.budgetmanager.models.budget_logic;
    opens fr.vinvin129.budgetmanager.models.budget_logic to javafx.fxml;
}