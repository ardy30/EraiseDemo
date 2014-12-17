package com.eraise.eraisedemo.activity;

import java.io.IOException;
import java.util.Calendar;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eraise.eraisedemo.R;

/**
 * 发送短信需要有权限
 * android.permission.SEND_SMS
 * 接收短信需要权限
 * android.permission.RECEIVE_SMS
 * @author: 思落羽
 * @date: 2014-2-13
 * @Description:
 */
public class SendMessageDemo extends Activity{

	protected EditText et_target;
	protected EditText et_chats;
	protected EditText et_input;
	protected Button btn;
	protected Toast toast;
	
	/**
	 * 短信管理，用来发送短信
	 */
	protected SmsManager smsManager;
	
	/**
	 * 接收短信
	 */
	protected BroadcastReceiver smsReceiver;
	
	/**
	 * 接收短信的过滤器
	 */
	protected IntentFilter smsFilter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_message_demo);
		
		initView();
		init();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		try {
			registerReceiver(smsReceiver, smsFilter);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		try {
			unregisterReceiver(smsReceiver);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		
		int which = intent.getIntExtra("which", 0);
		switch(which) {
		case 1:
			showToast("短信已发送");
			System.out.println("Sent Intent");
			break;
		case 2:
			showToast("短信已送达");
			System.out.println("Delivery Intent");
			break;
		}
	}
	
	protected void initView() {
		
		et_target = (EditText) findViewById(R.id.et_target);
		et_chats = (EditText) findViewById(R.id.et_chats);
		et_input = (EditText) findViewById(R.id.et_input);
		btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(new SendMessage());
	}
	
	protected void init() {
		
		smsManager = SmsManager.getDefault();
		smsReceiver = new ReceiveMessage();
		smsFilter = new IntentFilter();
		smsFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
	}
	
	/**
	 * 显示Toast
	 * @param msg
	 */
	protected void showToast(String msg) {
		
		if (toast == null) {
			toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		} else {
			toast.setText(msg);
		}
		toast.show();
	}
	
	protected class ReceiveMessage extends BroadcastReceiver {
		
		/* AudioManager获取当前系统提示的设置 */
		AudioManager am = 
				(AudioManager) getSystemService(Context.AUDIO_SERVICE);

		@Override
		public void onReceive(Context context, Intent intent) {

			/* 提示 */
			prompt();
			/* 获取短信们 */
			Object[] pdus = (Object[]) intent.getExtras().get("pdus");
			SmsMessage[] messages = new SmsMessage[pdus.length];
			/* 把短信数据从字节数组转到 SmsMessage 对象 */
			for (int i = 0; i < messages.length; i ++) {
				messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
			}
			/* 遍历短信(有可能一条短信被分为几块) */
			for (SmsMessage msg : messages) {
				/* 获取短信发送方 */
				if (msg.getDisplayOriginatingAddress()
						.equals(et_target.getText().toString().trim())) {
					/* 阻止广播的继续扩散 */
					this.abortBroadcast();
					et_chats.append(DateFormat
							.format("yyyy-MM-dd kk:mm:ss", msg.getTimestampMillis()));
					et_chats.append("\n");
					et_chats.append(msg.getDisplayMessageBody());
					et_chats.append("\n");
				}
			}
		}
		
		/**
		 * 根据系统设置进行提示
		 */
		void prompt() {
			
			int ringerMode = am.getRingerMode();
			switch (ringerMode) {
			case AudioManager.RINGER_MODE_NORMAL:
				if (am.shouldVibrate(AudioManager.VIBRATE_TYPE_NOTIFICATION)) {
					vibrator();
				}
				sound();
				break;
			case AudioManager.RINGER_MODE_SILENT:
				break;
			case AudioManager.RINGER_MODE_VIBRATE:
				vibrator();
				break;
			}
		}
		
		void vibrator() {
			
			/* 震动服务 */
			Vibrator vib = 
				(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			/* 震动 */
			vib.vibrate(500);
		}
		
		void sound() {
			
			MediaPlayer mp = new MediaPlayer();
			int volume = am.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
			mp.setVolume(volume, volume);
			try {
				mp.reset();
				mp.setLooping(false);
				mp.setDataSource(SendMessageDemo.this, 
						Settings.System.DEFAULT_NOTIFICATION_URI);
				mp.prepare();
				mp.start();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	protected class SendMessage implements OnClickListener {

		@Override
		public void onClick(View v) {

			String target = et_target.getText().toString().trim();
			if (target.length() == 0) {
				showToast("接收人不能为空");
			}
			String msg = et_input.getText().toString().trim();
			if (msg.length() == 0) {
				showToast("短信不能为空");
			}
			/* 发送短信的Intent */
			Intent sentIntent = 
					new Intent(SendMessageDemo.this, SendMessageDemo.class);
			sentIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | 
					Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			sentIntent.putExtra("which", 1);
			/* 短信到达的Intent */
			Intent deliveryIntent =
					new Intent(SendMessageDemo.this, SendMessageDemo.class);
			deliveryIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | 
					Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			/* 把Intent封装到延迟的PendingIntent中 */
			PendingIntent psi = 
					PendingIntent.getActivity(SendMessageDemo.this, 0, 
							sentIntent, PendingIntent.FLAG_ONE_SHOT);
			PendingIntent pdi = 
					PendingIntent.getActivity(SendMessageDemo.this, 1, 
							deliveryIntent, PendingIntent.FLAG_ONE_SHOT);
			deliveryIntent.putExtra("which", 2);
			/* 发送短信，PendingIntent只能有一个，不知道何解 */
			smsManager.sendTextMessage(target, null, msg, psi, pdi);
			et_input.setText(null);
			et_chats.append(DateFormat.format("yyyy-MM-dd kk:mm:ss", Calendar.getInstance()));
			et_chats.append("\n");
			et_chats.append(msg);
			et_chats.append("\n");
		}
		
	}
	
}
