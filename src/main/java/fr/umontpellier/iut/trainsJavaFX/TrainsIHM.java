package fr.umontpellier.iut.trainsJavaFX;

import fr.umontpellier.iut.trainsJavaFX.mecanique.Jeu;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.FabriqueListeDeCartes;
import fr.umontpellier.iut.trainsJavaFX.mecanique.plateau.Plateau;
import fr.umontpellier.iut.trainsJavaFX.vues.DonneesGraphiques;
import fr.umontpellier.iut.trainsJavaFX.vues.VueChoixJoueurs;
import fr.umontpellier.iut.trainsJavaFX.vues.VueDuJeu;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TrainsIHM extends Application {
    private VueChoixJoueurs vueChoixJoueurs;
    private Stage primaryStage;
    private Jeu jeu;

    private final boolean avecVueChoixJoueurs = false;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        debuterJeu();
    }

    private void debuterJeu() {
        if (avecVueChoixJoueurs) {
            vueChoixJoueurs = new VueChoixJoueurs();
            vueChoixJoueurs.setNomsDesJoueursDefinisListener(quandLesNomsJoueursSontDefinis);
            vueChoixJoueurs.show();
        } else {
            demarrerPartie();
        }
    }

    public void demarrerPartie() {
       String[] nomsJoueurs;
       Plateau plateau = Plateau.OSAKA;
       if (avecVueChoixJoueurs) {
            nomsJoueurs = vueChoixJoueurs.getNomsJoueurs().toArray(new String[0]);
            plateau = vueChoixJoueurs.getPlateau();
       } else {
            nomsJoueurs = new String[]{"John", "Paul"/*, "George", "Ringo"*/};
       }
        // Tirer aléatoirement 8 cartes préparation
        List<String> cartesPreparation = new ArrayList<>(FabriqueListeDeCartes.getNomsCartesPreparation());
        Collections.shuffle(cartesPreparation);
        String[] nomsCartes = cartesPreparation.subList(0, 8).toArray(new String[0]);
        jeu = new Jeu(nomsJoueurs, nomsCartes, plateau);
        GestionJeu.setJeu(jeu);
        VueDuJeu vueDuJeu = new VueDuJeu(jeu);

        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight() * DonneesGraphiques.pourcentageEcran;
        Scene scene = new Scene(vueDuJeu, screenWidth, screenHeight);

        vueDuJeu.creerBindings();
        jeu.run(); // le jeu doit être démarré après que les bindings ont été mis en place

//        VueResultats vueResultats = new VueResultats(this);
        primaryStage.setMinWidth(Screen.getPrimary().getBounds().getWidth() / 2.5);
        primaryStage.setMinHeight(Screen.getPrimary().getBounds().getHeight() / 2.5);
        primaryStage.setMaxWidth(Screen.getPrimary().getBounds().getWidth());
        primaryStage.setMaxHeight(Screen.getPrimary().getBounds().getHeight());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Trains");
        primaryStage.centerOnScreen();
        primaryStage.setOnCloseRequest(event -> {
            this.arreterJeu();
            event.consume();
        });
        primaryStage.show();
    }

    private final ListChangeListener<String> quandLesNomsJoueursSontDefinis = change -> {
        if (!vueChoixJoueurs.getNomsJoueurs().isEmpty())
            demarrerPartie();
    };

    public void arreterJeu() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("On arrête de jouer ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

    public Jeu getJeu() {
        return jeu;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

}