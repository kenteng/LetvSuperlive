package com.lesports.stadium.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.lesports.stadium.R;
import com.lesports.stadium.adapter.OrderDetailExpandableListViewAdapter;
import com.lesports.stadium.adapter.OrderExpandableListViewAdapter;
import com.lesports.stadium.base.BaseActivity;
import com.lesports.stadium.bean.CouponBean;
import com.lesports.stadium.bean.OrderListBean;
import com.lesports.stadium.bean.ShippingAddressBean;
import com.lesports.stadium.view.CustomExpandableListView;
/**
 * 
 * ***************************************************************
 * 
 * @Desc : 众筹订单详情待收货
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
public class OrderDetailWaitRepecipt extends BaseActivity implements OnClickListener{
	
	/**
	 * 顶部返回按钮按钮
	 */
	private ImageView mBack;
	/**
	 * 选择地址的布局
	 */
	private RelativeLayout mLayout_choiseAddress;
	/**
	 * 收货人
	 */
	private TextView mTakeGoodsName;
	/**
	 * 地址
	 */
	private TextView mTakeGoodsAddress;
	/**
	 * 邮政编码
	 */
	private TextView mTakeGoodsEmail;
	/**
	 * 电话
	 */
	private TextView mTakeGoodsPhone;
	/**
	 * 展示订单商品数据的listveiw
	 */
	private CustomExpandableListView mExListview;
	/**
	 * 数据源
	 */
	private List<OrderListBean> list=new ArrayList<OrderListBean>();
	/**
	 * 适配器
	 */
	private OrderDetailExpandableListViewAdapter mAdapter;
	/**
	 * 众筹代支付状态图标
	 */
	private ImageView mStatus;
	/**
	 * 标记
	 */
	private String tag="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_waitgoodsreceipt);
		initViews();
		initData();
		if(list!=null&&list.size()!=0&&!TextUtils.isEmpty(tag)){
			mAdapter=new OrderDetailExpandableListViewAdapter(list, OrderDetailWaitRepecipt.this,tag);
			mExListview.setAdapter(mAdapter);
			for (int i = 0; i < mAdapter.getGroupCount(); i++)
			{
				mExListview.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
			}
			mExListview.setOnGroupClickListener(new OnGroupClickListener() {
	            @Override
	            public boolean onGroupClick(ExpandableListView parent, View v,
	                    int groupPosition, long id) {
	                
	                return true;
	            }
	        });
		}
	}
	/**
	 * 初始化数据
	 */
	private void initData() {
		// TODO Auto-generated method stub
		Intent intent=getIntent();
		OrderListBean bean=(OrderListBean) intent.getSerializableExtra("bean");
		if(bean!=null){
			list.add(bean);
			mTakeGoodsName.setText(bean.getCompanies());
			mTakeGoodsAddress.setText("收货地址："+bean.getPosition());
			mTakeGoodsEmail.setVisibility(View.GONE);
			mTakeGoodsPhone.setText(bean.getTelePhone());
		}
		tag=intent.getStringExtra("tag");
	}
	/**
	 * 初始化界面
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		mBack=(ImageView) findViewById(R.id.crowd_wait_goods_receipt_back);
		mBack.setOnClickListener(this);
		mLayout_choiseAddress=(RelativeLayout) findViewById(R.id.order_daifukuan_address_choise);
//		mLayout_choiseAddress.setOnClickListener(this);
		mTakeGoodsName=(TextView) findViewById(R.id.order_daifukuan_dingdanzhifu_shouhuoren_name);
		mTakeGoodsAddress=(TextView) findViewById(R.id.order_daifukuan_dingdanzhifu_shouhuoren_dizhi);
		mTakeGoodsEmail=(TextView) findViewById(R.id.order_daifukuan_dingdanzhifu_shouhuoren_youxiang);
		mTakeGoodsPhone=(TextView) findViewById(R.id.order_daifukuan_dingdanzhifu_shouhuoren_dianhua);
		mExListview=(CustomExpandableListView) findViewById(R.id.order_all_daifukuan_listview);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.crowd_wait_goods_receipt_back:
			finish();
			break;

		default:
			break;
		}
	}

}
