package com.eraise.eraisedemo.bluetooth.adapter;

import java.util.ArrayList;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eraise.eraisedemo.R;

public class DeviceAdapter extends BaseAdapter{
	
	private final Object LOCK = new Object();
	
	private Context mContext;
	private ArrayList<BluetoothDevice> data;
	
	public DeviceAdapter (Context context) {
		data = new ArrayList<BluetoothDevice>();
		this.mContext = context;
	}
	
	public void addDevice(BluetoothDevice device) {
		synchronized (LOCK) {
			data.add(device);
		}
	}

	public void clear() {
		synchronized (LOCK) {
			data.clear();
		}
	}
	
	public boolean isContain(BluetoothDevice device) {
		return data.contains(device);
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public BluetoothDevice getItem(int position) {
		synchronized (LOCK) {
			return data.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.demo_bluetooth_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		BluetoothDevice device = getItem(position);
		holder.tvName.setText(device.getName());
		holder.tvType.setText(getType(device));
		holder.tvAddress.setText(device.getAddress());
		setBg(convertView, device);
		return convertView;
	}
	
	/**
	 * 根据连接状态设置背景
	 */
	private void setBg(View v, BluetoothDevice device) {
		switch (device.getBondState()) {
		case BluetoothDevice.BOND_BONDED:
			v.setBackgroundColor(Color.YELLOW);
			break;
		case BluetoothDevice.BOND_BONDING:
			v.setBackgroundColor(Color.LTGRAY);
			break;
		case BluetoothDevice.BOND_NONE:
			v.setBackgroundColor(Color.TRANSPARENT);
			break;
		}
	}
	
	private String getType(BluetoothDevice device) {
		String type;
		switch (device.getBluetoothClass().getMajorDeviceClass()) {
		case BluetoothClass.Device.Major.AUDIO_VIDEO:
			type = "多媒体设备/耳机/音箱";
			break;
		case BluetoothClass.Device.Major.COMPUTER:
			type = "电脑";
			break;
		case BluetoothClass.Device.Major.HEALTH:
			type = "健康设备";
			break;
		case BluetoothClass.Device.Major.IMAGING:
			type = "扫描仪";
			break;
		case BluetoothClass.Device.Major.MISC:
			type = "音乐";
			break;
		case BluetoothClass.Device.Major.NETWORKING:
			type = "网络";
			break;
		case BluetoothClass.Device.Major.PERIPHERAL:
			type = "外设";
			break;
		case BluetoothClass.Device.Major.PHONE:
			type = "手机";
			break;
		case BluetoothClass.Device.Major.TOY:
			type = "玩具";
			break;
		case BluetoothClass.Device.Major.WEARABLE:
			type = "可写";
			break;
		default:
			type = "未知设备";
			break;
		}
		return type;
	}
	
	private class ViewHolder {
		TextView tvName, tvType, tvAddress;
		
		ViewHolder (View v) {
			tvName = (TextView) v.findViewById(R.id.tv_name);
			tvType = (TextView) v.findViewById(R.id.tv_type);
			tvAddress = (TextView) v.findViewById(R.id.tv_address);
		}
	}

}
