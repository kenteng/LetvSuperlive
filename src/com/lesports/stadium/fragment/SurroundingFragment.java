package com.lesports.stadium.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lesports.stadium.R;
import com.lesports.stadium.activity.GoodsDetailActivity;
import com.lesports.stadium.adapter.RoundAdapter;
import com.lesports.stadium.adapter.RoundsAdapter;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.base.BaseV4Fragment;
import com.lesports.stadium.bean.RoundGoodsBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

/**
 * ***************************************************************
 * 
 * @ClassName: SurroundingFragment
 * 
 * @Desc : TODO(展示周边商品的fragment)
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwc
 * 
 * @data:2016-2-1 上午11:07:48
 * 
 * @Version : v1.0
 * 
 * @Modify : null
 *         ***************************************************************
 */
public class SurroundingFragment extends BaseV4Fragment {

	/**
	 * 支持Gridview 能给下啦刷新的控件
	 */
	private PullToRefreshGridView mGridviewPullToRefreshGridView;
	/**
	 * 表格控件
	 */
	private GridView mGridView;
	/**
	 * 活动id  标签
	 */
	private String id, no, lable;
	/**
	 * GridView 适配器
	 */
	public RoundsAdapter adapter;
	/**
	 * 存储商品实体类 的结合
	 */
	public List<RoundGoodsBean> list;
	/**
	 * 获取网络数据成功
	 */
	private final int GETDATE_SUCCESS = 1;
	/**
	 * 下啦刷新成功
	 */
	private final int REFRESH_OK = 100;
	/**
	 * 推迟1秒后 发送刷新成功
	 */
	private final int DELATE_SECOND = 1000;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case GETDATE_SUCCESS:
				String backdata = (String) msg.obj;
				if (!TextUtils.isEmpty(backdata)) {
					jsonData(backdata, getActivity());
				}
				break;
			case REFRESH_OK:
				mGridviewPullToRefreshGridView.onRefreshComplete();
				break;
			default:
				break;
			}
			initadapterorlistview(list, getActivity());
		};
	};


	/**
	 * 有参构造方法
	 * 
	 * @param id
	 *            当前活动id
	 * @param no
	 * @param lable
	 *            当前活动标签
	 */
	public SurroundingFragment(String id, String no, String lable) {
		this.id = id;
		this.no = no;
		this.lable = lable;
	}

	/**
	 * 无惨构造方法
	 */
	public SurroundingFragment() {
		super();
	}

	@Override
	public View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_srounding, null);
		initviews(view);
		getdataFromService(getActivity(), id, no, lable);
		return view;
	}

	/**
	 * 调用方法，从网络获取周边商品数据
	 * 
	 * @param context
	 */
	private void getdataFromService(final Context context, String id,
			String no, String lable) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityId", id);
		params.put("pageNo", "1");
		params.put("rows", "20");
		params.put("label", "演唱会");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.SROUNDING_LIST_DATA, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if(handler==null)
							return;
						if (data != null && data.getNetResultCode() == 0&&handler !=null) {
							Message message = new Message();
							message.arg1 = GETDATE_SUCCESS;
							message.obj = data.getObject();
							handler.sendMessage(message);
						}
					}

				}, false, false);
	}

	/**
	 * 解析如下数据，并且将至存储到集合当中
	 * 
	 * @param backdata
	 * @param context
	 */
	private void jsonData(String backdata, Context context) {
		list = new ArrayList<RoundGoodsBean>();
		try {
			JSONArray array = new JSONArray(backdata);
			int count = array.length();
			for (int i = 0; i < count; i++) {
				JSONObject objs = array.getJSONObject(i);
				RoundGoodsBean bean = new RoundGoodsBean();
				if (objs.has("bigimg")) {
					bean.setBigimg(objs.getString("bigimg"));
				}
				if (objs.has("cId")) {
					bean.setcId(objs.getString("cId"));
				}
				if (objs.has("classicId")) {
					bean.setClassicId(objs.getString("classicId"));
				}
				if (objs.has("classicName")) {
					bean.setClassicName(objs.getString("classicName"));
				}
				if (objs.has("createTime")) {
					bean.setCreateTime(objs.getString("createTime"));
				}
				if (objs.has("id")) {
					bean.setId(objs.getString("id"));
				}
				if (objs.has("freight")) {
					bean.setFreight(objs.getString("freight"));
				}
				if (objs.has("goodsName")) {
					bean.setGoodsName(objs.getString("goodsName"));
				}
				if (objs.has("label")) {
					bean.setLabel(objs.getString("label"));
				}
				if (objs.has("mediumImg")) {
					bean.setMediumImg(objs.getString("mediumImg"));
				}
				if (objs.has("parentId")) {
					bean.setParentId(objs.getString("parentId"));
				}
				if (objs.has("price")) {
					bean.setPrice(objs.getString("price"));
				}
				if (objs.has("priceunit")) {
					bean.setPriceunit(objs.getString("priceunit"));
				}
				if (objs.has("referPrice")) {
					bean.setReferPrice(objs.getString("referPrice"));
				}
				if (objs.has("sales")) {
					bean.setSales(objs.getString("sales"));
				}
				if (objs.has("sellerAddress")) {
					bean.setSellerAddress(objs.getString("sellerAddress"));
				}
				if (objs.has("sellerName")) {
					bean.setSellerName(objs.getString("sellerName"));
				}
				if (objs.has("smallImg")) {
					bean.setSmallImg(objs.getString("smallImg"));
				}
				if (objs.has("status")) {
					bean.setStatus(objs.getString("status"));
				}
				if (objs.has("stock")) {
					bean.setStock(objs.getString("stock"));
				}
				if(objs.has("pickup_address")){
					bean.setPickup_address(objs.getString("pickup_address"));
				}
				if(objs.has("pickup_remark")){
					bean.setPickup_remark(objs.getString("pickup_remark"));
				}
				list.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// 调用方法，将数据加载到适配器中
		if(handler!=null)
			handler.sendEmptyMessage(0);

	}

	private void initadapterorlistview(final List<RoundGoodsBean> list,
			Context context) {
		if (!TextUtils.isEmpty(GlobalParams.USER_ID)) {
			adapter = new RoundsAdapter(list, getActivity(), true, 0);
		} else {
			adapter = new RoundsAdapter(list, getActivity(), false, 0);
		}
		mGridView.setAdapter(adapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				GlobalParams.Goods_Address = list.get(arg2).getPickup_address();
				GlobalParams.Goods_Store_Name = list.get(arg2).getSellerName();
				GlobalParams.Goods_Store_Phone = list.get(arg2).getTelephone();
				Intent intent = new Intent(getActivity(),
						GoodsDetailActivity.class);
				intent.putExtra("id", list.get(arg2).getId());
				intent.putExtra("bean", list.get(arg2));
				intent.putExtra("tag", "round");
				startActivity(intent);
			}
		});
	}

	/**
	 *
	 * 初始化控件
	 */
	private void initviews(View view) {
		mGridviewPullToRefreshGridView = (PullToRefreshGridView) view
				.findViewById(R.id.round_gridview);
		mGridView = mGridviewPullToRefreshGridView.getRefreshableView();
		// Set a listener to be invoked when the list should be refreshed.
		mGridviewPullToRefreshGridView
				.setOnRefreshListener(new OnRefreshListener2<GridView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						// 调用方法，进行刷新数据
						useWayRefrenshData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						useWayAddMoreData();
					}

				});

	}

	/**
	 * 加载数据的时候调用的方法
	 */
	private void useWayAddMoreData() {
		Message message = new Message();
		message.arg1 = REFRESH_OK;
		handler.sendMessageDelayed(message, DELATE_SECOND);
	}

	/**
	 * 刷新数据的时候调用的方法
	 */
	private void useWayRefrenshData() {
		Message message = new Message();
		message.arg1 = REFRESH_OK;
		handler.sendMessageDelayed(message, DELATE_SECOND);
	}

	@Override
	public void onResume() {
		super.onResume();
		List<RoundGoodsBean> list = LApplication.dbBuyCar.findByCondition(
				"buy_mUsename= ?", new String[] { GlobalParams.USER_ID }, null);
		if (list.size() == 0) {
			if (adapter != null) {
				adapter.setBoolean(false);
			}
		} else {
			if (adapter != null) {
				adapter.setBoolean(true);
			}
		}
	}

	@Override
	public void onDestroy() {
		if (handler != null){
			handler.removeCallbacksAndMessages(null);
			handler = null;
		}
		if (list != null) {
			list.clear();
			list = null;
		}
		adapter = null;
		mGridviewPullToRefreshGridView = null;
		mGridView = null;
		System.gc();
		super.onDestroy();
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
