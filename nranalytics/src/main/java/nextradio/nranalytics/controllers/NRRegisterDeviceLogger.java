package nextradio.nranalytics.controllers;

import android.util.Log;

import java.net.URLEncoder;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import nextradio.nranalytics.objects.registerdevice.DeviceRegResponse;
import nextradio.nranalytics.objects.registerdevice.DeviceRegistration;
import nextradio.nranalytics.objects.registerdevice.DeviceRegistrationData;
import nextradio.nranalytics.web.TagStationApiClientRequest;

/**
 * Created by gkondati on 11/3/2017.
 */

class NRRegisterDeviceLogger {
    private static final String TAG = "NRRegisterDeviceLogger";

    private NRDeviceDescriptor deviceDescriptor;

    NRRegisterDeviceLogger() {
        deviceDescriptor = new NRDeviceDescriptor(NRAppContext.getAppContext());
    }

    /**
     * The method registers the device with the TAG service and generates
     * a device ID in the process
     *
     * @param radioSourceName : send "unknown" if no radio source
     */
    void registerDevice(String radioSourceName, String fmSourceName) {
        Observable.fromCallable(() -> deviceDescriptor.getDeviceDescription(radioSourceName, fmSourceName))
                .subscribeOn(Schedulers.io())
                .subscribe(this::register, Throwable::printStackTrace);
    }

    /**
     * do all registration process
     */
    private void register(DeviceRegistrationData deviceState) {
        DeviceRegistration deviceRegistration = new DeviceRegistration();
        deviceState.setCountry(Locale.getDefault().getISO3Country());
        deviceState.setLocale(Locale.getDefault().toString());
        deviceRegistration.setData(deviceState);

        String lastDeviceRegistrationString = NRPersistedAppStorage.getInstaince().getDeviceString();
        String newDeviceRegistrationString = deviceState.getUpdateString();

        //Log.d(TAG, "register: " + deviceRegistration.getData().toString());
        Log.d(TAG, "getDeviceId: " + NRPersistedAppStorage.getInstaince().getDeviceId());

        if (!isFullyRegistered() || lastDeviceRegistrationString == null) { //new registration
            TagStationApiClientRequest.getInstance()
                    .registerDevice(deviceRegistration)
                    .subscribeOn(Schedulers.io())
                    .subscribe(deviceRegResponse -> {
                        NRPersistedAppStorage.getInstaince().setDeviceString(newDeviceRegistrationString);
                        saveDeviceRegResponse(deviceRegResponse);
                    }, this::handleError);

        } else if (!lastDeviceRegistrationString.equals(newDeviceRegistrationString)) {//update
            try {
                TagStationApiClientRequest.getInstance()
                        .updateRegisteredDevice(URLEncoder.encode(NRPersistedAppStorage.getInstaince().getDeviceId(), "UTF-8"), deviceRegistration)
                        .subscribeOn(Schedulers.io())
                        .subscribe(deviceRegResponse -> {
                            NRPersistedAppStorage.getInstaince().setDeviceString(newDeviceRegistrationString);
                            saveDeviceRegResponse(deviceRegResponse);
                        }, this::handleError);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveDeviceRegResponse(DeviceRegResponse deviceRegResponse) {
       // Log.d(TAG, "saveDeviceRegResponse: " + deviceRegResponse.getData().getTsd());
        NRPersistedAppStorage.getInstaince().setDeviceRegistration(deviceRegResponse.getData());
        NRPersistedAppStorage.getInstaince().setDeviceId(deviceRegResponse.getData().getTsd());
    }

    private void handleError(Throwable error) {
        //reg failed
       // Log.d(TAG, "handleError: " + error.getLocalizedMessage());
    }

    private boolean isFullyRegistered() {
        String deviceId = NRPersistedAppStorage.getInstaince().getDeviceId();
        return deviceId != null && deviceId.length() > 0;
    }
}
