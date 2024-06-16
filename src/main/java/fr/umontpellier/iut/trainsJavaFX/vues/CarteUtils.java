package fr.umontpellier.iut.trainsJavaFX.vues;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class CarteUtils {

    public static void createButton(Button carte, String cardName) {
        String imageFileName = convertCardNameToImageFileName(cardName);
        Image cardImage = new Image(Objects.requireNonNull(CarteUtils.class.getResourceAsStream("/images/cartes/" + imageFileName)));
        ImageView imageView = new ImageView(cardImage);
        imageView.setFitWidth(120);
        imageView.setFitHeight(160);
        carte.setGraphic(imageView);
        carte.setStyle("-fx-background-color: transparent; -fx-padding: 6;");

        carte.setOnMouseEntered(event -> {
            carte.setScaleX(1.1);
            carte.setScaleY(1.1);
        });

        carte.setOnMouseExited(event -> {
            carte.setScaleX(1.0);
            carte.setScaleY(1.0);
        });
    }

    public static String convertCardNameToImageFileName(String cardName) {
        return cardName.toLowerCase()
                .replace(" ", "_")
                .replace("é", "e")
                .replace("ô", "o") + ".jpg";
    }
}
