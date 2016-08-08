package com.lesports.stadium.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.lesports.stadium.R;
import com.lesports.stadium.bean.Address;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.fragment.ServiceFragment;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.AMapUtil;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.ToastUtil;
import com.lesports.stadium.utils.Utils;

/**
 * ***************************************************************
 * 
 * @ClassName: BoardingActivity
 * 
 * @Desc : 上车位置的activity
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 盛之刚
 * 
 * @data:2016-2-15 下午12:00:05
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *  ***************************************************************
 */

@SuppressLint("HandlerLeak") public class BoardingActivity  extends Activity implements
	OnGeocodeSearchListener, OnClickListener,LocationSource,AMapLocationListener,OnPoiSearchListener,
	TextWatcher, InputtipsListener{
	/**
	 * 弹出进度
	 */
	private ProgressDialog progDialog = null;
	/**
	 * 位置搜索
	 */
	private GeocodeSearch geocoderSearch;
	/**
	 * 设置默认地址：北京
	 */
	private String city = "北京";
	/**
	 * 搜索框
	 */
private AutoCompleteTextView searchEt;
	/**
	 * 位置
	 */
	private String addressName;
	
	/**地图
	 * 
	 */
	private AMap aMap;
	
	/**
	 * 地图View
	 */
	private MapView mapView;
	
	/**
	 * 标注
	 */
	private Marker geoMarker;
	
	/**
	 * 标注
	 */
	private Marker regeoMarker;
	
	/**
	 * 位置
	 */
	private LatLonPoint latLonPoint=null;
	
	/**
	 * 位置监听
	 */
	private OnLocationChangedListener mListener;
	
	/**
	 * 定位
	 */
	private AMapLocationClient mlocationClient;
	
	/**
	 *地图设置
	 */
	private AMapLocationClientOption mLocationOption;
	
	/**
	 * 返回按钮
	 */
	private ImageView ivBack;
	/**
	 * 位置的listView
	 */
	private ListView lvAddresses;
	/**
	 * 标题
	 */
	private TextView tv_title;
	
	
	/**
	 * 位置集合
	 */
	private List<Address> addressList=new ArrayList<Address>();
	/**
	 *位置适配器
	 */
	private MyAddressAdapter adapter=null;
	/**
	 * 静待变量设置地址
	 */
	public static Address rideAddress;
	/**
	 *请求成功，展示位置
	 */
	private static final int SUCCESS=0;
	@SuppressWarnings("unused")
	private final static int RIDE_CODE = 801;
	/**
	 * 地址列表数据存储
	 */
	private List<Address> listString = new ArrayList<Address>();
 
	private final int HANDLE_TAG_1=1;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
			//	Log.e("BUSLocation", "执行定位结束没啊");
				if(Utils.isNullOrEmpty(adapter)){
					adapter=new MyAddressAdapter(getApplicationContext(), addressList);
				}
				
				lvAddresses.setAdapter(adapter);
				adapter.notifyDataSetChanged();			
				break;
			case HANDLE_TAG_1:
				//常用地址请求
				if(Utils.isNullOrEmpty(adapter)){
					adapter=new MyAddressAdapter(getApplicationContext(), addressList);
				}
				String backdata=(String)msg.obj;
				//			Log.e("ADDRESS", msg.obj.toString());
				if(!TextUtils.isEmpty(backdata)){
					useWayHand1leAddress(backdata);
				}
				break;
			case 2:
				if(ServiceFragment.instence!=null)
					ServiceFragment.instence.tag_views=3;
				finish();
				break;
			default:
				break;
			}				
		}

	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_boarding);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		initView();
		init();
		initListener();
		requestHistoryAddress();
	}
	
	/**
	* 初始化AMap对象
	*/
	private void init() {
	//	addressList=new ArrayList<Address>();
		if (aMap == null) {
			aMap = mapView.getMap();
			geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
			BitmapDescriptor des=BitmapDescriptorFactory.fromResource(R.drawable.ic_map_gocar);
			geoMarker.setIcon(des);
			
			regeoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
			regeoMarker.setIcon(des);
			
			setUpMap();
		}

		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		progDialog = new ProgressDialog(this);
	
	}
	
	
	private void initView(){
		tv_title = (TextView) findViewById(R.id.tv_title);
		ivBack=(ImageView)findViewById(R.id.title_left_iv);
		lvAddresses=(ListView)findViewById(R.id.lv_addresses);
		lvAddresses.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			//	Toast.makeText(BoardingActivity.this, "点击了位置"+position, 0).show();
				rideAddress=addressList.get(position);
				GlobalParams.MY_LOCATION = rideAddress;
				sendCommonAddress(rideAddress);
			//	Log.e("Address", "这是地理位置："+rideAddress.getAddress());
				if(ServiceFragment.instence!=null)
					ServiceFragment.instence.tag_views=3;
				finish();
			}
		});
		searchEt=(AutoCompleteTextView)findViewById(R.id.keyWord);
		searchEt.addTextChangedListener(BoardingActivity.this);// 添加文本输入框监听事件
	//	searchEt.setFocusable(false);
		Intent intent = getIntent();
		if(intent!=null){
			String tag = intent.getStringExtra("tag");
			if(tag!=null){
				tv_title.setText("当前位置");
				searchEt.setHint("请输入当前位置");
			}
		}
	}
	
	private void initListener(){
		ivBack.setOnClickListener(this);
	}
	
	//=====================start==================================
	
	/**
	* 设置一些amap的属性
	*/
	private void setUpMap() {
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		//设置是否展示比例尺
		UiSettings mUiSettings= aMap.getUiSettings();
		mUiSettings.setScaleControlsEnabled(false);
		mUiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);// 设置地图logo显示在右下方
		mUiSettings.setZoomControlsEnabled(false);
		mUiSettings.setMyLocationButtonEnabled(false); // 是否显示默认的定位按钮
		aMap.setMyLocationEnabled(true);// 是否可触发定位并显示定位层
	}
	
	/**
	* 定位成功后回调函数
	*/
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
	if (mListener != null && amapLocation != null) {
		if (amapLocation != null
				&& amapLocation.getErrorCode() == 0) {
			//	当前位置
			Address address=new Address();
			address.setSpecificLocation(amapLocation.getAddress());
			address.setAddress(amapLocation.getPoiName());
			address.setLatitude(amapLocation.getLatitude());
			address.setLongitude(amapLocation.getLongitude());
			address.setCity("bj");
			addressList.add(0,address);
			handler.sendEmptyMessage(SUCCESS);
			 //经度和纬度
			latLonPoint = new LatLonPoint(amapLocation.getLatitude(),amapLocation.getLongitude());
			//在地图上获取当前的位置 
			getAddress(latLonPoint);
			//停止定位
			mlocationClient.stopLocation();
		} else {
			String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
			Log.e("AmapErr",errText);
		}
	}
	}
	
	/**
	* 激活定位
	*/
	@Override
	public void activate(OnLocationChangedListener listener) {
	mListener = listener;
	if (mlocationClient == null) {
		mlocationClient = new AMapLocationClient(this);
		mLocationOption = new AMapLocationClientOption();
		//设置定位监听
		mlocationClient.setLocationListener(this);
		//设置为高精度定位模式
		mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		//设置是否返回地址信息（默认返回地址信息）  
		mLocationOption.setNeedAddress(true); 
		//设置定位参数
		mlocationClient.setLocationOption(mLocationOption);
		// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
		// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
		// 在定位结束后，在合适的生命周期调用onDestroy()方法
		// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
		mlocationClient.startLocation();
	}
	}
	
	/**
	* 停止定位
	*/
	@Override
	public void deactivate() {
		mListener = null;
		if (mlocationClient != null) {
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}
		mlocationClient = null;
	}

	//===========================end=====================
	
	/**
	* 方法必须重写
	*/
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}
	
	/**
	* 方法必须重写
	*/
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}
	
	/**
	* 方法必须重写
	*/
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}
	
	/**
	* 方法必须重写
	*/
	@Override
	protected void onDestroy() {
		if(handler!=null)
			handler.removeCallbacksAndMessages(null);
		super.onDestroy();
		mapView.onDestroy();
	}
	
	/**
	* 显示进度条对话框
	*/
	public void showDialog() {
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage("正在获取地址");
		progDialog.show();
	}
	
	/**
	* 隐藏进度条对话框
	*/
	public void dismissDialog() {
	if (progDialog != null) {
		progDialog.dismiss();
	}
	}
	
	/**
	* 响应地理编码
	*/
	public void getLatlon(final String name) {
		showDialog();
		GeocodeQuery query = new GeocodeQuery(name, "010");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
		geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
	}
	
	/**
	* 响应逆地理编码
	*/
	public void getAddress(final LatLonPoint latLonPoint) {
		showDialog();
		RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 10,GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
		geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
	}
	
	/**
	* 地理编码查询回调
	*/
	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		dismissDialog();
		if (rCode == 0) {
			if (result != null && result.getGeocodeAddressList() != null
					&& result.getGeocodeAddressList().size() > 0) {
				GeocodeAddress address = result.getGeocodeAddressList().get(0);
				String temp= address.getLatLonPoint().toString();
				addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
						+ address.getFormatAddress();
				Log.e("offmap", rCode+"返回结果"+result.toString()+addressName);
				String[] landl=temp.split(",");
				Address thisAddress=new Address();
				thisAddress.setAddress(""+address.getFormatAddress());
				thisAddress.setSpecificLocation(""+address.getCity()+address.getDistrict()+address.getFormatAddress());
				thisAddress.setCity(address.getCity());
				thisAddress.setLatitude(Double.parseDouble(landl[0]));
				thisAddress.setLongitude(Double.parseDouble(landl[1]));
				rideAddress=thisAddress;
				GlobalParams.MY_LOCATION = rideAddress;
			//ToastUtil.show(BoardingActivity.this, landl[0]+"维度"+landl[1]);
				
				handler.sendEmptyMessage(2);
			} else {
				ToastUtil.show(BoardingActivity.this, R.string.no_result);
			}
		} else if (rCode == 27) {
			ToastUtil.show(BoardingActivity.this, R.string.error_network);
		} else if (rCode == 32) {
			ToastUtil.show(BoardingActivity.this, R.string.error_key);
		} else {
			ToastUtil.show(BoardingActivity.this,
					getString(R.string.error_other) + rCode);
		}
	}

	/**
	* 逆地理编码回调 
	*/
	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		dismissDialog();
		if (rCode == 0) {
			if (result != null && result.getRegeocodeAddress() != null
					&& result.getRegeocodeAddress().getFormatAddress() != null) {
		//		List<PoiItem> list=result.getRegeocodeAddress().getPois();
				
			/*	addressName = result.getRegeocodeAddress().getFormatAddress();*/
				aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						AMapUtil.convertToLatLng(latLonPoint), 25));
				regeoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
				//ToastUtil.show(BoardingActivity.this, addressName);
			} else {
				ToastUtil.show(BoardingActivity.this, R.string.no_result);
			}
		} else if (rCode == 27) {
			ToastUtil.show(BoardingActivity.this, R.string.error_network);
		} else if (rCode == 32) {
			ToastUtil.show(BoardingActivity.this, R.string.error_key);
		} else {
			ToastUtil.show(BoardingActivity.this,
					getString(R.string.error_other) + rCode);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_iv:
			if(ServiceFragment.instence!=null)
				ServiceFragment.instence.tag_views=3;
			finish();
		break;
		default:
			break;
		}
	}
	
	
	//=======================位置适配器=================
	class MyAddressAdapter extends BaseAdapter{
	    /**
	     * 上下文
	     */
		private Context context;
		
		/**
		 * 位置列表
		 */
		private List<Address> list=new ArrayList<Address>();
		
		/**
		 * 构造函数
		 */
		public MyAddressAdapter(Context context,List<Address> list){
			this.context=context;
			this.list=list;
		}
		
		@Override
		public int getCount() {
			return list.size();
		}

		public List<Address> getList() {
			return list;
		}

		public void setList(List<Address> list) {
			this.list = list;
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder = null;
			if (convertView == null) {
				holder = new Holder();
				convertView = View.inflate(context, R.layout.lv_item_address, null);
				holder.tv_address=(TextView)convertView.findViewById(R.id.tv_address);
				holder.tv_location=(TextView)convertView.findViewById(R.id.tv_location);
				holder.iv_image=(ImageView)convertView.findViewById(R.id.iv_image);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			if(position==0){
				holder.tv_address.setText("我的位置-"+list.get(position).getAddress());
			}else{
				holder.tv_address.setText(list.get(position).getAddress());
			}
			holder.tv_location.setText(list.get(position).getSpecificLocation());
			return convertView;
		}
		
	  class Holder{
		  TextView tv_address,tv_location;
		  ImageView iv_image,iv_last;
	  }
	}
	/**
	 * 上传公用地址
	 * @param address
	 */
	private void sendCommonAddress(Address address){
		Map<String, String> params = new HashMap<String, String>();
		 params.put("addressTitle", address.getAddress()); //地址
		 params.put("address", address.getSpecificLocation()); //详细地址
	//	 params.put("addressType", "1"); //地址类型
	//	 params.put("useCarType", "1"); //车型
		 params.put("longitude",""+ address.getLongitude()); //经度
		 params.put("latitude", ""+address.getLatitude()); //纬度
		 params.put("city", address.getCity()); //城市
	//	 params.put("mapType", "1"); //地图类型
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.SEND_COMMON_ADDRESS, params, new GetDatas() {

					@SuppressWarnings("null")
					@Override
					public void getServerData(BackData data) {
						if (data == null) {
							// 说明网络获取无数据
							Toast.makeText(BoardingActivity.this, "网络错误", Toast.LENGTH_SHORT)
									.show();
						} else {
							Object obj = data.getObject();
							if (obj == null) {
								Toast.makeText(BoardingActivity.this, "网络错误", Toast.LENGTH_SHORT)
										.show();
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
									Toast.makeText(BoardingActivity.this, "网络异常",
											Toast.LENGTH_SHORT).show();
								} else {
								/*	Log.i("ADDRESS",
											"常用地址添加成功？" + data.getNetResultCode()
													+ backdata);*/
									
								}
							}

						}
					}
				}, false, false);

	
	}
