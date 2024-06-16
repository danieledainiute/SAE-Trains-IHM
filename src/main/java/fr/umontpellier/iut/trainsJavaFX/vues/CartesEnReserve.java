package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.GestionJeu;
import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Cette classe gère l'affichage des cartes disponibles dans la réserve du jeu.
 */
public class CartesEnReserve extends HBox {

    private final IJeu jeu;
    private final HBox cartesRecues;

    public CartesEnReserve(IJeu jeu, HBox cartesRecues) {
        this.jeu = jeu;
        this.cartesRecues = cartesRecues;
        createCartesEnReserve();
    }

    private void createCartesEnReserve() {
        for (Carte c : jeu.getReserve()) {
            IntegerProperty nbCarteReserve = new SimpleIntegerProperty(GestionJeu.getJeu().getTaillesPilesReserveProperties().get(c.getNom()).getValue());
            Button carteButton = createCarteButtonFromReserve(c, nbCarteReserve);
            Label nbCarte = new Label();
            nbCarte.textProperty().bind(nbCarteReserve.asString());
            nbCarte.setFont(Font.font("Calibri", FontWeight.EXTRA_BOLD, 14));
            nbCarte.setStyle("-fx-text-fill: darkblue;");

            StackPane carte = new StackPane();
            carte.getChildren().addAll(carteButton, nbCarte);
            StackPane.setAlignment(nbCarte, Pos.BOTTOM_CENTER);

            getChildren().add(carte);
        }
    }

    private Button createCarteButtonFromReserve(Carte c, IntegerProperty nbCarteReserve) {
        Button carte = new Button();
        CarteUtils.createButton(carte, c.getNom());
        carte.setOnAction(event -> {
            IJoueur joueurCourant = jeu.joueurCourantProperty().get();
            IntegerProperty argent = joueurCourant.argentProperty();
            int money = argent.getValue();
            jeu.uneCarteDeLaReserveEstAchetee(c.getNom());
            if (money >= c.getCout()) {
                int currentNbCarte = nbCarteReserve.get();

                if (currentNbCarte > 0) {
                    nbCarteReserve.set(currentNbCarte - 1);
                    Button carteToAdd = new Button();
                    CarteUtils.createButton(carteToAdd, c.getNom());
                    cartesRecues.getChildren().add(carteToAdd);
                }

                if (currentNbCarte == 0) {
                    StackPane parent = (StackPane) carte.getParent();
                    getChildren().remove(parent);
                }
            }
        });

        return carte;
    }
}
