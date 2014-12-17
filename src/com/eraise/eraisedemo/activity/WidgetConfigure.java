package com.eraise.eraisedemo.activity;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;

import com.eraise.eraisedemo.R;

public class WidgetConfigure extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setResult(RESULT_CANCELED);
		setContentView(R.layout.activity_configure_widget);
		
	}
	
	@Override
	public void onBackPressed() {
		
		/* 获取要配置的Widget的ID */
		int mAppWidgetId = getIntent().getExtras().getInt(
				AppWidgetManager.EXTRA_APPWIDGET_ID, 
				AppWidgetManager.INVALID_APPWIDGET_ID);
		Intent result = new Intent();
		/* 设置要生效的WIdget的ID */
		result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		/* 告诉系统已经配置成功，并且将配置的id给系统 */
		setResult(RESULT_OK, result);
		super.onBackPressed();
	}
	
}
