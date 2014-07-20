package com.tien.ao.adapter;

import java.util.ArrayList;


import com.tien.ao.R;
import com.tien.ao.demain.Comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter {
	
	private ArrayList<Comment> comments = new ArrayList<Comment>();
	
	private Context context;
	
	public CommentAdapter(Context context){
		this.context = context;
	}

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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.comment_listview_item, null);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	
	static class ViewHolder{
		ImageView avatarIV;
		TextView contentTV;
		TextView timeTV;
	}
}
