package cs1302.api;

import com.google.gson.annotations.SerializedName;
import javafx.application.Application;
import javafx.scene.control.DialogPane;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import cs1302.api.CityData;
import cs1302.api.WeatherData;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpClient;
import java.net.URI;
import java.net.URL;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;
import java.lang.StringBuilder;

/**
 * ApiApp class is an application that utilizes the Lyrics.ovh and Shutterstock APIs
 * to allow users to search for the lyrics of a song, then use word(s) from the lyrics
 * to search for related images. The intended purpose of this application is to help
 * users find images that can be utilized as playlist covers on music streaming applications.
 */
public class ApiApp extends Application {

    /** HTTP client. */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
         .version(HttpClient.Version.HTTP_2)
         .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    /** Google {@code Gson} object for parsing JSON-formatted strings. */
    public static Gson GSON = new GsonBuilder()
         .setLenient()
        .setPrettyPrinting()
        .create();

    /** WeatherResponse class. */
    public class WeatherResponse {
        @SerializedName("wind_speed")
        String windSpeed;
        @SerializedName("wind_degrees")
        String windDegrees;
        String temp;
        String humidity;
        String sunset;
        String sunrise;
        @SerializedName("min_temp")
        String minTemp;
        @SerializedName("max_temp")
        String maxTemp;
        @SerializedName("cloud_pct")
        String cloudPct;
        @SerializedName("feels_like")
        String feelsLike;
    } // WeatherResponse


    /** ZipResponse class.*/
    public class ZipResponse {
        String countryAbbreviation;
        ZipResult[] places;
        String country;
        String placeName;
        String state;
        String stateAbbreviation;
    } //ZipResponse

    /** ZipResult class. */
    public class ZipResult {
        @SerializedName("place name")
        String placeName;
        String longitude;
        @SerializedName("post code")
        String postCode;
        String latitude;
    } //ZipResult

    Alert alert;
    DialogPane dialogPane;
    String cResults;
    String wResults;
    ZipResponse zipResponse;

    Stage stage;
    Scene scene;
    HBox root;
    CityData city;
    WeatherData weather;

    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        root = new HBox(10);
        city = new CityData();
        weather = new WeatherData();
        alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        dialogPane = alert.getDialogPane();
        dialogPane.setMinWidth(350);
        dialogPane.setMinHeight(150);
    } // ApiApp

    /** {@inheritDoc} */
    @Override
    public void init() {
        city.setPrefWidth(550);
        city.setPadding(new Insets(10));
        weather.setPrefWidth(550);
        weather.setPadding(new Insets(10));
        root.getChildren().addAll(city, weather);
        root.setAlignment(Pos.CENTER);
        weather.button.setDisable(true);
        Runnable zipTask = () -> {
            city.message.setText("Searching for ZIP codes in " + city.cityField.getText() + "...");
            cResults = "\n";
            this.zipSearch();
        };
        city.button.setOnAction(e -> runNow(zipTask));
        Runnable weatherTask = () -> {
            weather.message.setText("Searching for weather data...");
            wResults = "\n";
            this.weatherSearch();
        };
        weather.button.setOnAction(e -> runNow(weatherTask));
    } // init

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        scene = new Scene(root);
        stage.setTitle("ApiApp!");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();
    } // start

    /**
     * Method to create new thread and execute task.
     * @param target runnable task
     */
    public static void runNow(Runnable target) {
        Thread t = new Thread(target);
        t.setDaemon(true);
        t.start();
    } // runNow

    /**
     * Method to utilize Zippopotam.us API to searcg and retrieve all zipcodes of a city.
     */
    public void zipSearch() {
        try {
            String cityName = city.cityField.getText();
            cityName = cityName.replace(" ", "%20");
            String stateName = city.stateField.getText();
            String uri = "https://api.zippopotam.us/us/" +
                stateName + "/" + cityName;
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IOException();
            } // if
            String jsonString = response.body();
            zipResponse =  GSON.fromJson(jsonString, ZipResponse.class);
            for (var r : zipResponse.places) {
                cResults = cResults + r.placeName + "\nlatitude: " + r.latitude +
                    "\nlongitude: " + r.longitude + "\nZIP Code: " + r.postCode + "\n\n";
            }
            Platform.runLater(() -> {
                city.message.setText("ZIP codes in " + city.cityField.getText() + ":");
                city.text.setText(cResults);
                weather.button.setDisable(false);
            });
        } catch (IllegalArgumentException e) {
            Platform.runLater(() -> {
                alert.setContentText("No ZIP code may be available. Please try again.");
                alert.showAndWait();
            });
        } catch (IOException | InterruptedException e) {
            Platform.runLater(() -> {
                alert.setContentText("An error has occured. Please try again.");
                alert.showAndWait();
            });
        } // catch
    } //zipSearch

    /**
     * Method to utilize World Cities API to search and retrieve cities in a state.
     */
    public void weatherSearch() {
        String latSearch = weather.latField.getText();
        String lonSearch = weather.lonField.getText();
        String lat = null;
        String lon = null;
        try {
            boolean match = false;
            for (var r : zipResponse.places) {
                if (latSearch.equals(r.latitude) && lonSearch.equals(r.longitude)) {
                    match = true;
                    lat = r.latitude;
                    lon = r.longitude;
                } // if
            } // for
            if (match == false) {
                throw new IllegalArgumentException();
            } // if
            String uri = "https://api.api-ninjas.com/v1/weather?lat=" + lat + "&lon=" + lon;
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("X-Api-Key", "q58zb/mcMFvrFmq4wJjiVA==LZU5Mr5hRYqDR635")
                .build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
            String jsonString = response.body();
            WeatherResponse w = GSON.fromJson(jsonString, WeatherResponse.class);
            wResults = "Cloud %: " + w.cloudPct + "\nTemp (C): " + w.temp + "\nFeels like (C): " +
                w.feelsLike + "\nHumidity: " + w.humidity + "\nMin. temp (C): " + w.minTemp +
                "\nMax. temp (C): " + w.maxTemp + "\nWind speed (MPH): " + w.windSpeed +
                "\nWind degrees (°): " + w.windDegrees + "\nSunrise: " + w.sunrise + "\nSunset: " +
                w.sunset + "\n";
            Platform.runLater(() -> {
                weather.message.setText("Weather for " + weather.latField.getText() +
                    ", " + weather.lonField.getText() + "...");
                weather.text.setText(wResults);
                city.button.setDisable(false);
                weather.button.setDisable(false);
            });
        } catch (IOException | InterruptedException e) {
            Platform.runLater(() -> {
                alert.setContentText("Please try again with a different set of coordinates.");
                alert.showAndWait();
            });
        } catch ( IllegalArgumentException e) {
            Platform.runLater(() -> {
                dialogPane.setMinWidth(500);
                alert.setContentText("Please enter a set of coordinates from the generated list.");
                alert.showAndWait();
            });
        } // catch
    } // zipSearch()
} // ApiApp
