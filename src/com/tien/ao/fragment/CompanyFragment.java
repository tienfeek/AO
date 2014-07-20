package com.tien.ao.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.tien.ao.R;
import com.tien.ao.widget.AutoListView;
import com.tien.ao.widget.AutoListView.OnLoadListener;
import com.tien.ao.widget.AutoListView.OnRefreshListener;


public class CompanyFragment extends Fragment  implements OnRefreshListener,OnLoadListener  {
	
	private AutoListView mListView;
	private CompanyAdapter mAdapter;
	//private ArrayList<Message> messages = new ArrayList<Message>();
	public List<HashMap<String, String>> listData =new ArrayList<HashMap<String,String>>();
	private Handler handler=new Handler(){
		public void handleMessage(Message msg){
			List<HashMap<String, String>> result = (List<HashMap<String, String>>)msg.obj;
			switch (msg.what) {
			case AutoListView.REFRESH:
				mListView.onRefreshComplete();
				listData.clear();
				listData.addAll(result);
				break;
			case AutoListView.LOAD:
				mListView.onLoadComplete();
				listData.addAll(result);
				break;
		
			} 
			mListView.setResultSize(result.size());
			mAdapter.notifyDataSetChanged();
		}
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.company_view, null);
		this.findView(view);
		//listData=this.getData();
		return view;
	}
	
	private void findView(View rootView){
		this.mListView = (AutoListView)rootView.findViewById(R.id.company_listview);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.mAdapter = new CompanyAdapter();
		this.mListView.setAdapter(mAdapter);
		this.mListView.setOnRefreshListener(this);
		this.mListView.setOnLoadListener(this);
		initData();
		this.mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int postion,
					long id) {
				// TODO Auto-generated method stub
			}
		});
	}
	public List<HashMap<String, String>> getData()
	{
		List<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
		Random random = new Random();
		for(int i=0;i<10;i++)
		{
			HashMap<String, String> map = new HashMap<String, String>();
			long l = random.nextInt(10000);
			map.put("content", "东邪西毒"+l);
			map.put("time", "2014-05-03");
			data.add(map);
		}
		return data;
	}
	private void loadData(final int what)
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					Thread.sleep(200);
				}catch(Exception e){
					e.printStackTrace();
				}
				android.os.Message msg = handler.obtainMessage();
				msg.what=what;
				msg.obj=getData();
				handler.sendMessage(msg);
			}
		}).start();
	}
	private void initData() {
		loadData(AutoListView.REFRESH);
	}
	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		loadData(AutoListView.REFRESH);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		loadData(AutoListView.LOAD);
	} 
	/**
	 * adapter 静态类
	 * @author wangjun11
	 *
	 */
	
	class CompanyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return  listData.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.company_listview_item, null);
				holder.contentTV = (TextView)convertView.findViewById(R.id.content_tv);
				holder.time=(TextView)convertView.findViewById(R.id.time);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			
			holder.contentTV.setText(listData.get(position).get("content"));
			holder.time.setText(listData.get(position).get("time"));
			return convertView;
			
		}
		
	}
	
	static class ViewHolder{
		TextView contentTV;
		TextView time;
	}

	

}
