package com.lesports.stadium.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lesports.stadium.R;
import com.lesports.stadium.adapter.ShopcartExpandableListViewAdapter;
import com.lesports.stadium.adapter.ShopcartExpandableListViewAdapter.CheckInterface;
import com.lesports.stadium.adapter.ShopcartExpandableListViewAdapter.ModifyCountInterface;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.base.BaseActivity;
import com.lesports.stadium.bean.AllChipsBean;
import com.lesports.stadium.bean.CrowReportBackBean;
import com.lesports.stadium.bean.CrowdDetailBean;
import com.lesports.stadium.bean.OrderListBean;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.view.CustomExpandableListView;
import com.umeng.analytics.MobclickAgent;
/**
 * ***************************************************************
 * @ClassName:  CrowdPayActivty 
 * 
 * @Desc : 众筹支付成功界面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : lwc
 * 
 * @data:2016-4-2 下午5:55:37
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */
public class CrowdPaySuccessActivity extends BaseActivity implements OnClickListener,CheckInterface,
ModifyCountInterface{
	/**
	 * 顶部返回
	 */
	private ImageView mBack;
	/**
	 * 订单已提交状态
	 */
	private ImageView mOrder_yitijiao;
	/**
	 * 订单已确认状态
	 */
	private ImageView mOrder_yiqueren;
	/**
	 * 订单正在配送状态
	 */
	private ImageView mOrder_peisong;
	/**
	 * 订单已送达状态
	 */
	private ImageView mOrder_yisongda;
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
//	/**
//	 * // 组元素数据列表
//	 */
//	private List<GroupInfo> groups = new ArrayList<GroupInfo>();
//	/**
//	 * // 子元素数据列表
//	 */
//	private Map<String, List<ProductInfo>> children = new HashMap<String, List<ProductInfo>>();
	/**
	 * 收货人姓名
	 */
	private TextView mTakeGoodsName;
	/**
	 * 收货人电话
	 */
	private TextView mTakeGoodsPhone;
	/**
	 * 收货人地址
	 */
	private TextView mTakeGoodsAddress;
	/**
	 * 收货人邮箱地址
	 */
	private TextView mTakeGoodsEmail;
	/**
	 * 支付成功商品图片
	 */
	private ImageView mImage;
	/**
	 * 支付成功商品名称
	 */
	private TextView mTextview;
	/**
	 * 支付成功商品介绍
	 */
	private TextView mTextinfo;
	/**
	 * 众筹实体类
	 */
	private CrowdDetailBean mBean;
	/**
	 * 
	 * 众筹列表项数据
	 */
	private AllChipsBean mAllBean;
	/**
	 * 需要支持的子数据
	 */
	private CrowReportBackBean mChildBean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crowdpaysuccess);
		MobclickAgent.onEvent(CrowdPaySuccessActivity.this,"PaySuccessfully");
		initviews();
		initDatas();
	}
	/**
	 * 初始化数据
	 */
	private void initDatas() {
//		groups.clear();
//		children.clear();
		Intent intent=getIntent();
		String tag=intent.getStringExtra("tag");
		if(tag.equals("zhongchou")){
			mBean=(CrowdDetailBean) intent.getSerializableExtra("bean");
			mAllBean=(AllChipsBean) intent.getSerializableExtra("beans");
			mChildBean=(CrowReportBackBean) intent.getSerializableExtra("beanss");
			String name=intent.getStringExtra("name");
			String address=intent.getStringExtra("address");
			String phone=intent.getStringExtra("phone");
			String youbian=intent.getStringExtra("youbian");
			//将数据加载到控件上
			if(mBean!=null&&!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(address)&&mAllBean!=null){
				initviewdata(name,phone,youbian,address,mBean,mAllBean);
			}
		}else if(tag.equals("order")){
			OrderListBean bean=(OrderListBean) intent.getSerializableExtra("bean");
			String content=bean.getList_zhong().get(0).getReturnContent();
			String zhichijine=bean.getList_zhong().get(0).getPrice();
			String imageurl=bean.getList_zhong().get(0).getPropagatePicture();
			String name=bean.getCompanies();
			String address=bean.getPosition();
			String phone=bean.getTelePhone();
			initdatatoview(content,zhichijine,imageurl,name,address,phone);
		}
		
		
		
	}
	/**
	 * 将数据加载到空间商
	 * @param content
	 * @param zhichijine
	 * @param imageurl
	 * @param name
	 * @param address
	 * @param phone
	 */
	private void initdatatoview(String content, String zhichijine,
			String imageurl, String name, String address, String phone) {
		mTakeGoodsAddress.setText(address);
		mTakeGoodsName.setText(name);
		mTakeGoodsPhone.setText(phone);
		LApplication.loader.DisplayImage(ConstantValue.BASE_IMAGE_URL
				+imageurl + ConstantValue.IMAGE_END,mImage,
				R.drawable.zhoubianshangpin_zhanwei);
		mTextview.setText("支持"+zhichijine);
		mTextinfo.setText(content);
	}
	/**
	 * 将数据加载到空间商
	 * @param name
	 * @param phone
	 * @param youbian
	 * @param address
	 * @param mBean2
	 * @param mAllBean2 
	 */
	private void initviewdata(String name, String phone, String youbian,
			String address, CrowdDetailBean mBean2, AllChipsBean mAllBean2) {
		mTakeGoodsAddress.setText(address);
		mTakeGoodsEmail.setText(youbian);
		mTakeGoodsName.setText(name);
		mTakeGoodsPhone.setText(phone);
		LApplication.loader.DisplayImage(ConstantValue.BASE_IMAGE_URL
				+GlobalParams.ZHONGCHOU_ORDER_IMAGE + ConstantValue.IMAGE_END,mImage,
				R.drawable.zhoubianshangpin_zhanwei);
		mTextview.setText(mChildBean.getReturnName());
		mTextinfo.setText(mChildBean.getReturnContent());
		mYunfeiMoney.setText("（不包含运费"+mBean.getFreight()+"元）");
		int price=Integer.parseInt(mChildBean.getReturnPrice());
		int yunfei=Integer.parseInt(mBean.getFreight());
		mTotalMoney.setText("￥"+(price));
	}
	/**
	 * 初始化界面view控件
	 */
	private void initviews() {
		mImage=(ImageView) findViewById(R.id.order_child_zhongchou_image);
		mTextview=(TextView) findViewById(R.id.order_child_zhongchou_jiage);
		mTextinfo=(TextView) findViewById(R.id.order_child_zhongchou_shouhuodizhi);
		mTakeGoodsAddress=(TextView) findViewById(R.id.crowd_paysuccess_shouhuoren_dizhi);
		mTakeGoodsEmail=(TextView) findViewById(R.id.crowd_paysuccess_shouhuoren_youxiang);
		mTakeGoodsName=(TextView) findViewById(R.id.crowd_paysuccess_shouhuoren_name);
		mTakeGoodsPhone=(TextView) findViewById(R.id.crowd_paysuccess_shouhuoren_dianhua);
		mBack=(ImageView) findViewById(R.id.crowd_pay_successs_coupon_back);
		mBack.setOnClickListener(this);
		mOrder_yitijiao=(ImageView) findViewById(R.id.crowd_pay_dingdanyitijiaozhuangtai);
		mOrder_yiqueren=(ImageView) findViewById(R.id.crowd_pay_dingdanyiquerenzhuangtai);
		mOrder_peisong=(ImageView) findViewById(R.id.crowd_pay_dingdanzhengzaipeisong);
		mOrder_yisongda=(ImageView) findViewById(R.id.crowd_pay_yisongdan);
		mYunfeiMoney=(TextView) findViewById(R.id.order_zhongchou_hejiduoshao);
		mTotalMoney=(TextView) findViewById(R.id.order_zhongchou_hejiduoshaoqian);
		mBackFirst=(TextView) findViewById(R.id.crowd_pay_fanhuishouyye);
		mBackFirst.setOnClickListener(this);
		mListview=(CustomExpandableListView) findViewById(R.id.crowd_pay_order_exListView);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.crowd_pay_successs_coupon_back:
			CrowdPaySuccessActivity.this.finish();
			break;
		case R.id.crowd_pay_fanhuishouyye:
			//回到首页面
			CrowdPaySuccessActivity.this.finish();
			if(OnlineCateringDetailActivity.instance!=null){
				OnlineCateringDetailActivity.instance.finish();
			}
			if(CrowdPayActivty.instance!=null){
				CrowdPayActivty.instance.finish();
			}
			if(OnLineCreatingPayActivity.instence!=null){
				OnLineCreatingPayActivity.instence.finish();
			}
			if(GoodsDetailActivity.instance!=null){
				GoodsDetailActivity.instance.finish();
			}
			if(BuyCarActivity.instance!=null){
				BuyCarActivity.instance.finish();
			}
			if(OrderActivity.instance!=null){
				OrderActivity.instance.finish();
			}
			if(CrowdFundingDetailActivity.instance!=null){
				CrowdFundingDetailActivity.instance.finish();
				CrowdFundingDetailActivity.instance = null;
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public void doIncrease(String string, int groupPosition, int childPosition,
			View showCountView, boolean isChecked) {
	}
	@Override
	public void doDecrease(String string, int groupPosition, int childPosition,
			View showCountView, boolean isChecked) {
	}
	@Override
	public void checkGroup(int groupPosition, boolean isChecked) {
	}
	@Override
	public void checkChild(int groupPosition, int childPosition,
			boolean isChecked) {
	}

}
