
package com.tien.ao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.tien.ao.adapter.ComentViewAdapter;
import com.tien.ao.widget.AutoListView;

public class ListContent extends Activity {

	public static final int REFRESH = 0;
	public static final int LOAD = 1;
	private ListView lstv;
	private ComentViewAdapter adapter;
	private ScrollView scv;
	private List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			List<HashMap<String, String>> result = (List<HashMap<String, String>>) msg.obj;
			switch (msg.what) {
			case ListContent.REFRESH:
				
				list.clear();
				list.addAll(result);
				break;
			case ListContent.LOAD:
				list.addAll(result);
				break;
			}
			setListViewHeightBasedOnChildren(lstv);
			adapter.notifyDataSetChanged();
		};
	};
	public void initData()
	{
		loadData(ListContent.LOAD);
	}
	public List<HashMap<String, String>> getData() {
		// 娴嬭瘯鏁版嵁 public List<HashMap<String, String>> getData() {
		List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < 40; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("coment_cotent", "这是评论jjjj");
			data.add(map);
		}
		return data;

	}
	public void loadData(final int what) {
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
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_content);
		lstv = (ListView) findViewById(R.id.comment_listview);

//		List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < 2; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("coment_cotent", "this is评论");
			//list.add(map);
		}
		initData();
		adapter = new ComentViewAdapter(this, list);
		lstv.setAdapter(adapter);
		this.setListViewHeightBasedOnChildren(lstv);
		scv = (ScrollView) findViewById(R.id.scv);
		scv.smoothScrollTo(0, 0);
		scv.setOnTouchListener(new TouchListenerImpl());
		
		// lstv.setSelection(0);
	}

	/**
	 * 动态设置ListView的高度
	 * 
	 * @param listView
	 */
	public void setListViewHeightBasedOnChildren(ListView listView) {
		if (listView == null)
			return;

		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	private class TouchListenerImpl implements OnTouchListener {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:

				break;
			case MotionEvent.ACTION_MOVE:
				int scrollY = view.getScrollY();
				int height = view.getHeight();
				int scrollViewMeasuredHeight = scv.getChildAt(0)
						.getMeasuredHeight();
				if (scrollY == 0) {
					// System.out.println("滑动到了顶端 view.getScrollY()="+scrollY);
				}
				if ((scrollY + height) == scrollViewMeasuredHeight) {
					// System.out.println("滑动到了底部 scrollY="+scrollY);
					// System.out.println("滑动到了底部 height="+height);
					// System.out.println("滑动到了底部 scrollViewMeasuredHeight="+scrollViewMeasuredHeight);
					//loadData(ListContent.LOAD);
				}
				break;

			default:
				break;
			}
			return false;
		}
	}
}

