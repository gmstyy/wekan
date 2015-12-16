package com.wekan.utils;

import java.util.Arrays;

/**
 * Created by yuanyuan06 on 2015/12/13.
 */
public class PaintUtil {

    public static float[] transformVertex(float[] vertexes, float rate, float[] tranVector) {
        if (null != tranVector && tranVector.length >= 3) {
            for (int i = vertexes.length - 1; i >= 0; i--) {
                vertexes[i] = vertexes[i] * rate + tranVector[i % 3];
            }
        }
        return vertexes;
    }

    public static float[] getVertexArrRect(float[] vertexArr, int offset) {
        float[] rt = new float[18];
        for (int i = 0; i < 9; i++) {
            rt[i] = vertexArr[offset + i];
        }
        for (int i = 0; i < 3; i++) {
            rt[i + 9] = vertexArr[offset + i + 3];
        }
        for (int i = 0; i < 3; i++) {
            rt[i + 12] = vertexArr[offset + i + 6] - vertexArr[offset + i] + vertexArr[offset + i + 3];
        }
        for (int i = 0; i < 3; i++) {
            rt[i + 15] = vertexArr[offset + i + 6];
        }
        return rt;
    }

    public static float[] getVertexArrRectAll(float[] vertexArr, int offset) {
        int size = (vertexArr.length - offset) / 9;
        float[] result = new float[size * 18];
        for (int i = 0; i < size; i++) {
            result = concatArr(result, i * 18, getVertexArrRect(vertexArr, i * 9));
        }
        return result;
    }

    public static float[] mergeArr(float[] first, float[] second) {
        float[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static float[] concatArr(float[] result, int offset, float[] second) {
        System.arraycopy(second, 0, result, offset, second.length);
        return result;
    }
}
