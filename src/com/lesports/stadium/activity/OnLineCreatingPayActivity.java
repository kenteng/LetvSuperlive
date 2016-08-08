package com.lesports.stadium.activity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.lesports.stadium.R;
import com.lesports.stadium.adapter.OrderAddressAdapter;
import com.lesports.stadium.adapter.ShopcartExpandableListViewAdapter;
import com.lesports.stadium.adapter.ShopcartExpandableListViewAdapter.CheckInterface;
import com.lesports.stadium.adapter.ShopcartExpandableListViewAdapter.ModifyCountInterface;
import com.lesports.stadium.base.BaseActivity;
import com.lesports.stadium.bean.CouponBean;
import com.lesports.stadium.bean.GroupInfo;
import com.lesports.stadium.bean.OrderAddressBean;
import com.lesports.stadium.bean.OrderBean;
import com.lesports.stadium.bean.PayBean;
import com.lesports.stadium.bean.PayParametric;
import com.lesports.stadium.bean.ProductInfo;
import com.lesports.stadium.bean.ServiceCateringDetailBean;
import com.lesports.stadium.bean.YouHuiBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.EditLengthOnclistener;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.PayUtils;
import com.lesports.stadium.utils.TimeSelector;
import com.lesports.stadium.utils.Utils;
import com.lesports.stadium.view.CustomDialog;
import com.lesports.stadium.view.MyScrollView;
import com.lesports.stadium.view.MyScrollView.OnScrollListener;
import com.lesports.stadium.view.Mylistview;
import com.lesports.stadium.view.WiperSwitch;
import com.lesports.stadium.view.WiperSwitch.OnChangedListener;
import com.letv.lepaysdk.ELePayState;
import com.letv.lepaysdk.LePayApi;
import com.letv.lepaysdk.LePayConfig;
import com.letv.lepaysdk.LePay.ILePayCallback;
import com.umeng.analytics.MobclickAgent;

