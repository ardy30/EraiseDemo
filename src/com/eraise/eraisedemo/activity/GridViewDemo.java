package com.eraise.eraisedemo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.ViewSwitcher;

import com.eraise.eraisedemo.R;

/**
 * 表格布局，在表格内添加了SwitcherView
 * @author: 思落羽
 * @date: 2014-2-11
 * @Description:
 */
public class GridViewDemo extends Activity {
	
	protected List<Map<String, String>> data = null;
	protected GridView gv = null;
	protected SimpleAdapter adapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grid_view_demo);
		
		init();
	}
	
	protected void init() {
		
		/* 初始化一些数值到data对象 */
		data = new ArrayList<Map<String, String>>();
		gv = (GridView) findViewById(R.id.gv_grid);
		HashMap<String, String> tempMap;
		for (int i = 0; i < 20; i ++) {
			tempMap = new HashMap<String, String>();
			tempMap.put("img", "false");
			tempMap.put("title", "条目" + i);
			data.add(tempMap);
		}
		adapter = new SimpleAdapter(this, data, R.layout.demo_gridview_item, 
				new String[]{"img", "title"}, new int[] {R.id.iv, R.id.tv});
		/* 自行渲染部分页面，return false为没有处理，true为已经处理不需要系统为我们再处理 */
		adapter.setViewBinder(new ViewBinder() {
			
			@Override
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				
				if (view instanceof ImageView) {
					((ImageView)view).setImageResource(R.drawable.ic_launcher);
					ViewSwitcher switcher = (ViewSwitcher)view.getParent().getParent();
					if (data.equals("true")) {
						switcher.setDisplayedChild(1);
					} else {
						switcher.setDisplayedChild(0);
					}
					return true;
				}
				return false;
			}
			
		});
		gv.setAdapter(adapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				((ViewSwitcher)view).setDisplayedChild(1);
				data.get(position).put("img", "true");
			}
			
		});
	}

}
