package nextradio.nranalytics.controllers;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.Locale;

/**
 * Created by gkondati on 12/19/2017.
 */

public class NrUAppUtils {

    /**
     * @return true if data is empty
     */
    static boolean isNullOrEmpty(String data) {
        return data == null || data.isEmpty();
    }

    static String getDeviceCountryCode(Context context) {

        String countryCode = getCountryCodeFromSIM(context);
        if (!TextUtils.isEmpty(countryCode)) {
            return countryCode;
        }

        if (TextUtils.isEmpty(countryCode)) {
            countryCode = getCountryCodeFromDefaultLocale();
        }

        if (TextUtils.isEmpty(countryCode)) {
            countryCode = getCountryCodeFromConfigurationLocale(context);
        }

        return countryCode;
    }

    private static String getCountryCodeFromDefaultLocale() {
        String country = Locale.getDefault().getCountry();
        if (TextUtils.isEmpty(country)) {
            country = null;
        }
        return country;
    }

    private static String getCountryCodeFromConfigurationLocale(Context context) {
        return context.getResources().getConfiguration().locale.getCountry();
    }

    /**
     * Get ISO 3166-1 alpha-2 country code for this device (or null if not available)
     *
     * @return country code or null
     */
    private static String getCountryCodeFromSIM(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                final String simCountry = tm.getSimCountryIso();
                if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                    return simCountry.toLowerCase(Locale.US);
                } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                    String networkCountry = tm.getNetworkCountryIso();
                    if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                        return networkCountry.toLowerCase(Locale.US);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
