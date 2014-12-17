package com.eraise.eraisedemo.service;

import java.util.LinkedList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.eraise.eraisedemo.R;

/**
 * 在 manifest.xml 中应该配置权限 android.permission.SYSTEM_ALERT_WINDOW
 * 为了只在主界面应用才显示，需要获取任务栈，
 * 需要权限 android.permission.GET_TASKS
 * 把悬浮窗布局/控件添加到 WindowManager 上，
 * 并且设置 WindowManager.LayoutParams 即可，
 * 通过监听触摸事件，并且更新 WindowManager.LayoutParams 的x,y就可以更新位置
 * @author: 思落羽
 * @date: 2014-2-12
 * @Description:
 */
public class SuspendButtonService extends Service {
	
	public static final String ACTION_OPERATE = "action_operate";
	public static final short OPERATE_SHOW = 1;
	public static final short OPERATE_HIDE = 2;
	
	protected boolean added = false;
	protected boolean show = false;
	protected WindowManager.LayoutParams wmParams;
	
	/**
	 * 检查当前是否为主界面应用
	 */
	protected Handler hCheckMain;
	
	/**
	 * 主界面包名
	 */
	protected List<String> homeNames;
	
	protected Button btn;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		
		if (intent != null) {
			/* 获取操作来确定当前应该显示或是取消显示 */
			short operate = intent.getShortExtra(ACTION_OPERATE, (short)0);
			switch(operate) {
			case OPERATE_SHOW:
				added = true;
				show();
				break;
			case OPERATE_HIDE:
				added = false;
				hide();
				break;
			}
		}
	}
	
	/**
	 * 初始化共用的对象
	 */
	protected void init() {
	
		/* 2003是 WindowManager.LayoutParams.TYPE_SySTEM_ALERT 的值
		 * 初始化 WindowManager.LayoutParams */
		wmParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT, 2003, 
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.RGBA_8888); // 类型要为透明才不会把背景也覆盖掉
		/* 获取悬浮窗控件 */
		btn = (Button) LayoutInflater.from(this)
				.inflate(R.layout.demo_suspend_button, null);
		btn.setOnTouchListener(new TouchBtn());
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Toast.makeText(SuspendButtonService.this, "点击了悬浮窗", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	/**
	 * 获取到桌面应用的应用名称
	 * @return
	 */
	protected List<String> getHomes() {
		
		List<String> names = new LinkedList<String>();
		PackageManager packageManager = getPackageManager();
		/* 找出action为 Intent.ACTION_MAIN，
		 * Category为Intent.CATEGORY_HOME的包的包名，这个包就是主界面的包 */
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		/* 查找intent可以唤醒的Activity的信息 */
		List<ResolveInfo> resolveInfos = packageManager
				.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		for (ResolveInfo resolveInfo : resolveInfos) {
			names.add(resolveInfo.activityInfo.packageName);
		}
		return names;
	}
	
	/**
	 * 把悬浮窗添加到 WindowManager上
	 */
	protected void show() {
		
		if (!show) {
			show = true;
			/* 获取 WindowManager */
			WindowManager wm = (WindowManager) getSystemService(Service.WINDOW_SERVICE);
			wm.addView(btn, wmParams);
			checkMain();
		}
	}
	
	/**
	 * 把悬浮穿从 WindowManager 上移除
	 */
	protected void hide() {
		
		if (show) {
			show = false;
			/* 获取 WindowManager */
			WindowManager wm = (WindowManager) getSystemService(Service.WINDOW_SERVICE);
			wm.removeView(btn);
		}
	}
	
	/**
	 * 检查当前界面是否在主界面，悬浮穿控件为只在主界面显示
	 */
	protected void checkMain() {
		
		/* hCheckMain 为空就新建 */
		if (hCheckMain == null) {
			homeNames = getHomes();
			hCheckMain = new Handler(new Handler.Callback() {
				
				@Override
				public boolean handleMessage(Message msg) {

					if (isHome()) {
						if (!show)
							show();
					} else {
						if (show)
							hide();
					}
					if (added) {
						/* 1秒后扫描当前是否需要显示 */
						hCheckMain.sendEmptyMessageDelayed(0, 1000);
					}
					return true;
				}
			});
		}
		/* 1秒扫描一次当前是否在主界面 */
		hCheckMain.sendEmptyMessageDelayed(0, 1000);
	}
	
	/**
	 * 当前界面是否为主界面
	 * @return 为Home界面则返回true，否则为false
	 */
	protected boolean isHome() {
		
		/* 获取 ActivityManager ，检查是否存在主界面 */ 
		ActivityManager activityManager = 
				(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		/* 获取当前正在运行的任务 */
		List<RunningTaskInfo> infos = activityManager.getRunningTasks(1);
		/* 主界面是否包含了当前正在运行的任务的包名 */
		/* RunningTaskInfo 是任务栈信息，得到栈顶的Activity再去获取期包名 */
		return homeNames.contains(infos.get(0).topActivity.getPackageName());
	}
	
	protected class TouchBtn implements OnTouchListener {
		
		float x;
		float y;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				down(v, event);
				break;
			case MotionEvent.ACTION_UP:
				up(v, event);
				break;
			case MotionEvent.ACTION_MOVE:
				move(v, event);
				break;
			}
			return true;
		}
		
		void down(View v, MotionEvent event) {
			/* 按下时记录按下的坐标 */
			x = event.getX();
			y = event.getY();
		}
		
		void up(View v, MotionEvent event) {
			
			if (x == event.getX() && y == event.getY()) {
				/* 松开与点下的x y不变，视为点击 */
				v.performClick();
			}
		}
		
		void move(View v, MotionEvent event) {
			
			/* 获取 WindowManager */
			WindowManager wm = (WindowManager) getSystemService(Service.WINDOW_SERVICE);
			/* 设置新的x y的值 */
			wmParams.x += event.getX() - x;
			wmParams.y += event.getY() - y;
			/* 更新位置 */
			wm.updateViewLayout(v, wmParams);
		}
		
	}

}
