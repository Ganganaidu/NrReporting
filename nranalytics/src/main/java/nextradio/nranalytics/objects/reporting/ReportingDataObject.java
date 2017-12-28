package nextradio.nranalytics.objects.reporting;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by gkondati on 12/13/2017.
 */

public class ReportingDataObject<T> {

    @SerializedName("meta")
    private Meta meta;

    @SerializedName("data")
    private List<T> data = null;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

}
