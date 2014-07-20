package com.tien.ao.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tien.ao.R;


/**
 * @author SunnyCoffee
 * @date 2014-2-2
 * @version 1.0
 * @desc 适配器
 * 
 */
public class ListViewAdapter extends BaseAdapter {

	private ViewHolder holder;
	private List<HashMap<String, String>> list;
	private Context context;

	public ListViewAdapter(Context context, List<HashMap<String, String>> list) {
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size();
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
		ViewsHolder holder = null;
		if(convertView == null){
			holder = new ViewsHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.company_listview_item, null);
			holder.contentTV = (TextView)convertView.findViewById(R.id.content_tv);
			holder.time=(TextView)convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		}else{
			holder = (ViewsHolder)convertView.getTag();
		}
		
		holder.contentTV.setText(list.get(position).get("content"));
		holder.time.setText(list.get(position).get("time"));
		return convertView;
		
	}

	private static class ViewHolder {
		TextView text;
	}
	static class ViewsHolder{
		TextView contentTV;
		TextView time;
		ImageView list_item_menu;
		TextView list_item_commentnum;
		ImageView list_item_commenticon;
		TextView list_item_zannum;
		ImageView list_item_zanicon;
		
		
		
	}

}
