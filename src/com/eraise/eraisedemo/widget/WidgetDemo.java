package com.eraise.eraisedemo.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.eraise.eraisedemo.service.UpdateWidgetService;
import com.eraise.eraisedemo.utils.Toast;

/**
 * Widget的编写：
 * 1、在res/layout中编写widget的布局和配置页面的布局(如果有需要)
 * 2、在res/xml中写appwidget-provider，在里面配置widget的各种信息
 * 3、书写继承于AppWidgetProvider的Java类和Widget的配置Activity(如果有需要)
 * 4、在manifest.xml中配置 AppWidgetProvider的 receiver，
 * 		receiver需要有action为android.appwidget.action.APPWIDGET_UPDATE的IntentFielter，
 * 		同时设置meta-data的resource为res/xml中的appwidget-provider，android:name为android.appwidget.provider
 * 5、在manifest.xml中配置Widget配置Activity，
 * 		需要一个action为android.appwidget.action.APPWIDGET_CONFIGURE的intent-filter
 * 6、如果需要定时更新需要写一个Service或AlarmManager来进行更新，不能用updatePeriodMillis
 * @author: 思落羽
 * @date: 2014-2-17
 * @Description:
 */
public class WidgetDemo extends AppWidgetProvider{
	
	/**
	 * 一个Widget被删除的时候调用 
	 */
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		
		Toast.showToast("删除");
	}

	/**
	 * 当添加Widget的时候触发
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		System.out.println("更新 onUpdate");
//		/* 获取RemoteView (桌面的Launcher） */
//		RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.activity_widget);
//		view.setCharSequence(R.id.tv, "setText", 
//				DateFormat.format("kk:mm:ss", Calendar.getInstance()).toString());
//		appWidgetManager.updateAppWidget(appWidgetIds, view);
//		super.onEnabled(context);
	}
	
	/**
	 * 最后一个Widget被删除的时候调用 
	 */
	@Override
	public void onDisabled(Context context) {
		
		System.out.println("禁用");
		Intent intent = new Intent();
		intent.setClass(context, UpdateWidgetService.class);
		context.stopService(intent);
	}
	
	/**
	 * 第一个Widget被添加上的时候调用 
	 */
	@Override
	public void onEnabled(Context context) {
		
		System.out.println("使用");
		Intent intent = new Intent();
		intent.setClass(context, UpdateWidgetService.class);
		context.startService(intent);
	}
}
