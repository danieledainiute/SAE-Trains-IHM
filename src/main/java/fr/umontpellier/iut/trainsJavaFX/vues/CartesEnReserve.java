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

public class CartesEnReserve extends HBox {

    private final IJeu jeu;
    private final HBox cartesRecues;

    public CartesEnReserve(IJeu jeu, HBox cartesRecues) {
        this.jeu = jeu;
        this.cartesRecues = cartesRecues;
        createCartesEnReserve();
    }

    private void createCartesEnReserve() {
        jeu.getReserve().forEach(carte -> {
            IntegerProperty nbCarteReserve = new SimpleIntegerProperty(
                    GestionJeu.getJeu().getTaillesPilesReserveProperties().get(carte.getNom()).getValue()
            );
            StackPane carteStackPane = new StackPane(
                    createCarteButton(carte, nbCarteReserve),
                    createNbCarteLabel(nbCarteReserve)
            );
            StackPane.setAlignment(carteStackPane.getChildren().get(1), Pos.BOTTOM_CENTER);
            getChildren().add(carteStackPane);
        });
    }

    private Button createCarteButton(Carte carte, IntegerProperty nbCarteReserve) {
        Button carteButton = new Button();
        CarteUtils.createButton(carteButton, carte.getNom());
        carteButton.setOnAction(event -> handleCarteButtonAction(carte, nbCarteReserve, carteButton));
        return carteButton;
    }

    private Label createNbCarteLabel(IntegerProperty nbCarteReserve) {
        Label nbCarteLabel = new Label();
        nbCarteLabel.textProperty().bind(nbCarteReserve.asString());
        nbCarteLabel.setFont(Font.font("Calibri", FontWeight.EXTRA_BOLD, 14));
        nbCarteLabel.setStyle("-fx-text-fill: darkblue;");
        return nbCarteLabel;
    }

    private void handleCarteButtonAction(Carte carte, IntegerProperty nbCarteReserve, Button carteButton) {
        IJoueur joueurCourant = jeu.joueurCourantProperty().get();
        nbCarteReserve.bind(jeu.joueurCourantProperty().getValue().cartesRecuesProperty().sizeProperty());
        int money = joueurCourant.argentProperty().getValue();
        jeu.uneCarteDeLaReserveEstAchetee(carte.getNom());
        if (money >= carte.getCout() && nbCarteReserve.get() > 0) {
            cartesRecues.getChildren().add(createCarteButton(carte, nbCarteReserve));
        }
        if (nbCarteReserve.get() == 0) {
            getChildren().remove(carteButton.getParent());
        }
    }
}
