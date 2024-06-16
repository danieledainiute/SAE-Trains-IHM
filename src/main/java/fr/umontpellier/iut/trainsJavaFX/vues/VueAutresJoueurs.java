package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.ListeDeCartes;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * Cette classe présente les éléments des joueurs autres que le joueur courant,
 * en cachant ceux que le joueur courant n'a pas à connaitre.
 * <p>
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueAutresJoueurs extends AnchorPane {
    @FXML
    private Label nomJoueur;
    @FXML
    private Label nbPoints;
    @FXML
    private Label nbRails;
    @FXML
    private Label nbDeck;
    @FXML
    private Label nbDeffause;
    private IJoueur joueur;

    @FXML
    public void initialize() {
        // Initialization code can go here if needed
    }

    public void setJoueur(IJoueur joueur) {
        this.joueur = joueur;
        nomJoueur.setText(joueur.getNom());
        nbPoints.textProperty().bind(joueur.scoreProperty().asString());
        nbRails.textProperty().bind(joueur.nbJetonsRailsProperty().asString());
        nbDeck.textProperty().bind(joueur.mainProperty().sizeProperty().asString());
        nbDeffause.textProperty().bind(joueur.defausseProperty().sizeProperty().asString());
    }
}
