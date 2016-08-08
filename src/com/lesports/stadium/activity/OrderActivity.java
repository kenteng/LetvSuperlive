/**
 * 
 */
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

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.Toast;
import com.lesports.stadium.R;
import com.lesports.stadium.adapter.OrderAddressAdapter;
import com.lesports.stadium.adapter.ShopcartExpandableListViewAdapter;
import com.lesports.stadium.adapter.ShopcartExpandableListViewAdapter.CheckInterface;
import com.lesports.stadium.adapter.ShopcartExpandableListViewAdapter.ModifyCountInterface;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.base.BaseActivity;
import com.lesports.stadium.bean.CouponBean;
import com.lesports.stadium.bean.GroupInfo;
import com.lesports.stadium.bean.OrderAddressBean;
import com.lesports.stadium.bean.OrderBean;
import com.lesports.stadium.bean.PayBean;
import com.lesports.stadium.bean.PayParametric;
import com.lesports.stadium.bean.ProductInfo;
import com.lesports.stadium.bean.ShippingAddressBean;
import com.lesports.stadium.bean.YouHuiBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.EditLengthOnclistener;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.InPutTextUitils;
import com.lesports.stadium.utils.PayUtils;
import com.lesports.stadium.utils.TimeSelector;
import com.lesports.stadium.utils.ToastUtil;
import com.lesports.stadium.utils.Utils;
import com.lesports.stadium.view.CustomDialog;
import com.lesports.stadium.view.Mylistview;
import com.lesports.stadium.view.WiperSwitch;
import com.lesports.stadium.view.WiperSwitch.OnChangedListener;
import com.letv.lepaysdk.ELePayState;
import com.letv.lepaysdk.LePayConfig;
import com.letv.lepaysdk.LePay.ILePayCallback;
import com.letv.lepaysdk.view.MProgressDialog;
import com.letv.lepaysdk.LePayApi;
import com.umeng.analytics.MobclickAgent;

/**
 * ***************************************************************
 * 
 * @Desc : 订单确认界面
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

public class OrderActivity extends BaseActivity implements CheckInterface,
		ModifyCountInterface, OnClickListener, OnChangedListener {
	/**
	 * 顶部标题
	 */
	private TextView mTitle;
	/**
	 * 顶部返回按钮
	 */
	private ImageView mBack;
	/**
	 * 环绕你的风是我的魂  擦肩的都是陌路人  大理落不下去的黄昏 下雨天  那孤独的城
	 * 
	 * 青石砖的城墙 关不住的念想 你不再穿的碎花裙 那一年  让人沉醉
	 * 
	 * 再不曾遇到的姑娘  是那个夏天最美的想象   红绿灯闪烁的瞬间  消失在了斑马线的人海
	 * 
	 * 我再也弹不出的音调  是记忆里那一声鸟叫   吉他的灰尘   吹起的时候弄花了我的脸
	 * 
	 * 我有多想，多想     告诉你我忘不了你    
	 * 
	 * 
	 * 
	 * 
	 */
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
	 * 展示收货方式的listview
	 */
	private RelativeLayout mCustomListview;
	/**
	 * 需要支付的价格总计
	 */
	private TextView mPayPrice;
	/**
	 * 未登陆提示按钮
	 */
	private CustomDialog exitDialog;
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
	private RadioButton mRadioZiqu;
	/**
	 * 送货
	 */
	private RadioButton mRadioSonghuo;
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
	 * 用来标示youji 
	 */
	private final String YOUJI_TAG = "youji";
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
	 * 展示收货地址的布局
	 */
	private LinearLayout mLayoutShouhuodizhi;
	/**
	 * 定义变量，用来标示商品总数量
	 */
	private int mGoodscount = 0;
	/**
	 * 点击跳往选择优惠券界面
	 */
	private RelativeLayout mLayout_xuanzeyouhui;
	/**
	 * 显示所选的优惠券的名称的view
	 */
	private TextView mYouhuiquanName;
	private final int RESCODE_TAG = 1;
	/**
	 * 用来展示运费
	 */
	private TextView mYunfei;
	/**
	 * 用来展示座位号
	 */
	private EditText mZuoweihao;
	/***
	 * 手机号
	 */
	private EditText mPhoneNum;
	/**
	 * 本类对象
	 */
	public static OrderActivity instance;
	/**
	 * 座位选择器
	 */
	public TimeSelector timeSelector;
	/**
	 * 用来标记当前选择了自取
	 */
	private boolean IS_ZIQU = true;
	/**
	 * 用来标记当前选择了送货
	 */
	private boolean IS_SONGHUO = true;
	/**
	 * 用来标记当前选择了youji
	 */
	private boolean IS_YOUJI = true;

	/**
	 * 送货的退款说明
	 */
	private TextView mTuikuan_songhuo;
	/**
	 * Dialog dialog
	 */
	private Dialog dialog;
	/**
	 * 该标记用来标记用户选择的是取货还是送货
	 */
	private String ZIQU_OR_SONG = "0";
	/**
	 * 自提地址
	 */
	private TextView mZiTiAddress;
