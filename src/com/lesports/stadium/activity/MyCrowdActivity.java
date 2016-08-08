package com.lesports.stadium.activity;

import java.util.ArrayList;

import com.lesports.stadium.R;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.view.AllCrowdView;
import com.lesports.stadium.view.AllOrderView;
import com.lesports.stadium.view.WaitPayCrowdView;
import com.lesports.stadium.view.WaitPayOrderView;
import com.lesports.stadium.view.WaitReceiverCrowdView;
import com.lesports.stadium.view.WaitReceiverOrderView;
import com.lesports.stadium.view.WaitSendCrowdView;
import com.lesports.stadium.view.WaitSendOrderView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

public class MyCrowdActivity extends Activity implements OnClickListener {
	private View tabImgView;
	/*
	 * 所有订单
	 */
	private TextView t_order_all;
	/*
	 * 待支付订单
	 */
	private TextView t_order_wait_pay;
	/*
	 * 待发送
	 */
	private TextView t_order_wait_send;
	/*
	 * 待接收
	 */
	private TextView t_order_wait_receiver;
	/*
	 * VewPager 对象
	 */
	private ViewPager categoryPager;
	/*
	 * viewPager适配器
	 */
	private CategoryPageAdapter categoryAdapter;

	/**
	 * 所有订单
	 */
	private static final int ORDER_ALL = 0;
	/**
	 * 待支付订单
	 */
	private static final int ORDER_WAIT_PAY = 1;
	/**
	 * 待发送订单
	 */
	private static final int ORDER_WAIT_SEND = 2;
	/**
	 * 待收获订单
	 */
	private static final int ORDER_WAIT_RECEIVER = 3;
	/*
	 * 存储数据
	 */
	private ArrayList<View> pagerList;
	/**
	 * 选项卡最后选择的是哪个
	 */
	private int lastPager = 0;
	/*
	 * 众筹 所有订单界面view
	 */
	private AllCrowdView allOrderView;
	/*
	 * 众筹 待支付订单界面view
	 */
	private WaitPayCrowdView waitPayOrderView;
	/*
	 * 众筹 待发送订单界面view
	 */
	private WaitSendCrowdView waitSendOrderView;
	/*
	 * 众筹 待接收订单界面view
	 */
	private WaitReceiverCrowdView waitReceiverOrderView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_allorder);
		initview();
		initListener();
	}

	private void initListener() {
		t_order_all.setOnClickListener(this);
		t_order_wait_pay.setOnClickListener(this);
		t_order_wait_receiver.setOnClickListener(this);
		t_order_wait_send.setOnClickListener(this);
		categoryPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				TranslateAnimation animation = new TranslateAnimation(lastPager
						* GlobalParams.WIN_WIDTH / 4, position
						* GlobalParams.WIN_WIDTH / 4, 0, 0);

				animation.setDuration(300);
				animation.setFillAfter(true);
				tabImgView.startAnimation(animation);

				lastPager = position;

				t_order_all.setTextColor(getResources().getColor(
						R.color.word_gray));
				t_order_wait_pay.setTextColor(getResources().getColor(
						R.color.word_gray));
				t_order_wait_send.setTextColor(getResources().getColor(
						R.color.word_gray));
				t_order_wait_receiver.setTextColor(getResources().getColor(
						R.color.word_gray));

				switch (position) {
				case ORDER_ALL:
					t_order_all.setTextColor(getResources().getColor(
							R.color.service_select_txt));
					break;
				case ORDER_WAIT_PAY:
					t_order_wait_pay.setTextColor(getResources().getColor(
							R.color.service_select_txt));
					break;
				case ORDER_WAIT_SEND:
					t_order_wait_send.setTextColor(getResources().getColor(
							R.color.service_select_txt));
					break;
				case ORDER_WAIT_RECEIVER:
					t_order_wait_receiver.setTextColor(getResources().getColor(
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

	private void initDate() {
		pagerList = new ArrayList<View>();
		allOrderView = new AllCrowdView(this);
		pagerList.add(allOrderView.getView());
		waitPayOrderView = new WaitPayCrowdView(this);
		pagerList.add(waitPayOrderView.getView());
		waitSendOrderView = new WaitSendCrowdView(this);
		pagerList.add(waitSendOrderView.getView());
		waitReceiverOrderView = new WaitReceiverCrowdView(this);
		pagerList.add(waitReceiverOrderView.getView());

	}

	private void initview() {
		t_order_all = (TextView) findViewById(R.id.t_order_all);
		t_order_wait_pay = (TextView) findViewById(R.id.t_order_wait_pay);
		t_order_wait_send = (TextView) findViewById(R.id.t_order_wait_send);
		t_order_wait_receiver = (TextView) findViewById(R.id.t_order_wait_receiver);
		tabImgView = (View) findViewById(R.id.tabImgView);
		LayoutParams params = (LayoutParams) tabImgView.getLayoutParams();
		params.width = GlobalParams.WIN_WIDTH / 4;
		tabImgView.setLayoutParams(params);
		categoryPager = (ViewPager) findViewById(R.id.pager);
		categoryPager.setOffscreenPageLimit(1);
		initDate();
		categoryAdapter = new CategoryPageAdapter();
		categoryPager.setAdapter(categoryAdapter);
		setSelectPager(0);

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
			return 4;
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
				* GlobalParams.WIN_WIDTH / 3, position * GlobalParams.WIN_WIDTH
				/ 3, 0, 0);
		animation.setFillAfter(true);
		tabImgView.startAnimation(animation);
		lastPager = position;
		switch (position) {
		case ORDER_ALL:
			t_order_all.setTextColor(getResources().getColor(
					R.color.service_select_txt));
			categoryPager.setCurrentItem(ORDER_ALL);
			break;
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
		case ORDER_WAIT_RECEIVER:
			t_order_wait_receiver.setTextColor(getResources().getColor(
					R.color.service_select_txt));
			categoryPager.setCurrentItem(ORDER_WAIT_RECEIVER);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.t_order_all:
			categoryPager.setCurrentItem(ORDER_ALL);
			break;
		case R.id.t_order_wait_pay:
			categoryPager.setCurrentItem(ORDER_WAIT_PAY);
			break;
		case R.id.t_order_wait_send:
			categoryPager.setCurrentItem(ORDER_WAIT_SEND);
			break;
		case R.id.t_order_wait_receiver:
			categoryPager.setCurrentItem(ORDER_WAIT_RECEIVER);
			break;

		default:
			break;
		}

	}
	@Override
	protected void onDestroy() {
		if(pagerList!=null){
			pagerList.clear();
			pagerList = null;
		}
		allOrderView = null;
		waitPayOrderView = null;
		waitSendOrderView = null;
		waitReceiverOrderView = null;
		super.onDestroy();
	}
}
