package com.wekan.model.shape;

import com.wekan.utils.PaintUtil;

public class Arrows extends AbstractShap {
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

    public Arrows() {
        this(1.0f, null);
    }

    public Arrows(float rate, float[] tranVector) {
        super();
        mVertexes = PaintUtil.mergeArr(headPosition, PaintUtil.getVertexArrRectAll(tailPosition, 0));
        mColors = PaintUtil.mergeArr(headColor, tailColor);
        PaintUtil.transformVertex(mVertexes, rate, tranVector);
    }
}