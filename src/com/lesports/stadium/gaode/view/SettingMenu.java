/**
 * 
 */
package com.lesports.stadium.gaode.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.im.listener.IMMapLoadListener;
import com.amap.api.im.util.IMFloorInfo;
import com.amap.api.im.overlay.LonLat;
import com.amap.api.im.overlay.Marker;
import com.amap.api.im.overlay.PolyLine;
import com.amap.api.im.overlay.Polygon;
import com.amap.api.im.util.IMLog;
import com.amap.api.im.util.IMUtils;
import com.amap.api.im.view.IMIndoorMapFragment;
import com.lesports.stadium.gaode.IndoorGaodeGuide;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qingyangli
 *
 */
public class SettingMenu {

	private enum TestInterfaceEnmu {
		CLEAR_ALL_DATA,
		CLEAR_HILIGHT,
		CLEAR_LOCATION_POS,
		CLEAR_ROUTE_RESULT,
		CLEAR_ROUTE_START,
		CLEAR_ROUTE_STOP,
		CLEAR_SEARCH_RESULT,
		CLEAR_SELECTED,

		GET_BUILDING_ID,
		GET_FLOOR_INFO_LIST,
		GET_FLOOR_NO,
		GET_SELECTED_SOURCE_ID,
		GET_ICON_DIR,
		GET_STYLE_DIR,
		GET_MAP_ROTATION,
		GET_MAP_INCLINE,
		GET_SCALE_LEN,
		GET_SCALR_NUM,
		GET_SCALE_UNIT,

		INPUT_MAP_ROTATION,

		AMAP_LOGO,
		COMPASS_VIEW,
		FLOOR_VIEW,
		SCALE_VIEW,
		ZOOM_VIEW,

		SHOVE,
		MOVE,
		ROTATE,
		SCALE,
		ALL_GESTURE,
		CUSTOM_SHAPE,
        CUSTOM_POINT,
        CUSTOM_DELETE,
        CUSTOM_LINE,
        CUSTOM_SURFACE,
		ID_COLOR,
        ID_RESTORE_COLOR,
		ID_ICON,
		RESET_MAP,
		RESET_COMPASS,
		RESET_MAP_INCLINE,
		RESET_MAP_MOVE,
		RESET_MAP_ROTATE,
		RESET_MAP_SCALE,
		RESET_SCALE,

		SET_DATA_PATH,
		SET_FEATURE_CENTER,
		SET_FEATURE_HILIGHT,
		SET_GESTURE_ENABLE,
		SET_LOC_VIEW_ENABLE,
		SET_SHOVE_ENABLE,
		SET_ROUTE_START,
		SET_ROUTE_STOP,
		SET_SCALE_UNIT,

	};


	public SettingMenu(Context context) {
		this.mContext = context;
		mBuildingMenu = new AlertDialog.Builder(context);
		mInterfaceMenu = new AlertDialog.Builder(context);
		initSettingMenu();
	}

	private static final String TAG = "SettingMenu";

	LinearLayout mLinear;
	List<String> mMenuStringList;

	private Context mContext = null;
	
	private AlertDialog.Builder mBuildingMenu = null;

	private AlertDialog.Builder mInterfaceMenu = null;

	private IMIndoorMapFragment mIndoorMapFragment = null;

	private IndoorGaodeGuide mMainActivity = null;

	private IMMapLoadListener mMapLoadListener = null;

	//private Map

	// 测试列表类
	private List<EnableInfo> mTestInterfaceList = new ArrayList<EnableInfo>();

	//修改列表
	private List<String> mBuildingList = new ArrayList<String>();
	
	private static final int SWITCH_BUILDING = 0;
	private static final int ROUTE_PLANNING = 1;
	private static final int TEST_INTERFACE = 2;
	//private static final int TEST_RANDOM = 3;
	private static final int ABOUT_INDODRRENDER3D = 3;

	private void initSettingMenu() {

		//init Main menu
		mMenuStringList = new ArrayList<String>();
		mMenuStringList.add("切换建筑物");
		mMenuStringList.add("路径规划");
		mMenuStringList.add("指定接口测试");
		//mMenuStringList.add("随机测试");
		mMenuStringList.add("关于定位SDK Demo");

		String[] stringArray = (String[])mMenuStringList.toArray(new String[0]);

		mBuildingMenu.setItems(stringArray, mBuildingMenuOnClickListener);

		initBuildingMenu();
		initTestInterfaceMenu();

	}

