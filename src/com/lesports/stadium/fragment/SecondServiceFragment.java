/**
 * 
 */
package com.lesports.stadium.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.LiveDetialActivity;
import com.lesports.stadium.activity.MainActivity;
import com.lesports.stadium.activity.NoStartActivity;
import com.lesports.stadium.adapter.SecondSenceFragmentListviewAdapter;
import com.lesports.stadium.base.BaseFragment;
import com.lesports.stadium.bean.SenceBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.pullrefreshview.PullToRefreshBase;
import com.lesports.stadium.pullrefreshview.PullToRefreshBase.OnRefreshListener;
import com.lesports.stadium.pullrefreshview.PullToRefreshListView;
import com.lesports.stadium.utils.CommonUtils;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.UIUtils;

/**
 * ***************************************************************
 * 
 * @ClassName: ServiceFragment
 * 
 * @Desc : 二期开发回顾界面dierban
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwc
 * 
 * @data:2016-1-29 上午11:39:29
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class SecondServiceFragment extends BaseFragment implements
		OnClickListener {
	/**
	 * 下拉刷新控件
	 */
	private PullToRefreshListView mPulllistview;
	/**
	 * Listview控件
	 */
	private ListView mListview;
	/**
	 * 列表项数据控件listview的适配器
	 */
	private SecondSenceFragmentListviewAdapter mAdapter;
	/**
	 * 列表项的数据源集合
	 */
	private List<SenceBean> mList = new ArrayList<SenceBean>();
	/**
	 * 上拉加载后要显示的行数
	 */
	private int shouCount;
	/**
	 * 是否是上拉加载，需要将list职位最后一行显示
	 */
	private boolean isShowLast = false;
	/**
	 * 需要传递的活动id的标记
	 */
	private final String ID_TAG = "id";
	/**
	 * 定义第一条活动的开始时间
	 */
	private String mFirdtStartTime;
	/**
	 * 定义最后一条的开始时间
	 */
	private String mLastStartTime;
	/**
	 * 下拉刷新失败
	 */
	private final int FERESH_ERROR = 21;
	/**
	 * 下拉刷新成功
	 */
	private final int FRESH_SUCCESS = 20;
	/**
	 * 加载更多成功
	 */
	private final int ADD_MORE_SUCCESS = 888;
	/**
	 * 加载更多失败
	 */
	private final int ADD_MORE_FILE = 889;
	/**
	 * 第一次获取数据成功
	 */
	private final int LOAD_DAT_SUCCESS = 2;
	/**
	 * 第一次获取数据失败
	 */
	private final int LOAD_DAT_FILURE = 3;
	/**
	 * 处理数据的handler；
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case LOAD_DAT_SUCCESS:
				mPulllistview.onPullDownRefreshComplete();
				analyseDate((String) msg.obj);
				break;
			case LOAD_DAT_FILURE:
				mPulllistview.onPullDownRefreshComplete();
				addDataToListview(null);
				break;
			case ADD_MORE_SUCCESS:
				analyseAddMoreDate((String) msg.obj);
				break;
			case ADD_MORE_FILE:
				MainActivity.instance.setLayoutVisible();
				mPulllistview.onPullUpRefreshComplete();
				break;
			case FRESH_SUCCESS:
				refreshSuccess((String) msg.obj);
				break;
			case FERESH_ERROR:
				MainActivity.instance.setLayoutVisible();
				mPulllistview.onPullDownRefreshComplete();
				break;
			default:
				break;
			}

		}
	};

	@Override
	public View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.second_fragment_service, null);
		mPulllistview = (PullToRefreshListView) view
				.findViewById(R.id.second_server_fragment_listviewssss);
		mListview = mPulllistview.getRefreshableView();
		mPulllistview.setPullLoadEnabled(true);
		mPulllistview.setPullRefreshEnabled(true);
		mPulllistview.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				if (!UIUtils.isAvailable(getActivity())) {
					mPulllistview.onPullDownRefreshComplete();
				} else {
					MainActivity.instance.setLayoutVisible();
					((MainActivity) getActivity()).getCityData();
					// 下拉刷新加载数据
					if (mList == null || mList.size() == 0) {
						userUtilsGetData();
					} else {
						userUtilsGetData_shuaxin();
					}
				}
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				if (!UIUtils.isAvailable(getActivity())) {
					mPulllistview.onPullUpRefreshComplete();
				} else {
					MainActivity.instance.setLayoutVisible();
					// 上拉刷新加载数据
					loadRefreshData_add();
				}
			}
		});
		// 控制底部菜单栏显示和隐藏
		mListview.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					// 开始滑动 隐藏
					if (MainActivity.instance != null) {
						MainActivity.instance.setLayoutGone();
					}
					break;

				case OnScrollListener.SCROLL_STATE_FLING:
					// 滑动中
					break;

				case OnScrollListener.SCROLL_STATE_IDLE:
					// 滑动结束 显示
					MainActivity.instance.setLayoutVisible();
					break;

				default:
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
		initDate();
		return view;
	}

	/**
	 * 下拉刷新数据
	 * 
	 */
	protected void loadRefreshData() {
		isShowLast = false;
		Message message = new Message();
		message.arg1 = 100;
		handler.sendMessageDelayed(message, 1500);

	}

	/**
	 * 上拉加载数据
	 */
	protected void loadRefreshData_add() {
		isShowLast = true;
		if (mList != null && mList.size() > 2)
			shouCount = mList.size() - 2;
		userUtilsGetData_jiazai();
	}

	/**
	 * 获取回顾数据
	 */
	public void initDate() {
		userUtilsGetData();
	}

	/**
	 * 从网络获取回顾数据
	 */
	private void userUtilsGetData() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("status", "2");
		// params.put("venueId", "24");
		params.put("action", "2");
		params.put("starttime", "");
		params.put("rows", "6");
		params.put("cityId", GlobalParams.CITY_ID);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ACTIVITY_DATA, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						if (data != null && data.getNetResultCode() == 0) {
							// 说明网络获取无数据
							Object obj = data.getObject();
							Message msg = new Message();
							msg.arg1 = LOAD_DAT_SUCCESS;
							msg.obj = obj;
							handler.sendMessage(msg);
							return;
						} else {
							Message msg = new Message();
							msg.arg1 = LOAD_DAT_FILURE;
							handler.sendMessage(msg);
						}
					}
				}, false, false);
	}

	/**
	 * 加载更多
	 * 
	 */
	private void userUtilsGetData_jiazai() {
		String time = "";
		if (!TextUtils.isEmpty(mLastStartTime)) {
			time = CommonUtils.getChangeToTime(mLastStartTime);
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("status", "2");
		// params.put("venueId", "24");
		params.put("action", "2");
		params.put("starttime", time);
		params.put("rows", "6");
		params.put("cityId", GlobalParams.CITY_ID);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ACTIVITY_DATA, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						if (data != null && data.getNetResultCode() == 0) {
							// 说明网络获取无数据
							Message msg = new Message();
							msg.arg1 = ADD_MORE_SUCCESS;
							msg.obj = data.getObject();
							handler.sendMessage(msg);
						} else {
							Message msg = new Message();
							msg.arg1 = ADD_MORE_FILE;
							handler.sendMessage(msg);
						}
					}
				}, false, false);
	}

	/**
	 * 下拉刷新
	 * 
	 * @param str
	 */
	private void userUtilsGetData_shuaxin() {
		// 调用方法，转换时间数据
		String time = "";
		if (!TextUtils.isEmpty(mFirdtStartTime)) {
			time = CommonUtils.getChangeToTime(mFirdtStartTime);
		}
		Map<String, String> params = new HashMap<String, String>();
		if (TextUtils.isEmpty(time)) {
			params.put("status", "2");
			// params.put("venueId", "24");
			params.put("action", "2");
			params.put("starttime", "");
			params.put("rows", "6");
		} else {
			params.put("status", "2");
			params.put("action", "1");
			params.put("starttime", time);
			int rows = (mList == null ? 6 : mList.size());
			params.put("rows", rows + "");
		}
		params.put("cityId", GlobalParams.CITY_ID);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ACTIVITY_DATA, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if (data != null && data.getNetResultCode() == 0) {
							Message msg = new Message();
							msg.arg1 = FRESH_SUCCESS;
							msg.obj = data.getObject();
							handler.sendMessage(msg);
						} else {
							Message msg = new Message();
							msg.arg1 = FERESH_ERROR;
							handler.sendMessage(msg);
						}
					}
				}, false, false);
	}

	/**
	 * 用来解析该界面从网络获取到的数据
	 * 
	 * @2016-2-18下午4:45:18
	 */
	private List<SenceBean> jsonParserData(String jsonstring) {
		List<SenceBean> list = new ArrayList<SenceBean>();
		try {
			JSONArray array = new JSONArray(jsonstring);
			int count = array.length();
			for (int i = 0; i < count; i++) {
				SenceBean bean = new SenceBean();
				JSONObject objs = array.getJSONObject(i);
				if (objs.has("camptype")) {
					bean.setCamptype(objs.getString("camptype"));
				}
				if (objs.has("cHeat")) {
					bean.setcHeat(objs.getString("cHeat"));
				}
				if (objs.has("id")) {
					bean.setId(objs.getString("id"));
				}
				if (objs.has("creattime")) {
					bean.setCreattime(objs.getString("creattime"));
				}
				if (objs.has("cStatus")) {
					bean.setcStatus(objs.getString("cStatus"));
				}
				if (objs.has("frontCoverImageURL")) {
					bean.setFrontCoverImageURL(objs
							.getString("frontCoverImageURL"));
				}
				if (objs.has("subhead")) {
					bean.setSubhead(objs.getString("subhead"));
				}
				if (objs.has("endtime")) {
					bean.setEndtime(objs.getString("endtime"));
				}
				if (objs.has("fileId")) {
					bean.setFileId(objs.getString("fileId"));
				}
				if (objs.has("title")) {
					bean.setmTitle(objs.getString("title"));
				}
				if (objs.has("selltimeend")) {
					bean.setSelltimeend(objs.getString("selltimeend"));
				}
				if (objs.has("starttime")) {
					bean.setStarttime(objs.getString("starttime"));
				}
				if (objs.has("tips")) {
					bean.setTips(objs.getString("tips"));
				}
				if (objs.has("venueId")) {
					bean.setVenueId(objs.getString("venueId"));
				}
				if (objs.has("label")) {
					bean.setLabel(objs.getString("label"));
				}
				if (objs.has("summary")) {
					bean.setSummary(objs.getString("summary"));
				}
				if (objs.has("fileUrl")) {
					bean.setFileUrl(objs.getString("fileUrl"));
				}
				if (objs.has("venueName")) {
					bean.setVenueName(objs.getString("venueName"));
				}
				if (objs.has("backgroudImageURL")) {
					bean.setBackgroudImageURL(objs
							.getString("backgroudImageURL"));
				}
				if (objs.has("resourceId")) {
					bean.setResourceId(objs.getString("resourceId"));
				}
				if (objs.has("productId")) {
					Log.i("wxn", "productId:" + objs.getString("productId"));
					bean.setProductId(objs.getString("productId"));
				}
				list.add(bean);
			}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return list;

	}

	/**
	 * 将网络获取的数据加载到界面当中的listview的方法
	 * 
	 * @2016-2-19上午9:35:33
	 */
	private void addDataToListview(final List<SenceBean> list) {
		if (mAdapter == null) {
			mAdapter = new SecondSenceFragmentListviewAdapter(getActivity(),
					list);
			mListview.setAdapter(mAdapter);
		} else {
			mAdapter.setData(list);
		}
		if (isShowLast) {
			mListview.setSelection(shouCount);
		}
		mListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				SenceBean bean = list.get(arg2);
				String status = bean.getcStatus();
				String id = bean.getId();
				if (status.equals("0")) {
					// 说明是未开始
					Intent intents = new Intent(getActivity(),
							NoStartActivity.class);
					intents.putExtra("list", list.get(arg2));
					intents.putExtra("tag", "activity");
					intents.putExtra("productId", list.get(arg2).getProductId());
					intents.putExtra(ID_TAG, list.get(arg2).getId());
					startActivity(intents);
				} else {
					Intent intent = new Intent(getActivity(),
							LiveDetialActivity.class);

					intent.putExtra("id", bean.getId());
					intent.putExtra("no", "1");
					intent.putExtra("lable", bean.getLabel());
					intent.putExtra("url", bean.getFileUrl());
					// 直播源id 用于直播
					intent.putExtra("resourceId", bean.getResourceId());
					intent.putExtra("end", "end");
					// 区别音乐会和球赛 0.比赛1.音乐会
					intent.putExtra("camptype", bean.getCamptype());
					startActivity(intent);
				}
			}

		});
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		if (mList != null) {
			mList.clear();
			mList = null;
		}
		mPulllistview = null;
		mAdapter = null;
		System.gc();
		super.onDestroy();
	}

	/**
	 * 解析加载更多多少
	 * 
	 * @param obj
	 */
	private void analyseAddMoreDate(String backdata_jiazai) {
		MainActivity.instance.setLayoutVisible();
		mPulllistview.onPullUpRefreshComplete();
		if (TextUtils.isEmpty(backdata_jiazai)) {
			return;
		}
		if (backdata_jiazai != null && !TextUtils.isEmpty(backdata_jiazai)) {
			List<SenceBean> list = jsonParserData(backdata_jiazai);
			if (list != null) {
				mList.addAll(list);
				if (mList != null) {
					mLastStartTime = mList.get(mList.size() - 1).getStarttime();
				}
				addDataToListview(mList);
			}
		}
	}

	/**
	 * 处理第一次加载数据
	 * 
	 * @param backdata
	 */
	private void analyseDate(String backdata) {
		if (mPulllistview != null)
			mPulllistview.onPullUpRefreshComplete();
		if (!TextUtils.isEmpty(backdata)) {
			mList = jsonParserData(backdata);
			if (mList != null && mList.size() > 0) {
				mFirdtStartTime = mList.get(0).getStarttime();
				if (mList.size() == 1) {
					mLastStartTime = mFirdtStartTime;
				} else {
					mLastStartTime = mList.get(mList.size() - 1).getStarttime();
				}
			}
			addDataToListview(mList);
		} else {
			addDataToListview(null);
		}
	}

	/***
	 * 解析下拉刷新数据
	 * 
	 * @param data
	 */
	private void refreshSuccess(String backdata_shuaxin) {
		MainActivity.instance.setLayoutVisible();
		mPulllistview.onPullDownRefreshComplete();
		if (!TextUtils.isEmpty(backdata_shuaxin)) {
			mList.clear();
			mList = jsonParserData(backdata_shuaxin);
			if (mList != null) {
				mLastStartTime = mList.get(mList.size() - 1).getStarttime();
			}
			addDataToListview(mList);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	}

	@Override
	public void initListener() {
	}

	@Override
	public void initData(Bundle savedInstanceState) {
	}

	@Override
	public void onClick(View v) {
	}

}
