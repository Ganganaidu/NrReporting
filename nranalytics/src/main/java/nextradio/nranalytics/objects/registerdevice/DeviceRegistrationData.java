package nextradio.nranalytics.objects.registerdevice;

import com.google.gson.annotations.SerializedName;

/**
 * Created by gkondati on 12/5/2017.
 */

public class DeviceRegistrationData {

    @SerializedName("clientName")
    private String clientName;

    @SerializedName("brand")
    private String brand;

    @SerializedName("device")
    private String device;

    @SerializedName("manufacturer")
    private String manufacturer;

    @SerializedName("model")
    private String model;

    @SerializedName("carrier")
    private String carrier;

    @SerializedName("fmapi")
    private String fmapi;

    @SerializedName("adId")
    private String adId;

    @SerializedName("appVersion")
    private String appVersion; //formerly nextradio_version, used for data analysis (4.0.1.1056)

    @SerializedName("country")
    private String country;

    @SerializedName("locale")
    private String locale;

    @SerializedName("systemVersion")
    private String systemVersion;

    @SerializedName("macAddress")
    private String macAddress;

    @SerializedName("systemSoftware")
    private String systemSoftware;

    @SerializedName("sdkVersion")
    private String sdkVersion;

    @SerializedName("otherInfo")
    private String otherInfo;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getFmapi() {
        return fmapi;
    }

    public void setFmapi(String fmapi) {
        this.fmapi = fmapi;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getSystemSoftware() {
        return systemSoftware;
    }

    public void setSystemSoftware(String systemSoftware) {
        this.systemSoftware = systemSoftware;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public String getUpdateString() {
        return brand +
                device +
                manufacturer +
                model +
                fmapi +
                adId +
                appVersion +
                sdkVersion +
                systemVersion;
    }

    public boolean equals() {
        return !(brand == null || brand.length() == 0)
                && !(manufacturer == null || manufacturer.length() == 0)
                && !(locale == null || locale.length() == 0)
                && !(appVersion == null || appVersion.length() == 0)
                && !(systemVersion == null || systemVersion.length() == 0);
    }

    @Override
    public String toString() {
        return "DeviceState{" +
                ", brand='" + brand + '\'' +
                ", device='" + device + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", carrier='" + carrier + '\'' +
                ", otherInfo='" + otherInfo + '\'' +
                ", countryCode='" + country + '\'' +
                ", fmAPI='" + fmapi + '\'' +
                ", adID='" + adId + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", systemVersion='" + systemVersion + '\'' +
                '}';
    }
}
