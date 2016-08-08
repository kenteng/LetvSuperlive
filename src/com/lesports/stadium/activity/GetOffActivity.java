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
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.google.gson.JsonArray;
import com.lesports.stadium.R;
import com.lesports.stadium.adapter.HistoryAddressAdapter;
import com.lesports.stadium.adapter.PoiSearchAdapter;
import com.lesports.stadium.bean.Address;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.fragment.ServiceFragment;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.AMapUtil;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.ToastUtil;

/**
 * ***************************************************************
 * 
 * @ClassName: GetOffActivity
 * 
 * @Desc : 下车Activity
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 盛之刚
 * 
 * @data:2016-2-18
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
@SuppressLint("HandlerLeak")
public class GetOffActivity extends Activity implements OnPoiSearchListener,
		TextWatcher, InputtipsListener ,OnGeocodeSearchListener{
	/**
	 * 返回
	 */
	private ImageView ivBack;
	/**
	 * 标题
	 */
	private TextView tvTitle;
	/**
	 * 静待变量设置地址
	 */
	public static Address getoffAddress;
	/**
	 * 设置默认地址：北京
	 */
	private String city = "北京";
	/**
	 * 搜索
	 */
	private ImageView ivSearch;
	
	/**
	 * 存储地址信息
	 */
	private List<Address> addressList = new ArrayList<Address>();
	/**
	 * poi查询条件类
	 */
	private PoiSearch.Query query;
	/**
	 * poi返回的结果
	 */
	private PoiResult poiResult; // poi返回的结果
	/**
	 * 搜索时进度条
	 */
	private ProgressDialog progDialog = null;
	/**
	 * 输入搜索关键字
	 */
	private AutoCompleteTextView searchInputText;// 输入搜索关键字
	/**
	 * 要输入打的poi搜索关键字
	 */
	private String keyWord = "";// 要输入的poi搜索关键字
	private int currentPage = 0;// 当前页面，从0开始计数
	private PoiSearch poiSearch;// POI搜索
	private List<Address> listString = new ArrayList<Address>();
	/**
	 * 打印地址信息
	 */
	private String addressName;
	private GeocodeSearch geocoderSearch;
	/**
	 * 历史记录
	 */
	// private LinearLayout llytSearch;
	private ListView mHistoryAddressLv;
	private HistoryAddressAdapter mAdapter;
	/**
	 * handle标记
	 */
	private final int GET_USE_CAR_INFO=300;
	private Handler addressHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_USE_CAR_INFO:
				String backdata=(String)msg.obj;
				if(!TextUtils.isEmpty(backdata)){
					useWayHandleUseCarInfo(backdata);
				}
				break;
			case 301:
//				ServiceFragment.instence.tag_views=3;
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
		setContentView(R.layout.activity_get_off);
		initView();
		initData();
		initListener();
		requestHistoryAddress();
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		ivBack = (ImageView) findViewById(R.id.title_left_iv);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		ivSearch = (ImageView) findViewById(R.id.iv_search);
		// llytSearch = (LinearLayout) findViewById(R.id.llyt_address_history);
		mHistoryAddressLv = (ListView) findViewById(R.id.getoff_lv);
		
		mAdapter = new HistoryAddressAdapter(this, addressList);
		mHistoryAddressLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				getoffAddress=addressList.get(position);
