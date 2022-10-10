package fr.vinvin129.budgetmanager.ihm.views.controllers;

import fr.vinvin129.budgetmanager.ihm.IHM;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

public class HomeController {

    public Button startButton;

    @FXML
    protected void onBeginButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(IHM.class.getResource("createViews/budgets/create-budget.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        IHM.getPrincipalStage().setScene(scene);
    }
}