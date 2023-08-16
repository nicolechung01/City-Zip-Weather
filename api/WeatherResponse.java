package cs1302.api;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a response from the Weather API.
 */
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
