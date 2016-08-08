package com.lesports.stadium.activity;

import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lesports.stadium.R;
import com.lesports.stadium.adapter.OrderDetailGoodsExpandableListViewAdapter;
import com.lesports.stadium.adapter.ShopcartExpandableListViewAdapter;
import com.lesports.stadium.adapter.ShopcartExpandableListViewAdapter.CheckInterface;
import com.lesports.stadium.adapter.ShopcartExpandableListViewAdapter.ModifyCountInterface;
import com.lesports.stadium.base.BaseActivity;
import com.lesports.stadium.bean.GroupInfo;
import com.lesports.stadium.bean.OrderBean;
import com.lesports.stadium.bean.OrderListBean;
import com.lesports.stadium.bean.OrderListBeanGoodsBean;
import com.lesports.stadium.bean.PayBean;
import com.lesports.stadium.bean.PayParametric;
import com.lesports.stadium.bean.ProductInfo;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.DensityUtil;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.PayUtils;
import com.lesports.stadium.utils.ScreenUtil;
import com.lesports.stadium.view.CustomExpandableListView;
import com.letv.lepaysdk.ELePayState;
import com.letv.lepaysdk.LePayApi;
import com.letv.lepaysdk.LePayConfig;
import com.letv.lepaysdk.LePay.ILePayCallback;
import com.letv.lepaysdk.view.MProgressDialog;
import com.umeng.analytics.MobclickAgent;

