package com.tien.ao;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tien.ao.adapter.CommentAdapter;
import com.tien.ao.widget.QuickReturnListView;

public class DetailActivity extends BaseActivity {
	
	private QuickReturnListView mCommentLV;
	private View mHeader;
	private View mQuickReturnView;
	private View mPlaceHolder;

	private int mCachedVerticalScrollRange;
	private int mQuickReturnHeight;

	private static final int STATE_ONSCREEN = 0;
	private static final int STATE_OFFSCREEN = 1;
	private static final int STATE_RETURNING = 2;
	private int mState = STATE_ONSCREEN;
	private int mScrollY;
	private int mMinRawY = 0;

	private TranslateAnimation anim;
	private CommentAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.detail_layout);
		this.findView();
		this.addListener();
		
		
		mCommentLV.addHeaderView(mHeader);
		
		adapter = new CommentAdapter(this);
		mCommentLV.setAdapter(adapter);

		
	}
	
	private void findView(){
		mHeader = LayoutInflater.from(this).inflate(R.layout.header, null);
		mCommentLV = (QuickReturnListView) findViewById(R.id.comment_lv);
		mQuickReturnView =  findViewById(R.id.detail_include);
		mPlaceHolder = mHeader.findViewById(R.id.placeholder);
	}
	
	private void addListener(){
		mCommentLV.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						mQuickReturnHeight = mQuickReturnView.getHeight();
						mCommentLV.computeScrollY();
						mCachedVerticalScrollRange = mCommentLV.getListHeight();
					}
				});

		mCommentLV.setOnScrollListener(new OnScrollListener() {
			@SuppressLint("NewApi")
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				mScrollY = 0;
				int translationY = 0;
				
				if (mCommentLV.scrollYIsComputed()) {
					mScrollY = mCommentLV.getComputedScrollY();
				}

				int rawY = mPlaceHolder.getTop()
						- Math.min(
								mCachedVerticalScrollRange
										- mCommentLV.getHeight(), mScrollY);
				
				switch (mState) {
				case STATE_OFFSCREEN:
					if (rawY <= mMinRawY) {
						mMinRawY = rawY;
					} else {
						mState = STATE_RETURNING;
					}
					translationY = rawY;
					break;

				case STATE_ONSCREEN:
					if (rawY < -mQuickReturnHeight) {
						mState = STATE_OFFSCREEN;
						mMinRawY = rawY;
					}
					translationY = rawY;
					break;

				case STATE_RETURNING:
					translationY = (rawY - mMinRawY) - mQuickReturnHeight;
					if (translationY > 0) {
						translationY = 0;
						mMinRawY = rawY - mQuickReturnHeight;
					}

					if (rawY > 0) {
						mState = STATE_ONSCREEN;
						translationY = rawY;
					}

					if (translationY < -mQuickReturnHeight) {
						mState = STATE_OFFSCREEN;
						mMinRawY = rawY;
					}
					break;
				}
				
				/** this can be used if the build is below honeycomb **/
				if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
					anim = new TranslateAnimation(0, 0, translationY,
							translationY);
					anim.setFillAfter(true);
					anim.setDuration(0);
					mQuickReturnView.startAnimation(anim);
				} else {
					mQuickReturnView.setTranslationY(translationY);
				}

			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
		});
	}

}
