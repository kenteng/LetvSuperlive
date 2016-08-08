package com.lesports.stadium.activity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.lesports.stadium.R;
import com.lesports.stadium.adapter.OrderDetailExpandableListViewAdapter;
import com.lesports.stadium.adapter.OrderExpandableListViewAdapter;
import com.lesports.stadium.adapter.OrderTicesAdapter;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.base.BaseActivity;
import com.lesports.stadium.bean.AllChipsBean;
import com.lesports.stadium.bean.CrowReportBackBean;
import com.lesports.stadium.bean.CrowdDetailBean;
import com.lesports.stadium.bean.GroupInfo;
import com.lesports.stadium.bean.OrderBean;
import com.lesports.stadium.bean.OrderListBean;
import com.lesports.stadium.bean.OrderListBeanGoodsBean;
import com.lesports.stadium.bean.OrderListBeanTices;
import com.lesports.stadium.bean.OrderListBeanZhongchouBean;
import com.lesports.stadium.bean.PayBean;
import com.lesports.stadium.bean.PayParametric;
import com.lesports.stadium.bean.ProductInfo;
import com.lesports.stadium.bean.ReportBackGroupBean;
import com.lesports.stadium.bean.ShippingAddressBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.PayUtils;
import com.lesports.stadium.view.CustomExpandableListView;
import com.lesports.stadium.view.Mylistview;
import com.letv.lepaysdk.ELePayState;
import com.letv.lepaysdk.LePayApi;
import com.letv.lepaysdk.LePayConfig;
import com.letv.lepaysdk.LePay.ILePayCallback;
import com.umeng.analytics.MobclickAgent;

public class WaitPayActivity extends BaseActivity implements OnClickListener{
	/**
	 * 顶部返回按钮
	 */
	private ImageView mBack;
	/**
	 * 展示数据的listview
	 */
	private CustomExpandableListView mEXlistview;
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
	 * 选择地址的layout
	 */
	private RelativeLayout mLayoutChoise;
	/**
	 * 数据源
	 */
	private List<OrderListBean> list=new ArrayList<OrderListBean>();
	/**
	 * 适配器
	 */
	private OrderDetailExpandableListViewAdapter mAdapter;
	/**
	 * 去支付按钮
	 */
	private TextView mGotoPay;
	/**
	 * 读取出的标记
	 */
	private String tag="";
	/**
	 * 传递过来的实体类对象
	 */
	private OrderListBean bean;
	/**
	 * // 组元素数据列表
	 */
	private List<GroupInfo> groups = new ArrayList<GroupInfo>();
	/**
	 * 进度布局
	 */
	private RelativeLayout mLayout_progress;
	/**
	 * 取消支付以后需要显示的布局
	 */
	private RelativeLayout mLayout_Quxiao;
	/**
	 * 取消支付以后的图片
	 */
	private ImageView mImage_quxiao;
	/**
	 * 取消支付以后的价格
	 */
	private TextView mPrice_quxiao;
	/**
	 * 取消支付以后的介绍
	 */
	private TextView mContent_quxiao;
	/**
	 * bean.get
	 */
	private CrowdDetailBean mZ_Bean;
	/**
	 * 
	 */
	private OrderBean mZ_Order;
	/**
	 * 
	 */
	private ReportBackGroupBean mZ_Group;
	/**
	 * 需要支持的子数据
	 */
	private CrowReportBackBean mZ_ChildBean;
	/**
	 * 
	 * 众筹列表项数据
	 */
	private AllChipsBean mZ_AllBean;
	/**
	 * 姓名
	 */
	private String mPay_TakeGoodsNames;
	private String mPay_mTakeGoodsAddress;
	private String mPay_mTakeGoodsPhone;
	
