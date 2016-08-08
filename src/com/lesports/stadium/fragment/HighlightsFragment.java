/**
 * ***************************************************************
 * @ClassName:  HighlightsFragment 
 * 
 * @Desc : TODO(这里用一句话描述这个类的作用)
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 司明
 * 
 * @data:2016-2-1 上午11:06:59
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */

package com.lesports.stadium.fragment;

import java.io.Serializable;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.VideoPlayerActivity;
import com.lesports.stadium.adapter.HeigthLightAdapter;
import com.lesports.stadium.base.BaseFragment;
import com.lesports.stadium.base.BaseV4Fragment;
import com.lesports.stadium.bean.HeightLightBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.pullrefreshview.PullToRefreshBase;
import com.lesports.stadium.pullrefreshview.PullToRefreshListView;
import com.lesports.stadium.pullrefreshview.PullToRefreshBase.OnRefreshListener;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.UIUtils;

/**
 * 
 * ***************************************************************
 * 
 * @Desc : 活动项内部的集锦
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
public class HighlightsFragment extends BaseV4Fragment {

	/**
	 * 视图view
	 */
	private View mView;
	/**
	 * 可下拉刷新的listview
	 */
	private PullToRefreshListView mPulltorefreshlistview;
	/**
	 * 加载数据的列表项
	 */
	private ListView mListview;
	/**
	 * 适配器
	 */
	private HeigthLightAdapter mAdapter;
	/**
	 * 用来标记网络获取的数据为空
	 */
	private final int TAG_DATANULL = 0;
	/**
	 * 用来标记网络获取的数据
	 */
	private final int TAG_DATA = 1;
	/**
	 * 界面跳转时传递过来的id
	 */
	private String mId;
	/**
	 * 处理数据的handler；
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case TAG_DATANULL:
				Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT)
						.show();
				break;
			case TAG_DATA:
				// 调用方法，解析数据
				String backdata = (String) msg.obj;
				if (!TextUtils.isEmpty(backdata)) {
					// 调用方法，解析数据
					jsondatajijing(backdata);
				} else {
					Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 构造方法
	 * @param id 活动id
	 */
	public HighlightsFragment(String id) {
		this.mId = id;
	}
	/**
	 * 无惨构造方法
	 */
	public HighlightsFragment() {
		super();
	}

	@Override
	public View initView(LayoutInflater inflater) {
		mView = inflater.inflate(R.layout.fragment_heightlight, null);
		mPulltorefreshlistview = (PullToRefreshListView) mView
				.findViewById(R.id.heightlight_fragment_listview);
		mListview = mPulltorefreshlistview.getRefreshableView();
		getdataFromServiceheight();
		mPulltorefreshlistview
				.setOnRefreshListener(new OnRefreshListener<ListView>() {

					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 网络异常
						if (UIUtils.isAvailable(getActivity())) {
							mPulltorefreshlistview.onPullDownRefreshComplete();
						} else {
							// 下拉刷新加载数据
							loadRefreshData(1, 1);

						}
					}

					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// 网络异常
						if (UIUtils.isAvailable(getActivity())) {
							mPulltorefreshlistview.onPullUpRefreshComplete();
						} else {
							// 上拉刷新加载数据
							loadRefreshData(0, 1);

						}
					}
				});
		return mView;
	}


	/**
	 * 需要解析的集锦数据
	 * 
	 * @param backdata
	 */
	private void jsondatajijing(String backdata) {
		List<HeightLightBean> list = new ArrayList<HeightLightBean>();
		try {
			JSONArray array = new JSONArray(backdata);
			int count = array.length();
			for (int i = 0; i < count; i++) {
				JSONObject obj = array.getJSONObject(i);
				HeightLightBean bean = new HeightLightBean();
				if (obj.has("activityId")) {
					bean.setActivityId(obj.getString("activityId"));
				}
				if (obj.has("fileName")) {
					bean.setFileName(obj.getString("fileName"));
				}
				if (obj.has("fileProfile")) {
					bean.setFileProfile(obj.getString("fileProfile"));
				}
				if (obj.has("fileSource")) {
					bean.setFileSource(obj.getString("fileSource"));
				}
				if (obj.has("fileType")) {
					bean.setFileType(obj.getString("fileType"));
				}
				if (obj.has("fileUrl")) {
					bean.setFileUrl(obj.getString("fileUrl"));
				}
				if (obj.has("id")) {
					bean.setId(obj.getString("id"));
				}
				if (obj.has("mImageUrl")) {
					bean.setmImageUrl(obj.getString("mImageUrl"));
				}
				if (obj.has("mTitle")) {
					bean.setmTitle(obj.getString("mTitle"));
				}
				if (obj.has("resourceId")) {
					bean.setResourceId(obj.getString("resourceId"));
				}
				if (obj.has("videoImageUrl")) {
					bean.setVideoImageUrl(obj.getString("videoImageUrl"));
				}
				if(obj.has("supportFullView")){
					bean.setSupportFullView(obj.getString("supportFullView"));
				}
				if("1".equals(bean.getFileType()))
					list.add(bean);
			}
			// 调用方法，将数据集添加到适配器中
			addDataToList(list, mListview);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	};

	/**
	 * 调用方法，从网络获集锦数据
	 * 
	 * @param context
	 */
	private void getdataFromServiceheight() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityId", mId);
		params.put("fileClass", "1");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.HIGHT_LEIGHT, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						if (data != null&& data.getNetResultCode()==0) {
							Message msg = new Message();
							msg.arg1 = TAG_DATA;
							msg.obj = data.getObject();
							handler.sendMessage(msg);
						} else {
							// 说明网络获取无数据
							Message msg = new Message();
							msg.arg1 = TAG_DATANULL;
							handler.sendMessage(msg);
						}
					}

				}, false, false);
	}

	/**
	 * 讲数据添加到列表项中
	 * 
	 * @2016-2-29下午3:14:15
	 */
	private void addDataToList(final List<HeightLightBean> list,
			ListView mListview2) {
		mAdapter = new HeigthLightAdapter(list, getActivity());
		mListview2.setAdapter(mAdapter);
		mListview2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(),
						VideoPlayerActivity.class);
				intent.putExtra("tag", "height");
				intent.putExtra("id", list.get(arg2).getId());
				intent.putExtra("heightlightbean", list.get(arg2));
				intent.putExtra("list", (Serializable) list);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onDestroy() {
		if (handler != null)
			handler.removeCallbacksAndMessages(null);
		mPulltorefreshlistview = null;
		mListview = null;
		mAdapter = null;
		System.gc();
		super.onDestroy();
	}
	
	/**
	 * 上拉下拉数据加载
	 * 
	 * @param i
	 * @param j
	 */
	protected void loadRefreshData(int i, int j) {
		Toast.makeText(getActivity(), "haha", 0).show();
	}

	@Override
	public void initListener() {
	}

	@Override
	public void initData(Bundle savedInstanceState) {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}
}
