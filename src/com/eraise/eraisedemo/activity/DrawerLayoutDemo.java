package com.eraise.eraisedemo.activity;

import java.util.LinkedList;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.eraise.eraisedemo.R;

/**
 * SupportV4包中，存在一个控件 DrawerLayout，
 * 拥有替代简单的SlidingMenu功能，
 * 内部控件通过设置 layout_gravity 来隐藏
 * 通过 closeDrawers() 可以关闭掉滑动出来的侧边栏
 * @author: 思落羽
 * @date: 2014-2-11
 * @Description:
 */
public class DrawerLayoutDemo extends FragmentActivity {
	
	protected DrawerLayout dl = null;
	protected FragmentManager fragmentManager = null;
	protected ListView listView = null;
	protected ListView listView2 = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawerlayout_demo);
		
		dl = (DrawerLayout) findViewById(R.id.dl_layer);
		listView = (ListView) findViewById(R.id.listview);
		listView2 = (ListView) findViewById(R.id.listview2);
		/* Fragment管理器 */
		fragmentManager = getSupportFragmentManager();
		
		initListView();
	}
	
	public void initListView() {
		
		/* 第一个菜单滑动栏数据初始化 */
		LinkedList<String> data = new LinkedList<String>();
		for(int i = 0; i < 10; i ++) {
			data.add("条目" + i);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				android.R.id.text1, data);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new ItemClick());
		LinkedList<String> data2 = new LinkedList<String>();
		/* 第二个菜单滑动栏数据初始化 */
		for(int i = 0; i < 10; i ++) {
			data2.add("菜单" + i);
		}
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				android.R.id.text1, data2);
		listView2.setAdapter(adapter2);
		listView2.setOnItemClickListener(new ItemClick());
	}
	
	protected class ItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			dl.closeDrawers();
		}
		
	}

}
