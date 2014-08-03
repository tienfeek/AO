package com.tien.ao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tien.ao.adapter.CommentAdapter;
import com.tien.ao.demain.Comment;
import com.tien.ao.demain.Sercet;
import com.tien.ao.utils.KeyboardHelper;
import com.tien.ao.utils.NetworkUtil;
import com.tien.ao.utils.StringUtil;
import com.tien.ao.utils.ToastUtil;
import com.tien.ao.volley.Response;
import com.tien.ao.volley.VolleyError;
import com.tien.ao.volley.imagecache.ImageCacheManager;
import com.tien.ao.volley.toolbox.ImageLoader;
import com.tien.ao.volley.toolbox.JsonObjectRequest;
import com.tien.ao.volley.toolbox.ImageLoader.ImageListener;
import com.tien.ao.widget.quickreturnheader.QuickReturnHeaderHelper;

public class DetailActivity extends BaseActivity implements OnClickListener{

	private LinearLayout backLL;
	private ListView mCommentLV;
	private RelativeLayout container;
	private ImageView bgIV;
	private TextView contentTV;
	private TextView timeTV;
	private TextView favourCountTV;
	private ImageView favouriV;
	private TextView commentCountTV;
	private ImageView commentIV;
	private EditText commentET;
	private Button sendBtn;
	private TextView emptyView;
	private ContentLoadingProgressBar loadingBar;
	

	private CommentAdapter adapter;
	
