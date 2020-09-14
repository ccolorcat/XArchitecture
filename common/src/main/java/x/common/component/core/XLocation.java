package x.common.component.core;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-09-14
 * GitHub: https://github.com/ccolorcat
 */
public final class XLocation implements Serializable {
    private String province;
    private String city;
    private String district;
    private String address;

    private String longitude;
    private String latitude;
    private float radius;

    XLocation() {
    }

    XLocation(String province, String city, String district, String address, String longitude, String latitude, float radius) {
        this.province = province;
        this.city = city;
        this.district = district;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.radius = radius;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public double getLongitudeAsDouble(double defaultValue) {
        try {
            return Utils.isEmpty(longitude) ? defaultValue : Double.parseDouble(longitude);
        } catch (Throwable t) {
            return defaultValue;
        }
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public double getLatitudeAsDouble(double defaultValue) {
        try {
            return Utils.isEmpty(latitude) ? defaultValue : Double.parseDouble(latitude);
        } catch (Throwable t) {
            return defaultValue;
        }
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XLocation xLocation = (XLocation) o;
        return Float.compare(xLocation.radius, radius) == 0 &&
                Objects.equals(province, xLocation.province) &&
                Objects.equals(city, xLocation.city) &&
                Objects.equals(district, xLocation.district) &&
                Objects.equals(address, xLocation.address) &&
                Objects.equals(longitude, xLocation.longitude) &&
                Objects.equals(latitude, xLocation.latitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(province, city, district, address, longitude, latitude, radius);
    }

    @NonNull
    @Override
    public String toString() {
        return "XLocation{" +
                "province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", address='" + address + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", radius=" + radius +
                '}';
    }
}
