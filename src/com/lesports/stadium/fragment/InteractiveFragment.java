package com.lesports.stadium.fragment;

import java.util.ArrayList;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.LiveDetialActivity;
import com.lesports.stadium.activity.LoginActivity;
import com.lesports.stadium.activity.RecordActivity;
import com.lesports.stadium.activity.ShakeDetailActivity;
import com.lesports.stadium.base.BaseV4Fragment;
import com.lesports.stadium.bean.TemMessage;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.view.CustomDialog;

/**
 * ***************************************************************
 * 
 * @ClassName: InteractiveFragment
 * 
 * @Desc : 互动界面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 司明
 * 
 * @data:2016-2-1 上午11:08:32
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class InteractiveFragment extends BaseV4Fragment implements
		OnClickListener {
	/**
	 * 分贝
	 */
	private ImageButton ib_db;
	/**
	 * 摇一摇
	 */
	private ImageButton ib_shake;
	/**
	 * 未登陆提示按钮
	 */
	private CustomDialog exitDialog;
	// /**
	// * 颜值
	// */
	// private ImageButton ib_face;
	// /**
	// * led
	// */
	// private ImageButton ib_led;
	private RelativeLayout rl_background;
	private ImageView iv_background;
	private ImageView iv_pre;
	// private TextView tv_db;
	// // private TextView tv_face;
	// private TextView tv_shake;
	// private TextView tv_led;
	/**
	 * 活动id
	 */
	private String id;
	private ImageView iv_up;
	private ImageView iv_down;

	
	public static InteractiveFragment netInstance(String id){
		InteractiveFragment inter =new InteractiveFragment();
		Bundle b =new Bundle();
		b.putString("id", id);
		inter.setArguments(b);
		return inter;
	}
	@Override
	public View initView(LayoutInflater inflater) {
		this.id =getArguments().getString("id");
		View view = View.inflate(getActivity(), R.layout.fragment_interactive,
				null);
		ib_db = (ImageButton) view.findViewById(R.id.ib_db);
		ib_shake = (ImageButton) view.findViewById(R.id.ib_shake);
		// ib_face = (ImageButton) view.findViewById(R.id.ib_face);
		// ib_led = (ImageButton) view.findViewById(R.id.ib_led);
		rl_background = (RelativeLayout) view.findViewById(R.id.rl_background);
		// iv_background = (ImageView)view.findViewById(R.id.iv_background);
		iv_pre = (ImageView) view.findViewById(R.id.iv_pre);
		iv_up = (ImageView) view.findViewById(R.id.iv_up);
		iv_down = (ImageView) view.findViewById(R.id.iv_down);

		// iv_background.setImageResource(R.drawable.goods_image);
		// tv_face = (TextView) view.findViewById(R.id.tv_face);
		// tv_led = (TextView) view.findViewById(R.id.tv_led);

		// InputStream is = getResources().openRawResource(R.drawable.image_2);
		//
		// Bitmap mBitmap = BitmapFactory.decodeStream(is);

		return view;
	}

	@Override
	public void initListener() {
		ib_db.setOnClickListener(this);
		ib_shake.setOnClickListener(this);
		iv_up.setOnClickListener(this);
		iv_down.setOnClickListener(this);
		// ib_face.setOnClickListener(this);
		// ib_led.setOnClickListener(this);
		//
		// tv_face.setOnClickListener(this);
		// tv_led.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 分贝
		case R.id.iv_up:
		case R.id.ib_db:
			startRecordActivity();
			break;
		// 摇一摇
		case R.id.iv_down:
		case R.id.ib_shake:
			if (TextUtils.isEmpty(GlobalParams.USER_ID)) {
				createDialog();
				exitDialog.setRemindTitle("温馨提示");
				exitDialog.setCancelTxt("取消");
				exitDialog.setConfirmTxt("立即登录");
				exitDialog.setRemindMessage("登录之后才能摇哦~");
				exitDialog.show();
				return;
			}
			Intent shakeIntent = new Intent();
			shakeIntent.putExtra("id", id);
			shakeIntent.setClass(getActivity(), ShakeDetailActivity.class);
			startActivity(shakeIntent);
			break;
		// 颜值
		// case R.id.tv_face:
		// case R.id.ib_face:
		// tv_face.setTextColor(getResources().getColor(
		// R.color.service_select_txt));
		// Intent fsIntent=new Intent();
		// fsIntent.setClass(getActivity(), FaceScoreActivity.class);
		// startActivity(fsIntent);
		// break;
		// led
		// case R.id.tv_led:
		// case R.id.ib_led:
		// tv_led.setTextColor(getResources().getColor(
		// R.color.service_select_txt));
		// Intent ledIntent=new Intent();
		// ledIntent.setClass(getActivity(), LEDActivity.class);
		// startActivity(ledIntent);
		// break;
		default:
			break;
		}

	}

	/**
	 * 
	 * @param view
	 * @param x
	 *            x轴的平移量
	 * @param y
	 *            y轴的平移量
	 */
	public static void startAnim(View view, float x, float y) {
		ObjectAnimator obj1 = ObjectAnimator.ofFloat(view, "translationX", 0f,
				x);
		ObjectAnimator obj2 = ObjectAnimator.ofFloat(view, "translationY", 0f,
				y);
		AnimatorSet set = new AnimatorSet();
		set.setDuration(800);
		set.setInterpolator(new LinearInterpolator());
		set.playTogether(obj1, obj2);
		set.start();
	}

	/**
	 * 创建未登陆提示框
	 */
	private void createDialog() {
		exitDialog = new CustomDialog(getActivity(), new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
				exitDialog.dismiss();
			}
		}, new OnClickListener() {

			@Override
			public void onClick(View v) {
				exitDialog.dismiss();
			}
		});
	}

	/**
	 * 跳转到我要尖叫界面 获取liveDetialActivity数据
	 */
	private void startRecordActivity() {
		LiveDetialActivity liveActivity = (LiveDetialActivity) getActivity();
		boolean isScreamend = liveActivity.isScreamed();
		ArrayList<TemMessage> msssage = liveActivity.getTemsMessage();
		int index = liveActivity.getAgreeTem();
		Intent recordIntent = new Intent();
		recordIntent.setClass(liveActivity, RecordActivity.class);
		if (!isScreamend)
			recordIntent.putExtra("hasScreamed", "hasScreamed");
		recordIntent.putExtra("tems", msssage);
		recordIntent.putExtra("roomId", liveActivity.getId());
		recordIntent.putExtra("id", liveActivity.getId());
		recordIntent.putExtra("agreeTemIndex", index);
		startActivity(recordIntent);
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		// int w = View.MeasureSpec.makeMeasureSpec(0,
		// View.MeasureSpec.UNSPECIFIED);
		// int h = View.MeasureSpec.makeMeasureSpec(0,
		// View.MeasureSpec.UNSPECIFIED);
		// iv_pre.measure(w, h);
		// int height = iv_pre.getMeasuredHeight();
		// int width = iv_pre.getMeasuredWidth();

	}

	@Override
	public void onResume() {
		super.onResume();
		// int width = DensityUtil.dip2px(getActivity(), 80);
		// startAnim(ib_db,-width,-width);
		// startAnim(ib_shake,width,-width);
		// startAnim(ib_face,-width,width);
		// startAnim(ib_led,width,width);
		// Bitmap image =
		// ((BitmapDrawable)iv_background.getDrawable()).getBitmap();
		// Fglass.blur(image, iv_pre, 8, 2);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}

}
