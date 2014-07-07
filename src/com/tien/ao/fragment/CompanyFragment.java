package com.tien.ao.fragment;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tien.ao.R;
import com.tien.ao.demain.Message;

public class CompanyFragment extends Fragment {
	
	private ListView mListView;
	private CompanyAdapter mAdapter;
	private ArrayList<Message> messages = new ArrayList<Message>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.company_view, null);
		this.findView(view);
		return view;
	}
	
	private void findView(View rootView){
		this.mListView = (ListView)rootView.findViewById(R.id.company_listview);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		this.mAdapter = new CompanyAdapter();
		this.mListView.setAdapter(mAdapter);
	}
	
	
	
	
	
	
	
	
	class CompanyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return 20;
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
				
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			
			
			return convertView;
		}
		
	}
	
	static class ViewHolder{
		TextView contentTV;
	} 

}
