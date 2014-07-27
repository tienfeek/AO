package com.tien.ao;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AbsListView.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tien.ao.adapter.SercetListAdapter;
import com.tien.ao.demain.Sercet;
import com.tien.ao.utils.NetworkUtil;
import com.tien.ao.utils.XLog;
import com.tien.ao.volley.Cache;
import com.tien.ao.volley.Response;
import com.tien.ao.volley.VolleyError;
import com.tien.ao.volley.toolbox.JsonObjectRequest;

public class MainActivity extends BaseActivity implements OnClickListener{
	
	private PullToRefreshListView mPullRefreshListView;
	private ListView mListView;
	
	private SercetListAdapter mAdapter;
	private List<Sercet> sercets = new ArrayList<Sercet>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main_layout);
		
		this.findView();
		this.addListener();
		
		mAdapter = new SercetListAdapter(sercets);
		mListView.setAdapter(mAdapter);
		
		loadData(true, false);
		

	}
	
	private void findView(){
		
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mListView = mPullRefreshListView.getRefreshableView();
		
		TextView emptyView = new TextView(this);
		emptyView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, 100));
		emptyView.setGravity(Gravity.CENTER);
		emptyView.setText("没有更多数据！");
		//mPullRefreshListView.setEmptyView(emptyView);
		
		View view = LayoutInflater.from(this).inflate(R.layout.secret_list_footer, null);
		
		mPullRefreshListView.getRefreshableView().addFooterView(view);
	}
	
	private void addListener(){
		this.findViewById(R.id.add_ll).setOnClickListener(this);
		
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				
				loadData(false, true);

			}
		});

		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				//Toast.makeText(PullToRefreshListActivity.this, "End of List!", Toast.LENGTH_SHORT).show();
				if(mListView.getLastVisiblePosition() > 21){
					loadData(false, false);
				}
			}
		});
		mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(MainActivity.this, DetailActivity.class);
				intent.putExtra("secretId", "");
				startActivity(intent);
			}
		});
		
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.add_ll){
			Intent intent = new Intent(MainActivity.this, SendActivity.class);
			startActivity(intent);
		}
		
	}
	
	
	private void handleData(JSONObject json, boolean refersh){
		if(json.optInt("status") == 0){
			JSONObject data = json.optJSONObject("data");
			if(data != null){
				
				JSONArray array = data.optJSONArray("secret");
				
				if(array != null){
					Gson gson = new Gson();
					List<Sercet> newSercets = gson.fromJson(array.toString(), new TypeToken<List<Sercet>>(){}.getType());
					
					mAdapter.setData(!refersh, newSercets);
					mAdapter.notifyDataSetChanged();
				}
			}
		}
	}
	
	private void loadData(boolean useCache, final boolean refersh){
		String url = Constant.URL_REQUEST;
		
		if(useCache){
			Cache.Entry entry = AOApplication.getRequestQueue().getCache().get(url);
			try {
				if(entry != null){
					String json = new String(entry.data, "UTF-8");
					XLog.i("wanges", "cache json:"+json);
					if(json != null && "".equals(json)){
						handleData(new JSONObject(json), refersh);
						return;
					}
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		Map<String, String> params = NetworkUtil.initRequestParams();
		params.put("act", "getSecret");
		
		JsonObjectRequest request = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				handleData(response, refersh);
				mPullRefreshListView.onRefreshComplete();
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				mPullRefreshListView.onRefreshComplete();
			}
		});
		
		request.setTag(this);
		
		AOApplication.getRequestQueue().add(request);
	}
	

 
}
