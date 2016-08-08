package com.lesports.stadium.activity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
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
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.lesports.stadium.R;
import com.lesports.stadium.adapter.WindowMenuAdapter;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.GroupInfo;
import com.lesports.stadium.bean.OrderListBean;
import com.lesports.stadium.bean.OrderListBeanCar;
import com.lesports.stadium.bean.OrderListBeanGoodsBean;
import com.lesports.stadium.bean.OrderListBeanTices;
import com.lesports.stadium.bean.OrderListBeanZhongchouBean;
import com.lesports.stadium.bean.OrderWindowBean;
import com.lesports.stadium.bean.PayBean;
import com.lesports.stadium.bean.PayParametric;
import com.lesports.stadium.bean.ProductInfo;
import com.lesports.stadium.bean.TicesDetailBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.net.Code.HttpSetting;
import com.lesports.stadium.net.NetLoadListener;
import com.lesports.stadium.net.NetService;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.PayUtils;
import com.lesports.stadium.utils.WifiUtils;
import com.lesports.stadium.view.AllOrderView;
import com.lesports.stadium.view.CustomDialog;
import com.lesports.stadium.view.MyViewPager;
import com.lesports.stadium.view.WaitPayOrderView;
import com.lesports.stadium.view.WaitReceiverOrderView;
import com.lesports.stadium.view.WaitSendOrderView;
import com.letv.lepaysdk.ELePayState;
import com.letv.lepaysdk.LePay.ILePayCallback;
import com.letv.lepaysdk.LePayApi;
import com.letv.lepaysdk.LePayConfig;
import com.umeng.analytics.MobclickAgent;

