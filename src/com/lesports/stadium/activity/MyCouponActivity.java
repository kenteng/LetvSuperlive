package com.lesports.stadium.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lesports.stadium.R;
import com.lesports.stadium.bean.OrderListBean;
import com.lesports.stadium.bean.YouHuiBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.view.AllCouponView;
import com.lesports.stadium.view.CouponOveredView;
import com.lesports.stadium.view.CouponingView;
import com.lesports.stadium.view.CustomDialog;

/**
 * 
 * ***************************************************************
 * 
 * @ClassName: MineFragment
 * 
 * @Desc : 我 的 优惠券
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : lwc
 * 
 * @data:2016-1-29 上午11:38:42
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class MyCouponActivity extends Activity implements OnClickListener {
	private View tabImgView;
	/*
	 * 全部
	 */
	private TextView t_order_all;
	/*
	 * 可用
	 */
	private TextView t_order_wait_pay;
	/*
	 * 失效
	 */
	private TextView t_order_wait_send;
	/*
	 * VewPager 对象
	 */
	private ViewPager categoryPager;
	/*
	 * viewPager适配器
	 */
	private CategoryPageAdapter categoryAdapter;

	/**
	 * 全部
	 */
	private static final int ORDER_ALL = 0;
	/**
	 * 可用
	 */
	private static final int ORDER_WAIT_PAY = 1;
	/**
	 * 失效
	 */
	private static final int ORDER_WAIT_SEND = 2;
	/*
	 * 存储数据
	 */
	private ArrayList<View> pagerList;
	/**
	 * 选项卡最后选择的是哪个
	 */
	private int lastPager = 0;
	/*
	 * 全部view
	 */
	private AllCouponView allCouponView;
	/*
	 * 可用view
	 */
	private CouponingView couponingView;
	/*
	 * 失效view
	 */
	private CouponOveredView couponOveredView;

	/**
	 * 优惠券数据源
	 */
	private List<YouHuiBean> mList;

	public static MyCouponActivity intance;
	private CustomDialog exitDialog;
	/**
	 * 我的优惠券按钮
	 */
	private TextView mWodeyouhuiquan;
	/**
	 * 顶部标题栏按钮
	 */
	private RelativeLayout mTitleLayout;
	/**
	 * 返回按钮
	 */
	private ImageView mBack;
	/**
	 * 需要弹窗的layout
	 */
	private LinearLayout ll_popul_filter;
	/**
	 * window
	 */
	private PopupWindow popFilter = null;
	private ImageView im_quanbu;
	private ImageView im_canyin;
	private ImageView im_shangpin;
	private ImageView im_menpiao;
	private ImageView im_yongche;
	private ImageView im_zhongchou;
	private TextView mQUANBU, mCANYIN, mSHANGPN, mYONGCHE, mgoupiao,
			mzhongchou;
	private int IS_SHOW_VIEW = 0;// 默认为0，也就是全部
	/**
	 * handle里面需要用的标记
	 */
	private final int COUPON_GET_SUCCESS=1;
	/**
	 * 处理数据的handle
	 */
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case COUPON_GET_SUCCESS:
				String backdata = (String) msg.obj;
				if (!TextUtils.isEmpty(backdata)) {
					Log.i("优惠券数据", backdata);
					// 调用方法，进行解析
					mList = useWayJsonData(backdata);
					// 调用方法，进行分拣数据
					if (mList != null && mList.size() != 0) {
						useWayFenjianData(mList);
					}
				}
				break;
			case 2:
				// 说明无数据
				// 通知界面显示无数据状态
				notifyNoData();
				break;

			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myguess);
		intance = this;
		initview();
		initListener();
		// 调用方法获取用户优惠信息
		initpopuwindow();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!TextUtils.isEmpty(GlobalParams.USER_ID)) {
			useWayGetData();
		} else {
			createDialog();
		}

	}

	/**
	 * 该方法用来在未登录的时候调用，显示提示信息，让用户来进行登录
	 */
	public void createDialog() {
		exitDialog = new CustomDialog(this, new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyCouponActivity.this,
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
		exitDialog.setCancelTxt("取消");
		exitDialog.setConfirmTxt("立即登录");
		exitDialog.setRemindTitle("温馨提示");
		exitDialog.setRemindMessage("只有登录以后才能查看用户优惠信息哦");
		exitDialog.show();
	}

	/**
	 * 按照可用和不可用来讲数据进行分拣出来，并且分别传递到相对饮的界面view中
	 * 
	 * @param mList
	 */
	private void useWayFenjianData(List<YouHuiBean> mLists) {
		// TODO Auto-generated method stub
		List<YouHuiBean> list_yes = new ArrayList<YouHuiBean>();
		List<YouHuiBean> list_no = new ArrayList<YouHuiBean>();
		if (mLists != null && mLists.size() != 0) {

			int count = mLists.size();
			for (int i = 0; i < count; i++) {
				if (mLists.get(i).getStatus().equals("1")&&mLists.get(i).getInusing().equals("0")) {
					// 说明可用
					list_yes.add(mLists.get(i));
				} else {
					// 说明不可用
					list_no.add(mLists.get(i));
				}
			}
		}
		// 用view对象来调用，加载界面
		if (mLists != null && mLists.size() != 0) {
			allCouponView.setList(mLists);
		} else {
			allCouponView.setEmpty();
		}
		if (list_yes != null && list_yes.size() != 0) {
			couponingView.setList(list_yes);
		} else {
			couponingView.setEmpty();
		}
		if (list_no != null && list_no.size() != 0) {
			couponOveredView.setList(list_no);
		} else {
			couponOveredView.setEmpty();
		}

	}

	/**
	 * 提示界面暂无数据的状态
	 */
	private void notifyNoData() {
		// TODO Auto-generated method stub
		allCouponView.setEmpty();
		couponingView.setEmpty();
		couponOveredView.setEmpty();
	}

	/**
	 * 解析优惠数据
	 * 
	 * @param backdata
	 * @return
	 */
	private List<YouHuiBean> useWayJsonData(String backdata) {
		// TODO Auto-generated method stub
		List<YouHuiBean> list = new ArrayList<YouHuiBean>();
		try {
			JSONArray array = new JSONArray(backdata);
			int count = array.length();
			for (int i = 0; i < count; i++) {
				JSONObject obj = array.getJSONObject(i);
				YouHuiBean bean = new YouHuiBean();
				if (obj.has("beginTime")) {
					bean.setBeginTime(obj.getString("beginTime"));
				}
				if (obj.has("couponCondition")) {
					bean.setCouponCondition(obj.getString("couponCondition"));
				}
				if (obj.has("couponPrice")) {
					bean.setCouponPrice(obj.getString("couponPrice"));
				}
				if (obj.has("couponType")) {
					bean.setCouponType(obj.getString("couponType"));
				}
				if (obj.has("endTime")) {
					bean.setEndTime(obj.getString("endTime"));
				}
				if (obj.has("id")) {
					bean.setId(obj.getString("id"));
				}
				if (obj.has("status")) {
					bean.setStatus(obj.getString("status"));
				}
				if(obj.has("myCouponId")){
					bean.setMyCouponId(obj.getString("myCouponId"));
				}
				if(obj.has("inusing")){
					bean.setInusing(obj.getString("inusing"));
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

	private void initpopuwindow() {
		// TODO Auto-generated method stub
		popFilter = new PopupWindow(MyCouponActivity.this);
		LayoutInflater inflater = (LayoutInflater) MyCouponActivity.this
				.getSystemService(MyCouponActivity.this.LAYOUT_INFLATER_SERVICE);
		View viewFilter = inflater.inflate(R.layout.window_order, null);
		initWindowListvew(viewFilter);
		ll_popul_filter = (LinearLayout) viewFilter
				.findViewById(R.id.layout_window_order);
		int ScreenHeight = getScreenHeight(MyCouponActivity.this);
		popFilter.setWidth(LayoutParams.MATCH_PARENT);
		popFilter.setHeight(ScreenHeight / 2);
		popFilter.setBackgroundDrawable(new BitmapDrawable());
		popFilter.setFocusable(true);
		popFilter.setOutsideTouchable(true);
		popFilter.setContentView(viewFilter);
	}

	/**
	 * 获得屏幕高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.heightPixels;
	}

	/**
	 * 初始化菜单选择项
	 * 
	 * @param viewFilter
	 */
	private void initWindowListvew(View viewFilter) {
		mQUANBU = (TextView) viewFilter
				.findViewById(R.id.order_window_menu_title_quanbu);
		mCANYIN = (TextView) viewFilter
				.findViewById(R.id.order_window_menu_title_canyin);
		mCANYIN.setText("餐饮券");
		mSHANGPN = (TextView) viewFilter
				.findViewById(R.id.order_window_menu_title_shangpin);
		mSHANGPN.setText("购物券");
		mzhongchou = (TextView) viewFilter
				.findViewById(R.id.order_window_menu_title_zhongchou);
		mYONGCHE = (TextView) viewFilter
				.findViewById(R.id.order_window_menu_title_yongche);
		mYONGCHE.setText("用车券");
		mgoupiao = (TextView) viewFilter
				.findViewById(R.id.order_window_menu_title_menpiao);
		mgoupiao.setText("购票券");
		RelativeLayout layout_quanbu = (RelativeLayout) viewFilter
				.findViewById(R.id.window_layout_quanbu);
		layout_quanbu.setOnClickListener(this);
		im_quanbu = (ImageView) viewFilter
				.findViewById(R.id.order_window_menu_im_quanbu);
		im_quanbu.setVisibility(View.VISIBLE);
		RelativeLayout layout_canyin = (RelativeLayout) viewFilter
				.findViewById(R.id.window_layout_canyin);
		layout_canyin.setOnClickListener(this);
		im_canyin = (ImageView) viewFilter
				.findViewById(R.id.order_window_menu_im_canyin);
		im_canyin.setVisibility(View.GONE);
		RelativeLayout layout_shangpin = (RelativeLayout) viewFilter
				.findViewById(R.id.window_layout_shangpin);
		layout_shangpin.setOnClickListener(this);
		im_shangpin = (ImageView) viewFilter
				.findViewById(R.id.order_window_menu_im_shangpiin);
		im_shangpin.setVisibility(View.GONE);
		RelativeLayout layout_menpiao = (RelativeLayout) viewFilter
				.findViewById(R.id.window_layout_menpiao);
		layout_menpiao.setOnClickListener(this);
		im_menpiao = (ImageView) viewFilter
				.findViewById(R.id.order_window_menu_im_menpiao);
		im_menpiao.setVisibility(View.GONE);
		RelativeLayout layout_yongche = (RelativeLayout) viewFilter
				.findViewById(R.id.window_layout_yongche);
		layout_yongche.setOnClickListener(this);
		im_yongche = (ImageView) viewFilter
				.findViewById(R.id.order_window_menu_im_yongche);
		im_yongche.setVisibility(View.GONE);
		RelativeLayout layout_zhongchou = (RelativeLayout) viewFilter
				.findViewById(R.id.window_layout_zhongchou);
		layout_zhongchou.setVisibility(View.GONE);
		layout_zhongchou.setOnClickListener(this);
		im_zhongchou = (ImageView) viewFilter
				.findViewById(R.id.order_window_menu_im_zhongchou);
		im_zhongchou.setVisibility(View.GONE);

	}

	/**
	 * 网络获取优惠券数据
	 */
	private void useWayGetData() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("couponType", "");
		params.put("status", "");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.YOUHUIQUAN_LIST, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						// TODO Auto-generated method stub
						if (data == null) {
							// 说明网络获取无数据
						} else {
							Object obj = data.getObject();
							if (obj == null) {
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
								} else {
									if (data.getNetResultCode() == 0) {
										Log.i("优惠券信息",
												"走到这里了么？"
														+ data.getNetResultCode()
														+ backdata);
										Message sendMessage = new Message();
										sendMessage.arg1 = 1;
										sendMessage.obj = backdata;
										if(null!=handler){
											handler.sendMessage(sendMessage);
										}
									} else {
										Message msg = new Message();
										msg.arg1 = 2;
										if(null!=handler){
											handler.sendMessage(msg);
										}
										
									}

								}
							}

						}
					}
				}, false, false);

	}

	/**
	 * 初始化监听事件
	 */
	private void initListener() {
		t_order_all.setOnClickListener(this);
		t_order_wait_pay.setOnClickListener(this);
		t_order_wait_send.setOnClickListener(this);
		categoryPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				TranslateAnimation animation = new TranslateAnimation(lastPager
						* GlobalParams.WIN_WIDTH / 3, position
						* GlobalParams.WIN_WIDTH / 3, 0, 0);

				animation.setDuration(300);
				animation.setFillAfter(true);
				tabImgView.startAnimation(animation);

				lastPager = position;

				t_order_all.setTextColor(getResources().getColor(
						R.color.word_gray));
				t_order_wait_pay.setTextColor(getResources().getColor(
						R.color.word_gray));
				t_order_wait_send.setTextColor(getResources().getColor(
						R.color.word_gray));

				switch (position) {
				case ORDER_ALL:
					t_order_all.setTextColor(getResources().getColor(
							R.color.service_select_txt));
					break;
				case ORDER_WAIT_PAY:
					t_order_wait_pay.setTextColor(getResources().getColor(
							R.color.service_select_txt));
					break;
				case ORDER_WAIT_SEND:
					t_order_wait_send.setTextColor(getResources().getColor(
							R.color.service_select_txt));
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

	}

	private void initDate() {
		pagerList = new ArrayList<View>();
		allCouponView = new AllCouponView(this);
		pagerList.add(allCouponView.getView());
		couponingView = new CouponingView(this);
		pagerList.add(couponingView.getView());
		couponOveredView = new CouponOveredView(this);
		pagerList.add(couponOveredView.getView());

	}

	private void initview() {
		mTitleLayout = (RelativeLayout) findViewById(R.id.layout_youhuiquan_dingbu);
		mWodeyouhuiquan = (TextView) findViewById(R.id.wodeyouhuiquan_title);
		mWodeyouhuiquan.setOnClickListener(this);
		mBack = (ImageView) findViewById(R.id.wodeyoouhuiquan_back);
		mBack.setOnClickListener(this);
		t_order_all = (TextView) findViewById(R.id.t_order_all);
		t_order_wait_pay = (TextView) findViewById(R.id.t_order_wait_pay);
		t_order_wait_send = (TextView) findViewById(R.id.t_order_wait_send);
		tabImgView = (View) findViewById(R.id.tabImgView);
		LayoutParams params = (LayoutParams) tabImgView.getLayoutParams();
		params.width = GlobalParams.WIN_WIDTH / 3;
		tabImgView.setLayoutParams(params);
		categoryPager = (ViewPager) findViewById(R.id.pager);
		categoryPager.setOffscreenPageLimit(1);
		initDate();
		categoryAdapter = new CategoryPageAdapter();
		categoryPager.setAdapter(categoryAdapter);
		setSelectPager(0);

	}

	private class CategoryPageAdapter extends PagerAdapter {

		public Object instantiateItem(ViewGroup container, int position) {
			View view = null;
			view = pagerList.get(position);
			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View view = pagerList.get(position);
			container.removeView(view);
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	/**
	 * 设置选中项
	 */
	public void setSelectPager(int position) {
		TranslateAnimation animation = new TranslateAnimation(lastPager
				* GlobalParams.WIN_WIDTH / 3, position * GlobalParams.WIN_WIDTH
				/ 3, 0, 0);
		animation.setFillAfter(true);
		tabImgView.startAnimation(animation);
		lastPager = position;
		switch (position) {
		case ORDER_ALL:
			t_order_all.setTextColor(getResources().getColor(
					R.color.service_select_txt));
			categoryPager.setCurrentItem(ORDER_ALL);
			break;
		case ORDER_WAIT_PAY:
			t_order_wait_pay.setTextColor(getResources().getColor(
					R.color.service_select_txt));
			categoryPager.setCurrentItem(ORDER_WAIT_PAY);
			break;
		case ORDER_WAIT_SEND:
			t_order_wait_send.setTextColor(getResources().getColor(
					R.color.service_select_txt));
			categoryPager.setCurrentItem(ORDER_WAIT_SEND);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.t_order_all:
			IS_SHOW_VIEW = 0;
			categoryPager.setCurrentItem(ORDER_ALL);
			break;
		case R.id.t_order_wait_pay:
			IS_SHOW_VIEW = 1;
			categoryPager.setCurrentItem(ORDER_WAIT_PAY);
			break;
		case R.id.t_order_wait_send:
			IS_SHOW_VIEW = 2;
			categoryPager.setCurrentItem(ORDER_WAIT_SEND);
			break;
		case R.id.wodeyoouhuiquan_back:
			finish();
			break;
		case R.id.wodeyouhuiquan_title:
			// 需要弹出window来进行筛选
			ll_popul_filter.startAnimation(AnimationUtils.loadAnimation(
					MyCouponActivity.this, R.anim.activity_translate_in));
			popFilter.showAsDropDown(mTitleLayout);
			break;
		case R.id.window_layout_quanbu:
			// 说明需要展示全部数据
			mWodeyouhuiquan.setText("全部");
			mQUANBU.setTextColor(Color.rgb(22, 154, 218));
			mCANYIN.setTextColor(Color.WHITE);
			mSHANGPN.setTextColor(Color.WHITE);
			mzhongchou.setTextColor(Color.WHITE);
			mYONGCHE.setTextColor(Color.WHITE);
			mgoupiao.setTextColor(Color.WHITE);
			im_quanbu.setVisibility(View.VISIBLE);
			im_canyin.setVisibility(View.GONE);
			im_shangpin.setVisibility(View.GONE);
			im_menpiao.setVisibility(View.GONE);
			im_yongche.setVisibility(View.GONE);
			im_zhongchou.setVisibility(View.GONE);
			// 首先需要判断标记，是哪一类型的全部
			switch (IS_SHOW_VIEW) {
			case 0:
				// 显示全部优惠券的全部
				if (mList != null && mList.size() != 0) {
					allCouponView.setList(mList);
				} else {
					allCouponView.setEmpty();
				}
				break;
			case 1:
				// 显示可用优惠券的全部
				if (mList != null && mList.size() != 0) {
					// 调用方法，分拣出全部可用的优惠券
					List<YouHuiBean> list_keyongall = useWaycheckData_keyongall(mList);
					if (list_keyongall != null && list_keyongall.size() != 0) {
						couponingView.setList(list_keyongall);
					} else {
						couponingView.setEmpty();
					}

				} else {
					couponingView.setEmpty();
				}
				break;
			case 2:
				// 显示不可用优惠券的全部
				if (mList != null && mList.size() != 0) {
					// 调用方法，分拣出全部可用的优惠券
					List<YouHuiBean> list_bukeyongall = useWaycheckData_bukeyongall(mList);
					if (list_bukeyongall != null
							&& list_bukeyongall.size() != 0) {
						couponOveredView.setList(list_bukeyongall);
					} else {
						couponOveredView.setEmpty();
					}

				} else {
					couponOveredView.setEmpty();
				}
			default:
				break;
			}
			popFilter.dismiss();
			break;
		case R.id.window_layout_canyin:
			// 说明是餐饮
			mWodeyouhuiquan.setText("餐饮券");
			mQUANBU.setTextColor(Color.WHITE);
			mCANYIN.setTextColor(Color.rgb(22, 154, 218));
			mSHANGPN.setTextColor(Color.WHITE);
			mzhongchou.setTextColor(Color.WHITE);
			mYONGCHE.setTextColor(Color.WHITE);
			mgoupiao.setTextColor(Color.WHITE);
			im_quanbu.setVisibility(View.GONE);
			im_canyin.setVisibility(View.VISIBLE);
			im_shangpin.setVisibility(View.GONE);
			im_menpiao.setVisibility(View.GONE);
			im_yongche.setVisibility(View.GONE);
			im_zhongchou.setVisibility(View.GONE);
			switch (IS_SHOW_VIEW) {
			case 0:
				// 当前是在全部的view界面来切换，所以展示全部餐饮的数据
				if (mList != null && mList.size() != 0) {
					List<YouHuiBean> list_canyinall = useWaycheckData_all_canyin(mList);
					if (list_canyinall != null && list_canyinall.size() != 0) {
						allCouponView.setList(list_canyinall);
					} else {
						allCouponView.setEmpty();
					}

				} else {
					allCouponView.setEmpty();
				}
				break;
			case 1:
				// 当前是在可用的view界面来切换，所以这里展示代支付的餐饮数据
				if (mList != null && mList.size() != 0) {
					// 调用方法，分拣出全部可用的优惠券
					List<YouHuiBean> list_keyongall_canyin = useWaycheckData_keyongall_canyin(mList);
					if (list_keyongall_canyin != null
							&& list_keyongall_canyin.size() != 0) {
						couponingView.setList(list_keyongall_canyin);
					} else {
						couponingView.setEmpty();
					}

				} else {
					couponingView.setEmpty();
				}
				break;
			case 2:
				// 说明当前是不可用的view里面来点击了餐饮，所以在代发货界面只展示代发货的餐饮数据
				// 显示不可用优惠券的全部
				if (mList != null && mList.size() != 0) {
					// 调用方法，分拣出全部可用的优惠券
					List<YouHuiBean> list_bukeyongall_canyin = useWaycheckData_bukeyongall_canyin(mList);
					if (list_bukeyongall_canyin != null
							&& list_bukeyongall_canyin.size() != 0) {
						couponOveredView.setList(list_bukeyongall_canyin);
					} else {
						couponOveredView.setEmpty();
					}

				} else {
					couponOveredView.setEmpty();
				}
				break;
			default:
				break;
			}
			popFilter.dismiss();
			break;
		case R.id.window_layout_shangpin:
			// 说明是商品
			mWodeyouhuiquan.setText("购物券");
			mQUANBU.setTextColor(Color.WHITE);
			mCANYIN.setTextColor(Color.WHITE);
			mSHANGPN.setTextColor(Color.rgb(22, 154, 218));
			mzhongchou.setTextColor(Color.WHITE);
			mYONGCHE.setTextColor(Color.WHITE);
			mgoupiao.setTextColor(Color.WHITE);
			im_quanbu.setVisibility(View.GONE);
			im_canyin.setVisibility(View.GONE);
			im_shangpin.setVisibility(View.VISIBLE);
			im_menpiao.setVisibility(View.GONE);
			im_yongche.setVisibility(View.GONE);
			im_zhongchou.setVisibility(View.GONE);
			switch (IS_SHOW_VIEW) {
			case 0:
				// 说明是在全部view界面点了商品
				// 当前是在全部的view界面来切换，所以展示全部餐饮的数据
				if (mList != null && mList.size() != 0) {
					List<YouHuiBean> list_canyinall_shangpin = useWaycheckData_all_shangpin(mList);
					if (list_canyinall_shangpin != null
							&& list_canyinall_shangpin.size() != 0) {
						allCouponView.setList(list_canyinall_shangpin);
					} else {
						allCouponView.setEmpty();
					}

				} else {
					allCouponView.setEmpty();
				}
				break;
			case 1:
				// 说明是代支付view界面点了商品
				if (mList != null && mList.size() != 0) {
					// 调用方法，分拣出全部可用的优惠券
					List<YouHuiBean> list_keyongall_shangpin = useWaycheckData_keyongall_shangpin(mList);
					if (list_keyongall_shangpin != null
							&& list_keyongall_shangpin.size() != 0) {
						couponingView.setList(list_keyongall_shangpin);
					} else {
						couponingView.setEmpty();
					}

				} else {
					couponingView.setEmpty();
				}
				break;
			case 2:
				// 说明是待发货界面点了商品
				if (mList != null && mList.size() != 0) {
					// 调用方法，分拣出全部可用的优惠券
					List<YouHuiBean> list_bukeyongall_shangpin = useWaycheckData_bukeyongall_shangpin(mList);
					if (list_bukeyongall_shangpin != null
							&& list_bukeyongall_shangpin.size() != 0) {
						couponOveredView.setList(list_bukeyongall_shangpin);
					} else {
						couponOveredView.setEmpty();
					}

				} else {
					couponOveredView.setEmpty();
				}
				break;

			default:
				break;
			}
			popFilter.dismiss();
			break;
		case R.id.window_layout_menpiao:
			// 说明是门票
			mWodeyouhuiquan.setText("购票券");
			mQUANBU.setTextColor(Color.WHITE);
			mCANYIN.setTextColor(Color.WHITE);
			mSHANGPN.setTextColor(Color.WHITE);
			mzhongchou.setTextColor(Color.WHITE);
			mYONGCHE.setTextColor(Color.WHITE);
			mgoupiao.setTextColor(Color.rgb(22, 154, 218));
			im_quanbu.setVisibility(View.GONE);
			im_canyin.setVisibility(View.GONE);
			im_shangpin.setVisibility(View.GONE);
			im_menpiao.setVisibility(View.VISIBLE);
			im_yongche.setVisibility(View.GONE);
			im_zhongchou.setVisibility(View.GONE);
			popFilter.dismiss();
			switch (IS_SHOW_VIEW) {
			case 0:
				if (mList != null && mList.size() != 0) {
					List<YouHuiBean> list_canyinall_goupiao = useWaycheckData_all_goupiiao(mList);
					if (list_canyinall_goupiao != null
							&& list_canyinall_goupiao.size() != 0) {
						allCouponView.setList(list_canyinall_goupiao);
					} else {
						allCouponView.setEmpty();
					}

				} else {
					allCouponView.setEmpty();
				}
				break;
			case 1:
				if (mList != null && mList.size() != 0) {
					// 调用方法，分拣出全部可用的优惠券
					List<YouHuiBean> list_keyongall_goupiao = useWaycheckData_keyongall_goupiao(mList);
					if (list_keyongall_goupiao != null
							&& list_keyongall_goupiao.size() != 0) {
						couponingView.setList(list_keyongall_goupiao);
					} else {
						couponingView.setEmpty();
					}

				} else {
					couponingView.setEmpty();
				}
				break;
			case 2:
				if (mList != null && mList.size() != 0) {
					// 调用方法，分拣出全部可用的优惠券
					List<YouHuiBean> list_bukeyongall_goupiao = useWaycheckData_bukeyongall_goupiao(mList);
					if (list_bukeyongall_goupiao != null
							&& list_bukeyongall_goupiao.size() != 0) {
						couponOveredView.setList(list_bukeyongall_goupiao);
					} else {
						couponOveredView.setEmpty();
					}

				} else {
					couponOveredView.setEmpty();
				}
				break;

			default:
				break;
			}
			break;
		case R.id.window_layout_yongche:
			// 说明是用车
			mWodeyouhuiquan.setText("用车券");
			mQUANBU.setTextColor(Color.WHITE);
			mCANYIN.setTextColor(Color.WHITE);
			mSHANGPN.setTextColor(Color.WHITE);
			mzhongchou.setTextColor(Color.WHITE);
			mYONGCHE.setTextColor(Color.rgb(22, 154, 218));
			mgoupiao.setTextColor(Color.WHITE);
			im_quanbu.setVisibility(View.GONE);
			im_canyin.setVisibility(View.GONE);
			im_shangpin.setVisibility(View.GONE);
			im_menpiao.setVisibility(View.GONE);
			im_yongche.setVisibility(View.VISIBLE);
			im_zhongchou.setVisibility(View.GONE);
			switch (IS_SHOW_VIEW) {
			case 0:
				if (mList != null && mList.size() != 0) {
					List<YouHuiBean> list_canyinall_yongche = useWaycheckData_all_yongche(mList);
					if (list_canyinall_yongche != null
							&& list_canyinall_yongche.size() != 0) {
						allCouponView.setList(list_canyinall_yongche);
					} else {
						allCouponView.setEmpty();
					}

				} else {
					allCouponView.setEmpty();
				}
				break;
			case 1:
				if (mList != null && mList.size() != 0) {
					// 调用方法，分拣出全部可用的优惠券
					List<YouHuiBean> list_keyongall_yongche = useWaycheckData_keyongall_yongche(mList);
					if (list_keyongall_yongche != null
							&& list_keyongall_yongche.size() != 0) {
						couponingView.setList(list_keyongall_yongche);
					} else {
						couponingView.setEmpty();
					}

				} else {
					couponingView.setEmpty();
				}
				break;
			case 2:
				if (mList != null && mList.size() != 0) {
					// 调用方法，分拣出全部可用的优惠券
					List<YouHuiBean> list_bukeyongall_yong = useWaycheckData_bukeyongall_yongche(mList);
					if (list_bukeyongall_yong != null
							&& list_bukeyongall_yong.size() != 0) {
						couponOveredView.setList(list_bukeyongall_yong);
					} else {
						couponOveredView.setEmpty();
					}

				} else {
					couponOveredView.setEmpty();
				}
				break;

			default:
				break;
			}
			popFilter.dismiss();
			break;

		default:
			break;
		}

	}

	/**
	 * 分拣出可用优惠券的全部
	 * 
	 * @param mList2
	 * @return
	 */
	private List<YouHuiBean> useWaycheckData_keyongall(List<YouHuiBean> mList2) {
		// TODO Auto-generated method stub
		List<YouHuiBean> list = new ArrayList<YouHuiBean>();
		for (int i = 0; i < mList2.size(); i++) {
			if (mList2.get(i).getStatus().equals("1")) {

			} else {
				list.add(mList2.get(i));
			}
		}
		return list;
	}

	/**
	 * 分拣出bu 可用优惠券的全部
	 * 
	 * @param mList2
	 * @return
	 */
	private List<YouHuiBean> useWaycheckData_bukeyongall(List<YouHuiBean> mList2) {
		// TODO Auto-generated method stub
		List<YouHuiBean> list = new ArrayList<YouHuiBean>();
		for (int i = 0; i < mList2.size(); i++) {
			if (mList2.get(i).getStatus().equals("2")) {
				list.add(mList2.get(i));
			} else {

			}
		}
		return list;
	}

	/**
	 * 分拣出可用优惠券的餐饮
	 * 
	 * @param mList2
	 * @return
	 */
	private List<YouHuiBean> useWaycheckData_keyongall_canyin(
			List<YouHuiBean> mList2) {
		// TODO Auto-generated method stub
		List<YouHuiBean> list = new ArrayList<YouHuiBean>();
		for (int i = 0; i < mList2.size(); i++) {
			if (mList2.get(i).getStatus().equals("1")) {
			} else {
				if (mList2.get(i).getCouponType().equals("1")) {
					list.add(mList2.get(i));
				}
			}
		}
		return list;
	}

	/**
	 * 分拣出可用优惠券的商品
	 * 
	 * @param mList2
	 * @return
	 */
	private List<YouHuiBean> useWaycheckData_keyongall_shangpin(
			List<YouHuiBean> mList2) {
		// TODO Auto-generated method stub
		List<YouHuiBean> list = new ArrayList<YouHuiBean>();
		for (int i = 0; i < mList2.size(); i++) {
			if (mList2.get(i).getStatus().equals("1")) {
			} else {
				if (mList2.get(i).getCouponType().equals("4")) {
					list.add(mList2.get(i));
				}
			}
		}
		return list;
	}

	/**
	 * 分拣出可用优惠券的购票
	 * 
	 * @param mList2
	 * @return
	 */
	private List<YouHuiBean> useWaycheckData_keyongall_yongche(
			List<YouHuiBean> mList2) {
		// TODO Auto-generated method stub
		List<YouHuiBean> list = new ArrayList<YouHuiBean>();
		for (int i = 0; i < mList2.size(); i++) {
			if (mList2.get(i).getStatus().equals("1")) {
			} else {
				if (mList2.get(i).getCouponType().equals("2")) {
					list.add(mList2.get(i));
				}
			}
		}
		return list;
	}

	/**
	 * 分拣出可用优惠券的购票
	 * 
	 * @param mList2
	 * @return
	 */
	private List<YouHuiBean> useWaycheckData_keyongall_goupiao(
			List<YouHuiBean> mList2) {
		// TODO Auto-generated method stub
		List<YouHuiBean> list = new ArrayList<YouHuiBean>();
		for (int i = 0; i < mList2.size(); i++) {
			if (mList2.get(i).getStatus().equals("1")) {
			} else {
				if (mList2.get(i).getCouponType().equals("3")) {
					list.add(mList2.get(i));
				}
			}
		}
		return list;
	}

	/**
	 * 分拣出bu 可用优惠券的canyin
	 * 
	 * @param mList2
	 * @return
	 */
	private List<YouHuiBean> useWaycheckData_bukeyongall_canyin(
			List<YouHuiBean> mList2) {
		// TODO Auto-generated method stub
		List<YouHuiBean> list = new ArrayList<YouHuiBean>();
		for (int i = 0; i < mList2.size(); i++) {
			if (mList2.get(i).getStatus().equals("2")
					&& mList2.get(i).getCouponType().equals("1")) {
				list.add(mList2.get(i));
			} else {

			}
		}
		return list;
	}

	/**
	 * 分拣出bu 可用优惠券的shangpin
	 * 
	 * @param mList2
	 * @return
	 */
	private List<YouHuiBean> useWaycheckData_bukeyongall_shangpin(
			List<YouHuiBean> mList2) {
		// TODO Auto-generated method stub
		List<YouHuiBean> list = new ArrayList<YouHuiBean>();
		for (int i = 0; i < mList2.size(); i++) {
			if (mList2.get(i).getStatus().equals("2")
					&& mList2.get(i).getCouponType().equals("4")) {
				list.add(mList2.get(i));
			} else {

			}
		}
		return list;
	}

	/**
	 * 分拣出bu 可用优惠券的goupiao
	 * 
	 * @param mList2
	 * @return
	 */
	private List<YouHuiBean> useWaycheckData_bukeyongall_goupiao(
			List<YouHuiBean> mList2) {
		// TODO Auto-generated method stub
		List<YouHuiBean> list = new ArrayList<YouHuiBean>();
		for (int i = 0; i < mList2.size(); i++) {
			if (mList2.get(i).getStatus().equals("2")
					&& mList2.get(i).getCouponType().equals("3")) {
				list.add(mList2.get(i));
			} else {

			}
		}
		return list;
	}

	/**
	 * 分拣出bu 可用优惠券的yongche
	 * 
	 * @param mList2
	 * @return
	 */
	private List<YouHuiBean> useWaycheckData_bukeyongall_yongche(
			List<YouHuiBean> mList2) {
		// TODO Auto-generated method stub
		List<YouHuiBean> list = new ArrayList<YouHuiBean>();
		for (int i = 0; i < mList2.size(); i++) {
			if (mList2.get(i).getStatus().equals("2")
					&& mList2.get(i).getCouponType().equals("2")) {
				list.add(mList2.get(i));
			} else {

			}
		}
		return list;
	}

	/**
	 * 分拣出用车的全部优惠券
	 * 
	 * @param mList2
	 * @return
	 */
	private List<YouHuiBean> useWaycheckData_all_yongche(List<YouHuiBean> mList2) {
		// TODO Auto-generated method stub
		List<YouHuiBean> list = new ArrayList<YouHuiBean>();
		for (int i = 0; i < mList2.size(); i++) {
			if (mList2.get(i).getCouponType().equals("2")) {
				list.add(mList2.get(i));
			} else {

			}
		}
		return list;
	}

	/**
	 * 分拣出餐饮的全部优惠券
	 * 
	 * @param mList2
	 * @return
	 */
	private List<YouHuiBean> useWaycheckData_all_canyin(List<YouHuiBean> mList2) {
		// TODO Auto-generated method stub
		List<YouHuiBean> list = new ArrayList<YouHuiBean>();
		for (int i = 0; i < mList2.size(); i++) {
			if (mList2.get(i).getCouponType().equals("1")) {
				list.add(mList2.get(i));
			} else {

			}
		}
		return list;
	}

	/**
	 * 分拣出商品的全部优惠券
	 * 
	 * @param mList2
	 * @return
	 */
	private List<YouHuiBean> useWaycheckData_all_shangpin(
			List<YouHuiBean> mList2) {
		// TODO Auto-generated method stub
		List<YouHuiBean> list = new ArrayList<YouHuiBean>();
		for (int i = 0; i < mList2.size(); i++) {
			if (mList2.get(i).getCouponType().equals("4")) {
				list.add(mList2.get(i));
			} else {

			}
		}
		return list;
	}

	/**
	 * 分拣出购票的全部优惠券
	 * 
	 * @param mList2
	 * @return
	 */
	private List<YouHuiBean> useWaycheckData_all_goupiiao(
			List<YouHuiBean> mList2) {
		// TODO Auto-generated method stub
		List<YouHuiBean> list = new ArrayList<YouHuiBean>();
		for (int i = 0; i < mList2.size(); i++) {
			if (mList2.get(i).getCouponType().equals("3")) {
				list.add(mList2.get(i));
			} else {

			}
		}
		return list;
	}
	@Override
	protected void onDestroy() {
		if(handler!=null){
			handler.removeCallbacksAndMessages(null);
		}
		if(pagerList!=null){
			pagerList.clear();
			pagerList = null;
		}
		if(mList!=null){
			mList.clear();
			mList = null;
		}
		allCouponView = null;
		couponingView = null;
		couponOveredView = null;
		intance = null;
		super.onDestroy();
	}

}
