package com.lesports.stadium.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lesports.stadium.R;
import com.lesports.stadium.adapter.WodeyuyueListviewAdapter;
import com.lesports.stadium.bean.MyYuyueBean;
import com.lesports.stadium.bean.SenceBean;
import com.lesports.stadium.bean.YuYueActivityBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.view.CustomDialog;

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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * ***************************************************************
 * 
 * @ClassName: MineFragment
 * 
 * @Desc : 我的预约
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : lwc
 * 
 * @data:2016-1-29 上午11:38:42
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class MySubscribeActivity extends Activity implements OnClickListener {
	/**
	 * 列表项控件
	 */
	private ListView mysubscribe;
	/**
	 * 未登陆提示按钮
	 */
	private CustomDialog exitDialog;
	/**
	 * 无数据时候显示的布局
	 */
	private RelativeLayout mLayout_nodata;
	/**
	 * 去活动看看按钮
	 */
	private TextView mGotoActivity;
	/**
	 * 适配器
	 */
	private WodeyuyueListviewAdapter mAdapter;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case 1:
				String backdata = (String) msg.obj;
				if (!TextUtils.isEmpty(backdata)) {
					mysubscribe.setVisibility(View.VISIBLE);
					mLayout_nodata.setVisibility(View.GONE);
					// 调用方法，进行解析
					List<MyYuyueBean> list = useWayJsonData(backdata);
					if (list != null && list.size() != 0) {
						adddatatolist(list);
					}
				} else {
					mysubscribe.setVisibility(View.GONE);
					mLayout_nodata.setVisibility(View.VISIBLE);
				}
				break;
			case 2:
				mysubscribe.setVisibility(View.GONE);
				mLayout_nodata.setVisibility(View.VISIBLE);
				break;

			default:
				break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mysubscribe);
		initview();
	}

	private void initview() {
		mGotoActivity = (TextView) findViewById(R.id.layout_meiyouyuyue_xianshi_xiaolian_ss);
		mGotoActivity.setOnClickListener(this);
		mLayout_nodata = (RelativeLayout) findViewById(R.id.layout_meiyouyuyue_xianshi);
		findViewById(R.id.back).setOnClickListener(this);
		mysubscribe = (ListView) findViewById(R.id.mysubscribe);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 调用方法，获取活动列表数据
		String isdenglu = GlobalParams.USER_ID;
		if (!TextUtils.isEmpty(isdenglu)) {
			// 获取
			huoquyonghuyuyueliebiao_intent();
		} else {
			mysubscribe.setVisibility(View.GONE);
			mLayout_nodata.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 把数据加载到列表项中
	 * 
	 * @param list
	 */
	private void adddatatolist(final List<MyYuyueBean> list) {
		// TODO Auto-generated method stub
		mAdapter = new WodeyuyueListviewAdapter(MySubscribeActivity.this, list);
		mysubscribe.setAdapter(mAdapter);
		mysubscribe.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				SenceBean bean = list.get(arg2).getmSenceBean();
				String status = bean.getcStatus();
				String id = bean.getId();
				Log.i("现在的状态是", status);
				Log.i("现在的id是", id);
				if (status.equals("0")) {
					// 说明是未开始
					Intent intents = new Intent(MySubscribeActivity.this,
							NoActionActivity.class);
					intents.putExtra("tag", "yuyue");
					intents.putExtra("bean", list.get(arg2).getmSenceBean());
					startActivity(intents);
				} else {
					Intent intent = new Intent(MySubscribeActivity.this,
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
	 * @param mListview2
	 * @2016-2-18下午2:24:29 huoqu bao ming liebiao
	 */
	private void huoquyonghuyuyueliebiao_intent() {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("detailed", "1");
		params.put("finished", "0");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.YONGHUYUYUE_LIST, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						// TODO Auto-generated method stub
						if (data == null) {
							// 说明网络获取无数据
						} else {
							Object obj = data.getObject();
							if (obj == null) {

							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
								} else {
									if (handler == null)
										return;
									// 调用方法，给listview加载数据，先将数据通过handler发送到主线程中
									if (data.getNetResultCode() == 0) {
										Message msg = new Message();
										msg.arg1 = 1;
										msg.obj = backdata;
										handler.sendMessage(msg);
									} else {
										Message msg = new Message();
										msg.arg1 = 2;
										handler.sendMessage(msg);
									}

								}
							}
						}
					}
				}, false, false);
	}

	/**
	 * 提示用户进行登录的dialog
	 */
	private void createDialog() {
		exitDialog = new CustomDialog(this, new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(MySubscribeActivity.this,
						LoginActivity.class);
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
		exitDialog.setConfirmTxt("立即登录");
		exitDialog.setRemindTitle("温馨提示");
		exitDialog.setRemindMessage("登录之后才能进行支付哦~");
		exitDialog.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.layout_meiyouyuyue_xianshi_xiaolian_ss:
			// 调用方法，进入首界面，展示活动列表项
			MainActivity.instance.seeActivity();
			finish();
			MineActivity.instance.finish();
			break;

		default:
			break;
		}

	}

	/**
	 * 解析预约列表数据
	 * 
	 * @param backdata
	 * @return
	 */
	private List<MyYuyueBean> useWayJsonData(String backdata) {
		// TODO Auto-generated method stub
		List<MyYuyueBean> list = new ArrayList<MyYuyueBean>();
		try {
			JSONArray array = new JSONArray(backdata);
			int count = array.length();
			for (int i = 0; i < count; i++) {
				JSONObject obj = array.getJSONObject(i);
				MyYuyueBean bean = new MyYuyueBean();
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
				if (obj.has("activity")) {
					SenceBean beans = new SenceBean();
					JSONObject objs = obj.getJSONObject("activity");
					if (objs.has("camptype")) {
						beans.setCamptype(objs.getString("camptype"));
					}
					if (objs.has("cHeat")) {
						beans.setcHeat(objs.getString("cHeat"));
					}
					if (objs.has("id")) {
						beans.setId(objs.getString("id"));
					}
					if (objs.has("creattime")) {
						beans.setCreattime(objs.getString("creattime"));
					}
					if (objs.has("cStatus")) {
						beans.setcStatus(objs.getString("cStatus"));
					}
					if (objs.has("frontCoverImageURL")) {
						beans.setFrontCoverImageURL(objs
								.getString("frontCoverImageURL"));
					}
					if (objs.has("subhead")) {
						beans.setSubhead(objs.getString("subhead"));
					}
					if (objs.has("endtime")) {
						beans.setEndtime(objs.getString("endtime"));
					}
					if (objs.has("fileId")) {
						beans.setFileId(objs.getString("fileId"));
					}
					if (objs.has("title")) {
						beans.setmTitle(objs.getString("title"));
					}
					if (objs.has("selltimeend")) {
						beans.setSelltimeend(objs.getString("selltimeend"));
					}
					if (objs.has("starttime")) {
						beans.setStarttime(objs.getString("starttime"));
					}
					if (objs.has("tips")) {
						beans.setTips(objs.getString("tips"));
					}
					if (objs.has("venueId")) {
						beans.setVenueId(objs.getString("venueId"));
					}
					if (objs.has("label")) {
						beans.setLabel(objs.getString("label"));
					}
					if (objs.has("summary")) {
						beans.setSummary(objs.getString("summary"));
					}
					if (objs.has("fileUrl")) {
						beans.setFileUrl(objs.getString("fileUrl"));
					}
					if (objs.has("venueName")) {
						beans.setVenueName(objs.getString("venueName"));
					}
					if (objs.has("backgroudImageURL")) {
						beans.setBackgroudImageURL(objs
								.getString("backgroudImageURL"));
					}
					if (objs.has("resourceId")) {
						beans.setResourceId(objs.getString("resourceId"));
					}

					bean.setmSenceBean(beans);

				}
				list.add(bean);
			}
			return list;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	@Override
	protected void onDestroy() {
		if (handler != null) {
			handler.removeCallbacksAndMessages(null);
			handler = null;
		}
		mAdapter = null;
		mysubscribe = null;
		super.onDestroy();
	}
}
