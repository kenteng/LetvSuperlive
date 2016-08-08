package com.lesports.stadium.activity;

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

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.lesports.stadium.R;
import com.lesports.stadium.bean.CreateOrderBean;
import com.lesports.stadium.bean.GetIntegralBean;
import com.lesports.stadium.bean.PayBean;
import com.lesports.stadium.bean.PayParametric;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.JsonUtil;
import com.lesports.stadium.utils.PayUtils;
import com.letv.lepaysdk.ELePayState;
import com.letv.lepaysdk.LePayApi;
import com.letv.lepaysdk.LePayConfig;
import com.letv.lepaysdk.LePay.ILePayCallback;

/**
 * 积分充值界面
 * 
 * @author Administrator
 * 
 */
public class RechargeintegralActivity extends Activity implements
		OnClickListener {

	// private List<IntegralInfo> integral=new ArrayList<IntegralInfo>();

	private RelativeLayout rl_top1;
	private RelativeLayout rl_top2;
	private RelativeLayout rl_top3;
	private RelativeLayout rl_top4;
	private RelativeLayout rl_top5;
	private RelativeLayout rl_top6;
	private TextView tv_number1;
	private TextView tv_number2;
	private TextView tv_number3;
	private TextView tv_number4;
	private TextView tv_number5;
	private TextView tv_number6;
	private TextView tv_money1;
	private TextView tv_money2;
	private TextView tv_money3;
	private TextView tv_money4;
	private TextView tv_money5;
	private TextView tv_money6;

	int selected = 1;
	private ImageView iv_background1;
	private ImageView iv_background2;
	private ImageView iv_background3;
	private ImageView iv_background4;
	private ImageView iv_background5;
	private ImageView iv_background6;
	private ColorStateList csl;
	private ColorStateList csl2;
	private TextView tv_sum;

	private int orderIdNumber = -1;

	// CreateOrderBean
	CreateOrderBean createOrderBean = new CreateOrderBean();

	List<GetIntegralBean> integralList = new ArrayList<GetIntegralBean>();
	
	/**
	 * handle需要使用的标记
	 */
	private final int RECHARGEINTEGRAL_BILI=100;
	private final int ADD_RECHARGE_ORDER=200;
	private final int PAY_RECHARGE_ORDER=222;
	private final int PAY_RECHARGE_ORDER_FAILED=22;
	private final int PAY_RECHARGE_ORDER_SUCCESS=2222;

	private Handler integralHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case RECHARGEINTEGRAL_BILI:
				// IntegralBean
				String backdata = (String) msg.obj;
				if(!TextUtils.isEmpty(backdata)){
					useWayHandleRecharge(backdata);
				}
				break;
			case ADD_RECHARGE_ORDER:
				String backdataOrder = (String) msg.obj;
				createOrderBean = JsonUtil.parseJsonToBean(backdataOrder,
						CreateOrderBean.class);
				createOrderNext();
				break;
			case PAY_RECHARGE_ORDER:
				String jsonstring = (String) msg.obj;
				if (!TextUtils.isEmpty(jsonstring)) {
					PayBean paybean = useWayJsonDataPay(jsonstring);
					if (paybean != null
							&& !TextUtils.isEmpty(paybean.getSign())) {
						// 开始调用方法，进行支付
						pay(paybean);
					}
				}
				break;
			case PAY_RECHARGE_ORDER_FAILED:
				Toast.makeText(RechargeintegralActivity.this, "充值失败", 0).show();
				break;
			case PAY_RECHARGE_ORDER_SUCCESS:
				finish();
				break;
			default:

				break;
			}
		}


	};
	private TextView tv_go;
	private RadioGroup rg_group;
	private boolean isCheck = false;

	private View view4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rechargeintegra);
		initview();
		initData();
	}

	private void initData() {
		requestIntegral();
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
	 * 获取积分充值比
	 */
	private void requestIntegral() {
		Map<String, String> params = new HashMap<String, String>();
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.GETINTEGRAL, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if(integralHandler==null)
							return;
						if (data != null && data.getNetResultCode() == 0) {
							Object obj = data.getObject();
							String backdata = obj.toString();
							Message msg = new Message();
							msg.arg1 = 100;
							msg.obj = backdata;
							integralHandler.sendMessage(msg);
						}
					}
				}, false, false);

	}

	/**
	 * 生成订单
	 */
	private void createOrder() {
		if (integralList == null || integralList.size() == 0)
			return;
		Map<String, String> params = new HashMap<String, String>();
		params.put("rechargeId", integralList.get(orderIdNumber).getId()); // 充值id号
		params.put("payType", "20"); // 类型
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ADD_JIFEN_ORDER, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if(integralHandler==null)
							return;
						if (data == null) {
							// 说明网络获取无数据
						} else {
							Object obj = data.getObject();
							if (obj == null) {
							} else {
								String backdata = obj.toString();
								Log.i("123", "支付：" + backdata);
								if (backdata == null && backdata.equals("")) {
								} else {

									Message msg = new Message();
									msg.arg1 = ADD_RECHARGE_ORDER;
									msg.obj = backdata;
									integralHandler.sendMessage(msg);

								}
							}
						}
					}
				}, false, false);

	}

	/**
	 * 生成积分充值支付订单
	 */
	private void createOrderNext() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("user_name", GlobalParams.USER_NAME); // 用户名
		params.put("pay_expire", "45"); // 超时时间
		params.put("orderId", createOrderBean.getId()); // 订单id号
		params.put("ip", getPsdnIp()); // ip地址

		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ADD_JIFEN_ORDER_NEXT, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if(integralHandler==null)
							return;
						if (data == null) {
							// 说明网络获取无数据
						} else {
							Object obj = data.getObject();
							if (obj == null) {
							} else {
								String backdata = obj.toString();
								Log.e("积分充值数据next", data.getNetResultCode()
										+ "...." + backdata);
								if (data.getNetResultCode() == 0) {
									Message msg = new Message();
									msg.arg1 = PAY_RECHARGE_ORDER;
									msg.obj = backdata;
									integralHandler.sendMessage(msg);
								} else {
									Message msg = new Message();
									msg.arg1 = PAY_RECHARGE_ORDER_FAILED;
									integralHandler.sendMessage(msg);
								}
							}
						}
					}
				}, false, false);

	}

	private void pay(PayBean paybean) {
		Log.i("221", "pay start");
		// Toast.makeText(RechargeintegralActivity.this, "结果",
		// Toast.LENGTH_SHORT).show();
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
			parameters.setPrice(paybean.getPrice());
			parameters.setCurrency(paybean.getCurrency());
			parameters.setPay_expire(paybean.getPay_expire());
			parameters.setProduct_id(paybean.getProduct_id());
			String product_name = paybean.getProduct_name();
			if (TextUtils.isEmpty(product_name))
				product_name = "乐豆";
			parameters.setProduct_name(product_name);
			String desc = paybean.getProduct_desc();
			if (TextUtils.isEmpty(desc))
				desc = "乐豆充值";
			parameters.setProduct_desc(desc);
			parameters.setMerchant_business_id(paybean
					.getMerchant_business_id());
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
			LePayApi.initConfig(RechargeintegralActivity.this, config);
			LePayApi.doPay(RechargeintegralActivity.this, param,
					new ILePayCallback() {
						@Override
						public void payResult(ELePayState status, String message) {
							// Toast.makeText(RechargeintegralActivity.this,
							// "结果:" + status + "|" + message,
							// Toast.LENGTH_SHORT)
							// .show();
							if(integralHandler==null)
								return;
							if (ELePayState.CANCEL == status) { // 支付取消

							} else if (ELePayState.FAILT == status) { // 支付失败

							} else if (ELePayState.OK == status) { // 支付成功

								Log.i("支付成功了么", "走了这");
								Message msg = new Message();
								msg.arg1 = PAY_RECHARGE_ORDER_SUCCESS;
								integralHandler.sendMessage(msg);

							} else if (ELePayState.WAITTING == status) { // 支付中

							}
						}
					});
		} else {
			Toast.makeText(RechargeintegralActivity.this, "支付异常",
					Toast.LENGTH_SHORT).show();
		}

	}

	private void initview() {
		// IntegralInfo info1=new IntegralInfo();
		// info1.setMoney("1");
		// info1.setNumber("100");
		// IntegralInfo info2=new IntegralInfo();
		// info2.setMoney("10");
		// info2.setNumber("1000");
		// IntegralInfo info3=new IntegralInfo();
		// info3.setMoney("20");
		// info3.setNumber("2000");
		// IntegralInfo info4=new IntegralInfo();
		// info4.setMoney("50");
		// info4.setNumber("5000");
		// IntegralInfo info5=new IntegralInfo();
		// info5.setMoney("100");
		// info5.setNumber("10000");
		// integral.add(info1);
		// integral.add(info2);
		// integral.add(info3);
		// integral.add(info4);
		// integral.add(info5);
		findViewById(R.id.back).setOnClickListener(this);
		rl_top1 = (RelativeLayout) findViewById(R.id.rl_top1);
		rl_top2 = (RelativeLayout) findViewById(R.id.rl_top2);
		rl_top3 = (RelativeLayout) findViewById(R.id.rl_top3);
		rl_top4 = (RelativeLayout) findViewById(R.id.rl_top4);
		rl_top5 = (RelativeLayout) findViewById(R.id.rl_top5);
		rl_top6 = (RelativeLayout) findViewById(R.id.rl_top6);
		tv_number1 = (TextView) findViewById(R.id.tv_number1);
		tv_number2 = (TextView) findViewById(R.id.tv_number2);
		tv_number3 = (TextView) findViewById(R.id.tv_number3);
		tv_number4 = (TextView) findViewById(R.id.tv_number4);
		tv_number5 = (TextView) findViewById(R.id.tv_number5);
		tv_number6 = (TextView) findViewById(R.id.tv_number6);
		tv_money1 = (TextView) findViewById(R.id.tv_money1);
		tv_money2 = (TextView) findViewById(R.id.tv_money2);
		tv_money3 = (TextView) findViewById(R.id.tv_money3);
		tv_money4 = (TextView) findViewById(R.id.tv_money4);
		tv_money5 = (TextView) findViewById(R.id.tv_money5);
		tv_money6 = (TextView) findViewById(R.id.tv_money6);

		tv_sum = (TextView) findViewById(R.id.tv_sum);

		rg_group = (RadioGroup) findViewById(R.id.rg_group);

		rg_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				Log.e("hahaha", checkedId + "");
				isCheck = true;
			}
		});

		iv_background1 = (ImageView) findViewById(R.id.iv_background1);
		iv_background2 = (ImageView) findViewById(R.id.iv_background2);
		iv_background3 = (ImageView) findViewById(R.id.iv_background3);
		iv_background4 = (ImageView) findViewById(R.id.iv_background4);
		iv_background5 = (ImageView) findViewById(R.id.iv_background5);
		iv_background6 = (ImageView) findViewById(R.id.iv_background6);

		view4 = findViewById(R.id.view4);

		rl_top1.setOnClickListener(this);
		rl_top2.setOnClickListener(this);
		rl_top3.setOnClickListener(this);
		rl_top4.setOnClickListener(this);
		rl_top5.setOnClickListener(this);
		rl_top6.setOnClickListener(this);

		Resources resource = (Resources) getBaseContext().getResources();
		csl = (ColorStateList) resource.getColorStateList(R.color.white);
		csl2 = (ColorStateList) resource.getColorStateList(R.color.retextcolor);

		tv_go = (TextView) findViewById(R.id.tv_go);
		tv_go.setOnClickListener(this);

		// IntegralListViewAdapter integralListViewAdapter=new
		// IntegralListViewAdapter();
		// lv_much = (ListView) findViewById(R.id.lv_much);
		// lv_much.setAdapter(integralListViewAdapter);
		// lv_much.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3) {
		//
		// }
		// });
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.rl_top1:
			orderIdNumber = 0;
			selected = 1;
			tv_number1.setTextSize(16);
			tv_money1.setTextSize(16);
			tv_number1.setTextColor(csl);
			tv_money1.setTextColor(csl);
			iv_background1.setBackgroundResource(R.color.retexton);
			tv_sum.setText(tv_money1.getText());
			backToOrigin();
			break;
		case R.id.rl_top2:
			orderIdNumber = 1;
			selected = 2;
			tv_number2.setTextSize(16);
			tv_money2.setTextSize(16);
			tv_number2.setTextColor(csl);
			tv_money2.setTextColor(csl);
			iv_background2.setBackgroundResource(R.color.retexton);
			tv_sum.setText(tv_money2.getText());
			backToOrigin();
			break;
		case R.id.rl_top3:
			orderIdNumber = 2;
			selected = 3;
			tv_number3.setTextSize(16);
			tv_money3.setTextSize(16);
			tv_number3.setTextColor(csl);
			tv_money3.setTextColor(csl);
			iv_background3.setBackgroundResource(R.color.retexton);
			tv_sum.setText(tv_money3.getText());
			backToOrigin();
			break;
		case R.id.rl_top4:
			orderIdNumber = 3;
			selected = 4;
			tv_number4.setTextSize(16);
			tv_money4.setTextSize(16);
			tv_number4.setTextColor(csl);
			tv_money4.setTextColor(csl);
			iv_background4.setBackgroundResource(R.color.retexton);
			tv_sum.setText(tv_money4.getText());
			backToOrigin();
			break;
		case R.id.rl_top5:
			orderIdNumber = 4;
			selected = 5;
			tv_number5.setTextSize(16);
			tv_money5.setTextSize(16);
			tv_number5.setTextColor(csl);
			tv_money5.setTextColor(csl);
			iv_background5.setBackgroundResource(R.color.retexton);
			tv_sum.setText(tv_money5.getText());
			backToOrigin();
			break;
		case R.id.rl_top6:
			orderIdNumber = 5;
			selected = 6;
			tv_number6.setTextSize(16);
			tv_money6.setTextSize(16);
			tv_number6.setTextColor(csl);
			tv_money6.setTextColor(csl);
			iv_background6.setBackgroundResource(R.color.retexton);
			tv_sum.setText(tv_money6.getText());
			backToOrigin();
			break;
		case R.id.tv_go:
			// if(orderIdNumber==-1){
			// Toast.makeText(RechargeintegralActivity.this, "请选择充值数量",
			// 0).show();
			// }else if(!isCheck){
			// Toast.makeText(RechargeintegralActivity.this, "请选择支付方式",
			// 0).show();
			// }else{
			// createOrder();
			// }
			//
			if (orderIdNumber == -1) {
				Toast.makeText(RechargeintegralActivity.this, "请选择充值数量", 0)
						.show();
			} else {
				createOrder();
			}

			break;

		default:
			break;
		}

	}

	public void backToOrigin() {
		if (selected != 1) {
			tv_number1.setTextSize(14);
			tv_money1.setTextSize(14);
			tv_number1.setTextColor(csl2);
			tv_money1.setTextColor(csl2);
			iv_background1.setBackgroundResource(R.color.retext);
		}
		if (selected != 2) {
			tv_number2.setTextSize(14);
			tv_money2.setTextSize(14);
			tv_number2.setTextColor(csl2);
			tv_money2.setTextColor(csl2);
			iv_background2.setBackgroundResource(R.color.retext);
		}
		if (selected != 3) {
			tv_number3.setTextSize(14);
			tv_money3.setTextSize(14);
			tv_number3.setTextColor(csl2);
			tv_money3.setTextColor(csl2);
			iv_background3.setBackgroundResource(R.color.retext);
		}
		if (selected != 4) {
			tv_number4.setTextSize(14);
			tv_money4.setTextSize(14);
			tv_number4.setTextColor(csl2);
			tv_money4.setTextColor(csl2);
			iv_background4.setBackgroundResource(R.color.retext);
		}
		if (selected != 5) {
			tv_number5.setTextSize(14);
			tv_money5.setTextSize(14);
			tv_number5.setTextColor(csl2);
			tv_money5.setTextColor(csl2);
			iv_background5.setBackgroundResource(R.color.retext);
		}
		if (selected != 6) {
			tv_number6.setTextSize(14);
			tv_money6.setTextSize(14);
			tv_number6.setTextColor(csl2);
			tv_money6.setTextColor(csl2);
			iv_background6.setBackgroundResource(R.color.retext);
		}
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

	// public class IntegralListViewAdapter extends BaseAdapter {
	// @Override
	// public int getCount() {
	// return integral.size();
	// }
	//
	// @Override
	// public Object getItem(int arg0) {
	// return integral.get(arg0);
	// }
	//
	// @Override
	// public long getItemId(int arg0) {
	// return arg0;
	// }
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// ItemInfoHolder itemInfoHolder=new ItemInfoHolder();
	// if(convertView==null){
	// convertView = View.inflate(RechargeintegralActivity.this,
	// R.layout.activity_rechargeintegra_item, null);
	//
	// RelativeLayout
	// rl_all=(RelativeLayout)convertView.findViewById(R.id.rl_all);
	// itemInfoHolder.tv_number = (TextView)
	// convertView.findViewById(R.id.tv_number);
	// itemInfoHolder.tv_money = (TextView)
	// convertView.findViewById(R.id.tv_money);
	// itemInfoHolder.iv_background = (ImageView)
	// convertView.findViewById(R.id.iv_background);
	// itemInfoHolder.tv_number.setText(integral.get(position).getNumber()+"乐豆");
	// itemInfoHolder.tv_money.setText("￥"+integral.get(position).getNumber());
	// final ItemInfoHolder info=itemInfoHolder;
	// // rl_all.setOnClickListener(new OnClickListener() {
	// //
	// // @Override
	// // public void onClick(View v) {
	// // info.iv_background.setBackgroundResource(R.color.blue);
	// // info.tv_number.setTextSize(30);
	// // info.tv_money.setTextSize(30);
	// // }
	// // });
	//
	//
	// convertView.setOnFocusChangeListener(new OnFocusChangeListener() {
	//
	// @Override
	// public void onFocusChange(View v, boolean hasFocus) {
	// if(hasFocus){
	// info.iv_background.setBackgroundResource(R.color.blue);
	// info.tv_number.setTextSize(30);
	// info.tv_money.setTextSize(30);
	// }else{
	// info.iv_background.setBackgroundResource(R.color.bootom_bg);
	// info.tv_number.setTextSize(20);
	// info.tv_money.setTextSize(20);
	// }
	// }
	// });
	// convertView.setTag(itemInfoHolder);
	// }else{
	// itemInfoHolder=(ItemInfoHolder) convertView.getTag();
	//
	// }
	// return convertView;
	// }
	//
	//
	// }
	// static class ItemInfoHolder {
	// TextView tv_number, tv_money;
	// ImageView iv_background;
	// }
	@Override
	protected void onDestroy() {
		if (integralList != null)
			integralList = null;
		if(integralHandler!=null)
			integralHandler.removeCallbacksAndMessages(null);
		integralHandler = null;
		createOrderBean = null;
		super.onDestroy();
	}
	/**
	 * 处理积分充值比例数据
	 * @param backdata
	 */
	private void useWayHandleRecharge(String backdata) {
		// TODO Auto-generated method stub
		integralList = (ArrayList<GetIntegralBean>) JsonUtil
				.parseJsonToList(backdata,
						new TypeToken<List<GetIntegralBean>>() {
						}.getType());
		if (integralList == null) {
			rl_top6.setVisibility(View.GONE);
			rl_top5.setVisibility(View.GONE);
			rl_top4.setVisibility(View.GONE);
			rl_top3.setVisibility(View.GONE);
			rl_top2.setVisibility(View.GONE);
			rl_top1.setVisibility(View.GONE);
			return;
		}
		if (integralList.size() < 5) {
			rl_top5.setVisibility(View.GONE);
			view4.setVisibility(View.GONE);
		}
		if (integralList.size() > 5) {
			rl_top6.setVisibility(View.VISIBLE);
		}
		DecimalFormat df = new DecimalFormat("#0.00");
		for (int i = 0; i < integralList.size(); i++) {
			switch (i) {
			case 0:
				tv_number1.setText(integralList.get(0)
						.getIntegralRechargeCount() + "乐豆");
				tv_money1.setText("￥"
						+ df.format(integralList.get(0).getMoney()));
				break;
			case 1:
				tv_number2.setText(integralList.get(1)
						.getIntegralRechargeCount() + "乐豆");
				tv_money2.setText("￥"
						+ df.format(integralList.get(1).getMoney()));
				break;
			case 2:
				tv_number3.setText(integralList.get(2)
						.getIntegralRechargeCount() + "乐豆");
				tv_money3.setText("￥"
						+ df.format(integralList.get(2).getMoney()));
				break;
			case 3:
				tv_number4.setText(integralList.get(3)
						.getIntegralRechargeCount() + "乐豆");
				tv_money4.setText("￥"
						+ df.format(integralList.get(3).getMoney()));
				break;
			case 4:
				tv_number5.setText(integralList.get(4)
						.getIntegralRechargeCount() + "乐豆");
				tv_money5.setText("￥"
						+ df.format(integralList.get(4).getMoney()));
				break;
			case 5:
				tv_number6.setText(integralList.get(5)
						.getIntegralRechargeCount() + "乐豆");
				tv_money6.setText("￥"
						+ df.format(integralList.get(5).getMoney()));
				break;

			default:
				break;
			}
		}
	};
}
