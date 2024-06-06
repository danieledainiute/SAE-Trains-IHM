package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.ListeDeCartes;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.TrainOmnibus;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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
    private Map<String, Image> cartesImages;
    private Label score;
    private Label argent;
    private HBox cartesEnReserve;


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
        cartesImages = new HashMap<>();
        score = new Label("0");
        argent = new Label("0");
        cartesEnReserve = new HBox();
        initializeCardImages();

        createCartesEnReserve();
        //BorderPane.setAlignment(plateau, Pos.CENTER);
        VBox bottom = new VBox();
        bottom.getChildren().addAll(instruction, nomJoueur, cartesEnMain);
        bottom.setAlignment(Pos.TOP_LEFT);
        VBox right = new VBox();
        right.getChildren().addAll(passer, score, argent);
        right.setAlignment(Pos.CENTER);

        ScrollPane top = new ScrollPane(cartesEnReserve);


        setCenter(plateau);
        setBottom(bottom);
        setTop(top);
        //setBottom(plateau);

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
            bindScore(newValue);
            bindArgent(newValue);
        });

        bindArgent(jeu.joueurCourantProperty().get());
        bindScore(jeu.joueurCourantProperty().get());
        plateau.creerBindings();
    }

    private void bindScore(IJoueur joueur) {
        score.textProperty().bind(joueur.scoreProperty().asString());
    }

    private void bindArgent(IJoueur joueur) {
        argent.textProperty().bind(joueur.argentProperty().asString());
    }

    public IJeu getJeu() {
        return jeu;
    }

    private void updateCartesEnMain(ListeDeCartes main) {
        cartesEnMain.getChildren().clear();
        for (Carte c : main) {
            Button carteButton = createCarteButton(c);
            //carteButton.setOnAction(event -> jeu.joueurCourantProperty().get().uneCarteDeLaMainAEteChoisie(c.getNom()));
            cartesEnMain.getChildren().add(carteButton);
        }
    }

    //fix what the buttons do when pressed
    private Button createCarteButton(Carte c) {
        Button carte = new Button();
        carte.setOnAction(event -> {
            jeu.joueurCourantProperty().get().uneCarteDeLaMainAEteChoisie(c.getNom());
            cartesEnMain.getChildren().remove(carte); // Remove the button when it is clicked
        });

        createButton(c, carte);
        //carte.setId(c.getNom());
        return carte;
    }

    //fix and understand if there have to be cartes de reserve or cartes en jeu on the top
    private Button createCarteButtonFromReserve(Carte c) {
        Button carte = new Button();
        carte.setOnAction(event -> {
            jeu.uneCarteDeLaReserveEstAchetee(c.getNom());
            cartesEnReserve.getChildren().remove(carte);
        });

        createButton(c, carte);
        return carte;
    }

    private void createButton(Carte c, Button carte) {
        String imageFileName = convertCardNameToImageFileName(c.getNom());
        Image card = cartesImages.get(imageFileName);
        if (card != null) {
            ImageView imageView = new ImageView(card);
            imageView.setFitWidth(80);
            imageView.setFitHeight(100);
            carte.setGraphic(imageView);
        } else carte.setText(c.getNom());
    }


    private void initializeCardImages() {
        String imageFileName;
        String path;
        InputStream imageStream;
        for (Carte c : jeu.getReserve()) {
            createImage(c);
        }
        Carte omni = new TrainOmnibus();
        createImage(omni);

    }

    private void createImage(Carte c) {
        //Image image = null;
        String imageFileName = convertCardNameToImageFileName(c.getNom());
        String path = "/images/cartes/" + imageFileName;
        InputStream imageStream = getClass().getResourceAsStream(path);
        if (imageStream != null) {
            Image image = new Image(imageStream);
            cartesImages.put(imageFileName, image);
        }
    }

    private String convertCardNameToImageFileName(String card) {
        return card.toLowerCase().replace(" ", "_") + ".jpg";
    }

    private void createCartesEnReserve() {
        for (Carte c : jeu.getReserve()) {
            Button carteButton = createCarteButtonFromReserve(c);
            cartesEnReserve.getChildren().add(carteButton);
        }
    }

    private Button trouverBoutonCarte(Carte carteATrouver) {
        for (Node node : cartesEnMain.getChildren()) {
            if (node instanceof Button && ((Button) node).getText().equals(carteATrouver.getNom())) {
                return (Button) node;
            }
        }
        return null;
    }

    EventHandler<? super MouseEvent> actionPasserParDefaut = (mouseEvent -> System.out.println("Passer a été demandé"));

}
