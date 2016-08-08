package com.lesports.stadium.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.CrowdFundingDetailActivity;
import com.lesports.stadium.activity.MainActivity;
import com.lesports.stadium.adapter.CrowdFundingAdapter;
import com.lesports.stadium.base.BaseFragment;
import com.lesports.stadium.bean.AllChipsBean;
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
 * @Desc : 众筹界面
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
 * ***************************************************************
 */
public class AllChipsFragment extends BaseFragment implements OnClickListener{

	/**
	 * 界面视图
	 */
	private View  mView;
	/**
	 * 可下拉刷新的listviw
	 */
	private PullToRefreshListView mPulltorefreshlistview;
	/**
	 * 展示数据的列表项
	 */
	private ListView mListview;
	/**
	 * 顶部标题
	 */
	private TextView mTitle;
	/**
	 * 顶部右边按钮
	 */
	private TextView mRight;
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
	private final int ALL_TAG=3;
	/**
	 * 用于标示进行中的标记
	 */
	private final int ING_TAG=1;
	/**
	 * 用于标示未开始的标记
	 */
	private final int NOSTATRT_TAG=0;
	/**
	 * 用于表示已结束的标记
	 */
	private final int EDDED_TAG=2;
	/**
	 * 列表项的数据源
	 */
	private List<AllChipsBean> mList;
	/**
	 * 用于标记网络获取数据为空
	 */
	private final int NUMM_TAG=1;
	/**
	 * 用于标记网络数据不为空
	 */
	private final int DATA_ISHAVE=2;
	/**
	 * 下拉刷新数据
	 */
	private final int REFRESH=100;
	/**
	 * 上拉加载更多数据
	 */
	private final int LOAD_MORE=200;
	/**
	 * 刷新数据成功
	 */
	private final int REFRESH_SUCCESS=121;
	/**
	 * 刷新失败
	 */
	private final int REFRESH_END=122;
	/**
	 * 加载失败
	 */
	private final int LOAD_ERROR=202;
	/**
	 * 加载更多成功
	 */
	private final int LOAD_SUCCESS=201;
	/**
	 * 用来标记现有众筹的最后一个数据的id
	 */
	private String mNowDataLastId;
	/**
	 * 适配器
	 */
	private CrowdFundingAdapter mAdapter;
	/**
	 * 展示进度的布局
	 */
	private RelativeLayout mLayout_jindu;
	/**
	 * 进度条
	 */
	private ProgressBar mProgressBar;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case NUMM_TAG:
				Toast.makeText(getActivity(), "服务器异常，请稍后重试",Toast.LENGTH_SHORT).show();
				mLayout_jindu.setVisibility(View.GONE);
				break;
			case DATA_ISHAVE:
				String backdata=(String) msg.obj;
				if(backdata!=null&&!TextUtils.isEmpty(backdata)){
					mLayout_jindu.setVisibility(View.GONE);
					jsondataway(backdata);
				}
				break;
			case REFRESH:
				MainActivity.instance.setLayoutVisible();
				mPulltorefreshlistview.onPullDownRefreshComplete();
				break;
			case LOAD_MORE:
				MainActivity.instance.setLayoutVisible();
				mPulltorefreshlistview.onPullUpRefreshComplete();
				break;
			case REFRESH_SUCCESS:
				//用于刷新
				analyseRefreshData((String) msg.obj);
				break;
			case REFRESH_END:
				mPulltorefreshlistview.onPullDownRefreshComplete();
				break;
			case LOAD_SUCCESS:
				//用于家在更多
				analyseLoadMore((String) msg.obj);
				break;
			case LOAD_ERROR:
				//用于加载更多
				mPulltorefreshlistview.onPullUpRefreshComplete();
				break;
			default:
				break;
			}
			
		}
	};
	
	@Override
	public View initView(LayoutInflater inflater) {
		mView=inflater.inflate(R.layout.fragment_allchips,null);
		initviews();
		return mView;
	}

	/**
	 * 初始化view控件
	 * @2016-2-17下午5:18:37
	 */
	private void initviews() {
		mLayout_jindu=(RelativeLayout) mView.findViewById(R.id.layout_progress_layout_zhongchou);
		// TODO Auto-generated method stub
		mPulltorefreshlistview=(PullToRefreshListView) mView.findViewById(R.id.crowdfunding_listview);
		mPulltorefreshlistview.setPullLoadEnabled(true);
		mPulltorefreshlistview.setPullRefreshEnabled(true);
		mListview=mPulltorefreshlistview.getRefreshableView();
//		initlvDate();
		//调用方法。网络获取众筹列表数据
		userUtilsGetDataLIST(0);
		mPulltorefreshlistview
		.setOnRefreshListener(new OnRefreshListener<ListView>() {

			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 网络异常
				if (!UIUtils.isAvailable(getActivity())) {
					mPulltorefreshlistview.onPullDownRefreshComplete();
				} else {
					// 下拉刷新加载数据
					MainActivity.instance.setLayoutVisible();
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
					MainActivity.instance.setLayoutVisible();
					loadRefreshData_up(0, 1);

				}
			}
		});
		mListview.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					//开始滑动
					if(MainActivity.instance!=null){
						MainActivity.instance.setLayoutGone();
					}
					break;

					case OnScrollListener.SCROLL_STATE_FLING:
					//滑动中
					break;

					case OnScrollListener.SCROLL_STATE_IDLE:
					//滑动结束
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
	}
	/**
	 * 获取所有众筹数据
	 * @param i_tag
	 */
	private void userUtilsGetDataLIST(int i_tag) {
		if(i_tag==0){
			mLayout_jindu.setVisibility(View.VISIBLE);
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("crowdfundId", "");
		params.put("flag","");
		params.put("status","");
		params.put("rows","10");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ZHONGCHOU_LIST, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if (data == null) {
							// 说明网络获取无数据
							Message msg=new Message();
							msg.arg1=NUMM_TAG;
							handler.sendMessage(msg);
							
						} else {
							Object obj = data.getObject();
							if(obj==null){
								Message msg=new Message();
								msg.arg1=NUMM_TAG;
								handler.sendMessage(msg);
							}else{
								String backdata = obj.toString();
								if (data.getNetResultCode()!=0) {
									Message msg=new Message();
									msg.arg1=NUMM_TAG;
									handler.sendMessage(msg);
								} else {
									// 调用方法，给listview加载数据，先将数据通过handler发送到主线程中
									Message msg = new Message();
									msg.arg1=DATA_ISHAVE;
									msg.obj = backdata;
									handler.sendMessage(msg);
								}
							}
							
						}
					}
				}, false,false);
	}
	private void userUtilsGetDataLIST_shuaxin(final String str) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("crowdfundId",mNowDataLastId);
		params.put("flag","U");
		params.put("status","");
		params.put("rows","10");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.ZHONGCHOU_LIST, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						if (data == null) {
							// 说明网络获取无数据
							Message msg=new Message();
							msg.arg1=NUMM_TAG;
							handler.sendMessage(msg);
						} else {
							Object obj = data.getObject();
							if(obj==null){
								Message msg=new Message();
								msg.arg1=NUMM_TAG;
								handler.sendMessage(msg);
							}else{
								String backdata = obj.toString();
								if (backdata == null&&!TextUtils.isEmpty(backdata)) {
									Message msg=new Message();
									msg.arg1=NUMM_TAG;
									handler.sendMessage(msg);
								} else {
									// 调用方法，给listview加载数据，先将数据通过handler发送到主线程中
									if(data.getNetResultCode()==0){
										if(str.equals("U")){
											//说明是加载更多
											Message msg = new Message();
											msg.arg1=LOAD_SUCCESS;
											msg.obj = backdata;
											handler.sendMessage(msg);
										}else if(str.equals("D")){
											Message msg = new Message();
											msg.arg1=REFRESH_SUCCESS;
											msg.obj = backdata;
											handler.sendMessage(msg);
										}
										
									}else{
										if(str.equals("U")){
											//说明是加载更多
											Message msg = new Message();
											msg.arg1=LOAD_ERROR;
											handler.sendMessage(msg);
										}else if(str.equals("D")){
											Message msg = new Message();
											msg.arg1=REFRESH_END;
											handler.sendMessage(msg);
										}
									}
									
								}
							}
						}
					}
				}, false,false);
	}
	/**
	 * @param iconDown 
	 * @param mRight2
	 * @param string
	 * @author JZKJ-LWC
	 * @2016-1-29上午10:41:15
	 * 将传入的控件，字符串，图片，整合成一个完整的控件
	 */
	private void initRightTv(TextView mRight2, String string, int iconDown) {
		// TODO Auto-generated method stub
		Drawable drawable = getActivity().getResources().getDrawable(
				iconDown);
		drawable.setBounds(0, 3, 18, 15);
		mRight2.setCompoundDrawables(null, null,
				drawable, null);
		mRight2.setText(string);
		mRight2.setTextColor(Color.rgb(0, 124, 172));
		mRight2.setVisibility(View.VISIBLE);
		mRight2.setOnClickListener(this);
	}
	/**
	 * 下拉刷新数据
	 * 
	 * @param i
	 * @param j
	 */
	protected void loadRefreshData(int i, int j) {
		Message message=new Message();
		message.arg1=REFRESH;
		handler.sendMessageDelayed(message, 1500);
		userUtilsGetDataLIST(1);
	}
	/**
	 * 上拉加载更多
	 * @param i
	 * @param j
	 */
	protected void loadRefreshData_up(int i, int j) {
		Message message=new Message();
		message.arg1=LOAD_MORE;
		handler.sendMessageDelayed(message, 1500);
		userUtilsGetDataLIST_shuaxin("U");
	}
	/**
	 * 根据传入的控件资源id，以及坐标点，来弹出一个window
	 * @2016-1-29下午3:00:29
	 * @param x y 是为了定位，
	 * @author JZKJ-LWC
	 */
	public void showPopupWindow(int id,int x, int y) {
		mRightLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(
				R.layout.sence_dialog, null);
		
		mPopupWindow = new PopupWindow(getActivity());
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow
				.setWidth(getActivity().getWindowManager().getDefaultDisplay().getWidth() /3);
		mPopupWindow.setHeight(getActivity().getWindowManager().getDefaultDisplay().getWidth()/2);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setContentView(mRightLayout);
		 mPopupWindow.showAsDropDown(mView.findViewById(id), x, 10);
//		mPopupWindow.showAtLocation(mFragmentview.findViewById(id), Gravity.RIGHT
//				| Gravity.TOP, x, y);
		//初始化该布局内控件
		initWindowView(mRightLayout);
	}
	/**
	 * @2016-1-29下午3:26:05
	 * 获取该布局内控件，并且进行初始化操作
	 */
	private void initWindowView(LinearLayout mRightLayout2) {
		mRightAll=(TextView) mRightLayout2.findViewById(R.id.sence_window_all);
		mRightAll.setOnClickListener(this);
		mRightEnd=(TextView) mRightLayout2.findViewById(R.id.sence_window_end);
		mRightEnd.setOnClickListener(this);
		mRightIng=(TextView) mRightLayout2.findViewById(R.id.sence_window_ing);
		mRightIng.setOnClickListener(this);
		mRightNoStart=(TextView) mRightLayout2.findViewById(R.id.sence_window_nostart);
		mRightNoStart.setOnClickListener(this);
	}

	/**
	 * 将数据源添加到适配器以及列表项中
	 * @param list 
	 * @2016-2-26下午4:55:06
	 */
	private void addDataToListview(final List<AllChipsBean> list) {
		mAdapter=new CrowdFundingAdapter(list,getActivity());
		mListview.setAdapter(mAdapter);
		mListview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String status=list.get(arg2).getCrowdfundStatus();
				if(!TextUtils.isEmpty(status)){
					if(status.equals("2")){
						//为开始
						Intent intent=new Intent(getActivity(),CrowdFundingDetailActivity.class);
						intent.putExtra("bean",list.get(arg2));
						intent.putExtra("tag","2");
						startActivity(intent);
					}else if(status.equals("1")){
						//惊醒中
						// TODO Auto-generated method stub
						Intent intent=new Intent(getActivity(),CrowdFundingDetailActivity.class);
						intent.putExtra("bean",list.get(arg2));
						intent.putExtra("tag","1");
						startActivity(intent);
					}else if(status.equals("3")){
						//已结束
						Intent intent=new Intent(getActivity(),CrowdFundingDetailActivity.class);
						intent.putExtra("bean",list.get(arg2));
						intent.putExtra("tag","3");
						startActivity(intent);
					}
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_right_tv:
			//右侧按钮被点击，弹出window
			mRight.getTop();
			int y = mRight.getBottom() * 3 / 2;
			int x = getActivity().getWindowManager().getDefaultDisplay().getWidth() / 6;
			showPopupWindow(R.id.title_right_tv,x, y);
			break;
		case R.id.sence_window_all:
			//菜单项内全部
//			if(mList!=null&&mList.size()!=0){
//				mAdapter.setTagandData(ALL_TAG,mList);
//			}
//			mRight.setText(mRightAll.getText().toString());
//			mPopupWindow.dismiss();
			break;
		case R.id.sence_window_end:
			//菜单项内已结束
//			//调用方法，取出数据集合中已结束的活动数据
//			List<AllChipsBean> list_end=getdata_end();
//			if(list_end!=null&&list_end.size()!=0){
//				mAdapter.setTagandData(EDDED_TAG,list_end);
//			}
//			mRight.setText(mRightEnd.getText().toString());
//			mPopupWindow.dismiss();
			break;
		case R.id.sence_window_ing:
			//菜单项内正在进行中
//			List<AllChipsBean> list_ing=getdata_ing();
//			if(list_ing!=null&&list_ing.size()!=0){
//				mAdapter.setTagandData(ING_TAG,list_ing);
//			}
//			mRight.setText(mRightIng.getText().toString());
//			mPopupWindow.dismiss();
			break;
		case R.id.sence_window_nostart:
			//菜单项内未开始
//			List<AllChipsBean> list_nostart=getdata_nostart();
//			if(list_nostart!=null&&list_nostart.size()!=0){
//				mAdapter.setTagandData(NOSTATRT_TAG,list_nostart);
//			}
//			mRight.setText(mRightNoStart.getText().toString());
//			mPopupWindow.dismiss();
			break;
			

		default:
			break;
		}
	}
	/**
	 * 取出数据集中未开始的活动数据
	 * @2016-2-26下午5:14:50
	 */
	private List<AllChipsBean> getdata_nostart() {
		// TODO Auto-generated method stub
		List<AllChipsBean> list=new ArrayList<AllChipsBean>();
		if(mList!=null&&mList.size()!=0){
			for(int i=0;i<mList.size();i++){
				AllChipsBean bean=mList.get(i);
				if(bean.getCrowdfundStatus().equals("0")){
					list.add(bean);
				}
			}
		}
		return list;
	}
	/**
	 * 取出数据集中进行中的活动数据
	 * @2016-2-26下午5:14:50
	 */
	private List<AllChipsBean> getdata_ing() {
		// TODO Auto-generated method stub
		List<AllChipsBean> list=new ArrayList<AllChipsBean>();
		if(mList!=null&&mList.size()!=0){
			for(int i=0;i<mList.size();i++){
				AllChipsBean bean=mList.get(i);
				if(bean.getCrowdfundStatus().equals("1")){
					list.add(bean);
				}
			}
		}
		return list;
	}

	/**
	 * 取出数据集中已结束的活动数据
	 * @2016-2-26下午5:14:50
	 */
	private List<AllChipsBean> getdata_end() {
		List<AllChipsBean> list=new ArrayList<AllChipsBean>();
		if(mList!=null&&mList.size()!=0){
			for(int i=0;i<mList.size();i++){
				AllChipsBean bean=mList.get(i);
				if(bean.getCrowdfundStatus().equals("2")){
					list.add(bean);
				}
			}
		}
		return list;
	}
	/**
	 * 解析众筹数据
	 * @param backdata
	 */
	private void jsondataway(String backdata) {
		mList=new ArrayList<AllChipsBean>();
		try {
			JSONArray array=new JSONArray(backdata);
			int count=array.length();
			for(int i=0;i<count;i++){
				JSONObject obj=array.getJSONObject(i);
				AllChipsBean bean=new AllChipsBean();
				if(obj.has("beginTime")){
					bean.setBeginTime(obj.getString("beginTime"));
				}
				if(obj.has("crowdfundName")){
					bean.setCrowdfundName(obj.getString("crowdfundName"));
				}
				if(obj.has("crowdfundStatus")){
					bean.setCrowdfundStatus(obj.getString("crowdfundStatus"));
				}
				if(obj.has("endTime")){
					bean.setEndTime(obj.getString("endTime"));
				}
				if(obj.has("id")){
					bean.setId(obj.getString("id"));
				}
				if(obj.has("hasMoney")){
					bean.setHasMoney(obj.getString("hasMoney"));
				}
				if(obj.has("participation")){
					bean.setParticipation(obj.getString("participation"));
				}
				if(obj.has("propagatePicture")){
					bean.setPropagatePicture(obj.getString("propagatePicture"));
				}
				if(obj.has("targetMoney")){
					bean.setTargetMoney(obj.getString("targetMoney"));
				}
				mList.add(bean);
			}
			//调用方法，进行数据
			if(mList!=null&&mList.size()!=0){
				mNowDataLastId=mList.get(mList.size()-1).getId();
				addDataToListview(mList);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	};
	
	/**
	 * 用于刷新和加载数据的时候用
	 * @param backdata
	 */
	private List<AllChipsBean> jsondataway_sa(String backdata) {
		List<AllChipsBean> sList=new ArrayList<AllChipsBean>();
		try {
			JSONArray array=new JSONArray(backdata);
			int count=array.length();
			for(int i=0;i<count;i++){
				JSONObject obj=array.getJSONObject(i);
				AllChipsBean bean=new AllChipsBean();
				if(obj.has("beginTime")){
					bean.setBeginTime(obj.getString("beginTime"));
				}
				if(obj.has("crowdfundName")){
					bean.setCrowdfundName(obj.getString("crowdfundName"));
				}
				if(obj.has("crowdfundStatus")){
					bean.setCrowdfundStatus(obj.getString("crowdfundStatus"));
				}
				if(obj.has("endTime")){
					bean.setEndTime(obj.getString("endTime"));
				}
				if(obj.has("id")){
					bean.setId(obj.getString("id"));
				}
				if(obj.has("hasMoney")){
					bean.setHasMoney(obj.getString("hasMoney"));
				}
				if(obj.has("participation")){
					bean.setParticipation(obj.getString("participation"));
				}
				if(obj.has("propagatePicture")){
					bean.setPropagatePicture(obj.getString("propagatePicture"));
				}
				if(obj.has("targetMoney")){
					bean.setTargetMoney(obj.getString("targetMoney"));
				}
				sList.add(bean);
			}
			return sList;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return sList;
	};
	
	@Override
	public void onResume() {
		if(mList==null||mList.size()==0){
			userUtilsGetDataLIST(0);
		}
		super.onResume();
	}
	
	@Override
	public void onDestroy() {
		if(handler!=null)
			handler.removeCallbacksAndMessages(null);
		if(mList!=null){
			mList.clear();
			mList = null;
		}
		mPulltorefreshlistview = null;
		mAdapter = null;
		super.onDestroy();
	}
	/**
	 * 解析加载更多数据
	 */
	private void analyseLoadMore(String backdatass_ass){
		if(backdatass_ass!=null&&!TextUtils.isEmpty(backdatass_ass)){
			mPulltorefreshlistview.onPullUpRefreshComplete();
			//调用方法，解析数据
			List<AllChipsBean> list=jsondataway_sa(backdatass_ass);
			if(null!=list && list.size()>0){
				mNowDataLastId=list.get(list.size()-1).getId();
				mList.addAll(list);
				mAdapter.setTagandData(mList);
			}
		}
	}
	
	/**
	 * 解析下拉刷新数据
	 */
	private void analyseRefreshData(String backdatass){
		if(backdatass!=null&&!TextUtils.isEmpty(backdatass)){
			mPulltorefreshlistview.onPullDownRefreshComplete();
			//调用方法，解析数据
			List<AllChipsBean> list=jsondataway_sa(backdatass);
			mNowDataLastId=list.get(list.size()-1).getId();
			mAdapter.setTagandData(list);
		}
	}
	
	
	@Override
	public void initListener() {
	}

	@Override
	public void initData(Bundle savedInstanceState) {
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	}

	
	/**
	 * 初始化list的数据
	 * @2016-2-17下午5:19:53
	 */
//	private void initlvDate() {
//		// TODO Auto-generated method stub
//		mList=new ArrayList<AllChipsBean>();
//		int[] status={0,1,2,0,1,2,0,1,2,0,1,2,0,1,2,0,1,2,0,1};
//		for(int i=0;i<20;i++){
//			AllChipsBean bean=new AllChipsBean();
//			String str="测试数据"+i;
//			bean.setmName(str);
//			bean.setmStatus(String.valueOf(status[i]));
//			mList.add(bean);
//		}
//		addDataToListview();
//		
//	}

}