/**
 * ***************************************************************
 * 
 * @Desc : 在线餐饮订单确认界面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwc
 * @data:
 * @Version : v1.0
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class OnLineCreatingPayActivity extends BaseActivity implements
		OnClickListener, OnChangedListener, CheckInterface,
		ModifyCountInterface, OnScrollListener {

	/**
	 * 顶部标题
	 */
	private TextView mTitle;
	/**
	 * 顶部返回按钮
	 */
	private ImageView mBack;
	/**
	 * 展示用户选定的需要购买的商品的列表项
	 */
	private ExpandableListView mExListview;
	/**
	 * 用户所选商品的总金额
	 * */
	private TextView mTotalPrice;
	/**
	 * 优惠信息，可获得的积分数额
	 */
	private TextView mJifenShuE;
	/**
	 * 是否使用优惠券开关
	 */
	private WiperSwitch mYouhuiSwitch;
	/**
	 * 可用积分
	 */
	private TextView mKeyongJF;
	/**
	 * 可抵用金额
	 */
	private TextView mKediJinE;
	/**
	 * 是否使用积分开关
	 */
	private WiperSwitch mUserJifenSwitch;
	/**
	 * 备注信息
	 */
	private EditText mBeiZhuInfo;
	/**
	 * 商品数量
	 */
	private TextView mGoodsNum;
	/**
	 * 商品总价格
	 */
	private TextView mGoodsTotalPrice;
	/**
	 * 展示收货方式的buju
	 */
	private RelativeLayout mCustomListview;
	/**
	 * 需要支付的价格总计
	 */
	private TextView mPayPrice;
	/**
	 * 需要支付的运费说明
	 */
	private TextView mPayYunfei;
	/**
	 * 支付按钮
	 */
	private TextView mPay;
	/**
	 * 列表项控件的数据适配器
	 */
	private ShopcartExpandableListViewAdapter selva;
	/**
	 * // 组元素数据列表
	 */
	private List<GroupInfo> groups = new ArrayList<GroupInfo>();
	/**
	 * // 子元素数据列表
	 */
	private Map<String, List<ProductInfo>> children = new HashMap<String, List<ProductInfo>>();
	/**
	 * 存储界面收货和自提地址的数据源集合
	 */
	private List<OrderAddressBean> mAddressBeanlist;
	/**
	 * 用于适配地址数据的适配器
	 */
	private OrderAddressAdapter mAddressAdapter;
	/**
	 * 自取
	 */
	private CheckBox mRadioZiqu;
	/**
	 * 送货
	 */
	private CheckBox mRadioSonghuo;
	/**
	 * 用于记录用户选择的收货方式的id
	 */
	private String Address_ID = new String();
	/**
	 * 用于标记是否使用优惠券
	 */
	private boolean If_USE_YHQ = false;
	/**
	 * 用于标记是否使用积分
	 */
	private boolean If_USE_JF = false;
	/**
	 * 用来标示收货方式的TAG
	 */
	private String Goods_Recipet_TAG = new String();
	/**
	 * 用来标示自取的常量
	 */
	private final String ZIQU_TAG = "ziqu";
	/**
	 * 用来标示收货的常量
	 */
	private final String SHOUHUO_TAG = "shouhuo";
	/**
	 * 标记组
	 */
	private final String GROUP_TAG = "group";
	/**
	 * 标记子
	 */
	private final String CHILD_TAG = "child";
	/**
	 * 需要改变的商品总价,计算积分前
	 */
	private double totalPrice = 0.00;// 购买的商品总价
	/**
	 * 积分与现金的抵用规则
	 */
	private double JIFEN_SPEC = 0.02;
	/**
	 * 未登陆提示按钮
	 */
	private CustomDialog exitDialog;
	/**
	 * 退款说明的提示框
	 */
	private CustomDialog mTuikuanshuoming;
	/**
	 * 送货地址
	 */
	private LinearLayout mSonghuoAddress;
	/**
	 * 定义变量，用来标示商品总数量
	 */
	private int mGoodscount = 0;
	/**
	 * 订单中进行选择优惠优惠的布局
	 */
	private RelativeLayout mLayout_choiseCoupon;
	/**
	 * 用来标记界面回调
	 */
	private final int RESCODE_TAG = 1;
	/**
	 * 用来显示用户选定的优惠信息的名称的
	 */
	private TextView mCouponName;
	/**
	 * 运费
	 */
	private TextView mYunfei;
	/**
	 * 座位号
	 */
	private EditText mZuoweihao;
	/**
	 * 手机号
	 */
	private EditText mPhoneNum;

	/**
	 * 实例对象
	 */
	public static OnLineCreatingPayActivity instence;
	/**
	 * 选座布局
	 */
	private RelativeLayout mLayoutChoiseZuowei;
	/**
	 * 座位选择器
	 */
	public TimeSelector timeSelector;
	/**
	 * 本类对象
	 */
	/**
	 * 用来标记是否选择送货
	 */
	private boolean IS_SONGHUO = true;
	/**
	 * 是否选择取货
	 */
	private boolean IS_QUHUO = true;
	/**
	 * 送货的退款说明
	 */
	private TextView mTuikuan_songhuo;
	/**
	 * 自取的退款说明
	 */
	private TextView mTuikuan_ziqu;
	/**
	 * Dialog dialog
	 */
	private Dialog dialog;
	/**
	 * 该标记用来标记用户选择的是取货还是送货
	 */
	private String ZIQU_OR_SONG = "wu";
	/**
	 * 自提地址
	 */
	private TextView mZiTiAddress;
	/**
	 * 底部运费说明
	 */
	private TextView mYunfeiShuoming;
	/**
	 * 显示引导页部分的布局
	 */
	private RelativeLayout mLayout_yindaoye;
	/**
	 * 
	 * 上划的scrollview
	 */
	private ScrollView mScrollview;
	/**
	 * 用来记录所要动态显示的高度
	 */
	private int mScrllviewHeight = 0;
	/**
	 * 顶部布局
	 */
	private RelativeLayout mLayout_topss;
	/**
	 * 底部布局
	 */
	private RelativeLayout mLayout_bottom;
	/**
	 * 底部需要减掉的部分
	 */
	private LinearLayout mLayout_bottomss;
	/**
	 * 更布局
	 */
	private RelativeLayout mLayout_root;
	/**
	 * 引导页部分
	 */
	private RelativeLayout mLayout_guide;
	private final String SHAREDPREFERENCES_ONLINE = "LS_APP_ONLINE";
	private String orderid_s;
	private PayBean mPaybean;
	private OrderBean mOrderbean;

	/**
	 * 用来标注是自取还是送货
	 */
	private String ZIQU_OR_SONGHHUO = "0";
	/**
	 * 进度层
	 */
	private RelativeLayout mLayout_progress;
	/**
	 * 商家名称
	 */
	private String mGoodsStoreName;
	/**
	 * 运费
	 */
	private double yunfei;
	/**
	 * 自取手机号
	 */
	private EditText mZiquPhone;
	/**
	 * 用来标注用户是否使用优惠券
	 */
	private boolean is_use_coupone = false;
	/**
	 * YouHuiBean bean
	 */
	private YouHuiBean mUsebean;
	/**
	 * 优惠券数据集合
	 */
	private List<YouHuiBean> mCounponList;
	/**
	 * 自取姓名
	 */
	private EditText mZiquName;
	/**
	 * 送货姓名
	 */
	private EditText mSonghuoName;
	/**
	 * handle里面需要用到的标记
	 */
	private final int GET_PAY_ORDER_INFO = 1;// 生成订单
	private final int GET_PAY_ORDER_FAILED = 111;// 生成订单失败
	private final int PAY_SUCCESS_HANDLE = 2;// 支付成功处理
	private final int GET_PAY_DATA = 4;// 生成支付参数
	private final int GET_PAY_DATA_FAILED = 444;// 生成支付参数失败处理
	private final int USER_CANCLE_PAY = 222;// 用户取消支付
	private final int GET_CURRENT_USER_COUPON = 134;// 获取用户优惠券成功
	private final int GET_CURRENT_USER_COUPON_FAILED = 135;// 优惠券失败
	/**
	 * 处理网络数据的handle
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case GET_PAY_ORDER_INFO:
				String backdata = (String) msg.obj;
				if (backdata != null && !TextUtils.isEmpty(backdata)) {
					useWayHandlePayOrderInfo(backdata);
				}
				break;
			case GET_PAY_ORDER_FAILED:
				mLayout_progress.setVisibility(View.GONE);
				Toast.makeText(OnLineCreatingPayActivity.this, "生成订单失败",
						Toast.LENGTH_SHORT).show();
				break;
			case PAY_SUCCESS_HANDLE:
				// 支付成功的结果处理
				useWayPaySuccessHandle();
				break;
			case GET_PAY_DATA:
				String jsondatass = (String) msg.obj;
				if (jsondatass != null && !TextUtils.isEmpty(jsondatass)) {
					useWayPayDataHandle(jsondatass);
				}
				break;
			case GET_PAY_DATA_FAILED:
				Toast.makeText(OnLineCreatingPayActivity.this, "生成订单失败",
						Toast.LENGTH_SHORT).show();
				mLayout_progress.setVisibility(View.GONE);
				break;
			case USER_CANCLE_PAY:
				// 用户取消支付以后
				useWayUserCanclePayHandle();
				break;
			case GET_CURRENT_USER_COUPON:
				// 获取优惠券数据
				String backdatasss = (String) msg.obj;
				if (!TextUtils.isEmpty(backdatasss)) {
					useWayHandleCurrentUserCoupon(backdatasss);
				}
				break;
			case GET_CURRENT_USER_COUPON_FAILED:
				mCouponName.setText("无可用");
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
		instence = this;
		setContentView(R.layout.activity_order_online);
		MobclickAgent.onEvent(OnLineCreatingPayActivity.this, "FoodOrderPage");
		initviews();
//		initGuide(mLayout_guide);//去掉引导页
		virtualData();
		initEvents();
		initCheckBoxClick();
		initZuoweiChoise();
		// 调用方法，判断优惠券是否可用
		useWayGetData();
	}

	/**
	 * 该方法用来向服务端提交订单号以及商品信息，以及用户信息，获取支付参数来进行支付
	 * 
	 * @param bean
	 */
	private void UseWayGetPayContent(OrderBean bean) {
		// TODO Auto-generated method stub
		// 该方法用来向
		if (bean != null) {
			String orderId = bean.getId();// 订单号
			String username = GlobalParams.USER_NAME;// 用户名
			String ip = getPsdnIp();
			String goodsprice = bean.getAmount();
			String currency = "CNY";
			String pay_expire = "45";
			String product_id = "";
			String product_name = "";
			String product_desc = "";
			String product_urls = "";
			if (groups != null && groups.size() != 0 && children != null
					&& children.size() != 0) {
				// 调用方法，计算实际支付金额
				product_id = children.get(groups.get(0).getId()).get(0).getId();
				product_name = children.get(groups.get(0).getId()).get(0)
						.getName();
				product_desc = children.get(groups.get(0).getId()).get(0)
						.getDesc();
				product_urls = ConstantValue.BASE_IMAGE_URL
						+ children.get(groups.get(0).getId()).get(0)
								.getImageUrl() + ConstantValue.IMAGE_END;
			}
			UseWayGetPayData(orderId, username, ip, goodsprice, currency,
					pay_expire, product_id, product_name, product_desc,
					product_urls);
		}
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
								// 调用方法，将数据加载到适配器中
								if (data.getNetResultCode() == 0) {
									Message msg = new Message();
									msg.arg1 = GET_PAY_DATA;
									msg.obj = backdata;
									handler.sendMessage(msg);
								} else {
									Message msg = new Message();
									msg.arg1 = 444;
									handler.sendMessage(msg);
								}
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

	/**
	 * 座位选择器
	 */
	private void initZuoweiChoise() {
		// TODO Auto-generated method stub
		timeSelector = new TimeSelector(OnLineCreatingPayActivity.this,
				new TimeSelector.ResultHandler() {
					@Override
					public void handle(String time) {
						Toast.makeText(getApplicationContext(), time,
								Toast.LENGTH_LONG).show();
					}
				}, "2015-10-27 09:33", "2016-11-29 21:54", instence, true);
		timeSelector.setScrollUnit(TimeSelector.SCROLLTYPE.HOUR,
				TimeSelector.SCROLLTYPE.MINUTE);
	}

	private void pay(PayBean paybean) {
		// Log.i("221", "pay start");
		// Toast.makeText(OnLineCreatingPayActivity.this,
		// "业务线"+paybean.getMerchant_business_id(), Toast.LENGTH_SHORT).show();
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
			parameters.setPrice(paybean.getPrice());
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
			LePayApi.initConfig(OnLineCreatingPayActivity.this, config);
			LePayApi.doPay(OnLineCreatingPayActivity.this, param,
					new ILePayCallback() {
						@Override
						public void payResult(ELePayState status, String message) {
							// Toast.makeText(OnLineCreatingPayActivity.this,
							// "结果:" + status + "|" + message,
							// Toast.LENGTH_SHORT)
							// .show();
							if (ELePayState.CANCEL == status) { // 支付取消
							// Toast.makeText(OnLineCreatingPayActivity.this,
							// "取消支付", Toast.LENGTH_SHORT)
							// .show();
								Log.i("支付取消", "取消支付");
								Message msg = new Message();
								msg.arg1 = USER_CANCLE_PAY;
								handler.sendMessage(msg);

							} else if (ELePayState.FAILT == status) { // 支付失败
								Toast.makeText(OnLineCreatingPayActivity.this,
										"支付失败", Toast.LENGTH_SHORT).show();

							} else if (ELePayState.OK == status) { // 支付成功
							// Toast.makeText(OnLineCreatingPayActivity.this,
							// "支付成功", Toast.LENGTH_SHORT)
							// .show();

								Log.i("支付成功了么", "走了这");
								Message msg = new Message();
								msg.arg1 = 2;
								handler.sendMessage(msg);

							} else if (ELePayState.WAITTING == status) { // 支付中

							}
						}
					});
		} else {
			Toast.makeText(OnLineCreatingPayActivity.this, "支付异常",
					Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 获取传递过来的数据
	 */
	private void virtualData() {
		groups.clear();
		children.clear();
		Intent intent = getIntent();
		groups = (List<GroupInfo>) intent.getSerializableExtra(GROUP_TAG);
		children = (Map<String, List<ProductInfo>>) intent
				.getSerializableExtra(CHILD_TAG);
		// 调用方法，进行初始化界面UI数据
		if (children != null && children.size() != 0) {
			useWayDefaultFeright();
			userWayAddDataToUI(children, groups);
		}

	}

	/**
	 * 该方法用来将传递过来的商品进行运费归组处理
	 */
	private void useWayDefaultFeright() {
		// TODO Auto-generated method stub
		if (groups != null && groups.size() != 0 && children != null
				&& children.size() != 0) {
			for (int i = 0; i < groups.size(); i++) {
				List<ProductInfo> list = children.get(groups.get(i).getId());
				// 调用方法，获取该集合内部商品中的运费最大值
				double feright = useWayGetBigFeright(list);
				String fer = feright + "";
				groups.get(i).setmFeright(fer);
			}
		}
	}

	/**
	 * 计算出该集合内部数据中运费最大的商品，并返回运费
	 * 
	 * @param list
	 */
	private double useWayGetBigFeright(List<ProductInfo> list) {
		double feright = 0;

		if (list != null && list.size() != 0) {
			for (int i = 0; i < list.size(); i++) {
				if (!TextUtils.isEmpty(list.get(i).getFrieight())) {
					double d = Double.parseDouble(list.get(i).getFrieight());
					if (d > feright) {
						feright = d;
					}
				}
			}
		} else {
			return feright;
		}

		return feright;
		// TODO Auto-generated method stub

	}

	/**
	 * 根据传入集合数据，来计算运费
	 * 
	 * @param groups2
	 * @return
	 */
	private double useWayCountFeright(List<GroupInfo> groups2) {
		// TODO Auto-generated method stub
		double feright = 0;
		for (int i = 0; i < groups.size(); i++) {
			if (!TextUtils.isEmpty(groups2.get(i).getmFeright())) {
				double f = Double.parseDouble(groups2.get(i).getmFeright());
				feright = feright + f;
			}

		}
		return feright;
	}

	/**
	 * 根据传入的参数，计算商品总价，
	 * 
	 * @param groups2
	 * @param groups2
	 */
	private void userWayAddDataToUI(Map<String, List<ProductInfo>> children2,
			List<GroupInfo> groups2) {
		// TODO Auto-generated method stub
		yunfei = useWayCountFeright(groups2);
		for (int i = 0; i < groups2.size(); i++) {
			GroupInfo group = groups2.get(i);
			List<ProductInfo> childs = children2.get(group.getId());
			for (int j = 0; j < childs.size(); j++) {
				ProductInfo product = childs.get(j);
				// if (product.isChoosed())
				// {
				/**
				 * 
				 */
				totalPrice += product.getPrice() * product.getCount();
				mGoodscount = mGoodscount + product.getCount();
				// }
			}
		}
		String mPrices = changeDoubleToString(totalPrice);
		mTotalPrice.setText("￥" + Utils.parseTwoNumber(mPrices));
		if (ZIQU_OR_SONGHHUO.equals("0")) {
			mYunfei.setText("￥0.00");
			String mPrice = changeDoubleToString(totalPrice);
			mPayPrice.setText("￥" + Utils.parseTwoNumber(mPrice));
			mGoodsTotalPrice.setText("￥" + Utils.parseTwoNumber(mPrice));
			mYunfeiShuoming.setVisibility(View.GONE);
		} else {
			mYunfei.setText("￥" + Utils.parseTwoNumber(yunfei + ""));
			double mPricess = totalPrice + yunfei;
			String mPricesss = changeDoubleToString(mPricess);
			mPayPrice.setText("￥" + Utils.parseTwoNumber(mPricesss));
			mGoodsTotalPrice.setText("￥" + Utils.parseTwoNumber(mPricesss));
			mYunfeiShuoming.setVisibility(View.VISIBLE);
			mYunfeiShuoming.setText("(含运费)");
		}
		mGoodsNum.setText("共" + mGoodscount + "件商品");
		mZiTiAddress.setText(GlobalParams.Online_Address);

	}

	/**
	 * 将数据加载进入
	 */
	private void initEvents() {
		selva = new ShopcartExpandableListViewAdapter(false, groups, children,
				this);
		selva.setCheckInterface(this);// 关键步骤1,设置复选框接口
		selva.setModifyCountInterface(this);// 关键步骤2,设置数量增减接口
		mExListview.setAdapter(selva);

		for (int i = 0; i < selva.getGroupCount(); i++) {
			mExListview.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
		}
	}

	/**
	 * 给选定支付方式的checkbox添加监听
	 * 
	 * @2016-2-24下午2:45:58
	 */
	private void initCheckBoxClick() {
		// TODO Auto-generated method stub
		mRadioZiqu.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				ZIQU_OR_SONGHHUO = "0";
				ZIQU_OR_SONG = "ziqu";
				IS_QUHUO = isChecked;
				IS_SONGHUO = !isChecked;
				mRadioSonghuo.setChecked(!isChecked);
				Goods_Recipet_TAG = ZIQU_TAG;
				mCustomListview.setVisibility(View.VISIBLE);
				mSonghuoAddress.setVisibility(View.GONE);
				if (is_use_coupone) {
					// 说明是选择了优惠券，所以计算金额的时候要减去优惠信息
					double youhuiquan = Double.parseDouble(mUsebean
							.getCouponPrice());
					double payben = totalPrice - youhuiquan;
					String prives = changeDoubleToString(payben);
					mPayPrice.setText(Utils.parseTwoNumber(prives) + "");
					mGoodsTotalPrice.setText(Utils.parseTwoNumber(prives) + "");
				} else {
					String mPrice = changeDoubleToString(totalPrice);
					mYunfei.setText("￥0.00");
					mPayPrice.setText("￥" + Utils.parseTwoNumber(mPrice));
					mGoodsTotalPrice.setText("￥" + Utils.parseTwoNumber(mPrice));
					mYunfeiShuoming.setVisibility(View.GONE);
				}

			}
		});
		mRadioSonghuo.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				ZIQU_OR_SONGHHUO = "1";
				ZIQU_OR_SONG = "songhuo";
				IS_SONGHUO = isChecked;
				IS_QUHUO = !isChecked;
				mRadioZiqu.setChecked(!isChecked);
				Goods_Recipet_TAG = SHOUHUO_TAG;
				mCustomListview.setVisibility(View.GONE);
				mSonghuoAddress.setVisibility(View.VISIBLE);
				mYunfeiShuoming.setText("(含运费)");
				if (!is_use_coupone) {
					mYunfei.setText("￥" + yunfei + "");
					double ssss = totalPrice + yunfei;
					String sssfdsf = changeDoubleToString(ssss);
					mPayPrice.setText("￥" + Utils.parseTwoNumber(sssfdsf));
					mGoodsTotalPrice.setText("￥"
							+ Utils.parseTwoNumber(sssfdsf));
					mYunfeiShuoming.setVisibility(View.VISIBLE);
				} else {
					if (mUsebean != null) {
						if (!TextUtils.isEmpty(mUsebean.getCouponPrice())) {
							double isusecon = Double.parseDouble(mUsebean
									.getCouponPrice());
							double pricesss = (totalPrice + yunfei);
							double pays = pricesss - isusecon;
							String privesss = changeDoubleToString(pays);
							mPayPrice.setText(Utils.parseTwoNumber(privesss)
									+ "");
							// String
							// privesssdff=changeDoubleToString(totalPrice);
							mGoodsTotalPrice.setText(Utils
									.parseTwoNumber(privesss) + "");
						} else {
							String privesss = changeDoubleToString(totalPrice);
							mPayPrice.setText(Utils.parseTwoNumber(privesss)
									+ "");
							mGoodsTotalPrice.setText(""
									+ Utils.parseTwoNumber(privesss));
						}
					} else {
						String privesss = changeDoubleToString(totalPrice);
						mPayPrice.setText(Utils.parseTwoNumber(privesss) + "");
						mGoodsTotalPrice.setText(Utils.parseTwoNumber(privesss)
								+ "");
					}

				}
			}
		});
	}

	/**
	 * 将传入的double数据保留有两位转换成字符串返回
	 * 
	 * @param d
	 * @return
	 */
	public String changeDoubleToString(double d) {
		DecimalFormat df = new DecimalFormat("#.##");
		String string = df.format(d);
		return string;
	}

	/**
	 * 初始化view控件
	 */
	private void initviews() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		mZiquName=(EditText) findViewById(R.id.online_shouhuodizhi_shoujihao_num_ziqu_name);
		mSonghuoName=(EditText) findViewById(R.id.online_shouhuodizhi_shoujihao_num_name);
		mZiquPhone = (EditText) findViewById(R.id.online_shouhuodizhi_shoujihao_num_ziqu);
		mCustomListview = (RelativeLayout) findViewById(R.id.online_layout_zitidizhi);
		mLayout_progress = (RelativeLayout) findViewById(R.id.layout_progress_layout_online);
		mLayout_progress.setOnClickListener(this);
		mLayout_guide = (RelativeLayout) findViewById(R.id.layout_online_yindaoyebufen);
		mLayout_root = (RelativeLayout) findViewById(R.id.layoout_roots);
		mLayout_bottomss = (LinearLayout) findViewById(R.id.layout_online_dibuxuyaojjiandiaodebufen);
		mLayout_bottom = (RelativeLayout) findViewById(R.id.layout_layout_onlien_bottom);
		mLayout_topss = (RelativeLayout) findViewById(R.id.online_order_layout_top_height);
		mScrollview = (ScrollView) findViewById(R.id.online_scrollView1);
		mLayout_yindaoye = (RelativeLayout) findViewById(R.id.layout_xianshibufentubiao);
		mYunfeiShuoming = (TextView) findViewById(R.id.online_order_pay_yunfeishuoming);
		mZiTiAddress = (TextView) findViewById(R.id.online_shouhuodizhi_zitidizhi);
		mTuikuan_songhuo = (TextView) findViewById(R.id.zaixiancanyin_tuikuanshuoming_zhifuyesssdd);
		mTuikuan_songhuo.setOnClickListener(this);
		mTuikuan_ziqu = (TextView) findViewById(R.id.zaixiancanyin_tuikuanshuoming_zhifuye);
		mTuikuan_ziqu.setOnClickListener(this);
		mPhoneNum = (EditText) findViewById(R.id.online_shouhuodizhi_shoujihao_num);
		mZuoweihao = (EditText) findViewById(R.id.online_order_shouhuodizhi);
		mYunfei = (TextView) findViewById(R.id.online_order_goodsyunfei);
		mCouponName = (TextView) findViewById(R.id.online_order_youhuiquan_zanwu);
		mLayout_choiseCoupon = (RelativeLayout) findViewById(R.id.layout_canyin_dingdan_shiyongyouhui);
		mLayout_choiseCoupon.setOnClickListener(this);
		mTotalPrice = (TextView) findViewById(R.id.online_order_goodsprice);
		mBack = (ImageView) findViewById(R.id.order_title_left_iv);
		mBack.setOnClickListener(this);
		mExListview = (ExpandableListView) findViewById(R.id.online_order_exListView);
		mJifenShuE = (TextView) findViewById(R.id.online_order_jifenshue);
		mKeyongJF = (TextView) findViewById(R.id.online_order_keyongjifen);
		mKediJinE = (TextView) findViewById(R.id.online_order_kedikoujines);
		mUserJifenSwitch = (WiperSwitch) findViewById(R.id.online_order_shifoushiyongjifen);
		// 设置初始状态为false
		mUserJifenSwitch.setChecked(false);
		// 设置监听
		mUserJifenSwitch.setOnChangedListener(this);
		mBeiZhuInfo = (EditText) findViewById(R.id.online_order_beizhushurukuang);
		mBeiZhuInfo.addTextChangedListener(new EditLengthOnclistener(10,
				mBeiZhuInfo, OnLineCreatingPayActivity.this));
		mGoodsNum = (TextView) findViewById(R.id.online_order_gongduoshaojian);
		mGoodsTotalPrice = (TextView) findViewById(R.id.online_order_shangpinzongjia);
		mPayPrice = (TextView) findViewById(R.id.online_order_pay_price);
		mPay = (TextView) findViewById(R.id.online_order_pay_pay);
		mPay.setOnClickListener(this);
		mRadioZiqu = (CheckBox) findViewById(R.id.online_order_songhuofangshi_ziqu);
		mRadioSonghuo = (CheckBox) findViewById(R.id.online_order_songhuofangshi_songhuo);
		mSonghuoAddress = (LinearLayout) findViewById(R.id.online_layout_songhuodizhi);
		mLayoutChoiseZuowei = (RelativeLayout) findViewById(R.id.online_layout_xuanzuo);
