package com.lesports.stadium.activity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.utils.UIHandler;

import com.lesports.stadium.R;
import com.lesports.stadium.adapter.NoStartActionAdapter;
import com.lesports.stadium.adapter.NoactionVideoAdapter;
import com.lesports.stadium.adapter.VideoAdapter;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.base.BaseActivity;
import com.lesports.stadium.bean.ActivityDetailBean;
import com.lesports.stadium.bean.AdressForTicks;
import com.lesports.stadium.bean.HeightLightBean;
import com.lesports.stadium.bean.PayBean;
import com.lesports.stadium.bean.PayParametric;
import com.lesports.stadium.bean.SenceBean;
import com.lesports.stadium.bean.ShareModel;
import com.lesports.stadium.bean.YuYueActivityBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.PayUtils;
import com.lesports.stadium.utils.WifiUtils;
import com.lesports.stadium.view.CustomDialog;
import com.lesports.stadium.view.MyGridView;
import com.lesports.stadium.view.MyScrollView;
import com.lesports.stadium.view.MyScrollViews;
import com.lesports.stadium.view.SharePopupWindow;
import com.lesports.stadium.view.MyScrollViews.OnScrollListener;
import com.letv.lepaysdk.ELePayState;
import com.letv.lepaysdk.LePayApi;
import com.letv.lepaysdk.LePayConfig;
import com.letv.lepaysdk.LePay.ILePayCallback;
import com.ylpw.sdk.R.string;
import com.ylpw.sdk.activity.SubmitOrderNewActivity.OrderLister;
import com.ylpw.sdk.model.ProductLeiShi;
import com.ylpw.sdk.util.YlpwUtil;

