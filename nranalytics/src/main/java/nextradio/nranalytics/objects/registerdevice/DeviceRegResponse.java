package nextradio.nranalytics.objects.registerdevice;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gkondati on 12/5/2017.
 */

public class DeviceRegResponse {

    @SerializedName("data")
    private DeviceRegResponseInfo data;

    public DeviceRegResponseInfo getData() {
        return data;
    }

    public void setData(DeviceRegResponseInfo data) {
        this.data = data;
    }
}
