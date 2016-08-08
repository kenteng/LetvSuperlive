package com.lesports.stadium.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lesports.stadium.R;
import com.lesports.stadium.adapter.RoundsAdapter;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.GuangGaoBean;
import com.lesports.stadium.bean.RoundGoodsBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.view.CustomDialog;
import com.lesports.stadium.view.ResID;
import com.lesports.stadium.view.ServiceGoodView;
import com.lesports.stadium.view.MyScrollView.OnScrollListener;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;

/**
 * 
 * @ClassName: ShoppingActivity
 * 
 * @Description: 购物界面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @author wangxinnian
 * 
 * @date 2016-7-11 上午10:29:13
 * 
 * 
 */
public class ShoppingActivity extends Activity implements OnScrollListener {
	/**
	 * 展示附近餐饮数据的可刷新listview
	 */
	private PullToRefreshGridView mGridViewPullToRefreshGridView;
	private GridView mGridview;
	/**
	 * 数据源
	 */
	public List<RoundGoodsBean> mList;
	/**
	 * 自定义scrollview，用来控制部分布局悬停
	 */
	/**
	 * 需要悬停在屏幕顶端的布局
	 */
	private RelativeLayout mLayoutXT;
	/**
	 * 当前悬停布局所在父布局
	 */
	private RelativeLayout mXuanDangqian;
	/**
	 * 滑动后悬停布局所在布局
	 */
	private LinearLayout mXuanHuadonghou;
	/**
	 * 动态获取当前悬停布局的顶部位置
	 */
	private int searchLayoutTop;
	/**
	 * 购物车图标
	 */
	private ImageView mBuyCarIcon;
	/**
	 * 用于展示广告轮播的viewpager
	 */
	private ViewPager mViewPager; // android-support-v4中的滑动组件
	/**
	 * 轮播viewpager的适配器
	 */
	private ImageAdapter mViewpagerAdapter;
	/**
	 * 顶部指示点布局
	 */
	private LinearLayout group;
	/**
	 * 顶部购物车数据图表
	 */
	private TextView mBuyNum;
	/**
	 * 用来标记网络获取的数据为空
	 */
	private final int TAG_DATANULL = 0;
	/**
	 * 用来标记网络获取的数据
	 */
	private final int TAG_DATA = 1;
	/**
	 * 用来标记网络获取的数据
	 */
	private final int TAG_DATA_GUANG = 3;
	/**
	 * 获取广告位失败
	 */
	private final int GUANG_FILUUE = 44;
	/**
	 * //动画层
	 */
	private ViewGroup anim_mask_layout;
	/**
	 * 本类对象
	 */
	public static ShoppingActivity instance;
	/**
	 * 展示的是否是广告 默认 fasle
	 */
	private boolean isNoGuanggao = false;
	/**
	 * 获取下来的商品数据的最后一条的id
	 */
	private String mGoodsId;
	private List<View> dots; // 图片标题正文的那些点
	private int currentItem = 0; // 当前图片的索引号
	private List<ImageView> imageViews; // 滑动的图片集合
	private ScheduledExecutorService scheduledExecutorService;
	private CustomDialog exitDialog;
	/**
	 * 服务商品的适配器
	 */
	public RoundsAdapter adapter;

