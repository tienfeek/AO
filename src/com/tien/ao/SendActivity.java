package com.tien.ao;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tien.ao.demain.Sercet;
import com.tien.ao.fragment.*;
import com.tien.ao.net.FormFile;
import com.tien.ao.net.HttpResult;
import com.tien.ao.net.ProtocolClient;
import com.tien.ao.notification.NotificationCenter;
import com.tien.ao.notification.NotificationItem;
import com.tien.ao.utils.FileUtil;
import com.tien.ao.utils.ImageUtil;
import com.tien.ao.utils.KeyboardHelper;
import com.tien.ao.utils.NetworkUtil;
import com.tien.ao.utils.ToastUtil;
import com.tien.ao.utils.XLog;
import com.tien.ao.volley.Response;
import com.tien.ao.volley.VolleyError;
import com.tien.ao.volley.toolbox.JsonObjectRequest;
import com.tien.ao.widget.AoProgressDialog;
import com.tien.ao.widget.KeyboardListenLinearLayout;
import com.tien.ao.widget.KeyboardListenLinearLayout.IOnKeyboardStateChangedListener;
import com.tien.ao.widget.crop.CropImage;
import com.tien.ao.widget.crop.CropUtil;
import com.tien.ao.widget.viewpagerindicator.IconPageIndicator;

public class SendActivity extends FragmentActivity implements OnClickListener {

	public static final int TAKE_PRICTURE = 1000;
	public static final int LOC_PRICTURE = 1001;
	public static final int PHOTO_RESULT = 1002;

	private LinearLayout backLL;
	private Button sendBtn;
	private EditText contentET;
	private ImageView bgIV;
	private ImageView photoIV;
	private ImageView templateIV;
	private RelativeLayout bottomRL;
	private TextView tipTV;
	private LinearLayout bgLL;
	private ViewPager pager;
	private IconPageIndicator mIndicator;
	private KeyboardListenLinearLayout container;
	
	private int defaultTemplateResId;

