package com.wekan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.wekan.view.LogView;

/**
 * A login screen that offers login via email/password.
 */
public class MapActivity extends AbstractActivity implements OnGetPoiSearchResultListener {
    MapView mMapView;
    BaiduMap mBaiduMap;
    EditText mSearchWord;
    Button mButton;
    PoiSearch mPoiSearch;
    TextView mPoiMsg;
    Button mSeeBtn;
    RelativeLayout mMsgBar;

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
        mPoiSearch.setOnGetPoiSearchResultListener(this);
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
            mPoiSearch.searchInCity((new PoiCitySearchOption())
                    .city("北京")
                    .keyword(word)
                    .pageNum(10));
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