public class OrderDetailActivity extends BaseActivity implements
		OnClickListener, CheckInterface, ModifyCountInterface {

	/**
	 * 列表项数据
	 */
	private CustomExpandableListView mEXlistview;
	/**
	 * 数据源
	 */
	private List<OrderListBean> list = new ArrayList<OrderListBean>();
	/**
	 * 适配器
	 */
	private OrderDetailGoodsExpandableListViewAdapter mAdapter;
	/**
	 * 送货状态
	 */
	private int status;
	/**
	 * 是自取还是送货
	 */
	private String courier = "1";
	/**
	 * 列表项界面传递过来的数据实体类
	 */
	private OrderListBean bean;
	/**
	 * 订单状态
	 */
	private TextView mStatus;
	/**
	 * 返回按钮
	 */
	private ImageView mBack;
	/**
	 * 当订单为代支付的时候需要展示的布局
	 */
	private RelativeLayout mLayout_daifukuan;
	/**
	 * 待付款按钮
	 */
	private TextView mWaitPay;
	/**
	 * 取货地址布局
	 */
	private RelativeLayout mLayout_quhuodizhi;
	/**
	 * 取货详细地址
	 */
	private TextView mQuhuodizhi_detail;
	/**
	 * 取货退款说明
	 */
	private TextView mQuhuoTuikuan;
	/**
	 * 送货的地址布局
	 */
	private LinearLayout mLayout_songhuo;
	/**
	 * 送货的座位号
	 */
	private TextView mSonghuoZuoweihao;
	/**
	 * 送货的退款说明
	 */
	private TextView mSonghuoTuikuan;
	/**
	 * 订单id
	 */
	/**
	 * Dialog dialog
	 */
	private Dialog dialog;

	/**
	 * // 组元素数据列表
	 */
	private List<GroupInfo> groups = new ArrayList<GroupInfo>();
	/**
	 * // 子元素数据列表
	 */
	private Map<String, List<ProductInfo>> children = new HashMap<String, List<ProductInfo>>();
	/**
	 * 订单bean
	 */
	private OrderBean mOrderBean;
	/**
	 * 列表项控件的数据适配器
	 */
	private ShopcartExpandableListViewAdapter selva;
	private PayBean mPayBean;
	private String ZIQU_OR_SONGHUO;
	/**
	 * 用来标记是取消后进来的还是从订单列表进来的
	 */
	private String QUXIAO_OR_LIST = "0";
	/**
	 * 进度条布局
	 */
	private RelativeLayout mLayout_progressbar;
	/**
	 * 流程布局自取
	 */
	private LinearLayout mLayout_lliucheng_ziqu;
	/**
	 * 流程布局送货
	 */
	private LinearLayout mLayout_liucheng_songhuo;
	/**
	 * 十分钟内退款的顶部流程布局
	 */
	private LinearLayout mLayout_tuikuan_nei;
	/**
	 * 十分钟外退款的顶部流程布局
	 */
	private LinearLayout mLayout_tuikuan_wai;
	/**
	 * 自取还是送货
	 */
	private TextView mStatus_zi_or_s;
	/**
	 * 运费
	 */
	private TextView mFeright;
	/**
	 * 商品总价格
	 */
	private TextView mGoodsTotalPrice;
	/**
	 * 十分钟之外退款的订单的流程背景图
	 */
	private ImageView mTenMinOutBackground;
	/**
	 * 十分钟之内退款的订单的流程背景图
	 */
	private ImageView mTenMinInBackground;
	/**
	 * 自取的流程背景图
	 */
	private ImageView mWayZiquBackground;
	/**
	 * 送货的流程北京图
	 */
	private ImageView mWaySonghuoBackground;
	/**
	 * 自取的手机号
	 */
	private TextView mZiqushoujihao;
	/**
	 * 商品运费价格
	 */
	private String mGoodsYunfei;
	/**
	 * 送货手机号
	 */
	private TextView mSonghuoShoujihao;
	/**
	 * 优惠信息
	 */
	private TextView mYouhuiInfo;
	/**
	 * handle需要使用的标记
	 */
	private final int PAY_SUCCESS = 2;// 支付成功
	private final int GO_TO_PAY = 14;// 去支付
	private final int NO_PROGRESS = 114;
	/**
	 * 邮寄地址布局
	 */
	private RelativeLayout mLayout_youji;
	/**
	 * 邮寄姓名
	 */
	private TextView mYoujiName;
	/**
	 * 邮寄电话
	 */
	private TextView mYoujiPhone;
	/**
	 * 邮寄地址
	 */
	private TextView mYoujiAddress;
	/**
	 * 自取的姓名
	 */
	private TextView mZiquNametv;
	/**
	 * 送货的姓名
	 */
	private TextView mSonghuoNametv;
	/**
	 * 自取说明
	 */
	private TextView mZiqushuoming;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case PAY_SUCCESS:
				// 支付成功的结果处理
				useWayHandlePaySuccess();
				break;
			case GO_TO_PAY:
				String jsondatass = (String) msg.obj;
				if (jsondatass != null && !TextUtils.isEmpty(jsondatass)) {
					mLayout_progressbar.setVisibility(View.GONE);
					PayBean paybean = useWayJsonDataPay(jsondatass);
					if (paybean != null
							&& !TextUtils.isEmpty(paybean.getSign())) {
						// 开始调用方法，进行支付
						pay(paybean);
					}
				}
				break;
			case NO_PROGRESS:
				mLayout_progressbar.setVisibility(View.GONE);
				break;
			default:
				break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goodsandcanyinxiangqing);
		initviews();
		// 这里是为了做屏幕适配
		int bgWidth = ScreenUtil.getInstance(OrderDetailActivity.this)
				.getScreenWidth();
		initdatas();
	}

	/**
	 * @param bean2
	 * @param courier2
	 * 
	 */
	private void initaddressview(OrderListBean bean2, String courier2) {
		// TODO Auto-generated method stub
		// 首先先看订单的收货类型
		double totalprice = countGoodsPricess(bean);
		double feright = countGoodsferightss(bean);
		if (!TextUtils.isEmpty(bean.getPrivilege())) {
			mYouhuiInfo.setText(bean.getPrivilege());
		}
		if (!TextUtils.isEmpty(courier2)) {
			if (courier2.equals("0")) {
				mStatus_zi_or_s.setText("自取");
				mZiquNametv.setText(bean2.getUserName());
				mZiqushoujihao.setText(bean2.getTelePhone());
				mQuhuodizhi_detail.setText(bean2.getPosition());
//				mZiqushuoming.setText(bean.getList().get(0).getPickup_remark());
				mLayout_lliucheng_ziqu.setVisibility(View.VISIBLE);
				mLayout_quhuodizhi.setVisibility(View.VISIBLE);
				if (bean.getOrdersType().equals("0")) {
					mQuhuoTuikuan.setVisibility(View.VISIBLE);
				} else {
					mQuhuoTuikuan.setVisibility(View.GONE);
				}
				if (status == 0) {
					mWayZiquBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.three_one));
				} else if (status == 1) {
					mWayZiquBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.three_two));

				} else if (status == 2 || status == 4 || status == 3) {
					mWayZiquBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.three_three));
				}
				mFeright.setText("0.00");
				mGoodsTotalPrice.setText(totalprice + "");
			} else if (courier2.equals("1")) {
				mStatus_zi_or_s.setText("送货");
				mSonghuoNametv.setText(bean2.getUserName());
				mSonghuoShoujihao.setText(bean2.getTelePhone());
				mSonghuoZuoweihao.setText(bean2.getPosition());
				mLayout_liucheng_songhuo.setVisibility(View.VISIBLE);
				mLayout_songhuo.setVisibility(View.VISIBLE);
				if (bean.getOrdersType().equals("0")) {
					mSonghuoTuikuan.setVisibility(View.VISIBLE);
				} else {
					mSonghuoTuikuan.setVisibility(View.GONE);
				}
				if (status == 0) {
					mWaySonghuoBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.four_one));
				} else if (status == 1) {
					mWaySonghuoBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.four_two));
				} else if (status == 2) {
					mWaySonghuoBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.four_three));
				} else if (status == 3 || status == 4) {
					mWaySonghuoBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.four_four));
				}
				String ferights = changeDoubleToString(feright);
				mFeright.setText(ferights);
				mGoodsTotalPrice.setText((totalprice + feright) + "");
			}else if(courier2.equals("2")){
				mStatus_zi_or_s.setText("邮寄");
				mLayout_youji.setVisibility(View.VISIBLE);
				mYoujiPhone.setText(bean2.getMobilePhone());
				mYoujiAddress.setText(bean2.getCityAddress()+bean2.getAddress());
				mYoujiName.setText(bean2.getName());
				//这里还少了一个收货人名称
				mLayout_liucheng_songhuo.setVisibility(View.VISIBLE);
				if (status == 0) {
					mWaySonghuoBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.four_one));
				} else if (status == 1) {
					mWaySonghuoBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.four_two));
				} else if (status == 2) {
					mWaySonghuoBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.four_three));
				} else if (status == 3 || status == 4) {
					mWaySonghuoBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.four_four));
				}
				String ferights = changeDoubleToString(feright);
				mFeright.setText(ferights);
				mGoodsTotalPrice.setText((totalprice + feright) + "");
			}
		}
	}

	/**
	 * 计算商品运费
	 * 
	 * @param bean2
	 * @return
	 */
	private double countGoodsferightss(OrderListBean bean2) {
		// TODO Auto-generated method stub
		double totalprice = 0;
		totalprice = Double.parseDouble(bean2.getFreight());
		return totalprice;
	}

	/**
	 * 计算商品总价
	 * 
	 * @param bean2
	 * @return
	 */
	private double countGoodsPricess(OrderListBean bean2) {
		// TODO Auto-generated method stub
		double totalprice = 0;
		List<OrderListBeanGoodsBean> list = bean2.getList();
		if (list != null && list.size() != 0) {
			for (int i = 0; i < list.size(); i++) {
				double price = Double.parseDouble(list.get(i).getPrice());
				double num = Double.parseDouble(list.get(i).getWareNumber());
				totalprice = totalprice + (price * num);
			}
		}
		return totalprice;
	}

	/**
	 * 将数据加载到列表项
	 */
	private void initdatatoview() {
		// TODO Auto-generated method stub
		if (list != null && list.size() != 0) {
			mAdapter = new OrderDetailGoodsExpandableListViewAdapter(list,
					OrderDetailActivity.this);
			mEXlistview.setAdapter(mAdapter);
			for (int i = 0; i < mAdapter.getGroupCount(); i++) {
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
		switch (status) {
		case 0:
			mStatus.setText("等待买家付款");
			mLayout_daifukuan.setVisibility(View.VISIBLE);
			break;
		case 1:
			mStatus.setText("等待卖家发货");
			break;
		case 2:
			mStatus.setText("等待买家收货");
			break;
		case 3:
			mStatus.setText("交易完成");
			break;
		default:
			break;
		}
	}

	/**
	 * 将数据加载到列表项
	 */
	private void initdatatoview_tuikuan() {
		// TODO Auto-generated method stub
		if (list != null && list.size() != 0) {
			mAdapter = new OrderDetailGoodsExpandableListViewAdapter(list,
					OrderDetailActivity.this);
			mEXlistview.setAdapter(mAdapter);
			for (int i = 0; i < mAdapter.getGroupCount(); i++) {
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
	 * 初始化数据
	 */
	private void initdatas() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		String tag = intent.getStringExtra("tags");
		if (tag.equals("online")) {
			QUXIAO_OR_LIST = "1";
			groups.clear();
			children.clear();
			groups = (List<GroupInfo>) intent.getSerializableExtra("group");
			children = (Map<String, List<ProductInfo>>) intent
					.getSerializableExtra("child");
			// 计算商品总价格
			double totalprice = countGoodsPrice(groups, children);
			double feright = countGoodsferight(groups, children);
			mOrderBean = (OrderBean) intent.getSerializableExtra("bean");
			mPayBean = (PayBean) intent.getSerializableExtra("paybean");
			mGoodsYunfei = intent.getStringExtra("yunfeis");
			String cs = intent.getStringExtra("cs");
			if (cs.equals("canyin")) {
				mQuhuoTuikuan.setVisibility(View.VISIBLE);
				mSonghuoTuikuan.setVisibility(View.VISIBLE);
			} else {
				mQuhuoTuikuan.setVisibility(View.GONE);
//				if(!TextUtils.isEmpty(GlobalParams.Goods_Remark)){
//					mZiqushuoming.setText(GlobalParams.Goods_Remark);
//				}
				mSonghuoTuikuan.setVisibility(View.GONE);
			}
			mStatus.setText("等待买家付款");
			ZIQU_OR_SONGHUO = intent.getStringExtra("zs");
			if (ZIQU_OR_SONGHUO.equals("0")) {
				mStatus_zi_or_s.setText("自取");
//				if(!TextUtils.isEmpty(GlobalParams.Goods_Remark)){
//					mZiqushuoming.setText(GlobalParams.Goods_Remark);
//				}
				mZiquNametv.setText(GlobalParams.ORDER_ZIQU_NAME);
				mQuhuodizhi_detail.setText(GlobalParams.ORDER_ZIQU_ADDRESS);
				mZiqushoujihao.setText(GlobalParams.ORDER_ZIQU_PHONE);
				mLayout_quhuodizhi.setVisibility(View.VISIBLE);
				mLayout_lliucheng_ziqu.setVisibility(View.VISIBLE);
				mWayZiquBackground.setImageBitmap(readBitMap(
						OrderDetailActivity.this, R.drawable.three_one));
				mFeright.setText("0.00");
				mGoodsTotalPrice.setText((totalprice) + "");
			} else if (ZIQU_OR_SONGHUO.equals("1")) {
				mSonghuoNametv.setText(GlobalParams.ORDER_SONGHUO_NAME);
				mSonghuoShoujihao.setText(GlobalParams.ORDER_SONGHUO_PHONE);
				mSonghuoZuoweihao.setText(GlobalParams.ORDER_SONGHUO_ADDRESS);
				mStatus_zi_or_s.setText("送货");
				String yunfeis = changeDoubleToString(feright);
				mFeright.setText(yunfeis);
				String jiage = changeDoubleToString((totalprice + feright));
				mGoodsTotalPrice.setText(jiage);
				mLayout_songhuo.setVisibility(View.VISIBLE);
				mLayout_liucheng_songhuo.setVisibility(View.VISIBLE);
				mWaySonghuoBackground.setImageBitmap(readBitMap(
						OrderDetailActivity.this, R.drawable.four_one));
			}else if(ZIQU_OR_SONGHUO.equals("2")){
				//说明选择了邮寄方式
				mStatus_zi_or_s.setText("邮寄");
				mLayout_youji.setVisibility(View.VISIBLE);
				mLayout_liucheng_songhuo.setVisibility(View.VISIBLE);
				mWaySonghuoBackground.setImageBitmap(readBitMap(
						OrderDetailActivity.this, R.drawable.four_one));
				mYoujiAddress.setText(GlobalParams.ORDER_YOUJI_ADDRESS);
				mYoujiName.setText(GlobalParams.ORDER_YOUJI_NAME);
				mYoujiPhone.setText(GlobalParams.ORDER_YOUJI_PHONE);
				String yunfeis = changeDoubleToString(feright);
				mFeright.setText(yunfeis);
				String jiage = changeDoubleToString((totalprice + feright));
				mGoodsTotalPrice.setText(jiage);
			}
			mLayout_daifukuan.setVisibility(View.VISIBLE);
			usewayAddDataToview(groups, children);

		} else if (tag.equals("goods")) {

		} else if (tag.equals("order")) {
			QUXIAO_OR_LIST = "0";
			String tuikuan = intent.getStringExtra("tuikuan");
			if (!TextUtils.isEmpty(tuikuan)) {
				// 说明是退款的订单详情
				if (tuikuan.equals("xys")) {
					bean = (OrderListBean) intent.getSerializableExtra("bean");
					double totalprice = countGoodsPricess(bean);
					double feright = countGoodsferightss(bean);
					String totalprices = changeDoubleToString(totalprice);
					mGoodsTotalPrice.setText(totalprices);
					String feirghts = changeDoubleToString(feright);
					mFeright.setText(feirghts);
					list.add(bean);
					status = intent.getIntExtra("tag", 0);
					courier = intent.getStringExtra("courser");
					initTuikuanLayout(courier, status);
					initdatatoview_tuikuan();
				} else if (tuikuan.equals("dys")) {
					bean = (OrderListBean) intent.getSerializableExtra("bean");
					double totalprice = countGoodsPricess(bean);
					double feright = countGoodsferightss(bean);
					String totalprices = changeDoubleToString(totalprice);
					mGoodsTotalPrice.setText(totalprices);
					String feirghts = changeDoubleToString(feright);
					mFeright.setText(feirghts);
					list.add(bean);
					status = intent.getIntExtra("tag", 0);
					courier = intent.getStringExtra("courser");
					initTuikuanLayoutw(courier, status);
					initdatatoview_tuikuan();
				}
			} else {
				// 说明不是餐饮和商品的其他订单详情
				bean = (OrderListBean) intent.getSerializableExtra("bean");
				list.add(bean);
				status = intent.getIntExtra("tag", 0);
				courier = intent.getStringExtra("courser");
				// 控制流程部分布局
				initdatatoview();
				initaddressview(bean, courier);
			}
		}
	}

	/**
	 * 将传入的double数据保留有两位转换成字符串返回
	 * 
	 * @param d
	 * @return
	 */
	public String changeDoubleToString(double d) {
		DecimalFormat df = new DecimalFormat("#.##");
		String string = df.format(d);
		return string;
	}

	/**
	 * 计算商品的总运费
	 * 
	 * @param groups2
	 * @param children2
	 * @return
	 */
	private double countGoodsferight(List<GroupInfo> groups2,
			Map<String, List<ProductInfo>> children2) {
		// TODO Auto-generated method stub
		double totalprice = 0;
		if (groups2 != null && groups2.size() != 0) {
			for (int i = 0; i < groups2.size(); i++) {
				if (!TextUtils.isEmpty(groups2.get(i).getmFeright())) {
					double feight = Double.parseDouble(groups2.get(i)
							.getmFeright());
					totalprice = totalprice + feight;
				} else {
					totalprice = totalprice + 0;
				}
			}
		}
		return totalprice;
	}

	/**
	 * 用来计算该集合内的商品总计
	 * 
	 * @param groups2
	 * @param children2
	 */
	private double countGoodsPrice(List<GroupInfo> groups2,
			Map<String, List<ProductInfo>> children2) {
		// TODO Auto-generated method stub
		double totalprice = 0;
		List<ProductInfo> list = children2.get(groups2.get(0).getId());
		if (list != null && list.size() != 0) {
			for (int i = 0; i < list.size(); i++) {
				double price = list.get(i).getPrice();
				double num = list.get(i).getCount();
				totalprice = totalprice + (price * num);
			}
		}
		return totalprice;
	}

	/**
	 * 初始化退款部分
	 * 
	 * @param courier2
	 *            自取还是送货
	 * @param i
	 *            状态
	 */
	private void initTuikuanLayout(String courier2, int i) {
		// TODO Auto-generated method stub
		// 首先先看订单的收货类型
		if (!TextUtils.isEmpty(courier2)) {
			if (!TextUtils.isEmpty(bean.getPrivilege())) {
				mYouhuiInfo.setText(bean.getPrivilege());
			}
			if (courier2.equals("0")) {
				mStatus_zi_or_s.setText("自取");
				mZiquNametv.setText(bean.getUserName());
				mZiqushoujihao.setText(bean.getTelePhone());
				mFeright.setText("0.00");
//				mZiqushuoming.setText(bean.getList().get(0).getPickup_remark());
				mGoodsTotalPrice.setText(bean.getOrderAmount() + "");
				mLayout_quhuodizhi.setVisibility(View.VISIBLE);
				mLayout_songhuo.setVisibility(View.GONE);
				if (bean.getOrdersType().equals("0")) {
					mQuhuoTuikuan.setVisibility(View.VISIBLE);
				} else {
					mQuhuoTuikuan.setVisibility(View.GONE);
				}
				mLayout_tuikuan_nei.setVisibility(View.VISIBLE);
				if (i == 5) {
					mStatus.setText("退款成功");
					mTenMinInBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.two_two));
				} else if (i == 6) {
					// 说明是十分钟内直接进行退款的
					mStatus.setText("退款成功");
					mTenMinInBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.two_two));
				} else if (i == 7) {
					mStatus.setText("退款成功");
					mTenMinInBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.two_two));
				} else if (i == 8) {
					mStatus.setText("退款成功");
					mTenMinInBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.two_two));
				}
			} else if (courier2.equals("1")) {
				mStatus_zi_or_s.setText("送货");
				mSonghuoNametv.setText(bean.getUserName());
				mSonghuoShoujihao.setText(bean.getTelePhone());
				mFeright.setText(bean.getFreight() + "");
				mGoodsTotalPrice.setText(bean.getOrderAmount() + "");
				mLayout_quhuodizhi.setVisibility(View.GONE);
				mLayout_songhuo.setVisibility(View.VISIBLE);
				if (bean.getOrdersType().equals("0")) {
					mQuhuoTuikuan.setVisibility(View.VISIBLE);
				} else {
					mQuhuoTuikuan.setVisibility(View.GONE);
				}

				mLayout_tuikuan_nei.setVisibility(View.VISIBLE);
				if (i == 5) {
					mStatus.setText("退款成功");
					mTenMinInBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.two_two));
				} else if (i == 6) {
					// 说明是十分钟内直接进行退款的
					mStatus.setText("退款成功");
					mTenMinInBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.two_two));
				} else if (i == 7) {
					mStatus.setText("退款成功");
					mTenMinInBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.two_two));
				} else if (i == 8) {
					mStatus.setText("退款成功");
					mTenMinInBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.two_two));
				}

			}else if(courier2.equals("2")){
				mStatus_zi_or_s.setText("邮寄");
				mYoujiPhone.setText(bean.getTelePhone());
				mYoujiAddress.setText(bean.getPosition());
				mYoujiName.setText(bean.getUserName());
				mLayout_youji.setVisibility(View.VISIBLE);
				mLayout_tuikuan_wai.setVisibility(View.VISIBLE);
				if (i == 8) {
					// 说明是十分钟wai直接进行退款的
					mStatus.setText("已发起退款");
					mTenMinOutBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.three_one));
				} else if (i == 9) {
					mStatus.setText("商家审核");
					mTenMinOutBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.three_two));
				} else if (i == 10 || i == 11) {
					mStatus.setText("退款完成");
					mTenMinOutBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.three_three));
				}
			}
		}
	}

	/**
	 * 初始化退款部分
	 * 
	 * @param courier2
	 *            自取还是送货
	 * @param i
	 *            状态
	 */
	private void initTuikuanLayoutw(String courier2, int i) {
		// TODO Auto-generated method stub
		// 首先先看订单的收货类型
		if (!TextUtils.isEmpty(courier2)) {
			if (!TextUtils.isEmpty(bean.getPrivilege())) {
				mYouhuiInfo.setText(bean.getPrivilege());
			}
			if (courier2.equals("0")) {
				mZiqushoujihao.setText(bean.getTelePhone());
				mQuhuodizhi_detail.setText(bean.getPosition());
				mZiquNametv.setText(bean.getUserName());
//				mZiqushuoming.setText(bean.getList().get(0).getPickup_remark());
				mStatus_zi_or_s.setText("自取");
				mLayout_quhuodizhi.setVisibility(View.VISIBLE);
				mLayout_songhuo.setVisibility(View.GONE);
				if (bean.getOrdersType().equals("0")) {
					mQuhuoTuikuan.setVisibility(View.VISIBLE);
				} else {
					mQuhuoTuikuan.setVisibility(View.GONE);
				}
				mLayout_tuikuan_wai.setVisibility(View.VISIBLE);
				if (i == 8) {
					// 说明是十分钟wai直接进行退款的
					mStatus.setText("已发起退款");
					mTenMinOutBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.three_one));
				} else if (i == 9) {
					mStatus.setText("商家审核");
					mTenMinOutBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.three_two));
				} else if (i == 10 || i == 11) {
					mStatus.setText("退款完成");
					mTenMinOutBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.three_three));
				}
			} else if (courier2.equals("1")) {
				mStatus_zi_or_s.setText("送货");
				mSonghuoNametv.setText(bean.getUserName());
				mSonghuoShoujihao.setText(bean.getTelePhone());
				mSonghuoZuoweihao.setText(bean.getPosition());
				//这里还少了一个收货人名称
				mLayout_quhuodizhi.setVisibility(View.GONE);
				mLayout_songhuo.setVisibility(View.VISIBLE);
				if (bean.getOrdersType().equals("0")) {
					mQuhuoTuikuan.setVisibility(View.VISIBLE);
				} else {
					mQuhuoTuikuan.setVisibility(View.GONE);
				}

				mLayout_tuikuan_wai.setVisibility(View.VISIBLE);
				if (i == 8) {
					// 说明是十分钟wai直接进行退款的
					mStatus.setText("已发起退款");
					mTenMinOutBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.three_one));
				} else if (i == 9) {
					mStatus.setText("商家审核");
					mTenMinOutBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.three_two));
				} else if (i == 10 || i == 11) {
					mStatus.setText("退款完成");
					mTenMinOutBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.three_three));
				}
			}else if(courier2.equals("2")){
				mStatus_zi_or_s.setText("邮寄");
				mYoujiPhone.setText(bean.getTelePhone());
				mYoujiAddress.setText(bean.getPosition());
				mYoujiName.setText(bean.getUserName());
				//这里也是少了一个收货人名称
				mLayout_youji.setVisibility(View.VISIBLE);
				mLayout_tuikuan_wai.setVisibility(View.VISIBLE);
				if (i == 8) {
					// 说明是十分钟wai直接进行退款的
					mStatus.setText("已发起退款");
					mTenMinOutBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.three_one));
				} else if (i == 9) {
					mStatus.setText("商家审核");
					mTenMinOutBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.three_two));
				} else if (i == 10 || i == 11) {
					mStatus.setText("退款完成");
					mTenMinOutBackground.setImageBitmap(readBitMap(
							OrderDetailActivity.this, R.drawable.three_three));
				}
			}
		}
	}

	/**
	 * 加载数据
	 * 
	 * @param groups2
	 * @param children2
	 */
	private void usewayAddDataToview(List<GroupInfo> groups2,
			Map<String, List<ProductInfo>> children2) {
		// TODO Auto-generated method stub
		selva = new ShopcartExpandableListViewAdapter(false, groups, children,
				this);
		selva.setCheckInterface(this);// 关键步骤1,设置复选框接口
		selva.setModifyCountInterface(this);// 关键步骤2,设置数量增减接口
		mEXlistview.setAdapter(selva);

		for (int i = 0; i < selva.getGroupCount(); i++) {
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

	/**
	 * 初始化界面view
	 */
	private void initviews() {
		// TODO Auto-generated method stub
//		mZiqushuoming=(TextView) findViewById(R.id.dingdanxiangqign_order_order_xiadanshuomingsss);
		mZiquNametv=(TextView) findViewById(R.id.ziqu_dingdanxiangqign_order_shouhuodizhi_shoujihao_num_name);
		mSonghuoNametv=(TextView) findViewById(R.id.dingdanxiangqign_order_shouhuodizhi_shoujihao_num_name);
		mLayout_youji=(RelativeLayout) findViewById(R.id.new_orderdetail_address_choise);
		mYoujiAddress=(TextView) findViewById(R.id.new_orderdetail_dingdanzhifu_shouhuoren_dizhi);
		mYoujiName=(TextView) findViewById(R.id.new_orderdetail_dingdanzhifu_shouhuoren_name);
		mYoujiPhone=(TextView) findViewById(R.id.new_orderdetail_dingdanzhifu_shouhuoren_dianhua);
		mYouhuiInfo = (TextView) findViewById(R.id.shangpincanyindingdan_yunfei_tv_youhui);
		mSonghuoShoujihao = (TextView) findViewById(R.id.dingdanxiangqign_order_shouhuodizhi_shoujihao_num);
		mZiqushoujihao = (TextView) findViewById(R.id.ziqu_dingdanxiangqign_order_shouhuodizhi_shoujihao_num);
		mWayZiquBackground = (ImageView) findViewById(R.id.layout_order_detail_ziqu_jindu);
		mWaySonghuoBackground = (ImageView) findViewById(R.id.layout_order_detail_songhuo_jindu);
		mTenMinInBackground = (ImageView) findViewById(R.id.layout_order_detail_shifenzhongneituikuan_jindu);
		mTenMinOutBackground = (ImageView) findViewById(R.id.layout_order_detail_shifenzhongzhiwai_jindu);
		mGoodsTotalPrice = (TextView) findViewById(R.id.shangpincanyindingdan_zongjia_tv);
		mFeright = (TextView) findViewById(R.id.shangpincanyindingdan_yunfei_tv);
		mLayout_tuikuan_nei = (LinearLayout) findViewById(R.id.layout_order_detail_shifenzhongneituikuan);
		mLayout_tuikuan_wai = (LinearLayout) findViewById(R.id.layout_order_detail_shifenzhongzhiwai);
		mStatus_zi_or_s = (TextView) findViewById(R.id.shangpincanyindingdan_zhifufangshi_content);
		mLayout_lliucheng_ziqu = (LinearLayout) findViewById(R.id.layout_order_detail_ziqu);
		mLayout_liucheng_songhuo = (LinearLayout) findViewById(R.id.layout_order_detail_songhuo);
		mLayout_progressbar = (RelativeLayout) findViewById(R.id.layout_progress_layout_order_quzhifu);
		mLayout_progressbar.setOnClickListener(this);
		mLayout_progressbar.setVisibility(View.GONE);
		mSonghuoTuikuan = (TextView) findViewById(R.id.dingdanxiangqign_order_zaixiancanyin_tuikuanshuoming_zhifuyesssdd);
		mSonghuoTuikuan.setOnClickListener(this);
		mSonghuoZuoweihao = (TextView) findViewById(R.id.dingdanxiangqign_order_shouhuodizhi);
		mLayout_songhuo = (LinearLayout) findViewById(R.id.dingdanxiangqign_order_layout_songhuodizhi);
		mQuhuoTuikuan = (TextView) findViewById(R.id.dingdanxiangqign_order_zaixiancanyin_tuikuanshuoming_zhifuye);
		mQuhuoTuikuan.setOnClickListener(this);
		mQuhuodizhi_detail = (TextView) findViewById(R.id.dingdanxiangqign_order_shouhuodizhi_zitidizhi);
		mLayout_quhuodizhi = (RelativeLayout) findViewById(R.id.dingdanxiangqign_order_layout_zitidizhi);
		mLayout_daifukuan = (RelativeLayout) findViewById(R.id.layout_dingdanxiangqing_quzhifu);
		mWaitPay = (TextView) findViewById(R.id.dingdanxiangqing_quzhifu_daifkuan);
		mWaitPay.setOnClickListener(this);
		mEXlistview = (CustomExpandableListView) findViewById(R.id.shangpincanyindingdan_order_exListView);
		mStatus = (TextView) findViewById(R.id.shangpincanyindingdan_zhuangtai);
		mBack = (ImageView) findViewById(R.id.shangpincanyindingdan_back);
		mBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.dingdanxiangqing_quzhifu_daifkuan:
			MobclickAgent.onEvent(OrderDetailActivity.this, "pay");
			// 代支付订单去支付
			if (QUXIAO_OR_LIST.equals("0")) {
				// 说明是从列表进来
				// 需要先生成支付信息来进行支付
				mLayout_progressbar.setVisibility(View.VISIBLE);
				UseWayGetPayContent(bean);
			} else if (QUXIAO_OR_LIST.equals("1")) {
				// 说明是从取消支付后进来
				pay(mPayBean);
			}
			break;
		case R.id.dingdanxiangqign_order_zaixiancanyin_tuikuanshuoming_zhifuye:
			// 自提的退款说明
			createDialog_tuikuan();
			break;
		case R.id.dingdanxiangqign_order_zaixiancanyin_tuikuanshuoming_zhifuyesssdd:
			// 送货的退款说明
			createDialog_tuikuan();
			break;
		case R.id.shangpincanyindingdan_back:
			finish();
			break;
		case R.id.tuikuanshuoming_tv_confirm_close:
			dialog.dismiss();
			break;

		default:
			break;
		}

	}

	/**
	 * 该方法用来向服务端提交订单号以及商品信息，以及用户信息，获取支付参数来进行支付
	 * 
	 * @param bean
	 */
	private void UseWayGetPayContent(OrderListBean bean) {
		// TODO Auto-generated method stub
		// 该方法用来向
		if (bean != null) {
			String orderId = bean.getId();// 订单号
			String username = GlobalParams.USER_NAME;// 用户名
			String ip = getPsdnIp();
			String goodsprice = null;
			String currency = "CNY";
			String pay_expire = "45";
			String product_id = null;
			String product_name = null;
			String product_desc = null;
			String product_urls = null;
			// 先判断是哪种
			if (bean.getOrdersType().equals("0")) {
				// 说明是商品
				goodsprice = bean.getOrderAmount();
				product_id = bean.getList().get(0).getId();
				product_name = bean.getList().get(0).getGoodsName();
				product_desc = bean.getList().get(0).getGoodsName();
				product_urls = ConstantValue.BASE_IMAGE_URL
						+ bean.getList().get(0).getSmallImg()
						+ ConstantValue.IMAGE_END;
			} else if (bean.getOrdersType().equals("1")) {
				goodsprice = bean.getOrderAmount();
				product_id = bean.getList().get(0).getId();
				product_name = bean.getList().get(0).getGoodsName();
				product_desc = bean.getList().get(0).getGoodsName();
				product_urls = ConstantValue.BASE_IMAGE_URL
						+ bean.getList().get(0).getSmallImg()
						+ ConstantValue.IMAGE_END;
			} else if (bean.getOrdersType().equals("2")) {
				goodsprice = bean.getOrderAmount();
				product_id = bean.getList_car().get(0).getCar_id();
				product_name = bean.getList_car().get(0)
						.getCar_passenger_Name();
				product_desc = bean.getList_car().get(0)
						.getCar_passenger_Name();
				product_urls = ConstantValue.BASE_IMAGE_URL
						+ bean.getList_car().get(0).getCar_passenger_Name()
						+ ConstantValue.IMAGE_END;
			} else if (bean.getOrdersType().equals("2")) {
				goodsprice = bean.getOrderAmount();
				product_id = bean.getList_zhong().get(0).getId();
				product_name = bean.getList_zhong().get(0).getCrowdfundName();
				product_desc = bean.getList_zhong().get(0).getProjectInfo();
				product_urls = ConstantValue.BASE_IMAGE_URL
						+ bean.getList_zhong().get(0).getPropagatePicture()
						+ ConstantValue.IMAGE_END;
			}
			UseWayGetPayData(orderId, username, ip, goodsprice, currency,
					pay_expire, product_id, product_name, product_desc,
					product_urls);

		}
	}

	/**
	 * 网络获取支付时候需要提交的参数
	 * 
	 * @param orderId
	 *            订单号
	 * @param username
	 *            用户名
	 * @param ip
	 *            当前网络ip
	 * @param goodsprice
	 *            商品价格
	 * @param currency
	 *            当前
	 * @param pay_expire
	 *            运费
	 * @param product_id
	 *            商品id
	 * @param product_name
	 *            商品名
	 * @param product_desc
	 *            商品描述
	 * @param product_urls
	 *            商品图片url
	 */
	private void UseWayGetPayData(String orderId, String username, String ip,
			String goodsprice, String currency, String pay_expire,
			String product_id, String product_name, String product_desc,
			String product_urls) {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("user_name", username);
		params.put("price", goodsprice);
		params.put("currency", currency);
		params.put("pay_expire", pay_expire);
		params.put("product_id", product_id);
		params.put("product_name", product_name);
		params.put("product_desc", product_desc);
		params.put("orderId", orderId);
		params.put("ip", ip);
		params.put("product_urls", product_urls);
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.GET_PAY_DATA, params, new GetDatas() {
					@Override
					public void getServerData(BackData data) {
						// TODO Auto-generated method stub
						if (data == null) {
							// 说明网络获取无数据
						} else {
							Object obj = data.getObject();
							String backdata = obj.toString();
							if (backdata == null && backdata.equals("")) {
							} else {
								Log.i("生成支付订单",
										"走到这里了么？" + data.getNetResultCode());
								if (data.getNetResultCode() == 0) {
									Message msg = new Message();
									msg.arg1 = 14;
									msg.obj = backdata;
									handler.sendMessage(msg);
								} else {
									Message msg = new Message();
									msg.arg1 = 114;
									handler.sendMessage(msg);
								}

							}
						}
					}

				}, false, false);
	}

	/**
	 * 用来获取手机拨号上网（包括CTWAP和CTNET）时由PDSN分配给手机终端的源IP地址。
	 * 
	 * @return
	 * @author SHANHY
	 */
	public static String getPsdnIp() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& inetAddress instanceof Inet4Address) {
						// if (!inetAddress.isLoopbackAddress() && inetAddress
						// instanceof Inet6Address) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (Exception e) {
		}
		return "";
	}

	/**
	 * 自定义的退款说明的dialog
	 */
	private void createDialog_tuikuan() {
		// 需要先弹出一个dialog，在该dialog上面来进行选择
		dialog = new Dialog(OrderDetailActivity.this,
				R.style.Theme_Light_Dialog);
		View dialogView = LayoutInflater.from(OrderDetailActivity.this)
				.inflate(R.layout.tuikuanshuoming, null);
		// 获得dialog的window窗口
		TextView title = (TextView) dialogView
				.findViewById(R.id.tuikuanshuoming_tv_title);
		TextView message = (TextView) dialogView
				.findViewById(R.id.tuikuanshuoming_tv_remind_message);
		message.setText("1.支付后十分钟之内可以直接申请退款;" + "\r\n" + "\r\n"
				+ "2.由于场馆内需要商户提前备餐，在支付完成的十分钟后用户发生退款，需要申请，商户通过后才可以退款。");
		TextView qsure = (TextView) dialogView
				.findViewById(R.id.tuikuanshuoming_tv_confirm_close);
		qsure.setOnClickListener(this);
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

	/**
	 * 该方法用来吊起乐视支付SDK
	 * 
	 * @param paybean
	 */
	private void pay(PayBean paybean) {
		if (paybean != null && !TextUtils.isEmpty(paybean.getSign())) {
			PayParametric parameters = new PayParametric();
			parameters.setVersion(paybean.getVersion());
			parameters.setSign(paybean.getSign());
			parameters.setService(paybean.getService());
			parameters.setUser_id(paybean.getUser_id());
			parameters.setMerchant_business_id(paybean
					.getMerchant_business_id());
			parameters.setUser_name(paybean.getUser_name());
			parameters.setNotify_url(paybean.getNotify_url());
			parameters.setMerchant_no(paybean.getMerchant_no());
			parameters.setOut_trade_no(paybean.getOut_trade_no());
			parameters.setPrice(paybean.getPrice());
			parameters.setCurrency(paybean.getCurrency());
			parameters.setPay_expire(paybean.getPay_expire());
			parameters.setProduct_id(paybean.getProduct_id());
			parameters.setProduct_name(paybean.getProduct_name());
			parameters.setProduct_desc(paybean.getProduct_desc());
			parameters.setProduct_urls(paybean.getProduct_urls());
			parameters.setKey_index(paybean.getKey_index());
			parameters.setInput_charset(paybean.getInput_charset());
			parameters.setSign_type(paybean.getSign_type());

			String param = null;
			try {
				param = PayUtils.getTradeInfo(parameters);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			LePayConfig config = new LePayConfig();
			config.hasShowTimer = false;
			LePayApi.initConfig(OrderDetailActivity.this, config);
			LePayApi.doPay(OrderDetailActivity.this, param,
					new ILePayCallback() {
						@Override
						public void payResult(ELePayState status, String message) {
							// Toast.makeText(OrderDetailActivity.this,
							// "结果:" + status + "|" + message,
							// Toast.LENGTH_SHORT)
							// .show();
							if (ELePayState.CANCEL == status) { // 支付取消
								Log.i("支付取消", "取消支付");
								// Toast.makeText(OrderDetailActivity.this,
								// "支付取消", Toast.LENGTH_SHORT)
								// .show();
								Message msg = new Message();
								msg.arg1 = 222;
								handler.sendMessage(msg);

							} else if (ELePayState.FAILT == status) { // 支付失败
								Toast.makeText(OrderDetailActivity.this,
										"支付失败", Toast.LENGTH_SHORT).show();

							} else if (ELePayState.OK == status) { // 支付成功

								Log.i("支付成功了么", "走了这");
								Message msg = new Message();
								msg.arg1 = 2;
								handler.sendMessage(msg);

							} else if (ELePayState.WAITTING == status) { // 支付中

							}
						}
					});
		} else {
			Toast.makeText(OrderDetailActivity.this, "支付异常", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * 解析获取下来的真正的支付参数
	 * 
	 * @param jsondatass
	 */
	private PayBean useWayJsonDataPay(String jsondatass) {
		// TODO Auto-generated method stub
		PayBean bean = null;
		try {
			bean = new PayBean();
			if (jsondatass != null && !TextUtils.isEmpty(jsondatass)) {
				JSONObject obj = new JSONObject(jsondatass);
				if (obj.has("currency")) {
					bean.setCurrency(obj.getString("currency"));
				}
				if (obj.has("input_charset")) {
					bean.setInput_charset(obj.getString("input_charset"));
				}
				if (obj.has("ip")) {
					bean.setIp(obj.getString("ip"));
				}
				if (obj.has("key_index")) {
					bean.setKey_index(obj.getString("key_index"));
				}
				if (obj.has("merchant_business_id")) {
					bean.setMerchant_business_id(obj
							.getString("merchant_business_id"));
				}
				if (obj.has("merchant_no")) {
					bean.setMerchant_no(obj.getString("merchant_no"));
				}
				if (obj.has("notify_url")) {
					bean.setNotify_url(obj.getString("notify_url"));
				}
				if (obj.has("out_trade_no")) {
					bean.setOut_trade_no(obj.getString("out_trade_no"));
				}
				if (obj.has("pay_expire")) {
					bean.setPay_expire(obj.getString("pay_expire"));
				}
				if (obj.has("price")) {
					bean.setPrice(obj.getString("price"));
				}
				if (obj.has("product_desc")) {
					bean.setProduct_desc(obj.getString("product_desc"));
				}
				if (obj.has("product_id")) {
					bean.setProduct_id(obj.getString("product_id"));
				}
				if (obj.has("product_name")) {
					bean.setProduct_name(obj.getString("product_name"));
				}
				if (obj.has("product_urls")) {
					bean.setProduct_urls(obj.getString("product_urls"));
				}
				if (obj.has("service")) {
					bean.setService(obj.getString("service"));
				}
				if (obj.has("sign")) {
					bean.setSign(obj.getString("sign"));
				}
				if (obj.has("sign_type")) {
					bean.setSign_type(obj.getString("sign_type"));
				}
				if (obj.has("timestamp")) {
					bean.setTimestamp(obj.getString("timestamp"));
				}
				if (obj.has("user_id")) {
					bean.setUser_id(obj.getString("user_id"));
				}
				if (obj.has("user_name")) {
					bean.setUser_name(obj.getString("user_name"));
				}
				if (obj.has("version")) {
					bean.setVersion(obj.getString("version"));
				}
				return bean;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bean;
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

	@Override
	protected void onDestroy() {
		if (handler != null) {
			handler.removeCallbacksAndMessages(null);
		}
		if (children != null) {
			children.clear();
			children = null;
		}
		if (groups != null) {
			groups.clear();
			groups = null;
		}
		if (list != null) {
			list.clear();
			list = null;
		}
		dialog = null;
		super.onDestroy();
	}

	/**
	 * 支付成功的结果处理方法
	 */
	private void useWayHandlePaySuccess() {
		// TODO Auto-generated method stub
		if (ZIQU_OR_SONGHUO.equals("0")) {
			if (groups != null && groups.size() != 0 && children != null
					&& children.size() != 0) {
				Intent intent = new Intent(OrderDetailActivity.this,
						PaySuccessActivity.class);
				intent.putExtra("group", (Serializable) groups);
				intent.putExtra("child", (Serializable) children);
				// 这里是标记
				intent.putExtra("tag", "ziqu");
				intent.putExtra("type", "canyin");
				intent.putExtra("ziqudizhi", mOrderBean.getPosition());
				intent.putExtra("yunfei", mOrderBean.getFreight());
				intent.putExtra("price", mOrderBean.getPayMent());
				startActivity(intent);
			}
		} else if (ZIQU_OR_SONGHUO.equals("1")) {
			if (groups != null && groups.size() != 0 && children != null
					&& children.size() != 0) {
				Intent intent = new Intent(OrderDetailActivity.this,
						PaySuccessActivity.class);
				intent.putExtra("group", (Serializable) groups);
				intent.putExtra("child", (Serializable) children);
				// 这里是标记
				intent.putExtra("tag", "songhuo");
				intent.putExtra("type", "canyin");
				intent.putExtra("yunfei", mOrderBean.getFreight());
				intent.putExtra("price", mOrderBean.getPayMent());
				intent.putExtra("zuoweihao", mOrderBean.getPosition());
				intent.putExtra("phone", mOrderBean.getRemark());
				startActivity(intent);
			}
		}
	}

}