//	/**
//	 * 上个界面传递过来的运费和自取地址
//	 */
//	private String serlerAddress;
	/**
	 * 商品运费
	 */
	private String yunfei;
	/**
	 * 底部含运费多少
	 */
	private TextView mHanyunfei;

	/**
	 * 引导页部分
	 */
	private RelativeLayout mYindaoLayout;
	/**
	 * scrollview
	 */
	private ScrollView mScrollview;
	private final String SHAREDPREFERENCES_ONLINE = "LS_APP_ORDER";
	/**
	 * 进度布局
	 */
	private RelativeLayout mLayout_progress;
	/**
	 * 订单数据对象
	 */
	private OrderBean mOrderbean;
	/**
	 * 支付参数对象
	 */
	private PayBean mPaybean;
	/**
	 * 自取手机号
	 */
	private EditText mZiquPhone;
	/**
	 * 用来标注用户是否使用优惠券
	 */
	private boolean is_use_coupone=false;
	/**
	 * YouHuiBean bean
	 */
	private YouHuiBean mUsebean;
	/**
	 * 优惠券数据集合
	 */
	private List<YouHuiBean> mCounponList;
	/**
	 * 邮寄方式的选择框
	 */
	private RadioButton mCheckboxYouji;
	/**
	 * 邮寄方式名称
	 */
	private TextView mNameyouji;
	/**
	 * 自取方式名称
	 */
	private TextView mNameZiqu;
	/**
	 * 送货方式名称
	 */
	private TextView mNameSonghuo;
	/**
	 * 邮寄收获方式的选择布局
	 */
	private RelativeLayout mLayoutYouji;
	/**
	 * 邮寄地址名称
	 */
	private TextView mYoujiWayName;
	/**
	 * 邮寄地址电话
	 */
	private TextView mYoujiWayPhone;
	/**
	 * 邮寄地址
	 */
	private TextView mYoujiWayAddress;
	/**
	 * 送货方式地址选择group
	 */
	private RadioGroup mRadioGroup;
	/**
	 * 地址id
	 */
	private String deliveryId;
	/**
	 * handle里面需要用到的标记
	 */
	private final int GET_PAY_ORDER_INFO = 1;// 生成订单
	private final int GET_PAY_ORDER_FAILED = 111;// 生成订单失败
	private final int PAY_SUCCESS_HANDLE = 2;// 支付成功处理
	private final int GET_PAY_DATA = 4;// 生成支付参数
	private final int GET_PAY_DATA_FAILED = 444;// 生成支付参数失败处理
	private final int USER_CANCLE_PAY = 232;// 用户取消支付
	private final int GET_CURRENT_USER_COUPON = 134;// 获取用户优惠券成功
	private final int GET_CURRENT_USER_COUPON_FAILED = 135;// 优惠券失败
	private String addressId;
	/**
	 * 地址实体类
	 */
	private ShippingAddressBean mSelectBean;
	/**
	 * 是否加载默认地址
	 */
	private boolean isDefault = true;
	/**
	 * 自取的收货人姓名
	 */
	private EditText mZiquName;
	/**
	 * 送货的收货人姓名
	 */
	private EditText mSonghuoName;
	/**
	 * 自取说明
	 */
	private TextView mZiquRemark;
	/**
	 * 自取说明
	 */
	private String mZiqushuoming;
	/**
	 * 处理网络数据的handle
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case GET_PAY_ORDER_INFO:
				String backdata = (String) msg.obj;
				if (backdata != null && !TextUtils.isEmpty(backdata)) {
					useWayHandleMakaOrder(backdata);
				}
				break;
			case GET_PAY_ORDER_FAILED:
				mLayout_progress.setVisibility(View.GONE);
				Toast.makeText(OrderActivity.this, "订单生成失败", 0).show();
				break;
			case PAY_SUCCESS_HANDLE:
				useWayHandlePaySuccess();
				// 支付成功的结果处理
				break;
			case USER_CANCLE_PAY:
				// 订单取消的时候，需要依旧进入订单详情页面来进行支付
				useWayHandleCanclePay();
				break;
			case GET_PAY_DATA:
				String jsondatass = (String) msg.obj;
				if (jsondatass != null && !TextUtils.isEmpty(jsondatass)) {
					mPaybean = useWayJsonDataPay(jsondatass);
					if (mPaybean != null
							&& !TextUtils.isEmpty(mPaybean.getSign())) {
						mLayout_progress.setVisibility(View.GONE);
						// 开始调用方法，进行支付
						pay(mPaybean);
					}
				}
				break;
			case GET_PAY_DATA_FAILED:
				mLayout_progress.setVisibility(View.GONE);
				Toast.makeText(OrderActivity.this, "订单生成失败",Toast.LENGTH_SHORT).show();
				break;
			case GET_CURRENT_USER_COUPON:
				//获取优惠券数据
				String backdatasss=(String) msg.obj;
				if(!TextUtils.isEmpty(backdatasss)){
					useWayHandleCurrentUserCoupon(backdatasss);
				}
				break;
			case GET_CURRENT_USER_COUPON_FAILED:
				mYouhuiquanName.setText("无可用");
				break;
			case 222:
				String backdatassss = (String) msg.obj;
				if (!TextUtils.isEmpty(backdatassss)) {
					useWayHandleDefaultAddress(backdatassss);
				}
				break;

			default:
				break;
			}
		}
	};
	/**
	 * (non-Javadoc)
	 * @see com.jzkj.lstv.base.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.activity_order);
		MobclickAgent.onEvent(OrderActivity.this,"SelectFood");
		initviews();
//		initFirstView();//第三版，去掉引导页，不要了
		virtualData();
		//调用方法，来计算本次订单中的商品都支持哪种收获方式
		useCountWay();
		calculate();
		coulentjifen();
		initEvents();
		initCheckBoxClick();
		initZuoweiChoise();
		//调用方法，判断优惠券是否可用
		useWayGetData();
	}
	/**
	 * 根据现在读取出来的商品数据，来进行计算本次订单中的商品支持哪些送货方式
	 */
	private void useCountWay() {
		// TODO Auto-generated method stub
		if(groups!=null&&groups.size()>0&&children!=null&&children.size()>0){
			List<String> listway=new ArrayList<>();
			for(int i=0;i<groups.size();i++){
					String key=groups.get(i).getId();
					List<ProductInfo> list=children.get(key);
					for(int j=0;j<list.size();j++){
						listway.add(list.get(j).getSendWay());
					}
			}
			//取出集合中的元素，将其以“，”进行分割，然后计算个数
			List<String[]> listcount=new ArrayList<>();
			for(int l=0;l<listway.size();l++){
				String sendway=listway.get(l);
				String[] sendways=sendway.split(",");
				listcount.add(sendways);
			}
			//计算集合中所有数组内的元素的交集
			if(listcount!=null&&listcount.size()>0){
				List<String> liststrings=useWayCoount(listcount.get(0));
				for(int i=1;i<listcount.size();i++){
					List<String> liststringss=useWayCoount(listcount.get(i));
					//计算两个集合内的交集
					liststrings.retainAll(liststringss);
				}
				//循环结束，拿到最终交集结果集
				useWayInitSendWay(liststrings);
			}
		}else{
			//说明为空，则在这里只展示出来自取的收获方式即可也就是不做任何操作
		}
	}
	/**
	 * 根据所取到的商品配送方式的交集，来配置当前到底需要显示那些收获方式
	 * @param liststrings
	 */
	private void useWayInitSendWay(List<String> liststrings) {
		// TODO Auto-generated method stub
		if(liststrings!=null&&liststrings.size()>0){
			for(int i=0;i<liststrings.size();i++){
				if(liststrings.get(i).equals("0")){
					//说明自取存在，由于默认存在自取，所以这里先不做什么处理
				}else if(liststrings.get(i).equals("1")){
					//说明送货也存在
					mRadioSonghuo.setVisibility(View.VISIBLE);
					mNameSonghuo.setVisibility(View.VISIBLE);
				}else if(liststrings.get(i).equals("2")){
					//说明是邮寄
					if (isDefault)
						requestDefaultAddress();
					mCheckboxYouji.setVisibility(View.VISIBLE);
					mNameyouji.setVisibility(View.VISIBLE);
				}
			}
		}
	}
	/**
	 * 将数组转换成集合，以便于数据交集的计算
	 * @param strings
	 * @return
	 */
	private List<String> useWayCoount(String[] strings) {
		// TODO Auto-generated method stub
		List<String> list=new ArrayList<>();
		for(int i=0;i<strings.length;i++){
			list.add(strings[i]);
		}
		return list;
	}
	/**
	 * 网络获取优惠券数据
	 */
	private void useWayGetData() {
		// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			Map<String, String> params = new HashMap<String, String>();
			params.put("couponType",""); 
			params.put("status",""); 
			GetDataFromInternet.getInStance().interViewNet(
					ConstantValue.YOUHUIQUAN_LIST, params, new GetDatas() {

						@Override
						public void getServerData(BackData data) {
							if(handler==null)
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
										Log.i("优惠券信息",
												"走到这里了么？"+ data.getNetResultCode()
														+ backdata);
										if(data.getNetResultCode()==0){
											Message sendMessage = new Message();
											sendMessage.arg1 = GET_CURRENT_USER_COUPON;
											sendMessage.obj = backdata;
											handler.sendMessage(sendMessage);
										}else{
											Message sendMessage = new Message();
											sendMessage.arg1 = GET_CURRENT_USER_COUPON_FAILED;
											handler.sendMessage(sendMessage);
										}
											
									}
								}

							}
						}
					}, false,false);

	}

	/**
	 * 用来第一次添加的时候调用这个方法
	 */
	private void initFirstView() {
		// TODO Auto-generated method stub
		final SharedPreferences preferences = getSharedPreferences(
				SHAREDPREFERENCES_ONLINE, MODE_PRIVATE);
		// 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
		boolean isFirstIn = preferences.getBoolean("isFirstIn_order", true);
		if (isFirstIn) {
			mScrollview.post(new Runnable() {
				public void run() {
					mScrollview.fullScroll(ScrollView.FOCUS_DOWN);
				}
			});
			mYindaoLayout.setVisibility(View.VISIBLE);
			mYindaoLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					preferences.edit().putBoolean("isFirstIn_order", false)
							.commit();
					mYindaoLayout.setVisibility(View.GONE);
				}
			});
		} else {
			mYindaoLayout.setVisibility(View.GONE);
		}
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
			String product_id = null;
			String product_name = null;
			String product_desc = null;
			String product_urls = null;
			if (groups != null && groups.size() != 0 && children != null
					&& children.size() != 0) {
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
						if(handler==null)
							return;
						if (data == null) {
							// 说明网络获取无数据
						} else {
							Object obj = data.getObject();
							String backdata = obj.toString();
							if (backdata == null && backdata.equals("")) {
							} else {
								Log.i("生成支付订单",
										"走到这里了么？" + data.getNetResultCode());
								if (data.getNetResultCode() == 0) {
									Message msg = new Message();
									msg.arg1 = GET_PAY_DATA;
									msg.obj = backdata;
									handler.sendMessage(msg);
								} else {
									Message msg = new Message();
									msg.arg1 = GET_PAY_DATA_FAILED;
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
		timeSelector = new TimeSelector(OrderActivity.this,
				new TimeSelector.ResultHandler() {
					@Override
					public void handle(String time) {
						Toast.makeText(getApplicationContext(), time,
								Toast.LENGTH_LONG).show();
					}
				}, "2015-10-27 09:33", "2016-11-29 21:54", instance, true,
				"order");

		timeSelector.setScrollUnit(TimeSelector.SCROLLTYPE.HOUR,
				TimeSelector.SCROLLTYPE.MINUTE);
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

	/**
	 * 根据积分换算规则和用户所选商品总价来计算用户可得多少积分
	 * 
	 * @2016-2-25下午4:30:53
	 */
	private void coulentjifen() {
		// TODO Auto-generated method stub
		int jf_num = (int) (totalPrice * JIFEN_SPEC);
		mJifenShuE.setText(jf_num + "");

	}

	private void pay(PayBean paybean) {
		Log.i("221", "pay start");
		// Toast.makeText(OrderActivity.this, "结果", Toast.LENGTH_SHORT).show();
		if (paybean != null && !TextUtils.isEmpty(paybean.getSign())) {
			PayParametric parameters = new PayParametric();
			parameters.setVersion(paybean.getVersion());
			parameters.setSign(paybean.getSign());
			parameters.setService(paybean.getService());
			parameters.setUser_id(paybean.getUser_id());
			parameters.setMerchant_business_id(paybean
					.getMerchant_business_id());
			parameters.setUser_name(paybean.getUser_name());
			parameters.setNotify_url(paybean.getNotify_url());
			parameters.setMerchant_no(paybean.getMerchant_no());
			parameters.setOut_trade_no(paybean.getOut_trade_no());
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
			LePayApi.initConfig(OrderActivity.this, config);
			LePayApi.doPay(OrderActivity.this, param, new ILePayCallback() {
				@Override
				public void payResult(ELePayState status, String message) {
					// Toast.makeText(OrderActivity.this,
					// "结果:" + status + "|" + message, Toast.LENGTH_SHORT)
					// .show();
					if (ELePayState.CANCEL == status) { // 支付取消
//						Toast.makeText(OrderActivity.this, "取消支付",
//								Toast.LENGTH_SHORT).show();
						Message msg = new Message();
						msg.arg1 = 232;
						handler.sendMessage(msg);

					} else if (ELePayState.FAILT == status) { // 支付失败
						Toast.makeText(OrderActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();

					} else if (ELePayState.OK == status) { // 支付成功
//						Toast.makeText(OrderActivity.this, "支付成功",
//								Toast.LENGTH_SHORT).show();
						Message msg = new Message();
						msg.arg1 = 2;
						handler.sendMessage(msg);

					} else if (ELePayState.WAITTING == status) { // 支付中
					}
				}
			});
		} else {  
			Toast.makeText(OrderActivity.this, "支付异常", Toast.LENGTH_SHORT)
					.show();
		}

	}

	/**
	 * * 统计操作<br>
	 * 1.先清空全局计数器<br>
	 * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作<br>
	 * 
	 * @2016-2-23下午2:15:18
	 */
	private void calculate() {
		totalPrice = 0.00;
		mGoodscount = 0;
		for (int i = 0; i < groups.size(); i++) {
			GroupInfo group = groups.get(i);
			List<ProductInfo> childs = children.get(group.getId());
			for (int j = 0; j < childs.size(); j++) {
				ProductInfo product = childs.get(j);
				totalPrice += product.getPrice() * product.getCount();
				mGoodscount = mGoodscount + product.getCount();
				// }
			}
		}
		String price = changeDoubleToString(totalPrice);
		mTotalPrice.setText("" + Utils.parseTwoNumber(price));
		if (ZIQU_OR_SONG.equals("0")) {
			mPayPrice.setText("" + (Utils.parseTwoNumber(price)));
			mGoodsTotalPrice.setText("" + (Utils.parseTwoNumber(price)));
		} else {
			double pricess = (totalPrice + Double.parseDouble(yunfei));
			String prices = changeDoubleToString(pricess);
			mGoodsTotalPrice.setText("" + Utils.parseTwoNumber(prices));
			mPayPrice.setText("" +Utils.parseTwoNumber(prices));
		}
		mGoodsNum.setText("共" + mGoodscount + "件商品");
	}

	/**
	 * 给选定支付方式的checkbox添加监听
	 * 
	 * @2016-2-24下午2:45:58
	 */
	private void initCheckBoxClick() {
		// TODO Auto-generated method stub
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.order_songhuofangshi_ziqu:
					ZIQU_OR_SONG = "0";
					IS_ZIQU = true;
					IS_SONGHUO =false;
					IS_YOUJI=false;
					Goods_Recipet_TAG = ZIQU_TAG;
					mLayoutYouji.setVisibility(View.GONE);
					mCustomListview.setVisibility(View.VISIBLE);
					mLayoutShouhuodizhi.setVisibility(View.GONE);
					mYunfei.setText("0.00");
					mHanyunfei.setVisibility(View.GONE);
					// mHanyunfei.setText("(含运费￥--元)");
					if(is_use_coupone){
						//说明是选择了优惠券，所以计算金额的时候要减去优惠信息
						double youhuiquan=Double.parseDouble(mUsebean.getCouponPrice());
						double payben=totalPrice-youhuiquan;
						String prives = changeDoubleToString(payben);
						mPayPrice.setText("" +Utils.parseTwoNumber(prives));
					}else{
						//说明没有计算优惠券
						String prive = changeDoubleToString(totalPrice);
						mPayPrice.setText("" + Utils.parseTwoNumber(prive));
						mGoodsTotalPrice.setText("" + Utils.parseTwoNumber(prive));
					}
					
					// mYunfeiShuoming.setText("(含运费￥--元)");
					break;
				case R.id.order_songhuofangshi_songhuo:
					ZIQU_OR_SONG = "1";
					IS_SONGHUO = true;
					IS_ZIQU = false;
					IS_YOUJI=false;
//					mRadioZiqu.setChecked(false);
//					mCheckboxYouji.setChecked(false);
					Goods_Recipet_TAG = SHOUHUO_TAG;
					mCustomListview.setVisibility(View.GONE);
					mLayoutShouhuodizhi.setVisibility(View.VISIBLE);
					mLayoutYouji.setVisibility(View.GONE);
					double strings=Utils.parseTwoNumber(yunfei);
					mYunfei.setText(strings+"");
					mHanyunfei.setVisibility(View.VISIBLE);
					mHanyunfei.setText("(含运费)");
					if(is_use_coupone){
						double isusecon=Double.parseDouble(mUsebean.getCouponPrice());
						double pricesss = (totalPrice + Double.parseDouble(yunfei));
						double pays=pricesss-isusecon;
						String privesss = changeDoubleToString(pays);
						mPayPrice.setText(Utils.parseTwoNumber(privesss)+"");
						mGoodsTotalPrice.setText(Utils.parseTwoNumber(privesss)+"");
					}else{
						double pricesss = (totalPrice + Double.parseDouble(yunfei));
						String privesss = changeDoubleToString(pricesss);
						mPayPrice.setText("" + Utils.parseTwoNumber(privesss));
						mGoodsTotalPrice.setText(Utils.parseTwoNumber(privesss)+"");
					}
					
					break;
				case R.id.order_songhuofangshi_youji:
					ZIQU_OR_SONG = "2";
					IS_SONGHUO = false;
					IS_ZIQU = false;
					IS_YOUJI=true;
//					mRadioZiqu.setChecked(false);
//					mRadioSonghuo.setChecked(false);
					Goods_Recipet_TAG = YOUJI_TAG;
					mCustomListview.setVisibility(View.GONE);
					mLayoutShouhuodizhi.setVisibility(View.GONE);
					mLayoutYouji.setVisibility(View.VISIBLE);
					double stringss=Utils.parseTwoNumber(yunfei);
					mYunfei.setText(stringss+"");
					mHanyunfei.setVisibility(View.VISIBLE);
					mHanyunfei.setText("(含运费)");
					if(is_use_coupone){
						double isusecon=Double.parseDouble(mUsebean.getCouponPrice());
						double pricesss = (totalPrice + Double.parseDouble(yunfei));
						double pays=pricesss-isusecon;
						String privesss = changeDoubleToString(pays);
						mPayPrice.setText(Utils.parseTwoNumber(privesss)+"");
						mGoodsTotalPrice.setText(Utils.parseTwoNumber(privesss)+"");
					}else{
						double pricesss = (totalPrice + Double.parseDouble(yunfei));
						String privesss = changeDoubleToString(pricesss);
						mPayPrice.setText("" + Utils.parseTwoNumber(privesss));
						mGoodsTotalPrice.setText(Utils.parseTwoNumber(privesss)+"");
					}
					
					break;

				default:
					break;
				}
			}
		});
	}

	/**
	 * 初始化界面控件
	 * 
	 * @2016-2-24上午11:47:13
	 */
	private void initviews() {
		mZiquRemark=(TextView) findViewById(R.id.order_order_xiadanshuomingsss);
		mZiquRemark.setOnClickListener(this);
		mZiquName=(EditText) findViewById(R.id.order_shouhuodizhi_shoujihao_num_ziqu_name);
		mSonghuoName=(EditText) findViewById(R.id.order_shouhuodizhi_namessss);
		mRadioGroup=(RadioGroup) findViewById(R.id.order_songhuofangshi_radiogroup);
		mCheckboxYouji=(RadioButton) findViewById(R.id.order_songhuofangshi_youji);
		mNameyouji=(TextView) findViewById(R.id.order_youji);
		mNameZiqu=(TextView) findViewById(R.id.order_ziquss);
		mNameSonghuo=(TextView) findViewById(R.id.order_songhuoss);
		mLayoutYouji=(RelativeLayout) findViewById(R.id.new_order_address_choise);
		mLayoutYouji.setOnClickListener(this);
		mYoujiWayName=(TextView) findViewById(R.id.new_order_dingdanzhifu_shouhuoren_name);
		mYoujiWayAddress=(TextView) findViewById(R.id.new_order_dingdanzhifu_shouhuoren_dizhi);
		mYoujiWayPhone=(TextView) findViewById(R.id.new_order_dingdanzhifu_shouhuoren_dianhua);
		mZiquPhone = (EditText) findViewById(R.id.order_shouhuodizhi_shoujihao_num_ziqu);
		mLayout_progress = (RelativeLayout) findViewById(R.id.layout_progress_layout_order);
		mLayout_progress.setVisibility(View.GONE);
		mLayout_progress.setOnClickListener(this);
		mYindaoLayout = (RelativeLayout) findViewById(R.id.layout_layout_order_yindaoye);
		mScrollview = (ScrollView) findViewById(R.id.scrollview_layout_quanju_order);
		mHanyunfei = (TextView) findViewById(R.id.order_pay_yunfeishuoming);
		mZiTiAddress = (TextView) findViewById(R.id.order_shouhuodizhi_zitidizhi);
		mTuikuan_songhuo = (TextView) findViewById(R.id.order_zaixiancanyin_tuikuanshuoming_zhifuyesssdd);
		mTuikuan_songhuo.setOnClickListener(this);
		mYouhuiquanName = (TextView) findViewById(R.id.online_order_youhuiquan_zanwusssssss);
		mLayout_xuanzeyouhui = (RelativeLayout) findViewById(R.id.layout_xuanzeshiyongyouhuiqun);
		mLayout_xuanzeyouhui.setOnClickListener(this);
		mTotalPrice = (TextView) findViewById(R.id.order_goodsprice);
		mBack = (ImageView) findViewById(R.id.order_title_left_iv);
		mBack.setVisibility(View.VISIBLE);
		mBack.setOnClickListener(this);
		mExListview = (ExpandableListView) findViewById(R.id.order_exListView);
		mJifenShuE = (TextView) findViewById(R.id.order_jifenshue);
		mKeyongJF = (TextView) findViewById(R.id.order_keyongjifen);
		mKediJinE = (TextView) findViewById(R.id.order_kedikoujines);
		mUserJifenSwitch = (WiperSwitch) findViewById(R.id.order_shifoushiyongjifen);
		// 设置初始状态为false
		mUserJifenSwitch.setChecked(false);
		// 设置监听
		mUserJifenSwitch.setOnChangedListener(this);
		mBeiZhuInfo = (EditText) findViewById(R.id.order_beizhushurukuang);
		mBeiZhuInfo.addTextChangedListener(new EditLengthOnclistener(10,
				mBeiZhuInfo, OrderActivity.this));
		mGoodsNum = (TextView) findViewById(R.id.order_gongduoshaojian);
		mGoodsTotalPrice = (TextView) findViewById(R.id.order_shangpinzongjia);
		mPayPrice = (TextView) findViewById(R.id.order_pay_price);
		mPay = (TextView) findViewById(R.id.order_pay_pay);
		mPay.setOnClickListener(this);
		mRadioZiqu = (RadioButton) findViewById(R.id.order_songhuofangshi_ziqu);
		mRadioSonghuo = (RadioButton) findViewById(R.id.order_songhuofangshi_songhuo);
		mLayoutShouhuodizhi = (LinearLayout) findViewById(R.id.order_layout_songhuodizhi);
		RelativeLayout layout_xuanzuo = (RelativeLayout) findViewById(R.id.layout_order_xuanzuo);
//		layout_xuanzuo.setOnClickListener(this);
		mCustomListview = (RelativeLayout) findViewById(R.id.order_layout_zitidizhi);
		mYunfei = (TextView) findViewById(R.id.order_goodsyunfei);
		mZuoweihao = (EditText) findViewById(R.id.order_shouhuodizhi);
		mPhoneNum = (EditText) findViewById(R.id.order_shouhuodizhi_shoujihao_num);
	}

	/**
	 * 初始化列表项的适配器对象，并且规定该可拓展的listview以展开的方式来呈现
	 * 
	 * @2016-2-23下午2:09:10
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
		mExListview.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				return true;
			}
		});
	}
	/**
	 * 模拟数据<br>
	 * 遵循适配器的数据列表填充原则，组元素被放在一个List中，对应的组元素下辖的子元素被放在Map中，<br>
	 * 其键是组元素的Id(通常是一个唯一指定组元素身份的值)
	 * 
	 * @2016-2-23下午2:11:20
	 */
	@SuppressWarnings("unchecked")
	private void virtualData() {
		groups.clear();
		children.clear();
		Intent intent = getIntent();
		groups = (List<GroupInfo>) intent.getSerializableExtra(GROUP_TAG);
		children = (Map<String, List<ProductInfo>>) intent
				.getSerializableExtra(CHILD_TAG);
		//调用方法，处理商品运费
		useWayDefaultFeright();
		if(!TextUtils.isEmpty(GlobalParams.Goods_Address)){
			mZiTiAddress.setText(GlobalParams.Goods_Address);
		}else{
			for(int i=0;i<groups.size();i++){
				if(!TextUtils.isEmpty(groups.get(i).getSelerAddress())){
					mZiTiAddress.setText(groups.get(i).getSelerAddress());
					break;
				}
			}
			
		}
		String stri=children.get(groups.get(0).getId()).get(0).getPickup_remark();
		if(!TextUtils.isEmpty(stri)){
			mZiqushuoming=stri;
			GlobalParams.Goods_Remark=stri;
		}
		//调用方法，计算运费
		if(groups!=null){
			yunfei = useWayCountFeright(groups);
		}else{
			yunfei="0.0";
		}
		if (ZIQU_OR_SONG.equals("0")) {
			mYunfei.setText("0.00");
			mHanyunfei.setVisibility(View.GONE);
			// mHanyunfei.setText("(含运费￥--元)");
		} else {
			mHanyunfei.setVisibility(View.VISIBLE);
			mYunfei.setText(yunfei);
			mHanyunfei.setText("(含运费)");
		}
	}
	/**
	 * 该方法用来将传递过来的商品进行运费归组处理
	 */
	private void useWayDefaultFeright() {
		// TODO Auto-generated method stub
		if(groups!=null&&groups.size()!=0&&children!=null&&children.size()!=0){
			for(int i=0;i<groups.size();i++){
				List<ProductInfo> list=children.get(groups.get(i).getId());
				//调用方法，获取该集合内部商品中的运费最大值
				double feright=useWayGetBigFeright(list);
				String fer=feright+"";
				groups.get(i).setmFeright(fer);
			}
		}
	}

	/**
	 * 计算出该集合内部数据中运费最大的商品，并返回运费
	 * @param list
	 */
	private double useWayGetBigFeright(List<ProductInfo> list) {
		double feright=0;
		
		if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
				if(!TextUtils.isEmpty(list.get(i).getFrieight())){
					double d=Double.parseDouble(list.get(i).getFrieight());
					if(d>feright){
						feright=d;
					}
				}
			}
		}else{
			return feright;
		}
		
		return feright;
		// TODO Auto-generated method stub
		
	}

	/**
	 * 根据传入集合数据，来计算运费
	 * @param groups2
	 * @return
	 */
	private String useWayCountFeright(List<GroupInfo> groups2) {
		// TODO Auto-generated method stub
		double feright = 0;
		for(int i=0;i<groups.size();i++){
			if(!TextUtils.isEmpty(groups2.get(i).getmFeright())){
				double f=Double.parseDouble(groups2.get(i).getmFeright());
				feright=feright+f;
			}
			
		}
		String fer=changeDoubleToString(feright);
		return fer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.order_order_xiadanshuomingsss:
			createDialog_tuikuan(mZiqushuoming);
			break;
		case R.id.order_pay_pay:
			 MobclickAgent.onEvent(OrderActivity.this,"pay");
			// 先判断用户是否登录
			final String userid = GlobalParams.USER_ID;
			if (userid != null && !TextUtils.isEmpty(userid)) {
				// 再判断用户是否选择了取货送货方式
				if (IS_ZIQU || IS_SONGHUO||IS_YOUJI) {
					// 判断是自取还是送货
					if (IS_ZIQU) {
						// 自取
						// 判断有没有取货地址
						if (!TextUtils.isEmpty(mZiTiAddress.getText()
								.toString())) {
							if (!TextUtils.isEmpty(mZiquPhone.getText()
									.toString())) {
								if(!TextUtils.isEmpty(mZiquName.getText().toString())&&!mZiquName.getText().toString().contains("请输入")){
									GlobalParams.ORDER_ZIQU_ADDRESS = mZiTiAddress
											.getText().toString();
									GlobalParams.ORDER_ZIQU_PHONE = mZiquPhone
											.getText().toString();
									GlobalParams.ORDER_ZIQU_NAME=mZiquName.getText().toString();
									// 判断手机号
									String phone = mZiquPhone.getText().toString();
									boolean isHefa = com.lesports.stadium.utils.CommonUtils
											.isRegexPhone(phone);
									if (isHefa){
										mLayout_progress
												.setVisibility(View.VISIBLE);
										Log.i("lwc", "订单支付按钮走了没有");
										if (groups != null && groups.size() != 0
												&& children != null
												&& children.size() != 0) {
											Log.i("组合子分别为", groups.size() + "组"
													+ children.size() + "子");
											List<ProductInfo> list_new = new ArrayList<ProductInfo>();
											for (int i = 0; i < groups.size(); i++) {
												List<ProductInfo> list = children
														.get(groups.get(i).getId());
												for (int j = 0; j < list.size(); j++) {
													if (list.get(j).getSeller() != null) {
													}
												}
												list_new.add(list.get(0));
											}
											if (list_new != null
													&& list_new.size() != 0) {
												// 调用方法，组成需要的字符串
												String jsonstring = userWayMakeJsonStringss(groups,children);
												Log.i("生成的json串是", jsonstring);
												String remark = mBeiZhuInfo
														.getText().toString();
												String way = "0";
												String payprice = totalPrice + "";
												String freight = "0";
												String companies =groups.get(0).getName();
												String position = mZiTiAddress
														.getText().toString();
												String telephone = mZiquPhone
														.getText().toString();
												String totalprice = totalPrice + "";
												String name=mZiquName.getText().toString();
												Log.e("检测参数", "备注是" + remark
														+ "收货方式是" + way + "商品总价"
														+ payprice + "运费" + freight
														+ "商家名称" + companies + "地址"
														+ position + "电话"
														+ telephone + "支付总价格"
														+ totalprice);
												if(is_use_coupone){
													//说明是要使用优惠券
													if(mUsebean!=null){
														double price=Double.parseDouble(mUsebean.getCouponPrice());
														double totalpricess=totalPrice;
//														double totalpricess=totalPrice-price;
														String couponeid=mUsebean.getMyCouponId();
														String total=totalpricess+"";
														String youhuiqian=price+"";
														payprice=changeDoubleToString(Double.parseDouble(payprice));
														total=changeDoubleToString(Double.parseDouble(total));
														String addressid="";
														Generateorder_songhuo(remark, way,
																payprice, freight,
																companies, position,
																telephone, total,
																jsonstring,youhuiqian,couponeid,name,addressid);
													}
												}else{
													String price="";
													String id="";
													payprice=changeDoubleToString(Double.parseDouble(payprice));
													totalprice=changeDoubleToString(Double.parseDouble(totalprice));
													String addressid="";
													Generateorder_songhuo(remark, way,
															payprice, freight,
															companies, position,
															telephone, totalprice,
															jsonstring,price,id,name,addressid);
												}
												// pay();
											}
										}
									} else {
										Toast.makeText(OrderActivity.this,
												"请检查手机号是否输入正确", 0).show();
									}

								}else{
									Toast.makeText(OrderActivity.this,"请输入姓名",Toast.LENGTH_SHORT).show();
								}

							} else {
								Toast.makeText(OrderActivity.this, "请输入手机号", 0)
										.show();
							}
						} else {
							Toast.makeText(OrderActivity.this, "自取地址有误", 0)
									.show();
						}
					} else if (IS_SONGHUO) {
						// 送货
						// 判断是否选择了座位号
						if (!TextUtils.isEmpty(mZuoweihao.getText().toString())&&!mZuoweihao.getText().toString().contains("请输入")) {
							// 判断手机号
							if (!TextUtils.isEmpty(mPhoneNum.getText()
									.toString())) {
								if(!TextUtils.isEmpty(mSonghuoName.getText().toString())&&!mZuoweihao.getText().toString().contains("请输入")){
									GlobalParams.ORDER_SONGHUO_NAME=mSonghuoName.getText().toString();
									GlobalParams.ORDER_SONGHUO_ADDRESS = mZuoweihao
											.getText().toString();
									GlobalParams.ORDER_SONGHUO_PHONE = mPhoneNum
											.getText().toString();
									// 判断手机号
									String phone = mPhoneNum.getText().toString();
									boolean isHefa = com.lesports.stadium.utils.CommonUtils
											.isRegexPhone(phone);
									if (isHefa) {
										mLayout_progress
												.setVisibility(View.VISIBLE);
										if (groups != null && groups.size() != 0
												&& children != null
												&& children.size() != 0) {
												String jsonstring = userWayMakeJsonStringss(groups,children);
												Log.i("生成的json串是", jsonstring);
												String remark = mBeiZhuInfo
														.getText().toString();
												String way = "1";
												String payprice = totalPrice + "";
												String freight = yunfei;
												String companies = groups.get(0).getName();
												String position = mZuoweihao
														.getText().toString();
												String telephone = mPhoneNum
														.getText().toString();
												String name=mSonghuoName.getText().toString();
												double yunfeis = Double
														.parseDouble(yunfei);
												String totalprice = (totalPrice+yunfeis)
														+ "";
												Log.e("检测参数", "备注是" + remark
														+ "收货方式是" + way + "商品总价"
														+ payprice + "运费" + freight
														+ "商家名称" + companies + "地址"
														+ position + "电话"
														+ telephone + "支付总价格"
														+ totalprice);
												if(is_use_coupone){
													//说明是要使用优惠券
													if(mUsebean!=null){
														double price=Double.parseDouble(mUsebean.getCouponPrice());
														double totalpricess=totalPrice;
//														double totalpricess=totalPrice-price;
														String couponeid=mUsebean.getMyCouponId();
														String total=totalpricess+"";
														String pricessss=price+"";
														payprice=changeDoubleToString(Double.parseDouble(payprice));
														total=changeDoubleToString(Double.parseDouble(total));
														String addressid="";
														Generateorder_songhuo(remark, way,
																payprice, freight,
																companies, position,
																telephone,total,
																jsonstring,pricessss,couponeid,name,addressid);
													}
												}else{
													String price="";
													String id="";
													payprice=changeDoubleToString(Double.parseDouble(payprice));
													totalprice=changeDoubleToString(Double.parseDouble(totalprice));
													String addressid="";
													Generateorder_songhuo(remark, way,
															payprice, freight,
															companies, position,
															telephone, totalprice,
															jsonstring,price,id,name,addressid);
												}
										}

									} else {
										Toast.makeText(OrderActivity.this,
												"请检查手机号是否输入正确",Toast.LENGTH_SHORT).show();
									}
								}else{
									Toast.makeText(OrderActivity.this,"请输入姓名",Toast.LENGTH_SHORT).show();
								}
							} else {
								Toast.makeText(OrderActivity.this,
										"请输入有效的电话号码",Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(OrderActivity.this, "请输入座位号",Toast.LENGTH_SHORT)
									.show();
						}
					}else if(IS_YOUJI){
						//说明当前选择了邮寄
						if(!TextUtils.isEmpty(mYoujiWayName.getText().toString())&&!TextUtils.isEmpty(mYoujiWayPhone.getText().toString())){
							GlobalParams.ORDER_YOUJI_ADDRESS=mYoujiWayAddress.getText().toString();
							GlobalParams.ORDER_YOUJI_NAME=mYoujiWayName.getText().toString();
							GlobalParams.ORDER_ZIQU_PHONE=mYoujiWayPhone.getText().toString();
							mLayout_progress
							.setVisibility(View.VISIBLE);
					if (groups != null && groups.size() != 0
							&& children != null
							&& children.size() != 0) {
							String jsonstring = userWayMakeJsonStringss(groups,children);
							Log.i("生成的json串是", jsonstring);
							String remark = mBeiZhuInfo
									.getText().toString();
							String way = "2";
							String payprice = totalPrice + "";
							String freight = yunfei;
							String companies = groups.get(0).getName();
							String position = mZuoweihao
									.getText().toString();
							String telephone = mPhoneNum
									.getText().toString();
							String name=mYoujiWayName.getText().toString();
							double yunfeis = Double
									.parseDouble(yunfei);
							String totalprice = (totalPrice+yunfeis)
									+ "";
							Log.e("检测参数", "备注是" + remark
									+ "收货方式是" + way + "商品总价"
									+ payprice + "运费" + freight
									+ "商家名称" + companies + "地址"
									+ position + "电话"
									+ telephone + "支付总价格"
									+ totalprice);
							if(is_use_coupone){
								//说明是要使用优惠券
								if(mUsebean!=null){
									double price=Double.parseDouble(mUsebean.getCouponPrice());
									double totalpricess=totalPrice;
//									double totalpricess=totalPrice-price;
									String couponeid=mUsebean.getMyCouponId();
									String total=totalpricess+"";
									String pricessss=price+"";
									payprice=changeDoubleToString(Double.parseDouble(payprice));
									total=changeDoubleToString(Double.parseDouble(total));
									Generateorder_songhuo(remark, way,
											payprice, freight,
											companies, position,
											telephone,total,
											jsonstring,pricessss,couponeid,name,addressId);
								}
							}else{
								String price="";
								String id="";
								payprice=changeDoubleToString(Double.parseDouble(payprice));
								totalprice=changeDoubleToString(Double.parseDouble(totalprice));
								Generateorder_songhuo(remark, way,
										payprice, freight,
										companies, position,
										telephone, totalprice,
										jsonstring,price,id,name,addressId);
							}
					}
						}else{
							//说明地址有误
							ToastUtil.show(OrderActivity.this,"请选择邮寄地址");
						}
					}
				} else {
					Toast.makeText(OrderActivity.this, "请选择收货方式",Toast.LENGTH_SHORT).show();
				}
			} else {
				createDialog();
			}
			break;
		case R.id.order_title_left_iv:
			Log.i("lwc", "订单返回按钮走了没有");
			OrderActivity.this.finish();
			break;
		case R.id.layout_xuanzeshiyongyouhuiqun:
			Intent intent = new Intent(OrderActivity.this,
					UseCounponActivity.class);
			intent.putExtra("tag", "goods");
			intent.putExtra("price", totalPrice + "");
			startActivityForResult(intent, RESCODE_TAG);
			break;
//		case R.id.layout_order_xuanzuo:
//			timeSelector.show();
//			break;
		case R.id.tuikuanshuoming_tv_confirm_close:
			// 弹出框的确定按钮
			dialog.dismiss();
			break;
		case R.id.layout_progress_layout_order:
			break;
		case R.id.new_order_address_choise:
			//选择用户收货地址
			Intent intents = new Intent(OrderActivity.this,
					SelectAddressActivity.class);
			intents.putExtra("id", addressId);
			startActivityForResult(intents, 2);
			break;

		default:
			break;
		}
	}

	/**
	 * 根据传入集合，来生成支付时候需要用到的
	 * 05-19 11:32:22.247: I/生成的json串是(26936):
	 *  {"4963":[{"goodsid":"97377376","count":1}],
	 *  "4956":[{"goodsid":"97377371","count":1}],
	 *  "4959":[{"goodsid":"97377372","count":1}]}

	 * @param groups2
	 * @param children2
	 * @return
	 */
	private String userWayMakeJsonStringss(List<GroupInfo> groups2,
			Map<String, List<ProductInfo>> children2) {
		// TODO Auto-generated method stub
		String jsonString="";
		if(groups2!=null&&groups2.size()!=0&&children2!=null&&children2.size()!=0){
			Log.i("组id是多少",groups2.get(0).getId());
			try {
				JSONObject obj=new JSONObject();
				for(int i=0;i<groups2.size();i++){
					List<ProductInfo> list=children2.get(groups2.get(i).getId());
					JSONArray jsonarray = new JSONArray();//json数组，里面包含的内容为pet的所有对象  
					for(int j=0;j<list.size();j++){
						JSONObject jsonObj = new JSONObject();//pet对象，json形式  
				        jsonObj.put("goodsid", list.get(j).getId());//向pet对象里面添加值  
				        jsonObj.put("count", list.get(j).getCount()); 
				        if(list.get(j).getSpace_id().equals("111")){
				        	jsonObj.put("specificationId","");
				        }else{
				        	jsonObj.put("specificationId",list.get(j).getSpace_id());
				        }
				        jsonarray.put(jsonObj);//向json数组里面添加pet对象  
					}
					obj.put(groups2.get(i).getId(),jsonarray);
				}
				jsonString=obj.toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			jsonString="";
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
			String telephone, String totalprice, String jsonstring,String youhuixinxi,
			String youhuiid,String name,String addressids) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("orderType", "1");
		params.put("ordersType", "1");
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
		params.put("deliveryId",addressids);
		params.put("myCouponId",youhuiid);
		params.put("privilege",youhuixinxi);
		params.put("userName",name);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.MAKE_A_ORDER_NUM, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						if(handler==null)
							return;
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

				Intent intent = new Intent(OrderActivity.this,
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
	 * 调用方法，处理字符串，需要生成订单的字符串
	 * 
	 * @param list_new
	 * @return
	 */
	private String userWayMakeJsonString(List<ProductInfo> list_new) {
		// TODO Auto-generated method stub
		String jsonresult = "";// 定义返回字符串
		// 先判断集合中有多少数据
		if (list_new != null && list_new.size() != 0) {
			if (list_new.size() > 1) {

			} else {
				JSONObject object = new JSONObject();// 创建一个总的对象，这个对象对整个json串
				try {
					JSONArray jsonarray = new JSONArray();// json数组，里面包含的内容为pet的所有对象
					JSONObject jsonObj = new JSONObject();// pet对象，json形式
					jsonObj.put("goodsid", list_new.get(0).getId());// 向pet对象里面添加值
					jsonObj.put("count", list_new.get(0).getCount());
					// 把每个数据当作一对象添加到数组里
					jsonarray.put(jsonObj);// 向json数组里面添加pet对象
					object.put(list_new.get(0).getSeller(), jsonarray);// 向总对象里面添加包含pet的数组
					jsonresult = object.toString();// 生成返回字符串
					return jsonresult;
				} catch (JSONException e) {

					// TODO Auto-generated catch block

					e.printStackTrace();

				}
			}
		}
		return jsonresult;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESCODE_TAG:
			// 说明是从购物券界面回调过来的
			if (data != null) {
				mUsebean= (YouHuiBean) data
						.getSerializableExtra("bean");
				if (mUsebean != null) {
					is_use_coupone=true;
					mYouhuiquanName
							.setText("可优惠" + mUsebean.getCouponPrice() + "元");
					//判断当前是自取还是送货
					if(ZIQU_OR_SONG.equals("0")){
						//说明是自取
						double coupon=Double.parseDouble(mUsebean.getCouponPrice());
						double payprice=totalPrice-coupon;
						mPayPrice.setText(Utils.parseTwoNumber(payprice+"")+"");
					}else{
						//说明是送货
						double coupon=Double.parseDouble(mUsebean.getCouponPrice());
						double payprices=totalPrice+coupon;
						double yunfies=Double.parseDouble(yunfei);
						double pay=payprices+yunfies;
						mPayPrice.setText(Utils.parseTwoNumber(pay+"")+"");
					}
				} else {
					mYouhuiquanName.setText("暂无优惠信息");
				}
			} else {
				Toast.makeText(OrderActivity.this, "网络异常", 0).show();
			}
			break;
		case 2:
			if (data != null) {
				mSelectBean = (ShippingAddressBean) data
						.getSerializableExtra("addressBean");
				if (mSelectBean != null
						&& !TextUtils.isEmpty(mSelectBean.getUserAddress())) {
					addressId = mSelectBean.getId();
					mYoujiWayName.setText(mSelectBean.getUserName());
					mYoujiWayAddress.setText("收货地址："
							+ mSelectBean.getUserCity()
							+ mSelectBean.getUserAddress());
					mYoujiWayPhone.setText(mSelectBean.getUserPhone());
				}
				isDefault = false;
			}

		default:
			break;
		}
	}

	/**
	 * 自定义的退款说明的dialog
	 */
	private void createDialog_tuikuan(String shuoming) {
		// 需要先弹出一个dialog，在该dialog上面来进行选择
		dialog = new Dialog(OrderActivity.this, R.style.Theme_Light_Dialog);
		View dialogView = LayoutInflater.from(OrderActivity.this).inflate(
				R.layout.tuikuanshuoming, null);
		// 获得dialog的window窗口
		TextView title = (TextView) dialogView
				.findViewById(R.id.tuikuanshuoming_tv_title);
		title.setText("自取说明");
		TextView message = (TextView) dialogView
				.findViewById(R.id.tuikuanshuoming_tv_remind_message);
		message.setText(shuoming);
		TextView qsure = (TextView) dialogView
				.findViewById(R.id.tuikuanshuoming_tv_confirm_close);
		qsure.setOnClickListener(this);
		Window window = dialog.getWindow();
		// 设置dialog在屏幕底部
		window.setGravity(Gravity.CENTER);
		// 设置dialog弹出时的动画效果，从屏幕底部向上弹出
		// window.setWindowAnimations(R.style.dialogStyle);
		window.getDecorView().setPadding(160, 0, 160,0);
		// 获得window窗口的属性
		android.view.WindowManager.LayoutParams lp = window.getAttributes();
		// 设置窗口宽度为充满全屏
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		// lp.width =getScreenHeight(OnLineCreatingPayActivity.this)/3;
		// 设置窗口高度为包裹内容
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		// 将设置好的属性set回去
		window.setAttributes(lp);
		// 将自定义布局加载到dialog上
		dialog.setContentView(dialogView);
		dialog.show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jzkj.lstv.adapter.ShopcartExpandableListViewAdapter.ModifyCountInterface
	 * #doIncrease(int, int, android.view.View, boolean)
	 */
	@Override
	public void doIncrease(String id, int groupPosition, int childPosition,
			View showCountView, boolean isChecked) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jzkj.lstv.adapter.ShopcartExpandableListViewAdapter.ModifyCountInterface
	 * #doDecrease(int, int, android.view.View, boolean)
	 */
	@Override
	public void doDecrease(String id, int groupPosition, int childPosition,
			View showCountView, boolean isChecked) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jzkj.lstv.adapter.ShopcartExpandableListViewAdapter.CheckInterface
	 * #checkGroup(int, boolean)
	 */
	@Override
	public void checkGroup(int groupPosition, boolean isChecked) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jzkj.lstv.adapter.ShopcartExpandableListViewAdapter.CheckInterface
	 * #checkChild(int, int, boolean)
	 */
	@Override
	public void checkChild(int groupPosition, int childPosition,
			boolean isChecked) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.RadioGroup.OnCheckedChangeListener#onCheckedChanged(android
	 * .widget.RadioGroup, int)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jzkj.lstv.view.WiperSwitch.OnChangedListener#OnChanged(com.jzkj.lstv
	 * .view.WiperSwitch, boolean)
	 */
	@Override
	public void OnChanged(WiperSwitch wiperSwitch, boolean checkState) {
		// TODO Auto-generated method stub
		switch (wiperSwitch.getId()) {
		case R.id.order_shifoushiyongjifen:
			// 是否使用积分
			if (checkState) {
				// 需要
				If_USE_JF = true;
			} else {
				// 不需要
				If_USE_JF = false;
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
	 * 用户支付以后从数据库中删除选定的商品
	 * 
	 * @param groups
	 * @param children
	 */
	private void useWayDeleteDataFromSQL(List<GroupInfo> groups,
			Map<String, List<ProductInfo>> children) {
		// TODO Auto-generated method stub
		if (groups != null && children != null && groups.size() != 0
				&& children.size() != 0) {
			for (int i = 0; i < groups.size(); i++) {
				List<ProductInfo> list = children.get(groups.get(i).getId());
				if (list != null && list.size() != 0) {
					for (int j = 0; j < list.size(); j++) {
						// 调用方法，删除数据
						deleteFoodOfDB(list.get(j));
					}
				}
			}
		}
	}

	/**
	 * 根据传入商品，从数据库中删除该商品
	 * 
	 * @param serviceCateringDetailBean
	 */
	private void deleteFoodOfDB(ProductInfo product) {
		// TODO Auto-generated method stub
		LApplication.dbBuyCar.delete(
				"buy_mUsename= ? AND buy_id = ? AND buy_mSpaceid =?",
				new String[] { GlobalParams.USER_ID, product.getId(),
						product.getSpace_id() });
	}
	
	/**
	 * 解析优惠数据
	 * @param backdata
	 * @return
	 */
	private List<YouHuiBean> useWayJsonData(String backdata) {
		// TODO Auto-generated method stub
		List<YouHuiBean> list=new ArrayList<YouHuiBean>();
		try {
			JSONArray array=new JSONArray(backdata);
			int count=array.length();
			for(int i=0;i<count;i++){
				JSONObject obj=array.getJSONObject(i);
				YouHuiBean bean=new YouHuiBean();
				if(obj.has("beginTime")){
					bean.setBeginTime(obj.getString("beginTime"));
				}
				if(obj.has("couponCondition")){
					bean.setCouponCondition(obj.getString("couponCondition"));
				}
				if(obj.has("couponPrice")){
					bean.setCouponPrice(obj.getString("couponPrice"));
				}
				if(obj.has("couponType")){
					bean.setCouponType(obj.getString("couponType"));
				}
				if(obj.has("endTime")){
					bean.setEndTime(obj.getString("endTime"));
				}
				if(obj.has("id")){
					bean.setId(obj.getString("id"));
				}
				if(obj.has("status")){
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

	/**
	 * 改方法用来判断是否有可用的优惠券
	 * @param mCounponList2 
	 */
	private void usewayCheckData(List<YouHuiBean> mCounponList2) {
		// TODO Auto-generated method stub
		if(mCounponList2!=null&&mCounponList2.size()!=0){
			List<YouHuiBean> list=new ArrayList<>();
			for(int i=0;i<mCounponList2.size();i++){
				if(mCounponList2.get(i).getStatus().equals("1")&&mCounponList2.get(i).getCouponType().equals("4")&&mCounponList2.get(i).getInusing().equals("0")){
					//说明可用
					if(!TextUtils.isEmpty(mCounponList2.get(i).getCouponCondition())){
						double price=Double.parseDouble(mCounponList2.get(i).getCouponCondition());
						if(totalPrice>=price){
							list.add(mCounponList2.get(i));
						}
					}
				}
			}
			
			//
			if(list!=null&&list.size()!=0){
				mYouhuiquanName.setText("请选择");
			}else{
				mYouhuiquanName.setText("无可用");
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		instance = null;
		if(handler!=null){
			handler.removeCallbacksAndMessages(null);
			handler = null;
		}
		if(groups!=null){
			groups.clear();
			groups = null;
		}
		if(children!=null){
			children .clear();
			children = null;
		}
		timeSelector = null;
		if(mCounponList!=null){
			mCounponList.clear();
			mCounponList = null;
		}
		mUsebean = null;
		super.onDestroy();
	}

	/**
	 * 生成订单数据
	 * @param backdata
	 */
	private void useWayHandleMakaOrder(String backdata) {
		// TODO Auto-generated method stub
		mLayout_progress.setVisibility(View.GONE);
		// 调用方法，解析订单数据
		mOrderbean = jsonWayGetData(backdata);
		if (mOrderbean != null) {
			if (mOrderbean.getResultCode() != null
					&& mOrderbean.getResultCode().equals("000")) {
				// 这个时候，订单已经顺利生成，调用方法，从数据库中删除选择的商品
				useWayDeleteDataFromSQL(groups, children);
				if (mOrderbean.getOrderNumber() != null) {

					// 调用方法，获取支付参数
					UseWayGetPayContent(mOrderbean);
				} else {
					Toast.makeText(OrderActivity.this, "订单状态异常", 0)
							.show();
				}
			} else if (mOrderbean.getResultCode().equals("001")) {
				Toast.makeText(OrderActivity.this, "商品库存不足", 0)
						.show();
			}
		}
	}

	/**
	 * 支付成功处理
	 */
	private void useWayHandlePaySuccess() {
		// TODO Auto-generated method stub
		// 需要将支付的商品全部传递过去，还需要将界面中的1运费，折扣费，总价，座位号，手机号，这些数据传递过去
		if (IS_ZIQU) {
			if (groups != null && groups.size() != 0
					&& children != null && children.size() != 0) {
				Intent intent = new Intent(OrderActivity.this,
						PaySuccessActivity.class);
				intent.putExtra("group", (Serializable) groups);
				intent.putExtra("child", (Serializable) children);
				// 这里是标记
				intent.putExtra("tag", "ziqu");
				intent.putExtra("type", "shangpin");
				intent.putExtra("ziqudizhi", mZiTiAddress.getText()
						.toString());
				intent.putExtra("yunfei", yunfei);
				intent.putExtra("price", mPayPrice.getText().toString());
				intent.putExtra("mZiqushuoming", mZiqushuoming);
				startActivity(intent);
			}
		} else if (IS_SONGHUO) {
			if (groups != null && groups.size() != 0
					&& children != null && children.size() != 0) {
				Intent intent = new Intent(OrderActivity.this,
						PaySuccessActivity.class);
				intent.putExtra("group", (Serializable) groups);
				intent.putExtra("child", (Serializable) children);
				// 这里是标记
				intent.putExtra("tag", "songhuo");
				intent.putExtra("type", "shangpin");
				intent.putExtra("yunfei", yunfei);
				intent.putExtra("price", mPayPrice.getText().toString());
				intent.putExtra("zuoweihao", mZuoweihao.getText()
						.toString());
				intent.putExtra("phone", mPhoneNum.getText().toString());
				startActivity(intent);
			}
		}else if(IS_YOUJI){
			if (groups != null && groups.size() != 0
					&& children != null && children.size() != 0) {
				Intent intent = new Intent(OrderActivity.this,
						PaySuccessActivity.class);
				intent.putExtra("group", (Serializable) groups);
				intent.putExtra("child", (Serializable) children);
				// 这里是标记
				intent.putExtra("tag", "youji");
				intent.putExtra("type", "shangpin");
				intent.putExtra("yunfei", yunfei);
				intent.putExtra("price", mPayPrice.getText().toString());
				intent.putExtra("phone", mYoujiWayPhone.getText().toString());
				intent.putExtra("name", mYoujiWayName.getText().toString());
				intent.putExtra("address",mYoujiWayAddress.getText().toString());
				startActivity(intent);
			}
		}
	}
	/**
	 * 用户取消支付处理
	 */
	private void useWayHandleCanclePay() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(OrderActivity.this,
				OrderDetailActivity.class);
		intent.putExtra("tag", 0);
		intent.putExtra("tags", "online");
		intent.putExtra("bean", (Serializable) mOrderbean);
		intent.putExtra("group", (Serializable) groups);
		intent.putExtra("yunfeis", yunfei);
		intent.putExtra("cs", "shangpin");
		if (IS_ZIQU) {
			intent.putExtra("zs","0");
		} else if(IS_SONGHUO){
			intent.putExtra("zs","1");
		}else if(IS_YOUJI){
			intent.putExtra("zs","2");
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
		//调用方法，进行解析
		mCounponList=useWayJsonData(backdatasss);
		//调用方法，进行分拣数据
		if(mCounponList!=null&&mCounponList.size()!=0){
			//调用方法，判断有无优惠券可用
			usewayCheckData(mCounponList);
		}else{
			mYouhuiquanName.setText("无可用");
		}
	}
	/**
	 * 获取默认收货地址
	 */
	private void requestDefaultAddress() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", GlobalParams.USER_ID); // userId
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.GET_DEFAULT_ADDRESS, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if(handler==null)
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
									Message msg = new Message();
									msg.arg1 = 222;
									msg.obj = backdata;
									handler.sendMessage(msg);
								}
							}

						}
					}
				}, false, false);
	}
	/**
	 * 处理默认收货地址
	 * 
	 * @param backdatassss
	 */
	private void useWayHandleDefaultAddress(String backdatassss) {
		try {
			JSONObject aObj = new JSONObject(backdatassss);
			ShippingAddressBean tempAddress = new ShippingAddressBean();
			tempAddress.setUserName(aObj.getString("name"));
			tempAddress.setUserPhone(aObj.getString("mobilePhone"));
			tempAddress.setId(aObj.getString("id"));
			tempAddress.setPostcode(aObj.getString("zipcode"));
			tempAddress.setUserCity(aObj.getString("cityAddress"));
			tempAddress.setUserAddress(aObj.getString("address"));
			// 使用方法，设置地址
			useWaySetAddress(tempAddress);
			addressId=tempAddress.getId();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	/**
	 * 设置默认收货地址
	 * 
	 * @param tempAddress
	 */
	private void useWaySetAddress(ShippingAddressBean tempAddress) {
		addressId = tempAddress.getId();
		mYoujiWayAddress.setText("收货地址：" + tempAddress.getUserCity()
				+ tempAddress.getUserAddress());
		mYoujiWayPhone.setText(tempAddress.getUserPhone());
		mYoujiWayName.setText(tempAddress.getUserName());
	}
}
