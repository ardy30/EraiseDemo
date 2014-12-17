package com.eraise.eraisedemo.service;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.widget.RemoteViews;

import com.eraise.eraisedemo.EraiseDemoActivity;
import com.eraise.eraisedemo.EraiseDemoApplication;
import com.eraise.eraisedemo.R;
import com.eraise.eraisedemo.widget.WidgetDemo;

/**
 * 定时更新Widget，为了防止被杀，用AlarmManager来做可能更适合。
 * @author: 思落羽
 * @date: 2014-2-17
 * @Description:
 */
public class UpdateWidgetService extends Service {
	
	public Timer timer;
	public TimerTask task;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	
		if (timer == null) {
			timer = new Timer();
		}
		if (task == null) {
			task = new TimerTask() {
				
				@Override
				public void run() {
					
					AppWidgetManager awm = AppWidgetManager
							.getInstance(UpdateWidgetService.this);
					int[] ids = awm.getAppWidgetIds(new ComponentName(
							EraiseDemoApplication.getApplication(), WidgetDemo.class));
					/* 获取RemoteView (桌面的Launcher） */
					RemoteViews view = new RemoteViews(getPackageName(), R.layout.activity_widget);
					view.setCharSequence(R.id.tv, "setText", 
							DateFormat.format("kk:mm:ss", Calendar.getInstance()).toString());
					/* 设置按钮监听，似乎更新的时候每次都要设置，否则不生效 */
					Intent intent = new Intent();
					intent.setClass(UpdateWidgetService.this, EraiseDemoActivity.class);
					/* 设置为new task 才能启动Activity */
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					PendingIntent pi = PendingIntent.getActivity(UpdateWidgetService.this,
							0, intent, 0);
					/* 设置监听 */
					view.setOnClickPendingIntent(R.id.tv, pi);
					awm.updateAppWidget(ids, view);
				}
			};
			timer.schedule(task, 0, 1000);
		}
	}
	
	@Override
	public void onDestroy() {

		if (task != null) {
			task.cancel();
		}
		if (timer != null) {
			timer.cancel();
		}
		timer = null;
		task = null;
	}
	
}
