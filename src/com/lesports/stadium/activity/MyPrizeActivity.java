package com.lesports.stadium.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.lesports.stadium.R;
import com.lesports.stadium.bean.PrizeBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.net.NetLoadListener;
import com.lesports.stadium.net.NetService;
import com.lesports.stadium.net.Code.HttpSetting;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.view.AllPrizeView;
import com.lesports.stadium.view.ReceiverPrizeView;
import com.lesports.stadium.view.UnReceiverPrizeView;

/**
 * 我的奖品界面
 * 
 * @author Administrator
 * 
 */
public class MyPrizeActivity extends Activity implements OnClickListener {
	private View tabImgView;
	/*
	 * 全部
	 */
	private TextView t_order_all;
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
	 * 全部
	 */
	private static final int ORDER_ALL = 0;
	/**
	 * 未接收
	 */
	private static final int ORDER_WAIT_PAY = 1;
	/**
	 * 已接收
	 */
	private static final int ORDER_WAIT_SEND = 2;
	/*
	 * 存储数据
	 */
	private ArrayList<View> pagerList;
	/**
	 * 选项卡最后选择的是哪个
	 */
	private int lastPager = 0;
	/*
	 * 所有奖品界面view
	 */
	private AllPrizeView allPrizeView;
	/*
	 * 已领取界面view
	 */
	private ReceiverPrizeView receiverPrizeView;
	/*
	 * 未领取界面view
	 */
	private UnReceiverPrizeView unReceiverPrizeView;
	public static String data = "data";
	/**
	 * 获取数据成功
	 */
	private final int FILER = 110;
	/**
	 * 获取数据失败
	 */
	private final int SCUSS = 111;
	/**
	 * 所有奖品
	 */
	private ArrayList<PrizeBean> allPrize;
	/**
	 * 已领取奖品
	 */
	private ArrayList<PrizeBean> receiverPrize;
	/**
	 * 未领取奖品
	 */
	private ArrayList<PrizeBean> unReceiverPrize;
	/**
	 * 当前对象的实例对象，便于在商品详情界面修改未领取为已领取
	 */
	public static MyPrizeActivity instalce;

	private TextView titleTv;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FILER:

