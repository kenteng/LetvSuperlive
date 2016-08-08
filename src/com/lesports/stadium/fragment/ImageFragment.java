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
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lesports.stadium.R;
import com.lesports.stadium.activity.ImagePagerActivity;
import com.lesports.stadium.activity.VideoPlayerActivity;
import com.lesports.stadium.adapter.HeigthLightAdapter;
import com.lesports.stadium.adapter.ImageAdapter;
import com.lesports.stadium.adapter.VideoAdapter;
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
 * @Desc : 回顾图片
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
public class ImageFragment extends BaseV4Fragment {

	/**
	 * 视图view
	 */
	private View mView;
	/**
	 * 可下拉刷新的listview
	 */
	private PullToRefreshGridView mPulltorefreshlistview;
	/**
	 * 加载数据的列表项
	 */
	private GridView mGridView;
	/**
	 * 适配器
	 */
	private ImageAdapter mAdapter;
	/**
	 * 用来标记网络获取的数据为空
	 */
	private final int TAG_DATANULL = 0;
	/**
	 * 用来标记网络获取的数据
	 */
	private final int TAG_DATA = 1;
	/**
	 * 用来标记网络获取的数据为空
	 */
	private final int OVER_TAG = 40;
	/**
	 * 用来标记网络获取的数据为空
	 */
	private final int TAG_DATANULL_down = 10;
	/**
	 * 用来标记网络获取的数据为空
	 */
	private final int TAG_DATANULL_up = 20;
	/**
	 * 用来标记网络获取的数据
	 */
	private final int TAG_DATA_DOWN = 11;
	/**
	 * 用来标记网络获取的数据
	 */
	private final int TAG_DATA_UP = 111;
	/**
	 * 界面跳转时传递过来的id
	 */
	private String mId;
	/**
	 * 存放数据的集合
	 */
	private List<HeightLightBean> mListData=new ArrayList<>();
	/**
	 * 刷新数据时候的页码
	 */
	private int PAGE_NUM=2;
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
					mListData=jsondatajijing(backdata);
					// 调用方法，将数据集添加到适配器中
					List<HeightLightBean> lists=useWayCheckData(mListData);
					addDataToList(lists, mGridView);
				} else {
					Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case TAG_DATANULL_down:
				//下拉刷新没有数据
				mPulltorefreshlistview.onRefreshComplete();
				break;
			case TAG_DATANULL_up:
				//上拉加载没有数据
				mPulltorefreshlistview.onRefreshComplete();
				break;
			case TAG_DATA_DOWN:
				String backdata_down = (String) msg.obj;
				if (!TextUtils.isEmpty(backdata_down)) {
					// 调用方法，解析数据
					mListData=jsondatajijing(backdata_down);
					// 调用方法，将数据集添加到适配器中
					List<HeightLightBean> lists_down=useWayCheckData(mListData);
					addDataToList(lists_down, mGridView);
				} else {
					Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case TAG_DATA_UP:
				//上拉加载有数据的情况
				String backdata_up = (String) msg.obj;
				if (!TextUtils.isEmpty(backdata_up)) {
					// 调用方法，解析数据
					List<HeightLightBean> list=jsondatajijing(backdata_up);
					mListData.addAll(list);
					// 调用方法，将数据集添加到适配器中
					List<HeightLightBean> lists_down=useWayCheckData(mListData);
					addDataToList(lists_down, mGridView);
					PAGE_NUM++;
				} else {
					Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case OVER_TAG:
				mPulltorefreshlistview.onRefreshComplete();
				mPulltorefreshlistview.onRefreshComplete();
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
	public ImageFragment(String id) {
		this.mId = id;
	}
	/**
	 * 无惨构造方法
	 */
	public ImageFragment() {
		super();
	}

	@Override
	public View initView(LayoutInflater inflater) {
		mView = inflater.inflate(R.layout.fragment_image, null);
		mPulltorefreshlistview = (PullToRefreshGridView) mView
				.findViewById(R.id.image_servicegoods_gridview);
		
		mGridView = mPulltorefreshlistview.getRefreshableView();
		getdataFromServiceheight();
		mPulltorefreshlistview
		.setOnRefreshListener(new OnRefreshListener2<GridView>() {

			@Override
			public void onPullDownToRefresh(
					com.handmark.pulltorefresh.library.PullToRefreshBase<GridView> refreshView) {
				// TODO Auto-generated method stub
				// 下拉刷新加载数据
				loadRefreshData(1, 1);
			}

			@Override
			public void onPullUpToRefresh(
					com.handmark.pulltorefresh.library.PullToRefreshBase<GridView> refreshView) {
				// TODO Auto-generated method stub
				// 上拉刷新加载数据
				loadRefreshDataadd(PAGE_NUM);
			}

		});
		return mView;
	}


	/**
	 * 需要解析的集锦数据
	 * 
	 * @param backdata
	 */
	private List<HeightLightBean>  jsondatajijing(String backdata) {
		List<HeightLightBean> list = new ArrayList<HeightLightBean>();
		try {
			JSONObject objss=new JSONObject(backdata);
			JSONArray array = objss.getJSONArray("data");
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
				if (obj.has("uploadDate")) {
					bean.setUploadDate(obj.getString("uploadDate"));
				}
				list.add(bean);
			}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;

	};
	/**
	 * 从集锦数据里面分拣出需要用的视频数据
	 * @param list
	 * @return
	 */
	private List<HeightLightBean> useWayCheckData(List<HeightLightBean> list) {
		// TODO Auto-generated method stub
		List<HeightLightBean> lists=new ArrayList<>();
		if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
				if(list.get(i).getFileType().equals("1")){
					lists.add(list.get(i));
				}
			}
		}
		return list;
	}
	/**
	 * 调用方法，从网络获集锦数据
	 * 
	 * @param context
	 */
	private void getdataFromServiceheight() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityId", mId);
		params.put("fileType", "0");
		params.put("page", "1");
		params.put("rows", "6");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.GET_RETURN_VIDEO_IMAGE, params, new GetDatas() {
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
			GridView mGridView2) {
		if(mAdapter!=null){
			mAdapter.setList(list);
			mAdapter.notifyDataSetChanged();
		}else{
			mAdapter = new ImageAdapter(list, getActivity());
			mGridView2.setAdapter(mAdapter);
		}
		mGridView2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(),
						ImagePagerActivity.class);
				intent.putExtra("tag", "height");
				intent.putExtra("id", list.get(arg2).getId());
				intent.putExtra("audioId", list.get(arg2).getResourceId());
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
		mGridView = null;
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
		Message message = new Message();
		message.arg1 = OVER_TAG;
		handler.sendMessageDelayed(message, 1500);
		getdataFromServiceheight_refrensh(6);
	}
	/**
	 * 上拉下拉数据加载
	 * 
	 * @param i
	 * @param j
	 */
	protected void loadRefreshDataadd(int j) {
		Message message = new Message();
		message.arg1 = OVER_TAG;
		handler.sendMessageDelayed(message, 1500);
		getdataFromServiceheight_loadmore(PAGE_NUM);
	}
	/**
	 * 调用方法，从网络获集锦数据
	 * 
	 * @param context
	 */
	private void getdataFromServiceheight_refrensh(int rows) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityId", mId);
		params.put("fileType", "0");//0图片1视频
		params.put("page", "1");
		params.put("rows", "6");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.GET_RETURN_VIDEO_IMAGE, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						if (data != null&& data.getNetResultCode()==0) {
							Message msg = new Message();
							msg.arg1 = TAG_DATA_DOWN;
							msg.obj = data.getObject();
							handler.sendMessage(msg);
						} else {
							// 说明网络获取无数据
							Message msg = new Message();
							msg.arg1 = TAG_DATANULL_down;
							handler.sendMessage(msg);
						}
					}

				}, false, false);
	}
	/**
	 * 调用方法，从网络获集锦数据
	 * 
	 * @param context
	 */
	private void getdataFromServiceheight_loadmore(int page) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityId", mId);
		params.put("fileType", "0");//0图片1视频
		params.put("page", page+"");
		params.put("rows", "6");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.GET_RETURN_VIDEO_IMAGE, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						if (data != null&& data.getNetResultCode()==0) {
							Message msg = new Message();
							msg.arg1 = TAG_DATA_UP;
							msg.obj = data.getObject();
							handler.sendMessage(msg);
						} else {
							// 说明网络获取无数据
							Message msg = new Message();
							msg.arg1 = TAG_DATANULL_up;
							handler.sendMessage(msg);
						}
					}

				}, false, false);
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
