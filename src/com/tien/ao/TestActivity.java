package com.tien.ao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.tien.ao.adapter.ListViewAdapter;
import com.tien.ao.widget.AutoListView;
import com.tien.ao.widget.AutoListView.OnLoadListener;


/**
 * @author SunnyCoffee
 * @date 2014-1-28
 * @version 1.0
 * @desc listview涓嬫媺鍒锋柊锛屼笂鎷夎嚜鍔ㄥ姞杞芥洿澶氥� http锛�/blog.csdn.com/limb99
 */

public class TestActivity extends Activity implements OnClickListener, com.tien.ao.widget.AutoListView.OnRefreshListener,OnLoadListener {

	private AutoListView lstv;
	private ListViewAdapter adapter;
	private List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			List<HashMap<String, String>> result = (List<HashMap<String, String>>) msg.obj;
			switch (msg.what) {
			case AutoListView.REFRESH:
				lstv.onRefreshComplete();
				list.clear();
				list.addAll(result);
				break;
			case AutoListView.LOAD:
				lstv.onLoadComplete();
				list.addAll(result);
				break;
			}
			lstv.setResultSize(result.size());
			adapter.notifyDataSetChanged();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company_view);

		lstv = (AutoListView) findViewById(R.id.company_listview);
		adapter = new ListViewAdapter(this, list);
		findViewById(R.id.add_ll).setOnClickListener(this);
		lstv.setAdapter(adapter);
		lstv.setOnRefreshListener(this);
		lstv.setOnLoadListener(this);
		initData();
		this.lstv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int postion,
					long id) {
				Intent intent = new Intent(TestActivity.this, DetailActivity.class);
				startActivity(intent);
			}
		});

	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.add_ll){
			Intent intent = new Intent(TestActivity.this, SendActivity.class);
			startActivity(intent);
		}
		
	}

	private void initData() {
		loadData(AutoListView.REFRESH);
	}

	private void loadData(final int what) {
		// 杩欓噷妯℃嫙浠庢湇鍔″櫒鑾峰彇鏁版嵁
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(700);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message msg = handler.obtainMessage();
				msg.what = what;
				msg.obj = getData();
				handler.sendMessage(msg);
			}
		}).start();
	}

	@Override
	public void onRefresh() {
		loadData(AutoListView.REFRESH);
	}

	@Override
	public void onLoad() {
		loadData(AutoListView.LOAD);
	}

	// 娴嬭瘯鏁版嵁
	public List<HashMap<String, String>> getData() {
		List<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
		for(int i=0;i<10;i++)
		{
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("content", "云南省委常委张田欣被免 曾被举报包养情妇");
			map.put("time", "2014-05-03");
			data.add(map);
		}
		return data;
	}
	

}
