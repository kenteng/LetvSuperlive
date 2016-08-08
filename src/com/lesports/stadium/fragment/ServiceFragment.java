/**
 * 
 */
package com.lesports.stadium.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lesports.stadium.R;
import com.lesports.stadium.base.BaseFragment;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.view.MyViewPager;
import com.lesports.stadium.view.ServiceCarView;
import com.lesports.stadium.view.ServiceFoodView;
import com.lesports.stadium.view.ServiceGoodView;
import com.umeng.analytics.MobclickAgent;

/**
 * ***************************************************************
 * 
 * @ClassName: ServiceFragment
 * 
 * @Desc : 服务界面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 王新年
 * 
 * @data:2016-1-29 上午11:39:29
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class ServiceFragment extends BaseFragment implements OnClickListener {
	/**
	 * 顶部标题
	 */
	private TextView mTitle;

	/**
	 * 餐饮
	 */
	private TextView t_service_food;
	/**
	 * 导航
	 */
	private TextView t_service_navigation;
	/**
	 * 商品
	 */
	private TextView t_service_goods;
	/**
	 * 用车
	 */
	private TextView t_service_car;
	/**
	 * 下划线
	 */
	private View tabImgView;
	/**
	 * 当前选中的是餐饮
	 */
	private static final int SERVICE_FOOD = 0;
	/**
	 * 当前选中的是导航
	 */
	private static final int SERVICE_NAVIGATION = 1;
	/**
	 * 当前选中的是商品
	 */
	private static final int SERVICE_GOODS = 1;
	/**
	 * 当前选中的是用车
	 */
	private static final int SERVICE_CAR = 2;
	/**
	 * 选项卡最后选择的是哪个
	 */
	private int lastPager = 0;
	/**
	 * 餐饮View对象
	 */
	private ServiceFoodView foodView;
	/**
	 * 导航View对象
	 */
