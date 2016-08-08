package com.lesports.stadium.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lesports.stadium.R;
import com.lesports.stadium.adapter.TickOrderAdapter;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.PayBean;
import com.lesports.stadium.bean.TickOrderBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.WifiUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * ***************************************************************
 * 
 * @Desc : 永乐购票成功后跳转到该界面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 王新年
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
public class TickOrderActivity extends Activity implements OnClickListener {
	// 购票人的名称
	private TextView receiverName;
	// 购票人的手机号
	private TextView receiverPhone;
	// 购票人的地址
	private TextView address;
	// 票名称
	private TextView tick_name;
	// 商品总量
	private TextView textAmount;
	// 商品总价格
	private TextView textAllprize;
	// 存储接收人的名称
	private String receName;
	// 存储接收人的手机号
	private String recePhone;
	// 存储接收人的邮箱
	private String receEmail;
	// 存储接收人的地址
	private String receAdress;
	// 票名称
	private String tickName;
	// 座位说明
	private String seatNumber;
	
	//商品数量
	private String wareNumber;
	//商品总额（商品价格+运费）
	private String amount;
	//商品总个数
	private String orderAmount;
	//运费
	private String freight;
	private boolean istiaozuan = true;
	private ArrayList<TickOrderBean> listbean;
	
	// 票简介
	private String tickInstruction;
	// 获取订单详情成功
	private final int SUCCESS_DATA = 111;
	// 获取订单详情失败
	private final int FILUE_NET_DATE = 222;
	
	private TickOrderAdapter adapter;
	
	private ListView tick_list;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS_DATA:
				// 获取订单详情成功
				anOrderJsonData((String) msg.obj);
				break;
			case FILUE_NET_DATE:
				// 获取订单详情成功
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tick_order);
		initDate();
		initView();
		initListener();
	}

	// 设置监听事件
	private void initListener() {
		findViewById(R.id.back_tick_order).setOnClickListener(this);
		findViewById(R.id.dingche).setOnClickListener(this);

	}

	// 初始化控件view
	private void initView() {
		receiverName = (TextView) findViewById(R.id.cdengdaimaijiashouhuo_shouhuoren_name);
		receiverPhone = (TextView) findViewById(R.id.cdengdaimaijiashouhuo_shouhuoren_dianhua);
		address = (TextView) findViewById(R.id.cdengdaimaijiashouhuo_shouhuoren_dizhi);
		tick_name = (TextView) findViewById(R.id.tick_name);
		textAmount = (TextView) findViewById(R.id.amount);
		textAllprize = (TextView) findViewById(R.id.allprize);
		tick_list = (ListView) findViewById(R.id.tick_list);
		adapter = new TickOrderAdapter(getApplicationContext());
		tick_list.setAdapter(adapter);
		// 设置值
	}

	// 初始化传递来的数据
	private void initDate() {
		Intent intent = getIntent();
		if (intent != null) {
			String orderNumber = intent.getStringExtra("orderNumber");
			Log.i("wxn", "orderNumber:"+orderNumber);
			getOrderMessage(orderNumber);
		}
//		getOrderMessage("12404153");
	}

	private void setData() {
		String allprize;
		if(TextUtils.isEmpty(freight)){
			allprize = "合计：￥"+amount+"(含运费￥0)";
		}else{
			allprize = "合计：￥"+amount+"(含运费￥"+freight+")";
		}
		textAllprize.setText(allprize);
		int countss = 0;
		for(TickOrderBean orderbean:listbean)
		{
			String warNumber = orderbean.getWareNumber();
			if(!TextUtils.isEmpty(warNumber)){
				countss += Integer.parseInt(warNumber);
			}
		}
		String mount = "共"+countss+"件商品";
		textAmount.setText(mount);
		receiverName.setText(receName);
		receiverPhone.setText(recePhone);
		if(TextUtils.isEmpty(receAdress))
			receAdress = "";
		address.setText("收货地址：" + receAdress);
		tick_name.setText(tickName);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_tick_order:
			finish();
			break;
		case R.id.dingche:
			if(!istiaozuan)
				return;
			istiaozuan = false;
			LApplication applic = (LApplication) getApplication();
			List<Activity> mList  = applic.getActivityList();
			if(mList!=null){
				for(int i= mList.size()-1;i>0;i--){
					if(!mList.get(i).getClass().equals(MainActivity.class)){
						mList.get(i).finish();
					}else{
						break;
					}
				}
//				MainActivity.instance.tag_view=2;
//				MainActivity.instance.initIntentData("yongche");
				Intent intent=new Intent(TickOrderActivity.this,UserCarActivity.class);
				TickOrderActivity.this.startActivity(intent);
			}
			break;
		default:
			break;
		}

	}

	// 根据永乐生成的订单号。查询订单信息
	private void getOrderMessage(String orderNumber) {
		Map<String, String> params = new HashMap<>();
		params.put("orderNumber", orderNumber);// 默认值
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ORDER_MESSAGE_NUMBER, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if (data != null) {
							Object obj = data.getObject();
							if (obj != null) {
								Message msg = new Message();
								msg.obj = obj;
								msg.what = SUCCESS_DATA;
								handler.sendMessage(msg);
								Log.i("wxn", "购票成功界面获取数据...." + obj);
								return;
							}
						}
						Message msg = new Message();
						msg.arg1 = FILUE_NET_DATE;
						handler.sendMessage(msg);
					}
				}, false, false);
	}

	/**
	 * 解析获取下来的真正的支付参数
	 * 
	 * @param jsondatass
	 */
	private void anOrderJsonData(String jsondatass) {
		try {
			if (jsondatass != null && !TextUtils.isEmpty(jsondatass)) {
				JSONObject obj = new JSONObject(jsondatass);
				if(obj.has("freight")){
					freight = obj.getString("freight");
				}
				if(obj.has("amount")){
					amount = obj.getString("amount");
				}
				if(obj.has("orderAmount")){
					orderAmount = obj.getString("orderAmount");
				}
				if (obj.has("position")) {
					String position = obj.getString("position");
					JSONObject posObj = new JSONObject(position);
					if (posObj.has("userName")) {
						receName = posObj.getString("userName");
					}
					if (posObj.has("telePhone")) {
						recePhone = posObj.getString("telePhone");
					}
					if (posObj.has("address")) {
						receAdress = posObj.getString("address");
					}
					
				}

				if (obj.has("orderDetailList")) {
					String orderList = obj.getString("orderDetailList");
					JSONArray orderArray = new JSONArray(orderList);
					int count = orderArray.length();
					listbean = new ArrayList<>();
					for (int i = 0; i < count; i++) {
						TickOrderBean  bean = new TickOrderBean();
						JSONObject orderObj = orderArray.getJSONObject(i);
						if (orderObj.has("pinfo")) {
							String pinfo = orderObj.getString("pinfo");
							JSONObject pinfoObj = new JSONObject(pinfo);
							if (pinfoObj.has("picture")) {
								bean.setPicUrl(pinfoObj.getString("picture"));
							}
							if (pinfoObj.has("productName")) {
								
								tickName = pinfoObj.getString("productName");
							}
							if (pinfoObj.has("seatNumber")) {
								bean.setSeatNumber(pinfoObj.getString("seatNumber"));
							}
							if (pinfoObj.has("priceTag")) {
								bean.setPrice(pinfoObj.getString("priceTag"));
							}
						}
						if (orderObj.has("wareNumber")) {
							bean.setWareNumber(orderObj.getString("wareNumber"));
						}
						listbean.add(bean);
					}
				}

			}
			adapter.setDate(listbean);
			setData();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	

}
