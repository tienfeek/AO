package com.tien.ao;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tien.ao.adapter.SercetListAdapter;
import com.tien.ao.demain.Sercet;
import com.tien.ao.notification.NotificationCenter;
import com.tien.ao.notification.NotificationItem;
import com.tien.ao.utils.NetworkUtil;
import com.tien.ao.utils.XLog;
import com.tien.ao.volley.Cache;
import com.tien.ao.volley.Response;
import com.tien.ao.volley.VolleyError;
import com.tien.ao.volley.toolbox.JsonObjectRequest;

public class MainActivity extends BaseActivity implements OnClickListener, Observer{
	
	private PullToRefreshListView mPullRefreshListView;
	private ListView mListView;
	private LinearLayout addLL;
	private TextView emptyView;
	private ContentLoadingProgressBar loadingBar;
	private RelativeLayout footerRL;
	private TextView loadingMoreTV;
	private ProgressBar loagingMoreBar;
	
	private SercetListAdapter mAdapter;
	private List<Sercet> sercets = new ArrayList<Sercet>();
	
	private boolean moreLoading = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main_layout);
		
		this.findView();
		this.addListener();
		
		this.mAdapter = new SercetListAdapter(sercets, this);
		this.mListView.setAdapter(mAdapter);
		
		this.loadData(true, false);
		
		NotificationCenter.defaultCenter().addObserver(this);
	}
	
	private void findView(){
		
		this.emptyView = (TextView)findViewById(R.id.emptyview);
		this.loadingBar = (ContentLoadingProgressBar)findViewById(R.id.loading_pb);
		this.addLL = (LinearLayout)findViewById(R.id.add_ll);
		this.mPullRefreshListView = (PullToRefreshListView)findViewById(R.id.pull_refresh_list);
		this.mListView = mPullRefreshListView.getRefreshableView();
		
		this.footerRL = (RelativeLayout)LayoutInflater.from(this).inflate(R.layout.secret_list_footer, null);
		this.footerRL.setVisibility(View.GONE);
		
		this.loadingMoreTV = (TextView)footerRL.findViewById(R.id.loading_more_tip_tv);
		this.loagingMoreBar = (ProgressBar)footerRL.findViewById(R.id.loading_more_pb);
		
		this.mListView.addFooterView(footerRL);
	}
	
	private void addListener(){
		this.findViewById(R.id.add_ll).setOnClickListener(this);
		
		this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				
				loadData(false, true);

			}
		});

		this.mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				footerRL.setVisibility(View.VISIBLE);
				XLog.i("wanges", "getLastVisiblePosition:"+mListView.getLastVisiblePosition() + "moreLoading:"+moreLoading+" "+(mListView.getLastVisiblePosition() % 20));
				if(!moreLoading && mListView.getLastVisiblePosition() % 20 == 1){
					moreLoading = true;
					loadData(false, false);
				}
			}
		});
//		mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				Intent intent = new Intent(MainActivity.this, DetailActivity.class);
//				intent.putExtra("secretId", "");
//				startActivity(intent);
//			}
//		});
		
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.add_ll){
			Intent intent = new Intent(MainActivity.this, SendActivity.class);
			startActivity(intent);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		NotificationCenter.defaultCenter().deleteObserver(this);
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
		
		if(!refersh){
			this.footerRL.setVisibility(View.VISIBLE);
			this.loadingMoreTV.setVisibility(View.INVISIBLE);
			this.loagingMoreBar.setVisibility(View.VISIBLE);
		}
		this.emptyView.setVisibility(View.GONE);
		this.loadingBar.show();
		Map<String, String> params = NetworkUtil.initRequestParams();
		params.put("act", "getSecret");
		
		JsonObjectRequest request = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				handleData(response, refersh);
				mPullRefreshListView.onRefreshComplete();
				emptyView.setVisibility(View.VISIBLE);
				loadingBar.hide();
				
				if(!refersh){
					loadingMoreTV.setVisibility(View.VISIBLE);
					loagingMoreBar.setVisibility(View.GONE);
					moreLoading = false;
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				mPullRefreshListView.onRefreshComplete();
				emptyView.setVisibility(View.VISIBLE);
				loadingBar.hide();
				
				if(!refersh){
					loadingMoreTV.setVisibility(View.VISIBLE);
					loagingMoreBar.setVisibility(View.GONE);
					moreLoading = false;
				}
			}
		});
		
		request.setTag(this);
		
		AOApplication.getRequestQueue().add(request);
	}

	@Override
	public void update(Observable observable, Object data) {
		if(data instanceof NotificationItem){
			NotificationItem item = (NotificationItem)data;
			int type = item.getType();
			if(type == NotificationItem.TYPE_SEND_SERCET_SUCCESS){
				Sercet sercet = (Sercet)item.getMessage();
				this.mAdapter.insertSercet(sercet);
				this.mListView.smoothScrollToPosition(0);
			}
		}
		
	}
	

 
}
