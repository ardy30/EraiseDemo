package com.eraise.eraisedemo.bluetooth.activity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.eraise.eraisedemo.R;
import com.eraise.eraisedemo.bluetooth.adapter.DeviceAdapter;
import com.eraise.eraisedemo.bluetooth.listener.ProxyListener;
import com.eraise.eraisedemo.bluetooth.receiver.AudioStateReceiver;
import com.eraise.eraisedemo.bluetooth.receiver.BondReceiver;
import com.eraise.eraisedemo.bluetooth.receiver.DeviceFoundReceiver;
import com.eraise.eraisedemo.bluetooth.receiver.DiscoveryReceiver;
import com.eraise.eraisedemo.bluetooth.receiver.ProxyReceiver;
import com.eraise.eraisedemo.utils.Toast;

/**
 * 需要有操作蓝牙的权限
 * <uses-permission android:name="android.permission.BLUETOOTH" />
 * <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
 * @author 思落羽
 * 2014年10月24日 上午10:12:57
 *
 */
public class BluetoothDemo extends Activity implements OnClickListener {

	private Button btnOpen;
	private Button btnSearch;
	private Button btnDiscovery;
	private ListView lvContent;
	
	private BluetoothAdapter mBtAdapter;
	
	private StateChangeReceiver mStateChangeReceiver;
	private DiscoveryReceiver mDiscoveryReceiver;
	private DeviceFoundReceiver mDeviceFoundReceiver;
	private BondReceiver mBondReceiver;
	private ProxyReceiver mProxyReceiver;
	private AudioStateReceiver mAudioStateReceiver;
	
	private DeviceAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth_demo);
		init();
	}
	
	private void init() {
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBtAdapter == null) {
			Toast.showToast("没有找到蓝牙设备");
			return;
		}
		findViews();
		initListView();
		showBonded();
	}
	
	private void findViews() {
		btnOpen = (Button)findViewById( R.id.btn_open );
		btnSearch = (Button)findViewById( R.id.btn_search );
		btnDiscovery = (Button)findViewById( R.id.btn_discovery );
		lvContent = (ListView)findViewById( R.id.lv_content );

		btnOpen.setOnClickListener( this );
		btnSearch.setOnClickListener( this );
		btnDiscovery.setOnClickListener( this );
	}
	
	private void initListView() {
		mAdapter = new DeviceAdapter(this);
		lvContent.setAdapter(mAdapter);
		lvContent.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				BluetoothDevice device = mAdapter.getItem(position);
				switch (device.getBondState()) {
				case BluetoothDevice.BOND_BONDED:
					connect(device);
					break;
				case BluetoothDevice.BOND_NONE:
					createBond(device);
					break;
				}
			}
		});
		lvContent.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				BluetoothDevice device = mAdapter.getItem(position);
				if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
					return removeBond(device);
				}
				return false;
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		registerBroadcast();
		setBtnState();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver();
	}
	
	private void registerBroadcast() {
		// 蓝牙状态改
		if (mStateChangeReceiver == null) {
			mStateChangeReceiver = new StateChangeReceiver();
		}
		registerReceiver(mStateChangeReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
		
		// 搜索附近设备
		if (mDiscoveryReceiver == null) {
			mDiscoveryReceiver = new DiscoveryReceiver(mBtAdapter, btnSearch, mAdapter);
		}
		IntentFilter dFilter = new IntentFilter();
		dFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		dFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(mDiscoveryReceiver, dFilter);
		
		// 发现设备
		if (mDeviceFoundReceiver == null) {
			mDeviceFoundReceiver = new DeviceFoundReceiver(mAdapter);
		}
		registerReceiver(mDeviceFoundReceiver,  new IntentFilter(BluetoothDevice.ACTION_FOUND));
		
		// 配对
		if (mBondReceiver == null) {
			mBondReceiver = new BondReceiver(mAdapter);
		}
		registerReceiver(mBondReceiver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));
		
		// 连接
		if (mProxyReceiver == null) {
			mProxyReceiver = new ProxyReceiver();
		}
		registerReceiver(mProxyReceiver, new IntentFilter(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED));
		
		// 音频
		if (mAudioStateReceiver == null) {
			mAudioStateReceiver = new AudioStateReceiver();
		}
		registerReceiver(mAudioStateReceiver, new IntentFilter(BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED));
	}
	
	private void unregisterReceiver() {
		unregisterReceiver(mStateChangeReceiver);
		unregisterReceiver(mDeviceFoundReceiver);
		unregisterReceiver(mDiscoveryReceiver);
		unregisterReceiver(mBondReceiver);
		unregisterReceiver(mProxyReceiver);
		unregisterReceiver(mAudioStateReceiver);
	}

	private void setBtnState() {
		switch (mBtAdapter.getState()) {
		case BluetoothAdapter.STATE_ON:
			btnOpen.setEnabled(true);
			btnOpen.setText("关闭");
//			searchBluetooth();
			break;
		case BluetoothAdapter.STATE_OFF:
			btnOpen.setEnabled(true);
			btnOpen.setText("打开");
			mAdapter.clear();
			mAdapter.notifyDataSetChanged();
			break;
		case BluetoothAdapter.STATE_TURNING_ON:
			btnOpen.setText("打开中");
			btnOpen.setEnabled(false);
			break;	
		case BluetoothAdapter.STATE_TURNING_OFF:
			btnOpen.setText("关闭中");
			btnOpen.setEnabled(false);
			break;
		}
		btnSearch.setEnabled(mBtAdapter.isEnabled());
		btnDiscovery.setEnabled(mBtAdapter.isEnabled());
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_open:
			openBluetooth();
			break;
		case R.id.btn_discovery:
			discovery();
			break;
		case R.id.btn_search:
			searchBluetooth();
			break;
		}
	}
	
	private void openBluetooth() {
		if (mBtAdapter.isEnabled()) {
			mBtAdapter.disable();
		} else {
			if (!mBtAdapter.enable()) {
				Toast.showToast("打开失败，当前飞行模式？");
			}
		}
	}
	
	private void discovery() {
		Intent disIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		disIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
		startActivityForResult(disIntent, 0);
	}
	
	private void searchBluetooth() {
		if (!mBtAdapter.isEnabled()) {
			Toast.showToast("请先打开蓝牙");
		}
		if (!mBtAdapter.isDiscovering()) {
			mBtAdapter.startDiscovery();
		}
	}
	
	/**
	 * 创建连接
	 * @param device
	 */
	private void connect(BluetoothDevice device) {
		// 通过监听获取到连接
		mBtAdapter.getProfileProxy(this, new ProxyListener(device), BluetoothProfile.A2DP);
	}
	
	/**
	 * 进行配对，createBond方法是隐藏的，反射出来
	 * @param device
	 * @return
	 */
	private boolean createBond(BluetoothDevice device) {
		try {
			Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
			return (Boolean) createBondMethod.invoke(device);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return false;		
	}
			
	private boolean removeBond(BluetoothDevice device) {
		try {
			Method removeBondMethod = BluetoothDevice.class.getMethod("removeBond");
			return (Boolean) removeBondMethod.invoke(device);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void showBonded() {
		mAdapter.clear();
		Iterator<BluetoothDevice> iterator = mBtAdapter.getBondedDevices().iterator();
		while (iterator.hasNext()) {
			mAdapter.addDevice(iterator.next());
		}
		mAdapter.notifyDataSetChanged();
	}
	
	/**
	 * 蓝牙状态改变的广播接收器
	 * @author 思落羽
	 * 2014年10月24日 上午10:18:56
	 *
	 */
	private class StateChangeReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			setBtnState();
		}
		
	}
	
}
