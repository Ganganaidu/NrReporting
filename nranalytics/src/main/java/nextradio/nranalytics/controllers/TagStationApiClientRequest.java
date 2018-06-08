package nextradio.nranalytics.controllers;

import io.reactivex.Completable;
import io.reactivex.Observable;
import nextradio.nranalytics.objects.GdPrApprovalObject;
import nextradio.nranalytics.objects.registerdevice.DeviceRegResponse;
import nextradio.nranalytics.objects.registerdevice.DeviceRegistration;
import nextradio.nranalytics.objects.reporting.ReportingDataObject;

/**
 * Created by gkondati on 7/6/2016.
 */
class TagStationApiClientRequest {
    //private static final String TAG = "TagStationApiClientRequest";
    private static TagStationApiClientRequest _instance;

    private TagStationApiClientRequest() {

    }

    static TagStationApiClientRequest getInstance() {
        if (_instance == null) {
            _instance = new TagStationApiClientRequest();
        }
        return _instance;
    }

    Observable<DeviceRegResponse> registerDevice(DeviceRegistration deviceRegistration) {
        IRestRequestInterface request = RestAPIRequest.getRetrofit().create(IRestRequestInterface.class);
        return request.registerDevice(deviceRegistration)
                .map(deviceRegResponse -> deviceRegResponse);
    }

    Observable<DeviceRegResponse> updateRegisteredDevice(String tsId, DeviceRegistration deviceRegistration) {
        IRestRequestInterface request = RestAPIRequest.getRetrofit().create(IRestRequestInterface.class);
        return request.updateRegisteredDevice(tsId, deviceRegistration)
                .map(deviceRegResponse -> deviceRegResponse);
    }

    Completable reportData(String tsId, ReportingDataObject<Object> reportingDataObject) {
        IRestRequestInterface request = RestAPIRequest.getRetrofit().create(IRestRequestInterface.class);
        return request.reportData(tsId, reportingDataObject);
    }

    /**
     * @param sdkVersion: required; follows format of ts.reporting-<sdkType>-<sdkVersion>
     */
    Observable<GdPrApprovalObject> initilizeSDK(String countryCOde, String sdkVersion) {
        IRestRequestInterface request = RestAPIRequest.getRetrofit().create(IRestRequestInterface.class);
        return request.initializeSdK(countryCOde, sdkVersion)
                .map(gdprApproved -> gdprApproved);
    }
}
