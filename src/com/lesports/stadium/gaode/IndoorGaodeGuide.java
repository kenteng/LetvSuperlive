package com.lesports.stadium.gaode;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.animation.ObjectAnimator;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.im.data.IMDataManager;
import com.amap.api.im.data.IMRoutePlanning;
import com.amap.api.im.listener.DownloadStatusCode;
import com.amap.api.im.listener.IMDataDownloadListener;
import com.amap.api.im.listener.IMMapEventListener;
import com.amap.api.im.listener.IMMapLoadListener;
import com.amap.api.im.listener.IMRoutePlanningListener;
import com.amap.api.im.listener.MapLoadStatus;
import com.amap.api.im.listener.RoutePLanningStatus;
import com.amap.api.im.util.IMFloorInfo;
import com.amap.api.im.util.IMLog;
import com.amap.api.im.util.IMPoint;
import com.amap.api.im.util.IMSearchResult;
import com.amap.api.im.view.IMIndoorMapFragment;
import com.autonavi.indoor.constant.Configuration;
import com.autonavi.indoor.constant.MessageCode;
import com.autonavi.indoor.entity.LocationResult;
import com.autonavi.indoor.location.ILocationManager;
import com.autonavi.indoor.onlinelocation.OnlineLocator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lesports.stadium.R;
import com.lesports.stadium.adapter.LocationDataAdapterGoods;
import com.lesports.stadium.bean.Venue;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.gaode.route.PathFragment;
import com.lesports.stadium.gaode.route.PoiInfo;
import com.lesports.stadium.gaode.route.PoiMapCell;
import com.lesports.stadium.gaode.search.IMSearchFragment;
import com.lesports.stadium.gaode.util.Constant;
import com.lesports.stadium.gaode.view.FloorPromotedActionsLibrary;
import com.lesports.stadium.gaode.view.GaodeGestureListener;
import com.lesports.stadium.gaode.view.PromotedActionsLibrary;
import com.lesports.stadium.gaode.view.SettingMenu;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.DensityUtil;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.view.CustomDialog;

public class IndoorGaodeGuide extends FragmentActivity implements View.OnClickListener {
	private List<Venue> listVenue = new ArrayList<Venue>();
	/**
	 * 地址列表数据源
	 */
	private List<Venue> mListLoactionData = new ArrayList<>();
	/**
	 * 场馆Id
	 */
	private String venueId;
	private boolean canShow=true;

	/**
	 * 处理数据的handler；
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case 1000:
				
				BackData backData = (BackData) msg.obj;
//				Log.v("123", "场馆列表  " + backData.getObject().toString());
//				Log.e("data", "场馆列表  " + backData.getObject().toString());
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
						venueId = listVenue.get(0).getId();
						String buildId=listVenue.get(0).getBuildId();
						mtitle.setText(listVenue.get(0).getVenueName());
				
						 String isSurport=listVenue.get(0).getSupportGuide();
						if("0".equals(isSurport)||null==isSurport){
//							Toast.makeText(IndoorGaodeGuide.this, "暂无场馆数据", 0).show();
							canShow=false;
							rl_nosupport.setVisibility(View.VISIBLE);
						}else{
							rl_nosupport.setVisibility(View.GONE);
							canShow=true;
							loadMap(buildId);
							  initLocationButton();
						 
						        initSettingButton();
						        initRoutePlanningFragment();

						}
//						loadMap(buildId);
						if(listVenue.size()==1){
							mtitle.setCompoundDrawables(null, null, null, null);
							mtitle.setClickable(false);
						}
					}
				}

				break;
		

			default:
				break;
			}
		}
	};
	
	public void loadMap(String buildId){
	     mDataManager = IMDataManager.getInstance();
	        IMDataManager.setRequestTimeOut(5000);
//	        mDataManager.setDataPath(Environment.getExternalStorageDirectory() + "/test_data");
//	        mDataManager.downloadBuildingData(mBuildingId, mDataDownloadListener);
	        //mDataManager.setDataPath(Environment.getExternalStorageDirectory() + "/test_data");
	        //mIndoorMapFragment.setDataPath(Environment.getExternalStorageDirectory() + "/test_data");
	        mIndoorMapFragment.loadMap(buildId, mMapLoadListener);//B023B173VP
	        mIndoorMapFragment.setMapEventListener(mMapEventListener);
	        mIndoorMapFragment.initSwitchFloorToolBar();
	        
	        mIndoorMapFragment.initZoomView();
	        ///隐藏缩放控件
	        mIndoorMapFragment.hideZoomView();
	        mIndoorMapFragment.initCompass();
	        //隐藏指南针
	        mIndoorMapFragment.hideCompassView();
	        mIndoorMapFragment.initPlottingScale();
	        //隐藏比例尺
	        mIndoorMapFragment.hidePlottingScale();
	        //隐藏楼层选择
	        mIndoorMapFragment.hideFloorView();
	        //隐藏高德地图logo
	        mIndoorMapFragment.hideAmapLogo();
	        createDialog();
	        
	}

    private IMIndoorMapFragment mIndoorMapFragment = null;
    private IMSearchFragment mSearchFragment = null;
    private PathFragment mPathFragment = null;

//    private EditText mSearchEditText = null;

    private IMDataManager mDataManager = null;

    private SettingMenu mSettingMenu = null;

    private Context mContext = null;

    private boolean mLocationStatus = false;                // 定位按钮状态


    private IMPoint mLastPoint = null;

    private String mLastSelectedPoiId = null;

    private ImageView mLocationView = null;

    private RelativeLayout mLocatingLayout = null;

//    private Button mSettingButton = null;
	private FloorPromotedActionsLibrary floorPromotedActionsLibrary;
	private PromotedActionsLibrary promotedActionsLibrary;
	private Button mainButton;
	//判断楼层是否展开
	private boolean isMainOpened;
	//判断搜索是否展开
	private boolean isMenuOpened;
	

//    // 去这里Tips变量
//    private View mBottomViewDetail = null;
//    private TextView mTextPoi;
//    private TextView mTextPoiDetail;
//    
	
	// 卫生间的type值
		private List<String> toiletList = Arrays.asList(new String[] { "200300",
				"200301", "200302", "200303", "200304" });
		// 扶梯
		private List<String> stairsList = Arrays.asList(new String[] { "990200","990100"});
		// 咨询
		private List<String> consultingList = Arrays.asList(new String[] {
				"070200", "070201","070202","070203","980452"});
		private List<Integer> consultingListInt = Arrays.asList(new Integer[] {
				070200, 070201,070202,070203,980452});
		// 出口
		private List<String> exitList = Arrays.asList(new String[] {"990800","991000","991001"});
	
	//商铺
	private List<String> shoppList = Arrays.asList(new String[] {"AM0001","AM0002","AM0003","AM0004","AM0007","AM0008","AM0010","AM0012","AM0013","AM0015"});

    private ImageView mCustomImage=null;
    String defaultBuilding="B0FFFAB6J2";//默认建筑//"B0FFFAB6J2"//B023B173VP//B01370626Q //B000A83AJN/ B000A7XW5D

    FrameDrawOverRunnable mFrameDrawOverRunnable=null;
    
	private PopupWindow pop;
	private LinearLayout ll_popup;
	private BluetoothAdapter blueadapter;
	RelativeLayout  rl_nosupport;
	
   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getApplicationContext();
        IMLog.logd("#######  --------------------------   App start   ---------------------------  #######");
        final boolean supportsEs2 = IMIndoorMapFragment.isSupportsOpenGlEs2(this);
        if (supportsEs2) {

            setContentView(R.layout.activity_indoor);
            mIndoorMapFragment =  (IMIndoorMapFragment)getSupportFragmentManager()
                        .findFragmentById(R.id.indoor_main_map_view);
            //设置自定义样式路径
            mIndoorMapFragment.setStyleDirectory("im_style");
            mIndoorMapFragment.setIconDirectory("im_icon");

            mSettingMenu = new SettingMenu(this);
            mSettingMenu.setIndoorMapFragment(mIndoorMapFragment);
            mSettingMenu.setMainActivity(this);
            mSettingMenu.setMapLoadListener(mMapLoadListener);
        } else {
            // Should never be seen in production, since the manifest filters
            // unsupported devices.
            Toast.makeText(this, "This device does not support OpenGL ES 2.0.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        //加载场馆数据
       rl_nosupport=(RelativeLayout) findViewById(R.id.rl_nosupport);
        getVenue();
        initOtherUI();
        L.d("##start!");
   
    	
    }
    


    /**
     * init UI
     */
    private void initOtherUI() {
    	mBack = (ImageView) findViewById(R.id.online_title_left_iv);
    	mBack.setOnClickListener(this);  	
    	mtitle = (TextView) findViewById(R.id.online_tv_title);
		mtitle.setOnClickListener(this);
		top = (RelativeLayout) findViewById(R.id.top);
		
	  rl_compass = (RelativeLayout) findViewById(R.id.rl_compass);
	  rl_compass.setOnClickListener(this);
		
		
		tv_seat_info = (EditText) findViewById(R.id.tv_seat_info);
		tv_seat_info.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					tv_seat_info.setHint("例:  223");
					tv_search.setVisibility(View.VISIBLE);
//					boolean isInputFirst=true;
//					if(!isInputFirst){
//						Timer timer = new Timer(); 
//						timer.schedule(new TimerTask() { //让软键盘延时弹出，以更好的加载Activity
//	
//						public void run() { 
//						InputMethodManager inputManager =
//						(InputMethodManager)tv_seat_info.getContext().
//						getSystemService(Context.INPUT_METHOD_SERVICE);
//	
//						inputManager.showSoftInput(tv_seat_info, 0); 
//						} 
//	
//						}, 500);
//					}
				}else{
					tv_seat_info.setHint("请输入 包厢号/区域号 导航");
					tv_search.setVisibility(View.GONE);
				}
				
			}
		});
		tv_search = (TextView) findViewById(R.id.tv_search);
		tv_search.setOnClickListener(this);
		
