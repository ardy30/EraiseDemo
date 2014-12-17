package com.eraise.eraisedemo.activity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

import com.eraise.eraisedemo.R;

/**
 * 模仿微信的ListView Item删除效果
 * @author: 思落羽
 * @date: 2014-2-11
 * @Description:
 */
public class ListItemDeleteDemo extends Activity{
	
	protected ListView lv = null;
	protected List<Map<String, String>> data = null;
	protected SimpleAdapter adapter= null;
	protected OnClickListener click = null;
	protected OnTouchListener drag = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eraise_demo);
		
		init();
	}
	
	protected void init() {
		
		lv = (ListView) findViewById(R.id.listview);
		data = createData();
		adapter = new SimpleAdapter(this, data, R.layout.demo_list_delete_item, 
				new String[]{"title"}, new int[]{R.id.tv});
		adapter.setViewBinder(new ViewBinder() {

			@Override
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				
				if (view instanceof TextView) {
					((TextView)view).setText((String)data);
					HorizontalScrollView sv = ((HorizontalScrollView)view.getParent().getParent());
					Map<String, String> map = getObject((String)data);
					/* 设置标签在记录打开状态的时候用 */
					sv.setTag(map);
					if ("true".equals(map.get("open"))) {
						open(sv, false);
					} else {
						close(sv, false);
					}
					sv.setOnTouchListener(getDragListener());
					/* 设置大小 */
					view.setLayoutParams(new LinearLayout.LayoutParams(getWidth(), 
							LinearLayout.LayoutParams.WRAP_CONTENT));
					View del = ((LinearLayout)view.getParent()).findViewById(R.id.del);
					/* 设置标签在点击删除的时候获取 */
					del.setTag(data);
					del.setOnClickListener(getClickListener());
				}
				return true;
			}
			
		});
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Toast.makeText(ListItemDeleteDemo.this, 
						"点击了" + ((Map<String, String>)adapter.getItem(position)).get("title"), 
						Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	/**
	 * 返回屏幕宽度
	 * @return
	 */
	protected int getWidth() {

		return getWindowManager().getDefaultDisplay().getWidth();
	}
	
	protected OnTouchListener getDragListener() {
		if (drag == null) {
			drag = new OnTouchListener() {
				
				float old = 0;
				Point down = null;

				@SuppressWarnings("unchecked")
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					
					if (event.getAction() == MotionEvent.ACTION_MOVE){
						v.scrollBy((int)(old - event.getX()), 0);
						old = event.getX();
					} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
						/* 记录最初的值及初始化滚动的值 */
						old = event.getX();
						down = new Point((int)event.getX(), (int)event.getY());
					} else { 
						View del = v.findViewById(R.id.del);
						int sl = (int)down.x - (int)event.getX();
						/* 原先为关闭，结果为打开 */
						if (sl > 0 && sl > del.getWidth() / 2) {
							open(((HorizontalScrollView)v), true);
							/* 记录打开状态 */
							((Map<String, String>)v.getTag()).put("open", "true");
						/* 原先为打开，结果为关闭 */
						} else if (sl < 0 && Math.abs(sl) > del.getWidth() / 2) {
							close(((HorizontalScrollView)v), true);
							/* 记录关闭状态 */
							((Map<String, String>)v.getTag()).put("open", "false");
						/* 原先为关闭，结果为关闭 */
						} else if (sl > 0) {
							close((HorizontalScrollView)v, true);
							/* 记录关闭状态 */
							((Map<String, String>)v.getTag()).put("open", "false");
						/* 原先为打开，结果为打开 */
						} else if (sl < 0) {
							open(((HorizontalScrollView)v), true);
							/* 记录打开状态 */
							((Map<String, String>)v.getTag()).put("open", "true");
						/* 点击事件调用ListView的OnItemClickListener */
						} else if (down.x == (int)event.getX() && down.y == (int)event.getY()) {
							/* 调用Item的点击事件 */
							lv.getOnItemClickListener().onItemClick(
									(AdapterView<?>)v.getParent(), v, 
									data.indexOf(v.getTag()), 
									adapter.getItemId(data.indexOf(v.getTag())));
						}
					}
					return true;
				}
				
			};
		}
		return drag;
	}
	
	protected void close(HorizontalScrollView v, boolean smooth) {
		
		if (smooth) {
			v.smoothScrollTo(0, 0);
		} else {
			v.scrollTo(0, 0);
		}
			
	}
	
	protected void open(HorizontalScrollView v, boolean smooth) {
		
		View del = v.findViewById(R.id.del);
		/* 父控件的padding值也需要算进去 */
		int value = del.getWidth() + (((View)del.getParent()).getPaddingRight() * 2);
		if (smooth) {
			v.smoothScrollTo(value, 0);
		} else {
			v.scrollTo(value, 0);
		}
	}
	
	/**
	 * 得到Click监听
	 * @return
	 */
	protected OnClickListener getClickListener() {
		
		if (click == null) {
			click = new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					data.remove(getObject((String)v.getTag()));
					/* 这方法可以不把ListView滚到顶 */
					adapter.notifyDataSetChanged();
				}
			};
		}
		return click;
	}
	
	/**
	 * 找到值为str的map
	 * @param str
	 * @return
	 */
	protected Map<String, String> getObject(String str) {
		
		if (str == null) {
			return null;
		}
		for (Map<String, String> mTemp : data) {
			if (mTemp.get("title").equals(str)) {
				return mTemp;
			}
		}
		return null;
	}

	/**
	 * 从字符串中解析出字母
	 * @param str
	 * @return
	 */
	protected int getInt(String str) {
		
		Pattern pattern = Pattern.compile("\\d+");
		Matcher m = pattern.matcher(str);
		/* find()后才执行查找 */
		m.find();
		return Integer.parseInt(m.group());
	}
	
	/**
	 * 初始化数据
	 * @return
	 */
	protected List<Map<String, String>> createData() {
		
		List<Map<String, String>> data = new LinkedList<Map<String, String>>();
		Map<String, String> mTemp;
		for (int i = 0; i < 20; i ++) {
			mTemp = new HashMap<String, String>();
			mTemp.put("title", "条目" + i);
			data.add(mTemp);
		}
		return data;
	}

}
