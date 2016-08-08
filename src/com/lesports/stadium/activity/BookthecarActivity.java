package com.lesports.stadium.activity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.AMap.OnMarkerDragListener;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.lesports.stadium.R;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.DrivierBean;
import com.lesports.stadium.bean.PayBean;
import com.lesports.stadium.bean.PayParametric;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.fragment.ServiceFragment;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.net.Code.HttpSetting;
import com.lesports.stadium.net.NetLoadListener;
import com.lesports.stadium.net.NetService;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.PayUtils;
import com.lesports.stadium.utils.Utils;
import com.lesports.stadium.utils.WifiUtils;
import com.lesports.stadium.view.CircleImageView;
import com.lesports.stadium.view.CustomDialog;
import com.letv.lepaysdk.ELePayState;
import com.letv.lepaysdk.LePayApi;
import com.letv.lepaysdk.LePayConfig;
import com.letv.lepaysdk.LePay.ILePayCallback;
import com.umeng.analytics.MobclickAgent;

/**
 * ***************************************************************
 * 
 * @ClassName: BookthecarActivity
 * 
 * @Desc : 去订车界面
 * @keywords : 地图、位置、司机状态
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 邓聪聪
 * 
 * @data:2016-3-11 下午4:16:46
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
@SuppressLint({ "InflateParams", "CutPasteId", "HandlerLeak" })
public class BookthecarActivity extends Activity implements OnClickListener,
		OnMarkerClickListener, OnMapClickListener, OnInfoWindowClickListener,
		OnMarkerDragListener, InfoWindowAdapter, OnMapLoadedListener {
	/**
	 * 返回
	 */
	private ImageView goBackImg;
	/**
	 * 标题
	 */
	public static TextView titleTv;
	/**
	 * 取消订单
	 */
	public static TextView cancleTv;
	/**
	 * 取消订车
	 */
	private CustomDialog exitDialog;
	/**
	 * 提示信息
	 */
	private static CustomDialog yidaoDialog;
	/**
	 * 网络请求
	 */
	private static NetService netService;
	/**
	 * 显示司机详情
	 */
	private static LinearLayout driverDetailLl;
	/**
	 * 上下文
	 */
	private static Context bookContext;
	/**
	 * 地图
	 */
	private MapView mapView;
	/**
	 * 地图
	 */
	private AMap aMap;
	/**
	 * 订单id
	 */
	private static String orderIdStr;
	/**
	 * 轮循计时器
	 */
	// private Timer mDriverTimer = new Timer();
	/**
	 * 司机信息populwindow
	 */
	/*
	 * private static PopupWindow pop = null; private static LinearLayout
	 * ll_popup;
	 */
	private View parentView;
	/**
	 * 标题栏
	 */
	@SuppressWarnings("unused")
	private static View driverTitleView;
	/**
	 * 打电话给司机
	 */
	private CircleImageView mPhoneImg;
	/**
	 * 司机头像
	 */
	private static CircleImageView mDriverHeadImg;
	/**
	 * 司机信息
	 */
	private static TextView mDriverNameTv, mDriverNumberTv, mDriverOrderTv;
	/**
	 * 司机等级信息
	 */
	private static RatingBar mDriverLeverRt;
	/**
	 * 存储司机详情
	 */
	public static DrivierBean driverDetailBean = new DrivierBean();;
	/**
	 * 注册广播
	 */
	public static carBroadCastReceiver myBroadCastReceiver;
	/**
	 * 支付UI，用于显示或隐藏控制
	 */
	public static LinearLayout mGoPayLl;
	/**
	 * 支付按钮
	 */
	private static Button mGoPayBt;
	/**
	 * 支付信息
	 */
	private static PayBean paybean;
	/**
	 * 支付金额
	 */
	public static TextView payFeeTv;
	public static String orderFeeStr = "";
	// 40.046051, 116.305931晶朝科技位置
	public static String longtitudeStr = "116.305931";
	public static String latitudeStr = "40.046051";

	private final static int HANDLE_TAG_0=0;
	private final static int HANDLE_TAG_1=1;
	private final static int HANDLE_TAG_2=2;
	private final static int HANDLE_TAG_3=3;
	private final static int HANDLE_TAG_4=4;
	private final static int HANDLE_TAG_5=5;
	/**
	 * 广播消息处理及网络请求
	 */
	public static Handler carhandler = new Handler() {
		public void handleMessage(Message msg) {
			String feeStr = "";
			String discountStr = "";
			switch (msg.what) {
			case HANDLE_TAG_0:
				// requestOrderDriver();
				break;
			case HANDLE_TAG_1:
				// 处理司机详细信息
				String driverBackdata = (String) msg.obj;
				if(!TextUtils.isEmpty(driverBackdata)){
					useWayHandleDricerInfo(driverBackdata);
				}
				driverDetailLl.setVisibility(View.VISIBLE);
				break;
			case HANDLE_TAG_2:
				// 处理账单确认
				String feeBackdata = (String) msg.obj;
				if(!TextUtils.isEmpty(feeBackdata)){
					JSONObject feeObj;

					try {
						feeObj = new JSONObject(feeBackdata);
						if (feeBackdata.contains("order_amount")) {
							JSONObject feeDeltealObj = feeObj
									.getJSONObject("result");
							// 调整金额，order_amount为应付金额;coupon_facevalue优惠金额
							feeStr = feeDeltealObj.getString("order_amount");
							discountStr = feeDeltealObj
									.getString("coupon_facevalue");
							orderFeeStr = feeStr;
							// mDriverLeverRt.setNumStars(Integer.parseInt(driverDetailBean.getStar_level()));
							// mDriverLeverRt.setRating(Integer.parseInt(driverDetailBean.getStar_level()));
							mGoPayBt.setText("确认支付￥" + Utils.parseTwoNumber(feeStr));
							payFeeTv.setText("￥" + feeStr);
							mGoPayLl.setVisibility(View.VISIBLE);
							requestCreatPayOrder(feeStr);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
				break;
			case HANDLE_TAG_3:
				// 处理生成的支付订单返回（）
				String jsondatass = (String) msg.obj;
				if (jsondatass != null && !TextUtils.isEmpty(jsondatass)) {
					paybean = useWayJsonDataPay(jsondatass);
					if (paybean != null
							&& !TextUtils.isEmpty(paybean.getSign())) {
						// 开始调用方法，进行支付
						// pay(paybean);
					}
				}
				break;
			case HANDLE_TAG_4:
				// 支付成功回调
				if (mGoPayLl.getVisibility() == 0) {
					mGoPayLl.setVisibility(View.GONE);
				}
				/*
				 * Intent payOkIntent=new
				 * Intent(bookContext,CarServiceCompleteActivity.class);
				 * bookContext.startActivity(payOkIntent);
				 */
				MobclickAgent.onEvent(bookContext, "PaySuccessfully");
				payOk();
				break;
			case HANDLE_TAG_5:
				// 易道支付成功回调
				String resultPay = (String) msg.obj;
				try {
					JSONObject payObj = new JSONObject(resultPay);
					String payCode = payObj.getString("code");
					if (payCode.equals("200")) {
						titleTv.setText("交易结束");
						Intent payOkIntent = new Intent(bookContext,
								CarServiceCompleteActivity.class);
						payOkIntent.putExtra("orderid", orderIdStr);
						payOkIntent.putExtra("fee", feeStr);
						payOkIntent.putExtra("discount", discountStr);
						bookContext.startActivity(payOkIntent);
					} else {
						Toast.makeText(bookContext, payObj.getString("result"),
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
		}
	};
	private Handler cancleHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 6:
				// 取消订单成功
				requestDeleteOrder();
				break;
			case 7:
				// 取消订单失败
				Toast.makeText(BookthecarActivity.this, "订单取消失败",
						Toast.LENGTH_SHORT).show();
				break;
			case 8:
				// 删除订单成功
				finish();
				break;
			case 9:
				// 删除订单失败
			/*	Toast.makeText(BookthecarActivity.this, "服务器订单取消失败",
						Toast.LENGTH_SHORT).show();*/
				finish();
				break;
			case 10:
				// 获取订单信息成功
				String orderResult = (String) msg.obj;
				try {
					JSONObject codeObj = new JSONObject(orderResult);
					String codeStr = codeObj.getString("code");
					if (codeStr.equals("200")) {
						JSONObject resultArr = new JSONObject(
								codeObj.getString("result"));
						String statusStr = resultArr.getString("status");
						longtitudeStr = resultArr.getString("start_longitude");
						latitudeStr = resultArr.getString("start_latitude");
						int statusint = Integer.parseInt(statusStr);
						if (statusint < 4) {
							titleTv.setText(R.string.yidao_wait_driver);
						} else if (statusint == 4) {
							titleTv.setText(R.string.yidao_driver_ok);
							// cancleTv.setVisibility(View.GONE);
							requestOrderDriver();
						} else if (statusint == 5) {
							titleTv.setText(R.string.yidao_driver_arrive);
							cancleTv.setVisibility(View.GONE);
							requestOrderDriver();
						} else if (statusint == 6) {
							titleTv.setText(R.string.yidao_service_start);
							cancleTv.setVisibility(View.GONE);
							requestOrderDriver();
						} else if (statusint == 7) {
							titleTv.setText(R.string.yidao_service_end);
							cancleTv.setVisibility(View.GONE);
							requestOrderDriver();
						} else if (statusint > 7) {
							titleTv.setText(R.string.yidao_end);
							cancleTv.setVisibility(View.GONE);
							requestOrderFee();
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case 11:
				// 获取订单信息失败

				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parentView = getLayoutInflater().inflate(R.layout.activity_bookthecar,
				null);
		setContentView(parentView);
		bookContext = this;
		Intent myIntent = getIntent();
		orderIdStr = myIntent.getStringExtra("orderid");
		String tag = myIntent.getStringExtra("tag");
		// 判断是否从订单跳转
		if (Utils.isNullOrEmpty(orderIdStr)) {
			orderIdStr = LApplication.yidaoOrderId;
		}
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();
		initView();
		if (Utils.isNullOrEmpty(tag)) {
			requestOrderStatus();
		} else if (GlobalParams.CESHI_YIDAO) {// 判断是否乐视测试版本
			titleTv.setText("司机已接单");
			if (driverDetailLl.getVisibility() == View.GONE) {
				driverDetailLl.setVisibility(View.VISIBLE);
				if (mGoPayLl.getVisibility() == View.VISIBLE) {
					mGoPayLl.setVisibility(View.GONE);
				}
				if (cancleTv.getVisibility() == View.VISIBLE) {
					cancleTv.setVisibility(View.GONE);
				}
			}
		}

	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
		}
		aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
		aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
		aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
		UiSettings mUiSettings = aMap.getUiSettings();
		mUiSettings.setScaleControlsEnabled(false);
		mUiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);// 设置地图logo显示在右下方
		mUiSettings.setZoomControlsEnabled(false);
		mUiSettings.setMyLocationButtonEnabled(false); // 是否显示默认的定位按钮
		addMarkersToMap();// 往地图上添加marker

	}

	/**
	 * 初始化组件
	 */
	private void initView() {
		// 标题栏
		goBackImg = (ImageView) findViewById(R.id.title_left_iv);
		goBackImg.setOnClickListener(this);
		titleTv = (TextView) findViewById(R.id.tv_title);
		titleTv.setText("等待司机接单");
		cancleTv = (TextView) findViewById(R.id.tv_btn);
		cancleTv.setText("取消用车");
		cancleTv.setVisibility(View.VISIBLE);
		cancleTv.setOnClickListener(this);
		driverTitleView = findViewById(R.id.driver_title_include);
		driverDetailLl = (LinearLayout) findViewById(R.id.bookthecar_driverdetail_ll);
		driverDetailLl.setVisibility(View.GONE);
		mDriverHeadImg = (CircleImageView) findViewById(R.id.bookthecar_driverhead_img);
		// 司机详情
		/*
		 * pop = new PopupWindow(this); View viewPop =
		 * getLayoutInflater().inflate(R.layout.populwindow_driver, null);
		 * ll_popup = (LinearLayout) viewPop.findViewById(R.id.driver_popul_ll);
		 * pop.setWidth(LayoutParams.MATCH_PARENT);
		 * pop.setHeight(LayoutParams.WRAP_CONTENT);
		 * pop.setBackgroundDrawable(new BitmapDrawable());
		 * pop.setFocusable(true); pop.setOutsideTouchable(true);
		 * pop.setContentView(viewPop);
		 */
		mPhoneImg = (CircleImageView) findViewById(R.id.driver_phone_img);
		mPhoneImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:"
						+ driverDetailBean.getCellphone()));
				startActivity(intent);
			}
		});
		mGoPayLl = (LinearLayout) findViewById(R.id.bookthecar_gopay_ll);
		mGoPayLl.setVisibility(View.GONE);
		mDriverNameTv = (TextView) findViewById(R.id.driveldetail_name_tv);
		mDriverNumberTv = (TextView) findViewById(R.id.driverdetail_numbercompany_tv);
		mDriverOrderTv = (TextView) findViewById(R.id.driverdetail_completecount_tv);
		mDriverLeverRt = (RatingBar) findViewById(R.id.driverdetail_lever_rb);
		payFeeTv = (TextView) findViewById(R.id.bookthecar_pay_fee_tv);

		/*
		 * LinearLayout parent =
		 * (LinearLayout)findViewById(R.id.driver_popul_ll);
		 * parent.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * 
		 * } });
		 */
		mGoPayBt = (Button) findViewById(R.id.bookthecar_pay_bt);
		mGoPayBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(BookthecarActivity.this, "pay");
				payOnline(paybean);
			}
		});

		// 计时器开始轮循获取订单接口
		/*
		 * mDriverTimer.schedule(new TimerTask() {
		 * 
		 * @Override public void run() { // requestOrderDriver();
		 * runOnUiThread(new Runnable() { public void run() {
		 * ll_popup.startAnimation
		 * (AnimationUtils.loadAnimation(BookthecarActivity.this,
		 * R.anim.activity_translate_in)); pop.showAsDropDown(driverTitleView);
		 * } }); } }, 2000);
		 */
		myBroadCastReceiver = new carBroadCastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.broadcast.yidao.car");
		registerReceiver(myBroadCastReceiver, intentFilter);
		// myBroadCastReceiver.onReceive(context, intent)
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		if(carhandler!=null)
			carhandler.removeCallbacksAndMessages(null);
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_iv:
			// 返回
			if (!Utils.isNullOrEmpty(ServiceFragment.instence)) {
				ServiceFragment.instence.tag_views = 3;
			}
			finish();
			break;
		case R.id.tv_btn:
			// 取消用车
			exitDialog = null;
			exitDialog = new CustomDialog(BookthecarActivity.this,
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							exitDialog.dismiss();
							if(ServiceFragment.instence!=null)
								ServiceFragment.instence.tag_views = 3;
							//requestCancleOrder();
							try {
								deleteRequest();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}, new OnClickListener() {

						@Override
						public void onClick(View v) {
							exitDialog.dismiss();
						}
					});
			exitDialog.setCancelTxt("暂不取消");
			exitDialog.setConfirmTxt("确定取消");
			exitDialog.setRemindMessage("若多次取消预定成功的行程，24小时内您将无法继续预定");
			exitDialog.show();

			break;
		}
	}

	@Override
	public void onMapClick(LatLng arg0) {

	}

	/**
	 * 对marker标注点点击响应事件
	 */
	@Override
	public boolean onMarkerClick(final Marker marker) {
		// markerText.setText("你点击的是" + marker.getTitle()); //获取位置信息
		return false;
	}

	/**
	 * 在地图上添加marker
	 */
	private void addMarkersToMap() {

		// 文字显示标注，可以设置显示内容，位置，字体大小颜色，背景色旋转角度,Z值等
		/*
		 * TextOptions textOptions = new TextOptions().position(new
		 * LatLng(39.90403, 116.407525)) .text("北京").fontColor(Color.BLACK)
		 * .backgroundColor
		 * (Color.BLUE).fontSize(30).rotate(20).align(Text.ALIGN_CENTER_HORIZONTAL
		 * , Text.ALIGN_CENTER_VERTICAL)
		 * .zIndex(1.f).typeface(Typeface.DEFAULT_BOLD) ;
		 * aMap.addText(textOptions);
		 */
		/*
		 * aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f) .position(new
		 * LatLng(40.04615, 116.307503)).title("上地七街 公交")
		 * .snippet("上地七街公交:40.04615, 116.307503").draggable(true));
		 * aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f) .position(new
		 * LatLng(40.045151, 116.30397)).title("上地开拓路北站")
		 * .snippet("上地开拓路北站:40.045151, 116.30397").draggable(true));
		 * 
		 * aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f) .position(new
		 * LatLng(40.04747, 116.29689)).title("软件广场")
		 * .snippet("软件广场:40.04747, 116.29689").draggable(true));
		 */

		drawMarkers();// 添加10个带有系统默认icon的marker
	}

	/**
	 * 绘制系统默认的1种marker背景图片
	 */
	public void drawMarkers() {
		Marker marker = aMap
				.addMarker(new MarkerOptions()
						.position(
								new LatLng(Double.parseDouble(latitudeStr),
										Double.parseDouble(longtitudeStr)))
						// 晶朝科技位置
						.title("您的上车位置")
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.ic_map_gocar))
						.draggable(true));
		marker.showInfoWindow();// 设置默认显示一个infowinfow
	}

	/**
	 * 监听拖动marker时事件回调
	 */
	@Override
	public void onMarkerDrag(Marker marker) {
		/*
		 * String curDes = marker.getTitle() + "拖动时当前位置:(lat,lng)\n(" +
		 * marker.getPosition().latitude + "," + marker.getPosition().longitude
		 * + ")";
		 */
		// markerText.setText(curDes);
	}

	/**
	 * 监听拖动marker结束事件回调
	 */
	@Override
	public void onMarkerDragEnd(Marker arg0) {

	}

	/**
	 * 监听开始拖动marker事件回调
	 */
	@Override
	public void onMarkerDragStart(Marker arg0) {

	}

	/**
	 * 监听点击infowindow窗口事件回调
	 */
	@Override
	public void onInfoWindowClick(Marker marker) {
		Toast.makeText(this, marker.getTitle(), Toast.LENGTH_SHORT).show();

	}

	/**
	 * 监听amap地图加载成功事件回调
	 */
	@Override
	public void onMapLoaded() {
		// 设置所有maker显示在当前可视区域地图中
		LatLngBounds bounds;
		if (!Utils.isNullOrEmpty(BoardingActivity.rideAddress)) {
			bounds = new LatLngBounds.Builder().include(
					new LatLng(BoardingActivity.rideAddress.getLatitude(),
							BoardingActivity.rideAddress.getLongitude()))
					.build();
		} else {
			bounds = new LatLngBounds.Builder().include(
					new LatLng(Double.parseDouble(latitudeStr), Double
							.parseDouble(longtitudeStr))).build();
		}
		aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
	}

	/**
	 * 监听自定义infowindow窗口的infocontents事件回调
	 */
	@Override
	public View getInfoContents(Marker arg0) {
		/*
		 * if (radioOption.getCheckedRadioButtonId() !=
		 * R.id.custom_info_contents) { return null; } View infoContent =
		 * getLayoutInflater().inflate( R.layout.custom_info_contents, null);
		 * render(marker, infoContent);
		 */
		return null;
	}

	/**
	 * 监听自定义infowindow窗口的infowindow事件回调
	 */
	@Override
	public View getInfoWindow(Marker arg0) {
		return null;
	}

	// GET
	/**
	 * 获取司机详细信息
	 */
	public static void requestOrderDriver() {
		netService = NetService.getInStance();

		netService.setParams("order_id", orderIdStr);
		netService.setParams("access_token", ConstantValue.CAR_TOKEN);
		netService.setHttpMethod(HttpSetting.GET_MODE);
		netService.setUrl(ConstantValue.GET_DRIVER_DETEAL);
		netService.loader(new NetLoadListener() {
			@Override
			public void onloaderListener(BackData result) {
				Message msg = new Message();
				msg.what = 1;
				msg.obj = result.getObject();
				carhandler.sendMessage(msg);
			}
		});

	}

	//
	// @Override
	// public void onBackPressed() {
	// super.onBackPressed();
	// ServiceFragment.instence.tag_views=3;
	// }

	public static class carBroadCastReceiver extends BroadcastReceiver {
		public carBroadCastReceiver() {

		};

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("com.broadcast.yidao.car")) {
				String msg = intent.getStringExtra("msg");
				try {
					if (msg.equals("无人接单")) {
						titleTv.setText(msg);
						// showAlert(msg);无人接单
					} else if (msg.equals("司机已确认")) {
						titleTv.setText(msg);
						// cancleTv.setVisibility(View.GONE);
						requestOrderDriver();
						// showAlert(msg);司机已确认
					} else if (msg.equals("司机已到达")) {
						titleTv.setText(msg);
						if (driverDetailLl.getVisibility() == View.GONE) {
							if (cancleTv.getVisibility() == View.VISIBLE) {
								cancleTv.setVisibility(View.GONE);
							}
							requestOrderDriver();
						}
						// showAlert(msg);司机已到达
					} else if (msg.equals("服务开始")) {
						titleTv.setText(msg);
						if (driverDetailLl.getVisibility() == View.GONE) {
							if (cancleTv.getVisibility() == View.VISIBLE) {
								cancleTv.setVisibility(View.GONE);
							}
							requestOrderDriver();
						}
						// showAlert(msg);服务开始
					} else if (msg.equals("服务完成")) {
						titleTv.setText(msg);
						if (driverDetailLl.getVisibility() == View.GONE) {
							if (cancleTv.getVisibility() == View.VISIBLE) {
								cancleTv.setVisibility(View.GONE);
							}
							requestOrderDriver();
						}
						// showAlert(msg);服务完成
					} else if (msg.equals("账单确认")) {
						titleTv.setText(msg);
						requestOrderFee();
						// 账单确认
					} else if (msg.equals("交易完成")) {
						titleTv.setText(msg);
						if (mGoPayLl.getVisibility() == 0) {
							mGoPayLl.setVisibility(View.GONE);
						}
						// showAlert(msg);交易完成
					}
				} catch (Exception e) {
				}
			}
			// Toast.makeText(BookthecarActivity.this, "onReceive=动", 0).show();

		}
	}

	/**
	 * 用车流程，提示信息
	 * 
	 * @param title
	 */
	public static void showAlert(String title) {
		yidaoDialog = null;
		yidaoDialog = new CustomDialog(new OnClickListener() {

			@Override
			public void onClick(View v) {
				yidaoDialog.dismiss();
			}
		});
		yidaoDialog.setRemindMessage(title);
		yidaoDialog.setConfirmTxt("确认");
		yidaoDialog.setRemindTitle("温馨提示");

		yidaoDialog.show();
	}

	/**
	 * 从易道获取价格
	 */
	public static void requestOrderFee() {

		netService = NetService.getInStance();

		// netService.setParams("order_id", LApplication.yidaoOrderId);
		netService.setParams("access_token", ConstantValue.CAR_TOKEN);
		netService.setHttpMethod(HttpSetting.GET_MODE);
		netService.setUrl(ConstantValue.GET_ORDER_FEE + orderIdStr);
		netService.loader(new NetLoadListener() {
			@Override
			public void onloaderListener(BackData result) {
				Message msg = new Message();
				msg.what = 2;
				msg.obj = result.getObject();
				carhandler.sendMessage(msg);
			}
		});
	}

	/**
	 * 从我们的服务器生成支付订单
	 */
	public static void requestCreatPayOrder(String money) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("user_name", GlobalParams.USER_NAME); // 用户名称
		params.put("orderId", orderIdStr); // 用车的订单
		WifiUtils mWifi = new WifiUtils(LApplication.context);
		params.put("ip", mWifi.getIpAddress()); // ip
		params.put("pay_expire", money); // 价格

		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.YIDAO_PAY_ORDER, params, new GetDatas() {
					@SuppressWarnings("null")
					@Override
					public void getServerData(BackData data) {
						if (data == null) {
							// 说明网络获取无数据
							Toast.makeText(LApplication.context, "网络错误",
									Toast.LENGTH_SHORT).show();
						} else {
							Object obj = data.getObject();
							if (obj == null) {
								Toast.makeText(LApplication.context, "网络错误",
										Toast.LENGTH_SHORT).show();
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
									Toast.makeText(LApplication.context,
											"网络异常", Toast.LENGTH_SHORT).show();
								} else {
									// 调用方法，给listview加载数据，先将数据通过handler发送到主线程中
									Message msg = new Message();
									msg.what = 3;
									msg.obj = backdata;
									carhandler.sendMessage(msg);
								}
							}

						}
					}
				}, false, false);

	}

	private void payOnline(PayBean paybean) {

		if (paybean != null && !TextUtils.isEmpty(paybean.getSign())) {
			PayParametric parameters = new PayParametric();
			parameters.setVersion(paybean.getVersion());
			parameters.setSign(paybean.getSign());
			parameters.setService(paybean.getService());
			parameters.setUser_id(paybean.getUser_id());
			parameters.setUser_name(paybean.getUser_name());
			parameters.setNotify_url(paybean.getNotify_url());
			parameters.setMerchant_no(paybean.getMerchant_no());
			parameters.setOut_trade_no(paybean.getOut_trade_no());
			parameters.setMerchant_business_id(paybean
					.getMerchant_business_id());
			parameters.setPrice(orderFeeStr);
			parameters.setCurrency(paybean.getCurrency());
			parameters.setPay_expire(paybean.getPay_expire());
			parameters.setProduct_id(paybean.getProduct_id());
			parameters.setProduct_name(paybean.getProduct_name());
			parameters.setProduct_desc(paybean.getProduct_desc());
			parameters.setProduct_urls(paybean.getProduct_urls());
			parameters.setKey_index(paybean.getKey_index());
			parameters.setInput_charset(paybean.getInput_charset());
			parameters.setSign_type(paybean.getSign_type());

			String param = null;
			try {
				param = PayUtils.getTradeInfo(parameters);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			LePayConfig config = new LePayConfig();
			config.hasShowTimer = false;
			LePayApi.initConfig(BookthecarActivity.this, config);
			LePayApi.doPay(BookthecarActivity.this, param,
					new ILePayCallback() {
						@Override
						public void payResult(ELePayState status, String message) {
							// Toast.makeText(OrderActivity.this,
							// "结果:" + status + "|" + message,
							// Toast.LENGTH_SHORT)
							// .show();
							if (ELePayState.CANCEL == status) { // 支付取消
							// Toast.makeText(BookthecarActivity.this, "取消支付",
							// Toast.LENGTH_SHORT).show();

							} else if (ELePayState.FAILT == status) { // 支付失败
								Toast.makeText(BookthecarActivity.this, "支付失败",
										Toast.LENGTH_SHORT).show();

							} else if (ELePayState.OK == status) { // 支付成功
//								Toast.makeText(BookthecarActivity.this, "支付成功",
//										Toast.LENGTH_SHORT).show();
								Message msg = new Message();
								msg.what = 4;
								carhandler.sendMessage(msg);

							} else if (ELePayState.WAITTING == status) { // 支付中

							}
						}
					});
		} else {
			Toast.makeText(BookthecarActivity.this, "支付异常", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * 解析获取下来的真正的支付参数
	 * 
	 * @param jsondatass
	 */
	private static PayBean useWayJsonDataPay(String jsondatass) {
		PayBean bean = null;
		try {
			bean = new PayBean();
			if (jsondatass != null && !TextUtils.isEmpty(jsondatass)) {
				JSONObject obj = new JSONObject(jsondatass);
				if (obj.has("currency")) {
					bean.setCurrency(obj.getString("currency"));
				}
				if (obj.has("input_charset")) {
					bean.setInput_charset(obj.getString("input_charset"));
				}
				if (obj.has("ip")) {
					bean.setIp(obj.getString("ip"));
				}
				if (obj.has("key_index")) {
					bean.setKey_index(obj.getString("key_index"));
				}
				if (obj.has("merchant_business_id")) {
					bean.setMerchant_business_id(obj
							.getString("merchant_business_id"));
				}
				if (obj.has("merchant_no")) {
					bean.setMerchant_no(obj.getString("merchant_no"));
				}
				if (obj.has("notify_url")) {
					bean.setNotify_url(obj.getString("notify_url"));
				}
				if (obj.has("out_trade_no")) {
					bean.setOut_trade_no(obj.getString("out_trade_no"));
				}
				if (obj.has("pay_expire")) {
					bean.setPay_expire(obj.getString("pay_expire"));
				}
				if (obj.has("price")) {
					bean.setPrice(obj.getString("price"));
				}
				if (obj.has("product_desc")) {
					bean.setProduct_desc(obj.getString("product_desc"));
				}
				if (obj.has("product_id")) {
					bean.setProduct_id(obj.getString("product_id"));
				}
				if (obj.has("product_name")) {
					bean.setProduct_name(obj.getString("product_name"));
				}
				if (obj.has("product_urls")) {
					bean.setProduct_urls(obj.getString("product_urls"));
				}
				if (obj.has("service")) {
					bean.setService(obj.getString("service"));
				}
				if (obj.has("sign")) {
					bean.setSign(obj.getString("sign"));
				}
				if (obj.has("sign_type")) {
					bean.setSign_type(obj.getString("sign_type"));
				}
				if (obj.has("timestamp")) {
					bean.setTimestamp(obj.getString("timestamp"));
				}
				if (obj.has("user_id")) {
					bean.setUser_id(obj.getString("user_id"));
				}
				if (obj.has("user_name")) {
					bean.setUser_name(obj.getString("user_name"));
				}
				if (obj.has("version")) {
					bean.setVersion(obj.getString("version"));
				}
				bean.setProduct_urls("yidao");
				return bean;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bean;
	}

	/**
	 * 支付成功给易道
	 */
	public static void payOk() {

		netService = NetService.getInStance();

		// netService.setParams("order_id", LApplication.yidaoOrderId);
		netService.setParams("access_token", ConstantValue.CAR_TOKEN);
		netService.setParams("order_id", orderIdStr);
		netService.setParams("amount", "1");
		netService.setHttpMethod(HttpSetting.GET_MODE);
		netService.setUrl(ConstantValue.PAY_SUCCESS_YIDAO);
		netService.loader(new NetLoadListener() {
			@Override
			public void onloaderListener(BackData result) {
				Message msg = new Message();
				msg.what = 5;
				if (result.getNetResultCode() == 200) {

				} else {
					msg.obj = result.getObject();
				}

				carhandler.sendMessage(msg);
			}
		});
	}

	/**
	 * 请求取消订单
	 */
	private void requestCancleOrder() {

		netService = NetService.getInStance();

		// netService.setParams("order_id", LApplication.yidaoOrderId);
		// netService.setParams("order_id", orderIdStr);
//		netService.setParams("order_id", orderIdStr);// token
		netService.setParams("access_token", ConstantValue.CAR_TOKEN);// token
		netService.setParams("reason_id", "100");// 58其它59没有信用卡无法验证,第三方合作100
		netService.setParams("other_reason", "信息有误");
		netService.setHttpMethod(HttpSetting.GET_MODE);
		netService.setUrl(ConstantValue.CANCLE_ORDER + orderIdStr);
		netService.loader(new NetLoadListener() {
			@Override
			public void onloaderListener(BackData result) {
				Message msg = new Message();
				String codeString = "";
				try {
					JSONObject cancleObj = new JSONObject((String) result
							.getObject());
					codeString = cancleObj.getString("code");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (codeString.equals("200")) {
					msg.what = 6;

				} else {
					msg.what = 7;
					msg.obj = result.getObject();
				}

				cancleHandler.sendMessage(msg);
			}
		});
	}

	/**
	 * 向我们服务器请求删除订单
	 */
	private void requestDeleteOrder() {

		Map<String, String> params = new HashMap<String, String>();
		params.put("id", orderIdStr); // 用车订单
		params.put("deleteFlag", "1"); // 用户标签？

		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.DELETE_ORDER, params, new GetDatas() {
					@SuppressWarnings("null")
					@Override
					public void getServerData(BackData data) {
						if (data == null) {
							// 说明网络获取无数据
							Toast.makeText(LApplication.context, "网络错误",
									Toast.LENGTH_SHORT).show();
						} else {
							Object obj = data.getObject();
							if (obj == null) {
								Toast.makeText(LApplication.context, "网络错误",
										Toast.LENGTH_SHORT).show();
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
								} else {
									// 调用方法，给listview加载数据，先将数据通过handler发送到主线程中
									Message msg = new Message();
									if (data.getNetResultCode() == 0) {
										msg.what = 8;
									} else {
										msg.what = 9;

									}
									msg.obj = backdata;
									cancleHandler.sendMessage(msg);
								}
							}

						}
					}
				}, false, false);

	}

	/**
	 * 获取订单状态
	 */
	private void requestOrderStatus() {

		netService = NetService.getInStance();

		// netService.setParams("order_id", LApplication.yidaoOrderId);
		// netService.setParams("order_id", orderIdStr);
		netService.setParams("access_token", ConstantValue.CAR_TOKEN);// token
		netService.setHttpMethod(HttpSetting.GET_MODE);
		netService.setUrl(ConstantValue.CANCLE_ORDER + orderIdStr);
		netService.loader(new NetLoadListener() {
			@Override
			public void onloaderListener(BackData result) {
				Message msg = new Message();
				if (result.getNetResultCode() == 0) {
					msg.what = 10;
					msg.obj = result.getObject();
				} else {
					msg.what = 11;
					msg.obj = result.getObject();
				}

				cancleHandler.sendMessage(msg);
			}
		});

	}

	/**
	 * Delete
	 * @param url 发送请求的URL
	 * @return 服务器响应字符串
	 * @throws Exception
	 */
	public  void deleteRequest()
		throws Exception
	{
		final HttpClient httpClient = new DefaultHttpClient();
		FutureTask<String> task = new FutureTask<String>(
		new Callable<String>()
		{
			@Override
			public String call() throws Exception
			{
				// 请求超时
				httpClient.getParams().setParameter(
						CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
				// 读取超时
				httpClient.getParams().setParameter(
						CoreConnectionPNames.SO_TIMEOUT, 5000);
				
			/*	netService.setParams("access_token", ConstantValue.CAR_TOKEN);// token
				netService.setParams("reason_id", "100");// 58其它59没有信用卡无法验证,第三方合作100
				netService.setParams("other_reason", "信息有误");
				*/
				
				// 创建HttpGet对象。
				HttpDelete delete = new HttpDelete(ConstantValue.CANCLE_ORDER + orderIdStr);
				delete.getParams().setParameter("access_token", ConstantValue.CAR_TOKEN);
				delete.getParams().setParameter("reason_id", "100");
				delete.getParams().setParameter("other_reason", "信息有误");
				// 发送GET请求
				HttpResponse httpResponse = httpClient.execute(delete);
				
				
				// 如果服务器成功地返回响应
				Log.d("dcc", "delete返回成功=="+httpResponse.getStatusLine()
						.getStatusCode()+"代码" +httpResponse.getEntity().toString());
				Message msg = new Message();
				String codeString = "";
				if (httpResponse.getStatusLine()
					.getStatusCode() == 204)
				{
					// 获取服务器响应字符串
					Log.d("dcc", "delete返回成功"+httpResponse.getEntity().toString());
					return "success";
				}else if(httpResponse.getStatusLine()
						.getStatusCode() == 200){
					Log.d("dcc", "delete返回成功"+httpResponse.getEntity().getContent().toString());
					msg.what = 6;
				}else{
					msg.what = 7;
				}
				cancleHandler.sendMessage(msg);
				return null;
			}
		});
		new Thread(task).start();
		Log.d("dcc", "delete返回成功代码"+task.get());
	}
	/**
	 * 处理获取到的司机的详细信息
	 * @param driverBackdata
	 */
	private static void useWayHandleDricerInfo(String driverBackdata) {
		// TODO Auto-generated method stub
		JSONObject orderObj;
		try {
			orderObj = new JSONObject(driverBackdata);
			if (driverBackdata.contains("name")) {
				JSONObject driverDeltealObj = orderObj
						.getJSONObject("result");
				driverDetailBean.setName(driverDeltealObj
						.getString("name"));
				driverDetailBean.setPhoto_url(driverDeltealObj
						.getString("photo_url"));
				driverDetailBean.setVehicle_number(driverDeltealObj
						.getString("vehicle_number"));
				driverDetailBean
						.setDriver_company_name(driverDeltealObj
								.getString("driver_company_name"));
				driverDetailBean.setStar_level(driverDeltealObj
						.getString("star_level"));
				driverDetailBean
						.setUnittime_complete_count(driverDeltealObj
								.getString("unittime_complete_count"));
				driverDetailBean.setCellphone(driverDeltealObj
						.getString("cellphone"));
				mDriverNameTv.setText(driverDetailBean.getName());
				mDriverNumberTv.setText(driverDetailBean
						.getVehicle_number()
						+ "　"
						+ driverDetailBean.getDriver_company_name());
				mDriverOrderTv.setText(driverDetailBean
						.getUnittime_complete_count() + "单");
				LApplication.loader.DisplayImage(
						driverDetailBean.getPhoto_url(),
						mDriverHeadImg, R.drawable.driver_defaulthead);
				mDriverLeverRt.setRating(Integer
						.parseInt(driverDetailBean.getStar_level()));
				// mDriverLeverRt.setNumStars(Integer.parseInt(driverDetailBean.getStar_level()));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	};
}
