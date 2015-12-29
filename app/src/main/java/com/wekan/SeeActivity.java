package com.wekan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.wekan.model.Position;
import com.wekan.view.CameraSurfaceView;
import com.wekan.view.LogView;
import com.wekan.view.WekanView;

import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class SeeActivity extends AbstractActivity implements BDLocationListener, SensorEventListener {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    public LocationClient mLocationClient;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    // UI references.

//    private EditText xView;
//    private EditText yView;
//    private EditText zView;
    private TextView mTitleView;
    private WekanView displayView;
    private CameraSurfaceView cameraView;
    private Button resetBtn;
    private SensorManager sm;
    private Sensor accelerometer; // 加速度传感器
    private Sensor magnetic; // 地磁场传感器
    private Position targetPostion;
    private LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showMessage("see created");
        setContentView(R.layout.activity_see);
        Intent intent = getIntent();
        double x = intent.getDoubleExtra("x", 0.0);
        double y = intent.getDoubleExtra("y", 0.0);
        double z = intent.getDoubleExtra("z", 0.0);
        String address = intent.getStringExtra("addr");
        targetPostion = new Position(x, y, z);
        targetPostion.msg = address;
        initGps();
        initSensor();
        initView();
    }


    private void initGps() {
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(this);    //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 10000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
//        option.setIsNeedAddress(false);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
//        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
//        option.setIsNeedLocationDescribe(false);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.requestLocation();
        mLocationClient.start();
        //启动自带GPS，为了获得海拔
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    /**
     * 返回查询条件
     *
     * @return
     */
    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        //设置是否需要方位信息
        criteria.setBearingRequired(false);
        //设置是否需要海拔信息
        criteria.setAltitudeRequired(true);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
//        // 设置对电源的需求
//        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        //Receive Location
        StringBuffer sb = new StringBuffer(256);
        sb.append("time : ");
        sb.append(location.getTime());
        sb.append("\nerror code : ");
        sb.append(location.getLocType());
        sb.append("\nlatitude : ");
        sb.append(location.getLatitude());
        sb.append("\nlontitude : ");
        sb.append(location.getLongitude());
        sb.append("\nradius : ");
        sb.append(location.getRadius());
        if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
            sb.append("gps定位成功");

        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
            sb.append("网络定位成功");
        } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
            sb.append("\ndescribe : ");
            sb.append("离线定位成功，离线定位结果也是有效的");
        } else if (location.getLocType() == BDLocation.TypeServerError) {
            sb.append("\ndescribe : ");
            sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
        } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
            sb.append("\ndescribe : ");
            sb.append("网络不同导致定位失败，请检查网络是否通畅");
        } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
            sb.append("\ndescribe : ");
            sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
        }
        sb.append("\nlocationdescribe : ");
        sb.append(location.getLocationDescribe());// 位置语义化信息
        List<Poi> list = location.getPoiList();// POI数据
        if (list != null) {
            sb.append("\npoilist size = : ");
            sb.append(list.size());
            for (Poi p : list) {
                sb.append("\npoi= : ");
                sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
            }
        }
        Position tp = new Position(location);

        if (isFineAtitude(tp.z) || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        } else {
            String bestProvider = lm.getBestProvider(getCriteria(), true);
            Location l = lm.getLastKnownLocation(bestProvider);
            showMessage("l : " + l);
            if (null != l && isFineAtitude(l.getAltitude())) {
                tp.setZ(l.getAltitude());
            } else {
                tp.setZ(0);
            }
        }
        displayView.updateSource(tp);
