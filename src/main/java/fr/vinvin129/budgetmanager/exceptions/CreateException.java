package fr.vinvin129.budgetmanager.exceptions;

import javafx.scene.control.Alert;

public class CreateException extends Exception{
    private final String titre;
    private final String description;

    public CreateException(String titre, String description) {
        super(titre + " " + description);
        this.titre = titre;
        this.description = description;
    }

    public CreateException(Throwable e) {
        super(e);
        this.titre = null;
        this.description = this.getMessage();
    }

    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }

    public void showWarningAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Alerte lors de la création d'un élément");
        alert.setHeaderText(titre);
        alert.setContentText(description);
        alert.showAndWait();
    }
}
