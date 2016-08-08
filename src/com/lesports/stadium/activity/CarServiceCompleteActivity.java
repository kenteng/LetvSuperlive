package com.lesports.stadium.activity;


import org.json.JSONException;
import org.json.JSONObject;

import com.lesports.stadium.R;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.DrivierBean;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.net.NetLoadListener;
import com.lesports.stadium.net.NetService;
import com.lesports.stadium.net.Code.HttpSetting;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.SharedPreferencesUtils;
import com.lesports.stadium.utils.Utils;
import com.lesports.stadium.view.CircleImageView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

/**
 * ***************************************************************
 * 
 * @ClassName: CarServiceCompleteActivity
 * 
 * @Desc : 用车交易完成页面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 邓聪聪
 * 
 * @data:2016-4-24 下午6:05:41
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */

@SuppressLint("HandlerLeak") public class CarServiceCompleteActivity extends Activity implements
		OnClickListener {
	/**
	 * 返回按钮
	 */
	private ImageView mGobackImg;
	/**
	 * 标题内容
	 */
	private TextView mTitleContentTv;
	/**
	 * 打电话给司机
	 */
	private CircleImageView mPhoneImg;
	/**
	 * 司机信息存储
	 */
	private DrivierBean mDriverBean;
	/**
	 * 司机头像
	 */
	private CircleImageView mDriverHeadImg;
	/**
	 * 司机信息
	 */
	private TextView mDriverNameTv, mDriverNumberTv, mDriverOrderTv;
	/**
	 * 司机等级信息
	 */
	private RatingBar mDriverLeverRt;
	/**
	 * 费用
	 */
	private String feeStr = "";
	/**
	 * 优惠金额
	 */
	private String discountStr = "";
	/**
	 * 订单id
	 */
	private String orderIdStr = "";
	/**
	 * 价格Tv
	 */
	private TextView feeTv;
	/**
	 * 用户优惠金额
	 */
	private TextView discountTv;
	/**
	 * get请求
	 */
	private static NetService netService;
	/**
	 *  用户选择RatingBar
	 */
	private RatingBar selectRb;
	/**
	 * handler消息处理
	 */
	private final int HANDLE_TAG_100=100;
	private final int HANDLE_TAG_101=101;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 100:
				// 处理账单确认
				String feeBackdata = (String) msg.obj;
				JSONObject feeObj;
				
				try {
					feeObj = new JSONObject(feeBackdata);
					if (feeBackdata.contains("order_amount")) {
						JSONObject feeDeltealObj = feeObj
								.getJSONObject("result");
						//调整金额，order_amount为应付金额;coupon_facevalue优惠金额
						feeStr=feeDeltealObj.getString("order_amount");
						discountStr=feeDeltealObj.getString("coupon_facevalue");
					//	mDriverLeverRt.setNumStars(Integer.parseInt(driverDetailBean.getStar_level()));
					//	mDriverLeverRt.setRating(Integer.parseInt(driverDetailBean.getStar_level()));
						feeTv.setText("￥" + feeStr);
						discountTv.setText("乐视支付￥" + feeStr + "," + "优惠￥" + discountStr);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				break;
			case 101:
				//司机信息处理
				String driverBackdata = (String) msg.obj;
				JSONObject orderObj;
				try {
					orderObj = new JSONObject(driverBackdata);
					if (driverBackdata.contains("name")) {
						JSONObject driverDeltealObj = orderObj
								.getJSONObject("result");
						mDriverBean.setName(driverDeltealObj.getString("name"));
						mDriverBean.setPhoto_url(driverDeltealObj.getString("photo_url"));
						mDriverBean.setVehicle_number(driverDeltealObj.getString("vehicle_number"));
						mDriverBean.setDriver_company_name(driverDeltealObj.getString("driver_company_name"));
						mDriverBean.setStar_level(driverDeltealObj.getString("star_level"));
						mDriverBean.setUnittime_complete_count(driverDeltealObj.getString("unittime_complete_count"));
						mDriverBean.setCellphone(driverDeltealObj.getString("cellphone"));
						mDriverNameTv.setText(mDriverBean.getName());
						mDriverNumberTv.setText(mDriverBean.getVehicle_number()+"　"+mDriverBean.getDriver_company_name());
						mDriverOrderTv.setText(mDriverBean.getUnittime_complete_count()+"单");
						LApplication.loader.DisplayImage(mDriverBean.getPhoto_url(), mDriverHeadImg, R.drawable.driver_defaulthead);
						mDriverLeverRt.setRating(Integer.parseInt(mDriverBean.getStar_level()));
					//	mDriverLeverRt.setNumStars(Integer.parseInt(driverDetailBean.getStar_level()));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				requestOrderFee();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_carservicecomplete);
		Intent mIntent = getIntent();
		orderIdStr =mIntent.getStringExtra("orderid");
		feeStr = mIntent.getStringExtra("fee");
		discountStr = mIntent.getStringExtra("discount");
		mDriverBean=new DrivierBean();
		initView();
		if(!Utils.isNullOrEmpty(orderIdStr)){
			requestOrderDriver();
		}
	
	}
	/**
	 * 初始化数据 
	 */
	private void initView() {
		mGobackImg = (ImageView) findViewById(R.id.title_left_iv);
		mGobackImg.setOnClickListener(this);
		mTitleContentTv = (TextView) findViewById(R.id.tv_title);
		mTitleContentTv.setText("我的行程");
		mPhoneImg = (CircleImageView) findViewById(R.id.driver_phone_img);
		mPhoneImg.setOnClickListener(this);
		mDriverNameTv = (TextView) findViewById(R.id.driveldetail_name_tv);
		mDriverNumberTv = (TextView) findViewById(R.id.textView2);
		mDriverOrderTv = (TextView) findViewById(R.id.textView4);
		mDriverLeverRt = (RatingBar) findViewById(R.id.driver_lever_rb);
		mDriverHeadImg = (CircleImageView) findViewById(R.id.bookthecar_driverhead_img);
		feeTv = (TextView) findViewById(R.id.pay_result_fee_tv);
		discountTv = (TextView) findViewById(R.id.pay_method_tv);
		feeTv.setText("￥" + feeStr);
		discountTv.setText("乐视支付￥" + feeStr + "," + "优惠￥" + discountStr);
		/*if (!Utils.isNullOrEmpty(mDriverBean)) {
			mDriverNameTv.setText(mDriverBean.getName());
			Log.i("wxn",
					"mDriverBean: "
							+ (mDriverBean == null ? null : mDriverBean
									.getVehicle_number()));
			Log.i("wxn",
					"mDriverBean: "
							+ (mDriverBean == null ? null : mDriverBean
									.getDriver_company_name()));
			Log.i("wxn", "mDriverNumberTv: " + mDriverNumberTv);
			mDriverNumberTv.setText(mDriverBean.getVehicle_number() + "　"
					+ mDriverBean.getDriver_company_name());
			mDriverOrderTv.setText(mDriverBean.getUnittime_complete_count()
					+ "单");
			LApplication.loader.DisplayImage(mDriverBean.getPhoto_url(),
					mDriverHeadImg, R.drawable.driver_defaulthead);
			mDriverLeverRt.setRating(Integer.parseInt(mDriverBean
					.getStar_level()));
		}*/
		selectRb=(RatingBar)findViewById(R.id.service_evaluate_rb);
		selectRb.setIsIndicator(false);
		selectRb.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				selectRb.setIsIndicator(true);
				SharedPreferencesUtils.saveData(GlobalParams.context, "ls_user_message", ""+orderIdStr, ""+selectRb.getRating());
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_iv:
			finish();
			break;
		case R.id.driver_phone_img:
			if (!Utils.isNullOrEmpty(mDriverBean)) {
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:" + mDriverBean.getCellphone()));
				startActivity(intent);
			}
			break;
		default:
			break;
		}
	}
	/**
	 * 请求订单费用
	 */
	private void requestOrderFee() {

		netService = NetService.getInStance();

		// netService.setParams("order_id", LApplication.yidaoOrderId);
		netService.setParams("access_token", ConstantValue.CAR_TOKEN);
		netService.setHttpMethod(HttpSetting.GET_MODE);
		netService.setUrl(ConstantValue.GET_ORDER_FEE + orderIdStr);
		netService.loader(new NetLoadListener() {
			@Override
			public void onloaderListener(BackData result) {
				Log.e("carOrder",
						LApplication.yidaoOrderId + "获取订单费用"
								+ result.getNetResultCode() + "结果："
								+ result.getObject());

				Message msg = new Message();
				msg.what = 100;
				msg.obj = result.getObject();
				mHandler.sendMessage(msg);
			}
		});

	}
	/**
	 * Get请求方式，请求司机信息
	 */
	private void requestOrderDriver(){

		netService = NetService.getInStance();

		netService.setParams("order_id",orderIdStr);
		netService.setParams("access_token", ConstantValue.CAR_TOKEN);
		// netService.setParams("gender", loginUser.gender);
		// netService.setParams("picture",
		// loginUser.picArray==null?"":loginUser.picArray[0]);
		netService.setHttpMethod(HttpSetting.GET_MODE);
		netService.setUrl(ConstantValue.GET_DRIVER_DETEAL);
		netService.loader(new NetLoadListener() {
			@Override
			public void onloaderListener(BackData result) {
				Log.e("carOrder", LApplication.yidaoOrderId + "获取司机详情"

				+ result.getNetResultCode() + "结果：" + result.getObject());
				Message msg = new Message();
				msg.what = 101;
				msg.obj = result.getObject();
				mHandler.sendMessage(msg);
			}
		});

	
	}
}
