package nextradio.nranalytics.controllers;

import io.reactivex.Completable;
import io.reactivex.Observable;
import nextradio.nranalytics.objects.GdPrApprovalObject;
import nextradio.nranalytics.objects.registerdevice.DeviceRegResponse;
import nextradio.nranalytics.objects.registerdevice.DeviceRegistration;
import nextradio.nranalytics.objects.reporting.ReportingDataObject;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by gkondati
 */
interface IRestRequestInterface {

    @POST("registration")
    Observable<DeviceRegResponse> registerDevice(@Body DeviceRegistration carIncident);

    @PUT("registration")
    Observable<DeviceRegResponse> updateRegisteredDevice(@Query(value = "tsd", encoded = true) String tsID, @Body DeviceRegistration carIncident);

    @PUT("usage")
    Completable reportData(@Query(value = "tsd", encoded = true) String tsID, @Body ReportingDataObject<Object> reportingDataObject);

    @GET("initialization")
    Observable<GdPrApprovalObject> initializeSdK(@Query("country") String tsID, @Query("sdkVersion") String sdkVersion);

}

