package com.lesports.stadium.engine;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
/**
 * 
 * ***************************************************************
 * @ClassName:  CaptureSensorsObserver 
 * 
 * @Desc : 监视传感器事件
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 王新年
 * 
 * @data:2016-2-14 下午2:57:34
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */
public class CaptureSensorsObserver implements SensorEventListener {
    private RefocuseListener listener;
    private SensorManager mSensorManager;
    private Sensor sensor;
    private boolean waitingFocuse;
    private int staticCount;

    private final static int kMotiveThresholdCount = 2;
    private final static int kStaticThresholdCount = 3;
    private static final int MOVE_THRESHOLD = 20;
    private long mLastUpdate;
    float mLastX;
    float mLastY;
    float mLastZ;

    public void setRefocuseListener(RefocuseListener l) {
        this.listener = l;
    }

    public CaptureSensorsObserver(Context context) {
        mSensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);
        sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void start() {
        waitingFocuse = true;
        mSensorManager.registerListener(this, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        mSensorManager.unregisterListener(this, sensor);
    }
    /**
     * 传感器精度的改变时会调用这个方法
     */
    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }
    /**
     * 传感器报告新的值时会调用这个方法
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            if (mLastUpdate + 100 > curTime) {
                return;
            }

            long diffTime = (curTime - mLastUpdate);
            mLastUpdate = curTime;

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float dx = x - mLastX;
            float dy = y - mLastY;
            float dz = z - mLastZ;

            float speed = (float) (Math.sqrt(dx * dx + dy * dy + dz * dz)
                    / diffTime * 10000);
            mLastX = x;
            mLastY = y;
            mLastZ = z;

            //LogEx.i("speed: " + speed + ", MOVE_THRESHOLD: " + MOVE_THRESHOLD);
            if (speed > MOVE_THRESHOLD) {
                staticCount = staticCount < 0 ? --staticCount : -1;
                if (staticCount + kMotiveThresholdCount <= 0) {
                    waitingFocuse = true;
                }
            } else {
                staticCount = staticCount > 0 ? ++staticCount : 1;
                if (waitingFocuse && staticCount >= kStaticThresholdCount) {
                    waitingFocuse = false;
                    if (null != listener) {
                        listener.needFocuse();
                    }
                }
            }
        }
    }

    public interface RefocuseListener {
        public void needFocuse();
    }
}
