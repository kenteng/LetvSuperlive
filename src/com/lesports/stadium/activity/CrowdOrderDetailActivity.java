package com.lesports.stadium.activity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lesports.stadium.R;
import com.lesports.stadium.adapter.ReportbackExpandableListViewAdapter;
import com.lesports.stadium.base.BaseActivity;
import com.lesports.stadium.bean.ReportBackChildBean;
import com.lesports.stadium.bean.ReportBackGroupBean;
import com.lesports.stadium.view.MyScrollView;
import com.lesports.stadium.view.ResID;
import com.lesports.stadium.view.MyScrollView.OnScrollListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * ***************************************************************
 * 
 * @ClassName: CrowdOrderDetailActivity
 * 
 * @Desc : 众筹订单详情界面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : lwc
 * 
 * @data:2016-1-29 上午11:38:42
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 ****************************************************************
 */
public class CrowdOrderDetailActivity extends BaseActivity implements OnScrollListener{
	
	/**
	 * 需要悬停在屏幕顶端的布局
	 */
	private RelativeLayout mLayoutXT;
	/**
	 * 当前悬停布局所在父布局
	 */
	private RelativeLayout mXuanDangqian;
	/**
	 * 滑动后悬停布局所在布局
	 */
	private LinearLayout mXuanHuadonghou;
	/**
	 * 动态获取当前悬停布局的顶部位置
	 */
	private int searchLayoutTop;
	/**
	 * 自定义scrollview，用来控制部分布局悬停
	 */
	private MyScrollView mScrollview;
	/**
	 * 展示回报数据的适配器
	 */
	private ReportbackExpandableListViewAdapter mReportbackAdapter;
	/**
	 * 展示回报数据的listview
	 */
	private ExpandableListView mListviewReportback;
	/**
	 * // 组元素数据列表——回报
	 */
	private List<ReportBackGroupBean> groups = new ArrayList<ReportBackGroupBean>();
	/**
	 * // 子元素数据列表——回报
	 */
	private Map<String, List<ReportBackChildBean>> children = new HashMap<String, List<ReportBackChildBean>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ordercrowdfunding_detail);
		MobclickAgent.onEvent(CrowdOrderDetailActivity.this,"CrowdFundingOrder");
		initviews();
	}
	/**
	 * 初始化view控件
	 */
	private void initviews() {
		// TODO Auto-generated method stub
		mLayoutXT=(RelativeLayout) findViewById(R.id.order_crowdfunding_xuanting);
		mXuanDangqian=(RelativeLayout) findViewById(R.id.order_crowdfunding_layout);
		mXuanHuadonghou=(LinearLayout) findViewById(R.id.order_search01);
		mScrollview=(MyScrollView) findViewById(R.id.order_crowdfunding_scrollview);
		mScrollview.setOnScrollListener(this);
		mListviewReportback=(ExpandableListView) findViewById(R.id.order_crowdfunding_listview_huibao);
//		initdataofreport();
	}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			searchLayoutTop = mXuanDangqian.getTop();// 获取悬停布局的顶部位置
		}
	}
	@Override
	public void onScroll(int scrollY) {
		// TODO Auto-generated method stub
		if (scrollY >= searchLayoutTop) {
			if (mLayoutXT.getParent() != mXuanHuadonghou) {
				mXuanDangqian.removeView(mLayoutXT);
				mXuanHuadonghou.addView(mLayoutXT);
			}
		} else {
			if (mLayoutXT.getParent() != mXuanDangqian) {
				mXuanHuadonghou.removeView(mLayoutXT);
				mXuanDangqian.addView(mLayoutXT);
			}
		}
	}

}