public class NoActionActivity extends Activity implements 
		OnClickListener, PlatformActionListener, Callback {

	/**
	 * 自定义scrollview，用来控制部分布局悬停
	 */
	private MyScrollViews mScrollview;
	/**
	 * 时间——天数
	 */
	private TextView mTimeDay;
	/**
	 * 时间——小时
	 */
	private TextView mTimeHour;
	/**
	 * 时间——分钟
	 */
	private TextView mTimeMinu;
	/**
	 * 预约人数
	 */
	private TextView mYuyueNum;
	/**
	 * 预约按钮
	 */
	private RelativeLayout mLayoutYuyue;
	/**
	 * 购票按钮
	 */
	private RelativeLayout mLayoutGoupiao;
	/**
	 * 预约的状态——心
	 */
	private ImageView mImageYuyue;
	/**
	 * 预约标题
	 */
	private TextView mYuyueTitle;
	/**
	 * 副标题
	 */
	private TextView mYuyueFuBiaoti;
	/**
	 * 点击后需要展开的按钮
	 */
	private ImageView mImageMore;
	/**
	 * 活动id
	 */
	private SenceBean mActivityBean;
	/**
	 * 背景image <ImageView android:id="@+id/new_nocation_background"
	 * android:layout_width="match_parent" android:layout_height="match_parent"
	 * android:scaleType="centerCrop"
	 * android:src="@drawable/weikaishibeijing_zhanwei" />
	 */
	/**
	 * 用来刷新时间
	 */
	private Timer refreshDiscussTimer = new Timer();
	/**
	 * 用户预约列表数据集合
	 */
	public List<YuYueActivityBean> mYuyueList = new ArrayList<>();
	private CustomDialog exitDialog;
	
	private CustomDialog exitDialogs;
	/**
	 * 用来存储解析地址集合
	 */
	private ArrayList<AdressForTicks> adresslist;
	/**
	 * 购票productId
	 */
	private String productId;
	/**
	 * 永乐购票生成的订单号
	 */
	private String orderNumber;
	// 通知永乐 购票成功 参数 永乐订单ID
	private String ylOrderId;
	// 通知永乐 购票成功 参数 购票交易号
	private String ylPayNo;
	// 通知永乐 购票成功 参数 购票金额
	private String ylTotalPrice;
	/**
	 * 定义标记，该标记用来标注当前用户是否已经报名
	 */
	private int IS_BAO = 0;
	/**
	 * 顶部table
	 */
	private RelativeLayout mLayoutTop;
	/**
	 * 顶部返回按钮
	 */
	private ImageView mBack;
	/**
	 * 顶部分享按钮
	 */
	private ImageView mShared;
	/**
	 * 分享链接是需要携带的图片地址
	 */
	private String url;
	/*
	 * 分享内容
	 */
	private String content;
	/**
	 * 分享标题
	 */
	private String shardTitle;
	private SharePopupWindow share;
	/**
	 * 总布局
	 */
	private RelativeLayout mLayoutRoot;
	/**
	 * 用于标记第一次获取的全部数据
	 */
	private final int ALL_TAG = 1;
	/**
	 * 成功获取地址
	 */
	private final int SUCCESS_ADRESS = 100;
	/**
	 * 获取网络数据失败
	 */
	private final int FILUE_NET_DATE = 1001;
	/**
	 * 永乐购票订单成功
	 */
	private final int SUCCESS_ORDER = 1111;
	/**
	 * 用来标记网络获取的数据为空
	 */
	private final int TAG_DATANULL = 0;
	/**
	 * 购票支付成功
	 */
	private final int PAY_SUCCESS = 2222;
	/**
	 * 成功获取通知永乐的数据
	 */
	private final int NOTIFY_SUCCESS = 666666;
	/**
	 * 通知永乐失败
	 */
	private final int NOTIFY_FILLER = 55555;

	/**
	 * 用于标记用户进行预约参加活动
	 */
	private final int ADD_TAG = 3;
	/**
	 * 用来标记网络获取的数据
	 */
	private final int TAG_DATA = 5;
	/**
	 * 用于标记刷新界面的时候获取的时间数据
	 */
	private final int TIME_TAG = 2;
	/**
	 * 
	 */
	private MyScrollViews myScrollView;
	/**
	 * 
	 */
	private LinearLayout mBuyLayout;
	/**
	 */
	private ActivityDetailBean mBean;
	private LinearLayout mTopBuyLayout;
	/**
	 * 取消预约的bid
	 */
	private String bid;
	/**
	 * 取消预约成功
	 */
	private final int CANCU_ACTIVE_SUCCESS = 15;
	/**
	 * 背景图片
	 */
	private ImageView mImageBackground;
	/**
	 * 标题布局
	 */
	private RelativeLayout mLayout_addmore;
	/**
	 * 取消预约失败
	 */
	private final int CANCU_ACTIVE_FILUE = 16;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case ALL_TAG:
				String backdata = (String) msg.obj;
				Log.i("活动详情获取的数据是", backdata);
				mBean = jsonParserData(backdata);
				// 调用方法，适配数据
				addDataToUI(mBean);
				break;
			case TIME_TAG:
				String backdatatime = (String) msg.obj;
				initDate(backdatatime);
				break;
			case ADD_TAG:
				String addbackadata = (String) msg.obj;
				if (addbackadata != null && !TextUtils.isEmpty(addbackadata)) {
					List<YuYueActivityBean> lists = jsonway(addbackadata);
					if (lists != null && lists.size() != 0) {
						// 说明报名成功
						mImageYuyue
								.setImageResource(R.drawable.huodong_yijingbaoming);
						// 修改数字
						String num = (String) mYuyueNum.getText();
						if (TextUtils.isEmpty(num))
							num = "0";
						int nums = Integer.parseInt(num);
						nums = nums + 1;
						mYuyueNum.setText(nums + "");
						mYuyueNum.setClickable(false);
						IS_BAO = 1;
						if (mYuyueList != null) {
							mYuyueList.addAll(lists);
						}
						createDialog_canlande();
					} else {
						Toast.makeText(getApplicationContext(),
								"对不起，该活动参加的人数已满", 1).show();
					}
				}
				break;
			case 1011:
				Toast.makeText(getApplicationContext(), "预约失败",
						Toast.LENGTH_SHORT).show();
				break;
			case TAG_DATA:
				String jijindata = (String) msg.obj;
				if (jijindata != null && !TextUtils.isEmpty(jijindata)) {
					// 调用方法解析数据
					jsondatajijing(jijindata);
				}
				break;
			case SUCCESS_ADRESS:
				// 解析从网络获取的地址数据
				analyzeAdressDate((String) msg.obj);

				break;
			case FILUE_NET_DATE:
				// 获取网络数据失败
				Toast.makeText(getApplicationContext(), "服务繁忙，请稍候重试", 0).show();
				break;
			case SUCCESS_ORDER:

				String jsondatass = (String) msg.obj;
				if (!TextUtils.isEmpty(jsondatass)) {
					Log.i("支付参数", jsondatass);
					PayBean paybean = useWayJsonDataPay(jsondatass);
					if (paybean != null
							&& !TextUtils.isEmpty(paybean.getSign())) {
						// 开始调用方法，进行支付
						pay(paybean);
					}
				}

				break;
			case PAY_SUCCESS:
				// Toast.makeText(getApplicationContext(), "支付成功", 0).show();
				Intent intent = new Intent(NoActionActivity.this,
						TickOrderActivity.class);
				startActivity(intent);
				break;
			case NOTIFY_SUCCESS:
				annliysYLe((String) msg.obj);
				break;
			case NOTIFY_FILLER:
				notifyYLe();
				break;
			case 99:
				// 第一次获取预约列表下来没有数据的情况处理
				break;
			case 10:
				// 这里是第一次处理获取下来的预约列表数据
				String bacadatas = (String) msg.obj;
				if (!TextUtils.isEmpty(bacadatas)) {
					// 解析该数据
					mYuyueList = jsonway(bacadatas);
					if (mYuyueList != null && mYuyueList.size() != 0) {
						useWayCheckData();
					}
				}
				break;
			case CANCU_ACTIVE_SUCCESS:
				analyseCancelActive((String) msg.obj);
				break;
			case CANCU_ACTIVE_FILUE:
				Toast.makeText(NoActionActivity.this, "取消失败", 0).show();
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_nocation_activity);
		initview();
		initGet();
		if (mActivityBean != null) {
			userUtilsGetDataSenceBean(mActivityBean.getId());
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 调用方法，来判断用户是否预约该活动
		// 先判断用户是否登陆，如果用户已经登陆，则在这里获取用户预约列表，来判断该用户是否预约该活动
		if (!TextUtils.isEmpty(GlobalParams.USER_ID)) {
			// 已经登陆状态
			huoquyonghuyuyueliebiao_intent();
		}
	}


	/**
	 * 获取上一个界面传递过来的数据
	 */
	private void initGet() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		mActivityBean = (SenceBean) intent.getSerializableExtra("bean");
		refrenshUI();
	}

	/**
	 * 该方法用来每隔一分钟刷新一次界面
	 */
	private void refrenshUI() {
		// TODO Auto-generated method stub
		refreshDiscussTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				// 调用方法，重新获取界面数据，拿出时间数据，进行重新适配加载到UI
				reGetTimeData();
			}
		}, 60000, 60000);
	}

	/**
	 * 重新获取时间数据，进行计时操作
	 */
	private void reGetTimeData() {
		// TODO Auto-generated method stub
		// userUtilsGetDataxstime(id);
		Message msg = new Message();
		msg.arg1 = TIME_TAG;
		msg.obj = mActivityBean.getStarttime();
		if (handler != null)
			handler.sendMessage(msg);

	}

	/**
	 * 初始化view
	 */
	private void initview() {
		// TODO Auto-generated method stub
		mLayout_addmore=(RelativeLayout) findViewById(R.id.layout_new_nocation_data_s);
		mLayout_addmore.setOnClickListener(this);
		mImageBackground = (ImageView) findViewById(R.id.new_action_image_bg);
		mScrollview = (MyScrollViews) findViewById(R.id.new_action_scrollview);
		mTimeDay = (TextView) findViewById(R.id.new_nocation_day_num);
		mTimeHour = (TextView) findViewById(R.id.new_nocation_hour_num);
		mTimeMinu = (TextView) findViewById(R.id.new_nocation_minut_num);
		mYuyueNum = (TextView) findViewById(R.id.new_nocation_tv_canjiarenshu);
		mLayoutYuyue = (RelativeLayout) findViewById(R.id.new_action_layout_yuyue);
		mLayoutYuyue.setOnClickListener(this);
		mLayoutGoupiao = (RelativeLayout) findViewById(R.id.new_action_layout_goupiao);
		mLayoutGoupiao.setOnClickListener(this);
		mImageYuyue = (ImageView) findViewById(R.id.new_nocation_minut_num_sssss_yuyue);
		mYuyueTitle = (TextView) findViewById(R.id.new_nocation_tv_title);
		mYuyueFuBiaoti = (TextView) findViewById(R.id.new_nocation_tv_title_xiao);
		mLayoutTop = (RelativeLayout) findViewById(R.id.new_noaction_top);
		mBack = (ImageView) findViewById(R.id.new_noaction_detail_top_back);
		mBack.setOnClickListener(this);
		mShared = (ImageView) findViewById(R.id.new_noaction_detail_top_share);
		mShared.setOnClickListener(this);
		mLayoutRoot = (RelativeLayout) findViewById(R.id.new_no_mian);
		mLayoutRoot.setFocusableInTouchMode(true);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.layout_new_nocation_data_s:
			// 点击的时候，需要将内容简介显示出来，并且将该按钮隐藏
			Intent intent=new Intent(NoActionActivity.this,NoActionActivityTWO.class);
			intent.putExtra("bean",mActivityBean);
			startActivity(intent);
			NoActionActivity.this.overridePendingTransition(R.anim.activity_down_to_up,0);
			break;
		case R.id.new_action_layout_yuyue:
			// 调用方法进行预约
			// 先判断是要报名还是取消报名
			Log.i("现在标记是多少", IS_BAO + "");
			if (IS_BAO == 0) {
				// 说明是需要报名
				if (!TextUtils.isEmpty(GlobalParams.USER_ID)) {
					// 调用方法，进行报名处理
					userUtilsGetDataxsbaoming(GlobalParams.USER_ID);
				} else {
					createDialog();
				}
			} else if (IS_BAO == 1) {
				// 说明需要取消
				useWayCancleYuyue();
			}
			break;
		case R.id.new_action_layout_goupiao:
			// 调用方法进行购票
			// 点击该按钮，进行买票
			if (TextUtils.isEmpty(GlobalParams.USER_ID)) {
				createDialog();
				return;
			}
			getReceiptAddress();
			break;
		case R.id.new_noaction_detail_top_back:
			finish();
			break;
		case R.id.new_noaction_detail_top_share:
			showPop();
			break;

		default:
			break;
		}
	}

	/**
	 * 取消预约
	 */
	private void useWayCancleYuyue() {
		// TODO Auto-generated method stub
		// 调用方法，进行报名
		// 从预约列表里面，得到该活动id对应的bid
		if (mYuyueList != null && mYuyueList.size() != 0) {
			for (int i = 0; i < mYuyueList.size(); i++) {
				if (mYuyueList.get(i).getActivityId()
						.equals(mActivityBean.getId())) {
					bid = mYuyueList.get(i).getBid();
					userUtilsGetDataquxiao(mYuyueList.get(i).getBid());
				}
			}
		}
	}

	/**
	 * @param mListview2
	 * @2016-2-18下午2:24:29 调用工具类，从网络获取服务器数据
	 */
	private void userUtilsGetDataSenceBean(String str) {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", str);
		params.put("type", "0");
		Log.i("活动详情id是", str);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ACTIVITY_DETAIL_NOACTION, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						if (data == null) {
							// 说明网络获取无数据
						} else {
							if (data.getNetResultCode() == 0) {
								// 调用方法，给listview加载数据，先将数据通过handler发送到主线程中
								Object obj = data.getObject();
								String backdata = obj.toString();
								Message msg = new Message();
								msg.arg1 = ALL_TAG;
								msg.obj = backdata;
								handler.sendMessage(msg);
							}
						}
					}
				}, false, false);
	}

	/**
	 * 用来解析该界面从网络获取到的数据
	 * 
	 * @2016-2-18下午4:45:18
	 */
	private ActivityDetailBean jsonParserData(String jsonstring) {
		ActivityDetailBean bean = new ActivityDetailBean();
		try {

			JSONObject objs = new JSONObject(jsonstring);
			if (objs.has("camptype")) {
				bean.setCamptype(objs.getString("camptype"));
			}
			if (objs.has("cHeat")) {
				bean.setcHeat(objs.getString("cHeat"));
			}
			if (objs.has("id")) {
				bean.setId(objs.getString("id"));
			}
			if (objs.has("creattime")) {
				bean.setCreattime(objs.getString("creattime"));
			}
			if (objs.has("cStatus")) {
				bean.setcStatus(objs.getString("cStatus"));
			}
			if (objs.has("endtime")) {
				bean.setEndtime(objs.getString("endtime"));
			}
			if (objs.has("fileId")) {
				bean.setFileId(objs.getString("fileId"));
			}
			if (objs.has("title")) {
				bean.setmTitle(objs.getString("title"));
			}
			if (objs.has("selltimeend")) {
				bean.setSelltimeend(objs.getString("selltimeend"));
			}
			if (objs.has("starttime")) {
				bean.setStarttime(objs.getString("starttime"));
			}
			if (objs.has("tips")) {
				bean.setTips(objs.getString("tips"));
			}
			if (objs.has("venueId")) {
				bean.setVenueId(objs.getString("venueId"));
			}
			if (objs.has("label")) {
				bean.setLabel(objs.getString("label"));
			}
			if (objs.has("summary")) {
				bean.setSummary(objs.getString("summary"));
			}
			if (objs.has("fileUrl")) {
				bean.setFileUrl(objs.getString("fileUrl"));
			}
			if (objs.has("productId")) {
				productId = objs.getString("productId");
				bean.setProductId(productId);
			}
			if (objs.has("roomId")) {
				bean.setRoomId(objs.getString("roomId"));
			}
			if (objs.has("subhead")) {
				bean.setSubhead(objs.getString("subhead"));
			}
			if (objs.has("venueName")) {
				bean.setVenueName(objs.getString("venueName"));
			}
			if (objs.has("backgroudImageURL")) {
				bean.setBackgroudImageURL(objs.getString("backgroudImageURL"));
			}
			return bean;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bean;

	}

	/**
	 * 该方法用来将数据加载到控件上
	 * 
	 * @param bean
	 */
	private void addDataToUI(ActivityDetailBean bean) {
		/**
		 * 这里还需要一步，查询该用户是否预约了该活动
		 */
		mYuyueNum.setText(mActivityBean.getcHeat());
		mYuyueTitle.setText(mActivityBean.getSubhead());
		url = ConstantValue.BASE_IMAGE_URL + bean.getBackgroudImageURL()
				+ ConstantValue.IMAGE_END;
		LApplication.loader.DisplayImage(
				ConstantValue.BASE_IMAGE_URL + bean.getBackgroudImageURL()
						+ ConstantValue.IMAGE_END, mImageBackground);
		useWayCountTime(mYuyueFuBiaoti, mActivityBean);
		content = bean.getSummary();
		shardTitle = bean.getTitle();
		// 调用方法，计算当前时间与开始时间之间的间隔值，然后进行设置
		if (!TextUtils.isEmpty(mActivityBean.getStarttime())) {
			initDate(mActivityBean.getStarttime());
		}
		getdataFromServiceheightjijin();

	}
	/**
	 * 创建方法用来计算开始时间和结束时间
	 * 
	 * @param mTimeEnd
	 * @param senceBean
	 */
	private void useWayCountTime(TextView mTimeEnd, SenceBean senceBean) {
		// TODO Auto-generated method stub
		String starttime = getChangeToTimess(senceBean.getStarttime());
		String endtime = getChangeToTimess(senceBean.getEndtime());
		if (!TextUtils.isEmpty(starttime) && !TextUtils.isEmpty(endtime)) {
			String startmonth = getMonthAndDay(starttime);
			String starthour = getHourAndMinute(starttime);
			String endhour = getHourAndMinute(endtime);
			String time = startmonth + "  " + starthour + "-" + endhour;
			mTimeEnd.setText(time+"　|　"+senceBean.getVenueName());
		}
	}

	/**
	 * 将一个传入的字符串毫秒值转换成一个字符串时间值
	 * 
	 * @param str
	 * @return
	 */
	public String getChangeToTimess(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss",
				Locale.getDefault());
		long time = Long.parseLong(str);
		Date date = new Date();
		date.setTime(time);
		String timestring = sdf.format(date);

		return timestring;

	}

	/**
	 * 根据传入的字符串时间值，截取当前月日时间字符串
	 * 
	 * @param str
	 * @return
	 */
	public String getMonthAndDay(String str) {
		String md = str.substring(5, 10);
		return md;

	}

	/**
	 * 根据传入的时间值，截取小时和分钟数
	 * 
	 * @param str
	 * @return
	 */
	public String getHourAndMinute(String str) {
		String hm = str.substring(11, 16);
		return hm;
	}


	/**
	 * 将传递进来的开始时间与当前时间进行时间差计算
	 * 
	 * @param starttime
	 */
	private void initDate(String starttime) {
		// TODO Auto-generated method stub
		long starttimes = getChangeToTime(starttime);
		Date date = new Date();
		long currenttimes = date.getTime();
		Log.i("当前时间", currenttimes + "");
		Log.i("开始时间", starttimes + "");

		// 调用方法，计算两个时间之间差值
		calculationTime(starttimes, currenttimes);
	}

	/**
	 * 将时间毫秒字符串转换成long类型的整数
	 * 
	 * @param str
	 * @return
	 */
	public long getChangeToTime(String str) {
		if (TextUtils.isEmpty(str))
			return 0;
		long time = Long.parseLong(str);
		return time;

	}

	/**
	 * 计算时间差值
	 * 
	 * @param starttimes
	 * @param currenttimes
	 */
	private void calculationTime(long starttimes, long currenttimes) {
		// TODO Auto-generated method stub
		// SimpleDateFormat dfs = new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		long between = 0;
		try {
			// java.util.Date begin = dfs.parse("2009-07-10 10:22:21.214");
			// java.util.Date end = dfs.parse("2009-07-20 11:24:49.145");
			// between = (end.getTime() - begin.getTime());// 得到两者的毫秒数
			between = (starttimes - currenttimes);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		long day = between / (24 * 60 * 60 * 1000);
		long hour = (between / (60 * 60 * 1000) - day * 24);
		long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		long ms = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
				- min * 60 * 1000 - s * 1000);
		Log.i("当前计算后时间是", day + "天" + hour + "小时" + min + "分" + s + "秒" + ms
				+ "毫秒");
		// System.out.println(day + "天" + hour + "小时" + min + "分" + s + "秒" + ms
		// + "毫秒");
		/**
		 * 设置时间到控件上
		 */
		mTimeDay.setText(day + "");
		mTimeHour.setText(hour + "");
		mTimeMinu.setText(min + "");
		// if(between<=0){
		// Intent intent = new Intent(NoStartActivity.this,
		// LiveDetialActivity.class);
		// intent.putExtra("id", bean.getId());
		// intent.putExtra("no", "1");
		// intent.putExtra("lable", bean.getLabel());
		// intent.putExtra("url", bean.getFileUrl());
		// //直播源id 用于直播
		// intent.putExtra("resourceId", bean.getResourceId());
		// startActivity(intent);
		// NoStartActivity.this.finish();
		// }
	}

	/**
	 * 调用方法，从网络获集锦数据
	 * 
	 * @param context
	 */
	private void getdataFromServiceheightjijin() {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityId", mActivityBean.getId());
		params.put("fileClass", "0");
		Log.i("集锦获取数据", "走到这里了么？");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.HIGHT_LEIGHT, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						if (handler == null)
							return;
						if (data == null) {
							// 说明网络获取无数据
							Message msg = new Message();
							msg.arg1 = TAG_DATANULL;
							handler.sendMessage(msg);
						} else {
							Object obj = data.getObject();
							if (obj == null) {
								// 说明数据为空
								Message msg = new Message();
								msg.arg1 = TAG_DATANULL;
								handler.sendMessage(msg);
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
									Message msg = new Message();
									msg.arg1 = TAG_DATANULL;
									handler.sendMessage(msg);
								} else {
									Log.i("集锦获取下来的数据",
											"走到这里了么？" + data.getNetResultCode());
									Log.i("集锦获取下来的数据", backdata);
									Message msg = new Message();
									msg.arg1 = TAG_DATA;
									msg.obj = backdata;
									handler.sendMessage(msg);
								}
							}
						}
					}

				}, false, false);
	}

	/**
	 * 用来解析集锦数据
	 * 
	 * @param backdata
	 */

	private void jsondatajijing(String backdata) {
		List<HeightLightBean> list = new ArrayList<HeightLightBean>();
		try {
			JSONArray array = new JSONArray(backdata);
			int count = array.length();
			for (int i = 0; i < count; i++) {
				JSONObject obj = array.getJSONObject(i);
				HeightLightBean bean = new HeightLightBean();
				if (obj.has("activityId")) {
					bean.setActivityId(obj.getString("activityId"));
				}
				if (obj.has("fileName")) {
					bean.setFileName(obj.getString("fileName"));
				}
				if (obj.has("fileProfile")) {
					bean.setFileProfile(obj.getString("fileProfile"));
				}
				if (obj.has("fileSource")) {
					bean.setFileSource(obj.getString("fileSource"));
				}
				if (obj.has("fileType")) {
					bean.setFileType(obj.getString("fileType"));
				}
				if (obj.has("fileUrl")) {
					bean.setFileUrl(obj.getString("fileUrl"));
				}
				if (obj.has("id")) {
					bean.setId(obj.getString("id"));
				}
				if (obj.has("mImageUrl")) {
					bean.setmImageUrl(obj.getString("mImageUrl"));
				}
				if (obj.has("mTitle")) {
					bean.setmTitle(obj.getString("mTitle"));
				}
				if (obj.has("resourceId")) {
					bean.setResourceId(obj.getString("resourceId"));
				}
				if (obj.has("videoImageUrl")) {
					bean.setVideoImageUrl(obj.getString("videoImageUrl"));
				}
				if (obj.has("supportFullView")) {
					bean.setSupportFullView(obj.getString("supportFullView"));
				}
				if (obj.has("uploadDate")) {
					bean.setUploadDate(obj.getString("uploadDate"));
				}
				if ("0".equals(bean.getFileType())
						|| "1".equals(bean.getFileType()))
					list.add(bean);
			}
			// 调用方法，将数据集添加到适配器中
			if (list.size() != 0) {
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * @param mListview2
	 * @2016-2-18下午2:24:29 huoqu bao ming liebiao
	 */
	private void huoquyonghuyuyueliebiao_intent() {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("detailed", "");
		params.put("finished", "");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.YONGHUYUYUE_LIST, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						if (handler == null)
							return;
						Log.i("wxn", "预约列表:" + data.getNetResultCode() + ".."
								+ data.getObject());
						if (data != null && data.getNetResultCode() == 0) {
							Object obj = data.getObject();
							String backdata = obj.toString();
							Message msg = new Message();
							msg.arg1 = 10;
							msg.obj = backdata;
							handler.sendMessage(msg);
						} else {
							Message msg = new Message();
							msg.arg1 = 9;
							handler.sendMessage(msg);
						}
					}
				}, false, false);

	}

	/**
	 * 处理活动列表数据
	 * 
	 * @param backdataid
	 */
	private List<YuYueActivityBean> jsonway(String backdataid) {
		// TODO Auto-generated method stub
		List<YuYueActivityBean> list = new ArrayList<YuYueActivityBean>();
		try {
			JSONArray array = new JSONArray(backdataid);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				YuYueActivityBean bean = new YuYueActivityBean();
				if (obj.has("activityId")) {
					bean.setActivityId(obj.getString("activityId"));
				}
				if (obj.has("bid")) {
					bean.setBid(obj.getString("bid"));
				}
				if (obj.has("bStatus")) {
					bean.setbStatus(obj.getString("bStatus"));
				}
				if (obj.has("userId")) {
					bean.setUserId(obj.getString("userId"));
				}
				list.add(bean);

			}
			return list;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;

	};

	/**
	 * 用来检测该用户是否预约该活动
	 */
	private void useWayCheckData() {
		// TODO Auto-generated method stub
		boolean ishave = false;
		if (mYuyueList != null && mYuyueList.size() != 0
				&& mActivityBean != null) {
			for (int i = 0; i < mYuyueList.size(); i++) {
				if (mYuyueList.get(i).getActivityId()
						.equals(mActivityBean.getId())) {
					ishave = true;
				}
			}
			// 判断处理
			if (ishave) {
				// 说明预约了
				IS_BAO = 1;
				mImageYuyue.setImageResource(R.drawable.huodong_yijingbaoming);
			} else {
				// 说明没有取消
			}

		}
	}

	/**
	 * 创建未登陆提示框
	 */
	private void createDialog() {
		if (exitDialog == null) {
			exitDialog = new CustomDialog(NoActionActivity.this,
					new OnClickListener() {
						@Override
						public void onClick(View v) {

							Intent intent = new Intent(NoActionActivity.this,
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
		exitDialog.show();
	}

	// 获取收货地址
	private void getReceiptAddress() {
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.GET_ALL_ADDRESS, null, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if (handler == null)
							return;
						if (data != null) {
							Object obj = data.getObject();
							// Log.i("wxn", "获取地址。。。"+obj);
							if (obj != null) {
								Message msg = new Message();
								msg.obj = obj;
								msg.arg1 = SUCCESS_ADRESS;
								handler.sendMessage(msg);
								return;
							}
						}
						Message msg = new Message();
						msg.arg1 = FILUE_NET_DATE;
						handler.sendMessage(msg);
					}
				}, false, false);
	}

	/**
	 * 购票地址获取成功
	 * 
	 * @param adressJson
	 */
	private void analyzeAdressDate(String adressJson) {
		try {
			adresslist = new ArrayList<>();
			JSONArray listArray = new JSONArray(adressJson);
			for (int i = 0; i < listArray.length(); i++) {
				AdressForTicks bean = new AdressForTicks();
				JSONObject aObj = listArray.getJSONObject(i);

				if (aObj.has("address")) {
					bean.setAddressName(aObj.getString("address"));
				}
				if (aObj.has("province")) {
					String defaultar = aObj.getString("province");
					JSONObject defaultarArry = new JSONObject(defaultar);
					if (defaultarArry.has("name")) {
						String mergName = defaultarArry.getString("name");
						if (!mergName.endsWith("省") && !mergName.endsWith("市")
								&& !mergName.endsWith("区"))
							bean.setAllCityDetail(mergName + "市|");
						else
							bean.setAllCityDetail(mergName + "|");
					}
				}
				if (aObj.has("city")) {
					String defaultar = aObj.getString("city");
					JSONObject defaultarArry = new JSONObject(defaultar);
					if (defaultarArry.has("name")) {
						String mergName = defaultarArry.getString("name");
						bean.setAllCityDetail(bean.getAllCityDetail()
								+ mergName + "|");
					}
				}
				if (aObj.has("area")) {
					String defaultar = aObj.getString("area");
					JSONObject defaultarArry = new JSONObject(defaultar);
					if (defaultarArry.has("name")) {
						String mergName = defaultarArry.getString("name");
						bean.setAllCityDetail(bean.getAllCityDetail()
								+ mergName);
					}
				}
				if (aObj.has("isDefault")) {
					String defaultar = aObj.getString("isDefault");
					if ("1".equals(defaultar)) {
						bean.setDefalut(true);
					} else {
						bean.setDefalut(false);
					}
				}
				if (aObj.has("mobilePhone")) {
					bean.setUserPhoneNumber(aObj.getString("mobilePhone"));
				}
				if (aObj.has("name")) {
					bean.setUserName(aObj.getString("name"));
				}
				adresslist.add(bean);
			}
			toBuyTickets(adresslist);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	// 购票
	private void toBuyTickets(ArrayList<AdressForTicks> lists) {

		// String adressString =
		// "[{\"addressName\": \"地址名称1\",\"userName\": \"王志军1\",\"userPhoneNumber\": \"18310104620\",\"allCityDetail\": \"北京市|北京市|东城区\",\"isDefalut\": false},{\"addressName\": \"地址名称2\",\"userName\": \"王志军2\",\"userPhoneNumber\": \"18310104620\",\"allCityDetail\": \"北京市|北京市|东城区\",\"isDefalut\": false},{\"addressName\": \"地址名称3\",\"userName\": \"王志军3\",\"userPhoneNumber\": \"18310104620\",\"allCityDetail\": \"北京市|北京市|西城区\",\"isDefalut\":true}]";
		JSONArray jsonarray = new JSONArray();
		try {
			for (AdressForTicks bean : lists) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("addressName", bean.getAddressName());
				// userName = bean.getUserName();
				jsonObj.put("userName", bean.getUserName());
				jsonObj.put("userPhoneNumber", bean.getUserPhoneNumber());
				jsonObj.put("allCityDetail", bean.getAllCityDetail());
				jsonObj.put("isDefalut", bean.isDefalut());
				jsonarray.put(jsonObj);
			}
		} catch (Exception e) {
			Log.i("wxn", "异常1：" + e.getMessage());
		}
		try {
			String adressString = jsonarray.toString();
			Log.i("wxn", productId + "...购票：...." + adressString);
			ProductLeiShi productLeiShi = new ProductLeiShi();
			if (TextUtils.isEmpty(adressString) || "[]".equals(adressString))
				adressString = "";
			Log.i("wxn", productId + "...购票1：...." + adressString);
			productLeiShi.setCustomerAddress(adressString);
			productLeiShi.setCustomerName(GlobalParams.USER_NAME);
			productLeiShi.setLmCustomersOpenID(GlobalParams.USER_ID);
			// productLeiShi.setCustomerName("aaa");
			// productLeiShi.setLmCustomersOpenID("415465");
			if (TextUtils.isEmpty(productId) || "null".equals(productId)) {
				Toast.makeText(getApplicationContext(), "该演出暂未开始售票，敬请关注", Toast.LENGTH_SHORT).show();
				return;
			}
			productLeiShi.setProductId(productId);
			productLeiShi.setLmType("leshi");

			YlpwUtil ylpwUtil = new YlpwUtil(NoActionActivity.this,
					productLeiShi);
			Log.i("wxn", "异常...：");
			ylpwUtil.setONOrderLister(new OrderLister() {

				@Override
				public void orderSucess(String arg0) {
					Log.i("wxn", "orderNumber: ..." + arg0);
					orderNumber = arg0;
					createTicksOrder(arg0);
				}
			});
		} catch (Exception e) {
			Log.i("wxn", "异常：" + e.getMessage());
			Toast.makeText(getApplicationContext(), "服务繁忙，请稍候重试", 0).show();
		}
	}

	// 生成购票订单
	private void createTicksOrder(String orderNumber) {
		Map<String, String> params = new HashMap<>();
		params.put("currency", "CNY");// 默认值
		params.put("pay_expire", "45");// 默认值
		WifiUtils wifiUtils = new WifiUtils(this);
		params.put("ip", wifiUtils.getIpAddress());// 当前网络ip
		params.put("orderNumber", orderNumber);// 永乐提供的订单号
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.TICKEORDER, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						Log.i("wxn",
								"购票生成结果...." + data.getObject() + " result:"
										+ data.getNetResultCode());
						if (handler == null)
							return;
						if (data != null) {
							Object obj = data.getObject();
							Log.i("wxn",
									"购票生成结果...." + obj + " result:"
											+ data.getNetResultCode());
							if (obj != null && 0 == data.getNetResultCode()) {
								Message msg = new Message();
								msg.obj = obj;
								msg.arg1 = SUCCESS_ORDER;
								handler.sendMessage(msg);
								Log.i("wxn", "购票生成结果...." + obj);
								return;
							}
						}
						Message msg = new Message();
						msg.arg1 = FILUE_NET_DATE;
						handler.sendMessage(msg);
					}
				}, false, false);
	}

	/**
	 * 解析获取下来的真正的支付参数
	 * 
	 * @param jsondatass
	 */
	private PayBean useWayJsonDataPay(String jsondatass) {
		// TODO Auto-generated method stub
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
				return bean;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bean;
	}

	private void pay(PayBean paybean) {
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
			ylTotalPrice = paybean.getPrice();
			parameters.setPrice(ylTotalPrice);
			ylPayNo = paybean.getMerchant_business_id();
			ylOrderId = paybean.getOut_trade_no();
			parameters.setCurrency(paybean.getCurrency());
			parameters.setPay_expire(paybean.getPay_expire());
			parameters.setProduct_id(paybean.getProduct_id());
			String product_name = paybean.getProduct_name();
			if (TextUtils.isEmpty(product_name))
				product_name = "购票";
			parameters.setProduct_name(product_name);
			String miaoshu = paybean.getProduct_desc();
			if (TextUtils.isEmpty(miaoshu))
				miaoshu = "购票";
			parameters.setProduct_desc(miaoshu);
			String pro_url = paybean.getProduct_urls();
			if (TextUtils.isEmpty(pro_url))
				pro_url = "https://www.baidu.com/";
			parameters.setProduct_urls(pro_url);
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
			LePayApi.initConfig(NoActionActivity.this, config);
			LePayApi.doPay(NoActionActivity.this, param, new ILePayCallback() {
				@Override
				public void payResult(ELePayState status, String message) {
					Log.i("wxn", "支付结果：" + status + "  message :" + message);
					if (ELePayState.CANCEL == status) { // 支付取消

					} else if (ELePayState.FAILT == status) { // 支付失败

					} else if (ELePayState.OK == status) { // 支付成功
						notifyYLe(); // 通知永乐 服务器，已经支付
						Log.i("wxn", "支付成功了么");
						Intent intent = new Intent(NoActionActivity.this,
								TickOrderActivity.class);
						intent.putExtra("orderNumber", orderNumber);
						startActivity(intent);
					} else if (ELePayState.WAITTING == status) { // 支付中

					}
				}
			});
		} else {
			Toast.makeText(NoActionActivity.this, "支付异常", Toast.LENGTH_SHORT)
					.show();
		}

	}

	// 购票成功后通知永乐
	private void notifyYLe() {
		final Map<String, String> params = new HashMap<String, String>();
		params.put("orderId", ylOrderId); // 永乐订单ID
		params.put("totalPrice", ylTotalPrice); // 支付金额
		params.put("customerId", GlobalParams.USER_ID); // 乐视用户ID
		params.put("payno", ylPayNo); // 交易号
		params.put("paySource", "ANDROID"); // 交易源
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.BY_TICKOUS_SUCCESS, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if (handler == null)
							return;
						if (data != null) {
							// 解析返回来的数据
							Message msg = new Message();
							msg.what = NOTIFY_SUCCESS;
							msg.obj = data.getObject();
							Log.i("wxn", "notify yongle obj:..." + msg.obj);
							handler.sendMessage(msg);
							return;
						}
						handler.sendEmptyMessage(NOTIFY_FILLER);
					}
				}, false, false);
	}

	// 解析通知永乐返回的数据
	private void annliysYLe(String data) {
		try {
			JSONObject obj = new JSONObject(data);
			if (obj.has("result")) {
				String result = obj.getString("result");
				JSONObject resuObj = new JSONObject(result);
				if (resuObj.has("code")) {
					String code = resuObj.getString("code");
					if (!"0".equals(code)) {
						notifyYLe();
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param mListview2
	 * @2016-2-18下午2:24:29 调用工具类，从网络获取服务器数据
	 */
	private void userUtilsGetDataxsbaoming(String str) {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityId", mActivityBean.getId());
		params.put("userId", str);
		params.put("bStatus", "1");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ADD_ACTIVITY, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						if (handler == null)
							return;
						if (data == null) {
							// 说明网络获取无数据
						} else {
							Object obj = data.getObject();
							if (obj == null) {

							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {

								} else {
									Log.i("活动未开始报名",
											"走到这里了么？" + data.getNetResultCode());
									Log.i(NoActionActivity.this.getClass()
											.getName(), backdata);
									if (data.getNetResultCode() == 0) {
										// 调用方法，给listview加载数据，先将数据通过handler发送到主线程中
										Message msg = new Message();
										msg.arg1 = ADD_TAG;
										msg.obj = backdata;
										handler.sendMessage(msg);
									} else {
										Message msg = new Message();
										msg.arg1 = 1011;
										handler.sendMessage(msg);
									}

								}
							}
						}
					}
				}, false, false);
	}

	private void showPop() {
		if (TextUtils.isEmpty(url)) {
			Toast.makeText(getApplicationContext(), "网络异常无法分享", 0).show();
			return;
		}
		share = new SharePopupWindow(this);
		share.setPlatformActionListener(this);
		ShareModel model = new ShareModel();
		Log.i("wxn", "上showPop成功：" + url);
		// model.setImageUrl(url);
		model.setText(content);
		model.setTitle(shardTitle);
		model.setUrl(url);
		share.setWebUrl(ConstantValue.SHARED_NOSTART + mActivityBean.getId());
		share.initShareParams(model);
		share.showShareWindow(false);
		// 显示窗口 (设置layout在PopupWindow中显示的位置)
		share.showAtLocation(this.findViewById(R.id.new_no_mian),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		Message msg = new Message();
		msg.what = 0;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public void onComplete(Platform plat, int action,
			HashMap<String, Object> res) {
		Message msg = new Message();
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = plat;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		Message msg = new Message();
		msg.what = 1;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public boolean handleMessage(Message msg) {
		int what = msg.what;
		if (what == 1) {
			Toast.makeText(this, "分享失败", Toast.LENGTH_SHORT).show();
		}
		if (share != null) {
			share.dismiss();
		}
		return false;
	}

	/**
	 * 创建加入到日历记录中的提示框
	 */
	private void createDialog_canlande() {
		if (exitDialogs == null) {
			exitDialogs = new CustomDialog(NoActionActivity.this,
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							useWayAddActivityToDate();
							exitDialogs.dismiss();
						}
					}, new OnClickListener() {

						@Override
						public void onClick(View v) {
							exitDialogs.dismiss();
						}
					});
			exitDialogs.setRemindTitle("温馨提示");
			exitDialogs.setCancelTxt("取消");
			exitDialogs.setConfirmTxt("加入");
			exitDialogs.setRemindMessage("是否确定加入日历记录中，以便到期提醒");
		}
		exitDialogs.show();
	}

	/**
	 * 调用方法，将预约成功后的该活动添加到日历账号中
	 */
	private void useWayAddActivityToDate() {
		// TODO Auto-generated method stub
		String name = useWayReadCurrentPhoneCalanderCount();
		if (!TextUtils.isEmpty(name)) {
			// 说明日历账号存在
			addDataToCalendar();// 将输入插入到日历中
		} else {
			// 说明日历账号不存在
			initCalendars();// 调用方法，插入日历账号
			addDataToCalendar();// 将输入插入到日历中
		}
	}

	// Android2.2版本以后的URL，之前的就不写了
	private static String calanderURL = "content://com.android.calendar/calendars";
	private static String calanderEventURL = "content://com.android.calendar/events";
	private static String calanderRemiderURL = "content://com.android.calendar/reminders";

	/**
	 * 读取当前手机日历系统账号
	 * 
	 * @return
	 */
	private String useWayReadCurrentPhoneCalanderCount() {
		// TODO Auto-generated method stub
		String name = "";
		Cursor userCursor = getContentResolver().query(Uri.parse(calanderURL),
				null, null, null, null);
		// System.out.println("Count: " + userCursor.getCount());
		// Toast.makeText(this, "Count: " + userCursor.getCount(),
		// Toast.LENGTH_LONG).show();

		for (userCursor.moveToFirst(); !userCursor.isAfterLast(); userCursor
				.moveToNext()) {
			// System.out.println("name: " +
			// userCursor.getString(userCursor.getColumnIndex("ACCOUNT_NAME")));
			String userName1 = userCursor.getString(userCursor
					.getColumnIndex("name"));
			String userName0 = userCursor.getString(userCursor
					.getColumnIndex("ACCOUNT_NAME"));
			if (userName0.equals(GlobalParams.USER_ID)) {
				name = userName0;
			}
			// Toast.makeText(this, "NAME: " + userName1 + " -- ACCOUNT_NAME: "
			// + userName0, Toast.LENGTH_LONG).show();
		}
		return name;
	}

	// 添加账户
	private void initCalendars() {
		TimeZone timeZone = TimeZone.getDefault();
		ContentValues value = new ContentValues();
		value.put(Calendars.NAME, GlobalParams.USER_ID);
		value.put(Calendars.ACCOUNT_NAME, GlobalParams.USER_ID);
		value.put(Calendars.ACCOUNT_TYPE, "com.android.exchange");
		value.put(Calendars.CALENDAR_DISPLAY_NAME, "mytt");
		value.put(Calendars.VISIBLE, 1);
		value.put(Calendars.CALENDAR_COLOR, -9206951);
		value.put(Calendars.CALENDAR_ACCESS_LEVEL, Calendars.CAL_ACCESS_OWNER);
		value.put(Calendars.SYNC_EVENTS, 1);
		value.put(Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
		value.put(Calendars.OWNER_ACCOUNT, "mygmailaddress@gmail.com");
		value.put(Calendars.CAN_ORGANIZER_RESPOND, 0);
		Uri calendarUri = Calendars.CONTENT_URI;
		calendarUri = calendarUri
				.buildUpon()
				.appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER,
						"true")
				.appendQueryParameter(Calendars.ACCOUNT_NAME,
						GlobalParams.USER_ID)
				.appendQueryParameter(Calendars.ACCOUNT_TYPE,
						"com.android.exchange").build();
		getContentResolver().insert(calendarUri, value);
	}

	/**
	 * 将活动信息插入到日历系统当中
	 */
	private void addDataToCalendar() {
		// TODO Auto-generated method stub
		// 获取要出入的gmail账户的id
		String calId = "";
		Cursor userCursor = getContentResolver().query(Uri.parse(calanderURL),
				null, null, null, null);
		if (userCursor.getCount() > 0) {
			userCursor.moveToLast(); // 注意：是向最后一个账户添加，开发者可以根据需要改变添加事件 的账户
			calId = userCursor.getString(userCursor.getColumnIndex("_id"));
		} else {
			Toast.makeText(this, "没有账户，请先添加账户", 0).show();
			return;
		}

		ContentValues event = new ContentValues();
		event.put("title", mActivityBean.getSubhead());
		event.put("description", mActivityBean.getTitle());
		// 插入账户
		event.put("calendar_id", calId);
		// System.out.println("calId: " + calId);
		event.put("eventLocation", mBean.getVenueName());
		//
		Calendar mCalendar = Calendar.getInstance();
		// mCalendar.set(Calendar.HOUR_OF_DAY, 11);
		// mCalendar.set(Calendar.MINUTE, 45);
		// long start = mCalendar.getTime().getTime();

		long start = 0;
		if (!TextUtils.isEmpty(mActivityBean.getStarttime())) {
			start = Long.parseLong(mActivityBean.getStarttime());
		}
		// mCalendar.set(Calendar.HOUR_OF_DAY, 12);
		// long end = mCalendar.getTime().getTime();
		long end = 0;
		if (!TextUtils.isEmpty(mActivityBean.getEndtime())) {
			end = Long.parseLong(mActivityBean.getEndtime());
		}
		event.put("dtstart", start);
		event.put("dtend", end);
		event.put("hasAlarm", 1);
		event.put(Events.EVENT_TIMEZONE, "Asia/BeiJing"); // 这个是时区，必须有，
		// 添加事件
		Uri newEvent = getContentResolver().insert(Uri.parse(calanderEventURL),
				event);
		// 事件提醒的设定
		long id = Long.parseLong(newEvent.getLastPathSegment());
		String actioncanlander=mActivityBean.getId()+","+id;
		saveArray(actioncanlander);
		Log.i("事件id是多少", id + "");
		// long id=Long.parseLong(mActivityBean.getId());
		ContentValues values = new ContentValues();
		values.put("event_id", id);
		// 提前10分钟有提醒
		values.put("method", 1);
		values.put("minutes", 10);
		getContentResolver().insert(Uri.parse(calanderRemiderURL), values);
		Toast.makeText(getApplicationContext(), "加入成功", Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * @param mListview2
	 * @2016-2-18下午2:24:29 调用工具类，从网络获取服务器数据,取消报名
	 */
	private void userUtilsGetDataquxiao(String bid) {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("bid", bid);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.QUXIAOYUYUE_ACTIVITY, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						if (data != null && data.getNetResultCode() == 0) {
							Message msg = new Message();
							msg.arg1 = CANCU_ACTIVE_SUCCESS;
							msg.obj = data.getObject();
							handler.sendMessage(msg);
							// 说明网络获取无数据
						} else {
							Message msg = new Message();
							msg.arg1 = CANCU_ACTIVE_FILUE;
							handler.sendMessage(msg);
						}
					}
				}, false, false);
	}

	/**
	 * 解析取消预约 数据
	 */
	private void analyseCancelActive(String badadddd) {
		if (!TextUtils.isEmpty(badadddd)) {
			if (badadddd.equals("success")) {
				mImageYuyue.setImageResource(R.drawable.huodong_weikaishi);
				// 修改数字
				String num = (String) mYuyueNum.getText();
				int nums = Integer.parseInt(num);
				nums = nums - 1;
				mYuyueNum.setText(nums + "");
				mYuyueNum.setClickable(false);
				IS_BAO = 0;
				loadArray();
				huoquyonghuyuyueliebiao_intent();
				Toast.makeText(getApplicationContext(), "取消成功", Toast.LENGTH_SHORT).show();
			}
		}
	}


	// sp保存数组
	public boolean saveArray(String str) {
		SharedPreferences sp = getSharedPreferences("EVENT",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor mEdit1 = sp.edit();
		Set<String> list = new HashSet<>();
		list.add(str);
		mEdit1.putStringSet("EVENT_ID", list);
		return mEdit1.commit();
	}

	/**
	 * 删除事件
	 */
	public void loadArray() {
		SharedPreferences mSharedPreference1 = getSharedPreferences("EVENT",
				Context.MODE_PRIVATE);
		Set<String> list = mSharedPreference1.getStringSet("EVENT_ID", null);
		if (list != null && list.size() > 0) {
			for (Iterator it = list.iterator(); it.hasNext();) {
				String str=(String) it.next();
				if(!TextUtils.isEmpty(str)){
					String[] strs=str.split(",");
					String key_h_id=strs[0];
					String key_eventid=strs[1];
					if(key_h_id.equals(mActivityBean.getId())){
						//说明需要把该活动从日历中删除掉
						ContentValues updateValues = new ContentValues();
						Uri deleteUri = null;
						long eventid = Long.parseLong(key_eventid);
						deleteUri = ContentUris.withAppendedId(Uri.parse(calanderEventURL),
								eventid);
						getContentResolver().delete(deleteUri, null, null);
					}
				}
			}
		}

	}


}
