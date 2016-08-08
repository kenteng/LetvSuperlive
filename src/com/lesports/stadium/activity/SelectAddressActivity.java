package com.lesports.stadium.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import com.lesports.stadium.R;
import com.lesports.stadium.adapter.SelectAddressAdapter;
import com.lesports.stadium.bean.ShippingAddressBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;

/**
 * 
 * ***************************************************************
 * 
 * @ClassName: SelectAddressActivity
 * 
 * @Desc : 选择收获地址
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 邓聪聪
 * 
 * @data: 2016年3月16日 10:11:43
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
@SuppressLint("HandlerLeak") public class SelectAddressActivity extends Activity implements OnClickListener {
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
	private SelectAddressAdapter mAdapter;
	/**
	 * 管理收货地址
	 */
	private TextView manageAddressTv;
	/**
	 * 用户收货地址id
	 */
	private String id="";
	/**
	 * handle中需要用到的标记
	 */
	private final int USER_ADDRESS=0;
	/**
	 * 存储收货地址
	 */
	private List<ShippingAddressBean> addressList = new ArrayList<ShippingAddressBean>();
	private Handler selectHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case USER_ADDRESS:
				String result = (String) msg.obj;
				if(!TextUtils.isEmpty(result)){
					useWayHandleAddress(result);
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
		setContentView(R.layout.activity_selectaddress);
		// 添加布局
		// initDate();
		Intent mIntent=this.getIntent();
		id=mIntent.getStringExtra("id");
		initView();
	//	requestShippingAddress();
	}

	/**
	 * 添加数据
	 */
	@SuppressWarnings("unused")
	private void initDate() {
		ShippingAddressBean temp = new ShippingAddressBean();
		temp.setUserName("晶朝");
		temp.setUserPhone("18210987823");
		temp.setUserCity("北京市海淀区");
		temp.setUserStreet("西二旗国际创业园");
		temp.setUserAddress("2号院1号楼12D");
		temp.setPostcode("100000");
		addressList.add(temp);
	}

	private void initView() {
		// 添加标题
		mGobackImg = (ImageView) findViewById(R.id.title_left_iv);
		mGobackImg.setOnClickListener(this);
		mTitleContentTv = (TextView) findViewById(R.id.tv_title);
		mTitleContentTv.setText("选择收货地址");
		mAddressLv = (ListView) findViewById(R.id.address_shipping_lv);
		manageAddressTv = (TextView) findViewById(R.id.tv_btn);
		manageAddressTv.setText("管理");
		manageAddressTv.setVisibility(View.VISIBLE);
		manageAddressTv.setOnClickListener(this);
		mAddressLv = (ListView) findViewById(R.id.address_shipping_lv);
		mAdapter = new SelectAddressAdapter(this, addressList,id);
		mAddressLv.setAdapter(mAdapter);
		mAddressLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//  点击选择收货地址，返回
				ShippingAddressBean selectBean = new ShippingAddressBean();
				selectBean = addressList.get(position);
				Intent intent = new Intent();
				intent.putExtra("addressBean", (Serializable) selectBean);
				SelectAddressActivity.this.setResult(2, intent);
				finish();
			}
		});
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
		case R.id.tv_btn:
			// 管理收货地址
			Intent manageIntent = new Intent(SelectAddressActivity.this,
					ManageShippingAddressActivity.class);
			startActivity(manageIntent);
			break;

		default:
			break;
		}
	}

	/**
	 * 获取收货地址
	 */
	private void requestShippingAddress() {
		Map<String, String> params = new HashMap<String, String>();
	 params.put("userId", GlobalParams.USER_ID); // userId
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.GET_ALL_ADDRESS, params, new GetDatas() {

					@SuppressWarnings("null")
					@Override
					public void getServerData(BackData data) {
						if (data == null) {
							// 说明网络获取无数据
							Toast.makeText(SelectAddressActivity.this, "网络错误",
									Toast.LENGTH_SHORT).show();
						} else {
							Object obj = data.getObject();
							if (obj == null) {
								Toast.makeText(SelectAddressActivity.this,
										"网络错误", Toast.LENGTH_SHORT).show();
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
									Toast.makeText(SelectAddressActivity.this,
											"网络异常", Toast.LENGTH_SHORT).show();
								} else {
									Log.i("ADDRESS",
											GlobalParams.USER_ID + "获取所有收货地址成功"
													+ data.getNetResultCode()
													+ backdata);
									Message msg = new Message();
									msg.what = USER_ADDRESS;
									if(data.getNetResultCode()==0){
										msg.obj = backdata;
									}else{
										msg.obj="网络异常";
									}
									selectHandler.sendMessage(msg);
								}
							}

						}
					}
				}, false, false);

	}
	@Override
	protected void onResume() {
		super.onResume();
		requestShippingAddress();
	}
	/**
	 * 处理获取下来的用户收货地址
	 * @param result
	 */
	private void useWayHandleAddress(String result) {
		// TODO Auto-generated method stub

		if (result.contains("id")) {
			addressList.clear();
			try {
				JSONArray listArray = new JSONArray(result);
				Log.d("DCC", listArray.length() + listArray.toString());
				for (int z = 0; z < listArray.length(); z++) {
					JSONObject aObj = listArray.getJSONObject(z);
					Log.i("DCC", z + aObj.toString());
					ShippingAddressBean tempAddress = new ShippingAddressBean();
					tempAddress.setUserName(aObj.getString("name"));
					tempAddress.setUserPhone(aObj
							.getString("mobilePhone"));
					tempAddress.setPostcode(aObj.getString("zipcode"));
					tempAddress.setUserCity(aObj
							.getString("cityAddress"));
					tempAddress.setProvinceId(aObj
							.getString("provinceId"));
					tempAddress.setCityId(aObj.getString("cityId"));
					tempAddress.setAreaId(aObj.getString("areaId"));
					tempAddress.setUserStreet(aObj
							.getString("streedAddress"));
					tempAddress.setUserAddress(aObj
							.getString("address"));
					tempAddress.setIsDefault(aObj
							.getString("isDefault"));
					tempAddress.setId(aObj
							.getString("id"));
					if (aObj.getString("isDefault").equals("1")) {
						addressList.add(0, tempAddress);
					} else {
						addressList.add(tempAddress);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Log.e("DCC", addressList.size() + addressList.toString());
			mAdapter.setAddressList(addressList);
			mAdapter.notifyDataSetChanged();
		} else {
			// 提示系统返回信息
			/*Toast.makeText(SelectAddressActivity.this, "网络异常", 0)
					.show();*/
		}
	};
}
