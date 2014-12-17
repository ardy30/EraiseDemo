package com.eraise.eraisedemo.activity;

import java.util.LinkedList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.eraise.eraisedemo.R;
import com.eraise.eraisedemo.fragment.PageFragment;

public class ViewPagerDemo extends FragmentActivity{
	
	protected ViewPager vp;
	
	/**
	 * 页面指示器
	 */
	protected PagerTabStrip pagerTabStrip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewpager_demo);
	
		pagerTabStrip = (PagerTabStrip) findViewById(R.id.tab_strip);
		/* 整个屏幕的下划线，跟个分割线似的 */
		pagerTabStrip.setDrawFullUnderline(false);
		vp = (ViewPager) findViewById(R.id.vp);
		vp.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
		vp.setCurrentItem(100000);
		vp.setPageTransformer(true, new ViewPager.PageTransformer() {
			
			@Override
			public void transformPage(View v, float position) {
			    
				/* 覆盖 */
				if (position < 0 && position > -1) {
					v.setTranslationX(1 + position);
				} else if (position > 0 && position < 1){
					float scale = position < 0.65f ? position : 0.65f;
					v.setPivotX(0.5f);
					v.setPivotY(0.5f);
					v.setScaleX(1 - scale);
					v.setScaleY(1 - scale);
					float pageHeight = v.getHeight();
					float pageWidth = v.getWidth();
					float vertMargin = pageHeight - pageHeight * (1 - scale);
					float horiMargin = pageWidth - pageWidth * (1 - position);
					v.setTranslationX(- horiMargin / 2);
					v.setTranslationY(vertMargin / 2);
				} else {
					v.setTranslationX(0f);
					v.setTranslationY(0f);
					v.setScaleX(1);
					v.setScaleY(1);
				}
				
				/* 缩放 */
//				if (position < 0) {
//					v.setAlpha(1 + position);
//					v.setScaleX(1 + position);
//					v.setScaleY(1 + position);
//				} else if (position > 0) {
//					v.setAlpha(1 - position);
//					v.setScaleX(1 - position);
//					v.setScaleY(1 - position);
//				} else {
//					v.setAlpha(1);
//				}
			}
		});
	}
	
	protected class FragmentAdapter extends FragmentPagerAdapter {
		
		LinkedList<Fragment> fragments;
		LinkedList<String> tabs;

		public FragmentAdapter(FragmentManager fm) {
			super(fm);
			
			fragments = new LinkedList<Fragment>();
			tabs = new LinkedList<String>();
			for (int i = 0; i < 3; i ++) {
				fragments.add(new PageFragment());
				tabs.add("页面" + i);
			}
		}
		
		/**
		 * 返回页面指示器上的标题
		 */
		@Override
		public CharSequence getPageTitle(int position)
		{
//		    return "页面" + position;
		    return tabs.get(position % fragments.size());
		}
		
		@Override
		public Fragment getItem(int arg0) {
			PageFragment pf = (PageFragment) fragments.get(arg0 % fragments.size());
			try {
				return pf.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}
		
	}
}
