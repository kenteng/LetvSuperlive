/**
 * 
 */
package com.lesports.stadium.activity;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.text.DecimalFormat;
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

import android.R.integer;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lesports.stadium.R;
import com.lesports.stadium.adapter.OnlineCateringWindowAdapter;
import com.lesports.stadium.adapter.ServiceFoodDetailAdapter;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.base.BaseActivity;
import com.lesports.stadium.bean.GroupInfo;
import com.lesports.stadium.bean.ProductInfo;
import com.lesports.stadium.bean.RoundGoodsBean;
import com.lesports.stadium.bean.ServiceCateringDetailBean;
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
import com.lesports.stadium.view.CustomDialog;
import com.lidroid.xutils.view.annotation.event.OnClick;
/**
 * ***************************************************************
 * 
 * @Desc : 在线餐饮详情界面
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

public class OnlineCateringDetailActivity extends BaseActivity implements OnClickListener{
	
	/* (non-Javadoc)
	 * @see com.lesports.stadium.base.BaseActivity#onCreate(android.os.Bundle)
	 */
	
	/**
	 * 顶部标题控件，标题会随着用户选择发生改变
	 */
	private TextView mTitleBar;
	/**
	 * 顶部左侧返回按钮
	 */
	private ImageView mBack;
	/**
	 * 可刷新的listview
	 */
	private ListView mListview;
	/**
	 * 展示数据的listview
	 */
	private PullToRefreshListView mPullToListview;
	/**
	 * 底部可点击的购物车布局
	 */
	private RelativeLayout mLayoutBuy;
	/**
	 * 底部需要动态改变的购物数量
	 */
	private TextView mTextBuyNum;
	/**
	 * 底部合计价格
	 */
	private TextView mTextTotalPrice;
	/**
	 * 底部配送费
	 */
	private TextView mTextPeisonF;
	/**
	 * 底部支付按钮
	 */
	private TextView mPayFor;
	/**
	 * 列表项适配器
	 */
	private ServiceFoodDetailAdapter mAdapter;
	/**
	 * 本类对象
	 */
	public static  OnlineCateringDetailActivity mOnline;
	/**
	 * //动画层
	 */
	private ViewGroup anim_mask_layout;
	/**
	 * 需要在window上面展示的商品集合
	 */
	private List<ServiceCateringDetailBean> mNewList;
	/**
	 * 弹出的window当中的标题
	 */
	private TextView mWindowTitle;
	/**
	 * 弹出的window中的列表项控件
	 */
	private ListView mWindowListview;
	/**
	 * 弹出的window中的购物车布局
	 */
	private RelativeLayout mWindowLayoutBuy;
	/**
	 * 弹出窗口中的listview的适配器
	 */
	private OnlineCateringWindowAdapter mWindowAdapter;
	/**
	 * 弹出窗口中的已选商品数量图标
	 */
	private TextView mWindowtBuyNum;
	/**
	 * 弹出窗口中的已选商品的总价格
	 */
	private TextView mWindowGoodsPrice;
	/**
	 * 弹出窗口中的购物车所在布局
	 */
	private RelativeLayout mWindowLayoutCart;
	/**
	 * 商家id
	 */
	private String id;
	/**
	 * 用来标记网络获取的数据为空
	 */
	private final int TAG_DATANULL = 0;
	/**
	 * 用来标记网络获取的数据
	 */
	private final int TAG_DATA = 1;
	/**
	 * 弹出窗的标题，需要显示商家名称
	 */
	private TextView mWindowTextview;
	/**
	 * 商家名称
	 */
	private String CompanyName;
	/**
	 * // 组元素数据列表
	 */
	private List<GroupInfo> groups = new ArrayList<GroupInfo>();
	/**
	 * // 子元素数据列表
	 */
	private Map<String, List<ProductInfo>> children = new HashMap<String, List<ProductInfo>>();
	/**
	 * 标记组
	 */
	private final String GROUP_TAG="group";
	/**
	 * 标记子
	 */
	private final String CHILD_TAG="child";
	public static OnlineCateringDetailActivity  instance;
	/**
	 * 购物车窗口
	 */
	 private  Dialog dialog ;
	 /**
	  * 店铺的开店时间
	  */
	 private String mStartTime;
	 /**
	  * 店铺的关门时间
	  */
	 private String mEndTime;
	 /**
	  * 该方法用来标记，是否需要展示提示用户店铺营业时间未到的标记
	  */
	 private boolean isShowFood=false;
	/**
	 * 处理数据的handler；
	 */
	 /**
		 * 未登陆提示按钮
		 */
	 private CustomDialog exitDialog;
	private TextView mBuyCarPay;
	
	private String LAST_GOODS_ID="";
	
	/**
	 * 是否是上拉
	 */
	private boolean isShang=false;
	/**
	 * 存储数据的全局变量
	 */
	private List<ServiceCateringDetailBean> mList;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case TAG_DATANULL:
				Toast.makeText(getApplicationContext(), "网络异常", 0).show();
				break;
			case TAG_DATA:
				String backdata = (String) msg.obj;
				if (backdata != null && backdata.length() != 0) {
					// 调用方法，解析数据
					Log.i("开始解析，数据存在么",backdata);
					if(isShang){
						if(mList!=null){
							List<ServiceCateringDetailBean> list=jsonData(backdata);
//							mList.addAll(list);
							initlistview(list);
						}
					}else{
						mList=jsonData(backdata);
						if(mList!=null&&mList.size()!=0){
							LAST_GOODS_ID=mList.get(mList.size()-1).getgId();
							initlistview(mList);
						}
					}
				} else {
					Toast.makeText(getApplicationContext(), "网络异常", 0).show();
				}

				break;
			case 100:
				mPullToListview.onPullDownRefreshComplete();
				break;
			case 200:
				mPullToListview.onPullUpRefreshComplete();
				break;

			default:
				break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.activity_onclinecatering_detail);
		Intent intent=getIntent();
		id=intent.getStringExtra("id");
		//这里需要将商家id存储起来，在支付完成以后，清空用户在该商家内的所有商品
		GlobalParams.ONLINE_STORE_ID=id;
		CompanyName=intent.getStringExtra("title");
		mStartTime=intent.getStringExtra("stime");
		mEndTime=intent.getStringExtra("etime");
		initview();
		getdataFromService(id,false);
		inindatas();
		mNewList=new ArrayList<ServiceCateringDetailBean>();
		//计算是否需要展示通知
		useWayCountDialog(mStartTime,mEndTime);
	}
	/**
	 * 用来计算店铺营业开始时间和结束时间是否满足当前时间需求
	 * @param mStartTime2
	 * @param mEndTime2
	 */
	private void useWayCountDialog(String mStartTime2, String mEndTime2) {
		// TODO Auto-generated method stub
		Date date = new Date();
		long currenttimes=date.getTime();
		String currenttimesss=getChangeToTime(currenttimes+"");
		String currenttime_hour=getHourAndMinute(currenttimesss);
		if(!TextUtils.isEmpty(mStartTime2)&&!TextUtils.isEmpty(mEndTime2)){
			String[] start_hournum=getHourNums(mStartTime2);//截取了开始时间的小时数
			String[] end_times=getHourNums(mEndTime2);
			String[] current_times=getHourNums(currenttime_hour);
			//调用方法，看当前时间是否处于该范围内部
			boolean ishave=ishavecountNumIsHaveFanwei(start_hournum,end_times,current_times);
			if(ishave){
				//调用方法，需要把适配器里面的布尔值设置为true
				if(mAdapter!=null){
					mAdapter.isHave=true;
				}else{
					isShowFood=true;
				}
			}else{
				if(mAdapter!=null){
					mAdapter.isHave=false;
				}else{
					isShowFood=false;
				}
				
				createDialog(currenttime_hour,mEndTime2);
			}
		}
		
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
	/**
	 * 用来判断该事件是否处于该时间段内部
	 * @param start_hournum
	 * @param end_times
	 * @param current_times
	 * @return
	 */
	private boolean ishavecountNumIsHaveFanwei(String[] start_hournum,
			String[] end_times, String[] current_times) {
		int starttime=Integer.parseInt(start_hournum[0]);
		int endtime=Integer.parseInt(end_times[0]);
		int current=Integer.parseInt(current_times[0]);
		boolean ishave=false;
		if(starttime<=current&&current<=endtime){
			ishave=true;
		}else{
			ishave=false;
		}
		
		return ishave;
		
	}
	/**
	 * 调用方法，从网络获取周边商品数据
	 * @param id2 
	 * @param b 是否是上拉
	 * 
	 * @param context
	 */
	private void getdataFromService(String id2, boolean b) {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("flag", "");
		if(b){
			isShang=b;
			params.put("goodsId",LAST_GOODS_ID);
		}else{
			params.put("goodsId", "");
		}
		params.put("seller", id2);
		params.put("sort", "");
		params.put("classicId", "");
		params.put("price", "");
		Log.i("", "走到这里了么？");
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.SERVICE_FOODDIANPU_GOODS_LIST, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						// TODO Auto-generated method stub
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
									Log.i("",
											"走到这里了么？" + data.getNetResultCode());
									Log.i(getApplicationContext().getClass().getName(),
											backdata);
									Message msg = new Message();
									msg.arg1 = TAG_DATA;
									msg.obj = backdata;
									handler.sendMessage(msg);
								}
							}
						}
					}

				}, false,false);
	}
	  /**
     * @Description: 创建动画层
     * @param
     * @return void
     * @throws
     */
    private ViewGroup createAnimLayout() {
        ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setId(Integer.MAX_VALUE-1);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    
    /**
     * @2016-2-15下午3:44:57
     * 计算动画起始角度问题
     */
    private View addViewToAnimLayout(final ViewGroup parent, final View view,
                                     int[] location) {
        int x = location[0];
        int y = location[1];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
        return view;
    }
	 /**
	 * @2016-2-15下午3:32:08
	 * 给界面中的添加按钮设置动画
	 */
	public void setAnim(final View v, int[] startLocation) {
	        anim_mask_layout = null;
	        anim_mask_layout = createAnimLayout();
	        anim_mask_layout.addView(v);//把动画小球添加到动画层
	        final View view = addViewToAnimLayout(anim_mask_layout, v,
	                startLocation);
	        int[] endLocation = new int[2];// 存储动画结束位置的X、Y坐标
	        mLayoutBuy.getLocationInWindow(endLocation);// shopCart是那个购物车

	        // 计算位移
	        int endX = 0 - startLocation[0] + 40;// 动画位移的X坐标
	        int endY = endLocation[1] - startLocation[1];// 动画位移的y坐标
	        TranslateAnimation translateAnimationX = new TranslateAnimation(0,
	                endX, 0, 0);
	        translateAnimationX.setInterpolator(new LinearInterpolator());
	        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
	        translateAnimationX.setFillAfter(true);

	        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
	                0, endY);
	        translateAnimationY.setInterpolator(new AccelerateInterpolator());
	        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
	        translateAnimationX.setFillAfter(true);

	        AnimationSet set = new AnimationSet(false);
	        set.setFillAfter(false);
	        set.addAnimation(translateAnimationY);
	        set.addAnimation(translateAnimationX);
	        set.setDuration(800);// 动画的执行时间
	        view.startAnimation(set);
	        // 动画监听事件
	        set.setAnimationListener(new Animation.AnimationListener() {
	            // 动画的开始
	            @Override
	            public void onAnimationStart(Animation animation) {
	                v.setVisibility(View.VISIBLE);
	            }

	            @Override
	            public void onAnimationRepeat(Animation animation) {
	                // TODO Auto-generated method stub
	            }
	            // 动画的结束
	            @Override
	            public void onAnimationEnd(Animation animation) {
	                v.setVisibility(View.GONE);
	            }
	        });

	    }
	     /**
		 * @2016-2-15下午3:32:08
		 * 给弹出框的添加按钮设置动画
		 */
		public void setAnimWindow(final View v, int[] startLocation) {
		        anim_mask_layout = null;
		        anim_mask_layout = createAnimLayout();
		        anim_mask_layout.addView(v);//把动画小球添加到动画层
		        //因为弹出窗口的问题，所以这里需要将坐标点发生改变
		        int[] startLocations=new int[2];
		        startLocations[0]=startLocation[0];
		        startLocations[1]=startLocation[1]-getScreenHeight(OnlineCateringDetailActivity.this)/2;
		        final View view = addViewToAnimLayout(anim_mask_layout, v,
		                startLocations);
		        int[] endLocation = new int[2];// 存储动画结束位置的X、Y坐标
		        mWindowLayoutCart.getLocationInWindow(endLocation);// shopCart是那个购物车
		        //重新计算窗口的动画终点坐标
		        int[] endLocations=new int[2];
		        endLocations[0]=endLocation[0];
		        endLocations[1]=endLocation[1]-getScreenHeight(OnlineCateringDetailActivity.this)/2;
		        // 计算位移
		        int endX = 0 - startLocations[0] + 40;// 动画位移的X坐标
		        int endY = endLocations[1] - startLocations[1];// 动画位移的y坐标
		        TranslateAnimation translateAnimationX = new TranslateAnimation(0,
		                endX, 0, 0);
		        translateAnimationX.setInterpolator(new LinearInterpolator());
		        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
		        translateAnimationX.setFillAfter(true);

		        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
		                0, endY);
		        translateAnimationY.setInterpolator(new AccelerateInterpolator());
		        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
		        translateAnimationX.setFillAfter(true);

		        AnimationSet set = new AnimationSet(false);
		        set.setFillAfter(false);
		        set.addAnimation(translateAnimationY);
		        set.addAnimation(translateAnimationX);
		        set.setDuration(800);// 动画的执行时间
		        view.startAnimation(set);
		        // 动画监听事件
		        set.setAnimationListener(new Animation.AnimationListener() {
		            // 动画的开始
		            @Override
		            public void onAnimationStart(Animation animation) {
		                v.setVisibility(View.VISIBLE);
		            }

		            @Override
		            public void onAnimationRepeat(Animation animation) {
		                // TODO Auto-generated method stub
		            }

		            // 动画的结束
		            @Override
		            public void onAnimationEnd(Animation animation) {
		                v.setVisibility(View.GONE);
		            }
		        });

		    }
	/**
	 * @2016-2-15下午3:31:29
	 * 本类对象
	 */
	public static OnlineCateringDetailActivity instence(){
		if(mOnline==null){
			mOnline=new OnlineCateringDetailActivity();
		}else{
			return mOnline;
		}
		return mOnline;
	}
	/**
	 * @2016-2-15下午2:44:29
	 * 提供给适配器，用于动态改变界面中购物车上方图标的商品数量
	 */
	public void setBuyNum(List<ServiceCateringDetailBean> list){
		//循环遍历该集合，然后计算其中实体类的数量属性值的和，然后将结果设置到图标上
		String username=GlobalParams.USER_ID;
		List<ServiceCateringDetailBean> lists=LApplication.foodbuycar.findByCondition("foodbuy_usernametag =? AND foodbuy_seller=?",
				new String[]{username,id}, null);
		int num=0;
		double totalPrice=0;
		if(lists!=null&&lists.size()!=0){
			int size=lists.size();
			for(int i=0;i<lists.size();i++){
				num=num+lists.get(i).getGoodsNum();
				if(!TextUtils.isEmpty(lists.get(i).getPrice())){
					double price=Double.parseDouble(lists.get(i).getPrice());
					totalPrice=totalPrice+(price*lists.get(i).getGoodsNum());
				}else{
					double prise=Double.parseDouble(lists.get(i).getPriceUnit());
					totalPrice=totalPrice+(prise*lists.get(i).getGoodsNum());
				}
			}
		}
		if(mTextBuyNum.getVisibility()==View.VISIBLE){
			mTextBuyNum.setText(num+"");
//			String string=changeDoubleToString(totalPrice);
			mTextTotalPrice.setText(Utils.parseTwoNumber(totalPrice+"")+"");
		}else{
			mTextBuyNum.setVisibility(View.VISIBLE);
			mTextBuyNum.setText(num+"");
//			String string=changeDoubleToString(totalPrice);
			mTextTotalPrice.setText(Utils.parseTwoNumber(totalPrice+"")+"");
		}
	}
	/**
	 * @2016-2-15下午2:44:29
	 * 提供给窗体列表项适配器，用于动态改变窗体界面中购物车上方图标的商品数量以及价格
	 */
	public void setBuyNumWindow(List<ServiceCateringDetailBean> list){
		//循环遍历该集合，然后计算其中实体类的数量属性值的和，然后将结果设置到图标上
		int num=0;
		double totalPrice=0;
		for(ServiceCateringDetailBean bean:list){
			num=num+bean.getGoodsNum();
			double price=Double.parseDouble(bean.getPrice());
			totalPrice=totalPrice+(price*bean.getGoodsNum());
		}
//		String string=changeDoubleToString(totalPrice);
		mWindowtBuyNum.setText(num+"");
		mWindowGoodsPrice.setText(Utils.parseTwoNumber(totalPrice+"")+"元");
		//同时在这里，还需要动态改变界面中的购物车数量和价格以及列表项内容
		mTextBuyNum.setText(num+"");
		mTextTotalPrice.setText(Utils.parseTwoNumber(totalPrice+"")+"");
	}
	/**
	 * @2016-2-15下午12:58:24
	 * 用于给控件进行初始化数据
	 */
	private void inindatas() {
		// TODO Auto-generated method stub
		mTitleBar.setText(CompanyName);
	}
	/**
	 * @2016-2-15上午11:19:51
	 * 初始化view对象的方法
	 */
	private void initview() {
		// TODO Auto-generated method stub
		mTitleBar=(TextView) findViewById(R.id.order_tv_title);
		mBack=(ImageView) findViewById(R.id.order_title_left_iv);
		mBack.setVisibility(View.VISIBLE);
		mBack.setOnClickListener(this);
		mPullToListview=(PullToRefreshListView) findViewById(R.id.onclinecatering_listview);
		mListview=mPullToListview.getRefreshableView();
		mLayoutBuy=(RelativeLayout) findViewById(R.id.catering_detail_bottom_bar_buycart);
		mLayoutBuy.setOnClickListener(this);
		mTextTotalPrice=(TextView) findViewById(R.id.catering_detail_bottom_bar_buygoods_price);
		mTextBuyNum=(TextView) findViewById(R.id.catering_detail_bottom_bar_buynum);
		initBuynu(mTextBuyNum,mTextTotalPrice);
		mTextPeisonF=(TextView) findViewById(R.id.catering_detail_bottom_bar_buypeisongfei);
		mPayFor=(TextView) findViewById(R.id.catering_detail_bottom_bar_buypaybutton);
		mPayFor.setOnClickListener(this);
		mPullToListview.setPullLoadEnabled(false);
		mPullToListview.setPullRefreshEnabled(false);
		mPullToListview
		.setOnRefreshListener(new OnRefreshListener<ListView>() {

			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 网络异常
				if (!UIUtils.isAvailable(OnlineCateringDetailActivity.this)) {
					mPullToListview.onPullDownRefreshComplete();
				} else {

					// 下拉刷新加载数据
					loadRefreshData_down(1, 1);

				}
			}

			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 网络异常
				if (!UIUtils.isAvailable(OnlineCateringDetailActivity.this)) {
					mPullToListview.onPullUpRefreshComplete();
				} else {
					// 上拉刷新加载数据
					loadRefreshData_up(0, 1);

				}
			}
		});
	}
	/**
	 * 将传入的double数据保留有两位转换成字符串返回
	 * @param d
	 * @return
	 */
	public String changeDoubleToString(double d){
		DecimalFormat df = new DecimalFormat("#.##");
		String string = df.format(d);
		return string;
	}
	/**
	 * 调用方法，初始化该控
	 * @param mTextBuyNum2
	 * @param mTextTotalPrice2 
	 */
	private void initBuynu(TextView mTextBuyNum2, TextView mTextTotalPrice2) {
		double totalPrice=0;
		String username=GlobalParams.USER_ID;
		if(!TextUtils.isEmpty(username)){
			List<ServiceCateringDetailBean> list=LApplication.foodbuycar.findByCondition("foodbuy_usernametag =? AND foodbuy_seller=?",
					new String[]{username,id}, null);
			int num=0;
			if(list!=null&&list.size()!=0){
				int size=list.size();
				for(int i=0;i<list.size();i++){
					num=num+list.get(i).getGoodsNum();
					double price=Double.parseDouble(list.get(i).getPrice());
					totalPrice=totalPrice+(price*list.get(i).getGoodsNum());
				}
			}
			if(num==0){
				mTextTotalPrice2.setText("0.00");
				mTextBuyNum2.setVisibility(View.GONE);
			}else{
				mTextBuyNum2.setText(num+"");
//				String string=changeDoubleToString(totalPrice);
				mTextTotalPrice2.setText(Utils.parseTwoNumber(totalPrice+"")+"");
			}
		}else{
			mTextBuyNum2.setVisibility(View.GONE);
//			String string=changeDoubleToString(totalPrice);
			mTextTotalPrice2.setText(Utils.parseTwoNumber(totalPrice+"")+"");
		}
		
	}
	/**
	 * 上拉下拉数据加载
	 * 
	 * @param i
	 * @param j
	 */
	protected void loadRefreshData_down(int i, int j) {
		// TODO Auto-generated method stub
		Message message=new Message();
		message.arg1=100;
		handler.sendMessageDelayed(message, 1000);
		getdataFromService(id,false);
	}
	protected void loadRefreshData_up(int i, int j) {
		// TODO Auto-generated method stub
		Message message=new Message();
		message.arg1=200;
		handler.sendMessageDelayed(message, 1000);
		getdataFromService(id,true);
	}
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.catering_detail_bottom_bar_buycart:
			/**
			 * 先判断用户是否登录
			 */
			String usename=GlobalParams.USER_ID;
			if(!TextUtils.isEmpty(usename)){
				mNewList.clear();
				//这里开始需要使用数据库中的数据
				mNewList=LApplication.foodbuycar.findByCondition("foodbuy_usernametag =? AND foodbuy_seller=?",
						new String[]{usename,id}, null);
				if(mNewList!=null&&mNewList.size()==0){
					//说明未选择任何商品
					Toast.makeText(OnlineCateringDetailActivity.this,"请先选择商品",1).show();
				}else{
					//需要先弹出一个dialog，在该dialog上面来进行选择
					dialog = new Dialog(OnlineCateringDetailActivity.this, R.style.Theme_Light_Dialog);
		           View dialogView = LayoutInflater.from(OnlineCateringDetailActivity.this).inflate(R.layout.catering_detail_window_layout,null);
		           //获得dialog的window窗口
		           mWindowTextview=(TextView) dialogView.findViewById(R.id.catering_detail_window_layout_title);
		           mWindowTextview.setText(CompanyName);
		           mWindowListview= (ListView) dialogView.findViewById(R.id.catering_detail_window_listview);
		           mWindowtBuyNum=(TextView) dialogView.findViewById(R.id.catering_detail_window_bottom_bar_buynum);
		           mWindowGoodsPrice=(TextView) dialogView.findViewById(R.id.catering_detail_window_bottom_bar_buygoods_price);
		           mWindowGoodsPrice.setText(mTextTotalPrice.getText().toString()+"元");
		           mWindowLayoutCart=(RelativeLayout) dialogView.findViewById(R.id.catering_detail_window_bottom_bar_buycart);
		           mWindowLayoutCart.setOnClickListener(this);
		           mBuyCarPay=(TextView) dialogView.findViewById(R.id.catering_detail_window_bottom_bar_buypaybutton);
		           mBuyCarPay.setOnClickListener(this);
		           Window window = dialog.getWindow();
		           //设置dialog在屏幕底部
		           window.setGravity(Gravity.BOTTOM);
		           //设置dialog弹出时的动画效果，从屏幕底部向上弹出
		           window.setWindowAnimations(R.style.dialogStyle);
		           window.getDecorView().setPadding(0, 0, 0,0);
		           //获得window窗口的属性
		           android.view.WindowManager.LayoutParams lp = window.getAttributes();
		           //设置窗口宽度为充满全屏
		           lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		           //设置窗口高度为包裹内容
		           if(mNewList.size()>=4){
		        	   lp.height =getScreenHeight(OnlineCateringDetailActivity.this)/2;
		           }else{
		        	   lp.height =getScreenHeight(OnlineCateringDetailActivity.this)/3;
		           }
		           //将设置好的属性set回去
		           window.setAttributes(lp);
		           //将自定义布局加载到dialog上
		           dialog.setContentView(dialogView);
		           dialog.show();
		           dialog.setOnDismissListener(new OnDismissListener() {
					
					@Override
					public void onDismiss(DialogInterface dialog) {
						// TODO Auto-generated method stub
						//窗口中数据数据已经发生改变，如果窗口隐藏，则需要将界面中数据也改变，所以需要重新来对界面列表项
						//的数据源进行修改和适配
						List<ServiceCateringDetailBean> lists=mAdapter.mList;
						List<ServiceCateringDetailBean> listss=mWindowAdapter.mList;
						if(listss!=null&&listss.size()!=0){
							for(ServiceCateringDetailBean bean:lists){
								for(ServiceCateringDetailBean beans:listss){
									if(bean.getgId().equals(beans.getgId())){
										//说明是同一个商品
										bean.setGoodsNum(beans.getGoodsNum());//替换数量
									}
								}
							}
							//循环结束，重新适配数据
							mAdapter.setList(lists);
						}else{
							for(int j=0;j<lists.size();j++){
								lists.get(j).setGoodsNum(0);
							}
							mAdapter.setList(lists);
						}
						initBuynu(mTextBuyNum,mTextTotalPrice);
					}
				});
		           mWindowAdapter=new OnlineCateringWindowAdapter(mNewList, OnlineCateringDetailActivity.this);
		           mWindowListview.setAdapter(mWindowAdapter);
		           mWindowGoodsPrice.setText(mTextTotalPrice.getText()+"元");
		           mWindowtBuyNum.setText(mTextBuyNum.getText());
				}
			}else{
				createDialog();
			}
			break;
		case R.id.order_title_left_iv:
			OnlineCateringDetailActivity.this.finish();
			break;
		case R.id.catering_detail_window_bottom_bar_buypaybutton:
			//这里需要做的是，取出购物车内所有的商品，然后将之全部传递到订单界面
			String username=GlobalParams.USER_ID;
			if(!TextUtils.isEmpty(username)){
				groups.clear();
				children.clear();
				List<ServiceCateringDetailBean>  list=LApplication.foodbuycar.findByCondition("foodbuy_usernametag =? AND foodbuy_seller=?",
						new String[]{username,id}, null);
				if(list!=null&&list.size()!=0){
					for(int i=0;i<list.size();i++){
						virtualData(list.get(i));
					}
					if(groups!=null&&groups.size()!=0&&children!=null&&children.size()!=0){
						Intent intentsss=new Intent(OnlineCateringDetailActivity.this, OnLineCreatingPayActivity.class);
						intentsss.putExtra(GROUP_TAG,(Serializable)groups);
						intentsss.putExtra(CHILD_TAG,(Serializable)children);
						startActivity(intentsss);
					}
				}else{
					Toast.makeText(OnlineCateringDetailActivity.this,"请先选择商品",0).show();
				}
			}else{
				createDialog();
			}
			
			
			break;
		case R.id.catering_detail_window_bottom_bar_buycart:
			 dialog.dismiss(); 
			break;
		case R.id.catering_detail_bottom_bar_buypaybutton:
			//这里需要做的是，取出购物车内所有的商品，然后将之全部传递到订单界面
			String usesrname=GlobalParams.USER_ID;
			if(!TextUtils.isEmpty(usesrname)){
				groups.clear();
				children.clear();
				List<ServiceCateringDetailBean>  lists=LApplication.foodbuycar.findByCondition("foodbuy_usernametag =? AND foodbuy_seller=?",
						new String[]{usesrname,id}, null);
				if(lists!=null&&lists.size()!=0){
					for(int i=0;i<lists.size();i++){
						virtualData(lists.get(i));
					}
					if(groups!=null&&groups.size()!=0&&children!=null&&children.size()!=0){
						Log.i("目前有多少个组",groups.size()+"");
						Log.i("目前有多少个子",children.size()+"");
						Intent intentss=new Intent(OnlineCateringDetailActivity.this, OnLineCreatingPayActivity.class);
						intentss.putExtra(GROUP_TAG,(Serializable)groups);
						intentss.putExtra(CHILD_TAG,(Serializable)children);
						startActivity(intentss);
					}
				}else{
					Toast.makeText(OnlineCateringDetailActivity.this,"请先选择商品",0).show();
				}
			}else{
				createDialog();
			}
			
			break;

		default:
			break;
		}
	}
	/**
	 * 提示用户进行登录
	 */
	private void createDialog(){
		exitDialog = new CustomDialog(this,new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(OnlineCateringDetailActivity.this,LoginActivity.class);
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
		exitDialog.setRemindMessage("登录之后才能添加购物车哦  ~~");
		exitDialog.show();
	}
	private void virtualData(ServiceCateringDetailBean bean) 
	{
		//首先，判断组集合中是否存在该组id
		boolean ishave=checkGroupIsHaveTheBean(bean);
		if(ishave){
			//说明已经存在
			//判断该组id以及该商品是否存在在子集合当中
			boolean ishavess=checkChildIsHava(bean);
			if(ishavess){
				//说明已经存在
			}else{
				ProductInfo infos=new ProductInfo();
				infos.setCount(bean.getGoodsNum());
				infos.setDesc(bean.getGoodsName());
				infos.setSeller(bean.getSeller());
				if(!TextUtils.isEmpty(bean.getFreight())){
					infos.setYunfei(Double.parseDouble(bean.getFreight()));
				}else{
					infos.setYunfei(0);
				}
				infos.setId(bean.getgId());
				infos.setImageUrl(bean.getMediumImg());
				infos.setPrice(Double.parseDouble(bean.getPrice().trim()));
				infos.setName(bean.getGoodsName());
				children.get(bean.getSeller()).add(infos);
			}
		}else{
			//说明未存在，
			GroupInfo info=new GroupInfo();
			info.setId(bean.getSeller());
			info.setName(GlobalParams.Online_Name);
			groups.add(info);
			ProductInfo infos=new ProductInfo();
			infos.setCount(bean.getGoodsNum());
			infos.setDesc(bean.getGoodsName());
			infos.setSeller(bean.getSeller());
			if(!TextUtils.isEmpty(bean.getFreight())){
				infos.setYunfei(Double.parseDouble(bean.getFreight()));
			}else{
				infos.setYunfei(0);
			}
			infos.setId(bean.getgId());
			infos.setImageUrl(bean.getMediumImg());
			infos.setPrice(Double.parseDouble(bean.getPrice().trim()));
			infos.setName(bean.getGoodsName());
			List<ProductInfo> list=new ArrayList<ProductInfo>();
			list.add(infos);
			children.put(bean.getSeller(),list);
		}
	}
	
	/**
	 * 在当前组集合中判断，该组id是否已经存在
	 * @param bean
	 */
	private boolean checkGroupIsHaveTheBean(ServiceCateringDetailBean bean) {
		// TODO Auto-generated method stub
		boolean ishave=false;
		for(int i=0;i<groups.size();i++){
			if(groups.get(i).getId().equals(bean.getSeller())){
				//说明该组已经存在了
				ishave=true;
				break;
			}else{
				ishave=false;
			}
		}
		return ishave;
		
	}
	/**
	 * 在当前子集合中判断，该商品是否已经存在在子的集合当中
	 * @param bean
	 */
	private boolean checkChildIsHava(ServiceCateringDetailBean bean) {
		// TODO Auto-generated method stub
		boolean ishaves=false;
		//首先判断该组是否在子map中存在
		if(children.containsKey(bean.getSeller())){
			//说明该key已经存在，所以
			//根据该key来获取list集合，在集合中判断该商品是否存在
			List<ProductInfo> list=children.get(bean.getSeller());
			boolean ishave=checkTheGoodsIsHave(list,bean);
			if(ishave){
				//说明已经存在
				ishaves=ishave;
			}else{
				ishaves=ishave;
			}
		}
		return ishaves;
	}
	/**
	 * 判断该商品是否存在在该集合中
	 * @param list
	 * @param bean
	 */
	private boolean checkTheGoodsIsHave(List<ProductInfo> list, ServiceCateringDetailBean bean) {
		// TODO Auto-generated method stub
		boolean ishave=false;
		for(int i=0;i<list.size();i++){
			if(bean.getgId().equals(list.get(i).getId())){
				ishave=true;
				break;
			}else{
				ishave=false;
			}
		}
		return ishave;
	}

	/**
	 * 获得屏幕高度
	 * @param context
	 * @return
	 */
	public int getScreenHeight(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.heightPixels;
	}
	/**
	 * 解析网络数据
	 * @param backdata
	 */
	private List<ServiceCateringDetailBean> jsonData(String backdata) {
		// TODO Auto-generated method stub
		List<ServiceCateringDetailBean> list=new ArrayList<ServiceCateringDetailBean>();
		try {
			JSONArray array=new JSONArray(backdata);
			int count=array.length();
			for(int i=0;i<count;i++){
				JSONObject obj=array.getJSONObject(i);
				ServiceCateringDetailBean bean=new ServiceCateringDetailBean();
				if(obj.has("bigImg")){
					bean.setBigImg(obj.getString("bigImg"));
				}
				if(obj.has("category")){
					JSONObject objs=obj.getJSONObject("category");
					if(objs.has("cId")){
						bean.setcId(objs.getString("cId"));
					}
					if(objs.has("classicName")){
						bean.setClassicName(objs.getString("classicName"));
					}
				}
				if(obj.has("classicId")){
					bean.setClassicId(obj.getString("classicId"));
				}
				if(obj.has("createTime")){
					bean.setCreateTime(obj.getString("createTime"));
				}
				if(obj.has("freight")){
					bean.setFreight(obj.getString("freight"));
				}
				if(obj.has("gId")){
					bean.setgId(obj.getString("gId"));
				}
				if(obj.has("goodsName")){
					bean.setGoodsName(obj.getString("goodsName"));
				}
				if(obj.has("mediumImg")){
					bean.setMediumImg(obj.getString("mediumImg"));
				}
				if(obj.has("price")){
					bean.setPrice(obj.getString("price"));
				}
				if(obj.has("priceUnit")){
					bean.setPriceUnit(obj.getString("priceUnit"));
				}
				if(obj.has("referprice")){
					bean.setReferprice(obj.getString("referprice"));
				}
				if(obj.has("seller")){
					bean.setSeller(obj.getString("seller"));
				}
				if(obj.has("smallImg")){
					bean.setSmallImg(obj.getString("smallImg"));
				}
				if(obj.has("status")){
					bean.setStatus(obj.getString("status"));
				}
				if(obj.has("stock")){
					bean.setStock(obj.getString("stock"));
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
	/**
	 * 将数据加载到列表项控件上
	 * @param list
	 */
	private void initlistview(List<ServiceCateringDetailBean> list) {
		// TODO Auto-generated method stub
		//先读取数据库内数据，跟现有数据进行匹配，然后如果数据库中该商品已经存在，则直接将其直观的显示出来
		List<ServiceCateringDetailBean> newlist=containsData(list);
		if(newlist!=null&&newlist.size()!=0){
			mAdapter=new ServiceFoodDetailAdapter(isShowFood,list, OnlineCateringDetailActivity.this);
			mListview.setAdapter(mAdapter);
		}else{
			Toast.makeText(OnlineCateringDetailActivity.this,"网络异常",0).show();
		}
	}
	/**
	 * //先读取数据库内数据，跟现有数据进行匹配，
	 * 然后如果数据库中该商品已经存在，则直接将其直观的显示出来
	 * @param list
	 * @return
	 */
	private List<ServiceCateringDetailBean> containsData(
			List<ServiceCateringDetailBean> list) {
		// TODO Auto-generated method stub
		String usename=GlobalParams.USER_ID;
		if(!TextUtils.isEmpty(usename)){
			List<ServiceCateringDetailBean> listss=new ArrayList<ServiceCateringDetailBean>();
			if(list!=null&&list.size()!=0){
				List<ServiceCateringDetailBean> list_sql=LApplication.foodbuycar.findByCondition("foodbuy_usernametag =? AND foodbuy_seller=?",
						new String[]{usename,id}, null);
				if(list_sql!=null&&list_sql.size()!=0){
					int count=list_sql.size();
					for(int i=0;i<count;i++){
						String id=list_sql.get(i).getgId();
						int size=list.size();
						for(int j=0;j<size;j++){
							if(id.equals(list.get(j).getgId())){
								//说明该商品之前已经被添加到数据库中了
								list.get(j).setGoodsNum(list_sql.get(i).getGoodsNum());
							}
						}
					}
				}
				listss.addAll(list);
				return listss;
			}
		}else{
			return list;
		}
		
		return list;
	}
	
	@Override
	protected void onDestroy() {
		if(handler!=null){
			handler.removeCallbacksAndMessages(null);
		}
		if(mNewList!=null){
			mNewList.clear();
			mNewList=null;
		}
		if(groups!=null){
			groups.clear();
			groups = null;
		}
		if(children!=null){
			children.clear();
			children=null;
		}
		instance = null;
		super.onDestroy();
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
	 * 根据传入时间来截取小时数
	 * @param currenttime_hour
	 * @return
	 */
	private String getHourNum(String currenttime_hour) {
		// TODO Auto-generated method stub
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
	 * 用来处理服务器传入的时间
	 * @param currenttime_hour
	 * @return
	 */
	private String[] getHourNums(String currenttime_hour) {
		// TODO Auto-generated method stub
		String[] hms=new String[2];
		if(!TextUtils.isEmpty(currenttime_hour)){
			hms[1]=currenttime_hour.substring(3,5);
			String hmss=currenttime_hour.substring(0,1);
			if(hmss.equals("0")){
				hms[0]=currenttime_hour.substring(1,2);
			}else{
				hms[0]=currenttime_hour.substring(0,2);
			}
			return hms;
		}
		return hms;
		
		
	}
	
	

}
