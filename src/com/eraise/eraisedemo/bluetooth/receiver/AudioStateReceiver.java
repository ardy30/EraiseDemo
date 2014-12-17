package com.eraise.eraisedemo.bluetooth.receiver;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.eraise.eraisedemo.utils.Toast;

/**
 * {@linkplain BluetoothHeadset#ACTION_AUDIO_STATE_CHANGED} 监听
 * @author 思落羽
 * 2014年10月27日 下午2:38:03
 *
 */
public class AudioStateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		int state = intent.getIntExtra(BluetoothHeadset.EXTRA_STATE, -1);
		String stateStr = "未知";
		switch (state) {
		case BluetoothHeadset.STATE_AUDIO_CONNECTED:
			stateStr = "已连接";
			System.out.println("BluetoothHeadset.STATE_CONNECTED");
			break;
		case BluetoothHeadset.STATE_AUDIO_CONNECTING:
			System.out.println("BluetoothHeadset.STATE_AUDO_CONNECTING");
			break;
		case BluetoothHeadset.STATE_AUDIO_DISCONNECTED:
			stateStr = "已关闭";
			System.out.println("BluetoothHeadset.STATE_AUDIO_DISCONNECTED");
			break;
		}
		BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		Toast.showToast(device.getName() + " : " + stateStr);
	}
	
}