//		mLayoutChoiseZuowei.setOnClickListener(this);

	}

	private void initGuide(final RelativeLayout mLayout_guide2) {
		// TODO Auto-generated method stub
		final SharedPreferences preferences = getSharedPreferences(
				SHAREDPREFERENCES_ONLINE, MODE_PRIVATE);
		// 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
		boolean isFirstIn = preferences.getBoolean("isFirstIn_online", true);
		if (isFirstIn) {
			mScrollview.post(new Runnable() {
				public void run() {
					mScrollview.fullScroll(ScrollView.FOCUS_DOWN);
				}
			});
			mLayout_guide2.setVisibility(View.VISIBLE);
			mLayout_guide2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					preferences.edit().putBoolean("isFirstIn_online", false)
							.commit();
					mLayout_guide2.setVisibility(View.GONE);
				}
			});
		} else {
			mLayout_guide2.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.order_title_left_iv:
			finish();
			break;
		case R.id.layout_canyin_dingdan_shiyongyouhui:
			Intent intent = new Intent(OnLineCreatingPayActivity.this,
					UseCounponActivity.class);
			intent.putExtra("price", totalPrice + "");
			intent.putExtra("tag", "online");
			startActivityForResult(intent, RESCODE_TAG);
			break;
		case R.id.online_order_pay_pay:
			MobclickAgent.onEvent(OnLineCreatingPayActivity.this, "pay");
			// 先判断用户是否登录
			final String userid = GlobalParams.USER_ID;
			if (userid != null && !TextUtils.isEmpty(userid)) {
				// 判断是否选择收货方式
				if (IS_QUHUO || IS_SONGHUO) {
					// 再判断是自取还是送货
					if (IS_QUHUO) {
						// 说明是自取
						// 判断自取地址是否为空
						if (!TextUtils.isEmpty(mZiTiAddress.getText()
								.toString())) {
							if (!TextUtils.isEmpty(mZiquPhone.getText()
									.toString())) {
								if(!TextUtils.isEmpty(mZiquName.getText().toString())&&!mZiquName.getText().toString().contains("请输入")){
									// 判断手机号
									GlobalParams.ORDER_ZIQU_ADDRESS = mZiTiAddress
											.getText().toString();
									GlobalParams.ORDER_ZIQU_PHONE = mZiquPhone
											.getText().toString();
									GlobalParams.ORDER_ZIQU_NAME=mZiquName.getText().toString();
									String phone = mZiquPhone.getText().toString();
									boolean isHefa = com.lesports.stadium.utils.CommonUtils
											.isRegexPhone(phone);
									if (isHefa) {
										mLayout_progress
												.setVisibility(View.VISIBLE);
										if (groups != null && groups.size() != 0
												&& children != null
												&& children.size() != 0) {
											// 调用方法，组成需要的字符串
											String jsonstring = userWayMakeJsonStringss(
													groups, children);
											Log.i("生成的json串是", jsonstring);
											// 调用方法，向服务器提交订单数据03-21 18:38:30.150:
											// I/生成的json串是(16108):
											// {"1571":[{"goodsid":"94185","count":2}]}
											if (jsonstring != null
													&& !TextUtils
															.isEmpty(jsonstring)) {
												String remark = mBeiZhuInfo
														.getText().toString();
												String way = "0";
												String payprice = totalPrice + "";
												String freight = "0";
												String companies = GlobalParams.Online_Name;
												String position = mZiTiAddress
														.getText().toString();
												String telephone = mZiquPhone
														.getText().toString();
												String totalprice = totalPrice + "";
												String name=mZiquName.getText().toString();
//												Log.e("检测参数", "备注是" + remark
//														+ "收货方式是" + way + "商品总价"
//														+ payprice + "运费" + freight
//														+ "商家名称" + companies + "地址"
//														+ position + "电话"
//														+ telephone + "支付总价格"
//														+ totalprice);
												if (is_use_coupone) {
													String youhuixinxi = mUsebean
															.getCouponPrice();
													String youhuijine = mUsebean
															.getMyCouponId();
													double youhuijin = Double
															.parseDouble(youhuixinxi);
													// double
													// totalpeices=totalPrice-youhuijin;
													double totalpeices = totalPrice;
													String zongjine = totalpeices
															+ "";
													Generateorder_songhuo(remark,
															way, payprice, freight,
															companies, position,
															telephone, zongjine,
															jsonstring,
															youhuixinxi, youhuijine,name);
												} else {
													String youhuixinxi = "";
													String youhuiid = "";
													Generateorder_songhuo(remark,
															way, payprice, freight,
															companies, position,
															telephone, totalprice,
															jsonstring,
															youhuixinxi, youhuiid,name);
												}
											}
										}
								}else{
									Toast.makeText(getApplicationContext(), "检查手机号是否输入正确",Toast.LENGTH_SHORT).show();
								}
								} else {
									Toast.makeText(getApplicationContext(), "请输入您的姓名",Toast.LENGTH_SHORT).show();
								}

							} else {
								Toast.makeText(OnLineCreatingPayActivity.this,
										"请输入手机号", 0).show();
							}

						} else {
							Toast.makeText(OnLineCreatingPayActivity.this,
									"自取地址有误", 0).show();
						}
					} else if (IS_SONGHUO) {
						// 说明是送货
						// 先判断座位号是否为空
						if (!TextUtils.isEmpty(mZuoweihao.getText().toString())&&!mZuoweihao.getText().toString().contains("请输入")) {
							// 判断电话号码
							if (!TextUtils.isEmpty(mPhoneNum.getText()
									.toString())
									&& mPhoneNum.getText().toString().length() == 11) {
								if(!TextUtils.isEmpty(mSonghuoName.getText().toString())&&!mSonghuoName.getText().toString().contains("请输入")){
									// 判断手机号
									GlobalParams.ORDER_SONGHUO_ADDRESS = mZuoweihao
											.getText().toString();
									GlobalParams.ORDER_SONGHUO_PHONE = mPhoneNum
											.getText().toString();
									GlobalParams.ORDER_SONGHUO_NAME=mSonghuoName.getText().toString();
									String phone = mPhoneNum.getText().toString();
									boolean isHefa = com.lesports.stadium.utils.CommonUtils
											.isRegexPhone(phone);
									if (isHefa) {
										mLayout_progress
												.setVisibility(View.VISIBLE);
										if (groups != null && groups.size() != 0
												&& children != null
												&& children.size() != 0) {
											// 调用方法，组成需要的字符串
											String jsonstring = userWayMakeJsonStringss(
													groups, children);
											Log.i("生成的json串是", jsonstring);
											// 调用方法，向服务器提交订单数据03-21 18:38:30.150:
											// I/生成的json串是(16108):
											// {"1571":[{"goodsid":"94185","count":2}]}
											if (jsonstring != null
													&& !TextUtils
															.isEmpty(jsonstring)) {
												String remark = mBeiZhuInfo
														.getText().toString();
												String way = "1";
												String payprice = totalPrice + "";
												String freight = yunfei + "";
												String companies = GlobalParams.Online_Name;
												String position = mZuoweihao
														.getText().toString();
												String telephone = mPhoneNum
														.getText().toString();
												String totalprice = (totalPrice + yunfei)
														+ "";
												String name=mSonghuoName.getText().toString();
//												Log.e("检测参数", "备注是" + remark
//														+ "收货方式是" + way + "商品总价"
//														+ payprice + "运费" + freight
//														+ "商家名称" + companies + "地址"
//														+ position + "电话"
//														+ telephone + "支付总价格"
//														+ totalprice);
												if (is_use_coupone) {
													String youhuixinxi = mUsebean
															.getCouponPrice();
													String youhuiid = mUsebean
															.getMyCouponId();
													double youhuijine = Double
															.parseDouble(mUsebean
																	.getCouponPrice());
													// double
													// zongshu=totalPrice-youhuijine+yunfei;
													double zongshu = totalPrice
															+ yunfei;
													String zongshuju = zongshu + "";
													Generateorder_songhuo(remark,
															way, payprice, freight,
															companies, position,
															telephone, zongshuju,
															jsonstring,
															youhuixinxi, youhuiid,name);
												} else {
													String youhuixinxi = "";
													String youhuiid = "";
													Generateorder_songhuo(remark,
															way, payprice, freight,
															companies, position,
															telephone, totalprice,
															jsonstring,
															youhuixinxi, youhuiid,name);
												}

											}

										}
									} else {
										Toast.makeText(
												OnLineCreatingPayActivity.this,
												"请检查手机号是否输入正确", 0).show();
									}
								}else{
									Toast.makeText(getApplicationContext(), "请输入您的姓名",Toast.LENGTH_SHORT).show();
								}

							} else {
								Toast.makeText(OnLineCreatingPayActivity.this,
										"请输入有效的电话号码", 0).show();
							}
						} else {
							Toast.makeText(OnLineCreatingPayActivity.this,
									"请输入座位号", 0).show();
						}
					}
					/**
					 * 04-27 18:33:48.816: E/检测参数(31218): 备注是 收货方式是: 0 商品总价:0.18
					 * 运费:5.0 商家名称:牧雨轩 地址:西区A2020 电话18212345678:支付总价格5.18
					 * 
					 * 04-27 18:36:14.986: I/生成的json串是(31218):
					 * {"1603":[{"goodsid":"94235","count":2}]}
					 */

				} else {
					Toast.makeText(OnLineCreatingPayActivity.this, "请选择收货方式", 0)
							.show();
				}

			} else {
				createDialog();
			}

			break;
//		case R.id.online_layout_xuanzuo:
//			timeSelector.show();
//			break;
		case R.id.zaixiancanyin_tuikuanshuoming_zhifuyesssdd:
			// 送货的退款说明被点击的时候
			createDialog_tuikuan();
			break;
		case R.id.zaixiancanyin_tuikuanshuoming_zhifuye:
			// 自取的退款说明呗点击的时候
			createDialog_tuikuan();
			break;
		case R.id.tuikuanshuoming_tv_confirm_close:
			// 弹出框的确定按钮
			dialog.dismiss();
			break;
		case R.id.layout_progress_layout_online:

			break;

		default:
			break;
		}
	}

	/**
	 * 根据传入集合，来生成支付时候需要用到的 05-19 11:32:22.247: I/生成的json串是(26936):
	 * {"4963":[{"goodsid":"97377376","count":1}],
	 * "4956":[{"goodsid":"97377371","count":1}],
	 * "4959":[{"goodsid":"97377372","count":1}]}
	 * 
	 * @param groups2
	 * @param children2
	 * @return
	 */
	private String userWayMakeJsonStringss(List<GroupInfo> groups2,
			Map<String, List<ProductInfo>> children2) {
		// TODO Auto-generated method stub
		String jsonString = "";
		if (groups2 != null && groups2.size() != 0 && children2 != null
				&& children2.size() != 0) {
			Log.i("组id是多少", groups2.get(0).getId());
			try {
				JSONObject obj = new JSONObject();
				for (int i = 0; i < groups2.size(); i++) {
					List<ProductInfo> list = children2.get(groups2.get(i)
							.getId());
					JSONArray jsonarray = new JSONArray();// json数组，里面包含的内容为pet的所有对象
					for (int j = 0; j < list.size(); j++) {
						JSONObject jsonObj = new JSONObject();// pet对象，json形式
						jsonObj.put("goodsid", list.get(j).getId());// 向pet对象里面添加值
						jsonObj.put("count", list.get(j).getCount());
						jsonarray.put(jsonObj);// 向json数组里面添加pet对象
					}
					obj.put(GlobalParams.ONLINE_STORE_ID, jsonarray);
				}
				jsonString = obj.toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			jsonString = "";
		}
		return jsonString;
	}

	/**
	 * 
	 * @param remark
	 *            备注
	 * @param way
	 *            方式
	 * @param payprice
	 *            商品价格
	 * @param freight
	 *            运费
	 * @param companies
	 *            商家名称
	 * @param position
	 *            收货地址
	 * @param telephone
	 *            电话号码
	 * @param totalprice
	 *            总价格
	 * @param jsonstring
	 *            json字符串 04-27 18:08:51.656: I/生成的json串是(16194):
	 *            {"1603":[{"goodsid":"94235","count":2}]}
	 */
	private void Generateorder_songhuo(String remark, String way,
			String payprice, String freight, String companies, String position,
			String telephone, String totalprice, String jsonstring,
			String youhuixinxi, String couponid,String name) {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("orderType", "0");
		params.put("ordersType", "0");
		params.put("payMent", "1");
		params.put("remark", remark);
		params.put("payStatus", "0");
		params.put("freight", freight);
		params.put("companies", companies);
		params.put("position", position);
		params.put("courier", way);
		params.put("orderAmount", totalprice);
		params.put("details", jsonstring);
		params.put("telePhone", telephone);
		params.put("deliveryId", "");
		params.put("myCouponId", couponid);
		params.put("privilege", youhuixinxi);
		params.put("userName",name);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.MAKE_A_ORDER_NUM, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						Log.i("wxn", "支付失败。。。" + data.getNetResultCode()
								+ ">>>>>" + GlobalParams.SSO_TOKEN);
						if (data == null) {
							// 说明网络获取无数据
						} else {
							Object obj = data.getObject();
							String backdata = obj.toString();
							if (backdata == null && backdata.equals("")) {
							} else {
								Log.i("生成订单",
										"走到这里了么？" + data.getNetResultCode());
								if (data.getNetResultCode() == 0) {
									Message msg = new Message();
									msg.arg1 = GET_PAY_ORDER_INFO;
									msg.obj = backdata;
									handler.sendMessage(msg);
								} else {
									Message msg = new Message();
									msg.arg1 = GET_PAY_ORDER_FAILED;
									handler.sendMessage(msg);
									Log.i("wxn",
											"走到这里了么？" + data.getNetResultCode());
								}

							}
						}
					}

				}, false, false);
	}

	private void createDialog() {
		exitDialog = new CustomDialog(this, new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(OnLineCreatingPayActivity.this,
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
		exitDialog.setRemindMessage("登录之后才能进行支付哦~");
		exitDialog.setRemindTitle("温馨提示");
		exitDialog.show();
	}

	/**
	 * 自定义的退款说明的dialog
	 */
	private void createDialog_tuikuan() {
		// 需要先弹出一个dialog，在该dialog上面来进行选择
		dialog = new Dialog(OnLineCreatingPayActivity.this,
				R.style.Theme_Light_Dialog);
		View dialogView = LayoutInflater.from(OnLineCreatingPayActivity.this)
				.inflate(R.layout.tuikuanshuoming, null);
		// 获得dialog的window窗口
		TextView title = (TextView) dialogView
				.findViewById(R.id.tuikuanshuoming_tv_title);
		TextView message = (TextView) dialogView
				.findViewById(R.id.tuikuanshuoming_tv_remind_message);
		message.setText("1.支付后十分钟之内可以直接申请退款;" + "\r\n"
				+ "2.由于场馆内需要商户提前备餐，在支付完成的十分钟后用户发生退款，需要申请，商户通过后才可以退款。");
		TextView qsure = (TextView) dialogView
				.findViewById(R.id.tuikuanshuoming_tv_confirm_close);
		qsure.setOnClickListener(this);
		Window window = dialog.getWindow();
		// 设置dialog在屏幕底部
		window.setGravity(Gravity.CENTER);
		// 设置dialog弹出时的动画效果，从屏幕底部向上弹出
		// window.setWindowAnimations(R.style.dialogStyle);
		window.getDecorView().setPadding(60, 0, 60, 0);
		// 获得window窗口的属性
		android.view.WindowManager.LayoutParams lp = window.getAttributes();
		// 设置窗口宽度为充满全屏
		lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
		// lp.width =getScreenHeight(OnLineCreatingPayActivity.this)/3;
		// 设置窗口高度为包裹内容
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		// 将设置好的属性set回去
		window.setAttributes(lp);
		// 将自定义布局加载到dialog上
		dialog.setContentView(dialogView);
		dialog.show();
	}

	/**
	 * 获得屏幕高度
	 * 
	 * @param context
	 * @return
	 */
	public int getScreenHeight(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.heightPixels;
	}

	@Override
	public void OnChanged(WiperSwitch wiperSwitch, boolean checkState) {
		// TODO Auto-generated method stub
		switch (wiperSwitch.getId()) {
		case R.id.online_order_shifoushiyongjifen:
			// 是否使用积分
			if (checkState) {
				// 需要
				If_USE_JF = true;
			} else {
				// 不需要
				If_USE_JF = false;
			}
			break;
		}
	}

	@Override
	public void checkGroup(int groupPosition, boolean isChecked) {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkChild(int groupPosition, int childPosition,
			boolean isChecked) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doIncrease(String id, int groupPosition, int childPosition,
			View showCountView, boolean isChecked) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doDecrease(String id, int groupPosition, int childPosition,
			View showCountView, boolean isChecked) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESCODE_TAG:
			// 说明是从购物券界面回调过来的
			if (data != null) {
				mUsebean = (YouHuiBean) data.getSerializableExtra("bean");
				if (mUsebean != null) {
					mCouponName
							.setText("可优惠" + mUsebean.getCouponPrice() + "元");
				} else {
					mCouponName.setText("暂无优惠信息");
				}
				is_use_coupone = true;
				// 判断当前是自取还是送货
				if (IS_QUHUO) {
					// 说明是自取
					double coupon = Double.parseDouble(mUsebean
							.getCouponPrice());
					double payprice = totalPrice - coupon;
					String pricesss = changeDoubleToString(payprice);
					mPayPrice.setText(Utils.parseTwoNumber(pricesss) + "");
					mGoodsTotalPrice.setText(Utils.parseTwoNumber(pricesss)
							+ "");
				} else {
					// 说明是送货
					double coupon = Double.parseDouble(mUsebean
							.getCouponPrice());
					double payprices = totalPrice - coupon;
					double yunfies = yunfei;
					double pay = payprices + yunfies;
					String paypricesss = changeDoubleToString(pay);
					mPayPrice.setText(Utils.parseTwoNumber(paypricesss) + "");
					mGoodsTotalPrice.setText(Utils.parseTwoNumber(paypricesss)
							+ "");
				}
			} else {
				Toast.makeText(OnLineCreatingPayActivity.this, "网络异常", 0)
						.show();
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 用来解析获取的订单数据
	 * 
	 * @param backdata
	 */
	private OrderBean jsonWayGetData(String backdata) {
		// TODO Auto-generated method stub

		Log.i("订单数据开始解析", backdata);
		OrderBean bean = new OrderBean();
		try {
			JSONObject obj = new JSONObject(backdata);
			if (obj.has("resultCode")) {
				bean.setResultCode(obj.getString("resultCode"));
			}
			if (obj.has("ordersType")) {
				bean.setOrdersType(obj.getString("ordersType"));
			}
			if (obj.has("object")) {
				JSONObject objs = obj.getJSONObject("object");
				if (objs.has("orderNumber")) {
					bean.setOrderNumber(objs.getString("orderNumber"));
				}
				if (objs.has("amount")) {
					bean.setAmount(objs.getString("amount"));
				}
				if (objs.has("companies")) {
					bean.setCompanies(objs.getString("companies"));
				}
				if (objs.has("createBy")) {
					bean.setCreateBy(objs.getString("createBy"));
				}
				if (objs.has("createTime")) {
					bean.setCreateTime(objs.getString("createTime"));
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
				if (objs.has("orderType")) {
					bean.setOrderType(objs.getString("orderType"));
				}
				if (objs.has("payMent")) {
					bean.setPayMent(objs.getString("payMent"));
				}
				if (objs.has("payStatus")) {
					bean.setPayStatus(objs.getString("payStatus"));
				}
				if (objs.has("position")) {
					bean.setPosition(objs.getString("position"));
				}
				if (objs.has("remark")) {
					bean.setRemark(objs.getString("remark"));
				}
				if (objs.has("status")) {
					bean.setStatus(objs.getString("status"));
				}
				return bean;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bean;
	}

	/**
	 * 设置座位号
	 * 
	 * @param string
	 */
	public void setAddress(String string) {
		// TODO Auto-generated method stub
		if (string != null && !TextUtils.isEmpty(string)) {
			mZuoweihao.setText(string);
		}
	}

	@Override
	public void onScroll(int scrollY) {
		// TODO Auto-generated method stub
		int height = mScrollview.getHeight();
		int height_top = mLayout_topss.getHeight();
		int height_bottom = mLayout_bottom.getHeight();
		int height_bottoms = mLayout_bottomss.getHeight();
		int height_have = height - (height_bottom + 320 + height_top);
		if (scrollY >= height_bottoms) {
			Log.i("这个时候显示出来么", "是的");
		} else {
			Log.i("这个时候显示出来么", "不是的");
		}
		Log.i("目前的底部是多少", height_bottoms + "");
		Log.i("目前的高度是多少", scrollY + "");
		Log.i("目前这个控件的高度是多少", height + "");
		Log.i("顶部占比是多少", height_top + "");
		Log.i("底部占比是多少", height_bottom + "");
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			mScrllviewHeight = mLayout_yindaoye.getTop();// 获取悬停布局的顶部位置
			// Log.i("dierci当前高度", searchLayoutTop + "");
			// int[] location = new int[2];
			// mLayoutClickReportback.getLocationInWindow(location);
			// int x = location[0];
			// int y = location[1];
			// Log.i("dierici目前的高度是", x + "" + y);
		}
	}

	public static void scrollToBottom(final View scroll, final View inner) {

		Handler mHandler = new Handler();

		mHandler.post(new Runnable() {
			public void run() {
				if (scroll == null || inner == null) {
					return;
				}
				int offset = inner.getMeasuredHeight() - scroll.getHeight();
				if (offset < 0) {
					offset = 0;
				}

				scroll.scrollTo(0, offset);
			}
		});
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
									Log.i("优惠券信息",
											"走到这里了么？" + data.getNetResultCode()
													+ backdata);
									if (data.getNetResultCode() == 0) {
										Message sendMessage = new Message();
										sendMessage.arg1 = GET_CURRENT_USER_COUPON;
										sendMessage.obj = backdata;
										handler.sendMessage(sendMessage);
									} else {
										Message sendMessage = new Message();
										sendMessage.arg1 = GET_CURRENT_USER_COUPON_FAILED;
										handler.sendMessage(sendMessage);
									}

								}
							}

						}
					}
				}, false, false);

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
				if (obj.has("myCouponId")) {
					bean.setMyCouponId(obj.getString("myCouponId"));
				}
				if (obj.has("inusing")) {
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

	/**
	 * 改方法用来判断是否有可用的优惠券
	 * 
	 * @param mCounponList2
	 */
	private void usewayCheckData(List<YouHuiBean> mCounponList2) {
		// TODO Auto-generated method stub
		if (mCounponList2 != null && mCounponList2.size() != 0) {
			List<YouHuiBean> list = new ArrayList<>();
			for (int i = 0; i < mCounponList2.size(); i++) {
				if (mCounponList2.get(i).getStatus().equals("1")
						&& mCounponList2.get(i).getCouponType().equals("1")
						&& mCounponList2.get(i).getInusing().equals("0")) {
					// 说明可用
					if (!TextUtils.isEmpty(mCounponList2.get(i)
							.getCouponCondition())) {
						double price = Double.parseDouble(mCounponList2.get(i)
								.getCouponCondition());
						if (totalPrice >= price) {
							list.add(mCounponList2.get(i));
						}
					}
				}
			}

			//
			if (list != null && list.size() != 0) {
				mCouponName.setText("请选择");
			} else {
				mCouponName.setText("无可用");
			}
		}
	}

	@Override
	protected void onDestroy() {
		instence = null;
		if (handler != null) {
			handler.removeCallbacksAndMessages(null);
		}
		if (groups != null) {
			groups.clear();
			groups = null;
		}
		if (children != null) {
			children.clear();
			children = null;
		}
		if (mCounponList != null) {
			mCounponList.clear();
			mCounponList = null;
		}
		super.onDestroy();
	}

	/**
	 * 处理支付订单信息数据
	 * 
	 * @param backdata
	 */
	private void useWayHandlePayOrderInfo(String backdata) {
		// TODO Auto-generated method stub
		// 调用方法，解析订单数据
		mOrderbean = jsonWayGetData(backdata);
		orderid_s = mOrderbean.getId();
		if (mOrderbean != null) {
			if (mOrderbean.getResultCode() != null
					&& mOrderbean.getResultCode().equals("000")) {
				if (mOrderbean.getOrderNumber() != null) {
					Log.i("订单号是多少", mOrderbean.getOrderNumber());
					UseWayGetPayContent(mOrderbean);
				} else {
					Toast.makeText(OnLineCreatingPayActivity.this, "订单状态异常", 0)
							.show();
				}
			} else {
				mLayout_progress.setVisibility(View.GONE);
				Toast.makeText(OnLineCreatingPayActivity.this, "商品库存不足", 0)
						.show();
			}
		}
	}

	/**
	 * 支付成功的处理方法
	 */
	private void useWayPaySuccessHandle() {
		// TODO Auto-generated method stub
		if (IS_QUHUO) {
			if (groups != null && groups.size() != 0 && children != null
					&& children.size() != 0) {
				Intent intent = new Intent(OnLineCreatingPayActivity.this,
						PaySuccessActivity.class);
				intent.putExtra("group", (Serializable) groups);
				intent.putExtra("child", (Serializable) children);
				// 这里是标记
				intent.putExtra("tag", "ziqu");
				intent.putExtra("type", "canyin");
				intent.putExtra("ziqudizhi", mZiTiAddress.getText().toString());
				intent.putExtra("yunfei", yunfei + "");
				intent.putExtra("price", mPayPrice.getText().toString());
				startActivity(intent);
			}
		} else if (IS_SONGHUO) {
			// Toast.makeText(OnLineCreatingPayActivity.this,"songhuo",Toast.LENGTH_SHORT).show();
			if (groups != null && groups.size() != 0 && children != null
					&& children.size() != 0) {
				Intent intent = new Intent(OnLineCreatingPayActivity.this,
						PaySuccessActivity.class);
				intent.putExtra("group", (Serializable) groups);
				intent.putExtra("child", (Serializable) children);
				// 这里是标记
				intent.putExtra("tag", "songhuo");
				intent.putExtra("type", "canyin");
				intent.putExtra("yunfei", yunfei + "");
				intent.putExtra("price", mPayPrice.getText().toString());
				intent.putExtra("zuoweihao", mZuoweihao.getText().toString());
				intent.putExtra("phone", mPhoneNum.getText().toString());
				startActivity(intent);
			}
		}
	}

	/**
	 * 生成支付参数处理
	 * 
	 * @param jsondatass
	 */
	private void useWayPayDataHandle(String jsondatass) {
		// TODO Auto-generated method stub
		mPaybean = useWayJsonDataPay(jsondatass);
		if (mPaybean != null && !TextUtils.isEmpty(mPaybean.getSign())) {
			// 开始调用方法，进行支付
			mLayout_progress.setVisibility(View.GONE);
			pay(mPaybean);
		}
	}

	/**
	 * 用户取消支付以后的处理方法
	 */
	private void useWayUserCanclePayHandle() {
		// TODO Auto-generated method stub
		String yunfeiss = yunfei + "";
		Intent intent = new Intent(OnLineCreatingPayActivity.this,
				OrderDetailActivity.class);
		intent.putExtra("tag", 0);
		intent.putExtra("tags", "online");
		intent.putExtra("bean", (Serializable) mOrderbean);
		intent.putExtra("yunfeis", yunfeiss);
		intent.putExtra("group", (Serializable) groups);
		intent.putExtra("child", (Serializable) children);
		intent.putExtra("cs", "canyin");
		if (IS_QUHUO) {
			intent.putExtra("zs", "0");
		} else {
			intent.putExtra("zs", "1");
		}
		intent.putExtra("paybean", (Serializable) mPaybean);
		intent.putExtra("child", (Serializable) children);
		startActivity(intent);
	}

	/**
	 * 处理当前用户是否有优惠券数据
	 * 
	 * @param backdatasss
	 */
	private void useWayHandleCurrentUserCoupon(String backdatasss) {
		// TODO Auto-generated method stub
		// 调用方法，进行解析
		mCounponList = useWayJsonData(backdatasss);
		// 调用方法，进行分拣数据
		if (mCounponList != null && mCounponList.size() != 0) {
			// 调用方法，判断有无优惠券可用
			usewayCheckData(mCounponList);
		} else {
			mCouponName.setText("无可用");
		}
	}

}
