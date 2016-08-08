/**
 * ***************************************************************
 * @ClassName:  shakeActivity 
 * 
 * @Desc : 摇一摇activity
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 司明
 * 
 * @data:2016-2-15 下午2:54:55
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */

package com.lesports.stadium.activity;

import com.lesports.stadium.R;

import android.app.Activity;
import android.app.Service;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;

/**
 * @author simon
 * 
 */
public class ShakeActivity extends Activity implements SensorEventListener {
	/**
	 * 定义sensor管理器
	 */
	private SensorManager mSensorManager;
	/**
	 * 震动
	 */
	private Vibrator vibrator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shake);
		// 获取传感器管理服务
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		// 震动
		vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);

	}

	@Override
	protected void onResume() {
		super.onResume();
		// 加速度传感器

		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				// 还有SENSOR_DELAY_UI、SENSOR_DELAY_FASTEST、SENSOR_DELAY_GAME等，

				// 根据不同应用，需要的反应速率不同，具体根据实际情况设定

				SensorManager.SENSOR_DELAY_NORMAL);

	}

	@Override
	protected void onStop() {

		mSensorManager.unregisterListener(this);

		super.onStop();

	}

	@Override
	protected void onPause() {

		mSensorManager.unregisterListener(this);

		super.onPause();

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

		// TODO Auto-generated method stub

		// 当传感器精度改变时回调该方法，Do nothing.

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub

		int sensorType = event.sensor.getType();

		// values[0]:X轴，values[1]：Y轴，values[2]：Z轴

		float[] values = event.values;

		if (sensorType == Sensor.TYPE_ACCELEROMETER) {

			/*
			 * 因为一般正常情况下，任意轴数值最大就在9.8~10之间，只有在你突然摇动手机
			 * 
			 * 的时候，瞬时加速度才会突然增大或减少。
			 * 
			 * 所以，经过实际测试，只需监听任一轴的加速度大于14的时候，改变你需要的设置
			 * 
			 * 就OK了~~~
			 */

			if ((Math.abs(values[0]) > 16 || Math.abs(values[1]) > 16 || Math
					.abs(values[2]) > 16)) {

				// 摇动手机后，再伴随震动提示~~

				vibrator.vibrate(500);
			}
		}
	}

}
