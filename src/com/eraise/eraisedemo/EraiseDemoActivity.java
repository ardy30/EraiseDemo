package com.eraise.eraisedemo;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

import com.eraise.eraisedemo.bean.DemoBean;
import com.eraise.eraisedemo.utils.DemoConfig;

public class EraiseDemoActivity extends Activity {
	
	/** demo列表  */
	protected List<Map<String, DemoBean>> demoList = null;
	protected SimpleAdapter adapter = null;
	protected ListView listView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eraise_demo);
		
		init();
	}
	
	protected void init() {
		
		DemoConfig.init(this);
		demoList = DemoConfig.getDemoList();
		adapter = new SimpleAdapter(this, demoList, 
				R.layout.listview_item, new String[] {"demo"}, new int[]{R.id.tv_name});
		/* 设置SimpleAdapter内部显示的处理  */
		adapter.setViewBinder(new ViewBinder() {

			@Override
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				
				DemoBean demo = (DemoBean) data;
				view.setTag(demo);
				((TextView) view).setText(demo.title);
				return true;
			}
			
		});
		listView = (ListView) findViewById(R.id.listview);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				DemoBean demo = (DemoBean) view.findViewById(R.id.tv_name).getTag();
				Intent intent = new Intent();
				intent.setClass(EraiseDemoActivity.this, demo.cls);
				startActivity(intent);
				overridePendingTransition(R.anim.in, R.anim.out);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.eraise_demo, menu);
		return true;
	}

}