//	public ServiceNavigationView navigationView;
	/**
	 * 购物View对象
	 */
	private ServiceGoodView goodView;
	/**
	 * 用车View对象
	 */
	private ServiceCarView carView;
	/**
	 * ViewPager 对象
	 */
	private MyViewPager categoryPager;

	/**
	 * MyViewPager 适配器
	 */
	private CategoryPageAdapter categoryAdapter;
	/**
	 * 用来接收或者标记是否是需要来选座的
	 */
	private boolean IsUse = false;
	/**
	 * 封装view的对象
	 */
	private List<View> pagerList;
	/**
	 * 当前展示的是否是导航界面
	 */
	private boolean currentIsNaviView = false;
	/**
	 * 用车时间
	 */
	private PopupWindow pop = null;
	private LinearLayout ll_popup;

	/**
	 * 显示view的标记
	 */

	public int tag_views = 0;

	/**
	 * 本类对象
	 */
	public static ServiceFragment instence;

	/**
	 * 构造方法
	 * 
	 * @param tag
	 *            显示那个view的标记
	 */
	public ServiceFragment(int tag) {
		this.tag_views = tag;
		instence = this;
	}

	/**
	 * 无参数构造方法
	 */
	public ServiceFragment() {
		super();
		instence = this;
	}

	@Override
	public View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_service, null);
		mTitle = (TextView) view.findViewById(R.id.title_center_tv);
		mTitle.setText("服务");
		t_service_food = (TextView) view.findViewById(R.id.t_service_food);
		t_service_navigation = (TextView) view
				.findViewById(R.id.t_service_navigation);
		t_service_goods = (TextView) view.findViewById(R.id.t_service_goods);
		t_service_car = (TextView) view.findViewById(R.id.t_service_car);
		tabImgView = (View) view.findViewById(R.id.tabImgView);
		LayoutParams params = (LayoutParams) tabImgView.getLayoutParams();
		params.width = GlobalParams.WIN_WIDTH / 3;
		tabImgView.setLayoutParams(params);
		categoryPager = (MyViewPager) view.findViewById(R.id.pager);
		categoryPager.setOffscreenPageLimit(5);
		initDate();
		categoryAdapter = new CategoryPageAdapter();
		categoryPager.setAdapter(categoryAdapter);
		setSelectPager(tag_views);
		return view;
	}

	/**
	 * 初始化要展示的view界面
	 */
	private void initDate() {
		pagerList = new ArrayList<View>();
		foodView = new ServiceFoodView(getActivity());
		pagerList.add(foodView.getView());
		goodView = new ServiceGoodView(getActivity());
		pagerList.add(goodView.getView());
		carView = new ServiceCarView(getActivity());
		pagerList.add(carView.getView());
	}

	/**
	 * 设置监听
	 */
	@Override
	public void initListener() {
		t_service_food.setOnClickListener(this);
		t_service_goods.setOnClickListener(this);
		t_service_navigation.setOnClickListener(this);
		t_service_car.setOnClickListener(this);
		categoryPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				TranslateAnimation animation = new TranslateAnimation(lastPager
						* GlobalParams.WIN_WIDTH / pagerList.size(), position
						* GlobalParams.WIN_WIDTH / pagerList.size(), 0, 0);

				animation.setDuration(300);
				animation.setFillAfter(true);
				tabImgView.startAnimation(animation);

				lastPager = position;

				t_service_food.setTextColor(getResources().getColor(
						R.color.word_gray));
				t_service_navigation.setTextColor(getResources().getColor(
						R.color.word_gray));
				t_service_car.setTextColor(getResources().getColor(
						R.color.word_gray));
				t_service_goods.setTextColor(getResources().getColor(
						R.color.word_gray));

				switch (position) {
				case SERVICE_FOOD: // 餐饮
					t_service_food.setTextColor(getResources().getColor(
							R.color.service_select_txt));
					break;
				// case SERVICE_NAVIGATION: //室内导航
				// t_service_navigation.setTextColor(getResources().getColor(
				// R.color.service_select_txt));
				// break;
				case SERVICE_GOODS: // 商品
					t_service_goods.setTextColor(getResources().getColor(
							R.color.service_select_txt));
					break;
				case SERVICE_CAR: // 用车
					t_service_car.setTextColor(getResources().getColor(
							R.color.service_select_txt));
					// 用车页面统计
					MobclickAgent.onEvent(getActivity(), "UseCarPage");
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
	 * MyViewPager 适配器
	 * 
	 * @ClassName: CategoryPageAdapter
	 * 
	 * @Description: TODO(这里用一句话描述这个类的作用)
	 * 
	 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
	 * 
	 * @author wangxinnian
	 * 
	 * @date 2016-7-22 下午3:43:03
	 * 
	 * 
	 */
	private class CategoryPageAdapter extends PagerAdapter {

		public Object instantiateItem(ViewGroup container, int position) {
			View view = pagerList.get(position);
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
			return pagerList == null ? 0 : pagerList.size();
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
				* GlobalParams.WIN_WIDTH / pagerList.size(), position
				* GlobalParams.WIN_WIDTH / pagerList.size(), 0, 0);
		animation.setFillAfter(true);
		tabImgView.startAnimation(animation);
		lastPager = position;
		currentIsNaviView = false;
		switch (position) {
		case SERVICE_FOOD: // 餐饮
			t_service_food.setTextColor(getResources().getColor(
					R.color.service_select_txt));
			categoryPager.setCurrentItem(SERVICE_FOOD);
			break;
		// case SERVICE_NAVIGATION: //室内导航
		// t_service_navigation.setTextColor(getResources().getColor(
		// R.color.service_select_txt));
		// categoryPager.setCurrentItem(SERVICE_NAVIGATION);
		// currentIsNaviView =true;
		// break;
		case SERVICE_GOODS: // 商品
			t_service_goods.setTextColor(getResources().getColor(
					R.color.service_select_txt));
			categoryPager.setCurrentItem(SERVICE_GOODS);
			break;
		case SERVICE_CAR: // 用车
			t_service_car.setTextColor(getResources().getColor(
					R.color.service_select_txt));
			categoryPager.setCurrentItem(SERVICE_CAR);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		int viewID = v.getId();
		currentIsNaviView = false;
		switch (viewID) {
		case R.id.t_service_food: // 餐饮
			tag_views = 0;
			categoryPager.setCurrentItem(SERVICE_FOOD);
			break;
		// case R.id.t_service_navigation: //室内导航
		// tag_views=1;
		// currentIsNaviView =true;
		// categoryPager.setCurrentItem(SERVICE_NAVIGATION);
		// break;
		case R.id.t_service_goods: // /商品
			tag_views = 1;
			categoryPager.setCurrentItem(SERVICE_GOODS);
			// 同时调用商品view里面的resume方法，刷新购物车数量
			ServiceGoodView.instance.changeVIew();
			break;
		case R.id.t_service_car: // 用车
			tag_views = 2;
			categoryPager.setCurrentItem(SERVICE_CAR);
			break;

		default:
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		carView.onResume();
		setSelectPager(tag_views);
		ServiceGoodView.instance.changeVIew();
	}

	/**
	 * 当前展示的是否是当前View
	 * 
	 * @return
	 */
	public boolean curentIsNaVie() {
		return currentIsNaviView;
	}

	/**
	 * 展示用车界面
	 */
	public void displayCarTime() {
		ll_popup.startAnimation(AnimationUtils.loadAnimation(getActivity(),
				R.anim.activity_translate_in));
		pop.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);
	}

	@Override
	public void onDestroy() {
		destroyObject();
		super.onDestroy();
	}

	/**
	 * 需要销毁的数据
	 */
	private void destroyObject() {
		if (pagerList != null) {
			pagerList.clear();
			pagerList = null;
		}
		tabImgView = null;
		foodView = null;
//		navigationView = null;
		goodView = null;
		categoryPager = null;
		instence = null;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	}

	@Override
	public void initData(Bundle savedInstanceState) {

	}

}
