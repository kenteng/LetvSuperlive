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

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lesports.stadium.R;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.base.BaseActivity;
import com.lesports.stadium.bean.AllChipsBean;
import com.lesports.stadium.bean.CouponBean;
import com.lesports.stadium.bean.CrowReportBackBean;
import com.lesports.stadium.bean.CrowdDetailBean;
import com.lesports.stadium.bean.GroupInfo;
import com.lesports.stadium.bean.OrderBean;
import com.lesports.stadium.bean.PayBean;
import com.lesports.stadium.bean.PayParametric;
import com.lesports.stadium.bean.ProductInfo;
import com.lesports.stadium.bean.ReportBackGroupBean;
import com.lesports.stadium.bean.ShippingAddressBean;
import com.lesports.stadium.bean.YouHuiBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.EditLengthOnclistener;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.PayUtils;
import com.lesports.stadium.utils.Utils;
import com.lesports.stadium.view.CustomDialog;
import com.letv.lepaysdk.ELePayState;
import com.letv.lepaysdk.LePayApi;
import com.letv.lepaysdk.LePayConfig;
import com.letv.lepaysdk.LePay.ILePayCallback;
import com.umeng.analytics.MobclickAgent;

/**
 * ***************************************************************
 * 
 * @ClassName: CrowdPayActivty
 * 
 * @Desc : 众筹支付页面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : lwc
 * 
 * @data:2016-4-2 下午5:55:37
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
@SuppressLint("ShowToast") public class CrowdPayActivty extends BaseActivity implements OnClickListener {
	/**
	 * 顶部返回按钮
	 */
	private ImageView mBack;
	/**
	 * 汇报标题
	 */
	private TextView mTitle;
	/**
	 * 众筹图片
	 */
	private ImageView mCrowdImage;
	/**
	 * 支持金额数
	 */
	private TextView mMoneyNum;
	/**
	 * 支持说明
	 */
	private TextView mListview;
	/**
	 * 收货人姓名
	 */
	private TextView mTakeGoodsName;
	/**
	 * 收货人电话
	 */
	private TextView mTakeGoodsPhone;
	/**
	 * 收货人地址
	 */
	private TextView mTakeGoodsAddress;
	/**
	 * 收货人邮箱地址
	 */
	private TextView mTakeGoodsEmail;
	/**
	 * 商品金额
	 */
	private TextView mGoodsPrice;
	/**
	 * 运费
	 */
	private TextView mGoodsYunfei;
	/**
	 * 优惠信息
	 */
	private TextView mGoodsYouhuixinxi;
	/**
	 */
	private RelativeLayout mLayout_choiseYouhuiquan;
	/**
	 * 优惠券说明
	 */
	private TextView mYouhuiquan;
	/**
	 * 可用积分数
	 */
	private TextView mJifenshu;
	/**
	 * 可抵扣金额
	 */
	private TextView mKedikou;
	/**
	 * 备注内容
	 */
	private EditText mBeizhuContent;
	/**
	 * 商品总价格
	 */
	/**
	 * 未登陆提示按钮
	 */
	private CustomDialog exitDialog;
	/**
	 * 商品总数量
	 */
	private TextView mGoodsNum;
	/**
	 * 商品价格合计
	 */
	private TextView mZongjiage;
	/**
	 * 运输费用
	 */
	private TextView mYunfei;
	/**
	 * 支付按钮
	 */
	private TextView mGotoPay;
	/**
	 * bean.get
	 */
	private CrowdDetailBean mBean;
	/**
	 * 
	 */
	private ReportBackGroupBean mGroup;
	/**
	 * 需要支持的子数据
	 */
	private CrowReportBackBean mChildBean;
	/**
	 * // 组元素数据列表
	 */
	private List<GroupInfo> groups = new ArrayList<GroupInfo>();
	/**
	 * 
	 * 众筹列表项数据
	 */
	private AllChipsBean mAllBean;
	/**
	 * 选择地址的布局
	 * 
	 */
	private RelativeLayout mLayoutChoiseAddress;
	/**
	 * 本类对象
	 */
	public static CrowdPayActivty instance;
	private String addressId;
	private TextView mZongjiagess;
	/**
	 * 进度布局
	 */
	private RelativeLayout mLayout_progress;
	private OrderBean mOrderbean;
	/**
	 * 是否加载默认地址
	 */
	private boolean isDefault = true;
	/**
	 * 地址实体类
	 */
	private ShippingAddressBean mSelectBean;
	/**
	 * handle内需要使用的标记
	 */
	private final int GET_ORDER_DATA = 1;// 订单生成成功
	private final int GET_ORDER_FAILED = 1111;// 订单生成失败
	private final int PAY_SUCCESS = 2;// 支付成功，页面需要跳转到支付成功页面
	private final int CANCLE_PAY = 22;// 取消支付
	private final int GET_PAY_DATA = 4;// 获取支付参数
	private final int GET_PAY_DATA_FAILED = 4444;// 获取支付参数
	private final int GET_DEFAULT_ADDRESS = 111;
	/**
	 * 处理网络数据的handle
	 */
	@SuppressLint("HandlerLeak") private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case GET_ORDER_DATA:
				String backdata = (String) msg.obj;
				if (backdata != null && !TextUtils.isEmpty(backdata)) {
					useWayHandleOrderData(backdata);
				}
				break;
			case GET_ORDER_FAILED:
				mLayout_progress.setVisibility(View.GONE);
				Toast.makeText(CrowdPayActivty.this, "订单生成失败", 0).show();
				break;
			case PAY_SUCCESS:
				// 支付成功处理
				useWayHandlePaySuccess();
				break;
			case CANCLE_PAY:
				// 处理取消支付
				useWayHandleCanclePay();
				break;
			case GET_PAY_DATA:
				String jsondatass = (String) msg.obj;
				if (jsondatass != null && !TextUtils.isEmpty(jsondatass)) {
					PayBean paybean = useWayJsonDataPay(jsondatass);
					if (paybean != null
							&& !TextUtils.isEmpty(paybean.getSign())) {
						mLayout_progress.setVisibility(View.GONE);
						// 开始调用方法，进行支付
						pay(paybean);
					}
				}
				break;
			case GET_PAY_DATA_FAILED:
				mLayout_progress.setVisibility(View.GONE);
				Toast.makeText(CrowdPayActivty.this, "订单生成失败",  Toast.LENGTH_SHORT).show();
				break;
			case GET_DEFAULT_ADDRESS:
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crowdfunding_pay);
		instance = this;
		initviews();
		initIntentData();
		if (mBean != null && mGroup != null
				&& !TextUtils.isEmpty(mBean.getProjectAddress())
				&& mAllBean != null && mChildBean != null) {
			initViewData(mBean, mGroup, mAllBean, mChildBean);
		}
	}

	/**
	 * 用来初始化界面数据
	 * 
	 * @param mGroup2
	 * @param mBean2
	 * @param mAllBean2
	 * @param mChildBean2
	 */
	private void initViewData(CrowdDetailBean mBean2,
			ReportBackGroupBean mGroup2, AllChipsBean mAllBean2,
			CrowReportBackBean mChildBean2) {
		if (mBean2 != null && mGroup2 != null
				&& !TextUtils.isEmpty(mBean2.getPropagatePicture())
				&& mAllBean2 != null && mChildBean2 != null)
			LApplication.loader.DisplayImage(ConstantValue.BASE_IMAGE_URL
					+ GlobalParams.ZHONGCHOU_ORDER_IMAGE
					+ ConstantValue.IMAGE_END, mCrowdImage,
					R.drawable.zhoubianshangpin_zhanwei);
		// 初始化标题
		mTitle.setText(mAllBean2.getCrowdfundName());
		// 初始化金额数
		mMoneyNum.setText("支持￥" + mChildBean2.getReturnPrice());
		// 初始化说明部分
		mListview.setText(mChildBean2.getReturnContent());
		// 初始化商品金额
		mGoodsPrice.setText("￥：" + mChildBean2.getReturnPrice());
		mGoodsYunfei.setText("￥：" + mBean2.getFreight());
		if (mChildBean2.getReturnPrice().contains(".")) {
			double price = Double.parseDouble(mChildBean2.getReturnPrice());
			if (mBean2.getFreight().contains(".")) {
				double yunfei = Double.parseDouble(mBean2.getFreight());
				mZongjiage.setText("￥" + (price + yunfei) + "");
				mYunfei.setText("(含运费"
						+ Utils.parseTwoNumber(mBean2.getFreight()) + "元)");
				mZongjiagess.setText("￥"
						+ (Utils.parseTwoNumber("" + (price + yunfei))) + "");
			} else {
				double yunfei = Double.parseDouble(mBean2.getFreight());
				mZongjiage.setText("￥"
						+ (Utils.parseTwoNumber("" + (price + yunfei))) + "");
				mYunfei.setText("(含运费"
						+ Utils.parseTwoNumber(mBean2.getFreight()) + "元)");
				mZongjiagess.setText("￥"
						+ (Utils.parseTwoNumber("" + (price + yunfei))) + "");
			}
		} else {
			double price = Double.parseDouble(mChildBean2.getReturnPrice());
			double yunfei = Double.parseDouble(mBean2.getFreight());
			mZongjiage.setText("￥"
					+ (Utils.parseTwoNumber("" + (price + yunfei))) + "");
			mYunfei.setText("(含运费" + Utils.parseTwoNumber(mBean2.getFreight())
					+ "元)");
			mZongjiagess.setText("￥"
					+ (Utils.parseTwoNumber("" + (price + yunfei))) + "");
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		// 调用方法获取默认的收货地址
		if (isDefault)
			requestDefaultAddress();
	}

	/**
	 * 初始化获取界面跳转的时候传递过来的数据
	 */
	private void initIntentData() {
		Intent intent = getIntent();
		mGroup = (ReportBackGroupBean) intent.getSerializableExtra("group");
		if (mGroup != null) {
			mAllBean = mGroup.getBean();
			mBean = mGroup.getCbean();
			mChildBean = mGroup.getChildBean();
		}
	}

	/**
	 * 用来初始化界面ui控件
	 */
	private void initviews() {
		mLayout_progress = (RelativeLayout) findViewById(R.id.layout_progress_layout_crowdfunding);
		mLayout_progress.setVisibility(View.GONE);
		mLayout_progress.setOnClickListener(this);
		mLayoutChoiseAddress = (RelativeLayout) findViewById(R.id.crowd_address_choise);
		mLayoutChoiseAddress.setOnClickListener(this);
		mBack = (ImageView) findViewById(R.id.crowd_dingdanzhifu_back);
		mBack.setOnClickListener(this);
		mTitle = (TextView) findViewById(R.id.crowd_dingdanzhifu_title);
		mCrowdImage = (ImageView) findViewById(R.id.crowd_dingdanzhifu_image);
		mMoneyNum = (TextView) findViewById(R.id.crowd_dingdanzhifu_zhichijine);
		mListview = (TextView) findViewById(R.id.crowd_dingdanzhifu_zhichishuoming);
		mTakeGoodsName = (TextView) findViewById(R.id.crowd_dingdanzhifu_shouhuoren_name);
		mTakeGoodsPhone = (TextView) findViewById(R.id.crowd_dingdanzhifu_shouhuoren_dianhua);
		mTakeGoodsAddress = (TextView) findViewById(R.id.crowd_dingdanzhifu_shouhuoren_dizhi);
		mTakeGoodsEmail = (TextView) findViewById(R.id.crowd_dingdanzhifu_shouhuoren_youxiang);
		mGoodsPrice = (TextView) findViewById(R.id.crowd_dingdanzhifu_shangpinjine_s);
		mGoodsYunfei = (TextView) findViewById(R.id.crowd_dingdanzhifu_yunfei_s);
		mGoodsYouhuixinxi = (TextView) findViewById(R.id.crowd_dingdanzhifu_youhuixinxi_s);
		mLayout_choiseYouhuiquan = (RelativeLayout) findViewById(R.id.crowd_layout_youhuiquan);
		mLayout_choiseYouhuiquan.setOnClickListener(this);
		mYouhuiquan = (TextView) findViewById(R.id.crowd_youhuiquan_zanwussssssss);
		mJifenshu = (TextView) findViewById(R.id.crowd_keyongjifen);
		mKedikou = (TextView) findViewById(R.id.crowd_kedikoujines);
		mBeizhuContent = (EditText) findViewById(R.id.crowd_beizhushurukuang);
		mBeizhuContent.addTextChangedListener(new EditLengthOnclistener(10,
				mBeizhuContent, CrowdPayActivty.this));
		mZongjiage = (TextView) findViewById(R.id.crowd_shangpinzongjia);
		mGoodsNum = (TextView) findViewById(R.id.crowd_gongduoshaojian);
		mZongjiagess = (TextView) findViewById(R.id.crowd_order_pay_price);
		mYunfei = (TextView) findViewById(R.id.crowd_order_pay_yunfeishuoming);
		mGotoPay = (TextView) findViewById(R.id.crowd_order_pay_pay);
		mGotoPay.setOnClickListener(this);
	}

	private void pay(PayBean paybean) {
		if (paybean != null && !TextUtils.isEmpty(paybean.getSign())) {
			PayParametric parameters = new PayParametric();
			parameters.setVersion(paybean.getVersion());
			parameters.setSign(paybean.getSign());
			parameters.setService(paybean.getService());
			parameters.setMerchant_business_id(paybean
					.getMerchant_business_id());
			parameters.setUser_id(paybean.getUser_id());
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
			LePayApi.initConfig(CrowdPayActivty.this, config);
			LePayApi.doPay(CrowdPayActivty.this, param, new ILePayCallback() {
				@Override
				public void payResult(ELePayState status, String message) {
					// Toast.makeText(CrowdPayActivty.this,
					// "结果:" + status + "|" + message, Toast.LENGTH_SHORT)
					// .show();
					if (ELePayState.CANCEL == status) { // 支付取消
					// Toast.makeText(CrowdPayActivty.this,
					// "取消支付", Toast.LENGTH_SHORT)
					// .show();
						Message msg = new Message();
						msg.arg1 = CANCLE_PAY;
						handler.sendMessage(msg);
					} else if (ELePayState.FAILT == status) { // 支付失败
						Toast.makeText(CrowdPayActivty.this, "支付失败",
								Toast.LENGTH_SHORT).show();
					} else if (ELePayState.OK == status) { // 支付成功
					// Toast.makeText(CrowdPayActivty.this,
					// "支付成功", Toast.LENGTH_SHORT)
					// .show();
						Message msg = new Message();
						msg.arg1 = PAY_SUCCESS;
						handler.sendMessage(msg);

					} else if (ELePayState.WAITTING == status) { // 支付中

					}
				}
			});
		} else {
			Toast.makeText(CrowdPayActivty.this, "支付异常", Toast.LENGTH_SHORT)
					.show();
		}

	}

	/**
	 * 设置默认收货地址
	 * 
	 * @param tempAddress
	 */
	private void useWaySetAddress(ShippingAddressBean tempAddress) {
		addressId = tempAddress.getId();
		mTakeGoodsAddress.setText("收货地址：" + tempAddress.getUserCity()
				+ tempAddress.getUserAddress());
		mTakeGoodsEmail.setText(tempAddress.getPostcode());
		mTakeGoodsPhone.setText(tempAddress.getUserPhone());
		mTakeGoodsName.setText(tempAddress.getUserName());
	}

	/**
	 * 该方法用来向服务端提交订单号以及商品信息，以及用户信息，获取支付参数来进行支付
	 * 
	 * @param bean
	 */
	private void UseWayGetPayContent(OrderBean bean) {
		// 该方法用来向
		if (bean != null) {
			String orderId = bean.getId();// 订单号
			String username = GlobalParams.USER_NAME;// 用户名
			String ip = getPsdnIp();
			String goodsprice = null;
			String currency = "CNY";
			String pay_expire = "45";
			String product_id = null;
			String product_name = null;
			String product_desc = null;
			String product_urls = "http://serverurl/product_url";
			if (mChildBean != null) {
				double price = Double.parseDouble(mChildBean.getReturnPrice());
				double yunfei = Double.parseDouble(mBean.getFreight());
				goodsprice = (price + yunfei) + "";
				product_id = mChildBean.getId();
				product_name = mChildBean.getReturnName();
				product_desc = mChildBean.getReturnContent();
			} else {
			}

			UseWayGetPayData(orderId, username, ip, goodsprice, currency,
					pay_expire, product_id, product_name, product_desc,
					product_urls);

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
						if (data == null) {
							// 说明网络获取无数据
							Toast.makeText(CrowdPayActivty.this, "网络错误",
									Toast.LENGTH_SHORT).show();
						} else {
							Object obj = data.getObject();
							if (obj == null) {
								Toast.makeText(CrowdPayActivty.this, "网络错误",
										Toast.LENGTH_SHORT).show();
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
									Toast.makeText(CrowdPayActivty.this,
											"网络异常", Toast.LENGTH_SHORT).show();
								} else {
									Message msg = new Message();
									msg.arg1 = 111;
									msg.obj = backdata;
									handler.sendMessage(msg);
								}
							}

						}
					}
				}, false, false);
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
						if (data == null) {
							// 说明网络获取无数据
						} else {
							Object obj = data.getObject();
							String backdata = obj.toString();
							if (backdata == null && backdata.equals("")) {
							} else {
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.crowd_dingdanzhifu_back:
			finish();
			break;
		case R.id.crowd_layout_youhuiquan:
			Intent intent = new Intent(CrowdPayActivty.this,
					UseCounponActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.crowd_order_pay_pay:
			MobclickAgent.onEvent(CrowdPayActivty.this, "pay");
			// 这里是去支付
			// 调用方法，组成需要提交的参数
			// 先判断用户是否登录
			final String userid = GlobalParams.USER_ID;
			if (userid != null && !TextUtils.isEmpty(userid)) {
				// 判断用户是否选择收货地址
				if (!TextUtils.isEmpty(mTakeGoodsAddress.getText().toString())
						&& !TextUtils.isEmpty(mTakeGoodsName.getText()
								.toString())
						&& !TextUtils.isEmpty(mTakeGoodsPhone.getText()
								.toString())) {
					// 调用方法，组成字符串
					if (mChildBean != null) {
						String jsonstring = userWayMakeJsonString(mChildBean);
						Log.i("组成的串是", jsonstring);
						Generateorder(jsonstring);
					}
				} else {
					Toast.makeText(CrowdPayActivty.this, "请选择收货地址",  Toast.LENGTH_SHORT).show();
				}

			} else {
				createDialog();
			}
			break;
		case R.id.crowd_address_choise:
			Intent intents = new Intent(CrowdPayActivty.this,
					SelectAddressActivity.class);
			intents.putExtra("id", addressId);
			startActivityForResult(intents, 2);
			break;
		case R.id.layout_progress_layout_crowdfunding:
			break;

		default:
			break;
		}
	}

	/**
	 * 调用方法，生成订单数据
	 * 
	 * @param context
	 */
	private void Generateorder(String jsonstring) {
		// 测试生成订单的参数
		Map<String, String> params = new HashMap<String, String>();
		params.put("orderType", "0");
		params.put("ordersType", "3");
		params.put("payMent", "0");
		params.put("remark", mBeizhuContent.getText().toString());
		params.put("payStatus", "0");
		params.put("freight", mBean.getFreight());
		params.put("companies", mTakeGoodsName.getText().toString());
		params.put("position", mTakeGoodsAddress.getText().toString());
		params.put("telePhone", mTakeGoodsPhone.getText().toString());
		params.put("courier", "1");
		params.put("orderAmount", mChildBean.getReturnPrice());
		params.put("details", jsonstring);
		params.put("deliveryId", addressId);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.MAKE_A_ORDER_NUM, params, new GetDatas() {
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
								if (data.getNetResultCode() == 0) {
									Message msg = new Message();
									msg.arg1 = 1;
									msg.obj = backdata;
									handler.sendMessage(msg);
								} else {
									Message msg = new Message();
									msg.arg1 = 1111;
									handler.sendMessage(msg);
								}

							}
						}
					}

				}, false, false);
	}

	/**
	 * 创建自定义的dialog，提示用户进行登录
	 */
	private void createDialog() {
		exitDialog = new CustomDialog(this, new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(CrowdPayActivty.this,
						RegisterActivity.class);
				startActivity(intent);
				exitDialog.dismiss();
			}
		}, new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CrowdPayActivty.this,
						LoginActivity.class);
				startActivity(intent);
				exitDialog.dismiss();
			}
		});
		exitDialog.setCancelTxt("立即登录");
		exitDialog.setConfirmTxt("注册账号");
		exitDialog.setRemindMessage("登录之后才能进行支付哦~");
		exitDialog.show();
	}

	/**
	 * 调用方法，处理字符串，需要生成订单的字符串
	 * 
	 * @param mChildBean2
	 * @return
	 */
	private String userWayMakeJsonString(CrowReportBackBean mChildBean2) {
		String jsonresult = "";// 定义返回字符串
		// 先判断集合中有多少数据
		JSONObject object = new JSONObject();// 创建一个总的对象，这个对象对整个json串
		try {
			JSONArray jsonarray = new JSONArray();// json数组，里面包含的内容为pet的所有对象
			JSONObject jsonObj = new JSONObject();// pet对象，json形式
			jsonObj.put("goodsid", mChildBean2.getId());// 向pet对象里面添加值
			jsonObj.put("count", "1");
			// 把每个数据当作一对象添加到数组里
			jsonarray.put(jsonObj);// 向json数组里面添加pet对象
			object.put("0", jsonarray);// 向总对象里面添加包含pet的数组
			jsonresult = object.toString();// 生成返回字符串
			return jsonresult;
		} catch (JSONException e) {
			e.printStackTrace();

		}
		return jsonresult;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 1:
			if (data != null) {
				YouHuiBean bean = (YouHuiBean) data
						.getSerializableExtra("bean");
				if (bean != null) {
					String type = bean.getCouponType();
					if (!TextUtils.isEmpty(type)) {
						if (type.equals("0")) {
							mYouhuiquan.setText("餐饮券");
						} else if (type.equals("1")) {
							mYouhuiquan.setText("购物券");
						} else if (type.equals("2")) {
							mYouhuiquan.setText("用车券");
						} else if (type.equals("3")) {
							mYouhuiquan.setText("购票券");
						}
					}
				} else {
					mYouhuiquan.setText("暂无优惠信息");
				}
			} else {
				Toast.makeText(CrowdPayActivty.this, "网络异常",  Toast.LENGTH_SHORT).show();
			}
			break;
		case 2:
			if (data != null) {
				mSelectBean = (ShippingAddressBean) data
						.getSerializableExtra("addressBean");
				if (mSelectBean != null
						&& !TextUtils.isEmpty(mSelectBean.getUserAddress())) {
					addressId = mSelectBean.getId();
					mTakeGoodsName.setText(mSelectBean.getUserName());
					mTakeGoodsAddress.setText("收货地址："
							+ mSelectBean.getUserCity()
							+ mSelectBean.getUserAddress());
					mTakeGoodsEmail
							.setText("邮政编码：" + mSelectBean.getPostcode());
					mTakeGoodsPhone.setText(mSelectBean.getUserPhone());
				}
				isDefault = false;
			}

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
			e.printStackTrace();
		}
		return bean;
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
	 * 提交商品信息获取订单数据成功后的处理方法
	 * 
	 * @param backdata
	 */
	private void useWayHandleOrderData(String backdata) {
		// 调用方法，解析订单数据
		mOrderbean = jsonWayGetData(backdata);
		if (mOrderbean != null) {
			if (mOrderbean.getResultCode() != null
					&& mOrderbean.getResultCode().equals("000")) {
				if (mOrderbean.getOrderNumber() != null) {
					// 现在需要将该订单号提交到服务端，获取支付参数
					// 调用方法，获取支付参数
					UseWayGetPayContent(mOrderbean);
				} else {
					Toast.makeText(CrowdPayActivty.this, "订单状态异常",  Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(CrowdPayActivty.this, "商品库存不足", 0).show();
			}
		}
	}

	/**
	 * 众筹支付成功处理
	 */
	private void useWayHandlePaySuccess() {
		// 需要将整个众筹详情页实体传递过去，以及地址信息
		Intent intent = new Intent(CrowdPayActivty.this,
				CrowdPaySuccessActivity.class);
		intent.putExtra("tag", "zhongchou");
		intent.putExtra("bean", mBean);
		intent.putExtra("beans", mAllBean);
		intent.putExtra("beanss", mChildBean);
		intent.putExtra("name", mTakeGoodsName.getText().toString());
		intent.putExtra("address", mTakeGoodsAddress.getText().toString());
		intent.putExtra("phone", mTakeGoodsPhone.getText().toString());
		intent.putExtra("youbian", mTakeGoodsEmail.getText().toString());
		startActivity(intent);
	}

	/**
	 * 取消支付以后的结果处理
	 */
	private void useWayHandleCanclePay() {
		// 需要将整个众筹详情页实体传递过去，以及地址信息
		Intent intents = new Intent(CrowdPayActivty.this, WaitPayActivity.class);
		intents.putExtra("other", "pay");
		intents.putExtra("bean", mBean);
		intents.putExtra("beans", mAllBean);
		intents.putExtra("beanss", mChildBean);
		intents.putExtra("orderbean", mOrderbean);
		intents.putExtra("name", mTakeGoodsName.getText().toString());
		intents.putExtra("address", mTakeGoodsAddress.getText().toString());
		intents.putExtra("phone", mTakeGoodsPhone.getText().toString());
		intents.putExtra("youbian", mTakeGoodsEmail.getText().toString());
		startActivity(intents);
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
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
