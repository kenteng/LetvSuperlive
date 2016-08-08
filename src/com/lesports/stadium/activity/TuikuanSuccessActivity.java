package com.lesports.stadium.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lesports.stadium.R;
import com.lesports.stadium.base.BaseActivity;
import com.lesports.stadium.bean.OrderListBean;
/**
 * 
 * ***************************************************************
 * 
 * @Desc : 退款成功界面
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
public class TuikuanSuccessActivity extends BaseActivity implements OnClickListener{
	
	/**
	 * 顶部返回按钮
	 */
	private ImageView mImageBack;
	/**
	 * 商品退款金额
	 */
	private TextView mTuikuanMoney;
	/**
	 * 退款时间
	 */
	private TextView mTuikuanTime;
	/**
	 * 退款商品名称
	 */
	private TextView mTuikuanGoodsName;
	/**
	 * 退款原因
	 */
	private TextView mTuikuanYuanyin;
	/**
	 * 退款金额
	 */
	private TextView mTuikuanJine;
	/**
	 * 退款说明
	 */
	private TextView mTuikuanShuoming;
	/**
	 * 申请时间
	 */
	private TextView mShenQIngshijian;
	/**
	 * 返回首页
	 */
	private TextView mFanhuishouye;
	/**
	 * 说明
	 */
	private String mShuoming;
	/**
	 * 金额
	 */
	private String mJine;
	/**
	 * 原因
	 */
	private String mYuanyin;
	/**
	 * 实体类bean
	 */
	private OrderListBean mBean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tuikuanchenggong);
		initview();
		initgetdata();
	}
	/**
	 * 获取上个界面传递过来的时间
	 */
	private void initgetdata() {
		// TODO Auto-generated method stu
		Intent intent=getIntent();
		mYuanyin=intent.getStringExtra("yuanyin");
		mShuoming=intent.getStringExtra("shuoming");
		mJine=intent.getStringExtra("jine");
		mBean=(OrderListBean) intent.getSerializableExtra("order");
		addDataToView();
	}
	/**
	 * 将数据加载到控件上
	 */
	private void addDataToView() {
		// TODO Auto-generated method stub
		mTuikuanYuanyin.setText(mYuanyin);
		mTuikuanShuoming.setText(mShuoming);
		mTuikuanJine.setText("￥："+mJine);
		mTuikuanMoney.setText(mBean.getPayMent());
		mTuikuanGoodsName.setText(mBean.getList().get(0).getGoodsName());
	}
	/**
	 * 初始化view
	 */
	private void initview() {
		// TODO Auto-generated method stub
		mImageBack=(ImageView) findViewById(R.id.tuikuanchenggong_back);
		mImageBack.setOnClickListener(this);
		mTuikuanMoney=(TextView) findViewById(R.id.tuikuanchenggong_jine);
		mTuikuanTime=(TextView) findViewById(R.id.tuikuanchenggong_time);
		mTuikuanGoodsName=(TextView) findViewById(R.id.tuikuan_shangpinmingcheng);
		mTuikuanYuanyin=(TextView) findViewById(R.id.tuikuanchenggong_tuikuan_tuikuanyuanyin);
		mTuikuanJine=(TextView) findViewById(R.id.tuikuan_tuikuanjine_chenggonghou);
		mTuikuanShuoming=(TextView) findViewById(R.id.tuikuan_tuikuanshuoming);
		mShenQIngshijian=(TextView) findViewById(R.id.tuikuan_shenqingshijian);
		mFanhuishouye=(TextView) findViewById(R.id.tuikuanchengggong_fanhuishouye);
		mFanhuishouye.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tuikuanchenggong_back:
			TuikuanSuccessActivity.this.finish();
			break;
		case R.id.tuikuanchengggong_fanhuishouye:
			TuikuanSuccessActivity.this.finish();
			if(TuiKuanActivity.instance!=null){
				TuiKuanActivity.instance.finish();
			}
			break;

		default:
			break;
		}
	}

}