//	@Override
//	public void onBackPressed() {
//		super.onBackPressed();
//		ServiceFragment.instence.tag_views=3;
//	}
//	

	@Override
	public void onGetInputtips(List<Tip> tipList, int rCode) {
		Log.e("offmap", "onGetInputtips返回码："+rCode+"结果"+tipList.toString());
		if (rCode == 0) {
			listString.clear();
			String[] list=new String[tipList.size()];
			for (int i = 0; i < tipList.size(); i++) {
				Address add=new Address();
				add.setAddress(tipList.get(i).getName());
				add.setEaraCode(tipList.get(i).getAdcode());
				list[i]=tipList.get(i).getName();
				listString.add(add);
			}
	//		PoiSearchAdapter thisAdapter=new PoiSearchAdapter(listString, BoardingActivity.this);
			ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(BoardingActivity.this,R.layout.route_inputs,list);
			searchEt.setDropDownBackgroundResource(R.drawable.address_list_background);
			searchEt.setAdapter(mAdapter);
			searchEt.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
				//	Toast.makeText(GetOffActivity.this, "item被点击了："+listString.get(position).getAddress(), Toast.LENGTH_SHORT).show();
					// name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode  
				/*	GeocodeQuery query = new GeocodeQuery(listString.get(position).getAddress(), listString.get(position).getEaraCode());  
					geocoderSearch.getFromLocationNameAsyn(query);  */
					String name=listString.get(position).getAddress();
					GeocodeQuery query = new GeocodeQuery(name, "010");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
					
					geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
				}
			});
			mAdapter.notifyDataSetChanged();
		} else {
			ToastUtil.showerror(BoardingActivity.this, rCode);
		}
	
		
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	//	Toast.makeText(BoardingActivity.this, "内容改变啦", 0).show();
		String newText = s.toString().trim();
		if (!AMapUtil.IsEmptyOrNullString(newText)) {
			InputtipsQuery inputquery = new InputtipsQuery(newText, city);
			inputquery.setCityLimit(true);
			Inputtips inputTips = new Inputtips(BoardingActivity.this, inputquery);
			inputTips.setInputtipsListener(BoardingActivity.this);
			inputTips.requestInputtipsAsyn();
		}
	
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		
	}

	@Override
	public void onPoiItemSearched(PoiItem item, int rCode) {
		Log.e("offmap", "onPoiItemSearched返回码："+rCode+"结果"+item.getAdName());
		if (rCode == 1000) {
			List<PoiItem> poiItems = new ArrayList<PoiItem>();
			poiItems.add(item);
			/*
			 * mpoiadapter=new PoiListAdapter(this, poiItems);
			 * mPoiSearchList.setAdapter(mpoiadapter);
			 */
		}
	}

	@Override
	public void onPoiSearched(PoiResult result, int rcode) {
		Log.e("offmap", "onPoiSearched返回码："+rcode+"结果"+result);
		if (rcode == 1000) {
			if (result != null) {
//				List<PoiItem> poiItems = result.getPois();
				/*
				 * mpoiadapter=new PoiListAdapter(this, poiItems);
				 * mPoiSearchList.setAdapter(mpoiadapter);
				 */
			}
		}
	}
	/**
	 * 请求常用地址信息
	 */
	private void requestHistoryAddress() {

		Map<String, String> params = new HashMap<String, String>();
		// params.put("fileId", "1"); //宣传视屏id
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.RIDECAR_ADDRESS, params, new GetDatas() {
					@SuppressWarnings("null")
					@Override
					public void getServerData(BackData data) {
						if (data == null) {
							// 说明网络获取无数据
							Toast.makeText(BoardingActivity.this, "网络错误", Toast.LENGTH_SHORT)
									.show();
						} else {
							Object obj = data.getObject();
							if (obj == null) {
								Toast.makeText(BoardingActivity.this, "网络错误", Toast.LENGTH_SHORT)
										.show();
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
									Toast.makeText(BoardingActivity.this, "网络异常",
											Toast.LENGTH_SHORT).show();
								} else {
									Log.i("ADDRESS",
											"走到这里了么？" + data.getNetResultCode()
													+ backdata);
									// 调用方法，给listview加载数据，先将数据通过handler发送到主线程中
									Message msg = new Message();
									msg.what = HANDLE_TAG_1;
									msg.obj = backdata;
									handler.sendMessage(msg);
								}
							}

						}
					}
				}, false, false);
	}
	/**
	 * 用来处理请求下来的数据源
	 * @param backdata
	 */
	private void useWayHand1leAddress(String backdata) {
		// TODO Auto-generated method stub

		try {
			JSONArray tempArray=new JSONArray(backdata);
			for(int i=0;i<tempArray.length();i++){
				JSONObject tempO=tempArray.getJSONObject(i);
				Address tempAddress=new Address();
				tempAddress.setAddress(tempO.getString("addressTitle"));
				tempAddress.setSpecificLocation(tempO.getString("address"));
				tempAddress.setCity(tempO.getString("city"));
				tempAddress.setLatitude(tempO.getDouble("latitude"));
				tempAddress.setLongitude(tempO.getDouble("longitude"));
				addressList.add(tempAddress);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		adapter.notifyDataSetChanged();
	};
}
