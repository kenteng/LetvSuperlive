package com.lesports.stadium.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lesports.stadium.R;
import com.lesports.stadium.bean.CityBean;
import com.lesports.stadium.bean.ShippingAddressBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.Utils;
import com.lesports.stadium.view.CustomDialog;
import com.lesports.stadium.view.WiperSwitch;
import com.lesports.stadium.view.WiperSwitch.OnChangedListener;
import com.lesports.stadium.view.location.XmlParserJSON;
import com.lesports.stadium.view.location.model.CityModel;
import com.lesports.stadium.view.location.model.DistrictModel;
import com.lesports.stadium.view.location.model.ProvinceModel;
import com.lesports.stadium.view.location.widget.OnWheelChangedListener;
import com.lesports.stadium.view.location.widget.WheelView;
import com.lesports.stadium.view.location.widget.adapter.ArrayWheelAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

/**
 * ***************************************************************
 * @ClassName:  ChangeAddressActivity 
 * 
 * @Desc : 修改收货地址
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 邓聪聪
 * 
 * @data:2016-4-2 下午5:55:37
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */
@SuppressLint({ "HandlerLeak", "InflateParams" }) public class ChangeAddressActivity  extends Activity implements OnClickListener ,OnWheelChangedListener {
	/**
	 * 返回按钮
	 */
	private ImageView mGobackImg;
	/**
	 * 标题内容
	 */
	private TextView mTitleContentTv;
	/**
	 * 修改保存
	 */
	private TextView mChangeTv;
	private TextView mDeleteTv;
	/**
	 * 存储地址
	 */
	private TextView add_usercity;
	/**
	 * 字数限制
	 */
	private int BigIndex = 12;
	/**
	 * 保存地址
	 */
	private TextView saveAddressTv;
	/**
	 * 收货人姓名
	 */
	private EditText userNameEt;
	/**
	 * 收货人电话号码
	 */
	private EditText userPhoneEt;
	/**
	 * 收货人邮编
	 */
	private EditText userPostCodeEt;
	/**
	 * 收货人街道地址，暂时取消
	 */
//	private EditText userStreetEt;
	/**
	 * 收货地址，详细地址
	 */
	private EditText userAddressEt;
	/**
	 * 是否为默认地址
	 */
	private WiperSwitch userDefaultAddress;
	/**
	 * 判断类型
	 */
	private boolean isDefaultAddress = false;
	/**
	 *当前收货地址
	 */
	private ShippingAddressBean addressBean=new ShippingAddressBean();
	/**
	 * 存储省市区信息
	 */
	@SuppressWarnings("unused")
	private List<CityBean> data;
	/**
	 * 省市区json文件名称
	 */
	@SuppressWarnings("unused")
	private final static String fileName = "citycode.json";
	/**
	 * 所有省
	 */
	protected String[] mProvinceDatas;
	/**
	 * key - 省 value - 市
	 */
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区
	 */
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

	/**
	 * key - 区 values - 邮编
	 */
	protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();
	/**
	 * key - 省 values - ID
	 */
	protected Map<String, String> mProviceIDsMap = new HashMap<String, String>();
	/**
	 * key - 市values - ID
	 */
	protected Map<String, String> mCitisIDsMap = new HashMap<String, String>();
	/**
	 * key - 区 values - ID
	 */
	protected Map<String, String> mDistrictIDsMap = new HashMap<String, String>();

	/**
	 * 当前省的名称
	 */
	protected String mCurrentProviceName;
	
	/**
	 * 当前省ID
	 */
	protected String mCurrentProviceId;
	/**
	 * 当前市的名称
	 */
	protected String mCurrentCityName;
	/**
	 * 当前市的id
	 * 
	 */
	protected String mCurrentCityId;
	/**
	 * 当前区的名称
	 */
	protected String mCurrentDistrictName = "";
	/**
	 * 当前区的id
	 * 
	 */
	protected String mCurrentDistrictId = "";
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private PopupWindow window;
	/**
	 * 软键盘管理类
	 */
	private InputMethodManager inputManager;

