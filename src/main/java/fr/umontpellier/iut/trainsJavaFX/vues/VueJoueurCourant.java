package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.Joueur;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;

import java.io.IOException;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 * <p>
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */

//do this and vueAutreesJoueurs with fxml because vueDuJeu is already done in java
public class VueJoueurCourant extends AnchorPane {
    @FXML
    private Label moneyL;
    @FXML
    private Label scoreL;
    @FXML
    private Label railNbL;
    @FXML
    private Label rail;
    @FXML
    private Label deffause;
    @FXML
    private Label deck;
    @FXML
    private Label joueurEnJeu;

    private IJoueur joueur;

    public VueJoueurCourant() {

    }

    @FXML
    private void initialize() {
        // This method will be called after the FXML file has been loaded
        if (joueur != null) {
            bindProperties(joueur);
        }
    }

    public void setJoueur(IJoueur joueur) {
        this.joueur = joueur;
        if (moneyL != null && scoreL != null && railNbL != null) {
            bindProperties(joueur);
        }
    }

    private void bindProperties(IJoueur joueur) {
        moneyL.textProperty().bind(joueur.argentProperty().asString());
        scoreL.textProperty().bind(joueur.scoreProperty().asString());
        railNbL.textProperty().bind(joueur.nbJetonsRailsProperty().asString());
        joueurEnJeu.textProperty().bind(new SimpleStringProperty(joueur.getNom()+" en jeu"));
    }

}