	private Context context;
	/**
	 * 网络异常显示的布局
	 */
	private RelativeLayout netErrorLayout;
	/**
	 * 处理数据的handler；
	 */
	private final int HANDLE_TAG_5=5;
	private final int HANDLE_TAG_100=100;
	private final int HANDLE_TAG_200=200;
	private final int HANDLE_TAG_22=22;
	private final int HANDLE_TAG_222=222;
	private final int HANDLE_TAG_33=33;
	private final int HANDLE_TAG_333=333;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case TAG_DATANULL:
				netErrorLayout.setVisibility(View.VISIBLE);
				break;
			case TAG_DATA:
				// 调用方法，解析数据
				netErrorLayout.setVisibility(View.GONE);
				String backdata = (String) msg.obj;
				if (backdata != null && backdata.length() != 0
						&& !TextUtils.isEmpty(backdata)) {
					jsonData(backdata, context);
					mXuanHuadonghou.setVisibility(View.VISIBLE);
				}
				// 调用方法，获取广告列表数据
				UseWayGetData();
				break;
			case HANDLE_TAG_5:
				mViewPager.setCurrentItem(currentItem);// 切换当前显示的图片
				break;
			case TAG_DATA_GUANG:
				netErrorLayout.setVisibility(View.GONE);
				String bada = (String) msg.obj;
				if (!TextUtils.isEmpty(bada)) {
					// 调用方法，解析广告数据
					jsonGuanggaoData(bada);
				}
				break;
			case HANDLE_TAG_100:
				mGridViewPullToRefreshGridView.onRefreshComplete();
				break;
			case HANDLE_TAG_200:
				mGridViewPullToRefreshGridView.onRefreshComplete();
				break;
			case HANDLE_TAG_22:
				// 刷新失败
				mGridViewPullToRefreshGridView.onRefreshComplete();
				break;
			case HANDLE_TAG_222:
				// 刷新成功
				String backdatas = (String) msg.obj;
				if (!TextUtils.isEmpty(backdatas)) {
					useWayHandleRefrensh(backdatas);
				}
				break;
			case HANDLE_TAG_33:
				// 加载失败
				mGridViewPullToRefreshGridView.onRefreshComplete();
				break;
			case HANDLE_TAG_333:
				mGridViewPullToRefreshGridView.onRefreshComplete();
				String backdatasss = (String) msg.obj;
				if (!TextUtils.isEmpty(backdatasss)) {
					useWayHandleAddMore(backdatasss);
				}
				// 加载成功
				break;
			case GUANG_FILUUE:
				initViewpagerData(null);
				initAutoScrollviewViewpager(context);
				break;
			default:
				break;
			}
		}

		/**
		 * 加载更多成功后处理数据
		 * @param backdatasss
		 */
		private void useWayHandleAddMore(String backdatasss) {
			// TODO Auto-generated method stub
			List<RoundGoodsBean> list = jsonDatamore(backdatasss,
					context);
			if (list != null && list.size() > 0) {
				mList.addAll(list);
				if (adapter != null) {
					mGoodsId = mList.get(mList.size() - 1).getId();
					adapter.setlist(mList);
					mGridview.setSelection(adapter.getCount());
				}
			}
		}

		/**
		 * 刷新成功后处理数据
		 * @param backdatas
		 */
		private void useWayHandleRefrensh(String backdatas) {
			// TODO Auto-generated method stub
			List<RoundGoodsBean> list = jsonDatamore(backdatas, context);
			if (adapter != null && list != null && list.size() > 0) {
				mGoodsId = list.get(list.size() - 1).getId();
				adapter.setlist(list);
			}
			mXuanHuadonghou.setVisibility(View.VISIBLE);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_good);
		context = this;
		instance = this;
		initView();
		getdataFromService();
		changeVIew();
		initonclick();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		changeVIew();
	}
	/**
	 * 初始化监听
	 */
	private void initonclick() {
		findViewById(R.id.title_left_iv).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mBuyCarIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(context, "ShowCart");
				if (!TextUtils.isEmpty(GlobalParams.USER_ID)) {
					Intent intent = new Intent(context, BuyCarActivity.class);
					intent.putExtra("tag", "rounds");
					context.startActivity(intent);
				} else {
					createDialog();
				}
			}
		});
	}

	public void changeVIew() {
		List<RoundGoodsBean> list = LApplication.dbBuyCar.findByCondition(
				"buy_mUsename= ?", new String[] { GlobalParams.USER_ID }, null);
		if (list.size() == 0) {
			if (adapter != null) {
				adapter.setBoolean(false);
			}
			mBuyNum.setVisibility(View.INVISIBLE);
		} else {
			if (adapter != null) {
				adapter.setBoolean(true);
			}
			mBuyNum.setVisibility(View.VISIBLE);
			int mCount = 0;
			for (int i = 0; i < list.size(); i++) {
				int counss = Integer.parseInt(list.get(i).getmNum());
				mCount = mCount + counss;
			}
			if (mCount >= 99) {
				mBuyNum.setText(mCount + "");
			} else {
				mBuyNum.setText(mCount + "");
			}

		}
	}

	/**
	 * 加载数据的时候调用的方法
	 */
	private void useWayAddMoreData() {
		// TODO Auto-generated method stub
		Message message = new Message();
		message.arg1 = HANDLE_TAG_100;
		handler.sendMessageDelayed(message, 1000);
		getdataFromService_addmore();
	}

	/**
	 * 刷新数据的时候调用的方法
	 */
	private void useWayRefrenshData() {
		// TODO Auto-generated method stub
		Message message = new Message();
		message.arg1 = HANDLE_TAG_200;
		handler.sendMessageDelayed(message, 1000);
		// 调用方法，刷新数据
		getdataFromService_refrensh();
	}

	/**
	 * @Description: 创建动画层
	 * @param
	 * @return void
	 * @throws
	 */
	private ViewGroup createAnimLayout() {
		ViewGroup rootView = (ViewGroup) ((Activity) context).getWindow()
				.getDecorView();
		LinearLayout animLayout = new LinearLayout(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		animLayout.setLayoutParams(lp);
		animLayout.setId(Integer.MAX_VALUE - 1);
		animLayout.setBackgroundResource(android.R.color.transparent);
		rootView.addView(animLayout);
		return animLayout;
	}

	/**
	 * @2016-2-15下午3:44:57 计算动画起始角度问题
	 */
	private View addViewToAnimLayout(final ViewGroup parent, final View view,
			int[] location) {
		int x = location[0];
		int y = location[1];
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.leftMargin = x;
		lp.topMargin = y;
		view.setLayoutParams(lp);
		return view;
	}

	/**
	 * @2016-2-15下午3:32:08 给界面中的添加按钮设置动画
	 */
	public void setAnim(final View v, int[] startLocation) {
		anim_mask_layout = null;
		anim_mask_layout = createAnimLayout();
		anim_mask_layout.addView(v);// 把动画小球添加到动画层
		final View view = addViewToAnimLayout(anim_mask_layout, v,
				startLocation);
		int[] endLocation = new int[2];// 存储动画结束位置的X、Y坐标
		mBuyCarIcon.getLocationInWindow(endLocation);// shopCart是那个购物车
		Log.i("终点纵坐标", endLocation[1] + "");
		Log.i("终点横坐标", endLocation[0] + "");
		Log.i("起点纵坐标", startLocation[1] + "");
		Log.i("起点横坐标", startLocation[0] + "");

		// 计算位移
		int endX = 0 + endLocation[0] - startLocation[0];// 动画位移的X坐标
		int endY = endLocation[1] - startLocation[1];// 动画位移的y坐标
		TranslateAnimation translateAnimationX = new TranslateAnimation(0,
				endX, 0, 0);
		translateAnimationX.setInterpolator(new LinearInterpolator());
		translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
		translateAnimationX.setFillAfter(true);

		TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
				0, endY);
		translateAnimationY.setInterpolator(new AccelerateInterpolator());
		translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
		translateAnimationX.setFillAfter(true);

		AnimationSet set = new AnimationSet(false);
		set.setFillAfter(false);
		set.addAnimation(translateAnimationY);
		set.addAnimation(translateAnimationX);
		set.setDuration(800);// 动画的执行时间
		view.startAnimation(set);
		// 动画监听事件
		set.setAnimationListener(new Animation.AnimationListener() {
			// 动画的开始
			@Override
			public void onAnimationStart(Animation animation) {
				v.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}

			// 动画的结束
			@Override
			public void onAnimationEnd(Animation animation) {
				v.setVisibility(View.GONE);
			}
		});

	}

	/**
	 * 调用方法，从网络获取周边商品数据
	 * 
	 * @param context
	 */
	private void getdataFromService() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "0");
		params.put("flag", "U");
		params.put("goodsId", "0");
		params.put("sort", "4");
		params.put("classicId", "");
		params.put("price", "");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.SERVICE_OODLIST_DATA, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if(handler==null)
							return;
						if (data != null&& data.getNetResultCode()==0) {
							Message msg = new Message();
							msg.arg1 = TAG_DATA;
							msg.obj = data.getObject();
							handler.sendMessage(msg);
						} else {
							Message msg = new Message();
							msg.arg1 = TAG_DATANULL;
							handler.sendMessage(msg);
						}
					}

				}, false, false);
	}

	/**
	 * 调用方法，从网络获取周边商品数据
	 * 
	 * @param context
	 */
	private void getdataFromService_refrensh() {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "0");
		params.put("flag", "U");
		params.put("goodsId", "0");
		params.put("sort", "4");
		params.put("classicId", "");
		params.put("price", "");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.SERVICE_OODLIST_DATA, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if(handler==null)
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
								if (data.getNetResultCode() != 0) {
									Message msg = new Message();
									msg.arg1 = HANDLE_TAG_222;
									msg.obj = backdata;
									handler.sendMessage(msg);
								} else {
									Message msg = new Message();
									msg.arg1 = HANDLE_TAG_22;
									handler.sendMessage(msg);

								}
							}
						}
					}

				}, false, false);
	}

	/**
	 * 调用方法，从网络获取周边商品数据
	 * 
	 * @param context
	 */
	private void getdataFromService_addmore() {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "0");
		params.put("flag", "U");
		params.put("goodsId", mGoodsId);
		params.put("sort", "4");
		params.put("classicId", "");
		params.put("price", "");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.SERVICE_OODLIST_DATA, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if(handler==null)
							return;
						if (data != null&& data.getNetResultCode()==0) {
							Message msg = new Message();
							msg.arg1 = HANDLE_TAG_333;
							msg.obj = data.getObject();
							handler.sendMessage(msg);
						} else {
							Message msg = new Message();
							msg.arg1 = HANDLE_TAG_33;
							handler.sendMessage(msg);
						}
					}

				}, false, false);
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
		params.put("advertisementType", "6");
		params.put("status", "1");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.GUANGGAO_LIST, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if(handler==null)
							return;
						if (data != null && data.getNetResultCode() == 0) {
							Object obj = data.getObject();
							String backdata = obj.toString();
							if (!TextUtils.isEmpty(backdata)
									&& !"[]".equals(backdata)) {
								Message msg = new Message();
								msg.arg1 = TAG_DATA_GUANG;
								msg.obj = backdata;
								handler.sendMessage(msg);
								return;
							}
						}
						isNoGuanggao = true;
						Message msg = new Message();
						msg.arg1 = GUANG_FILUUE;
						handler.sendMessage(msg);
					}

				}, false, false);
	};

	/**
	 * 解析网络获取的广告数据
	 * 
	 * @param bada
	 */
	private void jsonGuanggaoData(String bada) {
		// TODO Auto-generated method stub
		List<GuangGaoBean> list = new ArrayList<GuangGaoBean>();
		try {
			JSONArray array = new JSONArray(bada);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				GuangGaoBean bean = new GuangGaoBean();

				if (obj.has("creatTime")) {
					bean.setCreatTime(obj.getString("creatTime"));
				}
				if (obj.has("advertisementTime")) {
					bean.setAdvertisementTime(obj
							.getString("advertisementTime"));
				}
				if (obj.has("entTime")) {
					bean.setEntTime(obj.getString("entTime"));
				}
				if (obj.has("id")) {
					bean.setId(obj.getString("id"));
				}
				if (obj.has("startTime")) {
					bean.setStartTime(obj.getString("startTime"));
				}
				if (obj.has("title")) {
					bean.setTitle(obj.getString("title"));
				}
				if (obj.has("url")) {
					bean.setUrl(obj.getString("url"));
				}
				if (obj.has("advertisementType")) {
					bean.setAdvertisementType(obj
							.getString("advertisementType"));
				}
				if (obj.has("displayOrder")) {
					bean.setDisplayOrder(obj.getString("displayOrder"));
				}
				if (obj.has("httpUrl")) {
					bean.setHttpUrl(obj.getString("httpUrl"));
				}
				if (obj.has("status")) {
					bean.setStatus(obj.getString("status"));
				}
				list.add(bean);
			}
			if (list != null && list.size() != 0) {
				// 使用方法，将数据加载到界面中
				isNoGuanggao = false;
				initViewpagerData(list);
				initAutoScrollviewViewpager(context);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 解析如下数据，并且将至存储到集合当中
	 * 
	 * @param backdata
	 * @param context
	 */
	private void jsonData(String backdata, Context context) {
		mList = new ArrayList<RoundGoodsBean>();
		try {
			JSONArray array = new JSONArray(backdata);
			int count = array.length();
			for (int i = 0; i < count; i++) {
				JSONObject objs = array.getJSONObject(i);
				RoundGoodsBean moundgoodsbean = new RoundGoodsBean();
				if (objs.has("bigImg")) {
					moundgoodsbean.setBigimg(objs.getString("bigImg"));
				}
				if (objs.has("classicId")) {
					moundgoodsbean.setClassicId(objs.getString("classicId"));
				}
				if (objs.has("bannerImage")) {
					moundgoodsbean
							.setBannerImage(objs.getString("bannerImage"));
				}
				if (objs.has("category")) {
					// 子实体类部分
					JSONObject objz = objs.getJSONObject("category");
					if (objz.has("classicName")) {
						moundgoodsbean.setClassicName(objz
								.getString("classicName"));
					}
					if (objz.has("cId")) {
						moundgoodsbean.setcId(objz.getString("cId"));
					}
				}
				if(objs.has("delivery_type")){
					moundgoodsbean.setDelivery_type(objs.getString("delivery_type"));
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
					moundgoodsbean.setSellerAddress(objs
							.getString("sellerAddress"));
				}
				if (objs.has("sellerName")) {
					moundgoodsbean.setSellerName(objs.getString("sellerName"));
				}
				if(objs.has("pickup_address")){
					moundgoodsbean.setPickup_address(objs.getString("pickup_address"));
				}
				if(objs.has("pickup_remark")){
					moundgoodsbean.setPickup_remark(objs.getString("pickup_remark"));
				}
				mList.add(moundgoodsbean);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 调用方法，加载数据到控件上
		if (mList != null && mList.size() != 0) {
			mGoodsId = mList.get(mList.size() - 1).getId();
			initviews(context, mList);
		}
	}

	/**
	 * 解析如下数据，并且将至存储到集合当中
	 * 
	 * @param backdata
	 * @param context
	 */
	private List<RoundGoodsBean> jsonDatamore(String backdata, Context context) {
		List<RoundGoodsBean> Lists = new ArrayList<RoundGoodsBean>();
		try {
			JSONArray array = new JSONArray(backdata);
			int count = array.length();
			for (int i = 0; i < count; i++) {
				JSONObject objs = array.getJSONObject(i);
				RoundGoodsBean moundgoodsbean = new RoundGoodsBean();
				if (objs.has("bigImg")) {
					moundgoodsbean.setBigimg(objs.getString("bigImg"));
				}
				if (objs.has("classicId")) {
					moundgoodsbean.setClassicId(objs.getString("classicId"));
				}
				if (objs.has("bannerImage")) {
					moundgoodsbean
							.setBannerImage(objs.getString("bannerImage"));
				}
				if (objs.has("category")) {
					// 子实体类部分
					JSONObject objz = objs.getJSONObject("category");
					if (objz.has("classicName")) {
						moundgoodsbean.setClassicName(objz
								.getString("classicName"));
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
					moundgoodsbean.setSellerAddress(objs
							.getString("sellerAddress"));
				}
				if (objs.has("sellerName")) {
					moundgoodsbean.setSellerName(objs.getString("sellerName"));
				}
				if(objs.has("pickup_address")){
					moundgoodsbean.setPickup_address(objs.getString("pickup_address"));
				}
				if(objs.has("pickup_remark")){
					moundgoodsbean.setPickup_remark(objs.getString("pickup_remark"));
				}
				Lists.add(moundgoodsbean);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Lists;
	}

	/**
	 * 初始化轮播控件
	 * 
	 * @2016-2-26下午1:41:15
	 */
	private void initAutoScrollviewViewpager(Context context) {
		// TODO Auto-generated method stub
		mViewPager.setAdapter(new MyAdapter());
		// 设置一个监听器，当ViewPager中的页面改变时调用
		mViewPager.setOnPageChangeListener(new MyPageChangeListener());
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每两秒钟切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 3,
				TimeUnit.SECONDS);
	}

	/**
	 * 填充ViewPager页面的适配器
	 * 
	 * @author Administrator
	 * 
	 */
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
					R.drawable.page_indicator_unfocused);
			dots.get(position).setBackgroundResource(
					R.drawable.page_indicator_focused);
			oldPosition = position;
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	/**
	 * 用来初始化顶部viewpager的数据源
	 * 
	 * @param context
	 * @param list
	 * @2016-2-26下午1:35:25
	 */
	@SuppressLint("NewApi")
	private void initViewpagerData(List<GuangGaoBean> list) {
		// TODO Auto-generated method stub
		// 这里需要从网络获取轮播图片，但是这个时候先用假数据
		// 先判断数据源数据是否满足4个
		int num = list == null ? 0 : list.size();
		int num_true = 4 - num;
		if (num_true > 0) {
			List<GuangGaoBean> lists = new ArrayList<>();
			if (mList != null && mList.size() >= num_true) {
				for (int l = 0; l < num_true; l++) {
					GuangGaoBean bean = new GuangGaoBean();
					bean.setUrl(mList.get(l).getBannerImage());
					Log.i("wxn", "有过少" + bean.getUrl());
					lists.add(bean);
				}
				if (list != null && list.size() != 0)
					lists.addAll(list);
				// 用来存储图片url
				if (lists != null && lists.size() != 0) {
					// 到时候只需要获取到这个images，就可以直接展示轮播
					int count = lists.size();
					Log.i("lwc", "有过少广告位" + count + "");
					dots = new ArrayList<View>();// 计算需要生成的表示点数量
					group.removeAllViews();
					for (int i = 0; i < count; i++) {
						TextView tv = new TextView(context);
						LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
								25, 25);
						lp.setMargins(5, 0, 5, 0);
						tv.setLayoutParams(lp);
						tv.setBackgroundDrawable(context.getResources()
								.getDrawable(
										R.drawable.page_indicator_unfocused));
						group.addView(tv);
						dots.add(tv);
					}
					imageViews = new ArrayList<ImageView>();
					for (int H = 0; H < count; H++) {
						final GuangGaoBean bean = lists.get(H);
						ImageView img = new ImageView(context);
						img.setScaleType(ScaleType.FIT_XY);
						// img.setBackground(context.getResources().getDrawable(images[H]));
						LApplication.loader.DisplayImage(
								ConstantValue.BASE_IMAGE_URL
										+ lists.get(H).getUrl()
										+ ConstantValue.IMAGE_END, img);
						imageViews.add(img);
						final int f = H;
						imageViews.get(H).setOnClickListener(
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										if (!isNoGuanggao && f == 3) {
											Intent intent = new Intent(
													context,
													GoodsGuanggaoActivity.class);
											intent.putExtra("bean", bean);
											context.startActivity(intent);
										} else {
											Intent intent = new Intent(
													context,
													GoodsDetailActivity.class);
											intent.putExtra("id", mList.get(f)
													.getId());
											intent.putExtra("bean",
													mList.get(f));
											intent.putExtra("tag", "round");
											context.startActivity(intent);
										}
									}
								});
					}
				}
			} else {
				// 用来存储图片url
				if (list != null && list.size() != 0) {
					// 到时候只需要获取到这个images，就可以直接展示轮播
					int count = list.size();
					Log.i("lwc", "有过少广告位" + count + "");
					dots = new ArrayList<View>();// 计算需要生成的表示点数量
					group.removeAllViews();
					for (int i = 0; i < count; i++) {
						TextView tv = new TextView(context);
						LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
								25, 25);
						lp.setMargins(5, 0, 5, 0);
						tv.setLayoutParams(lp);
						tv.setBackgroundDrawable(context.getResources()
								.getDrawable(
										R.drawable.page_indicator_unfocused));
						group.addView(tv);
						dots.add(tv);
					}
					imageViews = new ArrayList<ImageView>();
					for (int H = 0; H < count; H++) {
						final GuangGaoBean bean = list.get(H);
						ImageView img = new ImageView(context);
						img.setScaleType(ScaleType.FIT_XY);
						// img.setBackground(context.getResources().getDrawable(images[H]));
						LApplication.loader.DisplayImage(
								ConstantValue.BASE_IMAGE_URL
										+ list.get(H).getUrl()
										+ ConstantValue.IMAGE_END, img);
						imageViews.add(img);
						final int f = H;
						imageViews.get(H).setOnClickListener(
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										if (f == 3) {
											Intent intent = new Intent(
													context,
													GoodsGuanggaoActivity.class);
											intent.putExtra("bean", bean);
											context.startActivity(intent);
										}
									}
								});
					}
				}
			}

		} else {
			// 用来存储图片url
			if (list != null && list.size() != 0) {
				// 到时候只需要获取到这个images，就可以直接展示轮播
				int count = list.size();
				Log.i("lwc", "有过少广告位" + count + "");
				dots = new ArrayList<View>();// 计算需要生成的表示点数量
				group.removeAllViews();
				for (int i = 0; i < count; i++) {
					TextView tv = new TextView(context);
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							25, 25);
					lp.setMargins(5, 0, 5, 0);
					tv.setLayoutParams(lp);
					tv.setBackgroundDrawable(context.getResources()
							.getDrawable(R.drawable.page_indicator_unfocused));
					group.addView(tv);
					dots.add(tv);
				}
				imageViews = new ArrayList<ImageView>();
				for (int H = 0; H < count; H++) {
					final GuangGaoBean bean = list.get(H);
					ImageView img = new ImageView(context);
					img.setScaleType(ScaleType.FIT_XY);
					// img.setBackground(context.getResources().getDrawable(images[H]));
					LApplication.loader.DisplayImage(
							ConstantValue.BASE_IMAGE_URL + list.get(H).getUrl()
									+ ConstantValue.IMAGE_END, img);
					imageViews.add(img);
					final int f = H;
					imageViews.get(H).setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (f == 3) {
								Intent intent = new Intent(context,
										GoodsGuanggaoActivity.class);
								intent.putExtra("bean", bean);
								context.startActivity(intent);
							}
						}
					});
				}
			}
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
			synchronized (mViewPager) {
				currentItem = (currentItem + 1) % imageViews.size();
				Message msg = new Message();
				msg.arg1 = HANDLE_TAG_5;
				handler.sendMessage(msg);
			}
		}

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			searchLayoutTop = mXuanDangqian.getTop();// 获取悬停布局的顶部位置
		}
	}

	/**
	 * 初始化view控件
	 * 
	 * @param context
	 * @2016-2-19下午1:34:58
	 */
	private void initviews(final Context context,
			final List<RoundGoodsBean> list) {
		// TODO Auto-generated method stub
		// 先判断用户是否登录
		final String usename = GlobalParams.USER_ID;
		if (!TextUtils.isEmpty(usename)) {
			adapter = new RoundsAdapter(list, context, true, 1);
			mGridview.setAdapter(adapter);
		} else {
			adapter = new RoundsAdapter(list, context, false, 1);
			mGridview.setAdapter(adapter);
		}
		mGridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				GlobalParams.Goods_Address = list.get(arg2).getPickup_address();
				GlobalParams.Goods_Store_Name = list.get(arg2).getSellerName();
				GlobalParams.Goods_Store_Phone = list.get(arg2).getTelephone();
				Intent intent = new Intent(context, GoodsDetailActivity.class);
				intent.putExtra("id", list.get(arg2).getId());
				intent.putExtra("bean", list.get(arg2));
				intent.putExtra("tag", "round");
				context.startActivity(intent);
			}
		});

	}

	private void createDialog() {
		exitDialog = new CustomDialog(context, new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(context, LoginActivity.class);
				context.startActivity(intent);
				exitDialog.dismiss();
			}
		}, new OnClickListener() {

			@Override
			public void onClick(View v) {
				exitDialog.dismiss();
			}
		});
		exitDialog.setCancelTxt("取消");
		exitDialog.setConfirmTxt("去登录");
		exitDialog.setRemindTitle("温馨提示");
		exitDialog.setRemindMessage("登录之后才能添加购物车哦  ~~");
		exitDialog.show();
	}

	/**
	 * 上拉下拉数据加载
	 * 
	 * @param i
	 * @param j
	 * @param context
	 */
	protected void loadRefreshData(int i, int j, Context context) {
		// TODO Auto-generated method stub
		Toast.makeText(context, "haha", 0).show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lesports.stadium.view.MyScrollView.OnScrollListener#onScroll(int)
	 */
	@Override
	public void onScroll(int scrollY) {
		// TODO Auto-generated method stub
		if (scrollY >= searchLayoutTop) {
			if (mLayoutXT.getParent() != mXuanHuadonghou) {
				mXuanDangqian.removeView(mLayoutXT);
				mXuanHuadonghou.addView(mLayoutXT);
			}
		} else {
			if (mLayoutXT.getParent() != mXuanDangqian) {
				mXuanHuadonghou.removeView(mLayoutXT);
				mXuanDangqian.addView(mLayoutXT);
			}
		}
	}

	private void initView() {
		TextView title = (TextView) findViewById(R.id.tv_title);
		title.setText("商城");
		mGridViewPullToRefreshGridView = (PullToRefreshGridView) findViewById(R.id.servicegoods_gridview);
		mLayoutXT = (RelativeLayout) findViewById(R.id.servicegoods_layout_topbar);
		netErrorLayout = (RelativeLayout) findViewById(R.id.rl_neterror);
		mXuanDangqian = (RelativeLayout) findViewById(R.id.servicegoods_layout_top);
		mXuanHuadonghou = (LinearLayout) findViewById(R.id.goodsview_search01);
		mBuyCarIcon = (ImageView) findViewById(R.id.servicegoods_im_topgoutuche);
		mViewPager = (ViewPager) findViewById(R.id.vp_goods_top_vp_goods);
		group = (LinearLayout) findViewById(R.id.goods_layout_addview);
		mBuyNum = (TextView) findViewById(R.id.service_goods_bar_buynum);
		mGridview = mGridViewPullToRefreshGridView.getRefreshableView();
		mGridViewPullToRefreshGridView
				.setOnRefreshListener(new OnRefreshListener2<GridView>() {

					@Override
					public void onPullDownToRefresh(
							com.handmark.pulltorefresh.library.PullToRefreshBase<GridView> refreshView) {
						// TODO Auto-generated method stub
						useWayRefrenshData();
					}

					@Override
					public void onPullUpToRefresh(
							com.handmark.pulltorefresh.library.PullToRefreshBase<GridView> refreshView) {
						// TODO Auto-generated method stub
						useWayAddMoreData();

					}

				});
	}

	protected static class ImageAdapter extends PagerAdapter {
		ImageView[] mImaes;

		ImageAdapter(Context context, ImageView[] img) {
			this.mImaes = img;
		}

		@Override
		public int getCount() {

			// return mImaes.length + 1;
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return (arg0 == arg1);
		}

		@Override
		public void destroyItem(View container, int position, Object object) {

			((ViewPager) container)
					.removeView(mImaes[position % mImaes.length]);
		}

		@Override
		public Object instantiateItem(View container, int position) {
			ImageView img = mImaes[position % mImaes.length];
			((ViewPager) container).removeView(img);
			((ViewPager) container).addView(img);
			return img;
		}

	}

	@Override
	protected void onStop() {
		// 当Activity不可见的时候停止切换
		if(scheduledExecutorService!=null)
			scheduledExecutorService.shutdown();
		super.onStop();
	}

	public void setBuyNum() {
		mBuyNum.setVisibility(View.VISIBLE);
		List<RoundGoodsBean> list = LApplication.dbBuyCar.findByCondition(
				"buy_mUsename= ?", new String[] { GlobalParams.USER_ID }, null);
		if (list.size() == 0) {
			mBuyNum.setVisibility(View.INVISIBLE);
		} else {
			mBuyNum.setVisibility(View.VISIBLE);
			int Count = 0;
			for (int i = 0; i < list.size(); i++) {
				int counss = Integer.parseInt(list.get(i).getmNum());
				Count = Count + counss;
			}
			if (Count >= 99) {
				mBuyNum.setText(Count + "");
			} else {
				mBuyNum.setText(Count + "");
			}
		}
	}

	@Override
	protected void onDestroy() {
		if(handler!=null)
			handler.removeCallbacksAndMessages(null);
		handler = null;
		if (mList != null) {
			mList.clear();
			mList = null;
		}
		mGridViewPullToRefreshGridView = null;
		mGridview = null;
		instance = null;
		mViewPager = null;
		anim_mask_layout = null;
		if(dots!=null)
			dots.clear();
		dots = null;
		if(imageViews!=null)
			imageViews.clear();
		imageViews = null;
		adapter = null;
		exitDialog = null;
		scheduledExecutorService = null;
		context = null;
		System.gc();
		super.onDestroy();
	}

}
