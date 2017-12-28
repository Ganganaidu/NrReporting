package nextradio.nranalytics.objects.registerdevice;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gkondati on 12/5/2017.
 */

public class DeviceRegResponseInfo {

    @SerializedName("tsd")
    private String tsd;

    @SerializedName("cachingGroup")
    private String cachingGroup;

    @SerializedName("adGroup")
    private String adGroup;

    @SerializedName("feedUser")
    private String feedUser;

    public String getTsd() {
        return tsd;
    }

    public void setTsd(String tsd) {
        this.tsd = tsd;
    }

    public String getCachingGroup() {
        return cachingGroup;
    }

    public void setCachingGroup(String cachingGroup) {
        this.cachingGroup = cachingGroup;
    }

    public String getAdGroup() {
        return adGroup;
    }

    public void setAdGroup(String adGroup) {
        this.adGroup = adGroup;
    }

    public String getFeedUser() {
        return feedUser;
    }

    public void setFeedUser(String feedUser) {
        this.feedUser = feedUser;
    }

}
