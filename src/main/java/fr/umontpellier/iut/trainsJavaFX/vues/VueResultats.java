package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.TrainsIHM;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VueResultats extends BorderPane {

    private TrainsIHM ihm;

    public VueResultats(TrainsIHM ihm) {
        this.ihm = ihm;
        //afficherResultats();
    }

    /*public void afficherResultats() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Résultats du jeu");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        ihm.getJeu().getJoueurs().forEach(joueur -> {
            Label labelNomScore = new Label(joueur.getNom() + ": " + joueur.getScoreTotal());
            layout.getChildren().add(labelNomScore);
        });

        Button closeButton = new Button("Fermer");
        closeButton.setOnAction(event -> stage.close());

        Button restartButton = new Button("Rejouer");
        restartButton.setOnAction(event -> {
            stage.close();
            ihm.demarrerPartie();
        });

        layout.getChildren().addAll(closeButton, restartButton);

        setCenter(layout);

        Scene scene = new Scene(this, 300, 200);
        stage.setScene(scene);
        stage.showAndWait();
    }*/
}
