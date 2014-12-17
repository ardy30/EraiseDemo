package com.eraise.eraisedemo.bluetooth.receiver;

import com.eraise.eraisedemo.bluetooth.adapter.DeviceAdapter;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * 发现设备
 * @author 思落羽
 * 2014年10月24日 下午2:01:41
 *
 */
public class DeviceFoundReceiver extends BroadcastReceiver {
	
	private DeviceAdapter mAdapter;
	
	public DeviceFoundReceiver (DeviceAdapter adapter) {
		this.mAdapter = adapter;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		if (!mAdapter.isContain(device)) {
			mAdapter.addDevice(device);
			mAdapter.notifyDataSetChanged();
		}
		System.out.println("发现新设备 -> " + device.getName());
	}
	
}
