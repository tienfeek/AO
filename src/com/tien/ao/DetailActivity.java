package com.tien.ao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.tien.ao.adapter.CommentAdapter;
import com.tien.ao.widget.quickreturnheader.QuickReturnHeaderHelper;

public class DetailActivity extends BaseActivity {

	private ListView mCommentLV;
	private RelativeLayout container;

	private CommentAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.detail_layout);
		
		this.findView();
		this.addListener();
		
		QuickReturnHeaderHelper helper = new QuickReturnHeaderHelper(this,
				R.layout.comment_listview, R.layout.header);
		View view = helper.createView();
		container.addView(view);
		
		mCommentLV = (ListView) findViewById(android.R.id.list);
	    adapter = new CommentAdapter(this);
	    mCommentLV.setAdapter(adapter);
	}

	private void findView() {
		
		container = (RelativeLayout)findViewById(R.id.container);
	}

	private void addListener() {
	}


}
