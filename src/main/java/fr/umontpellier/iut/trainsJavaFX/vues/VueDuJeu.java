package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.ListeDeCartes;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.TrainOmnibus;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
    private Button passer;
    private HBox cartesEnMain;
    private Map<String, Image> cartesImages;
    private Label score;
    private Label argent;
    private HBox cartesEnReserve;
    private VBox joueursVBox;
    private VueJoueurCourant vueJoueurCourant;
    private HBox carteRecues;


    public VueDuJeu(IJeu jeu) {
        this.jeu = jeu;
        plateau = new VuePlateau();
        instruction = new Label();
        instruction.setFont(Font.font("Calibri", FontWeight.BOLD, 30));
        passer = new Button("Passer");
        passer.setOnMouseClicked(event -> {
            jeu.passerAEteChoisi();
            IJoueur joueurCourant = jeu.joueurCourantProperty().get();
            for (Node node : carteRecues.getChildren()) {
                if (node instanceof Button) {
                    Button carteButton = (Button) node;
                    Carte carte = (Carte) carteButton.getUserData();
                    //joueurCourant.argentProperty().set(joueurCourant.argentProperty().add(carte.getNbPointsVictoire()).getValue());
                    joueurCourant.defausseProperty().add(carte);
                }
            }
            carteRecues.getChildren().clear();
        });

        cartesEnMain = new HBox();
        carteRecues = new HBox();
        cartesImages = new HashMap<>();
        cartesEnReserve = new HBox();
        initializeCardImages();
        createCartesEnReserve();

        score = new Label("0");
        argent = new Label("0");

        AnchorPane rightColumn = loadVueJoueurCourant();
        HBox bottomRight = new HBox();
        bottomRight.getChildren().addAll(rightColumn, carteRecues);

        VBox leftColumn = new VBox();
        leftColumn.getChildren().addAll(cartesEnMain);

        VBox bottom = new VBox();
        //HBox bottomContent = new HBox();
        HBox hboxBottom = new HBox();
        TilePane forBottom = new TilePane();
        forBottom.setPrefColumns(2);

        hboxBottom.getChildren().addAll(instruction);
        hboxBottom.setAlignment(Pos.CENTER);
        hboxBottom.setStyle("-fx-background-color: lightblue;");

        forBottom.setHgap(30);
        forBottom.getChildren().add(leftColumn);
        forBottom.getChildren().add(bottomRight);
        //forBottom.getChildren().addAll(leftColumn, bottomRight);
        bottom.getChildren().addAll(hboxBottom, forBottom);

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
        VBox rightContainer = new VBox();
        Label titleLabel = new Label("Joueurs dans jeu");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        HBox titleContainer = new HBox();
        titleContainer.getChildren().add(titleLabel);
        titleContainer.setAlignment(Pos.TOP_LEFT);
        rightContainer.getChildren().addAll(titleContainer, joueursVBox, right);
        rightContainer.setAlignment(Pos.CENTER);
        rightContainer.setSpacing(20);


        setTop(top);
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
            updateCartesEnMain(newValue.mainProperty());
            bindScore(newValue);
            bindArgent(newValue);
            vueJoueurCourant.setJoueur(newValue);
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
            cartesEnMain.getChildren().add(carteButton);
        }
    }

    //fix what the buttons do when pressed
    private Button createCarteButton(Carte c) {
        Button carte = new Button();

        carte.setOnAction(event -> {
            if (jeu.joueurCourantProperty().get().nbJetonsRailsProperty().getValue() < 20) {
                jeu.joueurCourantProperty().get().uneCarteDeLaMainAEteChoisie(c.getNom());
                cartesEnMain.getChildren().remove(carte); // Remove the button when it is clicked
            }
        });

        createButton(c, carte);
        return carte;
    }

    private Button createCarteButtonFromReserve(Carte c, IntegerProperty nbCarteReserve) {
        Button carte = new Button();
        createButton(c, carte);

        carte.setOnAction(event -> {
            IJoueur joueurCourant = jeu.joueurCourantProperty().get();
            IntegerProperty argent = joueurCourant.argentProperty();

            if (argent.getValue() >= c.getCout()) {
                jeu.uneCarteDeLaReserveEstAchetee(c.getNom());
                int currentNbCarte = nbCarteReserve.get();

                if (currentNbCarte > 0) {
                    nbCarteReserve.set(currentNbCarte - 1);
                    Button carteToAdd = new Button();
                    createButton(c, carteToAdd);
                    carteRecues.getChildren().add(carteToAdd);
                }

                if (currentNbCarte == 1) {
                    Node parent = carte.getParent();

                    if (parent instanceof StackPane) {
                        StackPane stackPane = (StackPane) parent;
                        stackPane.getChildren().clear();
                        cartesEnReserve.getChildren().remove(stackPane);
                    }
                }
            }
        });

        return carte;
    }

    private void createButton(Carte c, Button carte) {
        String imageFileName = convertCardNameToImageFileName(c.getNom());
        Image card = cartesImages.get(imageFileName);
        if (card != null) {
            ImageView imageView = new ImageView(card);
            imageView.setFitWidth(120);
            imageView.setFitHeight(160);
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
            IntegerProperty nbCarteReserve = new SimpleIntegerProperty(10);
            Button carteButton = createCarteButtonFromReserve(c, nbCarteReserve);

            Label nbCarte = new Label();
            nbCarte.textProperty().bind(nbCarteReserve.asString());

            StackPane carte = new StackPane(carteButton, nbCarte);
            StackPane.setAlignment(nbCarte, Pos.BOTTOM_CENTER);

            cartesEnReserve.getChildren().add(carte);
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
