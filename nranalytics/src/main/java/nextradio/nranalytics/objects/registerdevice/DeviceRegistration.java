package nextradio.nranalytics.objects.registerdevice;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gkondati on 12/5/2017.
 */

public class DeviceRegistration {

    @SerializedName("data")
    private DeviceRegistrationData data;

    public DeviceRegistrationData getData() {
        return data;
    }

    public void setData(DeviceRegistrationData data) {
        this.data = data;
    }

}
