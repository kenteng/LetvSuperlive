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
import android.widget.ExpandableListView.OnGroupClickListener;

import com.lesports.stadium.R;
import com.lesports.stadium.adapter.OrderDetailExpandableListViewAdapter;
import com.lesports.stadium.base.BaseActivity;
import com.lesports.stadium.bean.OrderListBean;
import com.lesports.stadium.bean.ShippingAddressBean;
import com.lesports.stadium.view.CustomExpandableListView;

public class WaitTakeGoodsActivity extends BaseActivity implements OnClickListener {
	
	/**
	 * 顶部返回按钮
	 */
	private ImageView mBack;
	/**
	 * 列表项
	 */
	private CustomExpandableListView mEXlistview;
	/**
	 * 选择地址的布局
	 */
	private RelativeLayout mLayout_choise;
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
	 * 标记
	 */
	private String tag="";
	/**
	 * 适配器
	 */
	private OrderDetailExpandableListViewAdapter mAdapter;
	/**
	 * 数据源
	 */
	private List<OrderListBean> list=new ArrayList<OrderListBean>();
	/**
	 * 用于控制界面显示类型的标记
	 */
	private String type;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wait_take_goods);
		initviews();
		 initData();
		 initShow(type);
		 if(list!=null&&list.size()!=0&&!TextUtils.isEmpty(tag)){
				mAdapter=new OrderDetailExpandableListViewAdapter(list, WaitTakeGoodsActivity.this,tag);
				mEXlistview.setAdapter(mAdapter);
				for (int i = 0; i < mAdapter.getGroupCount(); i++)
				{
					mEXlistview.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
				}
				mEXlistview.setOnGroupClickListener(new OnGroupClickListener() {
		            @Override
		            public boolean onGroupClick(ExpandableListView parent, View v,
		                    int groupPosition, long id) {
		                
		                return true;
		            }
		        });
			}
	}
	/**
	 * 根据类型来判断界面中的某些控件是否需要显示
	 * @param type2
	 */
	private void initShow(String type2) {
		// TODO Auto-generated method stub
		if(!TextUtils.isEmpty(type2)){
			if(type2.equals("zc")){
				//
			}
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
			mTakeGoodsPhone.setText(bean.getTelePhone());
			mTakeGoodsEmail.setVisibility(View.GONE);
		}
		tag=intent.getStringExtra("tag");
		type=intent.getStringExtra("type");
	}

	private void initviews() {
		// TODO Auto-generated method stub
		mBack=(ImageView) findViewById(R.id.cdengdaimaijiashouhuo_top_back);
		mBack.setOnClickListener(this);
		mEXlistview=(CustomExpandableListView) findViewById(R.id.cdengdaimaijiashouhuo_listview);
		mLayout_choise=(RelativeLayout) findViewById(R.id.cdengdaimaijiashouhuo_choise);
		mLayout_choise.setOnClickListener(this);
		mTakeGoodsAddress=(TextView) findViewById(R.id.cdengdaimaijiashouhuo_shouhuoren_dizhi);
		mTakeGoodsName=(TextView) findViewById(R.id.cdengdaimaijiashouhuo_shouhuoren_name);
		mTakeGoodsEmail=(TextView) findViewById(R.id.cdengdaimaijiashouhuo_shouhuoren_youxiang);
		mTakeGoodsPhone=(TextView) findViewById(R.id.cdengdaimaijiashouhuo_shouhuoren_dianhua);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cdengdaimaijiashouhuo_top_back:
			finish();
			break;
//		case R.id.cdengdaimaijiashouhuo_choise:
//			Intent intents=new Intent(WaitTakeGoodsActivity.this,SelectAddressActivity.class);
//			startActivityForResult(intents, 2);
//			break;

		default:
			break;
		}
		
	}
	/**
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 2:
			if(data!=null){
				ShippingAddressBean selectBean=(ShippingAddressBean) data.getSerializableExtra("addressBean");
				if(selectBean!=null&&!TextUtils.isEmpty(selectBean.getUserAddress())){
					mTakeGoodsName.setText(selectBean.getUserName());
					mTakeGoodsAddress.setText("收货地址："+selectBean.getUserAddress());
					mTakeGoodsEmail.setText("邮政编码："+selectBean.getPostcode());
					mTakeGoodsPhone.setText(selectBean.getUserPhone());
				}
			}

		default:
			break;
		}
	}
	*/

}
