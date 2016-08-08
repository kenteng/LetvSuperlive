/**
 * 
 */
package com.lesports.stadium.activity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.lesports.stadium.R;
import com.lesports.stadium.adapter.ShopcartExpandableListViewAdapter;
import com.lesports.stadium.adapter.ShopcartExpandableListViewAdapter.CheckInterface;
import com.lesports.stadium.adapter.ShopcartExpandableListViewAdapter.ModifyCountInterface;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.base.BaseActivity;
import com.lesports.stadium.bean.GroupInfo;
import com.lesports.stadium.bean.ProductInfo;
import com.lesports.stadium.bean.RoundGoodsBean;
import com.lesports.stadium.fragment.ServiceFragment;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.Utils;
import com.umeng.analytics.MobclickAgent;

/**
 * ***************************************************************
 * 
 * @Desc : 购物车activity
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwc
 * 
 * @data:
 * 
 * @Version : v1.0
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */

public class BuyCarActivity extends BaseActivity implements CheckInterface, ModifyCountInterface, 
	OnClickListener{
	/**
	 * 展示数据的lisview
	 */
	private ExpandableListView exListView;
	/**
	 * 底部的全选按钮
	 */
	private CheckBox cb_check_all;
	/**
	 * 底部的总价格
	 */
	private TextView tv_total_price;
	/**
	 * 底部删除按钮，暂时先隐藏，若需要再取出
	 */
	private TextView tv_delete;
	/**
	 * 底部支付按钮
	 */
	private TextView tv_go_to_pay;
	/**
	 * 需要改变的商品总价
	 */
	private double totalPrice = 0.00;// 购买的商品总价
	/**
	 * 商品的总个数
	 */
	private int GoodsNum=0;

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
	 * // 组元素数据列表(用来存储已选定的商品组)
	 */
	private List<GroupInfo> groupsNewOrder = new ArrayList<GroupInfo>();
	/**
	 * // 子元素数据列表(用来存储已选定的商品子)
	 */
	private Map<String, List<ProductInfo>> childrenNewOrder = new HashMap<String, List<ProductInfo>>();
	/**
	 * 顶部返回按钮
	 */
	private ImageView mBack;
	/**
	 * 顶部右侧编辑按钮
	 */
	private TextView mSubmit;
	/**
	 * 用于标示编辑按钮是否呗点击
	 */
	private boolean isTag=false;
	/**
	 * 标记组
	 */
	private final String GROUP_TAG="group";
	/**
	 * 标记子
	 */
	private final String CHILD_TAG="child";
	/**
	 * 购物车没有数据的时候需要显示的布局
	 */
	private RelativeLayout mLayout_noHavegoods;
	/**
	 * 底部布局
	 */
	private LinearLayout mLayout_bottom; 
	/**
	 * 本类实例对象
	 */
	public static BuyCarActivity instance;
	/**
	 * 点击去逛逛按钮
	 */
	private TextView mGoGuangguang;
	/**
	 * 用来标记是从那个界面跳转过来的标记
	 */
	private String tag;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		instance=this;
		setContentView(R.layout.activity_buycar);
		MobclickAgent.onEvent(BuyCarActivity.this,"ShowCart");
		initIntentData();
		initView();
		initSQLdata();
		initEvents();
	}
	/**
	 * 接受传递过来的数据
	 * @param tag2
	 */
	private void initIntentData() {
		Intent intent=getIntent();
		tag=intent.getStringExtra("tag");
	}
	/**
	 * 从数据库中读取商品数据
	 */
	private void initSQLdata() {
		children.clear();
		groups.clear();
		List<RoundGoodsBean> list=LApplication.dbBuyCar.findByCondition("buy_mUsename= ?", 
				new String[]{GlobalParams.USER_ID}, null);
		if(list!=null&&list.size()!=0){
			cb_check_all.setChecked(true);
			//调用方法，计算商品数量以及价格
			useWayCountGoodsPrice(list);
			GlobalParams.Goods_Address=list.get(0).getPickup_address();
			GlobalParams.Goods_Store_Name=list.get(0).getSellerName();
			GlobalParams.Goods_Store_Phone=list.get(0).getTelephone();
			mLayout_bottom.setVisibility(View.VISIBLE);
			exListView.setVisibility(View.VISIBLE);
			mLayout_noHavegoods.setVisibility(View.GONE);
			int size=list.size();
			for(int i=0;i<size;i++){
				virtualData(list.get(i));
			}
		}else{
			mLayout_bottom.setVisibility(View.GONE);
			exListView.setVisibility(View.GONE);
			mLayout_noHavegoods.setVisibility(View.VISIBLE);
		}
		
	}
	/**
	 * 用来界面初始时候计算商品总价格和总数量
	 * @param list
	 */
	private void useWayCountGoodsPrice(List<RoundGoodsBean> list) {
		double totalprice=0;
		int num=0;
		for(int i=0;i<list.size();i++){
			double price=0;
			if(!TextUtils.isEmpty(list.get(i).getPrice())){
				price=Double.parseDouble(list.get(i).getPrice());
			}
			int nums=0;
			if(!TextUtils.isEmpty(list.get(i).getmNum())){
				nums=Integer.parseInt(list.get(i).getmNum());
			}
			totalprice=totalprice+(nums*price);
			num=num+nums;
		}
		String price=changeDoubleToString(totalprice);
		tv_total_price.setText("￥:"+Utils.parseTwoNumber(price));
//		tv_go_to_pay.setText("结算("+num+")");
//		tv_go_to_pay.setText("支付");
	}
	/**
	 * 从数据库中读取商品数据,看是否显示提示语
	 */
	private void initSQLdataisShow() {
		List<RoundGoodsBean> list=LApplication.dbBuyCar.findByCondition("buy_mUsename= ?", 
				new String[]{GlobalParams.USER_ID}, null);
		if(list!=null&&list.size()!=0){
			mLayout_bottom.setVisibility(View.VISIBLE);
			exListView.setVisibility(View.VISIBLE);
			mLayout_noHavegoods.setVisibility(View.GONE);
		}else{
			mLayout_bottom.setVisibility(View.GONE);
			exListView.setVisibility(View.GONE);
			mLayout_noHavegoods.setVisibility(View.VISIBLE);
		}
	}
	

	/**
	 * 将传入的double数据保留有两位转换成字符串返回
	 * @param d
	 * @return
	 */
	public String changeDoubleToString(double d){
		DecimalFormat df = new DecimalFormat("#.##");
		String string = df.format(d);
		return string;
	}
	/**
	 * 初始化view控件的方法
	 * @2016-2-23下午1:33:21
	 */
	private void initView()
	{
		mGoGuangguang=(TextView) findViewById(R.id.gouwucheweikong_sssss);
		mGoGuangguang.setOnClickListener(this);
		mLayout_noHavegoods=(RelativeLayout) findViewById(R.id.layout_buycar_meiyouwupinshihou);
		mLayout_bottom=(LinearLayout) findViewById(R.id.layout_buycar_gone);
		/**调用方法，初始化假数据 */
		exListView = (ExpandableListView) findViewById(R.id.exListView);
		cb_check_all = (CheckBox) findViewById(R.id.all_chekbox);
		tv_total_price = (TextView) findViewById(R.id.tv_total_price);
		tv_delete = (TextView) findViewById(R.id.tv_delete);
		tv_go_to_pay = (TextView) findViewById(R.id.tv_go_to_pay);
		mBack=(ImageView) findViewById(R.id.buy_back);
		mBack.setOnClickListener(this);
		mSubmit=(TextView) findViewById(R.id.buy_subtitle);
		
	}
	

	/**
	 * 初始化列表项的适配器对象，并且规定该可拓展的listview以展开的方式来呈现
	 * @2016-2-23下午2:09:10
	 */
	private void initEvents()
	{
		selva = new ShopcartExpandableListViewAdapter(true,groups, children, this);
		selva.setCheckInterface(this);// 关键步骤1,设置复选框接口
		selva.setModifyCountInterface(this);// 关键步骤2,设置数量增减接口
		exListView.setAdapter(selva);

		for (int i = 0; i < selva.getGroupCount(); i++)
		{
			exListView.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
		}
		exListView.setOnGroupClickListener(new OnGroupClickListener() {
            
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                    int groupPosition, long id) {
                
                return true;
            }
        });
		doCheckAll();
		cb_check_all.setOnClickListener(this);
		tv_delete.setOnClickListener(this);
		tv_go_to_pay.setOnClickListener(this);
		mSubmit.setOnClickListener(this);
		cb_check_all.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//根据状态，来计算总价格
				int num=0;
				if(isChecked){
					//被选中状态
					for (int i = 0; i < groups.size(); i++)
					{
						groups.get(i).setChoosed(cb_check_all.isChecked());
						GroupInfo group = groups.get(i);
						List<ProductInfo> childs = children.get(group.getId());
						for (int j = 0; j < childs.size(); j++)
						{		
								totalPrice=totalPrice+(childs.get(j).getCount()*childs.get(j).getPrice());
								num=num+childs.get(j).getCount();
						}
					}
					String price=changeDoubleToString(totalPrice);
					tv_total_price.setText("￥"+Utils.parseTwoNumber(price));
//					tv_go_to_pay.setText("结算(" + num + ")");
				}else{
					//全部被取消状态
					totalPrice=0.00;
					String price=changeDoubleToString(totalPrice);
					tv_total_price.setText("￥"+Utils.parseTwoNumber(price));
//					tv_go_to_pay.setText("结算(" + 0 + ")");
				}
			}
		});
	}
	/**
	 * 模拟数据<br>
	 * 遵循适配器的数据列表填充原则，组元素被放在一个List中，对应的组元素下辖的子元素被放在Map中，<br>
	 * 其键是组元素的Id(通常是一个唯一指定组元素身份的值)
	 * @2016-2-23下午2:11:20
	 */
	private void virtualData(RoundGoodsBean bean) 
	{
		//首先，判断组集合中是否存在该组id
		boolean ishave=checkGroupIsHaveTheBean(bean);
		if(ishave){
			//说明已经存在
			//判断该组id以及该商品是否存在在子集合当中
			boolean ishavess=checkChildIsHava(bean);
			if(ishavess){
				//说明已经存在
			}else{
				
				ProductInfo infos=new ProductInfo();
				infos.setCount(Integer.parseInt(bean.getmNum()));
				infos.setDesc(bean.getGoodsName());
				infos.setSpace_id(bean.getSpaceid());
				infos.setmGoodsSpaceShuoming(bean.getmGoodsSpecifications());
				infos.setId(bean.getId());
				infos.setPickup_remark(bean.getPickup_remark());
				infos.setSendWay(bean.getDelivery_type());
				infos.setImageUrl(bean.getMediumImg());
				infos.setName(bean.getLabel());
				infos.setSeller(bean.getSeller());
				infos.setFrieight(bean.getFreight());
				if(!TextUtils.isEmpty(bean.getPrice())){
					infos.setPrice(Double.parseDouble(bean.getPrice()));
				}else{
					infos.setPrice(1);
				}
				children.get(bean.getSeller()).add(infos);
			}
		}else{
			
			//说明未存在，
			GroupInfo info=new GroupInfo();
			info.setId(bean.getSeller());
			info.setName(bean.getSellerName());
			info.setSelerAddress(bean.getPickup_address());
			info.setmFeright(bean.getFreight());
			groups.add(info);
			ProductInfo infos=new ProductInfo();
			infos.setCount(Integer.parseInt(bean.getmNum()));
			infos.setDesc(bean.getGoodsName());
			infos.setId(bean.getId());
			infos.setSpace_id(bean.getSpaceid());
			infos.setSendWay(bean.getDelivery_type());
			infos.setImageUrl(bean.getMediumImg());
			infos.setName(bean.getLabel());
			infos.setPickup_remark(bean.getPickup_remark());
			infos.setSpace_id(bean.getSpaceid());
			infos.setSeller(bean.getSeller());
			infos.setFrieight(bean.getFreight());
			if(!TextUtils.isEmpty(bean.getPrice())){
				infos.setPrice(Double.parseDouble(bean.getPrice().trim()));
			}else{
				infos.setPrice(1);
			}
			List<ProductInfo> list=new ArrayList<ProductInfo>();
			list.add(infos);
			children.put(bean.getSeller(),list);
		}
	}
	

	/**
	 * 在当前子集合中判断，该商品是否已经存在在子的集合当中
	 * @param bean
	 */
	private boolean checkChildIsHava(RoundGoodsBean bean) {
		boolean ishaves=false;
		//首先判断该组是否在子map中存在
		if(children.containsKey(bean.getSeller())){
			//说明该key已经存在，所以
			//根据该key来获取list集合，在集合中判断该商品是否存在
			List<ProductInfo> list=children.get(bean.getSeller());
			boolean ishave=checkTheGoodsIsHave(list,bean);
			if(ishave){
				//说明已经存在
				ishaves=true;
			}else{
				ishaves=false;
			}
		}
		return ishaves;
	}

	/**
	 * 判断该商品是否存在在该集合中
	 * @param list
	 * @param bean
	 */
	private boolean checkTheGoodsIsHave(List<ProductInfo> list, RoundGoodsBean bean) {
		boolean ishave=false;
		for(int i=0;i<list.size();i++){
			if(bean.getId().equals(list.get(i).getId())&&bean.getSpaceid().equals(list.get(i).getSpace_id())){
				ishave=true;
				break;
			}else{
				ishave=false;
			}
		}
		return ishave;
	}

	/**
	 * 在当前组集合中判断，该组id是否已经存在
	 * @param bean
	 */
	private boolean checkGroupIsHaveTheBean(RoundGoodsBean bean) {
		boolean ishave=false;
		if(groups!=null&&groups.size()!=0){
			for(int i=0;i<groups.size();i++){
				if(!TextUtils.isEmpty(bean.getSeller())){
					if(groups.get(i).getId().equals(bean.getSeller())){
						//说明该组已经存在了
						ishave=true;
						break;
					}else{
						ishave=false;
					}
				}
			}
		}
		return ishave;
		
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.all_chekbox:
			/**调用方法，进行全选 */
			doCheckAll();
			break;
		case R.id.tv_go_to_pay:
			//调用方法，开始处理被选定的数据
			getDataForOrder(groups,children);
			if(groupsNewOrder!=null&&groupsNewOrder.size()!=0&&childrenNewOrder!=null&&childrenNewOrder.size()!=0){
				//这里必须做出限制，每次支付只能够选择一个商家去进行支付，
				if(groupsNewOrder.size()>1){
					//说明支付不符合要求
					Toast.makeText(getApplicationContext(), "暂不支持多个商家，请选择单个商家进行支付", Toast.LENGTH_SHORT).show();
					groupsNewOrder.clear();
					childrenNewOrder.clear();
				}else if(groupsNewOrder.size()==1){
					Intent intent=new Intent(BuyCarActivity.this, OrderActivity.class);
					intent.putExtra(GROUP_TAG,(Serializable)groupsNewOrder);
					intent.putExtra(CHILD_TAG,(Serializable)childrenNewOrder);
					startActivity(intent);
					groupsNewOrder.clear();
					childrenNewOrder.clear();
				}
				
			}else{
				Toast.makeText(getApplicationContext(), "请选择商品", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.buy_subtitle:
			if(isTag){
				//说明未点击，此刻为编辑状态
				mSubmit.setText("编辑");
				isTag=!isTag;
				selva.setIsShow(isTag);
				cb_check_all.setVisibility(View.GONE);
			}else{
				mSubmit.setText("完成");
				isTag=!isTag;
				selva.setIsShow(isTag);
				cb_check_all.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.buy_back:
//			if(ServiceFragment.instence!=null){
//				ServiceFragment.instence.tag_views=2;
//			}
			finish();
			break;
		case R.id.gouwucheweikong_sssss:
			if(GoodsDetailActivity.instance!=null){
				if(ServiceFragment.instence!=null){
					ServiceFragment.instence.tag_views=1;
				}
				GoodsDetailActivity.instance.finish();
			}
			finish();
			break;
		}
	}

	/**
	 * 用来从数据源中取出已经被用户选定的数据
	 * @2016-2-25上午11:02:34
	 */
	private void getDataForOrder(List<GroupInfo> groupsNew2,
			Map<String, List<ProductInfo>> childrenNew2) {
		for(int i=0;i<groupsNew2.size();i++){
			GroupInfo info=groupsNew2.get(i);
			//从存储子集合的map中得到该组对应的集合数据
			List<ProductInfo> list=childrenNew2.get(info.getId());
			for(int j=0;j<list.size();j++){
				ProductInfo infoP=list.get(j);
				if(infoP.isChoosed()){
					if(groupsNewOrder.contains(info)){
					}else{
						groupsNewOrder.add(info);
					}
				}
			}
			
		}
		
		for(int h=0;h<groupsNewOrder.size();h++){
			GroupInfo infos=groupsNewOrder.get(h);
			List<ProductInfo> list=childrenNew2.get(infos.getId());
			List<ProductInfo> products = new ArrayList<ProductInfo>();
			for(int k=0;k<list.size();k++){
				ProductInfo product=list.get(k);
				if(product.isChoosed()){
					products.add(product);
				}
			}
			childrenNewOrder.put(infos.getId(),products);
		}
		
	}


	/**
	 * 删除操作<br>
	 * @param product.getId() 
	 */
	protected void doDeletenum(ProductInfo product)
	{
		/**
		 *从数据库中直接将该商品删除，然后重新给界面刷新配置数据
		 */
		deleteFoodOfDB(product);
		//重新读取数据库，然后刷新界面
		groups.clear();
		children.clear();
		selva.notifyDataSetChanged();
		selva=null;
		initSQLdata();
		initEvents();
		calculate();
		initSQLdataisShow();
	}
	/**
	 * 根据传入商品，从数据库中删除该商品
	 * @param serviceCateringDetailBean
	 */
	private void deleteFoodOfDB(
			ProductInfo product) {
		LApplication.dbBuyCar.delete("buy_mUsename= ? AND buy_id = ? AND buy_mSpaceid =?", 
							new String[]{GlobalParams.USER_ID,product.getId(),product.getSpace_id()});
	}
	@Override
	public void doIncrease(String id,int groupPosition, int childPosition, View showCountView, boolean isChecked)
	{

		//调用方法，对数据库内该商品的数量进行修改
		
		ProductInfo product = (ProductInfo) selva.getChild(groupPosition, childPosition);
		boolean is=checkTheGoodsIsHaveStorck(product, GlobalParams.USER_ID);
		if(is){
			int currentCount = product.getCount();
			currentCount++;
			changeSQLGOODSnMUM_add(product);
			product.setCount(currentCount);
			((TextView) showCountView).setText(currentCount + "");

			selva.notifyDataSetChanged();
			calculate();
		}else{
			Toast.makeText(BuyCarActivity.this, "商品库存不足", Toast.LENGTH_SHORT).show();
		}
		
	}
	/**
	 * 判断该商品库存是否满足客户需求
	 * @param roundGoodsBean
	 * @param uSER_ID 
	 */
	private boolean checkTheGoodsIsHaveStorck(ProductInfo product, String uSER_ID) {
		boolean is=false;
		List<RoundGoodsBean> list=LApplication.dbBuyCar.findByCondition("buy_mUsename= ? AND buy_id= ? AND buy_mSpaceid= ?", new String[]{uSER_ID,product.getId(),product.getSpace_id()}, null);
		if(list!=null&&list.size()!=0){
			if(!TextUtils.isEmpty(list.get(0).getStock())){
				double nums=Double.parseDouble(list.get(0).getStock());
				double num_list=0;
				double num_sql=0;
				if(!TextUtils.isEmpty(list.get(0).getmNum())){
					num_sql=Double.parseDouble(list.get(0).getmNum());
					if(!TextUtils.isEmpty(product.getCount()+"")){
						num_list=Double.parseDouble(product.getCount()+"");
					}
					if(nums>(num_list+num_sql)){
						is=true;
					}else{
						is=false;
					}
				}
			}
		}
		return is;
		
	}
	/**
	 * 用来动态修改数据库，该id所对应的商品的数量,这里是进行加操作
	 * @param product
	 * @param currentCount
	 */
	private void changeSQLGOODSnMUM_add(ProductInfo product) {
		List<RoundGoodsBean> list=LApplication.dbBuyCar.findByCondition("buy_mUsename= ? AND buy_id= ? AND buy_mSpaceid= ?", new String[]{GlobalParams.USER_ID,product.getId(),product.getSpace_id()}, null);
		if(list!=null&&list.size()!=0){
			RoundGoodsBean roundGoodsBeans=list.get(0);
			int count=Integer.parseInt(roundGoodsBeans.getmNum());
			count++;
			ContentValues values = new ContentValues();
			values.put("buy_mNum",count+"");
			int i=LApplication.dbBuyCar.update(values,
				"buy_mUsename = ? AND buy_id= ? AND buy_mSpaceid= ?", new String[] {
						GlobalParams.USER_ID, roundGoodsBeans.getId(),roundGoodsBeans.getSpaceid()});
		}
	}
	/**
	 * 用来动态修改数据库，该id所对应的商品的数量,这里是进行减操作
	 * @param product
	 * @param currentCount
	 */
	private void changeSQLGOODSnMUM_jianqu(ProductInfo product) {
		List<RoundGoodsBean> list=LApplication.dbBuyCar.findByCondition("buy_mUsename= ? AND buy_id= ? AND buy_mSpaceid= ?", new String[]{GlobalParams.USER_ID,product.getId(),product.getSpace_id()}, null);
		if(list!=null&&list.size()!=0){
			RoundGoodsBean roundGoodsBeans=list.get(0);
			int count=Integer.parseInt(roundGoodsBeans.getmNum());
			if(count>=1){
				count--;
				ContentValues values = new ContentValues();
				values.put("buy_mNum",count+"");
				int i=LApplication.dbBuyCar.update(values,
					"buy_mUsename = ? AND buy_id= ? AND buy_mSpaceid= ?", new String[] {
							GlobalParams.USER_ID, roundGoodsBeans.getId(),roundGoodsBeans.getSpaceid()});
			}else{
				int i=LApplication.dbBuyCar.delete("buy_id =?  AND buy_mUsename= ? AND buy_mSpaceid= ?", new String[]{product.getId(),GlobalParams.USER_ID,product.getSpace_id()});
			}
		}
	}
	@Override
	public void doDecrease(String id,int groupPosition, int childPosition, View showCountView, boolean isChecked)
	{

		ProductInfo product = (ProductInfo) selva.getChild(groupPosition, childPosition);
		int currentCount = product.getCount();
		if (currentCount<= 1){
			//这里需要，调用方法，将该条数据直接删除
			doDeletenum(product);
		}else{
			currentCount--;
			changeSQLGOODSnMUM_jianqu(product);
			product.setCount(currentCount);
			((TextView) showCountView).setText(currentCount + "");
			selva.notifyDataSetChanged();
			calculate();
		}

		
	}

	@Override
	public void checkGroup(int groupPosition, boolean isChecked)
	{
		GroupInfo group = groups.get(groupPosition);
		List<ProductInfo> childs = children.get(group.getId());
		for (int i = 0; i < childs.size(); i++)
		{
			childs.get(i).setChoosed(isChecked);
		}
		if (isAllCheck())
			cb_check_all.setChecked(true);
		else
			cb_check_all.setChecked(false);
		selva.notifyDataSetChanged();
		calculate();
	}

	@Override
	public void checkChild(int groupPosition, int childPosiTion, boolean isChecked)
	{
		boolean allChildSameState = true;// 判断改组下面的所有子元素是否是同一种状态
		GroupInfo group = groups.get(groupPosition);
		List<ProductInfo> childs = children.get(group.getId());
		childs.get(childPosiTion).setChoosed(isChecked);
		for (int i = 0; i < childs.size(); i++)
		{
			if (childs.get(i).isChoosed() != isChecked)
			{
				allChildSameState = false;
				break;
			}
		}
		if (allChildSameState)
		{
			group.setChoosed(isChecked);// 如果所有子元素状态相同，那么对应的组元素被设为这种统一状态
		} else
		{
			group.setChoosed(false);// 否则，组元素一律设置为未选中状态
		}

		if (isAllCheck())
			cb_check_all.setChecked(true);
		else
			cb_check_all.setChecked(false);
		selva.notifyDataSetChanged();
		calculate();
	}

	private boolean isAllCheck()
	{

		for (GroupInfo group : groups)
		{
			if (!group.isChoosed())
				return false;

		}

		return true;
	}
	/**
	 * 该方法用来动态的进行全选和反选
	 * @2016-2-23下午2:14:26
	 */
	private void doCheckAll()
	{
		for (int i = 0; i < groups.size(); i++)
		{
			groups.get(i).setChoosed(cb_check_all.isChecked());
			GroupInfo group = groups.get(i);
			List<ProductInfo> childs = children.get(group.getId());
			for (int j = 0; j < childs.size(); j++)
			{
				childs.get(j).setChoosed(cb_check_all.isChecked());
			}
		}
		selva.notifyDataSetChanged();
	}

	/**
	 * * 统计操作<br>
	 * 1.先清空全局计数器<br>
	 * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作<br>
	 * 3.给底部的textView进行数据填充
	 * @2016-2-23下午2:15:18
	 */
	private void calculate()
	{
		GoodsNum=0;
		totalPrice = 0.00;
		
		for (int i = 0; i < groups.size(); i++)
		{
			GroupInfo group = groups.get(i);
			List<ProductInfo> childs = children.get(group.getId());
			for (int j = 0; j < childs.size(); j++)
			{	
				ProductInfo product = childs.get(j);
				if (product.isChoosed())
				{
					totalPrice += product.getPrice() * product.getCount();
					GoodsNum=GoodsNum+product.getCount();
				}
			}
		}
		String price=changeDoubleToString(totalPrice);
		tv_total_price.setText("￥" + Utils.parseTwoNumber(price));
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(groupsNewOrder!=null)
			groupsNewOrder.clear();
		if(childrenNewOrder!=null)
			childrenNewOrder.clear();
		if(children!=null)
			children.clear();
		if(groups!=null)
			groups.clear();
	}

}

