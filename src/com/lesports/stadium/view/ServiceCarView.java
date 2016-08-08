/**
 * 
 */
package com.lesports.stadium.view;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.BoardingActivity;
import com.lesports.stadium.activity.BookthecarActivity;
import com.lesports.stadium.activity.ChangePassengersActivity;
import com.lesports.stadium.activity.GetOffActivity;
import com.lesports.stadium.activity.LoginActivity;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.Address;
import com.lesports.stadium.bean.PhoneInfo;
import com.lesports.stadium.bean.YongCheBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.DensityUtil;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ***************************************************************
 * 
 * @ClassName: ServiceFoodView
 * 
 * @Desc : 用车服务
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 盛之刚
 * 
 * @data:2016-2-14 下午5:12:05
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
@SuppressLint("HandlerLeak") @ResID(id = R.layout.layout_carview)
public class ServiceCarView extends BaseView implements OnClickListener {
	/**
	 * 上下文
	 */
	@SuppressWarnings("unused")
	private Context myContent;

	/**
	 * 换乘车人,下车
	 */
	private RelativeLayout rlyt_get_off;
	private LinearLayout rlyt_change_passenger;
	/**
	 * 下车地址
	 */
	private TextView mGetoffTv;
	/**
	 * 计费提示
	 */
	private TextView mGetoffExplainTv;

	/**
	 * 乘车人名称
	 */
	private TextView tv_passenger_name;

	/**
	 * 乘车人手机号
	 */
	private TextView tv_passenger_phone;

	/**
	 * 上车位置
	 */
	private LinearLayout llyt_start_location;
	/**
	 * 上车数据存储对象
	 */
	private Address rideAddress;
	/**
	 * 上车位置
	 */
	private TextView mRideAddressTv;
	/**
	 * 下车数据存储
	 */
	private Address getoffAddress;
	/**
	 * 选择车型背景
	 */
	private ImageView bgSelectCar;

	/**
	 * 车图片
	 */
	private ImageView icCar;

	/**
	 * 价格
	 */
	private TextView tv_price;
	/**
	 * 打折信息
	 */
	private TextView discountTv;
	/**
	 * 打折标号
	 */
	private ForegroundColorSpan blueSpan;
	/**
	 * 时间选择器
	 */
	private CarTimeSelector timeSelector;

	/**
	 * 选车说明和详细说明
	 */
	// private TextView tvExplain, tvSeclectExplain;

	/**
	 * 是否系统选车
	 */
	private CheckBox chkExplain;

	/**
	 * 确认订单
	 */
	private RelativeLayout rlyt_order;

	/**
	 * 车型开始位置
	 */
	private Point startPoint;

	/**
	 * 车型选择结束为止
	 */
	private Point endPoint;

