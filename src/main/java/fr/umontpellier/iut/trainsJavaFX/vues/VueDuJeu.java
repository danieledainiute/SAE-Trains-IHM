package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.ListeDeCartes;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.TrainOmnibus;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
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
    private Label nomJoueur;
    private Button passer;
    private HBox cartesEnMain;
    private Map<String, Image> cartesImages;
    private Label score;
    private Label argent;
    private HBox cartesEnReserve;
    private VBox joueursVBox;
    private VueJoueurCourant vueJoueurCourant;


    public VueDuJeu(IJeu jeu) {
        this.jeu = jeu;
        plateau = new VuePlateau();
        instruction = new Label();
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
        VBox bottom = new VBox();
        HBox bottomContent = new HBox();
        VBox leftColumn = new VBox();

        AnchorPane rightColumn = loadVueJoueurCourant();

        leftColumn.getChildren().addAll(nomJoueur, cartesEnMain);
        bottomContent.getChildren().addAll(leftColumn, rightColumn);
        bottom.getChildren().addAll(instruction, bottomContent);
        //bottom.getChildren().addAll(instruction, nomJoueur, cartesEnMain);
        bottom.setAlignment(Pos.TOP_LEFT);
        VBox right = new VBox();
        right.getChildren().addAll(passer);
        right.setAlignment(Pos.CENTER);

        ScrollPane top = new ScrollPane(cartesEnReserve);

        top.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        StackPane centerPane = new StackPane(plateau);
        centerPane.setAlignment(Pos.CENTER);

        plateau.prefWidthProperty().bind(centerPane.widthProperty().multiply(0.5));

        joueursVBox = new VBox();
        joueursVBox.setSpacing(10);
        updateJoueursVBox();
        Label titleLabel = new Label("Joueurs dans jeu");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        VBox rightContainer = new VBox();
        rightContainer.getChildren().addAll(titleLabel, joueursVBox, right);
        rightContainer.setAlignment(Pos.CENTER_RIGHT);
        rightContainer.setSpacing(20);

        setCenter(centerPane);
        setBottom(bottom);
        setTop(top);
        setRight(rightContainer);
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
            Label nomJoueurLabel = new Label(joueur.getNom());
            String couleurHex = CouleursJoueurs.couleursBackgroundJoueur.get(joueur.getCouleur());
            Color couleur = Color.web(couleurHex);
            nomJoueurLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 16));

            //score
            Label scoreLabel = new Label();
            scoreLabel.textProperty().bind(joueur.scoreProperty().asString());
            scoreLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 16));
            scoreLabel.setTextFill(Color.rgb(255, 215, 0));

            ImageView scoreImageView = new ImageView(new Image("/images/boutons/score.png"));
            scoreImageView.setFitWidth(30);
            scoreImageView.setFitHeight(30);

            StackPane scorePane = new StackPane(scoreImageView, scoreLabel);
            StackPane.setAlignment(scoreLabel, Pos.CENTER);

            //rails
            Label railsLabel = new Label();
            railsLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 16));
            railsLabel.setTextFill(Color.rgb(255, 215, 0));
            railsLabel.textProperty().bind(joueur.nbJetonsRailsProperty().asString());
            ImageView railsImageView = new ImageView(new Image("/images/boutons/rails.png"));
            railsImageView.setFitWidth(30);
            railsImageView.setFitHeight(30);
            StackPane railsPane = new StackPane(railsImageView, railsLabel);
            StackPane.setAlignment(railsLabel, Pos.CENTER);

            //carteEnMain
            Label nbCarteEnMainLabel = new Label();
            nbCarteEnMainLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 16));
            nbCarteEnMainLabel.setTextFill(Color.rgb(255, 215, 0));
            nbCarteEnMainLabel.textProperty().bind(joueur.mainProperty().sizeProperty().asString());
            ImageView nbCarteEnMainImageView = new ImageView(new Image("/images/boutons/deck.png"));
            nbCarteEnMainImageView.setFitWidth(30);
            nbCarteEnMainImageView.setFitHeight(30);
            StackPane nbCarteEnMainPane = new StackPane(nbCarteEnMainImageView, nbCarteEnMainLabel);
            StackPane.setAlignment(nbCarteEnMainLabel, Pos.CENTER);

            //defausse
            Label nbCarteEnDeFausseLabel = new Label();
            nbCarteEnDeFausseLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 16));
            nbCarteEnDeFausseLabel.setTextFill(Color.rgb(255, 215, 0));
            nbCarteEnDeFausseLabel.textProperty().bind(joueur.defausseProperty().sizeProperty().asString());
            ImageView nbCarteEnDeFausseImageView = new ImageView(new Image("/images/boutons/defausse.png"));
            nbCarteEnDeFausseImageView.setFitWidth(30);
            nbCarteEnDeFausseImageView.setFitHeight(30);
            StackPane nbCarteEnDeFaussePane = new StackPane(nbCarteEnDeFausseImageView, nbCarteEnDeFausseLabel);
            StackPane.setAlignment(nbCarteEnDeFausseLabel, Pos.CENTER);


            HBox joueurHBox = new HBox(10, nomJoueurLabel, scorePane, railsPane, nbCarteEnMainPane, nbCarteEnDeFaussePane);
            joueurHBox.setAlignment(Pos.CENTER_LEFT);
            joueursVBox.getChildren().add(joueurHBox);
            joueurHBox.setStyle("-fx-background-color: " + couleurHex + ";");
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
