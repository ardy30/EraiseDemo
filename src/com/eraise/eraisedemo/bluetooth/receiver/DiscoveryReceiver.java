package com.eraise.eraisedemo.bluetooth.receiver;

import java.util.Iterator;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;

import com.eraise.eraisedemo.bluetooth.adapter.DeviceAdapter;
import com.eraise.eraisedemo.utils.Toast;

/**
 * 扫描广播接收器
 * @author 思落羽
 * 2014年10月24日 下午2:01:10
 *
 */
public class DiscoveryReceiver extends BroadcastReceiver {
	
	private long time;
	private BluetoothAdapter mBtAdapter;
	private Button btnSearch;
	private DeviceAdapter mAdapter;
	
	public DiscoveryReceiver (BluetoothAdapter btAdpater, Button btn, DeviceAdapter adapter) {
		this.mBtAdapter = btAdpater;
		this.btnSearch = btn;
		this.mAdapter = adapter;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (mBtAdapter.isDiscovering()) {
			btnSearch.setEnabled(false);
			// 清空原有数据
			mAdapter.clear();
			mAdapter.notifyDataSetChanged();
			// 显示已配对设备
			showBonded();
			Toast.showToast("开始扫描");
			time = System.currentTimeMillis();
		} else {
			btnSearch.setEnabled(true);
			Toast.showToast("扫描结束");
			System.out.println("扫描时长 -> " + (System.currentTimeMillis() - time));
		}
	}
	
	private void showBonded() {
		mAdapter.clear();
		Iterator<BluetoothDevice> iterator = mBtAdapter.getBondedDevices().iterator();
		while (iterator.hasNext()) {
			mAdapter.addDevice(iterator.next());
		}
		mAdapter.notifyDataSetChanged();
	}
	
}