package com.wekan.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.AttributeSet;

import com.baidu.mapapi.utils.DistanceUtil;
import com.wekan.AbstractActivity;
import com.wekan.model.Position;
import com.wekan.model.shape.Arrows;
import com.wekan.model.shape.Cube;
import com.wekan.utils.Opengl3DHelper;
import com.wekan.utils.PaintUtil;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


/**
 * Created by yuanyuan06 on 2015/12/4.
 */
public class WekanView extends GLSurfaceView implements GLSurfaceView.Renderer {

    private Opengl3DHelper mHelper;
    private Arrows mArrows;
    private float[] mVertex;
    private float[] mColor;
    private Position mSourcePosition;
    private Position mTargetPosition;
    private float[] mRotationMatrix = new float[16];
    private final double EARTH_RADIUS = 6378137.0;

    public void init(Position sourcePosition, Position targetPisition) {
        this.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setRenderer(this);
        Matrix.setIdentityM(mRotationMatrix, 0);
        mArrows = new Arrows();
        if (null != sourcePosition) {
            updateSource(sourcePosition);
        }
        updateTarget(targetPisition);
    }

    public float[] genRotationMatrixBy2Vector(double[] source, double[] target) {
//        showMessage("unitDx:" + target[0] + " unitDy:" + target[1]);
        double dotProduct = source[0] * target[0] + source[1] * target[1] + source[2] * target[2];
        double rotationAngle = Math.acos(dotProduct / normalize(source[0], source[1], source[2]) / normalize(target[0], target[1], target[2]));
        rotationAngle = rotationAngle * 180.0f / Math.PI;
        double[] rotationAxis = crossProduct(source, target);
//        showMessage("a:" + rotationAngle + "x:" + rotationAxis[0] + "y:" + rotationAxis[1] + "z:" + rotationAxis[2]);
        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.rotateM(mRotationMatrix, 0, d2f(rotationAngle), d2f(rotationAxis[0]), d2f(rotationAxis[1]), d2f(rotationAxis[2]));
        return mRotationMatrix;
    }

    private float d2f(double n) {
        return new Double(n).floatValue();
    }

    public double[] getPostionVector(Position source, Position target) {
        Position tx = new Position(target.x, source.y, target.z);
        Position sy = new Position(source.x, target.y, source.z);
        double dx = getDistance(source, tx);
        dx = source.x > target.x ? -dx : dx;
        double dy = getDistance(source, sy);
        dy = source.y > target.y ? -dy : dy;
        showMessage("dx:" + dx + " dy:" + dy + " dd:" + getDistance(source, target));
        double dz = target.z - source.z;
        return getUnitVector(dx, dy, dz);
    }

    double[] crossProduct(double[] a, double[] b) {
        double[] c = new double[3];
        c[0] = a[1] * b[2] - a[2] * b[1];
        c[1] = a[2] * b[0] - a[0] * b[2];
        c[2] = a[0] * b[1] - a[1] * b[0];
        return c;
    }

    double normalize(double x, double y, double z) {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }

    private double[] getUnitVector(double x, double y, double z) {
        double[] rt = new double[3];
        double length = normalize(x, y, z);
        if (length == 0) {
            rt[0] = 1.0f;
            rt[1] = 1.0f;
            rt[2] = 1.0f;
        } else {
            rt[0] = new Double(x) / length;
            rt[1] = new Double(y) / length;
            rt[2] = new Double(z) / length;
        }
//        showMessage("vx:" + rt[0] + "vy:" + rt[1] + "vz:" + rt[2]);
        return rt;
    }


    private double getDistance(Position p1, Position p2) {
        return DistanceUtil.getDistance(p1.getLatLng(), p2.getLatLng());
//        double radLat1 = (p1.x * Math.PI / 180.0);
//        double radLat2 = (p2.x * Math.PI / 180.0);
//        double a = radLat2 - radLat1;
//        double b = (p2.y - p1.y) * Math.PI / 180.0;
//        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
//                + Math.cos(radLat1) * Math.cos(radLat2)
//                * Math.pow(Math.sin(b / 2), 2)));
//        s = s * EARTH_RADIUS;
//        s = Math.round(s * 10000) / 10000;
//        return s;
    }

    public void updateSource(Position sourcePosition) {
//        showMessage("setsource");
        this.mSourcePosition = sourcePosition;
        genDirectionRotationMat();
    }

    public void updateTarget(Position targetPosition) {
//        showMessage("setposition");
        this.mTargetPosition = targetPosition;
        genDirectionRotationMat();
    }

    private void genDirectionRotationMat() {
        if (null == mSourcePosition || null == mTargetPosition) {
            return;
        }
        genRotationMatrixBy2Vector(new double[]{0.0d, 1.0d, 0.0d}, getPostionVector(mSourcePosition, mTargetPosition));
        double disInMeter = getDistance(mSourcePosition, mTargetPosition);
        float dis = dis2coordinateUnit(normalize(disInMeter, 0, mTargetPosition.z - mSourcePosition.z));
        showMessage("distance:" + disInMeter);
        Cube cube = new Cube(dis / 10, new float[]{0.0f, dis, 0.0f});
        mVertex = PaintUtil.mergeArr(mArrows.getVertexes(), cube.getVertexes());
        mColor = PaintUtil.mergeArr(mArrows.getColors(), cube.getColors());
        if (null == mHelper) {
            return;
        }
//        showMessage("新顶点:" + mVertex.length + " arrows:" + mArrows.getVertexes().length + " cube" + cube.getVertexes().length);
//        showMessage("新颜色:" + mColor.length);
        mHelper.setVertex(mVertex);
        mHelper.setColor(mColor);
//        if (null != mRotationMatrix) {
//            mHelper.setModelMatrix(mRotationMatrix);
//        }
        requestRender();
    }

    private float dis2coordinateUnit(double dis) {
        return (new Double(dis).floatValue() * 2) / 0.062f - 5;
    }

    private Float initDegree;

    public void updateSensorMaxtrix(float[] sensorMatrix, float degreeA) {
        if (null == mHelper) return;
        initDegree = (degreeA) * -1.0f;
        Matrix.rotateM(sensorMatrix, 0, initDegree, 0, 0, 1);
        float[] tmp = new float[16];
        if (null != mRotationMatrix) {
            Matrix.setIdentityM(tmp, 0);
            Matrix.multiplyMM(tmp, 0, sensorMatrix, 0, mRotationMatrix, 0);
        } else {
            tmp = sensorMatrix;
        }
        mHelper.setModelMatrix(tmp);
        requestRender();
//        cube.loadMatrix(rotationMatrix);
    }

    public void reset() {
        if (null == mHelper) return;
        mHelper.reset();
        requestRender();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (null == mHelper) return;
        mHelper.draw();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
//        gl.glViewport(0, 0, width, height);
        showMessage("openGl changed");
    }

    public WekanView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public WekanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        showMessage("startWekan");
        setEGLContextClientVersion(2);

    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        showMessage("openGl created");
        mHelper = new Opengl3DHelper((AbstractActivity) getContext());
        mHelper.setVertex(mVertex);
        mHelper.setColor(mColor);
        mHelper.setProjectionMatrix(getWidth(), getHeight());
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