//        showMessage("BaiduLocationApiDem", sb.toString());
    }

    public boolean isFineAtitude(double z) {
        if (z < -10 || z > 10000) {
            return false;
        }
        return true;
    }

    private void initSensor() {
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        showMessage("init sensor:" + sensor);
        // 初始化加速度传感器
//        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        // 初始化地磁场传感器
//        magnetic = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Sensor sensor1 = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, accelerometer, Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, magnetic, Sensor.TYPE_MAGNETIC_FIELD);
        sm.registerListener(this, sensor1, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void releaseSensor() {
        // 初始化地磁场传感器
        sm.unregisterListener(this);
    }

    private void initView() {
        logView = (LogView) findViewById(R.id.logView);
//        xView = (EditText) findViewById(R.id.tp_x);
//        yView = (EditText) findViewById(R.id.tp_y);
//        zView = (EditText) findViewById(R.id.tp_z);
        mTitleView = (TextView) findViewById(R.id.title);
        displayView = (WekanView) findViewById(R.id.display_view);
        cameraView = (CameraSurfaceView) findViewById(R.id.camera_view);
        cameraView.init();
        resetBtn = (Button) findViewById(R.id.resetBtn);
        //target天安门的位置
//        targetPostion = new Position(116.403694, 39.916042, 0.0);
//        //西二旗地铁站
//        targetPostion = new Position(116.312426, 40.05889, 0.0);
        //百度大厦
        displayView.init(new Position(116.312426, 40.05889, 0.0), targetPostion);
//        xView.setText(new Double(targetPostion.x).toString());
//        yView.setText(new Double(targetPostion.y).toString());
//        zView.setText(new Double(targetPostion.z).toString());
        String title = targetPostion.msg;
        if (null == title || title == "") {
            title = "x:" + String.format("%.2d,%.2d,%.2d ", targetPostion.x, targetPostion.y, targetPostion.z);
        }
        mTitleView.setText(title);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayView.reset();
            }
        });
    }

    //    private float[] accelerometerValues = new float[3];
//    private float[] magneticFieldValues = new float[3];
//    float[] values = new float[3];
//    float[] Rr = new float[16];
//    Float degreeN;
    float[] degreeN = new float[3];
    float degreeD;

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ORIENTATION:
                degreeN[0] = event.values[0];
                degreeN[1] = event.values[1];
                degreeN[2] = event.values[2];
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                showMessage("geoRotationVector: " + event.values[0] + " " + event.values[1] + " " + event.values[2]);
                float[] arr = new float[16];
                Matrix.setIdentityM(arr, 0);
                SensorManager.getRotationMatrixFromVector(
                        arr, event.values);
                float[] values1 = new float[3];
                SensorManager.getOrientation(arr, values1);
                float degreeZ = new Double(values1[0] * 180.0f / Math.PI).floatValue();
                float degreeX = new Double(values1[1] * 180.0f / Math.PI).floatValue();
                float degreeY = new Double(values1[2] * 180.0f / Math.PI).floatValue();
                if (null != degreeN && Math.abs(degreeX) < 10) {
                    degreeD = degreeZ - degreeN[0];
//                    showMessage("init " + degreeD + " degreeN:" + degreeN[0] + " " + degreeN[1] + " " + degreeN[2] + " degreeS:" + degreeZ + " " + degreeX + " " + degreeY);
                    showMessage(String.format("init %.2f degreeN:%.2f %.2f %.2f degreeS:%.2f %.2f %.2f", degreeD, degreeN[0], degreeN[1], degreeN[2], degreeZ, degreeX, degreeY));
                }
                displayView.updateSensorMaxtrix(arr, degreeD);
                break;
        }


//        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//            accelerometerValues = event.values;
//        }
//        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
//            magneticFieldValues = event.values;
//        }
//        if (null != accelerometerValues && null != magneticFieldValues) {
//            boolean rt = SensorManager.getRotationMatrix(Rr, null, accelerometerValues,
//                    magneticFieldValues);
//            if (rt) {
//                SensorManager.getOrientation(Rr, values);
//                degreeN = new Double(values[0] * 180.0f / Math.PI).floatValue();
////                showMessage("rt:" + rt + " degreeN:" + degreeN);
//            }
//
//        }
    }

    @Override
    protected void onDestroy() {
        releaseSensor();
        showMessage("see destroy");
        super.onDestroy();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        showMessage("sensor accuracy changed:" + sensor.getName() + ":" + accuracy);
    }

    public void showMessage(String msg) {
        super.showMessage("See", msg);
    }


}

