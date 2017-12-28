package nextradio.nranalytics.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gkondati on 12/20/2017.
 */

public class NRRadioImpressionEvent {

    @SerializedName("type")
    private String type;

    @SerializedName("createTime")
    private String createTime;

    @SerializedName("artist")
    private String artist;

    @SerializedName("title")
    private String title;

    @SerializedName("eventMetadata")
    private String eventMetadata;

    @SerializedName("deliveryType")
    private Integer deliveryType;

    @SerializedName("frequencyHz")
    private String frequencyHz;

    @SerializedName("frequencySubChannel")
    private Integer frequencySubChannel;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("callLetters")
    private String callLetters;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEventMetadata() {
        return eventMetadata;
    }

    public void setEventMetadata(String eventMetadata) {
        this.eventMetadata = eventMetadata;
    }

    public Integer getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Integer deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getFrequencyHz() {
        return frequencyHz;
    }

    public void setFrequencyHz(String frequencyHz) {
        this.frequencyHz = frequencyHz;
    }

    public Integer getFrequencySubChannel() {
        return frequencySubChannel;
    }

    public void setFrequencySubChannel(Integer frequencySubChannel) {
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

}
