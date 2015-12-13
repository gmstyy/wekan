package com.wekan.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.wekan.AbstractActivity;
import com.wekan.model.Position;
import com.wekan.model.shape.Cube;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


/**
 * Created by yuanyuan06 on 2015/12/4.
 */
public class WekanView extends GLSurfaceView implements GLSurfaceView.Renderer {

    private Cube cube;

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

    public void update(Position viewPosition) {

    }

    public void update(float[] rotationMatrix) {
        if (null == cube) return;
        cube.setModelMatrix(rotationMatrix);
        requestRender();
//        cube.loadMatrix(rotationMatrix);
    }

    public void reset() {
        if (null == cube) return;
        cube.reset();
        requestRender();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (null == cube) return;
        cube.draw();
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
        showMessage("1");
        cube = new Cube();
        int width = getWidth();
        int height = getHeight();
        GLES20.glViewport(0, 0, width, height);
        cube.setProjectionMatrix(getWidth(), getHeight());
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
