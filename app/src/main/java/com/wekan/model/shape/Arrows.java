package com.wekan.model.shape;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;

import com.wekan.Interface.AbstractShap;
import com.wekan.utils.PaintUtil;

public class Arrows extends AbstractShap {
    final float headPosition[] =
            {
                    // head up face
                    0.0f, 1.0f, 0.5f,
                    -1.0f, -0.0f, 0.5f,
                    1.0f, 0.0f, 0.5f,

                    // head back face
                    -1.0f, 0.0f, 0.5f,
                    -1.0f, 0.0f, -0.5f,
                    1.0f, 0.0f, 0.5f,
                    -1.0f, 0.0f, -0.5f,
                    1.0f, 0.0f, -0.5f,
                    1.0f, 0.0f, 0.5f,

                    // head left face
                    0.0f, 1.0f, 0.5f,
                    0.0f, 1.0f, -0.5f,
                    -1.0f, 0.0f, 0.5f,
                    0.0f, 1.0f, -0.5f,
                    -1.0f, 0.0f, -0.5f,
                    -1.0f, 0.0f, 0.5f,

                    // head right face
                    1.0f, 0.0f, 0.5f,
                    1.0f, 0.0f, -0.5f,
                    0.0f, 1.0f, 0.5f,
                    1.0f, 0.0f, -0.5f,
                    0.0f, 1.0f, -0.5f,
                    0.0f, 1.0f, 0.5f,

                    // head down face
                    0.0f, 1.0f, -0.5f,
                    1.0f, 0.0f, -0.5f,
                    -1.0f, -0.0f, -0.5f,
            };
    final float tailPosition[] = {
            // tail up face
            -0.5f, 0.0f, 0.5f,
            -0.5f, -1.0f, 0.5f,
            0.5f, 0.0f, 0.5f,

            // tail back face
            -0.5f, -1.0f, 0.5f,
            -0.5f, -1.0f, -0.5f,
            0.5f, -1.0f, 0.5f,

            // tail left face
            -0.5f, 0.0f, 0.5f,
            -0.5f, 0.0f, -0.5f,
            -0.5f, -1.0f, 0.5f,

            // tail right face
            0.5f, -1.0f, 0.5f,
            0.5f, -1.0f, -0.5f,
            0.5f, 0.0f, 0.5f,

            // tail down face
            0.5f, 0.0f, -0.5f,
            0.5f, -1.0f, -0.5f,
            -0.5f, 0.0f, -0.5f,
    };

    final float[] headColor =
            {
                    // head up  face (red)
                    1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,

                    // Front face (green)
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    // Front face (green)
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    // Front face (green)
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,

                    // Front face (green)
                    1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,

            };
    final float[] tailColor =
            {
                    // Front face (green)
                    1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,
                    // back face (green)
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,

                    // back face (green)
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,

                    // back face (green)
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    // back face (green)
                    // Front face (green)
                    1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,
            };
    private final float[] position = null;
    private final float[] color = null;

    public Arrows() {
        super();
        setViewMatrix();
        initProgram();
        GLES20.glUseProgram(mProgram);
        float[] position = PaintUtil.mergeArr(headPosition, PaintUtil.getVertexArrRectAll(tailPosition, 0));
        float[] color = PaintUtil.mergeArr(headColor, tailColor);
        setVertex(position);
        setColor(color);
    }

    @Override
    public void draw() {
        this.draw(position, color);
    }

    public void draw(float[] position, float[] color) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        super.draw();
    }


}