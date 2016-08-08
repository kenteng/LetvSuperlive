package com.lesports.stadium.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lesports.stadium.R;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.PowerAdapter;
import com.lesports.stadium.bean.PowerBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.pullrefreshview.PullToRefreshBase;
import com.lesports.stadium.pullrefreshview.PullToRefreshListView;
import com.lesports.stadium.pullrefreshview.PullToRefreshBase.OnRefreshListener;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.UIUtils;
import com.lesports.stadium.utils.Utils;
import com.lesports.stadium.view.CircleImageView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * ***************************************************************
 * 
 * @Desc : 能量值界面
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
public class PowerActivity extends Activity implements OnClickListener {

	/**
	 * 顶部返回键
	 */
	private ImageView mBack;
	/**
	 * 能量值数额
	 */
	private TextView mPowerNum;
	/**
	 * 左边头像
	 */
	private CircleImageView mLeftHrader;
	/**
	 * 左边名称
	 */
	private TextView mLeftName;
	/**
	 * 左边数量
	 */
	private TextView mLeftNum;
	/**
	 * 中间头像
	 */
	private CircleImageView mCenterHeader;
	/**
	 * 中间名称
	 */
	private TextView mCenterName;
	/**
	 * 中间数量
	 */
	private TextView mCenterNum;
	/**
	 * 右边头像
	 */
	private CircleImageView mRightHeader;
	/**
	 * 右边名称
	 */
	private TextView mRightName;
	/**
	 * 右边数量
	 */
	private TextView mRightNum;
	/**
	 * 刷新数据列表项
	 */
	private PullToRefreshListView mPullToRefreshListView;
	/**
	 * 列表项
	 */
	private ListView mListview;
	/**
	 * 用来标记网络获取的数据为空
	 */
	private final int OVER_TAG = 40;
	/**
	 * 数据存储
	 */
	private List<PowerBean> powerList = new ArrayList<PowerBean>();
	private PowerAdapter powerAdapter;
	public static final int POWER_OK = 0;
	/**
	 * 数据id
	 */
	private String mresource_id;
	private String mresource_type;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case POWER_OK:
				if (powerList != null) {
					powerList.clear();
				}
				if (mPullToRefreshListView != null) {
					mPullToRefreshListView.onPullDownRefreshComplete();
					mPullToRefreshListView.onPullUpRefreshComplete();
				}
				String result = (String) msg.obj;
				if (!TextUtils.isEmpty(result)) {
//					powerList = useWayHanderData(result);
					try {

						JSONArray powerArray = new JSONArray(result);
						for (int i = 0; i < powerArray.length(); i++) {
							PowerBean tempBean = new PowerBean();
							JSONObject powerObj = powerArray.getJSONObject(i);
							tempBean.setEnergy(powerObj.getString("energy"));
							JSONObject userObj = powerObj
									.getJSONObject("letvUser");
							tempBean.setNickname(userObj.getString("nickname"));
							tempBean.setPicture(userObj.getString("picture"));
							powerList.add(tempBean);
						}
						powerAdapter.setPowerList(powerList);
						if (powerList.size() > 3) {
							mCenterName.setText(powerList.get(0).getNickname());
							LApplication.loader.DisplayImage(
									ConstantValue.BASE_IMAGE_URL
											+ powerList.get(0).getPicture()
											+ ConstantValue.IMAGE_END,
									mCenterHeader, R.drawable.default_header);
							mCenterNum.setText(powerList.get(0).getEnergy());
							mLeftName.setText(powerList.get(1).getNickname());
							LApplication.loader.DisplayImage(
									ConstantValue.BASE_IMAGE_URL
											+ powerList.get(1).getPicture()
											+ ConstantValue.IMAGE_END,
									mLeftHrader, R.drawable.default_header);
							mLeftNum.setText(powerList.get(1).getEnergy());
							mRightName.setText(powerList.get(2).getNickname());
							LApplication.loader.DisplayImage(
									ConstantValue.BASE_IMAGE_URL
											+ powerList.get(2).getPicture()
											+ ConstantValue.IMAGE_END,
									mRightHeader, R.drawable.default_header);
							mRightNum.setText(powerList.get(2).getEnergy());
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				break;
			case OVER_TAG:
				if (mPullToRefreshListView != null) {
					mPullToRefreshListView.onPullDownRefreshComplete();
					mPullToRefreshListView.onPullUpRefreshComplete();
				}
				break;

			default:
				break;
			}
		}

	};
	/**
	 * 能量总值
	 */
	private String powerStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nengliangzhi);
		powerStr = getIntent().getStringExtra("power");
		initview();
		initLisenter();
		initget();
		getEngineList();

	}

	private void initLisenter() {
		mBack.setOnClickListener(this);
	}

	private List<PowerBean> useWayHanderData(String result) {
		// TODO Auto-generated method stub
		return null;
	};

	/**
	 * 初始化view
	 */
	private void initview() {
		// TODO Auto-generated method stub
		mBack = (ImageView) findViewById(R.id.nengliangzhi_back);
		View headerview = LayoutInflater.from(PowerActivity.this).inflate(
				R.layout.power_headerview, null);
		mPowerNum = (TextView) headerview.findViewById(R.id.nengliangzhi_num);
		mLeftHrader = (CircleImageView) headerview
				.findViewById(R.id.nengliangzhi_left_header_photo);
		mLeftName = (TextView) headerview
				.findViewById(R.id.nengliangzhi_left_header_name);
		mLeftNum = (TextView) headerview
				.findViewById(R.id.nengliangzhi_left_header_num);
		mCenterHeader = (CircleImageView) headerview
				.findViewById(R.id.nengliangzhi_center_header_photo);
		mCenterName = (TextView) headerview
				.findViewById(R.id.nengliangzhi_center_header_name);
		mCenterNum = (TextView) headerview
				.findViewById(R.id.nengliangzhi_center_header_num);
		mRightHeader = (CircleImageView) headerview
				.findViewById(R.id.nengliangzhi_right_header_photo);
		mRightName = (TextView) headerview
				.findViewById(R.id.nengliangzhi_right_header_name);
		mRightNum = (TextView) headerview
				.findViewById(R.id.nengliangzhi_right_header_num);
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.nengliangzhi_listview);
		mListview = mPullToRefreshListView.getRefreshableView();
		mListview.addHeaderView(headerview);
		powerAdapter = new PowerAdapter(PowerActivity.this, powerList);
		mListview.setAdapter(powerAdapter);
		if (!Utils.isNullOrEmpty(powerStr)) {
			mPowerNum.setText("能量值：" + powerStr);
		}
		mPullToRefreshListView.setPullLoadEnabled(false);
		mPullToRefreshListView.setPullRefreshEnabled(false);
		mPullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 下拉刷新城市数据
						if (!UIUtils.isAvailable(PowerActivity.this)) {
							mPullToRefreshListView.onPullDownRefreshComplete();
						} else {
							MainActivity.instance.setLayoutVisible();
							// 下拉刷新加载数据
							loadRefreshData();
						}
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						if (!UIUtils.isAvailable(PowerActivity.this)) {
							mPullToRefreshListView.onPullUpRefreshComplete();
						} else {
							MainActivity.instance.setLayoutVisible();
							// 上拉刷新加载数据
							loadRefreshData();

						}
					}
				});
	}

	/**
	 * 上拉下拉数据加载
	 * 
	 * @param i
	 * @param j
	 */
	protected void loadRefreshData() {
		Message message = new Message();
		message.arg1 = OVER_TAG;
		handler.sendMessageDelayed(message, 1500);
		getEngineList();
	}

	/**
	 * 获取数据的view
	 */
	private void initget() {
		Intent intent = getIntent();
		mresource_id = intent.getStringExtra("id");
		mresource_type = intent.getStringExtra("type");
	}

	private void getEngineList() {
		Map<String, String> paramss = new HashMap<String, String>();
		paramss.put("resource_id", mresource_id);
		paramss.put("resource_type", mresource_type);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.GET_ENERGY_LIST, paramss, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if (handler == null)
							return;
						Log.e("dcc", "能量返回数据" + data.getObject());
						if (data != null && data.getNetResultCode() == 0) {
							Message msg = new Message();
							// msg.what = SUCCESS_SUMENGER_GIFT;
							msg.what = POWER_OK;
							msg.obj = data.getObject();
							handler.sendMessage(msg);
						}
					}
				}, false, false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.nengliangzhi_back:
			finish();
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		if(handler!=null)
			handler.removeCallbacksAndMessages(null);
		handler =null;
		if(powerList!=null)
			powerList.clear();
		powerList = null;
		mLeftHrader = null;
		mPullToRefreshListView = null;
		super.onDestroy();
	}

}
