package nextradio.nranalytics.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gkondati on 12/11/2017.
 */

public class NRListeningObject {

    @SerializedName("type")
    private String type;

    @SerializedName("startTime")
    private String startTime;

    @SerializedName("endTime")
    private String endTime;

    @SerializedName("deliveryType")
    private String deliveryType;

    @SerializedName("frequencyHz")
    private String frequencyHz;

    @SerializedName("frequencySubChannel")
    private String frequencySubChannel;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("callLetters")
    private String callLetters;

    @SerializedName("gpsUtcTime")
    private String gpsUtcTime;

    public String getType() {
        type = "Session.Listening.Channel";
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getFrequencyHz() {
        return frequencyHz;
    }

    public void setFrequencyHz(String frequencyHz) {
        this.frequencyHz = frequencyHz;
    }

    public String getFrequencySubChannel() {
        return frequencySubChannel;
    }

    public void setFrequencySubChannel(String frequencySubChannel) {
        this.frequencySubChannel = frequencySubChannel;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCallLetters() {
        return callLetters;
    }

    public void setCallLetters(String callLetters) {
        this.callLetters = callLetters;
    }

    public String getGpsUtcTime() {
        return gpsUtcTime;
    }

    public void setGpsUtcTime(String gpsUtcTime) {
        this.gpsUtcTime = gpsUtcTime;
    }

}
