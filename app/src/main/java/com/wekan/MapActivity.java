package com.wekan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.wekan.model.Position;
import com.wekan.view.LogView;

import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class MapActivity extends AbstractActivity {
    MapView mMapView;
    BaiduMap mBaiduMap;
    EditText mSearchWord;
    Button mButton;
    PoiSearch mPoiSearch;
    TextView mPoiMsg;
    Button mSeeBtn;
    RelativeLayout mMsgBar;
    String mCurrentCity = "北京";
    LocationClient mLocationClient;
    MapListener mapListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logView = (LogView) findViewById(R.id.logView);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);
        //获取地图控件引用

        mSearchWord = (EditText) findViewById(R.id.searchWord);
        mButton = (Button) findViewById(R.id.searchBtn);
        mMsgBar = (RelativeLayout) findViewById(R.id.msgBar);
        mPoiMsg = (TextView) findViewById(R.id.poiMsg);
        mSeeBtn = (Button) findViewById(R.id.seeBtn);
        initMapView();
        initPoi();
        initGps();
        MapListener mapListener = new MapListener();
        SearchListener sl = new SearchListener();
        mButton.setOnClickListener(sl);
        mSearchWord.setOnEditorActionListener(sl);
        mSearchWord.setOnFocusChangeListener(sl);
        mSearchWord.selectAll();

    }

    private void initMapView() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
    }

    public void initPoi() {
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(mapListener);
    }


    private void displayResult(final PoiResult result) {
        mBaiduMap.clear();
        //创建PoiOverlay
        PoiOverlay overlay = new PoiOverlay(mBaiduMap) {
            @Override
            public boolean onPoiClick(int i) {
                if (result.getAllPoi() != null
                        && result.getAllPoi().get(i) != null) {
                    showDetail(result.getAllPoi().get(i));
                } else {
                    showMessage("ERROR", "未选中地址");
                }
                return false;
            }
        };
        //设置overlay可以处理标注点击事件
        mBaiduMap.setOnMarkerClickListener(overlay);
        //设置PoiOverlay数据
        overlay.setData(result);
        //添加PoiOverlay到地图中
        overlay.addToMap();
        overlay.zoomToSpan();
        return;

    }

    private void showDetail(final PoiInfo poiInfo) {
        showMessage("Map", "click poi:" + poiInfo.name);
        resetMapZoomController(true);
        mMsgBar.setVisibility(View.VISIBLE);
        mPoiMsg.setText(poiInfo.name + "\n\n" + poiInfo.address);
        mSeeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SeeActivity.class);
                intent.putExtra("x", poiInfo.location.longitude);
                intent.putExtra("y", poiInfo.location.latitude);
                intent.putExtra("z", 0.0d);
                intent.putExtra("addr", poiInfo.name);
                startActivity(intent);
            }
        });
    }

    private void resetMapZoomController(boolean hasButtom) {
        int buttom = hasButtom ? 250 : 180;
        Point p = new Point(mMapView.getWidth() - 350, mMapView.getHeight() - buttom);
        mMapView.setZoomControlsPosition(p);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    private void initGps() {
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(mapListener);    //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(5000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.requestLocation();
        mLocationClient.start();
    }


    public void searchInCity(String word, int pageNum) {
        mPoiSearch.searchInCity(new PoiCitySearchOption()
                .city(mCurrentCity)
                .keyword(word)
                .pageNum(pageNum).pageCapacity(10));
    }

    private class MapListener implements OnGetPoiSearchResultListener, BDLocationListener, BaiduMap.OnMapClickListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            showMessage("Map", "city:" + location.getCity() + " " + mCurrentCity);
            if (null == location || null == location.getCity()) {
                return;
            }
            mCurrentCity = location.getCity();
        }

        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                showMessage("Map", "poiResult is null");
                return;
            }
            showMessage("Map", "poiResult:" + poiResult.getAllPoi());
            if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
                displayResult(poiResult);
            }
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

        }

        @Override
        public void onMapClick(LatLng latLng) {
        }

        @Override
        public boolean onMapPoiClick(MapPoi mapPoi) {
            return false;
        }
    }

    private class SearchListener implements View.OnClickListener, TextView.OnEditorActionListener, View.OnFocusChangeListener {

        @Override
        public void onClick(View v) {
            mMsgBar.setVisibility(View.GONE);
//            p1 = new Point(mMapView.getWidth() - 350, mMapView.getHeight() - 250);
//            p2 = new Point(mMapView.getWidth() - 350, mMapView.getHeight() - 180);
            resetMapZoomController(false);
            String word = mSearchWord.getText().toString();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mSearchWord.getWindowToken(), 0);
            showMessage("Map", "word:" + word);
            searchInCity(word, 0);
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            /*判断是否是“GO”键*/
            showMessage("input", "" + actionId);
            switch (actionId) {
                case EditorInfo.IME_ACTION_UNSPECIFIED:
                case EditorInfo.IME_ACTION_SEARCH:
                    InputMethodManager imm = (InputMethodManager) v
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                                v.getApplicationWindowToken(), 0);
                    }
                    this.onClick(v);
                    mMsgBar.setVisibility(View.GONE);
                    return true;
            }
                    /*隐藏软键盘*/

            return false;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                mMsgBar.setVisibility(View.GONE);
            }
        }
    }
}
