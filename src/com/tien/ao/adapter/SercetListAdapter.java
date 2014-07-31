package com.tien.ao.adapter;

import java.util.List;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tien.ao.Constant;
import com.tien.ao.DetailActivity;
import com.tien.ao.R;
import com.tien.ao.demain.Sercet;
import com.tien.ao.utils.ViewHolder;
import com.tien.ao.utils.XLog;
import com.tien.ao.volley.imagecache.ImageCacheManager;
import com.tien.ao.volley.toolbox.ImageLoader;
import com.tien.ao.volley.toolbox.ImageLoader.ImageListener;
import com.tien.ao.volley.toolbox.NetworkImageView;

public class SercetListAdapter extends BaseAdapter {

	private List<Sercet> sercets;
	private ImageLoader mImageLoader;  
	
	public SercetListAdapter(List<Sercet> sercets){
		this.sercets = sercets;
		mImageLoader = ImageCacheManager.getInstance().getImageLoader();
	}
	
	public void setData(boolean append, List<Sercet> datas){
		if(!append){
			this.sercets.clear();
		}
		this.sercets.addAll(datas);
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
		
		Sercet sercet = sercets.get(position);
		
		contentTV.setText(sercet.getContent());
		XLog.i("wanges", "sercet.getBgtype()" +sercet.getBgtype());
		if(sercet.getBgtype() == 1){
		    if(!"".equals(sercet.getBgurl()) ){
		        bgIV.setTag(sercet.getBgurl());
                String url = Constant.URL_REQUEST+"/upload/background/" + sercet.getBgurl();
                XLog.i("wanges", position+" "+sercet.getContent() + "bg url:"+url);
//                 bgIV.setImageUrl(url, ImageCacheManager.getInstance().getImageLoader());
//              ImageCacheManager.getInstance().getImageLoader();
//              ImageLoader.getImageListener(bgIV, R.drawable.l_1, R.drawable.l_1);
                ImageListener listener = ImageLoader.getImageListener(bgIV, R.drawable.l_1, R.drawable.l_1);    
                mImageLoader.get(url, listener); 
            }
			
		}else if(sercet.getBgtype() > 1){
		    bgIV.setTag("");
		    bgIV.setImageResource(sercet.getBgtype());
            XLog.i("wanges", position+" "+sercet.getContent() + " "+sercet.getBgtype());
		}
		
		favouriV.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		convertView.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra("secretId", "");
                v.getContext().startActivity(intent);
                
            }
        });
		
		return convertView;
	}

}