				break;
			case SCUSS:
				analyze((String) msg.obj);
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myprize);
		instalce = this;
		initview();
		initNetDate();
		initListener();
	}

	// 获取获取数据
	private void initNetDate() {
		NetService netService = NetService.getInStance();
		Pattern pattern = Pattern.compile("[0-9]*");
		if(!TextUtils.isEmpty(GlobalParams.USER_ID)){
			Matcher isNum = pattern.matcher(GlobalParams.USER_ID);
			if (isNum.matches())
				netService.headers.setUid(Integer.parseInt(GlobalParams.USER_ID));
		}
		netService.setHttpMethod(HttpSetting.POST_MODE);
		netService.setUrl(ConstantValue.PRICE_LIST);
		netService.setUseCache(false);
		netService.loader(new NetLoadListener() {
			@Override
			public void onloaderListener(BackData result) {
				Message msg = new Message();
				if (data != null) {
					Object obj = result.getObject();
					if (obj != null) {
						msg.what = SCUSS;
						msg.obj = obj;
						Log.i("wxn", "ojbj 奖品列表：" + obj);
						handler.sendMessage(msg);
						return;
					}
				}
				msg.what = FILER;
				handler.sendMessage(msg);
			}
		});
	}

	// 解析返回来的数据
	private void analyze(String content) {
		try {
			JSONArray jsonArr = new JSONArray(content);
			allPrize = new ArrayList<>();
			receiverPrize = new ArrayList<>();
			unReceiverPrize = new ArrayList<>();
			for (int i = 0; i < jsonArr.length(); i++) {
				PrizeBean bean = new PrizeBean();
				JSONObject temp = (JSONObject) jsonArr.get(i);
				if (temp.has("amount"))
					bean.amount = (temp.getString("amount"));
				if (temp.has("deliveryId"))
					bean.deliveryId = (temp.getString("deliveryId"));
				if (temp.has("prizeId"))
					bean.prizeId = (temp.getString("prizeId"));
				if (temp.has("id"))
					bean.id = (temp.getString("id"));
				if (temp.has("prize")) {
					JSONObject temps = (JSONObject) temp.getJSONObject("prize");
					if (temps.has("image")) {
						bean.image = (temps.getString("image"));
					}
					if (temps.has("name")) {
						bean.name = (temps.getString("name"));
					}
					if (temps.has("price")) {
						bean.price = (temps.getString("price"));
					}
					if (temps.has("remark")) {
						bean.remark = (temps.getString("remark"));
					}
					if (temps.has("startTime")) {
						bean.startTime = (temps.getString("startTime"));
					}
					if (temps.has("endTime")) {
						bean.endTime = (temps.getString("endTime"));
					}
					if (temps.has("type")) {
						bean.type = (temps.getString("type"));
					}
					if (temps.has("contentId")) {
						bean.contentId = (temps.getString("contentId"));
					}
					if (temps.has("integral")) {
						bean.integral = (temps.getString("integral"));
					}
					
				}
				if (temp.has("status")) {
					String stats = temp.getString("status");
					bean.status = (stats);
					allPrize.add(bean);
					if ("2".equals(bean.type)||"3".equals(bean.type)) {
						// 是虚拟物品默认是已领取
						receiverPrize.add(bean);
					} else {
						// 是实物
						if ("0".equals(stats)) {
							unReceiverPrize.add(bean);
						} else if ("1".equals(stats)) {
							receiverPrize.add(bean);
						}
					}
				}
			}
			allPrizeView.setDate(allPrize);
			receiverPrizeView.setDate(receiverPrize);
			unReceiverPrizeView.setDate(unReceiverPrize);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void initListener() {
		findViewById(R.id.title_left_iv).setOnClickListener(this);
		t_order_all.setOnClickListener(this);
		t_order_wait_pay.setOnClickListener(this);
		t_order_wait_send.setOnClickListener(this);
		categoryPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				TranslateAnimation animation = new TranslateAnimation(lastPager
						* GlobalParams.WIN_WIDTH / 3, position
						* GlobalParams.WIN_WIDTH / 3, 0, 0);

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
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		titleTv = (TextView) findViewById(R.id.tv_title);
		titleTv.setText("我的奖品");
	}

	private void initDate() {
		pagerList = new ArrayList<View>();
		// 全部
		allPrizeView = new AllPrizeView(this);
		pagerList.add(allPrizeView.getView());
		// 未领取
		unReceiverPrizeView = new UnReceiverPrizeView(this);
		pagerList.add(unReceiverPrizeView.getView());
		// 已领取
		receiverPrizeView = new ReceiverPrizeView(this);
		pagerList.add(receiverPrizeView.getView());

	}

	private void initview() {
		t_order_all = (TextView) findViewById(R.id.t_order_all);
		t_order_wait_pay = (TextView) findViewById(R.id.t_order_wait_pay);
		t_order_wait_send = (TextView) findViewById(R.id.t_order_wait_send);
		tabImgView = (View) findViewById(R.id.tabImgView);
		LayoutParams params = (LayoutParams) tabImgView.getLayoutParams();
		params.width = GlobalParams.WIN_WIDTH / 3;
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
			return 3;
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
		case R.id.title_left_iv:
			// 返回按键
			finish();
			break;

		default:
			break;
		}

	}

	/**
	 * 将一个传入的字符串毫秒值转换成一个字符串时间值
	 * 
	 * @param str
	 * @return
	 */
	public static String getChangeToTime(String str) {
		if (TextUtils.isEmpty(str))
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd",
				Locale.getDefault());
		long time = Long.parseLong(str);
		Date date = new Date();
		date.setTime(time);
		String timestring = sdf.format(date);
		return timestring;

	}

	/**
	 * 修改奖品为已领取
	 */
	public void modifyState(String prize_id, String id, String addressId) {
		if (allPrize == null || TextUtils.isEmpty(id))
			return;
		for (int i = 0; i < allPrize.size(); i++) {
			PrizeBean bean = allPrize.get(i);
			if (prize_id.equals(bean.prizeId) && id.equals(bean.id)) {
				unReceiverPrize.remove(bean);
				bean.status = ("1");
				bean.deliveryId = (addressId);
				receiverPrize.add(bean);
				break;
			}
		}
	}

	@Override
	protected void onResume() {
		if(allPrize!=null)
			allPrizeView.setDate(allPrize);
		if(receiverPrize!=null)
			receiverPrizeView.setDate(receiverPrize);
		if(unReceiverPrize!=null)
			unReceiverPrizeView.setDate(unReceiverPrize);
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		handler.removeCallbacksAndMessages(null);
		instalce = null;
		if (pagerList != null) {
			pagerList.clear();
			pagerList = null;
		}
		if (allPrize != null) {
			allPrize.clear();
			allPrize = null;
		}
		if (receiverPrize != null) {
			receiverPrize.clear();
			receiverPrize = null;
		}
		if (unReceiverPrize != null) {
			unReceiverPrize.clear();
			unReceiverPrize = null;
		}
		allPrizeView = null;
		receiverPrizeView = null;
		unReceiverPrizeView = null;
		super.onDestroy();
	}
}
