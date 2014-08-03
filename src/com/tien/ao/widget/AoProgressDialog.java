package com.tien.ao.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.tien.ao.R;

/**
 * @Description:EwaveProgressDialog
 * @author:Tienfook Chang
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2012-10-30
 */
public class AoProgressDialog extends Dialog {

	private Context context = null;
	private static AoProgressDialog mAoProgressDialog = null;
	private Animation rotateOpenAnimation;
	public AoProgressDialog(Context context) {
		super(context);
		this.context = context;
	}

	public AoProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	public static AoProgressDialog createDialog(Context context) {
		mAoProgressDialog = new AoProgressDialog(context, R.style.AoProgressDialog);
		mAoProgressDialog.setContentView(R.layout.ao_progress_dialog);
		mAoProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;

		return mAoProgressDialog;
	}

	public void onWindowFocusChanged(boolean hasFocus) {

		if (mAoProgressDialog == null) {
			return;
		}
		ImageView imageView = (ImageView) mAoProgressDialog.findViewById(R.id.loadingImageView);
//		AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
//		animationDrawable.start();
		imageView.setAnimation(setAnimation());
		imageView.startAnimation(rotateOpenAnimation);
	}

	/**
	 * 
	 * @Description: 滚动动画
	 * @see:
	 * @since:
	 * @author:Tienfook Chang
	 * @date:2012-10-30
	 */
	private Animation setAnimation() {
		rotateOpenAnimation = new RotateAnimation(0.0f, +360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);// 设置位移动画
		rotateOpenAnimation.setDuration(1000);// 持续1秒
		rotateOpenAnimation.setRepeatCount(Animation.INFINITE);// 无限次

		// 旋转一次不停顿一下，需要以下两行代码
		LinearInterpolator lir = new LinearInterpolator();
		rotateOpenAnimation.setInterpolator(lir);

		return rotateOpenAnimation;
	}
	/**
	 * 
	 * @Description:setTitile
	 * @param strTitle
	 * @return
	 * @see:
	 * @since:
	 * @author:Tienfook Chang
	 * @date:2012-10-30
	 */
	public AoProgressDialog setTitile(String strTitle) {
		return mAoProgressDialog;
	}

	/**
	 * 
	 * @Description:setMessage
	 * @param strMessage
	 * @return
	 * @see:
	 * @since:
	 * @author:Tienfook Chang
	 * @date:2012-10-30
	 */
	public AoProgressDialog setMessage(CharSequence strMessage) {
		TextView tvMsg = (TextView) mAoProgressDialog.findViewById(R.id.loadingMsgTextView);

		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}

		return mAoProgressDialog;
	}
}
