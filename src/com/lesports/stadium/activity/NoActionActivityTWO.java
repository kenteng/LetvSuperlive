package com.lesports.stadium.activity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lesports.stadium.R;
import com.lesports.stadium.adapter.NoStartActionAdapter;
import com.lesports.stadium.adapter.NoactionVideoAdapter;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.ActivityDetailBean;
import com.lesports.stadium.bean.AdressForTicks;
import com.lesports.stadium.bean.HeightLightBean;
import com.lesports.stadium.bean.SenceBean;
import com.lesports.stadium.bean.YuYueActivityBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.view.CustomDialog;
import com.lesports.stadium.view.MyGridView;
import com.lesports.stadium.view.MyScrollViews;
import com.lesports.stadium.view.MyScrollViews.OnScrollListener;

public class NoActionActivityTWO extends Activity implements OnScrollListener,
		OnClickListener{

	/**
	 * 需要悬停的布局
	 */
	private LinearLayout mLayout_tip;
	/**
	 * 悬停的位置
	 */
	private LinearLayout mLayout_index;
	/**
	 * 自定义scrollview，用来控制部分布局悬停
	 */
	private MyScrollViews mScrollview;
	/**
	 * 内容介绍中的lable
	 */
	private TextView mLable;
	/**
	 * 内容
	 */
	private TextView mContent;
	/**
	 * 宣传视频
	 */
	private RelativeLayout mLayoutVideo, mLayoutVideos;
	/**
	 * 宣传海报
	 */
	private RelativeLayout mLayoutImage, mlayoutImages;
	/**
	 * 视频gridview
	 */
	private MyGridView mGridviewVideo;
	/**
	 * 图片view
	 */
	private MyGridView mGridviewImage;
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
	// private ImageView mImageBackground;
	/**
	 * 底部海报列表的适配器
	 */
	private NoStartActionAdapter mNoactionadapterImage;
	/**
	 * 底部海报列表的适配器
	 */
	private NoactionVideoAdapter mNoactionadapterVideo;
	/**
	 * 用来刷新时间
	 */
	private Timer refreshDiscussTimer = new Timer();
	/**
	 * 用户预约列表数据集合
	 */
	public List<YuYueActivityBean> mYuyueList = new ArrayList<>();
	private CustomDialog exitDialog;
	/**
	 * 用来存储解析地址集合
	 */
	private ArrayList<AdressForTicks> adresslist;
	/**
	 * 顶部table
	 */
	private RelativeLayout mLayoutTop;
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
	 * 宣传视频tv
	 */
	private TextView mTVvideo, mTVvideos;
	/**
	 * 宣传海报
	 */
	private TextView mTVimage, mTVimages;
	/**
	 * 背景图片
	 */
	private ImageView mImageBackground;
	/**
	 * 滑动的距离
	 */
	private int ScrollviewLength=1;
	private int scrollY;
	/**
	 * 
	 */
	private TextView mTitle;
	private TextView mTimeandAddress;
	/**
	 * 取消预约失败
	 */
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
			case TAG_DATA:
				String jijindata = (String) msg.obj;
				if (jijindata != null && !TextUtils.isEmpty(jijindata)) {
					// 调用方法解析数据
					jsondatajijing(jijindata);
				}
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
		setContentView(R.layout.new_nocation_activity_two);
		initview();
		initGet();
		if (mActivityBean != null) {
			userUtilsGetDataSenceBean(mActivityBean.getId());
		}

	}

	private float mPosX,mPosY,mCurPosX,mCurPosY;
	@Override
	public void onScrollView(final int scrollY) {
		this.scrollY=scrollY;
		int mBuyLayout2ParentTop = Math.max(scrollY, mLayout_tip.getTop());
		Log.i("滑动了多少", scrollY + "");
		Log.i("距离是多少", mLayout_tip.getTop() + "");
		if (scrollY >= mLayout_tip.getTop()) {
			mLayout_tip.setVisibility(View.INVISIBLE);
		} else {
			mLayout_tip.setVisibility(View.VISIBLE);
		}
		mLayout_index.layout(0, mBuyLayout2ParentTop, mLayout_index.getWidth(),
				mBuyLayout2ParentTop + mLayout_index.getHeight());

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
		mTitle=(TextView) findViewById(R.id.new_nocation_tv_title_two);
		mTimeandAddress=(TextView) findViewById(R.id.new_nocation_tv_title_xiao_two);
		mImageBackground = (ImageView) findViewById(R.id.new_action_image_bg_two);
		mLayout_index = (LinearLayout) findViewById(R.id.new_action_layout_xuantinghou_two);
		mLayout_tip = (LinearLayout) findViewById(R.id.new_action_layout_dangqianxuanting_two);
		mScrollview = (MyScrollViews) findViewById(R.id.new_action_scrollview_two);
		mScrollview.setOnScrollListener(this);
		mLayoutVideo = (RelativeLayout) findViewById(R.id.new_action_layout_xuanchuanshipin_two);
		mLayoutVideo.setOnClickListener(this);
		mLayoutVideos = (RelativeLayout) findViewById(R.id.new_action_layout_xuanchuanshipins_two);
		mLayoutVideos.setOnClickListener(this);
		mLayoutImage = (RelativeLayout) findViewById(R.id.new_action_layout_xuanchuanhaibao_two);
		mLayoutImage.setOnClickListener(this);
		mlayoutImages = (RelativeLayout) findViewById(R.id.new_action_layout_xuanchuanhaibaos_two);
		mlayoutImages.setOnClickListener(this);
		mTVvideo = (TextView) findViewById(R.id.new_action_layout_xuanchuanshipin_tv_two);
		mTVimage = (TextView) findViewById(R.id.new_action_layout_xuanchuanhaibao_tv_two);
		mTVimages = (TextView) findViewById(R.id.new_action_layout_xuanchuanhaibaos_tv_two);
		mTVvideos = (TextView) findViewById(R.id.new_action_layout_xuanchuanshipins_tv_two);
		mGridviewImage = (MyGridView) findViewById(R.id.new_action_grideview_picture_two);
		mGridviewImage.clearFocus();
		mGridviewVideo = (MyGridView) findViewById(R.id.new_action_grideview_vedio_two);
		mGridviewVideo.clearFocus();
		mLable = (TextView) findViewById(R.id.new_nocation_tv_title_content_title_two);
		mContent = (TextView) findViewById(R.id.new_nocation_tv_content_two);
		mLayoutRoot = (RelativeLayout) findViewById(R.id.new_no_mian_two);
		mLayoutRoot.setFocusableInTouchMode(true);
		mLayoutRoot.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						ScrollviewLength=mScrollview.getScrollY();
						onScrollView(ScrollviewLength);
						if(ScrollviewLength<=-1){
							Log.i("关闭了？", "好像是");
							finish();
						}
					}
				});
	
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.new_action_layout_xuanchuanshipin_two:
			mTVimage.setTextColor(Color.WHITE);
			mTVvideo.setTextColor(Color.rgb(70, 202, 253));
			mTVimages.setTextColor(Color.WHITE);
			mTVvideos.setTextColor(Color.rgb(70, 202, 253));
			mGridviewImage.setVisibility(View.GONE);
			mGridviewVideo.setVisibility(View.VISIBLE);
			break;
		case R.id.new_action_layout_xuanchuanhaibao_two:
			mTVimage.setTextColor(Color.rgb(70, 202, 253));
			mTVvideo.setTextColor(Color.WHITE);
			mTVimages.setTextColor(Color.rgb(70, 202, 253));
			mTVvideos.setTextColor(Color.WHITE);
			mGridviewImage.setVisibility(View.VISIBLE);
			mGridviewVideo.setVisibility(View.GONE);
			break;
		case R.id.new_action_layout_xuanchuanshipins_two:
			mTVimage.setTextColor(Color.WHITE);
			mTVvideo.setTextColor(Color.rgb(70, 202, 253));
			mTVimages.setTextColor(Color.WHITE);
			mTVvideos.setTextColor(Color.rgb(70, 202, 253));
			mGridviewImage.setVisibility(View.GONE);
			mGridviewVideo.setVisibility(View.VISIBLE);
			break;
		case R.id.new_action_layout_xuanchuanhaibaos_two:
			mTVimage.setTextColor(Color.rgb(70, 202, 253));
			mTVvideo.setTextColor(Color.WHITE);
			mTVimages.setTextColor(Color.rgb(70, 202, 253));
			mTVvideos.setTextColor(Color.WHITE);
			mGridviewImage.setVisibility(View.VISIBLE);
			mGridviewVideo.setVisibility(View.GONE);
			break;

		default:
			break;
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
				bean.setProductId(objs.getString("productId"));
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
		LApplication.loader.DisplayImage(
				ConstantValue.BASE_IMAGE_URL + bean.getBackgroudImageURL()
						+ ConstantValue.IMAGE_END, mImageBackground);
		mContent.setText(bean.getSummary());
		mLable.setText(bean.getLabel());
		mTitle.setText(mActivityBean.getSubhead());
		// 调用方法，计算当前时间与开始时间之间的间隔值，然后进行设置
		useWayCountTime(mTimeandAddress, mActivityBean);
		getdataFromServiceheightjijin();

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
				useWayAddDataToGridView(list);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 将集锦数据加载到空间中
	 * 
	 * @param list
	 */
	private void useWayAddDataToGridView(List<HeightLightBean> list) {
		// TODO Auto-generated method stub
		// 首先将数据分拣出来
		final List<HeightLightBean> listImage = new ArrayList<>();
		final List<HeightLightBean> listVideo = new ArrayList<>();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getFileType().equals("1")) {
					listVideo.add(list.get(i));
				} else {
					listImage.add(list.get(i));
				}
			}
			mNoactionadapterImage = new NoStartActionAdapter(listImage,
					NoActionActivityTWO.this);
			mNoactionadapterVideo = new NoactionVideoAdapter(listVideo,
					NoActionActivityTWO.this);
			mGridviewImage.setAdapter(mNoactionadapterImage);
			mGridviewVideo.setAdapter(mNoactionadapterVideo);
			mGridviewImage.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(NoActionActivityTWO.this,
							ImagePagerActivity.class);
					intent.putExtra("tag", "height");
					intent.putExtra("id", listImage.get(arg2).getId());
					intent.putExtra("audioId", listImage.get(arg2)
							.getResourceId());
					intent.putExtra("list", (Serializable) listImage);
					startActivity(intent);
				}
			});
			mGridviewVideo.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(NoActionActivityTWO.this,
							VideoPlayerActivity.class);
					intent.putExtra("tag", "height");
					intent.putExtra("id", listVideo.get(arg2).getId());
					intent.putExtra("heightlightbean", listVideo.get(arg2));
					intent.putExtra("list", (Serializable) listVideo);
					startActivity(intent);
				}
			});
		}

	};


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

	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
            mPosX = event.getX();
            mPosY = event.getY();
            break;
        case MotionEvent.ACTION_MOVE:
            mCurPosX = event.getX();
            mCurPosY = event.getY();
            break;
        case MotionEvent.ACTION_UP:
            if (mCurPosY - mPosY > 0
                    && (Math.abs(mCurPosY - mPosY) > 25)) {
                //向下滑動
            	if(scrollY<=0){
            		finish();
            		NoActionActivityTWO.this.overridePendingTransition(0,R.anim.activity_up_to_down);
            	}
            } 
            break;
		}
		return super.dispatchTouchEvent(event);
	}
}
