package nextradio.nranalytics.controllers;

import android.util.Log;

import java.net.URLEncoder;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import nextradio.nranalytics.objects.registerdevice.DeviceRegResponse;
import nextradio.nranalytics.objects.registerdevice.DeviceRegistration;
import nextradio.nranalytics.objects.registerdevice.DeviceRegistrationData;
import nextradio.nranalytics.utils.AppUtils;

/**
 * Created by gkondati on 11/3/2017.
 */

class NRRegisterDeviceLogger {
    private static final String TAG = "NRRegisterDeviceLogger";

    private NRDeviceDescriptor deviceDescriptor;
    private CompositeDisposable disposable = new CompositeDisposable();

    NRRegisterDeviceLogger() {
        deviceDescriptor = new NRDeviceDescriptor(NRAppContext.getAppContext());
    }


    void initSdk(String radioSourceName, String fmSourceName) {
        disposable.add(TagStationApiClientRequest.getInstance()
                .initilizeSDK(AppUtils.getDeviceCountryCode(NRAppContext.getAppContext()), NextRadioReportingSDK.SDK_VERSION)
                .subscribeOn(Schedulers.io())
                .subscribe(gdPrApprovalObject -> {
                    NRPersistedAppStorage.getInstance().setGdprApprovalStatus(gdPrApprovalObject.getGdprApproved());
                    NRRegisterDeviceLogger.this.registerDevice(radioSourceName, fmSourceName);
                }, Throwable::printStackTrace));
    }

    /**
     * The method registers the device with the TAG service and generates
     * a device ID in the process
     *
     * @param radioSourceName : send "unknown" if no radio source
     */
    private void registerDevice(String radioSourceName, String fmSourceName) {
        disposable.add(Observable.fromCallable(() -> deviceDescriptor.getDeviceDescription(radioSourceName, fmSourceName))
                .subscribeOn(Schedulers.io())
                .subscribe(this::register, Throwable::printStackTrace));
    }

    /**
     * do all registration process
     */
    private void register(DeviceRegistrationData deviceState) {
        DeviceRegistration deviceRegistration = new DeviceRegistration();
        deviceState.setCountry(Locale.getDefault().getISO3Country());
        deviceState.setLocale(Locale.getDefault().toString());
        deviceRegistration.setData(deviceState);

        String lastDeviceRegistrationString = NRPersistedAppStorage.getInstance().getDeviceString();
        String newDeviceRegistrationString = deviceState.getUpdateString();

        //Log.d(TAG, "register: " + deviceRegistration.getData().toString());
        Log.d(TAG, "getDeviceId: " + NRPersistedAppStorage.getInstance().getDeviceId());

        if (!isFullyRegistered() || lastDeviceRegistrationString == null) { //new registration
            disposable.add(TagStationApiClientRequest.getInstance()
                    .registerDevice(deviceRegistration)
                    .subscribeOn(Schedulers.io())
                    .subscribe(deviceRegResponse -> {
                        NRPersistedAppStorage.getInstance().setDeviceString(newDeviceRegistrationString);
                        saveDeviceRegResponse(deviceRegResponse);
                    }, this::handleError));

        } else if (!lastDeviceRegistrationString.equals(newDeviceRegistrationString)) {//update
            try {
                disposable.add(TagStationApiClientRequest.getInstance()
                        .updateRegisteredDevice(URLEncoder.encode(NRPersistedAppStorage.getInstance().getDeviceId(), "UTF-8"), deviceRegistration)
                        .subscribeOn(Schedulers.io())
                        .subscribe(deviceRegResponse -> {
                            NRPersistedAppStorage.getInstance().setDeviceString(newDeviceRegistrationString);
                            saveDeviceRegResponse(deviceRegResponse);
                        }, this::handleError));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveDeviceRegResponse(DeviceRegResponse deviceRegResponse) {
        // Log.d(TAG, "saveDeviceRegResponse: " + deviceRegResponse.getData().getTsd());
        NRPersistedAppStorage.getInstance().setDeviceRegistration(deviceRegResponse.getData());
        NRPersistedAppStorage.getInstance().setDeviceId(deviceRegResponse.getData().getTsd());
    }

    private void handleError(Throwable error) {
        //reg failed
        Log.d(TAG, "handleError: " + error.getLocalizedMessage());
    }

    private boolean isFullyRegistered() {
        String deviceId = NRPersistedAppStorage.getInstance().getDeviceId();
        return deviceId != null && deviceId.length() > 0;
    }

    void clear() {
        if (disposable != null) {
            disposable.clear();
        }
    }
}
