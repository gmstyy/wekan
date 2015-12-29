package com.wekan.view;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.wekan.AbstractActivity;

/**
 * Created by yuanyuan06 on 2015/12/4.
 */
public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        showMessage("destroy");
        try {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        } catch (Exception e) {
            showMessage("camera Release error");
        }

    }

    private SurfaceHolder mHolder;
    private Camera mCamera;

    public boolean init() {
        /**
         * 安全获取Camera对象实例的方法
         */
        if (mCamera == null) {
            try {
                mCamera = Camera.open(); // 试图获取Camera实例
            } catch (Exception e) {
                showMessage("相机不可用");
                return false;
            }
            // 安装一个SurfaceHolder.Callback，
            // 这样创建和销毁底层surface时能够获得通知。
            mHolder = getHolder();
            mHolder.addCallback(this);
        }
        return true;
    }


    public CameraSurfaceView(Context context) {
        //开启相机
        //将本surface对象加入到相机图层中
        //初始化画箭头的对象
        super(context);

    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // surface已被创建，现在把预览画面的位置通知摄像头
        showMessage("created");
        try {
            if (!init()) {
                return;
            }
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

    }

    private void showMessage(String message) {
        try {
            AbstractActivity activity = (AbstractActivity) getContext();
            activity.showMessage("3D", message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
