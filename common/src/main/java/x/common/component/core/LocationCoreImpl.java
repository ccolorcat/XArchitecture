package x.common.component.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

import androidx.annotation.NonNull;

import x.common.IClient;
import x.common.component.XObservableImpl;
import x.common.component.log.Logger;
import x.common.util.AndroidUtils;

/**
 * Author: cxx
 * Date: 2020-09-14
 * GitHub: https://github.com/ccolorcat
 */
final class LocationCoreImpl extends XObservableImpl<XLocation> implements LocationCore {
    private final LocationManager manager;
    private final Criteria criteria;
    private LocationListener listener;

    private LocationCoreImpl(@NonNull IClient client) {
        Context context = client.asAppClient().getApplication();
        manager = AndroidUtils.getSystemService(context, LocationManager.class);
        criteria = createLocationCriteria();
        dispatch(getLastKnownLocation());
    }

    @SuppressLint("MissingPermission")
    @Override
    public void start() {
        if (listener != null) return;
        try {
            //从可用的位置提供器中，匹配以上标准的最佳提供器
            String providerName = manager.getBestProvider(criteria, true);
            if (providerName == null) return;
            listener = new SimpleLocationListener();
            //监视地理位置变化
            manager.requestLocationUpdates(providerName, 5000, 5, listener, Looper.getMainLooper());
        } catch (Throwable t) {
            Logger.getDefault().e(t);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void requestLocation() {
        try {
            manager.requestSingleUpdate(criteria, new SimpleLocationListener(), Looper.getMainLooper());
        } catch (Throwable t) {
            Logger.getDefault().e(t);
        }
    }

    @Override
    public void stop() {
        if (listener != null) {
            manager.removeUpdates(listener);
            listener = null;
        }
    }

    @SuppressLint("MissingPermission")
    private Location getLastKnownLocation() {
        try {
            String providerName = manager.getBestProvider(criteria, true);
            if (providerName != null) {
                return manager.getLastKnownLocation(providerName);
            }
        } catch (Throwable t) {
            Logger.getDefault().e(t);
        }
        return null;
    }

    private void dispatch(Location location) {
        XLocation last = getValue();
        if (location != null) {
            XLocation xLocation = convert(location);
            if (!xLocation.equals(last)) {
                update(xLocation);
            }
        } else if (last == null) {
            update(new XLocation());
        }
    }

    private class SimpleLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            dispatch(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    }

    private static XLocation convert(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        XLocation xLocation = new XLocation();
        xLocation.setLatitude(String.valueOf(latitude));
        xLocation.setLongitude(String.valueOf(longitude));
        return xLocation;
    }

    private static Criteria createLocationCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE); //低精度，如果设置为高精度，依然获取不了location。
        criteria.setAltitudeRequired(false); //不要求海拔
        criteria.setBearingRequired(false); //不要求方位
        criteria.setCostAllowed(true); //允许有花费
        criteria.setPowerRequirement(Criteria.POWER_LOW); //低功耗
        return criteria;
    }
}
