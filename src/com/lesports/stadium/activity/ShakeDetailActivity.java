package com.lesports.stadium.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.lesports.stadium.R;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.ShakeAdvertBean;
import com.lesports.stadium.bean.ShakeNumberBean;
import com.lesports.stadium.bean.ShakePriceBean;
import com.lesports.stadium.bean.ShakeRuleBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.listener.ShakeListener;
import com.lesports.stadium.listener.ShakeListener.OnShakeListener;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.DensityUtil;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.JsonUtil;
import com.lesports.stadium.view.BubbleImageView;
import com.lesports.stadium.view.CustomDialog;

/**
 * 
 * ***************************************************************
 * 
 * @Desc : 摇一摇界面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 司明
 * 
 * @data:
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class ShakeDetailActivity extends Activity implements OnClickListener {

	ShakeListener mShakeListener = null;
	Vibrator mVibrator;
	private RelativeLayout mImgUp;
	private RelativeLayout mImgDn;
	private ImageButton ib_back;
	private ImageView iv_background;
	private List<ShakeAdvertBean> advertList = new ArrayList<ShakeAdvertBean>();
	private ShakeRuleBean shakeRuleBean = new ShakeRuleBean();
	private WebView wv_webview;
	private ShakePriceBean shakePriceBean;
	private ShakeNumberBean shakeNumberBean;
	/**
	 * 活动id
	 */
	private String id;

	/**
	 * 未登陆提示按钮
	 */
	private CustomDialog exitDialog;

	private int number = 3;
	
	/**
	 * handle需要用的标记
	 */
	private final int GET_GUANGGAO_LIST=100;
	private final int GET_SHAKE_GUIDE=200;
	private final int GET_SHAKE_JIANGPIN=300;
	private final int GET_JIFEN_GUIZE=400;

	private Handler integralHandler = new Handler() {

		@SuppressLint("UseValueOf") @SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_GUANGGAO_LIST:
				String backdata = (String) msg.obj;
				if (!TextUtils.isEmpty(backdata)){
					useWayHandleData(backdata);
				}
				break;
			case GET_SHAKE_GUIDE:
				String backdataRule = (String) msg.obj;
				if (!TextUtils.isEmpty(backdataRule)){
					useWayHandleShakeGuide(backdataRule);
				}
				break;
			case GET_SHAKE_JIANGPIN:
				String backdataPrice = (String) msg.obj;
				if (!TextUtils.isEmpty(backdataPrice)){
					useWayHandleShakeJiangpin(backdataPrice);
				}
				break;
			case GET_JIFEN_GUIZE:
				String backdataNumber = (String) msg.obj;
				if (!TextUtils.isEmpty(backdataNumber)) {
					useWayHandleJifenGuize(backdataNumber);
				}else{
					Toast.makeText(ShakeDetailActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
				}
				break;
			case 500:
				if (null != mShakeListener)
					mShakeListener.start();
				Toast.makeText(getApplicationContext(), "网络异常", 0).show();
			default:

				break;
			}
		}
	};

	private PopupWindow pop;
	private RelativeLayout ll_popup;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shake_detail_activity);

		Intent intent = getIntent();
		id = intent.getStringExtra("id");

		tv_shake_number = (TextView) findViewById(R.id.tv_shake_number);

		mVibrator = (Vibrator) getApplication().getSystemService(
				VIBRATOR_SERVICE);

		mImgUp = (RelativeLayout) findViewById(R.id.shakeImgUp);
		mImgDn = (RelativeLayout) findViewById(R.id.shakeImgDown);
		Canvas canvas = new Canvas();

		iv_background = (ImageView) findViewById(R.id.iv_background);
		popupView = View.inflate(ShakeDetailActivity.this, R.layout.item_shake,
				null);

		pop = new PopupWindow(ShakeDetailActivity.this);
		ll_popup = (RelativeLayout) popupView.findViewById(R.id.ll_popup);
		wv_webview = (WebView) popupView.findViewById(R.id.wv_webview);
		parent = (RelativeLayout) popupView.findViewById(R.id.parent);

		wv_webview.getSettings().setJavaScriptEnabled(true);
		wv_webview.getSettings().setDefaultTextEncodingName("utf-8");
		// wv_webview.setBackgroundColor(color.white);
		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(false);
		pop.setOutsideTouchable(true);
		pop.setContentView(popupView);
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
			}
		});

		tv_rule = (TextView) findViewById(R.id.tv_rule);
		tv_rule.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ll_popup.startAnimation(AnimationUtils.loadAnimation(
						ShakeDetailActivity.this, R.anim.activity_translate_in));
				pop.showAtLocation(mImgDn, Gravity.BOTTOM, 0, 0);
				// wv_webview.loadDataWithBaseURL(null,shakeRuleBean.getLotteryRule(),
				// "text/html", "utf-8", null);
			}
		});
		ib_back = (ImageButton) findViewById(R.id.ib_back);
		ib_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// requestIntegral();
		requestNumber();

		mShakeListener = new ShakeListener(ShakeDetailActivity.this);
		mShakeListener.setOnShakeListener(new OnShakeListener() {
			public void onShake() {
				// shakeBg.setVisibility(View.VISIBLE);
				MediaPlayer mPlayer = null;
				mPlayer = MediaPlayer.create(ShakeDetailActivity.this,
						R.raw.shake);
				mPlayer.setLooping(false);
				try {
					mPlayer.prepare();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				mPlayer.start();
				startAnim();
				mShakeListener.stop();

				startVibrato();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						// ShakePriceBean

						requestPrice();
						mVibrator.cancel();
						// mShakeListener.start();
					}
				}, 1000);
			}
		});

		// createDialog();

	}

	private void createDialogView(String type) {
		switch (type) {
		case "2":
			// 中奖
			dialogView = LayoutInflater.from(this).inflate(
					R.layout.pop_win_prize, null);
			dialogView.findViewById(R.id.delte).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (dialog != null && dialog.isShowing())
								dialog.dismiss();
							mShakeListener.start();
						}
					});
			dialogView.findViewById(R.id.takepart).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (dialog != null && dialog.isShowing())
								dialog.dismiss();
							Intent intent = new Intent();
							intent.setClass(ShakeDetailActivity.this,
									MyPrizeActivity.class);
							startActivity(intent);
						}
					});
			BubbleImageView iv_topbackground = (BubbleImageView) dialogView
					.findViewById(R.id.bg);
			// Log.e("222222222222222",
			// ConstantValue.DownIMage+shakePriceBean.getImage()+ConstantValue.Type);
			LApplication.loader.DisplayImage(ConstantValue.BASE_IMAGE_URL
					+ shakePriceBean.getImage() + ConstantValue.IMAGE_END,
					iv_topbackground);
			TextView rule2 = (TextView) dialogView.findViewById(R.id.rule2);
			rule2.setText(shakePriceBean.getName());
			// TextView rule3=(TextView) dialogView.findViewById(R.id.rule3);
			// if(!Utils.isNullOrEmpty(GlobalParams.USER_PHONE)){
			// rule3.setText("已放入手机号"+GlobalParams.USER_PHONE+"账户中");
			// }else{
			// rule3.setVisibility(View.GONE);
			// }
			break;
		case "0":
			// 未中奖界面
			dialogView = LayoutInflater.from(this).inflate(
					R.layout.pop_nowin_prize, null);
			dialogView.findViewById(R.id.delte).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (dialog != null && dialog.isShowing())
								dialog.dismiss();
							mShakeListener.start();
						}
					});
			dialogView.findViewById(R.id.takepart).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							mShakeListener.start();
						}
					});
			BubbleImageView iv_topbackground2 = (BubbleImageView) dialogView
					.findViewById(R.id.bg);
			// Log.e("00000000000000000",
			// ConstantValue.DownIMage+shakePriceBean.getImageURL()+ConstantValue.Type);
			LApplication.loader.DisplayImage(ConstantValue.BASE_IMAGE_URL
					+ shakePriceBean.getImageURL() + ConstantValue.IMAGE_END,
					iv_topbackground2);
			TextView rule22 = (TextView) dialogView.findViewById(R.id.rule2);
			rule22.setText(shakePriceBean.getNoPrizeContent());
			break;
		case "1":
			// 超过次数
			dialogView = LayoutInflater.from(this).inflate(
					R.layout.pop_numberout, null);
			dialogView.findViewById(R.id.delte).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (dialog != null && dialog.isShowing())
								dialog.dismiss();
							mShakeListener.start();
						}
					});
			dialogView.findViewById(R.id.takepart).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							mShakeListener.start();
						}
					});
			BubbleImageView iv_topbackground3 = (BubbleImageView) dialogView
					.findViewById(R.id.bg);
			// // Log.e("00000000000000000",
			// ConstantValue.DownIMage+shakePriceBean.getImageURL()+ConstantValue.Type);
			LApplication.loader.DisplayImage(ConstantValue.BASE_IMAGE_URL
					+ shakePriceBean.getImageURL() + ConstantValue.IMAGE_END,
					iv_topbackground3);
			// imageLoader.displayImage(ConstantValue.DownIMage+shakePriceBean.getImageURL()+ConstantValue.Type,
			// iv_topbackground3, list_options);
			break;

		default:
			break;
		}
	}

	/**
	 * 摇一摇获取奖品接口
	 */
	private void requestPrice() {

		if (TextUtils.isEmpty(GlobalParams.USER_ID)) {
			exitDialog.show();
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityId", id); // 活动id
		//
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.SHAKE_PRICE, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if(integralHandler==null)
							return;
						if (data == null) {
							// 说明网络获取无数据
							integralHandler.sendEmptyMessage(500);
						} else {
							Log.e("SSSSSSSSSSSgetNetResultCode",
									data.getNetResultCode() + "");
							Object obj = data.getObject();
							if (obj == null) {
							} else {
								String backdataPrice = obj.toString();
								if (backdataPrice == null
										|| backdataPrice.equals("")) {
									integralHandler.sendEmptyMessage(500);
								} else {
									Log.e("price", backdataPrice);
									Message msg = new Message();
									msg.what = GET_SHAKE_JIANGPIN;
									msg.obj = backdataPrice;
									integralHandler.sendMessage(msg);
								}
							}
						}
					}
				}, false, false);

	}

	private void requestAdvert() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("pageNo", "0"); // 起始页编号
		params.put("rows", "20"); // 返回数据条数
		params.put("status", "1"); // 是否开启广告
		params.put("advertisementType", "1"); // 广告类型 1为摇一摇
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.GUANGGAO_LIST, null, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if(integralHandler==null)
							return;
						if (data == null) {
							// 说明网络获取无数据
						} else {
							Object obj = data.getObject();
							if (obj == null) {
							} else {
								String backdataAdvert = obj.toString();
								Log.e("backdataAdvert backdataAdvert ",
										backdataAdvert);
								if (null == backdataAdvert
										|| backdataAdvert.equals("")) {
									Log.e("stop", "stop");
								} else {
									Message msg = new Message();
									msg.what = 100;
									msg.obj = backdataAdvert;
									integralHandler.sendMessage(msg);

								}
							}
						}
					}
				}, false, false);

	}

	private void requestIntegral() {
		Map<String, String> params = new HashMap<String, String>();
		// Log.e("ididididididididiidididididididididid", id);
		params.put("activityId", id); // id
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.SHAKE_RULE, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if(integralHandler==null)
							return;
						if (data == null) {
							// 说明网络获取无数据
						} else {
							Object obj = data.getObject();
							if (obj == null) {
							} else {
								String backdata = obj.toString();
								Log.e("rulerule rule rule ", backdata);

								if (backdata == null || backdata.equals("")
										|| backdata.equals("null")
										|| backdata.equals("resultMsg")) {
								} else {
									Message msg = new Message();
									msg.what = GET_SHAKE_GUIDE;
									msg.obj = backdata;
									integralHandler.sendMessage(msg);
								}
							}
						}
					}
				}, false, false);

	}

	/**
	 * 获取积分规则
	 */
	private void requestNumber() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityId", id);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.JIFENG_GUIZE, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if(integralHandler==null)
							return;
						if (data == null) {
							// 说明网络获取无数据
						} else {
							Object obj = data.getObject();
							if (obj == null) {
							} else {
								String backdata = obj.toString();
								Log.i("wxn", "摇奖：" + backdata);
								if (backdata == null && backdata.equals("")) {
								} else {
									Message msg = new Message();
									msg.what = GET_JIFEN_GUIZE;
									msg.obj = backdata;
									integralHandler.sendMessage(msg);
								}
							}
						}
					}
				}, false, false);

	}

	public void startAnim() {
		AnimationSet animup = new AnimationSet(true);
		TranslateAnimation mytranslateanimup0 = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
				-0.2f);
		mytranslateanimup0.setDuration(500);
		TranslateAnimation mytranslateanimup1 = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
				+0.2f);
		mytranslateanimup1.setDuration(500);
		mytranslateanimup1.setStartOffset(500);
		animup.addAnimation(mytranslateanimup0);
		animup.addAnimation(mytranslateanimup1);
		mImgUp.startAnimation(animup);

		AnimationSet animdn = new AnimationSet(true);
		TranslateAnimation mytranslateanimdn0 = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
				+0.2f);
		mytranslateanimdn0.setDuration(500);
		TranslateAnimation mytranslateanimdn1 = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
				-0.2f);
		mytranslateanimdn1.setDuration(500);
		mytranslateanimdn1.setStartOffset(500);
		animdn.addAnimation(mytranslateanimdn0);
		animdn.addAnimation(mytranslateanimdn1);
		mImgDn.startAnimation(animdn);
	}

	public void startVibrato() {

		mVibrator.vibrate(new long[] { 500, 200, 500, 200 }, -1);

	}

	public void shake_activity_back(View v) {
		this.finish();
	}

	public void linshi(View v) {
		startAnim();
	}

	private Dialog dialog;
	private View dialogView;
	// 剩余摇晃的次数
	private TextView tv_shake_number;

	private TextView tv_rule;
	private RelativeLayout parent;
	private View popupView;

	private void setDialog() {

		dialog = new Dialog(this, R.style.Theme_Light_Dialo);
		dialog.show();
		// 获得dialog的window窗口
		Window window = dialog.getWindow();
		// 设置dialog在屏幕底部
		window.setGravity(Gravity.CENTER);
		// 设置dialog弹出时的动画效果，从屏幕底部向上弹出
		// window.setWindowAnimations(R.style.dialogStyle);
		int lor = DensityUtil.dip2px(getApplicationContext(), 20);
		int top = DensityUtil.dip2px(getApplicationContext(), 15);
		int bot = DensityUtil.dip2px(getApplicationContext(), 5);
		// 获得window窗口的属性
		android.view.WindowManager.LayoutParams lp = window.getAttributes();
		// 设置窗口宽度为充满全屏
		lp.width = dp2px(300);
		// 设置窗口高度为包裹内容
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		// 将设置好的属性set回去

		window.setAttributes(lp);
		window.getDecorView().setPadding(0, top, 0, bot);
		// 将自定义布局加载到dialog上
		dialog.setContentView(dialogView);
		dialog.setCanceledOnTouchOutside(false);
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				ShakeDetailActivity.this.getResources().getDisplayMetrics());
	}

	@Override
	protected void onDestroy() {
		if(integralHandler!=null){
			integralHandler.removeCallbacksAndMessages(null);
			integralHandler = null;
		}
		if (mShakeListener != null) {
			mShakeListener.stop();
			mShakeListener = null;
		}
		if (advertList != null) {
			advertList.clear();
			advertList = null;
		}
		if (shakeRuleBean != null)
			shakeRuleBean = null;
		mVibrator = null;
		wv_webview = null;
		exitDialog = null;
		System.gc();
		super.onDestroy();
	}

	/**
	 * 创建未登陆提示框
	 */
	private void createDialog() {
		exitDialog = new CustomDialog(ShakeDetailActivity.this,
				new OnClickListener() {
					@Override
					public void onClick(View v) {

						Intent intent = new Intent(ShakeDetailActivity.this,
								LoginActivity.class);
						startActivity(intent);
						exitDialog.dismiss();
					}
				}, new OnClickListener() {

					@Override
					public void onClick(View v) {
						exitDialog.dismiss();
					}
				});
		exitDialog.setRemindTitle("温馨提示");
		exitDialog.setCancelTxt("取消");
		exitDialog.setConfirmTxt("立即登录");
		exitDialog.setRemindMessage("登录之后才可以摇一摇");
	}

	@Override
	protected void onPause() {
		super.onPause();
		mShakeListener.stop();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (dialog != null && dialog.isShowing()) {

		} else {
			mShakeListener.start();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.delte:
			if (dialog != null && dialog.isShowing())
				dialog.dismiss();
			break;
		case R.id.takepart:
			if (dialog != null && dialog.isShowing())
				dialog.dismiss();
			break;
		}
	}
	/**
	 * 处理广告列表数据
	 * @param backdata
	 */
	@SuppressWarnings("unchecked")
	private void useWayHandleData(String backdata) {
		// TODO Auto-generated method stub
		advertList = (ArrayList<ShakeAdvertBean>) JsonUtil
				.parseJsonToList(backdata,
						new TypeToken<List<ShakeAdvertBean>>() {
						}.getType());

		// 服务器无图片
		if (null != advertList && advertList.size() > 0
				&& !"".equals(advertList.get(0).getUrl())) {
			LApplication.loader.DisplayImage(
					ConstantValue.BASE_IMAGE_URL
							+ advertList.get(0).getUrl()
							+ ConstantValue.IMAGE_END, iv_background);
			Log.e("shakeImageUrl", ConstantValue.BASE_IMAGE_URL
					+ advertList.get(0).getUrl()
					+ ConstantValue.IMAGE_END);
			// Log.e("shakeImageUrl",advertList.get(0).getUrl()+"");
		}
		// 获取摇一摇规则
		requestIntegral();
	};
	/**
	 * 处理获取下来的摇一摇规则
	 * @param backdataRule
	 */
	private void useWayHandleShakeGuide(String backdataRule) {
		// TODO Auto-generated method stub
		shakeRuleBean = JsonUtil.parseJsonToBean(backdataRule,
				ShakeRuleBean.class);
		// if(null!=shakeRuleBean.getLotteryRule()){
		if (shakeRuleBean == null)
			return;
		String content = shakeRuleBean.getLotteryRule();
		while (content.contains("本活动与苹果公司无关"))
			content = content.substring(0, content.lastIndexOf("<p"));
		wv_webview.loadDataWithBaseURL(null,
				"<html><body background=\"#000000\" link=\"white\">"
						+ content + "</body</html>", "text/html",
				"utf-8", null);

	}
	/**
	 * 处理获取到的摇一摇奖品
	 * @param backdataPrice
	 */
	private void useWayHandleShakeJiangpin(String backdataPrice) {
		// TODO Auto-generated method stub
		shakePriceBean = JsonUtil.parseJsonToBean(backdataPrice,
				ShakePriceBean.class);
		if (number >= 1) {
			--number;
			tv_shake_number.setText("今日还可摇奖 " + number + " 次");
		}
		if (shakePriceBean != null)
			createDialogView(shakePriceBean.getWinningType());
		MediaPlayer mPlayer = null;
		mPlayer = MediaPlayer.create(ShakeDetailActivity.this,
				R.raw.shake_result);
		mPlayer.setLooping(false);
		try {
			mPlayer.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mPlayer.start();
		setDialog();
		dialog.show();
	}
	/**
	 * 处理获取下来的积分规则
	 * @param backdataNumber
	 */
	private void useWayHandleJifenGuize(String backdataNumber) {
		// TODO Auto-generated method stub
		if ("该用户没有抽过奖".equals(backdataNumber)) {
			tv_shake_number.setText("今日还可摇奖 " + 3 + " 次");
			number = 3;
		} else {
			shakeNumberBean = JsonUtil.parseJsonToBean(backdataNumber,
					ShakeNumberBean.class);
			// Log.e("SSSSSSSSSSSSS", shakeNumberBean.getCount());
			if (shakeNumberBean == null)
				return;
			int i = new Integer(shakeNumberBean.getCount());
			number = (3 - i);
			tv_shake_number.setText("今日还可摇奖 " + number + " 次");
		}

		requestAdvert();
	}

}