	private void initBuildingMenu() {
        //mBuildingList.add("你的建筑物ID, 你的建筑物描述");
		mBuildingList.add("B023B173VP, 城西银泰");
		mBuildingList.add("B000A6534B, 君太百货");
		mBuildingList.add("B000A8WE3K, 首创奥特莱斯");
		mBuildingList.add("B001B0I64M, 新世界百货(武昌店)");
		mBuildingList.add("B000A8UU5X, 华润五彩城购物中心");
		mBuildingList.add("B023B1D4BX, 阿里西溪园区");
		mBuildingList.add("B0FFFAB6J2, 首开广场");
        mBuildingList.add("B000A83AJN, 北京南站");
	}

	private void initTestInterfaceMenu() {

		mTestInterfaceList.add(new EnableInfo("获得当前建筑物ID", "", TestInterfaceEnmu.GET_BUILDING_ID));
		mTestInterfaceList.add(new EnableInfo("获得楼层信息列表", "", TestInterfaceEnmu.GET_FLOOR_INFO_LIST));
		mTestInterfaceList.add(new EnableInfo("获得当前楼层号", "", TestInterfaceEnmu.GET_FLOOR_NO));
		mTestInterfaceList.add(new EnableInfo("获得已选中的ID", "", TestInterfaceEnmu.GET_SELECTED_SOURCE_ID));
		mTestInterfaceList.add(new EnableInfo("获得旋转角度", "", TestInterfaceEnmu.GET_MAP_ROTATION));
		mTestInterfaceList.add(new EnableInfo("获得倾斜角度", "", TestInterfaceEnmu.GET_MAP_INCLINE));
		mTestInterfaceList.add(new EnableInfo("获得缩放长度", "", TestInterfaceEnmu.GET_SCALE_LEN));
		mTestInterfaceList.add(new EnableInfo("获得缩放显示数字", "", TestInterfaceEnmu.GET_SCALR_NUM));
		mTestInterfaceList.add(new EnableInfo("获得缩放单位", "", TestInterfaceEnmu.GET_SCALE_UNIT));

		mTestInterfaceList.add(new EnableInfo("输入旋转值", "", TestInterfaceEnmu.INPUT_MAP_ROTATION));

		mTestInterfaceList.add(new EnableInfo("Amap LOGO", true, TestInterfaceEnmu.AMAP_LOGO));
		mTestInterfaceList.add(new EnableInfo("显示罗盘", true, TestInterfaceEnmu.COMPASS_VIEW));
		mTestInterfaceList.add(new EnableInfo("显示楼层控件", true, TestInterfaceEnmu.FLOOR_VIEW));
		mTestInterfaceList.add(new EnableInfo("显示标尺控件", true, TestInterfaceEnmu.SCALE_VIEW));
		mTestInterfaceList.add(new EnableInfo("显示缩放控件", true, TestInterfaceEnmu.ZOOM_VIEW));

		mTestInterfaceList.add(new EnableInfo("平推", true, TestInterfaceEnmu.SHOVE));
		mTestInterfaceList.add(new EnableInfo("移动", true, TestInterfaceEnmu.MOVE));
		mTestInterfaceList.add(new EnableInfo("旋转", true, TestInterfaceEnmu.ROTATE));
		mTestInterfaceList.add(new EnableInfo("缩放", true, TestInterfaceEnmu.SCALE));
		mTestInterfaceList.add(new EnableInfo("所有手势", true, TestInterfaceEnmu.ALL_GESTURE));
		mTestInterfaceList.add(new EnableInfo("输入旋转值", "", TestInterfaceEnmu.INPUT_MAP_ROTATION));
        mTestInterfaceList.add(new EnableInfo("多边形(首开一层)", "", TestInterfaceEnmu.CUSTOM_SURFACE));
        mTestInterfaceList.add(new EnableInfo("折线(首开一层)", "", TestInterfaceEnmu.CUSTOM_LINE));
        mTestInterfaceList.add(new EnableInfo("marker点(首开一层)", "", TestInterfaceEnmu.CUSTOM_POINT));
        mTestInterfaceList.add(new EnableInfo("删除所有图形(首开一层)", "", TestInterfaceEnmu.CUSTOM_DELETE));
        mTestInterfaceList.add(new EnableInfo("通过ID设置图标(首开一层KFC)", "", TestInterfaceEnmu.ID_ICON));
		mTestInterfaceList.add(new EnableInfo("通过ID设置颜色(首开一层KFC)", "", TestInterfaceEnmu.ID_COLOR));
        mTestInterfaceList.add(new EnableInfo("清除设置颜色(首开一层KFC)", "", TestInterfaceEnmu.ID_RESTORE_COLOR));
		InterfaceAdapter adapter = new InterfaceAdapter(mContext, mTestInterfaceList);
		mInterfaceMenu.setAdapter(adapter, mInterfaceMenuOnClickListener);

	}
	
