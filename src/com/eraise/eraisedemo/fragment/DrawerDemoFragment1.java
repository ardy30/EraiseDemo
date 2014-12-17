package com.eraise.eraisedemo.fragment;

import java.util.LinkedList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.eraise.eraisedemo.R;

public class DrawerDemoFragment1 extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_drawerdemo1, null);
		ListView lv = (ListView) view.findViewById(R.id.lv_fragment1);
		LinkedList<String> data = new LinkedList<String>();
		for (int i = 0; i < 20; i ++) {
			data.add("条目 --> " + i);
		}
		ArrayAdapter<String> adapter = 
				new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_2,
						android.R.id.text1, data);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Toast.makeText(getActivity(), "条目" + position + "被点击", Toast.LENGTH_SHORT).show();
			}
		});
		return view;
	}
}
