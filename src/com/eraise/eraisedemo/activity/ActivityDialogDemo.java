package com.eraise.eraisedemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.eraise.eraisedemo.R;

/**
 * Activity模拟Dialog的效果，主要是设置style，
 * style为背景半透明，悬浮等效果
 * @author: 思落羽
 * @date: 2014-2-11
 * @Description:
 */
public class ActivityDialogDemo extends Activity {
	
	protected Button btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialog_demo);
		
		init();
	}
	
	public void init() {
		
		btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ActivityDialogDemo.this.finish();
			}
			
		});
	}

}
