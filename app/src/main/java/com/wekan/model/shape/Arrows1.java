package com.wekan.model.shape;

import android.opengl.GLES20;

import com.wekan.Interface.AbstractShap1;
import com.wekan.utils.PaintUtil;

public class Arrows1 extends AbstractShap1 {
    final float headPosition[] =
            {
                    // head up face
                    0.0f, 1.0f, 0.2f,
                    -1.0f, -0.0f, 0.2f,
                    1.0f, 0.0f, 0.2f,

                    // head back face
                    -1.0f, 0.0f, 0.2f,
                    -1.0f, 0.0f, -0.2f,
                    1.0f, 0.0f, 0.2f,
                    -1.0f, 0.0f, -0.2f,
                    1.0f, 0.0f, -0.2f,
                    1.0f, 0.0f, 0.2f,

                    // head left face
                    0.0f, 1.0f, 0.2f,
                    0.0f, 1.0f, -0.2f,
                    -1.0f, 0.0f, 0.2f,
                    0.0f, 1.0f, -0.2f,
                    -1.0f, 0.0f, -0.2f,
                    -1.0f, 0.0f, 0.2f,

                    // head right face
                    1.0f, 0.0f, 0.2f,
                    1.0f, 0.0f, -0.2f,
                    0.0f, 1.0f, 0.2f,
                    1.0f, 0.0f, -0.2f,
                    0.0f, 1.0f, -0.2f,
                    0.0f, 1.0f, 0.2f,

                    // head down face
                    0.0f, 1.0f, -0.2f,
                    1.0f, 0.0f, -0.2f,
                    -1.0f, -0.0f, -0.2f,
            };
    final float tailPosition[] = {
            // tail up face
            -0.5f, 0.0f, 0.2f,
            -0.5f, -1.0f, 0.2f,
            0.5f, 0.0f, 0.2f,

            // tail back face
            -0.5f, -1.0f, 0.2f,
            -0.5f, -1.0f, -0.2f,
            0.5f, -1.0f, 0.2f,

            // tail left face
            -0.5f, 0.0f, 0.2f,
            -0.5f, 0.0f, -0.2f,
            -0.5f, -1.0f, 0.2f,

            // tail right face
            0.5f, -1.0f, 0.2f,
            0.5f, -1.0f, -0.2f,
            0.5f, 0.0f, 0.2f,

            // tail down face
            0.5f, 0.0f, -0.2f,
            0.5f, -1.0f, -0.2f,
            -0.5f, 0.0f, -0.2f,
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
    private float[] position = null;
    private float[] color = null;

    public Arrows1() {
        this(new float[0], new float[0]);
    }

    public Arrows1(float[] otherPosition, float[] otherColor) {
        super();
        setViewMatrix();
        initProgram();
        GLES20.glUseProgram(mProgram);
        float[] position = PaintUtil.mergeArr(headPosition, PaintUtil.getVertexArrRectAll(tailPosition, 0));
        float[] color = PaintUtil.mergeArr(headColor, tailColor);
        if (null != otherPosition && null != otherColor) {
            position = PaintUtil.mergeArr(position, otherPosition);
            color = PaintUtil.mergeArr(color, otherColor);
        }
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