package com.eraise.eraisedemo.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.eraise.eraisedemo.R;

/**
 * ImageSpan可以在EditText里插入表情
 * @author: 思落羽
 * @date: 2014-2-14
 * @Description:
 */
public class ImageSpanDemo extends Activity{
	
	protected TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imagespan_demo);
		
		tv = (TextView) findViewById(R.id.tv);
		SpannableString  ss = new SpannableString("abcdefghijklmnopqrstuvwxyz");
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.f000);
		ImageSpan imageSpan1 = new ImageSpan(bitmap);
		ss.setSpan(imageSpan1, 1, 2, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
		Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.f001);
		ImageSpan imageSpan2 = new ImageSpan(bitmap2);
		ss.setSpan(imageSpan2, 5, 6, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv.setText(ss, BufferType.SPANNABLE);
	}
}