//		Log.e("citydi", GlobalParams.CITY_ID);
        mPathFragment = PathFragment.newInstance(this, mIndoorMapFragment);
        mPathFragment.setLocationView(mLocationView);
        
        iv_compass_icon = (ImageView) findViewById(R.id.iv_compass_icon);
        
        rl_parent = (RelativeLayout) findViewById(R.id.rl_pop);
        
     // 打开蓝牙

     		blueadapter = BluetoothAdapter.getDefaultAdapter();
     		
     		
     		View popupView = View
     				.inflate(getApplicationContext(), R.layout.item_popupwindows, null);
     	
     		//蓝牙的popuwWindow
     		 RelativeLayout poplayout=(RelativeLayout)popupView.findViewById(R.id.parent);
     		
     		 
     		pop = new PopupWindow(getApplicationContext());
     		 poplayout.setOnTouchListener(new GaodeGestureListener(IndoorGaodeGuide.this,pop));
     		ll_popup = (LinearLayout) popupView.findViewById(R.id.ll_popup);
     		pop.setWidth(LayoutParams.WRAP_CONTENT);
     		pop.setHeight(LayoutParams.WRAP_CONTENT);
     		pop.setBackgroundDrawable(new BitmapDrawable());
     		pop.setFocusable(true);
     		pop.setOutsideTouchable(true);
     		pop.setContentView(popupView);
     		// popupWindow的close点击事件
     		ImageView iv_close = (ImageView) popupView.findViewById(R.id.iv_close);
     		iv_close.setOnClickListener(new OnClickListener() {

     			@Override
     			public void onClick(View v) {
     				pop.dismiss();
     			}
     		});
     		
     		
     		 FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fl_search);
    		 promotedActionsLibrary = new PromotedActionsLibrary();
    		 promotedActionsLibrary.setup(getApplicationContext(), frameLayout);

//    	        View.OnClickListener onClickListener = new View.OnClickListener() {
//    	            @Override
//    	            public void onClick(View view) {
//    	            	promotedActionsLibrary.closePromotedActions().start();
    //
