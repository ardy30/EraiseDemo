package com.eraise.eraisedemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.eraise.eraisedemo.R;
import com.eraise.eraisedemo.service.SuspendButtonService;

/**
 * 悬浮窗主要依靠WindowManager来做，详细的看 com.eraise.eraisedemo.service.SuspendButtonService
 * @author: 思落羽
 * @date: 2014-2-12
 * @Description:
 */
public class SuspendButtonDemo extends Activity{
	
	protected Button btn_show;
	protected Button btn_hide;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suspend_button_demo);
		
		btn_show = (Button) findViewById(R.id.btn_show);
		btn_hide = (Button) findViewById(R.id.btn_hide);
		OnClickListener cl = getDefaultBtnListener();
		btn_show.setOnClickListener(cl);
		btn_hide.setOnClickListener(cl);
	}
	
	public OnClickListener getDefaultBtnListener() {
		
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				short operate = -1;
				switch (v.getId()) {
				case R.id.btn_show:
					operate = SuspendButtonService.OPERATE_SHOW;
					break;
				case R.id.btn_hide:
					operate = SuspendButtonService.OPERATE_HIDE;
					break;
				}
				if (operate != -1) {
					Intent intent = 
							new Intent(SuspendButtonDemo.this, SuspendButtonService.class);
					intent.putExtra(SuspendButtonService.ACTION_OPERATE, operate);
					startService(intent);
				}
			}
		};
	}
}