	/**
	 * 当前区的邮政编码
	 */
	protected String mCurrentZipCode = "";
	/**
	 * handler处理网络请求
	 */
	private Handler handler=new Handler(){public void handleMessage(Message msg) {
		switch (msg.what) {
		case 0:
			//删除收货地址
			finish();
		
			break;
		case 1:
			String msgStr=(String) msg.obj;
			mDiaLog=null;
			mDiaLog=new CustomDialog(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mDiaLog.dismiss();
					finish();
				}
			});
			mDiaLog.setRemindMessage(msgStr);
			mDiaLog.setConfirmTxt("确认");
			mDiaLog.setRemindTitle("温馨提示");
			mDiaLog.show();
		default:
			break;
		}
	};};
	private CustomDialog mDiaLog;
	private CustomDialog mDeleteLog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addshippingaddress);
		Intent intent=this.getIntent();
		addressBean=(ShippingAddressBean) intent.getSerializableExtra("ShippingAddressBean");
		initView();
		showPopwindow();
		setUpData();
		inputManager = (InputMethodManager) this.getSystemService(
				"input_method");
	}
	private void initView(){
		// 添加标题
		mGobackImg = (ImageView) findViewById(R.id.title_left_iv);
		mGobackImg.setOnClickListener(this);
		mTitleContentTv = (TextView) findViewById(R.id.tv_title);
		if(addressBean.getIsDefault().equals("1")){
			mTitleContentTv.setText("默认收货地址");
		}else{
			mTitleContentTv.setText("收货地址");
		}
		mChangeTv=(TextView)findViewById(R.id.tv_btn);
		mChangeTv.setText("修改");
		mChangeTv.setVisibility(View.VISIBLE);
		mChangeTv.setOnClickListener(this);
		mDeleteTv=(TextView)findViewById(R.id.add_address_delete);
		mDeleteTv.setOnClickListener(this);
		//隐藏保存按钮
		saveAddressTv = (TextView) findViewById(R.id.add_save_address_tv);
		saveAddressTv.setVisibility(View.GONE);
	//	saveAddressTv.setOnClickListener(this);
		add_usercity = (TextView)findViewById(R.id.add_usercity);
		add_usercity.setText(addressBean.getUserCity());
		add_usercity.setOnClickListener(this);
		
		userNameEt = (EditText) findViewById(R.id.add_username);
		userNameEt.setSelection(0);
		CharSequence text = userNameEt.getText();
		if (text instanceof Spannable) {
		Spannable spanText = (Spannable)text;
		Selection.setSelection(spanText, text.length());}
		userNameEt.addTextChangedListener(new EditTextWatcher());
		userPhoneEt = (EditText) findViewById(R.id.add_userphone);
		userPostCodeEt = (EditText) findViewById(R.id.add_userpostcode);
	//	userStreetEt = (EditText) findViewById(R.id.add_userstreet);
		userAddressEt = (EditText) findViewById(R.id.add_useraddress);
		userDefaultAddress = (WiperSwitch) findViewById(R.id.add_save_default_switch);
		
		userDefaultAddress.setOnChangedListener(new OnChangedListener() {
			@Override
			public void OnChanged(WiperSwitch wiperSwitch, boolean checkState) {
				if (checkState) {
					isDefaultAddress = true;
				} else {
					isDefaultAddress = false;
				}
			}
		});
		userDefaultAddress.setChecked(false);
		userNameEt.setText(addressBean.getUserName());
		userPhoneEt.setText(addressBean.getUserPhone());
		userPostCodeEt.setText(addressBean.getPostcode());
		userAddressEt.setText(addressBean.getUserAddress());
		if(addressBean.getIsDefault().equals("0")){
			userDefaultAddress.setChecked(false);
		}else{
			userDefaultAddress.setChecked(true);
		}
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
			//修改按钮
			checkAddress();
			break;
		case R.id.add_save_address_tv:
			// 保存地址,下方显示，隐藏不用
		//	Toast.makeText(ChangeAddressActivity.this, "点击了", 0).show();
			checkAddress();
			// finish();
			break;
		case R.id.add_address_delete:
			//删除收货地址
			showDeleteLog();
			break;
		case R.id.add_usercity:
			inputManager.hideSoftInputFromWindow(ChangeAddressActivity.this.getWindow()
					.getDecorView().getWindowToken(), 0);
			show();
			break;

		default:
			break;
		}
	}
	/**
	 * 删除收货地址
	 */
	private void deleteAddress(){


		Map<String, String> params = new HashMap<String, String>();
		 params.put("ids", addressBean.getId()); //收货地址id
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.DELETE_ADDRESS, params, new GetDatas() {

					@SuppressWarnings("null")
					@Override
					public void getServerData(BackData data) {
						if (data == null) {
							// 说明网络获取无数据
							Toast.makeText(ChangeAddressActivity.this, "网络错误", Toast.LENGTH_SHORT)
									.show();
						} else {
							Object obj = data.getObject();
							if (obj == null) {
								Toast.makeText(ChangeAddressActivity.this, "网络错误", Toast.LENGTH_SHORT)
										.show();
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
									Toast.makeText(ChangeAddressActivity.this, "网络异常",
											Toast.LENGTH_SHORT).show();
								} else {
									// 调用方法，给listview加载数据，先将数据通过handler发送到主线程中
									Message msg = new Message();
									msg.what = 0;
									if(data.getNetResultCode()==0){
										msg.obj = "删除成功";
									}else{
										msg.obj = "删除失败";
									}
									handler.sendMessage(msg);
								}
							}

						}
					}
				}, false, false);
	}
	/**
	 * 请求修改收获地址信息
	 */
	private void requestChange(String name, String provinceId,
			String cityId, String areaId, String address, String cityAddress,
			String mobilePhone, String zipcode,
			String isDefault){
		Log.i("wxn", "provinceId:."+provinceId+" cityId.."+cityId+"  areaId.."+areaId);
		final Map<String, String> params = new HashMap<String, String>();
	//	params.put("userId", GlobalParams.USER_ID); // 地址
		params.put("id", addressBean.getId()); // 地址
		params.put("name", name); // 地址
		params.put("provinceId", provinceId); // 详细地址
		params.put("cityId", cityId); // 地址类型
		params.put("areaId", areaId); // 车型
		params.put("address", address); // 详细地址
		params.put("cityAddress", cityAddress); // 纬度
		params.put("streedAddress", "ABC"); // 街道
		params.put("mobilePhone", mobilePhone); // 城市
		params.put("telphone", "9999999"); // 城市
		params.put("zipcode", zipcode); // 城市
		params.put("isDefault", isDefault); // 城市
		// params.put("mapType", "1"); //地图类型
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.CHANGE_SHIIPPING_ADDRESS, params, new GetDatas() {

					@SuppressWarnings("null")
					@Override
					public void getServerData(BackData data) {
						Log.i("wxn", "地址修改："+data.getNetResultCode()+"..."+data.getObject());
						Message msg=new Message();
						msg.what=1;
						if (data != null&&data.getNetResultCode()==0) {
							msg.obj="修改成功";
						} else {
							msg.obj="修改失败";
						}
						handler.sendMessage(msg);
					}
				}, false, false);
	
		
	}
	/**
	 * 检查收货地址是否符合规范
	 */
	private void checkAddress() {
		String userNameStr = userNameEt.getText().toString();
		String userPhoneStr = userPhoneEt.getText().toString();
		String userPostCode = userPostCodeEt.getText().toString();
//		String userStreet = userStreetEt.getText().toString();
		String userAddress = userAddressEt.getText().toString();
		String isDefault = "0";
		if (isDefaultAddress) {
			isDefault = "1";
		} else {
			isDefault = "0";
		}
		if (Utils.isNullOrEmpty(userNameStr)) {
			showDiaLog("请补全信息");
			/*Toast.makeText(ChangeAddressActivity.this, "请输入收货人姓名",
					Toast.LENGTH_SHORT).show();*/
		} else if (!com.lesports.stadium.utils.CommonUtils.isRegexPhone(userPhoneStr)) {
			showDiaLog("请输入正确的手机号");
			/*Toast.makeText(AddShippingAddressActivity.this, "请输入收货人电话号码",
					Toast.LENGTH_SHORT).show();*/
		} else if (Utils.isNullOrEmpty(userPostCode)) {
			showDiaLog("请补全信息");
			/*Toast.makeText(ChangeAddressActivity.this, "请输入邮政编码",
					Toast.LENGTH_SHORT).show();*/
		}else if(checkPost(userPostCode)){
			showDiaLog("请输入规范的邮政编码");
			
		} else if(userPostCode.length()!=6){
			showDiaLog("请输入规范的邮政编码");
		}else if (Utils.isNullOrEmpty(userAddress)) {
			showDiaLog("请补全信息");
		} else {
			// 请求修改收货地址
			requestChange(userNameStr, mCurrentProviceId, mCurrentCityId, mCurrentDistrictId, userAddress, add_usercity.getText().toString().trim(),
					 userPhoneStr, userPostCode, isDefault);
		}
	}
	
	/**
	 * 显示popupWindow
	 */
	private void showPopwindow() {
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.pop_location, null);

		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

		window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);

		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		window.setFocusable(true);

		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		window.setBackgroundDrawable(dw);

		// 设置popWindow的显示和消失动画
		window.setAnimationStyle(R.style.mypopwindow_anim_style);

		// popWindow消失监听方法
		window.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				System.out.println("popWindow消失");
			}
		});

		mViewProvince = (WheelView) view.findViewById(R.id.id_province);
		mViewCity = (WheelView) view.findViewById(R.id.id_city);
		mViewDistrict = (WheelView) view.findViewById(R.id.id_district);
		// 添加change事件
		mViewProvince.addChangingListener(this);
		// 添加change事件
		mViewCity.addChangingListener(this);
		// 添加change事件
		mViewDistrict.addChangingListener(this);

	}

	private void show() {
		// 在底部显示
		window.showAtLocation(this.findViewById(R.id.add_userpostcode), Gravity.BOTTOM, 0,
				0);
	}

	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(this,
				mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			updateAreas();
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentDistrictId = mDistrictIDsMap.get(mCurrentDistrictName+mCurrentCityName);
		}
		if(mDistrictDatasMap.get(mCurrentCityName)!=null&&mDistrictDatasMap.get(mCurrentCityName).length>0){
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
			add_usercity.setText(mCurrentProviceName + " " + mCurrentCityName + " "
					+ mCurrentDistrictName );
		}else{
			add_usercity.setText(mCurrentProviceName + " " + mCurrentCityName);
		}
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		mCurrentCityId = mCitisIDsMap.get(mCurrentCityName);
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict
				.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mViewDistrict.setCurrentItem(0);
		if(mDistrictDatasMap.get(mCurrentCityName)!=null&&mDistrictDatasMap.get(mCurrentCityName).length>0){
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
			mCurrentDistrictId = mDistrictIDsMap.get(mCurrentDistrictName+mCurrentCityName);
		}
		else{
			mCurrentDistrictName = "";
			mCurrentDistrictId = "";
		}
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		mCurrentProviceId = mProviceIDsMap.get(mCurrentProviceName);
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}

	/**
	 * 解析省市区的XML数据
	 */

	protected void initProvinceDatas() {
		List<ProvinceModel> provinceList = null;
		try {
			XmlParserJSON json = new XmlParserJSON(getApplicationContext());
			provinceList = json.getDataList();
			// */ 初始化默认选中的省、市、区
			if (provinceList != null && !provinceList.isEmpty()) {
				mCurrentProviceName = provinceList.get(0).getName();
				mCurrentProviceId = provinceList.get(0).getId();
				List<CityModel> cityList = provinceList.get(0).getCityList();
				if (cityList != null && !cityList.isEmpty()) {
					mCurrentCityName = cityList.get(0).getName();
					mCurrentCityId = cityList.get(0).getId();
					List<DistrictModel> districtList = cityList.get(0)
							.getDistrictList();
					mCurrentDistrictName = districtList.get(0).getName();
					mCurrentDistrictId = districtList.get(0).getId();
					mCurrentZipCode = districtList.get(0).getZipcode();
				}
			}
			// */
			mProvinceDatas = new String[provinceList.size()];
			for (int i = 0; i < provinceList.size(); i++) {
				// 遍历所有省的数据
				mProvinceDatas[i] = provinceList.get(i).getName();
				mProviceIDsMap.put(provinceList.get(i).getName(), provinceList.get(i).getId());
				List<CityModel> cityList = provinceList.get(i).getCityList();
				String[] cityNames = new String[cityList.size()];
				for (int j = 0; j < cityList.size(); j++) {
					// 遍历省下面的所有市的数据
					cityNames[j] = cityList.get(j).getName();
					mCitisIDsMap.put(cityList.get(j).getName(), cityList.get(j).getId());
					List<DistrictModel> districtList = cityList.get(j)
							.getDistrictList();
					String[] distrinctNameArray = new String[districtList
							.size()];
					DistrictModel[] distrinctArray = new DistrictModel[districtList
							.size()];
					for (int k = 0; k < districtList.size(); k++) {
						// 遍历市下面所有区/县的数据
						DistrictModel districtModel = new DistrictModel(
								districtList.get(k).getName(), districtList
										.get(k).getZipcode());
						// 区/县对于的邮编，保存到mZipcodeDatasMap
						mZipcodeDatasMap.put(districtList.get(k).getName(),
								districtList.get(k).getId());
						mDistrictIDsMap.put(districtList.get(k).getName()+cityNames[j], districtList.get(k).getId());
						distrinctArray[k] = districtModel;
						distrinctNameArray[k] = districtModel.getName();
					}
					// 市-区/县的数据，保存到mDistrictDatasMap
					mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
				}
				// 省-市的数据，保存到mCitisDatasMap
				mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {

		}
	}
	/**
	 * 提示修改信息
	 * @param msg
	 */
	private void showDiaLog(String msg){
		mDiaLog=null;
		mDiaLog=new CustomDialog(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mDiaLog.dismiss();
			}
		});
		mDiaLog.setRemindMessage(msg);
		mDiaLog.setConfirmTxt("确认");
		mDiaLog.setRemindTitle("温馨提示");
		mDiaLog.show();
	}
	private void showDeleteLog(){
		mDeleteLog=null;
		mDeleteLog = new CustomDialog(ChangeAddressActivity.this, new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDeleteLog.dismiss();
				deleteAddress();
			}
		}, new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDeleteLog.dismiss();
			}
		});
		mDeleteLog.setCancelTxt("取消");
		mDeleteLog.setConfirmTxt("删除");
		mDeleteLog.setRemindMessage("是否删除");
		mDeleteLog.show();
	}
	/**
	 * EditTextWatcher
	 * @author Administrator
	 * @description 编辑字数统计
	 */
	public class EditTextWatcher implements TextWatcher {

		public void afterTextChanged(Editable arg0) {
			 String edit = userNameEt.getText().toString();
			 
			 userNameEt.setVisibility(View.VISIBLE);
			if (edit.length() <= BigIndex) {
			//	text_view.setText(""+(BigIndex - edit.length()));
			} else {
				userNameEt.setText(edit.substring(0,BigIndex));
				userNameEt.setSelection(edit.substring(0,BigIndex).length());
		//		Toast.makeText(AddShippingAddressActivity.this,"超过字数限制 最大长度为"+edit.substring(0,BigIndex).length(),500).show();
			}
		}

		public void beforeTextChanged(CharSequence cs, int arg1, int arg2,int arg3) {
		}

		public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
		}

	}
	 //验证邮政编码  
	  public boolean checkPost(String post){  
    if(post.matches("[1-9]\\d{6}")){  
        return true;  
	       }else{  
       return false;  
   }  
}  
}
