package lab.gps3.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.GpsSatellite;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import lab.gps3.R;
import lab.gps3.view.GpsStatusView;

public class SatelliteActivity extends Activity implements android.location.GpsStatus.Listener, LocationListener {

    private LocationManager locationManager = null;
    private GpsStatusView gpsStatusView;
    boolean draw = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.satellite_view);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        locationManager.addGpsStatusListener(this);
        drawGps(new View(this));

    }

    public void drawGps(View v) {
        gpsStatusView = new GpsStatusView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll_main);

        if (draw == false)
            ll.removeViewAt(ll.getChildCount() - 1);
        this.draw = false;
        ll.addView(gpsStatusView, params);
    }

    public void showMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }


    @Override
    public void onGpsStatusChanged(int event) {
        android.location.GpsStatus gpsStatus = locationManager.getGpsStatus(null);

        if (gpsStatus != null) {
            int i = 0;
            GpsStatusView.satellites.clear();
            for (GpsSatellite satellite : gpsStatus.getSatellites()) {

                GpsStatusView.satellites.add(satellite);
                i++;
            }
            gpsStatusView.invalidate();
//            Toast.makeText(this, "onGpsStatusChanged: " + i, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onProviderEnabled(String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onProviderDisabled(String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
