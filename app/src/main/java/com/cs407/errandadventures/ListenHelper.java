package com.cs407.errandadventures;

import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public interface ListenHelper extends LocationListener, GpsStatus.Listener{
    public void onLocationChanged(Location loc);
    public void onProviderDisabled(String provider);
    public void onProviderEnabled(String provider);
    public void onStatusChanged(String provider, int status, Bundle extras);

    public void onGPSStatusChanged(int event);
}
