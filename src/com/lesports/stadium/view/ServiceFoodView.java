/**
 * 
 */
package com.lesports.stadium.view;

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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.OnlineCateringDetailActivity;
import com.lesports.stadium.adapter.ServiceFoodAdapter;
import com.lesports.stadium.bean.ServiceFoodBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.pullrefreshview.PullToRefreshBase;
import com.lesports.stadium.pullrefreshview.PullToRefreshBase.OnRefreshListener;
import com.lesports.stadium.pullrefreshview.PullToRefreshListView;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * ***************************************************************
 * 
 * @ClassName: ServiceFoodView
 * 
 * @Desc : TODO服务内的餐饮部分首页
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwc
 * 
 * @data:2016-2-14 下午5:12:05
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
@ResID(id = R.layout.layout_footview)
public class ServiceFoodView extends BaseView {
	/**
	 * 展示附近餐饮数据的可刷新listview
	 */
	@ResID(id = R.id.servicefoodview_listview)
	private PullToRefreshListView mPullToRefreshListview;
	/**
	 * 展示数据的listview
	 */
	private ListView mListview;
	/**
	 * listview的数据适配器对象
	 */
	private ServiceFoodAdapter mAdapter;
	/**
	 * 用来标记网络获取的数据为空
	 */
	private final int TAG_DATANULL = 0;
	/**
	 * 用来标记网络获取的数据
	 */
	private final int TAG_DATA = 1;
	/**
	 * @param context
	 */
	/**
	 * 上下文对象
	 */
	private Context context;
	
	private CustomDialog exitDialog;
	/**
	 * 该标记用来标注获取数据的页码
	 */
	private int PAGE_NUM=2;
	/**
	 * 刷新完成  失败或者成功
	 */
	private final int REFRESH100=100;
	private final int REFRESH111=111;
	private final int REFRESH1200=1200;
	private final int REFRESH222=222;
	/**
	 * 下拉刷新数据
	 */
	private final int GETDATAFROMSERVICE_DOWN=110;

	/**
	 * 上拉刷新数据
	 */
	private final int GETDATAFROMSERVICE_UP=220;
	/**
	 * 处理数据的handler；
	 */
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case TAG_DATANULL:
				break;
			case TAG_DATA:
				String backdata = (String) msg.obj;
				if (backdata != null && backdata.length() != 0) {
					// 调用方法，解析数据
					jsonDataOfString(backdata);
				}
				break;
			case REFRESH100:
				mPullToRefreshListview.onPullDownRefreshComplete();
				break;
			case REFRESH111:
				mPullToRefreshListview.onPullDownRefreshComplete();
				break;
			case GETDATAFROMSERVICE_DOWN:
				String backdatass = (String) msg.obj;
				if(!TextUtils.isEmpty(backdatass)){
					mPullToRefreshListview.onPullDownRefreshComplete();
					List<ServiceFoodBean> list=jsonDataOfString_refrensh(backdatass);
					if(list!=null&&list.size()!=0){
						if(mAdapter==null){
							initListview(context, list);
						}else{
							mAdapter.setList(list);
						}
					}
				}
				break;
			case REFRESH1200:
				mPullToRefreshListview.onPullUpRefreshComplete();
				break;
			case REFRESH222:
				mPullToRefreshListview.onPullUpRefreshComplete();
				break;
			case GETDATAFROMSERVICE_UP:
				String backdatassss = (String) msg.obj;
				if(!TextUtils.isEmpty(backdatassss)){
					mPullToRefreshListview.onPullUpRefreshComplete();
					PAGE_NUM++;
					List<ServiceFoodBean> list=jsonDataOfString_refrensh(backdatassss);
					if(list!=null&&list.size()!=0){
						mAdapter.addList(list);
					}
				}
				break;

