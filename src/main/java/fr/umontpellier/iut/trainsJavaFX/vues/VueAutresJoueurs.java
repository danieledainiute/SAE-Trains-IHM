package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;


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
    private Label nbScore;
    @FXML
    private Label nbRails;
    @FXML
    private Label nbDeck;
    @FXML
    private Label nbDeffause;
    private IJoueur joueur;
    @FXML
    private AnchorPane mainVue;

    @FXML
    public void initialize() {
    }

    public void setJoueur(IJoueur joueur) {
        this.joueur = joueur;
        nomJoueur.setText(joueur.getNom());
        nbScore.textProperty().bind(joueur.scoreProperty().asString());
        nbRails.textProperty().bind(joueur.nbJetonsRailsProperty().asString());
        nbDeck.textProperty().bind(joueur.mainProperty().sizeProperty().asString());
        nbDeffause.textProperty().bind(joueur.defausseProperty().sizeProperty().asString());
        String couleurHex = CouleursJoueurs.couleursBackgroundJoueur.get(joueur.getCouleur());
        mainVue.setStyle("-fx-background-color: " + couleurHex + ";");
    }
}
