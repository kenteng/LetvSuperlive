package com.lesports.stadium.adapter;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.AllOrderActivity;
import com.lesports.stadium.activity.CrowdPayActivty;
import com.lesports.stadium.activity.OrderActivity;
import com.lesports.stadium.activity.SeeLogiestActivity;
import com.lesports.stadium.activity.TuiKuanActivity;
import com.lesports.stadium.activity.WaitPayActivity;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.CrowdDetailBean;
import com.lesports.stadium.bean.GroupInfo;
import com.lesports.stadium.bean.OrderListBean;
import com.lesports.stadium.bean.OrderListBeanCar;
import com.lesports.stadium.bean.OrderListBeanGoodsBean;
import com.lesports.stadium.bean.OrderListBeanTices;
import com.lesports.stadium.bean.OrderListBeanZhongchouBean;
import com.lesports.stadium.bean.ProductInfo;
import com.lesports.stadium.bean.ReportBackChildBean;
import com.lesports.stadium.bean.ReportBackGroupBean;
import com.lesports.stadium.bean.RoundGoodsBean;
import com.lesports.stadium.bean.TicesDetailBean;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.DensityUtil;
import com.lesports.stadium.utils.Utils;
import com.lidroid.xutils.db.sqlite.CursorUtils.FindCacheSequence;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * ***************************************************************
 * 
 * @Desc : 订单数据的适配器
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * @Author : liuwc
 * @data:
 * @Version : v1.0
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class OrderExpandableListViewAdapter extends BaseExpandableListAdapter {
	private Context context;
	private CheckInterface checkInterface;
	private ModifyCountInterface modifyCountInterface;
	/**
	 * // 组元素数据列表
	 */
	private List<GroupInfo> groups = new ArrayList<GroupInfo>();
	/**
	 * // 子元素数据列表
	 */
	private Map<String, List<ProductInfo>> children = new HashMap<String, List<ProductInfo>>();
	private List<OrderListBean> mList;

	/**
	 * 构造函数
	 * 
	 * @param groups
	 *            组元素列表
	 * @param children
	 *            子元素列表
	 * @param context
	 */
	public OrderExpandableListViewAdapter(List<OrderListBean> mList,
			Context context) {
		super();
		this.context = context;
		this.mList = mList;
	}

	public void setList(List<OrderListBean> list) {
		this.mList = null;
		this.mList = list;
	}

	public void setCheckInterface(CheckInterface checkInterface) {
		this.checkInterface = checkInterface;
	}

	public void setModifyCountInterface(
			ModifyCountInterface modifyCountInterface) {
		this.modifyCountInterface = modifyCountInterface;
	}

	@Override
	public int getGroupCount() {
		return mList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		int count = 0;
		if(mList!=null&&mList.size()!=0){
			String groupStringTypes = mList.get(groupPosition).getOrdersType();
			if (groupStringTypes != null && !TextUtils.isEmpty(groupStringTypes)) {
				if (groupStringTypes.equals("0")) {
					// 说明现在所属的集合是商品的
					List<OrderListBeanGoodsBean> list=mList.get(groupPosition).getList();
					if(list!=null&&list.size()!=0){
						count =list.size();
					}
				} else if (groupStringTypes.equals("1")) {
					// 说明现在所属商品是餐饮的
					List<OrderListBeanGoodsBean> list=mList.get(groupPosition).getList();
					if(list!=null&&list.size()!=0){
						count =list.size();
					}
				} else if (groupStringTypes.equals("3")) {
					// 说明是众筹的
					List<OrderListBeanZhongchouBean> list=mList.get(groupPosition).getList_zhong();
					if(list!=null&&list.size()!=0){
						count = list.size();
					}
				}else if(groupStringTypes.equals("2")){
					//说明是用车的
					List<OrderListBeanCar> list=mList.get(groupPosition).getList_car();
					if(list!=null&&list.size()!=0){
						count = list.size();
					}
				}else if(groupStringTypes.equals("4")){
					//说明是购票的订单
					List<OrderListBeanTices> list=mList.get(groupPosition).getListtices();
					if(list!=null&&list.size()!=0){
						count = list.size();
					}
				}
			}
		}else{
			return count;
		}
		
		return count;
	}

	@Override
	public Object getGroup(int groupPosition) {
		if(getChildrenCount(groupPosition)==0){
			return null;
		}else{
			return mList.get(groupPosition);
		}
	
	}
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		String groupStringTypes = mList.get(groupPosition).getOrdersType();
		if (groupStringTypes != null && !TextUtils.isEmpty(groupStringTypes)) {
			if (groupStringTypes.equals("0")) {
				// 说明现在所属的集合是商品的
				return mList.get(groupPosition).getList().get(childPosition);
			} else if (groupStringTypes.equals("1")) {
				// 说明现在所属商品是餐饮的
				return mList.get(groupPosition).getList().get(childPosition);
			} else if (groupStringTypes.equals("3")) {
				// 说明是众筹的
				return mList.get(groupPosition).getList_zhong()
						.get(childPosition);
			}else if(groupStringTypes.equals("2")){
				//用车数据
				return mList.get(groupPosition).getList_car()
						.get(childPosition);
			}else if(groupStringTypes.equals("4")){
				//众筹数据
				return mList.get(groupPosition).getListtices().get(childPosition);
			}
		}
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupHolder gholder;
			gholder = new GroupHolder();
			convertView = View
					.inflate(context, R.layout.item_order_group, null);
			gholder.mName = (TextView) convertView
					.findViewById(R.id.order_item_group_name);
		final OrderListBean Ordergroup = (OrderListBean) getGroup(groupPosition);
		if (Ordergroup != null) {
			if(Ordergroup.getOrdersType().equals("0")){
				gholder.mName.setText(Ordergroup.getCompanyname());
			}else if(Ordergroup.getOrdersType().equals("1")){
				gholder.mName.setText(Ordergroup.getCompanyname());
			}else if(Ordergroup.getOrdersType().equals("2")){
				gholder.mName.setText("易到用车");
			}else if(Ordergroup.getOrdersType().equals("3")){
				if(getChildrenCount(groupPosition)==0){
					
				}else{
					String name=useChangeName(mList.get(groupPosition).getList_zhong().get(0).getCrowdfundName());
					gholder.mName.setText(name);
				}
			}else if(Ordergroup.getOrdersType().equals("4")){
				if(getChildrenCount(groupPosition)==0){
					
				}else{
					TicesDetailBean bean=GetDataFromDetail(Ordergroup.getListtices().get(0).getPinfo());
					gholder.mName.setText(bean.getProductName());
				}
			}
		}
		return convertView;
	}
	private String useChangeName(String crowdfundName) {
		String newString="";
		if(!TextUtils.isEmpty(crowdfundName)){
			newString=crowdfundName.replace("_","");
			newString="众筹—"+newString;
		}
		return newString;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		convertView=null;
		String orderDataType = mList.get(groupPosition).getOrdersType();
		if (orderDataType != null && !TextUtils.isEmpty(orderDataType)) {
			if (orderDataType.equals("1")) {
				// 说明是商品，所以这里需要加载商品的布局
				final ChildHolder cholder;
				cholder = new ChildHolder();
				convertView = View.inflate(context,
						R.layout.item_order_child_shangpin, null);
				cholder.mGuigename=(TextView) convertView.findViewById(R.id.order_child_goods_guige);
				cholder.mCheckWuliu = (TextView) convertView
						.findViewById(R.id.order_goods_chakanwuliu);
				cholder.mTuikuan=(TextView) convertView.findViewById(R.id.order_goods_tuikuan);
				cholder.mCount = (TextView) convertView
						.findViewById(R.id.order_goods_shangpinshuliang);
				cholder.mGoodsNum = (TextView) convertView
						.findViewById(R.id.order_child_goods_shuliang);
				cholder.mGoodsStatus = (TextView) convertView
						.findViewById(R.id.order_goods_zhuangtai);
				cholder.mImage = (ImageView) convertView
						.findViewById(R.id.order_child_goods_image);
				cholder.mLayou = (RelativeLayout) convertView
						.findViewById(R.id.order_layout_xuyaoyincang);
				cholder.mPrice = (TextView) convertView
						.findViewById(R.id.order_child_goods_jiage);
				cholder.mTotalPrice = (TextView) convertView
						.findViewById(R.id.order_goods_hejiduoshaoqian);
				cholder.mZuowei = (TextView) convertView
						.findViewById(R.id.order_child_goods_shouhuodizhi);
				cholder.mHejiduoshaoyunfei=(TextView) convertView.findViewById(R.id.order_goods_hejiduoshao);
				final OrderListBeanGoodsBean product = (OrderListBeanGoodsBean) getChild(
						groupPosition, childPosition);
				if (product != null) {
					if (isLastChild) {
						cholder.mLayou.setVisibility(View.VISIBLE);
					} else {
						cholder.mLayou.setVisibility(View.GONE);
					}
					LApplication.loader.DisplayImage(ConstantValue.BASE_IMAGE_URL
							+ product.getSmallImg() + ConstantValue.IMAGE_END, cholder.mImage,
							R.drawable.zhoubianshangpin_zhanwei);
					int numss=useCountGoodsTotalnum(mList.get(groupPosition).getList());
					cholder.mCount.setText("共"+numss+"件商品");
					cholder.mGoodsNum.setText("X"+product.getWareNumber());
					//在这里还需要对该数据做一次判断，也就是订单状态做个判断，不同的状态下，所显示的文字不一样，监听也不同
					TextView tv_status=cholder.mCheckWuliu ;
					cholder.mPrice.setText(mList.get(groupPosition).getList().get(childPosition).getGoodsName());
					if(!TextUtils.isEmpty(mList.get(groupPosition).getList().get(childPosition).getPrice())){
						cholder.mZuowei.setText("¥"+Utils.parseTwoNumber(mList.get(groupPosition).getList().get(childPosition).getPrice()));
					}else{
						cholder.mZuowei.setText("¥"+0.00);
					}
					useWanHanderGuide(cholder.mGuigename,mList.get(groupPosition).getList().get(childPosition).getPecificationName());
					//调用方法，计算商品总价格
					double price = useWayCountGoodsPrice(mList.get(groupPosition).getList());
					double feight=Double.parseDouble(mList.get(groupPosition).getFreight());
					double totalprice=price+feight;
					String priveee=changeDoubleToString(totalprice);
					cholder.mTotalPrice.setText("¥"+(Utils.parseTwoNumber(priveee)));
					cholder.mHejiduoshaoyunfei.setText("(含运费￥:"+Utils.parseTwoNumber(feight+"")+")");
					int status=Integer.parseInt(mList.get(groupPosition).getStatus());
					String courier=mList.get(groupPosition).getCourier();
					OrdersType(courier,groupPosition,cholder.mGoodsStatus,cholder.mTuikuan,status,tv_status,1,mList.get(groupPosition));
				}
				return convertView;
			} else if (orderDataType.equals("0")) {
				// 说明是餐饮，所以这里加载餐饮的布局
				final ChildHolder cholder;
				
				cholder = new ChildHolder();
				convertView = View.inflate(context,
						R.layout.item_order_child_shangpin, null);
				cholder.mGuigename=(TextView) convertView.findViewById(R.id.order_child_goods_guige);
				cholder.mCheckWuliu = (TextView) convertView
						.findViewById(R.id.order_goods_chakanwuliu);
				cholder.mCount = (TextView) convertView
						.findViewById(R.id.order_goods_shangpinshuliang);
				cholder.mTuikuan=(TextView) convertView.findViewById(R.id.order_goods_tuikuan);
				cholder.mGoodsNum = (TextView) convertView
						.findViewById(R.id.order_child_goods_shuliang);
				cholder.mGoodsStatus = (TextView) convertView
						.findViewById(R.id.order_goods_zhuangtai);
				cholder.mImage = (ImageView) convertView
						.findViewById(R.id.order_child_goods_image);
				cholder.mLayou = (RelativeLayout) convertView
						.findViewById(R.id.order_layout_xuyaoyincang);
				cholder.mPrice = (TextView) convertView
						.findViewById(R.id.order_child_goods_jiage);
				cholder.mTotalPrice = (TextView) convertView
						.findViewById(R.id.order_goods_hejiduoshaoqian);
				cholder.mZuowei = (TextView) convertView
						.findViewById(R.id.order_child_goods_shouhuodizhi);
				cholder.mHejiduoshaoyunfei=(TextView) convertView.findViewById(R.id.order_goods_hejiduoshao);
				final OrderListBeanGoodsBean product = (OrderListBeanGoodsBean) getChild(
						groupPosition, childPosition);

				if (product != null) {
					if (isLastChild) {
						cholder.mLayou.setVisibility(View.VISIBLE);
					} else {
						cholder.mLayou.setVisibility(View.GONE);
					}
					LApplication.loader.DisplayImage(
							ConstantValue.BASE_IMAGE_URL + product.getSmallImg() + ConstantValue.IMAGE_END,
							cholder.mImage, R.drawable.zhoubianshangpin_zhanwei);
					int numss=useCountGoodsTotalnum(mList.get(groupPosition).getList());
					cholder.mCount.setText("共"+numss+"件商品");
					cholder.mGoodsNum.setText("X"+product.getWareNumber());
				}
				//在这里还需要对该数据做一次判断，也就是订单状态做个判断，不同的状态下，所显示的文字不一样，监听也不同
				TextView tv_status=cholder.mCheckWuliu ;
				cholder.mPrice.setText(mList.get(groupPosition).getList().get(childPosition).getGoodsName());
				if(!TextUtils.isEmpty(mList.get(groupPosition).getList().get(childPosition).getPrice())){
					cholder.mZuowei.setText("¥"+Utils.parseTwoNumber(mList.get(groupPosition).getList().get(childPosition).getPrice()));
				}else{
					cholder.mZuowei.setText("¥"+0.00);
				}
				//调用方法，计算商品总价格
				double price = useWayCountGoodsPrice(mList.get(groupPosition).getList());
				double feight=Double.parseDouble(mList.get(groupPosition).getFreight());
				double totalprice=price+feight;
				String priveee=changeDoubleToString(totalprice);
				cholder.mTotalPrice.setText("¥"+Utils.parseTwoNumber(priveee));
				cholder.mHejiduoshaoyunfei.setText("(含运费￥:"+Utils.parseTwoNumber(feight+"")+")");
				int status=Integer.parseInt(mList.get(groupPosition).getStatus());
				String courier=mList.get(groupPosition).getCourier();
				OrdersType(courier,groupPosition,cholder.mGoodsStatus,cholder.mTuikuan,status,tv_status,0,mList.get(groupPosition));
				return convertView;
			} else if (orderDataType.equals("3")) {
				// 说明是众筹
				final ChildHolder cholder;
				cholder = new ChildHolder();
				convertView = View.inflate(context,
						R.layout.item_order_child_zhongchou, null);
				cholder.mCheckWuliu = (TextView) convertView
						.findViewById(R.id.s_order_zhongchou_chakanwuliu);
				cholder.mCount = (TextView) convertView
						.findViewById(R.id.s_order_zhongchou_shangpinshuliang);
				cholder.mGoodsStatus = (TextView) convertView
						.findViewById(R.id.s_order_zhongchou_zhuangtai);
				cholder.mImage = (ImageView) convertView
						.findViewById(R.id.s_order_child_zhongchou_image);
				cholder.mLayou = (RelativeLayout) convertView
						.findViewById(R.id.s_order_layout_zhongchou_xuyaoyincang);
				cholder.mPrice = (TextView) convertView
						.findViewById(R.id.s_order_child_zhongchou_jiage);
				cholder.mTotalPrice = (TextView) convertView
						.findViewById(R.id.s_order_zhongchou_hejiduoshaoqian);
				cholder.mZuowei = (TextView) convertView
						.findViewById(R.id.s_order_child_zhongchou_shouhuodizhi);
				cholder.mTuikuan=(TextView) convertView.findViewById(R.id.s_order_zhongchou_hejiduoshao);
				final OrderListBeanZhongchouBean product = (OrderListBeanZhongchouBean) getChild(
						groupPosition, childPosition);
				if (product != null) {
					if (isLastChild) {
						cholder.mLayou.setVisibility(View.VISIBLE);
					} else {
						cholder.mLayou.setVisibility(View.GONE);
					}
					cholder.mCount.setText("众筹");
					LApplication.loader.DisplayImage(ConstantValue.BASE_IMAGE_URL
							+ product.getPropagatePicture() + ConstantValue.IMAGE_END
							, cholder.mImage,
							R.drawable.zhoubianshangpin_zhanwei);
					//在这里还需要对该数据做一次判断，也就是订单状态做个判断，不同的状态下，所显示的文字不一样，监听也不同
					TextView tv_status=cholder.mCheckWuliu ;
					cholder.mPrice.setText("支持¥"+mList.get(groupPosition).getList_zhong().get(childPosition).getReturnPrice());
					cholder.mZuowei.setText(mList.get(groupPosition).getList_zhong().get(childPosition).getReturnContent());
					cholder.mCount.setText("共1件商品");
					cholder.mTuikuan.setText("(含运费：¥"+mList.get(groupPosition).getFreight()+")");
					if(!TextUtils.isEmpty(mList.get(groupPosition).getList_zhong().get(childPosition).getReturnPrice())){
						double price=Double.parseDouble(mList.get(groupPosition).getList_zhong().get(childPosition).getReturnPrice());
					}
					double price=Double.parseDouble(mList.get(groupPosition).getOrderAmount());
					double yunfei=Double.parseDouble(mList.get(groupPosition).getFreight());
					cholder.mTotalPrice.setText("¥"+price);
					int status=Integer.parseInt(mList.get(groupPosition).getStatus());
					String courier=mList.get(groupPosition).getCourier();
					OrdersType(courier,groupPosition,cholder.mGoodsStatus,cholder.mTuikuan,status,tv_status,4,mList.get(groupPosition));
				}
				return convertView;
			}else if(orderDataType.equals("2")){
				//说明要加载用车的布局
				final ChildHolder cholder;
				cholder = new ChildHolder();
				convertView = View.inflate(context,
						R.layout.item_order_child_car, null);
				cholder.mCheckWuliu = (TextView) convertView
						.findViewById(R.id.order_car_chakanwuliu);
				cholder.mGoodsStatus = (TextView) convertView
						.findViewById(R.id.order_car_shangpinshuliang);
				cholder.mImage = (ImageView) convertView
						.findViewById(R.id.order_child_car_image);
				cholder.mLayou = (RelativeLayout) convertView
						.findViewById(R.id.order_layout_car_xuyaoyincang);
				cholder.mPrice = (TextView) convertView
						.findViewById(R.id.order_car_hejiduoshaoqian);
				cholder.mTotalPrice = (TextView) convertView
						.findViewById(R.id.order_child_car_luxian);
				cholder.mZuowei = (TextView) convertView
						.findViewById(R.id.order_child_car_shouhuodizhi);
				final OrderListBeanCar product = (OrderListBeanCar) getChild(
						groupPosition, childPosition);
				if (product != null) {
					if (isLastChild) {
						cholder.mLayou.setVisibility(View.VISIBLE);
					} else {
						cholder.mLayou.setVisibility(View.GONE);
					}
					LApplication.loader.DisplayImage(ConstantValue.BASE_IMAGE_URL
							+ product.getCar_car_Brand() + ConstantValue.IMAGE_END//这里需要一张图片
							, cholder.mImage,
							R.drawable.yidaoyongche_zhanwei);
					//在这里还需要对该数据做一次判断，也就是订单状态做个判断不同的状态下，所显示的文字不一样，监听也不同
					String startaddress=mList.get(groupPosition).getList_car().get(childPosition).getCar_start_Address();
					String endadress=mList.get(groupPosition).getList_car().get(childPosition).getCar_end_Address();
					String position=startaddress+"-"+endadress;
					cholder.mTotalPrice.setText(position);
					String time=getChangeToTime(mList.get(groupPosition).getList_car().get(childPosition).getCar_create_Time());
					cholder.mZuowei.setText(time);
					String sprice = mList.get(groupPosition).getList_car().get(childPosition).getCar_price();
					if(TextUtils.isEmpty(sprice)){
						cholder.mLayou.setVisibility(View.GONE);
					}else{
						double price=Double.parseDouble(sprice);
						cholder.mPrice.setText(price+"");
						cholder.mLayou.setVisibility(View.VISIBLE);
					}
					TextView tv_status=cholder.mCheckWuliu;
					int status=Integer.parseInt(mList.get(groupPosition).getStatus());
					String courier=mList.get(groupPosition).getCourier();
					OrdersType(courier,groupPosition,cholder.mGoodsStatus,cholder.mZuowei,status,tv_status,2,mList.get(groupPosition));
				}
				return convertView;
			}else if(orderDataType.equals("4")){
				final ChildHolder cholder;
				cholder = new ChildHolder();
				convertView = View.inflate(context,
						R.layout.item_order_child_goupiao, null);
				cholder.mCheckWuliu = (TextView) convertView
						.findViewById(R.id.order_goupiao_chakanwuliu);
				cholder.mGoodsStatus = (TextView) convertView
						.findViewById(R.id.order_goupiao_shangpinshuliang);
				cholder.mImage = (ImageView) convertView
						.findViewById(R.id.order_child_goupiao_image);
				cholder.mLayou = (RelativeLayout) convertView
						.findViewById(R.id.goupiao_order_layout_xuyaoyincang);
				cholder.mPrice = (TextView) convertView
						.findViewById(R.id.order_child_goupiao_jiage);
				cholder.mTotalPrice = (TextView) convertView
						.findViewById(R.id.order_goupiao_hejiduoshaoqian);
				cholder.mZuowei = (TextView) convertView
						.findViewById(R.id.goupiao_order_child_goods_shouhuodizhi);
				cholder.mGoodsNum=(TextView) convertView.findViewById(R.id.order_goupiao_chakanwuliu_zhuangtai);
				cholder.mHejiduoshaoyunfei=(TextView) convertView.findViewById(R.id.order_goupiao_hejiduoshao);
				cholder.mCount=(TextView) convertView.findViewById(R.id.order_child_goupiao_shuliang);
				final OrderListBeanTices product = (OrderListBeanTices) getChild(
						groupPosition, childPosition);
				if(product!=null){
					if (isLastChild) {
						//传入集合计算总价
						double  totalprice=CountTotalPrice(mList.get(groupPosition).getListtices());
						cholder.mLayou.setVisibility(View.VISIBLE);
						cholder.mTotalPrice.setText("¥"+mList.get(groupPosition).getAmount());
						double feight=Double.parseDouble(mList.get(groupPosition).getFreight());
						cholder.mHejiduoshaoyunfei.setText("(含运费￥"+feight+"元)");
						cholder.mGoodsStatus.setText("共"+mList.get(groupPosition).getListtices().get(childPosition).getWareNumber()+"件商品");
						cholder.mCount.setText("X"+mList.get(groupPosition).getListtices().get(childPosition).getWareNumber());
						
					} else {
						cholder.mLayou.setVisibility(View.GONE);
					}
				}
				TicesDetailBean bean=GetDataFromDetail(product.getPinfo());
				LApplication.loader.DisplayImage(
						 bean.getPicture()//这里需要一张图片
						, cholder.mImage,
						R.drawable.zhoubianshangpin_zhanwei);
				//调用方法，转换实体对象
				float price=0;
				if(!TextUtils.isEmpty(product.getPrice())){
					price=Float.parseFloat(product.getPrice());
				}else{
					price=0;
				}
//				cholder.mPrice.setText("¥:"+price+" ("+bean.getPriceTag()+")");
				cholder.mPrice.setText("¥:"+bean.getPriceTag());
				cholder.mZuowei.setText(bean.getSeatNumber());
				int status=Integer.parseInt(mList.get(groupPosition).getStatus());
				OrdersType("1",groupPosition,cholder.mCheckWuliu,cholder.mGoodsNum,status,cholder.mPrice, 3, mList.get(groupPosition));
				return convertView;	
				
			}
		}
		return null;
	}
	/**
	 * 处理规格数据
	 * @param mGuigename
	 * @param pecificationName
	 */
	private void useWanHanderGuide(TextView mGuigename, String pecificationName) {
		// TODO Auto-generated method stub
		if(!TextUtils.isEmpty(pecificationName)){
			String[] str=pecificationName.split(",");
			String color=str[0];
			String chicun=str[1];
			mGuigename.setText("颜色："+color+"  尺寸："+chicun);
		}else{
			mGuigename.setText("颜色：默认  尺寸：默认");
		}
	}

	/**
	 * 根据传入集合，计算商品总数
	 * @param list
	 * @return
	 */
	private int useCountGoodsTotalnum(List<OrderListBeanGoodsBean> list) {
		int nums=0;
		if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
				if(!TextUtils.isEmpty(list.get(i).getWareNumber())){
					int num=Integer.parseInt(list.get(i).getWareNumber());
					nums=nums+num;
				}
			}
		}else{
			return 0;
		}
		return nums;
	}

	/**
	 * 计算购票订单的总价
	 * @param listtices
	 * @return
	 */
	private double CountTotalPrice(List<OrderListBeanTices> listtices) {
		double totalprice=0;
		if(listtices!=null&&listtices.size()!=0){
			for(int i=0;i<listtices.size();i++){
				double price=Double.parseDouble(listtices.get(i).getPrice());
				totalprice=totalprice+price;
			}
		}
		return totalprice;
	}

	/**
	 * 获取详细数据对象
	 * @param pinfo
	 * @return
	 */
	private TicesDetailBean GetDataFromDetail(String pinfo) {
		TicesDetailBean bean=new TicesDetailBean();
		if(!TextUtils.isEmpty(pinfo)){
			try {
				JSONObject obj=new JSONObject(pinfo);
				if(obj.has("productName")){
					bean.setPicture(obj.getString("productName"));
				}
				if(obj.has("priceTag")){
					bean.setPriceTag(obj.getString("priceTag"));
				}
				if(obj.has("productName")){
					bean.setProductName(obj.getString("productName"));
				}
				if(obj.has("seatNumber")){
					bean.setSeatNumber(obj.getString("seatNumber"));
				}
				if(obj.has("picture")){
					bean.setPicture(obj.getString("picture"));
				}
				
			return bean;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * 将一个传入的字符串毫秒值转换成一个字符串时间值
	 * @param str
	 * @return
	 */
	public String getChangeToTime(String str) {
		String strs="";
		if(!TextUtils.isEmpty(str)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  HH:mm",
					Locale.getDefault());
			long time = Long.parseLong(str);
			Date date = new Date();
			date.setTime(time);
			String timestring = sdf.format(date);
			return timestring;
		}else{
			
		}
		return strs;
		

	}
	/**
	 * 根据集合来计算商品总价
	 * @param list
	 */
	private double useWayCountGoodsPrice(List<OrderListBeanGoodsBean> list) {
		double price=0;
		double prices=0;
		if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
				if(!TextUtils.isEmpty(list.get(i).getPrice())){
					prices=Double.parseDouble(list.get(i).getPrice());
					if(!TextUtils.isEmpty(list.get(i).getWareNumber())){
						double num=Double.parseDouble(list.get(i).getWareNumber());
						price=price+(prices*num);
					}
					
				}else{
					price=price+0;
				}
			}
		}else{
			return price;
		}
		return price;
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
	 * 根据所传参数和控件，来加载不同的监听
	 * @param courier 
	 * @param groupPosition 
	 * @param mTuikuan 
	 * @param mTuikuan2 
	 * @param status
	 * @param tv_status
	 * @param i
	 * @param orderListBean 
	 */
	private void OrdersType(String courier, int groupPosition, TextView mTuikuan, TextView mTuikuan2, int status, TextView tv_status, int i, final OrderListBean orderListBean) {
		switch (i) {
		case 1:
			//说明是商品
			switch (status) {
			case 0:
				//提交订单（待付款）
				tv_status.setText("去支付");
				mTuikuan.setText("待支付");
				tv_status.setBackground(context.getResources().getDrawable(R.drawable.paymoney_style));
				tv_status.setTextColor(Color.WHITE);
				tv_status.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
//						//首先明白，这里支付的是商品数据，所以直接走商品支付订单确认界面
						//直接走支付界面，不走订单确认界面
						AllOrderActivity.instance.makeOrderData("shangpin", orderListBean);
					}
				});
				
				break;
			case 1:
				if(courier.equals("0")){
					//待发货（已付款或到付）
					mTuikuan.setText("未取货");
					tv_status.setVisibility(View.GONE);
				}else if(courier.equals("1")){
					//待发货（已付款或到付）
					mTuikuan.setText("待发货");
					tv_status.setText("提醒发货");
					tv_status.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
						AllOrderActivity.instance.TixingStoreSendGoods(orderListBean.getId());	
						}
					});
				}
				break;
			case 2:
				tv_status.setVisibility(View.GONE);
				//等待收货（已发货）
				tv_status.setText("查看物流");
				mTuikuan.setText("待收货");
				tv_status.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//需要带着订单数据跳转到查看物流界面
						Intent intens=new Intent(context, SeeLogiestActivity.class);
						intens.putExtra("bean",(Serializable) orderListBean);
						context.startActivity(intens);
					}
				});
				break;
			case 3:
				tv_status.setVisibility(View.GONE);
				mTuikuan.setText("已收货");
				break;
			case 4:
				tv_status.setVisibility(View.GONE);
				mTuikuan.setText("已完成");
				break;
			default:
				break;
			}
			break;
		case 0:
			//说明是餐饮
			switch (status) {
			case 0:
				//提交订单（待付款）
				tv_status.setText("去支付");
				mTuikuan.setText("待付款");
				tv_status.setBackground(context.getResources().getDrawable(R.drawable.paymoney_style));
				tv_status.setTextColor(Color.WHITE);
				tv_status.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						AllOrderActivity.instance.makeOrderData("canyin", orderListBean);
					}
				});
				break;
			case 1:
				if(courier.equals("0")){
					//待发货（已付款或到付）
					mTuikuan2.setVisibility(View.INVISIBLE);
					LayoutParams params = (LayoutParams) mTuikuan.getLayoutParams();
					params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					mTuikuan.setLayoutParams(params);
				}else if(courier.equals("1")){
					//待发货（已付款或到付）
					LayoutParams params = (LayoutParams) mTuikuan.getLayoutParams();
					params.addRule(RelativeLayout.ALIGN_LEFT,mTuikuan2.getId());
					mTuikuan.setLayoutParams(params);
					mTuikuan2.setVisibility(View.VISIBLE);
					mTuikuan2.setText("提醒发货");
					mTuikuan2.setOnClickListener(new OnClickListener() {
						//退款按钮被点击的时候
						@Override
						public void onClick(View v) {
							AllOrderActivity.instance.TixingStoreSendGoods(orderListBean.getId());	
						}
					});
				}
				tv_status.setText("退款");
				tv_status.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent=new Intent(context, TuiKuanActivity.class);
						intent.putExtra("order", orderListBean);
						context.startActivity(intent);
					}
				});
				if(courier.equals("0")){
					mTuikuan.setText("未取货");
				}else{
					mTuikuan.setText("待发货");
				}
				
				
				break;
			case 2:
				//等待收货（已发货）
				tv_status.setText("查看物流 ");
				mTuikuan.setText("待收货");
				tv_status.setVisibility(View.GONE);
				tv_status.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//需要带着订单数据跳转到查看物流界面
						Intent intens=new Intent(context, SeeLogiestActivity.class);
						intens.putExtra("bean",(Serializable) orderListBean);
						context.startActivity(intens);
					}
				});
				break;
			case 4:
				tv_status.setVisibility(View.GONE);
				mTuikuan.setText("已完成");
				break;
			case 7:
				tv_status.setVisibility(View.GONE);
				mTuikuan.setText("退款完成");
				break;
			case 8:
				//退款申请
				mTuikuan.setText("退款中");
				tv_status.setVisibility(View.GONE);
				break;
			case 9:
				mTuikuan.setText("退款审核中");
				tv_status.setVisibility(View.GONE);
				break;
			case 10:
				mTuikuan.setText("已退货，退款中");
				tv_status.setVisibility(View.GONE);
				break;
			case 11:
				mTuikuan.setText("已退款");
				tv_status.setVisibility(View.GONE);
				break;
			default:
				break;
			}
			break;
		case 3:
			//说明是门票
			switch (status) {
			case 0:
				mTuikuan2.setText("待支付");
				//提交订单（待付款）
				mTuikuan.setText("去支付");
				mTuikuan.setTextColor(Color.WHITE);
				mTuikuan.setBackground(context.getResources().getDrawable(R.drawable.paymoney_style));
				mTuikuan.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						AllOrderActivity.instance.makeOrderData("mengpiao", orderListBean);
					}
				});
				break;
			case 1:
				mTuikuan2.setText("待发货");
				mTuikuan.setText("提醒发货");
				mTuikuan.setVisibility(View.GONE);
				mTuikuan.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
					AllOrderActivity.instance.TixingStoreSendGoods(orderListBean.getId());	
					}
				});
				break;
			case 2:
				//等待收货（已发货）
				mTuikuan2.setText("已发货");
				mTuikuan.setVisibility(View.GONE);
				break;
			default:
				break;
			}
			break;
		case 2:
			//说明是用车
			switch (status) {
			case 21:
				//提交订单（待付款）
				tv_status.setText("去支付");
				tv_status.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						AllOrderActivity.instance.makeOrderData("yongche", orderListBean);
					}
				});
				mTuikuan.setText("待付款");
				break;
			case 22:
				//已预订（已付款或到付）
				tv_status.setVisibility(View.GONE);
				mTuikuan.setText("已预订");
				break;
			case 23:
				//等待收货（已发货）
				tv_status.setVisibility(View.GONE);
				mTuikuan.setText("已完成");
				break;
			default:
				break;
			}
			break;
		case 4:
			//说明是众筹
			switch (status) {
			case 0:
				//提交订单（待付款）
				tv_status.setVisibility(View.VISIBLE);
				tv_status.setText("去支付");
				mTuikuan.setText("待付款");
				tv_status.setBackground(context.getResources().getDrawable(R.drawable.paymoney_style));
				tv_status.setTextColor(Color.WHITE);
				tv_status.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Log.i("lwc","众筹支付");
						AllOrderActivity.instance.makeOrderData("zhongchou", orderListBean);
					}
				});
				//这里只是为了掩饰，所以进行了换位
				
				break;
			case 1:
				//待发货（已付款或到付）
				tv_status.setVisibility(View.GONE);
				tv_status.setText("提醒发货");
				mTuikuan.setText("待发货");
				tv_status.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
					AllOrderActivity.instance.TixingStoreSendGoods(orderListBean.getId());	
					}
				});
				
				break;
			case 2:
				//等待收货（已发货）
				tv_status.setText("查看物流");
				tv_status.setVisibility(View.GONE);
				mTuikuan.setText("待收货");
				tv_status.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//需要带着订单数据跳转到查看物流界面
						Log.i("lwc","众筹查看物流");
						Intent intens=new Intent(context, SeeLogiestActivity.class);
						intens.putExtra("bean",(Serializable) orderListBean);
						context.startActivity(intens);
					}
				});
				break;
			default:
				break;
			}
			break;

		default:
			break;
		}
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
	 * 在当前子集合中判断，该商品是否已经存在在子的集合当中
	 * @param bean
	 */
	private boolean checkChildIsHava(OrderListBeanGoodsBean bean) {
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

	/**
	 * 在当前组集合中判断，该组id是否已经存在
	 * @param orderListBeanGoodsBean
	 */
	private boolean checkGroupIsHaveTheBean(OrderListBeanGoodsBean orderListBeanGoodsBean) {
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

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	/**
	 * 组元素绑定器
	 * 
	 * 
	 */
	private class GroupHolder {
		TextView mName;
	}

	/**
	 * 子元素绑定器
	 * 
	 * 
	 */
	private class ChildHolder {
		TextView mPrice;
		ImageView mImage;
		TextView mZuowei;
		TextView mCount;
		RelativeLayout mLayou;
		TextView mGoodsNum;
		TextView mGoodsStatus;
		TextView mTotalPrice;
		TextView mCheckWuliu;
		TextView mTuikuan;
		TextView mHejiduoshaoyunfei;
		TextView mGuigename;
	}

	/**
	 * 复选框接口
	 * 
	 * 
	 */
	public interface CheckInterface {
		/**
		 * 组选框状态改变触发的事件
		 * 
		 * @param groupPosition
		 *            组元素位置
		 * @param isChecked
		 *            组元素选中与否
		 */
		public void checkGroup(int groupPosition, boolean isChecked);

		/**
		 * 子选框状态改变时触发的事件
		 * 
		 * @param groupPosition
		 *            组元素位置
		 * @param childPosition
		 *            子元素位置
		 * @param isChecked
		 *            子元素选中与否
		 */
		public void checkChild(int groupPosition, int childPosition,
				boolean isChecked);
	}

	/**
	 * 改变数量的接口
	 * 
	 * 
	 */
	public interface ModifyCountInterface {
		/**
		 * 增加操作
		 * 
		 * @param groupPosition
		 *            组元素位置
		 * @param childPosition
		 *            子元素位置
		 * @param showCountView
		 *            用于展示变化后数量的View
		 * @param isChecked
		 *            子元素选中与否
		 */
		public void doIncrease(int groupPosition, int childPosition,
				View showCountView, boolean isChecked);

		/**
		 * 删减操作
		 * 
		 * @param groupPosition
		 *            组元素位置
		 * @param childPosition
		 *            子元素位置
		 * @param showCountView
		 *            用于展示变化后数量的View
		 * @param isChecked
		 *            子元素选中与否
		 */
		public void doDecrease(int groupPosition, int childPosition,
				View showCountView, boolean isChecked);
	}

}
