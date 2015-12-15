package com.wekan.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.wekan.AbstractActivity;
import com.wekan.model.Position;

import java.io.IOException;

/**
 * Created by yuanyuan06 on 2015/12/4.
 */
public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private SurfaceHolder mHolder;
    private Camera mCamera;

    public void init() {
        mCamera = getCameraInstance();
        // 安装一个SurfaceHolder.Callback，
        // 这样创建和销毁底层surface时能够获得通知。
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    /**
     * 安全获取Camera对象实例的方法
     */

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // 试图获取Camera实例
        } catch (Exception e) {
            // 摄像头不可用（正被占用或不存在）
        }
        return c; // 不可用则返回null

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
        try {
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // 如果预览无法更改或旋转，注意此处的事件
        // 确保在缩放或重排时停止预览
        if (mHolder.getSurface() == null) {
            // 预览surface不存在
            return;
        }
        // 更改时停止预览
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // 忽略：试图停止不存在的预览
        }
//        Camera.Parameters parameters = mCamera.getParameters();// 获取mCamera的参数对象
//        parameters.setPreviewSize(w, h);// 设置预览图片尺寸
//        mCamera.setParameters(parameters);
        try {
            mCamera.startPreview();
        } catch (Exception e) {
            if (mCamera != null) {
                mCamera.release();
                mCamera = null;
            }
        }
        // 在此进行缩放、旋转和重新组织格式
        // 以新的设置启动预览
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
