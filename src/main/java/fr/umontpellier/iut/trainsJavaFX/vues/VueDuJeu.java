package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.ListeDeCartes;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.TrainOmnibus;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;
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
    private Button passer;
    private HBox cartesEnMain;
    private Map<String, Image> cartesImages;
    private HBox cartesEnReserve;
    private VBox joueursVBox;
    private VueJoueurCourant vueJoueurCourant;
    private HBox carteRecues;

    public VueDuJeu(IJeu jeu) {
        this.jeu = jeu;
        plateau = new VuePlateau();
        instruction = new Label();
        instruction.setFont(Font.font("Calibri", FontWeight.BOLD, 30));
        passer = new Button();
        ImageView passerImage = new ImageView(new Image("/images/boutons/passer.png"));
        passerImage.setFitHeight(60);
        passerImage.setFitWidth(60);
        passer.setGraphic(passerImage);
        passer.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        passer.setOnMouseClicked(event -> {
            jeu.passerAEteChoisi();
        });
        cartesEnMain = new HBox();
        carteRecues = new HBox();
        cartesImages = new HashMap<>();
        cartesEnReserve = new HBox();
        initializeCardImages();
        this.cartesEnReserve = new CartesEnReserve(jeu, carteRecues);

        //bottom
        AnchorPane rightColumn = loadVueJoueurCourant();
        HBox bottomRight = new HBox();
        bottomRight.getChildren().addAll(rightColumn);

        VBox leftColumn = new VBox();
        leftColumn.getChildren().addAll(cartesEnMain);

        HBox hboxBottom = new HBox();
        hboxBottom.getChildren().addAll(instruction);
        hboxBottom.setAlignment(Pos.CENTER);
        hboxBottom.setStyle("-fx-background-color: lightblue;");

        HBox bottomContent = new HBox();
        bottomContent.getChildren().addAll(leftColumn, rightColumn, carteRecues);
        HBox.setHgrow(leftColumn, Priority.ALWAYS);
        HBox.setHgrow(bottomRight, Priority.ALWAYS);
        bottomContent.setAlignment(Pos.CENTER);

        VBox bottom = new VBox();
        bottom.getChildren().addAll(hboxBottom, bottomContent);

        //top
        ScrollPane top = new ScrollPane(cartesEnReserve);
        top.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        //center
        StackPane centerPane = new StackPane(plateau);
        centerPane.setAlignment(Pos.CENTER);
        plateau.prefWidthProperty().bind(centerPane.widthProperty().multiply(0.5));

        //right side
        joueursVBox = new VBox();
        joueursVBox.setSpacing(10);
        updateJoueursVBox();
        VBox joueursContainer = new VBox();
        Label titleLabel = new Label("Vous jouez contre");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        HBox titleContainer = new HBox();
        titleContainer.getChildren().add(titleLabel);
        titleContainer.setAlignment(Pos.TOP_LEFT);
        joueursContainer.getChildren().addAll(titleContainer, joueursVBox);
        joueursContainer.setAlignment(Pos.CENTER);
        joueursContainer.setSpacing(20);
        joueursContainer.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");
        joueursContainer.setPadding(new Insets(15));

        VBox right = new VBox();
        right.setAlignment(Pos.CENTER);
        right.setSpacing(15);
        right.getChildren().addAll(joueursContainer, passer);
        right.setPadding(new Insets(30));

        //setting everything up on the borderpane
        setTop(top);
        setCenter(centerPane);
        setBottom(bottom);
        setTop(top);
        setRight(right);
    }

    private AnchorPane loadVueJoueurCourant() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/joueurCourant.fxml"));
        try {
            AnchorPane joueurCourantPane = loader.load();
            vueJoueurCourant = loader.getController();
            vueJoueurCourant.setJoueur(jeu.joueurCourantProperty().get());
            return joueurCourantPane;
        } catch (IOException e) {
            e.printStackTrace();
            return new AnchorPane();
        }
    }

    private void updateJoueursVBox() {
        joueursVBox.getChildren().clear();
        for (IJoueur joueur : jeu.getJoueurs()) {
            if (joueur != jeu.joueurCourantProperty().get()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/autresJoueurs.fxml"));
                try {
                    AnchorPane autresJoueursPane = loader.load();
                    VueAutresJoueurs vueAutresJoueurs = loader.getController();
                    vueAutresJoueurs.setJoueur(joueur);
                    joueursVBox.getChildren().add(autresJoueursPane);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
            updateCartesEnMain(newValue.mainProperty());
            vueJoueurCourant.setJoueur(newValue);
            updateJoueursVBox();
            updateCartesRecues(oldValue);
        });
        for (IJoueur joueur : jeu.getJoueurs()) {
            joueur.mainProperty().addListener((ListChangeListener<Carte>) change -> {
                updateCartesEnMain(joueur.mainProperty());
            });
        }

        plateau.creerBindings();
    }

    public IJeu getJeu() {
        return jeu;
    }

    private void updateCartesEnMain(ListeDeCartes main) {
        cartesEnMain.getChildren().clear();
        for (Carte c : main) {
            if (c == null) continue;
            Button carteButton = createCarteButton(c);
            cartesEnMain.getChildren().add(carteButton);
        }
    }

    private void updateCartesRecues(IJoueur joueur) {
        for (Node node : carteRecues.getChildren()) {
            if (node instanceof Button) {
                Button carteButton = (Button) node;
                Carte carte = (Carte) carteButton.getUserData();
                if (carte == null) continue;
                joueur.defausseProperty().add(carte);
            }
        }
        carteRecues.getChildren().clear();
    }


    private Button createCarteButton(Carte c) {
        Button carte = new Button();

        carte.setOnAction(event -> {
            if (jeu.joueurCourantProperty().get().nbJetonsRailsProperty().getValue() < 20) {
                jeu.joueurCourantProperty().get().uneCarteDeLaMainAEteChoisie(c.getNom());
                cartesEnMain.getChildren().remove(carte);
            }
        });

        CarteUtils.createButton(carte, c.getNom());
        return carte;
    }

    private void initializeCardImages() {
        for (Carte c : jeu.getReserve()) {
            createImage(c);
        }
        Carte omni = new TrainOmnibus();
        createImage(omni);
    }

    private void createImage(Carte c) {
        String imageFileName = CarteUtils.convertCardNameToImageFileName(c.getNom());
        String path = "/images/cartes/" + imageFileName;
        InputStream imageStream = getClass().getResourceAsStream(path);
        if (imageStream != null) {
            Image image = new Image(imageStream);
            cartesImages.put(imageFileName, image);
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
