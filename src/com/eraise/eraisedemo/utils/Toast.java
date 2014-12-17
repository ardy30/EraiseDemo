package com.eraise.eraisedemo.utils;

import com.eraise.eraisedemo.EraiseDemoApplication;

/**
 * 显示Toast
 * @author: 思落羽
 * @date: 2014-2-14
 * @Description:
 */
public class Toast {
	
	public static android.widget.Toast toast;
	
	public static void showToast(String text) {
		
		if (toast == null) {
			toast = android.widget.Toast
			.makeText(EraiseDemoApplication.getApplication(), text, 
					android.widget.Toast.LENGTH_SHORT);
		} else {
			toast.setText(text);
		}
		toast.show();
	}
}
