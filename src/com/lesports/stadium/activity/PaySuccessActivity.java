package com.lesports.stadium.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.lesports.stadium.R;
import com.lesports.stadium.adapter.ShopcartExpandableListViewAdapter;
import com.lesports.stadium.adapter.ShopcartExpandableListViewAdapter.CheckInterface;
import com.lesports.stadium.adapter.ShopcartExpandableListViewAdapter.ModifyCountInterface;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.base.BaseActivity;
import com.lesports.stadium.bean.GroupInfo;
import com.lesports.stadium.bean.ProductInfo;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.view.CustomExpandableListView;
import com.umeng.analytics.MobclickAgent;

public class PaySuccessActivity extends BaseActivity implements
		OnClickListener, CheckInterface, ModifyCountInterface {
	/**
	 * 顶部返回
	 */
	private ImageView mBack;
	/**
	 * 展示收货方式
	 */
	private TextView mTakeGoodsWay;
	/**
	 * 座位号点击布局
	 */
	private RelativeLayout mLayoutZuoweiNum;
	/**
	 * 展示座位信息
	 */
	private TextView mZuoweiInfo;
	/**
	 * 展示手机号信息
	 */
	private TextView mPhoneInfo;
	/**
	 * 运费金额数
	 */
	private TextView mYunfeiMoney;
	/**
	 * 积分折扣数
	 */
	private TextView mJifenMoney;
	/**
	 * 总价
	 */
	private TextView mTotalMoney;
	/**
	 * 返回首页按钮
	 */
	private TextView mBackFirst;
	/**
	 * 展示所购买的商品的列表项
	 */
	private CustomExpandableListView mListview;
	/**
	 * 列表项控件的数据适配器
	 */
	private ShopcartExpandableListViewAdapter selva;
	/**
	 * // 组元素数据列表
	 */
	private List<GroupInfo> groups = new ArrayList<GroupInfo>();
	/**
	 * // 子元素数据列表
	 */
	private Map<String, List<ProductInfo>> children = new HashMap<String, List<ProductInfo>>();
	/**
	 * 展示送货地址的布局
	 */
	private LinearLayout mLayout_songhuo;
	/**
	 * 展示自取货物的地址
	 */
	private LinearLayout mLayout_ziqu;
	/**
	 * 自取的地址
	 */
	private TextView mZiqudizhi;
	/**
	 * 自取部分的退款说明
	 */
	private TextView mZtuikuanshuoming;
	/**
	 * 送货部分的退款说明
	 */
	private TextView mStuikuanshuoming;
	/**
	 * 关闭退款说明的确定按钮
	 */
	private TextView mTextClose;
	/**
	 * Dialog dialog
	 */
	private Dialog dialog;
	/**
	 * 用来标记是餐饮还是商品支付成功
	 */
	private String is_type;
	/**
	 * 自取流程布局
	 * 
	 * @param savedInstanceState
	 */
	private LinearLayout mLayout_ziqus;
	/**
	 * 送货流程buju
	 * 
	 * @param savedInstanceState
	 */
	private LinearLayout mLayout_songhuos;
	/**
	 * 支付成功自取手机号
	 * 
	 * @param savedInstanceState
	 */
	private TextView mZiqu_shoujihao;
	/**
	 * 邮寄地址布局
	 */
	private RelativeLayout mLayoutYouji;
	/**
	 * 邮寄姓名
	 */
	private TextView mYoujiName;
	/**
	 * 邮寄地址
	 */
	private TextView mYoujiAddress;
	/**
	 * 邮寄电话
	 */
	private TextView mYoujiPhone;
	/**
	 * 自取姓名
	 */
	private TextView mZiquName;
	/**
	 * 送货姓名
	 */
	private TextView mSonghuoName;
	/**
	 * 自取说明
	 */
	private TextView mZiqushuoming;
	//自取说明
	private TextView ziquDialog;
	//自取说明
	private String mZiqushuoming2;
	//配送显示
	private RelativeLayout peisongShom;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paysuccess);
		MobclickAgent.onEvent(PaySuccessActivity.this, "PaySuccessfully");
		initviews();
		initDatas();
	}

	/**
	 * 初始化数据
	 */
	private void initDatas() {
		// TODO Auto-generated method stub
		groups.clear();
		children.clear();
		Intent intent = getIntent();
		groups = (List<GroupInfo>) intent.getSerializableExtra("group");
		children = (Map<String, List<ProductInfo>>) intent
				.getSerializableExtra("child");
		is_type = intent.getStringExtra("type");
		if (is_type.equals("canyin")) {
			// 调用方法，删除掉餐饮商家id对应的所有商品
			LApplication.foodbuycar.delete(
					"foodbuy_usernametag = ? AND foodbuy_seller = ?",
					new String[] { GlobalParams.USER_ID,
							GlobalParams.ONLINE_STORE_ID });
			mZtuikuanshuoming.setVisibility(View.VISIBLE);
			mStuikuanshuoming.setVisibility(View.VISIBLE);
		} else {
			mZtuikuanshuoming.setVisibility(View.GONE);
			mStuikuanshuoming.setVisibility(View.GONE);
		}
		String yunfei = intent.getStringExtra("yunfei");
		String price = intent.getStringExtra("price");
		if (price.contains("￥")) {
			price.replace("￥", "");
		}
		String tag = intent.getStringExtra("tag");
		if (tag != null && !TextUtils.isEmpty(tag)) {
			if (tag.equals("ziqu")) {
				// 说明收货方式是自取
				// 调用方法，以自取的形式来展示
				String ziqudizhi = intent.getStringExtra("ziqudizhi");
				mZiqushuoming2 = intent.getStringExtra("mZiqushuoming");
				// 调用方法，将数据加载上去
				initUiData_ziqu(yunfei, price, ziqudizhi);
			} else if (tag.equals("songhuo")) {
				// 说明收货方式是送货
				String zuoweihao = intent.getStringExtra("zuoweihao");
				String phone = intent.getStringExtra("phone");
				// 调用方法，将数据加载上去
				initUiData_songhuo(yunfei, price, zuoweihao, phone);
			}else if(tag.equals("youji")){
				String name=intent.getStringExtra("name");
				String address=intent.getStringExtra("address");
				String phone=intent.getStringExtra("phone");
				initUIData_youji(yunfei,price,name,address,phone);
				
			}
		}
		// 将商品数据适配上去
		initEvents();
	}

	/**
	 * 这是商品订单支付成功以后支付成功页面中显示的邮寄地址信息部分
	 * @param yunfei
	 * @param price
	 * @param name
	 * @param address
	 * @param phone
	 */
	private void initUIData_youji(String yunfei, String price, String name,
			String address, String phone) {
		// TODO Auto-generated method stub
		mTakeGoodsWay.setText("邮寄");
		mLayoutYouji.setVisibility(View.VISIBLE);
		mYunfeiMoney.setText(yunfei);
		mTotalMoney.setText(price);
		mYoujiAddress.setText(address);
		mYoujiName.setText(name);
		mYoujiPhone.setText(phone);
	}

	/**
	 * 按照送货的方式展示界面
	 * 
	 * @param yunfei
	 * @param price
	 * @param zuoweihao
	 * @param phone
	 */
	private void initUiData_songhuo(String yunfei, String price,
			String zuoweihao, String phone) {
		// TODO Auto-generated method stub
		mSonghuoName.setText(GlobalParams.ORDER_SONGHUO_NAME);
		mTakeGoodsWay.setText("送货");
		mLayout_songhuo.setVisibility(View.VISIBLE);
		mLayout_songhuos.setVisibility(View.VISIBLE);
		mLayout_ziqu.setVisibility(View.GONE);
		mYunfeiMoney.setText(yunfei);
		mZuoweiInfo.setText(zuoweihao);
		mPhoneInfo.setText(phone);
		mTotalMoney.setText(price);
	}

	/**
	 * 按照自取的形式展示界面
	 * 
	 * @param yunfei
	 * @param price
	 * @param ziqudizhi
	 */
	private void initUiData_ziqu(String yunfei, String price, String ziqudizhi) {
		// TODO Auto-generated method stub
		mZiquName.setText(GlobalParams.ORDER_ZIQU_NAME);
		mTakeGoodsWay.setText("自取");
		ziquDialog.setText("自取说明");
		ziquDialog.setTextColor(getResources().getColor(R.color.service_select_txt));
		mLayout_songhuo.setVisibility(View.GONE);
		peisongShom.setVisibility(View.GONE);
		mLayout_ziqu.setVisibility(View.VISIBLE);
		mLayout_ziqus.setVisibility(View.VISIBLE);
		mZiqudizhi.setText(ziqudizhi);
		mYunfeiMoney.setText(yunfei);
		mTotalMoney.setText(price);
		mZiqu_shoujihao.setText(GlobalParams.ORDER_ZIQU_PHONE);
		ziquDialog.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				createDialog_tuikuan(mZiqushuoming2);
			}
		});
