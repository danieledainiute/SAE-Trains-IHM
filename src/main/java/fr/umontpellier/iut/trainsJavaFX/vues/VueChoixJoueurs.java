package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.mecanique.plateau.Plateau;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class VueChoixJoueurs extends Stage {

    private final ObservableList<String> nomsJoueurs;
    private ComboBox<Integer> comboBoxNombreJoueurs;
    private ComboBox<String> comboBoxPlateau;

    private VBox vboxJoueurs;
    private Button boutonDemarrer;
    private Plateau plateauChoisi;

    public VueChoixJoueurs() {
        nomsJoueurs = FXCollections.observableArrayList();
        createUI();
    }

    private void createUI() {
        Pane pane = new Pane();
        pane.setPrefWidth(1000);
        pane.setPrefHeight(800);
        pane.setStyle("-fx-background-image: url('/images/trains.png'); -fx-background-size: cover;");

        Pane menu = new Pane();
        menu.relocate(370, 300);
        menu.setPrefSize(300, 400);
        menu.setBorder(new Border(new BorderStroke(Color.DARKRED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));
        menu.setStyle("-fx-stroke: darkred; -fx-stroke-width: 6; -fx-background-radius: 7; -fx-background-color: #F5F5DC;");
        menu.setStyle("-fx-background-color: #F5F5DC;");

        comboBoxNombreJoueurs = new ComboBox<>();
        comboBoxNombreJoueurs.getItems().addAll(2, 3, 4);
        comboBoxNombreJoueurs.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateNombreJoueurs(newValue);
        });
        comboBoxNombreJoueurs.setStyle("-fx-font-size: 14px; -fx-pref-width: 200px;");
        comboBoxNombreJoueurs.setPromptText("Nombre de joueurs");
        comboBoxNombreJoueurs.setBorder(new Border(new BorderStroke(Color.DARKRED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));

        comboBoxPlateau = new ComboBox<>();
        comboBoxPlateau.getItems().addAll("OSAKA", "TOKYO");
        comboBoxPlateau.setStyle("-fx-font-size: 14px; -fx-pref-width: 200px;");
        comboBoxPlateau.setPromptText("Plateau");
        comboBoxPlateau.setBorder(new Border(new BorderStroke(Color.DARKRED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        comboBoxPlateau.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            plateauChoisi = Plateau.valueOf(newValue);
        });

        HBox hBoxComboBoxes = new HBox(10);
        hBoxComboBoxes.setAlignment(Pos.CENTER);
        hBoxComboBoxes.getChildren().addAll(comboBoxNombreJoueurs, comboBoxPlateau);

        vboxJoueurs = new VBox();
        vboxJoueurs.setSpacing(10);

        boutonDemarrer = new Button();
        boutonDemarrer.setText("JOUER");
        boutonDemarrer.setOnAction(event -> {
            demarrerPartie();
        });
        boutonDemarrer.setStyle("-fx-font-size: 16px; -fx-pref-width: 200px; -fx-background-color: #606060; -fx-text-fill: white;");

        Label indication = new Label("Veuillez indiquer le \nnombre de joueurs :");
        indication.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

        VBox menuBox = new VBox(indication, hBoxComboBoxes, vboxJoueurs, boutonDemarrer);
        menuBox.setSpacing(20);
        menuBox.setPadding(new Insets(20));
        menuBox.setPrefSize(300, 400);
        menuBox.setAlignment(Pos.CENTER);

        menu.getChildren().add(menuBox);
        pane.getChildren().add(menu);

        Scene scene = new Scene(pane);

        setScene(scene);
        setTitle("Trains - Choix des joueurs");
        setResizable(false);
    }

    private void demarrerPartie() {
        Integer nombreJoueur = comboBoxNombreJoueurs.getValue();
        if (nombreJoueur == null) {
            showError("Veuillez choisir un nombre possible de joueurs");
            return;
        }
        String plateauChoisi = comboBoxPlateau.getValue();
        if (plateauChoisi == null) {
            showError("Veuillez choisir un plateau.");
            return;
        }
        setListeDesNomsDeJoueurs();
    }

    protected void setListeDesNomsDeJoueurs() {
        ArrayList<String> tempNamesList = new ArrayList<>();
        for (int i = 1; i <= vboxJoueurs.getChildren().size(); i++) {
            TextField textFieldNom = (TextField) vboxJoueurs.getChildren().get(i - 1);
            String name = textFieldNom.getText();
            if (name == null || name.equals("")) {
                tempNamesList.clear();
                showError("Veuillez renseigner tous les noms des joueurs.");
                return;
            } else {
                tempNamesList.add(name);
            }
        }
        if (!tempNamesList.isEmpty()) {
            hide();
            nomsJoueurs.clear();
            nomsJoueurs.addAll(tempNamesList);
        }
    }

    private void updateNombreJoueurs(int nombreJoueurs) {
        vboxJoueurs.getChildren().clear();
        for (int i = 1; i <= nombreJoueurs; i++) {
            TextField textFieldNom = new TextField();
            textFieldNom.setPromptText("Nom du joueur " + i);
            textFieldNom.setBorder(new Border(new BorderStroke(Color.DARKRED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
            vboxJoueurs.getChildren().add(textFieldNom);
        }
    }

    public List<String> getNomsJoueurs() {
        return nomsJoueurs;
    }

    public void setNomsDesJoueursDefinisListener(ListChangeListener<String> quandLesNomsDesJoueursSontDefinis) {
        nomsJoueurs.addListener(quandLesNomsDesJoueursSontDefinis);
    }

    public Plateau getPlateau() {
        return plateauChoisi;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
