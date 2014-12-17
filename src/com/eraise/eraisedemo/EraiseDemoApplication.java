package com.eraise.eraisedemo;

import android.app.Application;

public class EraiseDemoApplication extends Application {
	
	public static EraiseDemoApplication application;

	@Override
	public void onCreate() {
		super.onCreate();
		
		application = this;
	}

	public static EraiseDemoApplication getApplication() {
		
		return application;
	}
}