//				ServiceFragment.instence.tag_views=3;
				sendCommonAddress(getoffAddress);
				finish();
			}
		});
		mHistoryAddressLv.setAdapter(mAdapter);
		searchInputText = (AutoCompleteTextView) findViewById(R.id.keyWord);
		searchInputText.addTextChangedListener(GetOffActivity.this);// 添加文本输入框监听事件
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		
		
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		tvTitle.setText(getString(R.string.get_off));

	}

	/**
	 * 初始化listener
	 */
	private void initListener() {
		ivBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				ServiceFragment.instence.tag_views=3;
				finish();
			}
		});
	}

	/**
	 * 隐藏进度框
	 */
	private void dissmissProgressDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	/**
	 * 显示进度框
	 */
	private void showProgressDialog() {
		if (progDialog == null)
			progDialog = new ProgressDialog(this);
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(false);
		progDialog.setMessage("正在搜索:\n" + keyWord);
		progDialog.show();
	}

	/**
	 * 开始进行poi搜索
	 */
	protected void doSearchQuery() {
		showProgressDialog();// 显示进度框
		currentPage = 0;
		query = new PoiSearch.Query(keyWord, "", "北京");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
		query.setPageSize(15);// 设置每页最多返回多少条poiitem
		query.setPageNum(currentPage);// 设置查第一页
		query.setCityLimit(true);

		poiSearch = new PoiSearch(GetOffActivity.this, query);
		poiSearch.setOnPoiSearchListener(this);
		poiSearch.searchPOIAsyn();
	}

	/**
	 * 点击搜索按钮
	 */
	public void searchButton() {
		keyWord = AMapUtil.checkEditText(searchInputText);
		if ("".equals(keyWord)) {
			ToastUtil.show(GetOffActivity.this, "请输入搜索关键字");
			return;
		} else {
			doSearchQuery();
		}
	}

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
			Log.e("GETOFFTIP", tipList.toString());
		//	PoiSearchAdapter thisAdapter=new PoiSearchAdapter(GetOffActivity.this, R.layout.route_inputs, R.id.online_user_list_item_textview, listString);
			ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(GetOffActivity.this,R.layout.route_inputs,list);
			searchInputText.setDropDownBackgroundResource(R.drawable.address_list_background);
			searchInputText.setAdapter(mAdapter);
			searchInputText.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
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
			ToastUtil.showerror(GetOffActivity.this, rCode);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	//	Toast.makeText(GetOffActivity.this, "内容改变啦"+s, Toast.LENGTH_SHORT).show();
		String newText = s.toString().trim();
		if (!AMapUtil.IsEmptyOrNullString(newText)) {
			InputtipsQuery inputquery = new InputtipsQuery(newText, city);
			inputquery.setCityLimit(true);
			Inputtips inputTips = new Inputtips(GetOffActivity.this, inputquery);
			inputTips.setInputtipsListener(GetOffActivity.this);
			inputTips.requestInputtipsAsyn();
		}
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void onPoiItemSearched(PoiItem item, int rCode) {
		if (rCode == 1000) {
			List<PoiItem> poiItems = new ArrayList<PoiItem>();
			poiItems.add(item);
		}
	}

	@Override
	public void onPoiSearched(PoiResult result, int rcode) {
		Log.d("offmap", "onPoiSearched返回码："+rcode+"结果"+result);
		if (rcode == 1000) {
			if (result != null) {
			//	List<PoiItem> poiItems = result.getPois();
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
						} else {
							Object obj = data.getObject();
							if (obj == null) {
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
								} else {
									// 调用方法，给listview加载数据，先将数据通过handler发送到主线程中
									Message msg = new Message();
									msg.what = 300;
									msg.obj = backdata;
									addressHandler.sendMessage(msg);
								}
							}

						}
					}
				}, false, false);
	}

	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getGeocodeAddressList() != null
					&& result.getGeocodeAddressList().size() > 0) {
				GeocodeAddress address = result.getGeocodeAddressList().get(0);
				String temp= address.getLatLonPoint().toString();
				addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
						+ address.getFormatAddress();
				String[] landl=temp.split(",");
				Address thisAddress=new Address();
				thisAddress.setAddress(""+address.getFormatAddress());
				thisAddress.setSpecificLocation(""+address.getCity()+address.getDistrict()+address.getFormatAddress());
				thisAddress.setCity(address.getCity());
				thisAddress.setLatitude(Double.parseDouble(landl[0]));
				thisAddress.setLongitude(Double.parseDouble(landl[1]));
				getoffAddress=thisAddress;
				sendCommonAddress(thisAddress);
				 addressHandler.sendEmptyMessage(301);
			} else {
				ToastUtil.show(GetOffActivity.this, R.string.no_result);
			}

		} else {
			ToastUtil.showerror(GetOffActivity.this, rCode);
		}
	}
	/**
	 * 逆地理编码回调
	 */
	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		
		if (rCode == 1000) {
			if (result != null && result.getRegeocodeAddress() != null
					&& result.getRegeocodeAddress().getFormatAddress() != null) {
				addressName = result.getRegeocodeAddress().getFormatAddress()
						+ "附近";
				/*aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						AMapUtil.convertToLatLng(latLonPoint), 15));
				regeoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));*/
				ToastUtil.show(GetOffActivity.this, addressName);
			} else {
				ToastUtil.show(GetOffActivity.this, R.string.no_result);
			}
		} else {
			ToastUtil.showerror(GetOffActivity.this, rCode);
		}
	}
	/**
	 * 响应地理编码
	 */
	public void getLatlon(final String name) {
		GeocodeQuery query = new GeocodeQuery(name, "010");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
		geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
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
							Toast.makeText(GetOffActivity.this, "网络错误", Toast.LENGTH_SHORT)
									.show();
						} else {
							Object obj = data.getObject();
							if (obj == null) {
								Toast.makeText(GetOffActivity.this, "网络错误", Toast.LENGTH_SHORT)
										.show();
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
									Toast.makeText(GetOffActivity.this, "网络异常",
											Toast.LENGTH_SHORT).show();
								} else {
									
								}
							}

						}
					}
				}, false, false);

	
	}
	

	/**
	 * 用车地址信息处理
	 * @param backdata
	 */
	private void useWayHandleUseCarInfo(String backdata) {
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
		mAdapter.setAddressList(addressList);
		mAdapter.notifyDataSetChanged();
		
	};
	
}
