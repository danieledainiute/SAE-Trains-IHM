package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.ListeDeCartes;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Cette classe correspond à la fenêtre principale de l'application.
 * <p>
 * Elle est initialisée avec une référence sur la partie en cours (Jeu).
 * <p>
 * On y définit les bindings sur les éléments internes qui peuvent changer
 * (le joueur courant, ses cartes en main, son score, ...)
 * ainsi que les listeners à exécuter lorsque ces éléments changent
 */
public class VueDuJeu extends BorderPane {

    private final IJeu jeu;
    private VuePlateau plateau;
    private Label instruction;
    private Label nomJoueur;
    private Button passer;
    private HBox cartesEnMain;


    public VueDuJeu(IJeu jeu) {
        this.jeu = jeu;
        plateau = new VuePlateau();
        instruction = new Label();
        //Font font = new Font("Times New Roman", 30);
        instruction.setFont(Font.font("Calibri", FontWeight.BOLD, 30));
        nomJoueur = new Label();
        passer = new Button("Passer");
        passer.setOnMouseClicked(event -> jeu.passerAEteChoisi());
        cartesEnMain = new HBox();

        BorderPane.setAlignment(plateau, Pos.CENTER);
        VBox bottom = new VBox();
        bottom.getChildren().addAll(instruction, cartesEnMain);
        VBox right = new VBox();
        right.getChildren().addAll(nomJoueur, passer);

        setCenter(plateau);
        setBottom(bottom);

        setRight(right);
        //getChildren().addAll(plateau, instruction, nomJoueur, passer, cartesEnMain);
    }

    public void creerBindings() {
        plateau.prefWidthProperty().bind(getScene().widthProperty());
        plateau.prefHeightProperty().bind(getScene().heightProperty());
        instruction.textProperty().bind(jeu.instructionProperty());

        for (IJoueur joueur : jeu.getJoueurs()) {
            joueur.mainProperty().addListener((ListChangeListener<Carte>) change -> {
                while (change.next()) {
                    if (change.wasRemoved()) {
                        for (Carte c : change.getRemoved()) {
                            Button bouton = trouverBoutonCarte(c);
                            if (bouton != null) {
                                cartesEnMain.getChildren().remove(bouton);
                            }
                        }
                    }
                }
            });
        }

        updateCartesEnMain(jeu.joueurCourantProperty().get().mainProperty());

        jeu.joueurCourantProperty().addListener((observable, oldValue, newValue) -> {
            nomJoueur.setText("Joueur : " + newValue.getNom());
            updateCartesEnMain(newValue.mainProperty());
        });
        plateau.creerBindings();
    }

    public IJeu getJeu() {
        return jeu;
    }

    private void updateCartesEnMain(ListeDeCartes main) {
        cartesEnMain.getChildren().clear();
        for (Carte c : main) {
            Button carteButton = new Button(c.getNom());
            carteButton.setOnAction(event -> jeu.joueurCourantProperty().get().uneCarteDeLaMainAEteChoisie(c.getNom()));
            cartesEnMain.getChildren().add(carteButton);
        }
    }

    private Button trouverBoutonCarte(Carte carteATrouver) {
        for (javafx.scene.Node node : cartesEnMain.getChildren()) {
            if (node instanceof Button && ((Button) node).getText().equals(carteATrouver.getNom())) {
                return (Button) node;
            }
        }
        return null;
    }

    EventHandler<? super MouseEvent> actionPasserParDefaut = (mouseEvent -> System.out.println("Passer a été demandé"));

}
