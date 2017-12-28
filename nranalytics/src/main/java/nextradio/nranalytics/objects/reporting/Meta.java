package nextradio.nranalytics.objects.reporting;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gkondati on 12/13/2017.
 */

public class Meta {

    @SerializedName("version")
    private String version;

    public String getVersion() {
        return version;
    }

    public Meta setVersion(String version) {
        this.version = version;
        return this;
    }
}

