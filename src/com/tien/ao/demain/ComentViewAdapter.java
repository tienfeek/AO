package com.tien.ao.demain;



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
public class ComentViewAdapter extends BaseAdapter {


	private ViewHolder holder;
	private List<HashMap<String, String>> list;
	private Context context;

	public ComentViewAdapter(Context context, List<HashMap<String, String>> list) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_content__coment, null);
			holder.coment_cotent = (TextView)convertView.findViewById(R.id.coment_cotent);
			convertView.setTag(holder);
		}else{
			holder = (ViewsHolder)convertView.getTag();
		}
		
		holder.coment_cotent.setText(list.get(position).get("coment_cotent"));
		return convertView;
		
	}

	private static class ViewHolder {
		TextView coment_cotent;
	}
	static class ViewsHolder{
		TextView coment_cotent;
		
		
		
		
	}

}
