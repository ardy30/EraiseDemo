package com.eraise.eraisedemo.activity;

import java.util.List;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.eraise.eraisedemo.R;

/**
 * 加载手机上的所有的app
 * @author 思落羽
 * 2014年12月17日 下午5:48:00
 *
 */
public class LoadAllAppDemo extends ListActivity {
	
	private List<ResolveInfo> mResolveList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load_all_app_demo);
		init();
	}
	
	private void init() {
		// 意图调用打开主页(各个app都将响应)
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		// 获取包管理器，以便查询可以响应这个Intent的Activity
		PackageManager pm = getPackageManager();
		// 查询可以响应Intent的Activity的信息，信息是ResolveInfo列表，这样可以得到Activity所在的ApplicationInfo和PackageInfo
		mResolveList = pm.queryIntentActivities(mainIntent, 0);
		setListAdapter(new AppAdapter());
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		ResolveInfo resolveInfo = mResolveList.get(position);
		ActivityInfo activityInfo = resolveInfo.activityInfo;
		ComponentName component = new ComponentName(activityInfo.packageName, activityInfo.name);
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setComponent(component);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		startActivity(intent);
	}
	
	private class AppAdapter extends BaseAdapter {
		
		@Override
		public int getCount() {
			return mResolveList == null ? 0 : mResolveList.size();
		}

		@Override
		public ResolveInfo getItem(int position) {
			return mResolveList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.item_app_info, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// ResolveInfo 可以获取到包的信息
			ResolveInfo resolveInfo = getItem(position);
			ActivityInfo activityInfo = resolveInfo.activityInfo;
			// 加载图标、标签需要用到PackageManager
			PackageManager pm = getPackageManager();
			holder.tvName.setText(resolveInfo.loadLabel(pm));
			holder.tvPkg.setText(activityInfo.packageName);
			holder.tvAct.setText(activityInfo.name);
			displayIcon(holder.ivLogo, resolveInfo);
			return convertView;
		}
		
	}
	
	void displayIcon (final ImageView iv, final ResolveInfo resolveInfo) {
		iv.setTag(resolveInfo);
		new AsyncTask<Void, Void, Drawable>() {

			@Override
			protected Drawable doInBackground(Void... params) {
				return resolveInfo.loadIcon(getPackageManager());
			}
			
			@Override
			protected void onPostExecute(Drawable result) {
				if (iv.getTag() == resolveInfo) {
					iv.setImageDrawable(result);
				}
			}
			
		}.execute();
	}
	
	private class ViewHolder {
		
		ViewHolder(View v) {
			ivLogo = (ImageView) v.findViewById(R.id.iv_logo);
			tvName = (TextView) v.findViewById(R.id.tv_name);
			tvPkg = (TextView) v.findViewById(R.id.tv_pkg);
			tvAct = (TextView) v.findViewById(R.id.tv_act);
		}
		
		ImageView ivLogo;
		TextView tvName;
		TextView tvPkg;
		TextView tvAct;
	}
	
}
