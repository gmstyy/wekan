package com.wekan.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.wekan.AbstractActivity;
import com.wekan.Interface.AbstractShap;
import com.wekan.model.Position;
import com.wekan.model.shape.Arrows;
import com.wekan.model.shape.Cube;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


/**
 * Created by yuanyuan06 on 2015/12/4.
 */
public class WekanView extends GLSurfaceView implements GLSurfaceView.Renderer {

    private AbstractShap shap;
    private Position targetPosition;
    private final double EARTH_RADIUS = 6378137.0;

    public void init(Position targetPisition) {
//        Renderer render = new CubeDemo();
//        setRenderer(render);
//        this.setZOrderOnTop(true);//设置画布  背景透明
//
        this.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setRenderer(this);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
//        startCamera();
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

    public void update(Position viewPosition) {

    }

    public void update(float[] rotationMatrix) {
        if (null == shap) return;
        shap.setModelMatrix(rotationMatrix);
        requestRender();
//        cube.loadMatrix(rotationMatrix);
    }

    public void reset() {
        if (null == shap) return;
        shap.reset();
        requestRender();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (null == shap) return;
        shap.draw();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
//        gl.glViewport(0, 0, width, height);
    }

    public WekanView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
//        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public WekanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
//        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        setEGLConfigChooser(true);
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        shap = new Arrows();
        int width = getWidth();
        int height = getHeight();
        GLES20.glViewport(0, 0, width, height);
        shap.setProjectionMatrix(getWidth(), getHeight());
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
