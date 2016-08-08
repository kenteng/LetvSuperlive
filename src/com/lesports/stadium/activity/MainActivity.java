package com.lesports.stadium.activity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lesports.stadium.R;
import com.lesports.stadium.adapter.LocationDataAdapter;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.Address;
import com.lesports.stadium.bean.SupportCity;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.fragment.AllChipsFragment;
import com.lesports.stadium.fragment.MyFragment;
import com.lesports.stadium.fragment.SecondActivityFragment;
import com.lesports.stadium.fragment.SecondServiceFragment;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.ui.MyRadioButton;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.DensityUtil;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.UtilsLocation;
import com.lesports.stadium.view.CustomDialog;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * ***************************************************************
 * 
 * @ClassName: MainActivity
 * 
 * @Desc : 主页面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 王新年
 * 
 * @data:2016-1-29 上午11:29:57
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class MainActivity extends Activity implements OnClickListener,
		AMapLocationListener {

	private RadioGroup radioGroup;

	/**
	 * 添加活动 Fragment 的Tag 标识
	 */
	private final String activeTag = "activeFragmentTag";
	/**
	 * 添加服务 Fragment 的Tag 标识
	 */
	private final String serviceTag = "serviceFragmentTag";
	/**
	 * 添加众筹 Fragment 的Tag 标识
	 */
	private final String chipsTag = "chipsFragmentTag";
	/**
	 * 添加我 Fragment 的Tag 标识
	 */
	private final String myTag = "myFragmentTag";

	/**
	 * 用于对Fragment进行管理
	 */
	private FragmentManager fragmentManager;
	/**
	 * 服务 Fragment
	 */
	private SecondServiceFragment serviceFragment;
	/**
	 * 活动 Fragment
	 */
	private SecondActivityFragment activityFragment;
	/**
	 * 众筹 Fragment
	 */
	private AllChipsFragment chipsFragment;
	/**
	 * 我的 Fragment
	 */
	private MyFragment mineFragment;
	/**
	 * 当前对象实例
	 */
	public static MainActivity instance;

	// /**
	// * 用来标记是否是在这里进行选座的
	// */
	// private boolean isUser=false;
	// /**
	// * 用来标记界面回调
	// */
	// private final int RESCODE_TAG=1;
	/**
	 * 该标记用来动态显示服务模块的view内容
	 */
	public int tag_view = 0;
	/**
	 * 退出应用提示按钮
	 */
	private CustomDialog exitDialog;
	/**
	 * 扫码控件
	 */
	private ImageView scan;
	/**
	 * 搜索控件
	 */
	private RelativeLayout search;
	/**
	 * 选择城市
	 */
	private ImageView ivAddress;
	/**
	 * 选择城市
	 */
	private TextView tvAddress;
	/**
	 * 弹出侧滑栏
	 */
	private ImageView left;
	/**
	 * 顶部右侧弹出菜单
	 */
	private PopupWindow mPopupWindow;
	/**
	 * 定位失败弹出
	 */
	private PopupWindow resertLotionPopupWindow;
	/**
	 * 顶部右侧弹出菜单布局
	 */
	private LinearLayout mRightLayout;
	/**
	 * 顶部重新定位布局
	 */
	private LinearLayout mTopLayout;
	/**
	 * 底部布局
	 */
	private RelativeLayout mLayoutBottom;
	/**
	 * listview展示城市列表的listview
	 */
	private ListView mListviewLocation;
	/**
	 * 地址列表数据源
	 */
	private List<SupportCity> mListLoactionData;
	/**
	 * 弹窗布局适配器
	 */
	private LocationDataAdapter mLocationAdapter;

	private AMapLocationClient locationClient = null;
	private AMapLocationClientOption locationOption = null;
	/**
	 * 当前城市不支持提示
	 */
	private CustomDialog msgDialog;
	/**
	 * 存储所有支持该app的城市集合
	 */

	/**
	 * 定时器 当定位失败后弹出提示 5s自动消失
	 */
	private Timer timer = new Timer();
	private TimerTask task;
	/**
	 * 定时器
	 */
	private final int TIMER = 110;
	/**
	 * 获取城市数据
	 */
	private final int GET_CITY = 101;

	private int countDown = 0;

	Handler mHandler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			switch (msg.what) {
			case UtilsLocation.MSG_LOCATION_START:
				break;
			// 定位完成
			case UtilsLocation.MSG_LOCATION_FINISH:
				analyseLocation((AMapLocation) msg.obj);
				break;
			case UtilsLocation.MSG_LOCATION_STOP:
				break;
			case GET_CITY:
				anlalyseDate((BackData) msg.obj);
				break;
			case TIMER:
				countDown++;
				if (countDown == 5) {
					if (resertLotionPopupWindow != null
							&& resertLotionPopupWindow.isShowing())
						resertLotionPopupWindow.dismiss();
					timer.cancel();
					countDown = 0;
				}
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 顶部标题
	 */
	private RelativeLayout title;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 启动页结束后立马显示
		startguide();
		setContentView(R.layout.activity_main);
		fragmentManager = getFragmentManager();
		instance = this;
		GlobalParams.context = this;
		/** 友盟 设置是否对日志信息进行加密, 默认false(不加密). */
		AnalyticsConfig.enableEncrypt(true);
		getCityData();
		initView();
		initListener();
		initLocation();
		createDialog();
	}

	/**
	 * 启动引导页
	 */
	private void startguide() {
		SharedPreferences preferences = getSharedPreferences("hasLaunch",
				MODE_PRIVATE);

		boolean hasLaunch = preferences.getBoolean("hasLaunch", false);
		if (!hasLaunch) {
			Intent intent_Launch = new Intent();
			intent_Launch.setClass(MainActivity.this, LaunchActivity.class);
			startActivity(intent_Launch);
			Editor editor = preferences.edit();
			// 存入数据
			editor.putBoolean("hasLaunch", true);
			// 提交修改
			editor.commit();
		}
	}

	/**
	 * 提供给预约列表无数据的时候点击去看看来进行跳转界面
	 */
	public void seeActivity() {
		MyRadioButton button = (MyRadioButton) findViewById(R.id.tag1);
		button.setChecked(true);
		setTabSelection(0);
	}

	/**
	 * 提供给优惠券进行使用优惠券的方法
	 * 
	 * @param isUser2
	 */
	public void initIntentData(String tag) {
		// 该方法一调用，就把底部的按钮进行切换
		MyRadioButton button = (MyRadioButton) findViewById(R.id.tag2);
		button.setChecked(true);
		if (tag != null && !TextUtils.isEmpty(tag)) {
			if (tag.equals("canyin")) {
				// 说明目前是餐饮在线传递过来的意图
				// tag_view=0;
				radioGroup.check(R.id.tag2);
				// 同时在这里修改一下全局变量
			} else if (tag.equals("shangpin")) {
				// 说明是商品传递过来的意图
				// tag_view=2;
				radioGroup.check(R.id.tag2);
			} else if (tag.equals("yongche")) {
				// 说明是用车
				// tag_view=3;
				radioGroup.check(R.id.tag2);
			} else if (tag.equals("goupiao")) {
				// 这里需要进入购票界面
				radioGroup.check(R.id.tag1);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_scan:
			// 跳转到扫码界面
			Intent intent = new Intent(MainActivity.this,
					ShoppingActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_search:
			// 跳转到搜索界面
			Intent searchIntent = new Intent();
			searchIntent.setClass(MainActivity.this, SearchActivity.class);
			startActivity(searchIntent);
			break;
		case R.id.iv_address:
		case R.id.tv_address:
			// 弹出城市选择控件
			if (mListLoactionData != null && mListLoactionData.size() > 0) {
				showPopupWindows(ivAddress, mListLoactionData);
			} else {
				// 提示用户，当前暂无城市可选
				getCityData();
				Toast.makeText(MainActivity.this, "获取数据失败", Toast.LENGTH_SHORT)
						.show();
			}

			break;
		case R.id.iv_left:
			startMineActivity();
			break;

		default:
			break;
		}
	}

	public void initView() {
		title = (RelativeLayout) findViewById(R.id.title);
		scan = (ImageView) title.findViewById(R.id.iv_scan);
		left = (ImageView) title.findViewById(R.id.iv_left);
		search = (RelativeLayout) title.findViewById(R.id.rl_search);
		ivAddress = (ImageView) title.findViewById(R.id.iv_address);
		tvAddress = (TextView) title.findViewById(R.id.tv_address);
		// 获得dialog的window窗口
		mLayoutBottom = (RelativeLayout) findViewById(R.id.layout_xuyaoxianhsi_shouyemian);
		radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
		setTabSelection(0);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.tag1:
					setTabSelection(0);
					break;
				case R.id.tag2:
					setTabSelection(1);
					break;
				case R.id.tag3:
					setTabSelection(2);
					if (resertLotionPopupWindow != null
							&& resertLotionPopupWindow.isShowing())
						resertLotionPopupWindow.dismiss();
					break;
				// case R.id.tag4:
				// setTabSelection(3);
				// break;
				default:
					break;
				}

			}
		});

	}

	private void setTabSelection_choise(int index) {
		// 每次选中之前先清楚掉上次的选中状态
		// clearSelection();
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);
		if (serviceFragment == null) {
			// 给这个构造方法添加一个标记，默认为0，显示餐饮
			// serviceFragment = new ServiceFragment();
			// serviceFragment = new ServiceFragment(tag_view);
			transaction.add(R.id.main_content, serviceFragment, serviceTag);
		} else {
			// 如果MessageFragment不为空，则直接将它显示出来
			transaction.show(serviceFragment);
		}

		transaction.commitAllowingStateLoss();
	}

	private void setTabSelection(int index) {
		// 每次选中之前先清楚掉上次的选中状态
		// clearSelection();
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);
		switch (index) {
		case 0:
			MobclickAgent.onEvent(MainActivity.this, "ActivityPage");
			if (activityFragment == null) {
				// 如果ContactsFragment为空，则创建一个并添加到界面上
				activityFragment = new SecondActivityFragment();
				transaction.add(R.id.main_content, activityFragment, activeTag);
			} else {
				// 如果ContactsFragment不为空，则直接将它显示出来
				transaction.show(activityFragment);
				activityFragment.onResume();
			}
			break;
		case 1:
			MobclickAgent.onEvent(MainActivity.this, "ServicePage");
			if (serviceFragment == null) {
				// 给这个构造方法添加一个标记，默认为0，显示餐饮
				serviceFragment = new SecondServiceFragment();
				transaction.add(R.id.main_content, serviceFragment, serviceTag);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(serviceFragment);
				serviceFragment.onResume();
			}
			break;
		case 2:
			MobclickAgent.onEvent(MainActivity.this, "CrowdFundingPage");
			if (chipsFragment == null) {
				// 如果NewsFragment为空，则创建一个并添加到界面上
				chipsFragment = new AllChipsFragment();
				transaction.add(R.id.main_content, chipsFragment, chipsTag);
			} else {
				// 如果NewsFragment不为空，则直接将它显示出来
				transaction.show(chipsFragment);
				chipsFragment.onResume();
			}
			break;
		case 3:

		default:

			if (mineFragment == null) {
				// 如果SettingFragment为空，则创建一个并添加到界面上
				mineFragment = new MyFragment();
				transaction.add(R.id.main_content, mineFragment, myTag);
			} else {
				// 如果SettingFragment不为空，则直接将它显示出来
				transaction.show(mineFragment);
				mineFragment.onResume();
			}
			break;
		}
		transaction.commitAllowingStateLoss();
	}

	/**
	 * 
	 * @Title: hideFragments
	 * @Description: 根据Tag 标识获取 相应的Fragment 并隐藏
	 * @param: @param transaction
	 * @return: void
	 * @throws
	 */
	private void hideFragments(FragmentTransaction transaction) {
		activityFragment = (SecondActivityFragment) fragmentManager
				.findFragmentByTag(activeTag);
		serviceFragment = (SecondServiceFragment) fragmentManager
				.findFragmentByTag(serviceTag);
		chipsFragment = (AllChipsFragment) fragmentManager
				.findFragmentByTag(chipsTag);
		mineFragment = (MyFragment) fragmentManager.findFragmentByTag(myTag);
		if (activityFragment != null) {
			transaction.hide(activityFragment);
		}
		if (serviceFragment != null) {
			transaction.hide(serviceFragment);
		}
		if (chipsFragment != null) {
			transaction.hide(chipsFragment);
		}
		if (mineFragment != null) {
			transaction.hide(mineFragment);
		}
	}

	/**
	 * 
	 * @Title: initListener
	 * @Description: 初始化监听
	 * @param:
	 * @return: void
	 * @throws
	 */
	public void initListener() {
		scan.setOnClickListener(this);
		left.setOnClickListener(this);
		search.setOnClickListener(this);
		ivAddress.setOnClickListener(this);
		tvAddress.setOnClickListener(this);
		tvAddress.setClickable(true);
	}

	@Override
	public void onBackPressed() {
		// PromptManager.showExitSystem(this);
		exitDialog.setRemindTitle("温馨提示");
		exitDialog.setCancelTxt("确定");
		exitDialog.setConfirmTxt("取消");
		exitDialog.setRemindMessage("确定退出应用么");
		exitDialog.show();
		return;
	}

	@Override
	protected void onResume() {
		JPushInterface.onResume(this);
		super.onResume();
		MobclickAgent.onResume(this);
		fragmentManager = getFragmentManager();
		mLayoutBottom.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
		MobclickAgent.onPause(this);
	}

	/**
	 * 隐藏底部
	 */
	public void setLayoutGone() {
		if (mLayoutBottom != null) {
			mLayoutBottom.setVisibility(View.INVISIBLE);
		}

	}

	/**
	 * 显示
	 */
	public void setLayoutVisible() {
		if (mLayoutBottom != null) {
			mLayoutBottom.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (null != amapLocation) {
			Message msg = mHandler.obtainMessage();
			msg.obj = amapLocation;
			msg.what = UtilsLocation.MSG_LOCATION_FINISH;
			if(mHandler!=null)
				mHandler.sendMessage(msg);
		}
	}

	private void initLocation() {
		locationClient = new AMapLocationClient(this.getApplicationContext());
		locationOption = new AMapLocationClientOption();

		// 设置定位模式为低功耗模式
		locationOption.setLocationMode(AMapLocationMode.Battery_Saving);
		// 设置定位监听
		locationClient.setLocationListener(this);

		// 设置是否需要显示地址信息
		locationOption.setNeedAddress(true);
		// 设置是否开启缓存
		locationOption.setNeedAddress(true);
		// 设置是否只定位一次,默认为false
		locationOption.setOnceLocation(true);

		locationOption.setInterval(1000);

		// 设置定位参数
		locationClient.setLocationOption(locationOption);
		// 启动定位
		locationClient.startLocation();
		mHandler.sendEmptyMessage(UtilsLocation.MSG_LOCATION_START);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		// 删除广告条展示
		instance = null;
		LApplication application = (LApplication) getApplication();
		if (null != application) {
			application.removeView();
		}
		SharedPreferences preferences = getSharedPreferences("hasLaunch",
				MODE_PRIVATE);
		if (null != preferences) {
			Editor editor = preferences.edit();
			// 存入数据
			editor.putBoolean("hasLaunch", false);
			// 提交修改
			editor.commit();
		}
		if (null != LApplication.dbBuyCar)
			LApplication.dbBuyCar.delete(null, null);
		if (null != LApplication.dbBuyCar)
			LApplication.foodbuycar.delete(null, null);
		if (mListLoactionData != null)
			mListLoactionData.clear();
		mListLoactionData = null;
		LApplication.dbBuyCar = null;
		LApplication.foodbuycar = null;
		serviceFragment = null;
		chipsFragment = null;
		activityFragment = null;
		if (timer != null)
			timer.cancel();
		if (task != null)
			task.cancel();
		System.gc();
		super.onDestroy();
	}

	/**
	 * 创建退出提示框
	 */
	private void createDialog() {
		exitDialog = new CustomDialog(this, new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 取消
				exitDialog.dismiss();
			}
		}, new OnClickListener() {

			@Override
			public void onClick(View v) {
				((LApplication) getApplication()).removeView();
				SharedPreferences preferences = getSharedPreferences(
						"hasLaunch", MODE_PRIVATE);
				Editor editor = preferences.edit();
				// 存入数据
				editor.putBoolean("hasLaunch", false);
				// 提交修改
				editor.commit();
				if (null != LApplication.dbBuyCar)
					LApplication.dbBuyCar.delete(null, null);
				if (null != LApplication.dbBuyCar)
					LApplication.foodbuycar.delete(null, null);
				LApplication.dbBuyCar = null;
				LApplication.foodbuycar = null;
				instance = null;
				// 杀死进程前,保存友盟统计的数据
				MobclickAgent.onKillProcess(MainActivity.this);
				// 退出应用
				LApplication application = (LApplication) getApplication();
				application.exit();
				android.os.Process.killProcess(android.os.Process.myPid());
				exitDialog.dismiss();
			}
		});
	}

	/**
	 * 根据传入的控件资，以及坐标点，来弹出一个window
	 */

	@SuppressWarnings("deprecation")
	public void showPopupWindows(View view, List<SupportCity> list) {
		mRightLayout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.sence_dialog_sort, null);
		// 初始化该布局内控件
		initWindowViews(mRightLayout, list);
		mPopupWindow = new PopupWindow(this);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setWidth(DensityUtil.dip2px(this, 90));
		if (list.size() == 2) {
			mPopupWindow.setHeight(DensityUtil.dip2px(this, 75));
		} else if (list.size() == 1) {
			mPopupWindow.setHeight(DensityUtil.dip2px(this, 35));
		} else if (list.size() > 2) {
			mPopupWindow.setHeight(DensityUtil.dip2px(this, 110));
		} else if (list.size() == 0) {
			mPopupWindow.setHeight(DensityUtil.dip2px(this, 110));
		}

		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setContentView(mRightLayout);
		// mPopupWindow.showAtLocation(view, Gravity.RIGHT | Gravity.TOP, x, y);
		mPopupWindow.showAsDropDown(view);
	}

	/**
	 * 重新定位popupwindow
	 */
	public void showPopupWindowsLocation(View view) {
		mTopLayout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.mainactivity_top_popup, null);
		resertLotionPopupWindow = new PopupWindow(this);
		resertLotionPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		resertLotionPopupWindow.setWidth(LayoutParams.FILL_PARENT);
		resertLotionPopupWindow.setHeight(DensityUtil.dip2px(this, 35));
		resertLotionPopupWindow.setOutsideTouchable(false);
		resertLotionPopupWindow.setFocusable(false);
		resertLotionPopupWindow.setContentView(mTopLayout);
		// mPopupWindow.showAtLocation(view, Gravity.RIGHT | Gravity.TOP, x, y);
		resertLotionPopupWindow.showAsDropDown(view);
		LinearLayout ll_top_popup = (LinearLayout) mTopLayout
				.findViewById(R.id.ll_top_popup);
		ll_top_popup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 启动定位
				resertLotionPopupWindow.dismiss();
				locationClient.setLocationOption(locationOption);
				locationClient.startLocation();
			}
		});
	}

	/*
	 * 获取该布局内控件，并且进行初始化操作
	 */
	private void initWindowViews(LinearLayout mRightLayout,
			List<SupportCity> list) {
		mListviewLocation = (ListView) mRightLayout
				.findViewById(R.id.window_listview_data_view);
		if (mLocationAdapter != null) {
			mLocationAdapter.setlist(list);
			mListviewLocation.setAdapter(mLocationAdapter);
		} else {
			mLocationAdapter = new LocationDataAdapter(list, MainActivity.this);
			mListviewLocation.setAdapter(mLocationAdapter);
		}
	}

	/**
	 * 启动我的界面
	 */
	private void startMineActivity() {
		Intent intent = new Intent(MainActivity.this, MineActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out1);
	}

	/**
	 * 提示当前城市不支持该应用信息
	 * 
	 * 
	 */
	private void showDiaLog() {
		msgDialog = null;
		msgDialog = new CustomDialog(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tvAddress.setText("未知");
				msgDialog.dismiss();
			}
		});
		msgDialog.setRemindMessage("您的城市暂不支持,我们会努力的");
		msgDialog.setRemindTitle("温馨提示");
		msgDialog.setConfirmTxt("好的");
		msgDialog.show();
	}

	/**
	 * 获取支持的城市列表
	 */
	public void getCityData() {
		if (mListLoactionData != null && mListLoactionData.size() > 0)
			return;
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.SUPPORT_CITY, null, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if(mHandler==null)
							return;
						Log.i("123",
								data.getNetResultCode() + ",,,,"
										+ data.getObject());
						Message msg = new Message();
						msg.obj = data;
						msg.what = GET_CITY;
						mHandler.sendMessage(msg);
					}
				}, false, false);
	}

	private void anlalyseDate(BackData backData) {
		String currentCityName = "";
		if (backData.getNetResultCode() == 0
				&& backData.getObject().toString() != null
				&& !"".equals(backData.getObject().toString())) {
			Gson gson = new Gson();
			Object bean = gson.fromJson(backData.getObject().toString(),
					new TypeToken<List<SupportCity>>() {
					}.getType());
			mListLoactionData = (List<SupportCity>) bean;
			if (GlobalParams.MY_LOCATION != null) {
				currentCityName = GlobalParams.MY_LOCATION.getCity();
				if (!TextUtils.isEmpty(currentCityName)) {
					if (currentCityName.endsWith("市")) {
						int index = currentCityName.lastIndexOf("市");
						currentCityName = currentCityName.substring(0, index);
					}
				}

				for (int i = 0; i < mListLoactionData.size(); i++) {
					SupportCity beans = mListLoactionData.get(i);
					if (!TextUtils.isEmpty(currentCityName)) {
						if (beans.getShortName().contains(currentCityName)) {
							tvAddress.setText(beans.getShortName());
							GlobalParams.CITY_ID = beans.getId();
							return;
						}
					}
				}

				tvAddress.setText("未知");
				showDiaLog();
			}
		}
	}

	private void analyseLocation(AMapLocation amapLocation) {
		// String result = UtilsLocation.getLocationStr(amapLocation);
		if (amapLocation != null && amapLocation.getErrorCode() == 0) {
			// 当前位置
			Address address = new Address();
			// 设置具体问题
			address.setSpecificLocation(amapLocation.getAddress());
			address.setAddress(amapLocation.getPoiName());
			address.setLatitude(amapLocation.getLatitude());
			address.setLongitude(amapLocation.getLongitude());
			// 当前城市
			address.setCity(amapLocation.getCity());
			GlobalParams.MY_LOCATION = address;
			String currentCityName = address.getCity();
			if (!TextUtils.isEmpty(currentCityName)) {
				if (currentCityName.endsWith("市")) {
					int index = currentCityName.lastIndexOf("市");
					currentCityName = currentCityName.substring(0, index);
					if (mListLoactionData != null) {
						for (SupportCity bean : mListLoactionData) {
							if (bean.getShortName().contains(currentCityName)) {
								tvAddress.setText(bean.getShortName());
								GlobalParams.CITY_ID = bean.getId();
								return;
							}
						}
						tvAddress.setText("未知");
						showDiaLog();
					}
				}
			}
		} else {
			// 定位失败
			// 停止定位
			locationClient.stopLocation();
			if (resertLotionPopupWindow == null
					|| !resertLotionPopupWindow.isShowing()) {
				showPopupWindowsLocation(title);
				if (timer != null) {
					timer.cancel();
					timer = null;
					timer = new Timer();
				}
				initTask();
				countDown = 0;
				timer.schedule(task, 2000, 1000);
			}
		}
	}

	public void disMiss(SupportCity city) {
		if (city == null)
			return;
		tvAddress.setText(city.getShortName());
		GlobalParams.CITY_ID = city.getId();
		mPopupWindow.dismiss();
		refreshDate();
	}

	private void refreshDate() {
		if (activityFragment != null)
			activityFragment.initListviewDate();
		if (serviceFragment != null)
			serviceFragment.initDate();
	}

	private void initTask() {
		if (task != null) {
			task = null;
		}
		task = new TimerTask() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = TIMER;
				if(mHandler!=null)
					mHandler.sendMessage(message);
			}
		};
	}

}
