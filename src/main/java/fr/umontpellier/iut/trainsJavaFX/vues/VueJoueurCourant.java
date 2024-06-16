package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 * <p>
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
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
    @FXML
    private Label pioche;

    private IJoueur joueur;

    public VueJoueurCourant() {

    }

    @FXML
    private void initialize() {
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
        rail.textProperty().bind(joueur.pointsRailsProperty().asString());
        deck.textProperty().bind(joueur.mainProperty().sizeProperty().asString());
        deffause.textProperty().bind(joueur.defausseProperty().sizeProperty().asString());
        pioche.textProperty().bind(joueur.piocheProperty().sizeProperty().asString());
    }

    public IJoueur getJoueurCourant() {
        return joueur;
    }
}
