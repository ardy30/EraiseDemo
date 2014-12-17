package com.eraise.eraisedemo.bluetooth.receiver;

import com.eraise.eraisedemo.bluetooth.adapter.DeviceAdapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * 配对过程
 * @author 思落羽
 * 2014年10月24日 下午5:20:14
 *
 */
public class BondReceiver extends BroadcastReceiver {
	
	private DeviceAdapter mAdapter;
	
	public BondReceiver(DeviceAdapter adapter) {
		this.mAdapter = adapter;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		mAdapter.notifyDataSetChanged();
	}
	
}