	private OnClickListener mBuildingMenuOnClickListener = new OnClickListener() {
		
		@Override
	    public void onClick(DialogInterface view, int pos)
	    {
			
	        // TODO 自动生成的方法存根
	        if (pos == SWITCH_BUILDING) {
	        	showCompileSetting(SWITCH_BUILDING, "切换建筑物");

	        } else if (pos == ROUTE_PLANNING) {
				mMainActivity.btnGoHere();

			} else if (pos == TEST_INTERFACE) {
				mInterfaceMenu.show();
			//} else if (pos == TEST_RANDOM) {

			} else if (pos == ABOUT_INDODRRENDER3D) {
	        	AlertDialog.Builder builder2=new AlertDialog.Builder(mContext);
	            builder2.setTitle("关于定位SDK Demo");
	            builder2.setMessage("定位SDK Demo \nversion: " + mIndoorMapFragment.getVersion() +
							"\nsub version: " + mIndoorMapFragment.getSubVersion());
	            builder2.setPositiveButton("确定",new OnClickListener(){

	                public void onClick(DialogInterface dialog, int which)
	                {
	                    // TODO 自动生成的方法存根
	                    dialog.dismiss();
	                    
	                }
	            });
	            builder2.show();
	        }
	        
	        view.dismiss();
	    }
	};


