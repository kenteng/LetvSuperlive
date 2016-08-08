package com.lesports.stadium.fragment;

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

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.LiveDetialActivity;
import com.lesports.stadium.activity.LoginActivity;
import com.lesports.stadium.activity.NoStartActivity;
import com.lesports.stadium.adapter.SenceFragmentListviewAdapter;
import com.lesports.stadium.base.BaseFragment;
import com.lesports.stadium.bean.SenceBean;
import com.lesports.stadium.bean.YuYueActivityBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.pullrefreshview.PullToRefreshBase;
import com.lesports.stadium.pullrefreshview.PullToRefreshBase.OnRefreshListener;
import com.lesports.stadium.pullrefreshview.PullToRefreshListView;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.UIUtils;
import com.lesports.stadium.view.CustomDialog;

/**
 * 
 * ***************************************************************
 * 
 * @Desc : 现场活动fragment
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwc
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
public class ActivityFragment extends BaseFragment implements OnClickListener {

	/**
	 * 页面视图view
	 */
	private View mFragmentview;
	/**
	 * 顶部标题
	 */
	private TextView mTitle;
	/**
	 * 顶部右边按钮
	 */
	private TextView mRight;
	/**
	 * 展示列表项的可下拉刷新的控件
	 */
	private PullToRefreshListView mPulltorefreshlistview;
	/**
	 * 需要加载数据以及布局的listview
	 */
	private ListView mListview;
	/**
	 * 未登陆提示按钮
	 */
	private CustomDialog exitDialog;
	/**
	 * 列表项数据控件listview的适配器
	 */
	private SenceFragmentListviewAdapter mAdapter;
	/**
	 * 顶部右侧弹出菜单布局
	 */
	private LinearLayout mRightLayout;
	/**
	 * 顶部右侧弹出菜单
	 */
	private PopupWindow mPopupWindow;
	/**
	 * 右侧菜单项——全部
	 */
	private TextView mRightAll;
	/**
	 * 右侧菜单项——进行中
	 */
	private TextView mRightIng;
	/**
	 * 右侧菜单项——未开始
	 */
	private TextView mRightNoStart;
	/**
	 * 右侧菜单项——已结束
	 */
	private TextView mRightEnd;
	/**
	 * 右侧菜单项——演唱会
	 */
	private TextView mRightSong;
	/**
	 * 右侧菜单项——比赛
	 */
	private TextView mRightGame;
	/**
	 * 用于标示全部类型的标记
	 */
	private final int ALL_TAG = 3;
	/**
	 * 用于标示进行中的标记
	 */
	private final int ING_TAG = 1;
	/**
	 * 用于标示未开始的标记
	 */
	private final int NOSTATRT_TAG = 0;
	/**
	 * 用于表示已结束的标记
	 */
	private final int EDDED_TAG = 2;
	/**
	 * 用于标示演唱会的标记
	 */
	private final int SONG_TAG = 4;
	/**
	 * 用于标示比赛的标记
	 */
	private final int GAME_TAG = 5;
	/**
	 * 列表项的数据源集合
	 */
	private List<SenceBean> mList = new ArrayList<SenceBean>();;
	/**
	 * 需要传递的活动id的标记
	 */
	private final String ID_TAG = "id";
	/**
	 * 无数据的时候显示的vi
	 */
	private TextView Nodata;
	/**
	 * 右侧下拉图片
	 */
	private ImageView mRightimage;
	/**
	 * 本类对象
	 */
	public static ActivityFragment instance;
	/**
	 * 用户预约列表数据集合
	 */
	public List<YuYueActivityBean> mYuyueList = new ArrayList<YuYueActivityBean>();
	/**
	 * 取消预约的bid
	 */
	private String bid;
	/**
	 * 定义一个变量，该变量用来动态的标注上拉加载的时候改变页码来进行获取数据
	 */
	private int PAGE_NUM = 2;
	/**
	 * 进度条布局
	 */
	private RelativeLayout mLayout_progressbar;
	/**
	 * 定义第一条活动的开始时间
	 */
	private String mFirdtStartTime;
	/**
	 * 定义最后一条的开始时间
	 */
	private String mLastStartTime;
	/**
	 * 是否是上拉加载，需要将list职位最后一行显示
	 */
	private boolean isShowLast = false;
	/**
	 * 上拉加载后要显示的行数
	 */
	private int shouCount;
	/**
	 * 预约失败
	 */
	private final int ADD_ACTIVE_FILUE = 14;
	/**
	 * 预约成功
	 */
	private final int ADD_ACTIVE_SUCCESS = 13;
	/**
	 * 取消预约成功
	 */
	private final int CANCU_ACTIVE_SUCCESS = 15;
	/**
	 * 取消预约失败
	 */
	private final int CANCU_ACTIVE_FILUE = 16;
	/**
	 * 结束下拉
	 */
	private final int OVER_PULL = 211;
	/**
	 * 结束刷新
	 */
	private final int OVER_REFRE = 21;
	/**
	 * 获取首页手机成功
	 */
	private final int GET_DATE_SUCCESS = 20;
	/**
	 * 加载更多获取成功
	 */
	private final int JIAZAI_SUCCESS = 30;
	/**
	 * 加载更多获取失败
	 */
	private final int JIAZAI_FILL = 31;
	/**
	 * 成功获取预约列表数据
	 */
	private final int YUYUE_SUCCESS = 10;
	/**
	 * 成功获取预约列表数据
	 */
	private final int YUYUE_LIST = 11;
	/**
	 * 获取预约列表数据 失败
	 */
	private final int YUYUE_FILUE = 9;
	/**
	 * 获取首页面数据成功
	 */
	private final int GET_DATE_SUCCESSS = 2;
	/**
	 * 获取预约列表数据 失败
	 */
	private final int GET_DATE_SUCCESSS_NO = 222;
	/**
	 * 处理数据的handler；
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case GET_DATE_SUCCESSS:
				analyseGetDate((String) msg.obj);
				break;
			case GET_DATE_SUCCESSS_NO:
				mLayout_progressbar.setVisibility(View.GONE);
				break;
			case YUYUE_FILUE:
				// 第一次获取预约列表下来没有数据的情况处理
				mLayout_progressbar.setVisibility(View.GONE);
				addDataToListview(mList, mListview);
				break;
			case YUYUE_SUCCESS:
				// 这里是第一次处理获取下来的预约列表数据
				analyseYuyueDate((String) msg.obj);
				break;
			case YUYUE_LIST:
				analyesYuYueDates((String) msg.obj);
				break;
			case ADD_ACTIVE_SUCCESS:
				analyseAddActiveDate((String) msg.obj);
				break;
			case ADD_ACTIVE_FILUE:
				Toast.makeText(getActivity(), "预约失败", 0).show();
				break;
			case CANCU_ACTIVE_SUCCESS:
				analyseCancelActive((String) msg.obj);
				break;
			case CANCU_ACTIVE_FILUE:
				Toast.makeText(getActivity(), "取消失败", 0).show();
				break;
			case GET_DATE_SUCCESS:
				analyseGetSuccessDate((String) msg.obj);
				break;
			case OVER_REFRE:
				mPulltorefreshlistview.onPullDownRefreshComplete();
				break;
			case OVER_PULL:
				mPulltorefreshlistview.onPullDownRefreshComplete();
				break;
			case JIAZAI_SUCCESS:
				analyseGetMore((String) msg.obj);
				break;
			case JIAZAI_FILL:
				mPulltorefreshlistview.onPullUpRefreshComplete();
				break;
			default:
				break;
			}

		}
	};

	@Override
	public View initView(LayoutInflater inflater) {
		instance = this;
		mFragmentview = inflater.inflate(R.layout.fragment_active, null);
		mLayout_progressbar = (RelativeLayout) mFragmentview
				.findViewById(R.id.layout_progress_layout_activity);
		mLayout_progressbar.setOnClickListener(this);
		mRight = (TextView) mFragmentview
				.findViewById(R.id.activity_jiemian_top_all);
		// 改变右边按钮
		mRight.setOnClickListener(this);
		// initRightTv(mRight, "全部", R.drawable.icon_down);
		mPulltorefreshlistview = (PullToRefreshListView) mFragmentview
				.findViewById(R.id.sence_fragment_listview);
		mListview = mPulltorefreshlistview.getRefreshableView();
		mPulltorefreshlistview.setPullLoadEnabled(true);
		mPulltorefreshlistview.setPullRefreshEnabled(true);
		// 初始化listview
		initListview();
		mPulltorefreshlistview
				.setOnRefreshListener(new OnRefreshListener<ListView>() {

					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 网络异常
						if (!UIUtils.isAvailable(getActivity())) {
							mPulltorefreshlistview.onPullDownRefreshComplete();
						} else {
							// 下拉刷新加载数据
							loadRefreshData(1, 1);
						}
					}

					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 网络异常
						if (!UIUtils.isAvailable(getActivity())) {
							mPulltorefreshlistview.onPullUpRefreshComplete();
						} else {
							// 上拉刷新加载数据
							loadRefreshData_add(PAGE_NUM);

						}
					}
				});
		return mFragmentview;
	}

	/**
	 * 提供给未开始界面来报名以后刷新数据
	 * 
	 * @param list
	 */
	public void setList(List<YuYueActivityBean> list) {
		if (list != null && list.size() != 0 && mYuyueList != null) {
			mYuyueList.addAll(list);
			if (mList != null && mList.size() != 0) {
				for (int i = 0; i < mList.size(); i++) {
					for (int j = 0; j < list.size(); j++) {
						if (mList.get(i).getId()
								.equals(list.get(j).getActivityId())) {
							int count = Integer.parseInt(mList.get(i)
									.getcHeat());
							count++;
							mList.get(i).setcHeat(count + "");
						}
					}
				}
			}
		}
	}

	private int index = 0;

	@Override
	public void onResume() {
		super.onResume();
		// 如果当前没有获取到数据
		if (GlobalParams.IS_REFRESH) {
			GlobalParams.IS_REFRESH = false;
			userUtilsGetData("1");
		}
		// 判断用户是否登录
		String isdenglu = GlobalParams.USER_ID;
		if (!TextUtils.isEmpty(isdenglu)) {
			// 已经登陆
			// 先判断是否已经获取下来过用户预约列表数据
			if (mYuyueList != null && mYuyueList.size() != 0) {
				changeData(mYuyueList, mList);
			} else {
				// 调用方法重新获取预约列表
				huoquyonghuyuyueliebiao_resume();
			}
		} else {
			if (mAdapter != null) {
				// 全部全部切换为false
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	/**
	 * 用来进行报名活动
	 */
	public void IngBaoming(String id) {
		String isString = GlobalParams.USER_ID;
		if (!TextUtils.isEmpty(isString)) {
			// 调用方法，进行报名
			userUtilsGetDataxsbaoming(id, isString);
		} else {
			createDialog();
		}
	}

	/**
	 * 用来进行取消报名活动
	 */
	public void IngBaomingQuxiao(String id) {
		String isString = GlobalParams.USER_ID;
		if (!TextUtils.isEmpty(isString)) {
			// 调用方法，进行报名
			// 从预约列表里面，得到该活动id对应的bid
			if (mYuyueList != null && mYuyueList.size() != 0) {
				for (int i = 0; i < mYuyueList.size(); i++) {
					if (mYuyueList.get(i).getActivityId().equals(id)) {
						bid = mYuyueList.get(i).getBid();
						userUtilsGetDataquxiao(mYuyueList.get(i).getBid());
					}
				}
			}
		} else {
			createDialog();
		}
	}

	/**
	 * @param mListview2
	 * @2016-2-18下午2:24:29 调用工具类，从网络获取服务器数据,来进行报名活动
	 */
	private void userUtilsGetDataxsbaoming(String huodongid, String userid) {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityId", huodongid);
		params.put("userId", userid);
		params.put("bStatus", "1");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ADD_ACTIVITY, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						if (data != null && data.getNetResultCode() == 0) {
							Message msg = new Message();
							msg.arg1 = ADD_ACTIVE_SUCCESS;
							msg.obj = data.getObject();
							handler.sendMessage(msg);
							// 说明网络获取无数据
						} else {
							Message msg = new Message();
							msg.arg1 = ADD_ACTIVE_FILUE;
							handler.sendMessage(msg);
						}
					}
				}, false, false);
	}

	/**
	 * @param mListview2
	 * @2016-2-18下午2:24:29 调用工具类，从网络获取服务器数据,取消报名
	 */
	private void userUtilsGetDataquxiao(String bid) {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("bid", bid);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.QUXIAOYUYUE_ACTIVITY, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						if (data != null && data.getNetResultCode() == 0) {
							Message msg = new Message();
							msg.arg1 = CANCU_ACTIVE_SUCCESS;
							msg.obj = data.getObject();
							handler.sendMessage(msg);
							// 说明网络获取无数据
						} else {
							Message msg = new Message();
							msg.arg1 = CANCU_ACTIVE_FILUE;
							handler.sendMessage(msg);
						}
					}
				}, false, false);
	}

	private void createDialog() {
		exitDialog = new CustomDialog(getActivity(), new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
				exitDialog.dismiss();
			}
		}, new OnClickListener() {

			@Override
			public void onClick(View v) {
				exitDialog.dismiss();
			}
		});
		exitDialog.setCancelTxt("取消");
		exitDialog.setConfirmTxt("去登录");
		exitDialog.setRemindTitle("温馨提示");
		exitDialog.setRemindMessage("登录之后才能进行预约哦~");
		exitDialog.show();
	}

	/**
	 * 用来比对活动列表数据是否在预约列表中存在，如果存在，则置换数据未true
	 * 
	 * @param mYuyueList
	 * @param mList
	 */
	private List<SenceBean> changeDatasss_quxiao(
			List<YuYueActivityBean> mYuyueList, List<SenceBean> mList) {
		// TODO Auto-generated method stub
		if (mList != null && mList.size() != 0) {
			for (int i = 0; i < mList.size(); i++) {
				mList.get(i).setBaoming(false);
			}
		}
		if (mYuyueList != null && mYuyueList.size() != 0 && mList != null
				&& mList.size() != 0) {
			int counts = mList.size();
			for (int i = 0; i < counts; i++) {
				for (int j = 0; j < mYuyueList.size(); j++) {
					if (mList.get(i).getId()
							.equals(mYuyueList.get(j).getActivityId())) {
						mList.get(i).setBaoming(true);
					}
				}
			}
		} else {
			if (mList != null && mList.size() != 0) {
				for (int i = 0; i < mList.size(); i++) {
					mList.get(i).setBaoming(false);
				}
			}
		}
		return mList;
	};

	/**
	 * 用来比对活动列表数据是否在预约列表中存在，如果存在，则置换数据未true,并且更新数据
	 * 
	 * @param mYuyueList
	 * @param mList
	 */
	private List<SenceBean> changeDatasss_add(
			List<YuYueActivityBean> mYuyueList, List<SenceBean> mList) {
		// TODO Auto-generated method stub
		if (mYuyueList != null && mYuyueList.size() != 0 && mList != null
				&& mList.size() != 0) {
			int counts = mList.size();
			for (int i = 0; i < counts; i++) {
				for (int j = 0; j < mYuyueList.size(); j++) {
					if (mList.get(i).getId()
							.equals(mYuyueList.get(j).getActivityId())) {
						mList.get(i).setBaoming(true);
						int count = Integer.parseInt(mList.get(i).getcHeat());
						count = count + 1;
						mList.get(i).setcHeat(count + "");
					}
				}
			}
		}
		return mList;
	};

	/**
	 * 用来比对活动列表数据是否在预约列表中存在，如果存在，则置换数据未true
	 * 
	 * @param mYuyueList
	 * @param mList
	 */
	private List<SenceBean> changeDatasss(List<YuYueActivityBean> mYuyueList,
			List<SenceBean> mList) {
		// TODO Auto-generated method stub
		if (mYuyueList != null && mYuyueList.size() != 0 && mList != null
				&& mList.size() != 0) {
			int counts = mList.size();
			for (int i = 0; i < counts; i++) {
				for (int j = 0; j < mYuyueList.size(); j++) {
					if (mList.get(i).getId()
							.equals(mYuyueList.get(j).getActivityId())) {
						mList.get(i).setBaoming(true);
					}
				}
			}
		}
		return mList;
	};

	/**
	 * 用来比对活动列表数据是否在预约列表中存在，如果存在，则置换数据未true
	 * 
	 * @param mYuyueList
	 * @param mList
	 */
	private void changeData(List<YuYueActivityBean> mYuyueList,
			List<SenceBean> mList) {
		// TODO Auto-generated method stub
		if (mYuyueList != null && mYuyueList.size() != 0 && mList != null
				&& mList.size() != 0) {
			int counts = mList.size();
			for (int i = 0; i < counts; i++) {
				for (int j = 0; j < mYuyueList.size(); j++) {
					if (mList.get(i).getId()
							.equals(mYuyueList.get(j).getActivityId())) {
						mList.get(i).setBaoming(true);
					}
				}
			}
			if (mAdapter != null && mListview != null) {
				mAdapter.setData(mList);
			} else {
				mAdapter = new SenceFragmentListviewAdapter(getActivity(),
						mList);
				mListview.setAdapter(mAdapter);
			}
		}
	};

	/**
	 * 处理活动列表数据
	 * 
	 * @param backdataid
	 */
	private List<YuYueActivityBean> jsonway(String backdataid) {
		// TODO Auto-generated method stub
		List<YuYueActivityBean> list = new ArrayList<YuYueActivityBean>();
		try {
			JSONArray array = new JSONArray(backdataid);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				YuYueActivityBean bean = new YuYueActivityBean();
				if (obj.has("activityId")) {
					bean.setActivityId(obj.getString("activityId"));
				}
				if (obj.has("bid")) {
					bean.setBid(obj.getString("bid"));
				}
				if (obj.has("bStatus")) {
					bean.setbStatus(obj.getString("bStatus"));
				}
				if (obj.has("userId")) {
					bean.setUserId(obj.getString("userId"));
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
	 * @param mListview2
	 * @2016-2-18下午2:24:29 huoqu bao ming liebiao
	 */
	private void huoquyonghuyuyueliebiao_intent() {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("detailed", "");
		params.put("finished", "");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.YONGHUYUYUE_LIST, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						if (data != null && data.getNetResultCode() == 0) {
							Object obj = data.getObject();
							String backdata = obj.toString();
							Message msg = new Message();
							msg.arg1 = YUYUE_SUCCESS;
							msg.obj = backdata;
							handler.sendMessage(msg);
						} else {
							Message msg = new Message();
							msg.arg1 = YUYUE_FILUE;
							handler.sendMessage(msg);
						}
					}
				}, false, false);

	}

	/**
	 * @param mListview2
	 * @2016-2-18下午2:24:29 huoqu bao ming liebiao
	 */
	private void huoquyonghuyuyueliebiao_resume() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("detailed", "");
		params.put("finished", "");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.YONGHUYUYUE_LIST, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						if (data != null && data.getNetResultCode() == 0) {
							Message msg = new Message();
							msg.arg1 = YUYUE_LIST;
							msg.obj = data.getObject();
							handler.sendMessage(msg);
							// 说明网络获取无数据
						} else {
							Message msg = new Message();
							msg.arg1 = 111;
							handler.sendMessage(msg);
						}
					}
				}, false, false);
	}

	/**
	 * 上拉下拉数据加载
	 * 
	 * @param i
	 * @param j
	 */
	protected void loadRefreshData(int i, int j) {
		// TODO Auto-generated method stub
		// Toast.makeText(getActivity(), "haha", 0).show();
		isShowLast = false;
		Message message = new Message();
		message.arg1 = OVER_PULL;
		handler.sendMessageDelayed(message, 1500);
		if (mList != null && mList.size() != 0) {
			userUtilsGetData_shuaxin("1");
		} else {
			userUtilsGetData_shuaxin("1");
		}

	}

	protected void loadRefreshData_add(int i) {
		// TODO Auto-generated method stub
		// Toast.makeText(getActivity(), "haha", 0).show();
		isShowLast = true;
		if (mList != null && mList.size() > 2)
			shouCount = mList.size() - 2;
		Message message = new Message();
		message.arg1 = JIAZAI_FILL;
		handler.sendMessageDelayed(message, 1500);
		userUtilsGetData_jiazai(i);
	}

	/**
	 * 初始化界面中的listview，并且给其加载初始数据
	 * 
	 * @param mListview2
	 * @2016-1-29上午11:52:02
	 */
	private void initListview() {
		// 调用工具类，从网络获取数据
		/** 由于这会接口数据不对，所以这里先注释掉 */
		mLayout_progressbar.setVisibility(View.VISIBLE);
		userUtilsGetData("1");

		// TODO Auto-generated method stub
		// initlistviewdata();
	}

	/**
	 * @param mListview2
	 * @2016-2-18下午2:24:29 调用工具类，从网络获取服务器数据
	 */
	private void userUtilsGetData(String str) {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("status", "0,1");
		// params.put("venueId", "24");
		params.put("action", "2");
		params.put("starttime", "");
		params.put("rows", "6");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ACTIVITY_DATA, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						if (data != null) {
							// 说明网络获取无数据
							Object obj = data.getObject();
							if (obj != null) {
								String backdata = obj.toString();
								Log.i("wxn", "活动列表数据：" + backdata);
								if (data.getNetResultCode() == 0) {
									Message msg = new Message();
									msg.arg1 = GET_DATE_SUCCESSS;
									msg.obj = backdata;
									handler.sendMessage(msg);
									return;
								}
							}
						}
						Message msg = new Message();
						msg.arg1 = GET_DATE_SUCCESSS_NO;
						handler.sendMessage(msg);
					}
				}, false, false);
	}

	private void userUtilsGetData_jiazai(int str) {
		String time = "";
		if (!TextUtils.isEmpty(mLastStartTime)) {
			time = getChangeToTime(mLastStartTime);
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("status", "0,1");
		// params.put("venueId", "24");
		params.put("action", "2");
		params.put("starttime", time);
		params.put("rows", "6");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ACTIVITY_DATA, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						if (data != null && data.getNetResultCode() == 0) {
							// 调用方法，给listview加载数据，先将数据通过handler发送到主线程中
							Message msg = new Message();
							msg.arg1 = JIAZAI_SUCCESS;
							msg.obj = data.getObject();
							handler.sendMessage(msg);
						} else {
							// 调用方法，给listview加载数据，先将数据通过handler发送到主线程中
							Message msg = new Message();
							msg.arg1 = JIAZAI_FILL;
							handler.sendMessage(msg);
						}
					}
				}, false, false);
	}

	private void userUtilsGetData_shuaxin(String str) {
		// 调用方法，转换时间数据
		String time = "";
		if (!TextUtils.isEmpty(mFirdtStartTime)) {
			time = getChangeToTime(mFirdtStartTime);
		}
		Map<String, String> params = new HashMap<String, String>();
		if (TextUtils.isEmpty(time)) {
			params.put("status", "0,1");
			// params.put("venueId", "24");
			params.put("action", "2");
			params.put("starttime", "");
			params.put("rows", "6");
		} else {
			params.put("status", "0,1");
			// params.put("venueId", "24");
			params.put("action", "1");
			params.put("starttime", time);
			params.put("rows", "6");
		}
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ACTIVITY_DATA, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if (data == null) {
							// 说明网络获取无数据
						} else {
							Object obj = data.getObject();
							if (obj == null) {
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
								} else {
									if (data.getNetResultCode() == 0) {
										// 调用方法，给listview加载数据，先将数据通过handler发送到主线程中
										Message msg = new Message();
										msg.arg1 = GET_DATE_SUCCESS;
										msg.obj = backdata;
										handler.sendMessage(msg);
									} else if (data.getNetResultCode() == 600) {
										// 调用方法，给listview加载数据，先将数据通过handler发送到主线程中
										Message msg = new Message();
										msg.arg1 = OVER_PULL;
										handler.sendMessage(msg);
									} else {
										Message msg = new Message();
										msg.arg1 = OVER_REFRE;
										handler.sendMessage(msg);
									}
								}
							}

						}
					}
				}, false, false);
	}

	/**
	 * 将网络获取的数据加载到界面当中的listview的方法
	 * 
	 * @2016-2-19上午9:35:33
	 */
	private void addDataToListview(final List<SenceBean> list,
			ListView mListview2) {

		mAdapter = new SenceFragmentListviewAdapter(getActivity(), list);
		mListview2.setAdapter(mAdapter);
		mListview2.setEmptyView(mFragmentview
				.findViewById(R.id.activity_fragment_nodata));
		if (isShowLast) {
			mListview2.setSelection(shouCount);
		}
		mListview2.setOnItemClickListener(new OnItemClickListener() {
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
					startActivity(intent);
				}
			}

		});
	}

	/**
	 * 将一个传入的字符串毫秒值转换成一个字符串时间值
	 * 
	 * @param str
	 * @return
	 */
	public String getChangeToTime(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		long time = Long.parseLong(str);
		Date date = new Date();
		date.setTime(time);
		String timestring = sdf.format(date);

		return timestring;

	}

	/**
	 * 根据传入的控件资源id，以及坐标点，来弹出一个window
	 * 
	 * @2016-1-29下午3:00:29
	 * @param x
	 *            y 是为了定位，
	 * @author JZKJ-LWC
	 */
	public void showPopupWindow(int id, int x, int y) {
		mRightLayout = (LinearLayout) LayoutInflater.from(getActivity())
				.inflate(R.layout.sence_dialog, null);

		mPopupWindow = new PopupWindow(getActivity());
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setWidth(getActivity().getWindowManager()
				.getDefaultDisplay().getWidth() / 3);
		mPopupWindow.setHeight(getActivity().getWindowManager()
				.getDefaultDisplay().getWidth() / 3);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setContentView(mRightLayout);
		mPopupWindow.showAsDropDown(mFragmentview.findViewById(id), x, 10);
		// mPopupWindow.showAtLocation(mFragmentview.findViewById(id),
		// Gravity.RIGHT
		// | Gravity.TOP, x, y);
		// 初始化该布局内控件
		initWindowView(mRightLayout);
	}

	/**
	 * @2016-1-29下午3:26:05 获取该布局内控件，并且进行初始化操作
	 */
	private void initWindowView(LinearLayout mRightLayout2) {
		// TODO Auto-generated method stub
		mRightAll = (TextView) mRightLayout2
				.findViewById(R.id.sence_window_all);
		mRightAll.setOnClickListener(this);
		mRightEnd = (TextView) mRightLayout2
				.findViewById(R.id.sence_window_end);
		mRightEnd.setOnClickListener(this);
		mRightGame = (TextView) mRightLayout2
				.findViewById(R.id.sence_window_bisai);
		mRightGame.setOnClickListener(this);
		mRightIng = (TextView) mRightLayout2
				.findViewById(R.id.sence_window_ing);
		mRightIng.setOnClickListener(this);
		mRightNoStart = (TextView) mRightLayout2
				.findViewById(R.id.sence_window_nostart);
		mRightNoStart.setOnClickListener(this);
		mRightSong = (TextView) mRightLayout2
				.findViewById(R.id.sence_window_song);
		mRightSong.setOnClickListener(this);
	}

	/**
	 * @param iconDown
	 * @param mRight2
	 * @param string
	 * @author JZKJ-LWC
	 * @2016-1-29上午10:41:15 将传入的控件，字符串，图片，整合成一个完整的控件
	 */
	private void initRightTv(TextView mRight2, String string, int iconDown) {
		// TODO Auto-generated method stub
		// Drawable drawable =
		// getActivity().getResources().getDrawable(iconDown);
		// drawable.setBounds(0, 3, 18, 15);
		// mRight2.setCompoundDrawables(null, null, drawable, null);
		mRight2.setText(string);
		mRight2.setTextColor(Color.rgb(0, 124, 172));
		mRight2.setVisibility(View.VISIBLE);
		mRight2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.activity_jiemian_top_all:
			// 右侧按钮被点击，弹出window
			mRight.getTop();
			int y = mRight.getBottom() * 3 / 2;
			int x = getActivity().getWindowManager().getDefaultDisplay()
					.getWidth() / 6;
			showPopupWindow(R.id.title_right_tv, x, y);
			break;
		case R.id.sence_window_all:
			mRight.setText(mRightAll.getText().toString());
			mPopupWindow.dismiss();
			// 菜单项内全部
			if (mList != null && mList.size() != 0) {
				mAdapter.setTagandData(ALL_TAG, mList);
				// 同时需要，在点击该按钮的时候，需要通知适配器内数据发生改变，展示该类别
				mListview.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						String status = mList.get(arg2).getcStatus();
						Log.i("现在的状态是", status);
						if (status.equals("0")) {
							// 说明是未开始
							Intent intents = new Intent(getActivity(),
									NoStartActivity.class);
							intents.putExtra("productId", mList.get(arg2)
									.getProductId());
							intents.putExtra("bg", mList.get(arg2)
									.getBackgroudImageURL());
							intents.putExtra(ID_TAG, mList.get(arg2).getId());
							startActivity(intents);
						} else {
							Intent intent = new Intent(getActivity(),
									LiveDetialActivity.class);
							intent.putExtra("id", mList.get(arg2).getId());
							intent.putExtra("no", "1");
							intent.putExtra("lable", mList.get(arg2).getLabel());
							intent.putExtra("url", mList.get(arg2).getFileUrl());
							startActivity(intent);
						}
					}
				});
			}
			break;
		case R.id.sence_window_bisai:
			mRight.setText(mRightGame.getText().toString());
			mPopupWindow.dismiss();
			// 菜单项内比赛
			// 调用方法，从数据源集合中拿出所有的比赛数据，然后给列表项
			final List<SenceBean> list = getdataCheckGame(mList);
			if (list != null && list.size() != 0) {
				mAdapter.setTagandData(GAME_TAG, list);
				mListview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						String status = list.get(arg2).getcStatus();
						Log.i("现在的状态是", status);
						if (status.equals("0")) {
							// 说明是未开始
							Intent intents = new Intent(getActivity(),
									NoStartActivity.class);
							intents.putExtra("productId", list.get(arg2)
									.getProductId());
							intents.putExtra("bg", mList.get(arg2)
									.getBackgroudImageURL());
							intents.putExtra(ID_TAG, list.get(arg2).getId());
							startActivity(intents);
						} else {
							Intent intent = new Intent(getActivity(),
									LiveDetialActivity.class);
							intent.putExtra(ID_TAG, list.get(arg2).getId());
							intent.putExtra("no", "1");
							intent.putExtra("lable", list.get(arg2).getLabel());
							intent.putExtra("url", list.get(arg2).getFileUrl());
							startActivity(intent);
						}
					}
				});
			}

			break;
		case R.id.sence_window_end:
			// 菜单项内已结束
			mRight.setText(mRightEnd.getText().toString());
			mPopupWindow.dismiss();
			final List<SenceBean> list_End = getdataCheckEnd(mList);
			if (list_End != null && list_End.size() != 0) {
				mAdapter.setTagandData(EDDED_TAG, list_End);

				mListview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// 说明是未开始
						Intent intent = new Intent(getActivity(),
								LiveDetialActivity.class);
						intent.putExtra(ID_TAG, list_End.get(arg2).getId());
						intent.putExtra("no", "1");
						intent.putExtra("lable", list_End.get(arg2).getLabel());
						intent.putExtra("url", list_End.get(arg2).getFileUrl());
						startActivity(intent);
					}
				});
			}

			break;
		case R.id.layout_progress_layout_activity:
			break;
		case R.id.sence_window_ing:
			// 菜单项内正在进行中
			mRight.setText(mRightIng.getText().toString());
			mPopupWindow.dismiss();
			final List<SenceBean> list_ing = getdataCheckIng(mList);
			if (list_ing != null && list_ing.size() != 0) {
				mAdapter.setTagandData(ING_TAG, list_ing);

				mListview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// 说明是未开始
						Intent intent = new Intent(getActivity(),
								LiveDetialActivity.class);
						intent.putExtra(ID_TAG, list_ing.get(arg2).getId());
						intent.putExtra("no", "1");
						intent.putExtra("lable", list_ing.get(arg2).getLabel());
						intent.putExtra("url", list_ing.get(arg2).getFileUrl());
						startActivity(intent);
					}
				});
			}
			break;
		case R.id.sence_window_nostart:
			// 菜单项内未开始
			mRight.setText(mRightNoStart.getText().toString());
			mPopupWindow.dismiss();
			final List<SenceBean> list_nostart = getdataCheckNostart(mList);
			if (list_nostart != null && list_nostart.size() != 0) {
				mAdapter.setTagandData(NOSTATRT_TAG, list_nostart);
				mListview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// 说明是未开始
						Intent intents = new Intent(getActivity(),
								NoStartActivity.class);
						intents.putExtra("productId", list_nostart.get(arg2)
								.getProductId());
						intents.putExtra("bg", mList.get(arg2)
								.getBackgroudImageURL());
						intents.putExtra(ID_TAG, list_nostart.get(arg2).getId());
						startActivity(intents);
					}
				});
			}

			break;
		case R.id.sence_window_song:
			// 菜单项内演唱会
			mRight.setText(mRightSong.getText().toString());
			mPopupWindow.dismiss();
			final List<SenceBean> list_song = getdataCheckSong(mList);
			if (list_song != null && list_song.size() != 0) {
				mAdapter.setTagandData(SONG_TAG, list_song);
				mListview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						String status = list_song.get(arg2).getcStatus();
						Log.i("现在的状态是", status);
						if (status.equals("0")) {
							// 说明是未开始
							Intent intents = new Intent(getActivity(),
									NoStartActivity.class);
							intents.putExtra("bg", mList.get(arg2)
									.getBackgroudImageURL());
							intents.putExtra(ID_TAG, list_song.get(arg2)
									.getId());
							intents.putExtra("productId", list_song.get(arg2)
									.getProductId());
							startActivity(intents);
						} else {
							Intent intent = new Intent(getActivity(),
									LiveDetialActivity.class);
							intent.putExtra(ID_TAG, list_song.get(arg2).getId());
							intent.putExtra("no", "1");
							intent.putExtra("lable", list_song.get(arg2)
									.getLabel());
							intent.putExtra("url", list_song.get(arg2)
									.getFileUrl());
							startActivity(intent);
						}
					}
				});
			}

			break;

		default:
			break;
		}
	}

	/**
	 * 遍历数据源，从其中取出所有进行中相关数据
	 * 
	 * @2016-2-26上午11:08:52
	 */
	private List<SenceBean> getdataCheckEnd(List<SenceBean> mList2) {
		List<SenceBean> list = new ArrayList<SenceBean>();
		if (mList2 != null && mList2.size() != 0) {
			for (int i = 0; i < mList2.size(); i++) {
				SenceBean bean = mList2.get(i);
				int status = Integer.parseInt(bean.getcStatus());
				if (status == 2) {
					list.add(bean);
				}
			}
		}
		return list;
	}

	/**
	 * 遍历数据源，从其中取出所有进行中相关数据
	 * 
	 * @2016-2-26上午11:08:52
	 */
	private List<SenceBean> getdataCheckIng(List<SenceBean> mList2) {
		List<SenceBean> list = new ArrayList<SenceBean>();
		if (mList2 != null && mList2.size() != 0) {
			for (int i = 0; i < mList2.size(); i++) {
				SenceBean bean = mList2.get(i);
				int status = Integer.parseInt(bean.getcStatus());
				if (status == 1) {
					list.add(bean);
				}
			}
		}
		return list;
	}

	/**
	 * 遍历数据源，从其中取出所有未开始相关数据
	 * 
	 * @2016-2-26上午11:08:52
	 */
	private List<SenceBean> getdataCheckNostart(List<SenceBean> mList2) {
		List<SenceBean> list = new ArrayList<SenceBean>();
		if (mList2 != null && mList2.size() != 0) {
			for (int i = 0; i < mList2.size(); i++) {
				SenceBean bean = mList2.get(i);
				int status = Integer.parseInt(bean.getcStatus());
				if (status == 0) {
					list.add(bean);
				}
			}
		}
		return list;
	}

	/**
	 * 遍历数据源，从其中取出所有比赛相关数据
	 * 
	 * @2016-2-26上午11:08:52
	 */
	private List<SenceBean> getdataCheckGame(List<SenceBean> mList2) {
		List<SenceBean> list = new ArrayList<SenceBean>();
		if (mList2 != null && mList2.size() != 0) {
			for (int i = 0; i < mList2.size(); i++) {
				SenceBean bean = mList2.get(i);
				int company = Integer.parseInt(bean.getCamptype());
				if (company == 0) {
					list.add(bean);
				}
			}
		}

		return list;
	}

	/**
	 * 遍历数据源，从其中取出所有演唱会相关数据
	 * 
	 * @2016-2-26上午11:08:52
	 */
	private List<SenceBean> getdataCheckSong(List<SenceBean> mList2) {
		List<SenceBean> list = new ArrayList<SenceBean>();
		if (mList2 != null && mList2.size() != 0) {
			for (int i = 0; i < mList2.size(); i++) {
				SenceBean bean = mList2.get(i);
				int company = Integer.parseInt(bean.getCamptype());
				if (company == 1) {
					list.add(bean);
				}
			}
		}
		return list;
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

	@Override
	public void onDestroy() {
		if (handler != null)
			handler.removeCallbacksAndMessages(null);
		if (mList != null) {
			mList.clear();
			mList = null;
		}
		if (mYuyueList != null) {
			mYuyueList.clear();
			mYuyueList = null;
		}
		instance = null;
		super.onDestroy();
	}

	/**
	 * 解析预约成功数据
	 */
	private void analyseAddActiveDate(String baoming) {
		if (!TextUtils.isEmpty(baoming)) {
			// 将报名数据解析出来，然后加入到本地的预约列表集合中
			List<YuYueActivityBean> list = jsonway(baoming);
			mYuyueList.addAll(list);
			// 调用方法，刷新数据
			List<SenceBean> listss = changeDatasss(mYuyueList, mList);
			if (list != null && list.size() != 0) {
				YuYueActivityBean beans = list.get(0);
				String id = beans.getActivityId();
				if (!TextUtils.isEmpty(id)) {
					for (int i = 0; i < listss.size(); i++) {
						if (listss.get(i).getId().equals(id)) {
							int count = Integer.parseInt(listss.get(i)
									.getcHeat());
							count = count + 1;
							listss.get(i).setcHeat(count + "");
						}
					}
					mAdapter.setData(listss);
				}
			}
		} else {
			Toast.makeText(getActivity(), "预约失败", 0).show();
		}
	}

	/**
	 * 解析取消预约 数据
	 */
	private void analyseCancelActive(String badadddd) {
		if (!TextUtils.isEmpty(badadddd)) {
			if (badadddd.equals("success")) {
				if (mYuyueList != null && mYuyueList.size() != 0) {
					for (int i = 0; i < mYuyueList.size(); i++) {
						if (!TextUtils.isEmpty(bid)) {
							if (mYuyueList.get(i).getBid().equals(bid)) {// 根据活动id来删除数据，进行刷新
								for (int j = 0; j < mList.size(); j++) {
									if (mYuyueList.get(i).getActivityId()
											.equals(mList.get(j).getId())) {
										int counts = Integer.parseInt(mList
												.get(j).getcHeat());
										counts--;
										mList.get(j).setcHeat(counts + "");
									}
								}
								mYuyueList.remove(i);
							}
						}
					}
					// 循环完成，需要改变列表项状态
					if (mList != null && mList.size() != 0) {
						// changeData(mYuyueList, mList);
						List<SenceBean> list = changeDatasss_quxiao(mYuyueList,
								mList);
						mAdapter.setData(list);
					}
				}
			}
		}
	}

	/**
	 * 解析首页说起的数据
	 * 
	 * @param backdata_shuaxin
	 */
	private void analyseGetSuccessDate(String backdata_shuaxin) {
		if (backdata_shuaxin != null && backdata_shuaxin.length() != 0) {
			mPulltorefreshlistview.onPullDownRefreshComplete();
			mList.clear();
			mList = jsonParserData(backdata_shuaxin);
			if (mList != null && mList.size() != 0) {
				// 这里需要对登录状态做判断，如果已经登陆，则做请求，若未登录，则不做任何操作
				if (mList != null && mList.size() != 0) {
					mFirdtStartTime = mList.get(0).getStarttime();
					mLastStartTime = mList.get(mList.size() - 1).getStarttime();
					String isdenglu = GlobalParams.USER_ID;
					if (!TextUtils.isEmpty(isdenglu)) {
						// 已经登陆
						huoquyonghuyuyueliebiao_intent();
					} else {
						// 未登录状态
						addDataToListview(mList, mListview);
					}
				}
			}

		}
	}

	/**
	 * 解析加载更多数据
	 */
	private void analyseGetMore(String backdata_jiazai) {
		if (backdata_jiazai != null && !TextUtils.isEmpty(backdata_jiazai)) {
			mPulltorefreshlistview.onPullUpRefreshComplete();
			PAGE_NUM++;
			List<SenceBean> list = jsonParserData(backdata_jiazai);
			mList.addAll(list);
			if (mList != null && mList.size() != 0) {
				// 这里需要对登录状态做判断，如果已经登陆，则做请求，若未登录，则不做任何操作
				if (mList != null && mList.size() != 0) {
					mFirdtStartTime = mList.get(0).getStarttime();
					mLastStartTime = mList.get(mList.size() - 1).getStarttime();
					String isdenglu = GlobalParams.USER_ID;
					if (!TextUtils.isEmpty(isdenglu)) {
						// 已经登陆
						huoquyonghuyuyueliebiao_intent();
					} else {
						// 未登录状态
						addDataToListview(mList, mListview);
					}
				}
			}

		}
	}

	/**
	 * 解析预约列表数据
	 * 
	 * @param bacadatas
	 */
	private void analyseYuyueDate(String bacadatas) {
		if (!TextUtils.isEmpty(bacadatas)) {
			// 解析该数据
			mLayout_progressbar.setVisibility(View.GONE);
			mYuyueList = jsonway(bacadatas);
			if (mList != null && mList.size() != 0) {
				if (mYuyueList != null && mYuyueList.size() != 0) {
					List<SenceBean> list = changeDatasss(mYuyueList, mList);
					if (list != null && list.size() != 0) {
						addDataToListview(list, mListview);
					}
				} else {
					// 说明没有预约数据，则直接拿着现有数据显示
					addDataToListview(mList, mListview);
				}
			}
			// 调用方法，比对数据
		} else {
			mLayout_progressbar.setVisibility(View.GONE);
			addDataToListview(mList, mListview);
		}
	}

	private void analyesYuYueDates(String bacadatasss) {
		// 这里是meiyici 被看见的时候获取下来的预约列表数据
		if (!TextUtils.isEmpty(bacadatasss)) {
			// 解析该数据
			mYuyueList = jsonway(bacadatasss);
			if (mList != null && mList.size() != 0) {
				if (mYuyueList != null && mYuyueList.size() != 0) {
					changeData(mYuyueList, mList);
				} else {
					addDataToListview(mList, mListview);
				}
			}
		}
	}

	private void analyseGetDate(String backdata) {
		mLayout_progressbar.setVisibility(View.GONE);
		if (!TextUtils.isEmpty(backdata)) {
			mList = jsonParserData(backdata);
			if (mList != null && mList.size() != 0) {
				// 这里需要对登录状态做判断，如果已经登陆，则做请求，若未登录，则不做任何操作
				if (mList != null && mList.size() != 0) {
					mFirdtStartTime = mList.get(0).getStarttime();
					mLastStartTime = mList.get(mList.size() - 1).getStarttime();
					String isdenglu = GlobalParams.USER_ID;
					if (!TextUtils.isEmpty(isdenglu)) {
						// 已经登陆
						huoquyonghuyuyueliebiao_intent();
					} else {
						// 未登录状态
						addDataToListview(mList, mListview);
					}
				}
			}

		} else {
			mLayout_progressbar.setVisibility(View.GONE);
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

}
