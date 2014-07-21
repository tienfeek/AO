package com.tien.ao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.tien.ao.demain.ComentViewAdapter;
import com.tien.ao.utils.DensityUtil;

public class ListContent extends Activity implements OnClickListener{

	public static final int REFRESH = 0;
	public static final int LOAD = 1;
	private ListView lstv;
	private ComentViewAdapter adapter;
	private ScrollView scv;
	private List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	//弹出菜单按钮
	private ImageView popMenuButton;
	//弹出菜单  分享，关注，删除，屏蔽
	private TextView shareButton;
	private TextView focusButton;
	private TextView delButton;
	private TextView visButton;
	private PopupWindow popupwindow;
	//actionBar 的点赞按钮
	private CheckBox dianzanActionBar;
	//listView 的点赞按钮
	private CheckBox list_item_zanicon;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			List<HashMap<String, String>> result = (List<HashMap<String, String>>) msg.obj;
			switch (msg.what) {
			case ListContent.REFRESH:
				
				list.clear();
				list.addAll(result);
				break;
			case ListContent.LOAD:
				list.addAll(result);
				break;
			}
			setListViewHeightBasedOnChildren(lstv);
			adapter.notifyDataSetChanged();
		};
	};
	public void initData()
	{
		loadData(ListContent.LOAD);
	}
	public List<HashMap<String, String>> getData() {
		List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < 40; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("coment_cotent", "这是评论jjjj");
			data.add(map);
		}
		return data;

	}
	public void loadData(final int what) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(700);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message msg = handler.obtainMessage();
				msg.what = what;
				msg.obj = getData();
				handler.sendMessage(msg);
			}
		}).start();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_content);
		lstv = (ListView) findViewById(R.id.comment_listview);

//		List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < 2; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("coment_cotent", "this is评论");
			//list.add(map);
		}
		initData();
		adapter = new ComentViewAdapter(this, list);
		lstv.setAdapter(adapter);
		this.setListViewHeightBasedOnChildren(lstv);
		scv = (ScrollView) findViewById(R.id.scv);
		scv.smoothScrollTo(0, 0);
		scv.setOnTouchListener(new TouchListenerImpl());
		
		//弹出菜单按钮
		popMenuButton = (ImageView )findViewById(R.id.comment_more);
		popMenuButton.setOnClickListener(this);
		
		//actionBar的点赞按钮
		dianzanActionBar = (CheckBox) findViewById(R.id.dianzanactionbar);
		dianzanActionBar.setOnCheckedChangeListener(new DianzanListenner());
		
		list_item_zanicon = (CheckBox)findViewById(R.id.list_item_zanicon);
		list_item_zanicon.setOnCheckedChangeListener(new DianzanListenner());
		
	}

	/**
	 * 动态设置ListView的高度
	 * 
	 * @param listView
	 */
	public void setListViewHeightBasedOnChildren(ListView listView) {
		if (listView == null)
			return;

		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	private class TouchListenerImpl implements OnTouchListener {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:

				break;
			case MotionEvent.ACTION_MOVE:
				int scrollY = view.getScrollY();
				int height = view.getHeight();
				int scrollViewMeasuredHeight = scv.getChildAt(0)
						.getMeasuredHeight();
				if (scrollY == 0) {
					// System.out.println("滑动到了顶端 view.getScrollY()="+scrollY);
				}
				if ((scrollY + height) == scrollViewMeasuredHeight) {
					// System.out.println("滑动到了底部 scrollY="+scrollY);
					// System.out.println("滑动到了底部 height="+height);
					// System.out.println("滑动到了底部 scrollViewMeasuredHeight="+scrollViewMeasuredHeight);
					//loadData(ListContent.LOAD);
				}
				break;

			default:
				break;
			}
			return false;
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.comment_more:
            if (popupwindow != null&&popupwindow.isShowing()) {  
                popupwindow.dismiss();  
                return;  
            } else {  
            	initPopWindowView();  
                popupwindow.showAsDropDown(view, 0, 28);  
            }  
            break;  
		case R.id.comment_menu_share:
			showShare();
			break;
			
		default:
			break;
		}
	}
	public void initPopWindowView()
	{
        // // 获取自定义布局文件pop.xml的视图  
        View customView = getLayoutInflater().inflate(R.layout.comment_popmemu,  
                null, false);  
        // 创建PopupWindow实例,200,150分别是宽度和高度 
        int width = DensityUtil.dip2px(this, 150);
        int height = DensityUtil.dip2px(this, 180);
        popupwindow = new PopupWindow(customView, width, height);  
        customView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if(  popupwindow!=null&&popupwindow.isShowing())
				{
					popupwindow.dismiss();
					popupwindow=null;
				}
				return false;
			}
		});
        
		shareButton = (TextView) customView.findViewById(R.id.comment_menu_share);
		focusButton = (TextView) customView.findViewById(R.id.comment_memu_focus);
		delButton = (TextView) customView.findViewById(R.id.comment_memu_del);
		visButton = (TextView) customView.findViewById(R.id.comment_menu_vis);
		shareButton.setOnClickListener(this);
		focusButton.setOnClickListener(this);
		delButton.setOnClickListener(this);
		visButton.setOnClickListener(this);
  
	}
	public class DianzanListenner implements OnCheckedChangeListener
	{

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
			// TODO Auto-generated method stub
			if( isChecked )
			{
				list_item_zanicon.setChecked(true);
				dianzanActionBar.setChecked(true);
			}else{
				list_item_zanicon.setChecked(false);
				list_item_zanicon.setChecked(false);
			}
		}
		
	}
	
	  public void showShare() {
		  
	        ShareSDK.initSDK(this);
	        OnekeyShare oks = new OnekeyShare();
	        //关闭sso授权
	        oks.disableSSOWhenAuthorize();
	        
	        // 分享时Notification的图标和文字
	        oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
	        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
	        oks.setTitle(getString(R.string.share));
	        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
	        oks.setTitleUrl("http://sharesdk.cn");
	        // text是分享文本，所有平台都需要这个字段
	        oks.setText("我是分享文本");
	        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
	        oks.setImagePath("/sdcard/test.jpg");
	        // url仅在微信（包括好友和朋友圈）中使用
	        oks.setUrl("http://sharesdk.cn");
	        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
	        oks.setComment("我是测试评论文本");
	        // site是分享此内容的网站名称，仅在QQ空间使用
	        oks.setSite(getString(R.string.app_name));
	        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
	        oks.setSiteUrl("http://sharesdk.cn");

	        // 启动分享GUI
	        oks.show(this);
	        
	   }
	
	
	
	
	
	
	
	
}
