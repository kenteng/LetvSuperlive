package com.lesports.stadium.activity;

import java.io.InputStream;
import java.util.List;

import android.R.string;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lesports.stadium.R;
import com.lesports.stadium.base.BaseActivity;
import com.lesports.stadium.bean.OrderListBean;
import com.lesports.stadium.bean.OrderListBeanGoodsBean;

/**
 * 
 * ***************************************************************
 * 
 * @Desc : 退款订单详情界面
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
public class TuikuanOrderDetailActivity extends BaseActivity implements
		OnClickListener {

	/**
	 * 顶部返回按钮
	 */
	private ImageView mBack;
	/**
	 * 状态标题 申请
	 */
	private TextView mTextStatus0;
	/**
	 * 状态标题 审核
	 */
	private TextView mTextStatus1;
	/**
	 * 状态标题 退款
	 */
	private TextView mTextStatus2;
	/**
	 * 撤销申请
	 */
	private TextView mCheXiaoShenqing;
	/**
	 * 退货商品金额
	 */
	private TextView mTuihuoGoodsPrice;
	/**
	 * 应退金额
	 */
	private TextView mYingtuijine;
	/**
	 * 退款需要隐藏的布局
	 */
	private RelativeLayout mLayoutYincangTwo;
	/**
	 * 退款流程背景
	 */
	private ImageView mImageBackground;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tuikuanliucheng);
		initview();
		InitDatass();
	}

	/**
	 * 初始化数据
	 */
	private void InitDatass() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		String page = intent.getStringExtra("page");
		if (page.equals("three")) {
			String tag = intent.getStringExtra("tag");
			if (tag.equals("8")) {
				mImageBackground.setImageBitmap(readBitMap(
						TuikuanOrderDetailActivity.this, R.drawable.three_one));
			} else if (tag.equals("9") || tag.equals("10")) {
				mImageBackground.setImageBitmap(readBitMap(
						TuikuanOrderDetailActivity.this, R.drawable.three_two));
			} else if (tag.equals("11")) {
				mImageBackground
						.setImageBitmap(readBitMap(
								TuikuanOrderDetailActivity.this,
								R.drawable.three_three));
			}
			String tuihuojine = intent.getStringExtra("jine");
			mYingtuijine.setText("￥" + tuihuojine);
			mTuihuoGoodsPrice.setText("￥" + tuihuojine);
			OrderListBean bean = (OrderListBean) intent
					.getSerializableExtra("bean");
			// 计算商品金额
			// double price=countGoodsPrice(bean);
			// mTuihuoGoodsPrice.setText("￥"+price);
		} else if (page.equals("two")) {
			String tag = intent.getStringExtra("tag");
			mLayoutYincangTwo.setVisibility(View.GONE);
			if (tag.equals("8")) {
				mImageBackground.setImageBitmap(readBitMap(
						TuikuanOrderDetailActivity.this, R.drawable.two_one));
			}
			String tuihuojine = intent.getStringExtra("jine");
			mYingtuijine.setText("￥" + tuihuojine);
			OrderListBean bean = (OrderListBean) intent
					.getSerializableExtra("bean");
			mTuihuoGoodsPrice.setText("￥" + tuihuojine);
			// 计算商品金额
			// double price=countGoodsPrice(bean);
			// mTuihuoGoodsPrice.setText("￥"+price);
		}
	}

	/**
	 * 计算商品金额
	 * 
	 * @param bean
	 */
	private double countGoodsPrice(OrderListBean bean) {
		// TODO Auto-generated method stub
		double goodsPrice = 0;
		if (bean != null) {
			List<OrderListBeanGoodsBean> list = bean.getList();
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					double price = Double.parseDouble(list.get(i).getPrice());
					double num = Double
							.parseDouble(list.get(i).getWareNumber());
					goodsPrice = goodsPrice + (price * num);
				}
			} else {
				return 0;
			}
		} else {
			return 0;
		}
		return goodsPrice;
	}

	/**
	 * 初始化界面view
	 */
	private void initview() {
		// TODO Auto-generated method stub
		mLayoutYincangTwo = (RelativeLayout) findViewById(R.id.layout_tuikuanchenggong_diergebuju);
		mImageBackground = (ImageView) findViewById(R.id.tuikuanliucheng_top_liuchengbeijing);
		mBack = (ImageView) findViewById(R.id.tuikuanliucheng_top_back);
		mBack.setOnClickListener(this);
		mTextStatus0 = (TextView) findViewById(R.id.tuikuanliucheng_top_shenqingtuikuan);
		mTextStatus0.setTextColor(Color.GRAY);
		mTextStatus1 = (TextView) findViewById(R.id.tuikuanliucheng_top_shenhetongguo);
		mTextStatus1.setTextColor(Color.GRAY);
		mTextStatus2 = (TextView) findViewById(R.id.tuikuanliucheng_top_yifaqituikuan);
		mTextStatus2.setTextColor(Color.GRAY);
		mCheXiaoShenqing = (TextView) findViewById(R.id.tuikuanxiangqing_shenqingchexiao);
		mCheXiaoShenqing.setOnClickListener(this);
		mTuihuoGoodsPrice = (TextView) findViewById(R.id.tuikuanxiangqing_tuikuanshangpinjine);
		mYingtuijine = (TextView) findViewById(R.id.tuikuanxiangqing_yingtuijine);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tuikuanliucheng_top_back:
			finish();
			break;
		case R.id.tuikuanxiangqing_shenqingchexiao:
			// 撤销按钮
			break;

		default:
			break;
		}
	}

	/**
	 * 用来处理图片，目的是为了节省内存
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {

		BitmapFactory.Options opt = new BitmapFactory.Options();

		opt.inPreferredConfig = Bitmap.Config.RGB_565;

		opt.inPurgeable = true;

		opt.inInputShareable = true;

		// 获取资源图片

		InputStream is = context.getResources().openRawResource(resId);

		return BitmapFactory.decodeStream(is, null, opt);

	}

}
