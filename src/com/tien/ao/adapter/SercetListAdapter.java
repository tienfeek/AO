package com.tien.ao.adapter;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tien.ao.AOApplication;
import com.tien.ao.Constant;
import com.tien.ao.DetailActivity;
import com.tien.ao.R;
import com.tien.ao.demain.Sercet;
import com.tien.ao.utils.NetworkUtil;
import com.tien.ao.utils.StringUtil;
import com.tien.ao.utils.ViewHolder;
import com.tien.ao.volley.Response;
import com.tien.ao.volley.VolleyError;
import com.tien.ao.volley.imagecache.ImageCacheManager;
import com.tien.ao.volley.toolbox.ImageLoader;
import com.tien.ao.volley.toolbox.ImageLoader.ImageListener;
import com.tien.ao.volley.toolbox.JsonObjectRequest;

public class SercetListAdapter extends BaseAdapter {

	private List<Sercet> sercets;
	private ImageLoader mImageLoader;  
	private Context context;
	
	public SercetListAdapter(List<Sercet> sercets, Context context){
		this.sercets = sercets;
		mImageLoader = ImageCacheManager.getInstance().getImageLoader();
		this.context = context;
	}
	
	public void setData(boolean append, List<Sercet> datas){
		if(!append){
			this.sercets.clear();
		}
		this.sercets.addAll(datas);
	}
	
	public void insertSercet(Sercet sercet){
		this.sercets.add(0, sercet);
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return sercets.size();
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sercet_listview_item, null);
		}
		
		ImageView bgIV = ViewHolder.get(convertView, R.id.bg_iv);
		TextView contentTV = ViewHolder.get(convertView, R.id.content_tv);
		TextView timeTV = ViewHolder.get(convertView, R.id.time_tv);
		TextView favourCountTV = ViewHolder.get(convertView, R.id.favour_count_tv);
		ImageView favouriV = ViewHolder.get(convertView, R.id.favour_iv);
		TextView commentCountTV = ViewHolder.get(convertView, R.id.comment_count_tv);
		ImageView commentIV = ViewHolder.get(convertView, R.id.comment_iv);
		
		final Sercet sercet = sercets.get(position);
		
		contentTV.setText(sercet.getContent());
		timeTV.setText(StringUtil.formatTime(sercet.getAddtime()));
		commentCountTV.setText(String.valueOf(sercet.getCommentcount()));
		if(sercet.getBgtype() == 1){
		    if(!"".equals(sercet.getBgurl()) ){
                String url = Constant.URL_PHOTO_PREFIX + sercet.getBgurl();
                ImageListener listener = ImageLoader.getImageListener(bgIV, R.drawable.l_1, R.drawable.l_1);    
                mImageLoader.get(url, listener); 
            }
			
		}else if(sercet.getBgtype() > 1){
			
			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.s_3);  
			//bitmap = Bitmap.createBitmap(100, 20, Config.ARGB_8888);   
			BitmapDrawable drawable = new BitmapDrawable(bitmap);  
			drawable.setTileModeXY(TileMode.REPEAT , TileMode.REPEAT );  
			drawable.setDither(true);  
		    bgIV.setImageDrawable(drawable);
		    bgIV.setTag("");
		    //int val = sercet.getBgtype() + R.drawable.l_1 - 100;
		    //bgIV.setImageResource(sercet.getBgtype());
//		    String puppetUrl = "system_res"+sercet.getBgtype();
//		    Bitmap bitmap = ImageCacheManager.getInstance().getBitmap(puppetUrl);
//		    if(bitmap == null){
//		    	Drawable drawable = context.getResources().getDrawable(sercet.getBgtype());
//		    	bitmap = ImageUtil.drawableToBitmap(drawable);
//		    	
//		    	if(bitmap != null){
//		    		ImageCacheManager.getInstance().putBitmap(puppetUrl, bitmap);
//		    		XLog.i("wanges", "ImageCacheManager putBitmap");
//		    	}
//		    }
//		    
//		    bgIV.setImageBitmap(bitmap);
//		    ImageListener listener = ImageLoader.getImageListener(bgIV, R.drawable.l_1, R.drawable.l_1);   
//		    mImageLoader.getNative(sercet.getBgtype(), listener);
		    
		}
		
		favouriV.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				favour(1, sercet.getSecretid());
			}
		});
		
		convertView.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra("secretId", sercet.getSecretid());
                intent.putExtra("content", sercet.getContent());
                intent.putExtra("addtime", sercet.getAddtime());
                intent.putExtra("bgtype", sercet.getBgtype());
                intent.putExtra("bgurl", sercet.getBgurl());
                intent.putExtra("favorcount", sercet.getFavorcount());
                intent.putExtra("commentcount", sercet.getCommentcount());
                v.getContext().startActivity(intent);
                
            }
        });
		
		return convertView;
	}
	
	private void favour(int isFavor, String sercetId){
		String url = "";
		Map<String, String> params = NetworkUtil.initRequestParams();
		params.put("intFavorType", "2");
		params.put("intIsFavor", String.valueOf(isFavor));
		params.put("intId", sercetId);
		
		JsonObjectRequest request = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				
			}
		});
		
		request.setTag(context);
		AOApplication.getRequestQueue().add(request);
	}

}
