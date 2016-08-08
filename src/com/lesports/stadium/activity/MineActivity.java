package com.lesports.stadium.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lesports.stadium.R;
import com.lesports.stadium.bean.MessageNumerBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.fragment.MyFragment;
import com.lesports.stadium.gaode.IndoorGaodeGuide;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.JsonUtil;
import com.lesports.stadium.utils.SharedPreferencesUtils;
import com.lesports.stadium.view.CircleImageView;
import com.lesports.stadium.view.CustomDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * 
 * @ClassName: LeftFragment
 * 
 * @Description: 我的界面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @author wangxinnian
 * 
 * @date 2016-7-10 下午3:45:58
 * 
 * 
 */
public class MineActivity extends Activity implements OnClickListener {
	/**
	 * 左侧标题控件
	 */
	private View menuLeft;
	/**
	 * 圆形头像
	 */
	private CircleImageView iv_self_photo;
	/**
	 * 用户姓名
	 */
	private TextView name;
	/**
	 * 订单
	 */
	private TextView tOrder;
	/**
	 * 订单
	 */
	private ImageView order;
	/**
	 * 乐豆
	 */
	private TextView tLedou;
	/**
	 * 乐豆
	 */
	private ImageView ledou;
	/**
	 * 消息
	 */
	private TextView tMessage;
	/**
	 * 消息
	 */
	private ImageView message;
	/**
	 * 收货地址
	 */
	private TextView myAddress;
	/**
	 * 收货优惠券
	 */
	private TextView myCoupon;
	/**
	 * 我的奖品
	 */
	private TextView myPrice;
	/**
	 * 我的预约
	 */
	private TextView myApponitMent;
	/**
	 * 未登陆提示按钮
	 */
	private CustomDialog exitDialog;
	/**
	 * 设置
	 */
	private ImageView iv_scan;
	/**
	 * 获取积分
	 */
	private final int SUCCESS_DATE = 100;
	/**
	 * 获取未读消息数
	 */
	private final int SUCCESS_MESSAGE = 200;
	/**
	 * 消息数据封装
	 */
	private MessageNumerBean messageNumerBean;