	private Sercet sercet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.detail_layout);
		this.getSercetFromIntent();
		
		this.container = (RelativeLayout)findViewById(R.id.container);
		QuickReturnHeaderHelper helper = new QuickReturnHeaderHelper(this,
				R.layout.comment_listview, R.layout.header);
		View view = helper.createView();
		this.container.addView(view);
		
		this.findView();
		this.addListener();
		
		this.mCommentLV = (ListView) findViewById(android.R.id.list);
		this.mCommentLV.setEmptyView(emptyView);
		
		this.adapter = new CommentAdapter(this, sercet.getSecretid());
		this.mCommentLV.setAdapter(adapter);
		
		this.initSercet();
		this.loadComment(true);
	}

	private void findView() {
		
		this.backLL = (LinearLayout)findViewById(R.id.back_ll);
		
		this.bgIV = (ImageView)findViewById(R.id.bg_iv);
		this.contentTV = (TextView)findViewById(R.id.content_tv);
		this.timeTV = (TextView)findViewById(R.id.time_tv);
		this.favourCountTV = (TextView)findViewById(R.id.favour_count_tv);
		this.favouriV = (ImageView)findViewById(R.id.favour_iv);
		this.commentCountTV = (TextView)findViewById(R.id.comment_count_tv);
		this.commentIV = (ImageView)findViewById(R.id.comment_iv);
		this.sendBtn = (Button)findViewById(R.id.send_btn);
		this.commentET = (EditText)findViewById(R.id.comment_et);
		this.emptyView = (TextView)findViewById(R.id.emptyview);
		this.loadingBar = (ContentLoadingProgressBar)findViewById(R.id.loading_pb);
		
	}
	

	private void addListener() {
		this.backLL.setOnClickListener(this);
		this.favouriV.setOnClickListener(this);
		this.sendBtn.setOnClickListener(this);
	}

	
	private void getSercetFromIntent(){
		Intent intent = getIntent();
		
		if(intent != null){
			sercet = new Sercet();
			sercet.setSecretid(intent.getStringExtra("secretId"));
			sercet.setContent(intent.getStringExtra("content"));
			sercet.setAddtime(intent.getLongExtra("addtime", 0));
			sercet.setBgtype(intent.getIntExtra("bgtype", 0));
			sercet.setBgurl(intent.getStringExtra("bgurl"));
			sercet.setFavorcount(intent.getIntExtra("favorcount", 0));
			sercet.setCommentcount(intent.getIntExtra("commentcount", 0));
			
		}else{
			finish();
		}
	}
	
	private void initSercet(){
		if(sercet.getBgtype() == 1){
			ImageListener listener = ImageLoader.getImageListener(bgIV, R.drawable.l_1, R.drawable.l_1);    
			ImageCacheManager.getInstance().getImageLoader().get(Constant.URL_PHOTO_PREFIX + sercet.getBgurl(), listener); 
		}else{
			this.bgIV.setImageResource(sercet.getBgtype());
		}
		
		String conent = sercet.getContent();
		this.contentTV.setText(conent);
		this.favourCountTV.setText(String.valueOf(sercet.getFavorcount()));
		this.commentCountTV.setText(String.valueOf(sercet.getCommentcount()));
		this.timeTV.setText(StringUtil.formatTime(sercet.getAddtime()));
		
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.back_ll){
			finish();
		}else if(v.getId() == R.id.favour_iv){
			favour(1);
		}else if(v.getId() == R.id.send_btn){
			String content = this.commentET.getEditableText().toString().trim();
			if("".equals(content)){
				ToastUtil.shortToast("评论内容不能为空！");
				return;
			}
			this.sendComment(content);
		}
		
	}
	
	private void handleData(JSONObject json, boolean refersh){
		if(json.optInt("status") == 0){
			JSONArray array = json.optJSONArray("data");
			if(array != null){
				
				Gson gson = new Gson();
				ArrayList<Comment> newSercets = gson.fromJson(array.toString(), new TypeToken<List<Comment>>(){}.getType());
				
				adapter.setData(newSercets, !refersh);
				adapter.notifyDataSetChanged();
			}
		}
	}
	
	private void loadComment(final boolean refersh){
		this.emptyView.setVisibility(View.GONE);
		this.loadingBar.show();
		String url = Constant.URL_REQUEST;
		
		Map<String, String> params = NetworkUtil.initRequestParams();
		params.put("ctrl", "comment");
		params.put("act", "getcomment");
		params.put("intSecretId", sercet.getSecretid());
		
		JsonObjectRequest request = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				
				handleData(response, refersh);
				loadingBar.hide();
				emptyView.setVisibility(View.VISIBLE);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				loadingBar.hide();
				emptyView.setVisibility(View.VISIBLE);
			}
		});
		
		request.setTag(this);
		
		AOApplication.getRequestQueue().add(request);
	}
	
	private void handleFavourResult(JSONObject json, int favour){
		int status = json.optInt("status");
		if(status == 0){
			if(favour == 1){
				this.favouriV.setPressed(true);
			}else{
				this.favouriV.setPressed(false);
			}
		}else{
			if(favour == 1){
				ToastUtil.shortToast("点赞失败");
			}else{
				ToastUtil.shortToast("取消点赞失败");
			}
			
		}
	}
	
	private void favour(final int favour){
		String url = Constant.URL_REQUEST;
		
		Map<String, String> params = NetworkUtil.initRequestParams();
		params.put("ctrl", "favorite");
		params.put("act", "index");
		params.put("intFavorType", "2");
		params.put("intIsFavor", String.valueOf(favour));
		params.put("intId", sercet.getSecretid());
		
		JsonObjectRequest request = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				handleFavourResult(response, favour);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
			}
		});
		
		request.setTag(this);
		
		AOApplication.getRequestQueue().add(request);
	}

	
	private void handleCommentResult(JSONObject json){
		int status = json.optInt("status");
		if(status == 0){
			this.commentET.setText("");
			KeyboardHelper.hideSoftKeyboard(this, commentET);
			
			JSONObject data = json.optJSONObject("data");
			Gson gson = new Gson();
			Comment comment = gson.fromJson(data.toString(), Comment.class);
			
			adapter.insertComment(comment);
			mCommentLV.smoothScrollToPosition(0);
		}else{
			ToastUtil.shortToast("发表评论失败");
		}
	}
	
	private void sendComment(String comment){
		String url = Constant.URL_REQUEST;
		
		Map<String, String> params = NetworkUtil.initRequestParams();
		params.put("ctrl", "comment");
		params.put("act", "newcomment");
		params.put("intSecretId", sercet.getSecretid());
		params.put("strContent", comment);
		
		JsonObjectRequest request = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				handleCommentResult(response);
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
