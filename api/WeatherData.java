package cs1302.api;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;

/**
 * A custom component that searches for and displays images based on user input.
 */
public class WeatherData extends VBox {
    HBox searchBox;
    Label latLabel;
    Label lonLabel;
    TextField latField;
    TextField lonField;
    Button button;
    HBox messageBox;
    Text message;
    ScrollPane scrollpane;
    Text text;

    /** Constructs an {@code WeatherData} object.
     */
    public WeatherData() {
        super();
        searchBox = new HBox(8);
        latLabel = new Label("Lat:");
        lonLabel = new Label("Long:");
        latField = new TextField();
        lonField = new TextField();
        button = new Button("Search");
        message = new Text("Type in a latitude and longitude set from " +
        "the generated list, then click the button.");
        messageBox = new HBox();
        messageBox.setPadding(new Insets(10,0,10,0));
        scrollpane = new ScrollPane();
        scrollpane.setPadding(new Insets(10));
        text = new Text();

        this.getChildren().addAll(searchBox, messageBox, scrollpane);
        searchBox.getChildren().addAll(latLabel, latField, lonLabel, lonField, button);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(latLabel, Priority.ALWAYS);
        HBox.setHgrow(lonLabel, Priority.ALWAYS);
        messageBox.getChildren().add(message);
        scrollpane.setContent(text);
        scrollpane.setPrefHeight(450);
        scrollpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    } // WeatherData()
} // WeatherData