	private ImageLoader imageLoader = ImageLoader.getInstance();

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS_DATE:
				// 获取成功
				analyzeDate((String) msg.obj);
				break;
			case SUCCESS_MESSAGE:
				// 消息数量获取成功
				analyseUnredMessage((String) msg.obj);
				break;
			}
		}
	};
	/**
	 * 乐豆布局
	 */
	private LinearLayout llLedou;
	private View line;
	private ImageView unRedMessae;
	public static MineActivity instance;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_menu);
		instance = this;
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		findViews();
		initListener();
		createDialog();
	}

	private void initListener() {
		menuLeft.setOnClickListener(this);
		iv_self_photo.setOnClickListener(this);
		tOrder.setOnClickListener(this);
		order.setOnClickListener(this);
		ledou.setOnClickListener(this);
		tLedou.setOnClickListener(this);
		message.setOnClickListener(this);
		tMessage.setOnClickListener(this);
		myApponitMent.setOnClickListener(this);
		myPrice.setOnClickListener(this);
		myCoupon.setOnClickListener(this);
		myAddress.setOnClickListener(this);
		iv_scan.setOnClickListener(this);
	}

	public void findViews() {
		menuLeft = findViewById(R.id.iv_left);
		name = (TextView) findViewById(R.id.name);
		iv_self_photo = (CircleImageView) findViewById(R.id.iv_self_photo);
		message = (ImageView) findViewById(R.id.iv_message);
		tMessage = (TextView) findViewById(R.id.tv_message);
		ledou = (ImageView) findViewById(R.id.iv_ledou);
		tLedou = (TextView) findViewById(R.id.tv_ledou);
		order = (ImageView) findViewById(R.id.iv_order);
		tOrder = (TextView) findViewById(R.id.tv_order);
		myAddress = (TextView) findViewById(R.id.tv_my_address);
		myCoupon = (TextView) findViewById(R.id.tv_my_coupon);
		myPrice = (TextView) findViewById(R.id.tv_my_price);
		myApponitMent = (TextView) findViewById(R.id.tv_my_appointment);
		iv_scan = (ImageView) findViewById(R.id.iv_scan);
		llLedou = (LinearLayout) findViewById(R.id.ll_ledou);
		line = findViewById(R.id.line_ld);
		unRedMessae = (ImageView) findViewById(R.id.un_redmessage);
	}

	@Override
	public void onDestroy() {
		instance = null;
		if (handler != null) {
			handler.removeCallbacksAndMessages(null);
			handler = null;
		}
		messageNumerBean = null;
		iv_self_photo = null;
		System.gc();
		super.onDestroy();
	}

	@Override
	public void onResume() {
		showMessage();
		super.onResume();
	}

	/**
	 * 展示界面上的数据
	 */
	private void showMessage() {
		if (!TextUtils.isEmpty(GlobalParams.USER_HEADER)) {
			if (GlobalParams.USER_HEADER.startsWith("http:")) {
				imageLoader
						.displayImage(
								GlobalParams.USER_HEADER,
								iv_self_photo,
								MyFragment
										.setDefaultImageOptions(R.drawable.default_header));
			} else {
				imageLoader
						.displayImage(
								ConstantValue.BASE_IMAGE_URL
										+ GlobalParams.USER_HEADER
										+ ConstantValue.IMAGE_END,
								iv_self_photo,
								MyFragment
										.setDefaultImageOptions(R.drawable.default_header));
			}
		} else {
			iv_self_photo.setImageResource(R.drawable.default_header);
		}
		if (TextUtils.isEmpty(GlobalParams.USER_ID)) {
			name.setText("点击头像登录");
			llLedou.setVisibility(View.GONE);
			line.setVisibility(View.GONE);
			unRedMessae.setVisibility(View.GONE);
		} else {
			getUserJiFen();
			getUserMessageNumber();
			name.setText(GlobalParams.USER_NAME);
			llLedou.setVisibility(View.VISIBLE);
			line.setVisibility(View.VISIBLE);
		}
		tLedou.setText("乐豆:" + GlobalParams.USER_INTEGRAL + "");

	}

	@Override
	public void onClick(View v) {
		if (TextUtils.isEmpty(GlobalParams.USER_ID)
				&& v.getId() != R.id.iv_self_photo && v.getId() != R.id.iv_left
				&& v.getId() != R.id.iv_scan) {
			exitDialog.show();
			return;
		}
		Intent intent;
		switch (v.getId()) {

		case R.id.iv_scan:
			// 设置
			intent = new Intent(MineActivity.this, MySettingActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_my_address:
			// 收货地址
			intent = new Intent(MineActivity.this,
					ManageShippingAddressActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_my_coupon:
			// 我的优惠券
			intent = new Intent(MineActivity.this, MyCouponActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_my_price:
			// 我的奖品
			intent = new Intent(MineActivity.this, MyPrizeActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_my_appointment:
			// 我的预约
			intent = new Intent(MineActivity.this, MySubscribeActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_message:
		case R.id.iv_message:
			// 消息
			intent = new Intent(MineActivity.this, MessageNotifyActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_ledou:
		case R.id.iv_ledou:
			// 乐豆
			intent = new Intent(MineActivity.this, MyIntegralActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_order:
		case R.id.iv_order:
			// 订单
			intent = new Intent(MineActivity.this, AllOrderActivity.class);
			startActivity(intent);
			break;
		case R.id.iv_left:
			finish();
			break;
		case R.id.iv_self_photo:
			// 跳转个人资料界面
			if (TextUtils.isEmpty(GlobalParams.USER_ID)) {
				intent = new Intent(MineActivity.this, LoginActivity.class);
			} else {
				intent = new Intent(MineActivity.this,
						PersionDataActivity.class);
			}
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	/**
	 * 创建未登陆提示框
	 */
	private void createDialog() {
		exitDialog = new CustomDialog(MineActivity.this, new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(MineActivity.this,
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
		exitDialog.setRemindMessage("您还没有登录哦");
	}

	/**
	 * 获取用户积分
	 */
	private void getUserJiFen() {
		GetDataFromInternet.getInStance().interViewNet(ConstantValue.GET_JIFEN,
				null, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if (data != null) {
							Object obj = data.getObject();
							if (obj != null && handler != null) {
								Message msg = new Message();
								msg.what = SUCCESS_DATE;
								msg.obj = obj;
								handler.sendMessage(msg);
								return;
							}

						}
					}
				}, false, false);
	}

	private void analyzeDate(String obj) {
		try {
			if (TextUtils.isEmpty(obj)) {
				GlobalParams.USER_INTEGRAL = 0;
				tLedou.setText("乐豆:" + GlobalParams.USER_INTEGRAL + "");
				SharedPreferencesUtils.saveData(MineActivity.this,
						"ls_user_message", "ingegral",
						GlobalParams.USER_INTEGRAL);
				return;
			}
			JSONObject jObj = new JSONObject(obj);
			if (jObj.has("integralBalance")) {
				GlobalParams.USER_INTEGRAL = jObj.getInt("integralBalance");
				tLedou.setText("乐豆:" + GlobalParams.USER_INTEGRAL + "");
				SharedPreferencesUtils.saveData(MineActivity.this,
						"ls_user_message", "ingegral",
						GlobalParams.USER_INTEGRAL);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	};

	@Override
	public void finish() {
		super.finish();
		// 关闭窗体动画显示
		this.overridePendingTransition(0, R.anim.bottom_out);
	}

	/**
	 * 获取用户消息数量
	 */
	private void getUserMessageNumber() {
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.GET_MESSAGE_NUMBER, null, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if (data != null) {
							Object obj = data.getObject();
							if (obj != null && handler != null) {
								Message msg = new Message();
								msg.what = SUCCESS_MESSAGE;
								msg.obj = obj;
								handler.sendMessage(msg);
								return;
							}

						}
					}
				}, false, false);
	}

	/**
	 * 解析未读消息数量
	 * 
	 * @param data
	 */
	private void analyseUnredMessage(String data) {
		messageNumerBean = JsonUtil.parseJsonToBean(data,
				MessageNumerBean.class);
		if (null != messageNumerBean && null != messageNumerBean.getCount()) {
			if (!"0".equals(messageNumerBean.getCount())) {
				unRedMessae.setVisibility(View.VISIBLE);
			} else {
				unRedMessae.setVisibility(View.GONE);
			}
		} else {
			unRedMessae.setVisibility(View.GONE);
		}
	}

}
