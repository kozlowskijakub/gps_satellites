package lab.gps3.activities;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import lab.gps3.R;
import lab.gps3.database.DatabaseHandler;
import lab.gps3.logicObjects.POI;
import lab.gps3.logicObjects.Track;
import lab.gps3.view.MapLocation;

import java.util.ArrayList;


public class MapActivity extends Activity implements LocationListener {

    public static ArrayList<POI> poiList = new ArrayList<POI>();

    public static double maxNorth;
    public static double maxSouth;
    public static double maxEast;
    public static double maxWest;
    private static double distanceValue;
    private static Integer numberOfPOI;
    private static long trackNumber;

    private POI poi;
    private LocationManager myLocationManager;
    private TextView lattitudeView;
    private TextView longitudeView;
    private TextView distanceView;
    private MapLocation myLocationView;
    private boolean firstPoi = true;
    private boolean isRecording = false;
    private static DatabaseHandler databaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);
        myLocationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        this.lattitudeView = (TextView) findViewById(R.id.tv_lattitude);
        this.longitudeView = (TextView) findViewById(R.id.tv_longitude);
        this.distanceView = (TextView) findViewById(R.id.tv_distance);

        myLocationView = new MapLocation(this);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll_mapView);
        ll.addView(myLocationView, ll.getChildCount());

        this.databaseHandler = DatabaseHandler.getInstance(this);
    }

    private static long lastTime;
    private static long startTime;
    //ms
    private static long periodBetweenPOIs = 4000;

    @Override
    public void onLocationChanged(Location location) {
        if (isRecording) {
            this.poi = new POI(location);
            this.poi.order_id = MapActivity.numberOfPOI;
            MapActivity.numberOfPOI++;


            if (firstPoi == false) {
                POI lastPoi = poiList.get(poiList.size() - 1);
                if ((poi.getTime() - lastTime) > periodBetweenPOIs) {
                    lastTime = poi.getTime();
                    MapActivity.distanceValue += this.poi.distanceTo(lastPoi);
                    poiList.add(poi);
                    Log.i("countedTime", String.valueOf(lastTime - startTime));
                }
                this.distanceView.setText(String.format("%.2f km, poi: %d", MapActivity.distanceValue / 1000, poiList.size()));
                checkMaximumDimensions(this.poi);

                // olny first iteration
            } else {
                lastTime = poi.getTime();
                startTime = poi.getTime();
                poiList.add(poi);
                MapActivity.maxNorth = location.getLatitude();
                MapActivity.maxSouth = location.getLatitude();
                MapActivity.maxEast = location.getLongitude();
                MapActivity.maxWest = location.getLongitude();
            }

            this.firstPoi = false;

            this.lattitudeView.setText(String.valueOf(poi.getLatitude()));
            this.longitudeView.setText(String.valueOf(poi.getLongitude()));
            myLocationView.invalidate();
        }
    }


    public void checkMaximumDimensions(POI poi) {
        if (poi.getLongitude() < maxWest) {
            maxWest = poi.getLongitude();
        }
        if (poi.getLongitude() > maxEast) {
            maxEast = poi.getLongitude();
        }
        if (poi.getLatitude() > maxNorth) {
            maxNorth = poi.getLatitude();
        }
        if (poi.getLatitude() < maxSouth) {
            maxSouth = poi.getLatitude();
        }
    }


    public void recording(View view) {
        if (((Button) view).getText().toString().equals("Start recording")) {
            ((Button) view).setText("Stop recording");
            this.isRecording = true;
            numberOfPOI = 0;
            distanceValue = 0;
            firstPoi = true;
            poiList = new ArrayList<POI>();

            Track track = new Track();
            track.time = 10;
            track.distance = MapActivity.distanceValue;
            track.name = "tmpName";
            track.altitude = 13.13;
//            databaseHandler.dropDatabase(this);
            databaseHandler.createTrack(track);
            trackNumber = databaseHandler.selectMaxTrackID();

            // save track in eth (set in settings if save automatically)
            //
//            Location loca = poiList.get(0);

        } else {
            ((Button) view).setText("Start recording");
            this.isRecording = false;
            Track track = new Track();
            track.time = 10;
            track.distance = MapActivity.distanceValue;
            track.name = "tmpName";
            track.altitude = 13.13;
            databaseHandler.createTrack(track);
            databaseHandler.createPOIs(poiList, trackNumber);
        }
    }

    public void drawGPS(View view) {
        Intent intent = new Intent(this, SatelliteActivity.class);

        startActivity(intent);

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
