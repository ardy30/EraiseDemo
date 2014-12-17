package com.eraise.eraisedemo.activity;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.EditText;

import com.eraise.eraisedemo.R;

public class SensorDemo extends Activity{
	
	protected EditText et;
	
	/**
	 * 传感器监听，传感器并非所有的手机都具有的，
	 * 一般手机会拥有光线传感器和加速度传感器，
	 * 加速度传感器可以用来做摇一摇功能
	 */
	protected MySensorEventListener msel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor_demo);
		
		et = (EditText) findViewById(R.id.et);
		init();
	}
	
	protected void init() {
		
		/* 实例化传感器监听 */
		msel = new MySensorEventListener();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		/* 传感器管理器 */
		SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		/* 得到的传感器类型为加速传感器 */
		Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		/* 把传感器监听注册到传感器，最后一个参数是接收事件的时间间隔长度，
		 * 值必须为SensorManager的常量  SENSOR_DELAY_NORMAL, SENSOR_DELAY_UI, 
		 * SENSOR_DELAY_GAME, or SENSOR_DELAY_FASTEST 中的一个 */
		sm.registerListener(msel, sensor, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		/* 传感器管理器 */
		SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sm.unregisterListener(msel);
	}
	
	/**
	 * 传感器监听，根据加速度来做摇一摇
	 * @author: 思落羽
	 * @date: 2014-2-13
	 * @Description:
	 */
	protected class MySensorEventListener implements SensorEventListener {
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			
			switch(event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				System.out.println("加速传感器");
				break;
			case Sensor.TYPE_ORIENTATION:
				System.out.println("方向传感器");
				break;
			case Sensor.TYPE_AMBIENT_TEMPERATURE:
				System.out.println("温度传感器");
				break;
			case Sensor.TYPE_LIGHT:
				System.out.println("光线传感器");
				break;
			case Sensor.TYPE_GYROSCOPE:
				System.out.println("陀螺仪传感器");
				break;
			case Sensor.TYPE_ALL:
				System.out.println("所有类型传感器");
				break;
			}
//			et.append(event.sensor.toString());
			float[] mFloat = event.values;
			/* 三个方向中的一个的加速度达到这个速度就摇一摇被触动 */
			float medumValue = 19;
			if (Math.abs(mFloat[0]) > medumValue || Math.abs(mFloat[1]) > medumValue || Math.abs(mFloat[2]) > medumValue) {
				StringBuilder sb = new StringBuilder();
				for (float f : mFloat) {
					sb.append(f + " , ");
				}
				et.append(sb);
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

			System.out.println(accuracy);
		}
		
	}
	
}