//		if(!TextUtils.isEmpty(GlobalParams.Goods_Remark)){
//			mZiqushuoming.setText(GlobalParams.Goods_Remark);
//		}
		
	}

	/**
	 * 初始化列表项的适配器对象，并且规定该可拓展的listview以展开的方式来呈现
	 * 
	 * @2016-2-23下午2:09:10
	 */
	private void initEvents() {
		selva = new ShopcartExpandableListViewAdapter(false, groups, children,
				this);
		selva.setCheckInterface(this);// 关键步骤1,设置复选框接口
		selva.setModifyCountInterface(this);// 关键步骤2,设置数量增减接口
		mListview.setAdapter(selva);

		for (int i = 0; i < selva.getGroupCount(); i++) {
			mListview.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
		}
		mListview.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				return true;
			}
		});
	}

	/**
	 * 初始化界面view控件
	 */
	private void initviews() {
		// TODO Auto-generated method stub
		mZiqushuoming=(TextView) findViewById(R.id.zhifuchenggong_online_order_xiadanshuomingsss);
		ziquDialog = (TextView) findViewById(R.id.zhifuchenggong_online_order_zitishuoming);
		mZiquName=(TextView) findViewById(R.id.zhifuchenggong_shouhuodizhi_shoujihao_num_ziqu_name);
		mSonghuoName=(TextView) findViewById(R.id.pay_shoujihao_tv_name);
		mLayoutYouji=(RelativeLayout) findViewById(R.id.pay_success_address_choise);
		mYoujiAddress=(TextView) findViewById(R.id.pay_success_dingdanzhifu_shouhuoren_dizhi);
		mYoujiName=(TextView) findViewById(R.id.pay_success_dingdanzhifu_shouhuoren_name);
		mYoujiPhone=(TextView) findViewById(R.id.pay_success_dingdanzhifu_shouhuoren_dianhua);
		mZiqu_shoujihao = (TextView) findViewById(R.id.zhifuchenggong_shouhuodizhi_shoujihao_num_ziqu);
		mLayout_ziqus = (LinearLayout) findViewById(R.id.zhifuchenggong_ziqu_layout);
		mLayout_songhuos = (LinearLayout) findViewById(R.id.zhifuchenggong_layout_songhuo);
		mStuikuanshuoming = (TextView) findViewById(R.id.zhifuchengong_zaixiancanyin_tuikuanshuoming_zhifuyesssdd);
		mStuikuanshuoming.setOnClickListener(this);
		mZtuikuanshuoming = (TextView) findViewById(R.id.zhifuchenggong_zaixiancanyin_tuikuanshuoming_zhifuye);
		mZtuikuanshuoming.setOnClickListener(this);
		mZiqudizhi = (TextView) findViewById(R.id.zhifuchenggong_online_shouhuodizhi_zitidizhi);
		mLayout_ziqu = (LinearLayout) findViewById(R.id.layout_zhifuchenggong_canyin_ziqu);
		mLayout_songhuo = (LinearLayout) findViewById(R.id.layout_zhifuchenggong_canyin_songhuo);
		mBack = (ImageView) findViewById(R.id.pay_successs_coupon_back_backsss);
		mBack.setOnClickListener(this);
		mTakeGoodsWay = (TextView) findViewById(R.id.pay_zhifufangshi_content);
		mLayoutZuoweiNum = (RelativeLayout) findViewById(R.id.pay_layout_zuoweihao);
		mLayoutZuoweiNum.setOnClickListener(this);
		mZuoweiInfo = (TextView) findViewById(R.id.pay_zuoweihao_tv);
		mPhoneInfo = (TextView) findViewById(R.id.pay_shoujihao_tv);
		mYunfeiMoney = (TextView) findViewById(R.id.pay_yunfei_tv);
		mJifenMoney = (TextView) findViewById(R.id.pay_jifenzhekou_tv);
		mTotalMoney = (TextView) findViewById(R.id.pay_zongjia_tv);
		mBackFirst = (TextView) findViewById(R.id.pay_fanhuishouyye);
		mBackFirst.setOnClickListener(this);
		mListview = (CustomExpandableListView) findViewById(R.id.pay_order_exListView);
		peisongShom = (RelativeLayout) findViewById(R.id.peisong_shuomig);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.pay_successs_coupon_back_backsss:
			PaySuccessActivity.this.finish();
			if (OnlineCateringDetailActivity.instance != null) {
				OnlineCateringDetailActivity.instance.finish();
			}
			if (CrowdPayActivty.instance != null) {
				CrowdPayActivty.instance.finish();
			}
			if (OnLineCreatingPayActivity.instence != null) {
				OnLineCreatingPayActivity.instence.finish();
			}
			if (GoodsDetailActivity.instance != null) {
				GoodsDetailActivity.instance.finish();
			}
			if (BuyCarActivity.instance != null) {
				BuyCarActivity.instance.finish();
			}
			if (OrderActivity.instance != null) {
				OrderActivity.instance.finish();
			}
			break;
		case R.id.pay_layout_zuoweihao:
			// 跳转，进行选座
			break;
		case R.id.pay_fanhuishouyye:
			// 回到首页面
			PaySuccessActivity.this.finish();
			if (OnlineCateringDetailActivity.instance != null) {
				OnlineCateringDetailActivity.instance.finish();
			}
			if (CrowdPayActivty.instance != null) {
				CrowdPayActivty.instance.finish();
			}
			if (OnLineCreatingPayActivity.instence != null) {
				OnLineCreatingPayActivity.instence.finish();
			}
			if (GoodsDetailActivity.instance != null) {
				GoodsDetailActivity.instance.finish();
			}
			if (BuyCarActivity.instance != null) {
				BuyCarActivity.instance.finish();
			}
			if (OrderActivity.instance != null) {
				OrderActivity.instance.finish();
			}
			break;
		case R.id.zhifuchenggong_zaixiancanyin_tuikuanshuoming_zhifuye:
			createDialog_tuikuan();
			break;
		case R.id.zhifuchengong_zaixiancanyin_tuikuanshuoming_zhifuyesssdd:
			createDialog_tuikuan();
			break;
		case R.id.tuikuanshuoming_tv_confirm_close:
			dialog.dismiss();
			break;
		default:
			break;
		}
	}

	/**
	 * 自定义的退款说明的dialog
	 */
	private void createDialog_tuikuan() {
		// 需要先弹出一个dialog，在该dialog上面来进行选择
		dialog = new Dialog(PaySuccessActivity.this, R.style.Theme_Light_Dialog);
		View dialogView = LayoutInflater.from(PaySuccessActivity.this).inflate(
				R.layout.tuikuanshuoming, null);
		// 获得dialog的window窗口
		mTextClose = (TextView) dialogView
				.findViewById(R.id.tuikuanshuoming_tv_confirm_close);
		mTextClose.setOnClickListener(this);
		TextView title = (TextView) dialogView
				.findViewById(R.id.tuikuanshuoming_tv_title);
		TextView message = (TextView) dialogView
				.findViewById(R.id.tuikuanshuoming_tv_remind_message);
		message.setText("1.支付后十分钟之内可以直接申请退款;" + "\r\n" + "\r\n"
				+ "2.由于场馆内需要商户提前备餐，在支付完成的十分钟后用户发生退款，需要申请，商户通过后才可以退款。");
		Window window = dialog.getWindow();
		// 设置dialog在屏幕底部
		window.setGravity(Gravity.CENTER);
		// 设置dialog弹出时的动画效果，从屏幕底部向上弹出
		// window.setWindowAnimations(R.style.dialogStyle);
		window.getDecorView().setPadding(60, 0, 60, 0);
		// 获得window窗口的属性
		android.view.WindowManager.LayoutParams lp = window.getAttributes();
		// 设置窗口宽度为充满全屏
		lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
		// lp.width =getScreenHeight(OnLineCreatingPayActivity.this)/3;
		// 设置窗口高度为包裹内容
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		// 将设置好的属性set回去
		window.setAttributes(lp);
		// 将自定义布局加载到dialog上
		dialog.setContentView(dialogView);
		dialog.show();
	}

	@Override
	public void doIncrease(String string, int groupPosition, int childPosition,
			View showCountView, boolean isChecked) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doDecrease(String string, int groupPosition, int childPosition,
			View showCountView, boolean isChecked) {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkGroup(int groupPosition, boolean isChecked) {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkChild(int groupPosition, int childPosition,
			boolean isChecked) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDestroy() {
		if (children != null) {
			children.clear();
			children = null;
		}
		if (groups != null) {
			groups.clear();
			groups = null;
		}
		super.onDestroy();
	}
	
	/**
	 * 自定义的退款说明的dialog
	 */
	private void createDialog_tuikuan(String shuoming) {
		// 需要先弹出一个dialog，在该dialog上面来进行选择
		final Dialog dialogs = new Dialog(PaySuccessActivity.this, R.style.Theme_Light_Dialog);
		View dialogView = LayoutInflater.from(PaySuccessActivity.this).inflate(
				R.layout.tuikuanshuoming, null);
		// 获得dialog的window窗口
		TextView title = (TextView) dialogView
				.findViewById(R.id.tuikuanshuoming_tv_title);
		title.setText("自取说明");
		TextView message = (TextView) dialogView
				.findViewById(R.id.tuikuanshuoming_tv_remind_message);
		message.setText(shuoming);
		TextView qsure = (TextView) dialogView
				.findViewById(R.id.tuikuanshuoming_tv_confirm_close);
		qsure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogs.dismiss();
			}
		});
		Window window = dialogs.getWindow();
		// 设置dialog在屏幕底部
		window.setGravity(Gravity.CENTER);
		// 设置dialog弹出时的动画效果，从屏幕底部向上弹出
		// window.setWindowAnimations(R.style.dialogStyle);
		window.getDecorView().setPadding(160, 0, 160,0);
		// 获得window窗口的属性
		android.view.WindowManager.LayoutParams lp = window.getAttributes();
		// 设置窗口宽度为充满全屏
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		// lp.width =getScreenHeight(OnLineCreatingPayActivity.this)/3;
		// 设置窗口高度为包裹内容
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		// 将设置好的属性set回去
		window.setAttributes(lp);
		// 将自定义布局加载到dialog上
		dialogs.setContentView(dialogView);
		if(!dialogs.isShowing())
			dialogs.show();
	}

}