	private boolean keywordStatus = false;
	private String picPath = "";
	private String prePicPath = "";
	private String cameraPic = "";
	private Bitmap picBitmap = null;
	private String content = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.send_layout);
		this.findView();
		this.addListener();
		
		templateReselected(randomTemplate());
		
		TemplateFragmentAdapter mAdapter = new TemplateFragmentAdapter(getSupportFragmentManager());
        
		pager.setAdapter(mAdapter);

        mIndicator = (IconPageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(pager);
	}

	private void findView() {
		this.container = (KeyboardListenLinearLayout) findViewById(R.id.container);
		this.backLL = (LinearLayout) findViewById(R.id.back_ll);
		this.sendBtn = (Button) findViewById(R.id.send_btn);
		this.contentET = (EditText) findViewById(R.id.content_tv);
		this.bgIV = (ImageView) findViewById(R.id.bg_iv);
		this.photoIV = (ImageView) findViewById(R.id.photo_iv);
		this.templateIV = (ImageView) findViewById(R.id.template_iv);
		this.bottomRL = (RelativeLayout) findViewById(R.id.bottom_rl);
		this.tipTV = (TextView) findViewById(R.id.tip_tv);
		this.bgLL = (LinearLayout) findViewById(R.id.bg_ll);
		this.pager = (ViewPager)findViewById(R.id.pager);
		this.mIndicator = (IconPageIndicator)findViewById(R.id.indicator);
		
	}

	private void addListener() {
		this.backLL.setOnClickListener(this);
		this.sendBtn.setOnClickListener(this);
		this.photoIV.setOnClickListener(this);
		this.templateIV.setOnClickListener(this);
		this.contentET.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				if (keywordStatus) {
//					KeyboardHelper.hideSoftKeyboard(getApplicationContext(), v);
//					bottomRL.setVisibility(View.VISIBLE);
//				} else {
					bottomRL.setVisibility(View.GONE);
//				}

			}
		});
		this.container.setOnKeyboardStateChangedListener(new IOnKeyboardStateChangedListener() {

					public void onKeyboardStateChanged(int state) {
						switch (state) {
						case KeyboardListenLinearLayout.KEYBOARD_STATE_HIDE:// 软键盘隐藏
							keywordStatus = false;
							bottomRL.setVisibility(View.VISIBLE);
							break;
						case KeyboardListenLinearLayout.KEYBOARD_STATE_SHOW:// 软键盘显示
							keywordStatus = true;
							break;
						default:
							break;
						}
					}
				});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.back_ll) {
			finish();
		} else if (v.getId() == R.id.send_btn) {
			sendSercet();
		} else if (v.getId() == R.id.photo_iv) {
			showPicSelectDialog();
		} else if (v.getId() == R.id.template_iv) {
			changeTemplate();
		}
	}
	
	private void sendSercet(){
		
		content = contentET.getEditableText().toString().trim();
		if("".equals(content)){
			ToastUtil.shortToast("亲，你还没添加内容！");
			return;
		}
		
		if("".equals(picPath)){
			int val = defaultTemplateResId - R.drawable.l_1 + 100;
			sendSercet(content, defaultTemplateResId, "");
		}else{
			new uploadBgAsyncTask(content).execute();
		}
	}
	
	private int randomTemplate(){
		Random   rand   =   new   Random();   
		int num = rand.nextInt(30);
		
		return R.drawable.l_1 + num;
	}
	
	private void changeTemplate(){
		KeyboardHelper.hideSoftKeyboard(getApplicationContext(), contentET);
		if(bottomRL.getVisibility() == View.GONE){
			this.bottomRL.setVisibility(View.VISIBLE);
		}
		if(tipTV.getVisibility() == View.VISIBLE){
			this.tipTV.setVisibility(View.GONE);
			this.bgLL.setVisibility(View.VISIBLE);
		}else{
			this.tipTV.setVisibility(View.VISIBLE);
			this.bgLL.setVisibility(View.GONE);
		}
	}
	
	public void templateReselected(int resId){
		this.bgIV.setBackgroundResource(resId);
		this.defaultTemplateResId = resId;
	}

	private void showPicSelectDialog() {
		Builder builder = new Builder(new ContextThemeWrapper(this,R.style.AlertDialogCustom));
		builder.setItems(R.array.pic_item,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent intentLocPic = new Intent(
									Intent.ACTION_GET_CONTENT);
							intentLocPic.setType("image/*");
							intentLocPic.addCategory(Intent.CATEGORY_OPENABLE);
							startActivityForResult(intentLocPic, LOC_PRICTURE);
							break;
						case 1:
							// 调用相机
							String status = Environment
									.getExternalStorageState();
							if (status.equals(Environment.MEDIA_MOUNTED)) {
								Intent intentTakePic = new Intent(
										MediaStore.ACTION_IMAGE_CAPTURE);
								picPath = Environment.getExternalStorageDirectory()+ "/ao"+ "/";
								FileUtil.createDir(picPath);
								picPath += System.currentTimeMillis() + ".jpg";
								// 照片保存路径
								File myCaptureFile = new File(picPath);
								Uri uri = Uri.fromFile(myCaptureFile);
								intentTakePic.putExtra(MediaStore.EXTRA_OUTPUT, uri);
								intentTakePic.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
								startActivityForResult(intentTakePic, TAKE_PRICTURE);
							} else {
								ToastUtil.shortToast("没有存储卡!");
							}
							break;
						case 2:
							break;
						}
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inTempStorage = new byte[1024 * 1024 * 2]; // 2MB的临时存储空间
		opt.inSampleSize = 2;
		opt.inJustDecodeBounds = false;

		if (requestCode == LOC_PRICTURE) {
			ContentResolver cr = getContentResolver();
			// 相册
			if (data != null) {
				try {
					Uri uri = data.getData();
					if (uri != null) {
						XLog.i("From Gallary result uri is:" + uri.toString());
						if (uri.toString().contains("file://")) {
							picPath = uri.getPath();
							XLog.i("From Gallary file result path is:"+ picPath);
							if (ImageUtil.isImage(picPath)) {
								// 缩放图片
								picBitmap = BitmapFactory.decodeFile(picPath, opt);
							} else {
								ToastUtil.shortToast("图片路径不存在！");
								return;
							}
						} else if (uri.toString().contains("content://")) {
							picBitmap = BitmapFactory.decodeStream(
									cr.openInputStream(uri), null, opt);
							// 获取实际路径
							String[] proj = { MediaStore.Images.Media.DATA };
							Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
							int actual_image_column_index = actualimagecursor
									.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
							actualimagecursor.moveToFirst();
							// // 这里如果是调用媒体库返回的图片数据，则调用剪切程序进行剪切
							picPath = actualimagecursor.getString(actual_image_column_index);
							XLog.i("From Gallary content result path is:"+ picPath);
						}
					}

				} catch (FileNotFoundException e) {
					e.printStackTrace();
					ToastUtil.shortToast("图片路径不存在！");
				}
			}
			cropImage();
			return;
		} else if (requestCode == TAKE_PRICTURE) {
			// 相机
			if (requestCode == TAKE_PRICTURE || requestCode == -1) {
				// 第二照相取消时，没有图片，使用第一次的图片
				if ("".equals(cameraPic)) {
					File file = new File(prePicPath);
					if (file.exists()) {
						picPath = prePicPath;
					}
				} else {
					picPath = cameraPic;
				}
				// 缩放图片
				Options options = ImageUtil.getOptions(picPath);
				picBitmap = BitmapFactory.decodeFile(picPath);
			}
			cropImage();
			return;
		}
		// 调用图片剪切程序返回数据
		if (requestCode == PHOTO_RESULT) {
			XLog.i("is from crop image result");
			if (data != null) {
				picPath = data.getStringExtra("cropImagePath");
				XLog.i("is from crop image result cropImagePath:" + picPath);
				// 剪切后的图片 存放 sdcard/35ewave/photo
				picBitmap = BitmapFactory.decodeFile(picPath, opt);
				picBitmap = ImageUtil.toRoundCorner(picBitmap, 5);
				setPic();
				return;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void cropImage() {
		// 把得到的图片绑定在控件上显示
		if (picBitmap != null) { // 调用剪切程序
			// 将选择的图片等比例压缩后缓存到存储卡根目录，并返回图片文件
			File f = CropUtil.makeTempFile(picBitmap, "TEMPIMG");
			// 调用CropImage类对图片进行剪切
			Intent intent = new Intent(this, CropImage.class);
			Bundle extras = new Bundle();
			extras.putString("circleCrop", "true");
			extras.putInt("aspectX", 200);
			extras.putInt("aspectY", 200);
			intent.setDataAndType(Uri.fromFile(f), "image/jpeg");
			intent.putExtras(extras);
			startActivityForResult(intent, PHOTO_RESULT);
		}
	}

	private void setPic() {
		if (picBitmap != null) {
			this.bgIV.setImageBitmap(picBitmap);
		}
	}

	AoProgressDialog mProgressDialog;

	private class uploadBgAsyncTask extends AsyncTask<Void, Void, HttpResult> {
	    
	    private String content = "";
	    
	    public uploadBgAsyncTask(String content){
	        this.content = content;
	    }

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = AoProgressDialog.createDialog(SendActivity.this);
			mProgressDialog.show();
		}

		@Override
		protected HttpResult doInBackground(final Void... params) {
			HttpResult mHttpResult = null;
			try {
				if (!"".equals(picPath)) {
					FormFile imageFile = FormFile.buildFormFile(picPath, 600, 600, "resSecretBg");
					FormFile[] formfiles = new FormFile[] { imageFile };
					mHttpResult = uploadBg(content, formfiles, SendActivity.this);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mHttpResult;
		}

		@Override
		protected void onPostExecute(HttpResult result) {
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			handleSendSercetResult((JSONObject)result.getData());
		}
	}

	public HttpResult uploadBg(String content ,FormFile[] mFormFiles, Context mContext) {
		HttpResult mHttpResult = null;
		Map<String, String> params = NetworkUtil.initRequestParams();
		params.put("ctrl", "secret");
		params.put("act", "newsecret");
		params.put("strContent", content);
		params.put("intBgType", "1");
		try {
			mHttpResult = ProtocolClient.postWithFile(mContext, params, mFormFiles);
			String json = mHttpResult.getJson();
			XLog.i("wanges:" + json);
			if (!"".equals(json)) {
              JSONObject jsonObjcet = new JSONObject(json);
              String url = jsonObjcet.optString("data");
              mHttpResult.setData(jsonObjcet);
			}

		} catch (Exception e) {
			e.printStackTrace();
			mHttpResult = new HttpResult();
			mHttpResult.setStatus(false);
		}
		// 根据网络在UI以吐司方式提示
		return mHttpResult;
	}
	
	private void handleSendSercetResult(JSONObject response) {
		if(response.optInt("status") == 0){
			Gson gson = new Gson();
			JSONObject data = response.optJSONObject("data");
			if(data != null){
				Sercet sercet = gson.fromJson(data.toString(), Sercet.class);
				
				NotificationCenter.defaultCenter().postNotification(new NotificationItem(NotificationItem.TYPE_SEND_SERCET_SUCCESS, sercet));
				finish();
			}
		}else{
			ToastUtil.shortToast("发送失败！");
		}
	}
	
	private void sendSercet(String conent, int bgType, String bgurl){
		
		mProgressDialog = AoProgressDialog.createDialog(SendActivity.this);
		mProgressDialog.show();
	 
	    String url = Constant.URL_REQUEST;
		Map<String, String> params = NetworkUtil.initRequestParams();
		params.put("act", "newsecret");
		params.put("strContent", conent);
		params.put("intBgType", String.valueOf(bgType));
		params.put("strBgUrl", bgurl);
		
		JsonObjectRequest request = new JsonObjectRequest(url, params, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				mProgressDialog.dismiss();
				handleSendSercetResult(response);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				mProgressDialog.dismiss();
			}
		});
		
		request.setTag(this);
		
		AOApplication.getRequestQueue().add(request);
 }

}
