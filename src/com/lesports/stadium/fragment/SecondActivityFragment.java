package com.lesports.stadium.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lecloud.xutils.db.sqlite.CursorUtils.FindCacheSequence;
import com.lesports.stadium.R;
import com.lesports.stadium.activity.GoodsDetailActivity;
import com.lesports.stadium.activity.GoodsGuanggaoActivity;
import com.lesports.stadium.activity.H5Activity;
import com.lesports.stadium.activity.LiveDetialActivity;
import com.lesports.stadium.activity.MainActivity;
import com.lesports.stadium.activity.NoActionActivity;
import com.lesports.stadium.activity.NoStartActivity;
import com.lesports.stadium.activity.OnlineContainActivity;
import com.lesports.stadium.activity.ShoppingActivity;
import com.lesports.stadium.activity.UserCarActivity;
import com.lesports.stadium.adapter.SecondSenceFragmentListviewAdapter;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.base.BaseFragment;
import com.lesports.stadium.bean.GuangGaoBean;
import com.lesports.stadium.bean.GuanggaoActivitys;
import com.lesports.stadium.bean.RoundGoodsBean;
import com.lesports.stadium.bean.SenceBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.gaode.IndoorGaodeGuide;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.pullrefreshview.PullToRefreshBase;
import com.lesports.stadium.pullrefreshview.PullToRefreshBase.OnRefreshListener;
import com.lesports.stadium.pullrefreshview.PullToRefreshListView;
import com.lesports.stadium.utils.CommonUtils;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.UIUtils;
import com.lesports.stadium.utils.Utils;
import com.lesports.stadium.view.CustomDialog;
import com.lesports.stadium.view.MyScrollViews;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * 
 * ***************************************************************
 * 
 * @Desc : 第二版 进行界面 fragment
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwc
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
public class SecondActivityFragment extends BaseFragment implements
		OnClickListener {

	/**
	 * 页面视图view
	 */
	private View mFragmentview;
	/**
	 * 需要加载数据以及布局的listview
	 */
	private ListView mListview;
	/**
	 * 列表项数据控件listview的适配器
	 */
	private SecondSenceFragmentListviewAdapter mAdapter;
	/**
	 * 顶部右侧弹出菜单布局
	 */
	private LinearLayout mRightLayout;
	/**
	 * 顶部右侧弹出菜单
	 */
	/**
	 * 列表项的数据源集合
	 */
	private List<SenceBean> mList = new ArrayList<SenceBean>();;
	/**
	 * 需要传递的活动id的标记
	 */
	private final String ID_TAG = "id";
	/**
	 * 无数据的时候显示的vi
	 */
	private TextView Nodata;
	/**
	 * 右侧下拉图片
	 */
	private ImageView mRightimage;
	/**
	 * 本类对象
	 */
	public static SecondActivityFragment instance;
	/**
	 * 取消预约的bid
	 */
	private String bid;
	/**
	 * 定义一个变量，该变量用来动态的标注上拉加载的时候改变页码来进行获取数据
	 */
	private int PAGE_NUM = 2;
	/**
	 * 进度条布局
	 */
	private RelativeLayout mLayout_progressbar;
	/**
	 * 定义第一条活动的开始时间
	 */
	private String mFirdtStartTime;
	/**
	 * 定义最后一条的开始时间
	 */
	private String mLastStartTime;
	/**
	 * 是否是上拉加载，需要将list职位最后一行显示
	 */
	private boolean isShowLast = false;
	/**
	 * 上拉加载后要显示的行数
	 */
	private int shouCount;
	/**
	 * 需要做特殊处理的myscrollview
	 */
	private MyScrollViews mMyScrollview;

	/**
	 * 初始时候显示在界面的布局
	 */
	private LinearLayout mLayoutFirstShow;
	/**
	 * 上滑以后显示的布局
	 */
	private LinearLayout mLayoutSecondShow;

	private PullToRefreshListView mPulllistview;

	private View mHeaderView;

	/**
	 * 餐饮按钮
	 */
	private RelativeLayout mFoodTVOne, mFoodTVTwo;
	/**
	 * 用车按钮
	 */
	private RelativeLayout mUseCarOne, mUseCarTwo;
	/**
	 * 商城按钮
	 */
	private RelativeLayout mGoodsOne, mGoodsTwo;
	/**
	 * 导航按钮
	 */
	private RelativeLayout mDaoHangOne,mDaoHangTwo;

	/**
	 * 顶部需要显示的布局
	 */
	private LinearLayout mLayoutTop;
	/**
	 * 顶部轮播图
	 */
	private ViewPager second_activity_top_viewpager;
	/**
	 * 轮播图导航点的viewgroup
	 */
	private LinearLayout group;
	private ScheduledExecutorService scheduledExecutorService;
	/**
	 * 图片标题正文的那些点
	 */
	private List<View> dots;
	/**
	 * 当前图片的索引号
	 */
	private int currentItem = 0;
	/**
	 * 滑动的图片集合
	 */
	private List<ImageView> imageViews;
	/**
	 * 用来标记网络获取的数据
	 */
	private final int TAG_DATA_GUANG = 3;
	/**
	 * 下拉刷新失败
	 */
	private final int FERESH_ERROR = 21;
	/**
	 * 下拉刷新成功
	 */
	private final int FRESH_SUCCESS = 20;
	/**
	 * 展示的是否是广告 默认 fasle
	 */
	private boolean isNoGuanggao = false;

	private RelativeLayout mLayout_topss;
	private LinearLayout mLayout_topTwo;
	/**
	 * 广告列表数据
	 */
	private List<GuangGaoBean> guanggaoList = new ArrayList<GuangGaoBean>();
	int height;
	/**
	 * 当前城市下无数据
	 */
	private final int NO_DATA = 99;
	/**
	 * 加载更多成功
	 */
	private final int ADD_MORE_SUCCESS = 888;
	/**
	 * 加载更多失败
	 */
	private final int ADD_MORE_FILE = 889;
	/**
	 * 第一次获取数据成功
	 */
	private final int LOAD_DAT_SUCCESS = 666;
	/**
	 * 轮播图切换
	 */
	private final int REPATE = 555;
	/**
	 * 处理数据的handler；
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case LOAD_DAT_SUCCESS:
				mPulllistview.onPullDownRefreshComplete();
				analyseDate((String) msg.obj);
				break;
			case REPATE:
				second_activity_top_viewpager.setCurrentItem(currentItem);// 切换当前显示的图片
				break;
			case FRESH_SUCCESS:
				analyseRerreshDate((String) msg.obj);
				break;
			case FERESH_ERROR:
				MainActivity.instance.setLayoutVisible();
				mPulllistview.onPullDownRefreshComplete();
				break;
			case TAG_DATA_GUANG:
				analyseGuangGao((String) msg.obj);
				break;
			case NO_DATA:
				if (mPulllistview != null)
					mPulllistview.onPullDownRefreshComplete();
				mList = null;
				addDataToListview(null);
				break;
			case ADD_MORE_SUCCESS:
				analyseAddMoreDate((String) msg.obj);
				break;
			case ADD_MORE_FILE:
				MainActivity.instance.setLayoutVisible();
				mPulllistview.onPullUpRefreshComplete();
				break;
			default:
				break;
			}

		}

	};

	public void onViewCreated(View view, Bundle savedInstanceState) {

		ViewTreeObserver vto = mLayout_topss.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				mLayout_topss.getViewTreeObserver()
						.removeGlobalOnLayoutListener(this);
				height = mLayout_topss.getHeight();
			}
		});
	};

	@Override
	public View initView(LayoutInflater inflater) {
		instance = this;
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		mFragmentview = inflater.inflate(R.layout.fragment_activity_second,
				null);
		mPulllistview = (PullToRefreshListView) mFragmentview
				.findViewById(R.id.sence_fragment_listviewssss);
		mListview = (ListView) mPulllistview.getRefreshableView();
		mFoodTVTwo = (RelativeLayout) mFragmentview
				.findViewById(R.id.layout_shouye_canyin_two);
		mDaoHangTwo=(RelativeLayout) mFragmentview.findViewById(R.id.layout_shouye_daohang_two);
		mDaoHangTwo.setOnClickListener(new listener());
		mFoodTVTwo.setOnClickListener(new listener());
		mGoodsTwo = (RelativeLayout) mFragmentview
				.findViewById(R.id.layout_shouyemian_shangcheng_two);
		mGoodsTwo.setOnClickListener(new listener());
		mUseCarTwo = (RelativeLayout) mFragmentview
				.findViewById(R.id.layout_shouyemian_yongche_two);
		mUseCarTwo.setOnClickListener(new listener());
		mHeaderView = LayoutInflater.from(getActivity()).inflate(
				R.layout.second_activity_listview_header, null);
		mLayout_topss = (RelativeLayout) mHeaderView
				.findViewById(R.id.layout_second_activity_header_topsss);
		mFoodTVOne = (RelativeLayout) mHeaderView
				.findViewById(R.id.layout_shouye_canyin_one);

		second_activity_top_viewpager = (ViewPager) mHeaderView
				.findViewById(R.id.second_activity_top_viewpager);
		group = (LinearLayout) mHeaderView
				.findViewById(R.id.goods_layout_addview);

		mFoodTVOne.setOnClickListener(new listener());
		mGoodsOne = (RelativeLayout) mHeaderView
				.findViewById(R.id.layout_shouye_shangcheng_one);
		mGoodsOne.setOnClickListener(new listener());
		mUseCarOne = (RelativeLayout) mHeaderView
				.findViewById(R.id.layout_shouye_yongche_one);
		mUseCarOne.setOnClickListener(new listener());
		mLayout_topTwo = (LinearLayout) mHeaderView
				.findViewById(R.id.layout_shouyemian_firstshows);
		mDaoHangOne=(RelativeLayout) mHeaderView.findViewById(R.id.layout_shouye_daohang_one);
		mDaoHangOne.setOnClickListener(new listener());
		mListview.addHeaderView(mHeaderView);
		mLayoutTop = (LinearLayout) mFragmentview
				.findViewById(R.id.layout_shouyemian_firstshowssssss);
		mListview.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem > 0) {
					float alpha = 0.9f * 255;
					mLayoutTop.getBackground().setAlpha((int) alpha);
				} else if (firstVisibleItem == 0) {
					float alpha = 1f * 255;
					mLayoutTop.getBackground().setAlpha((int) alpha);
				}
				int top = mHeaderView.getTop();
				int bottom = mHeaderView.getBottom();
				int total = bottom - top;

				float percent = 0;
				percent = (float) (total - bottom) / (height);
				if (percent >= 1) {
					mLayoutTop.setVisibility(View.VISIBLE);
					float percents = percent - 1;
					if (percents > 0.45 && percents < 0.5) {
						float alpha = 0.85f * 255;
						mLayoutTop.getBackground().setAlpha((int) alpha);
					} else if (percents >= 0.5) {
						float alpha = 0.9f * 255;
						mLayoutTop.getBackground().setAlpha((int) alpha);
					}
				} else if (percent >= 0 && percent < 1) {
					mLayoutTop.getBackground().setAlpha(255);
					mLayoutTop.setVisibility(View.GONE);
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					// 开始滑动
					if (MainActivity.instance != null) {
						MainActivity.instance.setLayoutGone();
					}
					break;

				case OnScrollListener.SCROLL_STATE_FLING:
					// 滑动中
					break;

				case OnScrollListener.SCROLL_STATE_IDLE:
					// 滑动结束
					MainActivity.instance.setLayoutVisible();
					break;

				default:
					break;
				}

			}
		});
		mPulllistview.setPullLoadEnabled(true);
		mPulllistview.setPullRefreshEnabled(true);
		mPulllistview.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 下拉刷新城市数据
				((MainActivity) getActivity()).getCityData();
				if (!UIUtils.isAvailable(getActivity())) {
					mPulllistview.onPullDownRefreshComplete();
				} else {
					MainActivity.instance.setLayoutVisible();
					// 下拉刷新加载数据
					loadRefreshData(1, 1);
				}
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				if (!UIUtils.isAvailable(getActivity())) {
					mPulllistview.onPullUpRefreshComplete();
				} else {
					MainActivity.instance.setLayoutVisible();
					// 上拉刷新加载数据
					loadRefreshData_add(PAGE_NUM);

				}
			}
		});
		// initListviewDate();
		return mFragmentview;
	}

	@Override
	public void onResume() {
		loadRefreshData(1, 1);
		super.onResume();
	}

	/**
	 * 下拉刷新数据
	 * 
	 * @param i
	 * @param j
	 */
	protected void loadRefreshData(int i, int j) {
		isShowLast = false;
		if (mList != null && mList.size() != 0) {
			userUtilsGetData_shuaxin("1");
		} else {
			// userUtilsGetData_shuaxin("1");
			initListviewDate();
		}

	}

	/**
	 * 上拉加载数据
	 * 
	 * @param i
	 */
	protected void loadRefreshData_add(int i) {
		isShowLast = true;
		if (mList != null && mList.size() > 2)
			shouCount = mList.size() - 2;
		userUtilsGetData_jiazai(i);
	}

	/**
	 * 初始化界面中的listview，并且给其加载初始数据
	 * 
	 * @param mListview2
	 * @2016-1-29上午11:52:02
	 */
	public void initListviewDate() {
		// 调用工具类，从网络获取数据
		userUtilsGetData("1");
	}

	/**
	 * @param mListview2
	 * @2016-2-18下午2:24:29 调用工具类，从网络获取服务器数据
	 */
	private void userUtilsGetData(String str) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("status", "0,1");
		// params.put("venueId", "24");
		params.put("action", "2");
		// params.put("starttime","");
		params.put("rows", "6");
		params.put("cityId", GlobalParams.CITY_ID);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ACTIVITY_DATA, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						Log.i("wxn",
								GlobalParams.CITY_ID + "首页数据："
										+ data.getNetResultCode() + "..."
										+ data.getObject());
						if (data != null && data.getNetResultCode() == 0) {
							Message msg = new Message();
							msg.arg1 = LOAD_DAT_SUCCESS;
							msg.obj = data.getObject();
							handler.sendMessage(msg);

						} else {
							Message msg = new Message();
							msg.arg1 = NO_DATA;
							handler.sendMessage(msg);
						}
					}
				}, false, false);
	}

	/**
	 * 加载更多
	 * 
	 * @param str
	 */
	private void userUtilsGetData_jiazai(int str) {
		String time = "";
		if (!TextUtils.isEmpty(mLastStartTime)) {
			time = CommonUtils.getChangeToTime(mLastStartTime);
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("status", "0,1");
		// params.put("venueId", "24");
		params.put("action", "2");
		params.put("starttime", time);
		params.put("rows", "6");
		params.put("cityId", GlobalParams.CITY_ID);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ACTIVITY_DATA, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						Log.i("wxn", "加载更多" + data.getNetResultCode() + ".."
								+ data.getObject());
						if (data != null && data.getNetResultCode() == 0) {
							// 说明网络获取无数据
							Message msg = new Message();
							msg.arg1 = ADD_MORE_SUCCESS;
							msg.obj = data.getObject();
							handler.sendMessage(msg);
						} else {
							Message msg = new Message();
							msg.arg1 = ADD_MORE_FILE;
							handler.sendMessage(msg);
						}
					}
				}, false, false);
	}

	/**
	 * 下拉刷新
	 * 
	 * @param str
	 */
	private void userUtilsGetData_shuaxin(String str) {
		// 调用方法，转换时间数据
		String time = "";
		if (!TextUtils.isEmpty(mFirdtStartTime)) {
			time = CommonUtils.getChangeToTime(mFirdtStartTime);
		}
		Map<String, String> params = new HashMap<String, String>();
		if (TextUtils.isEmpty(time)) {
			params.put("status", "0,1");
			// params.put("venueId", "24");
			params.put("action", "2");
			params.put("starttime", "");
			params.put("rows", "6");
		} else {
			params.put("status", "0,1");
			params.put("action", "1");
			params.put("starttime", time);
			int rows = (mList == null ? 6 : mList.size());
			params.put("rows", "6");
		}
		params.put("cityId", GlobalParams.CITY_ID);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ACTIVITY_DATA, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if (data != null && data.getNetResultCode() == 0) {
							Message msg = new Message();
							msg.arg1 = FRESH_SUCCESS;
							msg.obj = data.getObject();
							handler.sendMessage(msg);
						} else {
							Message msg = new Message();
							msg.arg1 = FERESH_ERROR;
							handler.sendMessage(msg);
						}
					}
				}, false, false);
	}

	/**
	 * 将网络获取的数据加载到界面当中的listview的方法
	 * 
	 * @2016-2-19上午9:35:33
	 */
	private void addDataToListview(final List<SenceBean> list) {
		if (mAdapter == null) {
			mAdapter = new SecondSenceFragmentListviewAdapter(getActivity(),
					list);
			mListview.setAdapter(mAdapter);
		} else {
			mAdapter.setData(list);
		}
		if (isShowLast) {
			mListview.setSelection(shouCount);
		}
		mListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				SenceBean bean = list.get(arg2 - 1);
				String status = bean.getcStatus();
				String id = bean.getId();
				if (status.equals("0")) {
					// 说明是未开始
					Intent intents = new Intent(getActivity(),
							NoActionActivity.class);
					intents.putExtra("bean", list.get(arg2-1));
					startActivity(intents);
				} else {
					Intent intent = new Intent(getActivity(),
							LiveDetialActivity.class);

					intent.putExtra("id", bean.getId());
					intent.putExtra("no", "1");
//					intent.putExtra("lable", bean.getLabel());
//					intent.putExtra("url", bean.getFileUrl());
					// 区别音乐会和球赛 0.比赛1.音乐会
					intent.putExtra("camptype", bean.getCamptype());
					// 球赛是否支持过：0未支持；1已支持
					intent.putExtra("hasSupported", bean.getHasSupported());
					// 球赛是否已尖叫过 0：表示没有尖叫过1：表示已经尖叫过
					intent.putExtra("hasScreamed", bean.getHasScreamed());
					// 直播源id 用于直播
//					intent.putExtra("resourceId", bean.getResourceId());
					startActivity(intent);
				}
			}

		});
	}

	public class listener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.layout_shouye_canyin_one:
				// 不管是第一个还是第二个，都是需要跳转到餐饮页面
				// 先设置动画
				Intent intent = new Intent(getActivity(),
						OnlineContainActivity.class);
				getActivity().startActivity(intent);
				break;
			case R.id.layout_shouye_canyin_two:
				Intent intentc = new Intent(getActivity(),
						OnlineContainActivity.class);
				getActivity().startActivity(intentc);
				// 餐饮
				break;
			case R.id.layout_shouye_shangcheng_one:
				Intent intents = new Intent(getActivity(),
						ShoppingActivity.class);
				getActivity().startActivity(intents);
				// 商城
				break;
			case R.id.layout_shouyemian_shangcheng_two:
				Intent intentss = new Intent(getActivity(),
						ShoppingActivity.class);
				getActivity().startActivity(intentss);
				// 商城
				break;
			case R.id.layout_shouye_yongche_one:
				Intent intentu = new Intent(getActivity(),
						UserCarActivity.class);
				getActivity().startActivity(intentu);
				// 用车
				break;
			case R.id.layout_shouyemian_yongche_two:
				Intent intentus = new Intent(getActivity(),
						UserCarActivity.class);
				getActivity().startActivity(intentus);
				// 用车
				break;
			case R.id.layout_shouye_daohang_one:
				//导航按钮
				Intent intentda=new Intent(getActivity(),IndoorGaodeGuide.class);
				getActivity().startActivity(intentda);
				break;
			case R.id.layout_shouye_daohang_two:
				//导航按钮
				Intent intentdas=new Intent(getActivity(),IndoorGaodeGuide.class);
				getActivity().startActivity(intentdas);
				break;
			default:
				break;
			}
		}
	}
	/**
	 * 用来解析该界面从网络获取到的数据
	 * 
	 * @2016-2-18下午4:45:18
	 */
	private List<SenceBean> jsonParserData(String jsonstring) {
		List<SenceBean> list = new ArrayList<SenceBean>();
		try {
			JSONArray array = new JSONArray(jsonstring);
			int count = array.length();
			for (int i = 0; i < count; i++) {
				SenceBean bean = new SenceBean();
				JSONObject objs = array.getJSONObject(i);
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
				if (objs.has("frontCoverImageURL")) {
					bean.setFrontCoverImageURL(objs
							.getString("frontCoverImageURL"));
				}
				if (objs.has("subhead")) {
					bean.setSubhead(objs.getString("subhead"));
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
				if (objs.has("venueName")) {
					bean.setVenueName(objs.getString("venueName"));
				}
				if (objs.has("backgroudImageURL")) {
					bean.setBackgroudImageURL(objs
							.getString("backgroudImageURL"));
				}
				if (objs.has("resourceId")) {
					bean.setResourceId(objs.getString("resourceId"));
				}
				// 球赛是否 尖叫过
				if (objs.has("hasScreamed")) {
					bean.setHasScreamed(objs.getString("hasScreamed"));
				}
				// 球赛是否支持
				if (objs.has("hasSupported")) {
					bean.setHasSupported(objs.getString("hasSupported"));
				}
				if (objs.has("productId")) {
					Log.i("wxn", "productId:" + objs.getString("productId"));
					bean.setProductId(objs.getString("productId"));
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
	public void onDestroy() {
		if (scheduledExecutorService != null)
			scheduledExecutorService.shutdown();
		if (handler != null)
			handler.removeCallbacksAndMessages(null);
		if (mList != null) {
			mList.clear();
			mList = null;
		}
		if (imageViews != null) {
			imageViews.clear();
			imageViews = null;
		}
		if (dots != null) {
			dots.clear();
			dots = null;
		}
		if (guanggaoList != null) {
			guanggaoList.clear();
			guanggaoList = null;
		}
		scheduledExecutorService = null;
		second_activity_top_viewpager = null;
		instance = null;
		imageLoader = null;
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions list_options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.b240_160) // 设置图片在下载期间显示的图片
			.showImageForEmptyUri(R.drawable.b240_160)// 设置图片Uri为空或是错误的时候显示的图片
			.showImageOnFail(R.drawable.b240_160) // 设置图片加载/解码过程中错误时候显示的图片
			.cacheOnDisc(true).cacheInMemory(true)// 设置下载的图片是否缓存在内存中
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
			.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
			.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
			.displayer(new FadeInBitmapDisplayer(500)).build();// 构建完成

	/**
	 * 初始化轮播数据
	 */
	private void initviewpagerData() {
		if (!Utils.isNullOrEmpty(guanggaoList)) {
			int count = guanggaoList.size();
			dots = new ArrayList<View>();// 计算需要生成的表示点数量
			group.removeAllViews();
			for (int i = 0; i < count; i++) {
				TextView tv = new TextView(getActivity());
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						30, 6);
				lp.setMargins(5, 0, 5, 0);
				tv.setLayoutParams(lp);
				tv.setBackgroundDrawable(getActivity().getResources()
						.getDrawable(R.drawable.page_indicator_long_unfocused));
				group.addView(tv);
				dots.add(tv);
			}
			imageViews = new ArrayList<ImageView>();
			for (int H = 0; H < count; H++) {
				ImageView img = new ImageView(getActivity());
				img.setScaleType(ScaleType.FIT_XY);
				imageLoader.displayImage(ConstantValue.BASE_IMAGE_URL
						+ guanggaoList.get(H).getUrl()
						+ ConstantValue.IMAGE_END, img, list_options);
				img.setTag(guanggaoList.get(H));
				img.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						GuangGaoBean bean = (GuangGaoBean) v.getTag();
						String resourceType = bean.getResourceType();
						String httpUrl = bean.getHttpUrl();
						if ("0".equals(resourceType)) {
							Intent hIntent = new Intent(getActivity(),
									H5Activity.class);
							hIntent.putExtra("url", httpUrl);
							startActivity(hIntent);
						} else if ("1".equals(resourceType)) {
							SenceBean activibean = bean.getActivitysbean();
							String status = activibean.getcStatus();
							String id = activibean.getId();
							if ("0".equals(status)) {
								// 说明是未开始
								Intent intents = new Intent(getActivity(),
										NoStartActivity.class);
								intents.putExtra("list", activibean);
								intents.putExtra("tag", "activity");
								intents.putExtra("productId",
										activibean.getProductId());
								intents.putExtra(ID_TAG, id);
								startActivity(intents);
							} else {
								Intent intent = new Intent(getActivity(),
										LiveDetialActivity.class);

								intent.putExtra("id", id);
								intent.putExtra("no", "1");
								intent.putExtra("lable", activibean.getLabel());
								// 区别音乐会和球赛 0.比赛1.音乐会
								intent.putExtra("camptype",
										activibean.getCamptype());
								// 球赛是否支持过：0未支持；1已支持
								intent.putExtra("hasSupported",
										activibean.getHasSupported());
								// 球赛是否已尖叫过 0：表示没有尖叫过1：表示已经尖叫过
								intent.putExtra("hasScreamed",
										activibean.getHasScreamed());
								// 直播源id 用于直播
								intent.putExtra("resourceId",
										activibean.getResourceId());
								startActivity(intent);
							}
						} else if ("2".equals(resourceType)) {
							RoundGoodsBean goodsbean = bean.getGoodsbean();
							Intent intent = new Intent(getActivity(),
									GoodsDetailActivity.class);
							intent.putExtra("id", goodsbean.getId());
							intent.putExtra("bean", goodsbean);
							intent.putExtra("tag", "round");
							startActivity(intent);
						}
					}
				});
				imageViews.add(img);
			}
		}
	}

	/**
	 * 初始化轮播控件
	 * 
	 * @2016-2-26下午1:41:15
	 */
	private void initAutoScrollviewViewpager(Context context) {
		// TODO Auto-generated method stub
		second_activity_top_viewpager.setAdapter(new MyAdapter());
		// 设置一个监听器，当ViewPager中的页面改变时调用
		second_activity_top_viewpager
				.setOnPageChangeListener(new MyPageChangeListener());
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每两秒钟切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 3,
				TimeUnit.SECONDS);
	}

	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(imageViews.get(arg1));
			return imageViews.get(arg1);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			currentItem = position;
			dots.get(oldPosition).setBackgroundResource(
					R.drawable.page_indicator_long_unfocused);
			dots.get(position).setBackgroundResource(
					R.drawable.page_indicator_long_focused);
			oldPosition = position;
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	/**
	 * 换行切换任务
	 * 
	 * @author Administrator
	 * 
	 */
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (second_activity_top_viewpager) {
				currentItem = (currentItem + 1) % imageViews.size();
				Message msg = new Message();
				msg.arg1 = REPATE;
				handler.sendMessage(msg);
			}
		}

	}

	/**
	 * 获取广告列表数据的方法
	 */
	private void UseWayGetData() {
		// TODO Auto-generated method stub
		Log.i("wxn", "获取广告位的方法执行。。。");
		Map<String, String> params = new HashMap<String, String>();
		params.put("pageNo", "0");
		params.put("rows", "20");
		params.put("advertisementType", "5");
		params.put("status", "1");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.GUANGGAO_LIST, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if (data != null && data.getNetResultCode() == 0) {
							Object obj = data.getObject();
							String backdata = obj.toString();
							Log.e("dcc", "广告列表获取数据" + backdata);
							if (!TextUtils.isEmpty(backdata)
									&& !"[]".equals(backdata)) {
								Message msg = new Message();
								msg.arg1 = TAG_DATA_GUANG;
								msg.obj = backdata;
								handler.sendMessage(msg);
								return;
							}
						}
						// isNoGuanggao = true;
						// Message msg = new Message();
						// msg.arg1 = GUANG_FILUUE;
						// handler.sendMessage(msg);
					}

				}, false, false);
	};

	private void analyseGuangGao(String data) {
		if (TextUtils.isEmpty(data)) {
			return;
		}
		guanggaoList.clear();
		try {
			JSONArray guanggaoArray = new JSONArray(data);
			for (int i = 0; i < guanggaoArray.length(); i++) {
				JSONObject rObj = guanggaoArray.getJSONObject(i);
				GuangGaoBean tempBean = new GuangGaoBean();
				if (rObj.has("advertisementTime"))
					tempBean.setAdvertisementTime(rObj
							.getString("advertisementTime"));
				if (rObj.has("advertisementType"))
					tempBean.setAdvertisementType(rObj
							.getString("advertisementType"));
				if (rObj.has("creatTime"))
					tempBean.setCreatTime(rObj.getString("creatTime"));
				if (rObj.has("displayOrder"))
					tempBean.setDisplayOrder(rObj.getString("displayOrder"));
				if (rObj.has("entTime"))
					tempBean.setEntTime(rObj.getString("entTime"));
				if (rObj.has("httpUrl"))
					tempBean.setHttpUrl(rObj.getString("httpUrl"));
				if (rObj.has("id"))
					tempBean.setId(rObj.getString("id"));
				if (rObj.has("posCode"))
					tempBean.setPosCode(rObj.getString("posCode"));
				if (rObj.has("resourceId"))
					tempBean.setResourceId(rObj.getString("resourceId"));
				if (rObj.has("startTime"))
					tempBean.setStartTime(rObj.getString("startTime"));
				if (rObj.has("status"))
					tempBean.setStatus(rObj.getString("status"));
				if (rObj.has("title"))
					tempBean.setTitle(rObj.getString("title"));
				if (rObj.has("url"))
					tempBean.setUrl(rObj.getString("url"));
				if (rObj.has("resourceType")) {
					tempBean.setResourceType(rObj.getString("resourceType"));
				}
				if (rObj.has("resource")) {
					String resource = rObj.getString("resource");
					if ("1".equals(tempBean.getResourceType())) {
						SenceBean activitys = analyseActicResource(resource);
						tempBean.setActivitysbean(activitys);
					} else if ("2".equals(tempBean.getResourceType())) {
						RoundGoodsBean goodsbean = analyseGoods(resource);
						tempBean.setGoodsbean(goodsbean);
					}
				}
				guanggaoList.add(tempBean);

			}
			initviewpagerData();
			initAutoScrollviewViewpager(getActivity());
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 解析商品
	 * 
	 * @param resource
	 */
	private RoundGoodsBean analyseGoods(String resource) {
		try {
			JSONObject objs = new JSONObject(resource);
			RoundGoodsBean moundgoodsbean = new RoundGoodsBean();
			if (objs.has("bigImg")) {
				moundgoodsbean.setBigimg(objs.getString("bigImg"));
			}
			if (objs.has("classicId")) {
				moundgoodsbean.setClassicId(objs.getString("classicId"));
			}
			if (objs.has("bannerImage")) {
				moundgoodsbean.setBannerImage(objs.getString("bannerImage"));
			}
			if (objs.has("category")) {
				// 子实体类部分
				JSONObject objz = objs.getJSONObject("category");
				if (objz.has("classicName")) {
					moundgoodsbean
							.setClassicName(objz.getString("classicName"));
				}
				if (objz.has("cId")) {
					moundgoodsbean.setcId(objz.getString("cId"));
				}
			}
			if (objs.has("createTime")) {
				moundgoodsbean.setCreateTime(objs.getString("createTime"));
			}
			if (objs.has("freight")) {
				moundgoodsbean.setFreight(objs.getString("freight"));
			}
			if (objs.has("gId")) {
				moundgoodsbean.setId(objs.getString("gId"));
			}
			if (objs.has("label")) {
				moundgoodsbean.setLabel(objs.getString("label"));
			}
			if (objs.has("mediumImg")) {
				moundgoodsbean.setMediumImg(objs.getString("mediumImg"));
			}
			if (objs.has("parentId")) {
				moundgoodsbean.setParentId(objs.getString("parentId"));
			}
			if (objs.has("price")) {
				moundgoodsbean.setPrice(objs.getString("price"));
			}
			if (objs.has("goodsName")) {
				moundgoodsbean.setGoodsName(objs.getString("goodsName"));
			}
			if (objs.has("priceunit")) {
				moundgoodsbean.setPriceunit(objs.getString("priceunit"));
			}
			if (objs.has("referPrice")) {
				moundgoodsbean.setReferPrice(objs.getString("referPrice"));
			}
			if (objs.has("sales")) {
				moundgoodsbean.setSales(objs.getString("sales"));
			}
			if (objs.has("seller")) {
				moundgoodsbean.setSeller(objs.getString("seller"));
			}
			if (objs.has("smallImg")) {
				moundgoodsbean.setSmallImg(objs.getString("smallImg"));
			}
			if (objs.has("status")) {
				moundgoodsbean.setStatus(objs.getString("status"));
			}
			if (objs.has("stock")) {
				moundgoodsbean.setStock(objs.getString("stock"));
			}
			if (objs.has("sellerAddress")) {
				moundgoodsbean
						.setSellerAddress(objs.getString("sellerAddress"));
			}
			if (objs.has("sellerName")) {
				moundgoodsbean.setSellerName(objs.getString("sellerName"));
			}
			return moundgoodsbean;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 解析活动
	 * 
	 * @param resource
	 * @return
	 */
	private SenceBean analyseActicResource(String resource) {
		try {
			JSONObject resourObj = new JSONObject(resource);
			SenceBean activitys = new SenceBean();
			if (resourObj.has("backgroudImageURL")) {
				activitys.setBackgroudImageURL(resourObj
						.getString("backgroudImageURL"));
			}
			if (resourObj.has("cHeat")) {
				activitys.setcHeat(resourObj.getString("cHeat"));
			}
			if (resourObj.has("cStatus")) {
				activitys.setcStatus(resourObj.getString("cStatus"));
			}
			if (resourObj.has("camptype")) {
				activitys.setCamptype(resourObj.getString("camptype"));
			}

			if (resourObj.has("creattime")) {
				activitys.setCreattime(resourObj.getString("creattime"));
			}
			if (resourObj.has("endtime")) {
				activitys.setEndtime(resourObj.getString("endtime"));
			}
			if (resourObj.has("frontCoverImageURL")) {
				activitys.setFrontCoverImageURL(resourObj
						.getString("frontCoverImageURL"));
			}
			if (resourObj.has("id")) {
				activitys.setId(resourObj.getString("id"));
			}
			if (resourObj.has("label")) {
				activitys.setLabel(resourObj.getString("label"));
			}
			if (resourObj.has("productId")) {
				activitys.setProductId(resourObj.getString("productId"));
			}
			if (resourObj.has("selltimeend")) {
				activitys.setSelltimeend(resourObj.getString("selltimeend"));
			}
			if (resourObj.has("selltimestart")) {
				activitys
						.setSelltimestart(resourObj.getString("selltimestart"));
			}
			if (resourObj.has("starttime")) {
				activitys.setStarttime(resourObj.getString("starttime"));
			}
			if (resourObj.has("subhead")) {
				activitys.setSubhead(resourObj.getString("subhead"));
			}
			if (resourObj.has("summary")) {
				activitys.setSummary(resourObj.getString("summary"));
			}
			if (resourObj.has("tips")) {
				activitys.setTips(resourObj.getString("tips"));
			}
			if (resourObj.has("title")) {
				activitys.setTitle(resourObj.getString("title"));
			}
			if (resourObj.has("venueId")) {
				activitys.setVenueId(resourObj.getString("venueId"));
			}
			if (resourObj.has("productId")) {
				activitys.setProductId(resourObj.getString("productId"));
			}
			// 球赛是否 尖叫过
			if (resourObj.has("hasScreamed")) {
				activitys.setHasScreamed(resourObj.getString("hasScreamed"));
			}
			// 球赛是否支持
			if (resourObj.has("hasSupported")) {
				activitys.setHasSupported(resourObj.getString("hasSupported"));
			}
			if (resourObj.has("resourceId")) {
				activitys.setResourceId(resourObj.getString("resourceId"));
			}
			return activitys;
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 解析加载更多多少
	 * 
	 * @param obj
	 */
	private void analyseAddMoreDate(String backdata_jiazai) {
		MainActivity.instance.setLayoutVisible();
		mPulllistview.onPullUpRefreshComplete();
		if (TextUtils.isEmpty(backdata_jiazai)) {
			return;
		}
		if (backdata_jiazai != null && !TextUtils.isEmpty(backdata_jiazai)) {
			PAGE_NUM++;
			List<SenceBean> list = jsonParserData(backdata_jiazai);
			if (list != null) {
				mList.addAll(list);
				if (mList != null) {
					mLastStartTime = mList.get(mList.size() - 1).getStarttime();
				}
				addDataToListview(mList);
			}
		}
	}

	/**
	 * 处理第一次加载数据
	 * 
	 * @param backdata
	 */
	private void analyseDate(String backdata) {
		if (!TextUtils.isEmpty(backdata)) {
			mList = jsonParserData(backdata);
			if (mList != null && mList.size() > 0) {
				mFirdtStartTime = mList.get(0).getStarttime();
				if (mList.size() == 1) {
					mLastStartTime = mFirdtStartTime;
				} else {
					mLastStartTime = mList.get(mList.size() - 1).getStarttime();
				}
			}
			addDataToListview(mList);
		} else {
			addDataToListview(null);
		}
	}

	/**
	 * 解析下拉刷新的数据
	 * 
	 * @param backdata_shuaxin
	 */
	private void analyseRerreshDate(String backdata_shuaxin) {
		MainActivity.instance.setLayoutVisible();
		mPulllistview.onPullDownRefreshComplete();
		if (!TextUtils.isEmpty(backdata_shuaxin)) {
			mList.clear();
			mList = jsonParserData(backdata_shuaxin);
			if (mList != null) {
				mLastStartTime = mList.get(mList.size() - 1).getStarttime();
			}
			addDataToListview(mList);
		}
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void initListener() {
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		UseWayGetData();
	}

}
