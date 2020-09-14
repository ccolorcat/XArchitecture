package x.common.component.core;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import x.common.component.Lazy;
import x.common.util.Utils;


/**
 * Author: cxx
 * Date: 2020-08-26
 * GitHub: https://github.com/ccolorcat
 */
public final class ClientInfo {
    // App的版本号
    @SerializedName("v")
    private String versionName;
    // 手机设备唯一标识
    @SerializedName("did")
    private String deviceId;
    // 手机厂商
    @SerializedName("dh")
    private String deviceBrand;
    // 设备型号,如iPhone9,2
    @SerializedName("db")
    private String productModel;
    // 操作系统版本号
    @SerializedName("dv")
    private String osVersion;
    // 设备名称
    @SerializedName("dm")
    private String deviceName;
    // pad or phone
    private String deviceType;

    private Lazy<String> userAgent = Lazy.by(() -> "TeamMix/" + versionName
            + "(Android;" + deviceBrand + ";" + productModel + ";" + osVersion + ";)"
            + deviceType
    );
    private Lazy<Map<String, String>> commonQueries = Lazy.by(() -> {
        Map<String, String> result = new HashMap<>(5);
        result.put("v", versionName);
        result.put("did", deviceId);
        result.put("dh", deviceBrand);
        result.put("db", productModel);
        result.put("dv", "Android " + osVersion);
        result.put("dm", deviceName);
//        result.put("lat", base.lat);
//        result.put("lng", base.lng);
//        result.put("t", base.t);
        return Collections.synchronizedMap(result);
    });

    public ClientInfo(
            @NonNull String versionName,
            @NonNull String deviceId,
            @NonNull String deviceBrand,
            @NonNull String productModel,
            @NonNull String osVersion,
            @NonNull String deviceName,
            @NonNull String deviceType
    ) {
        this.versionName = Utils.requireNonNull(versionName);
        this.deviceId = Utils.requireNonNull(deviceId);
        this.deviceBrand = Utils.requireNonNull(deviceBrand);
        this.productModel = Utils.requireNonNull(productModel);
        this.osVersion = Utils.requireNonNull(osVersion);
        this.deviceName = Utils.requireNonNull(deviceName);
        this.deviceType = Utils.requireNonNull(deviceType);
    }

    @NonNull
    public String getVersionName() {
        return versionName;
    }

    @NonNull
    public String getDeviceId() {
        return deviceId;
    }

    @NonNull
    public String getDeviceBrand() {
        return deviceBrand;
    }

    @NonNull
    public String getProductModel() {
        return productModel;
    }

    @NonNull
    public String getOsVersion() {
        return osVersion;
    }

    @NonNull
    public String getDeviceName() {
        return deviceName;
    }

    @NonNull
    public String getDeviceType() {
        return deviceType;
    }

    @NonNull
    public String asUserAgent() {
        return userAgent.get();
    }

    @NonNull
    public Map<String, String> asCommonQueries(long timeMillis, String longitude, String latitude) {
        Map<String, String> result = new HashMap<>(commonQueries.get());
        result.put("t", String.valueOf(timeMillis / 1000L));
        if (Utils.isNotEmpty(longitude)) result.put("lng", longitude);
        if (Utils.isNotEmpty(latitude)) result.put("lat", latitude);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientInfo that = (ClientInfo) o;
        return Objects.equals(versionName, that.versionName) &&
                Objects.equals(deviceId, that.deviceId) &&
                Objects.equals(deviceBrand, that.deviceBrand) &&
                Objects.equals(productModel, that.productModel) &&
                Objects.equals(osVersion, that.osVersion) &&
                Objects.equals(deviceName, that.deviceName) &&
                Objects.equals(deviceType, that.deviceType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(versionName, deviceId, deviceBrand, productModel, osVersion, deviceName, deviceType);
    }

    @NonNull
    @Override
    public String toString() {
        return "ClientInfo{" +
                "versionName='" + versionName + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", deviceBrand='" + deviceBrand + '\'' +
                ", productModel='" + productModel + '\'' +
                ", osVersion='" + osVersion + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", deviceType='" + deviceType + '\'' +
                '}';
    }
}
