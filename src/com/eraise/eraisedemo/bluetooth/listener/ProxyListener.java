package com.eraise.eraisedemo.bluetooth.listener;

import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;

import com.eraise.eraisedemo.utils.Toast;

/**
 * 创建连接需要用到的监听
 * @author 思落羽
 * 2014年10月27日 上午11:42:38
 *
 */
public class ProxyListener implements BluetoothProfile.ServiceListener {
	
	BluetoothDevice pDevice;
	
	public ProxyListener (BluetoothDevice device) {
		this.pDevice = device;
	}

	@Override
	public void onServiceConnected(int profile, BluetoothProfile proxy) {
		List<BluetoothDevice> connDevices = proxy.getConnectedDevices();
		for (BluetoothDevice td : connDevices) {
			System.out.println(td.getName());
		}
		switch (profile) {
		case BluetoothProfile.HEADSET:
			System.out.println("耳机连接");
			BluetoothHeadset headset = (BluetoothHeadset) proxy;
			boolean b = headset.startVoiceRecognition(pDevice);
			if (!b) {
				Toast.showToast("耳机连接失败");
			}
			break;
		case BluetoothProfile.A2DP:
			System.out.println("A2DP连接");
			break;
		}
	}

	@Override
	public void onServiceDisconnected(int profile) {
		Toast.showToast("连接断开");
	}
	
}