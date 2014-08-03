package com.tien.ao.adapter;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONObject;


import com.tien.ao.AOApplication;
import com.tien.ao.Constant;
import com.tien.ao.R;
import com.tien.ao.demain.Comment;
import com.tien.ao.utils.NetworkUtil;
import com.tien.ao.utils.StringUtil;
import com.tien.ao.utils.ToastUtil;
import com.tien.ao.utils.ViewHolder;
import com.tien.ao.volley.Response;
import com.tien.ao.volley.VolleyError;
import com.tien.ao.volley.imagecache.ImageCacheManager;
import com.tien.ao.volley.toolbox.ImageLoader;
import com.tien.ao.volley.toolbox.JsonObjectRequest;
import com.tien.ao.volley.toolbox.ImageLoader.ImageListener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter {
	
	private ArrayList<Comment> comments = new ArrayList<Comment>();
	
	private ImageLoader mImageLoader;
	private String sercetId = "";
	private Context context;
	
	public CommentAdapter(Context context, String sercetId){
		this.context = context;
		this.sercetId = sercetId;
		this.mImageLoader = ImageCacheManager.getInstance().getImageLoader();
	}
	
	public void setData(ArrayList<Comment> newComments, boolean append){
		if(append){
			this.comments.addAll(newComments);
		}else{
			this.comments.clear();
			this.comments = newComments;
		}
	}
	
	public void insertComment(Comment comment){
		this.comments.add(0, comment);
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return comments.size();
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
		if(convertView == null){
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_listview_item, null);
		}
		
		ImageView avatarIV = ViewHolder.get(convertView, R.id.avatar_iv);
		TextView contentTV = ViewHolder.get(convertView, R.id.comment_tv);
		TextView timeTV = ViewHolder.get(convertView, R.id.time_tv);
		ImageView favouriIV = ViewHolder.get(convertView, R.id.favour_iv);
		
		
		final Comment comment = comments.get(position);
		
		ImageListener listener = ImageLoader.getImageListener(avatarIV, R.drawable.ic_launcher, R.drawable.ic_launcher);    
        mImageLoader.get("", listener); 
        
        contentTV.setText(comment.getContent());
        timeTV.setText(StringUtil.formatTime(comment.getAddtime()));
        
        favouriIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				favour(1 , v, comment);
			}
		});
		return convertView;
	}
	
	private void handleFavourResult(JSONObject response, int favour, View v, Comment comment){
		int status = response.optInt("status");
		ImageView favourIV = (ImageView)v;
		if(status == 0){
			if(favour == 1){
				favourIV.setPressed(true);
			}else{
				favourIV.setPressed(false);
			}
		}else{
			if(favour == 1){
				ToastUtil.shortToast("点赞失败");
			}else{
				ToastUtil.shortToast("取消点赞失败");
			}
			
		}
			
	}
	
	private void favour(final int favour, final View view ,final Comment comment){
		String url = Constant.URL_REQUEST;
		
		Map<String, String> params = NetworkUtil.initRequestParams();
		params.put("intFavorType", "3");
		params.put("intIsFavor", String.valueOf(favour));
		params.put("intId", sercetId);
		
		JsonObjectRequest request = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				handleFavourResult(response, favour, view, comment);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
			}
		});
		
		request.setTag(this);
		
		AOApplication.getRequestQueue().add(request);
	}

}