	private OnClickListener mInterfaceMenuOnClickListener = new OnClickListener() {

		@Override
		public void onClick(DialogInterface view, int pos)
		{
			EnableInfo enableInfo = mTestInterfaceList.get(pos);

			// TODO 自动生成的方法存根
			if (enableInfo.type == TestInterfaceEnmu.SHOVE) {
				mIndoorMapFragment.setMapInclineEnable(enableInfo.getReverseEnable());
				enableInfo.makeToast();

			}
            else if (enableInfo.type == TestInterfaceEnmu.MOVE) {
				mIndoorMapFragment.setMapTranslateEnable(enableInfo.getReverseEnable());
				enableInfo.makeToast();
			} else if (enableInfo.type == TestInterfaceEnmu.ROTATE) {
				mIndoorMapFragment.setMapRotateEnable(enableInfo.getReverseEnable());
				enableInfo.makeToast();
			} else if (enableInfo.type == TestInterfaceEnmu.SCALE) {
				mIndoorMapFragment.setMapScaleEnable(enableInfo.getReverseEnable());
				enableInfo.makeToast();
			} else if (enableInfo.type == TestInterfaceEnmu.ALL_GESTURE) {
				mIndoorMapFragment.setGestureEnable(enableInfo.getReverseEnable());
				enableInfo.makeToast();
			}
            else if (enableInfo.type == TestInterfaceEnmu.AMAP_LOGO) {
				boolean isShow = enableInfo.getReverseEnable();
				if (isShow) {
					mIndoorMapFragment.showAmapLogo();
				} else {
					mIndoorMapFragment.hideAmapLogo();
				}
				enableInfo.makeToast();
			} else if (enableInfo.type == TestInterfaceEnmu.COMPASS_VIEW) {
				boolean isShow = enableInfo.getReverseEnable();
				if (isShow) {
					mIndoorMapFragment.showCompassView();
				} else {
					mIndoorMapFragment.hideCompassView();
				}
				enableInfo.makeToast();
			} else if (enableInfo.type == TestInterfaceEnmu.FLOOR_VIEW) {
				boolean isShow = enableInfo.getReverseEnable();
				if (isShow) {
					mIndoorMapFragment.showFloorView();
				} else {
					mIndoorMapFragment.hideFloorView();
				}
				enableInfo.makeToast();
			} else if (enableInfo.type == TestInterfaceEnmu.SCALE_VIEW) {
				boolean isShow = enableInfo.getReverseEnable();
				if (isShow) {
					mIndoorMapFragment.showPlottingScale();
				} else {
					mIndoorMapFragment.hidePlottingScale();
				}
				enableInfo.makeToast();
			} else if (enableInfo.type == TestInterfaceEnmu.ZOOM_VIEW) {
				boolean isShow = enableInfo.getReverseEnable();
				if (isShow) {
					mIndoorMapFragment.showZoomView();
				} else {
					mIndoorMapFragment.hideZoomView();
				}
				enableInfo.makeToast();

			} else if (enableInfo.type == TestInterfaceEnmu.GET_BUILDING_ID) {
				Toast.makeText(mIndoorMapFragment.getActivity(), "建筑物ID:" +
						mIndoorMapFragment.getCurrentBuildingId(),
						Toast.LENGTH_SHORT).show();
			} else if (enableInfo.type == TestInterfaceEnmu.GET_FLOOR_INFO_LIST) {
				List<IMFloorInfo> floorList = mIndoorMapFragment.getCurrentFloorInfoList();
				String showStr = "";
				for (IMFloorInfo tmpInfo: floorList) {
					showStr += tmpInfo.getFloorNo() + ", ";
					showStr += tmpInfo.getFloorNona() + ", ";
					showStr += tmpInfo.getFloorName() + "\n";
				}

				Toast.makeText(mIndoorMapFragment.getActivity(), showStr,
						Toast.LENGTH_LONG).show();
			} else if (enableInfo.type == TestInterfaceEnmu.GET_FLOOR_NO) {
				Toast.makeText(mIndoorMapFragment.getActivity(), "floor no:" + mIndoorMapFragment.getCurrentFloorNo(),
						Toast.LENGTH_SHORT).show();
			} else if (enableInfo.type == TestInterfaceEnmu.GET_SELECTED_SOURCE_ID) {
				Toast.makeText(mIndoorMapFragment.getActivity(), "选中SourceID:" +
						mIndoorMapFragment.getCurrentSelectSourceId(),
						Toast.LENGTH_SHORT).show();
			} else if (enableInfo.type == TestInterfaceEnmu.GET_MAP_ROTATION) {
				Toast.makeText(mIndoorMapFragment.getActivity(), "旋转角度:" +
								mIndoorMapFragment.getMapRotation(),
						Toast.LENGTH_SHORT).show();
			} else if (enableInfo.type == TestInterfaceEnmu.GET_MAP_INCLINE) {
				Toast.makeText(mIndoorMapFragment.getActivity(), "倾斜角度:" +
								mIndoorMapFragment.getMapIncline(),
						Toast.LENGTH_SHORT).show();
			} else if (enableInfo.type == TestInterfaceEnmu.GET_SCALE_LEN) {
				Toast.makeText(mIndoorMapFragment.getActivity(), "标尺长度单元:" +
								mIndoorMapFragment.getPlottingScaleLength(),
						Toast.LENGTH_SHORT).show();
			} else if (enableInfo.type == TestInterfaceEnmu.GET_SCALR_NUM) {
				Toast.makeText(mIndoorMapFragment.getActivity(), "标尺显示数字:" +
								mIndoorMapFragment.getPlottingScaleNumber(),
						Toast.LENGTH_SHORT).show();
			} else if (enableInfo.type == TestInterfaceEnmu.GET_SCALE_UNIT) {
				Toast.makeText(mIndoorMapFragment.getActivity(), "标尺显示单位:" +
								mIndoorMapFragment.getPlottingScaleUnit(),
						Toast.LENGTH_SHORT).show();
			} else if (enableInfo.type == TestInterfaceEnmu.INPUT_MAP_ROTATION) {
				final EditText et = new EditText(mIndoorMapFragment.getActivity());
				new AlertDialog.Builder(mIndoorMapFragment.getActivity()).setTitle("旋转值测试")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(et)
					.setPositiveButton("确定", new OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							String input = et.getText().toString();
							if (input.equals("")) {
								Toast.makeText(mIndoorMapFragment.getActivity(), "旋转值不能为空！"
										+ input, Toast.LENGTH_LONG).show();
							} else {
								try {
									final float rotate = Float.parseFloat(input);
									mIndoorMapFragment.setMapRotate(rotate);
								} catch (NumberFormatException e) {
									e.printStackTrace();
								}
							}
						}
					})
					.setNegativeButton("取消", null)
					.show();
				//} else if (enableInfo.type == TestInterfaceEnmu.SCALE) {
			}
            else if(enableInfo.type == TestInterfaceEnmu.CUSTOM_LINE)
            {
                ArrayList<LonLat> lonlat =new ArrayList<LonLat>();
                lonlat.add(new LonLat(116.473007,39.993152));
                lonlat.add(new LonLat(116.473416,39.992909));
                lonlat.add(new LonLat(116.473703,39.99315));
                lonlat.add(new LonLat(116.473396,39.993177));
                PolyLine polyline=new PolyLine("polyline0");
                polyline.setPosition(lonlat).setLineColor("0xff0000ff").setLineWidth(4.1f).setFloorIndex(1);
                mIndoorMapFragment.addPolyline(polyline);
                mIndoorMapFragment.refreshMap();
            }
            else if(enableInfo.type == TestInterfaceEnmu.CUSTOM_SURFACE)
			{
                ArrayList<LonLat> lonlat =new ArrayList<LonLat>();
                lonlat.add(new LonLat(116.47276,39.993572));
                lonlat.add(new LonLat(116.47328,39.993607));
                lonlat.add(new LonLat(116.47300,39.993620));
                Polygon polygon=new Polygon("polygon0");
                polygon.setPosition(lonlat).setBorderColor("0xff0000ff").setBorderWidth(2.1f).
                        setFloorIndex(1).setFillColor("0xff00ff00");
                mIndoorMapFragment.addPolygon(polygon);
                mIndoorMapFragment.refreshMap();
			}
            else if(enableInfo.type == TestInterfaceEnmu.CUSTOM_POINT)
            {
                ArrayList<LonLat> lonlat =new ArrayList<LonLat>();
                lonlat.add(new LonLat(116.47276,39.993572));
                lonlat.add(new LonLat(116.47328,39.993607));
                lonlat.add(new LonLat(116.47300,39.993620));
                Bitmap bitmap= IMUtils.makeImageStream("custompoint.png",mContext);
                Marker marker0= new Marker("point0");
                Marker marker1= new Marker("point1");
                Marker marker2= new Marker("point2");
                marker0.setIcon(bitmap,"pointimage").setAnchor(bitmap.getWidth()/2,bitmap.getHeight()).setPosition(lonlat.get(0)).setFloorIndex(1);
                marker1.setIcon(bitmap,"pointimage").setAnchor(bitmap.getWidth()/2,bitmap.getHeight()).setPosition(lonlat.get(1)).setFloorIndex(1);
                marker2.setIcon(bitmap,"pointimage").setAnchor(bitmap.getWidth()/2,bitmap.getHeight()).setPosition(lonlat.get(2)).setFloorIndex(1);
                mIndoorMapFragment.addMarker(marker0);
                mIndoorMapFragment.addMarker(marker1);
                mIndoorMapFragment.addMarker(marker2);
                mIndoorMapFragment.refreshMap();
            }
            else if(enableInfo.type == TestInterfaceEnmu.CUSTOM_DELETE)
            {
				mIndoorMapFragment.deleteShapeByID("point0");
				mIndoorMapFragment.deleteShapeByID("point1");
				mIndoorMapFragment.deleteShapeByID("point2");
				mIndoorMapFragment.deleteShapeByID("polygon0");
                mIndoorMapFragment.refreshMap();
            }
			else if(enableInfo.type == TestInterfaceEnmu.ID_COLOR)
			{
                ArrayList<String> list=new ArrayList<String>();
                list.add("TY0001910210100004");
                mIndoorMapFragment.setColorByID(list,"0xff00ff00");
                mIndoorMapFragment.refreshMap();
			}
			else if(enableInfo.type == TestInterfaceEnmu.ID_ICON)
			{
                Bitmap bitmap= IMUtils.makeImageStream("KFC.png",mContext);
                mIndoorMapFragment.setIconByID(bitmap,"TY0001910210100004");
                mIndoorMapFragment.refreshMap();
			}
            else if(enableInfo.type == TestInterfaceEnmu.ID_RESTORE_COLOR)
            {
                mIndoorMapFragment.clearFeatureColor("TY0001910210100004");
                mIndoorMapFragment.refreshMap();
            }

