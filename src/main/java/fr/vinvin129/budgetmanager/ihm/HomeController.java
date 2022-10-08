package fr.vinvin129.budgetmanager.ihm;

import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.Label;

public class HomeController extends Control {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onBeginButtonClick() {
        welcomeText.setText("Vous pourrez bientôt créer votre budget ici !");
    }
}