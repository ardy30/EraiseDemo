package com.eraise.eraisedemo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PageFragment extends Fragment {
	
	protected static int page;
	protected int cp;
	protected TextView tv;
	protected int bg;
	
	public PageFragment() {
		
		super();
		page ++;
		cp = page;
		if (bg == 0) {
			bg = Color.rgb((int)(
					Math.random() * 255), (int)(Math.random() * 255), 
					(int)(Math.random() * 255));
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		tv = new TextView(inflater.getContext());
		tv.setText("页面 ---> " +cp);
		tv.setBackgroundColor(bg);
		return tv;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
//		System.out.println("onViewCreated");
	}
	
	@Override
	public PageFragment clone() throws CloneNotSupportedException {
		PageFragment pf = new PageFragment();
		pf.cp = this.cp;
		pf.bg = this.bg;
		page --;
		return pf;
	}
	
}
