package cs1302.api;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.Priority;

/**
 * A custom component that takes user input for a song's artist and title and displays the lyrics.
 */
public class CityData extends VBox {
    HBox searchBox;
    Label stateLabel;
    Label cityLabel;
    TextField stateField;
    TextField cityField;
    Button button;
    HBox messageBox;
    Text message;
    ScrollPane scrollpane;
    Text text;

    /** Constructs an {@code CityData} object.
     */
    public CityData() {
        super();
        searchBox = new HBox(8);
        stateLabel = new Label("State:");
        cityLabel = new Label("City:");
        stateField = new TextField("ga");
        cityField = new TextField("atlanta");
        button = new Button("Search");
        message = new Text("Type in a US city and its state (abbreviated), then click the button.");
        messageBox = new HBox();
        messageBox.setPadding(new Insets(10,0,10,0));
        scrollpane = new ScrollPane();
        scrollpane.setPadding(new Insets(10));
        text = new Text();

        this.getChildren().addAll(searchBox, messageBox, scrollpane);
        searchBox.getChildren().addAll(cityLabel, cityField, stateLabel, stateField, button);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(stateLabel, Priority.ALWAYS);
        HBox.setHgrow(cityLabel, Priority.ALWAYS);
        messageBox.getChildren().add(message);
        scrollpane.setContent(text);
        scrollpane.setPrefHeight(450);
        scrollpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    } // CityData()
} // CityData