			default:
				break;
			}
		}
	};
	public ServiceFoodView(final Context context) {
		super(context);
		this.context=context;
		initviews(context);
		getdataFromService();
	}

	
	/**
	 * @param context 
	 * @2016-2-16上午10:42:21
	 * 初始化控件view
	 */
	private void initviews(final Context context) {
		mListview=mPullToRefreshListview.getRefreshableView();
		mPullToRefreshListview.setPullLoadEnabled(true);
		mPullToRefreshListview.setPullRefreshEnabled(true);
		mPullToRefreshListview
		.setOnRefreshListener(new OnRefreshListener<ListView>() {

			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 网络异常
				if (!UIUtils.isAvailable(context) ){
					mPullToRefreshListview.onPullDownRefreshComplete();
				} else {

					// 下拉刷新加载数据
					loadRefreshData_down(1, 1,context);

				}
			}

			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 网络异常
				if (!UIUtils.isAvailable(context)) {
					mPullToRefreshListview.onPullUpRefreshComplete();
				} else {
					// 上拉刷新加载数据
					loadRefreshData_up(0, 1,context);

				}
			}
		});
	}
	/**
	 * 上拉下拉数据加载
	 * 
	 * @param i
	 * @param j
	 * @param context 
	 */
	protected void loadRefreshData_down(int i, int j, Context context) {
		Message message=new Message();
		message.arg1=100;
		handler.sendMessageDelayed(message, 1500);
		getdataFromService_down();
	}
	/**
	 * 上拉下拉数据加载
	 * 
	 * @param i
	 * @param j
	 * @param context 
	 */
	protected void loadRefreshData_up(int i, int j, Context context) {
		Message message=new Message();
		message.arg1=200;
		handler.sendMessageDelayed(message, 1500);
		getdataFromService_up(PAGE_NUM);
	}

	/**
	 * @param context 
	 * @param list 
	 * @2016-2-16上午10:44:06
	 * 初始化界面listview
	 */
	private void initListview(final Context context, final List<ServiceFoodBean> list) {
		mAdapter=new ServiceFoodAdapter(list,context);
		mListview.setAdapter(mAdapter);
		mListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				MobclickAgent.onEvent(context,"SellerListPage,");
				//首先需要拿着现在的事件去和店铺的营业时间进行比较
				GlobalParams.Online_Address=list.get(arg2).getAddress();
				GlobalParams.Online_Name=list.get(arg2).getCompanyname();
				GlobalParams.Online_Phone=list.get(arg2).getTelephone();
				Date date = new Date();
				long currenttimes=date.getTime();
				String currenttimesss=getChangeToTime(currenttimes+"");
				String currenttime_hour=getHourAndMinute(currenttimesss);
				//分别截取时间的小时数来进行比较和计算
				String current=getHourNum(currenttime_hour);
				if(!TextUtils.isEmpty(list.get(arg2).getStartTime())){
					String start=getHourNum(list.get(arg2).getStartTime());
					if(!TextUtils.isEmpty(list.get(arg2).getEndTime())){
						String end=getHourNum(list.get(arg2).getEndTime());
						int starts=Integer.parseInt(start);
						int ends=Integer.parseInt(end);
						int curr=Integer.parseInt(current);
						//调用方法，从该毫秒值上面截取小时数
						if(starts<=curr&&curr<=ends){
							Intent intent=new Intent(context, OnlineCateringDetailActivity.class);
							intent.putExtra("id",list.get(arg2).getId());
							intent.putExtra("title",list.get(arg2).getCompanyname());
							intent.putExtra("stime",list.get(arg2).getStartTime());
							intent.putExtra("etime",list.get(arg2).getEndTime());
							context.startActivity(intent);
						}else{
							createDialog(list.get(arg2).getStartTime(),list.get(arg2).getEndTime());
						}
					}
				}
			}
		});
	}
	/**
	 * 根据传入时间来截取小时数
	 * @param currenttime_hour
	 * @return
	 */
	private String getHourNum(String currenttime_hour) {
		String hms="";
		if(!TextUtils.isEmpty(currenttime_hour)){
			hms=currenttime_hour.substring(0,2);
			String hmss=hms.substring(0,1);
			if(hmss.equals("0")){
				hms=hms.substring(1,2);
			}
			return hms;
		}
		return hms;
		
		
	}
	/**
	 * 根据传入的时间值，截取小时和分钟数
	 * 
	 * @param str
	 * @return
	 */
	public String getHourAndMinute(String str) {
		String hm="";
		if(!TextUtils.isEmpty(str)){
			hm = str.substring(11, 16);
		}
		return hm;

	}
	/**
	 * 将一个传入的字符串毫秒值转换成一个字符串时间值
	 * 
	 * @param str
	 * @return
	 */
	public String getChangeToTime(String str) {
		String timestring="";
		if(!TextUtils.isEmpty(str)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss",
					Locale.getDefault());
			long time = Long.parseLong(str);
			Date date = new Date();
			date.setTime(time);
			timestring = sdf.format(date);
		}
		

		return timestring;

	}
	private void createDialog(String string, String string2) {
		exitDialog = new CustomDialog(new OnClickListener() {

			@Override
			public void onClick(View v) {
				exitDialog.dismiss();
			}
		});
		exitDialog.setConfirmTxt("确认");
		exitDialog.setRemindTitle("温馨提示");
//		exitDialog.setRemindMessage("抱歉，店铺还未营业"+string+"-"+string2);
		exitDialog.setRemindMessage("抱歉，店铺还未营业");
		exitDialog.show();
	}

	@Override
	public void initBaseData() {

	}

	@Override
	public void initView() {

	}

	@Override
	public void initData() {
		
	}

	@Override
	public void initListener() {

	}
	
	/**
	 * 调用方法，从网络获取周边商品数据
	 * 
	 * @param context
	 */
	private void getdataFromService_down() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("pageNo", "1");
		params.put("rows", "5");
		params.put("userType", "1");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.SERVICE_FOODDIANPU_LIST, params, new GetDatas() {

					@SuppressWarnings("null")
					@Override
					public void getServerData(BackData data) {
						if (data == null) {
							// 说明网络获取无数据
							Message msg = new Message();
							msg.arg1 = TAG_DATANULL;
							handler.sendMessage(msg);
						} else {
							Object obj = data.getObject();
							if (obj == null) {
								// 说明数据为空
								Message msg = new Message();
								msg.arg1 = TAG_DATANULL;
								handler.sendMessage(msg);
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
									Message msg = new Message();
									msg.arg1 = TAG_DATANULL;
									handler.sendMessage(msg);
								} else {
									if(data.getNetResultCode()==0){
										Message msg = new Message();
										msg.arg1 = 110;
										msg.obj = backdata;
										handler.sendMessage(msg);
									}else{
										Message msg = new Message();
										msg.arg1 = 111;
										handler.sendMessage(msg);
									}
								}
							}
						}
					}

				}, false,true);
	}
	/**
	 * 调用方法，从网络获取周边商品数据
	 * 
	 * @param context
	 */
	private void getdataFromService_up(int page) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("pageNo", page+"");
		params.put("rows", "5");
		params.put("userType", "1");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.SERVICE_FOODDIANPU_LIST, params, new GetDatas() {

					@SuppressWarnings("null")
					@Override
					public void getServerData(BackData data) {
						if (data == null) {
							// 说明网络获取无数据
							Message msg = new Message();
							msg.arg1 = TAG_DATANULL;
							handler.sendMessage(msg);
						} else {
							Object obj = data.getObject();
							if (obj == null) {
								// 说明数据为空
								Message msg = new Message();
								msg.arg1 = TAG_DATANULL;
								handler.sendMessage(msg);
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
									Message msg = new Message();
									msg.arg1 = TAG_DATANULL;
									handler.sendMessage(msg);
								} else {
									if(data.getNetResultCode()==0){
										Message msg = new Message();
										msg.arg1 = 220;
										msg.obj = backdata;
										handler.sendMessage(msg);
									}else{
										Message msg = new Message();
										msg.arg1 = 222;
										handler.sendMessage(msg);
									}
								}
							}
						}
					}

				}, false,false);
	}
	/**
	 * 调用方法，从网络获取周边商品数据
	 * 
	 * @param context
	 */
	private void getdataFromService() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("pageNo", "1");
		params.put("rows", "5");
		params.put("userType", "1");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.SERVICE_FOODDIANPU_LIST, params, new GetDatas() {

					@SuppressWarnings("null")
					@Override
					public void getServerData(BackData data) {
						if (data == null) {
							// 说明网络获取无数据
							Message msg = new Message();
							msg.arg1 = TAG_DATANULL;
							handler.sendMessage(msg);
						} else {
							Object obj = data.getObject();
							if (obj == null) {
								// 说明数据为空
								Message msg = new Message();
								msg.arg1 = TAG_DATANULL;
								handler.sendMessage(msg);
							} else {
								String backdata = obj.toString();
								if (backdata == null && backdata.equals("")) {
									Message msg = new Message();
									msg.arg1 = TAG_DATANULL;
									handler.sendMessage(msg);
								} else {
									Message msg = new Message();
									msg.arg1 = TAG_DATA;
									msg.obj = backdata;
									handler.sendMessage(msg);
								}
							}
						}
					}

				}, false,true);
	}
	/**
	 * 调用方法。解析数据
	 * @param backdata
	 */
	private void jsonDataOfString(String backdata) {
		List<ServiceFoodBean> list=new ArrayList<ServiceFoodBean>();
		try {
			JSONArray array=new JSONArray(backdata);
			int count=array.length();
			for(int i=0;i<count;i++){
				ServiceFoodBean bean=new ServiceFoodBean();
				JSONObject obj=array.getJSONObject(i);
				if(obj.has("startbusinesstime")){
					bean.setStartTime(obj.getString("startbusinesstime"));
				}
				if(obj.has("endbusinesstime")){
					bean.setEndTime(obj.getString("endbusinesstime"));
				}
				if(obj.has("address")){
					bean.setAddress(obj.getString("address"));
				}
				if(obj.has("companyname")){
					bean.setCompanyname(obj.getString("companyname"));
				}
				if(obj.has("contact")){
					bean.setContact(obj.getString("contact"));
				}
				if(obj.has("id")){
					bean.setId(obj.getString("id"));
				}
				if(obj.has("mobilephone")){
					bean.setMobilephone(obj.getString("mobilephone"));
				}
				if(obj.has("telephone")){
					bean.setTelephone(obj.getString("telephone"));
				}
				if(obj.has("zipcode")){
					bean.setZipcode(obj.getString("zipcode"));
				}
				if(obj.has("imageUrl")){
					bean.setImageUrl(obj.getString("imageUrl"));
				}
				list.add(bean);
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if(list!=null&&list.size()!=0){
			//调用方法，将数据加载到适配器以及控件中
			initListview(context,list);
		}
	};
	
	/**
	 * 调用方法。解析数据
	 * @param backdata
	 */
	private List<ServiceFoodBean> jsonDataOfString_refrensh(String backdata) {
		List<ServiceFoodBean> list=new ArrayList<ServiceFoodBean>();
		try {
			JSONArray array=new JSONArray(backdata);
			int count=array.length();
			for(int i=0;i<count;i++){
				ServiceFoodBean bean=new ServiceFoodBean();
				JSONObject obj=array.getJSONObject(i);
				if(obj.has("startbusinesstime")){
					bean.setStartTime(obj.getString("startbusinesstime"));
				}
				if(obj.has("endbusinesstime")){
					bean.setEndTime(obj.getString("endbusinesstime"));
				}
				if(obj.has("address")){
					bean.setAddress(obj.getString("address"));
				}
				if(obj.has("companyname")){
					bean.setCompanyname(obj.getString("companyname"));
				}
				if(obj.has("contact")){
					bean.setContact(obj.getString("contact"));
				}
				if(obj.has("id")){
					bean.setId(obj.getString("id"));
				}
				if(obj.has("mobilephone")){
					bean.setMobilephone(obj.getString("mobilephone"));
				}
				if(obj.has("telephone")){
					bean.setTelephone(obj.getString("telephone"));
				}
				if(obj.has("zipcode")){
					bean.setZipcode(obj.getString("zipcode"));
				}
				if(obj.has("imageUrl")){
					bean.setImageUrl(obj.getString("imageUrl"));
				}
				list.add(bean);
				
			}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	};

}