			view.dismiss();
		}
	};

	/**
	 * 显示Wifi编译设置
	 */
	private void showCompileSetting(final int compileType, String title)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //builder.setIcon(R.drawable.collapsed);
        builder.setTitle(title);

		String[] stringArray = (String[])mBuildingList.toArray(new String[0]);

		builder.setItems(stringArray, mSwitchFloorOnClickListener);

        builder.show();
	}

	private OnClickListener mSwitchFloorOnClickListener = new OnClickListener() {

		@Override
		public void onClick(DialogInterface view, int pos)
		{

			IMLog.logd("#######-------- onClick :" + mBuildingList.get(pos)
					+ ", id:" + Thread.currentThread().getId());

			String buildingId = mBuildingList.get(pos).split(",")[0];

			boolean isStartLoad = mIndoorMapFragment.loadMap(buildingId, mMapLoadListener);

			if (!isStartLoad) {
				Toast.makeText(mIndoorMapFragment.getActivity(), "已有地图正在加载中,加载失败!",
						Toast.LENGTH_LONG).show();
			}

			view.dismiss();
		}
	};


	
	public AlertDialog show() {
		return mBuildingMenu.show();
	}

	public void setIndoorMapFragment(IMIndoorMapFragment indoorMapFragment) {
		this.mIndoorMapFragment = indoorMapFragment;
	}

	public void setMainActivity(IndoorGaodeGuide mainActivity) {
		this.mMainActivity = mainActivity;
	}

	/**
	 * 字符串后加开启状态
	 * @param oriStr
	 * @param enable
	 * @return
	 */
	private String addEnableText(String oriStr, boolean enable) {
		String enableEndStr;

		if (enable) {
			enableEndStr = "(启用)";
		} else {
			enableEndStr = "(停用)";
		}
		return oriStr + enableEndStr;
	}

	public class InterfaceAdapter extends BaseAdapter
	{
		private Activity context = null;
		public List<Application> list = null;

		private Context mContext;
		private LayoutInflater mInflater;
		private List<EnableInfo> mInterfaceList;

		public InterfaceAdapter(Context context, List<EnableInfo> interfaceList) {
			mContext = context;
			mInterfaceList = interfaceList;
		}

		public List<EnableInfo> getInterfaceList() {
			return mInterfaceList;
		}

		@Override
		public int getCount() {
			return mInterfaceList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				TextView floorNameText = new TextView(mContext);

				convertView = floorNameText;
			}

			TextView mFloorNameText=(TextView)convertView;
			EnableInfo editInfo = mInterfaceList.get(position);
			String showText;
			if (editInfo.value == null) {
				showText = addEnableText(editInfo.text, editInfo.enable);
			} else {
				showText = editInfo.text;
			}

			mFloorNameText.setText(showText);

			return convertView;
		}

	}


	class EnableInfo {

		public EnableInfo() {
		}

		public EnableInfo(String text, boolean enable, TestInterfaceEnmu type) {
			this.text = text;
			this.enable = enable;
			this.type = type;
		}

		public EnableInfo(String text, String value, TestInterfaceEnmu type) {
			this.text = text;
			this.value = value;
			this.type = type;
		}

		public String text;
		public boolean enable;
		public String value = null;
		public boolean input;
		public TestInterfaceEnmu type;

		public boolean getReverseEnable() {
			enable = !enable;
			return enable;
		}

		public void makeToast() {
			Toast.makeText(mIndoorMapFragment.getActivity(), text + ":" + enable,
					Toast.LENGTH_SHORT).show();
		}
	}

	public void setMapLoadListener(IMMapLoadListener mapLoadListener) {
		this.mMapLoadListener = mapLoadListener;
	}
}
