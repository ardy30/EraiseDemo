package com.eraise.eraisedemo.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.content.res.XmlResourceParser;

import com.eraise.eraisedemo.R;
import com.eraise.eraisedemo.bean.DemoBean;

public class DemoConfig {
	
	protected static Context context = null;
	protected static List<Map<String, DemoBean>> demoList = null;

	public static void init(Context context) {
		
		DemoConfig.context = context;
		initList();
	}
	
	protected static void initList() {
		
		/* 列表的查找比增删来得频繁，所以LinkedList效率更高 */
		demoList = new LinkedList<Map<String, DemoBean>>();
		XmlResourceParser xmlParser = context.getResources().getXml(R.xml.demo_config);
		String className, title;
		Class<?> cls;
		Map<String, DemoBean> tempMap;
		try {
			while (xmlParser.getEventType() != XmlResourceParser.END_DOCUMENT) {
				if (xmlParser.getEventType() == XmlResourceParser.START_TAG 
						&& xmlParser.getName().equals("demo")) {
					className = xmlParser.getAttributeValue(null, "name");
					title = xmlParser.getAttributeValue(null, "title");
					if (className == null) {
						continue;
					}
					try {
						cls = Class.forName(className);
					} catch (ClassNotFoundException e) {
						cls = null;
						continue;
					}
					if (!isActivity(cls)) {
						continue;
					}
					if (title == null) {
						title = className.substring(className.lastIndexOf("."));
					}
					tempMap = new HashMap<String, DemoBean>();
					tempMap.put("demo", new DemoBean(cls, title));
					demoList.add(tempMap);
				}
				try {
					xmlParser.next();
				} catch (IOException e) {
					break;
				}
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 一个类是不是Activity的子类
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	protected static boolean isActivity(Class<?> cls) {
		
		try {
			Class<Activity> temp = (Class<Activity>) cls;
			return true;
		} catch (ClassCastException e) {
			return false;
		}
	}
	
	public static List<Map<String, DemoBean>> getDemoList() {
		
		return demoList;
	}
	
}
