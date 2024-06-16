package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.TrainsIHM;
import javafx.application.Platform;
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
import javafx.stage.Screen;
import javafx.stage.Stage;

public class VueResultats extends BorderPane {

    private TrainsIHM ihm;
    private Scene scene;

    public VueResultats(TrainsIHM ihm) {
        this.ihm = ihm;
        afficherResultats();
    }

    public void afficherResultats() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setBorder(new Border(new BorderStroke(Color.DARKRED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));
        layout.setStyle("-fx-stroke: darkred; -fx-stroke-width: 6; -fx-background-radius: 7; -fx-background-color: #F5F5DC;");
        layout.setStyle("-fx-background-color: #F5F5DC;");
        Label resultat = new Label("Résultats");
        resultat.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        layout.getChildren().add(resultat);

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
        closeButton.setOnAction(event ->
                ihm.getPrimaryStage().close());

        Button restartButton = new Button("Rejouer");
        restartButton.setOnAction(event -> ihm.demarrerPartie());

        HBox forButtons = new HBox();
        forButtons.getChildren().addAll(closeButton, restartButton);
        forButtons.setSpacing(30);
        forButtons.setAlignment(Pos.CENTER);
        layout.getChildren().add(forButtons);

        setCenter(layout);

        scene = new Scene(this, 300, 400);

        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        ihm.getPrimaryStage().setX(screenWidth / 2 - 200);
        ihm.getPrimaryStage().setY(screenHeight / 2 - 150);
    }
}
