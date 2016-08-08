package com.lesports.stadium.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lesports.stadium.R;
import com.lesports.stadium.adapter.LocationDataAdapter;
import com.lesports.stadium.adapter.LocationDataAdapterGoods;
import com.lesports.stadium.adapter.ServiceFoodAdapter;
import com.lesports.stadium.base.BaseActivity;
import com.lesports.stadium.bean.ServiceFoodBean;
import com.lesports.stadium.bean.Venue;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.pullrefreshview.PullToRefreshBase;
import com.lesports.stadium.pullrefreshview.PullToRefreshBase.OnRefreshListener;
import com.lesports.stadium.pullrefreshview.PullToRefreshListView;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.DensityUtil;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.UIUtils;
import com.lesports.stadium.view.CustomDialog;
import com.umeng.analytics.MobclickAgent;

public class OnlineContainActivity extends BaseActivity implements
		OnClickListener {

	private PullToRefreshListView mPullToRefreshListview;
	/**
	 * 展示数据的listview
	 */
	private ListView mListview;
	/**
	 * 用来标记网络获取的数据为空
	 */
	private final int TAG_DATANULL = 0;
	/**
	 * 用来标记网络获取的数据
	 */
	private final int TAG_DATA = 1;
	/**
	 * @param context
	 */
	/**
	 * 上下文对象
	 */
	private Context context;

	private CustomDialog exitDialog;
	/**
	 * 该标记用来标注获取数据的页码
	 */
	private int PAGE_NUM = 2;
	/**
	 * listview的数据适配器对象
	 */
	private ServiceFoodAdapter mAdapter;
	/**
	 * 返回按钮
	 */
	private ImageView mBack;
	/**
	 * 顶部右侧弹出菜单布局
	 */
	private LinearLayout mRightLayout;

	/**
	 * 顶部右侧弹出菜单
	 */
	private PopupWindow mPopupWindow;
	/**
	 * 标题栏
	 */
	private TextView mtitle;

	private List<Venue> listVenue = new ArrayList<Venue>();

	/**
	 * 场馆Id
	 */
	private String venueId;

	/**
	 * listview展示城市列表的listview
	 */
	private ListView mListviewLocation;

	/**
	 * 地址列表数据源
	 */
	private List<Venue> mListLoactionData = new ArrayList<>();
	/**
	 * 弹窗布局适配器
	 */
	private LocationDataAdapterGoods mLocationAdapter;
	private static double EARTH_RADIUS = 6378.137; 
	private final String SHAREDPREFERENCES_VENUE_CITYID = "CITYID";
	private final String SHAREDPREFERENCES_VENUE_VENUEID = "VENUEID";
	/**
	 * 处理数据的handler；
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case TAG_DATANULL:
				initListview(OnlineContainActivity.this,null);
				Toast.makeText(getApplicationContext(), R.string.no_food, Toast.LENGTH_SHORT).show();
				break;
			case TAG_DATA:
				String backdata = (String) msg.obj;
				if (backdata != null && backdata.length() != 0) {
					// 调用方法，解析数据
					jsonDataOfString(backdata);
				}
				break;
			case 100:
				if(mPullToRefreshListview!=null){
					mPullToRefreshListview.onPullDownRefreshComplete();
				}
				break;
			case 111:
				if(mPullToRefreshListview!=null){
					mPullToRefreshListview.onPullDownRefreshComplete();
				}
				break;
			case 110:
				String backdatass = (String) msg.obj;
				if (!TextUtils.isEmpty(backdatass)) {
					if(mPullToRefreshListview!=null){
						mPullToRefreshListview.onPullDownRefreshComplete();
					}
					List<ServiceFoodBean> list = jsonDataOfString_refrensh(backdatass);
					if (list != null && list.size() != 0) {
						if (mAdapter == null) {
							initListview(OnlineContainActivity.this, list);
						} else {
							mAdapter.setList(list);
						}
					}
				}
				break;
			case 1200:
				if(mPullToRefreshListview!=null){
					mPullToRefreshListview.onPullUpRefreshComplete();
				}
				break;
			case 222:
				if(mPullToRefreshListview!=null){
					mPullToRefreshListview.onPullUpRefreshComplete();
				}
				break;
			case 220:
				String backdatassss = (String) msg.obj;
				if (!TextUtils.isEmpty(backdatassss)) {
					if(mPullToRefreshListview!=null){
						mPullToRefreshListview.onPullUpRefreshComplete();
					}
					PAGE_NUM++;
					List<ServiceFoodBean> list = jsonDataOfString_refrensh(backdatassss);
					if (list != null && list.size() != 0) {
						mAdapter.addList(list);
					}
				}
				break;
			case 1000:
				BackData backData = (BackData) msg.obj;
				Log.v("123", "场馆列表  " + backData.getObject().toString());
				if (backData.getNetResultCode() == 0
						&& backData.getObject().toString() != null
						&& !"".equals(backData.getObject().toString())) {
					Gson gson = new Gson();
					Object bean = gson.fromJson(
							backData.getObject().toString(),
							new TypeToken<List<Venue>>() {
							}.getType());
					listVenue = (List<Venue>) bean;
					if(mListLoactionData!=null){
						mListLoactionData.clear();
					}
					if(listVenue!=null&&listVenue.size()>0){
						//先判断之前有没有选择过场馆
						SharedPreferences preferences = getSharedPreferences(
								SHAREDPREFERENCES_VENUE_CITYID, MODE_PRIVATE);
						String cityid=preferences.getString("CITYID",null);
						SharedPreferences preferencesvenue = getSharedPreferences(
								SHAREDPREFERENCES_VENUE_VENUEID, MODE_PRIVATE);
						String venuid=preferencesvenue.getString("VENUEID",null);
						if(!TextUtils.isEmpty(venuid)&&!TextUtils.isEmpty(cityid)){
							if(cityid.equals(GlobalParams.CITY_ID)){
								//说明场馆没有更换过
								
								for(int i=0;i<listVenue.size();i++){
									if(listVenue.get(i).getId().equals(venuid)){
										//说明有
										venueId = listVenue.get(i).getId();
										mtitle.setText(listVenue.get(i).getVenueName());
									}
								}
								if(listVenue.size()==1){
									mtitle.setCompoundDrawables(null, null, null, null);
									mtitle.setClickable(false);
								}
							}else{
								venueId = listVenue.get(0).getId();
								mtitle.setText(listVenue.get(0).getVenueName());
								if(listVenue.size()==1){
									mtitle.setCompoundDrawables(null, null, null, null);
									mtitle.setClickable(false);
								}
							}
						}else{
							venueId = listVenue.get(0).getId();
							mtitle.setText(listVenue.get(0).getVenueName());
							if(listVenue.size()==1){
								mtitle.setCompoundDrawables(null, null, null, null);
								mtitle.setClickable(false);
							}
						}
						getdataFromService();
					}
					
				}
				break;

			default:
				break;
			}
		}
	};
	private View top;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_footview);
		initview();
		getVenue();
	}

	/**
	 * 初始化view
	 */
	private void initview() {
		top = findViewById(R.id.top);
		mBack = (ImageView) findViewById(R.id.online_title_left_iv);
		mBack.setOnClickListener(this);
		mtitle = (TextView) findViewById(R.id.online_tv_title);
		mtitle.setOnClickListener(this);
		mPullToRefreshListview = (PullToRefreshListView) findViewById(R.id.servicefoodview_listview);
		mListview = mPullToRefreshListview.getRefreshableView();
		mPullToRefreshListview.setPullLoadEnabled(true);
		mPullToRefreshListview.setPullRefreshEnabled(true);
		mPullToRefreshListview
				.setOnRefreshListener(new OnRefreshListener<ListView>() {

					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 网络异常
						if (!UIUtils.isAvailable(OnlineContainActivity.this)) {
							mPullToRefreshListview.onPullDownRefreshComplete();
						} else {

							// 下拉刷新加载数据
							loadRefreshData_down(1, 1,
									OnlineContainActivity.this);

						}
					}

					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 网络异常
						if (!UIUtils.isAvailable(OnlineContainActivity.this)) {
							mPullToRefreshListview.onPullUpRefreshComplete();
						} else {
							// 上拉刷新加载数据
							loadRefreshData_up(0, 1, OnlineContainActivity.this);

						}
					}
				});
	}

	/**
	 * 上拉下拉数据加载
	 * 
	 * @param i
	 * @param j
	 * @param context
	 */
	protected void loadRefreshData_down(int i, int j, Context context) {
		Message message = new Message();
		message.arg1 = 100;
		handler.sendMessageDelayed(message, 1500);
		getdataFromService_down();
	}

	/**
	 * 上拉下拉数据加载
	 * 
	 * @param i
	 * @param j
	 * @param context
	 */
	protected void loadRefreshData_up(int i, int j, Context context) {
		Message message = new Message();
		message.arg1 = 200;
		handler.sendMessageDelayed(message, 1500);
		getdataFromService_up(PAGE_NUM);
	}

	/**
	 * 调用方法。解析数据
	 * 
	 * @param backdata
	 */
	private List<ServiceFoodBean> jsonDataOfString_refrensh(String backdata) {
		List<ServiceFoodBean> list = new ArrayList<ServiceFoodBean>();
		try {
			JSONArray array = new JSONArray(backdata);
			int count = array.length();
			for (int i = 0; i < count; i++) {
				ServiceFoodBean bean = new ServiceFoodBean();
				JSONObject obj = array.getJSONObject(i);
				if (obj.has("startbusinesstime")) {
					bean.setStartTime(obj.getString("startbusinesstime"));
				}
				if (obj.has("endbusinesstime")) {
					bean.setEndTime(obj.getString("endbusinesstime"));
				}
				if (obj.has("address")) {
					bean.setAddress(obj.getString("address"));
				}
				if (obj.has("companyname")) {
					bean.setCompanyname(obj.getString("companyname"));
				}
				if (obj.has("contact")) {
					bean.setContact(obj.getString("contact"));
				}
				if (obj.has("id")) {
					bean.setId(obj.getString("id"));
				}
				if (obj.has("mobilephone")) {
					bean.setMobilephone(obj.getString("mobilephone"));
				}
				if (obj.has("telephone")) {
					bean.setTelephone(obj.getString("telephone"));
				}
				if (obj.has("zipcode")) {
					bean.setZipcode(obj.getString("zipcode"));
				}
				if (obj.has("imageUrl")) {
					bean.setImageUrl(obj.getString("imageUrl"));
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
	 * 调用方法，从网络获取周边商品数据
	 * 
	 * @param context
	 */
	private void getdataFromService() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("pageNo", "1");
		params.put("rows", "5");
		params.put("userType", "1");
		params.put("venueId", venueId);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.SERVICE_FOODDIANPU_LIST, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						Log.v("123",
								data.getNetResultCode() + "餐饮列表"
										+ data.getObject());
						Object obj = data.getObject();
						if (TextUtils.isEmpty(obj.toString())) {
							// 说明数据为空
							Message msg = new Message();
							msg.arg1 = TAG_DATANULL;
							handler.sendMessage(msg);
						} else {
							String backdata = obj.toString();
							if (backdata == null && backdata.equals("")) {
								Message msg = new Message();
								msg.arg1 = TAG_DATANULL;
								handler.sendMessage(msg);
							} else {
								Message msg = new Message();
								msg.arg1 = TAG_DATA;
								msg.obj = backdata;
								handler.sendMessage(msg);
							}
						}
					}

				}, false, true);
	}

	/**
	 * 调用方法，从网络获取周边商品数据
	 * 
	 * @param context
	 */
	private void getdataFromService_up(int page) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("pageNo", page + "");
		params.put("rows", "5");
		params.put("userType", "1");
		params.put("venueId", venueId);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.SERVICE_FOODDIANPU_LIST, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						// TODO Auto-generated method stub
						if (data == null) {
							// 说明网络获取无数据
							Message msg = new Message();
							msg.arg1 = TAG_DATANULL;
							handler.sendMessage(msg);
						} else {
							Object obj = data.getObject();
							if (obj == null) {
								// 说明数据为空
								Message msg = new Message();
								msg.arg1 = TAG_DATANULL;
								handler.sendMessage(msg);
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
									Message msg = new Message();
									msg.arg1 = TAG_DATANULL;
									handler.sendMessage(msg);
								} else {
									if (data.getNetResultCode() == 0) {
										Message msg = new Message();
										msg.arg1 = 220;
										msg.obj = backdata;
										handler.sendMessage(msg);
									} else {
										Message msg = new Message();
										msg.arg1 = 222;
										handler.sendMessage(msg);
									}
								}
							}
						}
					}

				}, false, false);
	}

	/**
	 * 获取场馆
	 */
	private void getVenue() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("cityId", GlobalParams.CITY_ID);
		GetDataFromInternet.getInStance().interViewNet(ConstantValue.GET_VENUE,
				params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						Log.v("123", " ------- " + data.getObject().toString()
								+ " --" + data.getNetResultCode());
						Message msg = new Message();
						msg.obj = data;
						msg.arg1 = 1000;
						handler.sendMessage(msg);

					}
				}, false, false);

	}

	/**
	 * 调用方法，从网络获取周边商品数据
	 * 
	 * @param context
	 */
	private void getdataFromService_down() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("pageNo", "1");
		params.put("rows", "5");
		params.put("userType", "1");
		params.put("venueId", venueId);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.SERVICE_FOODDIANPU_LIST, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if (data == null) {
							// 说明网络获取无数据
							Message msg = new Message();
							msg.arg1 = TAG_DATANULL;
							handler.sendMessage(msg);
						} else {
							Object obj = data.getObject();
							if (obj == null) {
								// 说明数据为空
								Message msg = new Message();
								msg.arg1 = TAG_DATANULL;
								handler.sendMessage(msg);
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
									Message msg = new Message();
									msg.arg1 = TAG_DATANULL;
									handler.sendMessage(msg);
								} else {
									if (data.getNetResultCode() == 0) {
										Message msg = new Message();
										msg.arg1 = 110;
										msg.obj = backdata;
										handler.sendMessage(msg);
									} else {
										Message msg = new Message();
										msg.arg1 = 111;
										handler.sendMessage(msg);
									}
								}
							}
						}
					}

				}, false, true);
	}

	/**
	 * 调用方法。解析数据
	 * 
	 * @param backdata
	 */
	private void jsonDataOfString(String backdata) {
		// TODO Auto-generated method stub
		List<ServiceFoodBean> list = new ArrayList<ServiceFoodBean>();
		try {
			JSONArray array = new JSONArray(backdata);
			int count = array.length();
			for (int i = 0; i < count; i++) {
				ServiceFoodBean bean = new ServiceFoodBean();
				JSONObject obj = array.getJSONObject(i);
				if (obj.has("startbusinesstime")) {
					bean.setStartTime(obj.getString("startbusinesstime"));
				}
				if (obj.has("endbusinesstime")) {
					bean.setEndTime(obj.getString("endbusinesstime"));
				}
				if (obj.has("address")) {
					bean.setAddress(obj.getString("address"));
				}
				if (obj.has("companyname")) {
					bean.setCompanyname(obj.getString("companyname"));
				}
				if (obj.has("contact")) {
					bean.setContact(obj.getString("contact"));
				}
				if (obj.has("id")) {
					bean.setId(obj.getString("id"));
				}
				if (obj.has("mobilephone")) {
					bean.setMobilephone(obj.getString("mobilephone"));
				}
				if (obj.has("telephone")) {
					bean.setTelephone(obj.getString("telephone"));
				}
				if (obj.has("zipcode")) {
					bean.setZipcode(obj.getString("zipcode"));
				}
				if (obj.has("imageUrl")) {
					bean.setImageUrl(obj.getString("imageUrl"));
				}
				list.add(bean);

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (list != null && list.size() != 0) {
			// 调用方法，将数据加载到适配器以及控件中
			initListview(context, list);
		}
	};

	/**
	 * @param context
	 * @param list
	 * @2016-2-16上午10:44:06 初始化界面listview
	 */
	private void initListview(final Context context,
			final List<ServiceFoodBean> list) {
		// TODO Auto-generated method stub
		mAdapter = new ServiceFoodAdapter(list, OnlineContainActivity.this);
		mListview.setAdapter(mAdapter);
		mListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				MobclickAgent.onEvent(OnlineContainActivity.this,
						"SellerListPage,");
				// TODO Auto-generated method stub
				// 首先需要拿着现在的事件去和店铺的营业时间进行比较
				GlobalParams.Online_Address = list.get(arg2).getAddress();
				GlobalParams.Online_Name = list.get(arg2).getCompanyname();
				GlobalParams.Online_Phone = list.get(arg2).getTelephone();
				Date date = new Date();
				long currenttimes = date.getTime();
				String currenttimesss = getChangeToTime(currenttimes + "");
				String currenttime_hour = getHourAndMinute(currenttimesss);
				Log.i("当前时间是", currenttime_hour);
				// 分别截取时间的小时数来进行比较和计算
				String current = getHourNum(currenttime_hour);
				if (!TextUtils.isEmpty(list.get(arg2).getStartTime())) {
					String start = getHourNum(list.get(arg2).getStartTime());
					if (!TextUtils.isEmpty(list.get(arg2).getEndTime())) {
						String end = getHourNum(list.get(arg2).getEndTime());
						int starts = Integer.parseInt(start);
						int ends = Integer.parseInt(end);
						int curr = Integer.parseInt(current);
						// 调用方法，从该毫秒值上面截取小时数
						Log.i("现在的时间是", starts + "sssss" + ends + "ssss" + curr);
						if (starts <= curr && curr <= ends) {
							Intent intent = new Intent(
									OnlineContainActivity.this,
									OnlineCateringDetailActivity.class);
							intent.putExtra("id", list.get(arg2).getId());
							intent.putExtra("title", list.get(arg2)
									.getCompanyname());
							intent.putExtra("stime", list.get(arg2)
									.getStartTime());
							intent.putExtra("etime", list.get(arg2)
									.getEndTime());
							startActivity(intent);
						} else {
							createDialog(list.get(arg2).getStartTime(), list
									.get(arg2).getEndTime());
						}
					}
				}
			}
		});
	}

	/**
	 * 根据传入时间来截取小时数
	 * 
	 * @param currenttime_hour
	 * @return
	 */
	private String getHourNum(String currenttime_hour) {
		// TODO Auto-generated method stub
		String hms = "";
		if (!TextUtils.isEmpty(currenttime_hour)) {
			hms = currenttime_hour.substring(0, 2);
			String hmss = hms.substring(0, 1);
			if (hmss.equals("0")) {
				hms = hms.substring(1, 2);
			}
			return hms;
		}
		return hms;

	}

	/**
	 * 根据传入的时间值，截取小时和分钟数
	 * 
	 * @param str
	 * @return
	 */
	public String getHourAndMinute(String str) {
		String hm = "";
		if (!TextUtils.isEmpty(str)) {
			hm = str.substring(11, 16);
		}
		return hm;

	}

	/**
	 * 将一个传入的字符串毫秒值转换成一个字符串时间值
	 * 
	 * @param str
	 * @return
	 */
	public String getChangeToTime(String str) {
		String timestring = "";
		if (!TextUtils.isEmpty(str)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss",
					Locale.getDefault());
			long time = Long.parseLong(str);
			Date date = new Date();
			date.setTime(time);
			timestring = sdf.format(date);
		}

		return timestring;

	}

	private void createDialog(String string, String string2) {
		exitDialog = new CustomDialog(new OnClickListener() {

			@Override
			public void onClick(View v) {
				exitDialog.dismiss();
			}
		});
		exitDialog.setConfirmTxt("确认");
		exitDialog.setRemindTitle("温馨提示");
		// exitDialog.setRemindMessage("抱歉，店铺还未营业"+string+"-"+string2);
		exitDialog.setRemindMessage("抱歉，店铺还未营业");
		exitDialog.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.online_title_left_iv:
			finish();
			break;
		case R.id.online_tv_title:
			// 弹出城市选择控件

			if (listVenue != null && listVenue.size() > 0) {
				showPopupWindows(top, listVenue);
			} else {
				// 提示用户，当前暂无城市可选
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 根据传入的控件资，以及坐标点，来弹出一个window
	 */
	@SuppressWarnings("deprecation")
	public void showPopupWindows(View view, final List<Venue> list) {
		int ofy = view.getMeasuredHeight() / 2 + view.getMeasuredHeight();
		mRightLayout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.select_sprot, null);
		// 初始化该布局内控件
		mListviewLocation = (ListView) mRightLayout
				.findViewById(R.id.lv_pop_venue);
		mListLoactionData.clear();
		if(GlobalParams.MY_LOCATION!=null){
			if(!TextUtils.isEmpty(GlobalParams.MY_LOCATION.getLatitude()+"")&&!TextUtils.isEmpty(GlobalParams.MY_LOCATION.getLongitude()+"")){
				if(list.size()==1){
					//不做处理
						mListLoactionData.add(list.get(0));
				}else if(list.size()==2){
					//由于目前说一个城市最多有两个场馆，所以在这里只是单独的做一判断处理
					if(GlobalParams.MY_LOCATION!=null){
						String length1;
						String length2;
						if(!TextUtils.isEmpty(list.get(0).getLatitude())&&!TextUtils.isEmpty(list.get(0).getLongitude())){
							
						}
						length1=getDistance(GlobalParams.MY_LOCATION.getLatitude()+"",GlobalParams.MY_LOCATION.getLongitude()+"",list.get(0).getLatitude(), list.get(0).getLongitude());
						length2=getDistance(GlobalParams.MY_LOCATION.getLatitude()+"",GlobalParams.MY_LOCATION.getLongitude()+"",list.get(1).getLatitude(), list.get(1).getLongitude());
						int lengthss1=Integer.parseInt(length1);
						int lengthss2=Integer.parseInt(length2);
						if(lengthss1>lengthss2){
							mListLoactionData.add(list.get(0));
							mListLoactionData.add(list.get(1));
						}else{
							mListLoactionData.add(list.get(1));
							mListLoactionData.add(list.get(0));
						}
					}
				}else if(list.size()==0){
					//不做处理
					
				}else{
					for (int i = 0; i < list.size(); i++) {
						mListLoactionData.add(list.get(i));
					}
				}
			}else{
				for (int i = 0; i < list.size(); i++) {
					mListLoactionData.add(list.get(i));
				}
			}
		}else{
			for (int i = 0; i < list.size(); i++) {
				mListLoactionData.add(list.get(i));
			}
		}
		
		
		if (mLocationAdapter != null) {
			mLocationAdapter.setlist(mListLoactionData);
			mListviewLocation.setAdapter(mLocationAdapter);
		} else {
			mLocationAdapter = new LocationDataAdapterGoods(mListLoactionData,
					OnlineContainActivity.this);
			mListviewLocation.setAdapter(mLocationAdapter);
		}
		mListviewLocation.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				final SharedPreferences preferences = getSharedPreferences(
						SHAREDPREFERENCES_VENUE_CITYID, MODE_PRIVATE);
				preferences.edit().putString("CITYID",GlobalParams.CITY_ID).commit();
				final SharedPreferences preferencesvenue = getSharedPreferences(
						SHAREDPREFERENCES_VENUE_VENUEID, MODE_PRIVATE);
				preferencesvenue.edit().putString("VENUEID",mListLoactionData.get(arg2).getId()).commit();
				// 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
				Venue venue = mListLoactionData.get(arg2);
				venueId = venue.getId();
				mtitle.setText(venue.getVenueName());
				mPopupWindow.dismiss();
				getdataFromService();
			}
		});
		int height=0;
		if(list.size()==1){
			 height= DensityUtil.dip2px(OnlineContainActivity.this, 20)
						* list.size();
		}else if(list.size()>=2){
			height= DensityUtil.dip2px(OnlineContainActivity.this, 40)
					* list.size();
		}
		
		mPopupWindow = new PopupWindow(this);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setWidth(GlobalParams.WIN_WIDTH * 2 / 3);
		mPopupWindow.setHeight(height);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setContentView(mRightLayout);
		mPopupWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL
				| Gravity.TOP, 0, ofy);

		// mPopupWindow.showAsDropDown(view, 0, 0, Gravity.CENTER_HORIZONTAL);
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
	 /**
     * 根据两个位置的经纬度，来计算两地的距离（单位为KM）
     * 参数为String类型
     * @param lat1 用户经度
     * @param lng1 用户纬度
     * @param lat2 商家经度
     * @param lng2 商家纬度
     * @return
     */
    public static String getDistance(String lat1Str, String lng1Str, String lat2Str, String lng2Str) {
        Double lat1 = Double.parseDouble(lat1Str);
        Double lng1 = Double.parseDouble(lng1Str);
        Double lat2 = Double.parseDouble(lat2Str);
        Double lng2 = Double.parseDouble(lng2Str);
         
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double difference = radLat1 - radLat2;
        double mdifference = rad(lng1) - rad(lng2);
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(difference / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(mdifference / 2), 2)));
        distance = distance * EARTH_RADIUS;
        distance = Math.round(distance * 10000) / 10000;
        String distanceStr = distance+"";
        distanceStr = distanceStr.
            substring(0, distanceStr.indexOf("."));
         
        return distanceStr;
    }
    private static double rad(double d) { 
        return d * Math.PI / 180.0; 
    }
	@Override
	protected void onDestroy() {
		if(listVenue!=null){
			listVenue.clear();
			listVenue = null;
		}
		if(mListLoactionData!=null){
			mListLoactionData.clear();
			mListLoactionData = null;
		}
		mLocationAdapter= null;
		mListviewLocation= null;
		mPopupWindow = null;
		mAdapter= null;
		mAdapter = null;
		context = null;
		mPullToRefreshListview = null;
		mListview = null;
		System.gc();
		super.onDestroy();
	}


}
