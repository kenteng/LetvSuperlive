package com.lesports.stadium.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lesports.stadium.R;
import com.lesports.stadium.bean.CouponBean;
import com.lesports.stadium.bean.YouHuiBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.view.InvalindView;
import com.lesports.stadium.view.SureUseCouponView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

/**
 * ***************************************************************
 * 
 * @Desc : 使用优惠券界面
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
public class UseCounponActivity extends Activity implements OnClickListener {
	private View tabImgView;
	/*
	 * 未接收
	 */
	private TextView t_order_wait_pay;
	/*
	 * 已接收
	 */
	private TextView t_order_wait_send;
	/*
	 * VewPager 对象
	 */
	private ViewPager categoryPager;
	/*
	 * viewPager适配器
	 */
	private CategoryPageAdapter categoryAdapter;

	/**
	 * 未接收
	 */
	private static final int ORDER_WAIT_PAY = 0;
	/**
	 * 已接收
	 */
	private static final int ORDER_WAIT_SEND = 1;
	/*
	 * 存储数据
	 */
	private ArrayList<View> pagerList;
	/**
	 * 选项卡最后选择的是哪个
	 */
	private int lastPager = 0;
	/*
	 * 可以使用的优惠券view
	 */
	private SureUseCouponView mSureUseCouponView;
	/*
	 *已经失效的优惠券的 
	 */
	private InvalindView mInvalindView;

	public static String data = "data";
	/**
	 * 优惠券数据源
	 */
	private List<YouHuiBean> mList;
	private ImageView mBack;
	private String tag;
	private String goodsprice;
	/**
	 * 处理数据的handle
	 */
	private Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case 1:
				String backdata=(String) msg.obj;
				if(!TextUtils.isEmpty(backdata)){
					Log.i("优惠券数据",backdata);
					//调用方法，进行解析
					mList=useWayJsonData(backdata);
					//调用方法，进行分拣数据
					if(mList!=null&&mList.size()!=0){
						useWayFenjianData(mList);
					}
				}
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usecoupon);
		initview();
		Intent intent=getIntent();
		tag=intent.getStringExtra("tag");
		goodsprice=intent.getStringExtra("price");
		initListener();
		//调用方法获取用户优惠信息
		useWayGetData();
		
	}
	public void setBean(YouHuiBean bean){
		Intent intent=new Intent();
		intent.putExtra("bean",bean);
		UseCounponActivity.this.setResult(1,intent);
		finish();
	}

	private void initListener() {
		t_order_wait_pay.setOnClickListener(this);
		t_order_wait_send.setOnClickListener(this);
		categoryPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				TranslateAnimation animation = new TranslateAnimation(lastPager
						* GlobalParams.WIN_WIDTH / 2, position
						* GlobalParams.WIN_WIDTH / 2, 0, 0);

				animation.setDuration(300);
				animation.setFillAfter(true);
				tabImgView.startAnimation(animation);

				lastPager = position;
				t_order_wait_pay.setTextColor(getResources().getColor(
						R.color.word_gray));
				t_order_wait_send.setTextColor(getResources().getColor(
						R.color.word_gray));

				switch (position) {
				case ORDER_WAIT_PAY:
					t_order_wait_pay.setTextColor(getResources().getColor(
							R.color.service_select_txt));
					break;
				case ORDER_WAIT_SEND:
					t_order_wait_send.setTextColor(getResources().getColor(
							R.color.service_select_txt));
					break;
				}
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
	/**
	 * 按照可用和不可用来讲数据进行分拣出来，并且分别传递到相对饮的界面view中
	 * @param mList
	 */
	private void useWayFenjianData(List<YouHuiBean> mLists) {
		// TODO Auto-generated method stub
		List<YouHuiBean> list_yes=new ArrayList<YouHuiBean>();
		List<YouHuiBean> list_no=new ArrayList<YouHuiBean>();
		if(mLists!=null&&mLists.size()!=0){
			int count=mLists.size();
			for(int i=0;i<count;i++){
				if(tag.equals("online")){
					if(mLists.get(i).getStatus().equals("1")&&mLists.get(i).getCouponType().equals("1")&&mLists.get(i).getInusing().equals("0")){
						//说明可用
						list_yes.add(mLists.get(i));
					}else {
						//说明不可用
						list_no.add(mLists.get(i));
					}
				}else if(tag.equals("goods")){
					if(mLists.get(i).getStatus().equals("1")&&mLists.get(i).getCouponType().equals("4")&&mLists.get(i).getInusing().equals("0")){
						//说明可用
						list_yes.add(mLists.get(i));
					}else {
						//说明不可用
						list_no.add(mLists.get(i));
					}
				}
				
			}
		}
		//用view对象来调用，加载界面
		mSureUseCouponView.setList(list_yes,goodsprice);
		mInvalindView.setList(list_no,goodsprice);
	}
	/**
	 * 解析优惠数据
	 * @param backdata
	 * @return
	 */
	private List<YouHuiBean> useWayJsonData(String backdata) {
		// TODO Auto-generated method stub
		List<YouHuiBean> list=new ArrayList<YouHuiBean>();
		try {
			JSONArray array=new JSONArray(backdata);
			int count=array.length();
			for(int i=0;i<count;i++){
				JSONObject obj=array.getJSONObject(i);
				YouHuiBean bean=new YouHuiBean();
				if(obj.has("beginTime")){
					bean.setBeginTime(obj.getString("beginTime"));
				}
				if(obj.has("couponCondition")){
					bean.setCouponCondition(obj.getString("couponCondition"));
				}
				if(obj.has("couponPrice")){
					bean.setCouponPrice(obj.getString("couponPrice"));
				}
				if(obj.has("couponType")){
					bean.setCouponType(obj.getString("couponType"));
				}
				if(obj.has("endTime")){
					bean.setEndTime(obj.getString("endTime"));
				}
				if(obj.has("id")){
					bean.setId(obj.getString("id"));
				}
				if(obj.has("status")){
					bean.setStatus(obj.getString("status"));
				}
				if(obj.has("myCouponId")){
					bean.setMyCouponId(obj.getString("myCouponId"));
				}
				if(obj.has("inusing")){
					bean.setInusing(obj.getString("inusing"));
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
	private void initDate() {
		pagerList = new ArrayList<View>();
		mSureUseCouponView = new SureUseCouponView(UseCounponActivity.this);
		pagerList.add(mSureUseCouponView.getView());
		mInvalindView = new InvalindView(UseCounponActivity.this);
		pagerList.add(mInvalindView.getView());
	}

	private void initview() {
		mBack=(ImageView) findViewById(R.id.coupon_back);
		mBack.setOnClickListener(this);
		t_order_wait_pay = (TextView) findViewById(R.id.coupon_t_order_wait_pay);
		t_order_wait_send = (TextView) findViewById(R.id.coupon_t_order_wait_send);
		tabImgView = (View) findViewById(R.id.coupon_tabImgView);
		LayoutParams params = (LayoutParams) tabImgView.getLayoutParams();
		params.width = GlobalParams.WIN_WIDTH /2;
		tabImgView.setLayoutParams(params);
		categoryPager = (ViewPager) findViewById(R.id.coupon_pager);
		categoryPager.setOffscreenPageLimit(1);
		initDate();
		categoryAdapter = new CategoryPageAdapter();
		categoryPager.setAdapter(categoryAdapter);
		setSelectPager(0);
	}
	/**
	 * 网络获取优惠券数据
	 */
	private void useWayGetData() {
		// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			Map<String, String> params = new HashMap<String, String>();
			params.put("couponType",""); 
			params.put("status",""); 
			GetDataFromInternet.getInStance().interViewNet(
					ConstantValue.YOUHUIQUAN_LIST, params, new GetDatas() {

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
										Log.i("优惠券信息",
												"走到这里了么？"+ data.getNetResultCode()
														+ backdata);
											Message sendMessage = new Message();
											sendMessage.arg1 = 1;
											sendMessage.obj = backdata;
											handler.sendMessage(sendMessage);
									}
								}

							}
						}
					}, false,false);

	}
	private class CategoryPageAdapter extends PagerAdapter {

		public Object instantiateItem(ViewGroup container, int position) {
			View view = null;
			view = pagerList.get(position);
			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View view = pagerList.get(position);
			container.removeView(view);
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	/**
	 * 设置选中项
	 */
	public void setSelectPager(int position) {
		TranslateAnimation animation = new TranslateAnimation(lastPager
				* GlobalParams.WIN_WIDTH / 2, position * GlobalParams.WIN_WIDTH
				/ 2, 0, 0);
		animation.setFillAfter(true);
		tabImgView.startAnimation(animation);
		lastPager = position;
		switch (position) {
		case ORDER_WAIT_PAY:
			t_order_wait_pay.setTextColor(getResources().getColor(
					R.color.service_select_txt));
			categoryPager.setCurrentItem(ORDER_WAIT_PAY);
			break;
		case ORDER_WAIT_SEND:
			t_order_wait_send.setTextColor(getResources().getColor(
					R.color.service_select_txt));
			categoryPager.setCurrentItem(ORDER_WAIT_SEND);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.coupon_t_order_wait_pay:
			categoryPager.setCurrentItem(ORDER_WAIT_PAY);
			break;
		case R.id.coupon_t_order_wait_send:
			categoryPager.setCurrentItem(ORDER_WAIT_SEND);
			break;
		case R.id.coupon_back:
			finish();
			break;

		default:
			break;
		}

	}
	@Override
	protected void onDestroy() {
		if(handler!=null){
			handler.removeCallbacksAndMessages(null);
		}
		super.onDestroy();
	}
}
