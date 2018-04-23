package nextradio.nranalytics.controllers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import nextradio.nranalytics.objects.registerdevice.DeviceRegistrationData;

class NRDeviceDescriptor {
    // private static final String TAG = "NRDeviceDescriptor";


    private Context mContext;
    private DeviceRegistrationData returnVal;

    NRDeviceDescriptor(Context context) {
        mContext = context;
    }

    DeviceRegistrationData getDeviceDescription(String radioSourceName, String fmSourceName) {
        if (returnVal != null) {
            return returnVal;
        }
        //nr_android :// fmSoruce
        returnVal = new DeviceRegistrationData();
        returnVal.setClientName(radioSourceName);//nr_android
        returnVal.setBrand(Build.BRAND);
        returnVal.setDevice(Build.DEVICE);
        returnVal.setManufacturer(Build.MANUFACTURER);
        returnVal.setModel(Build.MODEL);
        returnVal.setFmapi(fmSourceName);
        returnVal.setSystemVersion(Build.FINGERPRINT);
        returnVal.setSdkVersion(NextRadioReportingSDK.SDK_VERSION);
        returnVal.setCountry(Locale.getDefault().getISO3Country());
        returnVal.setLocale(Locale.getDefault().toString());
        returnVal.setSystemSoftware("Android");

        String carrierName = "unknown";
        try {
            TelephonyManager manager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            if (manager != null) {
                carrierName = manager.getNetworkOperatorName();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        returnVal.setCarrier(carrierName);
        returnVal.setAppVersion(getDeviceVersionCode());
        returnVal.setMacAddress(getMacAddress());
        returnVal.setAdId(getGoogleAdId());

        return returnVal;
    }

    private String getGoogleAdId() {
        AdvertisingIdClient.Info adInfo;
        try {
            adInfo = AdvertisingIdClient.getAdvertisingIdInfo(mContext);
            if (adInfo != null) {
                return adInfo.getId();
            }
        } catch (IOException | GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }
        return "error";
    }

    /**
     * @return application manifest version name or current build version name
     */
    private String getDeviceVersionCode() {
        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            return String.valueOf(pInfo.versionName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * gets mac address based on availability
     */
    private String getMacAddress() {
        String macAddress = getMacAddressByWifiInfo();
        if (!"02:00:00:00:00:00".equals(macAddress)) {
            return macAddress;
        }
        macAddress = getMacAddressByNetworkInterface();
        if (!"02:00:00:00:00:00".equals(macAddress)) {
            return macAddress;
        }
        return null;
    }

    /**
     * @return mac address by using network interface
     */
    private String getMacAddressByNetworkInterface() {
        try {
            List<NetworkInterface> nis = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ni : nis) {
                if (!ni.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = ni.getHardwareAddress();
                if (macBytes != null && macBytes.length > 0) {
                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02x:", b));
                    }
                    return res1.deleteCharAt(res1.length() - 1).toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    /**
     * @return mac address by using wifi manager
     */
    @SuppressLint("MissingPermission")
    private String getMacAddressByWifiInfo() {
        try {
            WifiManager wifi = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifi == null) return null;

            WifiInfo info = wifi.getConnectionInfo();
            if (info == null) return null;

            return info.getMacAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }


}
