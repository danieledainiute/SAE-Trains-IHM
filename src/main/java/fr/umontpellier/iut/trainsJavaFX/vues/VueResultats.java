package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.TrainsIHM;
import javafx.css.Style;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VueResultats extends BorderPane {

    private TrainsIHM ihm;

    public VueResultats(TrainsIHM ihm) {
        this.ihm = ihm;
        afficherResultats();
    }

    public void afficherResultats() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Résultats du jeu");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setBorder(new Border(new BorderStroke(Color.DARKRED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));
        layout.setStyle("-fx-stroke: darkred; -fx-stroke-width: 6; -fx-background-radius: 7; -fx-background-color: #F5F5DC;");
        layout.setStyle("-fx-background-color: #F5F5DC;");

        ihm.getJeu().getJoueurs().forEach(joueur -> {
            String couleurHex = CouleursJoueurs.couleursBackgroundJoueur.get(joueur.getCouleur());

            String labelStyle = "-fx-text-fill: " + couleurHex + "; -fx-font-size: 20px; -fx-font-weight: bold;";
            Label nom = new Label(joueur.getNom());
            nom.setStyle(labelStyle);

            ImageView image = new ImageView(new Image("/images/boutons/score.png"));
            image.setFitWidth(60);
            image.setFitHeight(50);

            Label score = new Label(String.valueOf(joueur.getScoreTotal()));
            score.setStyle(labelStyle);

            StackPane stack = new StackPane();
            stack.getChildren().addAll(image, score);
            stack.setAlignment(Pos.CENTER);

            HBox playerBox = new HBox(10);
            playerBox.setAlignment(Pos.CENTER);
            playerBox.getChildren().addAll(nom, stack);

            layout.getChildren().add(playerBox);
        });

        Button closeButton = new Button("Fermer");
        closeButton.setOnAction(event -> stage.close());

        Button restartButton = new Button("Rejouer");
        restartButton.setOnAction(event -> {
            stage.close();
            ihm.demarrerPartie();
        });
        HBox forButtons = new HBox();
        forButtons.getChildren().addAll(closeButton, restartButton);
        forButtons.setSpacing(30);
        forButtons.setAlignment(Pos.CENTER);
        layout.getChildren().add(forButtons);

        setCenter(layout);

        Scene scene = new Scene(this, 300, 200);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
