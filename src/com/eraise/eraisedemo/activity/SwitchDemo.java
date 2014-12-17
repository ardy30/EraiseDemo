package com.eraise.eraisedemo.activity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Switch;
import android.widget.TextView;

import com.eraise.eraisedemo.R;

/**
 * Switch是4.0加入的新特性，并不是ViewSwitcher
 * @author: 思落羽
 * @date: 2014-1-21
 * @Description:
 */
public class SwitchDemo extends Activity{
	
	protected ListView lv = null;
	protected List<Map<String, Bean>> data = null;
	protected SimpleAdapter adapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eraise_demo);
		
		init();
	}
	
	protected void init() {
		
		lv = (ListView) findViewById(R.id.listview);
		data = new LinkedList<Map<String, Bean>>();
		Map<String, Bean> mTemp = null;
		Bean bTemp = null;
		/* 初始化数据 */
		for (int i = 0; i < 20; i ++) {
			mTemp = new HashMap<String, Bean>();
			bTemp = new Bean();
			bTemp.text = "选项" + i;
			bTemp.status = false;
			mTemp.put("bean", bTemp);
			data.add(mTemp);
		}
		/* 初始化Adapter */
		adapter = new SimpleAdapter(this, data, R.layout.demo_switch_item,
				new String[]{"bean", "bean"}, new int[] {R.id.s_switch, R.id.tv});
		/* 设置Adapter的渲染 */
		adapter.setViewBinder(new ViewBinder() {

			@Override
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				
				final Bean b = (Bean) data;
				if (view instanceof Switch) {
					Switch s = (Switch)view;
					/* 清空CheckedListeners */
					s.setOnCheckedChangeListener(null);
					s.setChecked(b.status);
					if (b.listener == null) {
						b.listener = new CheckedListener(b);
					}
					s.setOnCheckedChangeListener(b.listener);
				} else if (view instanceof TextView) {
					final TextView tv = (TextView) view;
					tv.setText(b.text);
					if (b.click == null) {
						b.click = new ClickListener();
					}
					((View)tv.getParent()).setOnClickListener(b.click);
					if (b.select == null) {
						b.select = new SelectListener(b);
					}
					((View)tv.getParent()).setOnLongClickListener(b.select);
					/* 还原状态，不延迟一下再显示设置Selector会失效 */
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							((View)tv.getParent()).setSelected(b.selected);
						}
						
					}, 100);
				}
				return true;
			}
			
		});
		lv.setAdapter(adapter);
		
	}
	
	class CheckedListener implements OnCheckedChangeListener {
		
		Bean bean;
		
		CheckedListener (Bean bean) {
			
			this.bean = bean;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			
			bean.status = isChecked;
		}

	}
	
	class SelectListener implements OnLongClickListener {
		
		Bean bean;
		
		SelectListener (Bean bean) {
			
			this.bean = bean;
		}
		
		@Override
		public boolean onLongClick(View v) {
			
			bean.selected = !bean.selected;
			v.setSelected(bean.selected);
			return true;
		}
	}
	
	class ClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			TextView tv = (TextView) v.findViewById(R.id.tv);
			new AlertDialog.Builder(v.getContext())
			.setMessage(tv.getText().toString())
			.setNegativeButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					dialog.dismiss();
				}
				
			})
			.show();
		}
		
	}
	
	class Bean {
		String text;
		boolean status;
		boolean selected;
		OnCheckedChangeListener listener;
		OnClickListener click;
		OnLongClickListener select;
	}
	
}
