package com.lesports.stadium.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lesports.stadium.R;
import com.lesports.stadium.adapter.ShippingAddressAdapter;
import com.lesports.stadium.bean.Address;
import com.lesports.stadium.bean.ShippingAddressBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ***************************************************************
 * 
 * @ClassName: ManageShippingAddressActivity
 * 
 * @Desc : 管理收货地址
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 邓聪聪
 * 
 * @data:2016-3-23 下午2:50:06
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class ManageShippingAddressActivity extends Activity implements
		OnClickListener {
	/**
	 * 返回按钮
	 */
	private ImageView mGobackImg;
	/**
	 * 标题内容
	 */
	private TextView mTitleContentTv;
	/**
	 * 收货地址列表
	 */
	private ListView mAddressLv;
	/**
	 * 收货地址适配器
	 */
	private ShippingAddressAdapter mAdapter;
	/**
	 * 收获地址数据存储
	 */
	private List<ShippingAddressBean> addressList = new ArrayList<ShippingAddressBean>();
	/**
	 * handle中所使用的标记
	 */
	private final int ADDRESS_LIST=0;
	/**
	 * handler消息处理
	 */
	private Handler addressHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ADDRESS_LIST:
				//获取所有收货地址
				String result=(String) msg.obj;
				if(!TextUtils.isEmpty(result)){
					useWayHandleAddress(result);
				}else{
				}
				break;
			case 1:
				break;
			default:
				break;
			}
		}

		/**
		 * 处理收货地址
		 * @param result
		 */
		private void useWayHandleAddress(String result) {
			// TODO Auto-generated method stub
			addressList.clear();
			try {
				JSONArray listArray=new JSONArray(result);
				for(int i=0;i<listArray.length();i++){
					JSONObject aObj=listArray.getJSONObject(i);
					ShippingAddressBean tempAddress=new ShippingAddressBean();
					tempAddress.setUserName(aObj.getString("name"));
					tempAddress.setUserPhone(aObj.getString("mobilePhone"));
					tempAddress.setPostcode(aObj.getString("zipcode"));
					tempAddress.setUserCity(aObj.getString("cityAddress"));
					tempAddress.setProvinceId(aObj.getString("provinceId"));
					tempAddress.setCityId(aObj.getString("cityId"));
					tempAddress.setAreaId(aObj.getString("areaId"));
					tempAddress.setUserStreet(aObj.getString("streedAddress"));
					tempAddress.setUserAddress(aObj.getString("address"));
					tempAddress.setIsDefault(aObj.getString("isDefault"));
					tempAddress.setId(aObj.getString("id"));
					Log.i("DCCid", tempAddress.getId());
					if(aObj.getString("isDefault").equals("1")){
						addressList.add(0,tempAddress);
					}else{
						addressList.add(tempAddress);
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		mAdapter.notifyDataSetChanged();
		};
	};
	/**
	 * 添加收货地址
	 */
	private TextView addAddressTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manageaddress);
		// 添加布局
		initView();
	//	requestDefaultAddress();
		//requestDefaultAddress();
	//	requestAllAddress();
	}

	private void initView() {
		// 添加标题
		mGobackImg = (ImageView) findViewById(R.id.title_left_iv);
		mGobackImg.setOnClickListener(this);
		mTitleContentTv = (TextView) findViewById(R.id.tv_title);
		mTitleContentTv.setText("管理收货地址");
		mAddressLv = (ListView) findViewById(R.id.address_shipping_lv);
		mAdapter=new ShippingAddressAdapter(this, addressList);
		mAddressLv.setAdapter(mAdapter);
		mAddressLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent changeIntent=new Intent(ManageShippingAddressActivity.this,ChangeAddressActivity.class);
				changeIntent.putExtra("ShippingAddressBean", addressList.get(position));
				startActivity(changeIntent);
			}
		});
		addAddressTv = (TextView) findViewById(R.id.address_add_tv);
		addAddressTv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_iv:
			finish();
			break;
		case R.id.tv_title:
			// 标题栏

			break;
		case R.id.address_add_tv:
			// 添加收货地址
			Intent addIntent = new Intent(ManageShippingAddressActivity.this,
					AddShippingAddressActivity.class);
			startActivity(addIntent);
			break;
		default:
			break;
		}
	}

	/**
	 * 获取默认收货地址
	 */
	private void requestDefaultAddress() {
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("userId",GlobalParams.USER_ID); //userId
		String ss="http://interface.jingzhaokeji.com:8070/letv_mgr/activity/shakeShakeCount.do";
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.GET_DEFAULT_ADDRESS, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						// TODO Auto-generated method stub
						if (data == null) {
							// 说明网络获取无数据
							Toast.makeText(ManageShippingAddressActivity.this,
									"网络错误", Toast.LENGTH_SHORT).show();
						} else {
							Object obj = data.getObject();
							if (obj == null) {
								Toast.makeText(
										ManageShippingAddressActivity.this,
										"网络错误", Toast.LENGTH_SHORT).show();
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
									Toast.makeText(
											ManageShippingAddressActivity.this,
											"网络异常", Toast.LENGTH_SHORT).show();
								} else {
									Log.i("ADDRESS",
											GlobalParams.USER_ID+"获取默认收货地址成功:"
													+ data.getNetResultCode()
													+ backdata);
									Message msg=new Message();
									msg.what=1;
									msg.obj=backdata;
									addressHandler.sendMessage(msg);
								}
							}

						}
					}
				}, false, false);
	}

	/**
	 * 获取所有收货地址列表
	 */
	private void requestAllAddress() {
		Map<String, String> params = new HashMap<String, String>();
//		params.put("userId",GlobalParams.USER_ID); //userId
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.GET_ALL_ADDRESS, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if (data != null&& data.getNetResultCode()==0) {
							Object obj = data.getObject();
							if(obj!=null){
								String backdata = obj.toString();
								Message msg=new Message();
								msg.what=0;
								msg.obj=backdata;
								addressHandler.sendMessage(msg);
								return;
							}
						} 
						Toast.makeText(ManageShippingAddressActivity.this,
								"服务器繁忙，请稍后重试", Toast.LENGTH_SHORT).show();
					}
				}, false, false);
	}
	@Override
	protected void onResume() {
		super.onResume();
		requestAllAddress();
	}
	
	@Override
	protected void onDestroy() {
		if (addressHandler != null) {
			addressHandler.removeCallbacksAndMessages(null);
		}
		super.onDestroy();
	}

}
