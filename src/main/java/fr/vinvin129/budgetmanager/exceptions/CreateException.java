package fr.vinvin129.budgetmanager.exceptions;

import javafx.scene.control.Alert;

/**
 * an exception class for graphics errors on something creations. Can show a graphic message.
 * @author vinvin129
 */
public class CreateException extends Exception{
    /**
     * the title for graphic alert dialog
     */
    private final String titre;
    /**
     * the description for the graphic alert dialog
     */
    private final String description;

    /**
     * Create a new exception for graphics creations
     * @param titre the title
     * @param description the description
     */
    public CreateException(String titre, String description) {
        super(titre + " " + description);
        this.titre = titre;
        this.description = description;
    }

    /**
     * Create a new exception for graphics creations
     * @param e the throwable
     */
    public CreateException(Throwable e) {
        super(e);
        this.titre = null;
        this.description = this.getMessage();
    }

    /**
     * get the title of this exception
     * @return the title
     */
    public String getTitre() {
        return titre;
    }

    /**
     * get the description of this exception
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * show the graphic alert dialog in show and await mode
     */
    public void showWarningAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Alerte lors de la création d'un élément");
        alert.setHeaderText(titre);
        alert.setContentText(description);
        alert.showAndWait();
    }
}
