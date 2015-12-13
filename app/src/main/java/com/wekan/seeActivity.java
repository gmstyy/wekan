package com.wekan;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wekan.model.Position;
import com.wekan.view.CameraSurfaceView;
import com.wekan.view.LogView;
import com.wekan.view.WekanView;

/**
 * A login screen that offers login via email/password.
 */
public class seeActivity extends AbstractActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    private final double EARTH_RADIUS = 6378137.0;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    // UI references.

    private EditText xView;
    private EditText yView;
    private EditText zView;
    private WekanView displayView;
    private CameraSurfaceView cameraView;
    private Button resetBtn;
    private LocationManager lm;
    private String GpsProvider;
    private SensorManager sm;
    private Position targetPostion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see);
        initGps();
        initSensor();
        initView();
        startLocationMonitor();
        startRotationSensor();
    }


    private double getDistance(Position p1, Position p2) {
        double radLat1 = (p1.x * Math.PI / 180.0);
        double radLat2 = (p2.x * Math.PI / 180.0);
        double a = radLat1 - radLat2;
        double b = (p1.y - p2.y) * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private void initGps() {
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(true);
        criteria.setCostAllowed(true);
        GpsProvider = lm.getBestProvider(criteria, true);
//        GpsProvider = LocationManager.NETWORK_PROVIDER;
    }

    private void initSensor() {
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    private void initView() {
        logView = (LogView) findViewById(R.id.logView);
        xView = (EditText) findViewById(R.id.tp_x);
        yView = (EditText) findViewById(R.id.tp_y);
        zView = (EditText) findViewById(R.id.tp_z);
        displayView = (WekanView) findViewById(R.id.display_view);
        cameraView = (CameraSurfaceView) findViewById(R.id.camera_view);
        resetBtn = (Button) findViewById(R.id.resetBtn);
        //天安门的位置
        targetPostion = new Position(116.403694, 39.916042, 0.0);
        displayView.init(targetPostion);
        cameraView.init(targetPostion);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            showMessage("GPS permiss denied");
//        } else {
//            targetPostion = new Position(lm.getLastKnownLocation(GpsProvider));
//
//        }
        xView.setText(new Double(targetPostion.x).toString());
        yView.setText(new Double(targetPostion.y).toString());
        zView.setText(new Double(targetPostion.z).toString());
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayView.reset();
            }
        });
    }

    /**
     * 获得gps数据并封装在postion里
     *
     * @return
     */
    private void startLocationMonitor() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lm.requestLocationUpdates(GpsProvider, 500, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Position tp = new Position(location);
                displayView.update(tp);
//                showMessage(tp.toString());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                showMessage("Gps status：" + provider + " status:" + status);
            }

            @Override
            public void onProviderEnabled(String provider) {
                showMessage("Gps enable：" + provider);
            }

            @Override
            public void onProviderDisabled(String provider) {
                showMessage("Gps disable：" + provider);
            }
        });
        showMessage("provider:" + GpsProvider + " " + new Position(lm.getLastKnownLocation(GpsProvider)).toString());
    }

    /**
     * 获得gps数据并封装在postion里
     *
     * @return
     */
    private void startRotationSensor() {

        Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sm.registerListener(new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                float[] arr = new float[16];
                SensorManager.getRotationMatrixFromVector(
                        arr, event.values);
                displayView.update(arr);
//                String text = "";
//                for (int i = 0; i < arr.length; i++) {
//                    if (i % 4 == 0) {
//                        text += "\n";
//                    }
//                    text += arr[i] + "|";
//                }
//                showMessage(text);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }, sensor, SensorManager.SENSOR_DELAY_NORMAL);


    }


    public void showMessage(String msg) {
        super.showMessage("See", msg);
    }
}