	/**
	 * 经济车型位置
	 */
	private Point firstPoint;
	private YongCheBean moneyBean;
	private TextView carDiscriptionTv1;
	/**
	 * Tesla型位置
	 */
	private Point secondPoint;
	private YongCheBean teslaBean;
	private TextView carDiscriptionTv2;
	/**
	 * 舒适车型位置
	 */
	private Point thirdPoint;
	private YongCheBean comfortableBean;
	private TextView carDiscriptionTv3;
	/**
	 * 商务车型位置
	 */
	private Point fourtPoint;
	private YongCheBean businessBean;
	private TextView carDiscriptionTv4;
	private int currentCar = 0;
	/**
	 * 乘车人
	 */
	public PhoneInfo info;
	/**
	 * 车型
	 */
	private YongCheBean thisCarBean = new YongCheBean();
	/**
	 * 默认选车时间
	 */
	private String carTimeStr = "2016-05-15 20:00:00";
	/**
	 * 用车类型，本APP使用时租
	 */
	private final static String userCarType = "1";// 用车类型为1：时租 ，7接送机
	/**
	 * 易到用车token
	 */
	// private final static String
	// token="95U8eQ7Uuwr5APXpzhOQfakdf4ku9rst2aQp0l4m";
//	private final static String token = "psTBKnSYPotjmpj0rRillUPhDdJ2S7CQEDRlyiKJ";
	/**
	 * 选择时间,乘车时间
	 */
	private TextView mRideTimeTv;
	/**
	 * 提示登陆信息
	 */
	private CustomDialog loginDIalog;
	/**
	 * 补全信息提示
	 */
	private CustomDialog msgDialog;
	/**
	 * 选择时间初值
	 */
	private String nowTimeStr;
	/**
	 * 判断预约单还是随叫随到，选择时间就是预约单
	 */
	private boolean isNowTime = false;
	private boolean isGoRide = true;
	/**
	 * 获取到用车估价
	 */
	private final int GETCARPRICE=0;
	/**
	 * 创建订单
	 */
	private final int CRATEORDER=1;
	/**
	 *  向我们服务器发送订单号完成
	 */
	private final int SENDORDER=2;
	/**
	 * 消息处理
	 */
	private Handler priceHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			// 获取到用车估价
			case GETCARPRICE:
				String backdata = (String) msg.obj;
				try {
					JSONObject priceObj = new JSONObject(backdata);
					if (priceObj.toString().contains("result")) {
						JSONObject resultObj = priceObj.getJSONObject("result");
							tv_price.setText(resultObj.getString("total_fee"));
					}else if(priceObj.getString("code").equals("400")){
						String errorStr=priceObj.getString("msg");
						if(!Utils.isNullOrEmpty(errorStr)){
						//	Toast.makeText(context, ""+errorStr, Toast.LENGTH_SHORT).show();
							tv_price.setText("30");

						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case CRATEORDER:
				// 创建订单
				String orderBackdata = (String) msg.obj;
				try {
					JSONObject orderObj = new JSONObject(orderBackdata);
					if (orderObj.toString().contains("result")) {
						JSONObject orderIdObj = orderObj
								.getJSONObject("result");
						String id = orderIdObj.getString("order_id");
						LApplication.yidaoOrderId = id;
						Log.i("USECAR", "订单请求成功==orderId:"
								+ LApplication.yidaoOrderId);
						requestCreatOrder(thisCarBean,
								LApplication.yidaoOrderId);
						/*
						 * Intent bookCarIntent = new Intent(context,
						 * BookthecarActivity.class);
						 * context.startActivity(bookCarIntent);
						 */
						MobclickAgent.onEvent(context,"OrderCarSuccessfully");
					} else {
						isGoRide=true;
						Toast.makeText(context, orderObj.getString("msg"),
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case SENDORDER:
				// 向我们服务器发送订单号完成
				isGoRide = true;
				Intent bookCarIntent = new Intent(context,
						BookthecarActivity.class);
				bookCarIntent.putExtra("orderid", LApplication.yidaoOrderId);
				bookCarIntent.putExtra("tag", "service");
				context.startActivity(bookCarIntent);
				break;
			default:
				break;
			}
		};
	};

	// private Activity mContext;
	/**
	 * 构造
	 * 
	 * @param context
	 */
	public ServiceCarView(Context context) {
		super(context);
		// mContext=(Activity)context;
	}

	@Override
	public void initBaseData() {
		// myContent = this.context;
		info = new PhoneInfo();
		startPoint = new Point();
		endPoint = new Point();
		firstPoint = new Point();
		secondPoint = new Point();
		thirdPoint = new Point();
		fourtPoint = new Point();
		initCar();
		thisCarBean = teslaBean;
		// pop=new PopupWindow(myContent);
		/*
		 * View viewPop =
		 * mContext.getLayoutInflater().inflate(R.layout.populwindow_datepicker,
		 * null); ll_popup = (LinearLayout)
		 * viewPop.findViewById(R.id.ll_datepicker);
		 * pop.setWidth(LayoutParams.MATCH_PARENT);
		 * pop.setHeight(LayoutParams.WRAP_CONTENT);
		 * pop.setBackgroundDrawable(new BitmapDrawable());
		 * pop.setFocusable(true); pop.setOutsideTouchable(true);
		 * pop.setContentView(viewPop);
		 */
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public void initView() {
		tv_passenger_name = (TextView) findViewById(R.id.tv_passenger_name);
		tv_passenger_phone = (TextView) findViewById(R.id.tv_passenger_phone);
		rlyt_change_passenger = (LinearLayout) findViewById(R.id.rlyt_change_passenger);
		rlyt_get_off = (RelativeLayout) findViewById(R.id.rlyt_get_off);
		llyt_start_location = (LinearLayout) findViewById(R.id.llyt_start_location);
		bgSelectCar = (ImageView) findViewById(R.id.ic_bg_select_car);
		icCar = (ImageView) findViewById(R.id.iv_car);
		mRideTimeTv = (TextView) findViewById(R.id.tv_time);
		mGetoffTv = (TextView) findViewById(R.id.tv_destination);
		mGetoffExplainTv = (TextView) findViewById(R.id.tv_destination_explain);
		mRideTimeTv.setOnClickListener(this);
		/*
		 * tvExplain = (TextView) findViewById(R.id.tv_explain);
		 * tvSeclectExplain = (TextView) findViewById(R.id.tv_select_explain);
		 */
		chkExplain = (CheckBox) findViewById(R.id.chk_explain);
		rlyt_order = (RelativeLayout) findViewById(R.id.rlyt_order);
		tv_price = (TextView) findViewById(R.id.tv_price);
		discountTv = (TextView) findViewById(R.id.iv_down);
		SpannableStringBuilder builder = new SpannableStringBuilder("相当于打5折");
		blueSpan = new ForegroundColorSpan(0xff46cafd);
		builder.setSpan(blueSpan, 4, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		discountTv.setText(builder);
		mRideAddressTv = (TextView) findViewById(R.id.car_rideaddress_tv);
		Calendar c = Calendar.getInstance();
		int month = c.get(Calendar.MONTH) + 1;
		nowTimeStr = "" + c.get(Calendar.YEAR) + "-" + month + "-"
				+ c.get(Calendar.DATE) + " " + c.get(Calendar.HOUR) + ":"
				+ c.get(Calendar.MINUTE);
		timeSelector = new CarTimeSelector(context,
				new CarTimeSelector.ResultHandler() {
					@Override
					public void handle(String time) {
						// Toast.makeText(context, time,
						// Toast.LENGTH_LONG).show();
						String[] timeStr = time.split("-");
						String[] dayStr = timeStr[2].split(" ");
						String selectTime = timeStr[1] + "月" + dayStr[0] + "日"
								+ " " + dayStr[1];
						nowTimeStr = selectTime;
						carTimeStr = Calendar.getInstance().get(Calendar.YEAR)
								+ "-" + timeStr[1] + "-" + dayStr[0] + " "
								+ dayStr[1] + ":00";
						mRideTimeTv.setText(selectTime);
					}
				}, nowTimeStr, "2050-11-29 23:59");

		timeSelector.setScrollUnit(CarTimeSelector.SCROLLTYPE.HOUR,
				CarTimeSelector.SCROLLTYPE.MINUTE);
		// 选车背景文字提示颜色
		carDiscriptionTv1 = (TextView) findViewById(R.id.cartype_tv_1);
		carDiscriptionTv2 = (TextView) findViewById(R.id.cartype_tv_2);
		carDiscriptionTv3 = (TextView) findViewById(R.id.cartype_tv_3);
		carDiscriptionTv4 = (TextView) findViewById(R.id.cartype_tv_4);
		carDiscriptionTv1.setOnClickListener(this);
		carDiscriptionTv2.setOnClickListener(this);
		carDiscriptionTv3.setOnClickListener(this);
		carDiscriptionTv4.setOnClickListener(this);

		// 选车背景宽度
		int bgWidth = GlobalParams.WIN_WIDTH - DensityUtil.dip2px(context, 20)
				- DensityUtil.dip2px(context, 70);
		firstPoint.setX(0);
		secondPoint.setX(bgWidth / 3);
		thirdPoint.setX(bgWidth * 2 / 3);
		fourtPoint.setX(bgWidth);
		firstPoint.setY(bgSelectCar.getPivotY());
		secondPoint.setY(bgSelectCar.getPivotY());
		thirdPoint.setY(bgSelectCar.getPivotY());
		fourtPoint.setY(bgSelectCar.getPivotY());
		startPoint = firstPoint;
		endPoint = firstPoint;
		bgSelectCar.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 判断点击位置
				if (firstPoint.getX() - 65 < event.getX()
						&& firstPoint.getX() + 65 > event.getX()) {
					endPoint = firstPoint;
					thisCarBean = moneyBean;
					// 修改选中车颜色1
					carDiscriptionTv1.setTextColor(0xff46cafd);
					carDiscriptionTv2.setTextColor(0xffffffff);
					carDiscriptionTv3.setTextColor(0xffffffff);
					carDiscriptionTv4.setTextColor(0xffffffff);
				} else if (secondPoint.getX() - 65 < event.getX()
						&& secondPoint.getX() + 65 > event.getX()) {
					endPoint = secondPoint;
					thisCarBean = teslaBean;
					// 修改选中车颜色2
					carDiscriptionTv1.setTextColor(0xffffffff);
					carDiscriptionTv2.setTextColor(0xff46cafd);
					carDiscriptionTv3.setTextColor(0xffffffff);
					carDiscriptionTv4.setTextColor(0xffffffff);
				} else if (thirdPoint.getX() - 65 < event.getX()
						&& thirdPoint.getX() + 65 > event.getX()) {
					endPoint = thirdPoint;
					thisCarBean = comfortableBean;
					// 修改选中车颜色3
					carDiscriptionTv1.setTextColor(0xffffffff);
					carDiscriptionTv2.setTextColor(0xffffffff);
					carDiscriptionTv3.setTextColor(0xff46cafd);
					carDiscriptionTv4.setTextColor(0xffffffff);
				} else if (fourtPoint.getX() - 65 < event.getX()
						&& fourtPoint.getX() + 65 > event.getX()) {
					endPoint = fourtPoint;
					thisCarBean = businessBean;
					// 修改选中车颜色4
					carDiscriptionTv1.setTextColor(0xffffffff);
					carDiscriptionTv2.setTextColor(0xffffffff);
					carDiscriptionTv3.setTextColor(0xffffffff);
					carDiscriptionTv4.setTextColor(0xff46cafd);
				}

				if (startPoint.getX() != endPoint.getX()) {
					selectCar();
				}
				return false;
			}
		});
		if(!Utils.isNullOrEmpty(GlobalParams.MY_LOCATION)){
			rideAddress=GlobalParams.MY_LOCATION;
		}
	}

	@Override
	public void initData() {
		if (!Utils.isNullOrEmpty(GlobalParams.USER_PHONE)) {
			info.setPhone(GlobalParams.USER_PHONE);
			info.setPhoneName("本人乘坐");
			tv_passenger_name.setText(info.getPhoneName());
			tv_passenger_phone.setText(info.getPhone());
		} else {
			info.setPhone("");
			info.setPhoneName("");
			tv_passenger_name.setHint("乘车人");
			tv_passenger_phone.setHint("手机号");
		}
		if(rideAddress!=null)
			mRideAddressTv.setText(rideAddress.getAddress());
		/*
		 * pop=new PopupWindow(context); View viewPop =
		 * LayoutInflater.from(context).inflate(R.layout.populwindow_datepicker,
		 * null); ll_popup = (RelativeLayout)
		 * viewPop.findViewById(R.id.ll_datepicker);
		 * pop.setWidth(LayoutParams.MATCH_PARENT);
		 * pop.setHeight(LayoutParams.WRAP_CONTENT);
		 * pop.setBackgroundDrawable(new BitmapDrawable());
		 * pop.setFocusable(true); pop.setOutsideTouchable(true);
		 * pop.setContentView(viewPop);
		 */

	}

	@Override
	public void initListener() {
		rlyt_change_passenger.setOnClickListener(this);
		llyt_start_location.setOnClickListener(this);
		rlyt_get_off.setOnClickListener(this);
		/*
		 * tvSeclectExplain.setOnClickListener(this);
		 * tvExplain.setOnClickListener(this);
		 */
		rlyt_order.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rlyt_change_passenger:
			Intent intentChange = new Intent(context,
					ChangePassengersActivity.class);
			intentChange.putExtra("phoneInfo", info);
			context.startActivity(intentChange);
			break;
		case R.id.llyt_start_location:
			Intent intent = new Intent(context, BoardingActivity.class);
			// ((Activity) context).startActivityForResult(intent, RIDE_CODE);
			context.startActivity(intent);
			break;
		case R.id.rlyt_get_off:
			Intent intentGetOff = new Intent(context, GetOffActivity.class);
			context.startActivity(intentGetOff);
			break;
		/*
		 * case R.id.tv_select_explain: showPopuWindow(v); break;
		 */
		case R.id.tv_explain:
			if (chkExplain.isChecked()) {
				chkExplain.setChecked(false);
			} else {
				chkExplain.setChecked(true);
			}
			break;
		case R.id.rlyt_order:
			// 去订车
			/*
			 * Intent intentOrder=new Intent(context,
			 * ConfirmOrderActivity.class); context.startActivity(intentOrder);
			 */
			// 判断是否可以点击
			MobclickAgent.onEvent(context,"OrderCar");
			if (isGoRide) {
				//判断用户是否登录
				if (Utils.isNullOrEmpty(GlobalParams.USER_ID)) {
					showLoginDialog();
				} else {
					if (Utils.isNullOrEmpty(rideAddress)
							|| Utils.isNullOrEmpty(getoffAddress)
							||Utils.isNullOrEmpty(mRideAddressTv.getText().toString())) {
						showDiaLog("请补全用车信息");
					} else {
						isGoRide = false;
						requestCarOrder(thisCarBean);
					}

				}
				/*
				 * Intent bookCarIntent = new Intent(context,
				 * BookthecarActivity.class);
				 * context.startActivity(bookCarIntent);
				 */
			}
			break;
		case R.id.tv_time:
			// 选择时间
			/*
			 * ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
			 * R.anim.activity_translate_in));
			 * pop.showAtLocation(findViewById(R.layout.layout_carview),
			 * Gravity.BOTTOM, 0, 0);
			 */
			// Toast.makeText(context, "选择乘车时间", 0).show();
			isNowTime = true;
			timeSelector.show();

			/*
			 * Intent timeIntent=new Intent(context,RideTimeActivity.class);
			 * context.startActivity(timeIntent);
			 */
			break;
		case R.id.cartype_tv_1:
			// point1
			endPoint = firstPoint;
			thisCarBean = moneyBean;
			// 修改选中车颜色1
			carDiscriptionTv1.setTextColor(0xff46cafd);
			carDiscriptionTv2.setTextColor(0xffffffff);
			carDiscriptionTv3.setTextColor(0xffffffff);
			carDiscriptionTv4.setTextColor(0xffffffff);
			if (startPoint.getX() != endPoint.getX()) {
				currentCar = 0;
				selectCar();
			}
			break;
		case R.id.cartype_tv_2:
			// point2
			endPoint = secondPoint;
			thisCarBean = teslaBean;
			// 修改选中车颜色2
			carDiscriptionTv1.setTextColor(0xffffffff);
			carDiscriptionTv2.setTextColor(0xff46cafd);
			carDiscriptionTv3.setTextColor(0xffffffff);
			carDiscriptionTv4.setTextColor(0xffffffff);
			if (startPoint.getX() != endPoint.getX()) {
				currentCar = 1;
				selectCar();
			}
			break;
		case R.id.cartype_tv_3:
			// point3
			endPoint = thirdPoint;
			thisCarBean = comfortableBean;
			// 修改选中车颜色3
			carDiscriptionTv1.setTextColor(0xffffffff);
			carDiscriptionTv2.setTextColor(0xffffffff);
			carDiscriptionTv3.setTextColor(0xff46cafd);
			carDiscriptionTv4.setTextColor(0xffffffff);
			if (startPoint.getX() != endPoint.getX()) {
				currentCar = 2;
				selectCar();
			}
			break;
		case R.id.cartype_tv_4:
			// point4
			endPoint = fourtPoint;
			thisCarBean = businessBean;
			// 修改选中车颜色4
			carDiscriptionTv1.setTextColor(0xffffffff);
			carDiscriptionTv2.setTextColor(0xffffffff);
			carDiscriptionTv3.setTextColor(0xffffffff);
			carDiscriptionTv4.setTextColor(0xff46cafd);
			if (startPoint.getX() != endPoint.getX()) {
				currentCar = 3;
				selectCar();
			}
			break;
		default:

			break;
		}
	}

	/**
	 * 展示的popupWindow
	 */
	private PopupWindow popupWindow;

	@SuppressWarnings({ "unused", "deprecation" })
	private void showPopuWindow(View v) {
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		// 弹出的popupWindow View
		View view = View.inflate(LApplication.getContext(),
				R.layout.popu_use_car, null);

		popupWindow = new PopupWindow(view, GlobalParams.WIN_WIDTH * 4 / 5,
				GlobalParams.WIN_HIGTH * 3 / 5);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// popupWindow.setAnimationStyle(R.style.popup_animation);
		popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
		popupWindow.showAsDropDown(v);

		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {

			}
		});
	}

	private void selectCar() {
		Animation translateAnimation = new TranslateAnimation(
				startPoint.getX(), endPoint.getX(), startPoint.getY(),
				endPoint.getY());
		translateAnimation.setDuration(500);// 设置动画持续时间为0.5秒
		translateAnimation.setFillAfter(true);// 设置动画结束后保持当前的位置（即不返回到动画开始前的位置）
		icCar.startAnimation(translateAnimation);
		translateAnimation.setInterpolator(new AccelerateInterpolator());
		startPoint = endPoint;
		translateAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				requestPrices(thisCarBean);
				if(currentCar ==0){
					icCar.setImageResource(R.drawable.ic_lnexpensive);
				}else if(currentCar ==1){
					icCar.setImageResource(R.drawable.ic_tesla);
				}else if(currentCar ==2){
					icCar.setImageResource(R.drawable.ic_comfort);
				}else if(currentCar ==3){
					icCar.setImageResource(R.drawable.ic_business);
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		if (ChangePassengersActivity.info != null) {
			PhoneInfo phoneInfo = ChangePassengersActivity.info;
			info = phoneInfo;
		}else if (!Utils.isNullOrEmpty(GlobalParams.USER_PHONE)) {
			info.setPhone(GlobalParams.USER_PHONE);
			info.setPhoneName("本人乘坐");
		} else {
			info.setPhone("未知");
			info.setPhoneName("未知");
		}
		tv_passenger_name.setText(info.getPhoneName());
		tv_passenger_phone.setText(info.getPhone());
		if (BoardingActivity.rideAddress != null) {
			// 设置上车地点
			rideAddress = BoardingActivity.rideAddress;
			mRideAddressTv.setText(rideAddress.getAddress());
		}
		if (GetOffActivity.getoffAddress != null) {
			//  设置下车地点
			getoffAddress = GetOffActivity.getoffAddress;
			mGetoffTv.setText(GetOffActivity.getoffAddress.getAddress());
			mGetoffTv.setTextColor(0xffffffff);
			mGetoffExplainTv.setVisibility(View.GONE);
			requestPrices(thisCarBean);
		}
		
	}

	class Point {
		private float x;
		private float y;

		public float getX() {
			return x;
		}

		public void setX(float x) {
			this.x = x;
		}

		public float getY() {
			return y;
		}

		public void setY(float y) {
			this.y = y;
		}
	}

	/**
	 * 初始化用车信息
	 */
	private void initCar() {
		teslaBean = new YongCheBean();
		teslaBean.setCar_type_id("43");
		teslaBean.setName("Tesla车型");
		teslaBean.setMin_fee("30");
		teslaBean.setFee_per_kilometer("5.5");
		teslaBean.setFee_per_hour("24");
		teslaBean.setKongshi_distance("1");
		teslaBean.setMin_response_time("1");
		teslaBean.setNight_service_fee("0.5");
		moneyBean = new YongCheBean();
		moneyBean.setCar_type_id("1");
		moneyBean.setName("经济车型");
		moneyBean.setMin_fee("10");
		moneyBean.setFee_per_kilometer("2.6");
		moneyBean.setFee_per_hour("24");
		moneyBean.setKongshi_distance("1");
		moneyBean.setMin_response_time("1");
		moneyBean.setNight_service_fee("0.5");
		comfortableBean = new YongCheBean();
		comfortableBean.setCar_type_id("2");
		comfortableBean.setName("舒适车型");
		comfortableBean.setMin_fee("15");
		comfortableBean.setFee_per_kilometer("5");
		comfortableBean.setFee_per_hour("30");
		comfortableBean.setKongshi_distance("1");
		comfortableBean.setMin_response_time("1");
		comfortableBean.setNight_service_fee("0.5");
		businessBean = new YongCheBean();
		businessBean.setCar_type_id("5");
		businessBean.setName("商务车型");
		businessBean.setMin_fee("20");
		businessBean.setFee_per_kilometer("5.5");
		businessBean.setFee_per_hour("36");
		businessBean.setKongshi_distance("1");
		businessBean.setMin_response_time("1");
		businessBean.setNight_service_fee("0.5");
		thisCarBean = moneyBean;
	}

	/**
	 * 估价
	 */
	private void requestPrices(YongCheBean carBean) {
		if(Utils.isNullOrEmpty(rideAddress)||Utils.isNullOrEmpty(getoffAddress)){
			Toast.makeText(context, "请完善您的用车信息", Toast.LENGTH_SHORT).show();
		}else{
			
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", ConstantValue.CAR_TOKEN); // token
		params.put("city", "bj"); // 城市：北京
		params.put("type", userCarType); // 时租
		params.put("car_type_id", carBean.getCar_type_id()); // 车型
		params.put("expect_start_longitude", ""+rideAddress.getLongitude()); // 出发地经度
		params.put("expect_start_latitude", ""+rideAddress.getLatitude()); // 出发地纬度
		params.put("expect_end_longitude", ""+getoffAddress.getLongitude()); // 目的地经度
		params.put("expect_end_latitude", ""+getoffAddress.getLatitude()); // 目的地纬度
		params.put("time", carTimeStr); // 时间
		params.put("rent_time", carBean.getFee_per_hour()); // 时租时间
		params.put("map_type", "1"); // 地图类型
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.GET_CAR_PRICE, params, new GetDatas() {

					@SuppressWarnings("null")
					@Override
					public void getServerData(BackData data) {
						if (data == null) {
							// 说明网络获取无数据
							Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT)
									.show();
						} else {
							Object obj = data.getObject();
							if (obj == null) {
								Toast.makeText(context, "网络错误",
										Toast.LENGTH_SHORT).show();
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
									Toast.makeText(context, "网络异常",
											Toast.LENGTH_SHORT).show();
								} else {
									Log.i("USECAR",
											GlobalParams.USER_ID + "价格请求成功？"
													+ data.getNetResultCode()
													+ backdata);
									Message msg = new Message();
									msg.what = 0;
									msg.obj = backdata;
									priceHandler.sendMessage(msg);

								}
							}

						}
					}
				}, false, false);

	}}

	/**
	 * 去订车
	 */
	private void requestCarOrder(YongCheBean carBean) {

		final Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", ConstantValue.CAR_TOKEN); // token
		params.put("city", "bj"); // 城市：北京
		params.put("type", userCarType); // 时租
		params.put("car_type_id", carBean.getCar_type_id()); // 车型
		params.put("start_position", codeUTF(rideAddress.getAddress())); // 出发地点
		params.put("start_address", codeUTF(rideAddress.getSpecificLocation())); // 出发地点
																					// ,非必填
		// params.put("expect_start_longitude", "116.458637"); // 出发地点经度
		params.put("expect_start_longitude", "" + rideAddress.getLongitude()); // 出发地点经度，测试
		// params.put("expect_start_latitude","39.955538"); // 出发地点 纬度
		params.put("expect_start_latitude", "" + rideAddress.getLatitude()); // 出发地点纬度,测试
		params.put("time", carTimeStr); // 出发时间
		params.put("rent_time", "1"); // 使用时长
		if (isNowTime) {
			params.put("is_asap", "0"); // 1是随叫随到的订单，0是普通订单
		} else {
			params.put("is_asap", "1"); // 1是随叫随到的订单，0是普通订单
		}
		params.put("end_position", codeUTF(getoffAddress.getAddress())); // 目标地点
		params.put("end_address", codeUTF(getoffAddress.getSpecificLocation())); // 目标地点详细
		params.put("map_type", codeUTF("1")); // 地图类型：1：百度，2：火星 3-谷歌 默认值：1
		params.put("expect_end_longitude", "" + getoffAddress.getLongitude()); // 目的地经度
		params.put("expect_end_latitude", "" + getoffAddress.getLatitude()); // 目的地纬度
		params.put("passenger_name", info.getPhoneName()); // 乘车人姓名
		params.put("passenger_phone", info.getPhone()); // 乘车人电话
		params.put("passenger_number", "1"); // 乘车人数量
		params.put("sms_type", ChangePassengersActivity.sendSmsStr); // 是否给乘车人发短信
																		// 1：发短信
																		// 0：不发短信
		params.put("app_user_id", GlobalParams.USER_ID); // 用户ID
		params.put("msg", codeUTF("")); // 客户留言
		params.put("is_face_pay", "1"); // 1面付，0或不传为非面付
		params.put("third_party_coupon", "0"); // 优惠券金额

		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.GET_CAR_ORDER, params, new GetDatas() {

					@SuppressWarnings("null")
					@Override
					public void getServerData(BackData data) {
						if (data == null) {
							// 说明网络获取无数据
							Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT)
									.show();
						} else {
							Object obj = data.getObject();
							if (obj == null) {
								Toast.makeText(context, "网络错误",
										Toast.LENGTH_SHORT).show();
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
									Toast.makeText(context, "网络异常",
											Toast.LENGTH_SHORT).show();
								} else {
									Log.e("USECAR", params.toString());
									Log.i("USECAR",
											GlobalParams.USER_ID + "订单创建成功:"
													+ data.getNetResultCode()
													+ backdata);
									Message msg = new Message();
									msg.what = 1;
									msg.obj = backdata;
									priceHandler.sendMessage(msg);

								}
							}

						}
					}
				}, false, false);
	}

	/**
	 * 增加订单信息
	 */
	private void requestCreatOrder(YongCheBean carBean, String orderId) {
		final Map<String, String> params = new HashMap<String, String>();
		// params.put("access_token",
		// "RPcFNRaJwlqNt0PBkMt1pu5y7p4cMu3UzNy5HwLs"); // token
		params.put("order_Id", orderId); // 城市：北京
		params.put("type", userCarType); // 时租
		params.put("city", "bj"); // 城市：北京
		params.put("car_type_id", carBean.getCar_type_id()); // 车型
		params.put("car_Type", carBean.getName()); // 车型
		params.put("expect_Start_Time", carTimeStr); // 出发时间
		params.put("time_Length", "1"); // 使用时长
		params.put("passenger_Name", info.getPhoneName()); // 乘车人姓名
		params.put("passenger_Phone", info.getPhone()); // 乘车人电话
		params.put("passenger_Number", "1"); // 乘车人数量
		params.put("start_Position", codeUTF(rideAddress.getAddress())); // 出发地点
		params.put("start_Address", codeUTF(rideAddress.getSpecificLocation())); // 出发地点
																					// ,非必填
		params.put("end_Position", codeUTF(getoffAddress.getAddress())); // 目标地点
		params.put("end_Address", codeUTF(getoffAddress.getSpecificLocation())); // 目标地点详细
		params.put("start_Lognitude", "" + rideAddress.getLongitude()); // 出发地点经度
		params.put("start_Latitude", "" + rideAddress.getLatitude()); // 出发地点纬度
		params.put("end_Lognitude", "" + getoffAddress.getLongitude()); // 目的地经度
		params.put("end_Latitude", "" + getoffAddress.getLatitude()); // 目的地纬度
		params.put("user_Id", GlobalParams.USER_ID); // 用户id
		params.put("payStatus", "1"); // 1面付，0或不传为非面付
		params.put("total_Amount", tv_price.getText().toString()); // 订单金额
		Log.i("USECAR", "price ...."+tv_price.getText().toString());

		/*
		 * params.put("is_asap", "1"); // 1是随叫随到的订单，0是普通订单
		 * params.put("map_type", codeUTF("1")); // 地图类型：1：百度，2：火星 3-谷歌 默认值：1
		 * params.put("sms_type", ChangePassengersActivity.sendSmsStr); //
		 * 是否给乘车人发短信 1：发短信 0：不发短信 params.put("app_user_id",
		 * GlobalParams.USER_ID); // 用户ID params.put("msg", codeUTF("")); //
		 * 客户留言
		 */
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.CREAT_CAR_ORDER, params, new GetDatas() {

					@SuppressWarnings("null")
					@Override
					public void getServerData(BackData data) {
						if (data == null) {
							// 说明网络获取无数据
							Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT)
									.show();
						} else {
							Object obj = data.getObject();
							if (obj == null) {
								Toast.makeText(context, "网络错误",
										Toast.LENGTH_SHORT).show();
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
									Toast.makeText(context, "网络异常",
											Toast.LENGTH_SHORT).show();
								} else {
									Log.e("USECAR", "晶朝服务器" + params);
									Log.i("USECAR",
											carTimeStr + "晶朝服务器"
													+ data.getNetResultCode()
													+ backdata);
									Message msg = new Message();
									msg.what = 2;
									msg.obj = backdata;
									priceHandler.sendMessage(msg);

								}
							}

						}
					}
				}, false, false);

	}

	/**
	 * 
	 * @param code
	 * @return String转换为utf8格式
	 */
	private String codeUTF(String code) {
		if(TextUtils.isEmpty(code)){
			return "";
		}
		try {
			return URLDecoder.decode(code, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}

	}

	private void showLoginDialog() {
		if(loginDIalog==null)
			loginDIalog = new CustomDialog(context, new OnClickListener() {
				@Override
				public void onClick(View v) {
	
					Intent intent = new Intent(context, LoginActivity.class);
					context.startActivity(intent);
					isGoRide = true;
					loginDIalog.dismiss();
				}
			}, new OnClickListener() {
	
				@Override
				public void onClick(View v) {
					loginDIalog.dismiss();
					isGoRide = true;
				}
			});
		loginDIalog.setRemindTitle("温馨提示");
		loginDIalog.setCancelTxt("取消");
		loginDIalog.setConfirmTxt("立即登录");
		loginDIalog.setRemindMessage("登录后才可以使用订车服务");
		loginDIalog.show();
	}

	/**
	 * 提示补全信息
	 * 
	 * @param msg
	 */
	private void showDiaLog(String msg) {
		msgDialog = null;
		msgDialog = new CustomDialog(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isGoRide = true;
				msgDialog.dismiss();
			}
		});
		msgDialog.setRemindMessage(msg);
		msgDialog.setRemindTitle("温馨提示");
		msgDialog.setConfirmTxt("确认");
		msgDialog.show();
	}
	
}
