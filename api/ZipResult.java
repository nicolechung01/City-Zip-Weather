package cs1302.api;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a zipcode result.
 */
public class ZipResult {
    @SerializedName("place name")
    String placename;
    String longitude;
    @SerializedName("post code")
    String postcode;
    String latitude;
} //ZipResult
