package nextradio.nranalytics.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gkondati on 12/11/2017.
 */

public class NRLocationObject {

    @SerializedName("type")
    private String type;

    @SerializedName("createTime")
    private String createTime;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("hAccuracy")
    private String hAccuracy;

    @SerializedName("speed")
    private String speed;

    @SerializedName("ipAddress")
    private String ipAddress;

    @SerializedName("gpsUtcTime")
    private String gpsUtcTime;

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

    public String getHAccuracy() {
        return hAccuracy;
    }

    public void setHAccuracy(String hAccuracy) {
        this.hAccuracy = hAccuracy;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getGpsUtcTime() {
        return gpsUtcTime;
    }

    public void setGpsUtcTime(String gpsUtcTime) {
        this.gpsUtcTime = gpsUtcTime;
    }

}
