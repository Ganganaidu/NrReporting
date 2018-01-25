package nextradio.nranalytics.controllers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Enumeration;

import nextradio.nranalytics.utils.DateFormats;
import nextradio.nranalytics.utils.DateUtils;
import nextradio.nranalytics.utils.GsonConverter;

/**
 * Created by gkondati on 7/28/2015.
 */
class NRLocationAdapter {

    private static final String TAG = "NRLocationAdapter";

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 90000; //30000

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Provides access to the Location Settings API.
     */
    private SettingsClient mSettingsClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Callback for Location events.
     */
    private LocationCallback mLocationCallback;

    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;

    /**
     * Represents last saved location.
     */
    private Location mLastLocation;

    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    private boolean mRequestingLocationUpdates;

    private double mSpeed = 0;

    private OnLocationFailedListener locationFailedListener;

    private NRPersistedAppStorage persistedAppStorage;

    /**
     * we need this failure listener to update user to switch on location when location updates failed to initiate
     */
    public interface OnLocationFailedListener {
        void locationFailed(int code, Exception e);
    }

    private static NRLocationAdapter _instance;

    public static NRLocationAdapter getInstance() {
        if (_instance == null) {
            _instance = new NRLocationAdapter();
        }
        return _instance;
    }

    /**
     * initiate all objects that needed for storing and retrieving location
     */
    void init(NRPersistedAppStorage appStorage) {
        persistedAppStorage = appStorage;

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(NRAppContext.getAppContext());

        mSettingsClient = LocationServices.getSettingsClient(NRAppContext.getAppContext());

        createLocationRequest();

        createLocationCallback();

        buildLocationSettingsRequest();
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        //update priority to get accurate location.. be aware this will effect device battery
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    /**
     * Creates a callback for receiving location events.
     */
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mRequestingLocationUpdates = true;
                mCurrentLocation = locationResult.getLastLocation();

                onLocationChanged(mCurrentLocation);
            }
        };
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    boolean isLocationUpdatesStarted() {
        return mRequestingLocationUpdates;
    }

    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    @SuppressLint("MissingPermission")
    void startLocationUpdates() {
        if (mRequestingLocationUpdates) {
            return;
        }
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(locationSettingsResponse -> {
                    Log.i(TAG, "All location settings are satisfied.");
                    //noinspection MissingPermission
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                })
                .addOnFailureListener(e -> {
                    int statusCode = ((ApiException) e).getStatusCode();
                    //location failed
                    mRequestingLocationUpdates = false;
                    mCurrentLocation = null;
                    Log.d(TAG, "addOnFailureListener: " + statusCode);
                    if (locationFailedListener != null) {
                        locationFailedListener.locationFailed(statusCode, e);
                    }
                });
    }

    private void onLocationChanged(Location location) {
        if (mLastLocation == null) {
            mLastLocation = location;
        }
        setCurrentLocation(location);
    }

    private boolean isLocationEnabled(Context context) {
        int locationMode;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    /**
     * save location data for every 2 min
     * creating NRLocation object using all the required fields that need to reported for location
     */
    void saveLocationData() {
        Location location = mCurrentLocation;
        if (location == null || !isLocationEnabled(NRAppContext.getAppContext())) {
            mRequestingLocationUpdates = false;
            return;
        }
        String currentUTCString = DateUtils.getCurrentUtcTime();

        JSONObject nrLocationObject = new JSONObject();
        String latitude = String.valueOf(location.getLatitude());
        String longitude = String.valueOf(location.getLongitude());
        String hAccuracy = String.valueOf(location.getAccuracy());
        //String locSpeed = roundTwoDecimals(Double.parseDouble(getLocSpeed()));

        //41.8918027
        if (latitude.isEmpty() && longitude.isEmpty()) {
            return;
        }

        if (!isSameLocation(location)) {
            try {
                nrLocationObject.put("type", "Location");
                nrLocationObject.put("createTime", currentUTCString);
                nrLocationObject.put("latitude", latitude);
                nrLocationObject.put("longitude", longitude);
                nrLocationObject.put("hAccuracy", hAccuracy);
                nrLocationObject.put("ipAddress", String.valueOf(location.getSpeed()));
                nrLocationObject.put("gpsUtcTime", getUtcGpsTime(location.getTime()));


                saveLocationInStorage(nrLocationObject);
                //save prev values to avoid same location
                persistedAppStorage.savePreviousLat(String.valueOf(latitude));
                persistedAppStorage.savePreviousLongitude(String.valueOf(longitude));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mLastLocation = location;
    }

    private String getUtcGpsTime(long time) {
        return DateFormats.msSqlDateFormat(new Date(time));
    }

    private boolean isSameLocation(Location location) {
        if (persistedAppStorage.getPreviousLat().isEmpty() || persistedAppStorage.getPreviousLongitude().isEmpty()) {
            return false;
        }
        double lat2 = Double.parseDouble(persistedAppStorage.getPreviousLat());
        double lng2 = Double.parseDouble(persistedAppStorage.getPreviousLongitude());
        // lat1 and lng1 are the values of a previously stored location
        Log.d(TAG, "distance: " + distance(location, lat2, lng2));
        return (distance(location, lat2, lng2) < 1.0);
    }

    /**
     * Returns the approximate distance in METERS between this old and the current location.
     */
    private double distance(Location location, double lat2, double lng2) {
        Location location2 = new Location("Location 2");

        location2.setLatitude(lat2);
        location2.setLongitude(lng2);

        return location.distanceTo(location2);
    }

    /**
     * convert NRLocation object into json and save json string into preference for later use
     */
    private void saveLocationInStorage(JSONObject nrLocationObject) {
        try {
            String data = GsonConverter.getInstance().createJsonObjectToString(persistedAppStorage.getLocationData(), nrLocationObject);
            persistedAppStorage.saveLocationData(data);
            Log.d(TAG, "saveLocationInStorage: " + data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * get speed of the location
     */
    private String getLocSpeed() {
        if (mCurrentLocation != null && mLastLocation != null) {

            mSpeed = Math.sqrt(Math.pow(mLastLocation.getLongitude() - mCurrentLocation.getLongitude(), 2)
                    + Math.pow(mLastLocation.getLatitude() - mCurrentLocation.getLatitude(), 2))
                    / (mLastLocation.getTime() - mCurrentLocation.getTime());
            mSpeed = (double) Math.round(mSpeed * 100) / 100;

            if (mCurrentLocation.hasSpeed() && mCurrentLocation.getSpeed() > 0) {
                mSpeed = mCurrentLocation.getSpeed();
            }
        }
        return String.valueOf(mSpeed).replace(",", ".");
    }

    /**
     * get ip address from device when we save location
     */
    private String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        //Log.d(TAG, "***** IP="+ ip);
                        return Formatter.formatIpAddress(inetAddress.hashCode());
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(TAG, ex.toString());
        }
        return null;
    }

    private String roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return twoDForm.format(d);
    }


    private void setLocationFailedListener(OnLocationFailedListener listener) {
        locationFailedListener = listener;
    }

    /**
     * @return current Location object
     */
    Location getCurrentLocation() {
        return mCurrentLocation;
    }

    /**
     * we use this to save current location object to use in other places to get location lat/long values
     */
    private void setCurrentLocation(Location mCurrentLocation) {
        this.mCurrentLocation = mCurrentLocation;
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    void stopLocationUpdates() {
        try {
            if (!mRequestingLocationUpdates) {
                Log.d(TAG, "stopLocationUpdates: updates never requested, no-op.");
                return;
            }
            // It is a good practice to remove location requests when the activity is in a paused or
            // stopped state. Doing so helps battery performance and is especially
            // recommended in applications that request frequent location updates.
            mFusedLocationClient.removeLocationUpdates(mLocationCallback).addOnCompleteListener(task -> mRequestingLocationUpdates = false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