//    	            }
//    	        };	        
    	
    	        promotedActionsLibrary.addItem(getApplication().getResources().getDrawable(R.drawable.guide_shopp), new View.OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					searchService(5);
    					promotedActionsLibrary.setMainButtonBg(R.drawable.guide_shopp);
    					promotedActionsLibrary.closePromotedActions().start();
    					isMenuOpened = false;
    					
    				}
    			});
    	        promotedActionsLibrary.addItem(getApplication().getResources().getDrawable(R.drawable.guide_consulting), new View.OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					searchService(4);
    					isMenuOpened = false;
    					promotedActionsLibrary.setMainButtonBg(R.drawable.guide_consulting);
    					promotedActionsLibrary.closePromotedActions().start();
    				}
    			});	
    	        promotedActionsLibrary.addItem(getApplication().getResources().getDrawable(R.drawable.gaode_guide_elevator), new View.OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					searchService(3);		
    					isMenuOpened = false;
    					promotedActionsLibrary.setMainButtonBg(R.drawable.gaode_guide_elevator);
    					promotedActionsLibrary.closePromotedActions().start();
    				}
    			});
    	        promotedActionsLibrary.addItem(getApplication().getResources().getDrawable(R.drawable.guide_exit), new View.OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					promotedActionsLibrary.closePromotedActions().start();
    					promotedActionsLibrary.setMainButtonBg(R.drawable.guide_exit);
    					isMenuOpened = false;
    					searchService(2);
    				}
    			});

    	        promotedActionsLibrary.addMainItem(getApplication().getResources().getDrawable(R.drawable.toilet_icon),new View.OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    	                if (isMenuOpened) {
    	                	searchService(1);
    	                	promotedActionsLibrary.closePromotedActions().start();
    	                	isMenuOpened = false;
    	                } else {
    	                	isMenuOpened = true;
    	                	promotedActionsLibrary.setMainButtonBg(R.drawable.toilet_icon);
    	                	promotedActionsLibrary.openPromotedActions().start();
    	                }
    				}
    			});

    }
    
    private void getVenue() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("cityId", GlobalParams.CITY_ID);
		Log.e("SSSSSSSSSS", GlobalParams.CITY_ID);
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
	 * 顶部右侧弹出菜单布局
	 */
	private LinearLayout mRightLayout;
	/**
	 * listview展示城市列表的listview
	 */
	private ListView mListviewLocation;
	/**
	 * 顶部右侧弹出菜单
	 */
	private PopupWindow mPopupWindow;
	/**
	 * 弹窗布局适配器
	 */
	private LocationDataAdapterGoods mLocationAdapter;
    
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
					IndoorGaodeGuide.this);
			mListviewLocation.setAdapter(mLocationAdapter);
		}
		mListviewLocation.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Venue venue = mListLoactionData.get(arg2);
				venueId = venue.getId();
				mtitle.setText(venue.getVenueName());
				mPopupWindow.dismiss();
				if(null!=venue.getBuildId()&&!"".equals(venue.getBuildId())){
					rl_nosupport.setVisibility(View.GONE);
					canShow=true;
					mIndoorMapFragment.loadMap(venue.getBuildId(), mMapLoadListener);
				}else{
					rl_nosupport.setVisibility(View.VISIBLE);


//					Toast.makeText(IndoorGaodeGuide.this, "没有该场馆的buildid", 0).show();
				}
			
			}
		});
		int height=0;
		if(list.size()==1){
			 height= DensityUtil.dip2px(IndoorGaodeGuide.this, 20)
						* list.size();
		}else if(list.size()>=2){
			height= DensityUtil.dip2px(IndoorGaodeGuide.this, 40)
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
	private static double EARTH_RADIUS = 6378.137; 

    /**
     * 初始化定位按钮
     */
    private void initLocationButton() {

        mLocationView = (ImageView)findViewById(R.id.locating_btn);

        mLocationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //IMLog.logd("#######-------- onClick ");
            	
            	if (null == blueadapter) {
					return;
				}		
            	// TODO Auto-generated method stub
				if (!blueadapter.isEnabled()) {
						pop.showAtLocation(rl_parent, Gravity.CENTER, 0, 0);
						return;
				}

                if (!mLocationStatus) {         // 处理开始定位
                    startLocating();
                    mLocationView.setImageResource(R.drawable.indoor_gps_locked);
                    mLocationStatus = true;
                    mFirstCenter = false;
                } else {                    // 处理结束定位
                    stopLocating();
                    mLocationView.setImageResource(R.drawable.indoor_gps_unlocked);
                    mIndoorMapFragment.clearLocatingPosition();
                    mIndoorMapFragment.clearLocationOnFloorView();
                    mIndoorMapFragment.refreshMap();
                    mLocationStatus = false;
                }

            }
        });
    }

    /**
     * 初始化设置按钮
     */
    private void initSettingButton() {
//        mSettingButton = (Button)findViewById(R.id.route_btn);
//
//        mSettingButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSettingMenu.show();
//
//
//            }
//        });
    }

    /**
     * 初始化路算试图
     */
    private void initRoutePlanningFragment() {

//        mBottomViewDetail = findViewById(R.id.bottom_view_detail);
//        mTextPoi = (TextView) findViewById(R.id.text_poi);
//        mTextPoiDetail = (TextView) findViewById(R.id.text_poi_detail);
//        Button btnGohere = (Button) findViewById(R.id.btn_gohere);
//        btnGohere.setOnClickListener(this);

    	bottomviewdetail = (LinearLayout) findViewById(R.id.bottomviewdetail);
    	text_poi = (TextView) findViewById(R.id.text_poi);
    	iv_destination_icon = (ImageView) findViewById(R.id.iv_destination_icon);
//    	text_poi_detail = (TextView) findViewById(R.id.text_poi_detail);
    	btn_gohere = (Button) findViewById(R.id.btn_gohere);
    	btn_gohere.setOnClickListener(this);
    }

    /**
     * 点击去这里按钮
     */
    public void btnGoHere() {

//        if (mLastSelectedPoiId == null) {
//            new AlertDialog.Builder(mIndoorMapFragment.getActivity())
//                    .setTitle("提示")
//                    .setMessage("未选择要到达的点!")
//                    .setIcon(
//                            android.R.drawable.ic_dialog_alert)
//                    .setPositiveButton("确定", null).show();
//            return;
//        }
        if(mPathFragment==null)
            return;
        mPathFragment.clear();
        mInfoTo = new PoiInfo();
        mInfoTo.PoiInfoType = Constant.TYPE_ROUTE_PLANNING_POI;
        PoiMapCell tmpPoiMapCell = new PoiMapCell();
        if (mLastSelectedPoiId != null && !mLastSelectedPoiId.equals("")) {
            tmpPoiMapCell.setPoiId(mLastSelectedPoiId);
            tmpPoiMapCell.setName(mLastSelectedPoiId);
        } else {
            tmpPoiMapCell.setPoiId(mLastSelectedPoiId);
            tmpPoiMapCell.setName("选择终点");
        }
        mInfoTo.cell = tmpPoiMapCell;
        mInfoTo.floor = new IMFloorInfo(mIndoorMapFragment.getCurrentFloorNo(), "", "0");
//        mPathFragment.setPoiInfoFrom();
//        mPathFragment.setPoiInfoTo(mInfoTo);

//        mSearchEditText.setVisibility(View.GONE);
//        mLocationView.setVisibility(View.GONE);
        btnSearch();
//        FragmentTransaction transcation = getSupportFragmentManager().beginTransaction();
//
//        transcation.setCustomAnimations(0, 0, 0,0);
//
//        if (!mPathFragment.isAdded()) {
//            transcation.hide(mIndoorMapFragment).add(R.id.indoor_main_view, mPathFragment)
//                    .commit();
//        } else {
//            transcation.hide(mIndoorMapFragment).show(mPathFragment)
//                    .commit();
//        }


    }
    
    
	PoiInfo mInfoFrom;
	
    public void btnSearch(){
//		if(mInfoFrom==null){
//			Toast.makeText(getActivity(), "请选择起始点", Toast.LENGTH_LONG).show();
//			return;
//		}
		if(mInfoTo==null){
			Toast.makeText(getApplicationContext(), "请选择终点", Toast.LENGTH_LONG).show();
			return;
		}

		IMRoutePlanning routePlanning = new IMRoutePlanning(getApplicationContext(),
				mRoutePlanningListener);
		String buildingId = IMDataManager.getInstance().getCurrentBuildingId();
//虚拟数据		
//		PoiInfo info = new PoiInfo();
//		info.PoiInfoType = Constant.TYPE_ROUTE_PLANNING_LOCATION;
//		String namecode="F5";
//		info.cell = new PoiMapCell(0 ,116.46301, 39.949066,
//				1, "我的位置");
//		info.floor = new IMFloorInfo(1, namecode, "-1");
//		this.mInfoFrom=info;
		
		this.mInfoFrom =  loadMylocation();
		if(null!=mInfoFrom){
			PoiMapCell fromMapCell = mInfoFrom.cell;
			PoiMapCell toMapCell = mInfoTo.cell;
	
			IMLog.logd("####### ------ from PoiInfoType:" + mInfoFrom.PoiInfoType);
			IMLog.logd("####### ------ to PoiInfoType:" + mInfoTo.PoiInfoType);
	
			if (mInfoFrom.PoiInfoType == Constant.TYPE_ROUTE_PLANNING_LOCATION) {
				routePlanning.excutePlanningPointToPoi(buildingId, fromMapCell.getFloorNo(),
						fromMapCell.getX(), fromMapCell.getY(), toMapCell.getPoiId());
				IMLog.logd("####### ------ start Point2Poi");
				return;
			}
	
			if (mInfoTo.PoiInfoType == Constant.TYPE_ROUTE_PLANNING_LOCATION) {
	
				routePlanning.excutePlanningPoiToPoint(buildingId, fromMapCell.getPoiId(),
						toMapCell.getFloorNo(), toMapCell.getX(), toMapCell.getY());
				IMLog.logd("####### ------ start Poi2Point");
				return;
			}
	
			if (mInfoTo.PoiInfoType == Constant.TYPE_ROUTE_PLANNING_POI &&
					mInfoFrom.PoiInfoType == Constant.TYPE_ROUTE_PLANNING_POI) {
				routePlanning.excutePlanningPoiToPoi(buildingId, fromMapCell.getPoiId(),
						toMapCell.getPoiId());
				IMLog.logd("####### ------ start Poi2Poi");
			}

		}

	}


	/**
	 * 路算回调接口
	 */
	private IMRoutePlanningListener mRoutePlanningListener = new IMRoutePlanningListener() {
		@Override
		public void onPlanningSuccess(String routePlanningData) {
			// TODO Auto-generated method stub
//			Toast.makeText(getActivity(), "路算成功", Toast.LENGTH_LONG).show();
			Log.e("SSSSSSSS", routePlanningData);
			mIndoorMapFragment.clearRouteStart();
			mIndoorMapFragment.clearRouteStop();
			mIndoorMapFragment.clearSelected();
			mIndoorMapFragment.clearFeatureColor("");
			drawRouteStartAndStop(routePlanningData);
			mIndoorMapFragment.refreshMap();

		}

		@Override
		public void onPlanningFailure(RoutePLanningStatus statusCode) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "路算失败,失败码:" + statusCode, Toast.LENGTH_LONG).show();
		
		}
	};
	
	/**
	 * 设置路径规划开始点和停止点
	 */
	public void drawRouteStartAndStop(String routePlanningData) {
		String fromPoiId = mInfoFrom.cell.getPoiId();
		String toPoiId = mInfoTo.cell.getPoiId();
		IMLog.logd("####### ------ from:" + fromPoiId + ", to:" + toPoiId);

		if (fromPoiId != null && !fromPoiId.equals("")) {
			mIndoorMapFragment.setRouteStart(fromPoiId);
		}

		if (toPoiId != null && !toPoiId.equals("")) {
			mIndoorMapFragment.setRouteStop(toPoiId);
		}

		mIndoorMapFragment.setRouteData(routePlanningData);
	}

	/**
	 * 未定位提示按钮
	 */
	private CustomDialog hintDialog;
	/**
	 * 创建未登陆提示框
	 */
	private void createDialog() {
		hintDialog = new CustomDialog(IndoorGaodeGuide.this, new OnClickListener() {
			@Override
			public void onClick(View v) {
				hintDialog.dismiss();
			}
		}, new OnClickListener() {	

			@Override
			public void onClick(View v) {
				hintDialog.dismiss();
			}
		});
		hintDialog.setRemindTitle("温馨提示");
		hintDialog.setCancelTxt("取消");
		hintDialog.setConfirmTxt("确定");
		hintDialog.setRemindMessage("没有定位结果,无法使用定位位置");
	}


    /**
     * Called when a view has been clicked.
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
        
        
        case R.id.rl_compass:
        	mIndoorMapFragment.resetMap();
        	ObjectAnimator.ofFloat(iv_compass_icon, "rotation",0).setDuration(100).start();
            break;
            // 到这里按键
            case R.id.btn_gohere:
                //IMLog.logd("#######-------- onClick ");
                btnGoHere();
                bottomviewdetail.setVisibility(View.GONE);
//                mBottomViewDetail.setVisibility(View.GONE);
//                mSearchEditText.setVisibility(View.GONE);
                break;
            case R.id.online_title_left_iv:
                finish();
                break;
            case R.id.online_tv_title:
            	if(canShow){
	            	promotedActionsLibrary.closePromotedActions().start();
	            	 floorPromotedActionsLibrary.closePromotedActions().start();
            	}
            	if (listVenue != null && listVenue.size() > 0) {
    				showPopupWindows(top, listVenue);
    			} else {
    				// 提示用户，当前暂无城市可选
    			}
                break;
                
            case R.id.tv_search:
            	  List<IMSearchResult> srList = IMDataManager.getInstance()
                  .searchByName(tv_seat_info.getText().toString(),0);
            	  if(srList.size()>0){
            		  IMSearchResult result = srList.get(0);
            		  int curFloorNo = mIndoorMapFragment.getCurrentFloorNo();

                      // 不在当前层,则切换楼层
                      if (curFloorNo != result.getFloorNo()) {
                    	  mIndoorMapFragment.switchFloorByFloorNo(result.getFloorNo());
                    	  if(curFloorNo>0){
                    		  mainButton.setText("F"+Math.abs(result.getFloorNo()));
                    	  }else{
                    		  mainButton.setText("B"+Math.abs(result.getFloorNo()));
                    	  }
                      }
                      String name="";
                      if (result.getId() != null && !result.getId().equals("")) {
                          IMLog.logd("sourceIasdfsadsasad"+result.getId());
                          if(mLastSelectedPoiId!=null)
                          {
                              mIndoorMapFragment.clearFeatureColor(mLastSelectedPoiId);
                          }
                          mLastSelectedPoiId = result.getId();

                          mIndoorMapFragment.selectFeature(result.getId());//气泡

                          mIndoorMapFragment.setFeatureColor(result.getId(),"0x00000000");//高亮
                          mIndoorMapFragment.refreshMap();
                          //mIndoorMapFragment.setFeatureCenter(poiId);//居中
                          String toasttext="";
                          toasttext+="PoiId:" + result.getId();
                          IMSearchResult searchresult = mDataManager.searchByID(result.getId());
                          if (searchresult!=null)
                          {
                              if (searchresult.getName()!=null) {
                                  toasttext+="\n"+"PoiName:" + searchresult.getName();
                                  name= searchresult.getName();
                              }
                              if (searchresult.getCatagory()!=null) {
                                  toasttext+="\n"+"基本分类:" + searchresult.getCatagory();
                                  ArrayList<String> cats= new ArrayList<String>();
                                  cats.add(searchresult.getCatagory());
                                  toasttext+="\n"+"本类型个数:" + mDataManager.searchByCategories(cats,mDataManager.getCurrentFloorNo()).size();
                              }
                          }

                          if (mPathFragment == null || !mPathFragment.isPoiSelect()) {
                          	bottomviewdetail.setVisibility(View.VISIBLE);
                          	text_poi.setText(name);
                          	showTips(searchresult.getCatagory());
                          }

                      } else {

                      }


                      mIndoorMapFragment.selectSearchResult(result.getId());
                      
                      
                  
                      
            	  }
//                Toast.makeText(IndoorGaodeGuide.this, tv_seat_info.getText().toString(), 0).show();
                break; 
                
                
            default:

                break;
        }

    }

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        // 获取手机当前音量值

        switch (keyCode) {
//            case KeyEvent.KEYCODE_BACK:
//            	mIndoorMapFragment.clearRouteStart();
//    			mIndoorMapFragment.clearRouteStop();
//    			mIndoorMapFragment.clearSelected();
//    			mIndoorMapFragment.clearFeatureColor("");
//                return true;
        }
        return super.onKeyDown (keyCode, event);
    }
    
    boolean floorFirst=true;

    /**
     * 地图加载回调接口
     */
    private IMMapLoadListener mMapLoadListener = new IMMapLoadListener() {

        @Override
        public void onMapLoadSuccess() {
            Toast.makeText(mIndoorMapFragment.getActivity(), "地图加载完毕",
                    Toast.LENGTH_LONG).show();
            //初始化定位方式
            initLocating(Configuration.LocationProvider.BLE);
            mIndoorMapFragment.getCurrentFloorInfoList();


            floorPromotedActionsLibrary = new FloorPromotedActionsLibrary();
//            if(!floorFirst){
//            	floorPromotedActionsLibrary.clearButton();
//            	floorFirst=false;
//            }
            
    		FrameLayout fl_floor_search = (FrameLayout) findViewById(R.id.fl_floor_search);
    		floorPromotedActionsLibrary.setup(getApplicationContext(), fl_floor_search);
    		 int total = mIndoorMapFragment.getCurrentFloorInfoList().size();
    		 
	        RelativeLayout.LayoutParams ll =(RelativeLayout.LayoutParams) fl_floor_search.getLayoutParams(); //取控件当前的布局参数   
//	        Log.e("SSSSSSSS",  DensityUtil.dip2px(getApplicationContext(),  50)*total+"");
	        ll.height = DensityUtil.dip2px(getApplicationContext(),  50)*total;
	        ll.width= DensityUtil.dip2px(getApplicationContext(),  50);
	        fl_floor_search.setLayoutParams(ll); //使设置好的布局参数应用到控件 
//	        final ObjectAnimator closeObjectAnimator;
//	        closeObjectAnimator = ObjectAnimator.ofFloat(mLocationView, View.TRANSLATION_Y, (float)(-fl_floor_search.getLayoutParams().height),0f);
//	        closeObjectAnimator.setRepeatCount(0);
//	        closeObjectAnimator.setDuration(50 * (total));
//	        
//	        final ObjectAnimator openObjectAnimator;
//	        openObjectAnimator = ObjectAnimator.ofFloat(mLocationView, View.TRANSLATION_Y,  0f,(float)(-fl_floor_search.getLayoutParams().height));
//	        openObjectAnimator.setRepeatCount(0);
//	        openObjectAnimator.setDuration(50 * (total));

    		 List<IMFloorInfo> currentFloorInfoList = mIndoorMapFragment.getCurrentFloorInfoList();
//    		 List<FloorInfo> allFloorIds=mMapView.getFloorList();
    		 for(int i=0; i<total; i++) {
    			 Log.e("SSSSSSSNO", currentFloorInfoList.get(i).getFloorNo()+"");
    			 final int select=currentFloorInfoList.get(i).getFloorNo();
    			 if(select>0){
	    	        	floorPromotedActionsLibrary.addItem("F"+select, new View.OnClickListener() {
	    					
	    					@Override
	    					public void onClick(View v) {
	    						
	    						 floorPromotedActionsLibrary.closePromotedActions().start();
//	    						 closeObjectAnimator.start();
	    						 mIndoorMapFragment.switchFloorByFloorNo(select);
	    						 isMainOpened = false;
	    						 mainButton.setText("F"+Math.abs(select));
	    					}
	    				});
	//    	            addFloor(inflater, lp,info.fl_index, info.fl_namecode);
	    	        }else{
	    	        	floorPromotedActionsLibrary.addItem("B"+Math.abs(select), new View.OnClickListener() {
	    					
	    					@Override
	    					public void onClick(View v) {
	    						
	    						 floorPromotedActionsLibrary.closePromotedActions().start();
//	    						 closeObjectAnimator.start();
	    						 mIndoorMapFragment.switchFloorByFloorNo(select);
	    						 isMainOpened = false;
	    						 mainButton.setText("B"+Math.abs(select));
	    					}
	    				});
	    	        }
	    		 }
    		 
    		mainButton = floorPromotedActionsLibrary.addMainItem(getApplication().getResources().getDrawable(R.drawable.toilet_icon),new View.OnClickListener() {

    				@Override
    				public void onClick(View v) {
    					if(isMainOpened){
//    						mLocationView
    						floorPromotedActionsLibrary.closePromotedActions().start();
//    						closeObjectAnimator.start();
    						isMainOpened = false;
    					}else{
    						isMainOpened = true;
    						floorPromotedActionsLibrary.openPromotedActions().start();
//    						openObjectAnimator.start();
    					}	                	
    				}
    			});
        }

        @Override
        public void onMapLoadFailure(MapLoadStatus mapLoadStatus) {
            //IMLog.logd("#######-------- onMapLoadFailure:" + mapLoadStatus + ", id:" + Thread.currentThread().getId());
            Toast.makeText(mIndoorMapFragment.getActivity(), "地图加载失败,失败状态:" + mapLoadStatus,
                    Toast.LENGTH_LONG).show();
        }

    };

    /**
     * 下载回调接口
     */
    private IMDataDownloadListener mDataDownloadListener = new IMDataDownloadListener() {

        @Override
        public void onDownloadSuccess(String buildingId) {
            // TODO Auto-generated method stub
            IMLog.logd("#######-------- download success:" + buildingId + ", id:" + Thread.currentThread().getId());
            //mDataManager.loadBuildingData();

        }

        @Override
        public void onDownloadFailure(String buildingId, DownloadStatusCode statusCode) {
            // TODO Auto-generated method stub
            IMLog.logd("#######-------- download failure:" + buildingId + ", errorCode:" +
                    statusCode + ", id:" + Thread.currentThread().getId());
        }

        @Override
        public void onDownloadProgress(String buildingId, float progress) {
            // TODO Auto-generated method stub
            IMLog.logd("####### building:" + buildingId + ", progress:" + progress + ", id:" + Thread.currentThread().getId());

        }

    };

    /**
     * 路算回调接口
     */
    private IMRoutePlanningListener mRoutePlanningListener1 = new IMRoutePlanningListener() {
        @Override
        public void onPlanningSuccess(String routePlanningData) {
            // TODO Auto-generated method stub
            IMLog.logd("#######-------- planning success id:" + Thread.currentThread().getId());

            mIndoorMapFragment.refreshMap();

        }

        @Override
        public void onPlanningFailure(RoutePLanningStatus statusCode) {
            // TODO Auto-generated method stub
            IMLog.logd("#######-------- planning failure errorCode:" + statusCode + ", id:" +
                    Thread.currentThread().getId());
        }
    };

    private IMMapEventListener mMapEventListener = new IMMapEventListener() {
        @Override
        public void onMarkerClick(String sourceID) {
            Toast.makeText(mContext, "选中自定义图标："+sourceID , Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onFrameDrawOver() {
            if (mCustomImage!=null&&mFrameDrawOverRunnable!=null)
                mCustomImage.post(mFrameDrawOverRunnable);
        }
        @Override
        public void onFloorChange(int floorNo) {
            //IMLog.logd("#######-------- onFloorChange id:" + Thread.currentThread().getId());
        }

        @Override
        public void onSelectedPoi(String poiId) {

           // IMLog.logd("#######-------- onSelectedShop:" + poiId + " id:" + Thread.currentThread().getId());

        	String name="";
            if (poiId != null && !poiId.equals("")) {
                IMLog.logd("sourceIasdfsadsasad"+poiId);
                if(mLastSelectedPoiId!=null)
                {
                    mIndoorMapFragment.clearFeatureColor(mLastSelectedPoiId);
                }
                mLastSelectedPoiId = poiId;

                mIndoorMapFragment.selectFeature(poiId);//气泡

                mIndoorMapFragment.setFeatureColor(poiId,"0x00000000");//高亮
                mIndoorMapFragment.refreshMap();
                //mIndoorMapFragment.setFeatureCenter(poiId);//居中
                String toasttext="";
                toasttext+="PoiId:" + poiId;
                IMSearchResult searchresult = mDataManager.searchByID(poiId);
                if (searchresult!=null)
                {
                    if (searchresult.getName()!=null) {
                        toasttext+="\n"+"PoiName:" + searchresult.getName();
                        name= searchresult.getName();
                    }
                    if (searchresult.getCatagory()!=null) {
                        toasttext+="\n"+"基本分类:" + searchresult.getCatagory();
                        ArrayList<String> cats= new ArrayList<String>();
                        cats.add(searchresult.getCatagory());
                        toasttext+="\n"+"本类型个数:" + mDataManager.searchByCategories(cats,mDataManager.getCurrentFloorNo()).size();
                    }
                }
//                Toast.makeText(mContext, toasttext , Toast.LENGTH_SHORT).show();
//              // 再路算选点界面下,不显示去这里按钮
                if (mPathFragment == null || !mPathFragment.isPoiSelect()) {
                	bottomviewdetail.setVisibility(View.VISIBLE);
                	text_poi.setText(name);
                	showTips(searchresult.getCatagory());
//                	text_poi_detail = (TextView) findViewById(R.id.text_poi_detail);
//                	btn_gohere = (Button) findViewById(R.id.btn_gohere);
//                    mBottomViewDetail.setVisibility(View.VISIBLE);
//                    mTextPoi.setText(poiId);
                }

            } else {
                // 取消选择
//                mIndoorMapFragment.clearSelected();
//                mIndoorMapFragment.clearHighlight();
                //mLastSelectedPoiId = "";
            }


        }

        @Override
        public void onSingleTap(double lng, double lat) {
            IMLog.logd("#######-------- onSingleTap lng:" + lng + ", lat:" + lat
                    + " id:" + Thread.currentThread().getId());
            IMPoint cvtPoint = mIndoorMapFragment.convertCoordinateToScreen(lng, lat);
            IMLog.logd("#######-------- onSingleTap posX:" + cvtPoint.getX() + ", poxY:" + cvtPoint.getY()
                    + " id:" + Thread.currentThread().getId());
        }

        @Override
        public void onDoubleTap() {
            //IMLog.logd("#######-------- onDoubleTap id:" + Thread.currentThread().getId());
            mIndoorMapFragment.zoomIn();
        }

        @Override
        public void onLongPress() {
            //IMLog.logd("#######-------- onLongPress id:" + Thread.currentThread().getId());
        }

        @Override
        public void onInclineBegin() {

        }

        @Override
        public void onIncline(float centerX, float centerY, float shoveAngle) {
            
        }

        @Override
        public void onInclineEnd() {

        }

        @Override
        public void onScaleBegin() {
            //IMLog.logd("#######-------- onScaleBegin id:" + Thread.currentThread().getId());
        }

        @Override
        public void onScale(float focusX, float focusY, float scaleValue) {
//            IMLog.logd("#######-------- onRotate x:" + focusX + ", y:" + focusY + ", value:"
//                    + scaleValue);
        }

        @Override
        public void onScaleEnd() {
            //IMLog.logd("#######-------- onScaleEnd id:" + Thread.currentThread().getId());
        }

        @Override
        public void onTranslateBegin() {
            //IMLog.logd("#######-------- onTranslateBegin id:" + Thread.currentThread().getId());
        }

        @Override
        public void onTranslate(float transX, float transY) {
//            IMLog.logd("#######-------- onTranslate x:" + transX + ", y:" + transY);
        	bottomviewdetail.setVisibility(View.GONE);
        }

        @Override
        public void onTranslateEnd() {
            //IMLog.logd("#######-------- onTranslateEnd id:" + Thread.currentThread().getId());
        }

        @Override
        public void onRotateBegin() {
            //IMLog.logd("#######-------- onRotateBegin id:" + Thread.currentThread().getId());
        }

        @Override
        public void onRotate(float centerX, float centerY, float rotateAngle) {
//            IMLog.logd("#######-------- onRotate x:" + centerX + ", y:" + centerY + ", value:"
//                    + rotateAngle);
            float d=mIndoorMapFragment.getMapRotation();
            //指南针旋转
            ObjectAnimator.ofFloat(iv_compass_icon, "rotation",-d).setDuration(100).start();//360度旋转
        }

        @Override
        public void onRotateEnd() {
            //IMLog.logd("#######-------- onRotateEnd id:" + Thread.currentThread().getId());
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    public Context getContext() {
        return mContext;
    }

 // 下方展示要去的位置
 	public void showTips(String integer) {
// 		Log.e("SSSSSSSSSSSSSSSSSSSS", poiCell.getType() + "");
 		//要去的地方,图片的展示
 		if (toiletList.contains(integer)) {
 			iv_destination_icon.setBackground(getApplicationContext().getResources()
 					.getDrawable(R.drawable.guide_totail));
 		} else if (stairsList.contains(integer)) {
 			iv_destination_icon.setBackground(getApplicationContext().getResources()
 					.getDrawable(R.drawable.guide_elevator));
 		} else if (consultingList.contains(integer)) {
 			iv_destination_icon.setBackground(getApplicationContext().getResources()
 					.getDrawable(R.drawable.guide_consulting));
 		} else if (exitList.contains(integer)) {
 			iv_destination_icon.setBackground(getApplicationContext().getResources()
 					.getDrawable(R.drawable.guide_exit));
 		} else if("05".equals(integer.substring(0,2))||"06".equals(integer.substring(0,2))){
 			iv_destination_icon.setBackground(getApplicationContext().getResources()
 					.getDrawable(R.drawable.guide_shopp));
 		}else{
 			iv_destination_icon.setBackground(getApplicationContext().getResources()
 					.getDrawable(R.drawable.guide_other));
 		}

 		
// 		text_poi_detail.setText(""
// 				+ getMapInterface().getFloorNameCode(poiCell.getFloorNo()));
 		
// 		showTipsAll();
 	}
    
    
    
    /**
	 * 搜索服务
	 * type表示对不同目标的搜索
	 */
	private void searchService(int type) {
		int typeIcon=1;
		int floorId = mDataManager.getCurrentFloorNo();
		List<IMSearchResult> resultList = new ArrayList<IMSearchResult>();
		switch (type) {
		case 1:
//			for (Integer nType : toiletList) {
				resultList.addAll(mDataManager.searchByType("AM1004", floorId));
				typeIcon=1;
//			}
			break;
		case 2:
//			for (Integer nType : exitList) {
				resultList.addAll(mDataManager.searchByType("AM1012", floorId));
				typeIcon=2;
//			}
			break;
		case 3:
//			for (Integer nType : stairsList) {
				resultList.addAll(mDataManager.searchByType("AM1011", floorId));
				typeIcon=3;
//			}
			break;
		case 4:
//			for (Integer nType : consultingList) {
				resultList.addAll(mDataManager.searchByType("AM1002", floorId));
				resultList.addAll(mDataManager.searchByType("AM0018", floorId));
				resultList.addAll(mDataManager.searchByCategories(consultingList, mDataManager.getCurrentFloorNo()));
				
				typeIcon=4;
//			}
			break;
		case 5:
			for (String nType : shoppList) {
				resultList.addAll(mDataManager.searchByType(nType, floorId));
			}
			typeIcon=5;
			break;
			
			

		default:
			break;
		}

//		// 其他楼层的寻找
//		if (resultList.size() == 0) {
//			List<FloorInfo> list = getMapInterface().getFloorList();
//			for (int i = 0; i < list.size(); i++) {
//				floorId = list.get(i).fl_index;
//				switch (type) {
//				case 1:
//					for (Integer nType : toiletList) {
//						resultList.addAll(searchmanager.searchType(nType,
//								floorId));
//					}
//					break;
//				case 2:
//					for (Integer nType : exitList) {
//						resultList.addAll(searchmanager.searchType(nType,
//								floorId));
//					}
//					break;
//				case 3:
//					for (Integer nType : stairsList) {
//						resultList.addAll(searchmanager.searchType(nType,
//								floorId));
//					}
//					break;
//				case 4:
//					for (Integer nType : consultingList) {
//						resultList.addAll(searchmanager.searchType(nType,
//								floorId));
//					}
//					break;
//				case 5:
//					for (Integer nType : shoppList) {
//						resultList.addAll(searchmanager.searchType(nType,
//								floorId));
//					}
//					break;
//
//				default:
//					break;
//				}
//
//				if (resultList.size() > 0) {
//					break;
//				}
//			}
//		}
	    if (resultList.size() == 0) {
            Toast.makeText(getApplicationContext(), "本楼层没有该建筑物，请切换楼层", Toast.LENGTH_SHORT).show();
//            mMapFragment.clearSearchResult();
        }

		if (resultList.size() > 0) {
//			IndoorE indoorE = resultList.get(0);
//			getMapInterface().loadMapFloor(floorId);
//			getMapInterface().setViewPortToLocation(indoorE.nCenterX,
//					indoorE.nCenterY);
//			// 清除所有标记
//			getMapInterface().clearMarkers();
			List<String> featureList = new ArrayList<String>();
			  for (IMSearchResult sr: resultList) {
                  if (sr.getFloorNo() == mDataManager.getCurrentFloorNo()) {
                      featureList.add(sr.getId());
                  }
              }
//			  typeIcon=1;
			  //设置图标
//		    Bitmap bitmap= IMUtils.makeImageStream("KFC.png",mContext);
//		    
//		    for(int i=0;i<featureList.size();i++){
//		    	Log.e("SSSSSSSSSSS", featureList.get(i));
//		    	 mIndoorMapFragment.setIconByID(bitmap,featureList.get(i));
//		    }
//		    mIndoorMapFragment.refreshMap();
       
			mIndoorMapFragment.selectSearchResultList(featureList);
			mIndoorMapFragment.setFeatureCenter(featureList);
			
		     
		}
//		for (IndoorE indoorE : resultList) {
//			if (indoorE != null) {
//				PoiMapCell poiCell = new PoiMapCell(indoorE.nTypeCode,
//						indoorE.nCenterX, indoorE.nCenterY, indoorE.strName_dp);
//				poiCell.setClickable(true);
//				poiCell.setGravity(PoiMapCell.BOTTOM_CENTER);
//				switch (type) {
//				case 1:
//					poiCell.setResId(R.drawable.indoor_bubble_toilet);
//					break;
//				case 2:
//					poiCell.setResId(R.drawable.exit_icon);
//					break;
//				case 3:
//					poiCell.setResId(R.drawable.elevator_icon);
//					break;
//				case 4:
//					poiCell.setResId(R.drawable.consulting_icon);
//					break;
//				case 5:
//					poiCell.setResId(R.drawable.shopp_icon);
//					break;
//
//				default:
//					break;
//				}
//				int result = getMapInterface().addMarker(poiCell);
//				Log.v("wmh", "addMarker result=" + result);
//			}
//		}
	}
	


    // ---------------------       定位SDK调用       -----------------------------
    boolean mIsManagerInited = false;
    boolean mIsLocating = false;
    SDKInitHandler mSDKInitHandler = new SDKInitHandler(this);;
    com.autonavi.indoor.constant.Configuration.Builder mConfigBuilder = null;
    private final InnerHandler mInnerHandler = new InnerHandler(this);
    public int mLastLocatedFloorNO = -99999;
    private ILocationManager mLocationManager = null;
    //ILocationManager mLocationManager;
    private static long mLastLocationTime = 0;
    private static int mLocationIntervalTime = 1000;
    private static boolean mLocationIntervalFlag = true;
    private boolean mFirstCenter = false;                       // 第一次定位居中
    //指南针
	private ImageView iv_compass_icon;
	private RelativeLayout rl_parent;
	private LinearLayout bottomviewdetail;
	private TextView text_poi;
//	private TextView text_poi_detail;
	private Button btn_gohere;
	//到这去的图标
	private ImageView iv_destination_icon;
	//到哪去的点
	private PoiInfo mInfoTo;

    public void initLocating(Configuration.LocationProvider provider) {
        IMLog.logd("#######init " + provider);

        //VERSION 5.5
        mConfigBuilder = new Configuration.Builder(mContext);
        String key = "1a2df7edf2823bec2c07fe319360c9ca";
//        try {
//            ApplicationInfo appInfo = mContext.getPackageManager()
//                    .getApplicationInfo(mContext.getPackageName(),
//                            PackageManager.GET_META_DATA);
//
//            key = appInfo.metaData.getString("indoormap3d_key");
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(this, "定位Lbs Key错误", Toast.LENGTH_SHORT).show();
//        }

        mConfigBuilder.setLBSParam(key);
        //mConfigBuilder.setSqlitePath(Environment.getExternalStorageDirectory() + "/autonavi/indoor/indoor_db.db");

//        //离线定位
//        mLocationManager = com.autonavi.indoor.location.LocationManager.getInstance();			//混合定位
//        //mConfigBuilder.setServer(Configuration.ServerType.SERVER_AOS, "");
//        mConfigBuilder.setLocationMode(Configuration.LocationMode.OFFLINE);

        //在线定位
		mLocationManager = OnlineLocator.getInstance();				//在线定位

		//mConfigBuilder.setServer(Configuration.ServerType.SERVER_AOS, "");
		//mConfigBuilder.setLocationMode(Configuration.LocationMode.AUTO);

        mSDKInitHandler = new SDKInitHandler(this);
        mConfigBuilder.setLocationProvider(provider);		//Configuration.LocationProvider.WIFI

        mLocationManager.init("",
                mConfigBuilder.build(), mSDKInitHandler);

//        mLocationManager.init(mIndoorMapFragment.getCurrentBuildingId(),
//                mConfigBuilder.build(), mSDKInitHandler);

    }


    private void destroyLocating(){
        IMLog.logd("#######destroy");
        if (mLocationManager != null) {
            IMLog.logd("#######destroy in");
            mLocationManager.destroy();
            mIsManagerInited = false;
            mLocationManager = null;
        }
    }

    void startLocating(){
        IMLog.logd("#######start");
        if (mIsManagerInited && !mLocationStatus && mLocationManager != null) {
            IMLog.logd("#######start in");

            //try {
            mLocationManager.requestLocationUpdates(mInnerHandler);
            //} catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            //	e.printStackTrace();
            //}

            //mIsLocating = true;
        }
    }
    void stopLocating(){
        IMLog.logd("#######stop");
        if (mIsManagerInited && mLocationManager != null) {
            IMLog.logd("#######stop in");
            mLocationManager.removeUpdates(mInnerHandler);
            mIsLocating = false;
        }
    }
    private class FrameDrawOverRunnable implements Runnable {
        @Override
        public void run() {
            if(mCustomImage!=null&&mIndoorMapFragment!=null) {
                IMPoint pt=mIndoorMapFragment.convertCoordinateToScreen(120.10769851677900,30.299475678357201);
                mCustomImage.layout((int) pt.getX()-mCustomImage.getWidth()/2,
                        (int)pt.getY()-mCustomImage.getHeight()/2 ,
                        (int) pt.getX()+mCustomImage.getWidth()/2,
                        (int)pt.getY()+mCustomImage.getHeight()/2);
            }
        }

    }


    private static class SDKInitHandler extends Handler {
        private final WeakReference<IndoorGaodeGuide> mParent;
        public SDKInitHandler(IndoorGaodeGuide parent) {
            mParent = new WeakReference<IndoorGaodeGuide>(parent);
        }

        @Override
        public void handleMessage(Message msg) {
            final IndoorGaodeGuide parent = mParent.get();
            if (parent == null ) {
                IMLog.logd("#######parent == null");
                return;
            }
//			//parent.mIsLocating = false;
//			L.d("mIsLocating = false");
            if (msg.what == MessageCode.MSG_THREAD_PREPARED){
                IMLog.logd("#######Initialize LocationManager with Configuration");
                if (parent.mLocationManager == null) {
                    return;
                }

                parent.mIsManagerInited = true;
                //parent.mLocationManager.requestLocationUpdates(parent.mInnerHandler);
                parent.mIsLocating = true;
                //parent.start();
                IMLog.logd("#######mIsLocating = true");
            }
            else if (msg.what == MessageCode.MSG_WIFI_NOT_ENABLED ){
                Toast.makeText(parent.getContext(), "请先打开wifi", Toast.LENGTH_SHORT).show();
            }else if (msg.what == MessageCode.MSG_WIFI_NOT_PERMITTED ){
                Toast.makeText(parent.getContext(), "wifi没有授权", Toast.LENGTH_SHORT).show();
            }else if (msg.what == MessageCode.MSG_BLE_NOT_PERMITTED ){
                Toast.makeText(parent.getContext(), "BLE没有授权", Toast.LENGTH_SHORT).show();
            } else if (msg.what == MessageCode.MSG_BLE_NOT_ENABLED ){
                Toast.makeText(parent.getContext(), "请先打开BLE", Toast.LENGTH_SHORT).show();
            }else if (msg.what == MessageCode.MSG_SENSOR_MISSING ){
                Toast.makeText(parent.getContext(), "手机缺少步导需要的传感器：加速度、磁力计、重力计等", Toast.LENGTH_SHORT).show();
            }else if (msg.what == MessageCode.MSG_NETWORK_ERROR ){
                Toast.makeText(parent.getContext(), "网络错误", Toast.LENGTH_SHORT).show();
            }else if (msg.what == MessageCode.MSG_NETWORK_NOT_SATISFY){
                Toast.makeText(parent.getContext(), "当前网络和用户设置的不符，不能下载数据", Toast.LENGTH_SHORT).show();
            }else if (msg.what == MessageCode.MSG_SERVER_ERROR){
                Toast.makeText(parent.getContext(), "服务器端错误", Toast.LENGTH_SHORT).show();
            } else {
                IMLog.logd("#######error!");
            }
        }
    };

    private static class InnerHandler extends Handler{
        private final WeakReference<IndoorGaodeGuide> mParent;
        public InnerHandler(IndoorGaodeGuide parent) {
            mParent = new WeakReference<IndoorGaodeGuide>(parent);
        }
        @Override
        public void handleMessage(Message msg)
        {
            IndoorGaodeGuide mParent = this.mParent.get();
            if (mParent == null) {
                IMLog.logd("#######2.00 parent is NULL");
                return;
            }

            switch (msg.what) {
                case -1: {
                    IMLog.logd("#######2.00");
                    break;
                }
                case MessageCode.MSG_REPORT_ONLINE_LOCATION: {
                    onLocated(msg, true);
                    break;
                }
                case MessageCode.MSG_REPORT_LOCATION: {
                    onLocated(msg, false);
                    break;
                }
                case MessageCode.MSG_SENSOR_MISSING: {
                    Toast.makeText(mParent.getContext(), "手机缺少步导需要的传感器：加速度、磁力计、重力计等", Toast.LENGTH_SHORT).show();
                    break;
                }
                case MessageCode.MSG_BLE_NO_SCAN: {
                    Toast.makeText(mParent.getContext(), "一段时间内没有蓝牙扫描", Toast.LENGTH_SHORT).show();
                    break;
                }
                case MessageCode.MSG_WIFI_NO_SCAN: {
                    Toast.makeText(mParent.getContext(), "一段时间内没有WIFI扫描", Toast.LENGTH_SHORT).show();
                    break;
                }
                case MessageCode.MSG_NETWORK_ERROR: {
                    Toast.makeText(mParent.getContext(), "网络错误", Toast.LENGTH_SHORT).show();
                    break;
                }
                case MessageCode.MSG_NETWORK_NOT_SATISFY: {
                    Toast.makeText(mParent.getContext(), "当前网络和用户设置的不符，不能下载数据", Toast.LENGTH_SHORT).show();
                    break;
                }
                case MessageCode.MSG_SERVER_ERROR: {
                    Toast.makeText(mParent.getContext(), "服务器端错误", Toast.LENGTH_SHORT).show();
                    break;
                }case MessageCode.MSG_PRESSURE_CHANGED: {
                    //Toast.makeText(mParent.getContext(), "气压值改变异常", Toast.LENGTH_SHORT).show();
                    break;
                }
                case MessageCode.MSG_REPORT_PED: {
                    break;
                }
                default:
                {
                    break;
                }
            }
        }
        void onLocated(Message msg, boolean isOnline){
            IMLog.logd("onLocated");
            IndoorGaodeGuide parent = mParent.get();
            if (parent == null)
                return;
            LocationResult result = (LocationResult)msg.obj;
            IMLog.logd("#######客户端收到定位结果：isOnline="+ isOnline + ", ("+result.x + ", " +result.y +","+ result.z +"),"+result.a);
            if (result.x == 0 && result.y == 0 && result.z == -99){
                IMLog.logd("#######无法定位 -99");
                return ;
            }
            if (result.x == -10000 || result.y == -10000 ){
                IMLog.logd("#######无法定位 -1000");
                return ;
            }
            if (result.x == 0 && result.y == 0 && result.z == -127){
                IMLog.logd("#######无法定位  -127");
                return ;
            }

            long curTime = System.currentTimeMillis();

            // 跳点模式
//            if (mLocationIntervalFlag && curTime - mLastLocationTime < mLocationIntervalTime) {
//                return;
//            }

            mLastLocationTime = curTime;

            IMLog.logd("#######lng:" + result.x + ", lat:"
                    + result.y + ", floor:"
                    + result.z + ", angel:"
                    + result.a + ", accuracy:"
                    + result.r);

            if (!result.bid.equals(parent.mIndoorMapFragment.getCurrentBuildingId())) {
                Toast.makeText(parent.getContext(), "定位结果不在当前建筑物内!", Toast.LENGTH_SHORT).show();
                return;
            }

            // 显示定位点
            parent.mIndoorMapFragment.setLocatingPosition(result.x, result.y, result.z,
                    result.a, result.r);

            if (parent.mIndoorMapFragment.getCurrentFloorNo() != result.z) {
                parent.mIndoorMapFragment.switchFloorByFloorNo(result.z);
            }

            if (!parent.mFirstCenter) {
                parent.mIndoorMapFragment.setCoordinateCenter(result.x, result.y, (int) result.z);
                parent.mIndoorMapFragment.setCoordinateDirect(result.a);
                parent.mIndoorMapFragment.setMapIncline(-45);
                //parent.mFirstCenter = true;
            }

            // 搜索定位周围的点
//            List<IMSearchResult> searchResultList = IMDataManager
//                    .getInstance().searchByDistance(result.x, result.y, result.z, 100, 20);
//
//            List<String> featureList = new ArrayList<String>();
//            for (IMSearchResult sr: searchResultList) {
//                    featureList.add(sr.getId());
//            }
//            parent.mIndoorMapFragment.selectSearchResultList(featureList);

            // 记录定位点
            IMPoint tmpPoint = new IMPoint(result.x, result.y, result.z);
            parent.mLastPoint = tmpPoint;
            
            mLocationPoint = tmpPoint;
            mLocationBdId=result.bid;
//            parent.mPathFragment.setLocationPoint(tmpPoint);
//            parent.mPathFragment.setLocationBdId(result.bid);

            // 切层
            parent.mIndoorMapFragment.showLocationOnFloorView(result.z);

        }
    };
    
    /**
	 * 加载指定的位置
	 */
	private PoiInfo loadMylocation() {
		String curBdId = mIndoorMapFragment.getCurrentBuildingId();
		if (mLocationPoint == null || mLocationBdId == null || !mLocationBdId.equals(curBdId)) {
//			new AlertDialog.Builder(mIndoorMapFragment.getActivity())
//					.setTitle("提示")
//					.setMessage("没有定位结果,无法使用定位位置!")
//					.setIcon(
//							android.R.drawable.ic_dialog_alert)
//					.setPositiveButton("确定", null).show();
			hintDialog.show();
			return null;
		}

		PoiInfo info = new PoiInfo();
		info.PoiInfoType = Constant.TYPE_ROUTE_PLANNING_LOCATION;
		String namecode="F1";

		info.cell = new PoiMapCell(0 ,mLocationPoint.getX(), mLocationPoint.getY(),
				mLocationPoint.getZ(), "我的位置");
		info.floor = new IMFloorInfo(mLocationPoint.getZ(), namecode, "-1");
		return info;
	}

	private static IMPoint mLocationPoint = null;
	private static String mLocationBdId = "";
	private ImageView mBack;
	private TextView mtitle;
	private RelativeLayout top;
	private EditText tv_seat_info;
	private TextView tv_search;
	private RelativeLayout rl_compass;


}