	/**
	 * 用于标记是从哪个界面过来的
	 */
	private String tags;
	/**
	 * 取消支付以后需要显示的布局
	 */
	private RelativeLayout mLayout_quxiao_xia;
	/**
	 * 取消支付商品金额
	 */
	private TextView mPrice_quxiaoss;
	/**
	 * 取消支付后的商品运费
	 */
	private TextView mYunfei_quxiao;
	/**
	 * 取消支付以后显示的标题
	 */
	private TextView mTitle;
	/**
	 * // 子元素数据列表
	 */
	private Mylistview mListviewTices;
	/**
	 * 处理网络数据的handle
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case 4:
				String jsondatass=(String) msg.obj;
				if(jsondatass!=null&&!TextUtils.isEmpty(jsondatass)){
					Log.i("支付参数",jsondatass);
					PayBean paybean=useWayJsonDataPay(jsondatass);
					if(paybean!=null&&!TextUtils.isEmpty(paybean.getSign())){
						mLayout_progress.setVisibility(View.VISIBLE);
						//开始调用方法，进行支付
						pay(paybean);
					}
				}
				break;
			case 444:
				mLayout_progress.setVisibility(View.VISIBLE);
				Toast.makeText(WaitPayActivity.this,"订单生成失败",0).show();
				break;
				
			default:
				break;
			}

		}

	};
	private Map<String, List<ProductInfo>> children = new HashMap<String, List<ProductInfo>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wait_pay);
		initviews();
		initData();
		initAddress(bean);
	}
	/**
	 * 初始化地址数据
	 * @param bean2
	 */
	private void initAddress(OrderListBean bean2) {
		// TODO Auto-generated method stub
		if(bean2!=null){
			if(bean2.getPosition().contains("收货地址")){
				mTakeGoodsAddress.setText(bean2.getPosition());
			}else{
				mTakeGoodsAddress.setText("收货地址："+bean2.getPosition());
			}
			mTakeGoodsPhone.setText("电话："+bean2.getTelePhone());
			mTakeGoodsName.setText(bean2.getCompanies());
		}
	}
	/**
	 * 将数据加载到列表项
	 */
	private void addDataTolistview() {
		// TODO Auto-generated method stub
		if(list!=null&&list.size()!=0&&!TextUtils.isEmpty(tag)){
			mAdapter=new OrderDetailExpandableListViewAdapter(list, WaitPayActivity.this,tag);
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
	 * 初始化数据
	 */
	private void initData() {
		// TODO Auto-generated method stub
		Intent intent=getIntent();
		tags=intent.getStringExtra("other");
		if(tags.equals("order")){
			bean=(OrderListBean) intent.getSerializableExtra("bean");
			tag=intent.getStringExtra("tag");
			if(bean!=null){
				list.add(bean);
				addDataTolistview();
			}
		}else if(tags.equals("pay")){
			mLayout_quxiao_xia.setVisibility(View.VISIBLE);
			mLayout_Quxiao.setVisibility(View.VISIBLE);
			mZ_AllBean=(AllChipsBean) intent.getSerializableExtra("beans");
			mZ_Bean=(CrowdDetailBean) intent.getSerializableExtra("bean");
			mZ_ChildBean=(CrowReportBackBean) intent.getSerializableExtra("beanss");
			mPay_mTakeGoodsAddress=intent.getStringExtra("address");
			mPay_TakeGoodsNames=intent.getStringExtra("name");
			mPay_mTakeGoodsPhone=intent.getStringExtra("phone");
			mZ_Order=(OrderBean) intent.getSerializableExtra("orderbean");
			initViewUseData();
		}else if(tags.equals("tices")){
			//说明是购票，首先需要显示控件
			bean=(OrderListBean) intent.getSerializableExtra("bean");
			List<OrderListBeanTices> list=bean.getListtices();
			if(list!=null&&list.size()!=0){
				//将数据加载进去
				useWayAddDataToView(list);
			}
		}
	}
	/**
	 * 将票单数据加载到控件上
	 * @param list2
	 */
	private void useWayAddDataToView(List<OrderListBeanTices> list2) {
		// TODO Auto-generated method stub
		OrderTicesAdapter madapter=new OrderTicesAdapter(WaitPayActivity.this,list2);
		mListviewTices.setAdapter(madapter);
	}
	/**
	 * 初始化界面数据
	 */
	private void initViewUseData() {
		// TODO Auto-generated method stub
		mTakeGoodsAddress.setText(mPay_mTakeGoodsAddress);
		mTakeGoodsName.setText(mPay_TakeGoodsNames);
		mTakeGoodsPhone.setText("电话："+mPay_mTakeGoodsPhone);
		mPrice_quxiao.setText("支持￥"+mZ_ChildBean.getReturnPrice());
		mContent_quxiao.setText(mZ_ChildBean.getReturnContent());
		double price=Double.parseDouble(mZ_ChildBean.getReturnPrice());
		double feright=Double.parseDouble(mZ_Bean.getFreight());
		double totalprice=price+feright;
		mPrice_quxiaoss.setText("￥"+totalprice);
		mYunfei_quxiao.setText("(含运费"+feright+"元)");
		LApplication.loader.DisplayImage(ConstantValue.BASE_IMAGE_URL
				+GlobalParams.ZHONGCHOU_ORDER_IMAGE + ConstantValue.IMAGE_END,mImage_quxiao,
				R.drawable.zhoubianshangpin_zhanwei);
		mTitle.setText(mZ_AllBean.getCrowdfundName());
		
	}
	
	/**
	 * 该方法用来向服务端提交订单号以及商品信息，以及用户信息，获取支付参数来进行支付
	 * @param bean
	 */
	private void UseWayGetPayContents(OrderBean bean) {
		// TODO Auto-generated method stub
		//该方法用来向
		if(bean!=null){
			String orderId=bean.getId();//订单号
			String username= GlobalParams.USER_NAME;//用户名
			String ip=getPsdnIp();
			String goodsprice=null;
			String currency="CNY";
			String pay_expire="45";
			String product_id=null;
			String product_name=null;
			String product_desc=null;
			String product_urls="http://serverurl/product_url";
			if(mZ_ChildBean!=null){
				goodsprice=mZ_ChildBean.getReturnPrice();
				product_id=mZ_ChildBean.getId();
				product_name=mZ_ChildBean.getReturnName();
				product_desc=mZ_ChildBean.getReturnContent();
			}else{
				goodsprice="250.41";
				product_id="94188";
				product_name="果汁";
				product_desc="这是一杯果汁";
				product_urls="http://serverurl/product_url";
			}
			UseWayGetPayData(orderId,username,ip,goodsprice,currency,pay_expire,
					product_id,product_name,product_desc,product_urls);
		}
	}
	/**
	 * 初始化界面UI
	 */
	private void initviews() {
		// TODO Auto-generated method stub
		mTitle=(TextView) findViewById(R.id.zhongchou_quxiaozhifu_zhongchoumingcheng);
		mListviewTices=(Mylistview) findViewById(R.id.order_goupiao_listview);
		mPrice_quxiaoss=(TextView) findViewById(R.id.sss_order_zhongchou_hejiduoshaoqian);
		mYunfei_quxiao=(TextView) findViewById(R.id.sss_order_zhongchou_hejiduoshao);
		mLayout_quxiao_xia=(RelativeLayout) findViewById(R.id.sss_order_layout_zhongchou_xuyaoyincang);
		mLayout_quxiao_xia.setVisibility(View.GONE);
		mLayout_Quxiao=(RelativeLayout) findViewById(R.id.layout_weizhifu_quxiao_buju);
		mLayout_Quxiao.setVisibility(View.GONE);
		mImage_quxiao=(ImageView) findViewById(R.id.zhongchou_weizhifu_tupian);
		mPrice_quxiao=(TextView) findViewById(R.id.zhongchou_weizhifu_jiage);
		mContent_quxiao=(TextView) findViewById(R.id.zhongchou_weizhifu_jieshao);
		mLayout_progress=(RelativeLayout) findViewById(R.id.layout_progress_layout_waitpay);
		mBack=(ImageView) findViewById(R.id.dengdaimaijiafukuan_back);
		mBack.setOnClickListener(this);
		mEXlistview=(CustomExpandableListView) findViewById(R.id.dengdaimaijiafukuan_listview);
		mTakeGoodsName=(TextView) findViewById(R.id.dengdaimaijiafukuan_shouhuoren_name);
		mTakeGoodsAddress=(TextView) findViewById(R.id.dengdaimaijiafukuan_shouhuoren_dizhi);
		mTakeGoodsEmail=(TextView) findViewById(R.id.dengdaimaijiafukuan_shouhuoren_youxiang);
		mTakeGoodsPhone=(TextView) findViewById(R.id.dengdaimaijiafukuan_shouhuoren_dianhua);
		mLayoutChoise=(RelativeLayout) findViewById(R.id.dengdaimaijiafukuan_address_choise);
//		mLayoutChoise.setOnClickListener(this);
		mGotoPay=(TextView) findViewById(R.id.order_waitpay_quzhifu);
		mGotoPay.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.dengdaimaijiafukuan_back:
			finish();
			break;
//		case R.id.dengdaimaijiafukuan_address_choise:
//			Intent intents=new Intent(WaitPayActivity.this,SelectAddressActivity.class);
//			startActivityForResult(intents, 2);
//			break;
		case R.id.order_waitpay_quzhifu:
			MobclickAgent.onEvent(WaitPayActivity.this,"pay");
			if(tags.equals("order")){
				if(bean!=null){
					//调用方法，获取支付参数
					mLayout_progress.setVisibility(View.VISIBLE);
					UseWayGetPayContent(bean);
				}
			}else if(tags.equals("pay")){
				UseWayGetPayContents(mZ_Order);
			}
			
			break;

		default:
			break;
		}
	}
	/**
	 * 该方法用来向服务端提交订单号以及商品信息，以及用户信息，获取支付参数来进行支付
	 * @param bean2
	 */
	private void UseWayGetPayContent(OrderListBean bean2) {
		// TODO Auto-generated method stub
		//该方法用来向
		if(bean2!=null){
			String orderId=bean2.getId();//订单号
			String username= GlobalParams.USER_NAME;//用户名
			String ip=getPsdnIp();
			String goodsprice=bean2.getList_zhong().get(0).getReturnPrice();
			String currency="CNY";
			String pay_expire=bean2.getList_zhong().get(0).getReturnPrice();
			String product_id=bean2.getList_zhong().get(0).getId();
			String product_name=bean2.getList_zhong().get(0).getCrowdfundName();
			String product_desc=bean2.getList_zhong().get(0).getReturnContent();
			String product_urls=bean2.getList_zhong().get(0).getPropagatePicture();
			UseWayGetPayData(orderId,username,ip,goodsprice,currency,pay_expire,
					product_id,product_name,product_desc,product_urls);
			
			
		}
	}

	/**
	 * 网络获取支付时候需要提交的参数
	 * @param orderId  订单号
	 * @param username  用户名
	 * @param ip 当前网络ip
	 * @param goodsprice 商品价格
	 * @param currency 当前
	 * @param pay_expire 运费
	 * @param product_id 商品id
	 * @param product_name 商品名
	 * @param product_desc 商品描述
	 * @param product_urls 商品图片url
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
		params.put("pay_expire",pay_expire);
		params.put("product_id", product_id);
		params.put("product_name", product_name);
		params.put("product_desc", product_desc);
		params.put("orderId",orderId);
		params.put("ip", ip);
		params.put("product_urls",product_urls);
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
								Log.i("生成支付订单", "走到这里了么？" + data.getNetResultCode());
								if(data.getNetResultCode()==0){
									Message msg = new Message();
									msg.arg1 =4;
									msg.obj = backdata;
									handler.sendMessage(msg);
								}else{
									Message msg = new Message();
									msg.arg1 =444;
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
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
					//if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet6Address) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (Exception e) {
		}
		return "";
	}
	/**
	 * 模拟数据<br>
	 * 遵循适配器的数据列表填充原则，组元素被放在一个List中，对应的组元素下辖的子元素被放在Map中，<br>
	 * 其键是组元素的Id(通常是一个唯一指定组元素身份的值)
	 * @2016-2-23下午2:11:20
	 */
	private void virtualData(OrderListBeanGoodsBean orderListBeanGoodsBean) 
	{
		//首先，判断组集合中是否存在该组id
		boolean ishave=checkGroupIsHaveTheBean(orderListBeanGoodsBean);
		if(ishave){
			//说明已经存在
			//判断该组id以及该商品是否存在在子集合当中
			boolean ishavess=checkChildIsHava(orderListBeanGoodsBean);
			if(ishavess){
				//说明已经存在
			}else{
				
				ProductInfo infos=new ProductInfo();
				infos.setCount(Integer.parseInt(orderListBeanGoodsBean.getWareNumber()));
				infos.setDesc(orderListBeanGoodsBean.getLabel());
				infos.setId(orderListBeanGoodsBean.getId());
				infos.setImageUrl(orderListBeanGoodsBean.getMediumImg());
				infos.setName(orderListBeanGoodsBean.getLabel());
				infos.setSeller(orderListBeanGoodsBean.getSeller());
				infos.setPrice(Double.parseDouble(orderListBeanGoodsBean.getPrice()));
				children.get(orderListBeanGoodsBean.getClassicId()).add(infos);
			}
		}else{
			//说明未存在，
			GroupInfo info=new GroupInfo();
			info.setId(orderListBeanGoodsBean.getClassicId());
			info.setName(orderListBeanGoodsBean.getClassicName());
			groups.add(info);
			ProductInfo infos=new ProductInfo();
			infos.setCount(Integer.parseInt(orderListBeanGoodsBean.getWareNumber()));
			infos.setDesc(orderListBeanGoodsBean.getGoodsName());
			infos.setId(orderListBeanGoodsBean.getId());
			infos.setImageUrl(orderListBeanGoodsBean.getMediumImg());
			infos.setName(orderListBeanGoodsBean.getLabel());
			infos.setSeller(orderListBeanGoodsBean.getSeller());
			infos.setPrice(Double.parseDouble(orderListBeanGoodsBean.getPrice().trim()));
			List<ProductInfo> list=new ArrayList<ProductInfo>();
			list.add(infos);
			children.put(orderListBeanGoodsBean.getClassicId(),list);
		}
	}
	/**
	 * 在当前组集合中判断，该组id是否已经存在
	 * @param orderListBeanGoodsBean
	 */
	private boolean checkGroupIsHaveTheBean(OrderListBeanGoodsBean orderListBeanGoodsBean) {
		// TODO Auto-generated method stub
		boolean ishave=false;
		for(int i=0;i<groups.size();i++){
			if(groups.get(i).getId().equals(orderListBeanGoodsBean.getClassicId())){
				//说明该组已经存在了
				ishave=true;
				break;
			}else{
				ishave=false;
			}
		}
		return ishave;
		
	}
	/**
	 * 在当前子集合中判断，该商品是否已经存在在子的集合当中
	 * @param bean
	 */
	private boolean checkChildIsHava(OrderListBeanGoodsBean bean) {
		// TODO Auto-generated method stub
		boolean ishaves=false;
		//首先判断该组是否在子map中存在
		if(children.containsKey(bean.getClassicId())){
			//说明该key已经存在，所以
			//根据该key来获取list集合，在集合中判断该商品是否存在
			List<ProductInfo> list=children.get(bean.getClassicId());
			boolean ishave=checkTheGoodsIsHave(list,bean);
			if(ishave){
				//说明已经存在
				ishaves=ishave;
			}else{
				ishaves=ishave;
			}
		}
		return ishaves;
	}

	/**
	 * 判断该商品是否存在在该集合中
	 * @param list
	 * @param bean
	 */
	private boolean checkTheGoodsIsHave(List<ProductInfo> list, OrderListBeanGoodsBean bean) {
		// TODO Auto-generated method stub
		boolean ishave=false;
		for(int i=0;i<list.size();i++){
			if(bean.getId().equals(list.get(i).getId())){
				ishave=true;
				break;
			}else{
				ishave=false;
			}
		}
		return ishave;
	}

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
					if(selectBean.getUserAddress().contains("收货地址")){
						mTakeGoodsAddress.setText(selectBean.getUserAddress());
					}else{
						mTakeGoodsAddress.setText("收货地址："+selectBean.getUserAddress());
					}
					
					mTakeGoodsEmail.setText("邮政编码："+selectBean.getPostcode());
					mTakeGoodsPhone.setText(selectBean.getUserPhone());
				}
			}

		default:
			break;
		}
	}
	
	/**
	 * 解析获取下来的真正的支付参数
	 * @param jsondatass
	 */
	private PayBean useWayJsonDataPay(String jsondatass) {
		// TODO Auto-generated method stub
		PayBean bean = null;
		try {
			bean = new PayBean();
			if(jsondatass!=null&&!TextUtils.isEmpty(jsondatass)){
				JSONObject obj=new JSONObject(jsondatass);
				if(obj.has("currency")){
					bean.setCurrency(obj.getString("currency"));
				}
				if(obj.has("input_charset")){
					bean.setInput_charset(obj.getString("input_charset"));
				}
				if(obj.has("ip")){
					bean.setIp(obj.getString("ip"));
				}
				if(obj.has("key_index")){
					bean.setKey_index(obj.getString("key_index"));
				}
				if(obj.has("merchant_business_id")){
					bean.setMerchant_business_id(obj.getString("merchant_business_id"));
				}
				if(obj.has("merchant_no")){
					bean.setMerchant_no(obj.getString("merchant_no"));
				}
				if(obj.has("notify_url")){
					bean.setNotify_url(obj.getString("notify_url"));
				}
				if(obj.has("out_trade_no")){
					bean.setOut_trade_no(obj.getString("out_trade_no"));
				}
				if(obj.has("pay_expire")){
					bean.setPay_expire(obj.getString("pay_expire"));
				}
				if(obj.has("price")){
					bean.setPrice(obj.getString("price"));
				}
				if(obj.has("product_desc")){
					bean.setProduct_desc(obj.getString("product_desc"));
				}
				if(obj.has("product_id")){
					bean.setProduct_id(obj.getString("product_id"));
				}
				if(obj.has("product_name")){
					bean.setProduct_name(obj.getString("product_name"));
				}
				if(obj.has("product_urls")){
					bean.setProduct_urls(obj.getString("product_urls"));
				}
				if(obj.has("service")){
					bean.setService(obj.getString("service"));
				}
				if(obj.has("sign")){
					bean.setSign(obj.getString("sign"));
				}
				if(obj.has("sign_type")){
					bean.setSign_type(obj.getString("sign_type"));
				}
				if(obj.has("timestamp")){
					bean.setTimestamp(obj.getString("timestamp"));
				}
				if(obj.has("user_id")){
					bean.setUser_id(obj.getString("user_id"));
				}
				if(obj.has("user_name")){
					bean.setUser_name(obj.getString("user_name"));
				}
				if(obj.has("version")){
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
	
	private void pay(PayBean paybean) {
		Log.i("221", "pay start");
		if(paybean!=null&&!TextUtils.isEmpty(paybean.getSign())){
			PayParametric parameters = new PayParametric();
			parameters.setVersion(paybean.getVersion());
			parameters.setSign(paybean.getSign());
			parameters.setService(paybean.getService());
			parameters.setMerchant_business_id(paybean.getMerchant_business_id());
			parameters.setUser_id(paybean.getUser_id());
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
			parameters
					.setProduct_urls(paybean.getProduct_urls());
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
			LePayApi.initConfig(WaitPayActivity.this, config);
			LePayApi.doPay(WaitPayActivity.this, param, new ILePayCallback() {
				@Override
				public void payResult(ELePayState status, String message) {
//					Toast.makeText(OrderActivity.this,
//							"结果:" + status + "|" + message, Toast.LENGTH_SHORT)
//							.show();
					if (ELePayState.CANCEL == status) { // 支付取消
//						Toast.makeText(WaitPayActivity.this,
//								"取消支付", Toast.LENGTH_SHORT)
//								.show();

					} else if (ELePayState.FAILT == status) { // 支付失败
						Toast.makeText(WaitPayActivity.this,
								"支付失败", Toast.LENGTH_SHORT)
								.show();

					} else if (ELePayState.OK == status) { // 支付成功
//						Toast.makeText(WaitPayActivity.this,
//								"支付成功", Toast.LENGTH_SHORT)
//								.show();
						Message msg = new Message();
						msg.arg1 = 2;
						handler.sendMessage(msg);

					} else if (ELePayState.WAITTING == status) { // 支付中

					}
				}
			});
		}else{
			Toast.makeText(WaitPayActivity.this, "支付异常", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	@Override
	protected void onDestroy() {
		if(handler!=null){
			handler.removeCallbacksAndMessages(null);
		}
		super.onDestroy();
	}


}