/**
 * ***************************************************************
 * 
 * @Desc : 我的订单
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwc
 * @data:
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class AllOrderActivity extends Activity implements OnClickListener {
	private View tabImgView;
	/*
	 * 所有订单
	 */
	private TextView t_order_all;
	/*
	 * 待支付订单
	 */
	private TextView t_order_wait_pay;
	/*
	 * 待发送
	 */
	private TextView t_order_wait_send;
	/*
	 * 待接收
	 */
	private TextView t_order_wait_receiver;
	/*
	 * VewPager 对象
	 */
	private MyViewPager categoryPager;
	/*
	 * viewPager适配器
	 */
	private CategoryPageAdapter categoryAdapter;

	/**
	 * 所有订单
	 */
	private static final int ORDER_ALL = 0;
	/**
	 * 待支付订单
	 */
	private static final int ORDER_WAIT_PAY = 1;
	/**
	 * 待发送订单
	 */
	private static final int ORDER_WAIT_SEND = 2;
	/**
	 * 待收获订单
	 */
	private static final int ORDER_WAIT_RECEIVER = 3;
	/*
	 * 存储数据
	 */
	private ArrayList<View> pagerList;
	/**
	 * 选项卡最后选择的是哪个
	 */
	private int lastPager = 0;
	/**
	 * 所有订单界面view
	 */
	private AllOrderView allOrderView;
	/**
	 * 待支付订单界面view
	 */
	private WaitPayOrderView waitPayOrderView;
	/**
	 * 待发送订单界面view
	 */
	private WaitSendOrderView waitSendOrderView;
	/**
	 * 待接收订单界面view
	 */
	private WaitReceiverOrderView waitReceiverOrderView;
	/**
	 * 顶部返回按钮
	 */
	private ImageView mBack;
	/**
	 * 顶部标题筛选按钮
	 */
	private TextView mTitle;
	/**
	 * 需要弹窗的layout
	 */
	private LinearLayout ll_popul_filter;
	/**
	 * window
	 */
	private PopupWindow popFilter = null;
	/**
	 * 弹窗布局位置
	 */
	private RelativeLayout layout_all_order_title;
	/**
	 * 窗口菜单数据源
	 */
	private List<OrderWindowBean> mWindowMenulist;
	/**
	 * 用于标记网络获取数据为空
	 */
	private final int NUMM_TAG = 1;
	/**
	 * 存储订单数据的总的集合
	 */
	private List<OrderListBean> mList = new ArrayList<OrderListBean>();
	/**
	 * 存储全部订单数据的list集合
	 */
	private List<OrderListBean> allList = new ArrayList<OrderListBean>();
	/**
	 * 全部
	 */
	private ImageView im_quanbu;
	/**
	 * 餐饮
	 */
	private ImageView im_canyin;
	/**
	 * 商品
	 */
	private ImageView im_shangpin;
	/**
	 * 门票
	 */
	private ImageView im_menpiao;
	/**
	 * 用车
	 */
	private ImageView im_yongche;
	/**
	 * 众筹
	 */
	private ImageView im_zhongchou;
	/**
	 * 登录提示框
	 */
	private CustomDialog exitDialog;
	/**
	 * 用于标记网络数据不为空
	 */
	private final int DATA_ISHAVE = 2;
	/**
	 * 定义一个标记，用来标注，当前展示的是哪个view
	 */
	private int IS_SHOW_VIEW = 0;// 默认为0，也就是全部
	/**
	 * 用来标注当前被选定的是哪一个菜单项
	 */
	private int IS_SHOW_MENU = 0;// 默认为0，也就是全部菜单项
	/**
	 * 该标记用来标注是哪一类商品
	 */
	private String isType = null;
	/**
	 * 该标记用来标注是自取还是送货
	 */
	private String isZIQUorSong = null;
	/**
	 * 选定的上个界面传递过来的bean
	 */
	private OrderListBean mBeanP;
	/**
	 * // 组元素数据列表
	 */
	private List<GroupInfo> groups = new ArrayList<GroupInfo>();
	/**
	 * // 子元素数据列表
	 */
	private Map<String, List<ProductInfo>> children = new HashMap<String, List<ProductInfo>>();
	/**
	 * 本类对象
	 */
	public static AllOrderActivity instance;
	/**
	 * 倒三角按钮
	 */
	private ImageView imageView_down;
	/**
	 * 
	 */
	private TextView mQUANBU, mCANYIN, mSHANGPN, mYONGCHE, mgoupiao,
			mzhongchou;
	/**
	 * 购票的时候需要用到的数据单号
	 */
	private String out_trade_no;
	private static NetService netService;
	/**
	 * 易道订单id
	 */
	private String mYiDaoOrderId;
	/**
	 * 易道支付成功后参数
	 */
	private String feeStr = "";
	private String discountStr = "";
	/**
	 * 用来标注订单页面是否需要刷新数据
	 */
	public boolean IS_NEED_REFRENSH = false;
	public PullToRefreshExpandableListView mPullToRefreshExpandableListView;// 刷新完成后关闭动画
	/**
	 * 进度条布局
	 */
	private RelativeLayout mLayout_progressbar;
	/**
	 * 通知永乐 购票成功 参数 永乐订单ID
	 */
	private String ylOrderId;
	/**
	 * 通知永乐 购票成功 参数 购票交易号
	 */
	private String ylPayNo;
	/**
	 * 通知永乐 购票成功 参数 购票金额
	 */
	private String ylTotalPrice;
	/**
	 * 成功获取 永乐检测数据
	 */
	private final int SUCCESS = 1111;
	/**
	 * 获取永乐检测数据失败
	 */
	private final int FILERE = 2222;
	/**
	 * 下拉刷新成功
	 */
	private final int REFRESH_SUCCESS = 123;
	/**
	 * 下拉刷新失败
	 */
	private final int REFRESH_FILERE = 124;
	/**
	 * 获取数据失败
	 */
	private final int GET_DATA_ERROR = 400;
	/**
	 * 获取数据失败
	 */
	private final int NOTIFY_YONG = 777;
	/**
	 * 通知永乐成功
	 */
	private final int NOTIFY_SUCCESS = 666;
	/**
	 * 生成支付订单
	 */
	private final int CREATE_ORDER = 42;
	/**
	 * 商品或者餐饮送货成功处理
	 */
	private final int TAKE_SUCCESS = 52;
	/**
	 * 用车订单获取失败
	 */
	private final int USE_CAR_ORDER_FAILED=111;
	/**
	 * 用车订单获取chenggong
	 */
	private final int USE_CAR_ORDER_SUCCESS=11;
	private final int HANDLE_TAG_40=40;
	private final int HANDLE_TAG_41=41;
	private final int HANDLE_TAG_50=50;
	private final int HANDLE_TAG_51=51;
	private final int HANDLE_TAG_221=221;
	private final int HANDLE_TAG_222=222;
	private final int HANDLE_TAG_70=70;
	private final int HANDLE_TAG_80=80;
	private final int HANDLE_TAG_230=230;
	private final int HANDLE_TAG_330=330;
	private final int HANDLE_TAG_555=555;
	/**
	 * 全局订单购票
	 */
	private OrderListBean ticksOrderbean;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case NUMM_TAG:
				Toast.makeText(AllOrderActivity.this, "服务器异常，请稍后重试",
						Toast.LENGTH_SHORT).show();
				break;
			case DATA_ISHAVE:
				analyseOrderData((String) msg.obj);
				break;
			case REFRESH_SUCCESS:
				// 刷新成功
				analyseRefreshData((String) msg.obj);
				break;
			case REFRESH_FILERE:
				// 刷新失败
				mPullToRefreshExpandableListView.onRefreshComplete();
				break;
			case GET_DATA_ERROR:
				mLayout_progressbar.setVisibility(View.GONE);
				break;
			case USE_CAR_ORDER_SUCCESS:
				analyseCareData((String) msg.obj);
				break;
			case USE_CAR_ORDER_FAILED:
				useWayHandleusecarOrder();
				break;
			case HANDLE_TAG_40:
				// 支付参数是
				String backdatass = (String) msg.obj;
				if (!TextUtils.isEmpty(backdatass)) {
					PayBean paybean = useWayJsonDataPay(backdatass);
					if (paybean != null
							&& !TextUtils.isEmpty(paybean.getSign())) {
						// 开始调用方法，进行支付
						out_trade_no = paybean.getOut_trade_no();
						pay(paybean);
					}
				}
				break;
			case HANDLE_TAG_41:
				// 支付参数是
				String backdatass_ziqu = (String) msg.obj;
				if (!TextUtils.isEmpty(backdatass_ziqu)) {
					PayBean paybean = useWayJsonDataPay(backdatass_ziqu);
					if (paybean != null
							&& !TextUtils.isEmpty(paybean.getSign())) {
						// 开始调用方法，进行支付
						pay(paybean);
					}
				}
				break;
			case CREATE_ORDER:
				// 支付参数是
				createOrder((String) msg.obj);
				break;
			case HANDLE_TAG_50:
				// 众筹支付成功处理
				useHandleCrowdPaySuccess();
				break;
			case HANDLE_TAG_51:
				// 商品或者餐饮自取支付成功处理
				useWayHandleGOODSppAYSuccess();

				break;
			case TAKE_SUCCESS:
				takeSuccess();
				break;
			case HANDLE_TAG_221:
				// 提醒发货请求成功处理
				String mshaaa = (String) msg.obj;
				if (!TextUtils.isEmpty(mshaaa)) {
					useWayTixingfahuo(mshaaa);
				} else {
					Toast.makeText(AllOrderActivity.this, "网络繁忙", Toast.LENGTH_SHORT).show();
				}
				break;
			case HANDLE_TAG_222:
				// 提醒发货请求失败处理
				Toast.makeText(AllOrderActivity.this, "网络繁忙",  Toast.LENGTH_SHORT).show();
				break;
			case HANDLE_TAG_70:
				if (!TextUtils.isEmpty(out_trade_no)) {
					// 通知永乐购票成功
					useWayNotifySuccessGOUPIAO();
				}
				break;
			case HANDLE_TAG_80:
				payOk();
				break;
			case HANDLE_TAG_230:
				// 处理账单确认
				String feeBackdata = (String) msg.obj;
				if(!TextUtils.isEmpty(feeBackdata)){
					useWayCheckFeeHandle(feeBackdata);
				}
				break;
			case HANDLE_TAG_330:
				// 处理生成的支付订单返回（）
				String jsondatass = (String) msg.obj;
				if (jsondatass != null && !TextUtils.isEmpty(jsondatass)) {
					PayBean paybean = useWayJsonDataPay(jsondatass);
					if (paybean != null
							&& !TextUtils.isEmpty(paybean.getSign())) {
						// 开始调用方法，进行支付
						pay(paybean);
					}
				}
				break;
			case HANDLE_TAG_555:
				// 易道支付成功回调
				String resultPay = (String) msg.obj;
				if(!TextUtils.isEmpty(resultPay)){
					useUseCarNotifySuccess(resultPay);
				}
				break;
			case NOTIFY_SUCCESS:
				annliysYLe((String) msg.obj);
				break;
			case NOTIFY_YONG:
				notifyYLe();
				break;
			case SUCCESS:
				String data = (String) msg.obj;
				anilyseYL(data);
				break;
			case FILERE:
				Toast.makeText(AllOrderActivity.this, "服务器异常",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}



	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_allorder);
		// 调用方法，从网络获取数据
		instance = this;
		initview();
		initpopuwindow();
		initListener();
		// useWayGetDataFromInternet();
	}

	/**
	 * 提供给子view界面用来刷新数据
	 * 
	 * @param mPullToRefreshExpandableListViews
	 */
	public void refrenshData(
			PullToRefreshExpandableListView mPullToRefreshExpandableListViews) {
		this.mPullToRefreshExpandableListView = mPullToRefreshExpandableListViews;
		useWayGetDataFromInternet_down();
	}

	private void useWayCheckDataToView(List<OrderListBean> allList) {
		// TODO Auto-generated method stub
		switch (IS_SHOW_VIEW) {
		case 0:
			// 说明当前是全部view
			switch (IS_SHOW_MENU) {
			case 0:
				// 全部view里面显示全部订单
				// 调用方法，在全部view里面进行数据刷新
				if (allList != null && allList.size() != 0) {
					allOrderView.setSecondData(allList);
				} else {
					allOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
				}
				break;
			case 1:
				// 全部view里面显示餐饮订单
				// 获取餐饮的全部订单
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_canyin = useWayCheckData_canyin(allList);
					if (list_canyin != null && list_canyin.size() != 0) {
						allOrderView.setSecondData(list_canyin);
					} else {
						allOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
					}
				} else {
					allOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
				}
				break;
			case 2:
				// 全部view里面显示商品订单
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_canyin = useWayCheckData_shangpin(allList);
					if (list_canyin != null && list_canyin.size() != 0) {
						allOrderView.setSecondData(list_canyin);
					} else {
						allOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
					}
				} else {
					allOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
				}
				break;
			case 3:
				// 全部view里面显示门票订单
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_canyin = useWayCheckData_menpiao(allList);
					if (list_canyin != null && list_canyin.size() != 0) {
						allOrderView.setSecondData(list_canyin);
					} else {
						allOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
					}
				} else {
					allOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
				}
				break;
			case 4:
				// 全部view里面显示用车订单
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_canyin = useWayCheckData_yongche(allList);
					if (list_canyin != null && list_canyin.size() != 0) {
						allOrderView.setSecondData(list_canyin);
					} else {
						allOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
					}
				} else {
					allOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
				}
				break;
			case 5:
				// 全部view里面显示众筹订单
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_canyin = useWayCheckData_zhongchou(allList);
					if (list_canyin != null && list_canyin.size() != 0) {
						allOrderView.setSecondData(list_canyin);
					} else {
						allOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
					}
				} else {
					allOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
				}
				break;

			default:
				break;
			}
			break;
		case 1:
			// 说明当前是在待支付view
			switch (IS_SHOW_MENU) {
			case 0:
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_waitall = useWayCheckData_waitpay(allList);
					if (list_waitall != null && list_waitall.size() != 0) {
						waitPayOrderView.sencondData(list_waitall);
					} else {
						waitPayOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				} else {
					waitPayOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
				}
				// 待支付view里面显示全部订单
				break;
			case 1:
				// 待支付view里面显示餐饮订单
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_waitall = useWayCheckData_can_daizhifu(allList);
					if (list_waitall != null && list_waitall.size() != 0) {
						waitPayOrderView.sencondData(list_waitall);
					} else {
						waitPayOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				} else {
					waitPayOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
				}
				break;
			case 2:
				// 待支付view里面显示商品订单
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_waitall = useWayCheckData_daifukuan_goods(allList);
					if (list_waitall != null && list_waitall.size() != 0) {
						waitPayOrderView.sencondData(list_waitall);
					} else {
						waitPayOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				} else {
					waitPayOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
				}
				break;
			case 3:
				// 待支付view里面显示门票订单
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_waitall = useWayCheckData_menpiao_daizhifu(allList);
					if (list_waitall != null && list_waitall.size() != 0) {
						waitPayOrderView.sencondData(list_waitall);
					} else {
						waitPayOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				} else {
					waitPayOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
				}
				break;
			case 4:
				// 待支付view里面显示用车订单
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_waitall = useWayCheckData_yongche_daizhifu(allList);
					if (list_waitall != null && list_waitall.size() != 0) {
						waitPayOrderView.sencondData(list_waitall);
					} else {
						waitPayOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				} else {
					waitPayOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
				}
				break;
			case 5:
				// 待支付view里面显示众筹订单
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_waitall = useWayCheckData_zhongchou_daizhifu(allList);
					if (list_waitall != null && list_waitall.size() != 0) {
						waitPayOrderView.sencondData(list_waitall);
					} else {
						waitPayOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				} else {
					waitPayOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
				}
				break;
			default:
				break;
			}
			break;
		case 2:
			// 说明当前是在待发货view
			switch (IS_SHOW_MENU) {
			case 0:
				// 待发货view里面显示全部订单
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_daifahuo = useWayCheckData_daifahuo(allList);
					if (list_daifahuo != null && list_daifahuo.size() != 0) {
						waitSendOrderView.secondData(list_daifahuo);
					} else {
						waitSendOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				} else {
					waitSendOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
				}
				break;
			case 1:
				// 待发货view里面显示餐饮订单
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_daifahuo = useWayCheckData_can_daifahuo(allList);
					if (list_daifahuo != null && list_daifahuo.size() != 0) {
						waitSendOrderView.secondData(list_daifahuo);
					} else {
						waitSendOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				} else {
					waitSendOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
				}
				break;
			case 2:
				// 待发货view里面显示商品订单
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_daifahuo = useWayCheckData_daifahuo_goods(allList);
					if (list_daifahuo != null && list_daifahuo.size() != 0) {
						waitSendOrderView.secondData(list_daifahuo);
					} else {
						waitSendOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				} else {
					waitSendOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
				}
				break;
			case 3:
				// 待发货view里面显示门票订单
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_daifahuo = useWayCheckData_menpiao_daifahuo(allList);
					if (list_daifahuo != null && list_daifahuo.size() != 0) {
						waitSendOrderView.secondData(list_daifahuo);
					} else {
						waitSendOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				} else {
					waitSendOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
				}
				break;
			case 4:
				// 待发货view里面显示用车订单
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_daifahuo = useWayCheckData_yongche_daifahuo(allList);
					if (list_daifahuo != null && list_daifahuo.size() != 0) {
						waitSendOrderView.secondData(list_daifahuo);
					} else {
						waitSendOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				} else {
					waitSendOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
				}
				break;
			case 5:
				// 待发货view里面显示众筹订单
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_daifahuo = useWayCheckData_zhongchou_daifahuo(allList);
					if (list_daifahuo != null && list_daifahuo.size() != 0) {
						waitSendOrderView.secondData(list_daifahuo);
					} else {
						waitSendOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				} else {
					waitSendOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
				}
				break;
			default:
				break;
			}
			break;
		case 3:
			// 说明当前是在待收货view
			switch (IS_SHOW_MENU) {
			case 0:
				// 待收货view里面显示全部订单
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_daifahuo = useWayCheckData_daishouhuo(allList);
					if (list_daifahuo != null && list_daifahuo.size() != 0) {
						waitReceiverOrderView.secondData(list_daifahuo);
					} else {
						waitReceiverOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				} else {
					waitReceiverOrderView.mLayoutNoData
							.setVisibility(View.VISIBLE);
				}
				break;
			case 1:
				// 待收货view里面显示餐饮订单
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_daifahuo = useWayCheckData_can_daishouhuo(allList);
					if (list_daifahuo != null && list_daifahuo.size() != 0) {
						waitReceiverOrderView.secondData(list_daifahuo);
					} else {
						waitReceiverOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				} else {
					waitReceiverOrderView.mLayoutNoData
							.setVisibility(View.VISIBLE);
				}
				break;
			case 2:
				// 待收货view里面显示商品订单
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_daifahuo = useWayCheckData_daishouhuo_goods(allList);
					if (list_daifahuo != null && list_daifahuo.size() != 0) {
						waitReceiverOrderView.secondData(list_daifahuo);
					} else {
						waitReceiverOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				} else {
					waitReceiverOrderView.mLayoutNoData
							.setVisibility(View.VISIBLE);
				}
				break;
			case 3:
				// 待收货view里面显示门票订单
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_daifahuo = useWayCheckData_menpiao_daishouhuo(allList);
					if (list_daifahuo != null && list_daifahuo.size() != 0) {
						waitReceiverOrderView.secondData(list_daifahuo);
					} else {
						waitReceiverOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				} else {
					waitReceiverOrderView.mLayoutNoData
							.setVisibility(View.VISIBLE);
				}
				break;
			case 4:
				// 待收货view里面显示用车订单
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_daifahuo = useWayCheckData_yongche_daishouhuo(allList);
					if (list_daifahuo != null && list_daifahuo.size() != 0) {
						waitReceiverOrderView.secondData(list_daifahuo);
					} else {
						waitReceiverOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				} else {
					waitReceiverOrderView.mLayoutNoData
							.setVisibility(View.VISIBLE);
				}
				break;
			case 5:
				// 待收货view里面显示众筹订单
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_daifahuo = useWayCheckData_zhongchou_daishouhuo(allList);
					if (list_daifahuo != null && list_daifahuo.size() != 0) {
						waitReceiverOrderView.secondData(list_daifahuo);
					} else {
						waitReceiverOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				} else {
					waitReceiverOrderView.mLayoutNoData
							.setVisibility(View.VISIBLE);
				}
				break;
			default:
				break;
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 该方法用来在未登录的时候调用，显示提示信息，让用户来进行登录
	 */
	public void createDialog() {
		exitDialog = new CustomDialog(this, new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AllOrderActivity.this,
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
		exitDialog.setRemindMessage("只有登录以后才能查看用户订单信息哦");
		exitDialog.show();
	}

	private void pay(PayBean paybean) {
		if (paybean != null && !TextUtils.isEmpty(paybean.getSign())) {
			if ("mengpiao".equals(isType)) {
				ylTotalPrice = paybean.getPrice();
				ylPayNo = paybean.getMerchant_business_id();
				ylOrderId = paybean.getOut_trade_no();
			}
			PayParametric parameters = new PayParametric();
			parameters.setVersion(paybean.getVersion());
			parameters.setSign(paybean.getSign());
			parameters.setMerchant_business_id(paybean
					.getMerchant_business_id());
			parameters.setService(paybean.getService());
			parameters.setUser_id(paybean.getUser_id());
			parameters.setUser_name(paybean.getUser_name());
			parameters.setNotify_url(paybean.getNotify_url());
			parameters.setMerchant_no(paybean.getMerchant_no());
			parameters.setOut_trade_no(paybean.getOut_trade_no());
			String price = paybean.getPrice();
			parameters.setPrice(price);
			parameters.setCurrency(paybean.getCurrency());
			String expric = paybean.getPay_expire();
			parameters.setPay_expire(expric);
			parameters.setProduct_id(paybean.getProduct_id());
			parameters.setProduct_name(paybean.getProduct_name());
			parameters.setProduct_desc(paybean.getProduct_desc());
			if (!TextUtils.isEmpty(paybean.getProduct_urls())) {
				parameters.setProduct_urls(paybean.getProduct_urls());
			} else {
				parameters.setProduct_urls(paybean.getNotify_url());
			}
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
			LePayApi.initConfig(AllOrderActivity.this, config);
			LePayApi.doPay(AllOrderActivity.this, param, new ILePayCallback() {
				@Override
				public void payResult(ELePayState status, String message) {
					if (ELePayState.CANCEL == status) { // 支付取消

					} else if (ELePayState.FAILT == status) { // 支付失败

					} else if (ELePayState.OK == status) { // 支付成功

						if (isType.equals("canyin")) {
							if (isZIQUorSong.equals("ziqu")) {
								Message msg = new Message();
								msg.arg1 = HANDLE_TAG_51;
								handler.sendMessage(msg);
							} else if (isZIQUorSong.equals("songhuo")) {
								Message msg = new Message();
								msg.arg1 = TAKE_SUCCESS;
								handler.sendMessage(msg);
							}
						} else if (isType.equals("shangpin")) {
							if (isZIQUorSong.equals("ziqu")) {
								Message msg = new Message();
								msg.arg1 = HANDLE_TAG_51;
								handler.sendMessage(msg);
							} else if (isZIQUorSong.equals("songhuo")) {
								Message msg = new Message();
								msg.arg1 = TAKE_SUCCESS;
								handler.sendMessage(msg);
							}
						} else if (isType.equals("zhongchou")) {
							Message msg = new Message();
							msg.arg1 = HANDLE_TAG_50;
							handler.sendMessage(msg);
						} else if (isType.equals("mengpiao")) {
							Message msg = new Message();
							msg.arg1 = HANDLE_TAG_70;
							handler.sendMessage(msg);
						} else if (isType.equals("yongche")) {
							Message msg = new Message();
							msg.arg1 = HANDLE_TAG_80;
							handler.sendMessage(msg);
						}
					} else if (ELePayState.WAITTING == status) { // 支付中

					}
				}
			});
		} else {
			Toast.makeText(AllOrderActivity.this, "支付异常", Toast.LENGTH_SHORT)
					.show();
		}

	}

	private void virtualData(OrderListBeanGoodsBean bean) {
		groups.clear();
		children.clear();
		// 首先，判断组集合中是否存在该组id
		boolean ishave = checkGroupIsHaveTheBean(bean);
		if (ishave) {
			// 说明已经存在
			// 判断该组id以及该商品是否存在在子集合当中
			boolean ishavess = checkChildIsHava(bean);
			if (ishavess) {
				// 说明已经存在
			} else {
				ProductInfo infos = new ProductInfo();
				infos.setCount(Integer.parseInt(bean.getWareNumber()));
				infos.setId(bean.getgId());
				infos.setImageUrl(bean.getMediumImg());
				infos.setName(bean.getGoodsName());
				// infos.setYunfei(Integer.parseInt(bean.getFreight()));
				infos.setPrice(Double.parseDouble(bean.getPrice()));
				children.get(bean.getClassicId()).add(infos);
			}
		} else {
			// 说明未存在，
			GroupInfo info = new GroupInfo();
			info.setId(bean.getClassicId());
			info.setName(bean.getClassicName());
			groups.add(info);
			ProductInfo infos = new ProductInfo();
			infos.setCount(Integer.parseInt(bean.getWareNumber()));
			infos.setDesc(bean.getGoodsName());
			infos.setSeller(bean.getSeller());
			// infos.setYunfei(Integer.parseInt(bean.getFreight()));
			infos.setId(bean.getgId());
			infos.setImageUrl(bean.getMediumImg());
			infos.setPrice(Double.parseDouble(bean.getPrice().trim()));
			List<ProductInfo> list = new ArrayList<ProductInfo>();
			list.add(infos);
			children.put(bean.getClassicId(), list);
		}
	}

	/**
	 * 在当前组集合中判断，该组id是否已经存在
	 * 
	 * @param bean
	 */
	private boolean checkGroupIsHaveTheBean(OrderListBeanGoodsBean bean) {
		boolean ishave = false;
		for (int i = 0; i < groups.size(); i++) {
			if (groups.get(i).getId().equals(bean.getClassicId())) {
				// 说明该组已经存在了
				ishave = true;
				break;
			} else {
				ishave = false;
			}
		}
		return ishave;

	}

	/**
	 * 在当前子集合中判断，该商品是否已经存在在子的集合当中
	 * 
	 * @param bean
	 */
	private boolean checkChildIsHava(OrderListBeanGoodsBean bean) {
		boolean ishaves = false;
		// 首先判断该组是否在子map中存在
		if (children.containsKey(bean.getClassicId())) {
			// 说明该key已经存在，所以
			// 根据该key来获取list集合，在集合中判断该商品是否存在
			List<ProductInfo> list = children.get(bean.getClassicId());
			boolean ishave = checkTheGoodsIsHave(list, bean);
			if (ishave) {
				// 说明已经存在
				ishaves = ishave;
			} else {
				ishaves = ishave;
			}
		}
		return ishaves;
	}

	/**
	 * 判断该商品是否存在在该集合中
	 * 
	 * @param list
	 * @param bean
	 */
	private boolean checkTheGoodsIsHave(List<ProductInfo> list,
			OrderListBeanGoodsBean bean) {
		// TODO Auto-generated method stub
		boolean ishave = false;
		for (int i = 0; i < list.size(); i++) {
			if (bean.getgId().equals(list.get(i).getId())) {
				ishave = true;
				break;
			} else {
				ishave = false;
			}
		}
		return ishave;
	}

	/**
	 * 提供给适配器用于进行提醒发货
	 */
	public void TixingStoreSendGoods(String id) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("orderId", id);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.TIXING_SEND_GOODS, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						// TODO Auto-generated method stub
						if (data == null) {
							// 说明网络获取无数据
						} else {
							Object obj = data.getObject();
							String backdata = obj.toString();
							if (TextUtils.isEmpty(backdata)) {
							} else {
								Log.i("提醒发货",
										"走到这里了么？" + data.getNetResultCode());
								// 调用方法，解析数据
								Log.i("提醒发货", backdata);
								// 调用方法，将数据加载到适配器中
								if (data.getNetResultCode() == 0) {
									Message msg = new Message();
									msg.arg1 = HANDLE_TAG_221;
									msg.obj = backdata;
									handler.sendMessage(msg);
								} else {
									Message msg = new Message();
									msg.arg1 = HANDLE_TAG_222;
									handler.sendMessage(msg);
								}

							}
						}
					}

				}, false, false);
	}

	/**
	 * 提供给适配器用于适配器当中支付的方法
	 * 
	 * @param bean
	 */
	public void makeOrderData(String tag, OrderListBean bean) {
		MobclickAgent.onEvent(AllOrderActivity.this, "pay");
		mBeanP = bean;
		if (tag.equals("canyin")) {
			isType = "canyin";
			String orderid = bean.getId();
			List<OrderListBeanGoodsBean> list = bean.getList();
			String useid = GlobalParams.USER_ID;
			String ip = getPsdnIp();
			String tags = "int";
			double goodsprice = 0;
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					double price = Double.parseDouble(list.get(i).getPrice());
					goodsprice = goodsprice + price;
				}
			}
			String currency = "CNY";
			String pay_expire = null;
			double yunfei = Double.parseDouble(bean.getFreight());
			pay_expire = (goodsprice + yunfei) + "";
			String product_id = bean.getList().get(0).getGoodsId();
			String product_name = bean.getList().get(0).getGoodsName();
			String product_desc = bean.getList().get(0).getGoodsName();
			String product_urls = bean.getList().get(0).getSmallImg();
			Log.i("商品参数", product_id + "名称" + product_name + "描述"
					+ product_desc + "地址" + product_urls);
			// product_id="94188";
			// product_name="果汁";
			// product_desc="这是一杯果汁";
			// product_urls="http://serverurl/product_url";
			if (bean.getCourier().equals("0")) {
				isZIQUorSong = "ziqu";
				UseWayGetPayData_ziqu(orderid, useid, ip, goodsprice + "",
						currency, pay_expire, product_id, product_name,
						product_desc, product_urls);
			} else if (bean.getCourier().equals("1")) {
				isZIQUorSong = "songhuo";
				UseWayGetPayData_songhuo(orderid, useid, ip, goodsprice + "",
						currency, pay_expire, product_id, product_name,
						product_desc, product_urls);
			}
		} else if (tag.equals("shangpin")) {
			isType = "shangpin";
			String orderid = bean.getId();
			List<OrderListBeanGoodsBean> list = bean.getList();
			String useid = GlobalParams.USER_ID;
			String ip = getPsdnIp();
			double goodsprice = 0;
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					double price = Double.parseDouble(list.get(i).getPrice());
					goodsprice = goodsprice + price;
				}
			}
			String currency = "CNY";
			double yunfei = Double.parseDouble(bean.getFreight());
			String pay_expire = (goodsprice + yunfei) + "";
			String product_id = bean.getList().get(0).getGoodsId();
			String product_name = bean.getList().get(0).getGoodsName();
			String product_desc = bean.getList().get(0).getGoodsName();
			String product_urls = bean.getList().get(0).getSmallImg();
			// product_id="94188";
			// product_name="果汁";
			// product_desc="这是一杯果汁";
			// product_urls="http://serverurl/product_url";
			if (bean.getCourier().equals("0")) {
				isZIQUorSong = "ziqu";
				UseWayGetPayData_ziqu(orderid, useid, ip, goodsprice + "",
						currency, pay_expire, product_id, product_name,
						product_desc, product_urls);
			} else if (bean.getCourier().equals("1")) {
				isZIQUorSong = "songhuo";
				UseWayGetPayData_songhuo(orderid, useid, ip, goodsprice + "",
						currency, pay_expire, product_id, product_name,
						product_desc, product_urls);
			}

		} else if (tag.equals("zhongchou")) {
			isType = "zhongchou";
			String orderid = bean.getId();
			List<OrderListBeanZhongchouBean> list = bean.getList_zhong();
			String useid = GlobalParams.USER_ID;
			String ip = getPsdnIp();
			double goodsprice = 0;
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					double price = Double.parseDouble(list.get(i).getPrice());
					goodsprice = goodsprice + price;
				}
			}
			String currency = "CNY";
			double yunfei = Double.parseDouble(bean.getFreight());
			String pay_expire = (goodsprice + yunfei) + "";
			String product_id = bean.getList_zhong().get(0).getId();
			String product_name = bean.getList_zhong().get(0).getReturnName();
			String product_desc = bean.getList_zhong().get(0)
					.getReturnContent();
			String product_urls = bean.getList_zhong().get(0)
					.getPropagatePicture();
			UseWayGetPayData(orderid, useid, ip, goodsprice + "", currency,
					pay_expire, product_id, product_name, product_desc,
					product_urls);
		} else if (tag.equals("mengpiao")) {
			isType = "mengpiao";
			ticksOrderbean = bean;
			// / 查询 该订单是否有效
			checkIsEffective(bean.getOrderNumber());
		} else if (tag.equals("yongche")) {
			isType = "yongche";
			/**
			 * 从易道获取价格
			 */
			mYiDaoOrderId = bean.getList_car().get(0).getCar_goodsName();
			requestOrderFee(bean.getList_car().get(0).getCar_goodsName());
		}
	}

	/**
	 * 从易道获取价格
	 */
	public void requestOrderFee(String orderid) {
		Log.i("lwc", "传入的订单id是多少" + orderid);
		netService = NetService.getInStance();

		// netService.setParams("order_id", LApplication.yidaoOrderId);
		netService.setParams("access_token", ConstantValue.CAR_TOKEN);
		netService.setHttpMethod(HttpSetting.GET_MODE);
		netService.setUrl(ConstantValue.GET_ORDER_FEE + orderid);
		netService.loader(new NetLoadListener() {
			@Override
			public void onloaderListener(BackData result) {
				Message msg = new Message();
				msg.arg1 = HANDLE_TAG_230;
				msg.obj = result.getObject();
				handler.sendMessage(msg);
			}
		});
	}

	/**
	 * 获取详细数据对象
	 * 
	 * @param pinfo
	 * @return
	 */
	private TicesDetailBean GetDataFromDetail(String pinfo) {
		// TODO Auto-generated method stub
		TicesDetailBean bean = new TicesDetailBean();
		if (!TextUtils.isEmpty(pinfo)) {
			try {
				JSONObject obj = new JSONObject(pinfo);
				if (obj.has("productName")) {
					bean.setPicture(obj.getString("productName"));
				}
				if (obj.has("priceTag")) {
					bean.setPriceTag(obj.getString("priceTag"));
				}
				if (obj.has("productName")) {
					bean.setProductName(obj.getString("productName"));
				}
				if (obj.has("seatNumber")) {
					bean.setSeatNumber(obj.getString("seatNumber"));
				}

				return bean;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 网络获取支付时候需要提交的参数
	 * 
	 * @param orderId
	 *            订单号
	 * @param username
	 *            用户名
	 * @param ip
	 *            当前网络ip
	 * @param goodsprice
	 *            商品价格
	 * @param currency
	 *            当前
	 * @param pay_expire
	 *            运费
	 * @param product_id
	 *            商品id
	 * @param product_name
	 *            商品名
	 * @param product_desc
	 *            商品描述
	 * @param product_urls
	 *            商品图片url
	 */
	private void UseWayGetPayData(String orderId, String username, String ip,
			String goodsprice, String currency, String pay_expire,
			String product_id, String product_name, String product_desc,
			String product_urls) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("user_name", username);
		params.put("price", goodsprice);
		params.put("currency", currency);
		params.put("pay_expire", pay_expire);
		params.put("product_id", product_id);
		params.put("product_name", product_name);
		params.put("product_desc", product_desc);
		params.put("orderId", orderId);
		params.put("ip", ip);
		params.put("product_urls", product_urls);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.GET_PAY_DATA, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						// TODO Auto-generated method stub
						if (data == null) {
							// 说明网络获取无数据
						} else {
							Object obj = data.getObject();
							String backdata = obj.toString();
							if (backdata == null && backdata.equals("")) {
							} else {
								// 调用方法，解析数据
								// 调用方法，将数据加载到适配器中
								Message msg = new Message();
								msg.arg1 = HANDLE_TAG_40;
								msg.obj = backdata;
								handler.sendMessage(msg);
							}
						}
					}

				}, false, false);
	}

	/**
	 * 商品自取的时候需要用这个方法
	 * 
	 * @param orderId
	 * @param username
	 * @param ip
	 * @param goodsprice
	 * @param currency
	 * @param pay_expire
	 * @param product_id
	 * @param product_name
	 * @param product_desc
	 * @param product_urls
	 */
	private void UseWayGetPayData_ziqu(String orderId, String username,
			String ip, String goodsprice, String currency, String pay_expire,
			String product_id, String product_name, String product_desc,
			String product_urls) {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("user_name", username);
		params.put("price", goodsprice);
		params.put("currency", currency);
		params.put("pay_expire", pay_expire);
		params.put("product_id", product_id);
		params.put("product_name", product_name);
		params.put("product_desc", product_desc);
		params.put("orderId", orderId);
		params.put("ip", ip);
		params.put("product_urls", product_urls);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.GET_PAY_DATA, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						if (data == null) {
							// 说明网络获取无数据
						} else {
							Object obj = data.getObject();
							String backdata = obj.toString();
							if (TextUtils.isEmpty(backdata)) {
							} else {
								// 调用方法，将数据加载到适配器中
								Message msg = new Message();
								msg.arg1 = HANDLE_TAG_41;
								msg.obj = backdata;
								handler.sendMessage(msg);
							}
						}
					}

				}, false, false);
	}

	/**
	 * 商品送货的时候需要用这个方法
	 * 
	 * @param orderId
	 * @param username
	 * @param ip
	 * @param goodsprice
	 * @param currency
	 * @param pay_expire
	 * @param product_id
	 * @param product_name
	 * @param product_desc
	 * @param product_urls
	 */
	private void UseWayGetPayData_songhuo(String orderId, String username,
			String ip, String goodsprice, String currency, String pay_expire,
			String product_id, String product_name, String product_desc,
			String product_urls) {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("user_name", username);
		params.put("price", goodsprice);
		params.put("currency", currency);
		params.put("pay_expire", pay_expire);
		params.put("product_id", product_id);
		params.put("product_name", product_name);
		params.put("product_desc", product_desc);
		params.put("orderId", orderId);
		params.put("ip", ip);
		params.put("product_urls", product_urls);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.GET_PAY_DATA, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						// TODO Auto-generated method stub
						if (data == null) {
							// 说明网络获取无数据
						} else {
							Object obj = data.getObject();
							String backdata = obj.toString();
							if (backdata == null && backdata.equals("")) {
							} else {
								Log.i("生成支付订单",
										"走到这里了么？" + data.getNetResultCode());
								// 调用方法，解析数据
								Log.i("LWC,真正的支付参数", backdata);
								// 调用方法，将数据加载到适配器中
								Message msg = new Message();
								msg.arg1 = CREATE_ORDER;
								msg.obj = backdata;
								handler.sendMessage(msg);
							}
						}
					}

				}, false, false);
	}

	/**
	 * 用来获取手机拨号上网（包括CTWAP和CTNET）时由PDSN分配给手机终端的源IP地址。
	 * 
	 * @return
	 * @author SHANHY
	 */
	public static String getPsdnIp() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& inetAddress instanceof Inet4Address) {
						// if (!inetAddress.isLoopbackAddress() && inetAddress
						// instanceof Inet6Address) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (Exception e) {
		}
		return "";
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 主要用来处理用户第一次进入界面未登录，第二次登录以后的情况
		// 这里需要进行数据刷新
		// useWayGetDataFromInternet_down();
		useWayGetDataFromInternet();
	}

	/**
	 * 该方法用来从网络获取订单数据
	 */
	private void useWayGetDataFromInternet() {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("ordersType", "");
		params.put("status", "");
		params.put("pageNo", "1");
		params.put("rows", "20");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ORDER_LIST, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						// TODO Auto-generated method stub
						if (data == null) {
							// 说明网络获取无数据
							Message msg = new Message();
							msg.arg1 = NUMM_TAG;
							handler.sendMessage(msg);

						} else {
							Object obj = data.getObject();
							if (obj == null) {
								Message msg = new Message();
								msg.arg1 = NUMM_TAG;
								handler.sendMessage(msg);
							} else {
								String backdata = obj.toString();
								if (backdata == null
										&& !TextUtils.isEmpty(backdata)) {
									Message msg = new Message();
									msg.arg1 = NUMM_TAG;
									handler.sendMessage(msg);
								} else {
									Log.i("订单",
											"走到这里了么？" + data.getNetResultCode());
									if (data.getNetResultCode() == 0) {
										Message msg = new Message();
										msg.arg1 = DATA_ISHAVE;
										msg.obj = backdata;
										handler.sendMessage(msg);
									} else {
										Message msg = new Message();
										msg.arg1 = GET_DATA_ERROR;
										handler.sendMessage(msg);
									}

								}
							}

						}
					}
				}, false, false);
	}

	/**
	 * 该方法用来从网络获取订单数据
	 */
	private void useWayGetDataFromInternet_down() {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("ordersType", "");
		params.put("status", "");
		params.put("pageNo", "1");
		params.put("rows", "20");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ORDER_LIST, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						// TODO Auto-generated method stub
						if (data == null) {
							// 说明网络获取无数据
							Message msg = new Message();
							msg.arg1 = NUMM_TAG;
							handler.sendMessage(msg);

						} else {
							Object obj = data.getObject();
							if (obj == null) {
								Message msg = new Message();
								msg.arg1 = NUMM_TAG;
								handler.sendMessage(msg);
							} else {
								String backdata = obj.toString();
								if (backdata == null
										&& !TextUtils.isEmpty(backdata)) {
									Message msg = new Message();
									msg.arg1 = NUMM_TAG;
									handler.sendMessage(msg);
								} else {
									Log.i("订单",
											"走到这里了么？" + data.getNetResultCode());
									if (data.getNetResultCode() == 0) {
										Message msg = new Message();
										msg.arg1 = REFRESH_SUCCESS;
										msg.obj = backdata;
										handler.sendMessage(msg);
									} else {
										Message msg = new Message();
										msg.arg1 = REFRESH_FILERE;
										handler.sendMessage(msg);
									}

								}
							}

						}
					}
				}, false, false);
	}

	/**
	 * 用来初始化弹窗window
	 */
	private void initpopuwindow() {
		// TODO Auto-generated method stub
		popFilter = new PopupWindow(AllOrderActivity.this);
		LayoutInflater inflater = (LayoutInflater) AllOrderActivity.this
				.getSystemService(AllOrderActivity.this.LAYOUT_INFLATER_SERVICE);
		View viewFilter = inflater.inflate(R.layout.window_order, null);
		initWindowListvew(viewFilter);
		ll_popul_filter = (LinearLayout) viewFilter
				.findViewById(R.id.layout_window_order);
		int ScreenHeight = getScreenHeight(AllOrderActivity.this);
		popFilter.setWidth(LayoutParams.MATCH_PARENT);
		popFilter.setHeight(ScreenHeight / 2);
		popFilter.setBackgroundDrawable(new BitmapDrawable());
		popFilter.setFocusable(true);
		popFilter.setOutsideTouchable(true);
		popFilter.setContentView(viewFilter);
	}

	/**
	 * 分拣数据，将数据加载到界面中
	 * 
	 * @param allList
	 */
	private void useWayFenjianData(List<OrderListBean> allList) {
		// TODO Auto-generated method stub
		List<OrderListBean> list_waitpay = new ArrayList<OrderListBean>();
		List<OrderListBean> list_waitfahuo = new ArrayList<OrderListBean>();
		List<OrderListBean> list_waitshouhuo = new ArrayList<OrderListBean>();
		if (allList != null && allList.size() != 0) {
			int count = allList.size();
			for (int i = 0; i < count; i++) {
				OrderListBean bean = allList.get(i);
				if (bean.getOrdersType().equals("2")) {

					if (bean.getStatus().equals("1")) {
						list_waitpay.add(bean);
					} else if (bean.getStatus().equals("2")) {
						list_waitfahuo.add(bean);
					} else if (bean.getStatus().equals("3")) {
						list_waitshouhuo.add(bean);
					}
				} else {
					if (bean.getStatus().equals("0")) {
						list_waitpay.add(bean);
					} else if (bean.getStatus().equals("1")) {
						list_waitfahuo.add(bean);
					} else if (bean.getStatus().equals("2")) {
						list_waitshouhuo.add(bean);
					}
				}
			}
		}
		// 用view对象调用方法，将数据加到相应的界面中
		if (list_waitpay != null && list_waitpay.size() != 0) {
			waitPayOrderView.setFirstOrderData(list_waitpay);
		} else {
			waitPayOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
		}
		if (list_waitfahuo != null && list_waitfahuo.size() != 0) {
			waitSendOrderView.setFirstOrderData(list_waitfahuo);
		} else {
			waitSendOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
		}
		if (list_waitshouhuo != null && list_waitshouhuo.size() != 0) {
			waitReceiverOrderView.setFirstOrderData(list_waitshouhuo);
		} else {
			waitReceiverOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
		}
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
		mSHANGPN = (TextView) viewFilter
				.findViewById(R.id.order_window_menu_title_shangpin);
		mzhongchou = (TextView) viewFilter
				.findViewById(R.id.order_window_menu_title_zhongchou);
		mYONGCHE = (TextView) viewFilter
				.findViewById(R.id.order_window_menu_title_yongche);
		mgoupiao = (TextView) viewFilter
				.findViewById(R.id.order_window_menu_title_menpiao);
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
		layout_zhongchou.setOnClickListener(this);
		im_zhongchou = (ImageView) viewFilter
				.findViewById(R.id.order_window_menu_im_zhongchou);
		im_zhongchou.setVisibility(View.GONE);

	}

	/**
	 * 用于分拣用车数据
	 * 
	 * @param allList2
	 * @return
	 */
	private List<OrderListBean> useWayCheckData_yongche(
			List<OrderListBean> allList2) {
		// TODO Auto-generated method stub
		List<OrderListBean> list = new ArrayList<OrderListBean>();
		if (allList != null && allList.size() != 0) {
			for (int i = 0; i < allList.size(); i++) {
				OrderListBean bean = allList.get(i);
				if (bean.getOrdersType().equals("2")) {
					list.add(bean);
				}
			}
		}
		return list;
	}

	/**
	 * 用于分拣用车数据
	 * 
	 * @param allList2
	 * @return
	 */
	private List<OrderListBean> useWayCheckData_yongche_daizhifu(
			List<OrderListBean> allList2) {
		List<OrderListBean> list = new ArrayList<OrderListBean>();
		if (allList != null && allList.size() != 0) {
			for (int i = 0; i < allList.size(); i++) {
				OrderListBean bean = allList.get(i);
				if (bean.getOrdersType().equals("2")) {
					if (bean.getStatus().equals("0")) {
						list.add(bean);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 用于分拣用车数据
	 * 
	 * @param allList2
	 * @return
	 */
	private List<OrderListBean> useWayCheckData_yongche_daifahuo(
			List<OrderListBean> allList2) {
		List<OrderListBean> list = new ArrayList<OrderListBean>();
		if (allList != null && allList.size() != 0) {
			for (int i = 0; i < allList.size(); i++) {
				OrderListBean bean = allList.get(i);
				if (bean.getOrdersType().equals("2")) {
					if (bean.getStatus().equals("1")) {
						list.add(bean);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 用于分拣用车数据
	 * 
	 * @param allList2
	 * @return
	 */
	private List<OrderListBean> useWayCheckData_yongche_daishouhuo(
			List<OrderListBean> allList2) {
		List<OrderListBean> list = new ArrayList<OrderListBean>();
		if (allList != null && allList.size() != 0) {
			for (int i = 0; i < allList.size(); i++) {
				OrderListBean bean = allList.get(i);
				if (bean.getOrdersType().equals("2")) {
					if (bean.getStatus().equals("2")) {
						list.add(bean);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 用来分拣众筹数据
	 * 
	 * @param allList2
	 * @return
	 */
	private List<OrderListBean> useWayCheckData_zhongchou(
			List<OrderListBean> allList2) {
		List<OrderListBean> list = new ArrayList<OrderListBean>();
		if (allList != null && allList.size() != 0) {
			for (int i = 0; i < allList.size(); i++) {
				OrderListBean bean = allList.get(i);
				if (bean.getOrdersType().equals("3")) {
					list.add(bean);
				}
			}
		}
		return list;
	}

	/**
	 * 用来分拣众筹数据
	 * 
	 * @param allList2
	 * @return
	 */
	private List<OrderListBean> useWayCheckData_waitpay(
			List<OrderListBean> allList2) {
		// TODO Auto-generated method stub
		List<OrderListBean> list = new ArrayList<OrderListBean>();
		if (allList != null && allList.size() != 0) {
			for (int i = 0; i < allList.size(); i++) {
				OrderListBean bean = allList.get(i);
				if (bean.getStatus().equals("0")) {
					list.add(bean);
				}
			}
		}
		return list;
	}

	/**
	 * 用来分拣门票数据
	 * 
	 * @param allList2
	 * @return
	 */
	private List<OrderListBean> useWayCheckData_menpiao(
			List<OrderListBean> allList2) {
		// TODO Auto-generated method stub
		List<OrderListBean> list = new ArrayList<OrderListBean>();
		if (allList != null && allList.size() != 0) {
			for (int i = 0; i < allList.size(); i++) {
				OrderListBean bean = allList.get(i);
				if (bean.getOrdersType().equals("4")) {
					list.add(bean);
				}
			}
		}
		return list;
	}

	/**
	 * 用来分拣门票数据
	 * 
	 * @param allList2
	 * @return
	 */
	private List<OrderListBean> useWayCheckData_menpiao_daizhifu(
			List<OrderListBean> allList2) {
		List<OrderListBean> list = new ArrayList<OrderListBean>();
		if (allList != null && allList.size() != 0) {
			for (int i = 0; i < allList.size(); i++) {
				OrderListBean bean = allList.get(i);
				if (bean.getOrdersType().equals("4")) {
					if (bean.getStatus().equals("0")) {
						list.add(bean);
					}

				}
			}
		}
		return list;
	}

	/**
	 * 用来分拣门票数据
	 * 
	 * @param allList2
	 * @return
	 */
	private List<OrderListBean> useWayCheckData_menpiao_daifahuo(
			List<OrderListBean> allList2) {
		List<OrderListBean> list = new ArrayList<OrderListBean>();
		if (allList != null && allList.size() != 0) {
			for (int i = 0; i < allList.size(); i++) {
				OrderListBean bean = allList.get(i);
				if (bean.getOrdersType().equals("4")) {
					if (bean.getStatus().equals("1")) {
						list.add(bean);
					}

				}
			}
		}
		return list;
	}

	/**
	 * 用来分拣门票数据
	 * 
	 * @param allList2
	 * @return
	 */
	private List<OrderListBean> useWayCheckData_menpiao_daishouhuo(
			List<OrderListBean> allList2) {
		List<OrderListBean> list = new ArrayList<OrderListBean>();
		if (allList != null && allList.size() != 0) {
			for (int i = 0; i < allList.size(); i++) {
				OrderListBean bean = allList.get(i);
				if (bean.getOrdersType().equals("4")) {
					if (bean.getStatus().equals("2")) {
						list.add(bean);
					}

				}
			}
		}
		return list;
	}

	/**
	 * 用来分拣商品数据
	 * 
	 * @param allList2
	 * @return
	 */
	private List<OrderListBean> useWayCheckData_shangpin(
			List<OrderListBean> allList2) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		List<OrderListBean> list = new ArrayList<OrderListBean>();
		if (allList != null && allList.size() != 0) {
			for (int i = 0; i < allList.size(); i++) {
				OrderListBean bean = allList.get(i);
				if (bean.getOrdersType().equals("1")) {
					list.add(bean);
				}
			}
		}
		return list;
	}

	/**
	 * 用来分拣出餐饮数据，将至加载到界面上
	 * 
	 * @param mList2
	 * @return
	 */
	private List<OrderListBean> useWayCheckData_canyin(
			List<OrderListBean> mList2) {
		List<OrderListBean> list = new ArrayList<OrderListBean>();
		if (allList != null && allList.size() != 0) {
			for (int i = 0; i < allList.size(); i++) {
				OrderListBean bean = allList.get(i);
				if (bean.getOrdersType().equals("0")) {
					list.add(bean);
				}
			}
		}
		return list;
	}

	/**
	 * 用来分拣出等待支付的全部订单
	 * 
	 * @param mList2
	 * @return
	 */
	private List<OrderListBean> useWayCheckData_dengdaizhifuAll(
			List<OrderListBean> mList2) {
		List<OrderListBean> list = new ArrayList<OrderListBean>();
		if (allList != null && allList.size() != 0) {
			for (int i = 0; i < allList.size(); i++) {
				OrderListBean bean = allList.get(i);
				if (bean.getStatus().equals("0")) {
					list.add(bean);
				}
			}
		}
		return list;
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

	private void initListener() {
		t_order_all.setOnClickListener(this);
		t_order_wait_pay.setOnClickListener(this);
		t_order_wait_receiver.setOnClickListener(this);
		t_order_wait_send.setOnClickListener(this);
		categoryPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				TranslateAnimation animation = new TranslateAnimation(lastPager
						* GlobalParams.WIN_WIDTH / 4, position
						* GlobalParams.WIN_WIDTH / 4, 0, 0);

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
				t_order_wait_receiver.setTextColor(getResources().getColor(
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
				case ORDER_WAIT_RECEIVER:
					t_order_wait_receiver.setTextColor(getResources().getColor(
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
		allOrderView = new AllOrderView(this);
		pagerList.add(allOrderView.getView());
		waitPayOrderView = new WaitPayOrderView(this);
		pagerList.add(waitPayOrderView.getView());
		waitSendOrderView = new WaitSendOrderView(this);
		pagerList.add(waitSendOrderView.getView());
		waitReceiverOrderView = new WaitReceiverOrderView(this);
		pagerList.add(waitReceiverOrderView.getView());

	}

	private void initview() {
		imageView_down = (ImageView) findViewById(R.id.my_order_title_down);
		imageView_down.setOnClickListener(this);
		mLayout_progressbar = (RelativeLayout) findViewById(R.id.layout_progress_layout_order_list);
		mLayout_progressbar.setOnClickListener(this);
		mLayout_progressbar.setVisibility(View.VISIBLE);
		layout_all_order_title = (RelativeLayout) findViewById(R.id.layout_all_order_title);
		mTitle = (TextView) findViewById(R.id.my_order_title_buttton);
		mBack = (ImageView) findViewById(R.id.my_order_back);
		mBack.setOnClickListener(this);
		mTitle.setOnClickListener(this);
		t_order_all = (TextView) findViewById(R.id.t_order_all);
		t_order_wait_pay = (TextView) findViewById(R.id.t_order_wait_pay);
		t_order_wait_send = (TextView) findViewById(R.id.t_order_wait_send);
		t_order_wait_receiver = (TextView) findViewById(R.id.t_order_wait_receiver);
		tabImgView = (View) findViewById(R.id.tabImgView);
		LayoutParams params = (LayoutParams) tabImgView.getLayoutParams();
		params.width = GlobalParams.WIN_WIDTH / 4;
		tabImgView.setLayoutParams(params);
		categoryPager = (MyViewPager) findViewById(R.id.pager);
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
			return 4;
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
		case ORDER_WAIT_RECEIVER:
			t_order_wait_receiver.setTextColor(getResources().getColor(
					R.color.service_select_txt));
			categoryPager.setCurrentItem(ORDER_WAIT_RECEIVER);
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
		case R.id.t_order_wait_receiver:
			IS_SHOW_VIEW = 3;
			categoryPager.setCurrentItem(ORDER_WAIT_RECEIVER);
			break;
		case R.id.my_order_title_buttton:
			// 需要弹出window来进行筛选
			ll_popul_filter.startAnimation(AnimationUtils.loadAnimation(
					AllOrderActivity.this, R.anim.activity_translate_in));
			popFilter.showAsDropDown(layout_all_order_title);
			break;
		case R.id.my_order_title_down:
			ll_popul_filter.startAnimation(AnimationUtils.loadAnimation(
					AllOrderActivity.this, R.anim.activity_translate_in));
			popFilter.showAsDropDown(layout_all_order_title);
			break;
		case R.id.my_order_back:
			finish();
			break;
		case R.id.window_layout_quanbu:
			IS_SHOW_MENU = 0;
			// 说明需要展示全部数据
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
				// 显示全部订单的全部,也就是说，这个时候是在全部订单的view里面进行切换的
				Log.i("全部", allList.size() + "");
				if (allList != null && allList.size() != 0) {
					Log.i("目前有多少数据源", allList.size() + "");
					Log.i("目前总的有多少数据源", allList.size() + "");
					allOrderView.setSecondData(allList);
					allOrderView.mLayoutNoData.setVisibility(View.GONE);
				} else {
					// 调用方法，显示暂无数据
					allOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
				}
				break;
			case 1:
				// 显示待支付订单的全部
				if (allList != null && allList.size() != 0) {
					// 调用方法，分检出待支付的全部订单，也就是说，这个时候是在待支付订单的view里面进行切换的
					List<OrderListBean> list_daizhifuall = useWayCheckData_dengdaizhifuAll(allList);
					if (list_daizhifuall != null
							&& list_daizhifuall.size() != 0) {
						// allOrderView.setFirstOrderData(list_canyin,1);
						Log.i("目前有多少数据源", list_daizhifuall.size() + "");
						Log.i("目前总的有多少数据源", allList.size() + "");
						waitPayOrderView.sencondData(list_daizhifuall);
						waitPayOrderView.mLayoutNoData.setVisibility(View.GONE);
					} else {
						// 调用方法，显示暂无数据
						waitPayOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				}
				break;
			case 2:
				// 显示待发货订单的全部，也就是说，这个时候是在代发货的view里面进行切换的
				List<OrderListBean> list_daifahuoall = useWayCheckData_dengdaifahuo(allList);
				if (list_daifahuoall != null && list_daifahuoall.size() != 0) {
					waitSendOrderView.secondData(list_daifahuoall);
					waitSendOrderView.mLayoutNoData.setVisibility(View.GONE);
				} else {
					// 调用方法，显示暂无数据
					waitSendOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
				}
				break;
			case 3:
				// 显示待收货的全部，也就是说，这个时候是在等待收货的的view里面进行切换的
				List<OrderListBean> list_daishouhuo = useWayCheckData_daishouhuo(allList);
				if (list_daishouhuo != null && list_daishouhuo.size() != 0) {
					waitReceiverOrderView.secondData(list_daishouhuo);
					waitReceiverOrderView.mLayoutNoData
							.setVisibility(View.GONE);
				} else {
					waitReceiverOrderView.mLayoutNoData
							.setVisibility(View.VISIBLE);
				}
				break;

			default:
				break;
			}
			popFilter.dismiss();
			break;
		case R.id.window_layout_canyin:
			IS_SHOW_MENU = 1;
			// 说明是餐饮
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
				// 当前是在全部订单的view界面来切换，所以展示全部餐饮的数据
				if (allList != null && allList.size() != 0) {
					// 调用方法，分拣出餐饮数据
					List<OrderListBean> list_canyin = useWayCheckData_canyin(allList);
					if (list_canyin != null && list_canyin.size() != 0) {
						// allOrderView.setFirstOrderData(list_canyin,1);
						Log.i("目前有多少数据源", list_canyin.size() + "");
						Log.i("目前总的有多少数据源", allList.size() + "");
						allOrderView.setSecondData(list_canyin);
						allOrderView.mLayoutNoData.setVisibility(View.GONE);
					} else {
						// 调用方法，显示暂无数据
						allOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
					}
				} else {
					allOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
				}
				break;
			case 1:
				// 当前是在代支付订单的view界面来切换，所以这里展示代支付的餐饮数据
				if (allList != null) {
					List<OrderListBean> list_daizhifu_canyin = useWayCheckData_can_daizhifu(allList);
					if (list_daizhifu_canyin != null
							&& list_daizhifu_canyin.size() != 0) {
						waitPayOrderView.sencondData(list_daizhifu_canyin);
						waitPayOrderView.mLayoutNoData.setVisibility(View.GONE);
					} else {
						waitPayOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}

				} else {
					waitPayOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
				}
				break;
			case 2:
				// 说明当前是在待发货的view里面来点击了餐饮，所以在代发货界面只展示代发货的餐饮数据
				if (allList != null) {
					List<OrderListBean> list_daifahuo_canyin = useWayCheckData_can_daifahuo(allList);
					if (list_daifahuo_canyin != null
							&& list_daifahuo_canyin.size() != 0) {
						waitSendOrderView.secondData(list_daifahuo_canyin);
						waitSendOrderView.mLayoutNoData
								.setVisibility(View.GONE);
					} else {
						waitSendOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}

				} else {
					waitSendOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
				}
				break;
			case 3:
				// 说明是 在待收货的view里面来点击了餐饮，所以在待收货的界面view中只展示待收货的餐饮数据
				if (allList != null) {
					List<OrderListBean> list_daishouhuo_canyin = useWayCheckData_can_daishouhuo(allList);
					if (list_daishouhuo_canyin != null
							&& list_daishouhuo_canyin.size() != 0) {
						waitReceiverOrderView
								.secondData(list_daishouhuo_canyin);
						waitReceiverOrderView.mLayoutNoData
								.setVisibility(View.GONE);
					} else {
						waitReceiverOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}

				} else {
					waitReceiverOrderView.mLayoutNoData
							.setVisibility(View.VISIBLE);
				}
				break;

			default:
				break;
			}
			popFilter.dismiss();
			break;
		case R.id.window_layout_shangpin:
			IS_SHOW_MENU = 2;
			// 说明是商品
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
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_shangpin = useWayCheckData_shangpin(allList);
					Log.i("目前有多少数据源", list_shangpin.size() + "");
					Log.i("目前总的有多少数据源", allList.size() + "");
					if (list_shangpin != null && list_shangpin.size() != 0) {

						// allOrderView.setFirstOrderData(list_shangpin,2);
						allOrderView.setSecondData(list_shangpin);
						allOrderView.mLayoutNoData.setVisibility(View.GONE);
					} else {
						// 调用方法，显示暂无数据
						allOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
					}
				}
				break;
			case 1:
				// 说明是代支付view界面点了商品
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_daifukuan_shangpin = useWayCheckData_daifukuan_goods(allList);
					if (list_daifukuan_shangpin != null
							&& list_daifukuan_shangpin.size() != 0) {
						waitPayOrderView.mLayoutNoData.setVisibility(View.GONE);
						waitPayOrderView.sencondData(list_daifukuan_shangpin);
					} else {
						waitPayOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				} else {
					waitPayOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
				}

				break;
			case 2:
				// 说明是待发货界面点了商品
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_daifahuo_shangpin = useWayCheckData_daifahuo_goods(allList);
					if (list_daifahuo_shangpin != null
							&& list_daifahuo_shangpin.size() != 0) {
						waitSendOrderView.mLayoutNoData
								.setVisibility(View.GONE);
						waitSendOrderView.secondData(list_daifahuo_shangpin);
					} else {
						waitSendOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				} else {
					waitSendOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
				}
				break;
			case 3:
				// 说明是在待收货界面点了商品
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_daishouhuo_shangpin = useWayCheckData_daishouhuo_goods(allList);
					if (list_daishouhuo_shangpin != null
							&& list_daishouhuo_shangpin.size() != 0) {
						waitReceiverOrderView.mLayoutNoData
								.setVisibility(View.GONE);
						waitReceiverOrderView
								.secondData(list_daishouhuo_shangpin);
					} else {
						waitReceiverOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				} else {
					waitReceiverOrderView.mLayoutNoData
							.setVisibility(View.VISIBLE);
				}
				break;

			default:
				break;
			}

			popFilter.dismiss();
			break;
		case R.id.window_layout_menpiao:
			IS_SHOW_MENU = 3;
			// 说明是门票
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
			switch (IS_SHOW_VIEW) {
			case 0:
				if (allList != null && allList.size() != 0) {
					// 调用方法，分拣出门票数据
					List<OrderListBean> list_menpiao = useWayCheckData_menpiao(allList);
					Log.i("目前有多少数据源", list_menpiao.size() + "");
					Log.i("目前总的有多少数据源", allList.size() + "");
					if (list_menpiao != null && list_menpiao.size() != 0) {
						allOrderView.setSecondData(list_menpiao);
						allOrderView.mLayoutNoData.setVisibility(View.GONE);
					} else {
						// 调用方法，显示暂无数据
						allOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
					}
				}
				break;
			case 1:
				if (allList != null && allList.size() != 0) {
					// 调用方法，分拣出餐饮数据
					List<OrderListBean> list_menpiao = useWayCheckData_menpiao_daizhifu(allList);
					Log.i("目前有多少数据源", list_menpiao.size() + "");
					Log.i("目前总的有多少数据源", allList.size() + "");
					if (list_menpiao != null && list_menpiao.size() != 0) {
						waitPayOrderView.sencondData(list_menpiao);
						waitPayOrderView.mLayoutNoData.setVisibility(View.GONE);
					} else {
						// 调用方法，显示暂无数据
						waitPayOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				}
				break;
			case 2:
				if (allList != null && allList.size() != 0) {
					// 调用方法，分拣出餐饮数据
					List<OrderListBean> list_menpiao = useWayCheckData_menpiao_daifahuo(allList);
					Log.i("目前有多少数据源", list_menpiao.size() + "");
					Log.i("目前总的有多少数据源", allList.size() + "");
					if (list_menpiao != null && list_menpiao.size() != 0) {
						waitSendOrderView.secondData(list_menpiao);
						waitSendOrderView.mLayoutNoData
								.setVisibility(View.GONE);
					} else {
						// 调用方法，显示暂无数据
						waitSendOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				}
				break;
			case 3:
				if (allList != null && allList.size() != 0) {
					// 调用方法，分拣出餐饮数据
					List<OrderListBean> list_menpiao = useWayCheckData_menpiao_daishouhuo(allList);
					Log.i("目前有多少数据源", list_menpiao.size() + "");
					Log.i("目前总的有多少数据源", allList.size() + "");
					if (list_menpiao != null && list_menpiao.size() != 0) {
						waitReceiverOrderView.secondData(list_menpiao);
						waitReceiverOrderView.mLayoutNoData
								.setVisibility(View.GONE);
					} else {
						// 调用方法，显示暂无数据
						waitReceiverOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				}
				break;

			default:
				break;
			}
			popFilter.dismiss();
			break;
		case R.id.window_layout_yongche:
			IS_SHOW_MENU = 4;
			// 说明是用车
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
			Log.i("用车", allList.size() + "");
			switch (IS_SHOW_VIEW) {
			case 0:
				if (allList != null && allList.size() != 0) {
					// 调用方法，分拣出餐饮数据
					List<OrderListBean> list_yongche = useWayCheckData_yongche(allList);
					Log.i("目前有多少数据源", list_yongche.size() + "");
					Log.i("目前总的有多少数据源", allList.size() + "");
					if (list_yongche != null && list_yongche.size() != 0) {
						// allOrderView.setFirstOrderData(list_yongche,4);
						allOrderView.setSecondData(list_yongche);
						allOrderView.mLayoutNoData.setVisibility(View.GONE);
					} else {
						// 调用方法，显示暂无数据
						allOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
					}
				}
				break;
			case 1:
				// 待支付view中点了用车
				if (allList != null && allList.size() != 0) {
					// 调用方法，分拣出餐饮数据
					List<OrderListBean> list_yongche = useWayCheckData_yongche_daizhifu(allList);
					if (list_yongche != null && list_yongche.size() != 0) {
						// allOrderView.setFirstOrderData(list_yongche,4);
						waitPayOrderView.sencondData(list_yongche);
						waitPayOrderView.mLayoutNoData.setVisibility(View.GONE);
					} else {
						// 调用方法，显示暂无数据
						waitPayOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				}
				break;
			case 2:
				if (allList != null && allList.size() != 0) {
					// 调用方法，分拣出餐饮数据
					List<OrderListBean> list_yongche = useWayCheckData_yongche_daifahuo(allList);
					if (list_yongche != null && list_yongche.size() != 0) {
						// allOrderView.setFirstOrderData(list_yongche,4);
						waitSendOrderView.secondData(list_yongche);
						waitSendOrderView.mLayoutNoData
								.setVisibility(View.GONE);
					} else {
						// 调用方法，显示暂无数据
						waitSendOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				}
				break;
			case 3:
				if (allList != null && allList.size() != 0) {
					// 调用方法，分拣出餐饮数据
					List<OrderListBean> list_yongche = useWayCheckData_yongche_daishouhuo(allList);
					if (list_yongche != null && list_yongche.size() != 0) {
						// allOrderView.setFirstOrderData(list_yongche,4);
						waitReceiverOrderView.secondData(list_yongche);
						waitReceiverOrderView.mLayoutNoData
								.setVisibility(View.GONE);
					} else {
						// 调用方法，显示暂无数据
						waitReceiverOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				}
				break;

			default:
				break;
			}

			popFilter.dismiss();
			break;
		case R.id.window_layout_zhongchou:
			IS_SHOW_MENU = 5;
			// 说明是众筹
			mQUANBU.setTextColor(Color.WHITE);
			mCANYIN.setTextColor(Color.WHITE);
			mSHANGPN.setTextColor(Color.WHITE);
			mzhongchou.setTextColor(Color.rgb(22, 154, 218));
			mYONGCHE.setTextColor(Color.WHITE);
			mgoupiao.setTextColor(Color.WHITE);
			im_quanbu.setVisibility(View.GONE);
			im_canyin.setVisibility(View.GONE);
			im_shangpin.setVisibility(View.GONE);
			im_menpiao.setVisibility(View.GONE);
			im_yongche.setVisibility(View.GONE);
			im_zhongchou.setVisibility(View.VISIBLE);
			switch (IS_SHOW_VIEW) {
			case 0:
				// 说明是在全部view界面中点击了众筹
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_zhongchou = useWayCheckData_zhongchou(allList);
					Log.i("目前有多少数据源", list_zhongchou.size() + "");
					Log.i("目前总的有多少数据源", allList.size() + "");
					if (list_zhongchou != null && list_zhongchou.size() != 0) {
						// allOrderView.setFirstOrderData(list_zhongchou,5);
						allOrderView.setSecondData(list_zhongchou);
						allOrderView.mLayoutNoData.setVisibility(View.GONE);
					} else {
						// 调用方法，显示暂无数据
						allOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
					}
				} else {
					allOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
				}
				break;
			case 1:
				// 说明是在代支付view界面中点击了众筹
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_zhongchou_daizhifu = useWayCheckData_zhongchou_daizhifu(allList);
					if (list_zhongchou_daizhifu != null
							&& list_zhongchou_daizhifu.size() != 0) {
						waitPayOrderView.mLayoutNoData.setVisibility(View.GONE);
						waitPayOrderView.sencondData(list_zhongchou_daizhifu);
					} else {
						waitPayOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				} else {
					waitPayOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
				}

				break;
			case 2:
				// 说明是在待发货view界面中点击了众筹
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_zhongchou_daifahuo = useWayCheckData_zhongchou_daifahuo(allList);
					if (list_zhongchou_daifahuo != null
							&& list_zhongchou_daifahuo.size() != 0) {
						waitSendOrderView.mLayoutNoData
								.setVisibility(View.GONE);
						waitSendOrderView.secondData(list_zhongchou_daifahuo);
					} else {
						waitSendOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				} else {
					waitSendOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
				}
				break;
			case 3:
				// 说明是在待收货view界面中点击了众筹
				if (allList != null && allList.size() != 0) {
					List<OrderListBean> list_zhongchou_daishouhuo = useWayCheckData_zhongchou_daishouhuo(allList);
					if (list_zhongchou_daishouhuo != null
							&& list_zhongchou_daishouhuo.size() != 0) {
						waitReceiverOrderView.mLayoutNoData
								.setVisibility(View.GONE);
						waitReceiverOrderView
								.secondData(list_zhongchou_daishouhuo);
					} else {
						waitReceiverOrderView.mLayoutNoData
								.setVisibility(View.VISIBLE);
					}
				} else {
					waitReceiverOrderView.mLayoutNoData
							.setVisibility(View.VISIBLE);
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
	 * 解析获取下来的真正的支付参数
	 * 
	 * @param jsondatass
	 */
	private PayBean useWayJsonDataPay(String jsondatass) {
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
			e.printStackTrace();
		}
		return bean;
	}

	/**
	 * 分拣出待收货的众筹数据
	 * 
	 * @param allList2
	 * @return
	 */
	private List<OrderListBean> useWayCheckData_zhongchou_daishouhuo(
			List<OrderListBean> allList2) {
		List<OrderListBean> list = new ArrayList<OrderListBean>();
		if (allList2 != null) {
			for (int i = 0; i < allList2.size(); i++) {
				if (allList2.get(i).getOrdersType().equals("3")
						&& allList2.get(i).getStatus().equals("2")) {
					list.add(allList2.get(i));
				}
			}
		}
		return list;
	}

	/**
	 * 分拣出待发货的众筹数据
	 * 
	 * @param allList2
	 * @return
	 */
	private List<OrderListBean> useWayCheckData_zhongchou_daifahuo(
			List<OrderListBean> allList2) {
		List<OrderListBean> list = new ArrayList<OrderListBean>();
		if (allList2 != null) {
			for (int i = 0; i < allList2.size(); i++) {
				if (allList2.get(i).getOrdersType().equals("3")
						&& allList2.get(i).getStatus().equals("1")) {
					list.add(allList2.get(i));
				}
			}
		}
		return list;
	}

	/**
	 * 分拣出待支付的众筹
	 * 
	 * @param allList2
	 * @return
	 */
	private List<OrderListBean> useWayCheckData_zhongchou_daizhifu(
			List<OrderListBean> allList2) {
		List<OrderListBean> list = new ArrayList<OrderListBean>();
		if (allList2 != null) {
			for (int i = 0; i < allList2.size(); i++) {
				if (allList2.get(i).getOrdersType().equals("3")
						&& allList2.get(i).getStatus().equals("0")) {
					list.add(allList2.get(i));
				}
			}
		}
		return list;
	}

	/**
	 * 分拣出待收货的商品数据
	 * 
	 * @param allList2
	 * @return
	 */
	private List<OrderListBean> useWayCheckData_daishouhuo_goods(
			List<OrderListBean> allList2) {
		List<OrderListBean> list = new ArrayList<OrderListBean>();
		if (allList2 != null) {
			for (int i = 0; i < allList2.size(); i++) {
				if (allList2.get(i).getOrdersType().equals("1")
						&& allList2.get(i).getStatus().equals("2")) {
					list.add(allList2.get(i));
				}
			}
		}
		return list;
	}

	/**
	 * 粉检出代发货的商品数据
	 * 
	 * @param allList2
	 * @return
	 */
	private List<OrderListBean> useWayCheckData_daifahuo_goods(
			List<OrderListBean> allList2) {
		List<OrderListBean> list = new ArrayList<OrderListBean>();
		if (allList2 != null) {
			for (int i = 0; i < allList2.size(); i++) {
				if (allList2.get(i).getOrdersType().equals("1")
						&& allList2.get(i).getStatus().equals("1")) {
					list.add(allList2.get(i));
				}
			}
		}
		return list;
	}

	/**
	 * 分拣出待付款的商品数据
	 * 
	 * @param allList2
	 * @return
	 */
	private List<OrderListBean> useWayCheckData_daifukuan_goods(
			List<OrderListBean> allList2) {
		List<OrderListBean> list = new ArrayList<OrderListBean>();
		if (allList2 != null) {
			for (int i = 0; i < allList2.size(); i++) {
				if (allList2.get(i).getOrdersType().equals("1")
						&& allList2.get(i).getStatus().equals("0")) {
					list.add(allList2.get(i));
				}
			}
		}
		return list;
	}

	/**
	 * 分拣出收货的餐饮的数据
	 * 
	 * @param allList2
	 * @return
	 */
	private List<OrderListBean> useWayCheckData_can_daishouhuo(
			List<OrderListBean> allList2) {
		List<OrderListBean> list = new ArrayList<OrderListBean>();
		for (int i = 0; i < allList2.size(); i++) {
			if (allList2.get(i).getStatus().equals("2")
					&& allList2.get(i).getOrdersType().equals("0")) {
				list.add(allList2.get(i));
			}
		}
		return list;
	}

	/**
	 * 分拣出发货的餐饮的数据
	 * 
	 * @param allList2
	 * @return
	 */
	private List<OrderListBean> useWayCheckData_can_daifahuo(
			List<OrderListBean> allList2) {
		List<OrderListBean> list = new ArrayList<OrderListBean>();
		for (int i = 0; i < allList2.size(); i++) {
			if (allList2.get(i).getStatus().equals("1")
					&& allList2.get(i).getOrdersType().equals("0")) {
				list.add(allList2.get(i));
			}
		}
		return list;
	}

	/**
	 * 分拣出发货的餐饮的数据
	 * 
	 * @param allList2
	 * @return
	 */
	private List<OrderListBean> useWayCheckData_daifahuo(
			List<OrderListBean> allList2) {
		List<OrderListBean> list = new ArrayList<OrderListBean>();
		for (int i = 0; i < allList2.size(); i++) {
			if (allList2.get(i).getStatus().equals("1")) {
				list.add(allList2.get(i));
			}
			if (allList2.get(i).getOrdersType().equals("2")
					&& allList2.get(i).getStatus().equals("22")) {
				list.add(allList2.get(i));
			}
		}
		return list;
	}

	/**
	 * 分拣出代支付的餐饮的数据
	 * 
	 * @param allList2
	 * @return
	 */
	private List<OrderListBean> useWayCheckData_can_daizhifu(
			List<OrderListBean> allList2) {
		List<OrderListBean> list = new ArrayList<OrderListBean>();
		for (int i = 0; i < allList2.size(); i++) {
			if (allList2.get(i).getStatus().equals("0")
					&& allList2.get(i).getOrdersType().equals("0")) {
				list.add(allList2.get(i));
			}
		}

		return list;
	}

	/**
	 * 用于分拣出等待收货的全部订单信息
	 * 
	 * @param allList2
	 * @return
	 */
	private List<OrderListBean> useWayCheckData_daishouhuo(
			List<OrderListBean> allList2) {
		List<OrderListBean> list_dengdaishouhuo = new ArrayList<OrderListBean>();
		if (allList2 != null) {
			for (int i = 0; i < allList2.size(); i++) {
				if (allList2.get(i).getStatus().equals("2")) {
					list_dengdaishouhuo.add(allList2.get(i));
				}
			}
		}
		return list_dengdaishouhuo;
	}

	/**
	 * 该方法用来分拣出代发货的全部订单数据
	 * 
	 * @param allList2
	 * @return
	 */
	private List<OrderListBean> useWayCheckData_dengdaifahuo(
			List<OrderListBean> allList2) {
		List<OrderListBean> list_daifahuo = new ArrayList<OrderListBean>();
		if (allList2 != null && allList2.size() != 0) {
			for (int i = 0; i < allList2.size(); i++) {
				if (allList2.get(i).getStatus().equals("1")) {
					list_daifahuo.add(allList2.get(i));
				}
			}
		}
		return list_daifahuo;
	}

	/**
	 * 用来解析订单数据，并将其返回
	 * 
	 * @param backdata
	 *            //
	 */
	private List<OrderListBean> useWayJsonData(String backdata) {
		try {
			JSONObject obj = new JSONObject(backdata);
			if (obj.has("data")) {
				JSONArray array = obj.getJSONArray("data");
				int count = array.length();
				for (int i = 0; i < count; i++) {
					OrderListBean bean = new OrderListBean();
					JSONObject objs = array.getJSONObject(i);
					if (objs.has("amount")) {
						bean.setAmount(objs.getString("amount"));
					}
					if (objs.has("companies")) {
						bean.setCompanies(objs.getString("companies"));
					}
					if (objs.has("courier")) {
						bean.setCourier(objs.getString("courier"));
					}
					if(objs.has("delivery")){
						JSONObject obj_address=objs.getJSONObject("delivery");
						if(obj_address.has("address")){
							bean.setAddress(obj_address.getString("address"));
						}
						if(obj_address.has("mobilePhone")){
							bean.setMobilePhone(obj_address.getString("mobilePhone"));
						}
						if(obj_address.has("cityAddress")){
							bean.setCityAddress(obj_address.getString("cityAddress"));
						}
						if(obj_address.has("name")){
							bean.setName(obj_address.getString("name"));
						}
					}
					if (objs.has("createBy")) {
						bean.setCreateBy(objs.getString("createBy"));
					}
					if (objs.has("createTime")) {
						bean.setCreateTime(objs.getString("createTime"));
					}
					if(objs.has("userName")){
						bean.setUserName(objs.getString("userName"));
					}
					if(objs.has("seller")){
						JSONObject obj_seller=objs.getJSONObject("seller");
						if(obj_seller.has("companyname")){
							bean.setCompanyname(obj_seller.getString("companyname"));
						}
					}
					if (objs.has("freight")) {
						bean.setFreight(objs.getString("freight"));
					}
					if (objs.has("id")) {
						bean.setId(objs.getString("id"));
					}
					if (objs.has("orderAmount")) {
						bean.setOrderAmount(objs.getString("orderAmount"));
					}
					if (objs.has("orderNumber")) {
						bean.setOrderNumber(objs.getString("orderNumber"));
					}
					if (objs.has("ordersType")) {
						bean.setOrdersType(objs.getString("ordersType"));
					}
					if (objs.has("payMent")) {
						bean.setPayMent(objs.getString("payMent"));
					}
					if (objs.has("position")) {
						bean.setPosition(objs.getString("position"));
					}
					if (objs.has("payStatus")) {
						bean.setPayStatus(objs.getString("payStatus"));
					}
					if (objs.has("remark")) {
						bean.setRemark(objs.getString("remark"));
					}
					if (objs.has("status")) {
						bean.setStatus(objs.getString("status"));
					}
					if (objs.has("telePhone")) {
						bean.setTelePhone(objs.getString("telePhone"));
					}
					if (objs.has("payTime")) {
						bean.setPayTime(objs.getString("payTime"));
					}
					if (objs.has("privilege")) {
						bean.setPrivilege(objs.getString("privilege"));
					}
					String type = null;
					if (objs.has("ordersType")) {
						type = objs.getString("ordersType");
					}
					if (type != null && !TextUtils.isEmpty(type)) {
						// 开始判断类型，因为解析数据类型不同
						if (type.equals("0")) {
							// 说明是商品
							if (objs.has("orderDetailList")) {
								JSONArray array_goods = objs
										.getJSONArray("orderDetailList");
								int count_goods = array_goods.length();
								List<OrderListBeanGoodsBean> list_goods = new ArrayList<OrderListBeanGoodsBean>();
								for (int l = 0; l < count_goods; l++) {
									JSONObject obj_goodss = array_goods
											.getJSONObject(l);
									JSONObject obj_goods = obj_goodss
											.getJSONObject("goods");
									OrderListBeanGoodsBean bean_goods = new OrderListBeanGoodsBean();
									if (obj_goods.has("bigimg")) {
										bean_goods.setBigImg(obj_goods
												.getString("bigimg"));
									}
									if (obj_goods.has("category")) {
										JSONObject goods_obj = obj_goods
												.getJSONObject("category");
										if (goods_obj.has("")) {
											bean_goods.setClassicName(goods_obj
													.getString("classicName"));
										}
									}
									if (obj_goods.has("classicId")) {
										bean_goods.setClassicId(obj_goods
												.getString("classicId"));
									}
									if (obj_goods.has("classicName")) {
										bean_goods.setClassicName(obj_goods
												.getString("classicName"));
									}
									if (obj_goods.has("createTime")) {
										bean_goods.setCreateTime(obj_goods
												.getString("createTime"));
									}
									if (obj_goods.has("id")) {
										bean_goods.setId(obj_goods
												.getString("id"));
									}
									if (obj_goods.has("goodsName")) {
										bean_goods.setGoodsName(obj_goods
												.getString("goodsName"));
									}
									if(obj_goodss.has("pecificationName")){
										bean_goods.setPecificationName(obj_goodss.
												getString("pecificationName"));
									}
									if (obj_goods.has("label")) {
										bean_goods.setLabel(obj_goods
												.getString("label"));
									}
									if (obj_goods.has("mediumImg")) {
										bean_goods.setMediumImg(obj_goods
												.getString("mediumImg"));
									}
									if (obj_goodss.has("price")) {
										bean_goods.setPrice(obj_goodss
												.getString("price"));
									}
									if (obj_goods.has("seller")) {
										bean_goods.setSeller(obj_goods
												.getString("seller"));
									}
									if (obj_goods.has("smallImg")) {
										bean_goods.setSmallImg(obj_goods
												.getString("smallImg"));
									}
									if (obj_goods.has("status")) {
										bean_goods.setStatus(obj_goods
												.getString("status"));
									}
									if (obj_goods.has("stock")) {
										bean_goods.setStock(obj_goods
												.getString("stock"));
									}
									if (obj_goodss.has("wareNumber")) {
										bean_goods.setWareNumber(obj_goodss
												.getString("wareNumber"));
									}
									if (obj_goods.has("referprice")) {
										bean_goods.setReferprice(obj_goods
												.getString("referprice"));
									}
									if (obj_goodss.has("id")) {
										bean_goods.setId(obj_goodss
												.getString("id"));
									}
									if (obj_goodss.has("goodsId")) {
										bean_goods.setGoodsId(obj_goodss
												.getString("goodsId"));
									}
									if(obj_goods.has("pickup_address")){
										bean_goods.setPickup_address(obj_goods.getString("pickup_address"));
									}
									if(obj_goods.has("pickup_remark")){
										bean_goods.setPickup_remark(obj_goods.getString("pickup_remark"));
									}
									list_goods.add(bean_goods);
								}
								bean.setList(list_goods);
							}
						} else if (type.equals("1")) {
							// 说明是餐饮
							if (objs.has("orderDetailList")) {
								JSONArray array_goods = objs
										.getJSONArray("orderDetailList");
								int count_goods = array_goods.length();
								List<OrderListBeanGoodsBean> list_goods = new ArrayList<OrderListBeanGoodsBean>();
								for (int l = 0; l < count_goods; l++) {
									JSONObject obj_goodss = array_goods
											.getJSONObject(l);
									JSONObject obj_goods = obj_goodss
											.getJSONObject("goods");
									OrderListBeanGoodsBean bean_goods = new OrderListBeanGoodsBean();
									if (obj_goods.has("bigimg")) {
										bean_goods.setBigImg(obj_goods
												.getString("bigimg"));
									}
									if (obj_goods.has("category")) {
										JSONObject goods_obj = obj_goods
												.getJSONObject("category");
										if (goods_obj.has("")) {
											bean_goods.setClassicName(goods_obj
													.getString("classicName"));
										}
									}
									if (obj_goods.has("classicId")) {
										bean_goods.setClassicId(obj_goods
												.getString("classicId"));
									}
									if (obj_goods.has("classicName")) {
										bean_goods.setClassicName(obj_goods
												.getString("classicName"));
									}
									if (obj_goods.has("createTime")) {
										bean_goods.setCreateTime(obj_goods
												.getString("createTime"));
									}
									if (obj_goods.has("id")) {
										bean_goods.setId(obj_goods
												.getString("id"));
									}
									if (obj_goods.has("goodsName")) {
										bean_goods.setGoodsName(obj_goods
												.getString("goodsName"));
									}
									if(obj_goodss.has("pecificationName")){
										bean_goods.setPecificationName(obj_goodss.
												getString("pecificationName"));
									}
									if (obj_goods.has("label")) {
										bean_goods.setLabel(obj_goods
												.getString("label"));
									}
									if (obj_goods.has("mediumImg")) {
										bean_goods.setMediumImg(obj_goods
												.getString("mediumImg"));
									}
									if (obj_goodss.has("price")) {
										bean_goods.setPrice(obj_goodss
												.getString("price"));
									}
									if (obj_goods.has("seller")) {
										bean_goods.setSeller(obj_goods
												.getString("seller"));
									}
									if (obj_goods.has("smallImg")) {
										bean_goods.setSmallImg(obj_goods
												.getString("smallImg"));
									}
									if (obj_goods.has("status")) {
										bean_goods.setStatus(obj_goods
												.getString("status"));
									}
									if (obj_goods.has("stock")) {
										bean_goods.setStock(obj_goods
												.getString("stock"));
									}
									if (obj_goodss.has("wareNumber")) {
										bean_goods.setWareNumber(obj_goodss
												.getString("wareNumber"));
									}
									if (obj_goods.has("referprice")) {
										bean_goods.setReferprice(obj_goods
												.getString("referprice"));
									}
									if (obj_goodss.has("id")) {
										bean_goods.setId(obj_goodss
												.getString("id"));
									}
									if (obj_goodss.has("goodsId")) {
										bean_goods.setGoodsId(obj_goodss
												.getString("goodsId"));
									}
									list_goods.add(bean_goods);
								}
								bean.setList(list_goods);
							}
						} else if (type.equals("3")) {
							// 说明是众筹
							// 开始解析众筹
							if (objs.has("orderDetailList")) {
								List<OrderListBeanZhongchouBean> list_zhongchou = new ArrayList<OrderListBeanZhongchouBean>();
								JSONArray array_zhongchou = objs
										.getJSONArray("orderDetailList");
								int count_zhongchou = array_zhongchou.length();
								for (int h = 0; h < count_zhongchou; h++) {
									OrderListBeanZhongchouBean bean_zhongchou = new OrderListBeanZhongchouBean();
									JSONObject obj_zhongchou = array_zhongchou
											.getJSONObject(h);// 最外层的jsonobj
									if (obj_zhongchou.has("goods")) {
										JSONObject obj_zhongchou_goods = obj_zhongchou
												.getJSONObject("goods");// 内层商品的obj
										if (obj_zhongchou_goods
												.has("crowdfund")) {
											JSONObject obj_zhongchou_zuineibu = obj_zhongchou_goods
													.getJSONObject("crowdfund");
											if (obj_zhongchou_zuineibu
													.has("beginTime")) {
												bean_zhongchou
														.setBeginTime(obj_zhongchou_zuineibu
																.getString("beginTime"));
											}
											if (obj_zhongchou_zuineibu
													.has("crowdfundName")) {
												bean_zhongchou
														.setCrowdfundName(obj_zhongchou_zuineibu
																.getString("crowdfundName"));
											}
											if (obj_zhongchou_zuineibu
													.has("crowdfundStatus")) {
												bean_zhongchou
														.setCrowdfundStatus(obj_zhongchou_zuineibu
																.getString("crowdfundStatus"));
											}
											if (obj_zhongchou_zuineibu
													.has("endTime")) {
												bean_zhongchou
														.setEndTime(obj_zhongchou_zuineibu
																.getString("endTime"));
											}
											if (obj_zhongchou_zuineibu
													.has("evaluateCount")) {
												bean_zhongchou
														.setEvaluateCount(obj_zhongchou_zuineibu
																.getString("evaluateCount"));
											}
											if (obj_zhongchou_zuineibu
													.has("hasMoney")) {
												bean_zhongchou
														.setHasMoney(obj_zhongchou_zuineibu
																.getString("hasMoney"));
											}
											if (obj_zhongchou_zuineibu
													.has("id")) {
												bean_zhongchou
														.setId(obj_zhongchou_zuineibu
																.getString("id"));
											}
											if (obj_zhongchou_zuineibu
													.has("participation")) {
												bean_zhongchou
														.setParticipation(obj_zhongchou_zuineibu
																.getString("participation"));
											}
											if (obj_zhongchou_zuineibu
													.has("projectAddress")) {
												bean_zhongchou
														.setProjectAddress(obj_zhongchou_zuineibu
																.getString("projectAddress"));
											}
											if (obj_zhongchou_zuineibu
													.has("projectInfo")) {
												bean_zhongchou
														.setProjectInfo(obj_zhongchou_zuineibu
																.getString("projectInfo"));
											}
											if (obj_zhongchou_zuineibu
													.has("projectIntroduction")) {
												bean_zhongchou
														.setProjectIntroduction(obj_zhongchou_zuineibu
																.getString("projectIntroduction"));
											}
											if (obj_zhongchou_zuineibu
													.has("projectTime")) {
												bean_zhongchou
														.setProjectTime(obj_zhongchou_zuineibu
																.getString("projectTime"));
											}
											if (obj_zhongchou_zuineibu
													.has("propagatePicture")) {
												bean_zhongchou
														.setPropagatePicture(obj_zhongchou_zuineibu
																.getString("propagatePicture"));
											}
											if (obj_zhongchou_zuineibu
													.has("remark")) {
												bean_zhongchou
														.setRemark(obj_zhongchou_zuineibu
																.getString("remark"));
											}
											if (obj_zhongchou_zuineibu
													.has("returnCount")) {
												bean_zhongchou
														.setReturnCount(obj_zhongchou_zuineibu
																.getString("returnCount"));
											}
											if (obj_zhongchou_zuineibu
													.has("targetMoney")) {
												bean_zhongchou
														.setTargetMoney(obj_zhongchou_zuineibu
																.getString("targetMoney"));
											}
										}
										if (obj_zhongchou_goods.has("id")) {
											bean_zhongchou
													.setGoodssssid(obj_zhongchou_goods
															.getString("id"));
										}
										if (obj_zhongchou_goods
												.has("returnContent")) {
											bean_zhongchou
													.setReturnContent(obj_zhongchou_goods
															.getString("returnContent"));
										}
										if (obj_zhongchou_goods
												.has("returnLimit")) {
											bean_zhongchou
													.setReturnLimit(obj_zhongchou_goods
															.getString("returnLimit"));
										}
										if (obj_zhongchou_goods
												.has("returnName")) {
											bean_zhongchou
													.setReturnName(obj_zhongchou_goods
															.getString("returnName"));
										}
										if (obj_zhongchou_goods
												.has("returnPrice")) {
											bean_zhongchou
													.setReturnPrice(obj_zhongchou_goods
															.getString("returnPrice"));
										}
										if (obj_zhongchou_goods
												.has("totalCount")) {
											bean_zhongchou
													.setTotalCount(obj_zhongchou_goods
															.getString("totalCount"));
										}

									}

									if (obj_zhongchou.has("goodsId")) {
										bean_zhongchou.setGoodsId(obj_zhongchou
												.getString("goodsId"));
									}
									if (obj_zhongchou.has("id")) {
										bean_zhongchou.setBeanid(obj_zhongchou
												.getString("id"));
									}
									if (obj_zhongchou.has("price")) {
										bean_zhongchou.setPrice(obj_zhongchou
												.getString("price"));
									}
									if (obj_zhongchou.has("wareNumber")) {
										bean_zhongchou
												.setWareNumber(obj_zhongchou
														.getString("wareNumber"));
									}
									list_zhongchou.add(bean_zhongchou);
								}
								bean.setList_zhong(list_zhongchou);
							}
						} else if (type.equals("2")) {
							/**
							 * 说明是用车的数据
							 */
							if (objs.has("orderDetailList")) {
								JSONArray arraycar = objs
										.getJSONArray("orderDetailList");
								List<OrderListBeanCar> listcar = new ArrayList<OrderListBeanCar>();
								for (int g = 0; g < arraycar.length(); g++) {
									JSONObject obj_c = arraycar
											.getJSONObject(g);
									OrderListBeanCar beansss = new OrderListBeanCar();
									if (obj_c.has("goods")) {
										JSONObject objcar = obj_c
												.getJSONObject("goods");
										if (objcar.has("car_Brand")) {
											beansss.setCar_car_Brand(objcar
													.getString("car_Brand"));
										}
										if (objcar.has("city")) {
											beansss.setCar_car_city(objcar
													.getString("city"));
										}
										if (objcar.has("car_Type_Id")) {
											beansss.setCar_car_Type_Id(objcar
													.getString("car_Type_Id"));
										}
										if (objcar.has("create_Time")) {
											beansss.setCar_create_Time(objcar
													.getString("create_Time"));
										}
										if (objcar.has("deleteFlag")) {
											beansss.setCar_deleteFlag(objcar
													.getString("deleteFlag"));
										}
										if (objcar.has("driver_Id")) {
											beansss.setCar_driver_Id(objcar
													.getString("driver_Id"));
										}
										if (objcar.has("driver_Name")) {
											beansss.setCar_driver_Name(objcar
													.getString("driver_Name"));
										}
										if (objcar.has("driver_Phone")) {
											beansss.setCar_driver_Phone(objcar
													.getString("driver_Phone"));
										}
										if (objcar.has("end_Address")) {
											beansss.setCar_end_Address(objcar
													.getString("end_Address"));
										}
										if (objcar.has("end_Latitude")) {
											beansss.setCar_end_Latitude(objcar
													.getString("end_Latitude"));
										}
										if (objcar.has("end_Lognitude")) {
											beansss.setCar_end_Lognitude(objcar
													.getString("end_Lognitude"));
										}
										if (objcar.has("end_Position")) {
											beansss.setCar_end_Position(objcar
													.getString("end_Position"));
										}
										if (objcar.has("expect_Start_Time")) {
											beansss.setCar_expect_Start_Time(objcar
													.getString("expect_Start_Time"));
										}
										if (objcar.has("flight_Number")) {
											beansss.setCar_flight_Number(objcar
													.getString("flight_Number"));
										}
										if (objcar.has("id")) {
											beansss.setCar_id(objcar
													.getString("id"));
										}
										if (objcar.has("insure_Company")) {
											beansss.setCar_insure_Company(objcar
													.getString("insure_Company"));
										}
										if (objcar.has("insure_Number")) {
											beansss.setCar_insure_Number(objcar
													.getString("insure_Number"));
										}
										if (objcar.has("msg")) {
											beansss.setCar_msg(objcar
													.getString("msg"));
										}
										if (objcar.has("passenger_Name")) {
											beansss.setCar_passenger_Name(objcar
													.getString("passenger_Name"));
										}
										if (objcar.has("passenger_Number")) {
											beansss.setCar_passenger_Number(objcar
													.getString("passenger_Number"));
										}
										if (objcar.has("passenger_Phone")) {
											beansss.setCar_passenger_Phone(objcar
													.getString("passenger_Phone"));
										}
										if (objcar.has("payStatus")) {
											beansss.setCar_payStatus(objcar
													.getString("payStatus"));
										}
										if (objcar.has("regulatepan_Reson")) {
											beansss.setCar_regulatepan_Reson(objcar
													.getString("regulatepan_Reson"));
										}
										if (objcar.has("start_Address")) {
											beansss.setCar_start_Address(objcar
													.getString("start_Address"));
										}
										if (objcar.has("start_Latitude")) {
											beansss.setCar_start_Latitude(objcar
													.getString("start_Latitude"));
										}
										if (objcar.has("start_Position")) {
											beansss.setCar_start_Position(objcar
													.getString("start_Position"));
										}
										if (objcar.has("time_Length")) {
											beansss.setCar_time_Length(objcar
													.getString("time_Length"));
										}
										if (objcar.has("total_Amount")) {
											beansss.setCar_total_Amount(objcar
													.getString("total_Amount"));
										}
										if (objcar.has("car_type")) {
											beansss.setCar_type(objcar
													.getString("car_type"));
										}
										if (objcar.has("user_Id")) {
											beansss.setCar_user_Id(objcar
													.getString("user_Id"));
										}
										if (objcar.has("vehicle_Number")) {
											beansss.setCar_vehicle_Number(objcar
													.getString("vehicle_Number"));
										}
										if (objcar.has("order_Id")) {
											beansss.setOrder_Id(objcar
													.getString("order_Id"));
										}
										if (objcar.has("price")) {
											beansss.setCar_price(objcar
													.getString("price"));
										}
										if (objcar.has("goodsId")) {
											beansss.setCar_goodsId(objcar
													.getString("goodsId"));
										}
										if (objcar.has("goodsName")) {
											beansss.setCar_goodsName(objcar
													.getString("goodsName"));
										}
									}
									if (obj_c.has("goodsName")) {
										beansss.setCar_goodsName(obj_c
												.getString("goodsName"));
									}
									listcar.add(beansss);
								}
								bean.setList_car(listcar);
							}
						} else if (type.equals("4")) {
							/**
							 * 说明是购票的数据
							 */
							if (objs.has("orderDetailList")) {
								JSONArray arraytices = objs
										.getJSONArray("orderDetailList");
								List<OrderListBeanTices> listtices = new ArrayList<OrderListBeanTices>();
								for (int s = 0; s < arraytices.length(); s++) {
									OrderListBeanTices beantices = new OrderListBeanTices();
									JSONObject objtices = arraytices
											.getJSONObject(s);
									if (objtices.has("goodsId")) {
										beantices.setGoodsId(objtices
												.getString("goodsId"));
									}
									if (objtices.has("id")) {
										beantices.setId(objtices
												.getString("id"));
									}
									if (objtices.has("pinfo")) {
										beantices.setPinfo(objtices
												.getString("pinfo"));
									}
									if (objtices.has("price")) {
										beantices.setPrice(objtices
												.getString("price"));
									}
									if (objtices.has("wareNumber")) {
										beantices.setWareNumber(objtices
												.getString("wareNumber"));
									}
									listtices.add(beantices);
								}
								bean.setListtices(listtices);
							}
						}
					}
					if (bean.getList() != null && bean.getList().size() > 0
							|| bean.getList_car() != null
							&& bean.getList_car().size() > 0
							|| bean.getList_zhong() != null
							&& bean.getList_zhong().size() > 0
							|| bean.getListtices() != null
							&& bean.getListtices().size() > 0)
						mList.add(bean);

				}
			} else {
				// Toast.makeText(AllOrderActivity.this,"网络异常",0).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mList;

	}

	/**
	 * 解析用车订单数据
	 * 
	 * @return
	 */
	private List<OrderListBean> UseWayJsonData_car(String jsonstring) {
		List<OrderListBean> list = new ArrayList<OrderListBean>();
		try {
			JSONObject obj = new JSONObject(jsonstring);
			JSONArray array = obj.getJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				JSONObject objs = array.getJSONObject(i);
				OrderListBean bean = new OrderListBean();
				bean.setOrdersType("2");
				if (objs.has("payStatus")) {
					bean.setStatus(objs.getString("payStatus"));
				}
				if (objs.has("passenger_Name")) {
					bean.setCompanies(objs.getString("passenger_Name"));
				}
				if (objs.has("id")) {
					bean.setId(objs.getString("id"));
				}
				List<OrderListBeanCar> lists = new ArrayList<OrderListBeanCar>();
				OrderListBeanCar beans = new OrderListBeanCar();
				if (objs.has("car_Brand")) {
					beans.setCar_car_Brand(objs.getString("car_Brand"));
				}
				if (objs.has("city")) {
					beans.setCar_car_city(objs.getString("city"));
				}
				if (objs.has("car_Type_Id")) {
					beans.setCar_car_Type_Id(objs.getString("car_Type_Id"));
				}
				if (objs.has("create_Time")) {
					beans.setCar_create_Time(objs.getString("create_Time"));
				}
				if (objs.has("deleteFlag")) {
					beans.setCar_deleteFlag(objs.getString("deleteFlag"));
				}
				if (objs.has("driver_Id")) {
					beans.setCar_driver_Id(objs.getString("driver_Id"));
				}
				if (objs.has("driver_Name")) {
					beans.setCar_driver_Name(objs.getString("driver_Name"));
				}
				if (objs.has("driver_Phone")) {
					beans.setCar_driver_Phone(objs.getString("driver_Phone"));
				}
				if (objs.has("end_Address")) {
					beans.setCar_end_Address(objs.getString("end_Address"));
				}
				if (objs.has("end_Latitude")) {
					beans.setCar_end_Latitude(objs.getString("end_Latitude"));
				}
				if (objs.has("end_Lognitude")) {
					beans.setCar_end_Lognitude(objs.getString("end_Lognitude"));
				}
				if (objs.has("end_Position")) {
					beans.setCar_end_Position(objs.getString("end_Position"));
				}
				if (objs.has("expect_Start_Time")) {
					beans.setCar_expect_Start_Time(objs
							.getString("expect_Start_Time"));
				}
				if (objs.has("flight_Number")) {
					beans.setCar_flight_Number(objs.getString("flight_Number"));
				}
				if (objs.has("id")) {
					beans.setCar_id(objs.getString("id"));
				}
				if (objs.has("insure_Company")) {
					beans.setCar_insure_Company(objs
							.getString("insure_Company"));
				}
				if (objs.has("insure_Number")) {
					beans.setCar_insure_Number(objs.getString("insure_Number"));
				}
				if (objs.has("msg")) {
					beans.setCar_msg(objs.getString("msg"));
				}
				if (objs.has("passenger_Name")) {
					beans.setCar_passenger_Name("passenger_Name");
				}
				if (objs.has("passenger_Number")) {
					beans.setCar_passenger_Number(objs
							.getString("passenger_Number"));
				}
				if (objs.has("passenger_Phone")) {
					beans.setCar_passenger_Phone(objs
							.getString("passenger_Phone"));
				}
				if (objs.has("payStatus")) {
					beans.setCar_payStatus(objs.getString("payStatus"));
				}
				if (objs.has("regulatepan_Reson")) {
					beans.setCar_regulatepan_Reson(objs
							.getString("regulatepan_Reson"));
				}
				if (objs.has("start_Address")) {
					beans.setCar_start_Address(objs.getString("start_Address"));
				}
				if (objs.has("start_Latitude")) {
					beans.setCar_start_Latitude(objs
							.getString("start_Latitude"));
				}
				if (objs.has("start_Position")) {
					beans.setCar_start_Position(objs
							.getString("start_Position"));
				}
				if (objs.has("time_Length")) {
					beans.setCar_time_Length(objs.getString("time_Length"));
				}
				if (objs.has("total_Amount")) {
					beans.setCar_total_Amount(objs.getString("total_Amount"));
				}
				if (objs.has("car_type")) {
					beans.setCar_type(objs.getString("car_type"));
				}
				if (objs.has("user_Id")) {
					beans.setCar_user_Id(objs.getString("user_Id"));
				}
				if (objs.has("vehicle_Number")) {
					beans.setCar_vehicle_Number(objs
							.getString("vehicle_Number"));
				}
				if (objs.has("order_Id")) {
					beans.setOrder_Id(objs.getString("order_Id"));
				}
				lists.add(beans);
				bean.setList_car(lists);
				list.add(bean);

			}
			return list;

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 从我们的服务器生成支付订单
	 */
	public void requestCreatPayOrder(String money) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("user_name", GlobalParams.USER_NAME); // 用户名称
		params.put("orderId", mYiDaoOrderId); // 用车的订单
		WifiUtils mWifi = new WifiUtils(LApplication.context);
		params.put("ip", mWifi.getIpAddress()); // ip
		params.put("pay_expire", money); // 价格

		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.YIDAO_PAY_ORDER, params, new GetDatas() {
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
									Log.i("ADDRESS",
											"生成支付订单" + data.getNetResultCode()
													+ backdata);
									// 调用方法，给listview加载数据，先将数据通过handler发送到主线程中
									Message msg = new Message();
									msg.arg1 = 330;
									msg.obj = backdata;
									handler.sendMessage(msg);
								}
							}

						}
					}
				}, false, false);

	}

	/**
	 * 支付成功给易道
	 */
	public void payOk() {

		netService = NetService.getInStance();
		netService.setParams("access_token", ConstantValue.CAR_TOKEN);
		netService.setParams("order_id", mYiDaoOrderId);
		netService.setParams("amount", "1");
		netService.setHttpMethod(HttpSetting.GET_MODE);
		netService.setUrl(ConstantValue.PAY_SUCCESS_YIDAO);
		netService.loader(new NetLoadListener() {
			@Override
			public void onloaderListener(BackData result) {
				Log.d("carOrder",
						mYiDaoOrderId + "易道回调成功" + result.getNetResultCode()
								+ "结果：" + result.getObject());
				Message msg = new Message();
				msg.arg1 = 555;
				if (result.getNetResultCode() == 200) {

				} else {
					msg.obj = result.getObject();
				}
				handler.sendMessage(msg);
			}
		});

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
						if (data != null) {
							// 解析返回来的数据
							Message msg = new Message();
							msg.arg1 = NOTIFY_SUCCESS;
							msg.obj = data.getObject();
							handler.sendMessage(msg);
							return;
						}
						handler.sendEmptyMessage(NOTIFY_YONG);
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

	@Override
	protected void onDestroy() {
		handler.removeCallbacksAndMessages(null);
		if (pagerList != null) {
			pagerList.clear();
			pagerList = null;
		}
		if (mWindowMenulist != null) {
			mWindowMenulist.clear();
			mWindowMenulist = null;
		}
		if (mList != null) {
			mList.clear();
			mList = null;
		}
		if (allList != null) {
			allList.clear();
			allList = null;
		}
		if (groups != null) {
			groups.clear();
			groups = null;
		}
		if (children != null) {
			children.clear();
			children = null;
		}
		instance = null;
		allOrderView = null;
		waitPayOrderView = null;
		waitSendOrderView = null;
		waitReceiverOrderView = null;
		super.onDestroy();
	}

	/**
	 * 查询当前订单是否在有效期内
	 */
	private void checkIsEffective(String orderid) {
		final Map<String, String> params = new HashMap<String, String>();
		params.put("orderIds", orderid); // 永乐订单ID
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.IS_EFECTIVE, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						Message msg = new Message();
						if (data != null) {
							// 解析返回来的数据
							msg.arg1 = SUCCESS;
							msg.obj = data.getObject();
							Log.i("wxn", "jiancha piao:..." + msg.obj);
							handler.sendMessage(msg);
							return;
						} else {
							msg.arg1 = FILERE;
							handler.sendMessage(msg);
						}
					}
				}, false, false);
	}

	/**
	 * 解析 永乐票是否在有效期内
	 * 
	 * @param data
	 */
	private void anilyseYL(String data) {
		if (TextUtils.isEmpty(data) || ticksOrderbean == null)
			return;
		try {
			JSONObject obj = new JSONObject(data);
			if (obj.has("data")) {
				String result = obj.getString("data");
				JSONObject resuObj = new JSONObject(result);
				if (resuObj.has("orderAndStatus")) {
					String orderAndStatus = resuObj.getString("orderAndStatus");
					if (!TextUtils.isEmpty(orderAndStatus)) {
						String[] strs = orderAndStatus.split(":");
						if (strs != null && strs.length > 0
								&& !"7".equals(strs[1])) {
							// 购票去支付
							String orderid = ticksOrderbean.getId();
							List<OrderListBeanTices> list = ticksOrderbean
									.getListtices();
							String useid = GlobalParams.USER_ID;
							String ip = getPsdnIp();
							double goodsprice = 0;
							if (list != null && list.size() != 0) {
								for (int i = 0; i < list.size(); i++) {
									double price = Double.parseDouble(list.get(
											i).getPrice());
									goodsprice = goodsprice + price;
								}
							}
							String currency = "CNY";
							double yunfei = Double.parseDouble(ticksOrderbean
									.getFreight());
							String pay_expire = (goodsprice + yunfei) + "";
							String product_id = ticksOrderbean.getListtices()
									.get(0).getId();
							// 调用方法，转换实体对象
							String string = ticksOrderbean.getListtices()
									.get(0).getPinfo();
							TicesDetailBean beans = GetDataFromDetail(string);
							String product_name = beans.getProductName();
							String product_desc = beans.getProductName();
							String product_urls = beans.getPicture();
							UseWayGetPayData(orderid, useid, ip, goodsprice
									+ "", currency, pay_expire, product_id,
									product_name, product_desc, product_urls);
							return;
						}
					}
				}
			}
			// 该订单已失效 、、更新界面
			Toast.makeText(getApplicationContext(), "该订单已失效，请重新购买",
					Toast.LENGTH_SHORT).show();
			// useWayGetDataFromInternet_down();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void analyseOrderData(String backdata) {
		if (backdata != null && !TextUtils.isEmpty(backdata)) {
			mLayout_progressbar.setVisibility(View.GONE);
			// 调用方法，解析数据
			Log.i("订单数据是", backdata);
			// 调用方法，解析详情数据
			if (allList != null)
				allList.clear();
			allList = useWayJsonData(backdata);
			if (allList != null && allList.size() != 0) {
				allOrderView.setFirstOrderData(allList);
				// 调用方法，分拣数据，并且将数据加载到不同的界面中
				useWayFenjianData(allList);
			} else {
				// 调用方法，显示暂无数据
				allOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
			}
		}
	}
	/**
	 * 解析下拉刷新的数据
	 */
	private void analyseRefreshData(String backdatassss){
		mPullToRefreshExpandableListView.onRefreshComplete();
		if (backdatassss != null && !TextUtils.isEmpty(backdatassss)) {
			// 调用方法，解析数据
			Log.i("刷新订单数据是", backdatassss);
			// 调用方法，解析详情数据
			allList.clear();
			allList = useWayJsonData(backdatassss);
			//
			// 调用方法，进行分拣数据
			if (allList != null && allList.size() != 0) {
				useWayCheckDataToView(allList);
			} else {
				// 调用方法，显示暂无数据
				allOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
			}
		}
	}
	/**
	 * 解析用车订单数据
	 */
	private void analyseCareData(String car_string){
		// 获取用车订单数据
		// 先解析
		if (!TextUtils.isEmpty(car_string)) {
			// 调用方法解析用车订单数据
			List<OrderListBean> list = UseWayJsonData_car(car_string);
			if (list != null) {
				allList.addAll(list);
				if (allList != null && allList.size() != 0) {
					allOrderView.setFirstOrderData(allList);
					// 调用方法，分拣数据，并且将数据加载到不同的界面中
					useWayFenjianData(allList);
				} else {
					// 调用方法，显示暂无数据
					allOrderView.mLayoutNoData
							.setVisibility(View.VISIBLE);
				}
			}
		}
	}
	/**
	 * 生成支付订单
	 * @param backdatass_songhuo
	 */
	private void createOrder(String backdatass_songhuo){
		if (!TextUtils.isEmpty(backdatass_songhuo)) {
			PayBean paybean = useWayJsonDataPay(backdatass_songhuo);
			if (paybean != null
					&& !TextUtils.isEmpty(paybean.getSign())) {
				// 开始调用方法，进行支付
				pay(paybean);
			}
		}
	}
	/**
	 *  商品或者餐饮送货成功处理
	 */
	private void takeSuccess(){
		if (mBeanP != null) {
			if (mBeanP.getOrdersType().equals("0")) {
				// 商品送货
				List<OrderListBeanGoodsBean> list = mBeanP.getList();
				double totalprice = 0;
				if (list != null && list.size() != 0) {
					for (int i = 0; i < list.size(); i++) {
						virtualData(list.get(i));
						double price = Double.parseDouble(list.get(i)
								.getPrice());
						totalprice = totalprice + price;

					}
					if (groups != null && groups.size() != 0
							&& children != null && children.size() != 0) {
						Intent intent = new Intent(
								AllOrderActivity.this,
								PaySuccessActivity.class);
						intent.putExtra("group", (Serializable) groups);
						intent.putExtra("child",
								(Serializable) children);
						// 这里是标记
						intent.putExtra("tag", "songhuo");
						intent.putExtra("type", "shangpin");
						intent.putExtra("yunfei", mBeanP.getFreight());
						intent.putExtra("price", totalprice + "");
						intent.putExtra("zuoweihao",
								mBeanP.getPosition());
						intent.putExtra("phone", mBeanP.getTelePhone());
						startActivity(intent);
					}
				}
			} else if (mBeanP.getOrdersType().equals("1")) {
				// 餐饮送货
				List<OrderListBeanGoodsBean> list = mBeanP.getList();
				double totalprice = 0;
				if (list != null && list.size() != 0) {
					for (int i = 0; i < list.size(); i++) {
						virtualData(list.get(i));
						double price = Double.parseDouble(list.get(i)
								.getPrice());
						totalprice = totalprice + price;
					}
					if (groups != null && groups.size() != 0
							&& children != null && children.size() != 0) {
						Intent intent = new Intent(
								AllOrderActivity.this,
								PaySuccessActivity.class);
						intent.putExtra("group", (Serializable) groups);
						intent.putExtra("child",
								(Serializable) children);
						// 这里是标记
						intent.putExtra("tag", "songhuo");
						intent.putExtra("type", "canyin");
						intent.putExtra("yunfei", mBeanP.getFreight());
						intent.putExtra("price", totalprice + "");
						intent.putExtra("zuoweihao",
								mBeanP.getPosition());
						intent.putExtra("phone", mBeanP.getTelePhone());
						startActivity(intent);
					}
				}
			}
		}
	}
	/**
	 * // 用车订单获取失败
	 */
	private void useWayHandleusecarOrder() {
		
		// 首先明白，初始状态下，需要显示的是全部的数据，所以，这里需要将全部订单通过方法调用来传递到各个可切换的子view中
		if (allList != null && allList.size() != 0) {
			allOrderView.setFirstOrderData(allList);
			// 调用方法，分拣数据，并且将数据加载到不同的界面中
			useWayFenjianData(allList);
		} else {
			// 调用方法，显示暂无数据
			allOrderView.mLayoutNoData.setVisibility(View.VISIBLE);
		}
	}
	/**
	 * 众筹支付成功处理
	 */
	private void useHandleCrowdPaySuccess() {
		// TODO Auto-generated method stub
		if (mBeanP != null) {
			// 这里需要生成三个实体对象
			
			Intent intent = new Intent(AllOrderActivity.this,
					CrowdPaySuccessActivity.class);
			intent.putExtra("tag", "order");
			intent.putExtra("bean", mBeanP);
			startActivity(intent);
		} else {
			Toast.makeText(AllOrderActivity.this, "网络异常",  Toast.LENGTH_SHORT).show();
		}
	}
	

	/**
	 * 餐饮或者商品支付成功处理
	 */
	private void useWayHandleGOODSppAYSuccess() {
		// TODO Auto-generated method stub
		if (mBeanP != null) {
			if (mBeanP.getOrdersType().equals("0")) {
				// s说明是商品
				List<OrderListBeanGoodsBean> list = mBeanP.getList();
				double totalprice = 0;
				if (list != null && list.size() != 0) {
					for (int i = 0; i < list.size(); i++) {
						virtualData(list.get(i));
						double price = Double.parseDouble(list.get(i)
								.getPrice());
						totalprice = totalprice + price;
					}
					if (groups != null && groups.size() != 0
							&& children != null && children.size() != 0) {
						Intent intent = new Intent(
								AllOrderActivity.this,
								PaySuccessActivity.class);
						intent.putExtra("group", (Serializable) groups);
						intent.putExtra("child",
								(Serializable) children);
						// 这里是标记
						intent.putExtra("tag", "ziqu");
						intent.putExtra("type", "shangpin");
						intent.putExtra("ziqudizhi",
								mBeanP.getPosition());
						intent.putExtra("yunfei", mBeanP.getFreight());
						intent.putExtra("price", totalprice + "");
						startActivity(intent);
					}
				}
			} else if (mBeanP.getOrdersType().equals("1")) {
				// s说明是餐饮
				List<OrderListBeanGoodsBean> list = mBeanP.getList();
				double totalprice = 0;
				if (list != null && list.size() != 0) {
					for (int i = 0; i < list.size(); i++) {
						virtualData(list.get(i));
						double price = Double.parseDouble(list.get(i)
								.getPrice());
						totalprice = totalprice + price;
					}
					if (groups != null && groups.size() != 0
							&& children != null && children.size() != 0) {
						Intent intent = new Intent(
								AllOrderActivity.this,
								PaySuccessActivity.class);
						intent.putExtra("group", (Serializable) groups);
						intent.putExtra("child",
								(Serializable) children);
						// 这里是标记
						intent.putExtra("tag", "ziqu");
						intent.putExtra("type", "shangpin");
						intent.putExtra("ziqudizhi",
								mBeanP.getPosition());
						intent.putExtra("yunfei", mBeanP.getFreight());
						intent.putExtra("price", totalprice + "");
						startActivity(intent);
					}
				}
			}
		}
	}
	/**
	 * 提醒发货请求成功处理
	 * @param mshaaa 
	 */
	private void useWayTixingfahuo(String mshaaa) {
		// TODO Auto-generated method stub
		if (mshaaa.equals("success")) {
			Toast.makeText(AllOrderActivity.this, "提醒成功", 0).show();
		} else {
			Toast.makeText(AllOrderActivity.this, "网络繁忙", 0).show();
		}
	}
	/**
	 * 通知永乐购票成功
	 */
	private void useWayNotifySuccessGOUPIAO() {
		// TODO Auto-generated method stub
		notifyYLe();
		Intent intent = new Intent(AllOrderActivity.this,
				TickOrderActivity.class);
		intent.putExtra("orderNumber", out_trade_no);
		startActivity(intent);
	}
	/**
	 * 账单成功确认处理
	 * @param feeBackdata
	 */
	private void useWayCheckFeeHandle(String feeBackdata) {
		// TODO Auto-generated method stub
		JSONObject orderObj;
		JSONObject feeObj;

		try {
			orderObj = new JSONObject(feeBackdata);
			if (feeBackdata.contains("order_amount")) {
				JSONObject feeDeltealObj = orderObj
						.getJSONObject("result");
				// 调整金额，order_amount为应付金额;coupon_facevalue优惠金额
				feeStr = feeDeltealObj.getString("order_amount");
				discountStr = feeDeltealObj
						.getString("coupon_facevalue");
				// mDriverLeverRt.setNumStars(Integer.parseInt(driverDetailBean.getStar_level()));
				// mDriverLeverRt.setRating(Integer.parseInt(driverDetailBean.getStar_level()));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		requestCreatPayOrder(feeStr);
	}
	/**
	 * 易道支付成功回调
	 * @param resultPay
	 */
	private void useUseCarNotifySuccess(String resultPay) {
		// TODO Auto-generated method stub
		try {
			JSONObject payObj = new JSONObject(resultPay);
			String payCode = payObj.getString("code");
			Log.i("lwc", "易道支付成功回掉返回结果码" + payCode + "结果值" + resultPay);
			if (payCode.equals("200")) {
				Intent payOkIntent = new Intent(AllOrderActivity.this,
						CarServiceCompleteActivity.class);
				payOkIntent.putExtra("fee", feeStr);
				payOkIntent.putExtra("discount", discountStr);
				payOkIntent.putExtra("orderid", mYiDaoOrderId);

				startActivity(payOkIntent);
			} else {
				Toast.makeText(AllOrderActivity.this,
						payObj.getString("result"),  Toast.LENGTH_SHORT)
						.show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}




}
